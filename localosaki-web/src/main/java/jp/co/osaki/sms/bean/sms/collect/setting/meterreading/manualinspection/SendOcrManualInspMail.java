package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsMailService;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseVelocity;

public class SendOcrManualInspMail {
    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    /**
     * リクエストログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    /**
     * 任意検針メール送信
     * @param toAddresses
     * @param buildingInfo
     * @param sendMailList
     * @param svDate
     */
    public void ocrManualInsp(List<String> toAddresses, ListInfo buildingInfo, List<ManualInspectionDataList> sendMailList, String svDate, String dayOfWeek) {


        String buildingName = "-";
        if(!CheckUtility.isNullOrEmpty(buildingInfo.getBuildingName())) {
            buildingName = buildingInfo.getBuildingName();
        }

            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_OCR_MANUAL_INSP,
                    smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("buildingName", buildingName);
            mailTemplate.put("severDate", svDate);
            mailTemplate.put("infoList", sendMailList);
            mailTemplate.put("dayOfWeek", dayOfWeek);
        // 件名
        String mailSubject = "【本日対応】AieLink(OCR) 任意検針実行通知メール";

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
     * 予約検針メール送信
     * @param toAddresses
     * @param buildingInfo
     * @param sendMailList
     * @param reserveDate
     */
    public void ocrReserveInsp(List<String> toAddresses, ListInfo buildingInfo, List<ManualInspectionDataList> sendMailList, String reserveDate, String dayOfWeek) {


        String buildingName = "-";
        if(!CheckUtility.isNullOrEmpty(buildingInfo.getBuildingName())) {
            buildingName = buildingInfo.getBuildingName();
        }

        // 本文テンプレート作成
        BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_OCR_RESERVE_INSP,
                smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
        mailTemplate.put("buildingName", buildingName);
        mailTemplate.put("reserveDate", reserveDate);
        mailTemplate.put("infoList", sendMailList);
        mailTemplate.put("dayOfWeek", dayOfWeek);
        // 件名
        String mailSubject = "【"+ convertDate(reserveDate) + dayOfWeek + "対応】" + "AieLink(OCR) 予約検針通知メール";

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
