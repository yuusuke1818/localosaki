package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgYearReportExcelDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportExcelDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgYearReportExcelDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportExcelDataResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況（個別・年報） Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "DemandOrgYearReportExcelDataBean")
@RequestScoped
public class DemandOrgYearReportExcelDataBean extends OsolApiBean<DemandOrgYearReportExcelDataParameter>
        implements BaseApiBean<DemandOrgYearReportExcelDataParameter, DemandOrgYearReportExcelDataResponse> {

    private DemandOrgYearReportExcelDataParameter parameter = new DemandOrgYearReportExcelDataParameter();

    private DemandOrgYearReportExcelDataResponse response = new DemandOrgYearReportExcelDataResponse();

    @EJB
    DemandOrgYearReportExcelDataDao demandOrgYearReportExcelDataDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgYearReportExcelDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgYearReportExcelDataParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public DemandOrgYearReportExcelDataResponse execute() throws Exception {
        DemandOrgYearReportExcelDataParameter param = new DemandOrgYearReportExcelDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getSumPeriodCalcType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(param.getSumPeriodCalcType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgYearReportExcelDataResult result = demandOrgYearReportExcelDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
