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
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.verify.DemandOrgMonthlyKensyoDataDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgMonthlyKensyoDataParameter;
import jp.co.osaki.osol.api.response.energy.verify.DemandOrgMonthlyKensyoDataResponse;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgMonthlyKensyoDataResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 検証画面用建物検索 月 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "DemandOrgMonthlyKensyoDataBean")
@RequestScoped
public class DemandOrgMonthlyKensyoDataBean extends OsolApiBean<DemandOrgMonthlyKensyoDataParameter>
        implements BaseApiBean<DemandOrgMonthlyKensyoDataParameter, DemandOrgMonthlyKensyoDataResponse> {

    private DemandOrgMonthlyKensyoDataParameter parameter = new DemandOrgMonthlyKensyoDataParameter();

    private DemandOrgMonthlyKensyoDataResponse response = new DemandOrgMonthlyKensyoDataResponse();

    @EJB
    private DemandOrgMonthlyKensyoDataDao demandOrgMonthlyKensyoDataDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgMonthlyKensyoDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgMonthlyKensyoDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgMonthlyKensyoDataResponse execute() throws Exception {
        DemandOrgMonthlyKensyoDataParameter param = new DemandOrgMonthlyKensyoDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setYmd(this.parameter.getYmd());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_VERIFY__SUM_PERIOD_MAX_MONTH)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgMonthlyKensyoDataResult result = demandOrgMonthlyKensyoDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
