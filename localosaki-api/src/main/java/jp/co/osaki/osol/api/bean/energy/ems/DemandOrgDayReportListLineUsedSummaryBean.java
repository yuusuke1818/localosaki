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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayReportListLineUsedSummaryDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListLineUsedSummaryParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayReportListLineUsedSummaryResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListLineUsedSummaryResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・日報・日報集計表） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgDayReportListLineUsedSummaryBean")
@RequestScoped
public class DemandOrgDayReportListLineUsedSummaryBean
        extends OsolApiBean<DemandOrgDayReportListLineUsedSummaryParameter>
        implements
        BaseApiBean<DemandOrgDayReportListLineUsedSummaryParameter, DemandOrgDayReportListLineUsedSummaryResponse> {

    private DemandOrgDayReportListLineUsedSummaryParameter parameter = new DemandOrgDayReportListLineUsedSummaryParameter();

    private DemandOrgDayReportListLineUsedSummaryResponse response = new DemandOrgDayReportListLineUsedSummaryResponse();

    @EJB
    DemandOrgDayReportListLineUsedSummaryDao demandOrgDayReportListLineUsedSummaryDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayReportListLineUsedSummaryParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayReportListLineUsedSummaryParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayReportListLineUsedSummaryResponse execute() throws Exception {
        DemandOrgDayReportListLineUsedSummaryParameter param = new DemandOrgDayReportListLineUsedSummaryParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setSmId(this.parameter.getSmId());
        param.setPointNo(this.parameter.getPointNo());
        param.setYmd(this.parameter.getYmd());
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
        if (ApiCodeValueConstants.RANGE_UNIT_MONTHLY.DAY.getVal().equals(param.getRangeUnit())) {
            if (param.getSumPeriod() != null &&
                    !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                            ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_ORG_SUM_PERIOD_MAX_DAY)) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }
        }
        else {
            if (param.getSumPeriod() != null &&
                    !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                            ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_ORG_SUM_PERIOD_MAX_MONTH)) {
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

        DemandOrgDayReportListLineUsedSummaryResult result = demandOrgDayReportListLineUsedSummaryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
