package jp.co.osaki.osol.api.bean.sms.collect.setting.concentrator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator.UpdateSmsConcentratorsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.UpdateSmsConcentratorParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequest;
import jp.co.osaki.osol.api.response.sms.collect.setting.concentrator.ListSmsConcentratorsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.ListSmsConcentratorsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * コンセントレータ管理 コンセントレータ更新 Beanクラス
 * @author kimura.m
 */
@Named(value = "smsCollectSettingConcentratorUpdateSmsConcentratorBean")
@RequestScoped
public class UpdateSmsConcentratorBean extends OsolApiBean<UpdateSmsConcentratorParameter>
implements BaseApiBean<UpdateSmsConcentratorParameter, ListSmsConcentratorsResponse> {

    private ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
    private UpdateSmsConcentratorParameter parameter = new UpdateSmsConcentratorParameter();

    @EJB
    UpdateSmsConcentratorsDao updateSmsConcentratorDao;

    @Override
    public UpdateSmsConcentratorParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(UpdateSmsConcentratorParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsConcentratorsResponse execute() throws Exception {
        UpdateSmsConcentratorParameter param = new UpdateSmsConcentratorParameter();
        UpdateSmsConcentratorRequest request = new UpdateSmsConcentratorRequest();
        param.setUpdateSmsConcentratorRequest(request);
        copyOsolApiParameter(this.parameter, param);
        param.getUpdateSmsConcentratorRequest().setDevId(this.parameter.getUpdateSmsConcentratorRequest().getDevId());
        param.getUpdateSmsConcentratorRequest().setConcentratorList(
                this.parameter.getUpdateSmsConcentratorRequest().getConcentratorList());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsConcentratorsResult result = updateSmsConcentratorDao.query(this.parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
