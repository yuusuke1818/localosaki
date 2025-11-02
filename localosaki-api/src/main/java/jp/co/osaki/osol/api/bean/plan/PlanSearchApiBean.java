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
import jp.co.osaki.osol.api.dao.plan.PlanSearchApiDao;
import jp.co.osaki.osol.api.parameter.plan.PlanSearchApiParameter;
import jp.co.osaki.osol.api.response.plan.PlanSearchApiResponse;
import jp.co.osaki.osol.api.result.plan.PlanSearchApiResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行検索 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "PlanSearchApiBean")
@RequestScoped
public class PlanSearchApiBean extends OsolApiBean<PlanSearchApiParameter>
        implements BaseApiBean<PlanSearchApiParameter, PlanSearchApiResponse> {

    private PlanSearchApiParameter parameter = new PlanSearchApiParameter();

    private PlanSearchApiResponse response = new PlanSearchApiResponse();

    @EJB
    PlanSearchApiDao planSearchApiDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanSearchApiParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanSearchApiParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public PlanSearchApiResponse execute() throws Exception {
        PlanSearchApiParameter param = new PlanSearchApiParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setPlanFulfillmentName(this.parameter.getPlanFulfillmentName());
        param.setPlanStartYm(this.parameter.getPlanStartYm());
        param.setPlanEndYm(this.parameter.getPlanEndYm());
        param.setPlanFulfillmentTargetList(this.parameter.getPlanFulfillmentTargetList());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanSearchApiResponse response = new PlanSearchApiResponse();
        PlanSearchApiResult result = planSearchApiDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
