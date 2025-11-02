package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsRequest;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ削除 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingMeterGroupDeleteSmsMeterGroupsBean")
@RequestScoped
public class DeleteSmsMeterGroupsBean extends OsolApiBean<DeleteSmsMeterGroupsParameter>
implements BaseApiBean<DeleteSmsMeterGroupsParameter, DeleteSmsMeterGroupsResponse> {

    private DeleteSmsMeterGroupsResponse response = new DeleteSmsMeterGroupsResponse();
    private DeleteSmsMeterGroupsParameter parameter = new DeleteSmsMeterGroupsParameter();

    @EJB
    DeleteSmsMeterGroupsDao deleteSmsMeterGroupDao;

    @Override
    public DeleteSmsMeterGroupsParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(DeleteSmsMeterGroupsParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public DeleteSmsMeterGroupsResponse execute() throws Exception {
        DeleteSmsMeterGroupsParameter param = new DeleteSmsMeterGroupsParameter();
        DeleteSmsMeterGroupsRequest request = new DeleteSmsMeterGroupsRequest();
        param.setDeleteMeterGroupsRequest(request);
        copyOsolApiParameter(this.parameter, param);
        param.getDeleteMeterGroupsRequest().setCorpId(this.parameter.getDeleteMeterGroupsRequest().getCorpId());
        param.getDeleteMeterGroupsRequest().setMeterGroupId(this.parameter.getDeleteMeterGroupsRequest().getMeterGroupId());
        param.getDeleteMeterGroupsRequest().setBuildingId(this.parameter.getDeleteMeterGroupsRequest().getBuildingId());
        param.getDeleteMeterGroupsRequest().setMeterGroupList(
                this.parameter.getDeleteMeterGroupsRequest().getMeterGroupList());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DeleteSmsMeterGroupsResult result = deleteSmsMeterGroupDao.query(this.parameter);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
