package jp.co.osaki.osol.api.bean.sms.manulinsp.reserveinsp;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspDao;
import jp.co.osaki.osol.api.parameter.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspParameter;
import jp.co.osaki.osol.api.response.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspResponse;
import jp.co.osaki.osol.api.result.sms.manualinsp.reserveinsp.UpdateManualInspectionReserveInspResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 任意検針 予約検針日時更新API Beanクラス.
 * @author kobayashi.sho
 */
@Named(value = "UpdateManualInspectionReserveInspBean")
@RequestScoped
public class UpdateManualInspectionReserveInspBean extends OsolApiBean<UpdateManualInspectionReserveInspParameter>
        implements BaseApiBean<UpdateManualInspectionReserveInspParameter, UpdateManualInspectionReserveInspResponse> {

    @EJB
    private UpdateManualInspectionReserveInspDao dao;

    private UpdateManualInspectionReserveInspParameter parameter = new UpdateManualInspectionReserveInspParameter();

    private UpdateManualInspectionReserveInspResponse response = new UpdateManualInspectionReserveInspResponse();

    @Override
    public UpdateManualInspectionReserveInspParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateManualInspectionReserveInspParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateManualInspectionReserveInspResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // アラート設定 更新
        UpdateManualInspectionReserveInspResult result = dao.query(parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
