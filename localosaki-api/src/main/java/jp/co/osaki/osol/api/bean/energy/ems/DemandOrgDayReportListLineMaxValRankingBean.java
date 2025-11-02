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
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayReportListLineMaxValRankingDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListLineMaxValRankingParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayReportListLineMaxValRankingResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListLineMaxValRankingResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・日報・系統最大値ランキング） Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "DemandOrgDayReportListLineMaxValRankingBean")
@RequestScoped
public class DemandOrgDayReportListLineMaxValRankingBean extends OsolApiBean<DemandOrgDayReportListLineMaxValRankingParameter>
        implements BaseApiBean<DemandOrgDayReportListLineMaxValRankingParameter, DemandOrgDayReportListLineMaxValRankingResponse> {

    private DemandOrgDayReportListLineMaxValRankingParameter parameter = new DemandOrgDayReportListLineMaxValRankingParameter();

    private DemandOrgDayReportListLineMaxValRankingResponse response = new DemandOrgDayReportListLineMaxValRankingResponse();

    @EJB
    DemandOrgDayReportListLineMaxValRankingDao DemandOrgDayReportListLineMaxValRankingDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayReportListLineMaxValRankingParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayReportListLineMaxValRankingParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayReportListLineMaxValRankingResponse execute() throws Exception {
        DemandOrgDayReportListLineMaxValRankingParameter param = new DemandOrgDayReportListLineMaxValRankingParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());
        param.setRankCount(this.parameter.getRankCount());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_ORG_SUM_PERIOD_MAX_YEAR)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計順位の範囲チェック
        if (param.getRankCount() != null &&
                !CheckUtility.checkIntegerRange(param.getRankCount().toString(),
                        ApiSimpleConstants.DEMAND_ORG_RANK_COUNT_MIN, ApiSimpleConstants.DEMAND_ORG_RANK_COUNT_MAX)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgDayReportListLineMaxValRankingResult result = DemandOrgDayReportListLineMaxValRankingDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
