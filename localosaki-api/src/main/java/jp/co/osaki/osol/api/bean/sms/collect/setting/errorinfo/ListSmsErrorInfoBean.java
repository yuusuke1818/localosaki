package jp.co.osaki.osol.api.bean.sms.collect.setting.errorinfo;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.errorinfo.ListSmsErrorInfoDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.errorinfo.ListSmsErrorInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.errorinfo.ListSmsErrorInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.errorinfo.ListSmsErrorInfoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 機器管理 異常情報画面 データ取得API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "ListSmsErrorInfoBean")
@RequestScoped
public class ListSmsErrorInfoBean extends OsolApiBean<ListSmsErrorInfoParameter>
        implements BaseApiBean<ListSmsErrorInfoParameter, ListSmsErrorInfoResponse> {

    private ListSmsErrorInfoParameter parameter = new ListSmsErrorInfoParameter();

    private ListSmsErrorInfoResponse response = new ListSmsErrorInfoResponse();

    @EJB
    private ListSmsErrorInfoDao dao;

    @Override
    public ListSmsErrorInfoParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsErrorInfoParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsErrorInfoResponse execute() throws Exception {
        ListSmsErrorInfoParameter param = new ListSmsErrorInfoParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());

        ListSmsErrorInfoResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }
}
