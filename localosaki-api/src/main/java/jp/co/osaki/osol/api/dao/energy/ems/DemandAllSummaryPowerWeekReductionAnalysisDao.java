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

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryPowerWeekReductionAnalysisResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForWeekResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerAnalysisDetailHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンドデータ実績取得処理（全体・週間削減率評価）Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllSummaryPowerWeekReductionAnalysisDao
        extends OsolApiDao<DemandAllSummaryPowerWeekReductionAnalysisParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonDemandWeekReportLineListServiceDaoImpl commonDemandWeekReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllSummaryPowerWeekReductionAnalysisDao() {
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonDemandWeekReportLineListServiceDaoImpl = new CommonDemandWeekReportLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllSummaryPowerWeekReductionAnalysisResult query(
            DemandAllSummaryPowerWeekReductionAnalysisParameter parameter) throws Exception {
        DemandAllSummaryPowerWeekReductionAnalysisResult result = new DemandAllSummaryPowerWeekReductionAnalysisResult();
        DemandAllSummaryPowerDayUsedAnalysisDetailResultData tempDetail;

        List<AllBuildingListDetailResultData> buildingList;
        List<DemandWeekCorpCalListDetailResultData> corpCalList;
        List<String> week54ExistList;
        SummaryRangeForWeekResult summaryRange;
        String currentWeek;
        String currentFiscalYear = "";
        BigDecimal currentWeekNo = BigDecimal.ZERO;
        DemandAllSummaryPowerDayUsedAnalysisHeaderResultData header = new DemandAllSummaryPowerDayUsedAnalysisHeaderResultData();
        List<DemandAllSummaryPowerAnalysisDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        List<DemandAllSummaryPowerDayUsedAnalysisTimeResultData> detail = new ArrayList<>();
        List<CommonDemandWeekReportLineListResult> tempReportList;
        String fiscalYearFrom;
        String fiscalYearTo;

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllSummaryPowerWeekReductionAnalysisResult();
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

        //比較対象がNULLの場合、前年値を設定
        if (CheckUtility.isNullOrEmpty(parameter.getCompareTarget())) {
            parameter.setCompareTarget(ApiCodeValueConstants.COMPARE_TARGET.LAST_YEAR.getVal());
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
            return new DemandAllSummaryPowerWeekReductionAnalysisResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllSummaryPowerWeekReductionAnalysisResult();
        }

        //企業デマンドを取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandAllSummaryPowerWeekReductionAnalysisResult();
        }

        //デマンド週報企業カレンダからデータを取得する
        corpCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());

        //年度の範囲でフィルタリングする
        if (ApiCodeValueConstants.RANGE_UNIT.WEEK.getVal().equals(parameter.getRangeUnit())) {
            //週単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear())
                    .subtract(parameter.getSumPeriod().divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP)).toString();
            fiscalYearTo = new BigDecimal(parameter.getFiscalYear())
                    .add(parameter.getSumPeriod().divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP))
                    .toString();
            corpCalList = corpCalList.stream()
                    .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                            && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                    .collect(Collectors.toList());
        } else if (ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal().equals(parameter.getRangeUnit())) {
            //年単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear()).subtract(parameter.getSumPeriod()).toString();
            fiscalYearTo = new BigDecimal(parameter.getFiscalYear()).add(parameter.getSumPeriod()).toString();
            corpCalList = corpCalList.stream()
                    .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                            && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                    .collect(Collectors.toList());
        }

        if (corpCalList == null || corpCalList.isEmpty()) {
            return null;
        }

        //54週あり年度を取得する
        week54ExistList = DemandDataUtility.getWeek54ListForCompany(corpCalList);

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

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForWeek(parameter.getFiscalYear(), parameter.getWeekNo(),
                week54ExistList, parameter.getSumPeriodCalcType(), parameter.getRangeUnit(), parameter.getSumPeriod());

        //ヘッダ情報を作成する（件数はいったん0を設定）
        header.setCorpId(parameter.getOperationCorpId());
        header.setUnit(null);
        header.setSummaryBuildingCount(0);
        header.setSummaryFrom(summaryRange.getFiscalYearFrom().concat(ApiSimpleConstants.HYPHEN).concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getWeekNoFrom().intValue())));
        header.setSummaryTo(summaryRange.getFiscalYearTo().concat(ApiSimpleConstants.HYPHEN).concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getWeekNoTo().intValue())));

        //年度+週番号単位でMapを作成する
        Map<String, Date> weekNoMap = corpCalList.stream()
                .collect(Collectors.toMap(
                        x -> x.getFiscalYear().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
        currentWeek = "";
        do {

            if (CheckUtility.isNullOrEmpty(currentWeek)) {
                currentWeek = summaryRange.getFiscalYearFrom().concat(String
                        .format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getWeekNoFrom().intValue()));
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

            detailHeaderList.add(new DemandAllSummaryPowerAnalysisDetailHeaderResultData(
                    ApiSimpleConstants.WEEK_NO.concat(currentWeekNo.toString()).concat(ApiSimpleConstants.WEEK),
                    DateUtility.changeDateFormat(weekNoMap.get(currentWeek), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM)));

        } while (!currentWeek.equals(summaryRange.getFiscalYearTo().concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getWeekNoTo().intValue()))));

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

            //デマンド週報系統テーブルからデータを取得する（現在年度）
            CommonDemandWeekReportLineListResult currentWeekParam = DemandEmsUtility.getWeekReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(),
                    ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), summaryRange.getFiscalYearFrom(),
                    summaryRange.getFiscalYearTo(),
                    summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo());
            tempReportList = getResultList(commonDemandWeekReportLineListServiceDaoImpl, currentWeekParam);

            if (tempReportList != null && !tempReportList.isEmpty()) {
                //データがある場合のみ処理を行う
                //年度、週番号でソートする
                tempReportList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandWeekReportLineListResult temp : tempReportList) {
                    String headerNendo = "";
                    BigDecimal headerWeek = BigDecimal.ZERO;

                    for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                        if (i == 0) {
                            headerNendo = summaryRange.getFiscalYearFrom();
                            headerWeek = summaryRange.getWeekNoFrom();
                        } else if (headerWeek.compareTo(new BigDecimal(54)) == 0) {
                            headerNendo = new BigDecimal(headerNendo).add(BigDecimal.ONE).toString();
                            headerWeek = BigDecimal.ONE;
                        } else if (headerWeek.compareTo(new BigDecimal(53)) == 0) {
                            if (week54ExistList.contains(currentFiscalYear)) {
                                headerWeek = headerWeek.add(BigDecimal.ONE);
                            } else {
                                headerNendo = new BigDecimal(headerNendo).add(BigDecimal.ONE).toString();
                                headerWeek = BigDecimal.ONE;
                            }
                        } else {
                            headerWeek = headerWeek.add(BigDecimal.ONE);
                        }

                        if (headerWeek.compareTo(temp.getWeekNo()) == 0) {
                            //年度と週番号が同じ場合、実績を積算してループを抜ける
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

            //デマンド週報系統テーブルからデータを取得する（比較対象用）
            CommonDemandWeekReportLineListResult compareWeekParam = new CommonDemandWeekReportLineListResult();
            if (ApiCodeValueConstants.COMPARE_TARGET.LAST_YEAR.getVal().equals(parameter.getCompareTarget())) {
                //前年値と比較の場合は1年前のデータを取得する
                compareWeekParam = DemandEmsUtility.getWeekReportLineListParam(buildingResult.getCorpId(),
                        buildingResult.getBuildingId(), ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        String.valueOf(new BigDecimal(summaryRange.getFiscalYearFrom()).subtract(BigDecimal.ONE)),
                        String.valueOf(new BigDecimal(summaryRange.getFiscalYearTo()).subtract(BigDecimal.ONE)),
                        summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo());
                tempReportList = getResultList(commonDemandWeekReportLineListServiceDaoImpl, compareWeekParam);
                if (tempReportList != null && !tempReportList.isEmpty()) {
                    //データがある場合のみ処理を行う
                    //年度、週番号でソートする
                    tempReportList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(tempReportList,
                            ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                    //取得した件数分処理を繰り返す
                    for (CommonDemandWeekReportLineListResult temp : tempReportList) {
                        String headerNendo = "";
                        BigDecimal headerWeek = BigDecimal.ZERO;

                        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                            if (i == 0) {
                                headerNendo = new BigDecimal(summaryRange.getFiscalYearFrom()).subtract(BigDecimal.ONE)
                                        .toString();
                                headerWeek = summaryRange.getWeekNoFrom();
                            } else if (headerWeek.compareTo(new BigDecimal(54)) == 0) {
                                headerNendo = new BigDecimal(headerNendo).add(BigDecimal.ONE).toString();
                                headerWeek = BigDecimal.ONE;
                            } else if (headerWeek.compareTo(new BigDecimal(53)) == 0) {
                                if (week54ExistList.contains(currentFiscalYear)) {
                                    headerWeek = headerWeek.add(BigDecimal.ONE);
                                } else {
                                    headerNendo = new BigDecimal(headerNendo).add(BigDecimal.ONE).toString();
                                    headerWeek = BigDecimal.ONE;
                                }
                            } else {
                                headerWeek = headerWeek.add(BigDecimal.ONE);
                            }

                            if (headerNendo.equals(temp.getFiscalYear())
                                    && headerWeek.compareTo(temp.getWeekNo()) == 0) {
                                //年度と週番号が同じ場合、実績を積算してループを抜ける
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
            } else {
                //導入前値と比較の場合は年度0の1週～54週の値を取得する
                compareWeekParam = DemandEmsUtility.getWeekReportLineListParam(buildingResult.getCorpId(),
                        buildingResult.getBuildingId(), ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), "0", "0",
                        BigDecimal.ONE, new BigDecimal("54"));
                tempReportList = getResultList(commonDemandWeekReportLineListServiceDaoImpl, compareWeekParam);
                if (tempReportList != null && !tempReportList.isEmpty()) {
                    //データがある場合のみ処理を行う
                    //年度、週番号でソートする
                    tempReportList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(tempReportList,
                            ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                    //取得した件数分処理を繰り返す
                    for (CommonDemandWeekReportLineListResult temp : tempReportList) {
                        BigDecimal headerWeek = BigDecimal.ZERO;

                        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                            if (i == 0) {
                                headerWeek = summaryRange.getWeekNoFrom();
                            } else if (headerWeek.compareTo(new BigDecimal(54)) == 0) {
                                headerWeek = BigDecimal.ONE;
                            } else if (headerWeek.compareTo(new BigDecimal(53)) == 0) {
                                if (week54ExistList.contains(currentFiscalYear)) {
                                    headerWeek = headerWeek.add(BigDecimal.ONE);
                                } else {
                                    headerWeek = BigDecimal.ONE;
                                }
                            } else {
                                headerWeek = headerWeek.add(BigDecimal.ONE);
                            }

                            if (headerWeek.compareTo(temp.getWeekNo()) == 0) {
                                //年度と週番号が同じ場合、実績を積算する（週のみで比較するためループを抜けない）
                                if (detail.get(i).getCompareTargetValue() == null) {
                                    detail.get(i).setCompareTargetValue(temp.getLineValueKwh());
                                } else {
                                    detail.get(i).setCompareTargetValue(
                                            detail.get(i).getCompareTargetValue().add(temp.getLineValueKwh()));
                                }
                            }
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
