package jp.co.osaki.sms.batch.timerservice;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.UnsettledMeterReadingDao;
import jp.co.osaki.sms.batch.logic.UnsettledMeterReadingLogic;
import jp.co.osaki.sms.batch.resultset.AutoInspMeterSvrResultSet;
import jp.co.osaki.sms.batch.resultset.BatchSendEmailResultSet;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.co.osaki.sms.batch.resultset.MeterTypeResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;
import jp.co.osaki.sms.batch.resultset.UnsettledMeterReadingDataCsvResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * SMS 未確定検針アラートメール送信 CSV添付 TimerServiceクラス
 *
 * @author nishida.t
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class UnsettledMeterReadingTimerService extends SmsBatchTimerService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6697009770906704792L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.UNSETTLED_METER_READING_CSV.getVal();

    private static final String alertCd = "unsettled_csv_err";

    /**
     * Dao
     */
    private UnsettledMeterReadingDao dao;

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
        this.dao = new UnsettledMeterReadingDao(this.entityManager);

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

            // 処理起点の日付を日付ベースで保持
            Date currentDate = new Date(dao.getServerDateTime().getTime());

         // 一括メール宛先取得
            BatchSendEmailResultSet batchSendMail = dao.getBatchSendEmailList(alertCd);

            // 検針年(YYYY)
            String inspYear = DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYY);

            // 対象建物取得
            List<MissingLoadSurveyListResultSet> buildingList = dao.getBuildingList();

            // Logicクラス初期化
            UnsettledMeterReadingLogic logic = new UnsettledMeterReadingLogic(this.batchName, smsBatchConfigs);

            // CSV出力データリスト
            List<UnsettledMeterReadingDataCsvResultSet> csvDataList = new ArrayList<>();

            // 対象建物ごとにループ
            for (MissingLoadSurveyListResultSet building : buildingList) {
                // 企業ID、建物ID取得
                String corpId = building.getCorpId();
                Long buildingId = building.getBuildingId();

                // 建物に属する対象装置取得
                List<CheckSurveyDevPrmResultSet> devList = dao.getDevPrmList(corpId, buildingId);

                // メーター種別リスト
                List<MeterTypeResultSet> meterTypeList = dao.getMeterTypeList(corpId, buildingId);

                // 装置ごとにループ
                for (CheckSurveyDevPrmResultSet devPrm : devList) {

                    MPauseMailResultSet sendMail = this.dao.getUnsettledSendMailFlg(devPrm.getDevId());

                    if(sendMail != null && (sendMail.getSendFlg() != null &&  sendMail.getSendFlg().equals("1"))) {
                        continue;
                    }

                    //メーター種別ごとに最新検針日時を格納
                    Map<Long, Timestamp> latestInspDateMap = new HashMap<>();

                    // 対象のメーター管理番号リスト
                    List<Long> targetMeterMngIdList = new ArrayList<>();

                    // メーター種別ごとに未完了の検針結果を取得（メーター種別ごとに月検針連番が違うため）
                    for (MeterTypeResultSet meterType : meterTypeList) {
                        // 装置の最新検針日時取得
                        Timestamp latestInspDate = dao.getLatestInspDate(devPrm.getDevId(), meterType.getMeterType());

                        latestInspDateMap.put(meterType.getMeterType(), latestInspDate);
                        // 月検針連番の最新値が取得できない場合スキップ
                        if (latestInspDate == null) {
                            continue;
                        }

                        // 未完了の検針結果リスト取得
                        List<AutoInspMeterSvrResultSet> retList = dao.getUnsettledMeterReadingList(devPrm.getDevId(), latestInspDate);

                        // 未完了の検針結果が無い場合スキップ
                        if (retList.isEmpty()) {
                            continue;
                        }

                        // 対象のメーター管理番号リストに追加
                        targetMeterMngIdList.addAll(
                                retList.stream()
                                .map(obj -> obj.getMeterMngId())
                                .collect(Collectors.toList()));
                    }

                    // 検針結果が未完了のメーターがない場合は次の装置へ
                    if (targetMeterMngIdList.isEmpty()) {
                        continue;
                    }

                    // 建物メーターリスト
                    List<UnsettledMeterReadingDataCsvResultSet> meterBuildingList = dao.getMeterBuildingList(corpId, buildingId, devPrm.getDevId(), targetMeterMngIdList);
                    // テナントメーターリスト
                    List<UnsettledMeterReadingDataCsvResultSet> meterTenantList = dao.getMeterTenantList(corpId, buildingId, devPrm.getDevId());

                    // CSVデータ一時格納リスト
                    List<UnsettledMeterReadingDataCsvResultSet> csvTempList = new ArrayList<>();

                    // 建物関係の情報とテナント関係の情報を結合
                    for (UnsettledMeterReadingDataCsvResultSet meterBuilding : meterBuildingList) {
                        UnsettledMeterReadingDataCsvResultSet ret = new UnsettledMeterReadingDataCsvResultSet();
                        ret.setCorpName(meterBuilding.getCorpName());
                        ret.setBuildingName(meterBuilding.getBuildingName());
                        ret.setDevName(meterBuilding.getDevName());
                        ret.setMeterMngId(meterBuilding.getMeterMngId());
                        ret.setMeterType(meterBuilding.getMeterType());
                        ret.setMeterSta(meterBuilding.getMeterSta());
                        ret.setMeterPresSitu(meterBuilding.getMeterPresSitu());
                        ret.setAlertPauseStart(meterBuilding.getAlertPauseStart());
                        ret.setAlertPauseEnd(meterBuilding.getAlertPauseEnd());
                        ret.setMeterStaMemo(meterBuilding.getMeterStaMemo());
                        ret.setLatestInspDate(latestInspDateMap.get(meterBuilding.getMeterType()));

                        // テナントに紐づくメーターは、テナント名を設定
                        for (UnsettledMeterReadingDataCsvResultSet meterTenant : meterTenantList) {
                            if (meterBuilding.getMeterMngId().equals(meterTenant.getMeterMngId())) {
                                ret.setTenantName(meterTenant.getBuildingName());
                            }
                        }

                        // テナントに紐づかないメーター
                        if (CheckUtility.isNullOrEmpty(ret.getTenantName())) {
                            ret.setTenantName("-");
                        }

                        csvTempList.add(ret);
                    }

                    // メーター種別リストと結合
                    for (UnsettledMeterReadingDataCsvResultSet csvTemp : csvTempList) {
                        for (MeterTypeResultSet meterType : meterTypeList) {
                            if (meterType.getMeterType().equals(csvTemp.getMeterType())) {
                                csvTemp.setMeterTypeName(meterType.getMeterTypeName());
                            }
                        }

                        if (CheckUtility.isNullOrEmpty(csvTemp.getMeterTypeName())) {
                            csvTemp.setMeterTypeName("-");
                        }

                        // CSV出力リストに追加
                        csvDataList.add(csvTemp);
                    }

                    // メーター情報が取得できない場合は次の装置へ
                    if (csvDataList.isEmpty()) {
                        continue;
                    }
                }
            }

            replaceNewLine(csvDataList);

            List<String> toAddresses = new ArrayList<>();
            String mailBody = "";

            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_UNSETTLED_METER_READINGDATA_SEND_CSV,
                    smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("recordList", csvDataList);

            // テンプレートファイル内、変数置換後のメール本文
            mailBody = mailBody + mailTemplate.merge();
            // メール添付用CSV作成(zip圧縮)
            String path = logic.createUnsettledMeterReadingDataCsv(csvDataList, new Timestamp(currentDate.getTime()));

            if (CheckUtility.isNullOrEmpty(path)) {
                batchLogger.error(this.batchName.concat(" Failed To Create Csv ")
                        .concat(this.getClass().getSimpleName()));
            } else {
                File file = new File(path);

                if (file.exists()) {
                    // メール本文生成

                    // メール本文 検針月
                    String meterReadingMonth= DateUtility.changeDateFormat(currentDate, "MM月dd日HH時");

                    // 検針年：yyyy年　検針月：MM月dd日HH時
                    // 下記装置において、検針が確定しておりません。
                    mailBody = "検針年：" + inspYear + "年　検針月：" + meterReadingMonth
                            + "\n下記装置において、検針が確定しておりません。\n\n" + mailBody;
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
                    String mailSubject = String.format(" SMS 未確定検針報告%s%s%s",
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
                        batchLogger.error(this.batchName.concat(" SendTargetUnsettledMeterReadingCsvMail Failure"));
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
     * 改行コード置き換え
     * @param csvDataList
     */
    private void replaceNewLine(List<UnsettledMeterReadingDataCsvResultSet> csvDataList) {
        for(UnsettledMeterReadingDataCsvResultSet data : csvDataList) {
            if(!CheckUtility.isNullOrEmpty(data.getMeterStaMemo())) {
                data.setMeterStaMemo(data.getMeterStaMemo().replace("\n", ""));
            }
        }
    }

}
