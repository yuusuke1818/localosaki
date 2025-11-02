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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListLineUsedCompareParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListLineUsedCompareResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CommonDisplayElementListResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataDetailResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerDayDataSummaryResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedCompareDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedCompareHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedCompareHeaderTitleResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineUsedCompareSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・日報・データ対比） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgDayReportListLineUsedCompareDao
        extends OsolApiDao<DemandOrgDayReportListLineUsedCompareParameter> {

    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;
    private final CommonDemandDayReportListServiceDaoImpl commonDemandDayReportListServiceDaoImpl;

    public DemandOrgDayReportListLineUsedCompareDao() {
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
        commonDemandDayReportListServiceDaoImpl = new CommonDemandDayReportListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgDayReportListLineUsedCompareResult query(DemandOrgDayReportListLineUsedCompareParameter parameter)
            throws Exception {
        DemandOrgDayReportListLineUsedCompareResult result = new DemandOrgDayReportListLineUsedCompareResult();
        DemandOrgDayReportListLineUsedCompareHeaderResultData header = new DemandOrgDayReportListLineUsedCompareHeaderResultData();
        List<DemandOrgDayReportListLineUsedCompareHeaderTitleResultData> headerTitleList = new ArrayList<>();
        List<DemandOrgDayReportListLineUsedCompareDetailResultData> detailList = new ArrayList<>();
        DemandOrgDayReportListLineUsedCompareSummaryAreaResultData summary = new DemandOrgDayReportListLineUsedCompareSummaryAreaResultData();
        Date fromDateDestination = null;
        Date toDateDestination = null;
        Date fromDateSource = null;
        Date toDateSource = null;
        BigDecimal currentJigenNo = null;
        List<CommonDisplayElementListResult> displayElementList;
        CommonLinePowerDayDataResult linePowerData;
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

        //比較先の集計範囲を取得する
        SummaryRangeForDayResult summaryRangeDestination = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(parameter.getDayCompareDestination(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getTimesOfDay(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //比較元の集計範囲を取得する
        SummaryRangeForDayResult summaryRangeSource = SummaryRangeUtility
                .getSummaryRangeForDay(
                        DateUtility.changeDateFormat(parameter.getDayCompareSource(), DateUtility.DATE_FORMAT_YYYYMMDD),
                        parameter.getTimesOfDay(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //ヘッダ部に値をセットする
        header.setRangeFromDestination(summaryRangeDestination.getRangeFrom());
        header.setRangeToDestination(summaryRangeDestination.getRangeTo());
        header.setRangeFromSource(summaryRangeSource.getRangeFrom());
        header.setRangeToSource(summaryRangeSource.getRangeTo());
        do {

            DemandOrgDayReportListLineUsedCompareDetailResultData detail = new DemandOrgDayReportListLineUsedCompareDetailResultData();

            if (fromDateDestination == null) {
                fromDateDestination = DateUtility.conversionDate(summaryRangeDestination.getRangeFrom(),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            } else {
                fromDateDestination = toDateDestination;
            }

            toDateDestination = DateUtility.plusMinute(fromDateDestination, 30);

            if (DateUtility.changeDateFormat(toDateDestination, DateUtility.DATE_FORMAT_HHMM_COLON).equals("00:00")) {
                detail.setTimeTitle(
                        DateUtility.changeDateFormat(fromDateDestination, DateUtility.DATE_FORMAT_HHMM_COLON)
                                .concat(ApiSimpleConstants.FROM).concat("24:00"));

            } else {
                detail.setTimeTitle(DateUtility
                        .changeDateFormat(fromDateDestination, DateUtility.DATE_FORMAT_HHMM_COLON)
                        .concat(ApiSimpleConstants.FROM)
                        .concat(DateUtility.changeDateFormat(toDateDestination, DateUtility.DATE_FORMAT_HHMM_COLON)));
            }

            if (currentJigenNo == null) {
                currentJigenNo = summaryRangeDestination.getJigenNoFrom();
            } else if (new BigDecimal(48).compareTo(currentJigenNo) == 0) {
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }

            if (fromDateSource == null) {
                fromDateSource = DateUtility.conversionDate(summaryRangeSource.getRangeFrom(),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            } else {
                fromDateSource = toDateSource;
            }

            toDateSource = DateUtility.plusMinute(fromDateSource, 30);

            detail.setMeasurementDateDestination(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(fromDateDestination, DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD));
            detail.setMeasurementDateSource(DateUtility.conversionDate(
                    DateUtility.changeDateFormat(fromDateSource, DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD));
            detail.setJigenNo(currentJigenNo);
            detail.setElementResultDestinationList(new ArrayList<>());
            detail.setElementResultSourceList(new ArrayList<>());
            detail.setElementSubstractedList(new ArrayList<>());
            detail.setElementReductionList(new ArrayList<>());

            detailList.add(detail);
        } while (!DateUtility.conversionDate(summaryRangeDestination.getRangeTo(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)
                .equals(fromDateDestination));

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
                headerTitleList
                        .add(new DemandOrgDayReportListLineUsedCompareHeaderTitleResultData(
                                displayElement.getElementName(),
                                ApiSimpleConstants.UNIT_DEMAND, ApiSimpleConstants.UNIT_USE_POWER,
                                displayElement.getElementType(),
                                displayElement.getGraphColor()));
            } else {
                headerTitleList.add(new DemandOrgDayReportListLineUsedCompareHeaderTitleResultData(
                        displayElement.getElementName(),
                        displayElement.getUnit(), displayElement.getSummaryUnit(), displayElement.getElementType(),
                        displayElement.getGraphColor()));
            }

            //系統別データを取得する（比較先）
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal()
                            .equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        parameter.getLineGroupId(), displayElement.getLineNo(),
                        summaryRangeDestination.getMeasurementDateFrom(), summaryRangeDestination.getJigenNoFrom(),
                        summaryRangeDestination.getMeasurementDateTo(), summaryRangeDestination.getJigenNoTo(),
                        parameter.getPrecision(), parameter.getBelowAccuracyControl(), displayElement.getElementType());
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        displayElement.getSmId(),
                        displayElement.getPointNo(),
                        summaryRangeDestination.getMeasurementDateFrom(), summaryRangeDestination.getJigenNoFrom(),
                        summaryRangeDestination.getMeasurementDateTo(), summaryRangeDestination.getJigenNoTo(),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        summaryRangeDestination.getMeasurementDateFrom(),
                        summaryRangeDestination.getJigenNoFrom(), summaryRangeDestination.getMeasurementDateTo(),
                        summaryRangeDestination.getJigenNoTo(), parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            } else {
                linePowerData = null;
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummaryDestinationList.add(null);
                elementMaxDestinationList.add(null);
                elementMinDestinationList.add(null);
                elementAverageDestinationList.add(null);

                for (DemandOrgDayReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultDestinationList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummaryDestinationList.add(linePowerData.getSummary().getSummaryNum());
                elementMaxDestinationList.add(linePowerData.getSummary().getMaxNum());
                elementMinDestinationList.add(linePowerData.getSummary().getMinNum());
                elementAverageDestinationList.add(linePowerData.getSummary().getAverageNum());
                for (DemandOrgDayReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerDayDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (detail.getMeasurementDateDestination().compareTo(detailResult.getMeasurementDate()) == 0
                                && detail.getJigenNo().compareTo(detailResult.getJigenNo()) == 0) {
                            //計測年月日と時限Noが一致する場合
                            detail.getElementResultDestinationList().add(detailResult.getResultNum());
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
                        summaryRangeSource.getMeasurementDateFrom(), summaryRangeSource.getJigenNoFrom(),
                        summaryRangeSource.getMeasurementDateTo(), summaryRangeSource.getJigenNoTo(),
                        parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                        displayElement.getElementType());
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        displayElement.getSmId(), displayElement.getPointNo(),
                        summaryRangeSource.getMeasurementDateFrom(), summaryRangeSource.getJigenNoFrom(),
                        summaryRangeSource.getMeasurementDateTo(), summaryRangeSource.getJigenNoTo(),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        summaryRangeSource.getMeasurementDateFrom(),
                        summaryRangeSource.getJigenNoFrom(), summaryRangeSource.getMeasurementDateTo(),
                        summaryRangeSource.getJigenNoTo(),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
            } else {
                linePowerData = null;
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementSummarySourceList.add(null);
                elementMaxSourceList.add(null);
                elementMinSourceList.add(null);
                elementAverageSourceList.add(null);

                for (DemandOrgDayReportListLineUsedCompareDetailResultData detail : detailList) {
                    detail.getElementResultSourceList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementSummarySourceList.add(linePowerData.getSummary().getSummaryNum());
                elementMaxSourceList.add(linePowerData.getSummary().getMaxNum());
                elementMinSourceList.add(linePowerData.getSummary().getMinNum());
                elementAverageSourceList.add(linePowerData.getSummary().getAverageNum());
                for (DemandOrgDayReportListLineUsedCompareDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerDayDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (detail.getMeasurementDateSource().compareTo(detailResult.getMeasurementDate()) == 0
                                && detail.getJigenNo().compareTo(detailResult.getJigenNo()) == 0) {
                            //計測年月日と時限Noが一致する場合
                            detail.getElementResultSourceList().add(detailResult.getResultNum());
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
            for (DemandOrgDayReportListLineUsedCompareDetailResultData detail : detailList) {
                if (detail.getElementResultDestinationList().get(i) == null
                        || detail.getElementResultSourceList().get(i) == null) {
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
        CommonDemandDayReportLineListResult dayLineParam = DemandEmsUtility.getDayReportLineListParam(corpId,
                buildingId, lineGroupId, lineNo, measurementDateFrom, jigenNoFrom, measurementDateTo, jigenNoTo);
        List<CommonDemandDayReportLineListResult> dayLineList = getResultList(
                commonDemandDayReportLineListServiceDaoImpl, dayLineParam);
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
        SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);

        //デマンド日報ポイントからデータを取得する
        CommonDemandDayReportPointListResult dayPointParam = DemandEmsUtility.getDayReportPointListParam(corpId,
                buildingId, smId, measurementDateFrom, jigenNoFrom, measurementDateTo, jigenNoTo, pointNo, pointNo);
        List<CommonDemandDayReportPointListResult> dayPointList = getResultList(
                commonDemandDayReportPointListServiceDaoImpl, dayPointParam);
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
