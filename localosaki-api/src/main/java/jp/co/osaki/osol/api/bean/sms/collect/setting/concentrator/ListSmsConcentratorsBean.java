package jp.co.osaki.osol.api.bean.sms.collect.setting.concentrator;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator.ListSmsConcentratorsDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.ListSmsConcentratorsParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.concentrator.ListSmsConcentratorsResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.ListSmsConcentratorsResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * コンセントレータ管理 コンセントレータ一覧取得 Beanクラス
 * @author kimura.m
 */
@Named(value = "smsCollectSettingConcentratorListSmsConcentratorsBean")
@RequestScoped
public class ListSmsConcentratorsBean extends OsolApiBean<ListSmsConcentratorsParameter>
implements BaseApiBean<ListSmsConcentratorsParameter, ListSmsConcentratorsResponse> {

    private ListSmsConcentratorsResponse response = new ListSmsConcentratorsResponse();
    private ListSmsConcentratorsParameter parameter = new ListSmsConcentratorsParameter();

    @EJB
    ListSmsConcentratorsDao listSmsConcentratorDao;

    @Override
    public ListSmsConcentratorsParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsConcentratorsParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsConcentratorsResponse execute() throws Exception {
        ListSmsConcentratorsParameter param = new ListSmsConcentratorsParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDevId(this.parameter.getDevId());
        param.setConcentId(this.parameter.getConcentId());
        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsConcentratorsResult result = listSmsConcentratorDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
