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
import jp.co.osaki.osol.api.dao.energy.ems.DemandAllSummaryBuildingMaxValueDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryBuildingMaxValueDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandAllSummaryBuildingMaxValueDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryBuildingMaxValueDataResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンドデータ実績取得処理（全体・建物・テナント一覧・最大値） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandAllSummaryBuildingMaxValueDataBean")
@RequestScoped
public class DemandAllSummaryBuildingMaxValueDataBean extends OsolApiBean<DemandAllSummaryBuildingMaxValueDataParameter>
        implements
        BaseApiBean<DemandAllSummaryBuildingMaxValueDataParameter, DemandAllSummaryBuildingMaxValueDataResponse> {

    private DemandAllSummaryBuildingMaxValueDataParameter parameter = new DemandAllSummaryBuildingMaxValueDataParameter();

    private DemandAllSummaryBuildingMaxValueDataResponse response = new DemandAllSummaryBuildingMaxValueDataResponse();

    @EJB
    DemandAllSummaryBuildingMaxValueDataDao demandAllSummaryBuildingMaxValueDataDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandAllSummaryBuildingMaxValueDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandAllSummaryBuildingMaxValueDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandAllSummaryBuildingMaxValueDataResponse execute() throws Exception {
        DemandAllSummaryBuildingMaxValueDataParameter param = new DemandAllSummaryBuildingMaxValueDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingNarrowing(this.parameter.getBuildingNarrowing());
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setSortKey(this.parameter.getSortKey());
        param.setSortOrder(this.parameter.getSortOrder());
        param.setFilterKey(this.parameter.getFilterKey());
        param.setYmd(this.parameter.getYmd());
        param.setDecimalBuilding(this.parameter.getDecimalBuilding());
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
        if (!CheckUtility.isNullOrEmpty(param.getSummaryKind()) && CheckUtility
                .isNullOrEmpty(genericTypeUtility.getKbnName(GROUP_CODE.SUMMARY_UNIT, param.getSummaryKind()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;

        }
        if (!CheckUtility.isNullOrEmpty(param.getSortKey())
                && CheckUtility.isNullOrEmpty(ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.getName(param.getSortKey()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;

        }
        if (!CheckUtility.isNullOrEmpty(param.getSortOrder())
                && CheckUtility.isNullOrEmpty(ApiCodeValueConstants.SORT_ORDER.getName(param.getSortOrder()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;

        }
        if (!CheckUtility.isNullOrEmpty(param.getBelowAccuracyControl()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(param.getBelowAccuracyControl()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 小数点精度の範囲チェック
        if (param.getDecimalBuilding() != null &&
                !CheckUtility.checkIntegerRange(param.getDecimalBuilding().toString(),
                ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)
                ) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandAllSummaryBuildingMaxValueDataResult result = demandAllSummaryBuildingMaxValueDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
