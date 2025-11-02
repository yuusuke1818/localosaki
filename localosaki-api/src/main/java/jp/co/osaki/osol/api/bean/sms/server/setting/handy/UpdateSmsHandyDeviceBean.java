package jp.co.osaki.osol.api.bean.sms.server.setting.handy;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.handy.UpdateSmsHandyDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.UpdateSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.handy.UpdateSmsHandyDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.UpdateSmsHandyDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "UpdateSmsHandyDeviceBean")
@RequestScoped
public class UpdateSmsHandyDeviceBean extends OsolApiBean<UpdateSmsHandyDeviceParameter>
        implements BaseApiBean<UpdateSmsHandyDeviceParameter, UpdateSmsHandyDeviceResponse> {

    private UpdateSmsHandyDeviceParameter parameter = new UpdateSmsHandyDeviceParameter();

    private UpdateSmsHandyDeviceResponse response = new UpdateSmsHandyDeviceResponse();

    @EJB
    UpdateSmsHandyDeviceDao UpdateSmsHandyDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsHandyDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsHandyDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsHandyDeviceResponse execute() throws Exception {
        UpdateSmsHandyDeviceParameter param = new UpdateSmsHandyDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(parameter.getDevId());
        param.setDevPw(parameter.getDevPw());
        param.setName(parameter.getName());
        param.setDelFlg(parameter.getDelFlg());
        param.setVersion(parameter.getVersion());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsHandyDeviceResult result = UpdateSmsHandyDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
