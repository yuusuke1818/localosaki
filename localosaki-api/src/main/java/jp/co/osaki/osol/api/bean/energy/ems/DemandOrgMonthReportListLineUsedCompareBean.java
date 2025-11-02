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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgMonthReportListLineUsedCompareDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMonthReportListLineUsedCompareParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgMonthReportListLineUsedCompareResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMonthReportListLineUsedCompareResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・月報・データ対比） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgMonthReportListLineUsedCompareBean")
@RequestScoped
public class DemandOrgMonthReportListLineUsedCompareBean
        extends OsolApiBean<DemandOrgMonthReportListLineUsedCompareParameter>
        implements
        BaseApiBean<DemandOrgMonthReportListLineUsedCompareParameter, DemandOrgMonthReportListLineUsedCompareResponse> {

    private DemandOrgMonthReportListLineUsedCompareParameter parameter = new DemandOrgMonthReportListLineUsedCompareParameter();

    private DemandOrgMonthReportListLineUsedCompareResponse response = new DemandOrgMonthReportListLineUsedCompareResponse();

    @EJB
    DemandOrgMonthReportListLineUsedCompareDao demandOrgMonthReportListLineUsedCompareDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgMonthReportListLineUsedCompareParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgMonthReportListLineUsedCompareParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgMonthReportListLineUsedCompareResponse execute() throws Exception {
        DemandOrgMonthReportListLineUsedCompareParameter param = new DemandOrgMonthReportListLineUsedCompareParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setGraphId(this.parameter.getGraphId());
        param.setDayCompareDestination(this.parameter.getDayCompareDestination());
        param.setDayCompareSource(this.parameter.getDayCompareSource());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setPrecision(this.parameter.getPrecision());
        param.setBelowAccuracyControl(this.parameter.getBelowAccuracyControl());

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
        if (!CheckUtility.isNullOrEmpty(param.getBelowAccuracyControl()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(param.getBelowAccuracyControl()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_ORG_SUM_PERIOD_MAX_MONTH)) {
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

        DemandOrgMonthReportListLineUsedCompareResult result = demandOrgMonthReportListLineUsedCompareDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
