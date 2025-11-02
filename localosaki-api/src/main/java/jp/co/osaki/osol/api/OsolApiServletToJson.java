package jp.co.osaki.osol.api;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;

/**
 *
 * OSOL API サーブレット共通クラス ContentType:application/json
 *
 * @author take_suzuki
 *
 */
@WebServlet(name = "OsolApiServletToJson", urlPatterns = {
        "/json" }, description = "レスポンスボディがJSON形式となるサーブレット", displayName = "OSOL API JSON Servlet")
public final class OsolApiServletToJson extends OsolApiServlet {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 6429367876544872143L;

    /**
     * レスポンス用ログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    /**
     * レスポンス結果のログ出力最大値（1000byte）
     */
    private static final int RESPONSE_RESULT_LOG_MAX_SIZE = 1000;

    /**
     *  レスポンス設定
     *  @param response HttpServletResponse
     *  @param apiResponse APIレスポンスのインスタンス
     */
    @Override
    protected void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse) {

        super.settingResponse(response, apiResponse);

        String json = gson.toJson(apiResponse);
        int newLength = 0;

        // ログ出力量削減のため、ログに出力するレスポンス結果の最大値を指定
        if (json.getBytes().length >= RESPONSE_RESULT_LOG_MAX_SIZE) {
            newLength = RESPONSE_RESULT_LOG_MAX_SIZE;
        } else {
            newLength = json.getBytes().length;
        }

        byte[] jsonByte = Arrays.copyOf(json.getBytes(), newLength);
        requestLogger.info("Response:".concat(new String(jsonByte)));

        response.setContentType("application/json; charset=UTF8");
        response.setContentLength(json.getBytes().length);
        try {
            response.getWriter().println(json);
        } catch (IOException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }
    }
}
