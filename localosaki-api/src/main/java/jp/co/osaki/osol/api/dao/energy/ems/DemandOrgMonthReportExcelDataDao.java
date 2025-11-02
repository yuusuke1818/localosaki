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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMonthReportExcelDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMonthReportExcelDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportPointListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportExcelDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportExcelDataLineResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportExcelDataPointResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportExcelDataSmResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportExcelDataTimelyResultData;
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
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportPointListServiceDaoImpl;
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

/**
 * エネルギー使用状況（個別・月報） Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class DemandOrgMonthReportExcelDataDao extends OsolApiDao<DemandOrgMonthReportExcelDataParameter> {

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
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandMonthReportPointListServiceDaoImpl commonDemandMonthReportPointListServiceDaoImpl;
    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;

    @Inject
    GenericTypeUtility genericTypeUtility;

    public DemandOrgMonthReportExcelDataDao() {
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandMonthReportPointListServiceDaoImpl = new CommonDemandMonthReportPointListServiceDaoImpl();
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
    public DemandOrgMonthReportExcelDataResult query(DemandOrgMonthReportExcelDataParameter parameter)
            throws Exception {

        DemandOrgMonthReportExcelDataResult result = new DemandOrgMonthReportExcelDataResult();
        DemandGraphElementListDetailResultData graphElementParam = null;
        List<DemandGraphElementListDetailResultData> graphElemetnList = null;

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、1（ヶ月）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(BigDecimal.ONE);
        }

        //企業デマンド情報を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandOrgMonthReportExcelDataResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(buildingDemandListServiceDaoImpl,
                buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandOrgMonthReportExcelDataResult();
        }

        //集計範囲を取得する
        SummaryRangeForMonthResult summaryRange = SummaryRangeUtility.getSummaryRangeForMonth(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //グラフ要素情報を取得する（グラフIDが設定されている場合のみ）
        if (parameter.getGraphId() != null) {
            graphElementParam = DemandEmsUtility.getGraphElementListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getGraphId(), null);

            graphElemetnList = getResultList(
                    demandGraphElementListServiceDaoImpl, graphElementParam);
            if (graphElemetnList == null || graphElemetnList.isEmpty()) {
                return new DemandOrgMonthReportExcelDataResult();
            }
        }

        result.setDayFr(summaryRange.getMeasurementDateFrom());
        result.setDayTo(summaryRange.getMeasurementDateTo());
        result.setCorpSumDate(corpDemandList.get(0).getSumDate());
        result.setBuildingSumDate(buildingDemandList.get(0).getSumDate());
        result.setLineList(getLineList(parameter, summaryRange, graphElemetnList, corpDemandList.get(0),
                buildingDemandList.get(0)));
        result.setSmList(getSmList(parameter, summaryRange, graphElemetnList));
        result.setTimelyList(getTimelyList(parameter, summaryRange, graphElemetnList));
        return result;
    }

    /**
     * 系統情報を取得する
     * @param parameter
     * @param summaryRange
     * @param graphElementList
     * @param corpDemand
     * @param buildingDemand
     * @return
     */
    private List<DemandOrgMonthReportExcelDataLineResultData> getLineList(
            DemandOrgMonthReportExcelDataParameter parameter, SummaryRangeForMonthResult summaryRange,
            List<DemandGraphElementListDetailResultData> graphElementList, CorpDemandSelectResult corpDemand,
            BuildingDemandListDetailResultData buildingDemand) {

        List<DemandOrgMonthReportExcelDataLineResultData> resultList = new ArrayList<>();
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
                DemandOrgMonthReportExcelDataLineResultData result = new DemandOrgMonthReportExcelDataLineResultData();
                List<DemandOrgMonthReportExcelDataDetailResultData> detailList = new ArrayList<>();

                //系統情報の設定
                result.setLineName(line.getLineName());
                result.setLineType(genericTypeUtility.getKbnName(GROUP_CODE.LINE_TARGET, line.getLineTarget()));
                result.setAmedas(Boolean.FALSE);

                //集計日情報の設定
                String buildingSumDate = null;
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
                    List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                            aggregateParam);
                    if (aggregateList != null && aggregateList.size() == 1) {
                        buildingSumDate = aggregateList.get(0).getSumDate();
                    }
                }

                result.setCorpSumDate(corpDemand.getSumDate());
                if (CheckUtility.isNullOrEmpty(buildingSumDate)) {
                    result.setBuildingSumDate(buildingDemand.getSumDate());
                } else {
                    result.setBuildingSumDate(buildingSumDate);
                }

                //実績値情報の設定
                //使用量
                DemandOrgMonthReportExcelDataDetailResultData usedValue = new DemandOrgMonthReportExcelDataDetailResultData();
                usedValue.setDivision(DIVISION_LINE[0]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    usedValue.setUnit(line.getLineUnit());
                } else {
                    usedValue.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                }
                usedValue.setValues(new ArrayList<>());
                //最大値
                DemandOrgMonthReportExcelDataDetailResultData maxValue = new DemandOrgMonthReportExcelDataDetailResultData();
                maxValue.setDivision(DIVISION_LINE[1]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    maxValue.setUnit(line.getLineUnit());
                } else {
                    maxValue.setUnit(ApiSimpleConstants.UNIT_DEMAND);
                }
                maxValue.setValues(new ArrayList<>());
                //標準外使用量
                DemandOrgMonthReportExcelDataDetailResultData standardValue = new DemandOrgMonthReportExcelDataDetailResultData();
                standardValue.setDivision(DIVISION_LINE[2]);
                if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                    standardValue.setUnit(line.getLineUnit());
                } else {
                    standardValue.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                }
                standardValue.setValues(new ArrayList<>());

                //デマンド月報情報を取得する
                CommonDemandMonthReportLineListResult monthLineReportParam = DemandEmsUtility
                        .getMonthReportLineListParam(
                                parameter.getOperationCorpId(), parameter.getBuildingId(), line.getLineGroupId(),
                                line.getLineNo(),
                                summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo());
                List<CommonDemandMonthReportLineListResult> monthLineReportList = getResultList(
                        commonDemandMonthReportLineListServiceDaoImpl, monthLineReportParam);

                //建物系統時限標準値情報を取得する
                BuildingLineTimeStandardListTimeResultData lineStandardParam = DemandEmsUtility
                        .getBuildingLineTimeStandardListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                line.getLineGroupId(), line.getLineNo());
                List<BuildingLineTimeStandardListTimeResultData> lineStandardList = getResultList(
                        buildingLineTimeStandardListServiceDaoImpl, lineStandardParam);
                //デマンド日報系統情報を取得する
                CommonDemandDayReportLineListResult dayLineReportParam = DemandEmsUtility.getDayReportLineListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(), line.getLineGroupId(),
                        line.getLineNo(),
                        summaryRange.getMeasurementDateFrom(), BigDecimal.ONE, summaryRange.getMeasurementDateTo(),
                        new BigDecimal("48"));
                List<CommonDemandDayReportLineListResult> dayLineReportList = getResultList(
                        commonDemandDayReportLineListServiceDaoImpl, dayLineReportParam);

                //実績値をセットする
                Date currentDate = null;
                do {

                    if (currentDate == null) {
                        currentDate = summaryRange.getMeasurementDateFrom();
                    } else {
                        //1日進める
                        currentDate = DateUtility.plusDay(currentDate, 1);
                    }

                    if (monthLineReportList == null || monthLineReportList.isEmpty()) {
                        //集計範囲のデータがない場合はnullを設定
                        usedValue.getValues().add(null);
                        maxValue.getValues().add(null);
                        standardValue.getValues().add(null);
                    } else {
                        Boolean setFlg = Boolean.FALSE;
                        for (CommonDemandMonthReportLineListResult monthLine : monthLineReportList) {
                            if (currentDate.equals(monthLine.getMeasurementDate())) {
                                usedValue.getValues().add(monthLine.getLineValueKwh());
                                maxValue.getValues().add(monthLine.getLineMaxKw());
                                //標準外使用量を算出する
                                standardValue.getValues()
                                        .add(getStandardOverValue(monthLine, lineStandardList, dayLineReportList,
                                                line.getLineTarget()));
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

                } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

                //各項目のサマリ値を取得する
                List<BigDecimal> usedValues = getSummaryData(usedValue.getValues(), Boolean.TRUE);
                List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE);
                List<BigDecimal> standardValues = getSummaryData(standardValue.getValues(), Boolean.TRUE);

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

            DemandOrgMonthReportExcelDataLineResultData result = new DemandOrgMonthReportExcelDataLineResultData();
            List<DemandOrgMonthReportExcelDataDetailResultData> detailList = new ArrayList<>();

            result.setLineName(LINE_NAME_AMEDAS);
            result.setLineType(LINE_TYPE_AMEDAS);
            result.setAmedas(Boolean.TRUE);
            result.setCorpSumDate(corpDemand.getSumDate());
            result.setBuildingSumDate(buildingDemand.getSumDate());

            //平均値
            DemandOrgMonthReportExcelDataDetailResultData averageValue = new DemandOrgMonthReportExcelDataDetailResultData();
            averageValue.setDivision(DIVISION_AMEDAS[0]);
            averageValue.setUnit(UNIT_AMEDAS);
            averageValue.setValues(new ArrayList<>());
            //最大値
            DemandOrgMonthReportExcelDataDetailResultData maxValue = new DemandOrgMonthReportExcelDataDetailResultData();
            maxValue.setDivision(DIVISION_AMEDAS[1]);
            maxValue.setUnit(UNIT_AMEDAS);
            maxValue.setValues(new ArrayList<>());
            //最小値
            DemandOrgMonthReportExcelDataDetailResultData minValue = new DemandOrgMonthReportExcelDataDetailResultData();
            minValue.setDivision(DIVISION_AMEDAS[2]);
            minValue.setUnit(UNIT_AMEDAS);
            minValue.setValues(new ArrayList<>());

            //デマンド月報からデータを取得する
            CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo());
            List<CommonDemandMonthReportListResult> monthReportList = getResultList(
                    commonDemandMonthReportListServiceDaoImpl, monthReportParam);

            //実績値をセットする
            Date currentDate = null;
            do {

                if (currentDate == null) {
                    currentDate = summaryRange.getMeasurementDateFrom();
                } else {
                    //1日進める
                    currentDate = DateUtility.plusDay(currentDate, 1);
                }

                if (monthReportList == null || monthReportList.isEmpty()) {
                    //集計範囲のデータがない場合はnullを設定
                    averageValue.getValues().add(null);
                    maxValue.getValues().add(null);
                    minValue.getValues().add(null);
                } else {
                    Boolean setFlg = Boolean.FALSE;
                    for (CommonDemandMonthReportListResult monthData : monthReportList) {
                        if (currentDate.equals(monthData.getMeasurementDate())) {
                            averageValue.getValues().add(monthData.getOutAirTempAvg());
                            maxValue.getValues().add(monthData.getOutAirTempMax());
                            minValue.getValues().add(monthData.getOutAirTempMin());
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

            } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

            //各項目のサマリ値を取得する
            List<BigDecimal> averageValues = getSummaryData(averageValue.getValues(), Boolean.FALSE, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            List<BigDecimal> minValues = getSummaryData(minValue.getValues(), Boolean.FALSE, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

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
     * @return
     */
    private List<DemandOrgMonthReportExcelDataSmResultData> getSmList(DemandOrgMonthReportExcelDataParameter parameter,
            SummaryRangeForMonthResult summaryRange, List<DemandGraphElementListDetailResultData> graphElementList) {

        List<DemandOrgMonthReportExcelDataSmResultData> resultList = new ArrayList<>();
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
            DemandOrgMonthReportExcelDataSmResultData result = new DemandOrgMonthReportExcelDataSmResultData();
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
                    DemandOrgMonthReportExcelDataPointResultData point = new DemandOrgMonthReportExcelDataPointResultData();
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
                            DemandOrgMonthReportExcelDataDetailResultData averageValue = new DemandOrgMonthReportExcelDataDetailResultData();
                            averageValue.setDivision(DIVISION_POINT[0]);
                            averageValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                            averageValue.setValues(new ArrayList<>());
                            //最大値
                            DemandOrgMonthReportExcelDataDetailResultData maxValue = new DemandOrgMonthReportExcelDataDetailResultData();
                            maxValue.setDivision(DIVISION_POINT[1]);
                            maxValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                            maxValue.setValues(new ArrayList<>());

                            //デマンド月報ポイント情報を取得する
                            CommonDemandMonthReportPointListResult monthPointReportParam = DemandEmsUtility
                                    .getMonthReportPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                            buildingSmPointList.get(0).getSmId(), summaryRange.getMeasurementDateFrom(),
                                            summaryRange.getMeasurementDateTo(), buildingSmPointList.get(0).getPointNo(),
                                            buildingSmPointList.get(0).getPointNo());
                            List<CommonDemandMonthReportPointListResult> monthPointList = getResultList(
                                    commonDemandMonthReportPointListServiceDaoImpl, monthPointReportParam);

                            //実績値をセットする
                            Date currentDate = null;
                            do {

                                if (currentDate == null) {
                                    currentDate = summaryRange.getMeasurementDateFrom();
                                } else {
                                    //1日進める
                                    currentDate = DateUtility.plusDay(currentDate, 1);
                                }

                                if (monthPointList == null || monthPointList.isEmpty()) {
                                    //集計範囲のデータがない場合はnullを設定
                                    averageValue.getValues().add(null);
                                    maxValue.getValues().add(null);
                                } else {
                                    Boolean setFlg = Boolean.FALSE;
                                    for (CommonDemandMonthReportPointListResult monthData : monthPointList) {
                                        if (currentDate.equals(monthData.getMeasurementDate())) {
                                            averageValue.getValues().add(monthData.getPointAvg());
                                            maxValue.getValues().add(monthData.getPointMax());
                                            setFlg = true;
                                        }
                                    }
                                    if (!setFlg) {
                                        //対象日にデータが取得できない場合はnullを設定
                                        averageValue.getValues().add(null);
                                        maxValue.getValues().add(null);
                                    }
                                }

                            } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

                            //各項目のサマリ値を取得する
                            List<BigDecimal> averageValues = getSummaryData(averageValue.getValues(), Boolean.FALSE,
                                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

                            List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE,
                                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

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
                    DemandOrgMonthReportExcelDataPointResultData point = new DemandOrgMonthReportExcelDataPointResultData();

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
                        DemandOrgMonthReportExcelDataDetailResultData maxValue = new DemandOrgMonthReportExcelDataDetailResultData();
                        maxValue.setDivision(DIVISION_POINT_SRC[0]);
                        maxValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                        maxValue.setValues(new ArrayList<>());
                        //最小値
                        DemandOrgMonthReportExcelDataDetailResultData minValue = new DemandOrgMonthReportExcelDataDetailResultData();
                        minValue.setDivision(DIVISION_POINT_SRC[1]);
                        minValue.setUnit(buildingSmPointList.get(0).getPointUnit());
                        minValue.setValues(new ArrayList<>());

                        //デマンド月報ポイント情報を取得する
                        CommonDemandMonthReportPointListResult monthPointReportParam = DemandEmsUtility
                                .getMonthReportPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                        buildingSmPointList.get(0).getSmId(), summaryRange.getMeasurementDateFrom(),
                                        summaryRange.getMeasurementDateTo(), buildingSmPointList.get(0).getPointNo(),
                                        buildingSmPointList.get(0).getPointNo());
                        List<CommonDemandMonthReportPointListResult> monthPointList = getResultList(
                                commonDemandMonthReportPointListServiceDaoImpl, monthPointReportParam);

                        //実績値をセットする
                        Date currentDate = null;
                        do {

                            if (currentDate == null) {
                                currentDate = summaryRange.getMeasurementDateFrom();
                            } else {
                                //1日進める
                                currentDate = DateUtility.plusDay(currentDate, 1);
                            }

                            if (monthPointList == null || monthPointList.isEmpty()) {
                                //集計範囲のデータがない場合はnullを設定
                                maxValue.getValues().add(null);
                                minValue.getValues().add(null);
                            } else {
                                Boolean setFlg = Boolean.FALSE;
                                for (CommonDemandMonthReportPointListResult monthData : monthPointList) {
                                    if (currentDate.equals(monthData.getMeasurementDate())) {
                                        maxValue.getValues().add(monthData.getPointMax());
                                        minValue.getValues().add(monthData.getPointMin());
                                        setFlg = true;
                                    }
                                }
                                if (!setFlg) {
                                    //対象日にデータが取得できない場合はnullを設定
                                    maxValue.getValues().add(null);
                                    minValue.getValues().add(null);
                                }
                            }

                        } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

                        //各項目のサマリ値を取得する
                        List<BigDecimal> maxValues = getSummaryData(maxValue.getValues(), Boolean.FALSE, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
                        List<BigDecimal> minValues = getSummaryData(minValue.getValues(), Boolean.FALSE, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());

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
     * 時間帯情報を取得する
     * @param parameter
     * @param summaryRange
     * @param graphElementList
     * @return
     */
    private List<DemandOrgMonthReportExcelDataTimelyResultData> getTimelyList(
            DemandOrgMonthReportExcelDataParameter parameter, SummaryRangeForMonthResult summaryRange,
            List<DemandGraphElementListDetailResultData> graphElementList) {

        List<DemandOrgMonthReportExcelDataTimelyResultData> resultList = new ArrayList<>();

        //デマンド月報情報からデータを取得する
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), summaryRange.getMeasurementDateFrom(),
                summaryRange.getMeasurementDateTo());
        List<CommonDemandMonthReportListResult> monthReportList = getResultList(
                commonDemandMonthReportListServiceDaoImpl, monthReportParam);

        //開店準備時間
        DemandOrgMonthReportExcelDataTimelyResultData openPrepTime = new DemandOrgMonthReportExcelDataTimelyResultData();
        DemandOrgMonthReportExcelDataDetailResultData openPrepDetail = new DemandOrgMonthReportExcelDataDetailResultData();
        openPrepTime.setTime(TIMES[0]);
        openPrepDetail.setDivision(DIVISION_TIME);
        openPrepDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        openPrepDetail.setValues(new ArrayList<>());
        //開店時間
        DemandOrgMonthReportExcelDataTimelyResultData openTime = new DemandOrgMonthReportExcelDataTimelyResultData();
        DemandOrgMonthReportExcelDataDetailResultData openDetail = new DemandOrgMonthReportExcelDataDetailResultData();
        openTime.setTime(TIMES[1]);
        openDetail.setDivision(DIVISION_TIME);
        openDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        openDetail.setValues(new ArrayList<>());
        //閉店準備時間
        DemandOrgMonthReportExcelDataTimelyResultData closePrepTime = new DemandOrgMonthReportExcelDataTimelyResultData();
        DemandOrgMonthReportExcelDataDetailResultData closePrepDetail = new DemandOrgMonthReportExcelDataDetailResultData();
        closePrepTime.setTime(TIMES[2]);
        closePrepDetail.setDivision(DIVISION_TIME);
        closePrepDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        closePrepDetail.setValues(new ArrayList<>());
        //閉店時間
        DemandOrgMonthReportExcelDataTimelyResultData closeTime = new DemandOrgMonthReportExcelDataTimelyResultData();
        DemandOrgMonthReportExcelDataDetailResultData closeDetail = new DemandOrgMonthReportExcelDataDetailResultData();
        closeTime.setTime(TIMES[3]);
        closeDetail.setDivision(DIVISION_TIME);
        closeDetail.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
        closeDetail.setValues(new ArrayList<>());

        //実績値をセットする
        Date currentDate = null;
        do {

            if (currentDate == null) {
                currentDate = summaryRange.getMeasurementDateFrom();
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            if (monthReportList == null || monthReportList.isEmpty()) {
                //集計範囲のデータがない場合はnullを設定
                openPrepDetail.getValues().add(null);
                openDetail.getValues().add(null);
                closePrepDetail.getValues().add(null);
                closeDetail.getValues().add(null);
            } else {
                Boolean setFlg = Boolean.FALSE;
                for (CommonDemandMonthReportListResult monthData : monthReportList) {
                    if (currentDate.equals(monthData.getMeasurementDate())) {
                        openPrepDetail.getValues().add(monthData.getOpenPreparationKwh());
                        openDetail.getValues().add(monthData.getOpenTimeKwh());
                        closePrepDetail.getValues().add(monthData.getClosePreparationKwh());
                        closeDetail.getValues().add(monthData.getCloseTimeKwh());
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

        } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

        //各項目のサマリ値を取得する
        List<BigDecimal> openPrepValues = getSummaryData(openPrepDetail.getValues(), Boolean.TRUE);
        List<BigDecimal> openValues = getSummaryData(openDetail.getValues(), Boolean.TRUE);
        List<BigDecimal> closePrepValues = getSummaryData(closePrepDetail.getValues(), Boolean.TRUE);
        List<BigDecimal> closeValues = getSummaryData(closeDetail.getValues(), Boolean.TRUE);

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
     * @return
     */
    private BigDecimal getStandardOverValue(CommonDemandMonthReportLineListResult monthReportList,
            List<BuildingLineTimeStandardListTimeResultData> starndardList,
            List<CommonDemandDayReportLineListResult> dayReportList, String lineTarget) {

        BigDecimal standardOverValue = BigDecimal.ZERO;

        if (monthReportList == null || starndardList == null || starndardList.isEmpty() || dayReportList == null
                || dayReportList.isEmpty()) {
            return null;
        }

        //デマンド日報系統データから対象計測年月日のデータを取得する
        List<CommonDemandDayReportLineListResult> targetDateReportList = dayReportList.stream()
                .filter(i -> i.getMeasurementDate().equals(monthReportList.getMeasurementDate()))
                .collect(Collectors.toList());

        if (targetDateReportList == null || targetDateReportList.isEmpty()) {
            return null;
        }

        for (CommonDemandDayReportLineListResult dayLine : targetDateReportList) {
            for (BuildingLineTimeStandardListTimeResultData standard : starndardList) {
                if (dayLine.getJigenNo().compareTo(standard.getJigenNo()) == 0) {
                    //時限Noが同じ場合
                    if (standard.getLineLimitStandardKw() != null
                     && dayLine.getLineValueKw().compareTo(standard.getLineLimitStandardKw()) > 0) {
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
    private List<BigDecimal> getSummaryData(List<BigDecimal> values, Boolean sumFlg) {

        return getSummaryData(values, sumFlg, ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
    }
    /**
     * 各項目のサマリ値を算出する
     * @param values
     * @param sumFlg
     * @return
     */
    private List<BigDecimal> getSummaryData(List<BigDecimal> values, Boolean sumFlg, String control) {

        BigDecimal maxValue = null;
        BigDecimal minValue = null;
        BigDecimal sumValue = null;
        BigDecimal countValue = null;
        BigDecimal averageValue = null;

        for (BigDecimal value : values) {
            if (value != null) {
                if (maxValue == null) {
                    maxValue = value;
                } else if (value.compareTo(maxValue) > 0) {
                    maxValue = value;
                }
                if (minValue == null) {
                    minValue = value;
                } else if (value.compareTo(minValue) < 0) {
                    minValue = value;
                }
                if (sumValue == null) {
                    sumValue = value;
                } else {
                    sumValue = sumValue.add(value);
                }
                if (countValue == null) {
                    countValue = BigDecimal.ONE;
                } else {
                    countValue = countValue.add(BigDecimal.ONE);
                }
            }
        }

        //平均値を算出する
        if (countValue == null) {
            averageValue = null;
        } else {
            averageValue = sumValue.divide(countValue, 11, BigDecimal.ROUND_HALF_UP).setScale(1, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control));
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
