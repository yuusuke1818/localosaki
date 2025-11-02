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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgBuildingTenantInfoDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgBuildingTenantInfoParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgBuildingTenantInfoResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgBuildingTenantInfoResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況（個別）_建物・テナント情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgBuildingTenantInfoBean")
@RequestScoped
public class DemandOrgBuildingTenantInfoBean extends OsolApiBean<DemandOrgBuildingTenantInfoParameter>
        implements BaseApiBean<DemandOrgBuildingTenantInfoParameter, DemandOrgBuildingTenantInfoResponse> {

    private DemandOrgBuildingTenantInfoParameter parameter = new DemandOrgBuildingTenantInfoParameter();

    private DemandOrgBuildingTenantInfoResponse response = new DemandOrgBuildingTenantInfoResponse();

    @EJB
    DemandOrgBuildingTenantInfoDao demandOrgBuildingTenantInfoDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgBuildingTenantInfoParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgBuildingTenantInfoParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgBuildingTenantInfoResponse execute() throws Exception {
        DemandOrgBuildingTenantInfoParameter param = new DemandOrgBuildingTenantInfoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setYmd(this.parameter.getYmd());
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

        // 小数点精度の範囲チェック
        if (param.getPrecision() != null &&
                !CheckUtility.checkIntegerRange(param.getPrecision().toString(),
                ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)
                ) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandOrgBuildingTenantInfoResult result = demandOrgBuildingTenantInfoDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
