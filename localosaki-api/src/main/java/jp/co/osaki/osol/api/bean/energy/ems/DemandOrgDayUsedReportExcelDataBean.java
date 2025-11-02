package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayUsedReportExcelDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayUsedReportExcelDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayUsedReportExcelDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayUsedReportExcelDataResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況（個別・日報・系統別使用量） Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "DemandOrgDayUsedReportExcelDataBean")
@RequestScoped
public class DemandOrgDayUsedReportExcelDataBean extends OsolApiBean<DemandOrgDayUsedReportExcelDataParameter>
        implements BaseApiBean<DemandOrgDayUsedReportExcelDataParameter, DemandOrgDayUsedReportExcelDataResponse> {

    private DemandOrgDayUsedReportExcelDataParameter parameter = new DemandOrgDayUsedReportExcelDataParameter();

    private DemandOrgDayUsedReportExcelDataResponse response = new DemandOrgDayUsedReportExcelDataResponse();

    @EJB
    DemandOrgDayUsedReportExcelDataDao demandOrgDayUsedReportExcelDataDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayUsedReportExcelDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayUsedReportExcelDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayUsedReportExcelDataResponse execute() throws Exception {
        DemandOrgDayUsedReportExcelDataParameter param = new DemandOrgDayUsedReportExcelDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());
        param.setYmd(this.parameter.getYmd());
        param.setTimesOfDay(this.parameter.getTimesOfDay());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setPrecision(this.parameter.getPrecision());
        param.setBelowAccuracyControl(this.parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgDayUsedReportExcelDataResult result = demandOrgDayUsedReportExcelDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
