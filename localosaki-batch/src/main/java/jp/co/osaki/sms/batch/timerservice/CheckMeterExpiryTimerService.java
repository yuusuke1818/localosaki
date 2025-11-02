package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchMailService;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.CheckMeterExpiryDao;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeBuildingResultSet;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeDeviceListResultSet;
import jp.co.osaki.sms.batch.resultset.ExpiryNoticeMeterListResultSet;
import jp.co.osaki.sms.batch.resultset.MAlertMailListResultSet;
import jp.co.osaki.sms.batch.resultset.MPauseMailResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * メーター検定満期通知メール実行 TimerServiceクラス
 *
 * @author sagi_h
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CheckMeterExpiryTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 4649339708744977436L;

    /**
     * メーター検定満期通知メール実行
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.CHECK_METER_EXPIRY.getVal();

    /**
     * DAO
     */
    private CheckMeterExpiryDao dao;

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
        this.dao = new CheckMeterExpiryDao(this.entityManager);
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
        List<ExpiryNoticeDeviceListResultSet> tDevPrmList = null;
        try {
            // 起動日時を取得
            svDate = (Date) dao.getServerDateTime();
            svCal = Calendar.getInstance();
            svCal.setTime(svDate);
            // 起動日は1日へ固定
            svCal.set(Calendar.DAY_OF_MONTH, 1);

            // 装置リスト取得
            tDevPrmList = dao.getMDevPrmOfMUDM2();
        } catch (Exception ex) {
            // ログ出力
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return;
        }

        // 各装置に対して処理実行
        for (ExpiryNoticeDeviceListResultSet tDevPrm : tDevPrmList) {
            try {
                String devId = tDevPrm.getDevId();
                String devName = tDevPrm.getName();
                BigDecimal examNoticeMonthBD = tDevPrm.getExamNoticeMonth();
                MPauseMailResultSet sendMail = this.dao.getDevSendMailFlg(devId);

                if(sendMail != null && (sendMail.getSendFlg() != null &&  sendMail.getSendFlg().equals("1"))) {
                    continue;
                }

                if (devName == null) {
                    // 空の場合は、空文字
                    devName = "";
                }

                batchLogger.info(this.batchName
                        .concat(String.format(" 検満通知メール開始( dev_id = %s dev_name = %s exam_notice_month = %s", devId,
                                devName, examNoticeMonthBD)));

                if (examNoticeMonthBD == null) {
                    continue; // 検満通知月(Nヶ月前)が未設定のため、メーター検定満期通知メールを送信しない
                }
                int examNoticeMonth = examNoticeMonthBD.intValue();


                // 装置がある建物を取得
                ExpiryNoticeBuildingResultSet tTBuilding = dao.getBuildingForExpiryNotice(devId);

                // メール送信先を取得
                List<MAlertMailListResultSet> tMAlertMailList = dao.getTargetAddressesForExpiryNotice(devId);

                List<String> toAddresses = new ArrayList<>();
                List<String> bccAddresses = new ArrayList<>();	// BCC送信用Listを追加

                for (MAlertMailListResultSet email : tMAlertMailList) {
                    if (email.getEmail() != null) {
                    //  toAddresses.add(email.getEmail());

                    	if (email.getEmail().matches(".*@osaki\\.co\\.jp")) {
                    		bccAddresses.add(email.getEmail());	//　宛先に「@osaki.co.jp」が含まれるメールはBCCへ
                    	} else {
                    		toAddresses.add(email.getEmail());
                    	}
                    }
                }

                if (toAddresses.size() == 0 && bccAddresses.size() == 0) {	// ToとBccのアドレス有無をチェック
                    batchLogger.info(this.batchName
                            .concat(" 送信対象アドレスがありません。"));
                // 送信先がなければ次の装置へ
                    continue;
                } else {
					if (toAddresses.size() == 0) {
	                    batchLogger.info(this.batchName
	                            .concat(" 送信対象アドレス(TO)がありません。"));
					}
					if (bccAddresses.size() == 0) {
	                    batchLogger.info(this.batchName
	                            .concat(" 送信対象アドレス(BCC)がありません。"));
					}
				}

                // 装置が管理するメーターを取得
                List<ExpiryNoticeMeterListResultSet> tMMeterList = dao.getMetersForExpiryNotice(devId);

                // 通知対象のメーターリスト
                List<ExpiryNoticeMeterListResultSet> metersCloseToExpiry = new ArrayList<>();

                // リスト内各メーターについて検満通知判定
                for (ExpiryNoticeMeterListResultSet meter : tMMeterList) {
                    long meterMngId = meter.getMeterMngId().longValue();
                    String meterId = meter.getMeterId();
                    String examEndYm = meter.getExamEndYm();
                    if (examEndYm.isEmpty()) {
                        continue;
                    }

                    batchLogger.info(this.batchName
                            .concat(String.format(" メーター取得( METER_MNG_ID = %s METER_ID = %s EXAM_END_YM = %s )",
                                    meterMngId, meterId, examEndYm)));

                    // 検満通知年月計算
                    SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMM);
                    Calendar examEndNotifCal = Calendar.getInstance();
                    examEndNotifCal.setTime(sdf.parse(examEndYm));
                    examEndNotifCal.set(Calendar.DAY_OF_MONTH, 1);
                    examEndNotifCal.add(Calendar.MONTH, -1 * examNoticeMonth); // 各装置の通知月だけ戻す

                    batchLogger.info(this.batchName.concat(String.format(" 検満通知日・現在日( EXAM_YMD = %s NOW_YMD = %s )",
                            sdf.format(examEndNotifCal.getTime()), sdf.format(svDate))));

                    // 検満通知月が現時点以前なら通知対象追加
                    if (!examEndNotifCal.after(svCal)) {
                        meter.setExamEndYm(examEndYm.replaceAll("([0-9]{2})$", "/$1")); // 月の前にスラッシュ挿入
                        metersCloseToExpiry.add(meter);
                    }
                }

                // 通知対象がない場合は次の装置の処理へ移行
                if (metersCloseToExpiry.size() == 0) {
                    batchLogger.info(this.batchName.concat(" 検満通知メーターがありません。"));
                    continue;
                }

                // 本文テンプレート作成
                BaseVelocity mailTemplate = new BaseVelocity(SmsBatchConstants.MAIL_TEMPLATE_CHECK_METER_EXPIRY,
                        smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_TEMPLATE_DIR));
                mailTemplate.put("devName", devName);
                mailTemplate.put("devId", devId);
                mailTemplate.put("meterList", metersCloseToExpiry);

                // 件名
                String mailSubject = String.format("SMSメーター検満通知メール(%s)", (tTBuilding == null ? "" : tTBuilding.getBuildingName()));

                // バッチ設定からメール送信フラグ取得
                boolean useSendMailFlg = false;
                String useSendStr = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SEND_AVAILABLE);
                if (useSendStr != null && SmsBatchConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
                    useSendMailFlg = true;
                }

                List<String> regstToAddresses = new ArrayList<>();
                List<String> regstBccAddresses = new ArrayList<>();

                if (toAddresses.size() == 0) {	// TO向けのアドレスが未登録の場合は、BCC（大崎関係者向け）をTOとして送信
                    regstToAddresses = bccAddresses;
                } else {
                    if (bccAddresses.size() == 0) {	// BCC向けのアドレスが未登録の場合は、BCC送信先を空にしてTOのみを送信
                        regstToAddresses = toAddresses;
                 	} else {	// TOもBCCも登録されている場合は、それぞれに送信
                         regstToAddresses = toAddresses;
                         regstBccAddresses = bccAddresses;
                    }
                }
                OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                        smsBatchConfigs.getConfig(SmsBatchConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), regstToAddresses, new ArrayList<>(),
                        regstBccAddresses, useSendMailFlg);

                // メール送信
                SmsBatchMailService batchMailService = new SmsBatchMailService();
                if (!batchMailService.mailSend(osolMailParameter)) {
                    batchLogger.error(this.batchName.concat(" SendTargetOberAlarmMail Failure"));
                }
            	if (toAddresses.size() > 0) {
	                batchLogger.info(this.batchName.concat(String.format(" To: %s", toAddresses.toString())));
				}
				if (bccAddresses.size() > 0) {
	                batchLogger.info(this.batchName.concat(String.format(" Bcc: %s", bccAddresses.toString())));
				}
                batchLogger.info(this.batchName
                        .concat(String.format(" 検満通知メール終了( dev_id = %s dev_name = %s exam_notice_month = %s )", devId,
                                devName, examNoticeMonth)));
            } catch (Exception ex) {
                // ログ出力
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }

        batchLogger.info(this.batchName.concat(" End"));
    }

}
