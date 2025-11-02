package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataResponse;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 請求データ選択 Beanクラス.
 *
 * @author tonohata.k
 */
@Named(value = "listSmsSelectBillingDataSearchBean")
@RequestScoped
public class ListSmsSelectBillingDataSearchBean extends OsolApiBean<ListSmsSelectBillingDataParameter>
        implements BaseApiBean<ListSmsSelectBillingDataParameter, ListSmsSelectBillingDataResponse> {

    private ListSmsSelectBillingDataParameter parameter = new ListSmsSelectBillingDataParameter();

    private ListSmsSelectBillingDataResponse response = new ListSmsSelectBillingDataResponse();

    @Override
    public ListSmsSelectBillingDataParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsSelectBillingDataParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public ListSmsSelectBillingDataResponse execute() throws Exception {
        return response;
    }

}
