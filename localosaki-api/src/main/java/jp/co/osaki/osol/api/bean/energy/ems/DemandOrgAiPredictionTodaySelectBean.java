package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandOrgAiPredictionTodaySelectDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgAiPredictionTodaySelectParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandOrgAiPredictionTodaySelectResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgAiPredictionTodaySelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * エネルギー使用状況実績取得（個別・AI予測・本日の予測電力） Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "DemandOrgAiPredictionTodaySelectBean")
@RequestScoped
public class DemandOrgAiPredictionTodaySelectBean extends OsolApiBean<DemandOrgAiPredictionTodaySelectParameter>
    implements BaseApiBean<DemandOrgAiPredictionTodaySelectParameter, DemandOrgAiPredictionTodaySelectResponse>{

    private DemandOrgAiPredictionTodaySelectParameter parameter = new DemandOrgAiPredictionTodaySelectParameter();

    private DemandOrgAiPredictionTodaySelectResponse response = new DemandOrgAiPredictionTodaySelectResponse();

    @EJB
    DemandOrgAiPredictionTodaySelectDao demandOrgAiPredictionTodaySelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandOrgAiPredictionTodaySelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandOrgAiPredictionTodaySelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandOrgAiPredictionTodaySelectResponse execute() throws Exception {
        DemandOrgAiPredictionTodaySelectParameter param = new DemandOrgAiPredictionTodaySelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandOrgAiPredictionTodaySelectResult result = demandOrgAiPredictionTodaySelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }



}
