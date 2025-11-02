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
import jp.co.osaki.osol.api.dao.energy.ems.DemandCalendarDayReportDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarDayReportParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandCalendarDayReportResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarDayReportResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド情報カレンダー（日報用） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandCalendarDayReportBean")
@RequestScoped
public class DemandCalendarDayReportBean extends OsolApiBean<DemandCalendarDayReportParameter>
        implements BaseApiBean<DemandCalendarDayReportParameter, DemandCalendarDayReportResponse> {

    private DemandCalendarDayReportParameter parameter = new DemandCalendarDayReportParameter();

    private DemandCalendarDayReportResponse response = new DemandCalendarDayReportResponse();

    @EJB
    DemandCalendarDayReportDao demandCalendarDayReportDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandCalendarDayReportParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandCalendarDayReportParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandCalendarDayReportResponse execute() throws Exception {
        DemandCalendarDayReportParameter param = new DemandCalendarDayReportParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandCalendarDayReportResult result = demandCalendarDayReportDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
