package jp.co.osaki.sms.bean.deviceCtrl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsMailService;
import jp.co.osaki.sms.deviceCtrl.resultset.SendConcentratorErrResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.SendMeterErrResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.SendTermErrResultSet;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseVelocity;

public class SendAlertMail {
    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    /**
     * リクエストログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    public void alertMailMeterErr(List<String> toAddresses, TBuilding tBuildingMeterErr, List<SendMeterErrResultSet> sendMeterErrList, MDevPrm mDevPrmMeterErr, List<SendMeterErrResultSet> meterContErrList) {

        List<SendMeterErrResultSet> sendList = new ArrayList<>();
        List<SendMeterErrResultSet> sendContMeterErrList = new ArrayList<>();

        //メーター状態が切り替わったもの
        for(SendMeterErrResultSet sendMeterErrResultSet : sendMeterErrList) {
            SendMeterErrResultSet ret = new SendMeterErrResultSet();
            ret.setMeterMngId(sendMeterErrResultSet.getMeterMngId());
            ret.setMeterId(sendMeterErrResultSet.getMeterId());
            ret.setTenantName(sendMeterErrResultSet.getTenantName());
            ret.setInspDate(sendMeterErrResultSet.getInspDate());
            ret.setMeterType(sendMeterErrResultSet.getMeterType());

            if(sendMeterErrResultSet.getMeterState().equals(DeviceCtrlConstants.zero.toString())) {
                ret.setMeterState(DeviceCtrlConstants.normal);
            }else if(sendMeterErrResultSet.getMeterState().equals(DeviceCtrlConstants.one.toString())) {
                ret.setMeterState(DeviceCtrlConstants.abnormal);
            }
            sendList.add(ret);
        }

        //異常状態のまま
        for(SendMeterErrResultSet sendMeterErrResultSet : meterContErrList) {
            SendMeterErrResultSet ret = new SendMeterErrResultSet();
            ret.setMeterMngId(sendMeterErrResultSet.getMeterMngId());
            ret.setMeterId(sendMeterErrResultSet.getMeterId());
            ret.setTenantName(sendMeterErrResultSet.getTenantName());
            ret.setInspDate(sendMeterErrResultSet.getInspDate());
            ret.setMeterType(sendMeterErrResultSet.getMeterType());
            sendContMeterErrList.add(ret);
        }
        String devName = "-";
        if(!CheckUtility.isNullOrEmpty(mDevPrmMeterErr.getName())) {
            devName = mDevPrmMeterErr.getName();
        }

        String devId = mDevPrmMeterErr.getDevId();
            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_ALERT_METER_EXPIRY,
                    smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devName", devName);
            mailTemplate.put("devId", devId);
            mailTemplate.put("meterList", sendList);
            mailTemplate.put("errMeterList", sendContMeterErrList);
        // 件名
        String mailSubject = String.format(DeviceCtrlConstants.mailMeterTitle+"(%s)", tBuildingMeterErr.getBuildingName());

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }

        OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                smsConfigs.getConfig(SmsConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                new ArrayList<>(), useSendMailFlg);
        // メール送信
        SmsMailService smsMailService = new SmsMailService();
        if (!smsMailService.mailSend(osolMailParameter)) {
            requestLogger.error(this.getClass().getSimpleName().concat(" SendTargetOberAlarmMail Failure"));
        }

    }

    public void alertMailTermErr(List<String> toAddresses, TBuilding tBuildingMeterErr, List<SendTermErrResultSet> sendTermErrList, MDevPrm mDevPrmMeterErr, List<SendTermErrResultSet> termContErrList) {

        List<SendTermErrResultSet> sendList = new ArrayList<>();
        List<SendTermErrResultSet> sendContTermErrList = new ArrayList<>();

        for(SendTermErrResultSet sendTermErrResultSet : sendTermErrList) {
            SendTermErrResultSet ret = new SendTermErrResultSet();
            ret.setMeterMngId(sendTermErrResultSet.getMeterMngId());
            ret.setTenantName(sendTermErrResultSet.getTenantName());
            ret.setInspDate(sendTermErrResultSet.getInspDate());
            ret.setMeterType(sendTermErrResultSet.getMeterType());

            if(sendTermErrResultSet.getTermState().equals(DeviceCtrlConstants.zero.toString())) {
                ret.setTermState(DeviceCtrlConstants.normal);
            }else if(sendTermErrResultSet.getTermState().equals(DeviceCtrlConstants.one.toString())) {
                ret.setTermState(DeviceCtrlConstants.abnormal);
            }
            sendList.add(ret);
        }

        for(SendTermErrResultSet sendTermErrResultSet : termContErrList) {
            SendTermErrResultSet ret = new SendTermErrResultSet();
            ret.setMeterMngId(sendTermErrResultSet.getMeterMngId());
            ret.setTenantName(sendTermErrResultSet.getTenantName());
            ret.setInspDate(sendTermErrResultSet.getInspDate());
            ret.setMeterType(sendTermErrResultSet.getMeterType());
            sendContTermErrList.add(ret);
        }


        String devName = "-";
        if(!CheckUtility.isNullOrEmpty(mDevPrmMeterErr.getName())) {
            devName = mDevPrmMeterErr.getName();
        }
        String devId = mDevPrmMeterErr.getDevId();
            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_ALERT_TERM_EXPIRY,
                    smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devName", devName);
            mailTemplate.put("devId", devId);
            mailTemplate.put("meterList", sendList);
            mailTemplate.put("errMeterList", sendContTermErrList);
        // 件名
        String mailSubject = String.format(DeviceCtrlConstants.mailTermTitle+"(%s)", tBuildingMeterErr.getBuildingName());

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }

        OsolMailParameter osolMailParameter = new OsolMailParameter(mailSubject, mailTemplate.merge(),
                smsConfigs.getConfig(SmsConstants.MAIL_POSTMAIL_OVERRATE_FROMADDRESS), toAddresses, new ArrayList<>(),
                new ArrayList<>(), useSendMailFlg);
        // メール送信
        SmsMailService smsMailService = new SmsMailService();
        if (!smsMailService.mailSend(osolMailParameter)) {
            requestLogger.error(this.getClass().getSimpleName().concat(" SendTargetOberAlarmMail Failure"));
        }

    }

    public void alertMailConcentErr(List<String> toAddresses, TBuilding tBuildingMeterErr, List<SendConcentratorErrResultSet> sendConcentratorErrList, MDevPrm mDevPrmMeterErr, List<SendConcentratorErrResultSet> sendContConcentratorErrList) {

        List<SendConcentratorErrResultSet> retList = new ArrayList<>();
        List<SendConcentratorErrResultSet> retContList = new ArrayList<>();

        for(SendConcentratorErrResultSet sendConcentratorErrResultSet : sendConcentratorErrList) {
            SendConcentratorErrResultSet ret = new SendConcentratorErrResultSet();
            ret.setConcentId(sendConcentratorErrResultSet.getConcentId());

            if(sendConcentratorErrResultSet.getConcentState().equals(DeviceCtrlConstants.zero.toString())) {
                ret.setConcentState(DeviceCtrlConstants.normal);
            }else if(sendConcentratorErrResultSet.getConcentState().equals(DeviceCtrlConstants.one.toString())) {
                ret.setConcentState(DeviceCtrlConstants.abnormal);
            }
            retList.add(ret);
        }

        for(SendConcentratorErrResultSet sendConcentratorErrResultSet : sendContConcentratorErrList) {
            SendConcentratorErrResultSet ret = new SendConcentratorErrResultSet();
            ret.setConcentId(sendConcentratorErrResultSet.getConcentId());
            retContList.add(ret);
        }
        String devName = "-";
        if(!CheckUtility.isNullOrEmpty(mDevPrmMeterErr.getName())) {
            devName = mDevPrmMeterErr.getName();
        }
        String devId = mDevPrmMeterErr.getDevId();
            // 本文テンプレート作成
            BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_ALERT_CONCENT_EXPIRY,
                    smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
            mailTemplate.put("devName", devName);
            mailTemplate.put("devId", devId);
            mailTemplate.put("meterList", retList);
            mailTemplate.put("errMeterList", retContList);
        // 件名
        String mailSubject = String.format(DeviceCtrlConstants.mailConcentTitle+"(%s)", tBuildingMeterErr.getBuildingName());

        // メール送信フラグ取得
        boolean useSendMailFlg = false;
        String useSendStr = smsConfigs.getConfig(SmsConstants.MAIL_SEND_AVAILABLE);
        if (useSendStr != null && SmsConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }

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
     * アドレスを最大10個詰める
     * @param mailList
     * @param ret
     */
    public void addMail(List<String> mailList, MBuildingSms ret) {

        if(!CheckUtility.isNullOrEmpty(ret.getMail1())) {
            mailList.add(ret.getMail1());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail2())) {
            mailList.add(ret.getMail2());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail3())) {
            mailList.add(ret.getMail3());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail4())) {
            mailList.add(ret.getMail4());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail5())) {
            mailList.add(ret.getMail5());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail6())) {
            mailList.add(ret.getMail6());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail7())) {
            mailList.add(ret.getMail7());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail8())) {
            mailList.add(ret.getMail8());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail9())) {
            mailList.add(ret.getMail9());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail10())) {
            mailList.add(ret.getMail10());
        }
    }


}
