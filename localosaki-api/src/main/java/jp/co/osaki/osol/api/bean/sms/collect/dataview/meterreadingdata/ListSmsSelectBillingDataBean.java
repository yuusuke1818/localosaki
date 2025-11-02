package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 請求データ選択 Beanクラス.
 *
 * @author tonohata.k
 */
@Named(value = "listSmsSelectBillingDataBean")
@RequestScoped
public class ListSmsSelectBillingDataBean extends OsolApiBean<ListSmsSelectBillingDataParameter>
        implements BaseApiBean<ListSmsSelectBillingDataParameter, ListSmsSelectBillingDataResponse> {

    private ListSmsSelectBillingDataParameter parameter = new ListSmsSelectBillingDataParameter();

    private ListSmsSelectBillingDataResponse response = new ListSmsSelectBillingDataResponse();

    @EJB
    ListSmsSelectBillingDataDao listSmsSelectBillingDataDao;


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

        ListSmsSelectBillingDataParameter param = new ListSmsSelectBillingDataParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setToYear(this.parameter.getToYear());
        param.setToMonth(this.parameter.getToMonth());
        param.setFromYear(this.parameter.getFromYear());
        param.setFromMonth(this.parameter.getFromMonth());

        ListSmsSelectBillingDataResult result = listSmsSelectBillingDataDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }




}
