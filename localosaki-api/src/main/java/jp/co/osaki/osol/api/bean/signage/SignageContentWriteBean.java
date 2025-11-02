/*
] * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.signage;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.signage.SignageContentWriteDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentWriteParameter;
import jp.co.osaki.osol.api.response.signage.SignageContentWriteResponse;
import jp.co.osaki.osol.api.result.signage.SignageContentWriteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * サイネージコンテンツ書き込み Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageContentWriteBean")
@RequestScoped
public class SignageContentWriteBean extends OsolApiBean<SignageContentWriteParameter>
        implements BaseApiBean<SignageContentWriteParameter, SignageContentWriteResponse> {

    private SignageContentWriteParameter parameter = new SignageContentWriteParameter();

    private SignageContentWriteResponse response = new SignageContentWriteResponse();

    @EJB
    SignageContentWriteDao signageContentWriteDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageContentWriteParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SignageContentWriteParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageContentWriteResponse execute() throws Exception {
        SignageContentWriteParameter param = new SignageContentWriteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSignageContentResultSet(this.parameter.getSignageContentResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageContentWriteResult result = signageContentWriteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
