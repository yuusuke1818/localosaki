package jp.co.osaki.sms.batch;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.mail.OsolMailAttachmentFileInterface;
import jp.co.osaki.osol.mail.OsolMailInterface;
import jp.co.osaki.osol.mail.OsolMailParameter;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * Osolメール送信クラス(Batch用)
 *
 * @author d-komatsubara
 *
 */
public class SmsBatchMailService implements OsolMailInterface, OsolMailAttachmentFileInterface, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9074103037859875622L;

    /**
     * エラー用ログ
     */
    static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * バッチログ
     */
    protected static Logger batchLogger = Logger.getLogger(LOGGER_NAME.BATCH.getVal());

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

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

        String mailHost = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_POSTMAIL_HOST);
        String mailSubjectPf = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SUBJECT_RELEASE_PF);
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
            if (mailSubjectPf != null && !mailSubjectPf.isEmpty()) {
                msg.setSubject(mailSubjectPf + parameter.getSubject());
            } else {
                msg.setSubject(parameter.getSubject());
            }
            // 送信日時
            msg.setSentDate(new Date());
            // 本文
            msg.setText(parameter.getContent());

            // メール送信
            Transport.send(msg);

            ret = true;
        } catch (SendFailedException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));

            batchLogger.error("SendFailedException ValidSentAddresses:" + e.getValidSentAddresses());
            batchLogger.error("SendFailedException ValidUnsentAddresses:" + e.getValidUnsentAddresses());
            batchLogger.error("SendFailedException InvalidAddresses:" + e.getInvalidAddresses());

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
     * メール送信（添付ファイルあり）
     *
     * @param parameter メール送信用パラメータクラス
     * @param attachmentFile 添付ファイルの実体
     * @return true：正常、false：異常
     */
    @Override
    public boolean sendMail(OsolMailParameter parameter, File attachmentFile) {
        batchLogger.info("メール送信処理 = " + this.getClass().getSimpleName());
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

        String mailHost = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_POSTMAIL_HOST);
        String mailSubjectPf = smsBatchConfigs.getConfig(OsolBatchConstants.MAIL_SUBJECT_RELEASE_PF);
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
            if (mailSubjectPf != null && !mailSubjectPf.isEmpty()) {
                msg.setSubject(mailSubjectPf + parameter.getSubject());
            } else {
                msg.setSubject(parameter.getSubject());
            }

            // 送信日時
            msg.setSentDate(new Date());

            MimeMultipart mmp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();

            // 本文
            mbp.setText(parameter.getContent());
            mmp.addBodyPart(mbp);

            // 添付ファイル
            if (attachmentFile != null) {
//                batchLogger.info(attachmentFile.getAbsolutePath());
//                batchLogger.info(attachmentFile.getCanonicalPath());
//                batchLogger.info(attachmentFile.getName());
//                batchLogger.info(attachmentFile.isFile());
                MimeBodyPart mbp2 = new MimeBodyPart();

                FileDataSource fds = new FileDataSource(attachmentFile);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
                mmp.addBodyPart(mbp2);
            }
            msg.setContent(mmp);

            // メール送信
            Transport.send(msg);
            ret = true;
        } catch (SendFailedException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));

            errorLogger.error("SendFailedException ValidSentAddresses:" + e.getValidSentAddresses());
            errorLogger.error("SendFailedException ValidUnsentAddresses:" + e.getValidUnsentAddresses());
            errorLogger.error("SendFailedException InvalidAddresses:" + e.getInvalidAddresses());

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
            batchLogger.error(this.getClass().getName() + " Validation Error Subject Is Null");
            ret = false;
        }

        // 本文
        if (CheckUtility.isNullOrEmpty(parameter.getContent())) {
            batchLogger.error(this.getClass().getName() + " Validation Error Content Is Null");
            ret = false;
        }

        // 送信元
        if (CheckUtility.isNullOrEmpty(parameter.getMailFrom())) {
            batchLogger.error(this.getClass().getName() + " Validation Error MailFrom Is Null");
            ret = false;
        }

        // 宛先リスト,CCリスト,BCCリスト全部 空の場合エラーとなる
        if ((parameter.getToAddresses() == null || parameter.getToAddresses().isEmpty())
                && (parameter.getCcAddresses() == null || parameter.getCcAddresses().isEmpty())
                && (parameter.getBccAddresses() == null || parameter.getBccAddresses().isEmpty())) {
            batchLogger.error(this.getClass().getName()
                    + " Validation Error ToAddresses And CcAddresses And BccAddresses All Empty");
            ret = false;
        }

        return ret;
    }

}
