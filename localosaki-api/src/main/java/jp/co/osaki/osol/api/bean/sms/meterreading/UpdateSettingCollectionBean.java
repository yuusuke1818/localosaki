package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.UpdateSettingCollectionDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSettingCollectionParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSettingCollectionResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSettingCollectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 設定一括収集 Beanクラス
 * @author kobayashi.sho
 */
@Named(value = "UpdateSettingCollectionBean")
@RequestScoped
public class UpdateSettingCollectionBean extends OsolApiBean<UpdateSettingCollectionParameter>
        implements BaseApiBean<UpdateSettingCollectionParameter, UpdateSettingCollectionResponse> {

    private UpdateSettingCollectionParameter parameter = new UpdateSettingCollectionParameter();

    private UpdateSettingCollectionResponse response = new UpdateSettingCollectionResponse();

    @EJB
    private UpdateSettingCollectionDao updateSettingCollectionDao;

    @Override
    public UpdateSettingCollectionParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSettingCollectionParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSettingCollectionResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }
        if (parameter.getCommands().getList() == null || parameter.getCommands().getList().isEmpty()) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 新規登録・更新
        UpdateSettingCollectionResult result = updateSettingCollectionDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
