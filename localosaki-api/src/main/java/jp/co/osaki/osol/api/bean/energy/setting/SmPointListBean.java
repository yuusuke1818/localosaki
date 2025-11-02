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
import jp.co.osaki.osol.api.dao.energy.setting.SmPointListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmPointListParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmPointListResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmPointListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器ポイント情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "SmPointListBean")
@RequestScoped
public class SmPointListBean extends OsolApiBean<SmPointListParameter>
        implements BaseApiBean<SmPointListParameter, SmPointListResponse> {

    private SmPointListParameter parameter = new SmPointListParameter();

    private SmPointListResponse response = new SmPointListResponse();

    @EJB
    SmPointListDao smPointListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmPointListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmPointListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmPointListResponse execute() throws Exception {
        SmPointListParameter param = new SmPointListParameter();
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmPointListResult result = smPointListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
