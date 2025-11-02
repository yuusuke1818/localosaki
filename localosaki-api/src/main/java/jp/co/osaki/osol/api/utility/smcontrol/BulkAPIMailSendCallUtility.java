package jp.co.osaki.osol.api.utility.smcontrol;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiMailService;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseVelocity;

/**
 * BulkAPIMailSendCallUtilityクラス
 *
 * @author takemura
 */
@Named(value = "BulkAPIMailSendCallUtility")
@ApplicationScoped
public class BulkAPIMailSendCallUtility {

    private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Inject
    private OsolConfigs osolConfigs;

    @EJB
    private BulkAPIMailSendDao dao;

    /**
     * @param : targetList各レコードに全ての値が設定されていること
     * @param : 呼び元の実行APIID
     */

    public void bulkMailSend(List<SmControlVerocityResult> targetList, String command) throws Exception {
        bulkMailSend(targetList, command, null);
    }
    public void bulkMailSend(List<SmControlVerocityResult> targetList, String command, String temperatureCondition) throws Exception {

        // メールアドレス,建物番号,建物名取得
        List<SmControlVerocityResult> recordList = dao.selectMailBody(targetList);

        // 建物番号, 機器ID順でソート
        recordList = recordList.stream()
                .sorted(Comparator.comparing(SmControlVerocityResult::getBuildingNo, Comparator.naturalOrder())
                        .thenComparing(SmControlVerocityResult::getSmId, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        // Velosityテンプレートファイルのクラス
        BaseVelocity bulkTempVm = new BaseVelocity(SmControlConstants.BULK_MAIL_BODY , SmControlConstants.TEMPLATE_DIR);

        // 一括機器制御_実行者情報用
        SmControlVerocityResult templateBody = recordList.get(0);

        // メール送信用に時間取得
        Calendar cTime = Calendar.getInstance();

        //フォーマットを設定
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        templateBody.setExecuteDate(sdf.format(cTime.getTime()));

        // 結果件数を設定
        int allCount = 0;
        int okCount = 0;
        int ngCount = 0;
        int noChangeCount = 0;
        for (SmControlVerocityResult target : targetList) {
            if (SmControlConstants.MAIL_SETTING_OK.equals(target.getResult())) {
                okCount++;
            } else if (SmControlConstants.MAIL_SETTING_NG.equals(target.getResult())) {
                ngCount++;
            } else if (SmControlConstants.MAIL_SETTING_NO_CHANGE.equals(target.getResult())) {
                noChangeCount++;
            }
            allCount++;
        }

        // テンプレートファイル内の変数にJava変数を格納
        bulkTempVm.put(SmControlConstants.MAIL_TEMPLATE_BODY,templateBody);
        bulkTempVm.put(SmControlConstants.MAIL_RECORD_LIST, recordList);
        bulkTempVm.put(SmControlConstants.MAIL_COMMAND, command);
        bulkTempVm.put(SmControlConstants.MAIL_TEMPERATURE_CONDITION, temperatureCondition);
        bulkTempVm.put(SmControlConstants.MAIL_ALL_COUNT, allCount);
        bulkTempVm.put(SmControlConstants.MAIL_OK_COUNT, okCount);
        bulkTempVm.put(SmControlConstants.MAIL_NG_COUNT, ngCount);
        bulkTempVm.put(SmControlConstants.MAIL_NO_CHANGE_COUNT, noChangeCount);

        // テンプレートファイル内、変数置換後のメール本文
        String bulkMailBody = bulkTempVm.merge();

        // 設定ファイル読み込み
        Properties prop = new Properties();
        try(FileReader mailSettingFile = new FileReader(SmControlConstants.BULK_MAIL_SETTING)){
            prop.load(mailSettingFile);
        }

        // コマンドにより件名を選択
        List<String> subjectList =  buildCsvDataToList(prop.getProperty("bulkMail.subject"));
        String subject = null;		// 件名
        if(SmControlConstants.BULK_TEMPERATURE_COMMAND.equals(command)) {
            subject = subjectList.get(0);	// 温度設定
        }else if(SmControlConstants.BULK_TARGET_POWER_COMMAND.equals(command)) {
            subject = subjectList.get(1);	// 目標電力
        }else if(SmControlConstants.BULK_SCHEDULE_COMMAND.equals(command)) {
            subject = subjectList.get(2);	// スケジュール
        }

        String mailFrom =  prop.getProperty("bulkMail.mailFrom");

        // 宛先リスト作成
        List<String> toList =Arrays.asList(templateBody.getMailAddress());

        List<String> ccList = buildCsvDataToList(prop.getProperty("bulkMail.cc"));
        List<String> bccList = buildCsvDataToList(prop.getProperty("bulkMail.bcc"));

        //
        if(SmControlConstants.BULK_TEMPERATURE_COMMAND.equals(command)) {
            // 温度設定
            String _regex = "冷暖房";
            String _replacement = SmControlConstants.TEMP_CONTROL.equals(temperatureCondition) ? "温度制御"
                                : SmControlConstants.COOLER.equals(temperatureCondition) ? "冷房"
                                : SmControlConstants.HEATER.equals(temperatureCondition) ? "暖房"
                                : "";
            subject = subject.replaceAll(_regex, _replacement);
            bulkMailBody = bulkMailBody.replaceAll(_regex, _replacement);
        }

        // debugログ
        eventLogger.debug("件名 = " + subject);
        eventLogger.debug("メール本文 = " + bulkMailBody);
        eventLogger.debug("差出人 = " + mailFrom);
        eventLogger.debug("宛先 = " + toList);
        eventLogger.debug("cc = " + ccList);
        eventLogger.debug("bcc = " + bccList);

        // メール送信フラグ
        String useSendStr = osolConfigs.getConfig("mail.send.available");
        boolean useSendMailFlg = false;
        if (useSendStr != null && OsolConstants.MAIL_USE_FLG_TRUE.equals(useSendStr)) {
            useSendMailFlg = true;
        }

        // メールAPI呼出
        OsolMailParameter mailParameter = new OsolMailParameter(subject, bulkMailBody, mailFrom, toList, ccList, bccList, useSendMailFlg);
        OsolApiMailService osolMailService = new OsolApiMailService();
        if(!osolMailService.mailSend(mailParameter)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            eventLogger.errorf("[%s][%s](%s) BULK_MAIL_FAILD body=%s",
                    st.getClassName(), st.getMethodName(), st.getLineNumber(), bulkMailBody);
        }
    }

    /**
     * カンマ区切りの設定値をList化
     *
     * @param csv
     * @return
     */
    private List<String> buildCsvDataToList(String csv){
        return csv==null? null : Arrays.asList(csv.split(","));
    }

}
