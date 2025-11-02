/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.signage;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.signage.SignageContentDeleteDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentDeleteParameter;
import jp.co.osaki.osol.api.response.signage.SignageContentDeleteResponse;
import jp.co.osaki.osol.api.result.signage.SignageContentDeleteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * サイネージコンテンツ削除 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageContentDeleteBean")
@RequestScoped
public class SignageContentDeleteBean extends OsolApiBean<SignageContentDeleteParameter>
        implements BaseApiBean<SignageContentDeleteParameter, SignageContentDeleteResponse> {

    private SignageContentDeleteParameter parameter = new SignageContentDeleteParameter();

    private SignageContentDeleteResponse response = new SignageContentDeleteResponse();

    @EJB
    SignageContentDeleteDao signageContentDeleteDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageContentDeleteParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SignageContentDeleteParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageContentDeleteResponse execute() throws Exception {
        SignageContentDeleteParameter param = new SignageContentDeleteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSignageContentId(this.parameter.getSignageContentId());
        param.setSignageContentsType(this.parameter.getSignageContentsType());
        param.setVersion(this.parameter.getVersion());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageContentDeleteResult result = signageContentDeleteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
