/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportPointListResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryBuildingUsedDataBuildingResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;

/**
 * ソートユーティリティクラス
 *
 * @author ya-ishida
 */
public class SortUtility {

    /**
     * デマンドデータ実績（全体・建物・テナント一覧）ソート処理
     *
     * @param list
     * @param summaryKind　0：使用量 1：最大値 2：削減率（前年比） 3：削減率（導入前比）
     * @param sortKey　0：建物・テナント番号　1：前日　2：前週　3：当月　4：当年
     * @param sortOrder　0：昇順　1：降順
     * @return
     */
    public static List<DemandAllSummaryBuildingUsedDataBuildingResultData> sortDemandAllBuildingList(
            List<DemandAllSummaryBuildingUsedDataBuildingResultData> list, String summaryKind, String sortKey,
            String sortOrder) {

        List<DemandAllSummaryBuildingUsedDataBuildingResultData> afterSortList;
        String firstSortKey;

        if (list == null || list.isEmpty() || list.size() == 1) {
            //nullまたは0件または1件の場合、処理を終了
            return list;
        }

        //ソートキーを再編成する
        firstSortKey = sortKey;
        if (ApiCodeValueConstants.ALL_SUMMARY_DETAIL_KIND.REDUCTION_LAST_YEAR.getVal().equals(summaryKind)) {
            //削減率（前年比）は、当月実績と当年実績ではソートできない
            if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_MONTH.getVal().equals(sortKey)
                    || ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_YEAR.getVal().equals(sortKey)) {
                firstSortKey = ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.BUILDING_NO.getVal();
            }
        }

        if (ApiCodeValueConstants.ALL_SUMMARY_DETAIL_KIND.REDUCTION_BEFORE_INNOVATE.getVal().equals(summaryKind)) {
            //削減率（導入前比）は前日実績と当月実績と当年実績ではソートできない
            if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.LAST_DAY.getVal().equals(sortKey)
                    || ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_MONTH.getVal().equals(sortKey)
                    || ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_YEAR.getVal().equals(sortKey)) {
                firstSortKey = ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.BUILDING_NO.getVal();
            }
        }

        //ソートを行う
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.BUILDING_NO.getVal().equals(firstSortKey)) {
                //建物・テナント番号でソート
                afterSortList = list.stream()
                        .sorted(Comparator.comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.LAST_DAY.getVal().equals(firstSortKey)) {
                //前日実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getLastDayData,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.LAST_WEEK.getVal().equals(firstSortKey)) {
                //前週実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getLastWeekData,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_MONTH.getVal().equals(firstSortKey)) {
                //当月実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getThisMonthData,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_YEAR.getVal().equals(firstSortKey)) {
                //当年実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getThisYearData,
                                        Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                //上記以外は何もしない
                return list;
            }
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.BUILDING_NO.getVal().equals(firstSortKey)) {
                //建物・テナント番号でソート
                afterSortList = list.stream()
                        .sorted(Comparator.comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.LAST_DAY.getVal().equals(firstSortKey)) {
                //前日実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getLastDayData,
                                        Comparator.nullsLast(Comparator.reverseOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.LAST_WEEK.getVal().equals(firstSortKey)) {
                //前週実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getLastWeekData,
                                        Comparator.nullsLast(Comparator.reverseOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_MONTH.getVal().equals(firstSortKey)) {
                //当月実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getThisMonthData,
                                        Comparator.nullsLast(Comparator.reverseOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.THIS_YEAR.getVal().equals(firstSortKey)) {
                //当年実績でソート
                afterSortList = list.stream()
                        .sorted(Comparator
                                .comparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getThisYearData,
                                        Comparator.nullsLast(Comparator.reverseOrder()))
                                .thenComparing(DemandAllSummaryBuildingUsedDataBuildingResultData::getBuildingNo,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            } else {
                //上記以外は何もしない
                return list;
            }
        } else {
            //上記以外は何もしない
            return list;
        }

        return afterSortList;

    }

    /**
     * 建物・テナント一覧を建物・テナント番号でソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<AllBuildingListDetailResultData> sortCommonBuildingListByBuildingNo(
            List<AllBuildingListDetailResultData> list,
            String sortOrder) {

        List<AllBuildingListDetailResultData> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream().sorted(Comparator.comparing(AllBuildingListDetailResultData::getBuildingNo,
                    Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream().sorted(Comparator.comparing(AllBuildingListDetailResultData::getBuildingNo,
                    Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド日報系統データを計測年月日、時限Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandDayReportLineListResult> sortCommonDemandDayReportLineListByMeasurement(
            List<CommonDemandDayReportLineListResult> list, String sortOrder) {

        List<CommonDemandDayReportLineListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportLineListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandDayReportLineListResult::getJigenNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportLineListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandDayReportLineListResult::getJigenNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド日報データを計測年月日、時限Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandDayReportListResult> sortCommonDemandDayReportListByMeasurement(
            List<CommonDemandDayReportListResult> list, String sortOrder) {
        List<CommonDemandDayReportListResult> afterSortList;
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandDayReportListResult::getJigenNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandDayReportListResult::getJigenNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }
        return afterSortList;
    }

    /**
     * デマンド日報ポイントデータを計測年月日、時限Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandDayReportPointListResult> sortCommonDemandDayReportPointListByMeasurement(
            List<CommonDemandDayReportPointListResult> list, String sortOrder) {
        List<CommonDemandDayReportPointListResult> afterSortList;
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportPointListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandDayReportPointListResult::getJigenNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandDayReportPointListResult::getMeasurementDate,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandDayReportPointListResult::getJigenNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }
        return afterSortList;
    }

    /**
     * デマンド週報系統データを年度、週Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandWeekReportLineListResult> sortCommonDemandWeekReportLineListByFiscalYearWeekNo(
            List<CommonDemandWeekReportLineListResult> list, String sortOrder) {
        List<CommonDemandWeekReportLineListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportLineListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandWeekReportLineListResult::getWeekNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportLineListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandWeekReportLineListResult::getWeekNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド週報データを年度、週Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandWeekReportListResult> sortCommonDemandWeekReportListByFiscalYearWeekNo(
            List<CommonDemandWeekReportListResult> list, String sortOrder) {
        List<CommonDemandWeekReportListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandWeekReportListResult::getWeekNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandWeekReportListResult::getWeekNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド週報ポイントデータを年度、週Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandWeekReportPointListResult> sortCommonDemandWeekReportPointListByFiscalYearWeekNo(
            List<CommonDemandWeekReportPointListResult> list, String sortOrder) {
        List<CommonDemandWeekReportPointListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportPointListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandWeekReportPointListResult::getWeekNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandWeekReportPointListResult::getFiscalYear,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandWeekReportPointListResult::getWeekNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド月報系統データを計測年月日でソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandMonthReportLineListResult> sortCommonDemandMonthReportLineListByMeasurement(
            List<CommonDemandMonthReportLineListResult> list, String sortOrder) {
        List<CommonDemandMonthReportLineListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportLineListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportLineListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド月報データを計測年月日でソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandMonthReportListResult> sortCommonDemandMonthReportListByMeasurement(
            List<CommonDemandMonthReportListResult> list, String sortOrder) {
        List<CommonDemandMonthReportListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド月報ポイントデータを計測年月日でソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandMonthReportPointListResult> sortCommonDemandMonthReportPointListByMeasurement(
            List<CommonDemandMonthReportPointListResult> list, String sortOrder) {
        List<CommonDemandMonthReportPointListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportPointListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator.comparing(CommonDemandMonthReportPointListResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }

        return afterSortList;
    }

    /**
     * デマンド年報系統データを年No、月Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandYearReportLineListResult> sortCommonDemandYearReportLineListByYearNoMonthNo(
            List<CommonDemandYearReportLineListResult> list, String sortOrder) {
        List<CommonDemandYearReportLineListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandYearReportLineListResult::getYearNo,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandYearReportLineListResult::getMonthNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandYearReportLineListResult::getYearNo,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandYearReportLineListResult::getMonthNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }
        return afterSortList;

    }

    /**
     * デマンド年報データを年No、月Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandYearReportListResult> sortCommonDemandYearReportListByYearNoMonthNo(
            List<CommonDemandYearReportListResult> list, String sortOrder) {
        List<CommonDemandYearReportListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandYearReportListResult::getYearNo,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(CommonDemandYearReportListResult::getMonthNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream()
                    .sorted(Comparator
                            .comparing(CommonDemandYearReportListResult::getYearNo,
                                    Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommonDemandYearReportListResult::getMonthNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }
        return afterSortList;
    }

    /**
     * デマンド年報ポイントデータを年No、月Noでソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<CommonDemandYearReportPointListResult> sortCommonDemandYearReportPointListByYearNoMonthNo(
            List<CommonDemandYearReportPointListResult> list, String sortOrder) {
        List<CommonDemandYearReportPointListResult> afterSortList;

        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //昇順の場合
            afterSortList = list.stream().sorted(Comparator
                    .comparing(CommonDemandYearReportPointListResult::getYearNo,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(CommonDemandYearReportPointListResult::getMonthNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //降順の場合
            afterSortList = list.stream().sorted(Comparator
                    .comparing(CommonDemandYearReportPointListResult::getYearNo,
                            Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(CommonDemandYearReportPointListResult::getMonthNo, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else {
            //上記以外はソートしない
            return list;
        }
        return afterSortList;
    }

    /**
     * 系統取得結果を系統番号（ALL⇒ALL/ETC以外⇒ETC）でソートする
     *
     * @param list
     * @param sortOrder
     * @return
     */
    public static List<LineListDetailResultData> sortLineListByLineNo(List<LineListDetailResultData> list,
            String sortOrder) {
        List<LineListDetailResultData> afterSortList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return list;
        }

        //ALL
        List<LineListDetailResultData> allFilterList = list.stream()
                .filter(i -> i.getLineTarget().equals(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal()))
                .collect(Collectors.toList());
        //ETC
        List<LineListDetailResultData> etcFilterList = list.stream()
                .filter(i -> i.getLineTarget().equals(ApiGenericTypeConstants.LINE_TARGET.ETC.getVal()))
                .collect(Collectors.toList());
        //ALL、ETC以外
        List<LineListDetailResultData> otherFilterList = list.stream()
                .filter(i -> !i.getLineTarget().equals(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal())
                        && !i.getLineTarget().equals(ApiGenericTypeConstants.LINE_TARGET.ETC.getVal()))
                .collect(Collectors.toList());

        //ALL、ETC以外の場合のリストに系統番号を数値換算した値をセットする
        for (LineListDetailResultData other : otherFilterList) {
            other.setLineNoToNumber(new Integer(other.getLineNo()));
        }

        if (otherFilterList != null && !otherFilterList.isEmpty()) {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                otherFilterList = otherFilterList.stream().sorted(
                        Comparator.comparing(LineListDetailResultData::getLineNoToNumber, Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
                otherFilterList = otherFilterList.stream().sorted(
                        Comparator.comparing(LineListDetailResultData::getLineNoToNumber, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }

        //順番に値を設定する
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            if (allFilterList != null && !allFilterList.isEmpty()) {
                afterSortList.add(allFilterList.get(0));
            }
            if (otherFilterList != null && !otherFilterList.isEmpty()) {
                for (LineListDetailResultData line : otherFilterList) {
                    afterSortList.add(line);
                }
            }

            if (etcFilterList != null && !etcFilterList.isEmpty()) {
                afterSortList.add(etcFilterList.get(0));
            }
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            if (etcFilterList != null && !etcFilterList.isEmpty()) {
                afterSortList.add(etcFilterList.get(0));
            }
            if (otherFilterList != null && !otherFilterList.isEmpty()) {
                for (LineListDetailResultData line : otherFilterList) {
                    afterSortList.add(line);
                }
            }
            if (allFilterList != null && !allFilterList.isEmpty()) {
                afterSortList.add(allFilterList.get(0));
            }

        } else {
            return list;
        }

        return afterSortList;
    }

    /**
     * グラフ要素設定情報を系統⇒ポイント/アナログでソートする（同じ場合はグラフ表示順）
     *
     * @param elementList
     * @param sortOrder
     * @return
     */
    public static List<DemandGraphElementListDetailResultData> sortElementListByGraphElementType(
            List<DemandGraphElementListDetailResultData> elementList, String sortOrder) {
        List<DemandGraphElementListDetailResultData> afterSortList = new ArrayList<>();

        //系統
        List<DemandGraphElementListDetailResultData> lineList = elementList.stream()
                .filter(i -> i.getGraphElementType().equals(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal()))
                .collect(Collectors.toList());
        //ポイント・アメダス（系統以外）
        List<DemandGraphElementListDetailResultData> noLineList = elementList.stream()
                .filter(i -> !i.getGraphElementType().equals(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal()))
                .collect(Collectors.toList());

        //それぞれをグラフ表示順でソートする
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            if (lineList != null && !lineList.isEmpty()) {
                lineList = lineList.stream().sorted(
                        Comparator.comparing(DemandGraphElementListDetailResultData::getDisplayOrder,
                                Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            }
            if (noLineList != null && !noLineList.isEmpty()) {
                noLineList = noLineList.stream().sorted(
                        Comparator.comparing(DemandGraphElementListDetailResultData::getDisplayOrder,
                                Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            }
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            if (lineList != null && !lineList.isEmpty()) {
                lineList = lineList.stream().sorted(
                        Comparator.comparing(DemandGraphElementListDetailResultData::getDisplayOrder,
                                Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
            if (noLineList != null && !noLineList.isEmpty()) {
                noLineList = noLineList.stream().sorted(
                        Comparator.comparing(DemandGraphElementListDetailResultData::getDisplayOrder,
                                Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }

        //順番に値をセットする
        if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
            //系統⇒系統以外の順
            for (DemandGraphElementListDetailResultData element : lineList) {
                afterSortList.add(element);
            }
            for (DemandGraphElementListDetailResultData element : noLineList) {
                afterSortList.add(element);
            }
        } else if (ApiCodeValueConstants.SORT_ORDER.DESC.getVal().equals(sortOrder)) {
            //系統以外⇒系統の順
            for (DemandGraphElementListDetailResultData element : noLineList) {
                afterSortList.add(element);
            }
            for (DemandGraphElementListDetailResultData element : lineList) {
                afterSortList.add(element);
            }
        } else {
            return elementList;
        }

        return afterSortList;
    }

}
