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
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgYearReportListMaxDemandDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportListMaxDemandParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgYearReportListMaxDemandResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportListMaxDemandResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・年報・最大デマンド） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgYearReportListMaxDemandBean")
@RequestScoped
public class DemandOrgYearReportListMaxDemandBean extends OsolApiBean<DemandOrgYearReportListMaxDemandParameter>
        implements BaseApiBean<DemandOrgYearReportListMaxDemandParameter, DemandOrgYearReportListMaxDemandResponse> {

    private DemandOrgYearReportListMaxDemandParameter parameter = new DemandOrgYearReportListMaxDemandParameter();

    private DemandOrgYearReportListMaxDemandResponse response = new DemandOrgYearReportListMaxDemandResponse();

    @EJB
    DemandOrgYearReportListMaxDemandDao demandOrgYearReportListMaxDemandDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgYearReportListMaxDemandParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgYearReportListMaxDemandParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgYearReportListMaxDemandResponse execute() throws Exception {
        DemandOrgYearReportListMaxDemandParameter param = new DemandOrgYearReportListMaxDemandParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setPrecision(this.parameter.getPrecision());
        param.setBelowAccuracyControl(this.parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getBelowAccuracyControl()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(param.getBelowAccuracyControl()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE,
                        ApiSimpleConstants.DEMAND_ORG_SUM_PERIOD_MAX_YEAR)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 小数点精度の範囲チェック
        if (param.getPrecision() != null &&
                !CheckUtility.checkIntegerRange(param.getPrecision().toString(),
                        ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgYearReportListMaxDemandResult result = demandOrgYearReportListMaxDemandDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
