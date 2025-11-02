package jp.co.osaki.osol.api.utility.analysis;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolAwsS3FileTransfer;

/**
 * 集計分析EMS ファイルアップロード Utiltiyクラス
 *
 * @author nishida.t
 */
@Named(value = "AnalysisEmsFileUploadUtility")
@ApplicationScoped
public class AnalysisEmsFileUploadUtility {

    @Inject
    private OsolAwsS3FileTransfer osolAwsS3FileTransfer;


    /**
     * S3へのファイルアップロード処理
     *
     * @param tempFilePath
     */
    public void S3fileUpload(String tempFilePath) {

        // ファイル指定チェック
        if (tempFilePath == null) {
            return;
        }

        // S3へのアップロード
        osolAwsS3FileTransfer.uploadFileToS3(tempFilePath, tempFilePath);
    }

    /**
     * S3のファイルダウンロード処理
     *
     * @param tempFilePath
     */
    public void S3fileDownload(String tempFilePath) {

        // ファイル指定チェック
        if (tempFilePath == null) {
            return;
        }

        // S3へのアップロード
        osolAwsS3FileTransfer.downloadFileFromS3(tempFilePath);
    }

}
