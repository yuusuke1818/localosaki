package jp.skygroup.enl.webap.base.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * リクエストログ出力フィルター
 *
 * @author take_suzuki
 *
 * リクエストの内容をログに出力する。
 */
public class BaseRequestLogFilter extends BaseConstants implements Filter {

    private static final Logger RequestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    /**
     * RequestHeader出力有無
     */
    private boolean outputRequestHeader = false;

    /**
     * QueryString出力有無
     */
    private boolean outputQueryString = false;

    /**
     * SessionKey出力有無
     */
    private boolean outputSessionKey = false;

    /**
     * OutputSessionKey
     */
    private final List<String> outputSessionKeyList = new ArrayList<>();

    /**
     * Parameter出力有無
     */
    private boolean outputParameter = false;

    /**
     * MaskingParameter
     */
    private final List<String> maskingParameterList = new ArrayList<>();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (this.outputRequestHeader){
                Enumeration<?> headerNames = httpRequest.getHeaderNames();
                while (headerNames.hasMoreElements()){
                    String headerName = (String)headerNames.nextElement();
                    RequestLogger.info("RequestHeader:".concat(headerName).concat(" [ ").concat(httpRequest.getHeader(headerName)).concat(" ]"));
                }
            }
            if (this.outputQueryString){
                if (httpRequest.getQueryString() != null){
                    RequestLogger.info("QueryString:".concat(" [ ").concat(httpRequest.getQueryString()).concat("]"));
                }
            }
            if (this.outputSessionKey){
                HttpSession session = httpRequest.getSession(false);
                if (session != null){
                    Enumeration<?> attributeNames = session.getAttributeNames();
                    while (attributeNames.hasMoreElements()){
                        String attrName = (String)attributeNames.nextElement();
                        if (this.outputSessionKeyList.contains(attrName)){
                            RequestLogger.info("SessionKey:".concat(attrName).concat(" [ ").concat(session.getAttribute(attrName).toString()).concat(" ]"));
                        }
                    }
                }
            }
            if (this.outputParameter){
                Enumeration<String> parameterNames = httpRequest.getParameterNames();
                while (parameterNames.hasMoreElements()){
                    String paramName = parameterNames.nextElement();
                    boolean blnMasking = false;
                    for (String maskingParameter : this.maskingParameterList) {
                        Pattern maskingPattern = Pattern.compile(".*".concat(maskingParameter).concat(".*"), Pattern.CASE_INSENSITIVE);
                        if (maskingPattern.matcher(paramName).find()) {
                            blnMasking = true;
                        }
                    }
                    if (blnMasking) {
                        RequestLogger.info("Parameter:".concat(paramName).concat(" [ * ]"));
                    } else {
                        RequestLogger.info("Parameter:".concat(paramName).concat(" [ ").concat(httpRequest.getParameter(paramName)).concat(" ]"));
                    }
                }
            }
        } catch (Exception e) {
            RequestLogger.error(BaseUtility.getStackTraceMessage(e));
            throw new ServletException(e);
        }
        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Enumeration<String> parameterNames = filterConfig.getInitParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String p = parameterNames.nextElement();
            switch (p.toUpperCase()) {
            case "OUTPUTREQUESTHEADER":
                this.outputRequestHeader = filterConfig.getInitParameter(p).toUpperCase().equals("TRUE");
                break;
            case "OUTPUTQUERYSTRING":
                this.outputQueryString = filterConfig.getInitParameter(p).toUpperCase().equals("TRUE");
                break;
            case "OUTPUTSESSIONKEY":
                this.outputSessionKey = filterConfig.getInitParameter(p).toUpperCase().equals("TRUE");
                break;
            case "OUTPUTSESSIONKEYLIST":
                this.outputSessionKeyList.addAll(Arrays.asList(filterConfig.getInitParameter(p).split(",")));
                break;
            case "OUTPUTPARAMETER":
                this.outputParameter = filterConfig.getInitParameter(p).toUpperCase().equals("TRUE");
                break;
            case "MASKINGPARAMETERLIST":
                this.maskingParameterList.addAll(Arrays.asList(filterConfig.getInitParameter(p).split(",")));
                break;
            default:
                break;
            }
        }
    }
    @Override
    public void destroy() {

        this.outputRequestHeader = false;
        this.outputQueryString = false;
        this.outputSessionKey = false;
        this.outputSessionKeyList.clear();
        this.outputParameter = false;
        this.maskingParameterList.clear();
    }
}
