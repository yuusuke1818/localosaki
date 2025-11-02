package jp.skygroup.enl.webap.base;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.jboss.logging.Logger;

public class BaseDaoInterceptor extends BaseConstants {

    private static final Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());
    
    @AroundInvoke
    public Object methodInvoke(InvocationContext ic) throws Exception {
        Object result = null;
        try {
            result = ic.proceed();
            
            if( ic.getTarget() instanceof BaseDao){
                BaseDao bd = (BaseDao)ic.getTarget();
                if(bd.getException() !=  null){
                    Exception ex = bd.getException();
                    bd.setExceptionFlg(false);
                    bd.setException(null);
                    errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                    throw ex;
                }
            }
        } finally {
            eventLogger.info(ic.getTarget().getClass().getName().concat(".").concat(ic.getMethod().getName()).concat(" [") + result + "]");
        }
        return result;
    }
}
