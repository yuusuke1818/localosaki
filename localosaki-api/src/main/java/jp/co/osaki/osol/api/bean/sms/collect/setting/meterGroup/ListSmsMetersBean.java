package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.ListSmsMetersDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMetersParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMetersResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMetersResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーター一覧取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingmeterGroupListSmsMetersBean")
@RequestScoped
public class ListSmsMetersBean extends OsolApiBean<ListSmsMetersParameter>
implements BaseApiBean<ListSmsMetersParameter, ListSmsMetersResponse> {

    private ListSmsMetersResponse response = new ListSmsMetersResponse();
    private ListSmsMetersParameter parameter = new ListSmsMetersParameter();

    @EJB
    ListSmsMetersDao listSmsMeterGroupsDao;

    @Override
    public ListSmsMetersParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsMetersParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMetersResponse execute() throws Exception {
        ListSmsMetersParameter param = new ListSmsMetersParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setCorpId(this.parameter.getCorpId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMetersResult result = listSmsMeterGroupsDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
