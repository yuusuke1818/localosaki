package jp.co.osaki.osol.api.bean.sms.collect.setting.meterUser;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser.GetSmsMeterUserDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.GetSmsMeterUserParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.GetSmsMeterUserResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.GetSmsMeterUserResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "GetSmsMeterUserBean")
@RequestScoped
public class GetSmsMeterUserBean extends OsolApiBean<GetSmsMeterUserParameter>
    implements BaseApiBean<GetSmsMeterUserParameter, GetSmsMeterUserResponse> {

    private GetSmsMeterUserParameter parameter = new GetSmsMeterUserParameter();

    private GetSmsMeterUserResponse response = new GetSmsMeterUserResponse();

    @EJB
    private GetSmsMeterUserDao dao;

    @Override
    public GetSmsMeterUserParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(GetSmsMeterUserParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsMeterUserResponse execute() throws Exception {
        GetSmsMeterUserParameter param = new GetSmsMeterUserParameter();
        copyOsolApiParameter(this.parameter, param);

        // 企業ID
        param.setCorpId(this.parameter.getCorpId());
        // 担当者ID
        param.setPersonId(this.parameter.getPersonId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsMeterUserResult result = dao.query(param);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }
}
