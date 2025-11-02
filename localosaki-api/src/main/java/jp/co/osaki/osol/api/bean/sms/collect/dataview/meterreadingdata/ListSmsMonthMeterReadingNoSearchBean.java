package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoSearchDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 月検針連番取得API Beanクラス.
 *
 * @author hosono.s
 */
@Named(value = "listSmsMonthMeterReadingNoSearchBean")
@RequestScoped
public class ListSmsMonthMeterReadingNoSearchBean extends OsolApiBean<ListSmsMonthMeterReadingNoParameter>
implements BaseApiBean<ListSmsMonthMeterReadingNoParameter, ListSmsMonthMeterReadingNoResponse> {

    private ListSmsMonthMeterReadingNoParameter parameter = new ListSmsMonthMeterReadingNoParameter();

    private ListSmsMonthMeterReadingNoResponse response = new ListSmsMonthMeterReadingNoResponse();

    @EJB
    ListSmsMonthMeterReadingNoSearchDao listSmsMonthMeterReadingNoSearchDao;

    @Override
    public ListSmsMonthMeterReadingNoResponse execute() throws Exception {
        ListSmsMonthMeterReadingNoParameter param = new ListSmsMonthMeterReadingNoParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setDevId(this.parameter.getDevId());
        param.setDevIdListStr(this.parameter.getDevIdListStr());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());

        ListSmsMonthMeterReadingNoResult result = listSmsMonthMeterReadingNoSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    public ListSmsMonthMeterReadingNoParameter getParameter() {
        return parameter;
    }

    public void setParameter(ListSmsMonthMeterReadingNoParameter parameter) {
        this.parameter = parameter;
    }

}
