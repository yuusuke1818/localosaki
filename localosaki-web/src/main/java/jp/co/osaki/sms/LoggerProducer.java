package jp.co.osaki.sms;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 *
 * ロガープロデューサー
 *
 * @author y.nakamura
 */
@Dependent
public class LoggerProducer {
    @Produces
    public Logger ProduceLogger(InjectionPoint injectionPoint) throws Exception {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
