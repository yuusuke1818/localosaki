package jp.skygroup.enl.webap.base.api;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * APIの基底サーブレットクラス.
 *
 * @author take_suzuki
 *
 */
public abstract class BaseApiServlet extends HttpServlet {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 5448187654120520603L;

    /**
     * 日付フォーマット形式
     */
    private static String JSON_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS zzz";

    /**
     * GSONインスタンス.
     */
    protected static final Gson gson = new GsonBuilder().setDateFormat(JSON_DATE_FORMAT).create();

    /**
     * 日付フォーマッター
     */
    protected static DateFormat dateJsonFormat = new SimpleDateFormat(JSON_DATE_FORMAT);

    /**
     * エラー用ログ
     */
    protected static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * リクエストパラメータからAPIパラメータクラスに設定する.
     *
     * @param httpServletRequest HttpServletRequest
     * @param parameter APIパラメータクラスのインスタンス
     * @return APIパラメータクラスのインスタンス
     */
    protected final <P extends BaseApiParameter> P getRequestParameter(
            HttpServletRequest httpServletRequest,
            P parameter) {

        Enumeration<String> keys = httpServletRequest.getParameterNames();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            try {
                if (BeanUtils.describe(parameter).keySet().contains(element)){
                    PropertyDescriptor pd = new PropertyDescriptor(element, parameter.getClass());
                    boolean blnJson = false;
                    for (Class<?> clazz : pd.getPropertyType().getInterfaces()){
                        if (clazz.getName().equals(BaseApiParameterJsonField.class.getName())) {
                            blnJson = true;
                            break;
                        }
                    }
                    if (blnJson) {
                        Object result = gson.fromJson(httpServletRequest.getParameter(element), pd.getPropertyType());
                        BeanUtils.setProperty(parameter, element, result);
                    } else if (pd.getPropertyType().getName().equals(java.util.Date.class.getName())) {
                        try {
                            BeanUtils.setProperty(parameter, element, dateJsonFormat.parse(httpServletRequest.getParameter(element)));
                        } catch (ParseException e) {
                            errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        }
                    } else {
                        BeanUtils.setProperty(parameter, element, httpServletRequest.getParameter(element));
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IntrospectionException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }
        return parameter;
    }

    /**
     * ManageredBeanを取得する.
     *
     * @param beanName beanの名前
     * @return ManageredBean CDI管理Bean
     */
    protected final BaseApiBean<?, ?> getBean(String beanName) {

        BeanManager beanManager = null;
        BaseApiBean<?, ?> apiBean = null;
        try {
            InitialContext ictx = new InitialContext();
            beanManager = (BeanManager) ictx.lookup("java:comp/BeanManager");
        } catch (NamingException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return null;
        }

        try {
            Set<Bean<?>> beans = beanManager.getBeans(beanName);
            if (beans == null || beans.isEmpty()) {
                return null;
            }
            Bean<?> bean = beanManager.resolve(beans);
            if (bean == null || bean.isNullable()) {
                return null;
            }
            apiBean = (BaseApiBean<?, ?>) beanManager.getReference(
                    bean,
                    Class.forName(bean.getBeanClass().getName()),
                    beanManager.createCreationalContext(bean));
        } catch (ClassNotFoundException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return null;
        }

        return apiBean;
    }

    /**
     * GETメソッド処理.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    /**
     * POSTメソッド処理.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        execute(request, response);
    }

    /**
     * リクエスト処理.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    protected abstract void execute(HttpServletRequest request, HttpServletResponse response);

    /**
     * エラーメッセージレスポンス生成.
     *
     * @param setResultCode 処理結果コード
     * @param errorMessage エラーメッセージ
     * @return apiResponse APIレスポンスのインスタンス
     */
    protected abstract BaseApiResponse createErrorMessageResponse(String setResultCode, String errorMessage);

    /**
     * エラーメッセージリストレスポンス生成.
     *
     * @param setResultCode 処理結果コード
     * @param errorMessage エラーメッセージリスト
     * @return apiResponse APIレスポンスのインスタンス
     */
    protected abstract BaseApiResponse createErrorMessageListResponse(String setResultCode, List<String> errorMessageList);

    /**
     * レスポンス設定.
     *
     *  @param response HttpServletResponse
     *  @param apiResponse APIレスポンスのインスタンス
     */
    protected abstract void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse);

}
