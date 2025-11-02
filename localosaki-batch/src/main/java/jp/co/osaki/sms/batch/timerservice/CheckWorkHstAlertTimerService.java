package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConstants.TCOMMAND_SRV_ENT;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchConstants.TCOMMAND_DEV_STA;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.CheckWorkHstAlertDao;
import jp.co.osaki.sms.batch.resultset.DevCommandBuildingMailAddressResultSet;
import jp.co.osaki.sms.batch.resultset.DevCommandDeviceListResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * 処理予約異常メール送信実行 TimerServiceクラス
 *
 * @author akr_iwamoto
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class CheckWorkHstAlertTimerService extends SmsBatchTimerService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5120604513235898160L;

    /**
     * メーター検定満期通知メール実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.
            CHECK_WORKHST_ALERT.getVal();

    /**
     * コマンドwaitの対象メーター件数閾値
     */
    private static final int COMMAND_WAIT_COUNT = 4;

    /**
     * コマンドsendingの対象メーター件数閾値
     */
    private static final int COMMAND_SENDING_COUNT = 3;

    /**
     * メール本文 装置状態：正常復帰
     */
    private static final String MAIL_BODY_DEVSTATE_SUCCESS = "正常復帰";


    /**
     * メール本文 装置状態：異常発生
     */
    private static final String MAIL_BODY_DEVSTATE_FAIL = "異常発生";

    /**
     * DAO
     */
    private CheckWorkHstAlertDao dao;

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
        this.dao = new CheckWorkHstAlertDao(this.entityManager);
        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
//              this.scheduleExpression = scheduleExpressionByCronTab("* * * * * ?",
//                  SCHEDULE_CRON_TYPE);
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
        List<DevCommandDeviceListResultSet> tDevPrmList = null;
        try {
            // 起動日時を取得
            svDate = (Date) dao.getServerDateTime();
            svCal = Calendar.getInstance();
            svCal.setTime(svDate);

            // 装置リスト取得
            tDevPrmList = dao.getMDevPrmOfMUDM2();
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        // 各装置に対して処理実行
        for (DevCommandDeviceListResultSet tDevPrm : tDevPrmList) {
            try {
                String devId = tDevPrm.getDevId();
                MPauseMailResultSet sendMail = this.dao.getDevSendMailFlg(devId);

                if(sendMail != null && (sendMail.getSendFlg() != null &&  sendMail.getSendFlg().equals("1"))) {
                    continue;
                }
                // 装置状態はNULLの可能性がある為、初期値異常とする
                int devSta = TCOMMAND_DEV_STA.FAIL.getVal();
                if (tDevPrm.getDevSta() != null) {
                    devSta = tDevPrm.getDevSta().intValue();
                }
                String devState;
                if (devSta == TCOMMAND_DEV_STA.SUCCESS.getVal()) {
                    // 正常復帰
                    devState = MAIL_BODY_DEVSTATE_SUCCESS;
                } else {
                    // 異常発生
                    devState = MAIL_BODY_DEVSTATE_FAIL;
                }
                String devName = tDevPrm.getName();
                if (devName == null) {
                    // 空の場合は、空文字
                    devName = "";
                }
                String homeDirectory = tDevPrm.getHomeDirectory();
                if (homeDirectory == null || homeDirectory.isEmpty()) {
                    // 空の場合は、--
                    homeDirectory = "--";
                }
                String revFlg = tDevPrm.getRevFlg();
                int flgCount;

                batchLogger.info(String.format("処理予約異常メール送信実行開始("
                        + " dev_id = %s devSta = %s devName = %s homeDirectory = %s revFlg = %s",
                        devId, devSta, devName, homeDirectory, revFlg));

                // 処理フラグ確認件数設定
                if (revFlg == null || revFlg.isEmpty() || TCOMMAND_SRV_ENT.WAIT.getVal().equals(revFlg)) {
                    // waitコマンドの閾値設定
                    flgCount = COMMAND_WAIT_COUNT;
                } else {
                    // sendingコマンドの閾値設定
                    flgCount = COMMAND_SENDING_COUNT;
                }

                // メータ件数取得
                long mMeterCount = dao.getCountMeter(devId);
                batchLogger.trace(String.format("Meter count=%d",mMeterCount));

                long mWaitCount = dao.getWaitWorkHstWaitCount(devId);
                batchLogger.trace(String.format("Wait command count=%d",mWaitCount));

                long mGetSendWaitCount = dao.getGetSendWaitCommandCount(devId);
                batchLogger.trace(String.format("Get/SendWait command count=%d",mGetSendWaitCount));


                // メール送信先を取得
                List<String> toAddresses = getEmailAddress(devId);

                // 送信先がなければ次の装置へ
                if (toAddresses == null || toAddresses.size() == 0) {
                    batchLogger.info("送信対象アドレスがありません。");
                    continue;
                }

                // コマンド送信数の閾値チェック
                if (mWaitCount >= flgCount ||
                        mGetSendWaitCount >= flgCount) {

                    // 本文テンプレート作成
                    BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_CHECK_DEVWORKHST_ALART,
                          smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
                    mailTemplate.put("devName", devName);
                    mailTemplate.put("devId", devId);
                    mailTemplate.put("devState", devState);
                    mailTemplate.put("homeDir", homeDirectory);
                    mailTemplate.put("meterCount", mMeterCount);
                    mailTemplate.put("waitCount", mWaitCount);
                    mailTemplate.put("getsendwaitCount", mGetSendWaitCount);
                    // 件名
                    String mailSubject = String.format("SMSコマンド送信異常通知メール(%s)", devName);

                    // バッチ設定からメール送信フラグ取得
                    boolean useSendMailFlg = false;
                    String useSendStr = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SEND_AVAILABLE);
                    if (useSendStr != null && SmsBatchConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
                        useSendMailFlg = true;
                    }

                    OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                            smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                          new ArrayList<>(), useSendMailFlg);

                    // メール送信
                    SmsBatchMailService batchMailService = new SmsBatchMailService();
                    if (!batchMailService.mailSend(osolMailParameter)) {
                        batchLogger.error(this.batchName.concat(" SendTargetOberAlarmMail Failure"));
                        }
                    batchLogger.info(String.format("処理予約異常メール送信実行終了("
                            + " dev_id = %s devSta = %s devName = %s homeDirectory = %s revFlg = %s",
                            devId, devSta, devName, homeDirectory, revFlg));
                }

            } catch (Exception ex) {
                // ログ出力
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

    /**
     * 対象メールアドレス取得
     * @param devId
     * @return アドレスリスト
     */
    private List<String> getEmailAddress(String devId) {


        // アラート送信先テーブルから宛先を取得
        List<MAlertMailListResultSet> tMAlertMailList = dao.getTargetAddressesForExpiryNotice(devId);

        if (tMAlertMailList == null || tMAlertMailList.size() == 0) {
            // 対象メールアドレス無しの為、終了
            return null;
        }

        List<String> toAddresses = new ArrayList<>();

        for (MAlertMailListResultSet email : tMAlertMailList) {
            if (email.getEmail() != null) {
                toAddresses.add(email.getEmail());
            }
        }

        // 装置建物のメールアドレスを取得
        DevCommandBuildingMailAddressResultSet tDevCommandBuildingMailaddr = dao.getBuildingMailAddrssForDev(devId);
        if (tDevCommandBuildingMailaddr == null) {
            return null;
        }

        // SMS建物メールアドレスをアドレスリストへ追加
        toAddresses = addBuildingSmsEmailAddr(toAddresses, tDevCommandBuildingMailaddr);

        return toAddresses;
    }

    /**
     * SMS建物メールアドレスをアドレスリストへ追加
     * @param devMailAddrList アドレスリスト
     * @param buildingSmsMailAddrList SMS建物メールアドレス情報
     * @return アドレスリスト
     */
    private List<String> addBuildingSmsEmailAddr(List<String> devMailAddrList,
            DevCommandBuildingMailAddressResultSet buildingSmsMailAddrList) {

        // パラメータチェック
        if (devMailAddrList == null) {
            return null;
        }
        if (buildingSmsMailAddrList == null) {
            return devMailAddrList;
        }

        // SMS建物メールアドレスが存在する場合、アドレスリストに設定
        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail1())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail1());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail2())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail2());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail3())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail3());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail4())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail4());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail5())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail5());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail6())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail6());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail7())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail7());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail8())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail8());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail9())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail9());
        }

        if(!CheckUtility.isNullOrEmpty(buildingSmsMailAddrList.getMail10())) {
            devMailAddrList.add(buildingSmsMailAddrList.getMail10());
        }

        return devMailAddrList;
    }

}
