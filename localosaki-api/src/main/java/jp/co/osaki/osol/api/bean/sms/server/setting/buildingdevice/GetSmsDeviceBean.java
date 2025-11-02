package jp.co.osaki.osol.api.bean.sms.server.setting.buildingdevice;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice.GetSmsDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.GetSmsDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.GetSmsDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.GetSmsDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "GetSmsDeviceBean")
@RequestScoped
public class GetSmsDeviceBean extends OsolApiBean<GetSmsDeviceParameter>
        implements BaseApiBean<GetSmsDeviceParameter, GetSmsDeviceResponse> {

    private GetSmsDeviceParameter parameter = new GetSmsDeviceParameter();

    private GetSmsDeviceResponse response = new GetSmsDeviceResponse();

    @EJB
    GetSmsDeviceDao GetSmsDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GetSmsDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GetSmsDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsDeviceResponse execute() throws Exception {
        GetSmsDeviceParameter param = new GetSmsDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsDeviceResult result = GetSmsDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
