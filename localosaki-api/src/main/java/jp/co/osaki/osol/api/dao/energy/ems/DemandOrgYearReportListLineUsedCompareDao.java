/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportListLineUsedCompareParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportListLineUsedCompareResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CommonDisplayElementListResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerYearDataDetailResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerYearDataResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerYearDataSummaryResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListLineUsedCompareDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListLineUsedCompareHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListLineUsedCompareHeaderTitleResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListLineUsedCompareSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * エネルギー使用状況実績取得（個別・年報・データ対比） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgYearReportListLineUsedCompareDao
        extends OsolApiDao<DemandOrgYearReportListLineUsedCompareParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final CommonDemandYearReportListServiceDaoImpl commonDemandYearReportListServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;
    private final CommonDemandYearReportPointListServiceDaoImpl commonDemandYearReportPointListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    public DemandOrgYearReportListLineUsedCompareDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        commonDemandYearReportListServiceDaoImpl = new CommonDemandYearReportListServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
        commonDemandYearReportPointListServiceDaoImpl = new CommonDemandYearReportPointListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgYearReportListLineUsedCompareResult query(DemandOrgYearReportListLineUsedCompareParameter parameter)
            throws Exception {
        DemandOrgYearReportListLineUsedCompareResult result = new DemandOrgYearReportListLineUsedCompareResult();
        DemandOrgYearReportListLineUsedCompareHeaderResultData header = new DemandOrgYearReportListLineUsedCompareHeaderResultData();
        List<DemandOrgYearReportListLineUsedCompareHeaderTitleResultData> headerTitleList = new ArrayList<>();
        List<DemandOrgYearReportListLineUsedCompareDetailResultData> detailList = new ArrayList<>();
        DemandOrgYearReportListLineUsedCompareSummaryAreaResultData summary = new DemandOrgYearReportListLineUsedCompareSummaryAreaResultData();
        List<DemandCalendarYearData> corpCalList = null;
        List<DemandCalendarYearData> buildingCalList = null;
        List<CommonDisplayElementListResult> displayElementList;
        CommonLinePowerYearDataResult linePowerData;
        CurrentYearReportResult currentYearReport = null;
        Map<String, Date> monthStartMap = null;
        String currentYm = "";
        List<BigDecimal> elementSummaryDestinationList = new ArrayList<>();
        List<BigDecimal> elementSummarySourceList = new ArrayList<>();
        List<BigDecimal> elementSummarySubstractedList = new ArrayList<>();
        List<BigDecimal> elementSummaryReductionList = new ArrayList<>();
        List<BigDecimal> elementMaxDestinationList = new ArrayList<>();
        List<BigDecimal> elementMaxSourceList = new ArrayList<>();
        List<BigDecimal> elementMaxSubstractedList = new ArrayList<>();
        List<BigDecimal> elementMaxReductionList = new ArrayList<>();
        List<BigDecimal> elementMinDestinationList = new ArrayList<>();
        List<BigDecimal> elementMinSourceList = new ArrayList<>();
        List<BigDecimal> elementMinSubstractedList = new ArrayList<>();
        List<BigDecimal> elementMinReductionList = new ArrayList<>();
        List<BigDecimal> elementAverageDestinationList = new ArrayList<>();
        List<BigDecimal> elementAverageSourceList = new ArrayList<>();
        List<BigDecimal> elementAverageSubstractedList = new ArrayList<>();
        List<BigDecimal> elementAverageReductionList = new ArrayList<>();
        SummaryRangeForYearResult summaryRangeDestination = null;
        SummaryRangeForYearResult summaryRangeSource = null;
        List<CorpDemandSelectResult> corpDemandList = null;
        List<BuildingDemandListDetailResultData> buildingDemandList = null;
        List<DemandCalendarYearData> buildingOrgCalList = null;
        CurrentYearReportResult currentOrgYearReport = null;
        Map<String, Date> monthOrgStartMap = null;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //集計日がNULLの場合、企業集計を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSummaryKind())) {
            parameter.setSummaryKind(ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal());
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、1（年間）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
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
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgYearReportListLineUsedCompareResult();
            }
            corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            if (corpCalList == null || corpCalList.isEmpty()) {
                return new DemandOrgYearReportListLineUsedCompareResult();
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                return new DemandOrgYearReportListLineUsedCompareResult();
            }
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    buildingDemandList.get(0).getSumDate());
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return null;
            }
        }

        //現在集計中の年月を取得する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            currentYearReport = DemandEmsUtility.getCurrentYearReport(corpCalList, getServerDateTime(),
                    corpDemandList.get(0).getSumDate());
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            currentYearReport = DemandEmsUtility.getCurrentYearReport(buildingCalList, getServerDateTime(),
                    buildingDemandList.get(0).getSumDate());
        }

        //比較先の集計範囲を取得する
        if (!"0".equals(parameter.getYearCompareDestination())) {
            summaryRangeDestination = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYearCompareDestination(),
                    parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        }

        //比較元の集計範囲を取得する
        if (!"0".equals(parameter.getYearCompareSource())) {
            summaryRangeSource = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYearCompareSource(),
                    parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        }

        //ヘッダ部の値をセットする
        if (summaryRangeDestination == null) {
            header.setYearFromDestination(null);
            header.setYearToDestination(null);
            header.setMonthFromDestination(null);
            header.setMonthToDestination(null);
        } else {
            header.setYearFromDestination(summaryRangeDestination.getYearFrom());
            header.setYearToDestination(summaryRangeDestination.getYearTo());
            header.setMonthFromDestination(summaryRangeDestination.getMonthFrom());
            header.setMonthToDestination(summaryRangeDestination.getMonthTo());
        }

        if (summaryRangeSource == null) {
            header.setYearFromSource(null);
            header.setYearToSource(null);
            header.setMonthFromSource(null);
            header.setMonthToSource(null);
        } else {
            header.setYearFromSource(summaryRangeSource.getYearFrom());
            header.setYearToSource(summaryRangeSource.getYearTo());
            header.setMonthFromSource(summaryRangeSource.getMonthFrom());
            header.setMonthToSource(summaryRangeSource.getMonthTo());
        }

        //カレンダ年月単位でMapを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            if (corpCalList != null && !corpCalList.isEmpty()) {
                monthStartMap = corpCalList.stream()
                        .collect(
                                Collectors.toMap(
                                        x -> x.getYearNo().concat(String.format(
                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                                        x -> x.getMonthStartDate()));
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            if (buildingCalList != null && !buildingCalList.isEmpty()) {
                monthStartMap = buildingCalList.stream()
                        .collect(
                                Collectors.toMap(
                                        x -> x.getYearNo().concat(String.format(
                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                                        x -> x.getMonthStartDate()));
            }
        }

        //明細部の年月と月開始日を設定する
        if (summaryRangeDestination != null && summaryRangeSource != null) {
            //比較元の情報を設定する
            do {
                DemandOrgYearReportListLineUsedCompareDetailResultData detail = new DemandOrgYearReportListLineUsedCompareDetailResultData();
                if (CheckUtility.isNullOrEmpty(currentYm)) {
                    currentYm = summaryRangeSource.getYearFrom().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    summaryRangeSource.getMonthFrom().intValue()));
                } else if (currentYm.substring(4).equals("12")) {
                    //12月の場合年を1年足して1月に
                    currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
                } else {
                    //1ヶ月足す
                    currentYm = currentYm.substring(0, 4).concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    Integer.parseInt(currentYm.substring(4)) + 1));
                }

                detail.setYmSource(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                detail.setMonthStartDateSourceList(new ArrayList<>());
                detail.setYmDestination(null);
                detail.setMonthStartDateDestinationList(new ArrayList<>());
                detail.setElementResultDestinationList(new ArrayList<>());
                detail.setElementResultSourceList(new ArrayList<>());
                detail.setElementSubstractedList(new ArrayList<>());
                detail.setElementReductionList(new ArrayList<>());
                detailList.add(detail);
            } while (!currentYm.equals(summaryRangeSource.getYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRangeSource.getMonthTo().intValue()))));

            currentYm = "";
            int i = 0;
            //比較先の情報を設定する
            do {

                if (CheckUtility.isNullOrEmpty(currentYm)) {
                    currentYm = summaryRangeDestination.getYearFrom().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    summaryRangeDestination.getMonthFrom().intValue()));
                } else if (currentYm.substring(4).equals("12")) {
                    //12月の場合年を1年足して1月に
                    currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
                } else {
                    //1ヶ月足す
                    currentYm = currentYm.substring(0, 4).concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    Integer.parseInt(currentYm.substring(4)) + 1));
                }

                //年報は順番にデータを投入すればよい
                detailList.get(i).setYmDestination(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                i++;

            } while (!currentYm.equals(summaryRangeDestination.getYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRangeDestination.getMonthTo().intValue()))));

        } else if (summaryRangeDestination == null && summaryRangeSource != null) {

            do {
                DemandOrgYearReportListLineUsedCompareDetailResultData detail = new DemandOrgYearReportListLineUsedCompareDetailResultData();
                if (CheckUtility.isNullOrEmpty(currentYm)) {
                    currentYm = summaryRangeSource.getYearFrom().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    summaryRangeSource.getMonthFrom().intValue()));
                } else if (currentYm.substring(4).equals("12")) {
                    //12月の場合年を1年足して1月に
                    currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
                } else {
                    //1ヶ月足す
                    currentYm = currentYm.substring(0, 4).concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    Integer.parseInt(currentYm.substring(4)) + 1));
                }

                detail.setYmSource(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                detail.setMonthStartDateSourceList(new ArrayList<>());
                detail.setYmDestination(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_MM));
                detail.setMonthStartDateDestinationList(new ArrayList<>());
                detail.setElementResultDestinationList(new ArrayList<>());
                detail.setElementResultSourceList(new ArrayList<>());
                detail.setElementSubstractedList(new ArrayList<>());
                detail.setElementReductionList(new ArrayList<>());
                detailList.add(detail);
            } while (!currentYm.equals(summaryRangeSource.getYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRangeSource.getMonthTo().intValue()))));

        } else if (summaryRangeDestination != null && summaryRangeSource == null) {

            do {
                DemandOrgYearReportListLineUsedCompareDetailResultData detail = new DemandOrgYearReportListLineUsedCompareDetailResultData();
                if (CheckUtility.isNullOrEmpty(currentYm)) {
                    currentYm = summaryRangeDestination.getYearFrom().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    summaryRangeDestination.getMonthFrom().intValue()));
                } else if (currentYm.substring(4).equals("12")) {
                    //12月の場合年を1年足して1月に
                    currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
                } else {
                    //1ヶ月足す
                    currentYm = currentYm.substring(0, 4).concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    Integer.parseInt(currentYm.substring(4)) + 1));
                }

                detail.setYmSource(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_MM));
                detail.setYmDestination(DateUtility.changeDateFormat(
                        DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                detail.setElementResultDestinationList(new ArrayList<>());
                detail.setElementResultSourceList(new ArrayList<>());
                detail.setElementSubstractedList(new ArrayList<>());
                detail.setElementReductionList(new ArrayList<>());
                detailList.add(detail);
            } while (!currentYm.equals(summaryRangeDestination.getYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRangeDestination.getMonthTo().intValue()))));

        }

        //表示対象要素情報を取得する
        if (parameter.getGraphId() == null) {
            displayElementList = getDisplayElementList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                    parameter.getLineGroupId());
        } else {
            displayElementList = getDisplayElementList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                    parameter.getLineGroupId(), parameter.getGraphId());
        }
        if (displayElementList == null || displayElementList.isEmpty()) {
            result.setHeader(header);
            result.setDetailList(detailList);
            return result;
        }

        for (CommonDisplayElementListResult displayElement : displayElementList) {
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                            .equals(displayElement.getElementType())) {
                headerTitleList.add(
                        new DemandOrgYearReportListLineUsedCompareHeaderTitleResultData(displayElement.getElementName(),
                                ApiSimpleConstants.UNIT_USE_POWER, ApiSimpleConstants.UNIT_USE_POWER,
                                displayElement.getElementType(),
                                displayElement.getGraphColor()));
            } else {
                headerTitleList.add(new DemandOrgYearReportListLineUsedCompareHeaderTitleResultData(
                        displayElement.getElementName(),
                        displayElement.getUnit(), displayElement.getSummaryUnit(), displayElement.getElementType(),
                        displayElement.getGraphColor()));
            }

            //系統別データを取得する（比較先）
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal()
                            .equals(displayElement.getElementType())) {

                if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                    //建物集計の場合のみ、集計日のカレンダの見直しを行う
                    //デマンド集計系統情報を取得する
                    AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility
                            .getAggregateDmLineListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                    parameter.getLineGroupId(), displayElement.getLineNo(), null, null, null);
                    List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                            aggregateDmLineListServiceDaoImpl, aggregateLineParam);
                    if (aggregateLineList != null && aggregateLineList.size() == 1) {
                        //デマンド集計情報を取得する
                        AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                                parameter.getOperationCorpId(), parameter.getBuildingId(),
                                aggregateLineList.get(0).getAggregateDmId());
                        List<AggregateDmListDetailResultData> aggregateList = getResultList(
                                aggregateDmListServiceDaoImpl, aggregateParam);
                        if (aggregateList != null && aggregateList.size() == 1
                                && !CheckUtility.isNullOrEmpty(aggregateList.get(0).getSumDate())) {
                            //集計日が設定されている場合、新しいカレンダを作成する
                            buildingOrgCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                                    aggregateList.get(0).getSumDate());
                            if (buildingOrgCalList == null || buildingOrgCalList.isEmpty()) {
                                buildingOrgCalList = null;
                            } else {
                                //現在集計中の年月を取得する
                                currentOrgYearReport = DemandEmsUtility.getCurrentYearReport(buildingOrgCalList,
                                        getServerDateTime(),
                                        aggregateList.get(0).getSumDate());
                                //カレンダ年月単位でMapを作成する
                                monthOrgStartMap = buildingOrgCalList.stream()
                                        .collect(
                                                Collectors.toMap(
                                                        x -> x.getYearNo().concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                x.getMonthNo().intValue())),
                                                        x -> x.getMonthStartDate()));
                            }
                        }
                    }
                }
                if (summaryRangeDestination == null) {
                    linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getLineGroupId(),
                            displayElement.getLineNo(), parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                            displayElement.getElementType());
                } else {
                    if (currentOrgYearReport != null) {
                        linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(),
                                parameter.getBuildingId(),
                                parameter.getLineGroupId(),
                                displayElement.getLineNo(),
                                summaryRangeDestination.getYearFrom(), summaryRangeDestination.getYearTo(),
                                summaryRangeDestination.getMonthFrom(), summaryRangeDestination.getMonthTo(),
                                parameter.getSummaryKind(),
                                parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                                displayElement.getElementType(), currentOrgYearReport);
                    } else {
                        linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(),
                                parameter.getBuildingId(),
                                parameter.getLineGroupId(),
                                displayElement.getLineNo(),
                                summaryRangeDestination.getYearFrom(), summaryRangeDestination.getYearTo(),
                                summaryRangeDestination.getMonthFrom(), summaryRangeDestination.getMonthTo(),
                                parameter.getSummaryKind(),
                                parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                                displayElement.getElementType(), currentYearReport);
                    }

                }
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                if (summaryRangeDestination == null) {
                    linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            displayElement.getSmId(),
                            displayElement.getPointNo(),
                            parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
                } else {
                    linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            displayElement.getSmId(),
                            displayElement.getPointNo(),
                            summaryRangeDestination.getYearFrom(), summaryRangeDestination.getYearTo(),
                            summaryRangeDestination.getMonthFrom(), summaryRangeDestination.getMonthTo(),
                            parameter.getSummaryKind(),
                            parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), currentYearReport);
                }
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                if (summaryRangeDestination == null) {
                    linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(),
                            parameter.getBuildingId(), parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
                } else {
                    linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(),
                            parameter.getBuildingId(),
                            summaryRangeDestination.getYearFrom(),
                            summaryRangeDestination.getYearTo(), summaryRangeDestination.getMonthFrom(),
                            summaryRangeDestination.getMonthTo(), parameter.getSummaryKind(), parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), currentYearReport);
                }
            } else {
                linePowerData = null;
            }

            //系統別データが取得できた・できていないに関わらず、月開始日を指定する
            for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                if (summaryRangeDestination == null) {
                    detail.getMonthStartDateDestinationList().add(null);
                } else if (monthOrgStartMap != null) {
                    detail.getMonthStartDateDestinationList().add(DateUtility
                            .changeDateFormat(monthOrgStartMap.get(detail.getYmDestination().replaceAll("/", "")),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
                } else {
                    detail.getMonthStartDateDestinationList().add(DateUtility
                            .changeDateFormat(monthStartMap.get(detail.getYmDestination().replace("/", "")),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
                }
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummaryDestinationList.add(null);
                elementMaxDestinationList.add(null);
                elementMinDestinationList.add(null);
                elementAverageDestinationList.add(null);
                for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultDestinationList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummaryDestinationList.add(linePowerData.getSummary().getSummaryNumUsed());
                elementMaxDestinationList.add(linePowerData.getSummary().getMaxNumUsed());
                elementMinDestinationList.add(linePowerData.getSummary().getMinNumUsed());
                elementAverageDestinationList.add(linePowerData.getSummary().getAverageNumUsed());
                for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerYearDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (summaryRangeDestination == null) {
                            if (detailResult.getMonthNo().compareTo(new BigDecimal(detail.getYmDestination())) == 0) {
                                detail.getElementResultDestinationList().add(detailResult.getResultNumUsed());
                                flg = true;
                                break;
                            }
                        } else if (detailResult.getYearNo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        detailResult.getMonthNo().intValue()))
                                .equals(detail.getYmDestination().replace(ApiSimpleConstants.SLASH, ""))) {
                            detail.getElementResultDestinationList().add(detailResult.getResultNumUsed());
                            flg = true;
                            break;
                        }
                    }
                    if (!flg) {
                        //実績値が設定できていない
                        detail.getElementResultDestinationList().add(null);
                    }
                }
            }

            //系統別データを取得する（比較元）
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal()
                            .equals(displayElement.getElementType())) {

                if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                    //建物集計の場合のみ、集計日のカレンダの見直しを行う
                    //デマンド集計系統情報を取得する
                    AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility
                            .getAggregateDmLineListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                    parameter.getLineGroupId(), displayElement.getLineNo(), null, null, null);
                    List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                            aggregateDmLineListServiceDaoImpl, aggregateLineParam);
                    if (aggregateLineList != null && aggregateLineList.size() == 1) {
                        //デマンド集計情報を取得する
                        AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                                parameter.getOperationCorpId(), parameter.getBuildingId(),
                                aggregateLineList.get(0).getAggregateDmId());
                        List<AggregateDmListDetailResultData> aggregateList = getResultList(
                                aggregateDmListServiceDaoImpl, aggregateParam);
                        if (aggregateList != null && aggregateList.size() == 1
                                && !CheckUtility.isNullOrEmpty(aggregateList.get(0).getSumDate())) {
                            //集計日が設定されている場合、新しいカレンダを作成する
                            buildingOrgCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                                    aggregateList.get(0).getSumDate());
                            if (buildingOrgCalList == null || buildingOrgCalList.isEmpty()) {
                                buildingOrgCalList = null;
                            } else {
                                //現在集計中の年月を取得する
                                currentOrgYearReport = DemandEmsUtility.getCurrentYearReport(buildingOrgCalList,
                                        getServerDateTime(),
                                        aggregateList.get(0).getSumDate());
                                //カレンダ年月単位でMapを作成する
                                monthOrgStartMap = buildingOrgCalList.stream()
                                        .collect(
                                                Collectors.toMap(
                                                        x -> x.getYearNo().concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                x.getMonthNo().intValue())),
                                                        x -> x.getMonthStartDate()));
                            }
                        }
                    }
                }

                if (summaryRangeSource == null) {
                    linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getLineGroupId(),
                            displayElement.getLineNo(), parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                            displayElement.getElementType());
                } else {
                    if (currentOrgYearReport != null) {
                        linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(),
                                parameter.getBuildingId(),
                                parameter.getLineGroupId(),
                                displayElement.getLineNo(),
                                summaryRangeSource.getYearFrom(), summaryRangeSource.getYearTo(),
                                summaryRangeSource.getMonthFrom(),
                                summaryRangeSource.getMonthTo(), parameter.getSummaryKind(), parameter.getPrecision(),
                                parameter.getBelowAccuracyControl(),
                                displayElement.getElementType(), currentOrgYearReport);
                    } else {
                        linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(),
                                parameter.getBuildingId(),
                                parameter.getLineGroupId(),
                                displayElement.getLineNo(),
                                summaryRangeSource.getYearFrom(), summaryRangeSource.getYearTo(),
                                summaryRangeSource.getMonthFrom(),
                                summaryRangeSource.getMonthTo(), parameter.getSummaryKind(), parameter.getPrecision(),
                                parameter.getBelowAccuracyControl(),
                                displayElement.getElementType(), currentYearReport);
                    }

                }
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                if (summaryRangeSource == null) {
                    linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            displayElement.getSmId(),
                            displayElement.getPointNo(),
                            parameter.getPrecision(), parameter.getBelowAccuracyControl());
                } else {
                    linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            displayElement.getSmId(),
                            displayElement.getPointNo(),
                            summaryRangeSource.getYearFrom(), summaryRangeSource.getYearTo(),
                            summaryRangeSource.getMonthFrom(),
                            summaryRangeSource.getMonthTo(), parameter.getSummaryKind(), parameter.getPrecision(),
                            parameter.getBelowAccuracyControl(), currentYearReport);
                }
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                if (summaryRangeSource == null) {
                    linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(),
                            parameter.getBuildingId(), parameter.getPrecision(), parameter.getBelowAccuracyControl());
                } else {
                    linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(),
                            parameter.getBuildingId(), summaryRangeSource.getYearFrom(),
                            summaryRangeSource.getYearTo(), summaryRangeSource.getMonthFrom(),
                            summaryRangeSource.getMonthTo(), parameter.getSummaryKind(),
                            parameter.getPrecision(), parameter.getBelowAccuracyControl(), currentYearReport);
                }
            } else {
                linePowerData = null;
            }

            //系統別データが取得できた・できていないに関わらず、月開始日を指定する
            for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                if (summaryRangeSource == null) {
                    detail.getMonthStartDateSourceList().add(null);
                } else if (monthOrgStartMap != null) {
                    detail.getMonthStartDateSourceList().add(DateUtility
                            .changeDateFormat(monthOrgStartMap.get(detail.getYmSource().replace("/", "")),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
                } else {
                    detail.getMonthStartDateSourceList().add(DateUtility
                            .changeDateFormat(monthStartMap.get(detail.getYmSource().replace("/", "")),
                                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
                }
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummarySourceList.add(null);
                elementMaxSourceList.add(null);
                elementMinSourceList.add(null);
                elementAverageSourceList.add(null);
                for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultSourceList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummarySourceList.add(linePowerData.getSummary().getSummaryNumUsed());
                elementMaxSourceList.add(linePowerData.getSummary().getMaxNumUsed());
                elementMinSourceList.add(linePowerData.getSummary().getMinNumUsed());
                elementAverageSourceList.add(linePowerData.getSummary().getAverageNumUsed());
                for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerYearDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (summaryRangeSource == null) {
                            if (detailResult.getMonthNo().compareTo(new BigDecimal(detail.getYmSource())) == 0) {
                                detail.getElementResultSourceList().add(detailResult.getResultNumUsed());
                                flg = true;
                                break;
                            }
                        } else if (detailResult.getYearNo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        detailResult.getMonthNo().intValue()))
                                .equals(detail.getYmSource().replace(ApiSimpleConstants.SLASH, ""))) {
                            detail.getElementResultSourceList().add(detailResult.getResultNumUsed());
                            flg = true;
                            break;
                        }
                    }
                    if (!flg) {
                        //実績値が設定できていない
                        detail.getElementResultSourceList().add(null);
                    }
                }
            }
        }

        header.setHeaderTitleElementResultList(headerTitleList);
        //差引、削減率を算出する
        for (int i = 0; i <= header.getHeaderTitleElementResultList().size() - 1; i++) {
            //サマリ部の差引、削減率
            if (elementSummaryDestinationList.get(i) == null || elementSummarySourceList.get(i) == null) {
                elementSummarySubstractedList.add(i, null);
                elementSummaryReductionList.add(i, null);
            } else if (elementSummaryDestinationList.get(i).compareTo(BigDecimal.ZERO) == 0
                    || elementSummarySourceList.get(i).compareTo(BigDecimal.ZERO) == 0) {
                elementSummarySubstractedList.add(i,
                        elementSummaryDestinationList.get(i).subtract(elementSummarySourceList.get(i)));
                elementSummaryReductionList.add(i, BigDecimal.ZERO);
            } else {
                elementSummarySubstractedList.add(i,
                        elementSummaryDestinationList.get(i).subtract(elementSummarySourceList.get(i)));
                elementSummaryReductionList.add(i,
                        (elementSummaryDestinationList.get(i).subtract(elementSummarySourceList.get(i))
                                .multiply(new BigDecimal(100))
                                .divide(elementSummaryDestinationList.get(i), parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl()))));
            }

            if (elementMaxDestinationList.get(i) == null || elementMaxSourceList.get(i) == null) {
                elementMaxSubstractedList.add(i, null);
                elementMaxReductionList.add(i, null);
            } else if (elementMaxDestinationList.get(i).compareTo(BigDecimal.ZERO) == 0
                    || elementMaxSourceList.get(i).compareTo(BigDecimal.ZERO) == 0) {
                elementMaxSubstractedList.add(i,
                        elementMaxDestinationList.get(i).subtract(elementMaxSourceList.get(i)));
                elementMaxReductionList.add(i, BigDecimal.ZERO);
            } else {
                elementMaxSubstractedList.add(i,
                        elementMaxDestinationList.get(i).subtract(elementMaxSourceList.get(i)));
                elementMaxReductionList.add(i,
                        (elementMaxDestinationList.get(i).subtract(elementMaxSourceList.get(i))
                                .multiply(new BigDecimal(100)).divide(
                                        elementMaxDestinationList.get(i), parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl()))));
            }

            if (elementMinDestinationList.get(i) == null || elementMinSourceList.get(i) == null) {
                elementMinSubstractedList.add(i, null);
                elementMinReductionList.add(i, null);
            } else if (elementMinDestinationList.get(i).compareTo(BigDecimal.ZERO) == 0
                    || elementMinSourceList.get(i).compareTo(BigDecimal.ZERO) == 0) {
                elementMinSubstractedList.add(i,
                        elementMinDestinationList.get(i).subtract(elementMinSourceList.get(i)));
                elementMinReductionList.add(i, BigDecimal.ZERO);
            } else {
                elementMinSubstractedList.add(i,
                        elementMinDestinationList.get(i).subtract(elementMinSourceList.get(i)));
                elementMinReductionList.add(i,
                        (elementMinDestinationList.get(i).subtract(elementMinSourceList.get(i))
                                .multiply(new BigDecimal(100)).divide(
                                        elementMinDestinationList.get(i), parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl()))));
            }

            if (elementAverageDestinationList.get(i) == null || elementAverageSourceList.get(i) == null) {
                elementAverageSubstractedList.add(i, null);
                elementAverageReductionList.add(i, null);
            } else if (elementAverageDestinationList.get(i).compareTo(BigDecimal.ZERO) == 0
                    || elementAverageSourceList.get(i).compareTo(BigDecimal.ZERO) == 0) {
                elementAverageSubstractedList.add(i,
                        elementAverageDestinationList.get(i).subtract(elementAverageSourceList.get(i)));
                elementAverageReductionList.add(i, BigDecimal.ZERO);
            } else {
                elementAverageSubstractedList.add(i,
                        elementAverageDestinationList.get(i).subtract(elementAverageSourceList.get(i)));
                elementAverageReductionList.add(i,
                        (elementAverageDestinationList.get(i).subtract(elementAverageSourceList.get(i))
                                .multiply(new BigDecimal(100))
                                .divide(elementAverageDestinationList.get(i), parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl()))));
            }

            //明細部の差引、削減率
            for (DemandOrgYearReportListLineUsedCompareDetailResultData detail : detailList) {
                if (detail.getElementResultDestinationList().get(i) == null
                        || detail.getElementResultSourceList().get(i) == null
                        || detail.getElementResultDestinationList().get(i).compareTo(BigDecimal.ZERO) == 0
                        || detail.getElementResultSourceList().get(i).compareTo(BigDecimal.ZERO) == 0) {
                    detail.getElementSubstractedList().add(i, null);
                    detail.getElementReductionList().add(i, null);
                } else if (detail.getElementResultDestinationList().get(i).compareTo(BigDecimal.ZERO) == 0
                        || detail.getElementResultSourceList().get(i).compareTo(BigDecimal.ZERO) == 0) {
                    detail.getElementSubstractedList().add(i,
                            detail.getElementResultDestinationList().get(i)
                                    .subtract(detail.getElementResultSourceList().get(i)));
                    detail.getElementReductionList().add(i, BigDecimal.ZERO);
                } else {
                    detail.getElementSubstractedList().add(i,
                            detail.getElementResultDestinationList().get(i)
                                    .subtract(detail.getElementResultSourceList().get(i)));
                    detail.getElementReductionList().add(i,
                            (detail.getElementResultDestinationList().get(i)
                                    .subtract(detail.getElementResultSourceList().get(i))
                                    .multiply(new BigDecimal(100))
                                    .divide(detail.getElementResultDestinationList().get(i), parameter.getPrecision(),
                                            ApiCodeValueConstants.PRECISION_CONTROL
                                                    .getControlType(parameter.getBelowAccuracyControl()))));
                }
            }
        }

        result.setHeader(header);
        result.setDetailList(detailList);
        summary.setElementSummaryDestinationList(elementSummaryDestinationList);
        summary.setElementSummarySourceList(elementSummarySourceList);
        summary.setElementSummarySubstractedList(elementSummarySubstractedList);
        summary.setElementSummaryReductionList(elementSummaryReductionList);
        summary.setElementMaxDestinationList(elementMaxDestinationList);
        summary.setElementMaxSourceList(elementMaxSourceList);
        summary.setElementMaxSubstractedList(elementMaxSubstractedList);
        summary.setElementMaxReductionList(elementMaxReductionList);
        summary.setElementMinDestinationList(elementMinDestinationList);
        summary.setElementMinSourceList(elementMinSourceList);
        summary.setElementMinSubstractedList(elementMinSubstractedList);
        summary.setElementMinReductionList(elementMinReductionList);
        summary.setElementAverageDestinationList(elementAverageDestinationList);
        summary.setElementAverageSourceList(elementAverageSourceList);
        summary.setElementAverageSubstractedList(elementAverageSubstractedList);
        summary.setElementAverageReductionList(elementAverageReductionList);
        result.setSummary(summary);
        return result;
    }

    /**
     * 表示対象要素情報取得（グラフID指定）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param graphId
     * @return
     */
    private List<CommonDisplayElementListResult> getDisplayElementList(String corpId, Long buildingId,
            Long lineGroupId, Long graphId) {
        List<CommonDisplayElementListResult> list = new ArrayList<>();

        //グラフ要素設定情報を取得する
        DemandGraphElementListDetailResultData elementParam = DemandEmsUtility.getGraphElementListParam(corpId,
                buildingId, lineGroupId, graphId, null);
        List<DemandGraphElementListDetailResultData> elementList = getResultList(demandGraphElementListServiceDaoImpl,
                elementParam);
        if (elementList == null || elementList.isEmpty()) {
            return null;
        }

        //グラフ要素種別、表示順でソートする
        elementList = SortUtility.sortElementListByGraphElementType(elementList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        for (DemandGraphElementListDetailResultData element : elementList) {
            if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.DEMAND.getVal().equals(element.getGraphElementType())) {
                //デマンドの場合、系統情報からデータを取得する
                LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(corpId, lineGroupId,
                        element.getGraphLineNo(), ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
                List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);
                if (lineList == null || lineList.size() != 1) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }

                if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(lineList.get(0).getLineNo())) {
                    //全体の場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            null,
                            null,
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                } else if (ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
                    //ロギングの場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            lineList.get(0).getLineUnit(),
                            lineList.get(0).getLineUnit(),
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                } else {
                    //ロギング以外の場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            null,
                            null,
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                }
            } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.ANALOG.getVal().equals(element.getGraphElementType())) {
                //アナログの場合、建物機器ポイント情報からデータを取得する
                DemandBuildingSmPointListDetailResultData pointParam = DemandEmsUtility.getBuildingSmPointListParam(
                        corpId, buildingId, element.getGraphSmId(), element.getGraphPointNo());
                List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                        demandBuildingSmPointListServiceDaoImpl, pointParam);
                if (buildingSmPointList == null || buildingSmPointList.size() != 1
                        || ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal()
                                .equals(buildingSmPointList.get(0).getPointSumFlg())) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        buildingSmPointList.get(0).getPointName(),
                        buildingSmPointList.get(0).getPointUnit(),
                        buildingSmPointList.get(0).getPointUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal(),
                        element.getGraphColorCode(),
                        null,
                        null,
                        null,
                        element.getGraphSmId(),
                        element.getGraphPointNo()));

            } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.AMEDAS.getVal().equals(element.getGraphElementType())) {
                //アメダスの場合、建物デマンド情報からデータを取得する
                BuildingDemandListDetailResultData demandParam = DemandEmsUtility.getBuildingDemandListParam(corpId,
                        buildingId);
                List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                        demandParam);
                if (demandList == null || demandList.size() != 1 || ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF
                        .getVal().equals(demandList.get(0).getOutAirTempDispFlg())) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        ApiSimpleConstants.OUT_TEMPERATURE,
                        ApiSimpleConstants.UNIT_TEMPERATURE,
                        ApiSimpleConstants.UNIT_TEMPERATURE,
                        ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal(),
                        element.getGraphColorCode(),
                        null,
                        null,
                        null,
                        null,
                        null));
            } else {
                continue;
            }
        }

        return list;
    }

    /**
     * 表示対象要素情報取得（グラフID指定なし）
     *
     * @param corpId
     * @param buidingId
     * @param lineGroupId
     * @return
     */
    private List<CommonDisplayElementListResult> getDisplayElementList(String corpId, Long buildingId,
            Long lineGroupId) {

        List<CommonDisplayElementListResult> list = new ArrayList<>();

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(corpId, lineGroupId, null,
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);

        //系統番号順にソートする
        lineList = SortUtility.sortLineListByLineNo(lineList, ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        for (LineListDetailResultData line : lineList) {
            if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(line.getLineNo())) {
                //全体の場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            } else if (ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                //ロギングの場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        line.getLineUnit(),
                        line.getLineUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            } else {
                //ロギング以外の場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            }
        }

        //建物機器ポイント情報を取得する
        DemandBuildingSmPointListDetailResultData pointParam = DemandEmsUtility.getBuildingSmPointListParam(corpId,
                buildingId, null, null);
        List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                demandBuildingSmPointListServiceDaoImpl, pointParam);

        if (buildingSmPointList != null && !buildingSmPointList.isEmpty()) {
            //機器ID、ポイント番号でソートする
            buildingSmPointList = buildingSmPointList.stream().sorted(Comparator
                    .comparing(DemandBuildingSmPointListDetailResultData::getSmId,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(DemandBuildingSmPointListDetailResultData::getPointNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        for (DemandBuildingSmPointListDetailResultData demandSmPoint : buildingSmPointList) {
            if (ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal().equals(demandSmPoint.getPointSumFlg())) {
                continue;
            } else {
                //機器ポイント情報を取得
                SmPointListDetailResultData smParam = DemandEmsUtility.getSmPointListParam(demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo());
                List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smParam);
                if (smPointList == null || smPointList.size() != 1
                        || !ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal()
                                .equals(smPointList.get(0).getPointType())) {
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        demandSmPoint.getPointName(),
                        demandSmPoint.getPointUnit(),
                        demandSmPoint.getPointUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal(),
                        null,
                        null,
                        null,
                        null,
                        demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo()));
            }
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = DemandEmsUtility.getBuildingDemandListParam(corpId,
                buildingId);
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);
        if (buildingDemandList != null && buildingDemandList.size() == 1
                && !ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF.getVal()
                        .equals(buildingDemandList.get(0).getOutAirTempDispFlg())) {
            list.add(new CommonDisplayElementListResult(
                    ApiSimpleConstants.OUT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null));
        }

        //グラフ自動配色を取得する
        if (!list.isEmpty()) {
            List<String> colorList = DemandGraphAutoColorUtility.getGraphAutoColorList(list.size());
            if (colorList.size() == list.size()) {
                //全色取得できた場合のみ設定
                for (int i = 0; i < colorList.size(); i++) {
                    list.get(i).setGraphColor(colorList.get(i));
                }
            }
        }

        return list;
    }

    /**
     * 系統別データ作成（系統・年報）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param elementType
     * @param currentYear
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromLine(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, String yearNoFrom, String yearNoTo, BigDecimal monthNoFrom,
            BigDecimal monthNoTo, String summaryUnit, Integer decimal, String control, String elementType,
            CurrentYearReportResult currentYear) {

        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;

        //デマンド年報系統からデータを取得する
        CommonDemandYearReportLineListResult yearLineParam = DemandEmsUtility.getYearReportLineListParam(corpId,
                buildingId, summaryUnit, lineGroupId, lineNo, yearNoFrom, yearNoTo, monthNoFrom, monthNoTo);
        List<CommonDemandYearReportLineListResult> yearLineList = getResultList(
                commonDemandYearReportLineListServiceDaoImpl, yearLineParam);
        if (yearLineList != null && !yearLineList.isEmpty()) {

            yearLineList = SortUtility.sortCommonDemandYearReportLineListByYearNoMonthNo(yearLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandYearReportLineListResult line : yearLineList) {
            //サマリ値の設定
            if (line.getLineValueKwh() != null) {
                if (summary == null) {
                    summary = line.getLineValueKwh();
                } else {
                    summary = summary.add(line.getLineValueKwh());
                }

                if (maxUse == null) {
                    maxUse = line.getLineValueKwh();
                } else if (line.getLineValueKwh().compareTo(maxUse) >= 0) {
                    maxUse = line.getLineValueKwh();
                }
            }

            if (line.getLineMaxKw() != null) {
                if (maxMax == null) {
                    maxMax = line.getLineMaxKw();
                } else if (line.getLineMaxKw().compareTo(maxMax) >= 0) {
                    maxMax = line.getLineMaxKw();
                }
            }

            if (Integer.parseInt(line.getYearNo().concat(
                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, line.getMonthNo().intValue()))) >= Integer
                            .parseInt(currentYear.getCalYear()
                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                            currentYear.getMonthNo().intValue())))) {
                //データの年、月Noが現在積算中の年、月以降は最小値、平均値をとらない
            } else {

                if (line.getLineValueKwh() != null) {
                    if (min == null) {
                        min = line.getLineValueKwh();
                    } else if (line.getLineValueKwh().compareTo(min) <= 0) {
                        min = line.getLineValueKwh();
                    }

                    if (averageSummary == null) {
                        averageSummary = line.getLineValueKwh();
                    } else {
                        averageSummary = averageSummary.add(line.getLineValueKwh());
                    }
                    if (averageCount == null) {
                        averageCount = BigDecimal.ONE;
                    } else {
                        averageCount = averageCount.add(BigDecimal.ONE);
                    }
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(line.getYearNo());
            detailData.setMonthNo(line.getMonthNo());
            if (line.getLineValueKwh() != null) {
                detailData.setResultNumUsed(line.getLineValueKwh().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(line.getLineValueKwh());
            }
            if (line.getLineMaxKw() != null) {
                detailData.setResultNumMax(line.getLineMaxKw().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(line.getLineMaxKw());
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (summary != null) {
            summaryData.setSummaryNumUsed(
                    summary.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（ポイント・年報）
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param currentYear
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromPoint(String corpId, Long buildingId, Long smId,
            String pointNo, String yearNoFrom, String yearNoTo, BigDecimal monthNoFrom, BigDecimal monthNoTo,
            String summaryUnit, Integer decimal, String control, CurrentYearReportResult currentYear) {
        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;
        BigDecimal pointAverageValAfterFormat;
        BigDecimal pointMaxValAfterFormat;

        //機器ポイントデータを取得する
        SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);

        //デマンド年報ポイントからデータを取得する
        CommonDemandYearReportPointListResult yearPointParam = DemandEmsUtility.getYearReportPointListParam(corpId,
                buildingId, smId, summaryUnit, yearNoFrom, yearNoTo, monthNoFrom, monthNoTo, pointNo, pointNo);
        List<CommonDemandYearReportPointListResult> yearPointList = getResultList(
                commonDemandYearReportPointListServiceDaoImpl, yearPointParam);

        //年月順にソートする
        if (yearPointList != null && !yearPointList.isEmpty()) {

            yearPointList = SortUtility.sortCommonDemandYearReportPointListByYearNoMonthNo(yearPointList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        }

        for (CommonDemandYearReportPointListResult point : yearPointList) {

            if (point.getPointAvg() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointAverageValAfterFormat = point.getPointAvg().add(smPointList.get(0).getAnalogOffSetValue());
                    pointAverageValAfterFormat = pointAverageValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointAverageValAfterFormat = point.getPointAvg();
                }
            } else {
                pointAverageValAfterFormat = point.getPointAvg();
            }

            if (point.getPointMax() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointMaxValAfterFormat = point.getPointMax().add(smPointList.get(0).getAnalogOffSetValue());
                    pointMaxValAfterFormat = pointMaxValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointMaxValAfterFormat = point.getPointMax();
                }
            } else {
                pointMaxValAfterFormat = point.getPointMax();
            }

            //サマリ値の設定
            if (pointAverageValAfterFormat != null) {
                if (maxUse == null) {
                    maxUse = pointAverageValAfterFormat;
                } else if (pointAverageValAfterFormat.compareTo(maxUse) >= 0) {
                    maxUse = pointAverageValAfterFormat;
                }
            }

            if (pointMaxValAfterFormat != null) {
                if (maxMax == null) {
                    maxMax = pointMaxValAfterFormat;
                } else if (pointMaxValAfterFormat.compareTo(maxMax) >= 0) {
                    maxMax = pointMaxValAfterFormat;
                }
            }

            if (Integer.parseInt(point.getYearNo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            point.getMonthNo().intValue()))) >= Integer
                                    .parseInt(currentYear.getCalYear()
                                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                    currentYear.getMonthNo().intValue())))) {
                //データの年、月Noが現在積算中の年、月以降は最小値、平均値をとらない
            } else {

                if (pointAverageValAfterFormat != null) {
                    if (min == null) {
                        min = pointAverageValAfterFormat;
                    } else if (pointAverageValAfterFormat.compareTo(min) <= 0) {
                        min = pointAverageValAfterFormat;
                    }

                    if (averageSummary == null) {
                        averageSummary = pointAverageValAfterFormat;
                    } else {
                        averageSummary = averageSummary.add(pointAverageValAfterFormat);
                    }
                    if (averageCount == null) {
                        averageCount = BigDecimal.ONE;
                    } else {
                        averageCount = averageCount.add(BigDecimal.ONE);
                    }
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(point.getYearNo());
            detailData.setMonthNo(point.getMonthNo());
            if (pointAverageValAfterFormat != null) {
                detailData.setResultNumUsed(pointAverageValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(pointAverageValAfterFormat);
            }
            if (pointMaxValAfterFormat != null) {
                detailData.setResultNumMax(pointMaxValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(pointMaxValAfterFormat);
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（アメダス・年報）
     *
     * @param corpId
     * @param buildingId
     * @param yearNoFrom
     * @param yearNoTo
     * @param monthNoFrom
     * @param monthNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param currentYear
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromAmedas(String corpId, Long buildingId,
            String yearNoFrom, String yearNoTo, BigDecimal monthNoFrom, BigDecimal monthNoTo, String summaryUnit,
            Integer decimal, String control, CurrentYearReportResult currentYear) {
        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;

        //デマンド年報からデータを取得する
        CommonDemandYearReportListResult yearParam = DemandEmsUtility.getYearReportListParam(corpId, buildingId,
                summaryUnit, yearNoFrom, yearNoTo, monthNoFrom, monthNoTo);
        List<CommonDemandYearReportListResult> yearList = getResultList(commonDemandYearReportListServiceDaoImpl,
                yearParam);

        if (yearList != null && !yearList.isEmpty()) {
            yearList = SortUtility.sortCommonDemandYearReportListByYearNoMonthNo(yearList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        }

        for (CommonDemandYearReportListResult report : yearList) {
            //サマリ値の設定
            if (report.getOutAirTempAvg() != null) {
                if (maxUse == null) {
                    maxUse = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(maxUse) >= 0) {
                    maxUse = report.getOutAirTempAvg();
                }
            }

            if (report.getOutAirTempMax() != null) {
                if (maxMax == null) {
                    maxMax = report.getOutAirTempMax();
                } else if (report.getOutAirTempMax().compareTo(maxMax) >= 0) {
                    maxMax = report.getOutAirTempMax();
                }
            }

            if (Integer.parseInt(report.getYearNo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            report.getMonthNo().intValue()))) >= Integer
                                    .parseInt(currentYear.getCalYear()
                                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                    currentYear.getMonthNo().intValue())))) {
                //データの年、月Noが現在積算中の年、月以降は最小値、平均値をとらない
            } else if (report.getOutAirTempAvg() != null) {
                if (min == null) {
                    min = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(min) <= 0) {
                    min = report.getOutAirTempAvg();
                }

                if (averageSummary == null) {
                    averageSummary = report.getOutAirTempAvg();
                } else {
                    averageSummary = averageSummary.add(report.getOutAirTempAvg());
                }

                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(report.getYearNo());
            detailData.setMonthNo(report.getMonthNo());
            if (report.getOutAirTempAvg() != null) {
                detailData.setResultNumUsed(report.getOutAirTempAvg().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(report.getOutAirTempAvg());
            }
            if (report.getOutAirTempMax() != null) {
                detailData.setResultNumMax(report.getOutAirTempMax().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(report.getOutAirTempMax());
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（系統・年報・導入前）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param decimal
     * @param control
     * @param elementType
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromLine(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, Integer decimal, String control, String elementType) {

        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド年報系統からデータを取得する
        CommonDemandYearReportLineListResult yearLineParam = DemandEmsUtility.getYearReportLineListParam(corpId,
                buildingId, ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(), lineGroupId, lineNo, "0", "0", null,
                null);
        List<CommonDemandYearReportLineListResult> yearLineList = getResultList(
                commonDemandYearReportLineListServiceDaoImpl, yearLineParam);

        for (CommonDemandYearReportLineListResult line : yearLineList) {
            //サマリ値の設定
            if (line.getLineValueKwh() != null) {
                if (summary == null) {
                    summary = line.getLineValueKwh();
                } else {
                    if (line.getLineValueKwh() != null) {
                        summary = summary.add(line.getLineValueKwh());
                    }
                }

                if (maxUse == null) {
                    maxUse = line.getLineValueKwh();
                } else if (line.getLineValueKwh() != null && line.getLineValueKwh().compareTo(maxUse) >= 0) {
                    maxUse = line.getLineValueKwh();
                }
            }

            if (line.getLineMaxKw() != null) {
                if (maxMax == null) {
                    maxMax = line.getLineMaxKw();
                } else if (line.getLineMaxKw() != null && line.getLineMaxKw().compareTo(maxMax) >= 0) {
                    maxMax = line.getLineMaxKw();
                }
            }

            if (line.getLineValueKwh() != null) {
                if (min == null) {
                    min = line.getLineValueKwh();
                } else if (line.getLineValueKwh() != null && line.getLineValueKwh().compareTo(min) <= 0) {
                    min = line.getLineValueKwh();
                }
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(line.getYearNo());
            detailData.setMonthNo(line.getMonthNo());
            if (line.getLineValueKwh() != null) {
                detailData.setResultNumUsed(line.getLineValueKwh().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(line.getLineValueKwh());
            }
            if (line.getLineMaxKw() != null) {
                detailData.setResultNumMax(line.getLineMaxKw().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(line.getLineMaxKw());
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (summary != null) {
            summaryData.setSummaryNumUsed(
                    summary.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            if (averageCount != null) {
                summaryData.setAverageNumUsed(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（ポイント・年報・導入前）
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param decimal
     * @param control
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromPoint(String corpId, Long buildingId, Long smId,
            String pointNo, Integer decimal, String control) {

        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal pointAverageValAfterFormat;
        BigDecimal pointMaxValAfterFormat;

        //機器ポイントデータを取得する
        SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);

        //デマンド年報ポイントからデータを取得する
        CommonDemandYearReportPointListResult yearPointParam = DemandEmsUtility.getYearReportPointListParam(corpId,
                buildingId, smId, ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(), "0", "0", null, null, pointNo,
                pointNo);
        List<CommonDemandYearReportPointListResult> yearPointList = getResultList(
                commonDemandYearReportPointListServiceDaoImpl, yearPointParam);

        for (CommonDemandYearReportPointListResult point : yearPointList) {

            if (point.getPointAvg() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointAverageValAfterFormat = point.getPointAvg().add(smPointList.get(0).getAnalogOffSetValue());
                    pointAverageValAfterFormat = pointAverageValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointAverageValAfterFormat = point.getPointAvg();
                }
            } else {
                pointAverageValAfterFormat = point.getPointAvg();
            }

            if (point.getPointMax() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointMaxValAfterFormat = point.getPointMax().add(smPointList.get(0).getAnalogOffSetValue());
                    pointMaxValAfterFormat = pointMaxValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointMaxValAfterFormat = point.getPointMax();
                }
            } else {
                pointMaxValAfterFormat = point.getPointMax();
            }

            //サマリ値の設定
            if (summary == null) {
                summary = pointAverageValAfterFormat;
            } else {
                if (pointAverageValAfterFormat != null) {
                    summary = summary.add(pointAverageValAfterFormat);
                }

            }

            if (maxUse == null) {
                maxUse = pointAverageValAfterFormat;
            } else if (pointAverageValAfterFormat != null && pointAverageValAfterFormat.compareTo(maxUse) >= 0) {
                maxUse = pointAverageValAfterFormat;
            }

            if (maxMax == null) {
                maxMax = pointMaxValAfterFormat;
            } else if (pointMaxValAfterFormat != null && pointMaxValAfterFormat.compareTo(maxMax) >= 0) {
                maxMax = pointMaxValAfterFormat;
            }

            if (min == null) {
                min = pointAverageValAfterFormat;
            } else if (pointAverageValAfterFormat != null && pointAverageValAfterFormat.compareTo(min) <= 0) {
                min = pointAverageValAfterFormat;
            }

            if (pointAverageValAfterFormat != null) {
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(point.getYearNo());
            detailData.setMonthNo(point.getMonthNo());
            if (pointAverageValAfterFormat != null) {
                detailData.setResultNumUsed(pointAverageValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(pointAverageValAfterFormat);
            }
            if (pointMaxValAfterFormat != null) {
                detailData.setResultNumMax(pointMaxValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(pointMaxValAfterFormat);
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (summary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);

    }

    /**
     * 系統別データ作成（アメダス・年報・導入前）
     *
     * @param corpId
     * @param buildingId
     * @param decimal
     * @param control
     * @return
     */
    private CommonLinePowerYearDataResult getLinePowerDataFromAmedas(String corpId, Long buildingId,
            Integer decimal, String control) {

        List<CommonLinePowerYearDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerYearDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド年報からデータを取得する
        CommonDemandYearReportListResult yearParam = DemandEmsUtility.getYearReportListParam(corpId, buildingId,
                ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(), "0", "0", null, null);
        List<CommonDemandYearReportListResult> yearList = getResultList(commonDemandYearReportListServiceDaoImpl,
                yearParam);

        for (CommonDemandYearReportListResult report : yearList) {
            //サマリ値の設定
            if (report.getOutAirTempAvg() != null) {
                if (summary == null) {
                    summary = report.getOutAirTempAvg();
                } else {
                    summary = summary.add(report.getOutAirTempAvg());
                }

                if (maxUse == null) {
                    maxUse = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(maxUse) >= 0) {
                    maxUse = report.getOutAirTempAvg();
                }
            }

            if (report.getOutAirTempMax() != null) {
                if (maxMax == null) {
                    maxMax = report.getOutAirTempMax();
                } else if (report.getOutAirTempMax().compareTo(maxMax) >= 0) {
                    maxMax = report.getOutAirTempMax();
                }
            }

            if (report.getOutAirTempAvg() != null) {
                if (min == null) {
                    min = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(min) <= 0) {
                    min = report.getOutAirTempAvg();
                }

                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerYearDataDetailResult detailData = new CommonLinePowerYearDataDetailResult();
            detailData.setYearNo(report.getYearNo());
            detailData.setMonthNo(report.getMonthNo());
            if (report.getOutAirTempAvg() != null) {
                detailData.setResultNumUsed(report.getOutAirTempAvg().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(report.getOutAirTempAvg());
            }
            if (report.getOutAirTempMax() != null) {
                detailData.setResultNumMax(report.getOutAirTempMax().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(report.getOutAirTempMax());
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerYearDataSummaryResult();
        if (summary != null) {
            summaryData.setSummaryNumUsed(
                    summary.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            if (averageCount != null) {
                summaryData.setAverageNumUsed(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerYearDataResult(detailList, summaryData);
    }

}
