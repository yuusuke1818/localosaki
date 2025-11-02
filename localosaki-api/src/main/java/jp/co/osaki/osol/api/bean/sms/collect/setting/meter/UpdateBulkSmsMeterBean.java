package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.UpdateBulkSmsMeterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateBulkSmsMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateBulkSmsMeterResponse;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター登録内容・設定内容一括変更 Beanクラス
 * @author maruta.y
 *
 */
@Named(value = "UpdateBulkSmsMeterBean")
@RequestScoped
public class UpdateBulkSmsMeterBean extends OsolApiBean<UpdateBulkSmsMeterParameter>
        implements BaseApiBean<UpdateBulkSmsMeterParameter, UpdateBulkSmsMeterResponse> {
    private UpdateBulkSmsMeterParameter parameter = new UpdateBulkSmsMeterParameter();
    private UpdateBulkSmsMeterResponse response = new UpdateBulkSmsMeterResponse();

    @EJB
    UpdateBulkSmsMeterDao updateBulkSmsMeterDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateBulkSmsMeterParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateBulkSmsMeterParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public UpdateBulkSmsMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }
        if (parameter.getRequest() == null ||
                parameter.getRequest().getRequestSetList() == null ||
                parameter.getRequest().getRequestSetList().isEmpty()) {
            return response;
        }

        UpdateBulkSmsMeterParameter param = new UpdateBulkSmsMeterParameter();
        copyOsolApiParameter(this.parameter, param);
        // メーター種類識別
        if (this.parameter.getRequest().getDevId().startsWith(DEVICE_KIND.IOTR.getVal())) {
            param.setMeterKind(METER_KIND.IOTR.getVal());
        } else if (this.parameter.getRequest().getDevId().startsWith(DEVICE_KIND.HANDY.getVal())) {
            param.setMeterKind(METER_KIND.HANDY.getVal());
        } else if (this.parameter.getRequest().getDevId().startsWith(DEVICE_KIND.OCR.getVal())) {
            param.setMeterKind(METER_KIND.OCR.getVal());
        } else {
            param.setMeterKind(this.parameter.getMeterKind());
        }
        param.setUpdatePattern(this.parameter.getUpdatePattern());
        param.setFromDeviceCtrl(this.parameter.getFromDeviceCtrl());
        param.setRequest(this.parameter.getRequest());
        param.setLteMLumpRegistExecFlg(this.parameter.getLteMLumpRegistExecFlg());

        List<String> resultList = updateBulkSmsMeterDao.query(param);

        response.setResultCode(OsolApiResultCode.API_OK);
        response.setErrorList(resultList);
        return response;
    }

}
