/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.apikeyissue;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.apikeyissue.ApiKeyCreateDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyCreateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.ApiKeyCreateResponse;
import jp.co.osaki.osol.api.result.apikeyissue.ApiKeyCreateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 外部アクセス用APIキー発行　Beanクラス
 *
 * @author n-takada
 */
@Named(value = "ApiKeyCreateBean")
@RequestScoped
public class ApiKeyCreateBean extends OsolApiBean<ApiKeyCreateParameter>
        implements BaseApiBean<ApiKeyCreateParameter, ApiKeyCreateResponse> {

    private ApiKeyCreateParameter parameter = new ApiKeyCreateParameter();

    private ApiKeyCreateResponse response = new ApiKeyCreateResponse();

    @EJB
    ApiKeyCreateDao apiKeyCreateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ApiKeyCreateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ApiKeyCreateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public ApiKeyCreateResponse execute() throws Exception {
        ApiKeyCreateParameter param = new ApiKeyCreateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setVersion(this.parameter.getVersion());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ApiKeyCreateResult result = apiKeyCreateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
