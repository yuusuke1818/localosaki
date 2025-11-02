package jp.co.osaki.osol.api.bean.sms.collect.setting.repeater;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.repeater.ListSmsRepeatersDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.ListSmsRepeatersParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.repeater.ListSmsRepeatersResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.ListSmsRepeatersResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 中継装置管理 中継装置一覧取得 Beanクラス
 * @author maruta.y
 */
@Named(value = "smsCollectSettingRepeaterListSmsRepeatersBean")
@RequestScoped
public class ListSmsRepeatersBean extends OsolApiBean<ListSmsRepeatersParameter>
implements BaseApiBean<ListSmsRepeatersParameter, ListSmsRepeatersResponse> {

    private ListSmsRepeatersResponse response = new ListSmsRepeatersResponse();
    private ListSmsRepeatersParameter parameter = new ListSmsRepeatersParameter();

    @EJB
    ListSmsRepeatersDao listSmsRepeaterDao;

    @Override
    public ListSmsRepeatersParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsRepeatersParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsRepeatersResponse execute() throws Exception {
        ListSmsRepeatersParameter param = new ListSmsRepeatersParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setRepeaterMngId(this.parameter.getRepeaterMngId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsRepeatersResult result = listSmsRepeaterDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
