package jp.co.osaki.osol.api.bean.sms.collect.setting.repeater;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.repeater.UpdateSmsRepeatersDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.UpdateSmsRepeaterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.repeater.UpdateSmsRepeaterRequest;
import jp.co.osaki.osol.api.response.sms.collect.setting.repeater.ListSmsRepeatersResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.ListSmsRepeatersResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 中継装置管理 中継装置更新 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingRepeaterUpdateSmsRepeaterBean")
@RequestScoped
public class UpdateSmsRepeaterBean extends OsolApiBean<UpdateSmsRepeaterParameter>
implements BaseApiBean<UpdateSmsRepeaterParameter, ListSmsRepeatersResponse> {

    private ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
    private UpdateSmsRepeaterParameter parameter = new UpdateSmsRepeaterParameter();

    @EJB
    UpdateSmsRepeatersDao updateSmsRepeaterDao;

    @Override
    public UpdateSmsRepeaterParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(UpdateSmsRepeaterParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsRepeatersResponse execute() throws Exception {
        UpdateSmsRepeaterParameter param = new UpdateSmsRepeaterParameter();
        UpdateSmsRepeaterRequest request = new UpdateSmsRepeaterRequest();
        param.setUpdateSmsRepeaterRequest(request);
        copyOsolApiParameter(this.parameter, param);
        param.getUpdateSmsRepeaterRequest().setDevId(this.parameter.getUpdateSmsRepeaterRequest().getDevId());
        param.getUpdateSmsRepeaterRequest().setRepeaterList(
                this.parameter.getUpdateSmsRepeaterRequest().getRepeaterList());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsRepeatersResult result = updateSmsRepeaterDao.query(this.parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
