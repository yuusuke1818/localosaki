/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllYearReportListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllYearReportListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllYearReportListDetailHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllYearReportListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllYearReportListHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
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
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンドデータ実績取得処理（全体・年報用） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllYearReportListDao extends OsolApiDao<DemandAllYearReportListParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllYearReportListDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllYearReportListResult query(DemandAllYearReportListParameter parameter) throws Exception {
        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllYearReportListResult();
        }

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

        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            return getDemandYearReportListForCompany(parameter);
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            return getDemandYearReportListForBuilding(parameter);
        } else {
            return new DemandAllYearReportListResult();
        }
    }

    /**
     * デマンドデータ実績を取得する（企業集計）
     * @param parameter
     * @return
     * @throws Exception
     */
    private DemandAllYearReportListResult getDemandYearReportListForCompany(DemandAllYearReportListParameter parameter)
            throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        DemandAllYearReportListHeaderResultData header = new DemandAllYearReportListHeaderResultData();
        List<DemandAllYearReportListDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        DemandAllYearReportListDetailResultData detail;
        List<DemandAllYearReportListDetailResultData> resultList = new ArrayList<>();
        List<DemandCalendarYearData> corpCalList;
        SummaryRangeForYearResult summaryRange;
        String currentYm;
        List<BigDecimal> timeUsedResult;
        List<BigDecimal> timeMaxResult;
        List<CommonDemandYearReportLineListResult> tempReportList;
        CurrentYearReportResult currentYearReport;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        if (lineList == null || lineList.size() != 1) {
            //1件以外はnullを返して処理を終了する
            return new DemandAllYearReportListResult();
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
            return new DemandAllYearReportListResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllYearReportListResult();
        }

        //企業集計カレンダ（年報）を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandAllYearReportListResult();
        }

        corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());
        if (corpCalList == null || corpCalList.isEmpty()) {
            return new DemandAllYearReportListResult();
        }

        //現在集計中の年月を取得する
        currentYearReport = DemandEmsUtility.getCurrentYearReport(corpCalList, getServerDateTime(),
                corpDemandList.get(0).getSumDate());
        if (currentYearReport == null) {
            return new DemandAllYearReportListResult();
        }

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(), parameter.getMonth(),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダ情報の設定
        if (demandFlg) {
            header.setUnitUsed(ApiSimpleConstants.UNIT_USE_POWER);
            header.setUnitMax(ApiSimpleConstants.UNIT_DEMAND);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnitUsed(lineList.get(0).getLineUnit());
            header.setUnitMax(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }

        header.setYearFrom(summaryRange.getYearFrom());
        header.setYearTo(summaryRange.getYearTo());
        header.setMonthFrom(summaryRange.getMonthFrom());
        header.setMonthTo(summaryRange.getMonthTo());
        header.setSummaryBuildingCount(0);

        //年No、月No単位でMapを作成する
        Map<String, Date> monthStartMap = corpCalList.stream()
                .collect(Collectors.toMap(
                        x -> x.getYearNo().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                        x -> x.getMonthStartDate()));
        currentYm = "";

        do {

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
            detailHeaderList.add(new DemandAllYearReportListDetailHeaderResultData(
                    DateUtility.changeDateFormat(DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                            DateUtility.DATE_FORMAT_YYYYMM_SLASH),
                    DateUtility.changeDateFormat(monthStartMap.get(currentYm), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM)));

        } while (!currentYm.equals(summaryRange.getYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthTo().intValue()))));

        header.setDetailHeaderList(detailHeaderList);

        if (buildingList == null || buildingList.isEmpty()) {
            //0件の場合、ヘッダのみ設定して処理を終了
            return new DemandAllYearReportListResult(header, null);
        }

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
            BigDecimal summaryForAverage = null;
            BigDecimal maxUsed = null;
            BigDecimal maxMax = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //明細部の初期化
            detail = new DemandAllYearReportListDetailResultData();

            //明細部の設定（建物情報のみ）
            detail.setCorpId(buildingResult.getCorpId());
            detail.setBuildingId(buildingResult.getBuildingId());
            detail.setBuildingNo(buildingResult.getBuildingNo());
            detail.setBuildingType(buildingResult.getBuildingType());
            detail.setBuildingName(buildingResult.getBuildingName());

            detail.setSummaryDate(corpDemandList.get(0).getSumDate());

            //時間帯別インデックスの初期化
            String current = "";

            //時間帯別実績の初期化
            timeUsedResult = new ArrayList<>();
            timeMaxResult = new ArrayList<>();

            //デマンド年報系統テーブルからデータを取得する
            CommonDemandYearReportLineListResult yearLineParam = DemandEmsUtility.getYearReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                    parameter.getLineGroupId(), parameter.getLineNo(), summaryRange.getYearFrom(),
                    summaryRange.getYearTo(), summaryRange.getMonthFrom(), summaryRange.getMonthTo());
            tempReportList = getResultList(commonDemandYearReportLineListServiceDaoImpl, yearLineParam);
            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
            } else {

                if (tempReportList.isEmpty()) {
                    //実績が取得できない場合、建物情報のみを返して次のレコードへ
                    setEmptyDetail(detail, header);
                    resultList.add(detail);
                    continue;
                }

                //年No、月Noでソートする
                tempReportList = SortUtility.sortCommonDemandYearReportLineListByYearNoMonthNo(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandYearReportLineListResult temp : tempReportList) {

                    //サマリー値の計算
                    if (temp.getLineValueKwh() != null) {
                        //使用量がnull以外の場合に実施
                        if (summary == null) {
                            summary = temp.getLineValueKwh();
                        } else {
                            summary = summary.add(temp.getLineValueKwh());
                        }

                        if (maxUsed == null) {
                            maxUsed = temp.getLineValueKwh();
                        } else if (temp.getLineValueKwh().compareTo(maxUsed) == 1) {
                            //最大より大きい場合
                            maxUsed = temp.getLineValueKwh();
                        }

                        if (Integer.parseInt(currentYearReport.getCalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        currentYearReport.getMonthNo().intValue()))) > Integer
                                                .parseInt(temp.getYearNo().concat(
                                                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                                temp.getMonthNo().intValue())))) {

                            //現在集計中の年月または未来の年月の場合（※データはないはずであるが対応しておく）、合計と平均の計算から除外する
                            if (summaryForAverage == null) {
                                summaryForAverage = temp.getLineValueKwh();
                            } else {
                                summaryForAverage = summaryForAverage.add(temp.getLineValueKwh());
                            }

                            if (averageCount == null) {
                                averageCount = BigDecimal.ONE;
                            } else {
                                averageCount = averageCount.add(BigDecimal.ONE);
                            }

                            if (min == null) {
                                min = temp.getLineValueKwh();
                            } else if (temp.getLineValueKwh().compareTo(min) == -1) {
                                //最小より小さい場合
                                min = temp.getLineValueKwh();
                            }
                        }
                    }

                    if (temp.getLineMaxKw() != null) {
                        //最大値がnull以外の場合に実施
                        if (maxMax == null) {
                            maxMax = temp.getLineMaxKw();
                        } else if (temp.getLineMaxKw().compareTo(maxMax) == 1) {
                            //最大より大きい場合
                            maxMax = temp.getLineMaxKw();
                        }
                    }

                    //実績値を詰める（一致するまで処理を行う）
                    while (true) {

                        //インデックスを変更する
                        if (CheckUtility.isNullOrEmpty(current)) {
                            current = summaryRange.getYearFrom().concat(
                                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                            summaryRange.getMonthFrom().intValue()));
                        } else if (current.substring(4).equals("12")) {
                            current = String.valueOf(Integer.parseInt(current.substring(0, 4)) + 1).concat("01");
                        } else {
                            current = current.substring(0, 4).concat(
                                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                            Integer.parseInt(current.substring(4)) + 1));
                        }

                        if (current.equals(temp.getYearNo()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        temp.getMonthNo().intValue())))) {
                            //インデックスと一致する場合は実績を詰めてループを抜ける
                            timeUsedResult.add(temp.getLineValueKwh().setScale(parameter.getPrecision(),
                                    ApiCodeValueConstants.PRECISION_CONTROL
                                            .getControlType(parameter.getBelowAccuracyControl())));

                            timeMaxResult.add(
                                    temp.getLineMaxKw().setScale(parameter.getPrecision(),
                                            ApiCodeValueConstants.PRECISION_CONTROL
                                                    .getControlType(parameter.getBelowAccuracyControl())));

                            break;
                        } else {
                            //一致しない場合はnullを詰める
                            timeUsedResult.add(null);
                            timeMaxResult.add(null);
                        }
                    }
                }

                //timeResultの要素数がdetailHeaderListの要素数と一致するまで、nullを詰めて追加する
                while (timeUsedResult.size() != header.getDetailHeaderList().size()) {
                    timeUsedResult.add(null);
                }

                while (timeMaxResult.size() != header.getDetailHeaderList().size()) {
                    timeMaxResult.add(null);
                }

                //平均値の計算
                BigDecimal average;
                if (averageCount == null) {
                    average = null;
                } else if (summaryForAverage == null) {
                    average = null;
                } else {
                    average = summaryForAverage.divide(averageCount, parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl()));
                }

                //合計値
                if (summary != null) {
                    summary = summary.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl()));
                }

                //最大値（使用量）
                if (maxUsed != null) {
                    maxUsed = maxUsed.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl()));
                }

                //最大値（最大値）
                if (maxMax != null) {
                    maxMax = maxMax.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl()));
                }

                //最小値
                if (min != null) {
                    min = min.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl()));
                }

                //明細部の設定（実績）
                detail.setTimeUsedResult(timeUsedResult);
                detail.setTimeMaxResult(timeMaxResult);
                detail.setSummary(summary);
                detail.setMaxUsed(maxUsed);
                detail.setMaxMax(maxMax);
                detail.setMin(min);
                detail.setAverage(average);
                detail.setMonthStartDate(null);
            }

            //デマンド月報データを取得する（目標電力）
            Date topDate = DateUtility.conversionDate(
                    header.getDetailHeaderList().get(0).getMonthStartDate().replace(ApiSimpleConstants.FROM, ""),
                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), topDate, topDate);
            List<CommonDemandMonthReportListResult> monthRepList = getResultList(
                    commonDemandMonthReportListServiceDaoImpl, monthReportParam);
            if (monthRepList != null && monthRepList.size() == 1) {
                detail.setTargetPower(monthRepList.get(0).getTargetKw().setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }

            //契約電力
            BuildingDemandListDetailResultData demandParam = DemandEmsUtility
                    .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
            List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                    demandParam);
            if (demandList == null || demandList.size() != 1) {
                detail.setContractPower(null);
            } else {
                detail.setContractPower(demandList.get(0).getContractKw());
            }

            resultList.add(detail);

        }

        return new DemandAllYearReportListResult(header, resultList);
    }

    /**
     * デマンドデータ実績を取得する（建物集計）
     * @param parameter
     * @return
     * @throws Exception
     */
    private DemandAllYearReportListResult getDemandYearReportListForBuilding(DemandAllYearReportListParameter parameter)
            throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        Map<Long, List<DemandCalendarYearData>> buildingCalMap = new HashMap<>();
        DemandAllYearReportListHeaderResultData header = new DemandAllYearReportListHeaderResultData();
        List<DemandAllYearReportListDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        DemandAllYearReportListDetailResultData detail;
        List<DemandAllYearReportListDetailResultData> resultList = new ArrayList<>();
        List<DemandCalendarYearData> buildingCalList = null;
        SummaryRangeForYearResult summaryRange;
        String currentYm;
        List<String> monthStart;
        List<BigDecimal> timeUsedResult;
        List<BigDecimal> timeMaxResult;
        List<CommonDemandYearReportLineListResult> tempReportList;
        CurrentYearReportResult currentYearReport;
        List<BuildingDemandListDetailResultData> buildingDemandList;

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

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
        if (!ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
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
            return new DemandAllYearReportListResult();
        }

        //ヘッダ情報の設定（単位情報）
        if (demandFlg) {
            header.setUnitUsed(ApiSimpleConstants.UNIT_USE_POWER);
            header.setUnitMax(ApiSimpleConstants.UNIT_DEMAND);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnitUsed(lineList.get(0).getLineUnit());
            header.setUnitMax(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }
        header.setSummaryBuildingCount(0);

        if (buildingList == null || buildingList.isEmpty()) {
            //0件の場合、ヘッダのみ設定して処理を終了
            return new DemandAllYearReportListResult(header, null);
        }

        //建物・テナント番号でソートする
        buildingList = SortUtility.sortCommonBuildingListByBuildingNo(buildingList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        //建物件数分処理を繰り返す（カレンダ情報取得・カレンダマップ作成・機器マップ作成用）
        for (AllBuildingListDetailResultData buildingResult : buildingList) {

            String sumDate = null;

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

            //集計デマンド系統情報を取得する
            AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), null, null, null);
            List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                    aggregateDmLineListServiceDaoImpl, aggregateLineParam);

            if (aggregateLineList != null && aggregateLineList.size() == 1) {
                //データが取得できた場合、集計デマンド情報を取得する
                AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                        buildingResult.getCorpId(), buildingResult.getBuildingId(),
                        aggregateLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                        aggregateParam);
                if (aggregateList != null && aggregateList.size() == 1) {
                    sumDate = aggregateList.get(0).getSumDate();
                }
            }

            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.isEmpty()) {
                //建物デマンドが取得できない場合、次のレコードへ
                continue;
            }

            //建物集計カレンダ（年報）を取得する
            if (CheckUtility.isNullOrEmpty(sumDate)) {
                //集計デマンドから集計時が取得できていない場合は建物デマンド情報から集計時を設定
                sumDate = buildingDemandList.get(0).getSumDate();
            }
            buildingCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo, sumDate);
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                //カレンダが取得できない場合、カレンダ情報にnullを設定して次のレコードへ
                buildingCalMap.put(buildingResult.getBuildingId(), null);
                continue;
            } else {
                buildingCalMap.put(buildingResult.getBuildingId(), buildingCalList);
            }

        }

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(), parameter.getMonth(),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダ情報の設定（集計範囲の情報）
        header.setYearFrom(summaryRange.getYearFrom());
        header.setYearTo(summaryRange.getYearTo());
        header.setMonthFrom(summaryRange.getMonthFrom());
        header.setMonthTo(summaryRange.getMonthTo());
        currentYm = "";

        do {

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
            detailHeaderList.add(new DemandAllYearReportListDetailHeaderResultData(DateUtility.changeDateFormat(
                    DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                    DateUtility.DATE_FORMAT_YYYYMM_SLASH), null));

        } while (!currentYm.equals(summaryRange.getYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthTo().intValue()))));

        header.setDetailHeaderList(detailHeaderList);

        //建物件数分処理を繰り返す（実績取得用）
        for (AllBuildingListDetailResultData buildingResult : buildingList) {

            //サマリー値の初期化
            BigDecimal summary = null;
            BigDecimal summaryForAverage = null;
            BigDecimal maxUsed = null;
            BigDecimal maxMax = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //カレンダMapの初期化
            List<DemandCalendarYearData> calList;
            Map<String, Date> calMap;

            //明細部の初期化
            detail = new DemandAllYearReportListDetailResultData();

            //明細部の設定（建物情報のみ）
            detail.setCorpId(buildingResult.getCorpId());
            detail.setBuildingId(buildingResult.getBuildingId());
            detail.setBuildingNo(buildingResult.getBuildingNo());
            detail.setBuildingType(buildingResult.getBuildingType());
            detail.setBuildingName(buildingResult.getBuildingName());

            if (buildingCalMap.get(buildingResult.getBuildingId()) == null
                    || buildingCalMap.get(buildingResult.getBuildingId()).isEmpty()) {
                //建物カレンダ情報が取得できない場合は、次のレコードへ
                continue;
            } else {
                //取得できた場合、対象建物のMapを作成する
                calList = buildingCalMap.get(buildingResult.getBuildingId());
                calMap = calList.stream()
                        .collect(Collectors.toMap(
                                x -> x.getYearNo().concat(
                                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                x.getMonthNo().intValue())),
                                x -> x.getMonthStartDate()));
            }

            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            } else {
                detail.setSummaryDate(buildingDemandList.get(0).getSumDate());
            }

            //現在集計中の年月を取得する
            currentYearReport = DemandEmsUtility.getCurrentYearReport(calList, getServerDateTime(),
                    buildingDemandList.get(0).getSumDate());
            if (currentYearReport == null) {
                continue;
            }
            //時間帯別インデックスの初期化
            String current = "";

            //時間帯別実績の初期化
            timeUsedResult = new ArrayList<>();
            timeMaxResult = new ArrayList<>();

            //月開始日の初期化
            monthStart = new ArrayList<>();

            currentYm = "";

            do {

                if (CheckUtility.isNullOrEmpty(currentYm)) {
                    currentYm = summaryRange.getYearFrom()
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    summaryRange.getMonthFrom().intValue()));
                } else if (currentYm.substring(4).equals("12")) {
                    //12月の場合年を1年足して1月に
                    currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
                } else {
                    //1ヶ月足す
                    currentYm = currentYm.substring(0, 4).concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    Integer.parseInt(currentYm.substring(4)) + 1));
                }
                monthStart
                        .add(DateUtility.changeDateFormat(calMap.get(currentYm), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                .concat(ApiSimpleConstants.FROM));

            } while (!currentYm.equals(summaryRange.getYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRange.getMonthTo().intValue()))));

            detail.setMonthStartDate(monthStart);

            //デマンド年報系統テーブルからデータを取得する
            CommonDemandYearReportLineListResult yearLineParam = DemandEmsUtility.getYearReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                    parameter.getLineGroupId(), parameter.getLineNo(), summaryRange.getYearFrom(),
                    summaryRange.getYearTo(), summaryRange.getMonthFrom(), summaryRange.getMonthTo());
            tempReportList = getResultList(commonDemandYearReportLineListServiceDaoImpl, yearLineParam);

            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }

            if (tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }

            //年No、月Noでソートする
            tempReportList = SortUtility.sortCommonDemandYearReportLineListByYearNoMonthNo(tempReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //取得した件数分処理を繰り返す
            for (CommonDemandYearReportLineListResult temp : tempReportList) {
                //サマリー値の計算
                if (temp.getLineValueKwh() != null) {
                    //使用量がnull以外の場合に実施
                    if (summary == null) {
                        summary = temp.getLineValueKwh();
                    } else {
                        summary = summary.add(temp.getLineValueKwh());
                    }

                    if (maxUsed == null) {
                        maxUsed = temp.getLineValueKwh();
                    } else if (temp.getLineValueKwh().compareTo(maxUsed) == 1) {
                        //最大より大きい場合
                        maxUsed = temp.getLineValueKwh();
                    }

                    if (Integer.parseInt(currentYearReport.getCalYear().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    currentYearReport.getMonthNo().intValue()))) > Integer
                                            .parseInt(temp.getYearNo().concat(
                                                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            temp.getMonthNo().intValue())))) {
                        //現在集計中の年月または未来の年月の場合（※データはないはずであるが対応しておく）、合計と平均の計算から除外する
                        if (summaryForAverage == null) {
                            summaryForAverage = temp.getLineValueKwh();
                        } else {
                            summaryForAverage = summaryForAverage.add(temp.getLineValueKwh());
                        }

                        if (averageCount == null) {
                            averageCount = BigDecimal.ONE;
                        } else {
                            averageCount = averageCount.add(BigDecimal.ONE);
                        }

                        if (min == null) {
                            min = temp.getLineValueKwh();
                        } else if (temp.getLineValueKwh().compareTo(min) == -1) {
                            //最小より小さい場合
                            min = temp.getLineValueKwh();
                        }

                    }
                }

                if (temp.getLineMaxKw() != null) {
                    //最大値がnull以外の場合に実施
                    if (maxMax == null) {
                        maxMax = temp.getLineMaxKw();
                    } else if (temp.getLineMaxKw().compareTo(maxMax) == 1) {
                        //最大より大きい場合
                        maxMax = temp.getLineMaxKw();
                    }
                }

                //実績値を詰める（一致するまで処理を行う）
                while (true) {

                    //インデックスを変更する
                    if (CheckUtility.isNullOrEmpty(current)) {
                        current = summaryRange.getYearFrom()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        summaryRange.getMonthFrom().intValue()));
                    } else if (current.substring(4).equals("12")) {
                        current = String.valueOf(Integer.parseInt(current.substring(0, 4)) + 1).concat("01");
                    } else {
                        current = current.substring(0, 4).concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        Integer.parseInt(current.substring(4)) + 1));
                    }

                    if (current.equals(temp.getYearNo()
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    temp.getMonthNo().intValue())))) {
                        //インデックスと一致する場合は実績を詰めてループを抜ける
                        timeUsedResult.add(
                                temp.getLineValueKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));

                        timeMaxResult.add(
                                temp.getLineMaxKw().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));

                        break;
                    } else {
                        //一致しない場合はnullを詰める
                        timeUsedResult.add(null);
                        timeMaxResult.add(null);
                    }
                }
            }

            //timeResultの要素数がdetailHeaderListの要素数と一致するまで、nullを詰めて追加する
            while (timeUsedResult.size() != header.getDetailHeaderList().size()) {
                timeUsedResult.add(null);
            }

            while (timeMaxResult.size() != header.getDetailHeaderList().size()) {
                timeMaxResult.add(null);
            }

            //平均値の計算
            BigDecimal average;
            if (averageCount == null) {
                average = null;
            } else if (summaryForAverage == null) {
                average = null;
            } else {
                average = summaryForAverage.divide(averageCount, parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //合計値
            if (summary != null) {
                summary = summary.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //最大値（使用量）
            if (maxUsed != null) {
                maxUsed = maxUsed.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //最大値（最大値）
            if (maxMax != null) {
                maxMax = maxMax.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //最小値
            if (min != null) {
                min = min.setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl()));
            }

            //デマンド月報データを取得する（目標電力）
            Date topDate = DateUtility.conversionDate(
                    detail.getMonthStartDate().get(0).replace(ApiSimpleConstants.FROM, ""),
                    DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), topDate, topDate);
            List<CommonDemandMonthReportListResult> monthRepList = getResultList(
                    commonDemandMonthReportListServiceDaoImpl, monthReportParam);

            //明細部の設定（実績）
            detail.setTimeUsedResult(timeUsedResult);
            detail.setTimeMaxResult(timeMaxResult);
            detail.setSummary(summary);
            detail.setMaxUsed(maxUsed);
            detail.setMaxMax(maxMax);
            detail.setMin(min);
            detail.setAverage(average);
            if (monthRepList != null && monthRepList.size() == 1) {
                detail.setTargetPower(monthRepList.get(0).getTargetKw().setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }
            detail.setContractPower(buildingDemandList.get(0).getContractKw());
            resultList.add(detail);

        }

        return new DemandAllYearReportListResult(header, resultList);
    }

    /**
     * 空の明細を作成する
     *
     * @param detail
     * @param header
     */
    private void setEmptyDetail(DemandAllYearReportListDetailResultData detail,
            DemandAllYearReportListHeaderResultData header) {
        List<BigDecimal> emptyResult = new ArrayList<>();
        List<String> emptyMonthStart = new ArrayList<>();

        if (header == null || header.getDetailHeaderList() == null || header.getDetailHeaderList().isEmpty()) {
            return;
        }

        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
            emptyResult.add(null);
            emptyMonthStart.add(null);
        }

        detail.setTimeUsedResult(emptyResult);
        detail.setTimeMaxResult(emptyResult);
        if (detail.getMonthStartDate() == null || detail.getMonthStartDate().isEmpty()) {
            detail.setMonthStartDate(emptyMonthStart);
        }
        detail.setSummary(null);
        detail.setMaxUsed(null);
        detail.setMaxMax(null);
        detail.setMin(null);
        detail.setAverage(null);
        detail.setContractPower(null);
        detail.setTargetPower(null);
    }

}
