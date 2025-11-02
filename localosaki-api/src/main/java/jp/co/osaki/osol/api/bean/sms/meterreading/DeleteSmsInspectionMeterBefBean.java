package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.DeleteSmsInspectionMeterBefDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.DeleteSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.DeleteSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.DeleteSmsInspectionMeterBefResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 確定前検針データ 削除 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "DeleteSmsInspectionMeterBefBean")
@RequestScoped
public class DeleteSmsInspectionMeterBefBean extends OsolApiBean<DeleteSmsInspectionMeterBefParameter>
        implements BaseApiBean<DeleteSmsInspectionMeterBefParameter, DeleteSmsInspectionMeterBefResponse> {

    private DeleteSmsInspectionMeterBefParameter parameter = new DeleteSmsInspectionMeterBefParameter();

    private DeleteSmsInspectionMeterBefResponse response = new DeleteSmsInspectionMeterBefResponse();

    @EJB
    private DeleteSmsInspectionMeterBefDao deleteSmsInspectionMeterBefDao;

    @Override
    public DeleteSmsInspectionMeterBefParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(DeleteSmsInspectionMeterBefParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public DeleteSmsInspectionMeterBefResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DeleteSmsInspectionMeterBefResult result = deleteSmsInspectionMeterBefDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
