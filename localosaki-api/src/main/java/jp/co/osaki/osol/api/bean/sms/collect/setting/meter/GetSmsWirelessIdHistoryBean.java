package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.GetSmsWirelessIdHistoryDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.GetSmsWirelessIdHistoryParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.GetSmsWirelessIdHistoryResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetSmsWirelessIdHistoryResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * @author sagi_h
 *
 */
@Named(value = "GetSmsWirelessIdHistoryBean")
@RequestScoped
public class GetSmsWirelessIdHistoryBean extends OsolApiBean<GetSmsWirelessIdHistoryParameter>
        implements BaseApiBean<GetSmsWirelessIdHistoryParameter, GetSmsWirelessIdHistoryResponse> {

    private GetSmsWirelessIdHistoryResponse response = new GetSmsWirelessIdHistoryResponse();
    private GetSmsWirelessIdHistoryParameter parameter = new GetSmsWirelessIdHistoryParameter();

    @EJB
    GetSmsWirelessIdHistoryDao getSmsWirelessIdHistoryDao;

    @Override
    public GetSmsWirelessIdHistoryParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(GetSmsWirelessIdHistoryParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsWirelessIdHistoryResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsWirelessIdHistoryParameter param = new GetSmsWirelessIdHistoryParameter();

        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setAmount(this.parameter.getAmount());

        GetSmsWirelessIdHistoryResult result = getSmsWirelessIdHistoryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
