/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryBuildingReductionLastDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryBuildingReductionLastDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearWeekNoResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryBuildingUsedDataBuildingResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryBuildingUsedDataDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.FilterUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンドデータ実績取得処理（全体・建物・テナント一覧・削減率（前年比）） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllSummaryBuildingReductionLastDataDao
        extends OsolApiDao<DemandAllSummaryBuildingReductionLastDataParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandWeekReportLineListServiceDaoImpl commonDemandWeekReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllSummaryBuildingReductionLastDataDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandWeekReportLineListServiceDaoImpl = new CommonDemandWeekReportLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllSummaryBuildingReductionLastDataResult query(
            DemandAllSummaryBuildingReductionLastDataParameter parameter) throws Exception {
        DemandAllSummaryBuildingReductionLastDataResult result = new DemandAllSummaryBuildingReductionLastDataResult();

        List<AllBuildingListDetailResultData> buildingList;
        List<DemandWeekCorpCalListDetailResultData> weekCorpCalList = null;
        List<DemandWeekBuildingCalListDetailResultData> weekBuildingCalList = null;
        List<CommonDemandMonthReportLineListResult> yesterdayResult = null;
        List<CommonDemandMonthReportLineListResult> yesterdayCompareResult = null;
        List<CommonDemandWeekReportLineListResult> beforeWeekResult = null;
        List<CommonDemandWeekReportLineListResult> beforeWeekCompareResult = null;
        TargetFiscalYearWeekNoResult targetBeforeWeek = null;
        List<DemandAllSummaryBuildingUsedDataBuildingResultData> detail = new ArrayList<>();
        BigDecimal yesterdayValue = null;
        BigDecimal yesterdayCompareValue = null;
        BigDecimal beforeWeekValue = null;
        BigDecimal beforeWeekCompareValue = null;

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllSummaryBuildingReductionLastDataResult();
        }

        //日付がNULLの場合、当日日付を設定
        if (parameter.getYmd() == null) {
            parameter.setYmd(getServerDateTime());
        }

        //集計日がNULLの場合、企業集計を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSummaryKind())) {
            parameter.setSummaryKind(ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal());
        }

        //ソートキーがNULLの場合、建物・テナント番号を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSortKey())) {
            parameter.setSortKey(ApiCodeValueConstants.ALL_DETAIL_SORT_KEY.BUILDING_NO.getVal());
        }

        //ソート順がNULLの場合、昇順を設定
        if (CheckUtility.isNullOrEmpty(parameter.getSortOrder())) {
            parameter.setSortOrder(ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、切り捨てを設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
        }

        //対象建物の取得
        AllBuildingListDetailResultData buildingParam = DemandEmsAllUtility.getBuildingListParam(
                parameter.getOperationCorpId(), parameter.getBuildingNarrowing(), parameter.getParentGroupId(),
                parameter.getChildGroupId(), parameter.getBuildingNo());
        if (ApiCodeValueConstants.BUILDING_FILTER.ALL.getVal().equals(parameter.getBuildingNarrowing())) {
            //すべての場合
            buildingList = getResultList(allBuildingListServiceDaoImpl, buildingParam);
        } else if (ApiCodeValueConstants.BUILDING_FILTER.GROUP.getVal().equals(parameter.getBuildingNarrowing())) {
            //グループの場合
            buildingList = getResultList(groupBuildingListServiceDaoImpl, buildingParam);
        } else if (ApiCodeValueConstants.BUILDING_FILTER.NO.getVal().equals(parameter.getBuildingNarrowing())) {
            //建物番号の場合
            buildingList = getResultList(noBuildingSelectServiceDaoImpl, buildingParam);
        } else {
            //上記以外は処理を終了
            return new DemandAllSummaryBuildingReductionLastDataResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllSummaryBuildingReductionLastDataResult();
        }

        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合、企業集計週報カレンダを作成する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                    corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandAllSummaryBuildingReductionLastDataResult();
            }
            weekCorpCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());
            if (weekCorpCalList == null || weekCorpCalList.isEmpty()) {
                return new DemandAllSummaryBuildingReductionLastDataResult();
            }
        }

        //建物件数分処理を繰り返す
        for (AllBuildingListDetailResultData buildingResult : buildingList) {

            if (FilterUtility.filterDemandBuildingListByBuilding(buildingResult, parameter.getFilterKey()) == null) {
                //対象建物ではない場合、次のレコードへ
                continue;
            }

            //建物機器情報を取得する
            DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                    .getBuildingSmListParam(buildingResult.getCorpId(), buildingResult.getBuildingId(), null);
            List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                    demandBuildingSmListServiceDaoImpl, buildingSmParam);

            if (buildingSmList == null || buildingSmList.isEmpty()) {
                //建物機器情報が存在しない場合は次のレコードへ
                continue;
            }

            //実績値の初期化
            DemandAllSummaryBuildingUsedDataBuildingResultData detailResult = new DemandAllSummaryBuildingUsedDataBuildingResultData();

            if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                //建物集計の場合、建物デマンド情報を取得する
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
                List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                        buildingDemandListServiceDaoImpl, buildingDemandParam);
                //建物集計の場合、電力集計週報カレンダを作成する
                weekBuildingCalList = DemandEmsUtility.getWeekBuildingCalList(buildingDemandList.get(0),
                        getServerDateTime());
                if (weekBuildingCalList == null || weekBuildingCalList.isEmpty()) {
                    detailResult.setCorpId(buildingResult.getCorpId());
                    detailResult.setBuildingId(buildingResult.getBuildingId());
                    detailResult.setBuildingNo(buildingResult.getBuildingNo());
                    detailResult.setBuildingType(buildingResult.getBuildingType());
                    detailResult.setBuildingName(buildingResult.getBuildingName());
                    continue;
                }
            }

            //デマンド系統月報テーブルから前日のデータを取得する
            CommonDemandMonthReportLineListResult monthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                    DateUtility.plusDay(DateUtility.conversionDate(
                            DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD), -1),
                    DateUtility.plusDay(DateUtility.conversionDate(
                            DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD), -1));
            yesterdayResult = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthReportParam);

            //デマンド系統月報テーブルから前日の前年のデータを取得する
            CommonDemandMonthReportLineListResult monthCompareReportParam = DemandEmsUtility
                    .getMonthReportLineListParam(buildingResult.getCorpId(), buildingResult.getBuildingId(),
                            parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                            DateUtility.plusYear(DateUtility.plusDay(DateUtility
                                    .conversionDate(
                                            DateUtility.changeDateFormat(parameter.getYmd(),
                                                    DateUtility.DATE_FORMAT_YYYYMMDD),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    -1), -1),
                            DateUtility.plusYear(DateUtility.plusDay(DateUtility
                                    .conversionDate(
                                            DateUtility.changeDateFormat(parameter.getYmd(),
                                                    DateUtility.DATE_FORMAT_YYYYMMDD),
                                            DateUtility.DATE_FORMAT_YYYYMMDD),
                                    -1), -1));
            yesterdayCompareResult = getResultList(commonDemandMonthReportLineListServiceDaoImpl,
                    monthCompareReportParam);

            //前週の年度・週番号を取得する
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                targetBeforeWeek = DemandEmsUtility.getTargetCorpFiscalYearWeekNo(weekCorpCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        new BigDecimal(-1));
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                targetBeforeWeek = DemandEmsUtility.getTargetBuildingFiscalYearWeekNo(weekBuildingCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        new BigDecimal(-1));
            } else {
                targetBeforeWeek = null;
            }

            if (targetBeforeWeek != null) {
                //デマンド系統週報テーブルから前週のデータを取得する
                CommonDemandWeekReportLineListResult weekLineParam = DemandEmsUtility.getWeekReportLineListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        targetBeforeWeek.getFiscalYear(), targetBeforeWeek.getFiscalYear(),
                        targetBeforeWeek.getWeekNo(), targetBeforeWeek.getWeekNo());
                beforeWeekResult = getResultList(commonDemandWeekReportLineListServiceDaoImpl, weekLineParam);

                //デマンド系統週報テーブルから前週の前年のデータを取得する
                CommonDemandWeekReportLineListResult weekCompareLineParam = DemandEmsUtility.getWeekReportLineListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        String.valueOf(new BigDecimal(targetBeforeWeek.getFiscalYear()).subtract(BigDecimal.ONE)),
                        String.valueOf(new BigDecimal(targetBeforeWeek.getFiscalYear()).subtract(BigDecimal.ONE)),
                        targetBeforeWeek.getWeekNo(), targetBeforeWeek.getWeekNo());
                beforeWeekCompareResult = getResultList(commonDemandWeekReportLineListServiceDaoImpl,
                        weekCompareLineParam);

            }

            //明細の設定
            detailResult.setCorpId(buildingResult.getCorpId());
            detailResult.setBuildingId(buildingResult.getBuildingId());
            detailResult.setBuildingNo(buildingResult.getBuildingNo());
            detailResult.setBuildingType(buildingResult.getBuildingType());
            detailResult.setBuildingName(buildingResult.getBuildingName());

            if (yesterdayResult != null && yesterdayResult.size() == 1) {
                //1件の場合のみ設定
                yesterdayValue = yesterdayResult.get(0).getLineValueKwh();
            }

            if (yesterdayCompareResult != null && yesterdayCompareResult.size() == 1) {
                //1件の場合のみ設定
                yesterdayCompareValue = yesterdayCompareResult.get(0).getLineValueKwh();
            }

            if (yesterdayValue == null || yesterdayCompareValue == null) {
                detailResult.setLastDayData(null);
            } else if (yesterdayCompareValue.compareTo(BigDecimal.ZERO) == 0) {
                //比較元が0の場合
                detailResult.setLastDayData(BigDecimal.ZERO.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                detailResult.setLastDayData(
                        (yesterdayCompareValue.subtract(yesterdayValue)).multiply(new BigDecimal(100))
                                .divide(yesterdayCompareValue, parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
            }

            if (beforeWeekResult != null && beforeWeekResult.size() == 1) {
                //1件の場合のみ設定
                beforeWeekValue = beforeWeekResult.get(0).getLineValueKwh();
            }

            if (beforeWeekCompareResult != null && beforeWeekCompareResult.size() == 1) {
                //1件の場合のみ設定
                beforeWeekCompareValue = beforeWeekCompareResult.get(0).getLineValueKwh();
            }

            if (beforeWeekValue == null || beforeWeekCompareValue == null) {
                detailResult.setLastWeekData(null);
            } else if (beforeWeekCompareValue.compareTo(BigDecimal.ZERO) == 0) {
                //比較元が0の場合
                detailResult.setLastWeekData(BigDecimal.ZERO.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                detailResult.setLastWeekData(
                        (beforeWeekCompareValue.subtract(beforeWeekValue)).multiply(new BigDecimal(100))
                                .divide(beforeWeekCompareValue, parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
            }

            detailResult.setThisMonthData(null);
            detailResult.setThisYearData(null);

            detail.add(detailResult);
        }

        if (!CheckUtility.isNullOrEmpty(parameter.getSortKey())
                && !CheckUtility.isNullOrEmpty(parameter.getSortOrder())) {
            //指定のキーでソートする
            if (!detail.isEmpty()) {
                detail = SortUtility.sortDemandAllBuildingList(detail,
                        ApiCodeValueConstants.ALL_SUMMARY_DETAIL_KIND.REDUCTION_LAST_YEAR.getVal(),
                        parameter.getSortKey(), parameter.getSortOrder());
            }
        }

        result.setDetail(new DemandAllSummaryBuildingUsedDataDetailResultData(ApiSimpleConstants.PERCENT, detail));
        return result;
    }

}
