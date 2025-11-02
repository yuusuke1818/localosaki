package jp.skygroup.enl.webap.base;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;

/**
 *
 * Baseメールクラス
 *
 * @author take_suzuki
 */
public class BaseMail extends BaseConstants {

    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    //メール送信
    public boolean send(
        String stmpUser,
        String smtpPassword,
        String smtpHost,
        String smtpPort,
        String smtpConnectiontimeout,
        String smtpTimeout,
        String smtpAuth,
        String smtpStarttlsEnable,
        String mailDebug,
        String messageEncoding,
        String messageSubjectCharset,
        String messageContentCharset,
        String messageFrom,
        String messageTo,
        String messageCc,
        String messageBcc,
        String messageSubject,
        String messageContent) {

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpStarttlsEnable);
        props.put("mail.smtp.connectiontimeout", smtpConnectiontimeout);
        props.put("mail.smtp.timeout", smtpTimeout);
        props.put("mail.debug", mailDebug);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
           @Override
           protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(stmpUser, smtpPassword);
           }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(messageFrom));
            message.setReplyTo(new Address[]{new InternetAddress(messageFrom)});
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(messageTo));
            message.setRecipient(Message.RecipientType.CC, new InternetAddress(messageCc));
            message.setRecipient(Message.RecipientType.BCC, new InternetAddress(messageBcc));
            message.setSubject(messageSubject, messageSubjectCharset);
            message.setText(messageContent, messageContentCharset);
            message.setHeader("Content-Transfer-Encoding", messageEncoding);
            Transport.send(message);
        } catch (MessagingException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        }
        return true;
    }
}
