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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgDayReportPointCountListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportPointCountListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgDayReportPointCountListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportPointCountListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・日報・手入力ポイント）デマンド日報ポイントデータ数取得 Beanクラス
 *
 * @author y-maruta
 */
@Named(value = "DemandOrgDayReportPointCountListBean")
@RequestScoped
public class DemandOrgDayReportPointCountListBean extends OsolApiBean<DemandOrgDayReportPointCountListParameter>
        implements BaseApiBean<DemandOrgDayReportPointCountListParameter, DemandOrgDayReportPointCountListResponse> {

    private DemandOrgDayReportPointCountListParameter parameter = new DemandOrgDayReportPointCountListParameter();

    private DemandOrgDayReportPointCountListResponse response = new DemandOrgDayReportPointCountListResponse();

    @EJB
    DemandOrgDayReportPointCountListDao DemandOrgDayReportPointCountListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgDayReportPointCountListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgDayReportPointCountListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgDayReportPointCountListResponse execute() throws Exception {
        DemandOrgDayReportPointCountListParameter param = new DemandOrgDayReportPointCountListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setMeasurementDate(this.parameter.getMeasurementDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgDayReportPointCountListResult result = DemandOrgDayReportPointCountListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
