package jp.co.osaki.osol.api.utility.smcontrol;

import java.io.File;
import java.io.FileOutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.SystemUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolAwsS3FileTransfer;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * ファイルアップロード Utiltiyクラス
 *
 * @author t_hayama
 */
@Named(value = "FileUploadUtility")
@ApplicationScoped
public class FileUploadUtility {

    /**
     * エラー用ログ
     */
    private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    @Inject
    private OsolAwsS3FileTransfer osolAwsS3FileTransfer;


    /**
     * S3へのファイルアップロード
     *
     * @param tempFilePath
     */
    public void S3fileUpload(String tempFilePath) {

        //ファイル指定チェック
        if (tempFilePath == null) {
            return;
        }

        //S3へのアップロード
        osolAwsS3FileTransfer.uploadFileToS3(tempFilePath, tempFilePath);
    }

    /**
     * ファイルアップロード
     *
     * @param outputDir
     * @param outputFileName
     * @param data
     * @param dataSize
     * @return
     */
    public String uploadFile(String outputDir, String outputFileName, byte[] data, int dataSize) {
        String ret = null;
        String outpitFilePath = null;
        FileOutputStream outputStream = null;

        // 出力先ディレクトリを確認
        File outputDirPath = new File(outputDir);
        if (!outputDirPath.exists()) {
            if (!outputDirPath.mkdirs()) {
                errorLogger.error("mkdir -p error ".concat(outputDirPath.toString()));
            }
        }

        outpitFilePath = outputDir.concat(File.separator).concat(outputFileName);
        try {
            outputStream = new FileOutputStream(outpitFilePath);
            outputStream.write(data, 0, dataSize);
            outputStream.close();
            ret = outpitFilePath;
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }

        return ret;
    }

    /**
     * AielMasterログ アップロードディレクトリ取得
     *
     * @param fileInfo
     * @return
     */
    public String getAielMasterLogUploadDir(AielMasterLogFileUtility fileInfo) {
        String ret = null;
        String reqDateTime = null;
        String dirYear = null;
        String dirMonth = null;
        String dirDay = null;

        if (fileInfo == null) {
            return ret;
        }

        // 要求日付から年月日を抽出
        reqDateTime = fileInfo.getReqDayTime();
        dirYear = "20" + reqDateTime.substring(0, 2);
        dirMonth = reqDateTime.substring(2, 4);
        dirDay = reqDateTime.substring(4, 6);

        ret = getUploadDir().
                concat(dirYear).concat(File.separator).
                concat(dirMonth).concat(File.separator).
                concat(dirDay);

        return ret;
    }

    /**
     * AielMasterログ ファイル名取得
     *
     * @param fileInfo
     * @return
     */
    public String getAielMasterLogFileName(AielMasterLogFileUtility fileInfo) {
        String ret = null;

        if (fileInfo == null) {
            return ret;
        }

        ret = String.valueOf(fileInfo.getSmId()).concat("_").
                concat(fileInfo.getReqDayTime()).concat("_").
                concat(fileInfo.getResDayTime()).concat("_").
                concat(fileInfo.getCommand()).concat(".").
                concat(fileInfo.getExtension());

        return ret;
    }

    /**
     * 実行環境のOSを判定して、適切なアップロードディレクトリを取得
     *
     * @return アップロードディレクトリ
     */
    private String getUploadDir() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return SmControlConstants.AIELMASTER_COLLECT_LOG_UPLOAD_DIR_WIN;
        } else {
            return SmControlConstants.AIELMASTER_COLLECT_LOG_UPLOAD_DIR;
        }
    }

}
