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
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgYearReportListTimeZoneUsedParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgYearReportListTimeZoneUsedResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListTimeZoneUsedDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListTimeZoneUsedHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgYearReportListTimeZoneUsedSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * エネルギー使用状況実績取得（個別・年報・時間帯別使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgYearReportListTimeZoneUsedDao extends OsolApiDao<DemandOrgYearReportListTimeZoneUsedParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandYearReportListServiceDaoImpl commonDemandYearReportListServiceDaoImpl;

    public DemandOrgYearReportListTimeZoneUsedDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandYearReportListServiceDaoImpl = new CommonDemandYearReportListServiceDaoImpl();
    }

    @Override
    public DemandOrgYearReportListTimeZoneUsedResult query(DemandOrgYearReportListTimeZoneUsedParameter parameter)
            throws Exception {
        DemandOrgYearReportListTimeZoneUsedResult result = new DemandOrgYearReportListTimeZoneUsedResult();
        DemandOrgYearReportListTimeZoneUsedHeaderResultData header = new DemandOrgYearReportListTimeZoneUsedHeaderResultData();
        List<DemandOrgYearReportListTimeZoneUsedDetailResultData> detailList = new ArrayList<>();
        DemandOrgYearReportListTimeZoneUsedSummaryAreaResultData summary = new DemandOrgYearReportListTimeZoneUsedSummaryAreaResultData();
        List<DemandCalendarYearData> corpCalList = null;
        List<DemandCalendarYearData> buildingCalList = null;
        CurrentYearReportResult currentYearReport = null;
        Map<String, Date> monthStartMap = null;
        String currentYm = "";
        BigDecimal summaryOpenTime = null;
        BigDecimal summaryOpenPreparation = null;
        BigDecimal summaryClosePreparation = null;
        BigDecimal summaryCloseTime = null;
        BigDecimal maxOpenTime = null;
        BigDecimal maxOpenPreparation = null;
        BigDecimal maxClosePreparation = null;
        BigDecimal maxCloseTime = null;
        BigDecimal minOpenTime = null;
        BigDecimal minOpenPreparation = null;
        BigDecimal minClosePreparation = null;
        BigDecimal minCloseTime = null;
        BigDecimal summaryAverageOpenTime = null;
        BigDecimal summaryAverageOpenPreparation = null;
        BigDecimal summaryAverageClosePreparation = null;
        BigDecimal summaryAverageCloseTime = null;
        BigDecimal summaryAverageCountOpenTime = null;
        BigDecimal summaryAverageCountOpenPreparation = null;
        BigDecimal summaryAverageCountClosePreparation = null;
        BigDecimal summaryAverageCountCloseTime = null;
        List<CorpDemandSelectResult> corpDemandList = null;
        List<BuildingDemandListDetailResultData> buildingDemandList = null;

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
                return new DemandOrgYearReportListTimeZoneUsedResult();
            }
            corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            if (corpCalList == null || corpCalList.isEmpty()) {
                return new DemandOrgYearReportListTimeZoneUsedResult();
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                return new DemandOrgYearReportListTimeZoneUsedResult();
            }
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    buildingDemandList.get(0).getSumDate());
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return new DemandOrgYearReportListTimeZoneUsedResult();
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

        //集計範囲を取得する
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(),
                parameter.getMonth(), parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダ情報を設定する
        header.setYearFrom(summaryRange.getYearFrom());
        header.setYearTo(summaryRange.getYearTo());
        header.setMonthFrom(summaryRange.getMonthFrom());
        header.setMonthTo(summaryRange.getMonthTo());

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

        do {

            DemandOrgYearReportListTimeZoneUsedDetailResultData detail = new DemandOrgYearReportListTimeZoneUsedDetailResultData();
            if (CheckUtility.isNullOrEmpty(currentYm)) {
                currentYm = summaryRange.getYearFrom()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getMonthFrom().intValue()));
            } else if (currentYm.substring(4).equals("12")) {
                //12月の場合年を1年足して1月に
                currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
            } else {
                //1ヶ月足す
                currentYm = currentYm.substring(0, 4)
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                Integer.parseInt(currentYm.substring(4)) + 1));
            }

            detail.setYm(
                    DateUtility.changeDateFormat(DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                            DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            detail.setMonthStartDate(
                    DateUtility.changeDateFormat(monthStartMap.get(currentYm), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
            detail.setOpenTimeKwh(null);
            detail.setOpenPreparationKwh(null);
            detail.setClosePreparationKwh(null);
            detail.setCloseTimeKwh(null);

            detailList.add(detail);

        } while (!currentYm.equals(summaryRange.getYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthTo().intValue()))));

        //デマンド年報データを取得する
        CommonDemandYearReportListResult demandYearReportParam = DemandEmsUtility.getYearReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                summaryRange.getYearFrom(), summaryRange.getYearTo(), summaryRange.getMonthFrom(),
                summaryRange.getMonthTo());
        List<CommonDemandYearReportListResult> demandYearReportList = getResultList(
                commonDemandYearReportListServiceDaoImpl, demandYearReportParam);

        //年No、月Noでソートする
        if (demandYearReportList != null && !demandYearReportList.isEmpty()) {
            demandYearReportList = SortUtility.sortCommonDemandYearReportListByYearNoMonthNo(demandYearReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandYearReportListResult demandYearReport : demandYearReportList) {
            for (DemandOrgYearReportListTimeZoneUsedDetailResultData detail : detailList) {
                if (demandYearReport.getYearNo()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                demandYearReport.getMonthNo().intValue()))
                        .equals(detail.getYm().replace(ApiSimpleConstants.SLASH, ""))) {

                    //開店時間
                    if (demandYearReport.getOpenTimeKwh() == null) {
                        detail.setOpenTimeKwh(null);
                    } else {
                        detail.setOpenTimeKwh(demandYearReport.getOpenTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandYearReport.getOpenTimeKwh() != null) {
                        if (summaryOpenTime == null) {
                            summaryOpenTime = demandYearReport.getOpenTimeKwh();
                        } else {
                            summaryOpenTime = summaryOpenTime.add(demandYearReport.getOpenTimeKwh());
                        }
                        if (maxOpenTime == null) {
                            maxOpenTime = demandYearReport.getOpenTimeKwh();
                        } else if (demandYearReport.getOpenTimeKwh().compareTo(maxOpenTime) >= 0) {
                            maxOpenTime = demandYearReport.getOpenTimeKwh();
                        }

                        if (currentYearReport != null && Integer.parseInt(currentYearReport.getCalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentYearReport.getMonthNo().intValue()))) > Integer
                                                .parseInt(demandYearReport.getYearNo()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandYearReport.getMonthNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minOpenTime == null) {
                                minOpenTime = demandYearReport.getOpenTimeKwh();
                            } else if (demandYearReport.getOpenTimeKwh().compareTo(minOpenTime) <= 0) {
                                minOpenTime = demandYearReport.getOpenTimeKwh();
                            }
                            if (summaryAverageCountOpenTime == null) {
                                summaryAverageCountOpenTime = BigDecimal.ONE;
                            } else {
                                summaryAverageCountOpenTime = summaryAverageCountOpenTime.add(BigDecimal.ONE);
                            }
                            if (summaryAverageOpenTime == null) {
                                summaryAverageOpenTime = demandYearReport.getOpenTimeKwh();
                            } else {
                                summaryAverageOpenTime = summaryAverageOpenTime.add(demandYearReport.getOpenTimeKwh());
                            }
                        }
                    }
                    //開店準備時間
                    if (demandYearReport.getOpenPreparationKwh() == null) {
                        detail.setOpenPreparationKwh(null);
                    } else {
                        detail.setOpenPreparationKwh(
                                demandYearReport.getOpenPreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandYearReport.getOpenPreparationKwh() != null) {
                        if (summaryOpenPreparation == null) {
                            summaryOpenPreparation = demandYearReport.getOpenPreparationKwh();
                        } else {
                            summaryOpenPreparation = summaryOpenPreparation
                                    .add(demandYearReport.getOpenPreparationKwh());
                        }
                        if (maxOpenPreparation == null) {
                            maxOpenPreparation = demandYearReport.getOpenPreparationKwh();
                        } else if (demandYearReport.getOpenPreparationKwh().compareTo(maxOpenPreparation) >= 0) {
                            maxOpenPreparation = demandYearReport.getOpenPreparationKwh();
                        }

                        if (currentYearReport != null && Integer.parseInt(currentYearReport.getCalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentYearReport.getMonthNo().intValue()))) > Integer
                                                .parseInt(demandYearReport.getYearNo()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandYearReport.getMonthNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minOpenPreparation == null) {
                                minOpenPreparation = demandYearReport.getOpenPreparationKwh();
                            } else if (demandYearReport.getOpenPreparationKwh().compareTo(minOpenPreparation) <= 0) {
                                minOpenPreparation = demandYearReport.getOpenPreparationKwh();
                            }
                            if (summaryAverageCountOpenPreparation == null) {
                                summaryAverageCountOpenPreparation = BigDecimal.ONE;
                            } else {
                                summaryAverageCountOpenPreparation = summaryAverageCountOpenPreparation
                                        .add(BigDecimal.ONE);
                            }
                            if (summaryAverageOpenPreparation == null) {
                                summaryAverageOpenPreparation = demandYearReport.getOpenPreparationKwh();
                            } else {
                                summaryAverageOpenPreparation = summaryAverageOpenPreparation
                                        .add(demandYearReport.getOpenPreparationKwh());
                            }
                        }
                    }

                    //閉店準備時間
                    if (demandYearReport.getClosePreparationKwh() == null) {
                        detail.setClosePreparationKwh(null);
                    } else {
                        detail.setClosePreparationKwh(
                                demandYearReport.getClosePreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandYearReport.getClosePreparationKwh() != null) {
                        if (summaryClosePreparation == null) {
                            summaryClosePreparation = demandYearReport.getClosePreparationKwh();
                        } else {
                            summaryClosePreparation = summaryClosePreparation
                                    .add(demandYearReport.getClosePreparationKwh());
                        }
                        if (maxClosePreparation == null) {
                            maxClosePreparation = demandYearReport.getClosePreparationKwh();
                        } else if (demandYearReport.getClosePreparationKwh().compareTo(maxClosePreparation) >= 0) {
                            maxClosePreparation = demandYearReport.getClosePreparationKwh();
                        }
                        if (currentYearReport != null && Integer.parseInt(currentYearReport.getCalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentYearReport.getMonthNo().intValue()))) > Integer
                                                .parseInt(demandYearReport.getYearNo()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandYearReport.getMonthNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minClosePreparation == null) {
                                minClosePreparation = demandYearReport.getClosePreparationKwh();
                            } else if (demandYearReport.getClosePreparationKwh().compareTo(minClosePreparation) <= 0) {
                                minClosePreparation = demandYearReport.getClosePreparationKwh();
                            }
                            if (summaryAverageCountClosePreparation == null) {
                                summaryAverageCountClosePreparation = BigDecimal.ONE;
                            } else {
                                summaryAverageCountClosePreparation = summaryAverageCountClosePreparation
                                        .add(BigDecimal.ONE);
                            }
                            if (summaryAverageClosePreparation == null) {
                                summaryAverageClosePreparation = demandYearReport.getClosePreparationKwh();
                            } else {
                                summaryAverageClosePreparation = summaryAverageClosePreparation
                                        .add(demandYearReport.getClosePreparationKwh());
                            }
                        }
                    }

                    //閉店時間
                    if (demandYearReport.getCloseTimeKwh() == null) {
                        detail.setCloseTimeKwh(null);
                    } else {
                        detail.setCloseTimeKwh(demandYearReport.getCloseTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandYearReport.getCloseTimeKwh() != null) {
                        if (summaryCloseTime == null) {
                            summaryCloseTime = demandYearReport.getCloseTimeKwh();
                        } else {
                            summaryCloseTime = summaryCloseTime.add(demandYearReport.getCloseTimeKwh());
                        }
                        if (maxCloseTime == null) {
                            maxCloseTime = demandYearReport.getCloseTimeKwh();
                        } else if (demandYearReport.getCloseTimeKwh().compareTo(maxCloseTime) >= 0) {
                            maxCloseTime = demandYearReport.getCloseTimeKwh();
                        }
                        if (currentYearReport != null && Integer.parseInt(currentYearReport.getCalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentYearReport.getMonthNo().intValue()))) > Integer
                                                .parseInt(demandYearReport.getYearNo()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandYearReport.getMonthNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minCloseTime == null) {
                                minCloseTime = demandYearReport.getCloseTimeKwh();
                            } else if (demandYearReport.getCloseTimeKwh().compareTo(minCloseTime) <= 0) {
                                minCloseTime = demandYearReport.getCloseTimeKwh();
                            }
                            if (summaryAverageCountCloseTime == null) {
                                summaryAverageCountCloseTime = BigDecimal.ONE;
                            } else {
                                summaryAverageCountCloseTime = summaryAverageCountCloseTime.add(BigDecimal.ONE);
                            }
                            if (summaryAverageCloseTime == null) {
                                summaryAverageCloseTime = demandYearReport.getCloseTimeKwh();
                            } else {
                                summaryAverageCloseTime = summaryAverageCloseTime
                                        .add(demandYearReport.getCloseTimeKwh());
                            }
                        }
                    }

                }
            }
        }

        //サマリ値を設定する
        if (summaryOpenTime == null) {
            summary.setSummaryOpenTime(null);
        } else {
            summary.setSummaryOpenTime(
                    summaryOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryOpenPreparation == null) {
            summary.setSummaryOpenPreparation(null);
        } else {
            summary.setSummaryOpenPreparation(
                    summaryOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryClosePreparation == null) {
            summary.setSummaryClosePreparation(null);
        } else {
            summary.setSummaryClosePreparation(
                    summaryClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryCloseTime == null) {
            summary.setSummaryCloseTime(null);
        } else {
            summary.setSummaryCloseTime(
                    summaryCloseTime.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxOpenTime == null) {
            summary.setMaxOpenTime(null);
        } else {
            summary.setMaxOpenTime(
                    maxOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxOpenPreparation == null) {
            summary.setMaxOpenPreparation(null);
        } else {
            summary.setMaxOpenPreparation(
                    maxOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxClosePreparation == null) {
            summary.setMaxClosePreparation(null);
        } else {
            summary.setMaxClosePreparation(
                    maxClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxCloseTime == null) {
            summary.setMaxCloseTime(null);
        } else {
            summary.setMaxCloseTime(
                    maxCloseTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minOpenTime == null) {
            summary.setMinOpenTime(null);
        } else {
            summary.setMinOpenTime(
                    minOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minOpenPreparation == null) {
            summary.setMinOpenPreparation(null);
        } else {
            summary.setMinOpenPreparation(
                    minOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minCloseTime == null) {
            summary.setMinCloseTime(null);
        } else {
            summary.setMinCloseTime(
                    minCloseTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minClosePreparation == null) {
            summary.setMinClosePreparation(null);
        } else {
            summary.setMinClosePreparation(
                    minClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountOpenTime == null || summaryAverageOpenTime == null
                || summaryAverageOpenTime.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageOpenTime(null);
        } else {
            summary.setAverageOpenTime(summaryAverageOpenTime.divide(summaryAverageCountOpenTime,
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountOpenPreparation == null || summaryAverageOpenPreparation == null
                || summaryAverageOpenPreparation.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageOpenPreparation(null);
        } else {
            summary.setAverageOpenPreparation(
                    summaryAverageOpenPreparation.divide(summaryAverageCountOpenPreparation, parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountClosePreparation == null || summaryAverageClosePreparation == null
                || summaryAverageClosePreparation.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageClosePreparation(null);
        } else {
            summary.setAverageClosePreparation(
                    summaryAverageClosePreparation.divide(summaryAverageCountClosePreparation, parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountCloseTime == null || summaryAverageCloseTime == null
                || summaryAverageCloseTime.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageCloseTime(null);
        } else {
            summary.setAverageCloseTime(summaryAverageCloseTime.divide(summaryAverageCountCloseTime,
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        BigDecimal totalSummary = BigDecimal.ZERO;
        if (summary.getSummaryOpenTime() != null) {
            totalSummary = totalSummary.add(summary.getSummaryOpenTime());
        }
        if (summary.getSummaryOpenPreparation() != null) {
            totalSummary = totalSummary.add(summary.getSummaryOpenPreparation());
        }
        if (summary.getSummaryClosePreparation() != null) {
            totalSummary = totalSummary.add(summary.getSummaryClosePreparation());
        }
        if (summary.getSummaryCloseTime() != null) {
            totalSummary = totalSummary.add(summary.getSummaryCloseTime());
        }

        if (totalSummary.compareTo(BigDecimal.ZERO) == 0) {
            summary.setSummaryRateOpenTime(null);
            summary.setSummaryRateOpenPreparation(null);
            summary.setSummaryRateClosePreparation(null);
            summary.setSummaryRateCloseTime(null);
        } else {
            if (summary.getSummaryOpenTime() == null || summary.getSummaryOpenTime().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateOpenTime(null);
            } else {
                summary.setSummaryRateOpenTime(
                        summary.getSummaryOpenTime().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryOpenPreparation() == null
                    || summary.getSummaryOpenPreparation().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateOpenPreparation(null);
            } else {
                summary.setSummaryRateOpenPreparation(
                        summary.getSummaryOpenPreparation().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryClosePreparation() == null
                    || summary.getSummaryClosePreparation().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateClosePreparation(null);
            } else {
                summary.setSummaryRateClosePreparation(
                        summary.getSummaryClosePreparation().multiply(new BigDecimal(100))
                                .divide(totalSummary, parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryCloseTime() == null
                    || summary.getSummaryCloseTime().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateCloseTime(null);
            } else {
                summary.setSummaryRateCloseTime(
                        summary.getSummaryCloseTime().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
        }

        //グラフ色を設定する
        List<String> graphColorList = DemandGraphAutoColorUtility.getGraphAutoColorList(4);
        if (graphColorList.size() == 4) {
            summary.setGraphColorOpenTime(graphColorList.get(0));
            summary.setGraphColorOpenPreparation(graphColorList.get(1));
            summary.setGraphColorClosePreparation(graphColorList.get(2));
            summary.setGraphColorCloseTime(graphColorList.get(3));
        }

        result.setHeader(header);
        result.setDetailList(detailList);
        result.setSummary(summary);

        return result;
    }

}
