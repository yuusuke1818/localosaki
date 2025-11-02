package jp.co.osaki.osol.api.bean.sms.server.setting.ocr;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.ocr.GetSmsOcrDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.GetSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.ocr.GetSmsOcrDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.GetSmsOcrDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "GetSmsOcrDeviceBean")
@RequestScoped
public class GetSmsOcrDeviceBean extends OsolApiBean<GetSmsOcrDeviceParameter>
        implements BaseApiBean<GetSmsOcrDeviceParameter, GetSmsOcrDeviceResponse> {

    private GetSmsOcrDeviceParameter parameter = new GetSmsOcrDeviceParameter();

    private GetSmsOcrDeviceResponse response = new GetSmsOcrDeviceResponse();

    @EJB
    GetSmsOcrDeviceDao GetSmsOcrDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GetSmsOcrDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GetSmsOcrDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsOcrDeviceResponse execute() throws Exception {
        GetSmsOcrDeviceParameter param = new GetSmsOcrDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsOcrDeviceResult result = GetSmsOcrDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
