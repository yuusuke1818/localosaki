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
import jp.co.osaki.osol.api.dao.signage.SignageContentListDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentListParameter;
import jp.co.osaki.osol.api.response.signage.SignageContentListResponse;
import jp.co.osaki.osol.api.result.signage.SignageContentListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * サイネージコンテンツ一覧取得 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SignageContentListBean")
@RequestScoped
public class SignageContentListBean extends OsolApiBean<SignageContentListParameter>
        implements BaseApiBean<SignageContentListParameter, SignageContentListResponse> {

    private SignageContentListParameter parameter = new SignageContentListParameter();

    private SignageContentListResponse response = new SignageContentListResponse();

    @EJB
    SignageContentListDao signageContentDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SignageContentListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(SignageContentListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SignageContentListResponse execute() throws Exception {
        SignageContentListParameter param = new SignageContentListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSignageContentsType(this.parameter.getSignageContentsType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SignageContentListResult result = signageContentDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
