package jp.co.osaki.sms.batch.timerservice;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;

import org.jboss.ejb3.annotation.TransactionTimeout;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.batch.util.DemandDataUtil;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.CheckSurvey_missingDataSendCsvDao;
import jp.co.osaki.sms.batch.logic.CheckSurvey_missngDataSendCsvLogic;
import jp.co.osaki.sms.batch.resultset.BatchSendEmailResultSet;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.LoadSurveyResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.co.osaki.sms.batch.resultset.MeterTypeResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;
import jp.co.osaki.sms.batch.resultset.MissngDataCsvResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * SMS 日報当日分のデータ欠損メール送信 CSV添付 TimerServiceクラス
 *
 * @author nishida.t
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CheckSurvey_missngDataSendCsvTimerService extends SmsBatchTimerService implements Serializable  {
    /**
     *
     */
    private static final long serialVersionUID = -6109132586636019077L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.CHECK_SURVEY_MISSNGDATA_CSV.getVal();

    private static final String alertCd = "loadsurvey_csv_err";

    /**
     * Dao
     */
    private CheckSurvey_missingDataSendCsvDao dao;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new CheckSurvey_missingDataSendCsvDao(this.entityManager);

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
            batchLogger.info(this.batchName.concat(" Start"));

            // 現在時刻の時限まで日報欠損チェック

            // 処理起点の日付を日付ベースで保持
            Date currentDate = new Date(dao.getServerDateTime().getTime());

            //昨日の日時を取得
            Calendar calYesterDayDate = Calendar.getInstance();
            calYesterDayDate.setTime(currentDate);
            // To（昨日の23:30まで）
            String aftDate = changeCurrentMinute(DateUtility.changeDateFormat(calYesterDayDate.getTime(), DateUtility.DATE_FORMAT_YYYYMMDD).concat("0000"));

            calYesterDayDate.add(Calendar.DAY_OF_MONTH, -1);
            // From
            String befDate = DateUtility.changeDateFormat(calYesterDayDate.getTime(), DateUtility.DATE_FORMAT_YYYYMMDD).concat("0030");


            //時刻をTOの時間に設定する
            calYesterDayDate.set(Calendar.HOUR_OF_DAY , 23);
            calYesterDayDate.set(Calendar.MINUTE, 30);

            // 現在時刻の6つ前の時限までが対象となる時限
            // 現在時刻から取得した時限No.に「-6」した数値が、欠損していない場合の日報レコード想定数
            // 直前の時限が欠損していても許容（実行タイミングによっては直前が欠損しているため）
            // 例：）23:58（48時限目）に実行 → 20:30～21:00（42時限目）の42が想定件数となる
            BigDecimal targetCount = getJigenNo(DateUtility.changeDateFormat(calYesterDayDate.getTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM));

            // 対象建物取得
            List<MissingLoadSurveyListResultSet> buildingList = dao.listMissingBuild();

            // Logicクラス初期化
            CheckSurvey_missngDataSendCsvLogic logic = new CheckSurvey_missngDataSendCsvLogic(this.batchName, smsBatchConfigs);

            // CSV出力データリストを作成
            List<MissngDataCsvResultSet> csvDataList = new ArrayList<>();

            //メール本文用
            List<MissngDataCsvResultSet> mailBodyList = new ArrayList<>();

            // 一括メール宛先取得
            BatchSendEmailResultSet batchSendMail = dao.getBatchSendEmailList(alertCd);

            // 対象建物ごとにループ
            for (MissingLoadSurveyListResultSet building : buildingList) {
                // 企業ID、建物ID取得
                String corpId = building.getCorpId();
                Long buildingId = building.getBuildingId();

                // 建物に属する対象装置取得
                List<CheckSurveyDevPrmResultSet> devList = dao.listMissingDev(corpId, buildingId);

                // メーター種別リスト
                List<MeterTypeResultSet> meterTypeList = dao.getMeterTypeList(corpId, buildingId);

                // 装置ごとにループ
                for (CheckSurveyDevPrmResultSet devPrm : devList) {
                    List<MissngDataCsvResultSet> mailDataList = new ArrayList<>();

                    MPauseMailResultSet sendMail = this.dao.getMissingDataSendMailFlg(devPrm.getDevId());

                    if(sendMail != null && (sendMail.getSendFlg() != null &&  sendMail.getSendFlg().equals("1"))) {
                        continue;
                    }

                    // 対象装置のメーター取得
                    List<MMeter> meterList =  dao.getCountTargetList(devPrm.getDevId(), calYesterDayDate.getTime());

                    // 対象のメーター管理番号リスト
                    List<Long> targetMeterMngIdList = meterList.stream()
                            .map(obj -> obj.getId().getMeterMngId())
                            .collect(Collectors.toList());

                    // メーターごとの日報欠損マップ Map<メーター管理番号, List<時限No>>
                    Map<Long, List<Integer>> targetMeterMngIdMap = new LinkedHashMap<>();

                    // メーターごとにチェック
                    for (Long meterMngId : targetMeterMngIdList) {
                        // 日報リスト取得（当日の対象時限まで）
                        List<LoadSurveyResultSet> loadSurveyList = dao.getLoadSurveyList(devPrm.getDevId(), befDate, aftDate, meterMngId);

                        // 欠損時限を格納するリスト
                        List<Integer> jigenList = new ArrayList<>();

                        // 想定件数分チェックして欠損箇所を抽出
                        for (int i = 1; i <= targetCount.intValue(); i++) {

                            boolean notMissingJigenFlg = false;
                            for (LoadSurveyResultSet laodSurvey : loadSurveyList) {
                                BigDecimal jigenNo = getLoadSurveyJigenNo(laodSurvey.getGetDate());
                                // 欠損なし（日報データあり）
                                if (i == jigenNo.intValue()) {
                                    notMissingJigenFlg = true;
                                    break;
                                }
                            }

                            // 欠損あり（日報データがないまたは、null値がある）
                            if (!notMissingJigenFlg) {
                                jigenList.add(i);
                            }

                        }

                        // 時限リストが空でなければ欠損あり
                        if (!jigenList.isEmpty()) {
                            // メーター番号、欠損時限リストをMapに追加
                            targetMeterMngIdMap.put(meterMngId, jigenList);
                        }

                    }

                    // 欠損しているメーターがない場合は次の装置へ
                    if (targetMeterMngIdMap.isEmpty()) {
                        continue;
                    }

                    // 建物メーターリスト
                    List<MissngDataCsvResultSet> meterBuildingList = dao.getMeterBuildingList(corpId, buildingId, devPrm.getDevId(),
                            new ArrayList<Long>(targetMeterMngIdMap.keySet()));
                    // テナントメーターリスト
                    List<MissngDataCsvResultSet> meterTenantList = dao.getMeterTenantList(corpId, buildingId, devPrm.getDevId());

                    // メール本文データ一時格納リスト
                    List<MissngDataCsvResultSet> mailTempList = new ArrayList<>();

                    // 建物関係の情報とテナント関係の情報を結合
                    for (MissngDataCsvResultSet meterBuilding : meterBuildingList) {
                        MissngDataCsvResultSet ret = new MissngDataCsvResultSet();
                        ret.setCorpName(meterBuilding.getCorpName());
                        ret.setBuildingName(meterBuilding.getBuildingName());
                        ret.setDevName(meterBuilding.getDevName());
                        ret.setMeterMngId(meterBuilding.getMeterMngId());
                        ret.setMeterType(meterBuilding.getMeterType());
                        ret.setMeterId(meterBuilding.getMeterId());
                        ret.setMeterSta(meterBuilding.getMeterSta());
                        ret.setMeterPresSitu(meterBuilding.getMeterPresSitu());
                        ret.setAlertPauseStart(meterBuilding.getAlertPauseStart());
                        ret.setAlertPauseEnd(meterBuilding.getAlertPauseEnd());
                        ret.setMeterStaMemo(meterBuilding.getMeterStaMemo());

                        // テナントに紐づくメーターは、テナント名を設定
                        for (MissngDataCsvResultSet meterTenant : meterTenantList) {
                            if (meterBuilding.getMeterMngId().equals(meterTenant.getMeterMngId())) {
                                ret.setTenantName(meterTenant.getBuildingName());
                            }
                        }

                        // テナントに紐づかないメーター
                        if (CheckUtility.isNullOrEmpty(ret.getTenantName())) {
                            ret.setTenantName("-");
                        }

                        // メーターID（計器ID）チェック
                        if (CheckUtility.isNullOrEmpty(ret.getMeterId())) {
                            ret.setMeterId("-");
                        }

                        mailTempList.add(ret);
                    }

                    // メーター種別リストと結合
                    for (MissngDataCsvResultSet mailTemp : mailTempList) {
                        for (MeterTypeResultSet meterType : meterTypeList) {
                            if (meterType.getMeterType().equals(mailTemp.getMeterType())) {
                                mailTemp.setMeterTypeName(meterType.getMeterTypeName());
                            }
                        }

                        if (CheckUtility.isNullOrEmpty(mailTemp.getMeterTypeName())) {
                            mailTemp.setMeterTypeName("-");
                        }

                        // 欠損ありとしてCSV出力リストに追加
                        mailDataList.add(mailTemp);
                        mailBodyList.add(mailTemp);
                    }

                    // 欠損しているメーター情報が取得できない場合は次の装置へ
                    if (mailDataList.isEmpty()) {
                        continue;
                    }

                    for (MissngDataCsvResultSet mailData : mailDataList) {
                        // 日報欠損Mapから欠損時限情報を取得
                        if (targetMeterMngIdMap.containsKey(mailData.getMeterMngId())) {
                            List<Integer> jigenNoList = targetMeterMngIdMap.get(mailData.getMeterMngId());
                            for (Integer jigenNo : jigenNoList) {
                                // 時限の開始時間を取得したいため 時限No. - 1
                                String jigenTime = getJigenTime(jigenNo - 1).concat("～");
                                MissngDataCsvResultSet csvData = mailData.clone();

                                // 時限（時刻）を設定
                                csvData.setJigenTime(jigenTime);

                                // CSV出力データリストに追加
                                csvDataList.add(csvData);
                            }
                        }
                    }
                }
            }

            replaceNewLine(csvDataList);

            List<String> toAddresses = new ArrayList<>();
            String mailBody = "";

            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_CHECK_SURVEY_MISSINGDATA_SEND_CSV,
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("recordList", mailBodyList);

            // テンプレートファイル内、変数置換後のメール本文
            mailBody = mailBody + mailTemplate.merge();

            // メール添付用CSV作成(zip圧縮)
            String path = logic.createCheckSurvey_missngDataCsv(csvDataList, new Timestamp(currentDate.getTime()));

            if (CheckUtility.isNullOrEmpty(path)) {
                batchLogger.error(this.batchName.concat(" Failed To Create Csv ")
                        .concat(this.getClass().getSimpleName()));
            } else {
                File file = new File(path);

                if (file.exists()) {
                    // メール本文生成
                    String bef = DateUtility.changeDateFormat(
                            DateUtility.conversionDate(befDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                            DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                    mailBody = bef + "分（48時限）において、下記装置にデータ欠損があります\n\n" + mailBody;
                    // 全量データの送信先アドレス
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail1());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail2());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail3());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail4());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail5());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail6());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail7());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail8());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail9());
                  toAddresses = addAddress(toAddresses, batchSendMail.getMail10());

                    // メール送信処理
                    // 件名に付与するメール配信時間
                    String mailSendDatetime = DateUtility.changeDateFormat(currentDate, "yyyy/MM/dd/HH:mm");

                    // 件名
                    String mailSubject = String.format(" SMS 日報昨日分のデータ欠損報告%s%s%s",
                            "（", mailSendDatetime, "）");
                    // バッチ設定からメール送信フラグ取得
                    boolean useSendMailFlg = false;
                    String useSendStr = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SEND_AVAILABLE);
                    if (useSendStr != null && SmsBatchConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
                        useSendMailFlg = true;
                    }
                    OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailBody,
                            smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                            new ArrayList<>(), useSendMailFlg);
                    // メール送信
                    SmsBatchMailService batchMailService = new SmsBatchMailService();
                    if (!batchMailService.sendMail(osolMailParameter, file)) {
                        batchLogger.error(this.batchName.concat(" SendTargetOberAlarmCsvMail Failure"));
                    }
                    AnalysisEmsUtility.deleteFileDirectory(new File(path));
                } else {
                    batchLogger.error(this.batchName.concat(" No Such Directory ")
                            .concat(this.getClass().getSimpleName()).concat(" filePath：" + path));
                }
            }
        } catch (Exception ex) {
            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

    /**
     * 時限Noを取得する
     *
     * @param recordDate
     * @return
     */
    private BigDecimal getJigenNo(String recordDate) {
        String hh = recordDate.substring(8, 10);
        String mm = recordDate.substring(10);

        BigDecimal jigenNo = DemandDataUtil.changeHHToJigenNo(hh);
        if (Integer.parseInt(mm) >= 00 && Integer.parseInt(mm) <= 29) {
            return  jigenNo;
        } else {
            return jigenNo.add(BigDecimal.ONE);
        }

    }

    /**
     * mm（分）を、00または30に変換した時刻を返却 <br/>
     * 0～29分を00、30～59分を30
     * @param recordDate yyyyMMddHHmm
     * @return yyyyMMddHH00 or yyyyMMddHH30
     */
    private String changeCurrentMinute(String recordDate) {
        String yyyymmdd = recordDate.substring(0, 8);
        String hh = recordDate.substring(8, 10);
        String mm = recordDate.substring(10);

        if (Integer.parseInt(mm) >= 00 && Integer.parseInt(mm) <= 29) {
            return  yyyymmdd.concat(hh).concat("00");
        } else {
            return yyyymmdd.concat(hh).concat("30");
        }
    }

    /**
     * 時限NoからHH:mm形式に変換
     * @param jigenNo
     * @return
     */
    private String getJigenTime (Integer jigenNo) {
        return DateUtility.changeDateFormat(DemandDataUtil.getJigenTime(dao.getServerDateTime(), jigenNo),
                DateUtility.DATE_FORMAT_HHMM_COLON);
    }

    /**
     * null or 空文字をチェックしてリストにadd
     *
     * @param List<String> addressList
     * @param String address
     * @return アドレスリスト
     */
    private List<String> addAddress(List<String> addressList, String address) {
        if (!CheckUtility.isNullOrEmpty(address)) {
            addressList.add(address);
        }
        return addressList;
    }

    /**
     * ロードサーベイ日データ.収集日時用の時限Noを取得する
     *
     * @param recordDate yyyyMMddHHmm
     * @return
     */
    private BigDecimal getLoadSurveyJigenNo(String recordDate) {
        String hh = recordDate.substring(8, 10);
        String mm = recordDate.substring(10);

        BigDecimal jigenNo = null;

        switch (hh) {
        case "00":
            jigenNo = new BigDecimal(0);
            break;
        case "01":
            jigenNo = new BigDecimal(2);
            break;
        case "02":
            jigenNo = new BigDecimal(4);
            break;
        case "03":
            jigenNo = new BigDecimal(6);
            break;
        case "04":
            jigenNo = new BigDecimal(8);
            break;
        case "05":
            jigenNo = new BigDecimal(10);
            break;
        case "06":
            jigenNo = new BigDecimal(12);
            break;
        case "07":
            jigenNo = new BigDecimal(14);
            break;
        case "08":
            jigenNo = new BigDecimal(16);
            break;
        case "09":
            jigenNo = new BigDecimal(18);
            break;
        case "10":
            jigenNo = new BigDecimal(20);
            break;
        case "11":
            jigenNo = new BigDecimal(22);
            break;
        case "12":
            jigenNo = new BigDecimal(24);
            break;
        case "13":
            jigenNo = new BigDecimal(26);
            break;
        case "14":
            jigenNo = new BigDecimal(28);
            break;
        case "15":
            jigenNo = new BigDecimal(30);
            break;
        case "16":
            jigenNo = new BigDecimal(32);
            break;
        case "17":
            jigenNo = new BigDecimal(34);
            break;
        case "18":
            jigenNo = new BigDecimal(36);
            break;
        case "19":
            jigenNo = new BigDecimal(38);
            break;
        case "20":
            jigenNo = new BigDecimal(40);
            break;
        case "21":
            jigenNo = new BigDecimal(42);
            break;
        case "22":
            jigenNo = new BigDecimal(44);
            break;
        case "23":
            jigenNo = new BigDecimal(46);
            break;
        }

        if (jigenNo == null) {
            return jigenNo;
        }

        // 00時の場合
        if (jigenNo.compareTo(BigDecimal.ZERO) == 0) {
            if (mm.equals("00")) {
                // 00分の場合
                jigenNo = new BigDecimal(48);
            } else if (mm.equals("30")) {
                // 30分の場合
                jigenNo = new BigDecimal(1);
            }

        } else if (mm.equals("30")) {
            // 00時以外は、30分であれば時限を1加算
            jigenNo = jigenNo.add(BigDecimal.ONE);
        }

        return jigenNo;
    }

    /**
     * 改行コード置き換え
     * @param csvDataList
     */
    private void replaceNewLine(List<MissngDataCsvResultSet> csvDataList) {
        for(MissngDataCsvResultSet data : csvDataList) {
            if(!CheckUtility.isNullOrEmpty(data.getMeterStaMemo())) {
                data.setMeterStaMemo(data.getMeterStaMemo().replace("\n", ""));
            }
        }
    }

    private String mailAftDateChg(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -3);
        date = calendar.getTime();

        return changeCurrentMinute(DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
    }

}
