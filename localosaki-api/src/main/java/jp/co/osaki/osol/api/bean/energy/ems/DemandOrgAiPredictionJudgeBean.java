package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgAiPredictionJudgeDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionJudgeParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgAiPredictionJudgeResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionJudgeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * AI予測表示判定 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "DemandOrgAiPredictionJudgeBean")
@RequestScoped
public class DemandOrgAiPredictionJudgeBean extends OsolApiBean<DemandOrgAiPredictionJudgeParameter>
    implements BaseApiBean<DemandOrgAiPredictionJudgeParameter, DemandOrgAiPredictionJudgeResponse>{

    private DemandOrgAiPredictionJudgeParameter parameter = new DemandOrgAiPredictionJudgeParameter();

    private DemandOrgAiPredictionJudgeResponse response = new DemandOrgAiPredictionJudgeResponse();

    @EJB
    DemandOrgAiPredictionJudgeDao demandOrgAiPredictionJudgeDao;


    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgAiPredictionJudgeParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgAiPredictionJudgeParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgAiPredictionJudgeResponse execute() throws Exception {
        DemandOrgAiPredictionJudgeParameter param = new DemandOrgAiPredictionJudgeParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgAiPredictionJudgeResult result = demandOrgAiPredictionJudgeDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
