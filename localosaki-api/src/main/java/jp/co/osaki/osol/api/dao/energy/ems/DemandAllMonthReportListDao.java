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
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllMonthReportListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllMonthReportListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllMonthReportListDetailHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllMonthReportListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllMonthReportListHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンドデータ実績取得処理（全体・月報用） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllMonthReportListDao extends OsolApiDao<DemandAllMonthReportListParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonDemandMonthReportLineListServiceDaoImpl commonDemandMonthReportLineListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllMonthReportListDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonDemandMonthReportLineListServiceDaoImpl = new CommonDemandMonthReportLineListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllMonthReportListResult query(DemandAllMonthReportListParameter parameter) throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        SummaryRangeForMonthResult summaryRange;
        DemandAllMonthReportListHeaderResultData header = new DemandAllMonthReportListHeaderResultData();
        DemandAllMonthReportListDetailResultData detail;
        List<DemandAllMonthReportListDetailResultData> resultList = new ArrayList<>();
        CorpDemandSelectResult corpResult;
        List<BuildingDemandListDetailResultData> buildingDemandList;
        List<BigDecimal> timeResult;
        List<CommonDemandMonthReportLineListResult> tempReportList;
        Date currentDate = null;
        List<DemandAllMonthReportListDetailHeaderResultData> detailHeaderList = new ArrayList<>();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllMonthReportListResult();
        }

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

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        if (lineList == null || lineList.size() != 1) {
            //1件以外はnullを返して処理を終了する
            return null;
        }

        //対象の系統がデマンド値かどうかを判定する
        if (!ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
            //ロギング以外はデマンド値
            demandFlg = true;
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
            return new DemandAllMonthReportListResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            //ヘッダの件数のみ設定し、処理を終了する
            return new DemandAllMonthReportListResult();
        }

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForMonth(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダー情報を作成する
        header.setDayFrom(summaryRange.getMeasurementDateFrom());
        header.setDayTo(summaryRange.getMeasurementDateTo());
        if (demandFlg) {
            header.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnit(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }

        do {

            if (currentDate == null) {
                currentDate = summaryRange.getMeasurementDateFrom();
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            //日付と曜日をセットする
            detailHeaderList.add(new DemandAllMonthReportListDetailHeaderResultData(
                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                    DateUtility.getDayOfWeekDay(currentDate)));

        } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));
        header.setDetailHeaderList(detailHeaderList);
        header.setSummaryBuildingCount(0);

        //企業デマンド情報を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandAllMonthReportListResult();
        }
        corpResult = corpDemandList.get(0);

        //建物・テナント番号でソートする
        buildingList = SortUtility.sortCommonBuildingListByBuildingNo(buildingList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

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

            //サマリー値の初期化
            BigDecimal summary = null;
            BigDecimal max = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //明細部の初期化
            detail = new DemandAllMonthReportListDetailResultData();

            //明細部の設定（建物情報のみ）
            detail.setCorpId(buildingResult.getCorpId());
            detail.setBuildingId(buildingResult.getBuildingId());
            detail.setBuildingNo(buildingResult.getBuildingNo());
            detail.setBuildingType(buildingResult.getBuildingType());
            detail.setBuildingName(buildingResult.getBuildingName());

            if (corpResult != null) {
                //企業デマンド情報が取得できた場合
                detail.setCorpSummaryDate(corpResult.getSumDate());
            }

            //時間帯別インデックスの初期化
            Date current = null;

            //時間帯別実績の初期化
            timeResult = new ArrayList<>();

            //集計デマンド系統情報を取得する
            AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), null, null, null);
            List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                    aggregateDmLineListServiceDaoImpl, aggregateLineParam);
            if (aggregateLineList != null && aggregateLineList.size() == 1) {
                //集計デマンド情報を取得する
                AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(),
                        aggregateLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                        aggregateParam);
                if (aggregateList != null && aggregateList.size() != 1) {
                    detail.setPowerSummaryDate(aggregateList.get(0).getSumDate());
                }
            }

            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData demandParam = DemandEmsUtility
                    .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, demandParam);
            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                //建物情報のみ返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            } else {
                if (CheckUtility.isNullOrEmpty(detail.getPowerSummaryDate())) {
                    detail.setPowerSummaryDate(buildingDemandList.get(0).getSumDate());
                }

            }

            //デマンド月報系統テーブルからデータを取得する
            CommonDemandMonthReportLineListResult monthLineParam = DemandEmsUtility.getMonthReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), summaryRange.getMeasurementDateFrom(), summaryRange.getMeasurementDateTo());
            tempReportList = getResultList(commonDemandMonthReportLineListServiceDaoImpl, monthLineParam);

            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }

            //計測年月日でソートする
            tempReportList = SortUtility.sortCommonDemandMonthReportLineListByMeasurement(tempReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //取得した件数分処理を繰り返す
            for (CommonDemandMonthReportLineListResult temp : tempReportList) {

                //サマリー値の計算
                if (temp.getLineValueKwh() != null) {
                    //null以外に実施
                    if (summary == null) {
                        summary = temp.getLineValueKwh();
                    } else {
                        summary = summary.add(temp.getLineValueKwh());
                    }

                    if (averageCount == null) {
                        averageCount = BigDecimal.ONE;
                    } else {
                        averageCount = averageCount.add(BigDecimal.ONE);
                    }

                    if (max == null) {
                        max = temp.getLineValueKwh();
                    } else if (temp.getLineValueKwh().compareTo(max) == 1) {
                        //最大より大きい場合
                        max = temp.getLineValueKwh();
                    }

                    if (min == null) {
                        min = temp.getLineValueKwh();
                    } else if (temp.getLineValueKwh().compareTo(min) == -1) {
                        //最小より小さい場合
                        min = temp.getLineValueKwh();
                    }
                }

                //実績値を詰める（一致するまで処理を行う）
                while (true) {

                    if (current == null) {
                        current = summaryRange.getMeasurementDateFrom();
                    } else {
                        //1日進める
                        current = DateUtility.plusDay(current, 1);
                    }

                    if (current.equals(temp.getMeasurementDate())) {
                        //インデックスと一致する場合は、時間帯実績をつめ、ループを抜ける
                        timeResult.add(temp.getLineValueKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                        break;
                    } else {
                        //一致しない場合はnullを詰める
                        timeResult.add(null);
                    }
                }

            }

            //timeResultの要素数がdetailHeaderListの要素数と一致するまで、nullを詰めて追加する
            while (timeResult.size() != header.getDetailHeaderList().size()) {
                timeResult.add(null);
            }

            //平均値の計算
            BigDecimal average;
            if (averageCount == null) {
                average = null;
            } else if (summary == null) {
                average = null;
            } else {
                average = summary.divide(averageCount, parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //合計値
            if (summary != null) {
                summary = summary.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //最大値
            if (max != null) {
                max = max.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //最小値
            if (min != null) {
                min = min.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //明細部の設定（実績）
            detail.setTimeResult(timeResult);
            detail.setSummary(summary);
            detail.setMax(max);
            detail.setMin(min);
            detail.setAverage(average);
            resultList.add(detail);

        }

        return new DemandAllMonthReportListResult(header, resultList);
    }

    /**
     * 空の明細を作成する
     *
     * @param detail
     * @param header
     */
    private void setEmptyDetail(DemandAllMonthReportListDetailResultData detail,
            DemandAllMonthReportListHeaderResultData header) throws Exception {
        List<BigDecimal> emptyResult = new ArrayList<>();
        if (header == null || header.getDetailHeaderList() == null || header.getDetailHeaderList().isEmpty()) {
            return;
        }

        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
            emptyResult.add(null);
        }

        detail.setTimeResult(emptyResult);
        detail.setSummary(null);
        detail.setMax(null);
        detail.setMin(null);
        detail.setAverage(null);
    }

}
