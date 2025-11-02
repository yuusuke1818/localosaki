package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.GetSmsVariousDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsVariousParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.GetSmsVariousResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.GetSmsVariousResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 各種設定情報検索 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "GetSmsVariousBean")
@RequestScoped
public class GetSmsVariousBean extends OsolApiBean<GetSmsVariousParameter>
        implements BaseApiBean<GetSmsVariousParameter, GetSmsVariousResponse> {

    private GetSmsVariousParameter parameter = new GetSmsVariousParameter();

    private GetSmsVariousResponse response = new GetSmsVariousResponse();

    @EJB
    private GetSmsVariousDao getSmsVariousDao;

    @Override
    public GetSmsVariousParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(GetSmsVariousParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsVariousResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsVariousResult result = getSmsVariousDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
