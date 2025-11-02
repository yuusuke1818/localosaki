package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.AutoInspExecDao;
import jp.co.osaki.sms.batch.dao.ReservationInspDao;
import jp.co.osaki.sms.batch.logic.InspectionLogic;
import jp.co.osaki.sms.batch.resultset.AutoInspLatestMonthNoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspLoadSurveyResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterResultSet;
import jp.co.osaki.sms.batch.resultset.ReserveInspExecResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 予約検針実行 TimerService
 * @author kobayashi.sho
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class ReservationInspTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = -7158125746904271359L;

    /**
     * 予約検針実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.
            RESERVATION_INSP_EXE.getVal();

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /** 自動検針Dao. */
    private AutoInspExecDao dao;

    /** 予約検針Dao.  */
    private ReservationInspDao riDao;

    /** 検針結果登録Logic. */
    private InspectionLogic logic;

//    /**
//     * コンフィグ設定値取得クラス.
//     */
//    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new AutoInspExecDao(this.entityManager); // 自動検針Dao
        this.riDao = new ReservationInspDao(this.entityManager); // 予約検針Dao
        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(),
                    SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);
    }

    @Override
    @Timeout
    protected void execute() {
        try {
            batchLogger.info(this.batchName.concat(" Start"));

            // サーバ時刻を取得
            serverDateTime = dao.getServerDateTime();

            logic = new InspectionLogic(batchLogger, errorLogger, entityManager, dao, serverDateTime, batchName);

            // --- ▽▽▽ 任意検視の予約検針処理(ここから) ▽▽▽ ---

            // 予約検針日時 生成  書式:yyyyMMddHHmm
            String currentYmdHm = DateUtility.changeDateFormat(new Date(serverDateTime.getTime()), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            // 30分区切りに補正（30分以下は切捨て）
            currentYmdHm = logic.getYMDH(currentYmdHm) + (convInt(logic.getMin(currentYmdHm)) < 30 ? "00" : "30");

            // リトライ猶予時間を考慮した予約検針日時リストを取得
            List<String> targetYmdHmList = createTargetYmdHmList(currentYmdHm);

            // 予約検針実施あり
            for (String targetYmdHm : targetYmdHmList) {
                // 予約検針日時(補正) 生成  書式:yyyyMMddHH
                String adjustDate = logic.getAdjustRetryDate(DateUtility.conversionDate(targetYmdHm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));

                // 予約検針対象メーター一覧を取得  ※検索条件：予約検針日時  ※1日0時の場合は、検針月を前月に変更したものを生成する
                List<ReserveInspExecResultSet> reserveInspList = riDao.getReserveInsp(convTimestamp(DateUtility.conversionDate(targetYmdHm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

                // 予約検針対象メーター一覧 を 装置ID 毎にまとめる
                Map<String, List<ReserveInspExecResultSet>> reserveInspListMap = new HashMap<>(); // Map<装置ID, List<予約検針対象メーター>>
                for (ReserveInspExecResultSet info : reserveInspList) {
                    if (!reserveInspListMap.containsKey(info.getDevId())) {
                        reserveInspListMap.put(info.getDevId(), (new ArrayList<ReserveInspExecResultSet>()));
                    }
                    reserveInspListMap.get(info.getDevId()).add(info);
                }

                // 装置ID 分ループする
                for (Map.Entry<String, List<ReserveInspExecResultSet>> entry : reserveInspListMap.entrySet()) {
                    String devId = entry.getKey(); // 装置ID

                    // 月検針連番を取得
                    AutoInspLatestMonthNoResultSet latestMonth = getLatestMonthNo(devId, adjustDate, targetYmdHm);

                    // 装置ID 毎 の 予約検針対象メーター一覧 分ループする
                    for (ReserveInspExecResultSet info : entry.getValue()) {
                        // メーター情報生成
                        AutoInspMeterResultSet meterInfo = new AutoInspMeterResultSet(devId, info.getMeterMngId(), info.getSrvEnt(), info.getMulti(), info.getMeterId());

                        List<Long> inComplateList = null; // 日報データ異常リスト  null:異常なし

                        // 日報データ から指定した日時のデータ取得 → 予約検針実施日 にセット
                        AutoInspLoadSurveyResultSet dlsResult = riDao.getLoadSurveyForLast(meterInfo.getDevId(), meterInfo.getMeterMngId(), targetYmdHm);
                        String reserveInspYmdHm = null; // 予約検針実施日
                        if (dlsResult != null) {
                            // 該当データあり 且つ 指針値あり (正常)
                            reserveInspYmdHm = dlsResult.getGetDate(); // 予約検針実施日 に 収集日時 をセット
                        } else {
                            // 該当データなし
                            reserveInspYmdHm = targetYmdHm; // 予約検針実施日 に 予約検針日時 セット
                        }
                        if (dlsResult == null || dlsResult.getDmvkwh() == null) {
                            // 該当データなし または 指針値なし (エラーケース) → 日報データが取得できないため、異常データとして処理
                            inComplateList = Arrays.asList(meterInfo.getMeterMngId()); // 日報データ異常リスト生成
                        }

                        // 検針結果登録処理
                        logic.inspMeter(
                                adjustDate,
                                inComplateList,
                                meterInfo,
                                latestMonth,
                                SmsBatchConstants.INSP_KIND.SCHEDULE.getVal(),
                                true, // true:未完了あり(または 予約検針)
                                reserveInspYmdHm, // 予約検針実施日
                                BigDecimal.ONE.equals(info.getChkInt()),
                                DateUtility.conversionDate(currentYmdHm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)
                                );
                    }
                }
            }

            // --- △△△ 任意検視の予約検針処理(ここまで) △△△ ---

            batchLogger.info(this.batchName.concat(" End"));

        } catch (Exception ex) {

            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                entityManager.clear();
            }

        }
    }

    /**
     * リトライ猶予時間を考慮した予約検針日時リストを取得
     *
     * @param String targetYmdHm 予約検針日時
     * @return List<String> 予約検針日時リスト 書式:yyyyMMddHH
     * @throws Exception
     */
    private List<String> createTargetYmdHmList(String targetYmdHm) throws Exception {
        List<String> resultList = new ArrayList<>();

        // String を Date に変換
        Date targetDate = DateUtility.conversionDate(targetYmdHm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        // リトライ猶予時間までの時間を計算
        Date startDate = getSubtractDate(targetYmdHm, SmsBatchConstants.INSPECTION_RETRY_HOUR);

        // 30分単位のリストを作成
        for (Date current = startDate; !current.after(targetDate); current = DateUtility.plusMinute(current, 30)) {
            resultList.add(DateUtility.changeDateFormat(current, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
        }
        return resultList;
    }

    /**
     * リトライ猶予時間を減算した日付生成
     *
     * @param String targetYmdHm  書式:yyyyMMddHHmm
     * @param String waitHour
     * @return Date
     */
    private Date getSubtractDate(String targetYmdHm, int waitHour) {
        Date date = DateUtility.conversionDate(targetYmdHm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

        date = DateUtility.plusHour(date, -waitHour);

        return date;
    }

    /**
     * 最新検針月連番 取得 (予約検針用).
     *
     * @param devId 装置ID
     * @param adjustDate 予約検針日時(補正)(yyyyMMddHH)
     * @param targetYmdHm 予約検針日時
     * @return AutoInspLatestMonthNoResultSet
     */
    public AutoInspLatestMonthNoResultSet getLatestMonthNo(String devId, String adjustDate, String targetYmdHm) {
        Long monthNo = riDao.getInspMonthNo(devId, logic.getYear(adjustDate), logic.getMonth(adjustDate), targetYmdHm);
        return new AutoInspLatestMonthNoResultSet(monthNo, false);
    }

    /**
     * Stringをintに変換
     * @param val String型の数値
     * @return int型の数値
     */
    private int convInt(String val) {
        if (val == null || val.isEmpty() || !StringUtility.isNumeric(val)) {
            return 0;
        }
        return new Integer(val);
    }

    /**
     * Date型をTimestamp型に変換
     * @param date Date型の日時
     * @return Timestamp型の日時
     */
    private Timestamp convTimestamp(Date date) {
        return (date == null ? null : new Timestamp(date.getTime()));
    }

}
