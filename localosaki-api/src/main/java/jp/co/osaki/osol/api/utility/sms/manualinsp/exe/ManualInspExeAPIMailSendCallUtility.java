package jp.co.osaki.osol.api.utility.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiMailService;
import jp.co.osaki.osol.api.result.sms.manualinsp.exe.ManualInspExeMeterResult;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * ManualInspExeAPIMailSendCallUtilityクラス
 *
 * @author takemura
 */
@Named(value = "ManualInspExeAPIMailSendCallUtility")
@ApplicationScoped
public class ManualInspExeAPIMailSendCallUtility {
    private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Inject
    private OsolConfigs osolConfigs;

    public void manulInspExeMailSend(
            String buildingName, String devName, String devId, List<String> mailList,
            Long inspMonthNo, List<ManualInspExeMeterResult> meterList,
            String targetDate) {
        // 件名
        String subject = null;
        subject = "SMSメーター任意検針通知メール(" + buildingName + ")";

        // 本文
        // Velosityテンプレートファイルのクラス
        BaseVelocity tempVm = new BaseVelocity("ManualInspExe.vm", "/home/wildfly/osol/template");
        // テンプレートファイル内の変数にJava変数を格納
        tempVm.put("dev_name", devName);
        tempVm.put("dev_id", devId);
        tempVm.put("recordList", meterList);
        tempVm.put("insp_month_no", inspMonthNo);
        tempVm.put("target_date", targetDate);
        // テンプレートファイル内、変数置換後のメール本文
        String mailBody = tempVm.merge();

        // メール送信フラグ
        String useSendStr = osolConfigs.getConfig("mail.send.available");
        boolean useSendMailFlg = false;
        if (useSendStr != null && OsolConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }

        // 送信元
        String mailFrom = "alert@osaki-sms.com";

        // メールAPI呼出
        OsolMailParameter mailParameter = new OsolMailParameter(
                subject, mailBody, mailFrom,
                mailList, new ArrayList<>(), new ArrayList<>(), useSendMailFlg);
        OsolApiMailService osolMailService = new OsolApiMailService();
        if (!osolMailService.mailSend(mailParameter)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            eventLogger.errorf("[%s][%s](%s) BULK_MAIL_FAILD body=%s",
                    st.getClassName(), st.getMethodName(), st.getLineNumber(), mailBody);
        }
    }

}
