/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarMonthReportParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarMonthReportResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * デマンド情報カレンダー（月報用）　Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandCalendarMonthReportDao extends OsolApiDao<DemandCalendarMonthReportParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;

    public DemandCalendarMonthReportDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandCalendarMonthReportResult query(DemandCalendarMonthReportParameter parameter) throws Exception {
        DemandCalendarMonthReportResult result = new DemandCalendarMonthReportResult();
        List<DemandCalendarYearData> calList;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //当日の日付を取得する
        Date svDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //前日の日付を取得する
        Date svDayBefore = DateUtility.conversionDate(
                DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                        DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        result.setNowDate(svDate);
        result.setBeforeDate(svDayBefore);
        result.setDefaultYear(null);
        result.setYearList(null);

        result.setYearMax(DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY));
        result.setYearMin(DateUtility.changeDateFormat(
                DateUtility.plusYear(svDate, parameter.getSumPeriod().multiply(new BigDecimal(-1)).intValue()),
                DateUtility.DATE_FORMAT_YYYY));

        //企業集計カレンダ（年報）を取得する
        CorpDemandSelectResult corpDemandParam = new CorpDemandSelectResult();
        corpDemandParam.setCorpId(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandCalendarMonthReportResult();
        }

        calList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());
        if (calList == null || calList.isEmpty()) {
            return new DemandCalendarMonthReportResult();
        }

        //システム日付でフィルタリングする
        calList = calList.stream()
                .filter(i -> i.getMonthStartDate().compareTo(svDate) <= 0 && i.getMonthEndDate().compareTo(svDate) >= 0)
                .collect(Collectors.toList());
        if (calList == null || calList.size() != 1) {
            return result;
        } else {
            result.setFirstDay(calList.get(0).getMonthStartDate());
        }

        //年リストをセットする
        result.setYearList(DemandEmsUtility.getYearList(result.getYearMax(), parameter.getSumPeriod()));

        return result;
    }

}
