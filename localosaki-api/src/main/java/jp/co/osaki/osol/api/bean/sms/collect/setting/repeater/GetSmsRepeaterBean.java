package jp.co.osaki.osol.api.bean.sms.collect.setting.repeater;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.repeater.GetSmsRepeaterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.GetSmsRepeaterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.repeater.GetSmsRepeaterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.GetSmsRepeaterResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 中継装置管理 中継装置情報取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingRepeaterGetSmsConcentratorBean")
@RequestScoped
public class GetSmsRepeaterBean extends OsolApiBean<GetSmsRepeaterParameter>
implements BaseApiBean<GetSmsRepeaterParameter, GetSmsRepeaterResponse> {

    private GetSmsRepeaterResponse response = new GetSmsRepeaterResponse();
    private GetSmsRepeaterParameter parameter = new GetSmsRepeaterParameter();

    @EJB
    GetSmsRepeaterDao getSmsRepeaterDao;

    @Override
    public GetSmsRepeaterParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(GetSmsRepeaterParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public GetSmsRepeaterResponse execute() throws Exception {
        GetSmsRepeaterParameter param = new GetSmsRepeaterParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setRepeaterMngId(this.parameter.getRepeaterMngId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsRepeaterResult result = getSmsRepeaterDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
