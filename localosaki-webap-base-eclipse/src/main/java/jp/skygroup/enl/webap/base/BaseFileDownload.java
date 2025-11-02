package jp.skygroup.enl.webap.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.jboss.logging.Logger;

/**
 *
 * Baseファイルダウンロードクラス
 *
 * @author take_suzuki
 */
public class BaseFileDownload extends BaseConstants {

    /**
     * イベントログ
     */
    private final static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    /**
     * エラー用ログ
     */
    private final static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

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
    private void output(File file, OutputStream os) throws IOException {
        byte buffer[] = new byte[4096];
        try (FileInputStream fis = new FileInputStream(file)) {
            int size;
            while ((size = fis.read(buffer)) != -1) {
                os.write(buffer, 0, size);
            }
        }
    }
    
    /**
     *
     * ファイルダウンロード処理
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     * @param saveFilename ダウンロードファイル保存名
     * @param contentType　ファイルタイプ
     * @return 実行結果 (0:成功 -1:失敗 9:エラー)
     */
    protected int fileDownload(String downloadFilePath, String saveFilename, String contentType) {
        return fileDownload(downloadFilePath, saveFilename, contentType, false);
    }

    /**
     *
     * ファイルダウンロード処理
     *
     * @param downloadFilePath ダウンロード対象ファイルパス(絶対パス)
     * @param saveFilename ダウンロードファイル保存名
     * @param contentType　ファイルタイプ
     * @param flagDispositionInline　インライン指定
     * @return 実行結果 (0:成功 -1:失敗 9:エラー)
     */
    protected int fileDownload(String downloadFilePath, String saveFilename, String contentType, boolean flagDispositionInline) {

        //ログ出力
        eventLogger.info(this.getClass().getName().concat(" Start ").concat(downloadFilePath).concat(" > ").concat(saveFilename).concat(" > ").concat(contentType).concat(" > ").concat(String.valueOf(flagDispositionInline)));

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {
            // ダウンロード対象ファイルのFileオブジェクトを生成
            File file = new File(downloadFilePath);
            if (!file.exists() || !file.isFile()) {
                return RETURN_CODE.FAILED.getInt();
            }
            // レスポンスオブジェクトのヘッダー情報を設定
            ec.responseReset();
            ec.setResponseContentType(contentType);
            if(flagDispositionInline){
                ec.setResponseHeader("Content-Disposition","inline; "
                    .concat("filename*=UTF-8''".concat(URLEncoder.encode(saveFilename, StandardCharsets.UTF_8.name()))));
            }else{
                ec.setResponseHeader("Content-Disposition","attachment; "
                    .concat("filename*=UTF-8''".concat(URLEncoder.encode(saveFilename, StandardCharsets.UTF_8.name()))));
            }
            ec.setResponseContentLength((int) file.length());
            output(file, ec.getResponseOutputStream());
            fc.responseComplete();
            //ログ出力
            eventLogger.info(this.getClass().getName().concat(" End ").concat(downloadFilePath).concat(" > ").concat(saveFilename).concat(" > ").concat(contentType).concat(" > ").concat(String.valueOf(flagDispositionInline)));
            return RETURN_CODE.SUCCESS.getInt();
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return RETURN_CODE.EXCEPTION.getInt();
        }
    }
    
}
