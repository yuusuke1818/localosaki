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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllSummaryPowerMonthUsedAnalysisParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllSummaryPowerMonthUsedAnalysisResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
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
import jp.co.osaki.osol.api.servicedao.common.CommonDemandYearReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンドデータ実績取得処理（全体・毎月の電力量評価）Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllSummaryPowerMonthUsedAnalysisDao
        extends OsolApiDao<DemandAllSummaryPowerMonthUsedAnalysisParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonDemandYearReportLineListServiceDaoImpl commonDemandYearReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllSummaryPowerMonthUsedAnalysisDao() {
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonDemandYearReportLineListServiceDaoImpl = new CommonDemandYearReportLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllSummaryPowerMonthUsedAnalysisResult query(
            DemandAllSummaryPowerMonthUsedAnalysisParameter parameter) throws Exception {
        DemandAllSummaryPowerMonthUsedAnalysisResult result = new DemandAllSummaryPowerMonthUsedAnalysisResult();
        DemandAllSummaryPowerDayUsedAnalysisDetailResultData tempDetail;

        List<AllBuildingListDetailResultData> buildingList;
        List<DemandCalendarYearData> corpCalList;
        SummaryRangeForYearResult summaryRange;
        String currentYm;
        DemandAllSummaryPowerDayUsedAnalysisHeaderResultData header = new DemandAllSummaryPowerDayUsedAnalysisHeaderResultData();
        List<DemandAllSummaryPowerAnalysisDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        List<DemandAllSummaryPowerDayUsedAnalysisTimeResultData> detail = new ArrayList<>();
        List<CommonDemandYearReportLineListResult> tempReportList;
        List<CommonDemandYearReportLineListResult> tempInnovateReportList;

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllSummaryPowerMonthUsedAnalysisResult();
        }

        //年報カレンダ取得にあたっての範囲指定
        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
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
            return new DemandAllSummaryPowerMonthUsedAnalysisResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllSummaryPowerMonthUsedAnalysisResult();
        }

        //企業デマンドを取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandAllSummaryPowerMonthUsedAnalysisResult();
        }

        //デマンド年報企業カレンダからデータを取得する
        corpCalList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());

        if (corpCalList == null || corpCalList.isEmpty()) {
            return new DemandAllSummaryPowerMonthUsedAnalysisResult();
        }

        //集計範囲を取得する
        summaryRange = SummaryRangeUtility.getSummaryRangeForYear(parameter.getYear(), parameter.getMonth(),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダ情報を作成する（件数はいったん0を設定）
        header.setCorpId(parameter.getOperationCorpId());
        header.setUnit(null);
        header.setSummaryBuildingCount(0);
        header.setSummaryFrom(DateUtility.changeDateFormat(DateUtility.conversionDate(
                summaryRange.getYearFrom()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getMonthFrom().intValue())),
                DateUtility.DATE_FORMAT_YYYYMM), DateUtility.DATE_FORMAT_YYYYMM_SLASH));
        header.setSummaryTo(DateUtility.changeDateFormat(DateUtility.conversionDate(
                summaryRange.getYearTo()
                        .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                summaryRange.getMonthTo().intValue())),
                DateUtility.DATE_FORMAT_YYYYMM), DateUtility.DATE_FORMAT_YYYYMM_SLASH));
        header.setWeek54Change(false);

        //カレンダ年月単位でMapを作成する
        Map<String, Date> monthStartMap = corpCalList.stream()
                .collect(Collectors.toMap(
                        x -> x.getYearNo().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getMonthNo().intValue())),
                        x -> x.getMonthStartDate()));
        currentYm = "";

        do {

            if (CheckUtility.isNullOrEmpty(currentYm)) {
                currentYm = summaryRange.getYearFrom().concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getMonthFrom().intValue()));
            } else if (currentYm.substring(4).equals("12")) {
                //12月の場合年を1年足して1月に
                currentYm = String.valueOf(Integer.parseInt(currentYm.substring(0, 4)) + 1).concat("01");
            } else {
                //1ヶ月足す
                currentYm = currentYm.substring(0, 4).concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        Integer.parseInt(currentYm.substring(4)) + 1));
            }

            detailHeaderList.add(new DemandAllSummaryPowerAnalysisDetailHeaderResultData(
                    DateUtility.changeDateFormat(DateUtility.conversionDate(currentYm, DateUtility.DATE_FORMAT_YYYYMM),
                            DateUtility.DATE_FORMAT_YYYYMM_SLASH),
                    DateUtility.changeDateFormat(monthStartMap.get(currentYm), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM)));

        } while (!currentYm.equals(summaryRange.getYearTo().concat(
                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, summaryRange.getMonthTo().intValue()))));

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

            //デマンド年報系統テーブルからデータを取得する（現在年度）
            CommonDemandYearReportLineListResult currentYearParam = DemandEmsUtility.getYearReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(),
                    ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), summaryRange.getYearFrom(),
                    summaryRange.getYearTo(), summaryRange.getMonthFrom(), summaryRange.getMonthTo());
            tempReportList = getResultList(commonDemandYearReportLineListServiceDaoImpl, currentYearParam);

            if (tempReportList != null && !tempReportList.isEmpty()) {
                //データがある場合のみ処理を行う

                //年No、月Noでソートする
                tempReportList = SortUtility.sortCommonDemandYearReportLineListByYearNoMonthNo(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandYearReportLineListResult temp : tempReportList) {
                    String headerYm;

                    for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                        headerYm = DateUtility.changeDateFormat(
                                DateUtility.conversionDate(header.getDetailHeaderList().get(i).getInfo1(),
                                        DateUtility.DATE_FORMAT_YYYYMM_SLASH),
                                DateUtility.DATE_FORMAT_YYYYMM);

                        if (headerYm.equals(temp.getYearNo().concat(String
                                .format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, temp.getMonthNo().intValue())))) {
                            //年月が同じ場合、実績を積算してループを抜ける
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

            //デマンド年報系統テーブルからデータを取得する（比較対象用）
            CommonDemandYearReportLineListResult compareYearParam = new CommonDemandYearReportLineListResult();
            if (ApiCodeValueConstants.COMPARE_TARGET.LAST_YEAR.getVal().equals(parameter.getCompareTarget())) {
                //前年値と比較の場合は1年前のデータを取得する
                compareYearParam = DemandEmsUtility.getYearReportLineListParam(buildingResult.getCorpId(),
                        buildingResult.getBuildingId(), ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(),
                        String.valueOf(Integer.parseInt(summaryRange.getYearFrom()) - 1),
                        String.valueOf(Integer.parseInt(summaryRange.getYearTo()) - 1), summaryRange.getMonthFrom(),
                        summaryRange.getMonthTo());
                tempReportList = getResultList(commonDemandYearReportLineListServiceDaoImpl, compareYearParam);

                if (tempReportList != null && !tempReportList.isEmpty()) {
                    //データがある場合のみ処理を行う

                    //年No、月Noでソートする
                    tempReportList = SortUtility.sortCommonDemandYearReportLineListByYearNoMonthNo(tempReportList,
                            ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                    //取得した件数分処理を繰り返す
                    for (CommonDemandYearReportLineListResult temp : tempReportList) {
                        String headerYm;

                        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
                            headerYm = DateUtility.changeDateFormat(
                                    DateUtility.plusYear(
                                            DateUtility.conversionDate(header.getDetailHeaderList().get(i).getInfo1(),
                                                    DateUtility.DATE_FORMAT_YYYYMM_SLASH),
                                            -1),
                                    DateUtility.DATE_FORMAT_YYYYMM);

                            if (headerYm.equals(temp.getYearNo().concat(String.format(
                                    StringUtility.STRING_FORMAT_ZERO_PADDING_2, temp.getMonthNo().intValue())))) {
                                //年月が同じ場合、実績を積算してループを抜ける
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
                //導入前値と比較の場合は年度0の1月～12月を取得する
                compareYearParam = DemandEmsUtility.getYearReportLineListParam(buildingResult.getCorpId(),
                        buildingResult.getBuildingId(), ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal(),
                        parameter.getLineGroupId(), ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), "0", "0",
                        null, null);
                tempInnovateReportList = getResultList(commonDemandYearReportLineListServiceDaoImpl, compareYearParam);
                if (tempInnovateReportList != null && !tempInnovateReportList.isEmpty()) {
                    //データがある場合のみ処理を行う

                    //取得した件数分処理を繰り返す
                    for (CommonDemandYearReportLineListResult temp : tempInnovateReportList) {
                        BigDecimal headerMonth;

                        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {

                            headerMonth = new BigDecimal(header.getDetailHeaderList().get(i).getInfo1().substring(5));
                            if (headerMonth.compareTo(temp.getMonthNo()) == 0) {
                                //月番号が同じ場合、実績を積算する（月のみで比較するためループを抜けない）
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

        //電力量評価のデータにする
        result.setDetail(DemandEmsAllUtility.setUsedAnalysis(tempDetail, parameter.getPrecision(),
                parameter.getBelowAccuracyControl()));

        return result;
    }

}
