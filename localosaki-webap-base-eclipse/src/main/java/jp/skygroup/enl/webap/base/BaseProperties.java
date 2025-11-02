package jp.skygroup.enl.webap.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import org.jboss.logging.Logger;

/**
 *
 * プロパティ取得クラス
 *
 * プロパティファイル内容を取得する。
 *
 * @author take_suzuki
 * 
 */
public class BaseProperties extends BaseConstants {

    /**
     * プロパティリスト
     */
    protected HashMap<String, String> properties;
    
    /**
     * プロパティファイル名
     */
    protected String propertyFileName;
    
    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     *  初期化処理
     * 
     *  プロパティファイルから読込みプロパティリストに設定する。
     */
    protected void initProperties(){
    
        Properties prop = new Properties();
        properties = new HashMap<>(); 
        try {
            prop.load(this.getClass().getResourceAsStream(this.propertyFileName));
            Enumeration<?> names = prop.propertyNames();
            while (names.hasMoreElements()){
                String paramName = (String)names.nextElement();
                properties.put(paramName, prop.getProperty(paramName));
            }
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }
    /**
     *
     * プロパティ取得
     * 
     * @param propertyName
     * @return メッセージ
     */
    protected String getProperty(final String propertyName) {

        if (properties.get(propertyName) != null){
            return properties.get(propertyName);
        } else {
            return STR_EMPTY;
        }
    }
}
