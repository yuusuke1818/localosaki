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
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMonthReportListLineUsedCompareParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMonthReportListLineUsedCompareResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CommonDisplayElementListResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerMonthDataDetailResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerMonthDataResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerMonthDataSummaryResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListLineUsedCompareDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListLineUsedCompareHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListLineUsedCompareHeaderTitleResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListLineUsedCompareSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・月報・データ対比） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgMonthReportListLineUsedCompareDao
        extends OsolApiDao<DemandOrgMonthReportListLineUsedCompareParameter> {

    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandMonthReportPointListServiceDaoImpl commonDemandMonthReportPointListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;

    public DemandOrgMonthReportListLineUsedCompareDao() {
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandMonthReportPointListServiceDaoImpl = new CommonDemandMonthReportPointListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
    }

    @Override
    public DemandOrgMonthReportListLineUsedCompareResult query(
            DemandOrgMonthReportListLineUsedCompareParameter parameter) throws Exception {
        DemandOrgMonthReportListLineUsedCompareResult result = new DemandOrgMonthReportListLineUsedCompareResult();
        DemandOrgMonthReportListLineUsedCompareHeaderResultData header = new DemandOrgMonthReportListLineUsedCompareHeaderResultData();
        List<DemandOrgMonthReportListLineUsedCompareHeaderTitleResultData> headerTitleList = new ArrayList<>();
        List<DemandOrgMonthReportListLineUsedCompareDetailResultData> detailList = new ArrayList<>();
        DemandOrgMonthReportListLineUsedCompareSummaryAreaResultData summary = new DemandOrgMonthReportListLineUsedCompareSummaryAreaResultData();
        List<CommonDisplayElementListResult> displayElementList;
        CommonLinePowerMonthDataResult linePowerData;
        Date currentDate = null;
        Date destinationDate = null;
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
        List<Date> exclusionDataList = new ArrayList<>();
        Date dayFromDestination;
        Date dayToDestination;
        String dayFromDestinationYyyyMm;
        String dayToDestinationYyyyMm;
        BigDecimal diff;
        BigDecimal yyyyCompare;
        BigDecimal yyyyHeader;
        BigDecimal mmCompare;
        BigDecimal mmHeader;

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
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

        //集計範囲を取得する（比較元）
        SummaryRangeForMonthResult summaryRangeSource = SummaryRangeUtility.getSummaryRangeForMonth(
                DateUtility.changeDateFormat(parameter.getDayCompareSource(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //ヘッダ部に値をセットする
        header.setDayFromSource(summaryRangeSource.getMeasurementDateFrom());
        header.setDayToSource(summaryRangeSource.getMeasurementDateTo());

        //集計範囲を取得する（比較先）
        if (DateUtility
                .changeDateFormat(parameter.getDayCompareSource(), DateUtility.DATE_FORMAT_YYYYMM)
                .equals(DateUtility.changeDateFormat(header.getDayFromSource(), DateUtility.DATE_FORMAT_YYYYMM))) {
            //比較先は、日付になっていない可能性があるので、文字列で取得
            dayFromDestinationYyyyMm = parameter.getDayCompareDestination().substring(0, 6);
        } else {
            //差分を取得（単位：月）
            yyyyCompare = new BigDecimal(DateUtility.changeDateFormat(parameter.getDayCompareSource(),
                    DateUtility.DATE_FORMAT_YYYY));
            yyyyHeader = new BigDecimal(
                    DateUtility.changeDateFormat(header.getDayFromSource(), DateUtility.DATE_FORMAT_YYYY));
            mmCompare = new BigDecimal(DateUtility.changeDateFormat(parameter.getDayCompareSource(),
                    DateUtility.DATE_FORMAT_MM));
            mmHeader = new BigDecimal(
                    DateUtility.changeDateFormat(header.getDayFromSource(), DateUtility.DATE_FORMAT_MM));

            diff = (yyyyHeader.multiply(new BigDecimal("12")).add(mmHeader))
                    .subtract(yyyyCompare.multiply(new BigDecimal("12")).add(mmCompare));

            //比較先の年月の1日から差分を加算（減算）し、算出
            dayFromDestinationYyyyMm = DateUtility
                    .changeDateFormat(
                            DateUtility.plusMonth(
                                    DateUtility.conversionDate(
                                            parameter.getDayCompareDestination().substring(0, 6).concat("01"),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    diff.intValue()),
                            DateUtility.DATE_FORMAT_YYYYMM);

        }

        if (DateUtility
                .changeDateFormat(parameter.getDayCompareSource(), DateUtility.DATE_FORMAT_YYYYMM)
                .equals(DateUtility.changeDateFormat(header.getDayToSource(), DateUtility.DATE_FORMAT_YYYYMM))) {
            //比較先は、日付になっていない可能性があるので、文字列で取得
            dayToDestinationYyyyMm = parameter.getDayCompareDestination().substring(0, 6);
        } else {
            //差分を取得（単位：月）
            yyyyCompare = new BigDecimal(DateUtility.changeDateFormat(parameter.getDayCompareSource(),
                    DateUtility.DATE_FORMAT_YYYY));
            yyyyHeader = new BigDecimal(
                    DateUtility.changeDateFormat(header.getDayToSource(), DateUtility.DATE_FORMAT_YYYY));
            mmCompare = new BigDecimal(DateUtility.changeDateFormat(parameter.getDayCompareSource(),
                    DateUtility.DATE_FORMAT_MM));
            mmHeader = new BigDecimal(
                    DateUtility.changeDateFormat(header.getDayToSource(), DateUtility.DATE_FORMAT_MM));

            diff = (yyyyHeader.multiply(new BigDecimal("12")).add(mmHeader))
                    .subtract(yyyyCompare.multiply(new BigDecimal("12")).add(mmCompare));

            //比較先の年月の1日から差分を加算（減算）し、算出
            dayToDestinationYyyyMm = DateUtility
                    .changeDateFormat(
                            DateUtility.plusMonth(
                                    DateUtility.conversionDate(
                                            parameter.getDayCompareDestination().substring(0, 6).concat("01"),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    diff.intValue()),
                            DateUtility.DATE_FORMAT_YYYYMM);

        }

        if (DateUtility.conversionDate(
                dayFromDestinationYyyyMm
                        .concat(DateUtility.changeDateFormat(header.getDayFromSource(), DateUtility.DATE_FORMAT_DD)),
                DateUtility.DATE_FORMAT_YYYYMMDD) == null) {
            //比較先の年月日Fromが取得できない場合は、翌月1日
            dayFromDestination = DateUtility.plusMonth(
                    DateUtility.conversionDate(dayFromDestinationYyyyMm.concat("01"), DateUtility.DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            dayFromDestination = DateUtility.conversionDate(
                    dayFromDestinationYyyyMm.concat(
                            DateUtility.changeDateFormat(header.getDayFromSource(), DateUtility.DATE_FORMAT_DD)),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
        }

        if (DateUtility.conversionDate(
                dayToDestinationYyyyMm
                        .concat(DateUtility.changeDateFormat(header.getDayToSource(), DateUtility.DATE_FORMAT_DD)),
                DateUtility.DATE_FORMAT_YYYYMMDD) == null) {
            //比較元の年月日Fromが取得できない場合は、当月末日
            dayToDestination = DateUtility.changeDateMonthLast(
                    DateUtility.conversionDate(dayToDestinationYyyyMm.concat("01"), DateUtility.DATE_FORMAT_YYYYMMDD));
        } else {
            dayToDestination = DateUtility.conversionDate(
                    dayToDestinationYyyyMm
                            .concat(DateUtility.changeDateFormat(header.getDayToSource(), DateUtility.DATE_FORMAT_DD)),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
        }

        //ヘッダ部に値をセットする
        header.setDayFromDestination(dayFromDestination);
        header.setDayToDestination(dayToDestination);

        do {
            DemandOrgMonthReportListLineUsedCompareDetailResultData detail = new DemandOrgMonthReportListLineUsedCompareDetailResultData();
            //比較元情報を作成する
            if (currentDate == null) {
                currentDate = summaryRangeSource.getMeasurementDateFrom();
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            //比較先情報を作成する。
            boolean flg = false;
            if (destinationDate == null) {
                //比較先日付が設定されていない場合、比較先Fromの日付と現在日付で日を比較
                if (DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_DD).equals(
                        DateUtility.changeDateFormat(header.getDayFromDestination(), DateUtility.DATE_FORMAT_DD))) {
                    //一致する場合のみ設定
                    flg = true;
                    destinationDate = header.getDayFromDestination();
                }
            } else if (DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_DD).equals(DateUtility
                    .changeDateFormat(DateUtility.plusDay(destinationDate, 1), DateUtility.DATE_FORMAT_DD))) {
                //設定されている場合は、1日後の日付と現在日付で比較
                //一致する場合
                if (destinationDate.compareTo(header.getDayToDestination()) >= 0) {
                    //1日進めると比較先Toより未来日になる場合は日付のみ進める
                    destinationDate = DateUtility.plusDay(destinationDate, 1);
                } else {
                    //1日進めると比較先To以前の場合は、日付とフラグを帰る
                    flg = true;
                    destinationDate = DateUtility.plusDay(destinationDate, 1);
                }
            } else if (DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_DD).equals("01")) {
                //比較元の日付が1日になっている場合、1日までの日付を除外リストに置き、1日を設定する
                do {
                    destinationDate = DateUtility.plusDay(destinationDate, 1);
                    if (!DateUtility.changeDateFormat(destinationDate, DateUtility.DATE_FORMAT_DD).equals("01")) {
                        exclusionDataList.add(destinationDate);
                    } else {
                        flg = true;
                    }
                } while (!DateUtility.changeDateFormat(destinationDate, DateUtility.DATE_FORMAT_DD).equals("01"));
            }

            detail.setDaySource(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
            detail.setDayOfWeekSource(DateUtility.getDayOfWeekDay(currentDate));
            if (flg) {
                detail.setDayDestination(
                        DateUtility.changeDateFormat(destinationDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
                detail.setDayOfWeekDestination(DateUtility.getDayOfWeekDay(destinationDate));
            } else {
                detail.setDayDestination(null);
                detail.setDayOfWeekDestination(null);
            }

            detail.setElementResultDestinationList(new ArrayList<>());
            detail.setElementResultSourceList(new ArrayList<>());
            detail.setElementSubstractedList(new ArrayList<>());
            detail.setElementReductionList(new ArrayList<>());
            detailList.add(detail);

        } while (!currentDate.equals(summaryRangeSource.getMeasurementDateTo()));

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
                        new DemandOrgMonthReportListLineUsedCompareHeaderTitleResultData(
                                displayElement.getElementName(),
                                ApiSimpleConstants.UNIT_USE_POWER, ApiSimpleConstants.UNIT_USE_POWER,
                                displayElement.getElementType(), displayElement.getGraphColor()));
            } else {
                headerTitleList.add(new DemandOrgMonthReportListLineUsedCompareHeaderTitleResultData(
                        displayElement.getElementName(), displayElement.getUnit(), displayElement.getSummaryUnit(),
                        displayElement.getElementType(), displayElement.getGraphColor()));
            }

            //系統別データを取得する（比較先）
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal()
                            .equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        parameter.getLineGroupId(), displayElement.getLineNo(),
                        header.getDayFromDestination(), header.getDayToDestination(), parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(),
                        displayElement.getElementType(), exclusionDataList);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        displayElement.getSmId(),
                        displayElement.getPointNo(), header.getDayFromDestination(), header.getDayToDestination(),
                        parameter.getPrecision(), parameter.getBelowAccuracyControl(), exclusionDataList);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        header.getDayFromDestination(),
                        header.getDayToDestination(), parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(),
                        exclusionDataList);
            } else {
                linePowerData = null;
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummaryDestinationList.add(null);
                elementMaxDestinationList.add(null);
                elementMinDestinationList.add(null);
                elementAverageDestinationList.add(null);
                for (DemandOrgMonthReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultDestinationList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummaryDestinationList.add(linePowerData.getSummary().getSummaryNumUsed());
                elementMaxDestinationList.add(linePowerData.getSummary().getMaxNumUsed());
                elementMinDestinationList.add(linePowerData.getSummary().getMinNumUsed());
                elementAverageDestinationList.add(linePowerData.getSummary().getAverageNumUsed());
                for (DemandOrgMonthReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerMonthDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (CheckUtility.isNullOrEmpty(detail.getDayDestination())) {
                            //対象日付が設定されていない場合
                            break;
                        }
                        if (DateUtility
                                .conversionDate(detail.getDayDestination(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .compareTo(detailResult.getMeasurementDate()) == 0) {
                            //計測年月日が一致する場合
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
                linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        parameter.getLineGroupId(), displayElement.getLineNo(),
                        summaryRangeSource.getMeasurementDateFrom(), summaryRangeSource.getMeasurementDateTo(),
                        parameter.getPrecision(),
                        parameter.getBelowAccuracyControl(), displayElement.getElementType(), null);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        displayElement.getSmId(),
                        displayElement.getPointNo(), summaryRangeSource.getMeasurementDateFrom(),
                        summaryRangeSource.getMeasurementDateTo(), parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), null);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        summaryRangeSource.getMeasurementDateFrom(), summaryRangeSource.getMeasurementDateTo(),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), null);
            } else {
                linePowerData = null;
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummarySourceList.add(null);
                elementMaxSourceList.add(null);
                elementMinSourceList.add(null);
                elementAverageSourceList.add(null);
                for (DemandOrgMonthReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultSourceList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummarySourceList.add(linePowerData.getSummary().getSummaryNumUsed());
                elementMaxSourceList.add(linePowerData.getSummary().getMaxNumUsed());
                elementMinSourceList.add(linePowerData.getSummary().getMinNumUsed());
                elementAverageSourceList.add(linePowerData.getSummary().getAverageNumUsed());
                for (DemandOrgMonthReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerMonthDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (DateUtility.conversionDate(detail.getDaySource(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .compareTo(detailResult.getMeasurementDate()) == 0) {
                            //計測年月日が一致する場合
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
                                .multiply(new BigDecimal(100)).divide(elementSummaryDestinationList.get(i),
                                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
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
                                .multiply(new BigDecimal(100)).divide(elementMaxDestinationList.get(i),
                                        parameter.getPrecision(),
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
                                .multiply(new BigDecimal(100)).divide(elementMinDestinationList.get(i),
                                        parameter.getPrecision(),
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
                                .multiply(new BigDecimal(100)).divide(elementAverageDestinationList.get(i),
                                        parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl()))));
            }

            //明細部の差引、削減率
            for (DemandOrgMonthReportListLineUsedCompareDetailResultData detail : detailList) {
                if (detail.getElementResultDestinationList().get(i) == null
                        || detail.getElementResultSourceList().get(i) == null) {
                    detail.getElementSubstractedList().add(i, null);
                    detail.getElementReductionList().add(i, null);
                } else if (detail.getElementResultDestinationList().get(i).compareTo(BigDecimal.ZERO) == 0
                        || detail.getElementResultSourceList().get(i).compareTo(BigDecimal.ZERO) == 0) {
                    detail.getElementSubstractedList().add(i, detail.getElementResultDestinationList().get(i)
                            .subtract(detail.getElementResultSourceList().get(i)));
                    detail.getElementReductionList().add(i, BigDecimal.ZERO);
                } else {
                    detail.getElementSubstractedList().add(i, detail.getElementResultDestinationList().get(i)
                            .subtract(detail.getElementResultSourceList().get(i)));
                    detail.getElementReductionList().add(i,
                            (detail.getElementResultDestinationList().get(i)
                                    .subtract(detail.getElementResultSourceList().get(i)).multiply(new BigDecimal(100))
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
     * 系統別データ作成（系統・月報）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param decimal
     * @param control
     * @param elementType
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerMonthDataResult getLinePowerDataFromLine(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, Date measurementDateFrom, Date measurementDateTo, Integer decimal,
            String control, String elementType, List<Date> exclusionDataList) {

        List<CommonLinePowerMonthDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerMonthDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド月報系統からデータを取得する
        CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(corpId,
                buildingId, lineGroupId, lineNo, measurementDateFrom, measurementDateTo);

        List<CommonDemandMonthReportLineListResult> monthLineList = getResultList(
                commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);
        if (monthLineList != null && !monthLineList.isEmpty()) {
            monthLineList = SortUtility.sortCommonDemandMonthReportLineListByMeasurement(monthLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportLineListResult line : monthLineList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(line.getMeasurementDate())) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }
            //サマリ値の設定
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

            if (maxMax == null) {
                maxMax = line.getLineMaxKw();
            } else if (line.getLineMaxKw().compareTo(maxMax) >= 0) {
                maxMax = line.getLineMaxKw();
            }

            if (min == null) {
                min = line.getLineValueKwh();
            } else if (line.getLineValueKwh().compareTo(min) <= 0) {
                min = line.getLineValueKwh();
            }

            if (line.getLineValueKwh() != null) {
                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }
            }

            //明細部の設定
            CommonLinePowerMonthDataDetailResult detailData = new CommonLinePowerMonthDataDetailResult();
            detailData.setMeasurementDate(line.getMeasurementDate());
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

        summaryData = new CommonLinePowerMonthDataSummaryResult();
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

        return new CommonLinePowerMonthDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（ポイント・月報）
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param decimal
     * @param control
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerMonthDataResult getLinePowerDataFromPoint(String corpId, Long buildingId, Long smId,
            String pointNo, Date measurementDateFrom, Date measurementDateTo, Integer decimal, String control,
            List<Date> exclusionDataList) {
        List<CommonLinePowerMonthDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerMonthDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal pointAverageValAfterFormat;
        BigDecimal pointMaxValAfterFormat;

        //機器ポイントデータを取得する
        SmPointListDetailResultData smParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smParam);

        //デマンド月報ポイントからデータを取得する
        CommonDemandMonthReportPointListResult monthReportParam = DemandEmsUtility.getMonthReportPointListParam(corpId,
                buildingId, smId, measurementDateFrom, measurementDateTo, pointNo, pointNo);
        List<CommonDemandMonthReportPointListResult> monthPointList = getResultList(
                commonDemandMonthReportPointListServiceDaoImpl, monthReportParam);
        if (monthPointList != null && !monthPointList.isEmpty()) {
            monthPointList = SortUtility.sortCommonDemandMonthReportPointListByMeasurement(monthPointList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportPointListResult point : monthPointList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(point.getMeasurementDate())) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }
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
                summary = summary.add(pointAverageValAfterFormat);
            }

            if (maxUse == null) {
                maxUse = pointAverageValAfterFormat;
            } else if (pointAverageValAfterFormat.compareTo(maxUse) >= 0) {
                maxUse = pointAverageValAfterFormat;
            }

            if (maxMax == null) {
                maxMax = pointMaxValAfterFormat;
            } else if (pointMaxValAfterFormat.compareTo(maxMax) >= 0) {
                maxMax = pointMaxValAfterFormat;
            }

            if (min == null) {
                min = pointAverageValAfterFormat;
            } else if (pointAverageValAfterFormat.compareTo(min) <= 0) {
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
            CommonLinePowerMonthDataDetailResult detailData = new CommonLinePowerMonthDataDetailResult();
            detailData.setMeasurementDate(point.getMeasurementDate());
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

        summaryData = new CommonLinePowerMonthDataSummaryResult();
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

        return new CommonLinePowerMonthDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（アメダス・月報）
     *
     * @param corpId
     * @param buildingId
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param decimal
     * @param control
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerMonthDataResult getLinePowerDataFromAmedas(String corpId, Long buildingId,
            Date measurementDateFrom, Date measurementDateTo, Integer decimal, String control,
            List<Date> exclusionDataList) {
        List<CommonLinePowerMonthDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerMonthDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;

        //デマンド月報からデータを取得する
        CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(corpId,
                buildingId, measurementDateFrom, measurementDateTo);
        List<CommonDemandMonthReportListResult> monthList = getResultList(commonDemandMonthReportListServiceDaoImpl,
                monthReportParam);
        if (monthList != null && !monthList.isEmpty()) {
            monthList = SortUtility.sortCommonDemandMonthReportListByMeasurement(monthList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportListResult report : monthList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(report.getMeasurementDate())) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }
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

            if (report.getOutAirTempMax() != null) {
                if (maxMax == null) {
                    maxMax = report.getOutAirTempMax();
                } else if (report.getOutAirTempMax().compareTo(maxMax) >= 0) {
                    maxMax = report.getOutAirTempMax();
                }
            }

            //明細部の設定
            CommonLinePowerMonthDataDetailResult detailData = new CommonLinePowerMonthDataDetailResult();
            detailData.setMeasurementDate(report.getMeasurementDate());
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

        summaryData = new CommonLinePowerMonthDataSummaryResult();
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

        return new CommonLinePowerMonthDataResult(detailList, summaryData);
    }

}
