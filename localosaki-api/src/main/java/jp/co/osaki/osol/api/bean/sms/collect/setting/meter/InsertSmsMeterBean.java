package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.InsertSmsMeterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.InsertSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.InsertSmsMeterResult;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター登録 Beanクラス
 * @author sagi_h
 *
 */
@Named(value = "InsertSmsMeterBean")
@RequestScoped
public class InsertSmsMeterBean extends OsolApiBean<InsertSmsMeterParameter>
        implements BaseApiBean<InsertSmsMeterParameter, InsertSmsMeterResponse> {

    private InsertSmsMeterParameter parameter = new InsertSmsMeterParameter();
    private InsertSmsMeterResponse response = new InsertSmsMeterResponse();

    @EJB
    InsertSmsMeterDao insertSmsMeterDao;

    @Override
    public InsertSmsMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        InsertSmsMeterParameter param = new InsertSmsMeterParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResult(this.parameter.getResult());

        // 機器制御からの呼び出しフラグ
        if (this.parameter.getFromDeviceCtrl() != null) {
            param.setFromDeviceCtrl(this.parameter.getFromDeviceCtrl());
        } else {
            param.setFromDeviceCtrl(false);
        }

        // メーター種類識別(パラメーター直接指定のmeterKindは無視するよう変更)
        if (param.getResult().getDevId().startsWith(DEVICE_KIND.HANDY.getVal())) {
            param.setMeterKind(METER_KIND.HANDY.getVal());

            // リレー無線IDの重複チェック
            final String hop1Id = param.getResult().getHop1Id();
            final String hop2Id = param.getResult().getHop2Id();
            final String hop3Id = param.getResult().getHop3Id();
            if (((hop1Id != null) && hop1Id.equals(hop2Id)) || ((hop2Id != null) && hop2Id.equals(hop3Id))
                    || ((hop3Id != null) && hop3Id.equals(hop1Id))) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("リレー無線IDが重複しています。");
                return response;

            }
        } else if (param.getResult().getDevId().startsWith(DEVICE_KIND.IOTR.getVal())) {
            param.setMeterKind(METER_KIND.IOTR.getVal());
        } else if (param.getResult().getDevId().startsWith(DEVICE_KIND.OCR.getVal())) {
            param.setMeterKind(METER_KIND.OCR.getVal());
        } else if (param.getResult().getMeterId().startsWith(METER_KIND.SMART.getVal())) {
            param.setMeterKind(METER_KIND.SMART.getVal());
        } else if (param.getResult().getMeterId().startsWith(METER_KIND.PULSE.getVal())) {
            param.setMeterKind(METER_KIND.PULSE.getVal());
        } else {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            response.setError("装置IDまたは計器IDが不正です。");
            return response;
        }

        try {
            InsertSmsMeterResult result = insertSmsMeterDao.query(param);
            response.setResultCode(OsolApiResultCode.API_OK);
            response.setResult(result);
        } catch (Exception e) {
            final String mes = e.getMessage();
            if (mes.equals("meterIdDuplicate")) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("入力した計器IDは既に登録されています。");
            } else if (mes.equals("meterMngIdDuplicate")) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("入力したメーター管理番号は既に登録されています。");
            } else if (mes.equals("wirelessIdDuplicate")) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("入力した無線IDは既に登録されています。");
            } else {
                throw e;
            }
        }

        return response;
    }

    @Override
    public InsertSmsMeterParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(InsertSmsMeterParameter parameter) {
        this.parameter = parameter;
    }

}
