/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgSummaryMeasurementDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgSummaryMeasurementDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingLineTimeStandardListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCoefficientHistoryManageServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * 前日までの計測データ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgSummaryMeasurementDataDao extends OsolApiDao<DemandOrgSummaryMeasurementDataParameter> {

    //TODO EntityServiceDaoは使わない
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final TAvailableEnergyServiceDaoImpl tAvailableEnergyServiceDaoImpl;
    private final MCoefficientHistoryManageServiceDaoImpl coefficientHistoryManageServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;

    public DemandOrgSummaryMeasurementDataDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        buildingLineTimeStandardListServiceDaoImpl = new BuildingLineTimeStandardListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        tAvailableEnergyServiceDaoImpl = new TAvailableEnergyServiceDaoImpl();
        coefficientHistoryManageServiceDaoImpl = new MCoefficientHistoryManageServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgSummaryMeasurementDataResult query(DemandOrgSummaryMeasurementDataParameter parameter)
            throws Exception {
        DemandOrgSummaryMeasurementDataResult result = new DemandOrgSummaryMeasurementDataResult();
        List<DemandCalendarYearData> yearCorpCalList = null;
        List<DemandCalendarYearData> tempCorpCalList;
        DemandCalendarYearData currentCorpCal = null;
        List<DemandCalendarYearData> buildingCalList = null;
        List<DemandCalendarYearData> tempBuildingCalList;
        DemandCalendarYearData currentBuildingCal = null;
        BigDecimal co2CalculationCoefficient = null;
        List<BuildingLineTimeStandardListTimeResultData> buildingLineTimeStandardList;
        List<CommonDemandMonthReportLineListResult> monthReportLineList;
        List<CommonDemandYearReportLineListResult> yearReportLineList = null;
        BigDecimal thisYearMaxValue = null;
        BigDecimal thisYearUsed = null;
        List<LineListDetailResultData> lineList;
        List<CommonDemandDayReportLineListResult> dayReportLineList;
        BigDecimal overStandardKwSummary = BigDecimal.ZERO;
        String yearNoFrom;
        String yearNoTo;
        BigDecimal monthNoFrom;
        BigDecimal monthNoTo;
        String baseFiscalYear;

        //年報カレンダ取得にあたっての範囲指定
        String calYearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String calYearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //集計日がNULLの場合、企業集計を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSummaryKind())) {
            parameter.setSummaryKind(ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal());
        }

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、切り捨てを設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
        }

        //年報カレンダを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgSummaryMeasurementDataResult();
            }
            yearCorpCalList = DemandCalendarYearUtility.getCalendarYearList(calYearNoFrom, calYearNoTo,
                    corpDemandList.get(0).getSumDate());

            //年月日でフィルタリングする
            tempCorpCalList = yearCorpCalList.stream()
                    .filter(i -> i.getMonthStartDate().compareTo(parameter.getYmd()) <= 0
                            && i.getMonthEndDate().compareTo(parameter.getYmd()) >= 0)
                    .collect(Collectors.toList());
            if (tempCorpCalList == null || tempCorpCalList.size() != 1) {
                return null;
            } else {
                currentCorpCal = tempCorpCalList.get(0);
            }
            //年度でフィルタリングするする必要があるため対象の年月の年度を取得する
            baseFiscalYear = DemandCalendarYearUtility.getFiscalYearNo(currentCorpCal,
                    corpDemandList.get(0).getSumDate());
            List<DemandCalendarYearData> filterCorpCalList = new ArrayList<>();
            for (DemandCalendarYearData yearCorp : yearCorpCalList) {
                //年度を比較する
                if (baseFiscalYear.equals(
                        DemandCalendarYearUtility.getFiscalYearNo(yearCorp, corpDemandList.get(0).getSumDate()))) {
                    filterCorpCalList.add(yearCorp);
                }
            }

            yearCorpCalList.clear();
            yearCorpCalList.addAll(filterCorpCalList);
            //フィルタリング結果を月終了日でソートする
            yearCorpCalList = yearCorpCalList.stream().sorted(
                    Comparator.comparing(DemandCalendarYearData::getMonthEndDate, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
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

            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                    buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                return new DemandOrgSummaryMeasurementDataResult();
            }
            //電力集計カレンダを作成する
            if (CheckUtility.isNullOrEmpty(sumDate)) {
                //集計時が取得できていない場合は建物デマンドから
                sumDate = buildingDemandList.get(0).getSumDate();
            }
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(calYearNoFrom, calYearNoTo, sumDate);
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return new DemandOrgSummaryMeasurementDataResult();
            }
            //年月日でフィルタリングする
            tempBuildingCalList = buildingCalList.stream()
                    .filter(i -> i.getMonthStartDate().compareTo(parameter.getYmd()) <= 0
                            && i.getMonthEndDate().compareTo(parameter.getYmd()) >= 0)
                    .collect(Collectors.toList());
            if (tempBuildingCalList == null || tempBuildingCalList.size() != 1) {
                return null;
            } else {
                currentBuildingCal = tempBuildingCalList.get(0);
            }

            //年度でフィルタリングするする必要があるため対象の年月の年度を取得する
            baseFiscalYear = DemandCalendarYearUtility.getFiscalYearNo(currentBuildingCal,
                    buildingDemandList.get(0).getSumDate());
            List<DemandCalendarYearData> filterBuildingCalList = new ArrayList<>();
            //フィルタリングした年度でフィルタリングする
            for (DemandCalendarYearData yearBuilding : buildingCalList) {
                //年度を比較する
                if (baseFiscalYear.equals(
                        DemandCalendarYearUtility.getFiscalYearNo(yearBuilding,
                                buildingDemandList.get(0).getSumDate()))) {
                    filterBuildingCalList.add(yearBuilding);
                }
            }

            buildingCalList.clear();
            buildingCalList.addAll(filterBuildingCalList);
            //フィルタリング結果を月終了日でソートする
            buildingCalList = buildingCalList.stream().sorted(
                    Comparator.comparing(DemandCalendarYearData::getMonthEndDate, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        //CO2排出量係数を取得する
        //建物デマンドを取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1
                || CheckUtility.isNullOrEmpty(buildingDemandList.get(0).getCo2EngTypeCd())
                || buildingDemandList.get(0).getCo2EngId() == null) {
            co2CalculationCoefficient = null;
        } else {
            //使用エネルギーからデータを取得する
            TAvailableEnergy availableParam = DemandEmsUtility.getTAvailableEnergyListParam(
                    parameter.getOperationCorpId(),
                    parameter.getBuildingId(), ApiCodeValueConstants.ENG_TYPE_CD.ELECTRICAL.getVal(),
                    buildingDemandList.get(0).getCo2EngId(), null);
            List<TAvailableEnergy> availableList = getResultList(tAvailableEnergyServiceDaoImpl, availableParam);
            TAvailableEnergy availableData = null;
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                availableData = DemandEmsUtility.getAvailableEnergyData(availableList,
                        DateUtility.conversionDate(
                                yearCorpCalList.get(0).getYearNo()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                yearCorpCalList.get(0).getMonthNo().intValue()))
                                        .concat("01"),
                                DateUtility.DATE_FORMAT_YYYYMMDD));
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                availableData = DemandEmsUtility.getAvailableEnergyData(availableList,
                        DateUtility.conversionDate(
                                buildingCalList.get(0).getYearNo()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                buildingCalList.get(0).getMonthNo().intValue()))
                                        .concat("01"),
                                DateUtility.DATE_FORMAT_YYYYMMDD));
            }
            //係数履歴管理からデータを取得する
            Long engId = null;
            String dayAndNightType = null;
            if(availableData != null) {
                engId = availableData.getId().getEngId();
                dayAndNightType = availableData.getDayAndNightType();
            }

            MCoefficientHistoryManage coefficientParam = DemandEmsUtility.getCoefficientHistroryListParam(
                    ApiCodeValueConstants.ENG_TYPE_CD.ELECTRICAL.getVal(), engId,dayAndNightType);
            List<MCoefficientHistoryManage> coefficientList = getResultList(coefficientHistoryManageServiceDaoImpl,
                    coefficientParam);
            MCoefficientHistoryManage coefficientData = null;
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                coefficientData = DemandEmsUtility.getCoefficientHistoryData(coefficientList,
                        DateUtility.conversionDate(
                                yearCorpCalList.get(0).getYearNo()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                yearCorpCalList.get(0).getMonthNo().intValue()))
                                        .concat("01"),
                                DateUtility.DATE_FORMAT_YYYYMMDD));
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                coefficientData = DemandEmsUtility.getCoefficientHistoryData(coefficientList,
                        DateUtility.conversionDate(
                                buildingCalList.get(0).getYearNo()
                                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                buildingCalList.get(0).getMonthNo().intValue()))
                                        .concat("01"),
                                DateUtility.DATE_FORMAT_YYYYMMDD));
            }
            if (coefficientData == null) {
                co2CalculationCoefficient = null;
            } else if (coefficientData.getStdCo2Coefficient() == null) {
                co2CalculationCoefficient = null;
            } else {
                co2CalculationCoefficient = coefficientData.getStdCo2Coefficient().multiply(new BigDecimal("1000"));
            }

        }

        //建物系統標準値を取得する（全グループ分取得する）
        BuildingLineTimeStandardListTimeResultData param = DemandEmsUtility.getBuildingLineTimeStandardListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(), null);
        buildingLineTimeStandardList = getResultList(buildingLineTimeStandardListServiceDaoImpl, param);

        //デマンド月報系統テーブルからデータを取得する
        CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), parameter.getYmd(), parameter.getYmd());
        monthReportLineList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);

        //デマンド年報系統テーブルからデータを取得する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            if (yearCorpCalList != null && !yearCorpCalList.isEmpty()) {
                //年No、月Noの範囲を取得する
                yearNoFrom = yearCorpCalList.get(0).getYearNo();
                yearNoTo = yearCorpCalList.get(yearCorpCalList.size() - 1).getYearNo();
                monthNoFrom = yearCorpCalList.get(0).getMonthNo();
                monthNoTo = yearCorpCalList.get(yearCorpCalList.size() - 1).getMonthNo();

                CommonDemandYearReportLineListResult yearReportParam = DemandEmsUtility.getYearReportLineListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), yearNoFrom,
                        yearNoTo, monthNoFrom, monthNoTo);
                yearReportLineList = getResultList(commonDemandYearReportLineListServiceDaoImpl, yearReportParam);
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            if (buildingCalList != null && !buildingCalList.isEmpty()) {
                //年No、月Noの範囲を取得する
                yearNoFrom = buildingCalList.get(0).getYearNo();
                yearNoTo = buildingCalList.get(buildingCalList.size() - 1).getYearNo();
                monthNoFrom = buildingCalList.get(0).getMonthNo();
                monthNoTo = buildingCalList.get(buildingCalList.size() - 1).getMonthNo();

                CommonDemandYearReportLineListResult yearReportParam = DemandEmsUtility.getYearReportLineListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), yearNoFrom,
                        yearNoTo, monthNoFrom, monthNoTo);
                yearReportLineList = getResultList(commonDemandYearReportLineListServiceDaoImpl, yearReportParam);
            }
        }

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), null, ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        //建物系統標準値の件数分処理を繰り返す
        if (lineList != null && !lineList.isEmpty()) {
            for (BuildingLineTimeStandardListTimeResultData lineTime : buildingLineTimeStandardList) {
                String lineTarget = "";

                if (lineTime.getLineLimitStandardKw() == null) {
                    //未設定の場合はスルー
                    continue;
                }

                for (LineListDetailResultData line : lineList) {
                    if (lineTime.getLineNo().equals(line.getLineNo())) {
                        lineTarget = line.getLineTarget();
                    }
                }
                if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(lineTarget)
                        || ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineTarget)) {
                    //ALLとロギングは処理をスルー
                    continue;
                }

                CommonDemandDayReportLineListResult dayParam = DemandEmsUtility.getDayReportLineListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                        lineTime.getLineNo(), parameter.getYmd(), lineTime.getJigenNo(), parameter.getYmd(),
                        lineTime.getJigenNo());
                dayReportLineList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayParam);

                if (dayReportLineList != null && dayReportLineList.size() == 1
                        && dayReportLineList.get(0).getLineValueKw().compareTo(lineTime.getLineLimitStandardKw()) > 0) {
                    //時限標準値を超えた場合のみ積算
                    overStandardKwSummary = overStandardKwSummary
                            .add(dayReportLineList.get(0).getLineValueKw().subtract(lineTime.getLineLimitStandardKw()));
                }

            }
        }

        //結果をセットする
        if (monthReportLineList != null && monthReportLineList.size() == 1) {
            result.setLastDayMaxValue(monthReportLineList.get(0).getLineMaxKw().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            result.setLastDayUsed(monthReportLineList.get(0).getLineValueKwh().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            if (co2CalculationCoefficient == null) {
                result.setLastDayCo2(null);
            } else {
                result.setLastDayCo2(monthReportLineList.get(0).getLineValueKwh().multiply(co2CalculationCoefficient)
                        .setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                .getControlType(parameter.getBelowAccuracyControl())));
            }

            result.setLastDayEcoValue(overStandardKwSummary.divide(new BigDecimal(2), parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        for (CommonDemandYearReportLineListResult yearReport : yearReportLineList) {
            if (thisYearUsed == null) {
                thisYearUsed = yearReport.getLineValueKwh();
            } else {
                thisYearUsed = thisYearUsed.add(yearReport.getLineValueKwh());
            }
            if (thisYearMaxValue == null) {
                thisYearMaxValue = yearReport.getLineMaxKw();
            } else if (yearReport.getLineMaxKw().compareTo(thisYearMaxValue) >= 0) {
                thisYearMaxValue = yearReport.getLineMaxKw();
            }
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                if (currentCorpCal != null) {
                    if (currentCorpCal.getYearNo().equals(yearReport.getYearNo())
                            && currentCorpCal.getMonthNo().compareTo(yearReport.getMonthNo()) == 0) {
                        result.setThisMonthMaxValue(yearReport.getLineMaxKw().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                        result.setThisMonthUsed(yearReport.getLineValueKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                        if (co2CalculationCoefficient == null) {
                            result.setThisMonthCo2(null);
                        } else {
                            result.setThisMonthCo2(
                                    yearReport.getLineValueKwh().multiply(co2CalculationCoefficient).setScale(
                                            parameter.getPrecision(),
                                            ApiCodeValueConstants.PRECISION_CONTROL
                                                    .getControlType(parameter.getBelowAccuracyControl())));
                        }
                    }
                }
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                if (currentBuildingCal != null) {
                    if (currentBuildingCal.getYearNo().equals(yearReport.getYearNo())
                            && currentBuildingCal.getMonthNo().compareTo(yearReport.getMonthNo()) == 0) {
                        result.setThisMonthMaxValue(yearReport.getLineMaxKw());
                        result.setThisMonthUsed(yearReport.getLineValueKwh());
                        if (co2CalculationCoefficient == null) {
                            result.setThisMonthCo2(null);
                        } else {
                            result.setThisMonthCo2(
                                    yearReport.getLineValueKwh().multiply(co2CalculationCoefficient).setScale(
                                            parameter.getPrecision(),
                                            ApiCodeValueConstants.PRECISION_CONTROL
                                                    .getControlType(parameter.getBelowAccuracyControl())));
                        }
                    }
                }
            }
        }

        if (thisYearUsed != null) {
            result.setThisYearUsed(
                    thisYearUsed.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
            if (co2CalculationCoefficient == null) {
                result.setThisYearCo2(null);
            } else {
                result.setThisYearCo2(thisYearUsed.multiply(co2CalculationCoefficient).setScale(
                        parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }
        } else {
            result.setThisYearUsed(null);
            result.setThisYearCo2(null);
        }

        if (thisYearMaxValue != null) {
            result.setThisYearMaxValue(thisYearMaxValue.setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        } else {
            result.setThisYearMaxValue(null);
        }

        return result;
    }

}
