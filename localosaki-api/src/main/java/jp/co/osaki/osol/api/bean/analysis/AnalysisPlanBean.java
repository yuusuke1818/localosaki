/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisPlanDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisPlanParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisPlanResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisPlanResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計分析-計画履行 Beanクラス
 *
 * @author n-takada
 */
@Named(value = "AnalysisPlanBean")
@RequestScoped
public class AnalysisPlanBean extends OsolApiBean<AnalysisPlanParameter>
        implements BaseApiBean<AnalysisPlanParameter, AnalysisPlanResponse> {

    private AnalysisPlanParameter parameter = new AnalysisPlanParameter();

    private AnalysisPlanResponse response = new AnalysisPlanResponse();

    @EJB
    AnalysisPlanDao analysisPlanDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AnalysisPlanParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AnalysisPlanParameter parameter) {
        this.parameter = parameter;

    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AnalysisPlanResponse execute() throws Exception {
        AnalysisPlanParameter param = new AnalysisPlanParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setPlanStartDate(this.parameter.getPlanStartDate());
        param.setPlanEndDate(this.parameter.getPlanEndDate());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingNarrowing(this.parameter.getBuildingNarrowing());
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisPlanResult result = analysisPlanDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
