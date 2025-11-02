package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.UpdateSmsVariousDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsVariousParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsVariousResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsVariousResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 各種設定情報登録 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "UpdateSmsVariousBean")
@RequestScoped
public class UpdateSmsVariousBean extends OsolApiBean<UpdateSmsVariousParameter>
        implements BaseApiBean<UpdateSmsVariousParameter, UpdateSmsVariousResponse> {

    private UpdateSmsVariousParameter parameter = new UpdateSmsVariousParameter();

    private UpdateSmsVariousResponse response = new UpdateSmsVariousResponse();

    @EJB
    private UpdateSmsVariousDao updateSmsVariousDao;

    @Override
    public UpdateSmsVariousParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsVariousParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsVariousResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // DB更新 ※排他エラーの時は OptimisticLockException になる
        UpdateSmsVariousResult result = updateSmsVariousDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
