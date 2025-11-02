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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayReportListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayReportListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・日報・系統別使用量） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgDayReportListBean")
@RequestScoped
public class DemandOrgDayReportListBean extends OsolApiBean<DemandOrgDayReportListParameter>
        implements BaseApiBean<DemandOrgDayReportListParameter, DemandOrgDayReportListResponse> {

    private DemandOrgDayReportListParameter parameter = new DemandOrgDayReportListParameter();

    private DemandOrgDayReportListResponse response = new DemandOrgDayReportListResponse();

    @EJB
    DemandOrgDayReportListDao DemandOrgDayReportListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayReportListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayReportListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayReportListResponse execute() throws Exception {
        DemandOrgDayReportListParameter param = new DemandOrgDayReportListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setMeasurementDateFrom(this.parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(this.parameter.getMeasurementDateTo());
        param.setJigenNoFrom(this.parameter.getJigenNoFrom());
        param.setJigenNoTo(this.parameter.getJigenNoTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgDayReportListResult result = DemandOrgDayReportListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
