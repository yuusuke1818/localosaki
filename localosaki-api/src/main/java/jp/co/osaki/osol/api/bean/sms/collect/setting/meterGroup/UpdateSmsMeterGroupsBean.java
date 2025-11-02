package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsRequest;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ更新 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingMeterGroupUpdateSmsMeterGroupsBean")
@RequestScoped
public class UpdateSmsMeterGroupsBean extends OsolApiBean<UpdateSmsMeterGroupsParameter>
implements BaseApiBean<UpdateSmsMeterGroupsParameter, ListSmsMeterGroupsResponse> {

    private ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
    private UpdateSmsMeterGroupsParameter parameter = new UpdateSmsMeterGroupsParameter();

    @EJB
    UpdateSmsMeterGroupsDao updateSmsMeterGroupDao;

    @Override
    public UpdateSmsMeterGroupsParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(UpdateSmsMeterGroupsParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMeterGroupsResponse execute() throws Exception {
        UpdateSmsMeterGroupsParameter param = new UpdateSmsMeterGroupsParameter();
        UpdateSmsMeterGroupsRequest request = new UpdateSmsMeterGroupsRequest();
        param.setUpdateMeterGroupsRequest(request);
        copyOsolApiParameter(this.parameter, param);
        param.getUpdateMeterGroupsRequest().setCorpId(this.parameter.getUpdateMeterGroupsRequest().getCorpId());
        param.getUpdateMeterGroupsRequest().setMeterGroupId(this.parameter.getUpdateMeterGroupsRequest().getMeterGroupId());
        param.getUpdateMeterGroupsRequest().setBuildingId(this.parameter.getUpdateMeterGroupsRequest().getBuildingId());
        param.getUpdateMeterGroupsRequest().setMeterGroupList(
                this.parameter.getUpdateMeterGroupsRequest().getMeterGroupList());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterGroupsResult result = updateSmsMeterGroupDao.query(this.parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
