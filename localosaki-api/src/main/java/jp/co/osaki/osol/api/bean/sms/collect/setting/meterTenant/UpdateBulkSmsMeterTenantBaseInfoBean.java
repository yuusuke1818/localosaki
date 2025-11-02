package jp.co.osaki.osol.api.bean.sms.collect.setting.meterTenant;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターテナント情報（一括登録・更新）ApiBeen
 * @author maruta.y
 *
 */

@Named(value = "UpdateBulkSmsMeterTenantBaseInfoBean")
@RequestScoped
public class UpdateBulkSmsMeterTenantBaseInfoBean extends OsolApiBean<UpdateBulkSmsMeterTenantBaseInfoParameter>
implements BaseApiBean<UpdateBulkSmsMeterTenantBaseInfoParameter, UpdateBulkSmsMeterTenantBaseInfoResponse> {

    private UpdateBulkSmsMeterTenantBaseInfoParameter parameter = new UpdateBulkSmsMeterTenantBaseInfoParameter();

    private UpdateBulkSmsMeterTenantBaseInfoResponse response = new UpdateBulkSmsMeterTenantBaseInfoResponse();

    @EJB
    private UpdateBulkSmsMeterTenantBaseInfoDao dao;

    @Override
    public UpdateBulkSmsMeterTenantBaseInfoParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateBulkSmsMeterTenantBaseInfoParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateBulkSmsMeterTenantBaseInfoResponse execute() throws Exception {
        UpdateBulkSmsMeterTenantBaseInfoParameter param = new UpdateBulkSmsMeterTenantBaseInfoParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateBulkSmsMeterTenantBaseInfoResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
