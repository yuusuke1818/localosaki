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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgSummaryMeasurementDataDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgSummaryMeasurementDataParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgSummaryMeasurementDataResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgSummaryMeasurementDataResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 前日までの計測データ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgSummaryMeasurementDataBean")
@RequestScoped
public class DemandOrgSummaryMeasurementDataBean extends OsolApiBean<DemandOrgSummaryMeasurementDataParameter>
        implements BaseApiBean<DemandOrgSummaryMeasurementDataParameter, DemandOrgSummaryMeasurementDataResponse> {

    private DemandOrgSummaryMeasurementDataParameter parameter = new DemandOrgSummaryMeasurementDataParameter();

    private DemandOrgSummaryMeasurementDataResponse response = new DemandOrgSummaryMeasurementDataResponse();

    @EJB
    DemandOrgSummaryMeasurementDataDao demandOrgSummaryMeasurementDataDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgSummaryMeasurementDataParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgSummaryMeasurementDataParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgSummaryMeasurementDataResponse execute() throws Exception {
        DemandOrgSummaryMeasurementDataParameter param = new DemandOrgSummaryMeasurementDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setYmd(this.parameter.getYmd());
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

        DemandOrgSummaryMeasurementDataResult result = demandOrgSummaryMeasurementDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
