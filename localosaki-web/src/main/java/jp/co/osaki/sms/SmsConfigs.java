package jp.co.osaki.sms;

import java.io.Serializable;

import jp.skygroup.enl.webap.base.BaseProperties;
/**
 *
 * @author hayashi_tak
 *
 */
public class SmsConfigs extends BaseProperties implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = -8110955518135097546L;

    /**
     *
     * コンストラクタ
     *
     */
    public SmsConfigs() {

        this.propertyFileName = this.getClass().getSimpleName().concat(".properties");
        super.initProperties();
    }

    /**
     *  コンフィグ取得処理
     *
     * @param propertyName コンフィグプロパティ名
     * @return 取得コンフィグ
     */
    public String getConfig(final String propertyName) {
        return super.getProperty(propertyName);

    }

}