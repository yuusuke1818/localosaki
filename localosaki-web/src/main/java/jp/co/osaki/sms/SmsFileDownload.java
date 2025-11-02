package jp.co.osaki.sms;

import javax.ejb.Stateless;
import javax.inject.Inject;

import jp.co.osaki.osol.OsolAwsS3FileTransfer;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseFileDownload;

/**
 *
 * ファイルダウンロードクラス
 *
 * ファイルダウンロードがある画面Beanにて当クラスをinjectして使用する
 *
 * @author take_suzuki
 */
@Stateless
public class SmsFileDownload extends BaseFileDownload {

    @Inject
    private OsolAwsS3FileTransfer osolAwsS3FileTransfer;

    /**
     *
     * S3からのファイルダウンロード処理
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     */
    public void S3fileDownload(String downloadFilePath) {

        osolAwsS3FileTransfer.downloadFileFromS3(downloadFilePath);
    }

    /**
     *
     * S3経由のファイルダウンロード処理
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     * @param saveFilename ダウンロードファイル保存名
     * @return 実行結果 (0:成功 -1:失敗 9:エラー)
     */
    public int S3fileDownload(String downloadFilePath, String saveFilename) {
        if (downloadFilePath == null || saveFilename == null) {
            return BaseConstants.RETURN_CODE.FAILED.getInt();
        }
        osolAwsS3FileTransfer.downloadFileFromS3(downloadFilePath);
        String contentType = this.getMimeType(saveFilename);
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return super.fileDownload(downloadFilePath, saveFilename, contentType);
    }

    /**
     *
     * S3経由ではないファイルダウンロード処理
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     * @param saveFilename ダウンロードファイル保存名
     * @return 実行結果 (0:成功 -1:失敗 9:エラー)
     */
    public int fileDownload(String downloadFilePath, String saveFilename) {
        return fileDownload(downloadFilePath, saveFilename, false);
    }

    /**
     *
     * S3経由ではないファイルダウンロード処理(インライン指定)
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     * @param saveFilename ダウンロードファイル保存名
     * @param flagDispositionInline　インライン指定
     * @return 実行結果 (0:成功 -1:失敗 9:エラー)
     */
    public int fileDownload(String downloadFilePath, String saveFilename, boolean flagDispositionInline) {
        if (downloadFilePath == null || saveFilename == null) {
            return BaseConstants.RETURN_CODE.FAILED.getInt();
        }
        String contentType = this.getMimeType(saveFilename);
        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return super.fileDownload(downloadFilePath, saveFilename, contentType, flagDispositionInline);

    }
}
