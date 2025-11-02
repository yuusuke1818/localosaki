/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.master;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.master.UiScreenListDao;
import jp.co.osaki.osol.api.parameter.master.UiScreenListParameter;
import jp.co.osaki.osol.api.response.master.UiScreenListResponse;
import jp.co.osaki.osol.api.result.master.UiScreenListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * UI画面マスタ Beanクラス
 *
 * @author n-takada
 */
@Named(value = "UiScreenListBean")
@RequestScoped
public class UiScreenListBean extends OsolApiBean<UiScreenListParameter>
        implements BaseApiBean<UiScreenListParameter, UiScreenListResponse> {

    private UiScreenListParameter parameter = new UiScreenListParameter();

    private UiScreenListResponse response = new UiScreenListResponse();

    @EJB
    UiScreenListDao uiScreenListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UiScreenListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UiScreenListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public UiScreenListResponse execute() throws Exception {
        UiScreenListParameter param = new UiScreenListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UiScreenListResult result = uiScreenListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}