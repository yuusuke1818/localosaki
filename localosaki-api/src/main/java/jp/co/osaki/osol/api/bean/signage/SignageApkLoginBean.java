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
import jp.co.osaki.osol.api.dao.signage.SignageApkLoginDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkLoginParameter;
import jp.co.osaki.osol.api.response.signage.SignageApkLoginResponse;
import jp.co.osaki.osol.api.result.signage.SignageApkLoginResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * アプリからログインするときに利用 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageApkLoginBean")
@RequestScoped
public class SignageApkLoginBean extends OsolApiBean<SignageApkLoginParameter>
        implements BaseApiBean<SignageApkLoginParameter, SignageApkLoginResponse> {

    private SignageApkLoginParameter parameter = new SignageApkLoginParameter();

    private SignageApkLoginResponse response = new SignageApkLoginResponse();

    @EJB
    private SignageApkLoginDao signageApkLoginDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageApkLoginParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(SignageApkLoginParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageApkLoginResponse execute() throws Exception {
        SignageApkLoginParameter param = new SignageApkLoginParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setPassword(this.parameter.getPassword());
        param.setBuildingNo(this.parameter.getBuildingNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageApkLoginResult result = signageApkLoginDao.query(param);
        if (result.getBuildingId() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_AUTHENTICATION_PERSON_NOT_VALID);
        } else {
            response.setResultCode(OsolApiResultCode.API_OK);
        }
        response.setResult(result);
        return response;
    }

}
