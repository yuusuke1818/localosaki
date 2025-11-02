/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarYearReportParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarYearReportResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandCalendarYearBuildingYearMonthResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandCalendarYearCorpYearMonthResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンド情報カレンダー（年報用）　Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandCalendarYearReportDao extends OsolApiDao<DemandCalendarYearReportParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    public DemandCalendarYearReportDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandCalendarYearReportResult query(DemandCalendarYearReportParameter parameter) throws Exception {
        DemandCalendarYearReportResult result = new DemandCalendarYearReportResult();
        List<DemandCalendarYearCorpYearMonthResultData> corpYearMonthList = new ArrayList<>();
        List<String> buildingYearList = new ArrayList<>();
        List<DemandCalendarYearBuildingYearMonthResultData> buildingYearMonthList = new ArrayList<>();

        //当日日付
        Date svDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //前日日付
        Date svDayBefore = DateUtility.conversionDate(
                DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                        DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //企業集計カレンダ（年報）を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandCalendarYearReportResult();
        }
        //企業集計カレンダを取得する
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));
        List<DemandCalendarYearData> corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());
        if (corpCalList == null || corpCalList.isEmpty()) {
            return new DemandCalendarYearReportResult();
        }

        //取得したカレンダをシステム日付でフィルタリングする
        List<DemandCalendarYearData> filterCorpCalList = corpCalList.stream()
                .filter(i -> i.getMonthStartDate().compareTo(svDate) <= 0 && i.getMonthEndDate().compareTo(svDate) >= 0)
                .collect(Collectors.toList());
        result.setNowDate(svDate);
        result.setBeforeDate(svDayBefore);
        if (filterCorpCalList == null || filterCorpCalList.size() != 1) {
            result.setDefaultYear(null);
            result.setDefaultMonth(null);
            result.setCurrentYear(null);
            result.setCurrentMonth(null);
            result.setYearList(null);
        } else {
            result.setCurrentYear(DateUtility.changeDateFormat(filterCorpCalList.get(0).getMonthEndDate(),
                    DateUtility.DATE_FORMAT_YYYY));
            result.setCurrentMonth(new BigDecimal(DateUtility
                    .changeDateFormat(filterCorpCalList.get(0).getMonthEndDate(), DateUtility.DATE_FORMAT_MM)));
            //            //フィルタリングした結果の年でフィルタリングする
            //            String baseYearNo = filterCorpCalList.get(0).getYearNo();
            //            List<DemandCalendarYearData> filterSecond = corpCalList.stream()
            //                    .filter(i -> i.getYearNo().equals(baseYearNo)).collect(Collectors.toList());
            //            //月終了日でソート
            //            filterSecond = filterSecond.stream().sorted(
            //                    Comparator.comparing(DemandCalendarYearData::getMonthEndDate, Comparator.naturalOrder()))
            //                    .collect(Collectors.toList());

            //デフォルト年は、現在日付の年度
            result.setDefaultYear(DemandCalendarYearUtility.getFiscalYearNo(filterCorpCalList.get(0),
                    corpDemandList.get(0).getSumDate()));

            //デフォルト月は、集計時次第
            if ("00".equals(corpDemandList.get(0).getSumDate().substring(4))) {
                //00の場合は、集計時の月
                result.setDefaultMonth(new BigDecimal(corpDemandList.get(0).getSumDate().substring(0, 2)));
            } else {
                //24の場合、集計時の月にデフォルト年を付与した場合の最大日数を取得
                String ymd = result.getDefaultYear().concat(corpDemandList.get(0).getSumDate().substring(0, 2))
                        .concat("01");
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateUtility.conversionDate(ymd, DateUtility.DATE_FORMAT_YYYYMMDD));
                int maxDay = cal.getActualMaximum(Calendar.DATE);
                String baseYmd = null;
                Date baseDate = null;
                String baseHour = null;
                baseHour = corpDemandList.get(0).getSumDate().substring(4);
                int baseDayCount = Integer.parseInt(corpDemandList.get(0).getSumDate().substring(2, 4));

                if ("24".equals(baseHour)) {
                    // 集計時の日が最大日数を超える場合
                    if (baseDayCount > maxDay) {
                        // 月末日の翌日とする
                        baseYmd = result.getDefaultYear().concat(corpDemandList.get(0).getSumDate().substring(0, 2))
                                .concat(String.valueOf(maxDay));
                        baseDate = DateUtility
                                .plusDay(DateUtility.conversionDate(baseYmd, DateUtility.DATE_FORMAT_YYYYMMDD), 1);
                    } else if (baseDayCount <= maxDay) {
                        // 集計時の翌日とする
                        baseYmd = result.getDefaultYear().concat(corpDemandList.get(0).getSumDate().substring(0, 4));
                        baseDate = DateUtility
                                .plusDay(DateUtility.conversionDate(baseYmd, DateUtility.DATE_FORMAT_YYYYMMDD), 1);
                    }

                } else if ("00".equals(baseHour)) {
                    if (baseDayCount > maxDay) {
                        // 月末日とする
                        baseYmd = result.getDefaultYear().concat(corpDemandList.get(0).getSumDate().substring(0, 2))
                                .concat(String.valueOf(maxDay));
                        baseDate = DateUtility
                                .plusDay(DateUtility.conversionDate(baseYmd, DateUtility.DATE_FORMAT_YYYYMMDD), 0);
                    } else if (baseDayCount <= maxDay) {
                        // 集計時とする
                        baseYmd = result.getDefaultYear().concat(corpDemandList.get(0).getSumDate().substring(0, 4));
                        baseDate = DateUtility
                                .plusDay(DateUtility.conversionDate(baseYmd, DateUtility.DATE_FORMAT_YYYYMMDD), 0);
                    }
                }

                //翌日が1日の場合は、その月
                if ("01".equals(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_DD))) {
                    result.setDefaultMonth(
                            new BigDecimal(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_MM)));
                } else {
                    // それ以外は24の場合翌月、00の場合当月
                    if ("24".equals(baseHour)) {
                        if (new BigDecimal(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_MM))
                                .compareTo(new BigDecimal("12")) == 0) {
                            //12月の場合は1月
                            result.setDefaultMonth(BigDecimal.ONE);
                        } else {
                            result.setDefaultMonth(
                                    new BigDecimal(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_MM))
                                            .add(BigDecimal.ONE));
                        }
                    } else {
                        result.setDefaultMonth(
                                new BigDecimal(DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_MM))
                                        .add(BigDecimal.ZERO));
                    }
                }
            }

            result.setYearList(
                    DemandEmsUtility.getYearList(DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY),
                            parameter.getSumPeriod()));
        }

        //取得したカレンダ（フィルタリング前）を年Noでフィルタリングする
        filterCorpCalList = corpCalList.stream().filter(
                i -> Integer.parseInt(i.getYearNo()) >= Integer.parseInt(result.getYearList().get(0))
                        && Integer.parseInt(i.getYearNo()) <= Integer
                                .parseInt(result.getYearList().get(result.getYearList().size() - 1)))
                .collect(Collectors.toList());

        //年月（企業集計）を作成する
        for (DemandCalendarYearData cal : filterCorpCalList) {
            corpYearMonthList.add(new DemandCalendarYearCorpYearMonthResultData(cal.getYearNo(), cal.getMonthNo(),
                    cal.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, cal.getMonthNo().intValue())),
                    cal.getMonthStartDate(), cal.getMonthEndDate()));
        }
        result.setCorpYearMonthList(corpYearMonthList);

        if (parameter.getBuildingId() == null) {
            return result;
        }

        //電力集計カレンダ（年報）を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());

        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandCalendarYearReportResult();
        }

        List<DemandCalendarYearData> buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom,
                yearNoTo,
                buildingDemandList.get(0).getSumDate());
        if (buildingCalList == null || buildingCalList.isEmpty()) {
            return null;
        }

        //システム日付でフィルタリングする
        List<DemandCalendarYearData> filterBuildingCalList = buildingCalList.stream()
                .filter(i -> i.getMonthStartDate().compareTo(svDate) <= 0 && i.getMonthEndDate().compareTo(svDate) >= 0)
                .collect(Collectors.toList());
        if (filterBuildingCalList != null && filterBuildingCalList.size() == 1) {
            //フィルタリング前のカレンダを年Noと集計期間でフィルタリングする
            Integer rangeFrom = new BigDecimal(filterBuildingCalList.get(0).getYearNo())
                    .subtract(parameter.getSumPeriod())
                    .intValue();
            Integer rangeTo = Integer.parseInt(filterBuildingCalList.get(0).getYearNo());
            filterBuildingCalList = buildingCalList.stream()
                    .filter(i -> Integer.parseInt(i.getYearNo()) >= rangeFrom
                            && Integer.parseInt(i.getYearNo()) <= rangeTo)
                    .collect(Collectors.toList());
            if (filterBuildingCalList != null && !filterBuildingCalList.isEmpty()) {
                //年（電力集計）を作成する
                //年Noでグルーピング
                Map<String, List<String>> calMap = filterBuildingCalList.stream()
                        .collect(Collectors.groupingBy(i -> i.getYearNo(),
                                Collectors.mapping(DemandCalendarYearData::getYearNo, Collectors.toList())));
                for (Map.Entry<String, List<String>> entry : calMap.entrySet()) {
                    buildingYearList.add(entry.getKey());
                }
                result.setBuildingYearList(buildingYearList);
                //年月（電力集計）を作成する
                for (DemandCalendarYearData cal : filterBuildingCalList) {
                    buildingYearMonthList
                            .add(new DemandCalendarYearBuildingYearMonthResultData(parameter.getBuildingId(),
                                    cal.getYearNo(), cal.getMonthNo(),
                                    cal.getYearNo()
                                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                    cal.getMonthNo().intValue())),
                                    cal.getMonthStartDate(), cal.getMonthEndDate()));
                }
                result.setBuildingYearMonthList(buildingYearMonthList);
            }
        }
        return result;
    }

}
