package jp.skygroup.enl.webap.base;

import java.io.StringWriter;
import static jp.skygroup.enl.webap.base.BaseBean.errorLogger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * 
 * BaseVelocity テンプレート変換処理
 *
 * @author take_suzuki
 */
public class BaseVelocity extends BaseConstants {

    /** テンプレートファイルの内容を格納したクラス */
    private Template template = null;
    
    /** テンプレート変換時に使用するオブジェクトを格納するためのクラス */
    private final VelocityContext context = new VelocityContext();
    
    /** 
     * 
     * 初期化処理
     * 
     * @param templateFileName  テンプレートファイル
     * @param mailTemplateDir テンプレートファイルディレクトリ　
    **/
    public BaseVelocity(String templateFileName, String mailTemplateDir) {

        // Velocityの初期化
        Velocity.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, mailTemplateDir);
        Velocity.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
        Velocity.init();
        // テンプレートの取得
        template = Velocity.getTemplate(templateFileName, "UTF-8");
    }
    /**
     *
     * 出力データ設定
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        context.put(key, value);
    }
    /**
     *
     * パース結果出力
     * 
     * @return パース結果
     */    
    public String merge() {
        StringWriter sw = new StringWriter();
        try {
            //テンプレートとのマージ処理
            template.merge(context, sw);
        } catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return STR_EMPTY;
        }
        //マージ結果出力
        return sw.toString();
    }
}
