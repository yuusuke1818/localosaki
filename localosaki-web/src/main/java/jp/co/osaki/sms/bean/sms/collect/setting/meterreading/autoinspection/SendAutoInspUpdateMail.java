package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.autoinspection;

import java.util.ArrayList;
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

public class SendAutoInspUpdateMail {
    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    /**
     * リクエストログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    public void autoInspSettingUpdate(List<String> toAddresses, ListInfo buildingInfo, List<AutoInspectionEditData> mailEditDataList) {

        ArrayList<AutoInspectionEditData> cloneEditDataList = new ArrayList<>(mailEditDataList);
        for(int i=0;i<cloneEditDataList.size();i++) {
            if(CheckUtility.isNullOrEmpty(cloneEditDataList.get(i).getInspectionMonthDisp())) {
                cloneEditDataList.get(i).setInspectionMonthDispMail("-");
            }
            if(cloneEditDataList.get(i).getInspectionDay().equals("0")) {
                cloneEditDataList.get(i).setInspectionDay("--");
            }
        }

        // 本文テンプレート作成
        BaseVelocity mailTemplate = new BaseVelocity(SmsConstants.MAIL_TEMPLATE_AUTO_INSP_SETTING_UPDATE,
                smsConfigs.getConfig(SmsConstants.MAIL_TEMPLATE_DIR));
        mailTemplate.put("corpName", buildingInfo.getCorpName());
        mailTemplate.put("corpId", buildingInfo.getCorpId());
        mailTemplate.put("buildingName", buildingInfo.getBuildingName());
        mailTemplate.put("buildingNo", buildingInfo.getBuildingNo());
        mailTemplate.put("recordList", cloneEditDataList);
        String subjectName = buildingInfo.getCorpName() + "_" + buildingInfo.getBuildingName();
        // 件名
        String mailSubject = String.format(SmsConstants.AUTO_INSP_SETTING_UPDATE+"(%s)", subjectName);

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
}
