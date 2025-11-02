/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.verify.DemandOrgDailyKensyoDataDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgDailyKensyoDataParameter;
import jp.co.osaki.osol.api.response.energy.verify.DemandOrgDailyKensyoDataResponse;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyKensyoDataResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況・日報・デマンド制御検証 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "DemandOrgDailyKensyoDataBean")
@RequestScoped
public class DemandOrgDailyKensyoDataBean extends OsolApiBean<DemandOrgDailyKensyoDataParameter>
        implements BaseApiBean<DemandOrgDailyKensyoDataParameter, DemandOrgDailyKensyoDataResponse> {

    private DemandOrgDailyKensyoDataParameter parameter = new DemandOrgDailyKensyoDataParameter();

    private DemandOrgDailyKensyoDataResponse response = new DemandOrgDailyKensyoDataResponse();

    @EJB
    private DemandOrgDailyKensyoDataDao demandOrgDailyKensyoDataDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDailyKensyoDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDailyKensyoDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDailyKensyoDataResponse execute() throws Exception {
        DemandOrgDailyKensyoDataParameter param = new DemandOrgDailyKensyoDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setYmd(this.parameter.getYmd());
        param.setTimesOfDay(this.parameter.getTimesOfDay());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラーメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getSumPeriodCalcType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(param.getSumPeriodCalcType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_VERIFY_SUM_PERIOD_MAX_HOUR)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgDailyKensyoDataResult result = demandOrgDailyKensyoDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
