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
import jp.co.osaki.osol.api.dao.plan.PlanFulfillmentResultWriteDao;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentResultWriteParameter;
import jp.co.osaki.osol.api.response.plan.PlanFulfillmentResultWriteResponse;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentResultWriteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行実績入力 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "PlanFulfillmentResultWriteBean")
@RequestScoped
public class PlanFulfillmentResultWriteBean extends OsolApiBean<PlanFulfillmentResultWriteParameter>
        implements BaseApiBean<PlanFulfillmentResultWriteParameter, PlanFulfillmentResultWriteResponse> {

    private PlanFulfillmentResultWriteParameter parameter = new PlanFulfillmentResultWriteParameter();

    private PlanFulfillmentResultWriteResponse response = new PlanFulfillmentResultWriteResponse();

    @EJB
    PlanFulfillmentResultDao planFulfillmentResultDao;

    @EJB
    PlanFulfillmentResultWriteDao planFulfillmentResultWriteDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanFulfillmentResultWriteParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanFulfillmentResultWriteParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public PlanFulfillmentResultWriteResponse execute() throws Exception {
        PlanFulfillmentResultWriteParameter param = new PlanFulfillmentResultWriteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setPlanFulfillmentId(this.parameter.getPlanFulfillmentId());
        param.setPlanStartDate(this.parameter.getPlanStartDate());
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanFulfillmentResultWriteResult result = planFulfillmentResultWriteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
