/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandWeekBuildingCalListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandWeekBuildingCalListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandWeekBuildingCalListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandWeekBuildingCalListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド週報建物カレンダ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandWeekBuildingCalListBean")
@RequestScoped
public class DemandWeekBuildingCalListBean extends OsolApiBean<DemandWeekBuildingCalListParameter>
        implements BaseApiBean<DemandWeekBuildingCalListParameter, DemandWeekBuildingCalListResponse> {

    private DemandWeekBuildingCalListParameter parameter = new DemandWeekBuildingCalListParameter();

    private DemandWeekBuildingCalListResponse response = new DemandWeekBuildingCalListResponse();

    @EJB
    DemandWeekBuildingCalListDao demandWeekBuildingCalListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandWeekBuildingCalListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandWeekBuildingCalListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandWeekBuildingCalListResponse execute() throws Exception {
        DemandWeekBuildingCalListParameter param = new DemandWeekBuildingCalListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setFiscalYearFrom(this.parameter.getFiscalYearFrom());
        param.setFiscalYearTo(this.parameter.getFiscalYearTo());
        param.setWeekNoFrom(this.parameter.getWeekNoFrom());
        param.setWeekNoTo(this.parameter.getWeekNoTo());
        param.setBaseDate(this.parameter.getBaseDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandWeekBuildingCalListResult result = demandWeekBuildingCalListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
