/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.SmControlLoadListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmControlLoadListParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmControlLoadListResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmControlLoadListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御負荷情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "SmControlLoadListBean")
@RequestScoped
public class SmControlLoadListBean extends OsolApiBean<SmControlLoadListParameter>
        implements BaseApiBean<SmControlLoadListParameter, SmControlLoadListResponse> {

    private SmControlLoadListParameter parameter = new SmControlLoadListParameter();

    private SmControlLoadListResponse response = new SmControlLoadListResponse();

    @EJB
    SmControlLoadListDao smControlLoadListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlLoadListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlLoadListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlLoadListResponse execute() throws Exception {
        SmControlLoadListParameter param = new SmControlLoadListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());
        param.setControlLoadFrom(this.parameter.getControlLoadFrom());
        param.setControlLoadTo(this.parameter.getControlLoadTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (param.getControlLoadFrom() == null && param.getControlLoadTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlLoadListResult result = smControlLoadListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
