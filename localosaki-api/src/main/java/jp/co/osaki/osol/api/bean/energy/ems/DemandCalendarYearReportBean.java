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
import jp.co.osaki.osol.api.dao.energy.ems.DemandCalendarYearReportDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarYearReportParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandCalendarYearReportResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarYearReportResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド情報カレンダー（年報用）　Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandCalendarYearReportBean")
@RequestScoped
public class DemandCalendarYearReportBean extends OsolApiBean<DemandCalendarYearReportParameter>
        implements BaseApiBean<DemandCalendarYearReportParameter, DemandCalendarYearReportResponse> {

    private DemandCalendarYearReportParameter parameter = new DemandCalendarYearReportParameter();

    private DemandCalendarYearReportResponse response = new DemandCalendarYearReportResponse();

    @EJB
    DemandCalendarYearReportDao demandCalendarYearReportDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandCalendarYearReportParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandCalendarYearReportParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandCalendarYearReportResponse execute() throws Exception {
        DemandCalendarYearReportParameter param = new DemandCalendarYearReportParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandCalendarYearReportResult result = demandCalendarYearReportDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
