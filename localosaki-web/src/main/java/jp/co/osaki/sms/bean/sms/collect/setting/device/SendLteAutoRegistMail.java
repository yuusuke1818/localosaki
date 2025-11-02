package jp.co.osaki.sms.bean.sms.collect.setting.device;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsMailService;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseVelocity;

public class SendLteAutoRegistMail {
    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    /**
     * リクエストログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    /**
     * LTE端末自動登録メール送信
     * @param toAddresses
     * @param devId
     * @param svDate
     * @param dayOfWeek
     */
    public void lteAutoRegist(List<String> toAddresses, String devId, String svDate, String dayOfWeek) {


        // 本文テンプレート作成
        BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_LTE_AUTO_REGIST,
                smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devId", devId);
            mailTemplate.put("serverDate", svDate);
            mailTemplate.put("dayOfWeek", dayOfWeek);
        // 件名
        String mailSubject = "LTE端末自動登録実行通知メール";

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }
        requestLogger.info(mailTemplate.merge());
        OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                smsConfigs.getConfig(SmsConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                new ArrayList<>(), useSendMailFlg);
        // メール送信
        SmsMailService smsMailService = new SmsMailService();
        if (!smsMailService.mailSend(osolMailParameter)) {
            requestLogger.error(this.getClass().getSimpleName().concat(" SendTargetOberAlarmMail Failure"));
        }

    }

    /**
     * LTE端末配下メーター自動登録メール送信
     * @param toAddresses
     * @param devId
     * @param meterMngId
     * @param meterId
     * @param svDate
     * @param dayOfWeek
     */
    public void lteMeterAdd(List<String> toAddresses, String devId, String meterMngId, String meterId, String svDate, String dayOfWeek) {


        // 本文テンプレート作成
        BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_LTE_METER_ADD,
                smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devId", devId);
            mailTemplate.put("meterMngId", meterMngId);
            mailTemplate.put("meterId",  meterId);
            mailTemplate.put("serverDate", svDate);
            mailTemplate.put("dayOfWeek", dayOfWeek);
        // 件名
        String mailSubject = "LTE端末配下メーター自動登録実行通知メール";

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }
        requestLogger.info(mailTemplate.merge());
        OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                smsConfigs.getConfig(SmsConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                new ArrayList<>(), useSendMailFlg);
        // メール送信
        SmsMailService smsMailService = new SmsMailService();
        if (!smsMailService.mailSend(osolMailParameter)) {
            requestLogger.error(this.getClass().getSimpleName().concat(" SendTargetOberAlarmMail Failure"));
        }

    }

    /**
     * LTE端末配下メーター変更登録メール送信
     * @param toAddresses
     * @param devId
     * @param meterMngId
     * @param newMeterId
     * @param oldMeterId
     * @param svDate
     * @param dayOfWeek
     */
    public void lteMeterChange(List<String> toAddresses, String devId, String meterMngId, String newMeterId, String oldMeterId, String svDate, String dayOfWeek) {


        // 本文テンプレート作成
        BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_LTE_METER_CHANGE,
                smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devId", devId);
            mailTemplate.put("meterMngId", meterMngId);
            mailTemplate.put("newMeterId",  newMeterId);
            mailTemplate.put("oldMeterId", oldMeterId);
            mailTemplate.put("serverDate", svDate);
            mailTemplate.put("dayOfWeek", dayOfWeek);
        // 件名
        String mailSubject = "LTE端末配下メーター変更登録実行通知メール";

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }
        requestLogger.info(mailTemplate.merge());
        OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                smsConfigs.getConfig(SmsConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                new ArrayList<>(), useSendMailFlg);
        // メール送信
        SmsMailService smsMailService = new SmsMailService();
        if (!smsMailService.mailSend(osolMailParameter)) {
            requestLogger.error(this.getClass().getSimpleName().concat(" SendTargetOberAlarmMail Failure"));
        }

    }

    /**
     *
     * @param reserveDate
     * @return
     */
    private String convertDate(String reserveDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = dateFormat.parse(reserveDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateFormat = new SimpleDateFormat("MM月dd日");
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
