package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jboss.ejb3.annotation.TransactionTimeout;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.MGroupPrice;
import jp.co.osaki.osol.entity.MGroupPricePK;
import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.AutoInspExecDao;
import jp.co.osaki.sms.batch.logic.InspectionLogic;
import jp.co.osaki.sms.batch.resultset.AutoInspExecResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspLatestMonthNoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspPriceCalcInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspTenantInfoResultSet;
import jp.co.osaki.sms.batch.resultset.AutoInspUnitPriceResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * SMS 自動検針 TimerServiceクラス
 *
 * @author tominaga
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class AutoInspExecTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = -7434303353450636144L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.AUTO_INSP_EXEC.getVal();

    /**
     * 自動検針Dao.
     */
    private AutoInspExecDao dao;

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /** 検針結果登録Logic. */
    private InspectionLogic logic;

    /**
     * 自動検針実施クラス.
     */
    class AutoInspExeDate {
        public final Integer AUTOINSPEXEDATETYPE_NONE = 0;
        public final Integer AUTOINSPEXEDATETYPE_EXE = 1;
        public final Integer AUTOINSPEXEDATETYPE_RETRY = 2;
        public final Integer AUTOINSPEXEDATETYPE_EXEANDRETRY = 3;
        public Date exeDate;
        public Integer exeType;

        public AutoInspExeDate() {
        }
    }

    /**
     * メーターグループ情報クラス.
     */
    class MeterGroupInfo {
        public String corpId;
        public Long buildingId;
        public Boolean calcDisEnable;
        public String latestDate;
        public Long totalUse;
        public Long totalPrice;

        public MeterGroupInfo(String corpId, Long buildingId, Boolean calcDisEnable, String latestDate, Long totalUse, Long totalPrice) {
            this.corpId = corpId;
            this.buildingId = buildingId;
            this.calcDisEnable = calcDisEnable;
            this.latestDate = latestDate;
            this.totalUse = totalUse;
            this.totalPrice = totalPrice;
        }

    }

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new AutoInspExecDao(this.entityManager);

        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(), SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);

    }

    /*
     * (非 Javadoc)
     *
     * @see jp.skygroup.enl.webap.base.batch.BaseBatchScheduleTimer#execute()
     */
    @TransactionTimeout(unit = TimeUnit.HOURS, value = 2)
    @Timeout
    @Override
    protected void execute() {

        try {
            // バッチ処理開始ログ出力
            batchLogger.info(this.batchName.concat(" Start"));

            // 処理日時を取得する
            serverDateTime = dao.getServerDateTime();

            logic = new InspectionLogic(batchLogger, errorLogger, entityManager, dao, serverDateTime, batchName);

            // --- ▽▽▽ 自動検針処理(ここから) ▽▽▽ ---

            // 処理起点の日付を日付ベースで保持
			Date currentDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(new Date(serverDateTime.getTime()), DateUtility.DATE_FORMAT_YYYYMMDDHH),
					DateUtility.DATE_FORMAT_YYYYMMDDHH);

            // 自動検針対象リスト取得
            List<AutoInspExecResultSet> autoinsplist = dao.getAutoInsp();
            batchLogger.info(this.batchName.concat(" 自動検針対象リスト数:" + autoinsplist.size()));

            // 検針対象リスト分ループ
            for (AutoInspExecResultSet info : autoinsplist) {

                // 無効インプットがあれば次へ
                if (info.getMonth() == null
                        || info.getMonth().length() == 0
                        || info.getDay() == null
                        || info.getDay().length() == 0
                        || info.getHour() == null) {
                    // batchLogger.info(this.batchName.concat(" 無効インプット:" + info.getDevId()));
                    continue;
                }

                // 月毎の自動検針実施有無で年月日時を生成(編集前)
                List<String> exec_day_list = createPreDayString(
                        info.getMonth(), info.getDay(), info.getHour().toString(),
                        DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDDHH));

                // 対象日時なしの場合は次へ
                if (exec_day_list.size() == 0) {
                    // batchLogger.info(this.batchName.concat(" 対象日なし:" + info.getDevId()));
                    continue;
                }

                // 月毎の自動検針実施有無で年月日時を生成(編集後)
                List<String> exec_day_mod_list = createAftDayString(exec_day_list);

                // 月検針連番を初期化
                AutoInspLatestMonthNoResultSet latestMonth = new AutoInspLatestMonthNoResultSet(0L, false);

                // 現在時刻との比較
                AutoInspExeDate exeDate = checkAutoInspDate(exec_day_mod_list, currentDate);

                // 自動検針実施日(yyyyMMddHHmm)
                String targetYmdHm = DateUtility.changeDateFormat(exeDate.exeDate, DateUtility.DATE_FORMAT_YYYYMMDDHH) + "00";

                // 自動検針実施あり
                if (exeDate.exeType == exeDate.AUTOINSPEXEDATETYPE_EXE || exeDate.exeType == exeDate.AUTOINSPEXEDATETYPE_EXEANDRETRY) {
                    // 自動検針実施日(補正)(yyyyMMddHH)
                    String adjustDate = logic.getAdjustRetryDate(exeDate.exeDate);

                    // 月検針連番を取得
                    latestMonth = logic.getLatestMonthNo(info.getDevId(), SmsBatchConstants.INSP_KIND.AUTO.getVal(), adjustDate, targetYmdHm);

                    // 検針データ未完了件数取得  ※未完了チェックに InspMonthNo を含まれるため、通常は 0 件になる
                    Long countIncomplete = dao.getCountIncomplate(info.getDevId(),
                            null,
                            info.getMeterType(),
                            logic.getYear(adjustDate),
                            logic.getMonth(adjustDate),
                            latestMonth.getInspMonthNo().toString());

                    // 自動検針データ作成開始 ログ出力
                    batchLogger.info(this.batchName.concat(" 自動検針データ作成開始( dev_id = " + info.getDevId()
                            + " meter_Type = " + info.getMeterType()
                            + " target_date = " + targetYmdHm
                            + " month_no = " + latestMonth.getInspMonthNo()
                            + " chk_int = " + info.getChkInt()));

                    // 日報データ異常リスト取得
                    List<Long> inComplateList = Stream.concat(
                            dao.listInCompLoadSurvey1(info.getDevId(), info.getMeterType(), targetYmdHm).stream(),
                            dao.listInCompLoadSurvey2(info.getDevId(), info.getMeterType(), targetYmdHm).stream()).collect(Collectors.toList());

                    boolean inspFlg = false; // 検針結果データ登録有無フラグ 初期化
                    // 装置ID毎の登録メータリスト取得
                    List<AutoInspMeterResultSet> meterList = dao.getAutoInspMeter(info.getDevId(), info.getMeterType());
                    // 登録メータリスト分ループ
                    for (AutoInspMeterResultSet meterInfo : meterList) {
                        // 検針結果登録処理
                        if (logic.inspMeter(
                                adjustDate,
                                inComplateList,
                                meterInfo,
                                latestMonth,
                                SmsBatchConstants.INSP_KIND.AUTO.getVal(),
                                countIncomplete > 0, // true:未完了あり  false:未完了なし(通常ケース)
                                targetYmdHm,
                                BigDecimal.ONE.equals(info.getChkInt()),
                                currentDate)) {
                            inspFlg = true;
                        }
                    }
                    // 検針結果データの登録が1件でもある場合
                    if (inspFlg) {
                        MPauseMailResultSet compResultSet = dao.getCompSendMailFlg(info.getDevId());
                        if(!(compResultSet != null && (compResultSet.getSendFlg() != null && compResultSet.getSendFlg().equals("1")))) {
                         // 自動検針完了メール送信
                            sendMailAutoInspComplate(info.getDevId(), info.getMeterTypeName(), adjustDate, latestMonth.getInspMonthNo().toString());
                        }
                    }
                }

                // 日報データ欠損チェック処理あり
                if (exeDate.exeType == exeDate.AUTOINSPEXEDATETYPE_RETRY || exeDate.exeType == exeDate.AUTOINSPEXEDATETYPE_EXEANDRETRY) {
                    MPauseMailResultSet missingResultSet = dao.getMissingSendMailFlg(info.getDevId());
                    if(!(missingResultSet != null && (missingResultSet.getSendFlg() != null && missingResultSet.getSendFlg().equals("1")))) {
                        // 日報データ欠損のお知らせメールを送信
                        sendMailAutoInspInComplate(info.getDevId(), info.getMeterType(), logic.getYMDH(targetYmdHm), latestMonth.getInspMonthNo().toString());
                       }

                }

            }

            // --- △△△ 自動検針処理(ここまで) △△△ ---

            // グループ毎の料金計算
            calcPrice(currentDate);

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
     * 月毎の自動検針実施有無で年月日時を生成(編集前)
     *
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param nowTime
     * @return List<String>
     */
    private List<String> createPreDayString(String month, String day, String hour, String nowTime) {
        List<String> ret = new ArrayList<>();
        String[] monthArr = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
        int dayWk = Integer.valueOf(day);
        int hourWk = Integer.valueOf(hour);

        for (int i = 0; i < monthArr.length; i++) {
            String monthEna = month.substring(i, i + 1);
            if (monthEna.equals("1")) {
                String execData = logic.getYear(nowTime) +
                        monthArr[i] +
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, dayWk) +
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, hourWk);
                ret.add(execData);
            }
        }
        return ret;
    }

    /**
     * 月毎の自動検針実施有無で年月日時を生成(編集後)
     *
     * @param List<String> preDayString
     * @return List<String>  書式:yyyyMMddHH
     */
    private List<String> createAftDayString(List<String> preDayString) {
        List<String> ret = new ArrayList<>();

        String wk_exec_day = "";
        for (String execDayWk : preDayString) {
            String yearWk = logic.getYear(execDayWk);
            String monthWk = logic.getMonth(execDayWk);
            String dayWk = getDay(execDayWk);
            String hourWk = getHour(execDayWk);

            if (day_exists(yearWk, monthWk, dayWk)) {
                if (dayWk.equals("01") && hourWk.equals("00")) {
                    wk_exec_day = yearWk + monthWk + "0100";
                } else {
                    wk_exec_day = execDayWk;
                }
            } else {
                // 該当年月の月末日を取得
                Date dateWk = DateUtility.conversionDate(yearWk + monthWk + "01", DateUtility.DATE_FORMAT_YYYYMMDD);
                String endDays = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, DateUtility.getNumberOfDays(dateWk));
                // 対象日の修正
                wk_exec_day = yearWk + monthWk + endDays + hourWk;
            }
            ret.add(wk_exec_day);
        }
        return ret;
    }

    /**
     * 現在時刻より自動検針実施設定から実施日、実施タイプを抽出
     *
     * @param List<String> exec_day_mod_list  書式:yyyyMMddHH
     * @param currentDate 現在日時
     * @return 自動検針実施日及び実施タイプ
     */
    private AutoInspExeDate checkAutoInspDate(List<String> exec_day_mod_list, Date currentDate) {
        AutoInspExeDate ret = new AutoInspExeDate();
        ret.exeType = ret.AUTOINSPEXEDATETYPE_NONE;
        ret.exeDate = null;
        // 現在時刻との比較
        for (String execDayModWk : exec_day_mod_list) {
            // 待ち時間を設ける(未使用なので0とする)
            Date execDayModAdd = getAddDate(execDayModWk, 0);
            // リトライ時間を考慮した日時を生成
            Date execRetryDate = getAddDate(execDayModWk, SmsBatchConstants.INSPECTION_RETRY_HOUR);

            ret.exeDate = execDayModAdd;
            if (execDayModAdd.getTime() <= currentDate.getTime() && currentDate.getTime() <= execRetryDate.getTime()) {
                ret.exeType = ret.AUTOINSPEXEDATETYPE_EXE;
            }
            if (currentDate.getTime() == execRetryDate.getTime()) {
                if (ret.exeType == ret.AUTOINSPEXEDATETYPE_EXE) {
                    ret.exeType = ret.AUTOINSPEXEDATETYPE_EXEANDRETRY;
                } else {
                    ret.exeType = ret.AUTOINSPEXEDATETYPE_RETRY;
                }
            }
            if (ret.exeType != ret.AUTOINSPEXEDATETYPE_NONE) {
                break;
            }
        }
        return ret;
    }

    /**
     * 時間を加算した日付生成
     *
     * @param String execDayModWk  書式:yyyyMMddHH
     * @param String waitHour
     * @return Date
     */
    private Date getAddDate(String execDayModWk, int waitHour) {
        Date date_mod = DateUtility.conversionDate(execDayModWk, DateUtility.DATE_FORMAT_YYYYMMDDHH);

        date_mod = DateUtility.plusHour(date_mod, waitHour);

        return date_mod;
    }

    /**
     * 検針結果完了メール送信
     *
     * @param String devId
     * @param String meterTypeName
     * @param String adjustDate
     * @param String monthNo
     * @return
     */
    private void sendMailAutoInspComplate(String devId, String meterTypeName, String adjustDate, String monthNo) {
        Long incompcnt = dao.getCountIncomplate(devId, null, null, logic.getYear(adjustDate), logic.getMonth(adjustDate), monthNo);
        List<MAlertMailListResultSet> tMAlertMailList = dao.getMAlertMail(devId, true);
        // 検針データの未完了件数が0、かつメール送信者が登録している場合に完了メール作成
        if (incompcnt == 0 && tMAlertMailList.size() > 0) {
            // 宛先作成
            List<String> toAddresses = new ArrayList<>();
            for (MAlertMailListResultSet email : tMAlertMailList) {
                if (email.getEmail() != null) {
                    toAddresses.add(email.getEmail());
                }
            }
            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_AUTO_INSP_COMPLATE,
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));

            // 件名
            String mailSubject = String.format("自動検針完了のお知らせ ( 装置番号：%s 検針年：%s 検針月：%s 種別：%s )",
                    devId,
                    logic.getYear(adjustDate),
                    logic.getMonth(adjustDate),
                    meterTypeName);

            // バッチ設定からメール送信フラグ取得
            boolean useSendMailFlg = false;
            String useSendStr = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SEND_AVAILABLE);
            if (useSendStr != null && SmsBatchConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
                useSendMailFlg = true;
            }

            OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                    new ArrayList<>(), useSendMailFlg);

            // 自動検針完了メール送信
            SmsBatchMailService batchMailService = new SmsBatchMailService();
            if (!batchMailService.mailSend(osolMailParameter)) {
                batchLogger.error(this.batchName.concat(" SendTargetOberAlarmMail Failure"));
            }

        }

    }

    /**
     * 検針結果未完了メール送信
     *
     * @param devId String
     * @param meterType Long
     * @param adjustDate 書式:yyyyMMddHH
     * @param monthNo String
     * @return
     */
    private void sendMailAutoInspInComplate(String devId, Long meterType, String adjustDate, String monthNo) {
        List<Long> incompList = dao.getListIncomplate(devId, null, meterType, logic.getYear(adjustDate), logic.getMonth(adjustDate), monthNo);
        List<MAlertMailListResultSet> tMAlertMailList = dao.getMAlertMail(devId, true);
        // 検針データの未完了件数が0より大きく、かつメール送信者が登録している場合にメール作成
        if (incompList.size() > 0 && tMAlertMailList.size() > 0) {
            // 宛先作成
            List<String> toAddresses = new ArrayList<>();
            for (MAlertMailListResultSet email : tMAlertMailList) {
                if (email.getEmail() != null) {
                    toAddresses.add(email.getEmail());
                }
            }

            String meterListStr = "";
            int i = 0;
            for (Long meterId : incompList) {
                i++;
                meterListStr += (i > 1 ? (i % 10 != 1 ? ",": "\n"): "") + meterId.toString();
            }
            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_LOADSURVEY_ERROR,
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("meterList", meterListStr);

            // 件名
            String mailSubject = String.format("日報データ欠損のお知らせ(装置番号：%s 検針日時：%s)",
                    devId,
                    adjustDate);

            // バッチ設定からメール送信フラグ取得
            boolean useSendMailFlg = false;
            String useSendStr = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SEND_AVAILABLE);
            if (useSendStr != null && SmsBatchConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
                useSendMailFlg = true;
            }

            OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                    new ArrayList<>(), useSendMailFlg);

            // 自動検針完了メール送信
            SmsBatchMailService batchMailService = new SmsBatchMailService();
            if (!batchMailService.mailSend(osolMailParameter)) {
                batchLogger.error(this.batchName.concat(" SendTargetOberAlarmMail Failure"));
            }

        }

    }

    /**
     * 少数なし数値取得
     *
     * @param String     decimal_fract
     * @param BigDecimal val
     * @return 整数
     */
    private BigDecimal getScaleValue(String decimal_fract, BigDecimal val) {
        BigDecimal ret = null;
        if (val != null) {
            if (decimal_fract.equals(SmsBatchConstants.DECIMAL_FRACTION.HALFUP.getVal())) {
                // 四捨五入
                ret = val.setScale(0, RoundingMode.HALF_UP);
            } else if (decimal_fract.equals(SmsBatchConstants.DECIMAL_FRACTION.ROUNDDOWN.getVal())) {
                // 切捨て
                ret = val.setScale(0, RoundingMode.DOWN);
            } else if (decimal_fract.equals(SmsBatchConstants.DECIMAL_FRACTION.ROUNDUP.getVal())) {
                // 切り上げ
                ret = val.setScale(0, RoundingMode.UP);
            }
        }
        return ret;
    }

    /**
     * 料金計算
     *
     * @param Date nowDate
     * @return
     */
    private void calcPrice(Date nowDate) {

        batchLogger.info(this.batchName.concat(" 料金計算開始"));
        // 料金計算対象リスト取得
        List<AutoInspPriceCalcInfoResultSet> list = dao.listPriceCalc();
        batchLogger.info(this.batchName.concat(" 料金計算対象リスト数:" + list.size()));

        if (list.size() == 0) {
            batchLogger.info(this.batchName.concat(" 料金計算対象レコード無し"));
            return;
        }

        Map<Long, MeterGroupInfo> map = new HashMap<>();

        // グループに属するメーター単位で料金計算実施できるかチェック
        for (AutoInspPriceCalcInfoResultSet info : list) {
            // グループ毎の情報初期化
            if (!map.containsKey(info.getMeterGrpId())) {
                map.put(info.getMeterGrpId(),
                        new MeterGroupInfo(
                                info.getCorpId(),
                                info.getBuildingId(),
                                false,
                                null,
                                0L,
                                0L));
            }
            // グループ集計OKの場合
            if (map.get(info.getMeterGrpId()).calcDisEnable == false) {
                // 自動検針日時設定が正常ではない場合、次のグループ処理を行う
                if (info.getDay() == null || info.getDay().equals("0")) {
                    batchLogger.info(this.batchName.concat(" 自動検針日が不正(0) group=" + info.getMeterGrpId() + ",meter=" + info.getMeterMngID()));
                    map.get(info.getMeterGrpId()).calcDisEnable = true;
                    continue;
                }

                // m_group_price より 最新の集計年月を取得
                String latestDate = dao.getMaxDate(info.getMeterGrpId());

                Date startDate;
                Date endDate;

                if (latestDate != null && !latestDate.equals("")) {
                    // 最新主計年月が取得できた場合
                    String work = latestDate + String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, Integer.valueOf(info.getDay()));
                    // 最新の集計年月の1ヵ月後を算出(開始日)
                    Date dateWk = DateUtility.plusMonth(DateUtility.conversionDate(work, DateUtility.DATE_FORMAT_YYYYMMDD), +1);
                    startDate = dateWk;
                    // 最新の集計年月の2ヵ月後を算出(終了日)
                    dateWk = DateUtility.plusMonth(DateUtility.conversionDate(work, DateUtility.DATE_FORMAT_YYYYMMDD), +2);
                    endDate = dateWk;
                } else {
                    // レコードが取得できない場合（初回の場合）
                    String work = DateUtility.changeDateFormat(nowDate, DateUtility.DATE_FORMAT_YYYYMM);
                    // 先月
                    work += String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, Integer.valueOf(info.getDay()));
                    Date dateWk = DateUtility.plusMonth(DateUtility.conversionDate(work, DateUtility.DATE_FORMAT_YYYYMMDD), -1);
                    startDate = dateWk;
                    // 今月
                    endDate = DateUtility.conversionDate(work, DateUtility.DATE_FORMAT_YYYYMMDD);
                }
                if (nowDate.getTime() < endDate.getTime()) {
                    batchLogger.info(this.batchName.concat(" 検針日未到来 group=" + info.getMeterGrpId() + ",meter=" + info.getMeterMngID()));
                    // 現在時刻が終了日より前は検針日未到来のため、該当グループは料金計算しない
                    map.get(info.getMeterGrpId()).calcDisEnable = true;
                } else {
                    // 検針日時より、開始日・終了日を算出する
                    Date start_date_time = DateUtility.plusMinute(startDate, 30);
                    Date end_date_time = endDate;

                    // 開始日～終了日までの日数×48 でレコード数を計算
                    Long dayDiff = (end_date_time.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                    Long dayPlanCount = dayDiff * 48;

                    // d_day_load_survey のレコード件数を取得する
                    Long loadSurveyCount = dao.getCountLoadSurvey(
                            info.getDevId(),
                            info.getMeterMngID(),
                            DateUtility.changeDateFormat(start_date_time, DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                            DateUtility.changeDateFormat(end_date_time, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
                    if (loadSurveyCount != null && !dayPlanCount.equals(loadSurveyCount)) {
                        batchLogger.info(this.batchName.concat(" 検針データがそろっていない group=" + info.getMeterGrpId() + ",meter=" + info.getMeterMngID()));
                        // 検針データが揃っていない為、該当グループは料金計算しない
                        map.get(info.getMeterGrpId()).calcDisEnable = true;
                    } else {
                        map.get(info.getMeterGrpId()).latestDate = DateUtility.changeDateFormat(startDate, DateUtility.DATE_FORMAT_YYYYMM);
                    }

                }
            }
        }

        // グループに属するメーター単位で料金計算実施
        for (AutoInspPriceCalcInfoResultSet info : list) {
            // グループ料金計算がOKのケースのみ料金計算実施
            if (!map.get(info.getMeterGrpId()).calcDisEnable) {

                // テナントからの情報取得
                AutoInspTenantInfoResultSet tenantInfo = dao.getTenantInfo(info.getDevId(), info.getMeterMngID(), info.getMeterType());
                if (tenantInfo != null) {
                    // 料金単価情報テーブルより取得
                    List<AutoInspUnitPriceResultSet> unitList = dao.listUnitPrice(tenantInfo.getPricePlanId());
                    if (unitList.size() > 0) {
                        // 料金単価情報テーブルと総使用量から計算中料金を取得
                        Long inTotalUse = map.get(info.getMeterGrpId()).totalUse;
                        BigDecimal calcprice = getCalcUnitPrice(unitList, inTotalUse);
                        // 最低従量を取得
                        Long minLimitUsageVal = (unitList.get(0) != null) ? unitList.get(0).getLimitUsageVal() : 0;

                        BigDecimal price = BigDecimal.ZERO; // 料金
                        switch (SmsBatchConstants.POWER_PLAN.getViewName(tenantInfo.getPowerPlanId().intValue())) {
                        // ============= 料金計算（その他）=============
                        case OTHER:
                            price = calcOtherPrice(
                                    tenantInfo.getBasicPrice(),
                                    calcprice,
                                    tenantInfo.getDiscountRate(),
                                    info.getDecimalFraction());
                            break;
                        // ============= 料金計算（従量電灯Ａ）=============
                        case MENULIGHTA:
                            // 料金メニュー(従量電灯A)テーブルの取得
                            MPriceMenuLighta menuA = dao.getPriceMenuLightA(info.getBuildingId());
                            if (menuA != null) {
                                price = calcMenuLightAPrice(
                                        inTotalUse, minLimitUsageVal,
                                        menuA.getLowestPrice(), menuA.getFuelAdjustPrice(),
                                        menuA.getAdjustPriceOver15(), menuA.getRenewEnerPrice(),
                                        calcprice, tenantInfo.getDiscountRate(),
                                        info.getDecimalFraction());
                            }
                            break;
                        // ============= 料金計算（従量電灯Ｂ）=============
                        case MENULIGHTB:
                            // 料金メニュー(従量電灯B)テーブルの取得
                            MPriceMenuLightb menuB = dao.getPriceMenuLightB(info.getBuildingId());
                            if (menuB != null) {
                                price = calcMenuLightBPrice(
                                        inTotalUse, minLimitUsageVal,
                                        menuB.getBasicPrice(), tenantInfo.getContractCapacity(),
                                        menuB.getFuelAdjustPrice(), menuB.getRenewEnerPrice(),
                                        calcprice, tenantInfo.getDiscountRate(),
                                        info.getDecimalFraction());
                            }
                            break;

                        default:
                            break;
                        }
                        // 計算方法が2(減算)の場合、対象の装置は集計料金からマイナスする。
                        if (info.getCalcType().equals(SmsBatchConstants.CALC_TYPE.SUB.getVal())) {
                            price = price.multiply(BigDecimal.valueOf(-1));
                        }
                        // 計算された料金を計算する
                        map.get(info.getMeterGrpId()).totalPrice += price.longValue();
                    }
                }
            }
        }

        // グループ毎のトータル料金をDBに書き込み
        map.forEach((k, v) -> {
            // グループ毎の料金計算がNGではない
            if (!v.calcDisEnable) {
                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                if (entityManager.getTransaction().isActive()) {
                    MGroupPrice mGroupPrice = new MGroupPrice();
                    MGroupPricePK pk = new MGroupPricePK();
                    pk.setCorpId(v.corpId);
                    pk.setBuildingId(v.buildingId);
                    pk.setMeterGroupId(k);
                    pk.setLatestInspDate(v.latestDate);
                    mGroupPrice.setId(pk);
                    mGroupPrice.setRecDate(serverDateTime);
                    mGroupPrice.setRecMan("ryoukinkeisan");
                    Long total = 0L;
                    if (v.totalPrice >= 0) {
                        total = v.totalPrice;
                    }
                    mGroupPrice.setGroupPrice(BigDecimal.valueOf(total));
                    mGroupPrice.setVersion(0);
                    mGroupPrice.setCreateUserId(Long.valueOf(0));
                    mGroupPrice.setCreateDate(serverDateTime);
                    mGroupPrice.setUpdateUserId(Long.valueOf(0));
                    mGroupPrice.setUpdateDate(serverDateTime);

                    dao.insertGroupPrice(mGroupPrice);
                    if (entityManager.getTransaction().getRollbackOnly()) {
                        entityManager.getTransaction().rollback();
                    } else {
                        entityManager.getTransaction().commit();
                    }
                }
                entityManager.clear();

            }
        });
        batchLogger.info(this.batchName.concat(" 料金計算終了"));

    }

    /**
     * 料金単価リストより計算中料金取得
     *
     * @param List<AutoInspUnitPriceResultSet> unitList
     * @param Long                             inTotalUse
     * @return 計算中料金
     */
    private BigDecimal getCalcUnitPrice(
            List<AutoInspUnitPriceResultSet> unitList,
            long inTotalUse) {
        Long workTmp = 0L;
        BigDecimal calcprice = BigDecimal.ZERO; // 計算中料金
        for (AutoInspUnitPriceResultSet uinfo : unitList) {
            // (引数)使用量 ＞ 上限従量値 の場合
            if (inTotalUse > uinfo.getLimitUsageVal().longValue()) {
                // 計算中料金 = 計算中料金 + ( 上限従量値 － 計算中使用量 ) × 料金単価
                calcprice = calcprice.add(BigDecimal.valueOf(uinfo.getLimitUsageVal() - workTmp).multiply(uinfo.getUnitPrice()));
                // 上限従量値を、計算中使用量に設定
                workTmp = uinfo.getLimitUsageVal();
            }
            // (引数)使用量 ≦ 上限従量値 の場合
            if (inTotalUse <= uinfo.getLimitUsageVal().longValue()) {
                // 計算中料金 = 計算中料金 + ( 使用量 － 計算中使用量 ) × 料金単価
                calcprice = calcprice.add(BigDecimal.valueOf(inTotalUse - workTmp).multiply(uinfo.getUnitPrice()));
                break;
            }
        }
        return calcprice;
    }

    /**
     * その他料金計算
     *
     * @param BigDecimal basicPrice
     * @param BigDecimal calcprice
     * @param BigDecimal discountRate
     * @param String     decimalFraction
     * @return 計算結果
     */
    private BigDecimal calcOtherPrice(
            BigDecimal basicPrice,
            BigDecimal calcprice,
            BigDecimal discountRate,
            String decimalFraction) {
        // 割引額 = ( 基本料金 + 従量料金 ) * ( 割引率 / 100 )
        BigDecimal discount_price = BigDecimal.ZERO;
        discount_price = discount_price.add(basicPrice);
        discount_price = discount_price.add(calcprice);
        discount_price = discount_price.multiply(discountRate.divide(BigDecimal.valueOf(100)));

        // 料金 = ( 基本料金 ＋ 従量料金 ) - 割引額
        BigDecimal price = BigDecimal.ZERO;
        price = price.add(basicPrice);
        price = price.add(calcprice);
        price = price.subtract(discount_price);
        price = getScaleValue(decimalFraction, price);
        return price;
    }

    /**
     * 従量電灯Ａ料金計算
     *
     * @param Long       inTotalUse
     * @param Long       minLimitUsageVal
     * @param BigDecimal lowestPrice
     * @param BigDecimal fuelAdjustPrice
     * @param BigDecimal adjustPriceOver15
     * @param BigDecimal renewEnerPrice
     * @param BigDecimal calcprice
     * @param BigDecimal discountRate
     * @param String     decimalFraction
     * @return 計算結果
     */
    private BigDecimal calcMenuLightAPrice(
            long inTotalUse,
            long minLimitUsageVal,
            BigDecimal lowestPrice,
            BigDecimal fuelAdjustPrice,
            BigDecimal adjustPriceOver15,
            BigDecimal renewEnerPrice,
            BigDecimal calcprice,
            BigDecimal discountRate,
            String decimalFraction) {
        // 最低料金の適用仕様
        BigDecimal minPrice = BigDecimal.ZERO;
        if (inTotalUse < minLimitUsageVal) {
            minPrice = lowestPrice;
        }

        // 燃料費調整額
        BigDecimal fuelPrice = BigDecimal.ZERO;
        if (inTotalUse > minLimitUsageVal) {
            fuelPrice = BigDecimal.valueOf(minLimitUsageVal);
            fuelPrice = fuelPrice.multiply(fuelAdjustPrice);
            fuelPrice = fuelPrice.add(BigDecimal.valueOf(inTotalUse - minLimitUsageVal).multiply(adjustPriceOver15));
        } else {
            fuelPrice = BigDecimal.valueOf(inTotalUse);
            fuelPrice = fuelPrice.multiply(fuelAdjustPrice);
        }
        // 再生可能エネルギー発電促進賦課金
        BigDecimal renewPrice = BigDecimal.ZERO;
        if (inTotalUse > minLimitUsageVal) {
            renewPrice = BigDecimal.valueOf(minLimitUsageVal);
            renewPrice = renewPrice.multiply(renewEnerPrice);
            renewPrice = renewPrice.add(BigDecimal.valueOf(inTotalUse - minLimitUsageVal).multiply(adjustPriceOver15));
        } else {
            renewPrice = BigDecimal.valueOf(inTotalUse);
            renewPrice = renewPrice.multiply(renewEnerPrice);
        }
        renewPrice = getScaleValue(decimalFraction, renewPrice);
        // 電力料金(試算額) ＝ 最低料金 + 従量料金 + 燃料費調整額 + 再生可能エネルギー発電促進賦課金
        BigDecimal powerPrice = BigDecimal.ZERO;
        powerPrice = powerPrice.add(minPrice);
        powerPrice = powerPrice.add(calcprice);
        powerPrice = powerPrice.add(fuelPrice);
        powerPrice = powerPrice.add(renewPrice);
        // 高圧一括受電割引 ＝ 電力料金（試算額）* 0.05 (※0.05は固定値)
        BigDecimal highPressurePrice = BigDecimal.ZERO;
        highPressurePrice = highPressurePrice.add(powerPrice);
        highPressurePrice = highPressurePrice.multiply(BigDecimal.valueOf(0.05));
        // 割引額 ＝ （電力料金（試算額）- 高圧一括受電割引） * 割引率
        BigDecimal discount_price = BigDecimal.ZERO;
        discount_price = discount_price.add(powerPrice);
        discount_price = discount_price.subtract(highPressurePrice);
        discount_price = discount_price.multiply(discountRate.divide(BigDecimal.valueOf(100)));
        // 小計金額 ＝ 電力料金（試算額）- 高圧一括受電割引 - 割引額
        BigDecimal price = powerPrice;
        price = price.subtract(highPressurePrice);
        price = price.subtract(discount_price);
        price = getScaleValue(decimalFraction, price);
        return price;
    }

    /**
     * 従量電灯Ｂ料金計算
     *
     * @param Long       inTotalUse
     * @param Long       minLimitUsageVal
     * @param BigDecimal inBasicPrice
     * @param BigDecimal contractCapacity
     * @param BigDecimal fuelAdjustPrice
     * @param BigDecimal renewEnerPrice
     * @param BigDecimal calcprice
     * @param BigDecimal discountRate
     * @param String     decimalFraction
     * @return 計算結果
     */
    private BigDecimal calcMenuLightBPrice(
            long inTotalUse,
            long minLimitUsageVal,
            BigDecimal inBasicPrice,
            BigDecimal contractCapacity,
            BigDecimal fuelAdjustPrice,
            BigDecimal renewEnerPrice,
            BigDecimal calcprice,
            BigDecimal discountRate,
            String decimalFraction) {
        // 基本料金 = 基本料金 × 契約容量 ※使用量(=0)の場合は半額
        BigDecimal basicPrice = inBasicPrice;
        if (inTotalUse <= 0) {
            basicPrice = basicPrice.multiply(contractCapacity);
            basicPrice = basicPrice.divide(BigDecimal.valueOf(2));
        } else {
            basicPrice = basicPrice.multiply(contractCapacity);
        }
        // 燃料費調整額
        BigDecimal fuelPrice = BigDecimal.valueOf(inTotalUse);
        fuelPrice = fuelPrice.multiply(fuelAdjustPrice);
        // 再生可能エネルギー発電促進賦課金
        BigDecimal renewPrice = getScaleValue(decimalFraction, BigDecimal.valueOf(inTotalUse).multiply(renewEnerPrice));
        // 電力料金(試算額) ＝ 基本料金 + 従量料金 + 燃料費調整額 + 再生可能エネルギー発電促進賦課金
        BigDecimal powerPrice = BigDecimal.ZERO;
        powerPrice = powerPrice.add(basicPrice);
        powerPrice = powerPrice.add(calcprice);
        powerPrice = powerPrice.add(fuelPrice);
        powerPrice = powerPrice.add(renewPrice);
        // 高圧一括受電割引 ＝ 電力料金（試算額）* 0.05 (※0.05は固定値)
        BigDecimal highPressurePrice = BigDecimal.ZERO;
        highPressurePrice = highPressurePrice.add(powerPrice);
        highPressurePrice = highPressurePrice.multiply(BigDecimal.valueOf(0.05));
        // 割引額 ＝ （電力料金（試算額）- 高圧一括受電割引） * 割引率
        BigDecimal discount_price = BigDecimal.ZERO;
        discount_price = discount_price.add(powerPrice);
        discount_price = discount_price.subtract(highPressurePrice);
        discount_price = discount_price.multiply(discountRate.divide(BigDecimal.valueOf(100)));
        // 小計金額 ＝ 電力料金（試算額）- 高圧一括受電割引 - 割引額
        BigDecimal price = powerPrice;
        price = price.subtract(highPressurePrice);
        price = price.subtract(discount_price);
        price = getScaleValue(decimalFraction, price);
        return price;
    }

    /**
     * 日付存在チェック
     *
     * @param String yearWk
     * @param String dayWk
     * @param String hourWk
     * @return Boolean
     */
    private Boolean day_exists(String yearWk, String monthWk, String dayWk) {
        Integer[] mlast = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int yearNum = Integer.valueOf(yearWk);
        int monthNum = Integer.valueOf(monthWk);
        int dayNum = Integer.valueOf(dayWk);
        if (monthNum < 1 || 12 < monthNum) {
            return false;
        }

        if (monthNum == 2) {
            if (((yearNum % 4 == 0) && (yearNum % 100 != 0))
                    || (yearNum % 400 == 0)) {
                mlast[1]++;
            }
        }

        if (dayNum < 1 || mlast[monthNum - 1] < dayNum) {
            return false;
        }
        return true;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 日文字列
     */
    private String getDay(String targetDate) {
        String ret = targetDate.substring(6, 8);
        return ret;
    }

    /**
     * 日付文字列変換
     *
     * @param String targetDate
     * @return 時間文字列
     */
    private String getHour(String targetDate) {
        String ret = targetDate.substring(8, 10);
        return ret;
    }
}
