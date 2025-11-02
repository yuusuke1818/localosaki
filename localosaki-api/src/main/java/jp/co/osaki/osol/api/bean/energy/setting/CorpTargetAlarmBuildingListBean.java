package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpTargetAlarmBuildingListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmBuildingListParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpTargetAlarmBuildingListResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmBuildingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業目標超過警報建物一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "CorpTargetAlarmBuildingListBean")
@RequestScoped
public class CorpTargetAlarmBuildingListBean extends OsolApiBean<CorpTargetAlarmBuildingListParameter>
        implements BaseApiBean<CorpTargetAlarmBuildingListParameter, CorpTargetAlarmBuildingListResponse> {

    @EJB
    private CorpTargetAlarmBuildingListDao corpTargetAlarmBuildingListDao;

    private CorpTargetAlarmBuildingListParameter parameter = new CorpTargetAlarmBuildingListParameter();

    private CorpTargetAlarmBuildingListResponse response = new CorpTargetAlarmBuildingListResponse();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CorpTargetAlarmBuildingListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpTargetAlarmBuildingListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CorpTargetAlarmBuildingListResponse execute() throws Exception {
        CorpTargetAlarmBuildingListParameter param = new CorpTargetAlarmBuildingListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpTargetAlarmBuildingListResult result = corpTargetAlarmBuildingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
