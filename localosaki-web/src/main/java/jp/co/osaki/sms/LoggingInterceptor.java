package jp.co.osaki.sms;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;

/**
 *
 * ロギングインターセプター
 *
 * @author y.nakamura
 */
@Interceptor
@Logged
@Dependent
public class LoggingInterceptor implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 1L;

    /**
     * アクセスログ
     */
    private static final Logger accessLogger = Logger.getLogger(LOGGER_NAME.ACCESS.getVal());

    @Inject
    private HttpServletRequest request;

    @AroundInvoke
    public Object logInvocation(InvocationContext context) throws Exception {

        try {
            return context.proceed();
        } finally {
            Class<?> targetClass = context.getTarget().getClass();
            Object instance = targetClass.getDeclaredConstructor().newInstance();
            Method getLoginCorpIdMethod = targetClass.getMethod("getLoginCorpId");
            Method getLoginPersonIdMethod = targetClass.getMethod("getLoginPersonId");

            String loginCorpId = (String)getLoginCorpIdMethod.invoke(instance);
            String loginPersonId = (String)getLoginPersonIdMethod.invoke(instance);

                // リクエストから画面情報を取得
                String uri = request.getRequestURI();

                // クラス名取得
                String className = context.getTarget().getClass().getName();
                // クラス名がプロキシ名の場合
                if (context.getTarget() == null) {
                    className = "Unknown";
                } else if (context.getTarget().getClass().getName().contains("$$")) {
                    className = context.getTarget().getClass().getSuperclass().getName();
                }

                // メソッド名取得
                String methodName = context.getMethod().getName();

                // ログ出力の文字列を取得
                String accessLogMessage = getLogMessage(loginCorpId, loginPersonId,uri, className, methodName);
                accessLogger.info(accessLogMessage);
        }
    }

    private String getLogMessage(String loginCorpId, String loginPersonId, String uri, String className, String methodName) {
        StringBuilder accessLogMessage = new StringBuilder();
        accessLogMessage.append("CorpId:");
        accessLogMessage.append(loginCorpId);
        accessLogMessage.append(" PersonId:");
        accessLogMessage.append(loginPersonId);
        accessLogMessage.append(" 画面:");
        accessLogMessage.append(uri);
        // 操作文面に関しては参照できなかったため実行メソッドのスーパークラスにてアクセスログ出力する
//        if (!CheckUtility.isNullOrEmpty(meterManagementBean.getAccessLogMessage())) {
//            accessLogMessage.append(" 操作:");
//            accessLogMessage.append(meterManagementBean.getAccessLogMessage());
//        }
        accessLogMessage.append(" Class:");
        accessLogMessage.append(className);
        accessLogMessage.append(" Method:");
        accessLogMessage.append(methodName);

        return accessLogMessage.toString();
    }

}
