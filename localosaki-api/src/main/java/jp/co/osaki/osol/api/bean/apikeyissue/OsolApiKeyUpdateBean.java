package jp.co.osaki.osol.api.bean.apikeyissue;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.auth.OsolApiAuthentication;
import jp.co.osaki.osol.api.dao.apikeyissue.OsolApiKeyUpdateDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyUpdateParameter;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyUpdateResponse;
import jp.co.osaki.osol.api.result.apikeyissue.OsolApiKeyUpdateResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * OSOL用APIキー更新 Beanクラス.
 *
 * @author ya-ishida
 */
@Named(value = "OsolApiKeyUpdateBean")
@RequestScoped
public class OsolApiKeyUpdateBean extends OsolApiBean<OsolApiKeyUpdateParameter>
        implements BaseApiBean<OsolApiKeyUpdateParameter, OsolApiKeyUpdateResponse> {

    @EJB
    private OsolApiKeyUpdateDao osolApiKeyUpdateDao;

    private OsolApiKeyUpdateParameter parameter = new OsolApiKeyUpdateParameter();

    private OsolApiKeyUpdateResponse response = new OsolApiKeyUpdateResponse();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public OsolApiKeyUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(OsolApiKeyUpdateParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public OsolApiKeyUpdateResponse execute() throws Exception {
        OsolApiKeyUpdateParameter param = new OsolApiKeyUpdateParameter();
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

        OsolApiKeyUpdateResult result = osolApiKeyUpdateDao.query(param);
        if (!CheckUtility.isNullOrEmpty(result.getApiKey())) {
            //結果をマップにセットする
            OsolApiAuthentication.setPrivateApiKeyMap(result.getApiKey(), result.getIssueDateTime());
        }
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
