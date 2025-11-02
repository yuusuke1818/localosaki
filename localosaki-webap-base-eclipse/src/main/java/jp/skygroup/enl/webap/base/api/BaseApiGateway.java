package jp.skygroup.enl.webap.base.api;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * BaseApiGatewayクラス.
 *
 * API用を呼び出す為の基底クラス
 *
 * @author take_suzuki
 */
public abstract class BaseApiGateway {

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
     * POSTメソッド.
     *
     * @param url APIサーバーのURL
     * @param path APIのエンドポイントパス
     * @param parameter APIパラメータのインスタンス
     * @return APIのレスポンスボディ
     */
    protected final String post(String url, String path, BaseApiParameter parameter) {

        Form form = new Form();
        String result = "";
        for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(parameter.getClass())) {
            try {
                if (!"CLASS".equals(pd.getName().toUpperCase())) {
                    boolean blnJson = false;
                    for (Class<?> clazz : pd.getPropertyType().getInterfaces()){
                        if (clazz.getName().equals(BaseApiParameterJsonField.class.getName())) {
                            blnJson = true;
                            break;
                        }
                    }
                    if (blnJson) {
                        Method method = pd.getReadMethod();
                        method.setAccessible(true);
                        form.param(pd.getName(), gson.toJson(method.invoke(parameter)));
                    } else if (pd.getPropertyType().getName().equals(java.util.Date.class.getName())) {
                        if (PropertyUtils.getProperty(parameter, pd.getName()) != null){
                            form.param(pd.getName(), dateJsonFormat.format(PropertyUtils.getProperty(parameter, pd.getName())));
                        }
                    } else {
                        form.param(pd.getName(), BeanUtils.getProperty(parameter, pd.getName()));
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                return result;
            }
        }
        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        try {
            Client client = ClientBuilder.newClient();
            result = client.target(url)
                    .path(path)
                    .request()
                    .post(entity, String.class);
        } catch (BadRequestException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return result;
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return result;
        }
        return result;
    }

}
