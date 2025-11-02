package jp.co.osaki.osol.api;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.mail.OsolMailInterface;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

public class OsolApiMailService implements OsolMailInterface, Serializable {

    private static final long serialVersionUID = 7000241420552256015L;

    /**
     * エラー用ログ
     */
    static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * イベント用ログ
     */
    static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Override
    public boolean mailSend(OsolMailParameter parameter) {
        boolean ret = false;

        // メール送信フラグ
        if (!parameter.isSendAvailable()) {
            errorLogger.debug(this.getClass().getName() + " MAIL_USE_FLG IS FALSE ");
            return true;
        }

        // バリデーション
        if (!checkParameter(parameter)) {
            return false;
        }

        Properties props = new Properties();

        OsolConfigs osolConfigs = new OsolConfigs();
        osolConfigs.initConfigs();
        String mailHost = osolConfigs.getConfig("mail.postmail.host");
        // メールサーバ
        props.put("mail.smtp.host", mailHost);

        try {
            Session session = Session.getInstance(props, null);

            Message msg = new MimeMessage(session);
            int addressListIndex = 0;

            // 送信元
            msg.setFrom(new InternetAddress(parameter.getMailFrom()));

            // TO
            if (parameter.getToAddresses() != null && !parameter.getToAddresses().isEmpty()) {
                InternetAddress[] toAddressList = new InternetAddress[parameter.getToAddresses().size()];
                for (String toAddress : parameter.getToAddresses()) {
                    toAddressList[addressListIndex] = new InternetAddress(toAddress);
                    addressListIndex++;
                }
                msg.setRecipients(Message.RecipientType.TO, toAddressList);
            }

            // CC
            if (parameter.getCcAddresses() != null && !parameter.getCcAddresses().isEmpty()) {
                addressListIndex = 0;
                InternetAddress[] ccAddressList = new InternetAddress[parameter.getCcAddresses().size()];
                for (String ccAddress : parameter.getCcAddresses()) {
                    ccAddressList[addressListIndex] = new InternetAddress(ccAddress);
                    addressListIndex++;
                }
                msg.setRecipients(Message.RecipientType.CC, ccAddressList);
            }

            // BCC
            if (parameter.getBccAddresses() != null && !parameter.getBccAddresses().isEmpty()) {
                addressListIndex = 0;
                InternetAddress[] bccAddressList = new InternetAddress[parameter.getBccAddresses().size()];
                for (String bccAddress : parameter.getBccAddresses()) {
                    bccAddressList[addressListIndex] = new InternetAddress(bccAddress);
                    addressListIndex++;
                }
                msg.setRecipients(Message.RecipientType.BCC, bccAddressList);
            }

            // 件名
            msg.setSubject(parameter.getSubject());
            // 送信日時
            msg.setSentDate(new Date());
            // 本文
            msg.setText(parameter.getContent());

            // メール送信
            Transport.send(msg);

            ret = true;
        } catch (SendFailedException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));

            eventLogger.error("SendFailedException ValidSentAddresses:" + e.getValidSentAddresses());
            eventLogger.error("SendFailedException ValidUnsentAddresses:" + e.getValidUnsentAddresses());
            eventLogger.error("SendFailedException InvalidAddresses:" + e.getInvalidAddresses());

            return false;
        } catch (MessagingException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return false;
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return false;
        }

        return ret;
    }

    /**
     * パラメータチェック
     *
     * @param parameter メール送信用パラメータクラス
     *
     * @return true:結果OK、false:結果NG
     */
    private boolean checkParameter(OsolMailParameter parameter) {
        boolean ret = true;

        // 必須項目チェック
        // 件名
        if (CheckUtility.isNullOrEmpty(parameter.getSubject())) {
            eventLogger.error(this.getClass().getName() + " Validation Error Subject Is Null");
            ret = false;
        }

        // 本文
        if (CheckUtility.isNullOrEmpty(parameter.getContent())) {
            eventLogger.error(this.getClass().getName() + " Validation Error Content Is Null");
            ret = false;
        }

        // 送信元
        if (CheckUtility.isNullOrEmpty(parameter.getMailFrom())) {
            eventLogger.error(this.getClass().getName() + " Validation Error MailFrom Is Null");
            ret = false;
        }

        // 宛先リスト,CCリスト,BCCリスト全部 空の場合エラーとなる
        if ((parameter.getToAddresses() == null || parameter.getToAddresses().isEmpty())
                && (parameter.getCcAddresses() == null || parameter.getCcAddresses().isEmpty())
                && (parameter.getBccAddresses() == null || parameter.getBccAddresses().isEmpty())) {
            eventLogger.error(this.getClass().getName()
                    + " Validation Error ToAddresses And CcAddresses And BccAddresses All Empty");
            ret = false;
        }

        return ret;
    }

}
