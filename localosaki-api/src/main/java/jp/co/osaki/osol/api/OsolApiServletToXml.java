package jp.co.osaki.osol.api;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;

/**
*
* OSOL API サーブレット共通クラス ContentType:text/xml
*
* @author take_suzuki
*
*/
@WebServlet(name = "OsolApiServletToXml", urlPatterns = {
       "/xml" }, description = "レスポンスボディがXML形式となるサーブレット", displayName = "OSOL API XML Servlet")
public class OsolApiServletToXml extends OsolApiServlet {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 6429367876544872149L;

    /**
     * レスポンス用ログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    /**
     *  レスポンス設定
     *  @param response HttpServletResponse
     *  @param apiResponse APIレスポンスのインスタンス
     * @throws Exception
     */
    @Override
    protected void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse) {

        super.settingResponse(response, apiResponse);

        boolean blnJson = false;
        for (Class<?> clazz : apiResponse.getClass().getInterfaces()){
            if (clazz.getName().equals(OsolApiResponseToXml.class.getName())) {
                blnJson = true;
                break;
            }
        }
        response.setContentType("text/xml; charset=UTF8");
        if (blnJson) {
            try {
                OsolApiResponseToXml apiResponseToXml = (OsolApiResponseToXml)apiResponse;
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                DOMSource source = new DOMSource(apiResponseToXml.toXml());
                transformer.transform(source, result);
                response.setContentLength(writer.toString().getBytes().length);
                requestLogger.info("Response:".concat(writer.toString()));
                PrintWriter out = response.getWriter();
                out.println(writer.toString());
            } catch (Exception | TransformerFactoryConfigurationError ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }
    }
}
