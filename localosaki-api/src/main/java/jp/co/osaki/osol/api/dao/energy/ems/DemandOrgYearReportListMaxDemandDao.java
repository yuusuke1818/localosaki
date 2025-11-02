/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportListMaxDemandParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportListMaxDemandResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayLoadListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayMaxListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.CommonDemandOrgMaxDemandHeaderPlogAnalogResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListMaxDemandDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListMaxDemandHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListMaxDemandSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayMaxListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
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
 * エネルギー使用状況実績取得（個別・年報・最大デマンド） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgYearReportListMaxDemandDao extends OsolApiDao<DemandOrgYearReportListMaxDemandParameter> {

    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandDayMaxListServiceDaoImpl commonDemandDayMaxListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;
    private final SmControlLoadListServiceDaoImpl smControlLoadListServiceDaoImpl;
    private final CommonDemandDayLoadListServiceDaoImpl commonDemandDayLoadListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    public DemandOrgYearReportListMaxDemandDao() {
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandDayMaxListServiceDaoImpl = new CommonDemandDayMaxListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
        smControlLoadListServiceDaoImpl = new SmControlLoadListServiceDaoImpl();
        commonDemandDayLoadListServiceDaoImpl = new CommonDemandDayLoadListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgYearReportListMaxDemandResult query(DemandOrgYearReportListMaxDemandParameter parameter)
            throws Exception {

        DemandOrgYearReportListMaxDemandResult result = new DemandOrgYearReportListMaxDemandResult();
        List<DemandCalendarYearData> corpCalList = null;
        List<DemandCalendarYearData> buildingCalList = null;
        Map<String, Date> monthStartMap = null;
        Map<String, Date> monthEndMap = null;
        DemandOrgDayReportListMaxDemandHeaderResultData header = new DemandOrgDayReportListMaxDemandHeaderResultData();
        DemandOrgDayReportListMaxDemandHeaderResultData maxHeader = null;
        List<DemandOrgDayReportListMaxDemandDetailResultData> detailList = new ArrayList<>();
        List<DemandOrgDayReportListMaxDemandDetailResultData> maxDetailList = null;
        DemandOrgDayReportListMaxDemandSummaryAreaResultData summary = new DemandOrgDayReportListMaxDemandSummaryAreaResultData();
        List<CommonDemandDayReportLineListResult> demandDayReportLineList = null;
        List<CommonDemandDayMaxListResult> demandDayMaxList = null;
        List<CommonDemandMonthReportListResult> demandMonthReportList;
        List<DemandBuildingSmPointListDetailResultData> demandBuildingSmPointList;
        BigDecimal summaryControlRate = BigDecimal.ZERO;
        Map<Date, List<CommonDemandDayMaxListResult>> demandDayMaxMap = null;
        BigDecimal averageKwMaxVal = null;
        BigDecimal averageKwMinVal = null;
        BigDecimal averageSummaryVal = null;
        BigDecimal averageSummaryCount = null;
        List<BigDecimal> plotAnalogPointValMaxList = new ArrayList<>();
        List<BigDecimal> plotAnalogPointValMinList = new ArrayList<>();
        List<BigDecimal> plotAnalogPointAverageSummaryValList = new ArrayList<>();
        List<BigDecimal> plotAnalogPointAverageSummaryCountList = new ArrayList<>();
        List<BigDecimal> plotAnalogPointOffsetValList = new ArrayList<>();
        List<BigDecimal> plotAnalogPointConversionValList = new ArrayList<>();
        List<SmPointListDetailResultData> smPointList;
        List<String> graphColoList;
        int i;
        List<ProductControlLoadListDetailResultData> productControlLoadList = null;

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
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgYearReportListMaxDemandResult();
            }
            corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            if (corpCalList == null || corpCalList.isEmpty()) {
                return new DemandOrgYearReportListMaxDemandResult();
            }
            monthStartMap = corpCalList.stream()
                    .collect(Collectors.toMap(x -> x.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                            x -> x.getMonthStartDate()));
            monthEndMap = corpCalList.stream()
                    .collect(Collectors.toMap(x -> x.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                            x -> x.getMonthEndDate()));
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
                //建物デマンド情報を取得する
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
                List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                        buildingDemandListServiceDaoImpl, buildingDemandParam);
                if (buildingDemandList == null || buildingDemandList.size() != 1) {
                    return new DemandOrgYearReportListMaxDemandResult();
                } else {
                    sumDate = buildingDemandList.get(0).getSumDate();
                }
            }

            //建物集計カレンダを取得する
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo, sumDate);
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return new DemandOrgYearReportListMaxDemandResult();
            }
            monthStartMap = buildingCalList.stream()
                    .collect(Collectors.toMap(x -> x.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                            x -> x.getMonthStartDate()));
            monthEndMap = buildingCalList.stream()
                    .collect(Collectors.toMap(x -> x.getYearNo().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                            x -> x.getMonthEndDate()));
        }

        if (monthStartMap == null || monthEndMap == null) {
            return new DemandOrgYearReportListMaxDemandResult();
        }

        //建物機器情報を取得する
        DemandBuildingSmListDetailResultData demandBuildingSmListParam = DemandEmsUtility
                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);
        List<DemandBuildingSmListDetailResultData> demandBuildingSmList = getResultList(
                demandBuildingSmListServiceDaoImpl, demandBuildingSmListParam);

        //集計範囲を取得する
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(),
                parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        result.setDayFrom(monthStartMap.get(summaryRange.getYearFrom()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthFrom().intValue()))));
        result.setDayTo(monthEndMap.get(summaryRange.getYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthTo().intValue()))));

        //集計時刻と計測日時行のMap作成
        Map<BigDecimal, String> rowMap;
        String keyTime;
        int count;
        keyTime = "0000";
        rowMap = new HashMap<>();

        for (count = 1; count <= 48; count++) {
            if (count == 1) {
                //1件目の場合keyTimeの検索が不要
                rowMap.put(new BigDecimal(count), keyTime);
            } else if (count % 2 == 1) {
                //奇数行の場合、keyTimeの1文字目、2文字目を1加算、3文字目、4文字目を"00"に変更
                keyTime = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        Integer.parseInt(keyTime.substring(0, 2)) + 1).concat("00");
                rowMap.put(new BigDecimal(count), keyTime);
            } else {
                //偶数行の場合、keyTimeの3文字目、4文字目を"30"に変更
                keyTime = keyTime.substring(0, 2).concat("30");
                rowMap.put(new BigDecimal(count), keyTime);
            }
        }

        if (demandBuildingSmList != null && demandBuildingSmList.size() >= 2) {
            header.setTargetSmMultiFlg(true);
        } else if (demandBuildingSmList != null && demandBuildingSmList.size() == 1) {
            header.setTargetSmMultiFlg(false);
        } else {
            return new DemandOrgYearReportListMaxDemandResult();
        }

        if (header.isTargetSmMultiFlg()) {
            CommonDemandDayReportLineListResult dayReportLineParam = DemandEmsUtility.getDayReportLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), result.getDayFrom(), BigDecimal.ONE,
                    result.getDayTo(), new BigDecimal("48"));
            demandDayReportLineList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportLineParam);
        } else {
            CommonDemandDayMaxListResult dayMaxParam = DemandEmsUtility.getDemandDayMaxListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), demandBuildingSmList.get(0).getSmId(),
                    result.getDayFrom(), result.getDayTo(), null, null);
            demandDayMaxList = getResultList(commonDemandDayMaxListServiceDaoImpl, dayMaxParam);
        }

        if (header.isTargetSmMultiFlg()) {
            //機器が複数ある場合
            BigDecimal maxDemandValue = null;
            Date maxDemandMeasurementDate = null;
            BigDecimal maxDemandJigenNo = null;

            if (demandDayReportLineList != null && !demandDayReportLineList.isEmpty()) {
                demandDayReportLineList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(
                        demandDayReportLineList, ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
            }

            for (CommonDemandDayReportLineListResult demandDayReportLine : demandDayReportLineList) {
                if (maxDemandValue == null) {
                    maxDemandValue = demandDayReportLine.getLineValueKw();
                    maxDemandMeasurementDate = demandDayReportLine.getMeasurementDate();
                    maxDemandJigenNo = demandDayReportLine.getJigenNo();
                } else if (demandDayReportLine.getLineValueKw().compareTo(maxDemandValue) >= 0) {
                    maxDemandValue = demandDayReportLine.getLineValueKw();
                    maxDemandMeasurementDate = demandDayReportLine.getMeasurementDate();
                    maxDemandJigenNo = demandDayReportLine.getJigenNo();
                }
            }

            //デマンド月報からデータを取得する
            CommonDemandMonthReportListResult monthReportListParam = DemandEmsUtility.getMonthReportListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), maxDemandMeasurementDate,
                    maxDemandMeasurementDate);
            demandMonthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportListParam);

            //結果にデータを設定する
            if (maxDemandMeasurementDate != null) {
                header.setMaxDemandOccurrenceTime(
                        DateUtility.changeDateFormat(
                                DateUtility.conversionDate(
                                        DateUtility
                                                .changeDateFormat(maxDemandMeasurementDate,
                                                        DateUtility.DATE_FORMAT_YYYYMMDD)
                                                .concat(rowMap.get(maxDemandJigenNo)),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
                if (demandMonthReportList == null || demandMonthReportList.size() != 1) {
                    header.setTargetPower(null);
                } else {
                    header.setTargetPower(demandMonthReportList.get(0).getTargetKw().setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
                }
            }

            for (int j = 1; j <= 29; j++) {
                detailList.add(
                        new DemandOrgDayReportListMaxDemandDetailResultData(String.valueOf(j), null, null, null, null));
            }

            if (maxDemandValue == null) {
                detailList.add(new DemandOrgDayReportListMaxDemandDetailResultData("30", null, null, null, null));
            } else {
                detailList
                        .add(new DemandOrgDayReportListMaxDemandDetailResultData("30",
                                maxDemandValue.setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())),
                                null, null, null));
            }

            result.setHeader(header);
            result.setDetailList(detailList);
            result.setSummary(null);
            return result;
        }

        //機器情報を取得
        SmSelectResult smParam = DemandEmsUtility.getSmSelectParam(demandBuildingSmList.get(0).getSmId());
        List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
        if (smList == null || smList.size() != 1) {
            header.setPlotAnalogPointList(null);
        } else {
            header.setPlotAnalogPointList(new ArrayList<>());
            if (CheckUtility.isNullOrEmpty(smList.get(0).getPlotAnalogPointNo1())) {
                header.getPlotAnalogPointList().add(null);
                plotAnalogPointOffsetValList.add(null);
                plotAnalogPointConversionValList.add(null);
            } else {
                DemandBuildingSmPointListDetailResultData buildingSmPointListParam = DemandEmsUtility
                        .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                demandBuildingSmList.get(0).getSmId(), smList.get(0).getPlotAnalogPointNo1());
                demandBuildingSmPointList = getResultList(demandBuildingSmPointListServiceDaoImpl,
                        buildingSmPointListParam);
                if (demandBuildingSmPointList == null || demandBuildingSmPointList.size() != 1) {
                    header.getPlotAnalogPointList().add(null);
                } else {
                    header.getPlotAnalogPointList()
                            .add(new CommonDemandOrgMaxDemandHeaderPlogAnalogResultData(
                                    demandBuildingSmPointList.get(0).getPointName(),
                                    demandBuildingSmPointList.get(0).getPointUnit(), null));
                }
                SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(
                        demandBuildingSmList.get(0).getSmId(), smList.get(0).getPlotAnalogPointNo1());
                smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);
                if (smPointList == null || smPointList.size() != 1) {
                    plotAnalogPointConversionValList.add(null);
                    plotAnalogPointOffsetValList.add(null);
                } else {
                    plotAnalogPointConversionValList.add(smPointList.get(0).getAnalogConversionFactor());
                    plotAnalogPointOffsetValList.add(smPointList.get(0).getAnalogOffSetValue());
                }
            }
            if (CheckUtility.isNullOrEmpty(smList.get(0).getPlotAnalogPointNo2())) {
                header.getPlotAnalogPointList().add(null);
                plotAnalogPointOffsetValList.add(null);
                plotAnalogPointConversionValList.add(null);
            } else {
                DemandBuildingSmPointListDetailResultData buildingSmPointListParam = DemandEmsUtility
                        .getBuildingSmPointListParam(parameter.getOperationCorpId(), parameter.getBuildingId(),
                                demandBuildingSmList.get(0).getSmId(), smList.get(0).getPlotAnalogPointNo2());
                demandBuildingSmPointList = getResultList(demandBuildingSmPointListServiceDaoImpl,
                        buildingSmPointListParam);
                if (demandBuildingSmPointList == null || demandBuildingSmPointList.size() != 1) {
                    header.getPlotAnalogPointList().add(null);
                } else {
                    header.getPlotAnalogPointList()
                            .add(new CommonDemandOrgMaxDemandHeaderPlogAnalogResultData(
                                    demandBuildingSmPointList.get(0).getPointName(),
                                    demandBuildingSmPointList.get(0).getPointUnit(), null));
                }
                SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(
                        demandBuildingSmList.get(0).getSmId(), smList.get(0).getPlotAnalogPointNo2());
                smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);
                if (smPointList == null || smPointList.size() != 1) {
                    plotAnalogPointConversionValList.add(null);
                    plotAnalogPointOffsetValList.add(null);
                } else {
                    plotAnalogPointConversionValList.add(smPointList.get(0).getAnalogConversionFactor());
                    plotAnalogPointOffsetValList.add(smPointList.get(0).getAnalogOffSetValue());
                }
            }
        }

        //製品制御負荷情報を取得
        if (smList != null && smList.size() == 1) {
            ProductControlLoadListDetailResultData productControlParam = DemandEmsUtility
                    .getProductControlLoadListParam(smList.get(0).getProductCd(), null, null);
            productControlLoadList = getResultList(productControlLoadListServiceDaoImpl, productControlParam);
            if (productControlLoadList != null && !productControlLoadList.isEmpty()) {
                productControlLoadList = productControlLoadList.stream().sorted(Comparator
                        .comparing(ProductControlLoadListDetailResultData::getControlLoad, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            }
        }

        //機器制御負荷情報を取得
        SmControlLoadListDetailResultData smControlLoadParam = DemandEmsUtility
                .getSmControlLoadListParam(demandBuildingSmList.get(0).getSmId(), null, null);
        List<SmControlLoadListDetailResultData> smControlLoadList = getResultList(smControlLoadListServiceDaoImpl,
                smControlLoadParam);
        header.setInterruptionGroupList(new ArrayList<>());

        //製品制御負荷をベースに作成する
        if (productControlLoadList != null && !productControlLoadList.isEmpty()) {
            for (ProductControlLoadListDetailResultData product : productControlLoadList) {
                DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData interruptionGroup = new DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData();
                interruptionGroup.setControlLoad(product.getControlLoad());
                interruptionGroup.setControlLoadCircuit(product.getControlLoadCircuit());
                //機器制御負荷の情報は、あれば設定する
                if (smControlLoadList == null || smControlLoadList.isEmpty()) {
                    interruptionGroup.setControlLoadName(null);
                    interruptionGroup.setControlLoadMemo(null);
                    interruptionGroup.setControlRate(null);
                } else {
                    for (SmControlLoadListDetailResultData smControlLoad : smControlLoadList) {
                        if (smControlLoad.getControlLoad().compareTo(product.getControlLoad()) == 0) {
                            interruptionGroup.setControlLoadName(smControlLoad.getControlLoadName());
                            interruptionGroup.setControlLoadMemo(smControlLoad.getControlLoadMemo());
                            if (CheckUtility.isNullOrEmpty(smControlLoad.getControlLoadShutOffCapacity())) {
                                interruptionGroup.setControlRate(null);
                            } else {
                                interruptionGroup
                                        .setControlRate(new BigDecimal(smControlLoad.getControlLoadShutOffCapacity()));
                            }
                            break;
                        }
                    }
                }

                header.getInterruptionGroupList().add(interruptionGroup);

            }
        }

        //制御率を算出する
        for (DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData headerInnterruptionGroupResult : header
                .getInterruptionGroupList()) {

            if (headerInnterruptionGroupResult.getControlRate() != null) {
                summaryControlRate = summaryControlRate.add(headerInnterruptionGroupResult.getControlRate());
            }

        }

        for (DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData headerInnterruptionGroupResult : header
                .getInterruptionGroupList()) {
            if (headerInnterruptionGroupResult.getControlRate() == null
                    || BigDecimal.ZERO.compareTo(headerInnterruptionGroupResult.getControlRate()) == 0) {
                headerInnterruptionGroupResult.setControlRate(BigDecimal.ZERO.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else if (BigDecimal.ZERO.compareTo(summaryControlRate) == 0) {
                headerInnterruptionGroupResult.setControlRate(BigDecimal.ZERO.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                headerInnterruptionGroupResult.setControlRate(headerInnterruptionGroupResult.getControlRate()
                        .multiply(new BigDecimal(100)).divide(summaryControlRate, parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
        }

        if (demandDayMaxList != null && !demandDayMaxList.isEmpty()) {
            //計測年月日、指定プロット行でソートする
            demandDayMaxList = demandDayMaxList.stream().sorted(Comparator
                    .comparing(CommonDemandDayMaxListResult::getMeasurementDate, Comparator.naturalOrder())
                    .thenComparing(
                            Comparator.comparing(CommonDemandDayMaxListResult::getCrntMin, Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            //計測年月日ごとにグルーピングする
            demandDayMaxMap = demandDayMaxList.stream()
                    .collect(Collectors.groupingBy(CommonDemandDayMaxListResult::getMeasurementDate));
        } else {
            return getMaxDemandInfoEmpty(header);
        }

        //グルーピングしたMapの件数分処理を行う
        for (Map.Entry<Date, List<CommonDemandDayMaxListResult>> entry : demandDayMaxMap.entrySet()) {
            DemandOrgDayReportListMaxDemandHeaderResultData tempHeader = new DemandOrgDayReportListMaxDemandHeaderResultData();
            List<DemandOrgDayReportListMaxDemandDetailResultData> tempDetailList = new ArrayList<>();
            List<CommonDemandDayMaxListResult> tempList = entry.getValue();
            BigDecimal beforeCurrentKwVal = BigDecimal.ZERO;
            averageSummaryVal = BigDecimal.ZERO;
            averageSummaryCount = BigDecimal.ZERO;
            for (CommonDemandDayMaxListResult temp : tempList) {
                DemandOrgDayReportListMaxDemandDetailResultData tempDetail = new DemandOrgDayReportListMaxDemandDetailResultData();
                if (tempHeader.getMaxDemandOccurrenceTime() == null) {
                    tempHeader.setMaxDemandOccurrenceTime(DateUtility.changeDateFormat(temp.getKwValUpdateTime(),
                            DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
                }
                tempDetail.setTimeTitle(temp.getCrntMin().toString());
                tempDetail.setCurrentKwVal(temp.getKwVal());
                tempDetail.setPlotAnalogPointValList(new ArrayList<>());
                if (header.getPlotAnalogPointList() != null && !header.getPlotAnalogPointList().isEmpty()) {
                    if (temp.getPlotAnalogPointNo1Val() == null) {
                        tempDetail.getPlotAnalogPointValList().add(temp.getPlotAnalogPointNo1Val());
                    } else if (plotAnalogPointOffsetValList.get(0) == null
                            || plotAnalogPointConversionValList.get(0) == null) {
                        tempDetail.getPlotAnalogPointValList().add(temp.getPlotAnalogPointNo1Val());
                    } else {
                        tempDetail.getPlotAnalogPointValList()
                                .add((temp.getPlotAnalogPointNo1Val().add(plotAnalogPointOffsetValList.get(0))
                                        .multiply(plotAnalogPointConversionValList.get(0))));
                    }

                    if (temp.getPlotAnalogPointNo2Val() == null) {
                        tempDetail.getPlotAnalogPointValList().add(temp.getPlotAnalogPointNo2Val());
                    } else if (plotAnalogPointOffsetValList.get(1) == null
                            || plotAnalogPointConversionValList.get(1) == null) {
                        tempDetail.getPlotAnalogPointValList().add(temp.getPlotAnalogPointNo2Val());
                    } else {
                        tempDetail.getPlotAnalogPointValList()
                                .add((temp.getPlotAnalogPointNo2Val().add(plotAnalogPointOffsetValList.get(1))
                                        .multiply(plotAnalogPointConversionValList.get(1))));
                    }
                }
                tempDetail.setControlStatusList(new ArrayList<>());
                tempDetailList.add(tempDetail);
            }
            //平均電力を算出する
            for (DemandOrgDayReportListMaxDemandDetailResultData tempDetail : tempDetailList) {
                tempDetail.setAverageKwVal(
                        (tempDetail.getCurrentKwVal().subtract(beforeCurrentKwVal)).multiply(new BigDecimal(30)));
                beforeCurrentKwVal = tempDetail.getCurrentKwVal();
                averageSummaryVal = averageSummaryVal.add(tempDetail.getAverageKwVal());
                averageSummaryCount = averageSummaryCount.add(BigDecimal.ONE);
            }

            //遮断グループ情報を設定する
            for (DemandOrgDayReportListMaxDemandHeaderInterruptionGroupResultData headerInterruptionGroupResult : header
                    .getInterruptionGroupList()) {
                //デマンド日負荷からデータを取得する
                CommonDemandDayLoadListResult demandDayLoadParam = DemandEmsUtility.getDemandDayLoadListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(),
                        demandBuildingSmList.get(0).getSmId(), entry.getKey(), entry.getKey(), null, null,
                        headerInterruptionGroupResult.getControlLoad(), headerInterruptionGroupResult.getControlLoad());
                List<CommonDemandDayLoadListResult> demandDayLoadList = getResultList(
                        commonDemandDayLoadListServiceDaoImpl, demandDayLoadParam);
                if (demandDayLoadList != null && !demandDayLoadList.isEmpty()) {
                    for (CommonDemandDayLoadListResult demandDayLoad : demandDayLoadList) {
                        for (DemandOrgDayReportListMaxDemandDetailResultData tempDetail : tempDetailList) {
                            if (demandDayLoad.getCrntMin().compareTo(new BigDecimal(tempDetail.getTimeTitle())) == 0) {
                                if ("0".equals(demandDayLoad.getContactOutStatus())) {
                                    tempDetail.getControlStatusList().add(false);
                                } else if ("1".equals(demandDayLoad.getContactOutStatus())) {
                                    tempDetail.getControlStatusList().add(true);
                                } else {
                                    tempDetail.getControlStatusList().add(null);
                                }
                            }
                        }
                    }
                }
            }

            //無制御時平均電力を算出する
            BigDecimal summaryAverageKw = BigDecimal.ZERO;
            BigDecimal summaryAverageCount = BigDecimal.ZERO;
            for (DemandOrgDayReportListMaxDemandDetailResultData tempDetail : tempDetailList) {
                boolean flg = true;
                for (Boolean controlStatus : tempDetail.getControlStatusList()) {
                    if (controlStatus == null || controlStatus == true) {
                        flg = false;
                        break;
                    }
                }
                if (flg) {
                    //無制御状態
                    summaryAverageKw = summaryAverageKw.add(tempDetail.getAverageKwVal());
                    summaryAverageCount = summaryAverageCount.add(BigDecimal.ONE);
                }
            }

            if (summaryAverageKw.compareTo(BigDecimal.ZERO) == 0
                    || summaryAverageCount.compareTo(BigDecimal.ZERO) == 0) {
                tempHeader.setAveragePowerWithoutControl(BigDecimal.ZERO);
            } else {
                tempHeader.setAveragePowerWithoutControl(
                        summaryAverageKw.divide(summaryAverageCount, 10, BigDecimal.ROUND_HALF_UP));
            }

            //平均電力の平均値を算出する
            if (averageSummaryCount.compareTo(BigDecimal.ZERO) == 0
                    || averageSummaryVal.compareTo(BigDecimal.ZERO) == 0) {
                tempHeader.setAveragePowerAverage(BigDecimal.ZERO);
            } else {
                tempHeader.setAveragePowerAverage(
                        averageSummaryVal.divide(averageSummaryCount, 10, RoundingMode.HALF_UP));
            }

            //現在保持中の平均電力の平均値比較
            if (maxHeader == null) {
                //初回はそのまま保持
                maxHeader = tempHeader;
                maxDetailList = tempDetailList;
            } else if (maxHeader.getAveragePowerAverage()
                    .compareTo(tempHeader.getAveragePowerAverage()) > 0) {
                //保持中が大きい場合は次の処理へ
                continue;
            } else if (tempHeader.getAveragePowerAverage()
                    .compareTo(maxHeader.getAveragePowerAverage()) > 0) {
                //一時保存が大きい場合は最大を置き換える
                maxHeader = tempHeader;
                maxDetailList = tempDetailList;
            } else if (tempHeader.getAveragePowerAverage()
                    .compareTo(maxHeader.getAveragePowerAverage()) == 0) {
                //同じ場合は無制御時平均電力で比較
                if (maxHeader.getAveragePowerWithoutControl()
                        .compareTo(tempHeader.getAveragePowerWithoutControl()) > 0) {
                    //保持中が大きい場合は次の処理へ
                    continue;
                } else if (tempHeader.getAveragePowerWithoutControl()
                        .compareTo(maxHeader.getAveragePowerWithoutControl()) > 0) {
                    //一時保存が大きい場合は最大を置き換える
                    maxHeader = tempHeader;
                    maxDetailList = tempDetailList;
                } else if (tempHeader.getAveragePowerWithoutControl()
                        .compareTo(maxHeader.getAveragePowerWithoutControl()) == 0) {
                    //同じ場合は未来日を適用
                    if (DateUtility
                            .conversionDate(tempHeader.getMaxDemandOccurrenceTime(),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH)
                            .after(DateUtility.conversionDate(maxHeader.getMaxDemandOccurrenceTime(),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH))) {
                        maxHeader = tempHeader;
                        maxDetailList = tempDetailList;
                    } else {
                        continue;
                    }
                }

            }
        }

        //保持した最大情報をresultにセットする
        if (maxHeader != null) {
            header.setMaxDemandOccurrenceTime(maxHeader.getMaxDemandOccurrenceTime());
            if (maxHeader.getAveragePowerWithoutControl() != null) {
                header.setAveragePowerWithoutControl(maxHeader.getAveragePowerWithoutControl().setScale(
                        parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                header.setAveragePowerWithoutControl(null);
            }
        }
        if (maxDetailList != null && !maxDetailList.isEmpty()) {
            for (DemandOrgDayReportListMaxDemandDetailResultData maxDetail : maxDetailList) {
                DemandOrgDayReportListMaxDemandDetailResultData detail = new DemandOrgDayReportListMaxDemandDetailResultData();
                detail.setTimeTitle(maxDetail.getTimeTitle());
                if (maxDetail.getCurrentKwVal() != null) {
                    detail.setCurrentKwVal(maxDetail.getCurrentKwVal().setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
                } else {
                    detail.setCurrentKwVal(null);
                }
                if (maxDetail.getAverageKwVal() != null) {
                    detail.setAverageKwVal(maxDetail.getAverageKwVal().setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
                } else {
                    detail.setAverageKwVal(null);
                }
                detail.setPlotAnalogPointValList(new ArrayList<>());
                if (maxDetail.getPlotAnalogPointValList() == null || maxDetail.getPlotAnalogPointValList().isEmpty()) {
                    detail.getPlotAnalogPointValList().add(0, null);
                } else if (maxDetail.getPlotAnalogPointValList().get(0) != null) {
                    detail.getPlotAnalogPointValList().add(0, maxDetail.getPlotAnalogPointValList().get(0)
                            .setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
                } else {
                    detail.getPlotAnalogPointValList().add(0, null);
                }
                if (maxDetail.getPlotAnalogPointValList() == null || maxDetail.getPlotAnalogPointValList().isEmpty()) {
                    detail.getPlotAnalogPointValList().add(1, null);
                } else if (maxDetail.getPlotAnalogPointValList().get(1) != null) {
                    detail.getPlotAnalogPointValList().add(1, maxDetail.getPlotAnalogPointValList().get(1)
                            .setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
                } else {
                    detail.getPlotAnalogPointValList().add(1, null);
                }
                if (maxDetail.getControlStatusList() == null) {
                    detail.setControlStatusList(null);
                } else {
                    detail.setControlStatusList(maxDetail.getControlStatusList());
                }

                detailList.add(detail);

            }
        } else {
            detailList = null;
        }

        //遮断電力を算出する
        if (detailList == null || detailList.isEmpty() || header.getAveragePowerWithoutControl() == null
                || detailList.get(detailList.size() - 1).getCurrentKwVal() == null) {
            header.setInterruptionPower(null);
        } else {
            header.setInterruptionPower(header.getAveragePowerWithoutControl()
                    .subtract(detailList.get(detailList.size() - 1).getCurrentKwVal())
                    .setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }

        //サマリ値を算出する
        plotAnalogPointValMaxList.add(0, null);
        plotAnalogPointValMaxList.add(1, null);
        plotAnalogPointValMinList.add(0, null);
        plotAnalogPointValMinList.add(1, null);
        plotAnalogPointAverageSummaryValList.add(0, null);
        plotAnalogPointAverageSummaryValList.add(1, null);
        plotAnalogPointAverageSummaryCountList.add(0, null);
        plotAnalogPointAverageSummaryCountList.add(1, null);
        averageSummaryVal = null;
        averageSummaryCount = null;
        for (DemandOrgDayReportListMaxDemandDetailResultData detail : detailList) {

            //平均電力用のサマリ
            if (detail.getAverageKwVal() != null) {
                //平均電力最大値
                if (averageKwMaxVal == null) {
                    averageKwMaxVal = detail.getAverageKwVal();
                } else if (detail.getAverageKwVal().compareTo(averageKwMaxVal) >= 0) {
                    averageKwMaxVal = detail.getAverageKwVal();
                }
                //平均電力最小値
                if (averageKwMinVal == null) {
                    averageKwMinVal = detail.getAverageKwVal();
                } else if (detail.getAverageKwVal().compareTo(averageKwMinVal) <= 0) {
                    averageKwMinVal = detail.getAverageKwVal();
                }
                //平均電力平均値用のサマリ
                if (averageSummaryVal == null) {
                    averageSummaryVal = detail.getAverageKwVal();
                    averageSummaryCount = BigDecimal.ONE;
                } else {
                    averageSummaryVal = averageSummaryVal.add(detail.getAverageKwVal());
                    averageSummaryCount = averageSummaryCount.add(BigDecimal.ONE);
                }
            }

            //プロットアナログ1用のサマリ
            if (detail.getPlotAnalogPointValList().get(0) != null) {
                //最大値
                if (plotAnalogPointValMaxList.get(0) == null) {
                    plotAnalogPointValMaxList.set(0, detail.getPlotAnalogPointValList().get(0));
                } else if (detail.getPlotAnalogPointValList().get(0).compareTo(plotAnalogPointValMaxList.get(0)) >= 0) {
                    plotAnalogPointValMaxList.set(0, detail.getPlotAnalogPointValList().get(0));
                }
                //最小値
                if (plotAnalogPointValMinList.get(0) == null) {
                    plotAnalogPointValMinList.set(0, detail.getPlotAnalogPointValList().get(0));
                } else if (detail.getPlotAnalogPointValList().get(0).compareTo(plotAnalogPointValMinList.get(0)) <= 0) {
                    plotAnalogPointValMinList.set(0, detail.getPlotAnalogPointValList().get(0));
                }
                //平均値用のサマリ
                if (plotAnalogPointAverageSummaryValList.get(0) == null) {
                    plotAnalogPointAverageSummaryValList.set(0, detail.getPlotAnalogPointValList().get(0));
                    plotAnalogPointAverageSummaryCountList.set(0, BigDecimal.ONE);
                } else {
                    plotAnalogPointAverageSummaryValList.set(0,
                            plotAnalogPointAverageSummaryValList.get(0).add(detail.getPlotAnalogPointValList().get(0)));
                    plotAnalogPointAverageSummaryCountList.set(0,
                            plotAnalogPointAverageSummaryCountList.get(0).add(BigDecimal.ONE));
                }
            }

            //プロットアナログ2用のサマリ
            if (detail.getPlotAnalogPointValList().get(1) != null) {
                //最大値
                if (plotAnalogPointValMaxList.get(1) == null) {
                    plotAnalogPointValMaxList.set(1, detail.getPlotAnalogPointValList().get(1));
                } else if (detail.getPlotAnalogPointValList().get(1).compareTo(plotAnalogPointValMaxList.get(1)) >= 0) {
                    plotAnalogPointValMaxList.set(1, detail.getPlotAnalogPointValList().get(1));
                }
                //最小値
                if (plotAnalogPointValMinList.get(1) == null) {
                    plotAnalogPointValMinList.set(1, detail.getPlotAnalogPointValList().get(1));
                } else if (detail.getPlotAnalogPointValList().get(1).compareTo(plotAnalogPointValMinList.get(1)) <= 0) {
                    plotAnalogPointValMinList.set(1, detail.getPlotAnalogPointValList().get(1));
                }
                //平均値用のサマリ
                if (plotAnalogPointAverageSummaryValList.get(1) == null) {
                    plotAnalogPointAverageSummaryValList.set(1, detail.getPlotAnalogPointValList().get(1));
                    plotAnalogPointAverageSummaryCountList.set(1, BigDecimal.ONE);
                } else {
                    plotAnalogPointAverageSummaryValList.set(1,
                            plotAnalogPointAverageSummaryValList.get(1).add(detail.getPlotAnalogPointValList().get(1)));
                    plotAnalogPointAverageSummaryCountList.set(1,
                            plotAnalogPointAverageSummaryCountList.get(1).add(BigDecimal.ONE));
                }
            }

        }

        if (averageKwMaxVal != null) {
            summary.setAverageKwMaxVal(
                    averageKwMaxVal.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.setAverageKwMaxVal(null);
        }
        if (averageKwMinVal != null) {
            summary.setAverageKwMinVal(
                    averageKwMinVal.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.setAverageKwMinVal(null);
        }
        if (averageSummaryVal == null || averageSummaryCount == null
                || averageSummaryVal.compareTo(BigDecimal.ZERO) == 0
                || averageSummaryCount.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageKwAverageVal(null);
        } else {
            summary.setAverageKwAverageVal(averageSummaryVal.divide(averageSummaryCount, parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }
        summary.setPlotAnalogPointValMaxList(new ArrayList<>());
        if (plotAnalogPointValMaxList.get(0) != null) {
            summary.getPlotAnalogPointValMaxList().add(0, plotAnalogPointValMaxList.get(0).setScale(
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.getPlotAnalogPointValMaxList().add(0, null);
        }
        if (plotAnalogPointValMaxList.get(1) != null) {
            summary.getPlotAnalogPointValMaxList().add(1, plotAnalogPointValMaxList.get(1).setScale(
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.getPlotAnalogPointValMaxList().add(1, null);
        }
        summary.setPlotAnalogPointValMinList(new ArrayList<>());
        if (plotAnalogPointValMinList.get(0) != null) {
            summary.getPlotAnalogPointValMinList().add(0, plotAnalogPointValMinList.get(0).setScale(
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.getPlotAnalogPointValMinList().add(0, null);
        }
        if (plotAnalogPointValMinList.get(1) != null) {
            summary.getPlotAnalogPointValMinList().add(1, plotAnalogPointValMinList.get(1).setScale(
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        } else {
            summary.getPlotAnalogPointValMinList().add(1, null);
        }
        summary.setPlotAnalogPointValAverageList(new ArrayList<>());
        if (plotAnalogPointAverageSummaryValList.get(0) == null || plotAnalogPointAverageSummaryCountList.get(0) == null
                || plotAnalogPointAverageSummaryValList.get(0).compareTo(BigDecimal.ZERO) == 0
                || plotAnalogPointAverageSummaryCountList.get(0).compareTo(BigDecimal.ZERO) == 0) {
            summary.getPlotAnalogPointValAverageList().add(0, null);
        } else {
            summary.getPlotAnalogPointValAverageList().add(0,
                    plotAnalogPointAverageSummaryValList.get(0).divide(plotAnalogPointAverageSummaryCountList.get(0),
                            parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (plotAnalogPointAverageSummaryValList.get(1) == null || plotAnalogPointAverageSummaryCountList.get(1) == null
                || plotAnalogPointAverageSummaryValList.get(1).compareTo(BigDecimal.ZERO) == 0
                || plotAnalogPointAverageSummaryCountList.get(1).compareTo(BigDecimal.ZERO) == 0) {
            summary.getPlotAnalogPointValAverageList().add(1, null);
        } else {
            summary.getPlotAnalogPointValAverageList().add(1,
                    plotAnalogPointAverageSummaryValList.get(1).divide(plotAnalogPointAverageSummaryCountList.get(1),
                            parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        //目標電力を取得する
        CommonDemandMonthReportListResult monthReportListParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), DateUtility.conversionDate(
                        DateUtility.changeDateFormat(DateUtility.conversionDate(header.getMaxDemandOccurrenceTime(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH), DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.conversionDate(
                        DateUtility.changeDateFormat(DateUtility.conversionDate(header.getMaxDemandOccurrenceTime(),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH), DateUtility.DATE_FORMAT_YYYYMMDD),
                        DateUtility.DATE_FORMAT_YYYYMMDD));
        demandMonthReportList = getResultList(commonDemandMonthReportListServiceDaoImpl, monthReportListParam);

        if (demandMonthReportList == null || demandMonthReportList.size() != 1) {
            header.setTargetPower(null);
        } else if (demandMonthReportList.get(0).getTargetKw() == null) {
            header.setTargetPower(null);
        } else {
            header.setTargetPower(demandMonthReportList.get(0).getTargetKw().setScale(parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        //グラフ色を取得する
        if (header.getPlotAnalogPointList() != null) {
            graphColoList = DemandGraphAutoColorUtility
                    .getGraphAutoColorList(header.getPlotAnalogPointList().size() + 1);
        } else {
            graphColoList = DemandGraphAutoColorUtility.getGraphAutoColorList(1);
        }

        header.setAverageKwValGraphColor(graphColoList.get(0));

        i = 1;
        for (CommonDemandOrgMaxDemandHeaderPlogAnalogResultData plotAnalog : header.getPlotAnalogPointList()) {
            if (plotAnalog != null) {
                plotAnalog.setGraphColor(graphColoList.get(i));
            }
            i++;
        }

        result.setHeader(header);
        result.setDetailList(detailList);
        result.setSummary(summary);
        return result;

    }

    /**
     * 空データ
     *
     * @param header
     * @return
     */
    private DemandOrgYearReportListMaxDemandResult getMaxDemandInfoEmpty(
            DemandOrgDayReportListMaxDemandHeaderResultData header) {
        DemandOrgYearReportListMaxDemandResult ret = new DemandOrgYearReportListMaxDemandResult();
        ret.setHeader(header);
        List<DemandOrgDayReportListMaxDemandDetailResultData> detailList = new ArrayList<>();
        for (int j = 1; j <= 30; j++) {
            detailList.add(
                    new DemandOrgDayReportListMaxDemandDetailResultData(String.valueOf(j), null, null, null, null));
        }
        ret.setDetailList(detailList);

        return ret;
    }

}
