package jp.skygroup.enl.webap.base;

import java.io.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jboss.logging.Logger;

/**
 *
 * BaseFTPクライアントクラス

* @author take_suzuki
 */
public class BaseFtpClient extends BaseConstants {
    
    /**
     * イベントログ
     */
    private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());
    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    //FTPクライアント
    private static final FTPClient ftpclient = new FTPClient();

    /**
     *
     * FTPファイルダウンロード処理
     * 
     * @param hostName  ホスト名
     * @param userName  ユーザー
     * @param password  パスワード
     * @param remoteFile リモートファイル名
     * @param localFile ローカルファイルパス
     * @param FileType ファイルタイプ
     * @return
     * @throws Exception
     */
    public static boolean FtpGet(String hostName, String userName, String password, String remoteFile, String localFile, int FileType) throws Exception {

        FileOutputStream ostream = null;
        try {
            //エンコーディングの設定
            //ftpclient.setControlEncoding(ftpclient.getControlEncoding());
            // サーバに接続
            ftpclient.connect(hostName);
            if (!FTPReply.isPositiveCompletion(ftpclient.getReplyCode())) {
                eventLogger.error(ftpclient.getReplyString());
                if (ftpclient.isConnected()){
                    ftpclient.disconnect();
                    eventLogger.info(ftpclient.getReplyString());
                }
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());
            }
            // ログイン
            if (!ftpclient.login(userName, password)) {
                eventLogger.error(ftpclient.getReplyString());
                if (ftpclient.isConnected()){
                    ftpclient.disconnect();
                    eventLogger.info(ftpclient.getReplyString());
                }
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());
            }
            // PASVモードに設定
            ftpclient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpclient.getReplyCode())) {
                eventLogger.error(ftpclient.getReplyString());
                if (ftpclient.isConnected()){
                    ftpclient.disconnect();
                    eventLogger.info(ftpclient.getReplyString());
                }
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());
            }
            //ファイルタイプ設定
            if (!ftpclient.setFileType(FileType)){
                eventLogger.error(ftpclient.getReplyString());
                if (ftpclient.isConnected()){
                    ftpclient.disconnect();
                    eventLogger.info(ftpclient.getReplyString());
                }
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());
            }
            // ファイル受信
            ostream = new FileOutputStream(localFile);
            if (!ftpclient.retrieveFile(remoteFile, ostream)){
                eventLogger.error(ftpclient.getReplyString());
                if (ftpclient.isConnected()){
                    ftpclient.disconnect();
                    eventLogger.info(ftpclient.getReplyString());
                }
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());
            }
            //ログアウト
            if (!ftpclient.logout()){
                eventLogger.error(ftpclient.getReplyString());
                return false;
            } else {
                eventLogger.info(ftpclient.getReplyString());                
            }
            //接続解除
            if (ftpclient.isConnected()){
                ftpclient.disconnect();
                eventLogger.info(ftpclient.getReplyString());
            }
            return true;
        } catch(Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        }
        finally {
            if (ftpclient.isConnected()){
                ftpclient.disconnect();
                eventLogger.info(ftpclient.getReplyString());
            }
            if (ostream != null) {
                try {
                    ostream.close();
                } catch(Exception ex) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                    return false;
                }
            }
        }
    }
}
