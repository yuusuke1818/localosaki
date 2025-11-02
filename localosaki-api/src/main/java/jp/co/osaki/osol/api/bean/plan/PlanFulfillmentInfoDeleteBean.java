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
import jp.co.osaki.osol.api.dao.plan.PlanFulfillmentInfoDeleteDao;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentInfoDeleteParameter;
import jp.co.osaki.osol.api.response.plan.PlanFulfillmentInfoDeleteResponse;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentInfoDeleteResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行情報削除 Beanクラス（後で実装・削除？）
 *
 * @author n-takada
 */
@Named(value = "PlanFulfillmentInfoDeleteBean")
@RequestScoped
public class PlanFulfillmentInfoDeleteBean extends OsolApiBean<PlanFulfillmentInfoDeleteParameter>
        implements BaseApiBean<PlanFulfillmentInfoDeleteParameter, PlanFulfillmentInfoDeleteResponse> {

    private PlanFulfillmentInfoDeleteParameter parameter = new PlanFulfillmentInfoDeleteParameter();

    private PlanFulfillmentInfoDeleteResponse response = new PlanFulfillmentInfoDeleteResponse();

    @EJB
    PlanFulfillmentInfoDeleteDao planFulfillmentInfoDeleteDao;

    @Override
    public PlanFulfillmentInfoDeleteParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(PlanFulfillmentInfoDeleteParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public PlanFulfillmentInfoDeleteResponse execute() throws Exception {
        PlanFulfillmentInfoDeleteParameter param = new PlanFulfillmentInfoDeleteParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setPlanFulfillmentId(this.parameter.getPlanFulfillmentId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanFulfillmentInfoDeleteResult result = planFulfillmentInfoDeleteDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
