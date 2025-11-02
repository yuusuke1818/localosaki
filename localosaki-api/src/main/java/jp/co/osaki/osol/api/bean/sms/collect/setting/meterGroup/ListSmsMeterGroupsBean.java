package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.ListSmsMeterGroupsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ一覧取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingmeterGroupListSmsMeterGroupsBean")
@RequestScoped
public class ListSmsMeterGroupsBean extends OsolApiBean<ListSmsMeterGroupsParameter>
implements BaseApiBean<ListSmsMeterGroupsParameter, ListSmsMeterGroupsResponse> {

    private ListSmsMeterGroupsResponse response = new ListSmsMeterGroupsResponse();
    private ListSmsMeterGroupsParameter parameter = new ListSmsMeterGroupsParameter();

    @EJB
    ListSmsMeterGroupsDao listSmsMeterGroupsDao;

    @Override
    public ListSmsMeterGroupsParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsMeterGroupsParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMeterGroupsResponse execute() throws Exception {
        ListSmsMeterGroupsParameter param = new ListSmsMeterGroupsParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setCorpId(this.parameter.getCorpId());
        param.setMeterGroupId(this.parameter.getMeterGroupId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterGroupsResult result = listSmsMeterGroupsDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
