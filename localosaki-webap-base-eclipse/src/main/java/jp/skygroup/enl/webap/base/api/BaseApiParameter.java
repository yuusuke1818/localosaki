package jp.skygroup.enl.webap.base.api;

import javax.validation.constraints.NotNull;

/**
 * APIのパラメータ基底クラス.
 *
 * @author take_suzuki
 *
 */
public abstract class BaseApiParameter {

    /**
     * bean
     */
    @NotNull
    private String bean;

    /**
     * bean取得
     *
     * @return bean
     */
    public final String getBean() {
        return bean;
    }

    /**
     * bean設定
     *
     * @param bean セットする bean
     */
    public final void setBean(String bean) {
        this.bean = bean;
    }

}