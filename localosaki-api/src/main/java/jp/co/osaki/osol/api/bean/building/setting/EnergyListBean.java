/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.building.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.building.setting.EnergyListDao;
import jp.co.osaki.osol.api.parameter.building.setting.EnergyListParameter;
import jp.co.osaki.osol.api.response.building.setting.EnergyListResponse;
import jp.co.osaki.osol.api.result.building.setting.EnergyListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー情報取得 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "EnergyListBean")
@RequestScoped
public class EnergyListBean extends OsolApiBean<EnergyListParameter>
        implements BaseApiBean<EnergyListParameter, EnergyListResponse> {

    private EnergyListParameter parameter = new EnergyListParameter();

    private EnergyListResponse response = new EnergyListResponse();

    @EJB
    EnergyListDao availableEnergyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public EnergyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(EnergyListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public EnergyListResponse execute() throws Exception {
        EnergyListParameter param = new EnergyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        EnergyListResult result = availableEnergyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
