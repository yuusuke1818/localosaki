package jp.skygroup.enl.webap.base;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

/**
 *
 * SFTPファイルクラス
 *
 * @author take_suzuki
 */
public class BaseSftpFile {

    /**
     * エラー用ログ
     */
    private static final org.jboss.logging.Logger errorLogger = org.jboss.logging.Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    /**
     * 文字コードセット
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * リモートホスト上の指定ディレクトリのファイル一覧を取得する
     *
     * @param hostName
     * @param user
     * @param password
     * @param portNo
     * @param fileNameCharset
     * @param remoteDir
     * @return
     *
     */
    public static List<String> getFileList(String hostName, int portNo, String user, String password, String fileNameCharset, String remoteDir) {

        List<String> fileList = new ArrayList<>();

        try {
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setFileNameEncoding(opts, fileNameCharset);
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            FileSystemManager fsManager = VFS.getManager();
            
            String URL = "sftp://" + URLEncoder.encode(user, CHARSET_NAME);
            URL = URL + ":" + URLEncoder.encode(password, CHARSET_NAME);
            URL = URL + "@" + URLEncoder.encode(hostName, CHARSET_NAME);
            URL = URL + ":" + portNo;
            URL = URL + remoteDir;

            //Directoryチェック
            try (FileObject localFileObject = fsManager.resolveFile(URL)) {
                //Directoryチェック
                if (!localFileObject.getType().equals(FileType.FOLDER)) {
                    localFileObject.close();
                    return fileList;
                }
                //ファイル存在チェック
                if (!localFileObject.getType().hasChildren()) {
                    localFileObject.close();
                    return fileList;
                }
                FileObject[] children = localFileObject.getChildren();
                for (FileObject children1 : children) {
                    if (!children1.getName().getBaseName().isEmpty()) {
                        fileList.add(children1.getName().getBaseName());
                    }
                }
                localFileObject.close();
            }
        } catch (FileSystemException | UnsupportedEncodingException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return fileList;
    }

    /**
     *
     * リモートホスト上の指定パスのファイルを取得する
     *
     * @param hostName
     * @param portNo
     * @param user
     * @param password
     * @param fileNameCharset
     * @param remotePath
     * @param localPath
     * @return
     */
    public static boolean getFile(String hostName, int portNo, String user, String password, String fileNameCharset, String remotePath, String localPath) {

        try {
            File localFile = new File(localPath);
            OutputStream outPutstream = new FileOutputStream(localFile);
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setFileNameEncoding(opts, fileNameCharset);
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            FileSystemManager fsManager = VFS.getManager();

            String URL = "sftp://" + URLEncoder.encode(user, CHARSET_NAME);
            URL = URL + ":" + URLEncoder.encode(password, CHARSET_NAME);
            URL = URL + "@" + URLEncoder.encode(hostName, CHARSET_NAME);
            URL = URL + ":" + portNo;
            URL = URL + remotePath;

            //ファイル存在チェック
            try (FileObject localFileObject = fsManager.resolveFile(URL)) {
                //ファイル存在チェック
                if (!localFileObject.exists()) {
                    localFileObject.close();
                    return false;
                }
                BufferedOutputStream bos;
                try (InputStream inputStream = localFileObject.getContent().getInputStream()) {
                    bos = new BufferedOutputStream(outPutstream);
                    byte[] buffer = new byte[1024];
                    int readLength = 0;
                    while ((readLength = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, readLength);
                    }
                    bos.close();
                } catch (IOException ex) {
                    localFileObject.close();
                    errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                    return false;
                }
            }
            return true;
        } catch (FileNotFoundException | FileSystemException | UnsupportedEncodingException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return false;
    }

    /**
     *
     * リモートホスト上の指定パスのファイルを削除する
     *
     * @param hostName
     * @param portNo
     * @param user
     * @param password
     * @param fileNameCharset
     * @param remotePath
     * @return
     */
    public static boolean delFile(String hostName, int portNo, String user, String password, String fileNameCharset, String remotePath) {

        boolean result = false;
        try {
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            SftpFileSystemConfigBuilder.getInstance().setFileNameEncoding(opts, fileNameCharset);
            SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
            FileSystemManager fsManager = VFS.getManager();

            String URL = "sftp://" + URLEncoder.encode(user, CHARSET_NAME);
            URL = URL + ":" + URLEncoder.encode(password, CHARSET_NAME);
            URL = URL + "@" + URLEncoder.encode(hostName, CHARSET_NAME);
            URL = URL + ":" + portNo;
            URL = URL + remotePath;

            try (FileObject localFileObject = fsManager.resolveFile(URL)) {
                if (localFileObject.exists()) {
                    result = localFileObject.delete();
                }
                localFileObject.close();
            }
        } catch (FileSystemException | UnsupportedEncodingException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
        return result;
    }
}
