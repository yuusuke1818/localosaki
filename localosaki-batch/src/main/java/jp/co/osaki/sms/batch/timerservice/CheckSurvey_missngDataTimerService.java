package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.CheckSurvey_missingDataDao;
import jp.co.osaki.sms.batch.resultset.CheckSurveyDevPrmResultSet;
import jp.co.osaki.sms.batch.resultset.MissingLoadSurveyListResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * SMS 日報2日前のデータ欠損メール送信 TimerServiceクラス
 *
 * @author tominaga
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CheckSurvey_missngDataTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 2475721550662362194L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.CHECK_SURVEY_MISSNGDATA.getVal();

    /**
     * Dao
     */
    private CheckSurvey_missingDataDao dao;

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
        this.dao = new CheckSurvey_missingDataDao(this.entityManager);

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

            // メール宛先取得
            List<String> mailList = dao.listEmailList();
            // ターゲット日時設定
            int dayNum = Integer.valueOf(smsBatchConfigs.getConfig(SmsBatchConstants.MISSING_LOAD_SURVEY_DAYNUM));
            String targetaftDate = DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD);
            String targetbefDate = DateUtility.changeDateFormat(
                    DateUtility.plusDay(currentDate, -dayNum),
                    DateUtility.DATE_FORMAT_YYYYMMDD);

            // 対象建物取得
            List<MissingLoadSurveyListResultSet> buildList = dao.listMissingBuild();

            // 対象建物毎でループ
            for (MissingLoadSurveyListResultSet buildInfo : buildList) {
                List<String> toAddresses = new ArrayList<>();
                String mailBody = "";
                // 企業ID、建物ID取得
                String corpId = buildInfo.getCorpId();
                Long buildId = buildInfo.getBuildingId();

                // 建物に属する対象装置取得
                List<CheckSurveyDevPrmResultSet> devList = dao.listMissingDev(corpId, buildId);
                // 建物毎にループ
                for (CheckSurveyDevPrmResultSet devPrm : devList) {
                    // ターゲット日時の間の実際の日報件数取得
                    Long countSurvey = dao.getCountLoadSurvey(devPrm.getDevId(), targetbefDate + "0030", targetaftDate + "0000");
                    // 対象装置のメーター件数取得
                    Long targetCount = dao.getCountTarget(devPrm.getDevId());
                    // 対象装置の予定日報件数算出(メーター1個につき1日48件としてターゲット日数分かける)
                    targetCount = targetCount * 48 * dayNum;
                    // 予実差異がない場合は次の装置へ
                    if (countSurvey == targetCount) {
                        continue;
                    }
                    // 予実差異ある場合は日報欠損ありとしてメール本文作成
                    Long missingData = targetCount - countSurvey;
                    if (missingData != 0) {
                        // 本文テンプレート作成
                        BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_CHECK_SURVEY_MISSINGDATA_ALART,
                                smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
                        mailTemplate.put("devId", !CheckUtility.isNullOrEmpty(devPrm.getDevName()) ? devPrm.getDevName() : devPrm.getDevId());
                        mailTemplate.put("countSurvey", countSurvey);
                        mailTemplate.put("missingDataNumber", missingData);
                        mailBody = mailBody + mailTemplate.merge();
                    }
                }


                // メール本文の空の場合は次の建物へ
                if (mailBody.equals("")) {
                    continue;
                }
                // メール本文生成
                String bef = DateUtility.changeDateFormat(
                        DateUtility.conversionDate(targetbefDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                String aft = DateUtility.changeDateFormat(
                        DateUtility.conversionDate(targetaftDate, DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                mailBody = bef + "～" + aft + "の下記装置にデータ欠落があります\n" + mailBody;
                // 建物のアドレスとメール宛先をToとして結合
                // 建物毎のメールアドレス1～10取得
                toAddresses = addAddress(toAddresses, buildInfo.getMail1());
                toAddresses = addAddress(toAddresses, buildInfo.getMail2());
                toAddresses = addAddress(toAddresses, buildInfo.getMail3());
                toAddresses = addAddress(toAddresses, buildInfo.getMail4());
                toAddresses = addAddress(toAddresses, buildInfo.getMail5());
                toAddresses = addAddress(toAddresses, buildInfo.getMail6());
                toAddresses = addAddress(toAddresses, buildInfo.getMail7());
                toAddresses = addAddress(toAddresses, buildInfo.getMail8());
                toAddresses = addAddress(toAddresses, buildInfo.getMail9());
                toAddresses = addAddress(toAddresses, buildInfo.getMail10());
                for (String address : mailList) {
                    toAddresses.add(address);
                }
                // メール送信処理
                // 件名
                String mailSubject = String.format(" SMS 30分値欠落データ報告");
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
                if (!batchMailService.mailSend(osolMailParameter)) {
                    batchLogger.error(this.batchName.concat(" SendTargetOberAlarmMail Failure"));
                }
            }

            batchLogger.info(this.batchName.concat(" End"));

        } catch (Exception ex) {

            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));

        }
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
}
