package jp.co.osaki.osol.api.bean.apikeyissue;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiErrorMessageResponse;
import jp.co.osaki.osol.api.OsolApiErrorMessageResult;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.apikeyissue.ApiKeyReCreateDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyReCreateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.ApiKeyReCreateResponse;
import jp.co.osaki.osol.api.result.apikeyissue.ApiKeyReCreateResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;

@Named(value = "ApiKeyReCreateBean")
@RequestScoped
public class ApiKeyReCreateBean extends OsolApiBean<ApiKeyReCreateParameter>
        implements BaseApiBean<ApiKeyReCreateParameter, BaseApiResponse> {

    private ApiKeyReCreateParameter parameter = new ApiKeyReCreateParameter();

    private ApiKeyReCreateResponse response = new ApiKeyReCreateResponse();

    @EJB
    ApiKeyReCreateDao apiKeyReCreateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public ApiKeyReCreateParameter getParameter() {
        return this.parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(ApiKeyReCreateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public BaseApiResponse execute() throws Exception {
        ApiKeyReCreateParameter param = new ApiKeyReCreateParameter();
        copyOsolApiParameter(this.parameter, param);
//        param.setVersion(this.parameter.getVersion());
        param.setRefreshKey(this.parameter.getRefreshKey());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ApiKeyReCreateResult result = apiKeyReCreateDao.query(param);
        if (CheckUtility.isNullOrEmpty(result.getApiKey())) {
            OsolApiErrorMessageResult errResult = new OsolApiErrorMessageResult();
            errResult.setErrorMessage("API_ERROR_ORG_PARAMETER_VALID");
            OsolApiErrorMessageResponse errResponse = new OsolApiErrorMessageResponse();
            errResponse.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            errResponse.setResult(errResult);
            return errResponse;
        }

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
