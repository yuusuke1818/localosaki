package jp.co.osaki.osol.api.bean.sms.collect.setting.meterTenant;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターテナント情報（登録・更新）ApiBeen
 * @author nishida.t
 *
 */

@Named(value = "UpdateSmsMeterTenantBaseInfoBean")
@RequestScoped
public class UpdateSmsMeterTenantBaseInfoBean extends OsolApiBean<UpdateSmsMeterTenantBaseInfoParameter>
implements BaseApiBean<UpdateSmsMeterTenantBaseInfoParameter, UpdateSmsMeterTenantBaseInfoResponse> {

    private UpdateSmsMeterTenantBaseInfoParameter parameter = new UpdateSmsMeterTenantBaseInfoParameter();

    private UpdateSmsMeterTenantBaseInfoResponse response = new UpdateSmsMeterTenantBaseInfoResponse();

    @EJB
    private UpdateSmsMeterTenantBaseInfoDao dao;

    @Override
    public UpdateSmsMeterTenantBaseInfoParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsMeterTenantBaseInfoParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsMeterTenantBaseInfoResponse execute() throws Exception {
        UpdateSmsMeterTenantBaseInfoParameter param = new UpdateSmsMeterTenantBaseInfoParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setRequestSet(this.parameter.getRequestSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsMeterTenantBaseInfoResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
