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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryBuildingUsedDataParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryBuildingUsedDataResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearMonthNoResult;
import jp.co.osaki.osol.api.result.utility.TargetFiscalYearSummaryRangeResult;
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
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.FilterUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * デマンドデータ実績取得処理（全体・建物・テナント一覧・使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllSummaryBuildingUsedDataDao extends OsolApiDao<DemandAllSummaryBuildingUsedDataParameter> {

    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final CommonDemandWeekReportLineListServiceDaoImpl commonDemandWeekReportLineListServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllSummaryBuildingUsedDataDao() {
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        commonDemandWeekReportLineListServiceDaoImpl = new CommonDemandWeekReportLineListServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllSummaryBuildingUsedDataResult query(DemandAllSummaryBuildingUsedDataParameter parameter)
            throws Exception {
        DemandAllSummaryBuildingUsedDataResult result = new DemandAllSummaryBuildingUsedDataResult();

        List<AllBuildingListDetailResultData> buildingList;
        List<DemandCalendarYearData> corpYearCalList = null;
        List<DemandWeekCorpCalListDetailResultData> corpWeekCalList = null;
        List<DemandCalendarYearData> buildingYearCalList = null;
        List<DemandWeekBuildingCalListDetailResultData> buildingWeekCalList = null;
        List<CommonDemandMonthReportLineListResult> yesterdayResult;
        List<CommonDemandWeekReportLineListResult> beforeWeekResult = null;
        List<CommonDemandYearReportLineListResult> thisYearResult = null;
        TargetFiscalYearWeekNoResult targetBeforeWeek = null;
        TargetFiscalYearMonthNoResult targetThisMonth = null;
        TargetFiscalYearSummaryRangeResult targetThisYear = null;
        List<DemandAllSummaryBuildingUsedDataBuildingResultData> detail = new ArrayList<>();
        List<CorpDemandSelectResult> corpDemandList = null;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllSummaryBuildingUsedDataResult();
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
            return new DemandAllSummaryBuildingUsedDataResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllSummaryBuildingUsedDataResult();
        }

        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合、企業集計カレンダを作成する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
            if (corpDemandList == null || corpDemandList.size() != 1) {
                return new DemandAllSummaryBuildingUsedDataResult();
            }
            corpYearCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                    corpDemandList.get(0).getSumDate());
            if (corpYearCalList == null || corpYearCalList.isEmpty()) {
                return new DemandAllSummaryBuildingUsedDataResult();
            }
            corpWeekCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());
            if (corpWeekCalList == null || corpWeekCalList.isEmpty()) {
                return new DemandAllSummaryBuildingUsedDataResult();
            }
        }

        //建物件数分処理を繰り返す
        for (AllBuildingListDetailResultData buildingResult : buildingList) {

            List<BuildingDemandListDetailResultData> buildingDemandList = null;

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
            //今月実績の初期化
            BigDecimal thisMonthValue = null;
            //今年実績の初期化
            BigDecimal thisYearValue = null;

            if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                //建物集計の場合、建物デマンド情報を取得する
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
                buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
                //建物集計カレンダを作成する
                buildingYearCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                        buildingDemandList.get(0).getSumDate());
                if (buildingYearCalList == null || buildingYearCalList.isEmpty()) {
                    detailResult.setCorpId(buildingResult.getCorpId());
                    detailResult.setBuildingId(buildingResult.getBuildingId());
                    detailResult.setBuildingNo(buildingResult.getBuildingNo());
                    detailResult.setBuildingType(buildingResult.getBuildingType());
                    detailResult.setBuildingName(buildingResult.getBuildingName());
                    continue;
                }
                buildingWeekCalList = DemandEmsUtility.getWeekBuildingCalList(buildingDemandList.get(0),
                        getServerDateTime());
                if (buildingWeekCalList == null || buildingWeekCalList.isEmpty()) {
                    detailResult.setCorpId(buildingResult.getCorpId());
                    detailResult.setBuildingId(buildingResult.getBuildingId());
                    detailResult.setBuildingNo(buildingResult.getBuildingNo());
                    detailResult.setBuildingType(buildingResult.getBuildingType());
                    detailResult.setBuildingName(buildingResult.getBuildingName());
                    continue;
                }
            }

            //デマンド系統月報テーブルから前日のデータを取得する
            CommonDemandMonthReportLineListResult monthLineParam = DemandEmsUtility.getMonthReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                    DateUtility.plusDay(DateUtility.conversionDate(
                            DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD), -1),
                    DateUtility.plusDay(DateUtility.conversionDate(
                            DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD), -1));
            yesterdayResult = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthLineParam);

            //前週の年度・週番号を取得する
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                targetBeforeWeek = DemandEmsUtility.getTargetCorpFiscalYearWeekNo(corpWeekCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        new BigDecimal(-1));
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                targetBeforeWeek = DemandEmsUtility.getTargetBuildingFiscalYearWeekNo(buildingWeekCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        new BigDecimal(-1));
            } else {
                targetBeforeWeek = null;
            }

            //デマンド系統週報テーブルから前週のデータを取得する
            if (targetBeforeWeek != null) {
                CommonDemandWeekReportLineListResult weekLineParam = DemandEmsUtility.getWeekReportLineListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        targetBeforeWeek.getFiscalYear(), targetBeforeWeek.getFiscalYear(),
                        targetBeforeWeek.getWeekNo(), targetBeforeWeek.getWeekNo());
                beforeWeekResult = getResultList(commonDemandWeekReportLineListServiceDaoImpl, weekLineParam);
            }

            //今月のカレンダ年月を取得する
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                targetThisMonth = DemandEmsUtility.getTargetFiscalYearMonthNo(corpYearCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        BigDecimal.ZERO, corpDemandList.get(0).getSumDate());
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                targetThisMonth = DemandEmsUtility.getTargetFiscalYearMonthNo(buildingYearCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        BigDecimal.ZERO, buildingDemandList.get(0).getSumDate());
            }

            //今年の集計範囲を取得する
            if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
                targetThisYear = DemandEmsUtility.getTargetFiscalYearSummaryRange(corpYearCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        BigDecimal.ZERO, corpDemandList.get(0).getSumDate());
            } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
                targetThisYear = DemandEmsUtility.getTargetFiscalYearSummaryRange(buildingYearCalList,
                        DateUtility.conversionDate(
                                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                                DateUtility.DATE_FORMAT_YYYYMMDD),
                        BigDecimal.ZERO, buildingDemandList.get(0).getSumDate());
            }

            //デマンド系統年報テーブルから今年度のデータを取得する
            if (targetThisYear != null) {
                CommonDemandYearReportLineListResult yearLineParam = DemandEmsUtility.getYearReportLineListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        targetThisYear.getCalYearFrom(), targetThisYear.getCalYearTo(),
                        targetThisYear.getCalMonthFrom(),
                        targetThisYear.getCalMonthTo());
                thisYearResult = getResultList(commonDemandYearReportLineListServiceDaoImpl, yearLineParam);
            }

            //明細の設定
            detailResult.setCorpId(buildingResult.getCorpId());
            detailResult.setBuildingId(buildingResult.getBuildingId());
            detailResult.setBuildingNo(buildingResult.getBuildingNo());
            detailResult.setBuildingType(buildingResult.getBuildingType());
            detailResult.setBuildingName(buildingResult.getBuildingName());
            if (yesterdayResult != null && yesterdayResult.size() == 1) {
                //1件の場合のみ設定
                detailResult.setLastDayData(yesterdayResult.get(0).getLineValueKwh().setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                detailResult.setLastDayData(null);
            }
            if (beforeWeekResult != null && beforeWeekResult.size() == 1) {
                //1件の場合のみ設定
                detailResult.setLastWeekData(beforeWeekResult.get(0).getLineValueKwh().setScale(
                        parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            } else {
                detailResult.setLastWeekData(null);
            }
            for (CommonDemandYearReportLineListResult temp : thisYearResult) {
                //今年の計算
                if (thisYearValue == null) {
                    thisYearValue = temp.getLineValueKwh();
                } else {
                    thisYearValue = thisYearValue.add(temp.getLineValueKwh());
                }

                //今月の計算
                if (targetThisMonth != null) {
                    if (targetThisMonth.getCalYear().compareTo(temp.getYearNo()) == 0
                            && targetThisMonth.getMonthNo().compareTo(temp.getMonthNo()) == 0) {
                        //年月が一致する場合
                        thisMonthValue = temp.getLineValueKwh();

                    }
                }
            }

            if (thisMonthValue == null) {
                detailResult.setThisMonthData(thisMonthValue);
            } else {
                detailResult.setThisMonthData(thisMonthValue.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }

            if (thisYearValue == null) {
                detailResult.setThisYearData(thisYearValue);
            } else {
                detailResult.setThisYearData(thisYearValue.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }

            detail.add(detailResult);
        }

        if (!CheckUtility.isNullOrEmpty(parameter.getSortKey())
                && !CheckUtility.isNullOrEmpty(parameter.getSortOrder())) {
            //指定のキーでソートする
            if (!detail.isEmpty()) {
                detail = SortUtility.sortDemandAllBuildingList(detail,
                        ApiCodeValueConstants.ALL_SUMMARY_DETAIL_KIND.USED.getVal(), parameter.getSortKey(),
                        parameter.getSortOrder());
            }
        }

        result.setDetail(
                new DemandAllSummaryBuildingUsedDataDetailResultData(ApiSimpleConstants.UNIT_USE_POWER, detail));
        return result;
    }

}
