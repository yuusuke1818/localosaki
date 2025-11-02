package jp.co.osaki.osol.api.bean.sms.collect.setting.meter;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meter.ListSmsMeterMngIdByDevIdDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListSmsMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListSmsMeterResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.ListSmsMeterResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * メーター管理 メーター情報メーター管理ID一覧取得（条件：装置IDのみ） Beanクラス
 * @author kimura.m
 */
@Named(value = "listSmsMeterMngIdByDevIdBean")
@RequestScoped
public class ListSmsMeterMngIdByDevIdBean extends OsolApiBean<ListSmsMeterParameter>
implements BaseApiBean<ListSmsMeterParameter, ListSmsMeterResponse> {

    private ListSmsMeterResponse response = new ListSmsMeterResponse();
    private ListSmsMeterParameter parameter = new ListSmsMeterParameter();

    @EJB
    ListSmsMeterMngIdByDevIdDao listSmsMeterMngIdByDevIdDao;

    @Override
    public ListSmsMeterParameter getParameter() {
        return parameter;
    }
    @Override
    public void setParameter(ListSmsMeterParameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public ListSmsMeterResponse execute() throws Exception {
        ListSmsMeterParameter param = new ListSmsMeterParameter();
        param.setDevId(this.parameter.getDevId());

        ListSmsMeterResult result = listSmsMeterMngIdByDevIdDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
