package jp.co.osaki.osol.api.bean.sms.server.setting.collect;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.collect.GetSmsCollectDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.GetSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.collect.GetSmsCollectDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.GetSmsCollectDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "GetSmsCollectDeviceBean")
@RequestScoped
public class GetSmsCollectDeviceBean extends OsolApiBean<GetSmsCollectDeviceParameter>
        implements BaseApiBean<GetSmsCollectDeviceParameter, GetSmsCollectDeviceResponse> {

    private GetSmsCollectDeviceParameter parameter = new GetSmsCollectDeviceParameter();

    private GetSmsCollectDeviceResponse response = new GetSmsCollectDeviceResponse();

    @EJB
    GetSmsCollectDeviceDao GetSmsCollectDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GetSmsCollectDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GetSmsCollectDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsCollectDeviceResponse execute() throws Exception {
        GetSmsCollectDeviceParameter param = new GetSmsCollectDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsCollectDeviceResult result = GetSmsCollectDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
