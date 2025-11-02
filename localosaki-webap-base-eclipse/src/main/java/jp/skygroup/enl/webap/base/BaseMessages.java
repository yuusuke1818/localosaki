package jp.skygroup.enl.webap.base;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * Baseメッセージプロパティ取得
 *
 * Baseで使用するメッセージを取得する。
 *
 * @author take_suzuki
 * 
 */
@Named(value = "BaseMessages")
@ApplicationScoped
public class BaseMessages extends BaseProperties implements Serializable {

    private static final long serialVersionUID = 7167778725863937028L;
    
    /**
     *  初期化処理
     * 
     *  プロパティファイルから読込みプロパティリストに設定する。
     */
    @PostConstruct
    public void initMessages() {

        this.propertyFileName = "BaseMessages.properties";
        super.initProperties();

    }

    /**
     *  メッセージ取得処理
     * 
     * @param propertyName メッセージプロパティ名
     * @return 取得メッセージ
     */
    public String getMessage(final String propertyName){
    
        return super.getProperty(propertyName);
        
    }
    
}
