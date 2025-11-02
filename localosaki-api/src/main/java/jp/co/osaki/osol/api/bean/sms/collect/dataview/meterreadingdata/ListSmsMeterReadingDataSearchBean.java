package jp.co.osaki.osol.api.bean.sms.collect.dataview.meterreadingdata;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataSearchDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResponse;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 検針データ取得API Beanクラス.
 *
 * @author hosono.s
 */
//@Named(value = "ListSmsMeterReadingDataSearchBean")	/* @Named(value = "listSmsMeterReadingDataSearchBean") */
@Named(value = "listSmsMeterReadingDataSearchBean")	/* @Named(value = "ListSmsMeterReadingDataSearchBean") */
@RequestScoped
public class ListSmsMeterReadingDataSearchBean extends OsolApiBean<ListSmsMeterReadingDataParameter>
        implements BaseApiBean<ListSmsMeterReadingDataParameter, ListSmsMeterReadingDataResponse> {

    private ListSmsMeterReadingDataParameter parameter = new ListSmsMeterReadingDataParameter();

    private ListSmsMeterReadingDataResponse response = new ListSmsMeterReadingDataResponse();

    @EJB
    ListSmsMeterReadingDataSearchDao listSmsMeterReadingDataSearchDao;

    @Override
    public ListSmsMeterReadingDataResponse execute() throws Exception {
        ListSmsMeterReadingDataParameter param = new ListSmsMeterReadingDataParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setDevId(this.parameter.getDevId());
        param.setDevIdListStr(this.parameter.getDevIdListStr());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setMonthMeterReadingNo(this.parameter.getMonthMeterReadingNo());
        param.setMeterReadingType(this.parameter.getMeterReadingType());

        ListSmsMeterReadingDataResult result = listSmsMeterReadingDataSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    @Override
    public ListSmsMeterReadingDataParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsMeterReadingDataParameter parameter) {
        this.parameter = parameter;
    }

}