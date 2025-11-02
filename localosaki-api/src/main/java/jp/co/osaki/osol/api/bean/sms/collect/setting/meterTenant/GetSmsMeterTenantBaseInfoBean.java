package jp.co.osaki.osol.api.bean.sms.collect.setting.meterTenant;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.GetSmsMeterTenantBaseInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターテナント情報取得
 * @author nishida.t
 *
 */

@Named(value = "GetSmsMeterTenantBaseInfoBean")
@RequestScoped
public class GetSmsMeterTenantBaseInfoBean extends OsolApiBean<GetSmsMeterTenantBaseInfoParameter>
implements BaseApiBean<GetSmsMeterTenantBaseInfoParameter, GetSmsMeterTenantBaseInfoResponse> {

    private GetSmsMeterTenantBaseInfoParameter parameter = new GetSmsMeterTenantBaseInfoParameter();

    private GetSmsMeterTenantBaseInfoResponse response = new GetSmsMeterTenantBaseInfoResponse();

    @EJB
    private GetSmsMeterTenantBaseInfoDao dao;

    @Override
    public GetSmsMeterTenantBaseInfoParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(GetSmsMeterTenantBaseInfoParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsMeterTenantBaseInfoResponse execute() throws Exception {
        GetSmsMeterTenantBaseInfoParameter param = new GetSmsMeterTenantBaseInfoParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        // テナント企業ID
        param.setTenantCorpId(this.parameter.getTenantCorpId());
        // テナント建物ID
        param.setTenantBuildingId(this.parameter.getTenantBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsMeterTenantBaseInfoResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
