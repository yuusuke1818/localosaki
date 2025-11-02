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
import jp.co.osaki.osol.api.dao.signage.SignageApkWeatherDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkWeatherParameter;
import jp.co.osaki.osol.api.response.signage.SignageApkWeatherResponse;
import jp.co.osaki.osol.api.result.signage.SignageApkWeatherResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * アプリから天気を取得する時に利用 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageApkWeatherBean")
@RequestScoped
public class SignageApkWeatherBean extends OsolApiBean<SignageApkWeatherParameter>
        implements BaseApiBean<SignageApkWeatherParameter, SignageApkWeatherResponse> {

    private SignageApkWeatherParameter parameter = new SignageApkWeatherParameter();

    private SignageApkWeatherResponse response = new SignageApkWeatherResponse();

    @EJB
    private SignageApkWeatherDao signageApkWeatherDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageApkWeatherParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SignageApkWeatherParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SignageApkWeatherResponse execute() throws Exception {
        SignageApkWeatherParameter param = new SignageApkWeatherParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageApkWeatherResult result = signageApkWeatherDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
