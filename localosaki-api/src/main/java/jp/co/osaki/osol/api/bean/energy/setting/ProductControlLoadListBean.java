/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.ProductControlLoadListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductControlLoadListParameter;
import jp.co.osaki.osol.api.response.energy.setting.ProductControlLoadListResponse;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 製品制御負荷情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "ProductControlLoadListBean")
@RequestScoped
public class ProductControlLoadListBean extends OsolApiBean<ProductControlLoadListParameter>
        implements BaseApiBean<ProductControlLoadListParameter, ProductControlLoadListResponse> {

    private ProductControlLoadListParameter parameter = new ProductControlLoadListParameter();

    private ProductControlLoadListResponse response = new ProductControlLoadListResponse();

    @EJB
    private ProductControlLoadListDao productControlLoadListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ProductControlLoadListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ProductControlLoadListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ProductControlLoadListResponse execute() throws Exception {
        ProductControlLoadListParameter param = new ProductControlLoadListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setProductCd(this.parameter.getProductCd());
        param.setControlLoadFrom(this.parameter.getControlLoadFrom());
        param.setControlLoadTo(this.parameter.getControlLoadTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (param.getControlLoadFrom() == null && param.getControlLoadTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        ProductControlLoadListResult result = productControlLoadListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
