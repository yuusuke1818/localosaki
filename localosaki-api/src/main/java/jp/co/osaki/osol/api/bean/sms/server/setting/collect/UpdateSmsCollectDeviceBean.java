package jp.co.osaki.osol.api.bean.sms.server.setting.collect;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.server.setting.collect.UpdateSmsCollectDeviceDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.UpdateSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.response.sms.server.setting.collect.UpdateSmsCollectDeviceResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.UpdateSmsCollectDeviceResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "UpdateSmsCollectDeviceBean")
@RequestScoped
public class UpdateSmsCollectDeviceBean extends OsolApiBean<UpdateSmsCollectDeviceParameter>
        implements BaseApiBean<UpdateSmsCollectDeviceParameter, UpdateSmsCollectDeviceResponse> {

    private UpdateSmsCollectDeviceParameter parameter = new UpdateSmsCollectDeviceParameter();

    private UpdateSmsCollectDeviceResponse response = new UpdateSmsCollectDeviceResponse();

    @EJB
    UpdateSmsCollectDeviceDao UpdateSmsCollectDeviceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsCollectDeviceParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsCollectDeviceParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsCollectDeviceResponse execute() throws Exception {
        UpdateSmsCollectDeviceParameter param = new UpdateSmsCollectDeviceParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(parameter.getDevId());
        param.setDevPw(parameter.getDevPw());
        param.setName(parameter.getName());
        param.setMemo(parameter.getMemo());
        param.setIpAddr(parameter.getIpAddr());
        param.setHomeDirectory(parameter.getHomeDirectory());
        param.setExamNoticeMonth(parameter.getExamNoticeMonth());
        param.setAlertDisableFlg(parameter.getAlertDisableFlg());
        param.setRevFlg(parameter.getRevFlg());
        param.setCommInterval(parameter.getCommInterval());
        param.setDelFlg(parameter.getDelFlg());
        param.setVersion(parameter.getVersion());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsCollectDeviceResult result = UpdateSmsCollectDeviceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
