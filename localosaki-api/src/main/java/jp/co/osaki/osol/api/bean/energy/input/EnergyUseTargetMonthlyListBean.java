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
import jp.co.osaki.osol.api.dao.energy.input.EnergyUseTargetMonthlyListDao;
import jp.co.osaki.osol.api.parameter.energy.input.EnergyUseTargetMonthlyListParameter;
import jp.co.osaki.osol.api.response.energy.input.EnergyUseTargetMonthlyListResponse;
import jp.co.osaki.osol.api.result.energy.input.EnergyUseTargetMonthlyListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 使用エネルギー各月目標取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "EnergyUseTargetMonthlyListBean")
@RequestScoped
public class EnergyUseTargetMonthlyListBean extends OsolApiBean<EnergyUseTargetMonthlyListParameter>
        implements BaseApiBean<EnergyUseTargetMonthlyListParameter, EnergyUseTargetMonthlyListResponse> {

    private EnergyUseTargetMonthlyListParameter parameter = new EnergyUseTargetMonthlyListParameter();

    private EnergyUseTargetMonthlyListResponse response = new EnergyUseTargetMonthlyListResponse();

    @EJB
    EnergyUseTargetMonthlyListDao energyUseTargetMonthlyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public EnergyUseTargetMonthlyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(EnergyUseTargetMonthlyListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public EnergyUseTargetMonthlyListResponse execute() throws Exception {
        EnergyUseTargetMonthlyListParameter param = new EnergyUseTargetMonthlyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setEngTypeCd(this.parameter.getEngTypeCd());
        param.setCalYmFrom(this.parameter.getCalYmFrom());
        param.setCalYmTo(this.parameter.getCalYmTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        EnergyUseTargetMonthlyListResult result = energyUseTargetMonthlyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
