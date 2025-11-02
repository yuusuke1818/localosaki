package jp.co.osaki.osol.api.bean.sms.collect.setting.meterTenant;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターテナント情報所属テナント取得
 * @author maruta.y
 *
 */

@Named(value = "ListSmsMeterTenantBelongTenants")
@RequestScoped
public class ListSmsMeterTenantBelongTenantsBean extends OsolApiBean<ListSmsMeterTenantBelongTenantsParameter>
implements BaseApiBean<ListSmsMeterTenantBelongTenantsParameter, ListSmsMeterTenantBelongTenantsResponse> {

    private ListSmsMeterTenantBelongTenantsParameter parameter = new ListSmsMeterTenantBelongTenantsParameter();

    private ListSmsMeterTenantBelongTenantsResponse response = new ListSmsMeterTenantBelongTenantsResponse();

    @EJB
    private ListSmsMeterTenantBelongTenantsDao dao;

    @Override
    public ListSmsMeterTenantBelongTenantsParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsMeterTenantBelongTenantsParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsMeterTenantBelongTenantsResponse execute() throws Exception {
        ListSmsMeterTenantBelongTenantsParameter param = new ListSmsMeterTenantBelongTenantsParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterTenantBelongTenantsResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
