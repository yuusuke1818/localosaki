package jp.co.osaki.osol.api.bean.sms.server.setting.ocr;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.ocr.UpdateSmsOcrDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.UpdateSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.ocr.UpdateSmsOcrDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.UpdateSmsOcrDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "UpdateSmsOcrDeviceBean")
@RequestScoped
public class UpdateSmsOcrDeviceBean extends OsolApiBean<UpdateSmsOcrDeviceParameter>
        implements BaseApiBean<UpdateSmsOcrDeviceParameter, UpdateSmsOcrDeviceResponse> {

    private UpdateSmsOcrDeviceParameter parameter = new UpdateSmsOcrDeviceParameter();

    private UpdateSmsOcrDeviceResponse response = new UpdateSmsOcrDeviceResponse();

    @EJB
    UpdateSmsOcrDeviceDao UpdateSmsOcrDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsOcrDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsOcrDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsOcrDeviceResponse execute() throws Exception {
        UpdateSmsOcrDeviceParameter param = new UpdateSmsOcrDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(parameter.getDevId());
        param.setName(parameter.getName());
        param.setExamNoticeMonth(parameter.getExamNoticeMonth());
        param.setDelFlg(parameter.getDelFlg());
        param.setVersion(parameter.getVersion());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsOcrDeviceResult result = UpdateSmsOcrDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
