package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgMonthReportExcelDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMonthReportExcelDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgMonthReportExcelDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMonthReportExcelDataResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況（個別・月報） Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "DemandOrgMonthReportExcelDataBean")
@RequestScoped
public class DemandOrgMonthReportExcelDataBean extends OsolApiBean<DemandOrgMonthReportExcelDataParameter>
        implements BaseApiBean<DemandOrgMonthReportExcelDataParameter, DemandOrgMonthReportExcelDataResponse> {

    private DemandOrgMonthReportExcelDataParameter parameter = new DemandOrgMonthReportExcelDataParameter();

    private DemandOrgMonthReportExcelDataResponse response = new DemandOrgMonthReportExcelDataResponse();

    @EJB
    DemandOrgMonthReportExcelDataDao demandOrgMonthReportExcelDataDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgMonthReportExcelDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgMonthReportExcelDataParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public DemandOrgMonthReportExcelDataResponse execute() throws Exception {
        DemandOrgMonthReportExcelDataParameter param = new DemandOrgMonthReportExcelDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());
        param.setYmd(this.parameter.getYmd());
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

        DemandOrgMonthReportExcelDataResult result = demandOrgMonthReportExcelDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
