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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgWeekReportListTimeZoneUsedParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgWeekReportListTimeZoneUsedResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportListResult;
import jp.co.osaki.osol.api.result.utility.CurrentWeekReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForWeekResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListTimeZoneUsedDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListTimeZoneUsedHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListTimeZoneUsedSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * エネルギー使用状況実績取得（個別・週報・時間帯別使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgWeekReportListTimeZoneUsedDao extends OsolApiDao<DemandOrgWeekReportListTimeZoneUsedParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandWeekReportListServiceDaoImpl commonDemandWeekReportListServiceDaoImpl;

    public DemandOrgWeekReportListTimeZoneUsedDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandWeekReportListServiceDaoImpl = new CommonDemandWeekReportListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgWeekReportListTimeZoneUsedResult query(DemandOrgWeekReportListTimeZoneUsedParameter parameter)
            throws Exception {

        DemandOrgWeekReportListTimeZoneUsedResult result = new DemandOrgWeekReportListTimeZoneUsedResult();
        DemandOrgWeekReportListTimeZoneUsedHeaderResultData header = new DemandOrgWeekReportListTimeZoneUsedHeaderResultData();
        List<DemandOrgWeekReportListTimeZoneUsedDetailResultData> detailList = new ArrayList<>();
        DemandOrgWeekReportListTimeZoneUsedSummaryAreaResultData summary = new DemandOrgWeekReportListTimeZoneUsedSummaryAreaResultData();
        List<DemandWeekCorpCalListDetailResultData> corpCalList = null;
        List<DemandWeekCorpCalListDetailResultData> filterCorpCalList = null;
        List<DemandWeekBuildingCalListDetailResultData> buildingCalList = null;
        List<DemandWeekBuildingCalListDetailResultData> filterBuildingCalList = null;
        String fiscalYearFrom;
        String fiscalYearTo;
        CurrentWeekReportResult currentWeekReport = null;
        List<String> week54ExistList = new ArrayList<>();
        Map<String, Date> weekNoMap = null;
        String currentWeek = null;
        String currentFiscalYear = "";
        BigDecimal currentWeekNo = BigDecimal.ZERO;
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

        //集計日がNULLの場合、企業集計を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSummaryKind())) {
            parameter.setSummaryKind(ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal());
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計範囲指定方法がNULLの場合、年単位を設定
        if (CheckUtility.isNullOrEmpty(parameter.getRangeUnit())) {
            parameter.setRangeUnit(ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal());
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

        //週報カレンダを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandOrgWeekReportListTimeZoneUsedResult();
            }
            corpCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());
            if (corpCalList == null || corpCalList.isEmpty()) {
                return null;
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            List<BuildingDemandListDetailResultData> buildingDemnadList = getResultList(
                    buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemnadList == null || buildingDemnadList.size() != 1) {
                return new DemandOrgWeekReportListTimeZoneUsedResult();
            }
            buildingCalList = DemandEmsUtility.getWeekBuildingCalList(buildingDemnadList.get(0), getServerDateTime());
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return null;
            }
        }

        //取得したカレンダを年度でフィルタリングする
        if (ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal().equals(parameter.getRangeUnit())) {
            //年単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear()).subtract(parameter.getSumPeriod()).toString();
            fiscalYearTo = new BigDecimal(parameter.getFiscalYear()).add(parameter.getSumPeriod()).toString();
        } else {
            //週単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear())
                    .subtract(parameter.getSumPeriod().divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
            fiscalYearTo = new BigDecimal(parameter.getFiscalYear())
                    .add(parameter.getSumPeriod().divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP))
                    .toString();
        }

        //年度でフィルタリングする
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            if (corpCalList != null && !corpCalList.isEmpty()) {
                filterCorpCalList = corpCalList.stream()
                        .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                                && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                        .collect(Collectors.toList());
                if (filterCorpCalList == null || filterCorpCalList.isEmpty()) {
                    return null;
                }
            }

        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            if (buildingCalList != null && !buildingCalList.isEmpty()) {
                filterBuildingCalList = buildingCalList.stream()
                        .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                                && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                        .collect(Collectors.toList());
                if (filterBuildingCalList == null || filterBuildingCalList.isEmpty()) {
                    return null;
                }
            }
        }

        //現在積算中の年度、週Noを取得する⇒54週あり年度を取得する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            currentWeekReport = DemandEmsUtility.getCurrentCorpWeekReport(corpCalList, getServerDateTime());
            week54ExistList = DemandDataUtility.getWeek54ListForCompany(filterCorpCalList);
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            currentWeekReport = DemandEmsUtility.getCurrentBuildingWeekReport(buildingCalList, getServerDateTime());
            week54ExistList = DemandDataUtility.getWeek54ListForPower(filterBuildingCalList);
        }

        //54週あり年度に対象の年度が存在するかチェックする
        if (parameter.getWeekNo().compareTo(new BigDecimal("54")) == 0) {
            if (week54ExistList == null || week54ExistList.isEmpty()) {
                //データがない場合は変更
                header.setWeek54Change(true);
                parameter.setWeekNo(new BigDecimal("53"));
            } else if (week54ExistList.contains(parameter.getFiscalYear())) {
                //対象年度が含まれている場合は変更しない
                header.setWeek54Change(false);
            } else {
                //上記以外は変更
                header.setWeek54Change(true);
                parameter.setWeekNo(new BigDecimal("53"));
            }
        } else {
            //それ以外は週番号を変更しない
            header.setWeek54Change(false);
        }

        //集計期間を取得する
        SummaryRangeForWeekResult summaryRange = SummaryRangeUtility.getSummaryRangeForWeek(parameter.getFiscalYear(),
                parameter.getWeekNo(), week54ExistList, parameter.getSumPeriodCalcType(), parameter.getRangeUnit(),
                parameter.getSumPeriod());

        //ヘッダ情報を設定する
        header.setFiscalYearFrom(summaryRange.getFiscalYearFrom());
        header.setFiscalYearTo(summaryRange.getFiscalYearTo());
        header.setWeekNoFrom(summaryRange.getWeekNoFrom());
        header.setWeekNoTo(summaryRange.getWeekNoTo());

        //年度+週No単位でMapを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            if (filterCorpCalList != null && !filterCorpCalList.isEmpty()) {
                weekNoMap = filterCorpCalList.stream().collect(Collectors.toMap(
                        x -> x.getFiscalYear().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            if (filterBuildingCalList != null && !filterBuildingCalList.isEmpty()) {
                weekNoMap = filterBuildingCalList.stream().collect(Collectors.toMap(
                        x -> x.getFiscalYear().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
            }
        }

        do {

            DemandOrgWeekReportListTimeZoneUsedDetailResultData detail = new DemandOrgWeekReportListTimeZoneUsedDetailResultData();

            if (CheckUtility.isNullOrEmpty(currentWeek)) {
                currentWeek = summaryRange.getFiscalYearFrom()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getWeekNoFrom().intValue()));
                currentFiscalYear = summaryRange.getFiscalYearFrom();
                currentWeekNo = summaryRange.getWeekNoFrom();
            } else {

                if (currentWeekNo.compareTo(new BigDecimal(54)) == 0) {
                    //54週目の場合
                    currentFiscalYear = new BigDecimal(currentFiscalYear).add(BigDecimal.ONE).toString();
                    currentWeekNo = BigDecimal.ONE;
                } else if (currentWeekNo.compareTo(new BigDecimal(53)) == 0) {
                    //53週目の場合
                    if (week54ExistList.contains(currentFiscalYear)) {
                        //54週目ありの場合
                        currentWeekNo = currentWeekNo.add(BigDecimal.ONE);
                    } else {
                        //53週のみの場合
                        currentFiscalYear = new BigDecimal(currentFiscalYear).add(BigDecimal.ONE).toString();
                        currentWeekNo = BigDecimal.ONE;
                    }
                } else {
                    currentWeekNo = currentWeekNo.add(BigDecimal.ONE);
                }

                currentWeek = currentFiscalYear
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentWeekNo.intValue()));

            }

            detail.setWeekNo(
                    ApiSimpleConstants.WEEK_NO.concat(currentWeekNo.toString()).concat(ApiSimpleConstants.WEEK));
            detail.setFiscalYearWeekNo(currentWeek);
            detail.setWeekStartDate(
                    DateUtility.changeDateFormat(weekNoMap.get(currentWeek), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
            detail.setOpenTimeKwh(null);
            detail.setOpenPreparationKwh(null);
            detail.setClosePreparationKwh(null);
            detail.setCloseTimeKwh(null);

            detailList.add(detail);

        } while (currentWeek != null && !currentWeek.equals(summaryRange.getFiscalYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getWeekNoTo().intValue()))));

        //デマンド週報データを取得する
        CommonDemandWeekReportListResult demandWeekReportParam = DemandEmsUtility.getWeekReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getSummaryKind(),
                summaryRange.getFiscalYearFrom(), summaryRange.getFiscalYearTo(), summaryRange.getWeekNoFrom(),
                summaryRange.getWeekNoTo());
        List<CommonDemandWeekReportListResult> demandWeekReportList = getResultList(
                commonDemandWeekReportListServiceDaoImpl, demandWeekReportParam);
        if (demandWeekReportList != null && !demandWeekReportList.isEmpty()) {
            demandWeekReportList = SortUtility.sortCommonDemandWeekReportListByFiscalYearWeekNo(demandWeekReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandWeekReportListResult demandWeekReport : demandWeekReportList) {
            for (DemandOrgWeekReportListTimeZoneUsedDetailResultData detail : detailList) {
                if (demandWeekReport.getFiscalYear()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                demandWeekReport.getWeekNo().intValue()))
                        .equals(detail.getFiscalYearWeekNo())) {
                    //開店時間
                    if (demandWeekReport.getOpenTimeKwh() == null) {
                        detail.setOpenTimeKwh(null);
                    } else {
                        detail.setOpenTimeKwh(demandWeekReport.getOpenTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandWeekReport.getOpenTimeKwh() != null) {
                        if (summaryOpenTime == null) {
                            summaryOpenTime = demandWeekReport.getOpenTimeKwh();
                        } else {
                            summaryOpenTime = summaryOpenTime.add(demandWeekReport.getOpenTimeKwh());
                        }
                        if (maxOpenTime == null) {
                            maxOpenTime = demandWeekReport.getOpenTimeKwh();
                        } else if (demandWeekReport.getOpenTimeKwh().compareTo(maxOpenTime) >= 0) {
                            maxOpenTime = demandWeekReport.getOpenTimeKwh();
                        }

                        if (Integer.parseInt(currentWeekReport.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentWeekReport.getWeekNo().intValue()))) > Integer
                                                .parseInt(demandWeekReport.getFiscalYear()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandWeekReport.getWeekNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minOpenTime == null) {
                                minOpenTime = demandWeekReport.getOpenTimeKwh();
                            } else if (demandWeekReport.getOpenTimeKwh().compareTo(minOpenTime) <= 0) {
                                minOpenTime = demandWeekReport.getOpenTimeKwh();
                            }
                            if (summaryAverageCountOpenTime == null) {
                                summaryAverageCountOpenTime = BigDecimal.ONE;
                            } else {
                                summaryAverageCountOpenTime = summaryAverageCountOpenTime.add(BigDecimal.ONE);
                            }
                            if (summaryAverageOpenTime == null) {
                                summaryAverageOpenTime = demandWeekReport.getOpenTimeKwh();
                            } else {
                                summaryAverageOpenTime = summaryAverageOpenTime.add(demandWeekReport.getOpenTimeKwh());
                            }
                        }
                    }
                    //開店準備時間
                    if (demandWeekReport.getOpenPreparationKwh() == null) {
                        detail.setOpenPreparationKwh(null);
                    } else {
                        detail.setOpenPreparationKwh(
                                demandWeekReport.getOpenPreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandWeekReport.getOpenPreparationKwh() != null) {
                        if (summaryOpenPreparation == null) {
                            summaryOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                        } else {
                            summaryOpenPreparation = summaryOpenPreparation
                                    .add(demandWeekReport.getOpenPreparationKwh());
                        }
                        if (maxOpenPreparation == null) {
                            maxOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                        } else if (demandWeekReport.getOpenPreparationKwh().compareTo(maxOpenPreparation) >= 0) {
                            maxOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                        }

                        if (Integer.parseInt(currentWeekReport.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentWeekReport.getWeekNo().intValue()))) > Integer
                                                .parseInt(demandWeekReport.getFiscalYear()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandWeekReport.getWeekNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minOpenPreparation == null) {
                                minOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                            } else if (demandWeekReport.getOpenPreparationKwh().compareTo(minOpenPreparation) <= 0) {
                                minOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                            }
                            if (summaryAverageCountOpenPreparation == null) {
                                summaryAverageCountOpenPreparation = BigDecimal.ONE;
                            } else {
                                summaryAverageCountOpenPreparation = summaryAverageCountOpenPreparation
                                        .add(BigDecimal.ONE);
                            }
                            if (summaryAverageOpenPreparation == null) {
                                summaryAverageOpenPreparation = demandWeekReport.getOpenPreparationKwh();
                            } else {
                                summaryAverageOpenPreparation = summaryAverageOpenPreparation
                                        .add(demandWeekReport.getOpenPreparationKwh());
                            }
                        }
                    }

                    //閉店準備時間
                    if (demandWeekReport.getClosePreparationKwh() == null) {
                        detail.setClosePreparationKwh(null);
                    } else {
                        detail.setClosePreparationKwh(
                                demandWeekReport.getClosePreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandWeekReport.getClosePreparationKwh() != null) {
                        if (summaryClosePreparation == null) {
                            summaryClosePreparation = demandWeekReport.getClosePreparationKwh();
                        } else {
                            summaryClosePreparation = summaryClosePreparation
                                    .add(demandWeekReport.getClosePreparationKwh());
                        }
                        if (maxClosePreparation == null) {
                            maxClosePreparation = demandWeekReport.getClosePreparationKwh();
                        } else if (demandWeekReport.getClosePreparationKwh().compareTo(maxClosePreparation) >= 0) {
                            maxClosePreparation = demandWeekReport.getClosePreparationKwh();
                        }
                        if (Integer.parseInt(currentWeekReport.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentWeekReport.getWeekNo().intValue()))) > Integer
                                                .parseInt(demandWeekReport.getFiscalYear()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandWeekReport.getWeekNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minClosePreparation == null) {
                                minClosePreparation = demandWeekReport.getClosePreparationKwh();
                            } else if (demandWeekReport.getClosePreparationKwh().compareTo(minClosePreparation) <= 0) {
                                minClosePreparation = demandWeekReport.getClosePreparationKwh();
                            }
                            if (summaryAverageCountClosePreparation == null) {
                                summaryAverageCountClosePreparation = BigDecimal.ONE;
                            } else {
                                summaryAverageCountClosePreparation = summaryAverageCountClosePreparation
                                        .add(BigDecimal.ONE);
                            }
                            if (summaryAverageClosePreparation == null) {
                                summaryAverageClosePreparation = demandWeekReport.getClosePreparationKwh();
                            } else {
                                summaryAverageClosePreparation = summaryAverageClosePreparation
                                        .add(demandWeekReport.getClosePreparationKwh());
                            }
                        }
                    }

                    //閉店時間
                    if (demandWeekReport.getCloseTimeKwh() == null) {
                        detail.setCloseTimeKwh(null);
                    } else {
                        detail.setCloseTimeKwh(demandWeekReport.getCloseTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandWeekReport.getCloseTimeKwh() != null) {
                        if (summaryCloseTime == null) {
                            summaryCloseTime = demandWeekReport.getCloseTimeKwh();
                        } else {
                            summaryCloseTime = summaryCloseTime.add(demandWeekReport.getCloseTimeKwh());
                        }
                        if (maxCloseTime == null) {
                            maxCloseTime = demandWeekReport.getCloseTimeKwh();
                        } else if (demandWeekReport.getCloseTimeKwh().compareTo(maxCloseTime) >= 0) {
                            maxCloseTime = demandWeekReport.getCloseTimeKwh();
                        }
                        if (Integer.parseInt(currentWeekReport.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentWeekReport.getWeekNo().intValue()))) > Integer
                                                .parseInt(demandWeekReport.getFiscalYear()
                                                        .concat(String.format(
                                                                StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                demandWeekReport.getWeekNo().intValue())))) {
                            //現在集計中以降の値は最小、平均に含まない
                            if (minCloseTime == null) {
                                minCloseTime = demandWeekReport.getCloseTimeKwh();
                            } else if (demandWeekReport.getCloseTimeKwh().compareTo(minCloseTime) <= 0) {
                                minCloseTime = demandWeekReport.getCloseTimeKwh();
                            }
                            if (summaryAverageCountCloseTime == null) {
                                summaryAverageCountCloseTime = BigDecimal.ONE;
                            } else {
                                summaryAverageCountCloseTime = summaryAverageCountCloseTime.add(BigDecimal.ONE);
                            }
                            if (summaryAverageCloseTime == null) {
                                summaryAverageCloseTime = demandWeekReport.getCloseTimeKwh();
                            } else {
                                summaryAverageCloseTime = summaryAverageCloseTime
                                        .add(demandWeekReport.getCloseTimeKwh());
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
