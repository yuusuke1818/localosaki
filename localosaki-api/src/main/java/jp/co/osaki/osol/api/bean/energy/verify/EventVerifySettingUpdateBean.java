package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.EventVerifySettingUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.verify.EventVerifySettingUpdateParameter;
import jp.co.osaki.osol.api.response.energy.verify.EventVerifySettingUpdateResponse;
import jp.co.osaki.osol.api.result.energy.verify.EventVerifySettingUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * イベント制御設定変更更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "EventVerifySettingUpdateBean")
@RequestScoped
public class EventVerifySettingUpdateBean extends OsolApiBean<EventVerifySettingUpdateParameter>
        implements BaseApiBean<EventVerifySettingUpdateParameter, EventVerifySettingUpdateResponse> {

    private EventVerifySettingUpdateParameter parameter = new EventVerifySettingUpdateParameter();

    private EventVerifySettingUpdateResponse response = new EventVerifySettingUpdateResponse();

    @EJB
    EventVerifySettingUpdateDao eventVerifySettingUpdateDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public EventVerifySettingUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(EventVerifySettingUpdateParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public EventVerifySettingUpdateResponse execute() throws Exception {
        EventVerifySettingUpdateParameter param = new EventVerifySettingUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setCorpVersion(this.parameter.getCorpVersion());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingVersion(this.parameter.getBuildingVersion());
        param.setSmId(this.parameter.getSmId());
        param.setSmVersion(this.parameter.getSmVersion());
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        EventVerifySettingUpdateResult result = eventVerifySettingUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
