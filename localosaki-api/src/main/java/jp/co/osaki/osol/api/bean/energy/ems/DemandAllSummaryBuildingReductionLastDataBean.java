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
import jp.co.osaki.osol.api.dao.energy.ems.DemandAllSummaryBuildingReductionLastDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryBuildingReductionLastDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandAllSummaryBuildingReductionLastDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryBuildingReductionLastDataResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンドデータ実績取得処理（全体・建物・テナント一覧・削減率（前年比））　Callクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandAllSummaryBuildingReductionLastDataBean")
@RequestScoped
public class DemandAllSummaryBuildingReductionLastDataBean
        extends OsolApiBean<DemandAllSummaryBuildingReductionLastDataParameter>
        implements
        BaseApiBean<DemandAllSummaryBuildingReductionLastDataParameter, DemandAllSummaryBuildingReductionLastDataResponse> {

    private DemandAllSummaryBuildingReductionLastDataParameter parameter = new DemandAllSummaryBuildingReductionLastDataParameter();

    private DemandAllSummaryBuildingReductionLastDataResponse response = new DemandAllSummaryBuildingReductionLastDataResponse();

    @EJB
    DemandAllSummaryBuildingReductionLastDataDao demandAllSummaryBuildingReductionLastDataDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandAllSummaryBuildingReductionLastDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandAllSummaryBuildingReductionLastDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandAllSummaryBuildingReductionLastDataResponse execute() throws Exception {
        DemandAllSummaryBuildingReductionLastDataParameter param = new DemandAllSummaryBuildingReductionLastDataParameter();
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
        if (param.getPrecision() != null &&
                !CheckUtility.checkIntegerRange(param.getPrecision().toString(),
                ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)
                ) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandAllSummaryBuildingReductionLastDataResult result = demandAllSummaryBuildingReductionLastDataDao
                .query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
