package jp.co.osaki.osol.api.bean.sms.collect.setting.meterreading.autoinspection;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ取得API Beanクラス.
 *
 * @author ozaki.y
 */
@Named(value = "ListSmsAutoInspectionBean")
@RequestScoped
public class ListSmsAutoInspectionBean extends OsolApiBean<ListSmsAutoInspectionParameter>
        implements BaseApiBean<ListSmsAutoInspectionParameter, ListSmsAutoInspectionResponse> {

    private ListSmsAutoInspectionParameter parameter = new ListSmsAutoInspectionParameter();

    private ListSmsAutoInspectionResponse response = new ListSmsAutoInspectionResponse();

    @EJB
    ListSmsAutoInspectionDao dao;

    @Override
    public ListSmsAutoInspectionParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsAutoInspectionParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsAutoInspectionResponse execute() throws Exception {
        ListSmsAutoInspectionParameter param = new ListSmsAutoInspectionParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setTenant(this.parameter.isTenant());

        ListSmsAutoInspectionResult result = dao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
