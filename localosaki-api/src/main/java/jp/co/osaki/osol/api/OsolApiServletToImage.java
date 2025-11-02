package jp.co.osaki.osol.api;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;

/**
*
* OSOL API サーブレット共通クラス ContentType:image/png ContentType:image/jpeg
*
* @author take_suzuki
*
*/
@WebServlet(name = "OsolApiServletToImage", urlPatterns = {
"/image" }, description = "レスポンスボディがイメージファイルのストリームとなるサーブレット", displayName = "OSOL API IMAGE Servlet")
public final class OsolApiServletToImage extends OsolApiServlet {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 6429367876544872145L;

    /**
     *  レスポンス設定
     *  @param response HttpServletResponse
     *  @param filePath レスポンスとして出力するイメージファイルのフルパス
     */
    @Override
    protected final void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse) {

        if (apiResponse instanceof OsolApiResponse) {
            if (((OsolApiResponse<?>)apiResponse).getImageFilePath() == null ||
                    ((OsolApiResponse<?>)apiResponse).getImageFilePath().isEmpty()) {
                return;
            }
        }
        String filePath = ((OsolApiResponse<?>)apiResponse).getImageFilePath();
        if (filePath.endsWith(".png")) {
            response.setContentType("image/png");
        } else {
            response.setContentType("image/jpeg");
        }
        // 存在チェック
        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                // バイナリを出力
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filePath));
                byte[] data = new byte[1024];
                int len;
                while ((len = stream.read(data, 0, 1024)) != -1) {
                    outputStream.write(data, 0, len);
                }
                stream.close();
            } catch (IOException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }
    }
}
