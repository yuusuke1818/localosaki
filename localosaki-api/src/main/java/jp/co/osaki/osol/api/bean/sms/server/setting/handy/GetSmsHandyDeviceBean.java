package jp.co.osaki.osol.api.bean.sms.server.setting.handy;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.handy.GetSmsHandyDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.GetSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.GetSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.GetSmsHandyDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "GetSmsHandyDeviceBean")
@RequestScoped
public class GetSmsHandyDeviceBean extends OsolApiBean<GetSmsHandyDeviceParameter>
        implements BaseApiBean<GetSmsHandyDeviceParameter, GetSmsHandyDeviceResponse> {

    private GetSmsHandyDeviceParameter parameter = new GetSmsHandyDeviceParameter();

    private GetSmsHandyDeviceResponse response = new GetSmsHandyDeviceResponse();

    @EJB
    GetSmsHandyDeviceDao GetSmsHandyDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GetSmsHandyDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GetSmsHandyDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsHandyDeviceResponse execute() throws Exception {
        GetSmsHandyDeviceParameter param = new GetSmsHandyDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsHandyDeviceResult result = GetSmsHandyDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
