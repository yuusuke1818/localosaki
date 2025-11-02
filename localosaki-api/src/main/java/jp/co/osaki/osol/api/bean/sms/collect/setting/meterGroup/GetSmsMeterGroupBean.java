package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.GetSmsMeterGroupDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.GetSmsMeterGroupParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.GetSmsMeterGroupResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.GetSmsMeterGroupResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingmeterGroupGetSmsMeterGroupBean")
@RequestScoped
public class GetSmsMeterGroupBean extends OsolApiBean<GetSmsMeterGroupParameter>
implements BaseApiBean<GetSmsMeterGroupParameter, GetSmsMeterGroupResponse> {

    private GetSmsMeterGroupResponse response = new GetSmsMeterGroupResponse();
    private GetSmsMeterGroupParameter parameter = new GetSmsMeterGroupParameter();

    @EJB
    GetSmsMeterGroupDao getSmsMeterGroupDao;

    @Override
    public GetSmsMeterGroupParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(GetSmsMeterGroupParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public GetSmsMeterGroupResponse execute() throws Exception {
        GetSmsMeterGroupParameter param = new GetSmsMeterGroupParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setCorpId(this.parameter.getCorpId());
        param.setMeterGroupId(this.parameter.getMeterGroupId());
        param.setMeterMngId(this.parameter.getMeterMngId());
        param.setDevId(this.parameter.getDevId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsMeterGroupResult result = getSmsMeterGroupDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
