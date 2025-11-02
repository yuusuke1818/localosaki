package jp.co.osaki.osol.api.bean.apikeyissue;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.auth.OsolApiAuthentication;
import jp.co.osaki.osol.api.dao.apikeyissue.OsolApiKeyCreateDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyCreateResponse;
import jp.co.osaki.osol.api.result.apikeyissue.OsolApiKeyCreateResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * OSOL用APIキー発行 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "OsolApiKeyCreateBean")
@RequestScoped
public class OsolApiKeyCreateBean extends OsolApiBean<OsolApiKeyCreateParameter>
        implements BaseApiBean<OsolApiKeyCreateParameter, OsolApiKeyCreateResponse> {

    @EJB
    private OsolApiKeyCreateDao osolApiKeyCreateDao;

    private OsolApiKeyCreateParameter parameter = new OsolApiKeyCreateParameter();

    private OsolApiKeyCreateResponse response = new OsolApiKeyCreateResponse();

    /**
     * @return parameter
     */
    @Override
    public OsolApiKeyCreateParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter セットする parameter
     */
    @Override
    public void setParameter(OsolApiKeyCreateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public OsolApiKeyCreateResponse execute() throws Exception {
        OsolApiKeyCreateParameter param = new OsolApiKeyCreateParameter();
        param.setBean(this.parameter.getBean());
        param.setLoginCorpId(this.parameter.getLoginCorpId());
        param.setLoginPersonId(this.parameter.getLoginPersonId());
        param.setAuthHash(this.parameter.getAuthHash());
        param.setMillisec(this.parameter.getMillisec());
        param.setOperationCorpId(this.parameter.getOperationCorpId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        OsolApiKeyCreateResult result = osolApiKeyCreateDao.query(param);
        if (!CheckUtility.isNullOrEmpty(result.getApiKey())) {
            //結果をマップにセットする
            OsolApiAuthentication.setPrivateApiKeyMap(result.getApiKey(), result.getIssueDateTime());
        }
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
