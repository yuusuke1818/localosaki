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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayReportListMaxDemandDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListMaxDemandParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayReportListMaxDemandResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListMaxDemandResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・日報・最大デマンド） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgDayReportListMaxDemandBean")
@RequestScoped
public class DemandOrgDayReportListMaxDemandBean extends OsolApiBean<DemandOrgDayReportListMaxDemandParameter>
        implements BaseApiBean<DemandOrgDayReportListMaxDemandParameter, DemandOrgDayReportListMaxDemandResponse> {

    private DemandOrgDayReportListMaxDemandParameter parameter = new DemandOrgDayReportListMaxDemandParameter();

    private DemandOrgDayReportListMaxDemandResponse response = new DemandOrgDayReportListMaxDemandResponse();

    @EJB
    DemandOrgDayReportListMaxDemandDao demandOrgDayReportListMaxDemandDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayReportListMaxDemandParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayReportListMaxDemandParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayReportListMaxDemandResponse execute() throws Exception {
        DemandOrgDayReportListMaxDemandParameter param = new DemandOrgDayReportListMaxDemandParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
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

        DemandOrgDayReportListMaxDemandResult result = demandOrgDayReportListMaxDemandDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
