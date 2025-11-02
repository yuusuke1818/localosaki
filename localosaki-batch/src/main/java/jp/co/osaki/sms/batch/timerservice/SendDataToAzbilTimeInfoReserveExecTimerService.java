package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.SendDataToAzbilTimeInfoDao;
import jp.co.osaki.sms.batch.logic.SendDataToAzbilCommonLogic;
import jp.co.osaki.sms.batch.resultset.AzbilBatchStartupInfoResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * アズビル用サーバー 日報データ送信実行(外部予約起動) TimerServiceクラス
 *
 * @author akr_iwamoto
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SendDataToAzbilTimeInfoReserveExecTimerService extends SmsBatchTimerService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3044059713250315521L;

	/**
     * アズビル用サーバー 日報データ送信実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.
    		SEND_DATA_TO_AZBIL_TIMEINFO_RESERVE_EXEC.getVal();

    /**
     * 日付フォーマットyyyyMMddHHmm
     */
    private static final String DATE_FORMAT_YYYYMMDDHHmm_SLASH = "yyyyMMddHHmm";

    /**
     * DAO
     */
    private SendDataToAzbilTimeInfoDao dao;

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new SendDataToAzbilTimeInfoDao(this.entityManager);
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
        batchLogger.info(this.batchName.concat(" Start"));

        Date svDate = null;
        Calendar svCal = null;

        try {
            // 起動日時を取得
            svDate = (Date) dao.getServerDateTime();
            svCal = Calendar.getInstance();
            svCal.setTime(svDate);

            // アズビルバッチ外部起動設定取得
            List<AzbilBatchStartupInfoResultSet> startupInfoList =  dao.getAzbilBatchStartupInfoList();
            if (startupInfoList != null && !startupInfoList.isEmpty()) {
            	// 順番に1件のみ取り出す
            	AzbilBatchStartupInfoResultSet info = startupInfoList.get(0);
            	String targetDateTime = info.getExeDate();

                // 対象日時算出
                List<String> dateTimeList = getTargetDate(targetDateTime, svDate);
                if (dateTimeList != null) {
                    // アズビル送信処理呼び出し
                    SendDataToAzbilCommonLogic logic = new SendDataToAzbilCommonLogic(entityManager, dao, this.batchName);
                    logic.startLogic(dateTimeList, svDate);
                }

                // アズビルバッチ外部起動設定削除
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }

                if (entityManager.getTransaction().isActive()) {
                	// アズビルバッチ外部起動設定削除
                	dao.removeAzbilBatchStartupInfo(info.getBatchId(), info.getExeDate());

                    if (entityManager.getTransaction().getRollbackOnly()) {
                        entityManager.getTransaction().rollback();
                    } else {
                        entityManager.getTransaction().commit();
                    }
                }
                entityManager.clear();

            } else {
            	// テーブル登録が無い為、処理無し
            }

        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

    /**
     * 対象日時算出
     *
     * @param String targetDateTime
     * @param String nowDate
     * @return 対象日(yyyymmddhhmm)リスト
     */
    private List<String> getTargetDate(String targetDateTime, Date nowDate) {

    	List<String> result = null;

    	// バリデート
    	if (targetDateTime == null || nowDate == null) {
    		batchLogger.info(this.batchName.concat("parameter is null."));
    		return null;
    	}

    	if (!checkRegDateYmSlash(targetDateTime)) {
    		batchLogger.info(this.batchName.concat("targetDateTime is invalid format."));
    		return null;
    	}

		// 分文字列を切り出し
        String dayWork = getMin(targetDateTime);
            if (Integer.valueOf(dayWork) == 0 || Integer.valueOf(dayWork) == 30) {
            	result = new ArrayList<>();
            	// 日付追加
            	result.add(targetDateTime);

            	String workTargetYMD = getYMD(targetDateTime);
            	String workNowYMD = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMMDD);

            	// 指定日が当日以外かチェック
            	if (Integer.valueOf(workTargetYMD) < Integer.valueOf(workNowYMD)) {
                   	// 指定日が当日以外の場合は、指定日の翌日00:00送信用日時作成
                	Date targetDate = DateUtility.conversionDate(targetDateTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                	targetDate = DateUtility.plusDay(targetDate, 1); // 翌日

                	String workTommorowDate = DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYYMMDD);
                	workTommorowDate = workTommorowDate + "0000";	// 0時0分指定

                	// 日付追加
                	result.add(workTommorowDate);
            	}

            } else {
        		batchLogger.info(this.batchName.concat("targetDateTime minute is not 0 or 30."));
        		return null;

            }

        return result;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 分文字列
     */
    private String getMin(String targetDate) {
        String ret = targetDate.substring(10, 12);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 年月日時文字列
     */
    private String getYMD(String targetDate) {
        String ret = targetDate.substring(0, 8);
        return ret;
    }

    /**
     * 日付書式チェックYYYYMMDDHHmm
     *
     * @param target
     * @return ture:OK, false:エラー
     */
    private static boolean checkRegDateYmSlash(String target) {
        Pattern p = Pattern.compile("\\d{12}");
        Matcher m = p.matcher(target);
        if (!m.matches()) {
            return false;
        }

        Date date = DateUtility.conversionDate(target, DATE_FORMAT_YYYYMMDDHHmm_SLASH);
        if (date == null) {
            return false;
        }
        return true;
    }

}
