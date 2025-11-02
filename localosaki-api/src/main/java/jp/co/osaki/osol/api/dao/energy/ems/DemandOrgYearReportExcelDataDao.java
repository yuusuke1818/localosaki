package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants.GROUP_CODE;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportExcelDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportExcelDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportExcelDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportExcelDataLineResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportExcelDataPointResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportExcelDataSmResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportExcelDataTimelyResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingLineTimeStandardListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * エネルギー使用状況（個別・年報） Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class DemandOrgYearReportExcelDataDao extends OsolApiDao<DemandOrgYearReportExcelDataParameter> {

    private static final String[] DIVISION_LINE = { "使用量", "最大値", "標準外使用量" };
    private static final String LINE_NAME_AMEDAS = "アメダス（外気温）";
    private static final String LINE_TYPE_AMEDAS = "-";
    private static final String UNIT_AMEDAS = ApiSimpleConstants.UNIT_TEMPERATURE;
    private static final String[] DIVISION_AMEDAS = { "平均値", "最大値", "最小値" };
    private static final String[] DIVISION_POINT = { "平均値", "最大値" };
    private static final String[] DIVISION_POINT_SRC = { "最大値", "最小値" };
    private static final String POINT_NO_SRC = "SRC";
    private static final String POINT_NM_SRC = "受電";
    private static final String[] TIMES = { "開店準備時間", "開店時間", "閉店準備時間", "閉店時間" };
    private static final String DIVISION_TIME = "使用量";

    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonDemandYearReportListServiceDaoImpl commonDemandYearReportListServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;
    private final CommonDemandYearReportPointListServiceDaoImpl commonDemandYearReportPointListServiceDaoImpl;
    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;

    @Inject
    GenericTypeUtility genericTypeUtility;

    public DemandOrgYearReportExcelDataDao() {
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonDemandYearReportListServiceDaoImpl = new CommonDemandYearReportListServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
        commonDemandYearReportPointListServiceDaoImpl = new CommonDemandYearReportPointListServiceDaoImpl();
        buildingLineTimeStandardListServiceDaoImpl = new BuildingLineTimeStandardListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgYearReportExcelDataResult query(DemandOrgYearReportExcelDataParameter parameter) throws Exception {

        DemandOrgYearReportExcelDataResult result = new DemandOrgYearReportExcelDataResult();
        DemandGraphElementListDetailResultData graphElementParam = null;
        List<DemandGraphElementListDetailResultData> graphElemetnList = null;
        List<DemandCalendarYearData> yearCalList = null;
        String sumDate = null;

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

        //集計期間がNULLの場合、1（年）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
        }

        //集計範囲を取得する
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(),
                parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //グラフ要素情報を取得する（グラフIDが設定されている場合のみ）
        if (parameter.getGraphId() != null) {
            graphElementParam = DemandEmsUtility.getGraphElementListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getGraphId(), null);

            graphElemetnList = getResultList(
                    demandGraphElementListServiceDaoImpl, graphElementParam);
            if (graphElemetnList == null || graphElemetnList.isEmpty()) {
                return new DemandOrgYearReportExcelDataResult();
            }
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandOrgYearReportExcelDataResult();
        }

        //年報カレンダを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgYearReportExcelDataResult();
            }
            //企業集計カレンダを取得する
            yearCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            sumDate = corpDemandList.get(0).getSumDate();
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物カレンダを取得する
            yearCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    buildingDemandList.get(0).getSumDate());
            sumDate = buildingDemandList.get(0).getSumDate();
        }

        if (yearCalList == null || yearCalList.isEmpty()) {
            return new DemandOrgYearReportExcelDataResult();
        }

        //集計開始年月の月開始日を取得する
        List<DemandCalendarYearData> filterCalList = yearCalList.stream()
                .filter(i -> i.getYearNo().equals(summaryRange.getYearFrom())
                        && i.getMonthNo().compareTo(summaryRange.getMonthFrom()) == 0)
                .collect(Collectors.toList());

        if (filterCalList == null || filterCalList.size() != 1) {
            return new DemandOrgYearReportExcelDataResult();
        }

        result.setYyyymm(parameter.getYear()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, parameter.getMonth().intValue())));
        result.setSumDate(sumDate);
        result.setDayFr(filterCalList.get(0).getMonthStartDate());
        result.setLineList(
                getLineList(parameter, summaryRange, graphElemetnList, sumDate, yearCalList, yearNoFrom, yearNoTo,
                        buildingDemandList.get(0)));
        result.setSmList(getSmList(parameter, summaryRange, graphElemetnList, yearCalList, sumDate));
        result.setTimelyList(getTimelyList(parameter, summaryRange, graphElemetnList, yearCalList, sumDate));
        return result;
    }

    /**
     * 系統情報を取得する
     * @param parameter
     * @param summaryRange
     * @param graphElementList
     * @param sumDate
     * @param yearCalList
     * @param yearNoFrom
     * @param yearNoTo
     * @param buildingDemand;
     * @return
     */
    private List<DemandOrgYearReportExcelDataLineResultData> getLineList(
            DemandOrgYearReportExcelDataParameter parameter, SummaryRangeForYearResult summaryRange,
            List<DemandGraphElementListDetailResultData> graphElementList, String sumDate,
            List<DemandCalendarYearData> yearCalList, String yearNoFrom, String yearNoTo,
            BuildingDemandListDetailResultData buildingDemand) {
        List<DemandOrgYearReportExcelDataLineResultData> resultList = new ArrayList<>();
        List<LineListDetailResultData> lineList = new ArrayList<>();

        if (parameter.getGraphId() == null) {
            //系統情報からデータを取得する
            LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                    parameter.getLineGroupId(), null, ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
            lineList = getResultList(lineListServiceDaoImpl, lineParam);
        } else {
            //グラフ要素情報から1：デマンドのデータを取得する
            for (DemandGraphElementListDetailResultData graphElement : graphElementList) {
                if (ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal()
                        .equals(graphElement.getGraphElementType())) {
                    //系統情報からデータを取得する
                    LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(graphElement.getCorpId(),
                            graphElement.getGraphLineGroupId(), graphElement.getGraphLineNo(),
                            ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
                    lineList.addAll(getResultList(lineListServiceDaoImpl, lineParam));
                } else {
                    continue;
                }
            }
        }

        if (lineList != null && !lineList.isEmpty()) {
            //系統情報をソートする
            lineList = SortUtility.sortLineListByLineNo(lineList, ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            for (LineListDetailResultData line : lineList) {
                DemandOrgYearReportExcelDataLineResultData result = new DemandOrgYearReportExcelDataLineResultData();
                List<DemandOrgYearReportExcelDataDetailResultData> detailList = new ArrayList<>();
                List<DemandCalendarYearData> currentLineCalList = new ArrayList<>();
                String currenLineSumDate = null;

                //系統情報の設定
                result.setLineName(line.getLineName());
                result.setLineType(genericTypeUtility.getKbnName(GROUP_CODE.LINE_TARGET, line.getLineTarget()));
                result.setAmedas(Boolean.FALSE);

                //集計日情報の設定
                if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                    //集計デマンド系統情報を取得する
                    AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility
                            .getAggregateDmLineListParam(
                                    parameter.getOperationCorpId(), parameter.getBuildingId(), line.getLineGroupId(),
                                    line.getLineNo(),
                                    null, null, null);
                    List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                            aggregateDmLineListServiceDaoImpl, aggregateLineParam);
                    if (aggregateLineList != null && aggregateLineList.size() == 1) {
                        //集計デマンド情報を取得する
                        AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                                parameter.getOperationCorpId(), parameter.getBuildingId(),
                                aggregateLineList.get(0).getAggregateDmId());
                        List<AggregateDmListDetailResultData> aggregateList = getResultList(
                                aggregateDmListServiceDaoImpl,
                                aggregateParam);
                        if (aggregateList != null && aggregateList.size() == 1) {
                            currenLineSumDate = aggregateList.get(0).getSumDate();
                            //カレンダを取得する
                            currentLineCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                                    currenLineSumDate);
                        }
                    }
                }

                if (CheckUtility.isNullOrEmpty(currenLineSumDate)) {
                    currenLineSumDate = sumDate;
                }
                if (currentLineCalList == null || currentLineCalList.isEmpty()) {
                    currentLineCalList.addAll(yearCalList);
                }

                result.setSumDate(currenLineSumDate);

                //実績値情報の設定
                //使用量
                DemandOrgYearReportExcelDataDetailResultData usedValue = new DemandOrgYearReportExcelDataDetailResultData();
                usedValue.setDivision(DIVISION_LINE[0]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    usedValue.setUnit(line.getLineUnit());
                } else {
                    usedValue.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                }
                usedValue.setValues(new ArrayList<>());
                //最大値
                DemandOrgYearReportExcelDataDetailResultData maxValue = new DemandOrgYearReportExcelDataDetailResultData();
                maxValue.setDivision(DIVISION_LINE[1]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    maxValue.setUnit(line.getLineUnit());
                } else {
                    maxValue.setUnit(ApiSimpleConstants.UNIT_DEMAND);
                }
                maxValue.setValues(new ArrayList<>());
                //標準外使用量
                DemandOrgYearReportExcelDataDetailResultData standardValue = new DemandOrgYearReportExcelDataDetailResultData();
                standardValue.setDivision(DIVISION_LINE[2]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    standardValue.setUnit(line.getLineUnit());
                } else {
                    standardValue.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                }
                standardValue.setValues(new ArrayList<>());

                //デマンド年報情報を取得する
                CommonDemandYearReportLineListResult yearLineReportParam = DemandEmsUtility.getYearReportLineListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                        line.getLineGroupId(), line.getLineNo(), summaryRange.getYearFrom(), summaryRange.getYearTo(),
                        summaryRange.getMonthFrom(), summaryRange.getMonthTo());
                List<CommonDemandYearReportLineListResult> yearLineReportList = getResultList(
                        commonDemandYearReportLineListServiceDaoImpl, yearLineReportParam);

                //建物系統時限標準値情報を取得する
                BuildingLineTimeStandardListTimeResultData lineStandardParam = DemandEmsUtility
                        .getBuildingLineTimeStandardListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                line.getLineGroupId(), line.getLineNo());
                List<BuildingLineTimeStandardListTimeResultData> lineStandardList = getResultList(
                        buildingLineTimeStandardListServiceDaoImpl, lineStandardParam);
                //デマンド日報系統情報を取得する
                //開始年月と終了年月のカレンダを取得する
                List<DemandCalendarYearData> startCalList = currentLineCalList.stream()
                        .filter(i -> i.getYearNo().equals(summaryRange.getYearFrom())
                                && i.getMonthNo().compareTo(summaryRange.getMonthFrom()) == 0)
                        .collect(Collectors.toList());
                List<DemandCalendarYearData> endCalList = currentLineCalList.stream()
                        .filter(i -> i.getYearNo().equals(summaryRange.getYearTo())
                                && i.getMonthNo().compareTo(summaryRange.getMonthTo()) == 0)
                        .collect(Collectors.toList());

                List<CommonDemandDayReportLineListResult> dayLineReportList = null;
                if (startCalList != null && startCalList.size() == 1 && endCalList != null && endCalList.size() == 1) {
                    CommonDemandDayReportLineListResult dayLineReportParam = DemandEmsUtility.getDayReportLineListParam(
                            parameter.getOperationCorpId(), parameter.getBuildingId(), line.getLineGroupId(),
                            line.getLineNo(),
                            startCalList.get(0).getMonthStartDate(), BigDecimal.ONE,
                            startCalList.get(0).getMonthEndDate(),
                            new BigDecimal("48"));
                    dayLineReportList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayLineReportParam);
                }

                //実績値をセットする
                String calYear = null;
                BigDecimal calMonth = null;
                do {

                    if (calYear == null && calMonth == null) {
                        calYear = summaryRange.getYearFrom();
                        calMonth = summaryRange.getMonthFrom();
                    } else {
                        //1ヶ月進める
                        if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                            //12月の場合
                            calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                            calMonth = BigDecimal.ONE;
                        } else {
                            calMonth = calMonth.add(BigDecimal.ONE);
                        }
                    }

                    if (yearLineReportList == null || yearLineReportList.isEmpty()) {
                        //集計範囲のデータがない場合はnullを設定
                        usedValue.getValues().add(null);
                        maxValue.getValues().add(null);
                        standardValue.getValues().add(null);
                    } else {
                        Boolean setFlg = Boolean.FALSE;
                        for (CommonDemandYearReportLineListResult yearLine : yearLineReportList) {
                            if (yearLine.getYearNo().equals(calYear)
                                    && yearLine.getMonthNo().compareTo(calMonth) == 0) {
                                usedValue.getValues().add(yearLine.getLineValueKwh());
                                maxValue.getValues().add(yearLine.getLineMaxKw());
                                //標準外使用量を算出する
                                standardValue.getValues()
                                        .add(getStandardOverValue(yearLine, lineStandardList, dayLineReportList,
                                                line.getLineTarget(), currentLineCalList));
                                setFlg = true;
                            }
                        }
                        if (!setFlg) {
                            //対象日にデータが取得できない場合はnullを設定
                            usedValue.getValues().add(null);
                            maxValue.getValues().add(null);
                            standardValue.getValues().add(null);
                        }
                    }

                } while (!calYear.equals(summaryRange.getYearTo())
                        || calMonth.compareTo(summaryRange.getMonthTo()) != 0);

                //各項目のサマリ値を取得する
                List<BigDecimal> usedValues = getSummaryData(usedValue.getValues(), Boolean.TRUE, currentLineCalList,
                        summaryRange, currenLineSumDate);
                List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, currentLineCalList,
                        summaryRange, currenLineSumDate);
                List<BigDecimal> standardValues = getSummaryData(standardValue.getValues(), Boolean.TRUE,
                        currentLineCalList, summaryRange, currenLineSumDate);

                usedValue.getValues().clear();
                usedValue.getValues().addAll(usedValues);
                maxValue.getValues().clear();
                maxValue.getValues().addAll(maxValues);
                standardValue.getValues().clear();
                standardValue.getValues().addAll(standardValues);

                detailList.add(usedValue);
                detailList.add(maxValue);
                detailList.add(standardValue);

                result.setDetailList(detailList);
                resultList.add(result);
            }
        }

        //アメダスの情報を取得する
        Boolean amedasFlg = Boolean.FALSE;
        if (parameter.getGraphId() == null) {
            if (ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_ON.getVal()
                    .equals(buildingDemand.getOutAirTempDispFlg())) {
                //外気温を表示する場合
                amedasFlg = Boolean.TRUE;
            }
        } else {
            //グラフ要素情報から3：アメダスのデータを取得する
            for (DemandGraphElementListDetailResultData graphElement : graphElementList) {
                if (ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.AMEDAS.getVal()
                        .equals(graphElement.getGraphElementType())) {
                    amedasFlg = Boolean.TRUE;
                } else {
                    continue;
                }
            }
        }

        if (amedasFlg) {

            DemandOrgYearReportExcelDataLineResultData result = new DemandOrgYearReportExcelDataLineResultData();
            List<DemandOrgYearReportExcelDataDetailResultData> detailList = new ArrayList<>();

            result.setLineName(LINE_NAME_AMEDAS);
            result.setLineType(LINE_TYPE_AMEDAS);
            result.setAmedas(Boolean.TRUE);
            result.setSumDate(sumDate);
            //平均値
            DemandOrgYearReportExcelDataDetailResultData averageValue = new DemandOrgYearReportExcelDataDetailResultData();
            averageValue.setDivision(DIVISION_AMEDAS[0]);
            averageValue.setUnit(UNIT_AMEDAS);
            averageValue.setValues(new ArrayList<>());
            //最大値
            DemandOrgYearReportExcelDataDetailResultData maxValue = new DemandOrgYearReportExcelDataDetailResultData();
            maxValue.setDivision(DIVISION_AMEDAS[1]);
            maxValue.setUnit(UNIT_AMEDAS);
            maxValue.setValues(new ArrayList<>());
            //最小値
            DemandOrgYearReportExcelDataDetailResultData minValue = new DemandOrgYearReportExcelDataDetailResultData();
            minValue.setDivision(DIVISION_AMEDAS[2]);
            minValue.setUnit(UNIT_AMEDAS);
            minValue.setValues(new ArrayList<>());

            //デマンド年報からデータを取得する
            CommonDemandYearReportListResult yearReportParam = DemandEmsUtility.getYearReportListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                    summaryRange.getYearFrom(), summaryRange.getYearTo(), summaryRange.getMonthFrom(),
                    summaryRange.getMonthTo());
            List<CommonDemandYearReportListResult> yearReportList = getResultList(
                    commonDemandYearReportListServiceDaoImpl, yearReportParam);

            //実績値をセットする
            String calYear = null;
            BigDecimal calMonth = null;
            do {

                if (calYear == null && calMonth == null) {
                    calYear = summaryRange.getYearFrom();
                    calMonth = summaryRange.getMonthFrom();
                } else {
                    //1ヶ月進める
                    if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                        //12月の場合
                        calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                        calMonth = BigDecimal.ONE;
                    } else {
                        calMonth = calMonth.add(BigDecimal.ONE);
                    }
                }

                if (yearReportList == null || yearReportList.isEmpty()) {
                    //集計範囲のデータがない場合はnullを設定
                    averageValue.getValues().add(null);
                    maxValue.getValues().add(null);
                    minValue.getValues().add(null);
                } else {
                    Boolean setFlg = Boolean.FALSE;
                    for (CommonDemandYearReportListResult yearData : yearReportList) {
                        if (yearData.getYearNo().equals(calYear)
                                && yearData.getMonthNo().compareTo(calMonth) == 0) {
                            averageValue.getValues().add(yearData.getOutAirTempAvg());
                            maxValue.getValues().add(yearData.getOutAirTempMax());
                            minValue.getValues().add(yearData.getOutAirTempMin());
                            setFlg = true;
                        }
                    }
                    if (!setFlg) {
                        //対象日にデータが取得できない場合はnullを設定
                        averageValue.getValues().add(null);
                        maxValue.getValues().add(null);
                        minValue.getValues().add(null);
                    }
                }

            } while (!calYear.equals(summaryRange.getYearTo())
                    || calMonth.compareTo(summaryRange.getMonthTo()) != 0);

            //各項目のサマリ値を取得する
            List<BigDecimal> averageValues = getSummaryData(averageValue.getValues(), Boolean.FALSE, yearCalList,
                    summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, yearCalList, summaryRange,
                    sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            List<BigDecimal> minValues = getSummaryData(minValue.getValues(), Boolean.FALSE, yearCalList, summaryRange,
                    sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

            averageValue.getValues().clear();
            averageValue.getValues().addAll(averageValues);
            maxValue.getValues().clear();
            maxValue.getValues().addAll(maxValues);
            minValue.getValues().clear();
            minValue.getValues().addAll(minValues);

            detailList.add(averageValue);
            detailList.add(maxValue);
            detailList.add(minValue);
            result.setDetailList(detailList);
            resultList.add(result);
        }

        return resultList;

    }

    /**
     * 機器情報を取得する
     * @param parameter
     * @param summaryRange
     * @param graphElementList
     * @param sumDate
     * @return
     */
    private List<DemandOrgYearReportExcelDataSmResultData> getSmList(DemandOrgYearReportExcelDataParameter parameter,
            SummaryRangeForYearResult summaryRange,
            List<DemandGraphElementListDetailResultData> graphElementList, List<DemandCalendarYearData> yearCalList,
            String sumDate) {
        List<DemandOrgYearReportExcelDataSmResultData> resultList = new ArrayList<>();
        List<DemandBuildingSmListDetailResultData> buildingSmList = new ArrayList<>();
        List<DemandGraphElementListDetailResultData> pointGraphList = new ArrayList<>();

        if (parameter.getGraphId() == null) {
            //建物機器情報からデータを取得する
            DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                    .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);
            buildingSmList = getResultList(demandBuildingSmListServiceDaoImpl, buildingSmParam);
            if (buildingSmList == null || buildingSmList.isEmpty()) {
                return new ArrayList<>();
            }
        } else {
            //グラフ要素情報から2：アナログのデータを取得する
            List<Long> smIdList = new ArrayList<>();
            for (DemandGraphElementListDetailResultData graphElement : graphElementList) {
                if (ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.ANALOG.getVal()
                        .equals(graphElement.getGraphElementType())) {
                    pointGraphList.add(graphElement);
                    if (!smIdList.contains(graphElement.getGraphSmId())) {
                        smIdList.add(graphElement.getGraphSmId());
                        //新しい機器の場合、建物機器情報からデータを取得する
                        DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                        graphElement.getGraphSmId());
                        buildingSmList.addAll(getResultList(demandBuildingSmListServiceDaoImpl, buildingSmParam));
                    }
                } else {
                    continue;
                }
            }
            if (buildingSmList == null || buildingSmList.isEmpty()) {
                return new ArrayList<>();
            }
        }

        //機器ID順にソートする
        buildingSmList = buildingSmList.stream()
                .sorted(Comparator.comparing(DemandBuildingSmListDetailResultData::getSmId, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
            DemandOrgYearReportExcelDataSmResultData result = new DemandOrgYearReportExcelDataSmResultData();
            List<SmPointListDetailResultData> smPointList = new ArrayList<>();

            result.setSmId(buildingSm.getSmId());
            result.setSmAddress(buildingSm.getSmAddress());
            result.setProductCd(buildingSm.getProductCd());
            result.setProductName(buildingSm.getProductName());
            result.setPointList(new ArrayList<>());

            //ポイント情報を取得する
            if (parameter.getGraphId() == null) {
                //機器ポイント情報を取得する
                SmPointListDetailResultData smPointParam = DemandEmsUtility
                        .getSmPointListParam(buildingSm.getSmId(), null);
                smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);
            } else {
                for (DemandGraphElementListDetailResultData graphElement : pointGraphList) {
                    if (buildingSm.getSmId().equals(graphElement.getGraphSmId())) {
                        //機器ポイント情報を取得する
                        SmPointListDetailResultData smPointParam = DemandEmsUtility
                                .getSmPointListParam(graphElement.getGraphSmId(), graphElement.getGraphPointNo());
                        smPointList.addAll(getResultList(smPointListServiceDaoImpl, smPointParam));
                    }
                }
            }

            if (smPointList != null && !smPointList.isEmpty()) {
                SmPointListDetailResultData srcPointData = null;
                //ポイント番号単位でソートする
                smPointList = smPointList.stream()
                        .sorted(Comparator.comparing(SmPointListDetailResultData::getPointNo,
                                Comparator.naturalOrder()))
                        .collect(Collectors.toList());
                for (SmPointListDetailResultData smPoint : smPointList) {
                    DemandOrgYearReportExcelDataPointResultData point = new DemandOrgYearReportExcelDataPointResultData();
                    if (POINT_NO_SRC.equals(smPoint.getPointNo())) {
                        //受電の場合データを退避する
                        srcPointData = smPoint;
                    } else {
                        //上記以外の場合
                        //建物機器ポイント情報を取得する
                        DemandBuildingSmPointListDetailResultData buildngSmPointParam = DemandEmsUtility
                                .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                        smPoint.getSmId(), smPoint.getPointNo());
                        List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                                demandBuildingSmPointListServiceDaoImpl, buildngSmPointParam);

                        if(buildingSmPointList != null && !buildingSmPointList.isEmpty()) {
                            point.setPointNo(smPoint.getPointNo());
                            point.setPointName(buildingSmPointList.get(0).getPointName());
                            point.setPointType(buildingSmPointList.get(0).getPointType());
                            point.setPointKind(genericTypeUtility.getKbnName(GROUP_CODE.POINT_TYPE,
                                    buildingSmPointList.get(0).getPointType()));
                            if (ApiGenericTypeConstants.POINT_TYPE.PULSE.getVal()
                                    .equals(buildingSmPointList.get(0).getPointType())
                                    && CheckUtility.isNullOrEmpty(buildingSmPointList.get(0).getPointUnit())) {
                                //パルスの場合で単位未設定の場合
                                point.setDemand(Boolean.TRUE);
                            } else {
                                point.setDemand(Boolean.FALSE);
                            }
                            point.setDetailList(new ArrayList<>());

                            //機器の実績値を取得する
                            //平均値
                            DemandOrgYearReportExcelDataDetailResultData averageValue = new DemandOrgYearReportExcelDataDetailResultData();
                            averageValue.setDivision(DIVISION_POINT[0]);
                            averageValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                            averageValue.setValues(new ArrayList<>());
                            //最大値
                            DemandOrgYearReportExcelDataDetailResultData maxValue = new DemandOrgYearReportExcelDataDetailResultData();
                            maxValue.setDivision(DIVISION_POINT[1]);
                            maxValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                            maxValue.setValues(new ArrayList<>());

                            //デマンド年報ポイント情報を取得する
                            CommonDemandYearReportPointListResult yearPointReportParam = DemandEmsUtility
                                    .getYearReportPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                            buildingSmPointList.get(0).getSmId(), parameter.getSummaryKind(),
                                            summaryRange.getYearFrom(), summaryRange.getYearTo(),
                                            summaryRange.getMonthFrom(), summaryRange.getMonthTo(),
                                            buildingSmPointList.get(0).getPointNo(),
                                            buildingSmPointList.get(0).getPointNo());
                            List<CommonDemandYearReportPointListResult> yearPointReportList = getResultList(
                                    commonDemandYearReportPointListServiceDaoImpl, yearPointReportParam);
                            //実績値をセットする
                            String calYear = null;
                            BigDecimal calMonth = null;
                            do {

                                if (calYear == null && calMonth == null) {
                                    calYear = summaryRange.getYearFrom();
                                    calMonth = summaryRange.getMonthFrom();
                                } else {
                                    //1ヶ月進める
                                    if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                                        //12月の場合
                                        calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                                        calMonth = BigDecimal.ONE;
                                    } else {
                                        calMonth = calMonth.add(BigDecimal.ONE);
                                    }
                                }

                                if (yearPointReportList == null || yearPointReportList.isEmpty()) {
                                    //集計範囲のデータがない場合はnullを設定
                                    averageValue.getValues().add(null);
                                    maxValue.getValues().add(null);
                                } else {
                                    Boolean setFlg = Boolean.FALSE;
                                    for (CommonDemandYearReportPointListResult yearData : yearPointReportList) {
                                        if (yearData.getYearNo().equals(calYear)
                                                && yearData.getMonthNo().compareTo(calMonth) == 0) {
                                            averageValue.getValues().add(yearData.getPointAvg());
                                            maxValue.getValues().add(yearData.getPointMax());
                                            setFlg = true;
                                        }
                                    }
                                    if (!setFlg) {
                                        //対象日にデータが取得できない場合はnullを設定
                                        averageValue.getValues().add(null);
                                        maxValue.getValues().add(null);
                                    }
                                }

                            } while (!calYear.equals(summaryRange.getYearTo())
                                    || calMonth.compareTo(summaryRange.getMonthTo()) != 0);

                            //各項目のサマリ値を取得する
                            List<BigDecimal> averageValues = getSummaryData(averageValue.getValues(), Boolean.FALSE,
                                    yearCalList, summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

                            List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, yearCalList,
                                    summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

                            averageValue.getValues().clear();
                            averageValue.getValues().addAll(averageValues);
                            maxValue.getValues().clear();
                            maxValue.getValues().addAll(maxValues);

                            point.getDetailList().add(averageValue);
                            point.getDetailList().add(maxValue);
                            result.getPointList().add(point);
                        }


                    }
                }

                if (srcPointData != null) {
                    //受電のデータを取得する
                    DemandOrgYearReportExcelDataPointResultData point = new DemandOrgYearReportExcelDataPointResultData();

                    //建物機器ポイント情報を取得する
                    DemandBuildingSmPointListDetailResultData buildngSmPointParam = DemandEmsUtility
                            .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                    srcPointData.getSmId(), srcPointData.getPointNo());
                    List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                            demandBuildingSmPointListServiceDaoImpl, buildngSmPointParam);

                    if(buildingSmPointList != null && !buildingSmPointList.isEmpty()) {
                        point.setPointNo(POINT_NO_SRC);
                        point.setPointName(POINT_NM_SRC);
                        point.setPointType(buildingSmPointList.get(0).getPointType());
                        point.setPointKind(genericTypeUtility.getKbnName(GROUP_CODE.POINT_TYPE,
                                buildingSmPointList.get(0).getPointType()));
                        if (ApiGenericTypeConstants.POINT_TYPE.PULSE.getVal()
                                .equals(buildingSmPointList.get(0).getPointType())
                                && CheckUtility.isNullOrEmpty(buildingSmPointList.get(0).getPointUnit())) {
                            //パルスの場合で単位未設定の場合
                            point.setDemand(Boolean.TRUE);
                        } else {
                            point.setDemand(Boolean.FALSE);
                        }
                        point.setDetailList(new ArrayList<>());

                        //機器の実績値を取得する
                        //最大値
                        DemandOrgYearReportExcelDataDetailResultData maxValue = new DemandOrgYearReportExcelDataDetailResultData();
                        maxValue.setDivision(DIVISION_POINT_SRC[0]);
                        maxValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                        maxValue.setValues(new ArrayList<>());
                        //最小値
                        DemandOrgYearReportExcelDataDetailResultData minValue = new DemandOrgYearReportExcelDataDetailResultData();
                        minValue.setDivision(DIVISION_POINT_SRC[1]);
                        minValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                        minValue.setValues(new ArrayList<>());

                        //デマンド年報ポイント情報を取得する
                        CommonDemandYearReportPointListResult yearPointReportParam = DemandEmsUtility
                                .getYearReportPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                        buildingSmPointList.get(0).getSmId(), parameter.getSummaryKind(),
                                        summaryRange.getYearFrom(), summaryRange.getYearTo(),
                                        summaryRange.getMonthFrom(), summaryRange.getMonthTo(),
                                        buildingSmPointList.get(0).getPointNo(),
                                        buildingSmPointList.get(0).getPointNo());
                        List<CommonDemandYearReportPointListResult> yearPointReportList = getResultList(
                                commonDemandYearReportPointListServiceDaoImpl, yearPointReportParam);

                        //実績値をセットする
                        String calYear = null;
                        BigDecimal calMonth = null;
                        do {

                            if (calYear == null && calMonth == null) {
                                calYear = summaryRange.getYearFrom();
                                calMonth = summaryRange.getMonthFrom();
                            } else {
                                //1ヶ月進める
                                if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                                    //12月の場合
                                    calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                                    calMonth = BigDecimal.ONE;
                                } else {
                                    calMonth = calMonth.add(BigDecimal.ONE);
                                }
                            }

                            if (yearPointReportList == null || yearPointReportList.isEmpty()) {
                                //集計範囲のデータがない場合はnullを設定
                                maxValue.getValues().add(null);
                                minValue.getValues().add(null);
                            } else {
                                Boolean setFlg = Boolean.FALSE;
                                for (CommonDemandYearReportPointListResult yearData : yearPointReportList) {
                                    if (yearData.getYearNo().equals(calYear)
                                            && yearData.getMonthNo().compareTo(calMonth) == 0) {
                                        maxValue.getValues().add(yearData.getPointMax());
                                        minValue.getValues().add(yearData.getPointMin());
                                        setFlg = true;
                                    }
                                }
                                if (!setFlg) {
                                    //対象日にデータが取得できない場合はnullを設定
                                    maxValue.getValues().add(null);
                                    minValue.getValues().add(null);
                                }
                            }

                        } while (!calYear.equals(summaryRange.getYearTo())
                                || calMonth.compareTo(summaryRange.getMonthTo()) != 0);

                        //各項目のサマリ値を取得する
                        List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, yearCalList,
                                summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
                        List<BigDecimal> minValues = getSummaryData(minValue.getValues(), Boolean.FALSE, yearCalList,
                                summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

                        maxValue.getValues().clear();
                        maxValue.getValues().addAll(maxValues);
                        minValue.getValues().clear();
                        minValue.getValues().addAll(minValues);

                        point.getDetailList().add(maxValue);
                        point.getDetailList().add(minValue);
                        //先頭に差し込む
                        result.getPointList().add(0, point);
                    }

                }

            }

            resultList.add(result);
        }

        return resultList;
    }

    /**
     * 時間帯別情報を取得する
     * @param parameter
     * @param summaryRange
     * @param graphElementList
     * @return
     */
    private List<DemandOrgYearReportExcelDataTimelyResultData> getTimelyList(
            DemandOrgYearReportExcelDataParameter parameter, SummaryRangeForYearResult summaryRange,
            List<DemandGraphElementListDetailResultData> graphElementList, List<DemandCalendarYearData> yearCalList,
            String sumDate) {
        List<DemandOrgYearReportExcelDataTimelyResultData> resultList = new ArrayList<>();

        //デマンド年報情報からデータを取得する
        CommonDemandYearReportListResult yearReportParam = DemandEmsUtility.getYearReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                summaryRange.getYearFrom(), summaryRange.getYearTo(), summaryRange.getMonthFrom(),
                summaryRange.getMonthTo());
        List<CommonDemandYearReportListResult> yearReportList = getResultList(commonDemandYearReportListServiceDaoImpl,
                yearReportParam);

        //開店準備時間
        DemandOrgYearReportExcelDataTimelyResultData openPrepTime = new DemandOrgYearReportExcelDataTimelyResultData();
        DemandOrgYearReportExcelDataDetailResultData openPrepDetail = new DemandOrgYearReportExcelDataDetailResultData();
        openPrepTime.setTime(TIMES[0]);
        openPrepDetail.setDivision(DIVISION_TIME);
        openPrepDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        openPrepDetail.setValues(new ArrayList<>());
        //開店時間
        DemandOrgYearReportExcelDataTimelyResultData openTime = new DemandOrgYearReportExcelDataTimelyResultData();
        DemandOrgYearReportExcelDataDetailResultData openDetail = new DemandOrgYearReportExcelDataDetailResultData();
        openTime.setTime(TIMES[1]);
        openDetail.setDivision(DIVISION_TIME);
        openDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        openDetail.setValues(new ArrayList<>());
        //閉店準備時間
        DemandOrgYearReportExcelDataTimelyResultData closePrepTime = new DemandOrgYearReportExcelDataTimelyResultData();
        DemandOrgYearReportExcelDataDetailResultData closePrepDetail = new DemandOrgYearReportExcelDataDetailResultData();
        closePrepTime.setTime(TIMES[2]);
        closePrepDetail.setDivision(DIVISION_TIME);
        closePrepDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        closePrepDetail.setValues(new ArrayList<>());
        //閉店時間
        DemandOrgYearReportExcelDataTimelyResultData closeTime = new DemandOrgYearReportExcelDataTimelyResultData();
        DemandOrgYearReportExcelDataDetailResultData closeDetail = new DemandOrgYearReportExcelDataDetailResultData();
        closeTime.setTime(TIMES[3]);
        closeDetail.setDivision(DIVISION_TIME);
        closeDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        closeDetail.setValues(new ArrayList<>());

        //実績値をセットする
        String calYear = null;
        BigDecimal calMonth = null;
        do {

            if (calYear == null && calMonth == null) {
                calYear = summaryRange.getYearFrom();
                calMonth = summaryRange.getMonthFrom();
            } else {
                //1ヶ月進める
                if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                    //12月の場合
                    calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                    calMonth = BigDecimal.ONE;
                } else {
                    calMonth = calMonth.add(BigDecimal.ONE);
                }
            }

            if (yearReportList == null || yearReportList.isEmpty()) {
                //集計範囲のデータがない場合はnullを設定
                openPrepDetail.getValues().add(null);
                openDetail.getValues().add(null);
                closePrepDetail.getValues().add(null);
                closeDetail.getValues().add(null);
            } else {
                Boolean setFlg = Boolean.FALSE;
                for (CommonDemandYearReportListResult yearData : yearReportList) {
                    if (yearData.getYearNo().equals(calYear)
                            && yearData.getMonthNo().compareTo(calMonth) == 0) {
                        openPrepDetail.getValues().add(yearData.getOpenPreparationKwh());
                        openDetail.getValues().add(yearData.getOpenTimeKwh());
                        closePrepDetail.getValues().add(yearData.getClosePreparationKwh());
                        closeDetail.getValues().add(yearData.getCloseTimeKwh());
                        setFlg = true;
                    }
                }
                if (!setFlg) {
                    //対象日にデータが取得できない場合はnullを設定
                    openPrepDetail.getValues().add(null);
                    openDetail.getValues().add(null);
                    closePrepDetail.getValues().add(null);
                    closeDetail.getValues().add(null);
                }
            }

        } while (!calYear.equals(summaryRange.getYearTo())
                || calMonth.compareTo(summaryRange.getMonthTo()) != 0);

        //各項目のサマリ値を取得する
        List<BigDecimal> openPrepValues = getSummaryData(openPrepDetail.getValues(), Boolean.TRUE, yearCalList,
                summaryRange, sumDate);
        List<BigDecimal> openValues = getSummaryData(openDetail.getValues(), Boolean.TRUE, yearCalList, summaryRange,
                sumDate);
        List<BigDecimal> closePrepValues = getSummaryData(closePrepDetail.getValues(), Boolean.TRUE, yearCalList,
                summaryRange, sumDate);
        List<BigDecimal> closeValues = getSummaryData(closeDetail.getValues(), Boolean.TRUE, yearCalList, summaryRange,
                sumDate);

        openPrepDetail.getValues().clear();
        openPrepDetail.getValues().addAll(openPrepValues);
        openDetail.getValues().clear();
        openDetail.getValues().addAll(openValues);
        closePrepDetail.getValues().clear();
        closePrepDetail.getValues().addAll(closePrepValues);
        closeDetail.getValues().clear();
        closeDetail.getValues().addAll(closeValues);

        openPrepTime.setDetail(openPrepDetail);
        openTime.setDetail(openDetail);
        closePrepTime.setDetail(closePrepDetail);
        closeTime.setDetail(closeDetail);

        resultList.add(openPrepTime);
        resultList.add(openTime);
        resultList.add(closePrepTime);
        resultList.add(closeTime);

        return resultList;
    }

    /**
     * 標準外使用量を算出する
     * @param monthReportList
     * @param starndardList
     * @param dayReportList
     * @param lineTarget
     * @param yearCalList
     * @return
     */
    private BigDecimal getStandardOverValue(CommonDemandYearReportLineListResult yearReportList,
            List<BuildingLineTimeStandardListTimeResultData> starndardList,
            List<CommonDemandDayReportLineListResult> dayReportList, String lineTarget,
            List<DemandCalendarYearData> yearCalList) {

        BigDecimal standardOverValue = BigDecimal.ZERO;

        if (yearReportList == null || starndardList == null || starndardList.isEmpty() || dayReportList == null
                || dayReportList.isEmpty() || yearCalList == null || yearCalList.isEmpty()) {
            return null;
        }

        //対象年月の期間を取得する
        List<DemandCalendarYearData> filterCalList = yearCalList.stream()
                .filter(i -> i.getYearNo().equals(yearReportList.getYearNo())
                        && i.getMonthNo().compareTo(yearReportList.getMonthNo()) == 0)
                .collect(Collectors.toList());
        if (filterCalList == null || filterCalList.size() != 1) {
            return null;
        }

        Date dayFrom = filterCalList.get(0).getMonthStartDate();
        Date dayTo = filterCalList.get(0).getMonthEndDate();

        //デマンド日報系統データから対象年月の期間のデータを取得する
        List<CommonDemandDayReportLineListResult> targetDateReportList = dayReportList.stream()
                .filter(i -> i.getMeasurementDate().compareTo(dayFrom) >= 0
                        && i.getMeasurementDate().compareTo(dayTo) <= 0)
                .collect(Collectors.toList());

        if (targetDateReportList == null || targetDateReportList.isEmpty()) {
            return null;
        }

        for (CommonDemandDayReportLineListResult dayLine : targetDateReportList) {
            for (BuildingLineTimeStandardListTimeResultData standard : starndardList) {
                if (dayLine.getJigenNo().compareTo(standard.getJigenNo()) == 0) {
                    //時限Noが同じ場合
                    if (dayLine.getLineValueKw().compareTo(standard.getLineLimitStandardKw()) > 0) {
                        //日報の系統電力が時限標準値の上限値を超えている場合
                        standardOverValue = standardOverValue
                                .add(dayLine.getLineValueKw().subtract(standard.getLineLimitStandardKw()));
                    }
                } else {
                    continue;
                }
            }
        }

        if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineTarget)) {
            //ロギングの場合はそのまま返す
            return standardOverValue;
        } else {
            //ロギング以外は1/2をして返す
            if (BigDecimal.ZERO.compareTo(standardOverValue) == 0) {
                return standardOverValue;
            } else {
                return standardOverValue.divide(new BigDecimal("2"));
            }
        }
    }

    /**
     * 各項目のサマリ値を算出する（ポイント、アメダス以外に使用）
     * @param values
     * @param sumFlg
     * @return
     */
    private List<BigDecimal> getSummaryData(List<BigDecimal> values, Boolean sumFlg,
            List<DemandCalendarYearData> yearCalList, SummaryRangeForYearResult summaryRange, String sumDate) {

        return getSummaryData(values, sumFlg, yearCalList, summaryRange, sumDate, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
    }

    /**
     * 各項目のサマリ値を算出する
     * @param values
     * @param sumFlg
     * @return
     */
    private List<BigDecimal> getSummaryData(List<BigDecimal> values, Boolean sumFlg,
            List<DemandCalendarYearData> yearCalList, SummaryRangeForYearResult summaryRange, String sumDate, String control) {

        BigDecimal maxValue = null;
        BigDecimal minValue = null;
        BigDecimal sumValue = null;
        BigDecimal sumAverageValue = null;
        BigDecimal countValue = null;
        BigDecimal averageValue = null;

        //現在集計中の年月を取得する
        CurrentYearReportResult currentYearReport = DemandEmsUtility.getCurrentYearReport(yearCalList,
                getServerDateTime(), sumDate);

        //現在の集計範囲が含まれるかどうか判断する
        Integer index = null;
        int i = 0;

        if (currentYearReport != null) {
            String calYear = null;
            BigDecimal calMonth = null;
            do {

                if (calYear == null && calMonth == null) {
                    calYear = summaryRange.getYearFrom();
                    calMonth = summaryRange.getMonthFrom();
                } else {
                    //1ヶ月進める
                    if (calMonth.compareTo(new BigDecimal("12")) == 0) {
                        //12月の場合
                        calYear = new BigDecimal(calYear).add(BigDecimal.ONE).toString();
                        calMonth = BigDecimal.ONE;
                    } else {
                        calMonth = calMonth.add(BigDecimal.ONE);
                    }
                }

                if (calYear.equals(currentYearReport.getCalYear())
                        && calMonth.compareTo(currentYearReport.getMonthNo()) == 0) {
                    index = i;
                    break;
                }

                i++;

            } while (!calYear.equals(summaryRange.getYearTo())
                    || calMonth.compareTo(summaryRange.getMonthTo()) != 0);
        }

        i = 0;
        for (BigDecimal value : values) {
            if (value != null) {
                if (maxValue == null) {
                    maxValue = value;
                } else if (value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
                if (sumValue == null) {
                    sumValue = value;
                } else {
                    sumValue = sumValue.add(value);
                }

                //積算中の年月以降のデータは最小値、平均値から除外する
                if (index != null && i <= index) {
                    if (minValue == null) {
                        minValue = value;
                    } else if (value.compareTo(minValue) < 0) {
                        minValue = value;
                    }
                    if (countValue == null) {
                        countValue = BigDecimal.ONE;
                    } else {
                        countValue = countValue.add(BigDecimal.ONE);
                    }
                    if (sumAverageValue == null) {
                        sumAverageValue = value;
                    } else {
                        sumAverageValue = sumAverageValue.add(value);
                    }
                }

            }
            i++;
        }

        //平均値を算出する
        if (countValue == null) {
            averageValue = null;
        } else {
            averageValue = sumAverageValue.divide(countValue, 11, BigDecimal.ROUND_HALF_UP).setScale(1,
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control));
        }

        //合計値、最大値、最小値、平均値の順で返却
        List<BigDecimal> result = new ArrayList<>();
        result.addAll(values);
        if (sumFlg) {
            result.add(sumValue);
            result.add(maxValue);
            result.add(minValue);
            result.add(averageValue);
        } else {
            result.add(null);
            result.add(maxValue);
            result.add(minValue);
            result.add(averageValue);
        }

        return result;
    }

}
