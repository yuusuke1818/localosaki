package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataSearchDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 請求金額データ取得API Beanクラス.
 *
 * @author hosono.s
 */
@Named(value = "listSmsBillingAmountDataSearchBean")
@RequestScoped
public class ListSmsBillingAmountDataSearchBean extends OsolApiBean<ListSmsBillingAmountDataParameter>
        implements BaseApiBean<ListSmsBillingAmountDataParameter, ListSmsBillingAmountDataResponse> {

    private ListSmsBillingAmountDataParameter parameter = new ListSmsBillingAmountDataParameter();

    private ListSmsBillingAmountDataResponse response = new ListSmsBillingAmountDataResponse();

    @EJB
    ListSmsBillingAmountDataSearchDao listSmsBillingAmountDataSearchDao;

    @Override
    public ListSmsBillingAmountDataResponse execute() throws Exception {
        ListSmsBillingAmountDataParameter param = new ListSmsBillingAmountDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setMonthMeterReadingNo(this.parameter.getMonthMeterReadingNo());
        param.setMeterReadingType(this.parameter.getMeterReadingType());
        param.setOperationBuildingId(this.parameter.getOperationBuildingId());

        ListSmsBillingAmountDataResult result = listSmsBillingAmountDataSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    @Override
    public ListSmsBillingAmountDataParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsBillingAmountDataParameter parameter) {
        this.parameter = parameter;
    }

}
