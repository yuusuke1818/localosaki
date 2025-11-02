/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListLineUsedSummaryParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListLineUsedSummaryResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataDetailResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataSummaryResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedSummaryDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedSummaryHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedSummaryHeaderTitleResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedSummarySummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingLineTimeStandardListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・日報・日報集計表） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgDayReportListLineUsedSummaryDao
        extends OsolApiDao<DemandOrgDayReportListLineUsedSummaryParameter> {

    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;

    public DemandOrgDayReportListLineUsedSummaryDao() {
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        buildingLineTimeStandardListServiceDaoImpl = new BuildingLineTimeStandardListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
    }

    @Override
    public DemandOrgDayReportListLineUsedSummaryResult query(DemandOrgDayReportListLineUsedSummaryParameter parameter)
            throws Exception {
        DemandOrgDayReportListLineUsedSummaryResult result = new DemandOrgDayReportListLineUsedSummaryResult();
        DemandOrgDayReportListLineUsedSummaryHeaderResultData header = new DemandOrgDayReportListLineUsedSummaryHeaderResultData();
        List<DemandOrgDayReportListLineUsedSummaryDetailResultData> detailList = new ArrayList<>();
        List<DemandOrgDayReportListLineUsedSummaryHeaderTitleResultData> headerTitleList = new ArrayList<>();
        DemandOrgDayReportListLineUsedSummarySummaryAreaResultData summary = new DemandOrgDayReportListLineUsedSummarySummaryAreaResultData();
        Date currentDate = null;
        Date fromDate = null;
        Date toDate = null;
        BigDecimal currentJigenNo = null;
        CommonLinePowerDayDataResult linePowerData;
        boolean demandFlg = false;
        List<BigDecimal> dailySummaryList = new ArrayList<>();
        List<BigDecimal> dailyMaxList = new ArrayList<>();
        List<BigDecimal> dailyMinList = new ArrayList<>();
        List<BigDecimal> dailyAverageList = new ArrayList<>();
        List<BigDecimal> dailyContractPowerList = new ArrayList<>();
        List<BigDecimal> dailyTargetPowerList = new ArrayList<>();
        SummaryRangeForMonthResult summaryRange = null;

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計範囲指定方法がNULLの場合、月単位を設定
        if (CheckUtility.isNullOrEmpty(parameter.getRangeUnit())) {
            parameter.setRangeUnit(ApiCodeValueConstants.RANGE_UNIT_MONTHLY.MONTH.getVal());
        }

        //集計期間がNULLの場合、1（ヶ月）を設定
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

        //集計範囲を取得する
        if (ApiCodeValueConstants.RANGE_UNIT_MONTHLY.MONTH.getVal().equals(parameter.getRangeUnit())) {
            summaryRange = SummaryRangeUtility.getSummaryRangeForMonth(
                    DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        } else if (ApiCodeValueConstants.RANGE_UNIT_MONTHLY.DAY.getVal().contains(parameter.getRangeUnit())) {
            summaryRange = SummaryRangeUtility
                    .getSummaryRangeForMonthRangeDaily(
                            DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                            parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        } else {
            return null;
        }

        //ヘッダ部に値をセットする
        header.setDayFrom(summaryRange.getMeasurementDateFrom());
        header.setDayTo(summaryRange.getMeasurementDateTo());
        do {

            if (currentDate == null) {
                currentDate = summaryRange.getMeasurementDateFrom();
            } else {
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            headerTitleList.add(new DemandOrgDayReportListLineUsedSummaryHeaderTitleResultData(
                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                    DateUtility.getDayOfWeekDay(currentDate), null));

        } while (!summaryRange.getMeasurementDateTo().equals(currentDate));
        header.setDetailHeaderList(headerTitleList);

        do {

            DemandOrgDayReportListLineUsedSummaryDetailResultData detail = new DemandOrgDayReportListLineUsedSummaryDetailResultData();

            if (fromDate == null) {
                fromDate = summaryRange.getMeasurementDateFrom();
            } else {
                fromDate = toDate;
            }

            toDate = DateUtility.plusMinute(fromDate, 30);

            if (currentJigenNo == null) {
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }

            if (DateUtility.changeDateFormat(toDate, DateUtility.DATE_FORMAT_HHMM_COLON).equals("00:00")) {
                detail.setTimeTitle(DateUtility.changeDateFormat(fromDate, DateUtility.DATE_FORMAT_HHMM_COLON)
                        .concat(ApiSimpleConstants.FROM).concat("24:00"));
            } else {
                detail.setTimeTitle(DateUtility.changeDateFormat(fromDate, DateUtility.DATE_FORMAT_HHMM_COLON)
                        .concat(ApiSimpleConstants.FROM)
                        .concat(DateUtility.changeDateFormat(toDate, DateUtility.DATE_FORMAT_HHMM_COLON)));
            }

            detail.setJigenNo(currentJigenNo);
            detail.setDailyNumResultList(new ArrayList<>());
            detail.setTimeMax(null);
            detail.setTimeMin(null);
            detail.setTimeAverage(null);
            detail.setTimeLimitStandardKw(null);
            detail.setTimeLowerStandardKw(null);
            detail.setTimeOverStandardKwSummary(null);

            detailList.add(detail);

        } while (!DateUtility.plusMinute(DateUtility.plusDay(summaryRange.getMeasurementDateFrom(), 1), -30)
                .equals(fromDate));

        //単位情報を取得する
        if (parameter.getLineGroupId() != null) {
            //系統情報
            LineListDetailResultData lineListParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                    parameter.getLineGroupId(), parameter.getLineNo(),
                    ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
            List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineListParam);
            if (lineList == null || lineList.size() != 1) {
                return null;
            }

            if (ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
                header.setUnit(lineList.get(0).getLineUnit());
                header.setSummaryUnit(lineList.get(0).getLineUnit());
            } else {
                header.setUnit(ApiSimpleConstants.UNIT_DEMAND);
                header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
                demandFlg = true;
            }
        } else if (parameter.getSmId() != null) {
            //ポイント情報
            DemandBuildingSmPointListDetailResultData buildingSmPointListParam = DemandEmsUtility
                    .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getSmId(), parameter.getPointNo());
            List<DemandBuildingSmPointListDetailResultData> pointList = getResultList(
                    demandBuildingSmPointListServiceDaoImpl, buildingSmPointListParam);
            if (pointList == null || pointList.size() != 1) {
                return null;
            }

            header.setUnit(pointList.get(0).getPointUnit());
            header.setSummaryUnit(null);
        } else {
            //外気温
            header.setUnit(ApiSimpleConstants.UNIT_TEMPERATURE);
            header.setSummaryUnit(null);
        }

        //日別実績を取得
        for (DemandOrgDayReportListLineUsedSummaryHeaderTitleResultData headerTitle : header.getDetailHeaderList()) {
            if (parameter.getLineGroupId() != null) {
                if (demandFlg) {
                    linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getLineGroupId(), parameter.getLineNo(),
                            DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                            BigDecimal.ONE,
                            DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                            new BigDecimal(48),
                            parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal());
                } else {
                    linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getLineGroupId(), parameter.getLineNo(),
                            DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                            BigDecimal.ONE,
                            DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                            new BigDecimal(48),
                            parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal());
                }
            } else if (parameter.getSmId() != null) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        parameter.getSmId(), parameter.getPointNo(),
                        DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        BigDecimal.ONE,
                        DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        new BigDecimal(48),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            } else {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        BigDecimal.ONE,
                        DateUtility.conversionDate(headerTitle.getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        new BigDecimal(48),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            }

            if (linePowerData == null) {
                //取得できない場合
                dailySummaryList.add(null);
                dailyMaxList.add(null);
                dailyMinList.add(null);
                dailyAverageList.add(null);
                dailyTargetPowerList.add(null);
                dailyContractPowerList.add(null);
                for (DemandOrgDayReportListLineUsedSummaryDetailResultData detail : detailList) {
                    detail.getDailyNumResultList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                dailySummaryList.add(linePowerData.getSummary().getSummaryNum());
                dailyMaxList.add(linePowerData.getSummary().getMaxNum());
                dailyMinList.add(linePowerData.getSummary().getMinNum());
                dailyAverageList.add(linePowerData.getSummary().getAverageNum());
                dailyTargetPowerList.add(null);
                dailyContractPowerList.add(null);
                for (DemandOrgDayReportListLineUsedSummaryDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerDayDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (detail.getJigenNo().compareTo(detailResult.getJigenNo()) == 0) {
                            detail.getDailyNumResultList().add(detailResult.getResultNum());
                            flg = true;
                            break;
                        }
                    }
                    if (!flg) {
                        //実績値が設定されていない場合
                        detail.getDailyNumResultList().add(null);
                    }
                }
            }
        }

        //時間帯別サマリ値を取得する
        for (DemandOrgDayReportListLineUsedSummaryDetailResultData detail : detailList) {
            BigDecimal timeSummary = null;
            BigDecimal timeSummaryCount = BigDecimal.ZERO;
            BigDecimal timeMax = null;
            BigDecimal timeMin = null;
            for (BigDecimal timeNum : detail.getDailyNumResultList()) {
                if (timeNum != null) {
                    timeSummaryCount = timeSummaryCount.add(BigDecimal.ONE);
                    if (timeSummary == null) {
                        timeSummary = timeNum;
                    } else {
                        timeSummary = timeSummary.add(timeNum);
                    }
                    if (timeMax == null) {
                        timeMax = timeNum;
                    } else if (timeNum.compareTo(timeMax) >= 0) {
                        timeMax = timeNum;
                    }
                    if (timeMin == null) {
                        timeMin = timeNum;
                    } else if (timeNum.compareTo(timeMin) <= 0) {
                        timeMin = timeNum;
                    }
                }
            }
            detail.setTimeMax(timeMax);
            detail.setTimeMin(timeMin);
            if (timeSummaryCount.compareTo(BigDecimal.ZERO) == 0 || timeSummary == null
                    || timeSummary.compareTo(BigDecimal.ZERO) == 0) {
                detail.setTimeAverage(null);
            } else {
                //系統の場合
                if (parameter.getLineGroupId() != null) {
                    detail.setTimeAverage(
                            timeSummary.divide(timeSummaryCount, parameter.getPrecision(),
                                    ApiCodeValueConstants.PRECISION_CONTROL
                                            .getControlType(parameter.getBelowAccuracyControl())));
                }
                //ポイントまたはアメダスの場合
                else {
                    detail.setTimeAverage(
                            timeSummary.divide(timeSummaryCount, parameter.getPrecision(),
                                    RoundingMode.HALF_UP));
                }
            }
        }

        //時限標準値を取得する
        if (parameter.getLineGroupId() != null) {
            BuildingLineTimeStandardListTimeResultData lineTimeStandardParam = DemandEmsUtility
                    .getBuildingLineTimeStandardListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                            parameter.getLineGroupId(), parameter.getLineNo());
            List<BuildingLineTimeStandardListTimeResultData> buildingLineTimeList = getResultList(
                    buildingLineTimeStandardListServiceDaoImpl, lineTimeStandardParam);
            for (DemandOrgDayReportListLineUsedSummaryDetailResultData detail : detailList) {
                for (BuildingLineTimeStandardListTimeResultData buildingLineTime : buildingLineTimeList) {
                    if (detail.getJigenNo().compareTo(buildingLineTime.getJigenNo()) == 0) {
                        detail.setTimeLimitStandardKw(buildingLineTime.getLineLimitStandardKw());
                        detail.setTimeLowerStandardKw(buildingLineTime.getLineLowerStandardKw());
                        if (demandFlg && buildingLineTime.getLineLimitStandardKw() != null) {
                            //標準外電力計を取得する
                            BigDecimal overPowerSummary = null;
                            for (BigDecimal timeNum : detail.getDailyNumResultList()) {
                                if (timeNum != null) {
                                    if (timeNum.compareTo(buildingLineTime.getLineLimitStandardKw()) > 0) {
                                        //上限基準電力を超えている
                                        if (overPowerSummary == null) {
                                            overPowerSummary = timeNum
                                                    .subtract(buildingLineTime.getLineLimitStandardKw());
                                        } else {
                                            overPowerSummary = overPowerSummary
                                                    .add(timeNum.subtract(buildingLineTime.getLineLimitStandardKw()));
                                        }
                                    }
                                }
                            }
                            detail.setTimeOverStandardKwSummary(overPowerSummary);
                        }
                        break;
                    }
                }
            }
        }

        //日別目標電力を取得する
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), header.getDayFrom(), header.getDayTo());
        List<CommonDemandMonthReportListResult> demandMonthReportList = getResultList(
                commonDemandMonthReportListServiceDaoImpl, monthReportParam);
        if (demandMonthReportList != null && !demandMonthReportList.isEmpty()) {
            demandMonthReportList = SortUtility.sortCommonDemandMonthReportListByMeasurement(demandMonthReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportListResult demandMonthReport : demandMonthReportList) {
            for (int i = 0; i <= headerTitleList.size() - 1; i++) {
                if (DateUtility.conversionDate(headerTitleList.get(i).getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                        .equals(demandMonthReport.getMeasurementDate())) {
                    dailyTargetPowerList.set(i, demandMonthReport.getTargetKw());
                    break;
                }
            }
        }

        //日別契約電力を取得する
        String currentYm = null;
        BigDecimal currentYmContractPower = null;
        for (int i = 0; i <= headerTitleList.size() - 1; i++) {
            if (currentYm == null || !currentYm.equals(DateUtility.changeDateFormat(
                    DateUtility.conversionDate(headerTitleList.get(i).getDay(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                    DateUtility.DATE_FORMAT_YYYYMM))) {
                currentYm = DateUtility.changeDateFormat(
                        DateUtility.conversionDate(headerTitleList.get(i).getDay(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                        DateUtility.DATE_FORMAT_YYYYMM);
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
                List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                        buildingDemandListServiceDaoImpl, buildingDemandParam);
                if (buildingDemandList == null || buildingDemandList.size() != 1) {
                    currentYmContractPower = null;
                } else {
                    currentYmContractPower = buildingDemandList.get(0).getContractKw();
                }
                dailyContractPowerList.set(i, currentYmContractPower);
            } else {
                dailyContractPowerList.set(i, currentYmContractPower);
            }
        }

        //グラフの色を取得する
        List<String> graphColor = DemandGraphAutoColorUtility
                .getGraphAutoColorList(header.getDetailHeaderList().size());

        int i = 0;
        for (DemandOrgDayReportListLineUsedSummaryHeaderTitleResultData headerTitle : header.getDetailHeaderList()) {
            headerTitle.setGraphColor(graphColor.get(i));
            i++;
        }

        result.setHeader(header);
        summary.setDailySummaryList(dailySummaryList);
        summary.setDailyMaxList(dailyMaxList);
        summary.setDailyMinList(dailyMinList);
        summary.setDailyAverageList(dailyAverageList);
        summary.setDailyTargetPowerList(dailyTargetPowerList);
        summary.setDailyContractPowerList(dailyContractPowerList);
        result.setDetailList(detailList);
        result.setSummary(summary);
        return result;
    }

    /**
     * 系統別データ作成（系統・日報）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @param decimal
     * @param control
     * @param elementType
     * @return
     */
    private CommonLinePowerDayDataResult getLinePowerDataFromLine(String corpId, Long buildingId, Long lineGroupId,
            String lineNo, Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo,
            BigDecimal jigenNoTo, Integer decimal, String control, String elementType) {

        List<CommonLinePowerDayDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerDayDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal max = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド日報系統からデータを取得する
        CommonDemandDayReportLineListResult dayLineListParam = DemandEmsUtility.getDayReportLineListParam(corpId,
                buildingId, lineGroupId, lineNo, measurementDateFrom, jigenNoFrom, measurementDateTo, jigenNoTo);
        List<CommonDemandDayReportLineListResult> dayLineList = getResultList(
                commonDemandDayReportLineListServiceDaoImpl, dayLineListParam);
        if (dayLineList != null && !dayLineList.isEmpty()) {
            dayLineList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(dayLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandDayReportLineListResult line : dayLineList) {
            //サマリ値の設定
            if (summary == null) {
                summary = line.getLineValueKw();
            } else {
                summary = summary.add(line.getLineValueKw());
            }
            if (max == null) {
                max = line.getLineValueKw();
            } else if (line.getLineValueKw().compareTo(max) >= 0) {
                max = line.getLineValueKw();
            }
            if (min == null) {
                min = line.getLineValueKw();
            } else if (line.getLineValueKw().compareTo(min) <= 0) {
                min = line.getLineValueKw();
            }
            if (line.getLineValueKw() != null) {
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }
            //明細部の設定
            CommonLinePowerDayDataDetailResult detailData = new CommonLinePowerDayDataDetailResult();
            detailData.setMeasurementDate(line.getMeasurementDate());
            detailData.setJigenNo(line.getJigenNo());
            if (line.getLineValueKw() != null) {
                detailData.setResultNum(line.getLineValueKw().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNum(line.getLineValueKw());
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerDayDataSummaryResult();
        if (summary != null) {
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(elementType)
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(elementType)) {
                //全体・デマンドの場合
                summaryData.setSummaryNum(summary.divide(new BigDecimal(2), decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                summaryData.setSummaryNum(
                        summary.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }

            if (averageCount != null) {
                summaryData.setAverageNum(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (max != null) {
            summaryData
                    .setMaxNum(max.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData
                    .setMinNum(min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerDayDataResult(detailList, summaryData);

    }

    /**
     * 系統別データ作成（ポイント・日報）
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @param decimal
     * @param control
     * @return
     */
    private CommonLinePowerDayDataResult getLinePowerDataFromPoint(String corpId, Long buildingId, Long smId,
            String pointNo, Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo,
            BigDecimal jigenNoTo, Integer decimal, String control) {
        List<CommonLinePowerDayDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerDayDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal max = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal pointValAfterFormat;

        //機器ポイントデータを取得する
        SmPointListDetailResultData smPointListParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointListParam);

        //デマンド日報ポイントからデータを取得する
        CommonDemandDayReportPointListResult dayPointListParam = DemandEmsUtility.getDayReportPointListParam(corpId,
                buildingId, smId, measurementDateFrom, jigenNoFrom, measurementDateTo, jigenNoTo, pointNo, pointNo);
        List<CommonDemandDayReportPointListResult> dayPointList = getResultList(
                commonDemandDayReportPointListServiceDaoImpl, dayPointListParam);
        if (dayPointList != null && !dayPointList.isEmpty()) {
            dayPointList = SortUtility.sortCommonDemandDayReportPointListByMeasurement(dayPointList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandDayReportPointListResult point : dayPointList) {
            if (point.getPointVal() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointValAfterFormat = point.getPointVal().add(smPointList.get(0).getAnalogOffSetValue());
                    pointValAfterFormat = pointValAfterFormat.multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointValAfterFormat = point.getPointVal();
                }
            } else {
                pointValAfterFormat = point.getPointVal();
            }

            if (pointValAfterFormat != null) {
              //サマリ値の設定
                if (summary == null) {
                    summary = pointValAfterFormat;
                } else {
                    summary = summary.add(pointValAfterFormat);
                }
                if (max == null) {
                    max = pointValAfterFormat;
                } else if (pointValAfterFormat.compareTo(max) >= 0) {
                    max = pointValAfterFormat;
                }
                if (min == null) {
                    min = pointValAfterFormat;
                } else if (pointValAfterFormat.compareTo(min) <= 0) {
                    min = pointValAfterFormat;
                }
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }
            //明細部の設定
            CommonLinePowerDayDataDetailResult detailData = new CommonLinePowerDayDataDetailResult();
            detailData.setMeasurementDate(point.getMeasurementDate());
            detailData.setJigenNo(point.getJigenNo());
            if (pointValAfterFormat != null) {
                detailData.setResultNum(pointValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNum(pointValAfterFormat);
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerDayDataSummaryResult();
        if (summary != null) {
            if (averageCount != null) {
                summaryData.setAverageNum(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (max != null) {
            summaryData
                    .setMaxNum(max.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData
                    .setMinNum(min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerDayDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（アメダス・日報）
     *
     * @param corpId
     * @param buildingId
     * @param measurementDateFrom
     * @param jigenNoFrom
     * @param measurementDateTo
     * @param jigenNoTo
     * @param decimal
     * @param control
     * @return
     */
    private CommonLinePowerDayDataResult getLinePowerDataFromAmedas(String corpId, Long buildingId,
            Date measurementDateFrom, BigDecimal jigenNoFrom, Date measurementDateTo, BigDecimal jigenNoTo,
            Integer decimal, String control) {
        List<CommonLinePowerDayDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerDayDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal max = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド日報からデータを取得する
        CommonDemandDayReportListResult dayListParam = DemandEmsUtility.getDayReportListParam(corpId, buildingId,
                measurementDateFrom, jigenNoFrom, measurementDateTo, jigenNoTo);
        List<CommonDemandDayReportListResult> dayList = getResultList(commonDemandDayReportListServiceDaoImpl,
                dayListParam);
        if (dayList != null && !dayList.isEmpty()) {
            dayList = SortUtility.sortCommonDemandDayReportListByMeasurement(dayList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandDayReportListResult report : dayList) {
            //サマリ値の設定
            if (report.getOutAirTemp() != null) {
                if (summary == null) {
                    summary = report.getOutAirTemp();
                } else {
                    summary = summary.add(report.getOutAirTemp());
                }
                if (max == null) {
                    max = report.getOutAirTemp();
                } else if (report.getOutAirTemp().compareTo(max) >= 0) {
                    max = report.getOutAirTemp();
                }
                if (min == null) {
                    min = report.getOutAirTemp();
                } else if (report.getOutAirTemp().compareTo(min) <= 0) {
                    min = report.getOutAirTemp();
                }
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerDayDataDetailResult detailData = new CommonLinePowerDayDataDetailResult();
            detailData.setMeasurementDate(report.getMeasurementDate());
            detailData.setJigenNo(report.getJigenNo());
            if (report.getOutAirTemp() != null) {
                detailData.setResultNum(report.getOutAirTemp().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNum(report.getOutAirTemp());
            }
            detailList.add(detailData);
        }

        summaryData = new CommonLinePowerDayDataSummaryResult();
        if (summary != null) {
            if (averageCount != null) {
                summaryData.setAverageNum(summary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        if (max != null) {
            summaryData
                    .setMaxNum(max.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData
                    .setMinNum(min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        return new CommonLinePowerDayDataResult(detailList, summaryData);
    }

}
