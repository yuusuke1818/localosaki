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
import jp.co.osaki.osol.api.dao.signage.SignageContentIdDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentIdParameter;
import jp.co.osaki.osol.api.response.signage.SignageContentIdResponse;
import jp.co.osaki.osol.api.result.signage.SignageContentIdResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * サイネージID取得 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageContentIdBean")
@RequestScoped
public class SignageContentIdBean extends OsolApiBean<SignageContentIdParameter>
        implements BaseApiBean<SignageContentIdParameter, SignageContentIdResponse> {

    private SignageContentIdParameter parameter = new SignageContentIdParameter();

    private SignageContentIdResponse response = new SignageContentIdResponse();

    @EJB
    SignageContentIdDao signageContentIdDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageContentIdParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     */
    @Override
    public void setParameter(SignageContentIdParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SignageContentIdResponse execute() throws Exception {
        SignageContentIdParameter param = new SignageContentIdParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageContentIdResult result = signageContentIdDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
