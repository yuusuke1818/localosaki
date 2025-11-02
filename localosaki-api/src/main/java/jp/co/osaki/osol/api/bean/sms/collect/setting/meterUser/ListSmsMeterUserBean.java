package jp.co.osaki.osol.api.bean.sms.collect.setting.meterUser;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser.ListSmsMeterUserDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.ListSmsMeterUserParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.ListSmsMeterUserResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.ListSmsMeterUserResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "ListSmsMeterUserBean")
@RequestScoped
public class ListSmsMeterUserBean extends OsolApiBean<ListSmsMeterUserParameter>
    implements BaseApiBean<ListSmsMeterUserParameter, ListSmsMeterUserResponse> {

    private ListSmsMeterUserParameter parameter = new ListSmsMeterUserParameter();

    private ListSmsMeterUserResponse response = new ListSmsMeterUserResponse();

    @EJB
    private ListSmsMeterUserDao dao;

    @Override
    public ListSmsMeterUserParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsMeterUserParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsMeterUserResponse execute() throws Exception {
        ListSmsMeterUserParameter param = new ListSmsMeterUserParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setRequest(parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterUserResult result = dao.query(param);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }
}
