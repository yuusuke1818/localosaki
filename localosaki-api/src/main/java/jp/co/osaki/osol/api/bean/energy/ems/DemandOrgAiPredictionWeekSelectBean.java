package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgAiPredictionWeekSelectDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionWeekSelectParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgAiPredictionWeekSelectResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionWeekSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・AI予測・週間予測電力量） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "DemandOrgAiPredictionWeekSelectBean")
@RequestScoped
public class DemandOrgAiPredictionWeekSelectBean extends OsolApiBean<DemandOrgAiPredictionWeekSelectParameter>
    implements BaseApiBean<DemandOrgAiPredictionWeekSelectParameter, DemandOrgAiPredictionWeekSelectResponse>{

    private DemandOrgAiPredictionWeekSelectParameter parameter = new DemandOrgAiPredictionWeekSelectParameter();

    private DemandOrgAiPredictionWeekSelectResponse response = new DemandOrgAiPredictionWeekSelectResponse();

    @EJB
    DemandOrgAiPredictionWeekSelectDao demandOrgAiPredictionWeekSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgAiPredictionWeekSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgAiPredictionWeekSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgAiPredictionWeekSelectResponse execute() throws Exception {
        DemandOrgAiPredictionWeekSelectParameter param = new DemandOrgAiPredictionWeekSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgAiPredictionWeekSelectResult result = demandOrgAiPredictionWeekSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
