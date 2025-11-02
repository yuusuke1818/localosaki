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
import jp.co.osaki.osol.api.dao.energy.ems.DemandCalendarWeekReportDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarWeekReportParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandCalendarWeekReportResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarWeekReportResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド情報カレンダー（週報用）　Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandCalendarWeekReportBean")
@RequestScoped
public class DemandCalendarWeekReportBean extends OsolApiBean<DemandCalendarWeekReportParameter>
        implements BaseApiBean<DemandCalendarWeekReportParameter, DemandCalendarWeekReportResponse> {

    private DemandCalendarWeekReportParameter parameter = new DemandCalendarWeekReportParameter();

    private DemandCalendarWeekReportResponse response = new DemandCalendarWeekReportResponse();

    @EJB
    DemandCalendarWeekReportDao demandCalendarWeekReportDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandCalendarWeekReportParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandCalendarWeekReportParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandCalendarWeekReportResponse execute() throws Exception {
        DemandCalendarWeekReportParameter param = new DemandCalendarWeekReportParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandCalendarWeekReportResponse response = new DemandCalendarWeekReportResponse();
        DemandCalendarWeekReportResult result = demandCalendarWeekReportDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
