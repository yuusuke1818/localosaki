package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.GetSmsMeterTypeRangeDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.GetSmsMeterTypeRangeResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.GetSmsMeterTypeRangeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メータ種別設定情報 + メータ種別従量値情報 検索 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "GetSmsMeterTypeRangeBean")
@RequestScoped
public class GetSmsMeterTypeRangeBean extends OsolApiBean<GetSmsMeterTypeRangeParameter>
        implements BaseApiBean<GetSmsMeterTypeRangeParameter, GetSmsMeterTypeRangeResponse> {

    private GetSmsMeterTypeRangeParameter parameter = new GetSmsMeterTypeRangeParameter();

    private GetSmsMeterTypeRangeResponse response = new GetSmsMeterTypeRangeResponse();

    @EJB
    private GetSmsMeterTypeRangeDao getSmsMeterTypeRangeDao;

    @Override
    public GetSmsMeterTypeRangeParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(GetSmsMeterTypeRangeParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public GetSmsMeterTypeRangeResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GetSmsMeterTypeRangeResult result = getSmsMeterTypeRangeDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
