package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoServiceDaoImpl;

/**
 * 月検針連番取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class ListSmsMonthMeterReadingNoSearchDao extends OsolApiDao<ListSmsMonthMeterReadingNoParameter> {

    private final ListSmsMonthMeterReadingNoServiceDaoImpl listSmsMonthMeterReadingNoServiceDaoImpl;

    public ListSmsMonthMeterReadingNoSearchDao() {
        listSmsMonthMeterReadingNoServiceDaoImpl = new ListSmsMonthMeterReadingNoServiceDaoImpl();

    }
    @Override
    public ListSmsMonthMeterReadingNoResult query(ListSmsMonthMeterReadingNoParameter parameter) throws Exception {

        ListSmsMeterReadingDataMonthNoDataResultData monthMeterReadingNoDataResultData = new ListSmsMeterReadingDataMonthNoDataResultData();

        ListSmsMeterReadingDataMonthNoResultData monthMeterReadingNoResultData = new ListSmsMeterReadingDataMonthNoResultData();

        monthMeterReadingNoResultData.setDevId(parameter.getDevId());
        List<String> devIds = Objects.isNull(parameter.getDevIdListStr()) ? null : Arrays.asList(parameter.getDevIdListStr().split(","));
        monthMeterReadingNoResultData.setDevIdList(devIds);
        monthMeterReadingNoResultData.setYear(parameter.getYear());
        monthMeterReadingNoResultData.setMonth(parameter.getMonth());

        List<ListSmsMeterReadingDataMonthNoResultData> monthMeterReadingNoResultDataList = getResultList(listSmsMonthMeterReadingNoServiceDaoImpl, monthMeterReadingNoResultData);

        monthMeterReadingNoDataResultData.setMonthMeterReadingNoResultDataList(monthMeterReadingNoResultDataList);

        ListSmsMonthMeterReadingNoResult result = new ListSmsMonthMeterReadingNoResult();
        result.setMonthMeterReadingNoDataResultData(monthMeterReadingNoDataResultData);

        return result;
    }
}
