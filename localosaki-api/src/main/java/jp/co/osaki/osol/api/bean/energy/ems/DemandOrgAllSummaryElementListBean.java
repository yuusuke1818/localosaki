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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgAllSummaryElementListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAllSummaryElementListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgAllSummaryElementListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAllSummaryElementListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況（個別）_建物・全集計要素情報取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgAllSummaryElementListBean")
@RequestScoped
public class DemandOrgAllSummaryElementListBean extends OsolApiBean<DemandOrgAllSummaryElementListParameter>
        implements BaseApiBean<DemandOrgAllSummaryElementListParameter, DemandOrgAllSummaryElementListResponse> {

    private DemandOrgAllSummaryElementListParameter parameter = new DemandOrgAllSummaryElementListParameter();

    private DemandOrgAllSummaryElementListResponse response = new DemandOrgAllSummaryElementListResponse();

    @EJB
    DemandOrgAllSummaryElementListDao demandOrgAllSummaryElementListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgAllSummaryElementListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgAllSummaryElementListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgAllSummaryElementListResponse execute() throws Exception {
        DemandOrgAllSummaryElementListParameter param = new DemandOrgAllSummaryElementListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgAllSummaryElementListResult result = demandOrgAllSummaryElementListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
