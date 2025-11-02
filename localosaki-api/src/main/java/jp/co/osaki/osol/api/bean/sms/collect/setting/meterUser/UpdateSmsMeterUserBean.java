package jp.co.osaki.osol.api.bean.sms.collect.setting.meterUser;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser.UpdateSmsMeterUserDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.UpdateSmsMeterUserParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.UpdateSmsMeterUserResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.UpdateSmsMeterUserResult;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "UpdateSmsMeterUserBean")
@RequestScoped
public class UpdateSmsMeterUserBean extends OsolApiBean<UpdateSmsMeterUserParameter>
    implements BaseApiBean<UpdateSmsMeterUserParameter, UpdateSmsMeterUserResponse> {

    private UpdateSmsMeterUserParameter parameter = new UpdateSmsMeterUserParameter();

    private UpdateSmsMeterUserResponse response = new UpdateSmsMeterUserResponse();

    @EJB
    private UpdateSmsMeterUserDao dao;

    @Override
    public UpdateSmsMeterUserParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsMeterUserParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsMeterUserResponse execute() throws Exception {
        UpdateSmsMeterUserParameter param = new UpdateSmsMeterUserParameter();
        copyOsolApiParameter(parameter, param);
        param.setCorpId(parameter.getCorpId());
        param.setPersonId(parameter.getPersonId());
        param.setPersonType(parameter.getPersonType());
        param.setPersonName(parameter.getPersonName());
        param.setPersonKana(parameter.getPersonKana());
        param.setDeptName(parameter.getDeptName());
        param.setPositionName(parameter.getPositionName());
        param.setTelNo(parameter.getTelNo());
        param.setFaxNo(parameter.getFaxNo());
        param.setMailAddress(parameter.getMailAddress());
        param.setPassword(parameter.getPassword());
        param.setTempPassword(parameter.getTempPassword());
        param.setPassMissCount(parameter.getPassMissCount());
        param.setAccountStopFlg(parameter.getAccountStopFlg());
        param.setTempPassExpirationDate(parameter.getTempPassExpirationDate());
        param.setApiPrivateFlg(parameter.getApiPrivateFlg());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        /**
         * 個別バリデーション
         */
        // パスワードと仮パスワードは一致していないといけない
        if (param.getTempPassword() != null && !param.getPassword().equals(param.getTempPassword())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 仮パスワードの有効期限の指定値は、1または7
        if (param.getTempPassExpirationDate() != null && !SmsConstants.METER_USER_EXPIRY_DATE.DAY.getVal().equals(param.getTempPassExpirationDate())
                && !SmsConstants.METER_USER_EXPIRY_DATE.WEEK.getVal().equals(param.getTempPassExpirationDate())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        UpdateSmsMeterUserResult result = dao.query(param);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }
}
