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
import jp.co.osaki.osol.api.dao.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisResult;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンドデータ実績取得処理（全体・週間削減率評価） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandAllSummaryPowerWeekReductionAnalysisBean")
@RequestScoped
public class DemandAllSummaryPowerWeekReductionAnalysisBean
        extends OsolApiBean<DemandAllSummaryPowerWeekReductionAnalysisParameter>
        implements
        BaseApiBean<DemandAllSummaryPowerWeekReductionAnalysisParameter, DemandAllSummaryPowerWeekReductionAnalysisResponse> {

    private DemandAllSummaryPowerWeekReductionAnalysisParameter parameter = new DemandAllSummaryPowerWeekReductionAnalysisParameter();

    private DemandAllSummaryPowerWeekReductionAnalysisResponse response = new DemandAllSummaryPowerWeekReductionAnalysisResponse();

    @EJB
    DemandAllSummaryPowerWeekReductionAnalysisDao demandAllSummaryPowerWeekReductionAnalysisDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandAllSummaryPowerWeekReductionAnalysisParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandAllSummaryPowerWeekReductionAnalysisParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public DemandAllSummaryPowerWeekReductionAnalysisResponse execute() throws Exception {
        DemandAllSummaryPowerWeekReductionAnalysisParameter param = new DemandAllSummaryPowerWeekReductionAnalysisParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNarrowing(this.parameter.getBuildingNarrowing());
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setFiscalYear(this.parameter.getFiscalYear());
        param.setWeekNo(this.parameter.getWeekNo());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setRangeUnit(this.parameter.getRangeUnit());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setCompareTarget(this.parameter.getCompareTarget());
        param.setPrecision(this.parameter.getPrecision());
        param.setBelowAccuracyControl(this.parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラメータの区分値チェック
        if (DemandEmsAllUtility.validateBuildingNarrowing(param.getBuildingNarrowing(), param.getParentGroupId(),
                param.getBuildingNo()).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (!CheckUtility.isNullOrEmpty(param.getSumPeriodCalcType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(param.getSumPeriodCalcType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (!CheckUtility.isNullOrEmpty(param.getRangeUnit())
                && CheckUtility.isNullOrEmpty(ApiCodeValueConstants.RANGE_UNIT.getName(param.getRangeUnit()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (!CheckUtility.isNullOrEmpty(param.getBelowAccuracyControl()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(param.getBelowAccuracyControl()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(param.getRangeUnit())) {
            if (param.getSumPeriod() != null &&
                    !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                            ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_SUM_PERIOD_MAX_WEEK)) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }
        }
        else {
            if (param.getSumPeriod() != null &&
                    !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                            ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_ALL_SUM_PERIOD_MAX_YEAR)) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }
        }

        // 小数点精度の範囲チェック
        if (param.getPrecision() != null &&
                !CheckUtility.checkIntegerRange(param.getPrecision().toString(),
                ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandAllSummaryPowerWeekReductionAnalysisResult result = demandAllSummaryPowerWeekReductionAnalysisDao
                .query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
