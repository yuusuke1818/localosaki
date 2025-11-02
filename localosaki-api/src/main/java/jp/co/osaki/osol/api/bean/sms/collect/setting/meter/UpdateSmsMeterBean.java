package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.UpdateSmsMeterDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.UpdateSmsMeterResult;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.co.osaki.sms.SmsConstants.METER_UPDATE_PATTERN;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター登録内容・設定内容変更 Beanクラス
 * @author sagi_h
 *
 */
@Named(value = "UpdateSmsMeterBean")
@RequestScoped
public class UpdateSmsMeterBean extends OsolApiBean<UpdateSmsMeterParameter>
        implements BaseApiBean<UpdateSmsMeterParameter, UpdateSmsMeterResponse> {
    private UpdateSmsMeterParameter parameter = new UpdateSmsMeterParameter();
    private UpdateSmsMeterResponse response = new UpdateSmsMeterResponse();

    @EJB
    UpdateSmsMeterDao updateSmsMeterDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public UpdateSmsMeterParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(UpdateSmsMeterParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public UpdateSmsMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        UpdateSmsMeterParameter param = new UpdateSmsMeterParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResult(this.parameter.getResult());

        // 更新パターン
        param.setUpdatePattern(this.parameter.getUpdatePattern());

        // 機器制御からの呼び出しフラグ
        if (this.parameter.getFromDeviceCtrl() != null) {
            param.setFromDeviceCtrl(this.parameter.getFromDeviceCtrl());
        } else {
            param.setFromDeviceCtrl(false);
        }

        // 機器に送信する
        param.setSendFlg(this.parameter.getSendFlg());

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

        if (METER_UPDATE_PATTERN.SETTING.getVal().equals(param.getUpdatePattern())) {
            // (「設定内容変更」は複数選択があるので) 選択行チェック
            if (param.getResult().getRequestSetList() == null
                    || param.getResult().getRequestSetList().isEmpty()) {
                // 選択行なしエラー  ※選択行なしでは「設定変更」ボタンが非活性になるため発生しないはずのエラー
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("更新対象の行が取得できません。");
                return response;
            }
        }

        // 機器に送信する
        param.setSendFlg(this.parameter.getSendFlg());

        try {
            UpdateSmsMeterResult result = updateSmsMeterDao.query(param);
            response.setResultCode(OsolApiResultCode.API_OK);
            response.setResult(result);
        } catch (Exception e) {
            final String mes = e.getMessage();
            if (mes.equals("meterIdDuplicate")) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("入力した計器IDは既に登録されています。");
            } else if (mes.equals("wirelessIdDuplicate")) {
                response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
                response.setError("入力した無線IDは既に登録されています。");
            } else {
                throw e;
            }
        }

        return response;
    }

}
