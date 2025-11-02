package jp.co.osaki.sms.batch;

import java.io.Serializable;

import jp.skygroup.enl.webap.base.BaseProperties;

/**
 *
 * コンフィグプロパティ取得
 *
 * OsolBatchConfigs.propertiesを取得する。
 *
 * @author take_suzuki
 */
public class SmsBatchConfigs extends BaseProperties implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * コンストラクタ
     *
     */
    public SmsBatchConfigs() {

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
