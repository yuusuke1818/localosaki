package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.UpdateSmsMeterAlertDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterAlertParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateSmsMeterAlertResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.UpdateSmsMeteAlertrResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター登録内容・アラート設定内容変更 Beanクラス
 * @author kobayashi.sho
 */
@Named(value = "UpdateSmsMeterAlertBean")
@RequestScoped
public class UpdateSmsMeterAlertBean extends OsolApiBean<UpdateSmsMeterAlertParameter>
        implements BaseApiBean<UpdateSmsMeterAlertParameter, UpdateSmsMeterAlertResponse> {
    private UpdateSmsMeterAlertParameter parameter = new UpdateSmsMeterAlertParameter();
    private UpdateSmsMeterAlertResponse response = new UpdateSmsMeterAlertResponse();

    @EJB
    UpdateSmsMeterAlertDao updateSmsMeterAlertDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsMeterAlertParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsMeterAlertParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public UpdateSmsMeterAlertResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // アラート設定 更新
        UpdateSmsMeteAlertrResult result = updateSmsMeterAlertDao.query(parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
