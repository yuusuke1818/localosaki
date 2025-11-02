package jp.co.osaki.osol.api.bean.sms.collect.setting.meterGroup;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーターグループ管理 メーターグループ名一覧取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingmeterGroupListSmsMeterGroupNamesBean")
@RequestScoped
public class ListSmsMeterGroupNamesBean extends OsolApiBean<ListSmsMeterGroupNamesParameter>
implements BaseApiBean<ListSmsMeterGroupNamesParameter, ListSmsMeterGroupNamesResponse> {

    private ListSmsMeterGroupNamesResponse response = new ListSmsMeterGroupNamesResponse();
    private ListSmsMeterGroupNamesParameter parameter = new ListSmsMeterGroupNamesParameter();

    @EJB
    ListSmsMeterGroupNamesDao listSmsMeterGroupNamesDao;

    @Override
    public ListSmsMeterGroupNamesParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsMeterGroupNamesParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMeterGroupNamesResponse execute() throws Exception {
        ListSmsMeterGroupNamesParameter param = new ListSmsMeterGroupNamesParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterGroupNamesResult result = listSmsMeterGroupNamesDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
