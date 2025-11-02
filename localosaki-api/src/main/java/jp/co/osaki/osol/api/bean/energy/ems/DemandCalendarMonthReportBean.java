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
import jp.co.osaki.osol.api.dao.energy.ems.DemandCalendarMonthReportDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarMonthReportParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandCalendarMonthReportResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarMonthReportResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド情報カレンダー（月報用）　Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandCalendarMonthReportBean")
@RequestScoped
public class DemandCalendarMonthReportBean extends OsolApiBean<DemandCalendarMonthReportParameter>
        implements BaseApiBean<DemandCalendarMonthReportParameter, DemandCalendarMonthReportResponse> {

    private DemandCalendarMonthReportParameter parameter = new DemandCalendarMonthReportParameter();

    private DemandCalendarMonthReportResponse response = new DemandCalendarMonthReportResponse();

    @EJB
    DemandCalendarMonthReportDao demandCalendarMonthReportDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandCalendarMonthReportParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandCalendarMonthReportParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandCalendarMonthReportResponse execute() throws Exception {
        DemandCalendarMonthReportParameter param = new DemandCalendarMonthReportParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandCalendarMonthReportResult result = demandCalendarMonthReportDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
