package jp.co.osaki.osol.api.bean.sms.collect.setting.concentrator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator.GetSmsConcentratorDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.GetSmsConcentratorParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.concentrator.GetSmsConcentratorResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.GetSmsConcentratorResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * コンセントレータ管理 コンセントレータ情報取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingConcentratorGetSmsConcentratorBean")
@RequestScoped
public class GetSmsConcentratorBean extends OsolApiBean<GetSmsConcentratorParameter>
implements BaseApiBean<GetSmsConcentratorParameter, GetSmsConcentratorResponse> {

    private GetSmsConcentratorResponse response = new GetSmsConcentratorResponse();
    private GetSmsConcentratorParameter parameter = new GetSmsConcentratorParameter();

    @EJB
    GetSmsConcentratorDao getSmsConcentratorDao;

    @Override
    public GetSmsConcentratorParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(GetSmsConcentratorParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public GetSmsConcentratorResponse execute() throws Exception {
        GetSmsConcentratorParameter param = new GetSmsConcentratorParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setConcentId(this.parameter.getConcentId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsConcentratorResult result = getSmsConcentratorDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
