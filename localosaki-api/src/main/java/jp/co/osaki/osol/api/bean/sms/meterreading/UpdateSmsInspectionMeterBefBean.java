package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.UpdateSmsInspectionMeterBefDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.UpdateSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsInspectionMeterBefResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 確定前検針データ検針連番 登録・更新 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "UpdateSmsInspectionMeterBefBean")
@RequestScoped
public class UpdateSmsInspectionMeterBefBean extends OsolApiBean<UpdateSmsInspectionMeterBefParameter>
        implements BaseApiBean<UpdateSmsInspectionMeterBefParameter, UpdateSmsInspectionMeterBefResponse> {

    private UpdateSmsInspectionMeterBefParameter parameter = new UpdateSmsInspectionMeterBefParameter();

    private UpdateSmsInspectionMeterBefResponse response = new UpdateSmsInspectionMeterBefResponse();

    @EJB
    private UpdateSmsInspectionMeterBefDao updateSmsInspectionMeterBefDao;

    @Override
    public UpdateSmsInspectionMeterBefParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsInspectionMeterBefParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsInspectionMeterBefResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 新規登録・更新
        UpdateSmsInspectionMeterBefResult result = updateSmsInspectionMeterBefDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
