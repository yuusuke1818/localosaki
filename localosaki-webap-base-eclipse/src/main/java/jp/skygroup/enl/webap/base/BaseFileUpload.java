package jp.skygroup.enl.webap.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.servlet.http.Part;
import org.jboss.logging.Logger;

/**
 *
 * Baseファイルアップロードクラス
 *
 * @author take_suzuki
 */
public class BaseFileUpload extends BaseConstants {

    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     *
     * 一時ディレクトリ削除処理
     * 
     * @param tempDir         一時ディレクトリ
     * @return true:成功 false:失敗
     */
    protected boolean tempDirDelete(String tempDir) {

        try {
            this.fileDelete(tempDir);
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        }

        return true;
    }
    /**
     *
     * ファイルアップロード
     * 
     * @param filePart          アップロードファイル情報
     * @param upLoadDir         アップロードディレクトリ
     * @param upLoadFileName    アップロードファイル
     * @return アップロード先の物理ファイルパス
     */
    protected String fileUpload(Part filePart, String upLoadDir, String upLoadFileName) {

        String outpitFilePath = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {

            File outputDir = new File(upLoadDir);
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()){
                    errorLogger.error("mkdir -p error ".concat(outputDir.toString()));
                    return null;
                }
            }

            inputStream = filePart.getInputStream();
            outpitFilePath = upLoadDir.concat(File.separator).concat(upLoadFileName);
            outputStream = new FileOutputStream(outpitFilePath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception ex) {
            outpitFilePath = null;
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        return outpitFilePath;
    }
    /**
     *
     * ファイルコピー
     * 
     * @param beforeFilePath   コピー元ファイル名
     * @param afterDir         コピー先ディレクトリ
     * @param afterFileName    コピー先ファイル
     * @return コピー先の物理ファイルパス
     */
    protected String fileCopy(String beforeFilePath, String afterDir, String afterFileName) {

        try {

            File createDir = new File(afterDir);
            if (!createDir.exists()) {
                if (!createDir.mkdirs()){
                    errorLogger.error("mkdir -p error ".concat(createDir.toString()));
                    return null;
                }
            }
            File beforeFile = new File(beforeFilePath);
            File afterFile = new File(afterDir.concat(File.separator).concat(afterFileName));
            this.copyFile(beforeFile, afterFile);
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return null;
        }

        return afterDir.concat(File.separator).concat(afterFileName);
    }
    /**
     * ファイル名取得
     *
     * @param part ファイルのクラス
     * @return ファイル名
     */
    public String getFileName(Part part) {
        
        return part.getSubmittedFileName().substring(part.getSubmittedFileName().lastIndexOf(File.separator)+1);
    }
    /**
     * ファイル名取得
     *
     * @param FilePath ファイルパス
     * @return ファイル名
     */
    public String getFileName(String FilePath) {
        
        return FilePath.substring(FilePath.lastIndexOf(File.separator)+1);
    }
    /**
     *
     * ファイル拡張子取得
     * 
     * @param fileName
     * @return
     */
    protected String getExtension(String fileName) {
        //ファイル名あり
        if (fileName != null) {
            //文字数チェック
            if (fileName.length() >= 2){
                int point = fileName.lastIndexOf(".");
                if (point > -1 && point < (fileName.length() - 1)) {
                    return fileName.substring(point + 1);
                }
            }
        }
        return STR_EMPTY;
    }
    /**
     * ファイル/ディレクトリ削除
     *
     * @param filePath
    */
    public void fileDelete(String filePath)
    {
        File deleteFile = new File(filePath);
        if(!deleteFile.exists()) {
            return;
        }
        if(deleteFile.isFile()) {
            deleteFile.delete();
        } else if(deleteFile.isDirectory()){
            File[] files = deleteFile.listFiles();
            for (File file1 : files) {
                fileDelete(file1.getPath());
            }
            deleteFile.delete();
         }
     }
    /**
     * ファイルコピー
     *
     * @param inFile ファイルパス
     * @param outFile
     * @throws java.lang.Exception
     */
    private void copyFile(File inFile, File outFile) throws Exception {
        FileInputStream fis  = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            fis.close();
            fos.close();
        }
    }

}
