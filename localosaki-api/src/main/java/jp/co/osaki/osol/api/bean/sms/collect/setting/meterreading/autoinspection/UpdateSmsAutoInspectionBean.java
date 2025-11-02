package jp.co.osaki.osol.api.bean.sms.collect.setting.meterreading.autoinspection;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ更新API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "UpdateSmsAutoInspectionBean")
@RequestScoped
public class UpdateSmsAutoInspectionBean extends OsolApiBean<UpdateSmsAutoInspectionParameter>
        implements BaseApiBean<UpdateSmsAutoInspectionParameter, UpdateSmsAutoInspectionResponse> {

    private UpdateSmsAutoInspectionParameter parameter = new UpdateSmsAutoInspectionParameter();

    private UpdateSmsAutoInspectionResponse response = new UpdateSmsAutoInspectionResponse();

    @EJB
    UpdateSmsAutoInspectionDao dao;

    @Override
    public UpdateSmsAutoInspectionParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(UpdateSmsAutoInspectionParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public UpdateSmsAutoInspectionResponse execute() throws Exception {
        UpdateSmsAutoInspectionParameter param = new UpdateSmsAutoInspectionParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());
        param.setRequest(this.parameter.getRequest());

        UpdateSmsAutoInspectionResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
