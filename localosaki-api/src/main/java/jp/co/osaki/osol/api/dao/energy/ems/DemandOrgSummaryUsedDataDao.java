/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgSummaryUsedDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgSummaryUsedDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.utility.DailyUsedTargetResult;
import jp.co.osaki.osol.api.result.utility.MonthlyTargetUsedPowerResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgSummaryUsedDataDailyResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * 電力使用量（目標対比）取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgSummaryUsedDataDao extends OsolApiDao<DemandOrgSummaryUsedDataParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    public DemandOrgSummaryUsedDataDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    @Override
    public DemandOrgSummaryUsedDataResult query(DemandOrgSummaryUsedDataParameter parameter) throws Exception {
        DemandOrgSummaryUsedDataResult result = new DemandOrgSummaryUsedDataResult();
        List<DemandOrgSummaryUsedDataDailyResultData> detailList = new ArrayList<>();
        List<DemandCalendarYearData> yearCorpCalList = null;
        List<DemandCalendarYearData> buildingCalList = null;
        Date monthStartDate = null;
        Date monthEndDate = null;
        Date currentDate = null;
        String calYm = null;
        List<CommonDemandMonthReportLineListResult> monthReportLineList;
        BigDecimal monthlyUsed = null;
        BigDecimal monthlyUsedForecast = null;
        BigDecimal recent6DaysUsed = null;
        Integer recent6DaysCount = null;
        BigDecimal targetContrastRate = null;
        Date baseDate = null;
        List<MonthlyTargetUsedPowerResult> monthlyTargetUsePowerList;
        List<DailyUsedTargetResult> dailyUsedTargetList;
        List<BuildingDemandListDetailResultData> buildingDemandList = null;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandOrgSummaryUsedDataResult();
        }

        //年報カレンダを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgSummaryUsedDataResult();
            }
            yearCorpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            if (yearCorpCalList == null || yearCorpCalList.isEmpty()) {
                return new DemandOrgSummaryUsedDataResult();
            }

            if (parameter.getYmd() != null) {
                //年月日でフィルタリングする
                yearCorpCalList = yearCorpCalList.stream().filter(
                        i -> i.getMonthStartDate().compareTo(parameter.getYmd()) <= 0
                                && i.getMonthEndDate().compareTo(parameter.getYmd()) >= 0)
                        .collect(Collectors.toList());
                if (yearCorpCalList == null || yearCorpCalList.size() != 1) {
                    return new DemandOrgSummaryUsedDataResult();
                }
            }

            if (!CheckUtility.isNullOrEmpty(parameter.getYear())) {
                //年月でフィルタリングする
                yearCorpCalList = yearCorpCalList.stream()
                        .filter(i -> i.getYearNo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        i.getMonthNo().intValue()))
                                .equals(parameter.getYear()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                parameter.getMonth().intValue()))))
                        .collect(Collectors.toList());
                if (yearCorpCalList == null || yearCorpCalList.size() != 1) {
                    return new DemandOrgSummaryUsedDataResult();
                }
            }

            calYm = yearCorpCalList.get(0).getYearNo().concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                    yearCorpCalList.get(0).getMonthNo().intValue()));
            monthStartDate = yearCorpCalList.get(0).getMonthStartDate();
            monthEndDate = yearCorpCalList.get(0).getMonthEndDate();

        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            String sumDate = null;
            //集計デマンド系統情報を取得する
            AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), null, null, null);
            List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                    aggregateDmLineListServiceDaoImpl, aggregateLineParam);
            if (aggregateLineList != null && aggregateLineList.size() == 1) {
                //集計デマンド情報を取得する
                AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(),
                        aggregateLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                        aggregateParam);
                if (aggregateList != null && aggregateList.size() == 1) {
                    sumDate = aggregateList.get(0).getSumDate();
                }
            }
            if (CheckUtility.isNullOrEmpty(sumDate)) {
                //集計時が取得できない場合は建物デマンドから取得
                sumDate = buildingDemandList.get(0).getSumDate();
            }
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo, sumDate);
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return new DemandOrgSummaryUsedDataResult();
            }

            if (parameter.getYmd() != null) {
                //年月日でフィルタリングする
                buildingCalList = buildingCalList.stream().filter(
                        i -> i.getMonthStartDate().compareTo(parameter.getYmd()) <= 0
                                && i.getMonthEndDate().compareTo(parameter.getYmd()) >= 0)
                        .collect(Collectors.toList());
                if (buildingCalList == null || buildingCalList.size() != 1) {
                    return null;
                }
            }

            if (!CheckUtility.isNullOrEmpty(parameter.getYear())) {
                //年月でフィルタリングする
                buildingCalList = buildingCalList.stream()
                        .filter(i -> i.getYearNo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        i.getMonthNo().intValue()))
                                .equals(parameter.getYear()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                parameter.getMonth().intValue()))))
                        .collect(Collectors.toList());
                if (buildingCalList == null || buildingCalList.size() != 1) {
                    return null;
                }
            }

            calYm = buildingCalList.get(0).getYearNo().concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                    buildingCalList.get(0).getMonthNo().intValue()));
            monthStartDate = buildingCalList.get(0).getMonthStartDate();
            monthEndDate = buildingCalList.get(0).getMonthEndDate();

        }

        //対象年月の月開始日～月終了日をセットする
        do {
            if (currentDate == null) {
                currentDate = monthStartDate;
            } else {
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            detailList.add(new DemandOrgSummaryUsedDataDailyResultData(
                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), null, null,
                    null));
        } while (currentDate != null && !currentDate.equals(monthEndDate));

        //デマンド月報系統データを取得する
        CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), monthStartDate, monthEndDate);
        monthReportLineList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);
        if (monthReportLineList != null && !monthReportLineList.isEmpty()) {
            monthReportLineList = SortUtility.sortCommonDemandMonthReportLineListByMeasurement(monthReportLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportLineListResult monthReport : monthReportLineList) {

            for (DemandOrgSummaryUsedDataDailyResultData detail : detailList) {
                if (detail.getDay().equals(DateUtility.changeDateFormat(monthReport.getMeasurementDate(),
                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                    if (DateUtility.conversionDate(detail.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .after(DateUtility.conversionDate(
                                    DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    DateUtility.DATE_FORMAT_YYYYMMDD))) {
                        //前日より未来の場合
                        detail.setDailyUsed(null);
                        detail.setDailyResult(null);
                    } else {
                        if (monthlyUsed == null) {
                            monthlyUsed = monthReport.getLineValueKwh();
                        } else {
                            monthlyUsed = monthlyUsed.add(monthReport.getLineValueKwh());
                        }
                        detail.setDailyUsed(monthlyUsed);
                        detail.setDailyResult(monthReport.getLineValueKwh());
                    }

                }
            }
        }

        //日別使用量が設定されていな場合、前日の値を設定する
        BigDecimal yesterDayDailyUsed = null;
        for (DemandOrgSummaryUsedDataDailyResultData detail : detailList) {
            if (DateUtility.conversionDate(detail.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                    .after(DateUtility.conversionDate(
                            DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                                    DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD))) {
                //前日より未来になった場合、処理を終了する
                break;
            }
            if (detail.getDailyUsed() != null) {
                yesterDayDailyUsed = detail.getDailyUsed();
            } else {
                detail.setDailyUsed(yesterDayDailyUsed);
            }
        }

        //月間目標電力量を取得する
        monthlyTargetUsePowerList = DemandEmsUtility.getMonthlyTargetUsedPower(parameter.getOperationCorpId(),
                parameter.getBuildingId(), calYm, calYm, buildingDemandList.get(0));

        //日別目標電力使用量を取得する
        if (monthlyTargetUsePowerList != null && monthlyTargetUsePowerList.size() == 1) {
            dailyUsedTargetList = DemandEmsUtility.getDailyUsedTarget(monthStartDate, monthEndDate,
                    monthlyTargetUsePowerList.get(0).getTargetUsedPower(), parameter.getPrecision(),
                    parameter.getBelowAccuracyControl());
            for (DailyUsedTargetResult daily : dailyUsedTargetList) {
                for (DemandOrgSummaryUsedDataDailyResultData detail : detailList) {
                    if (detail.getDay().equals(
                            DateUtility.changeDateFormat(daily.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
                        detail.setDailyUsedTarget(daily.getUsedTarget());
                    }
                }
            }
        }

        //月間予測電力使用量を取得する
        if (DateUtility
                .conversionDate(detailList.get(detailList.size() - 1).getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                .before(DateUtility.conversionDate(
                        DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))) {
            //最終日が当日より過去（前日以前）の場合、月間使用電力量と同じ
            monthlyUsedForecast = monthlyUsed;
            baseDate = monthEndDate;
        } else {
            //上記以外の場合は、前日を含む過去6日分の使用量を算出する。
            int currentIndex;

            for (currentIndex = detailList.size() - 1; currentIndex >= 0; currentIndex--) {
                //最終からさかのぼる
                if (recent6DaysCount == null) {
                    if (DateUtility
                            .conversionDate(detailList.get(currentIndex).getDay(),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .equals(DateUtility.conversionDate(
                                    DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    DateUtility.DATE_FORMAT_YYYYMMDD))) {
                        //前日と同じ場合のみ加算
                        if (detailList.get(currentIndex).getDailyResult() != null) {
                            recent6DaysUsed = detailList.get(currentIndex).getDailyResult();
                        }
                        baseDate = DateUtility.conversionDate(detailList.get(currentIndex).getDay(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                        recent6DaysCount = 1;
                    }
                } else {
                    //無条件で加算
                    if (detailList.get(currentIndex).getDailyResult() != null) {
                        if (recent6DaysUsed == null) {
                            recent6DaysUsed = detailList.get(currentIndex).getDailyResult();
                        } else {
                            recent6DaysUsed = recent6DaysUsed.add(detailList.get(currentIndex).getDailyResult());
                        }
                    }
                    recent6DaysCount = recent6DaysCount + 1;
                    if (recent6DaysCount == 6) {
                        //ループを抜ける
                        break;
                    }

                }
            }

            if (recent6DaysUsed != null) {
                monthlyUsedForecast = DemandEmsUtility.getSummaryUsedForecast(monthStartDate, monthEndDate, baseDate,
                        monthlyUsed,
                        recent6DaysUsed, new BigDecimal(recent6DaysCount), parameter.getPrecision(),
                        parameter.getBelowAccuracyControl());
            } else {
                monthlyUsedForecast = DemandEmsUtility.getSummaryUsedForecast(monthStartDate, monthEndDate, baseDate,
                        monthlyUsed,
                        BigDecimal.ZERO, BigDecimal.ZERO, parameter.getPrecision(),
                        parameter.getBelowAccuracyControl());
            }
        }

        //目標対比を取得する
        if (monthlyTargetUsePowerList != null && monthlyTargetUsePowerList.size() == 1) {
            targetContrastRate = DemandEmsUtility.getCompareSummaryUsedAndTargetUsed(monthStartDate, monthEndDate,
                    baseDate,
                    monthlyTargetUsePowerList.get(0).getTargetUsedPower(), monthlyUsed, parameter.getPrecision(),
                    parameter.getBelowAccuracyControl());
        }

        if (monthlyUsed != null) {
            result.setMonthlyUsed(
                    monthlyUsed.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (monthlyTargetUsePowerList != null && monthlyTargetUsePowerList.size() == 1
                && monthlyTargetUsePowerList.get(0).getTargetUsedPower() != null) {
            result.setMonthlyUsedTarget(monthlyTargetUsePowerList.get(0).getTargetUsedPower().setScale(
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }
        if (monthlyUsedForecast != null) {
            result.setMonthlyUsedForecast(monthlyUsedForecast.setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }
        if (targetContrastRate != null) {
            result.setTargetContrastRate(targetContrastRate.setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }
        result.setCalYm(calYm);
        result.setDailyResultList(detailList);
        return result;
    }

}
