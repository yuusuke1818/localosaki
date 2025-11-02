/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.input;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.input.EnergyUseListDao;
import jp.co.osaki.osol.api.parameter.energy.input.EnergyUseListParameter;
import jp.co.osaki.osol.api.response.energy.input.EnergyUseListResponse;
import jp.co.osaki.osol.api.result.energy.input.EnergyUseListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー実績取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "EnergyUseListBean")
@RequestScoped
public class EnergyUseListBean extends OsolApiBean<EnergyUseListParameter>
        implements BaseApiBean<EnergyUseListParameter, EnergyUseListResponse> {

    private EnergyUseListParameter parameter = new EnergyUseListParameter();

    private EnergyUseListResponse response = new EnergyUseListResponse();

    @EJB
    EnergyUseListDao energyUseListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public EnergyUseListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(EnergyUseListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public EnergyUseListResponse execute() throws Exception {
        EnergyUseListParameter param = new EnergyUseListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setEngId(this.parameter.getEngId());
        param.setContractId(this.parameter.getContractId());
        param.setCalYmFrom(this.parameter.getCalYmFrom());
        param.setCalYmTo(this.parameter.getCalYmTo());
        param.setDayAndNightType(this.parameter.getDayAndNightType());
        param.setEngSupplyType(this.parameter.getEngSupplyType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        EnergyUseListResult result = energyUseListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
