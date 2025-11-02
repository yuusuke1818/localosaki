package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.ProductControlLoadFlgSeparateListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductControlLoadFlgSeparateListParameter;
import jp.co.osaki.osol.api.response.energy.setting.ProductControlLoadFlgSeparateListResponse;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 製品制御負荷 制御フラグ別リスト取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "ProductControlLoadFlgSeparateListBean")
@RequestScoped
public class ProductControlLoadFlgSeparateListBean extends OsolApiBean<ProductControlLoadFlgSeparateListParameter>
            implements BaseApiBean<ProductControlLoadFlgSeparateListParameter, ProductControlLoadFlgSeparateListResponse> {

    private ProductControlLoadFlgSeparateListParameter parameter = new ProductControlLoadFlgSeparateListParameter();

    private ProductControlLoadFlgSeparateListResponse response = new ProductControlLoadFlgSeparateListResponse();

    @EJB
    private ProductControlLoadFlgSeparateListDao productControlLoadFlgSeparateListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ProductControlLoadFlgSeparateListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ProductControlLoadFlgSeparateListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public ProductControlLoadFlgSeparateListResponse execute() throws Exception {
        ProductControlLoadFlgSeparateListParameter param = new ProductControlLoadFlgSeparateListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setProductCd(this.parameter.getProductCd());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (CheckUtility.isNullOrEmpty(parameter.getProductCd()) && parameter.getSmId() == null) {
            //製品コードと機器IDのいずれも設定されていない場合はバリデーションエラー
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        ProductControlLoadFlgSeparateListResult result = productControlLoadFlgSeparateListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
