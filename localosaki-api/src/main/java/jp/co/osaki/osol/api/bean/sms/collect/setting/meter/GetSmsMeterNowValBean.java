package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.GetSmsMeterNowValDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsMeterNowValParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.GetSmsMeterNowValResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetSmsMeterNowValResult;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター管理 最新の検針値取得 Beanクラス
 * @author kimura.m
 */
@Named(value = "GetSmsMeterNowValBean")
@RequestScoped
public class GetSmsMeterNowValBean extends OsolApiBean<GetSmsMeterNowValParameter>
implements BaseApiBean<GetSmsMeterNowValParameter, GetSmsMeterNowValResponse> {

    private GetSmsMeterNowValResponse response = new GetSmsMeterNowValResponse();
    private GetSmsMeterNowValParameter parameter = new GetSmsMeterNowValParameter();

    @EJB
    GetSmsMeterNowValDao getSmsMeterNowValDao;

    @Override
    public GetSmsMeterNowValParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(GetSmsMeterNowValParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public GetSmsMeterNowValResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // ハンディは対応外
        if(parameter.getDevId().startsWith(DEVICE_KIND.HANDY.getVal())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsMeterNowValParameter param = new GetSmsMeterNowValParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setMeterMngId(this.parameter.getMeterMngId());

        GetSmsMeterNowValResult result = getSmsMeterNowValDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
