/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.plan;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.plan.PlanFulfillmentResultDao;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentResultParameter;
import jp.co.osaki.osol.api.response.plan.PlanFulfillmentResultResponse;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentResultResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行実績操作 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "PlanFulfillmentResultBean")
@RequestScoped
public class PlanFulfillmentResultBean extends OsolApiBean<PlanFulfillmentResultParameter>
        implements BaseApiBean<PlanFulfillmentResultParameter, PlanFulfillmentResultResponse> {

    private PlanFulfillmentResultParameter parameter = new PlanFulfillmentResultParameter();

    private PlanFulfillmentResultResponse response = new PlanFulfillmentResultResponse();

    @EJB
    PlanFulfillmentResultDao planFulfillmentResultDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanFulfillmentResultParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanFulfillmentResultParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public PlanFulfillmentResultResponse execute() throws Exception {
        PlanFulfillmentResultParameter param = new PlanFulfillmentResultParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setPlanFulfillmentId(this.parameter.getPlanFulfillmentId());
        param.setPlanStartDate(this.parameter.getPlanStartDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanFulfillmentResultResult result = planFulfillmentResultDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
