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
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgMaxDemandJudgeDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMaxDemandJudgeParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgMaxDemandJudgeResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMaxDemandJudgeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 最大デマンド取得可能判定 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandOrgMaxDemandJudgeBean")
@RequestScoped
public class DemandOrgMaxDemandJudgeBean extends OsolApiBean<DemandOrgMaxDemandJudgeParameter>
        implements BaseApiBean<DemandOrgMaxDemandJudgeParameter, DemandOrgMaxDemandJudgeResponse> {

    private DemandOrgMaxDemandJudgeParameter parameter = new DemandOrgMaxDemandJudgeParameter();

    private DemandOrgMaxDemandJudgeResponse response = new DemandOrgMaxDemandJudgeResponse();

    @EJB
    private DemandOrgMaxDemandJudgeDao demandOrgMaxDemandJudgeDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgMaxDemandJudgeParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgMaxDemandJudgeParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgMaxDemandJudgeResponse execute() throws Exception {
        DemandOrgMaxDemandJudgeParameter param = new DemandOrgMaxDemandJudgeParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgMaxDemandJudgeResult result = demandOrgMaxDemandJudgeDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
