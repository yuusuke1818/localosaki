/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants.GROUP_CODE;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgWeekReportListTimeZoneUsedDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgWeekReportListTimeZoneUsedParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgWeekReportListTimeZoneUsedResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgWeekReportListTimeZoneUsedResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・週報・時間帯別使用量） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgWeekReportListTimeZoneUsedBean")
@RequestScoped
public class DemandOrgWeekReportListTimeZoneUsedBean extends OsolApiBean<DemandOrgWeekReportListTimeZoneUsedParameter>
        implements
        BaseApiBean<DemandOrgWeekReportListTimeZoneUsedParameter, DemandOrgWeekReportListTimeZoneUsedResponse> {

    private DemandOrgWeekReportListTimeZoneUsedParameter parameter = new DemandOrgWeekReportListTimeZoneUsedParameter();

    private DemandOrgWeekReportListTimeZoneUsedResponse response = new DemandOrgWeekReportListTimeZoneUsedResponse();

    @EJB
    DemandOrgWeekReportListTimeZoneUsedDao demandOrgWeekReportListTimeZoneUsedDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgWeekReportListTimeZoneUsedParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgWeekReportListTimeZoneUsedParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgWeekReportListTimeZoneUsedResponse execute() throws Exception {
        DemandOrgWeekReportListTimeZoneUsedParameter param = new DemandOrgWeekReportListTimeZoneUsedParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setFiscalYear(this.parameter.getFiscalYear());
        param.setWeekNo(this.parameter.getWeekNo());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setRangeUnit(this.parameter.getRangeUnit());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setPrecision(this.parameter.getPrecision());
        param.setBelowAccuracyControl(this.parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getSummaryKind()) && CheckUtility
                .isNullOrEmpty(genericTypeUtility.getKbnName(GROUP_CODE.SUMMARY_UNIT, param.getSummaryKind()))) {
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

        DemandOrgWeekReportListTimeZoneUsedResult result = demandOrgWeekReportListTimeZoneUsedDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
