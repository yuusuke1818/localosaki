package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ名更新 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingMeterGroupUpdateSmsMeterGroupNameBean")
@RequestScoped
public class UpdateSmsMeterGroupNameBean extends OsolApiBean<UpdateSmsMeterGroupNameParameter>
implements BaseApiBean<UpdateSmsMeterGroupNameParameter, UpdateSmsMeterGroupNameResponse> {

    private UpdateSmsMeterGroupNameResponse response = new UpdateSmsMeterGroupNameResponse();
    private UpdateSmsMeterGroupNameParameter parameter = new UpdateSmsMeterGroupNameParameter();

    @EJB
    UpdateSmsMeterGroupNameDao updateSmsMeterGroupNameDao;

    @Override
    public UpdateSmsMeterGroupNameParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(UpdateSmsMeterGroupNameParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public UpdateSmsMeterGroupNameResponse execute() throws Exception {
        UpdateSmsMeterGroupNameParameter param = new UpdateSmsMeterGroupNameParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setMeterGroupId(this.parameter.getMeterGroupId());
        param.setMeterGroupName(this.parameter.getMeterGroupName());
        param.setVersion(parameter.getVersion());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsMeterGroupNameResult result = updateSmsMeterGroupNameDao.query(this.parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
