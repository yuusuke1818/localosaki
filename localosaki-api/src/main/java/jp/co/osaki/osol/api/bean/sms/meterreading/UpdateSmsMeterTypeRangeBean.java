package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.UpdateSmsMeterTypeRangeDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsMeterTypeRangeResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsMeterTypeRangeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メータ種別設定情報 + メータ種別従量値情報 登録・更新・削除 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "UpdateSmsMeterTypeRangeBean")
@RequestScoped
public class UpdateSmsMeterTypeRangeBean extends OsolApiBean<UpdateSmsMeterTypeRangeParameter>
        implements BaseApiBean<UpdateSmsMeterTypeRangeParameter, UpdateSmsMeterTypeRangeResponse> {

    private UpdateSmsMeterTypeRangeParameter parameter = new UpdateSmsMeterTypeRangeParameter();

    private UpdateSmsMeterTypeRangeResponse response = new UpdateSmsMeterTypeRangeResponse();

    @EJB
    private UpdateSmsMeterTypeRangeDao updateSmsMeterTypeRangeDao;

    @Override
    public UpdateSmsMeterTypeRangeParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsMeterTypeRangeParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsMeterTypeRangeResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsMeterTypeRangeResult result = updateSmsMeterTypeRangeDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
