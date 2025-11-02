package jp.skygroup.enl.webap.base.api;

/**
 *
 * APIBeanの基底インターフェース.
 *
 * @author take_suzuki
 *
 */
public interface BaseApiBean<P extends BaseApiParameter, R extends BaseApiResponse> {

    /**
     * APIパラメータインスタンス取得.
     *
     * @return APIパラメータのインスタンス
     */
    public abstract P getParameter();

    /**
     * APIパラメータインスタンス取得.
     *
     * @return APIパラメータのインスタンス
     */
    public abstract void setParameter(P parameter);

    /**
     * ApiBean実行.
     *
     * @param apiParam APIパラメータのインスタンス
     * @return APIレスポンスのインスタンス
     */
    public abstract R execute() throws Exception;

}
