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
import jp.co.osaki.osol.api.dao.plan.SelectInputBuildingPlanDao;
import jp.co.osaki.osol.api.parameter.plan.SelectInputBuildingPlanParameter;
import jp.co.osaki.osol.api.response.plan.SelectInputBuildingPlanResponse;
import jp.co.osaki.osol.api.result.plan.SelectInputBuildingPlanResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行入力建物検索 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "SelectInputBuildingPlanBean")
@RequestScoped
public class SelectInputBuildingPlanBean extends OsolApiBean<SelectInputBuildingPlanParameter>
        implements BaseApiBean<SelectInputBuildingPlanParameter, SelectInputBuildingPlanResponse> {

    private SelectInputBuildingPlanParameter parameter = new SelectInputBuildingPlanParameter();

    private SelectInputBuildingPlanResponse response = new SelectInputBuildingPlanResponse();

    @EJB
    SelectInputBuildingPlanDao selectInputBuildingPlanDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SelectInputBuildingPlanParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SelectInputBuildingPlanParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SelectInputBuildingPlanResponse execute() throws Exception {
        SelectInputBuildingPlanParameter param = new SelectInputBuildingPlanParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setPlanStartDate(this.parameter.getPlanStartDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SelectInputBuildingPlanResult result = selectInputBuildingPlanDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
