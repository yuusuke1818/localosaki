package jp.co.osaki.osol.api.dao.sms.collect.dataview;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDemandParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDemandDataResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDemandDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDemandSameMonthLastYearDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsMaxDemandPast;
import jp.co.osaki.osol.utility.CheckUtility;

@Stateless
public class ListSmsDemandDataDao extends OsolApiDao<ListSmsDemandParameter> {

    private final ListSmsDemandDaoImpl listSmsDemandDaoImpl;
    private final ListSmsDemandSameMonthLastYearDaoImpl listSmsDemandSameMonthLastYearDaoImpl;
    private final ListSmsMaxDemandPast listSmsMaxDemandPast;

    public ListSmsDemandDataDao() {
        listSmsDemandDaoImpl = new ListSmsDemandDaoImpl();
        listSmsDemandSameMonthLastYearDaoImpl = new ListSmsDemandSameMonthLastYearDaoImpl();
        listSmsMaxDemandPast = new ListSmsMaxDemandPast();
    }

    @Override
    public ListSmsDemandDataResult query(ListSmsDemandParameter parameter) throws Exception {
        String year = parameter.getYear();
        String month = parameter.getMonth();
        String day = parameter.getDay();

        String dateStr = "";
        if(!CheckUtility.isNullOrEmpty(day)) {
            dateStr = year + month + day;
        }else if(!CheckUtility.isNullOrEmpty(month)) {
            dateStr = year + month;
        }else {
            dateStr = year;
        }

        ListSmsDataViewResultData param = new ListSmsDataViewResultData();
        param.setDevId(parameter.getDevId());
        //param.setDate(dateStr);
        List<ListSmsDataViewResultData> resultList = getResultList(listSmsDemandDaoImpl, param);

        ListSmsDemandDataResult response = new ListSmsDemandDataResult();
        //response.setMeterSmsDemandResultDataList(resultList);

        return response;
    }

}
