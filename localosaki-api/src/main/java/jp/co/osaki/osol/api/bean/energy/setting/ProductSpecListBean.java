package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.ProductSpecListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductSpecListParameter;
import jp.co.osaki.osol.api.response.energy.setting.ProductSpecListResponse;
import jp.co.osaki.osol.api.result.energy.setting.ProductSpecListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 製品仕様情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "ProductSpecListBean")
@RequestScoped
public class ProductSpecListBean extends OsolApiBean<ProductSpecListParameter>
        implements BaseApiBean<ProductSpecListParameter, ProductSpecListResponse> {

    private ProductSpecListParameter parameter = new ProductSpecListParameter();

    private ProductSpecListResponse response = new ProductSpecListResponse();

    @EJB
    private ProductSpecListDao productSpecListDao;

    @Override
    public ProductSpecListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ProductSpecListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ProductSpecListResponse execute() throws Exception {
        ProductSpecListParameter param = new ProductSpecListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setProductCd(this.parameter.getProductCd());
        param.setProductName(this.parameter.getProductName());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ProductSpecListResult result = productSpecListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
