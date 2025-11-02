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

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryPowerDayReductionAnalysisParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryPowerDayReductionAnalysisResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerAnalysisDetailHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンドデータ実績取得処理（全体・毎日の削減率評価）Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllSummaryPowerDayReductionAnalysisDao
        extends OsolApiDao<DemandAllSummaryPowerDayReductionAnalysisParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllSummaryPowerDayReductionAnalysisDao() {
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllSummaryPowerDayReductionAnalysisResult query(
            DemandAllSummaryPowerDayReductionAnalysisParameter parameter) throws Exception {
        DemandAllSummaryPowerDayReductionAnalysisResult result = new DemandAllSummaryPowerDayReductionAnalysisResult();

        DemandAllSummaryPowerDayUsedAnalysisDetailResultData tempDetail;

        List<AllBuildingListDetailResultData> buildingList;
        SummaryRangeForMonthResult summaryRange;
        DemandAllSummaryPowerDayUsedAnalysisHeaderResultData header = new DemandAllSummaryPowerDayUsedAnalysisHeaderResultData();
        List<DemandAllSummaryPowerAnalysisDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        List<DemandAllSummaryPowerDayUsedAnalysisTimeResultData> detail = new ArrayList<>();
        Date currentDate = null;
        List<CommonDemandMonthReportLineListResult> tempReportList;

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllSummaryPowerDayReductionAnalysisResult();
        }

        //日付がNULLの場合、当日日付を設定
        if (parameter.getYmd() == null) {
            parameter.setYmd(getServerDateTime());
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、2（ヶ月）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(new BigDecimal("2"));
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
            return new DemandAllSummaryPowerDayReductionAnalysisResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllSummaryPowerDayReductionAnalysisResult();
        }

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForMonth(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getSumPeriodCalcType(),
                parameter.getSumPeriod());

        //ヘッダ情報を作成する（件数はいったん0を設定）
        header.setCorpId(parameter.getOperationCorpId());
        header.setUnit(null);
        header.setSummaryBuildingCount(0);
        header.setSummaryFrom(DateUtility.changeDateFormat(summaryRange.getMeasurementDateFrom(),
                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
        header.setSummaryTo(DateUtility.changeDateFormat(summaryRange.getMeasurementDateTo(),
                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
        header.setWeek54Change(false);
        do {

            if (currentDate == null) {
                currentDate = summaryRange.getMeasurementDateFrom();
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            //日付と曜日をセットする
            detailHeaderList.add(new DemandAllSummaryPowerAnalysisDetailHeaderResultData(
                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                    DateUtility.getDayOfWeekDay(currentDate)));

        } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

        header.setDetailHeaderList(detailHeaderList);

        //明細ヘッダの件数分、明細部リストに0を設定する
        for (int i = 1; i <= header.getDetailHeaderList().size(); i++) {
            detail.add(new DemandAllSummaryPowerDayUsedAnalysisTimeResultData());
        }

        //建物件数分処理を繰り返す
        for (AllBuildingListDetailResultData buildingResult : buildingList) {

            //建物機器情報を取得する
            DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                    .getBuildingSmListParam(buildingResult.getCorpId(), buildingResult.getBuildingId(), null);
            List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                    demandBuildingSmListServiceDaoImpl, buildingSmParam);

            if (buildingSmList == null || buildingSmList.isEmpty()) {
                //建物機器情報が存在しない場合は次のレコードへ
                continue;
            }

            //ヘッダの件数のカウントアップ
            header.setSummaryBuildingCount(header.getSummaryBuildingCount() + 1);

            //デマンド月報系統テーブルからデータを取得する（現在時間）
            CommonDemandMonthReportLineListResult currentMonthReportParam = DemandEmsUtility
                    .getMonthReportLineListParam(buildingResult.getCorpId(), buildingResult.getBuildingId(),
                            parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                            summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo());
            tempReportList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, currentMonthReportParam);

            if (tempReportList != null && !tempReportList.isEmpty()) {
                //データがある場合のみ処理を行う
                //計測年月日でソートする
                tempReportList = SortUtility.sortCommonDemandMonthReportLineListByMeasurement(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandMonthReportLineListResult temp : tempReportList) {
                    Date headerDate;

                    for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                        headerDate = DateUtility.conversionDate(header.getDetailHeaderList().get(i).getInfo1(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                        if (headerDate.equals(temp.getMeasurementDate())) {
                            //日付が同じ場合、実績を積算してループを抜ける
                            if (detail.get(i).getCurrentValue() == null) {
                                detail.get(i).setCurrentValue(temp.getLineValueKwh());
                            } else {
                                detail.get(i)
                                        .setCurrentValue(detail.get(i).getCurrentValue().add(temp.getLineValueKwh()));
                            }
                            break;
                        }
                    }
                }
            }

            //デマンド月報系統テーブルからデータを取得する（1年前）
            CommonDemandMonthReportLineListResult beforeMonthReportParam = DemandEmsUtility.getMonthReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                    DateUtility.plusYear(summaryRange.getMeasurementDateFrom(), -1),
                    DateUtility.plusYear(summaryRange.getMeasurementDateTo(), -1));
            tempReportList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, beforeMonthReportParam);

            if (tempReportList != null && !tempReportList.isEmpty()) {
                //データがある場合のみ処理を行う
                //指定年月日でソートする
                tempReportList = SortUtility.sortCommonDemandMonthReportLineListByMeasurement(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandMonthReportLineListResult temp : tempReportList) {
                    Date headerDate;

                    for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                        //ヘッダの日付を1年前にする
                        headerDate = DateUtility
                                .plusYear(DateUtility.conversionDate(header.getDetailHeaderList().get(i).getInfo1(),
                                        DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), -1);

                        if (headerDate.equals(temp.getMeasurementDate())) {
                            //日付が同じ場合、実績を積算してループを抜ける
                            if (detail.get(i).getCompareTargetValue() == null) {
                                detail.get(i).setCompareTargetValue(temp.getLineValueKwh());
                            } else {
                                detail.get(i).setCompareTargetValue(
                                        detail.get(i).getCompareTargetValue().add(temp.getLineValueKwh()));
                            }
                            break;
                        }
                    }
                }
            }
        }

        tempDetail = new DemandAllSummaryPowerDayUsedAnalysisDetailResultData(header, detail);

        //削減率評価のデータにする
        result.setDetail(DemandEmsAllUtility.setReductionAnalysis(tempDetail, parameter.getPrecision(),
                parameter.getBelowAccuracyControl()));
        return result;
    }

}
