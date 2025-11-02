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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllWeekReportListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllWeekReportListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.utility.CurrentWeekReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForWeekResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllWeekReportListDetailHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllWeekReportListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllWeekReportListHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * デマンドデータ実績取得処理（全体・週報用） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllWeekReportListDao extends OsolApiDao<DemandAllWeekReportListParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonDemandWeekReportLineListServiceDaoImpl commonDemandWeekReportLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllWeekReportListDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonDemandWeekReportLineListServiceDaoImpl = new CommonDemandWeekReportLineListServiceDaoImpl();
    }

    @Override
    public DemandAllWeekReportListResult query(DemandAllWeekReportListParameter parameter) throws Exception {

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllWeekReportListResult();
        }

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

        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            return getDemandWeekReportListForCompany(parameter);
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            return getDemandWeekReportListForBuilding(parameter);
        } else {
            return new DemandAllWeekReportListResult();
        }
    }

    /**
     * デマンドデータ実績を取得する（企業集計）
     *
     * @param parameter
     * @return
     */
    private DemandAllWeekReportListResult getDemandWeekReportListForCompany(
            DemandAllWeekReportListParameter parameter) throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        DemandAllWeekReportListHeaderResultData header = new DemandAllWeekReportListHeaderResultData();
        List<String> week54ExistList;
        List<DemandWeekCorpCalListDetailResultData> corpCalList;
        List<DemandWeekCorpCalListDetailResultData> filterCorpCalList;
        SummaryRangeForWeekResult summaryRange;
        List<DemandAllWeekReportListDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        String currentWeek;
        String currentFiscalYear = "";
        BigDecimal currentWeekNo = BigDecimal.ZERO;
        CorpDemandSelectResult corpResult;
        DemandAllWeekReportListDetailResultData detail;
        List<BigDecimal> timeResult;
        List<DemandAllWeekReportListDetailResultData> resultList = new ArrayList<>();
        List<CommonDemandWeekReportLineListResult> tempReportList;
        CurrentWeekReportResult currentWeekReport;
        String fiscalYearFrom;
        String fiscalYearTo;

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        if (lineList == null || lineList.size() != 1) {
            //1件以外はnullを返して処理を終了する
            return new DemandAllWeekReportListResult();
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
            return new DemandAllWeekReportListResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllWeekReportListResult();
        }

        //企業集計カレンダ（週報）を取得
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandAllWeekReportListResult();
        }

        corpCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());
        if (corpCalList == null || corpCalList.isEmpty()) {
            return new DemandAllWeekReportListResult();
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

        filterCorpCalList = corpCalList.stream()
                .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                        && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                .collect(Collectors.toList());

        //現在集計中の年度・週番号を取得する
        currentWeekReport = DemandEmsUtility.getCurrentCorpWeekReport(corpCalList, getServerDateTime());
        if (currentWeekReport == null) {
            return new DemandAllWeekReportListResult();
        }
        //54週あり年度を取得する
        week54ExistList = DemandDataUtility.getWeek54ListForCompany(filterCorpCalList);

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

        //ヘッダ情報の設定
        if (demandFlg) {
            header.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnit(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }
        header.setFiscalYearFrom(summaryRange.getFiscalYearFrom());
        header.setWeekNoFrom(summaryRange.getWeekNoFrom());
        header.setFiscalYearTo(summaryRange.getFiscalYearTo());
        header.setWeekNoTo(summaryRange.getWeekNoTo());
        header.setSummaryBuildingCount(0);

        //年度+週No単位でMapを作成する
        Map<String, Date> weekNoMap = corpCalList.stream()
                .collect(Collectors.toMap(
                        x -> x.getFiscalYear().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
        currentWeek = "";

        do {

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

            detailHeaderList.add(new DemandAllWeekReportListDetailHeaderResultData(
                    ApiSimpleConstants.WEEK_NO.concat(currentWeekNo.toString()).concat(ApiSimpleConstants.WEEK),
                    DateUtility.changeDateFormat(weekNoMap.get(currentWeek), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM)));

        } while (!currentWeek.equals(summaryRange.getFiscalYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getWeekNoTo().intValue()))));

        header.setDetailHeaderList(detailHeaderList);

        //企業デマンド情報を取得する
        corpResult = corpDemandList.get(0);

        if (buildingList == null || buildingList.isEmpty()) {
            //0件の場合、ヘッダのみ設定して処理を終了
            return new DemandAllWeekReportListResult(header, null);
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
            BigDecimal max = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //明細部の初期化
            detail = new DemandAllWeekReportListDetailResultData();

            //明細部の設定（建物情報のみ）
            detail.setCorpId(buildingResult.getCorpId());
            detail.setBuildingId(buildingResult.getBuildingId());
            detail.setBuildingNo(buildingResult.getBuildingNo());
            detail.setBuildingType(buildingResult.getBuildingType());
            detail.setBuildingName(buildingResult.getBuildingName());

            if (corpResult != null) {
                //集計日が取得できた場合
                detail.setSummaryDate(corpResult.getWeekStartDay().substring(0, 2).concat(ApiSimpleConstants.SLASH)
                        .concat(corpResult.getWeekStartDay().substring(2, 4)));
            }

            //時間帯別インデックスの初期化
            String currentYear = "";
            BigDecimal currentNo = BigDecimal.ZERO;

            //時間帯別実績の初期化
            timeResult = new ArrayList<>();

            //デマンド週報系統テーブルからデータを取得する
            CommonDemandWeekReportLineListResult weekLineParam = DemandEmsUtility.getWeekReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                    parameter.getLineGroupId(), parameter.getLineNo(), summaryRange.getFiscalYearFrom(),
                    summaryRange.getFiscalYearTo(), summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo());
            tempReportList = getResultList(commonDemandWeekReportLineListServiceDaoImpl, weekLineParam);

            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }

            //年度、週Noでソートする
            tempReportList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(tempReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //取得した件数分処理を繰り返す
            for (CommonDemandWeekReportLineListResult temp : tempReportList) {

                //サマリー値の計算
                if (temp.getLineValueKwh() != null) {
                    //null以外の場合に実施
                    if (summary == null) {
                        summary = temp.getLineValueKwh();
                    } else {
                        summary = summary.add(temp.getLineValueKwh());
                    }

                    if (max == null) {
                        max = temp.getLineValueKwh();
                    } else if (temp.getLineValueKwh().compareTo(max) == 1) {
                        //最大より大きい場合
                        max = temp.getLineValueKwh();
                    }

                    if (Integer.parseInt(currentWeekReport.getFiscalYear().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    currentWeekReport.getWeekNo().intValue()))) > Integer
                                            .parseInt(temp.getFiscalYear().concat(
                                                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            temp.getWeekNo().intValue())))) {
                        //現在集計中の週または未来の週の場合（※データはないはずであるが対応しておく）、合計と平均の計算から除外する

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

                //実績値を詰める（一致するまで処理を行う）
                while (true) {

                    //インデックスを変更する
                    if (CheckUtility.isNullOrEmpty(currentYear)) {
                        currentYear = summaryRange.getFiscalYearFrom();
                        currentNo = summaryRange.getWeekNoFrom();
                    } else if (currentNo.compareTo(new BigDecimal(54)) == 0) {
                        //年度と週番号を変更
                        currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                        currentNo = BigDecimal.ONE;
                    } else if (currentNo.compareTo(new BigDecimal(53)) == 0) {
                        if (week54ExistList == null || week54ExistList.isEmpty()) {
                            //年度と週番号を変更
                            currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                            currentNo = BigDecimal.ONE;
                        } else if (week54ExistList.contains(currentYear)) {
                            //54週がある場合、週番号のみ変更
                            currentNo = currentNo.add(BigDecimal.ONE);
                        } else {
                            //年度と週番号を変更
                            currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                            currentNo = BigDecimal.ONE;
                        }
                    } else {
                        //週番号のみ変更
                        currentNo = currentNo.add(BigDecimal.ONE);
                    }

                    if (currentYear.equals(temp.getFiscalYear()) && currentNo.compareTo(temp.getWeekNo()) == 0) {
                        //インデックスと一致する場合は実績を詰め、ループを抜ける
                        timeResult.add(
                                temp.getLineValueKwh().setScale(parameter.getPrecision(),
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
            detail.setWeekStartDate(null);
            resultList.add(detail);
        }

        return new DemandAllWeekReportListResult(header, resultList);
    }

    /**
     * デマンドデータ実績を取得する（建物集計）
     *
     * @param parameter
     * @return
     */
    private DemandAllWeekReportListResult getDemandWeekReportListForBuilding(DemandAllWeekReportListParameter parameter)
            throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        DemandAllWeekReportListHeaderResultData header = new DemandAllWeekReportListHeaderResultData();
        DemandAllWeekReportListDetailResultData detail;
        List<DemandAllWeekReportListDetailHeaderResultData> detailHeaderList = new ArrayList<>();
        List<BigDecimal> timeResult;
        List<String> weekStart;
        List<DemandAllWeekReportListDetailResultData> resultList = new ArrayList<>();
        List<DemandWeekBuildingCalListDetailResultData> buildingCalList = null;
        List<DemandWeekBuildingCalListDetailResultData> filterBuildingCalList;
        List<String> week54ExistList;
        List<String> week54TotalList = new ArrayList<>();
        Map<Long, List<DemandWeekBuildingCalListDetailResultData>> buildingCalMap = new HashMap<>();
        SummaryRangeForWeekResult summaryRange;
        List<CommonDemandWeekReportLineListResult> tempReportList;
        String currentWeek;
        String currentFiscalYear = "";
        BigDecimal currentWeekNo = BigDecimal.ZERO;
        CurrentWeekReportResult currentWeekReport;
        String fiscalYearFrom;
        String fiscalYearTo;
        List<BuildingDemandListDetailResultData> buildingDemandList = null;

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        if (lineList == null || lineList.size() != 1) {
            //1件以外はnullを返して処理を終了する
            return new DemandAllWeekReportListResult();
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
            return new DemandAllWeekReportListResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllWeekReportListResult();
        }

        //ヘッダ情報の設定（単位情報）
        if (demandFlg) {
            header.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnit(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }

        if (buildingList == null || buildingList.isEmpty()) {
            //0件の場合、ヘッダのみ設定して処理を終了
            return new DemandAllWeekReportListResult(header, null);
        }
        header.setSummaryBuildingCount(0);

        //建物・テナント番号でソートする
        buildingList = SortUtility.sortCommonBuildingListByBuildingNo(buildingList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        //カレンダ絞込みの年度の範囲を取得しておく
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

        //建物件数分処理を繰り返す（カレンダ情報取得・カレンダマップ作成・機器マップ作成用）
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

            //建物集計カレンダ（週報）を取得する
            BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                    .getBuildingDemandListParam(buildingResult.getCorpId(), buildingResult.getBuildingId());
            buildingDemandList = getResultList(buildingDemandListServiceDaoImpl, buildingDemandParam);
            if (buildingDemandList == null || buildingDemandList.isEmpty()) {
                //取得できない場合は、建物情報のみを返して次のレコードへ
                detail = new DemandAllWeekReportListDetailResultData();
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }
            buildingCalList = DemandEmsUtility.getWeekBuildingCalList(buildingDemandList.get(0),
                    getServerDateTime());
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                //カレンダが取得できない場合、カレンダ情報にnullを設定して次のレコードへ
                buildingCalMap.put(buildingResult.getBuildingId(), null);
                continue;
            }

            //取得したカレンダを年度でフィルタリングする
            filterBuildingCalList = buildingCalList.stream()
                    .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(fiscalYearFrom)
                            && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(fiscalYearTo))
                    .collect(Collectors.toList());

            if (filterBuildingCalList == null || filterBuildingCalList.isEmpty()) {
                //カレンダが取得できない場合、カレンダ情報にnullを設定して次のレコードへ
                buildingCalMap.put(buildingResult.getBuildingId(), null);
                continue;
            } else {
                buildingCalMap.put(buildingResult.getBuildingId(), filterBuildingCalList);
            }

            //54週あり年度を取得する
            week54ExistList = DemandDataUtility.getWeek54ListForPower(filterBuildingCalList);

            if (week54ExistList == null || week54ExistList.isEmpty()) {

            } else {
                for (String year : week54ExistList) {
                    if (!week54TotalList.contains(year)) {
                        week54TotalList.add(year);
                    }
                }
            }

        }

        //54週あり年度に対象の年度が存在するかチェックする
        if (parameter.getWeekNo().compareTo(new BigDecimal("54")) == 0) {
            if (week54TotalList.isEmpty()) {
                //データがない場合は変更
                header.setWeek54Change(true);
                parameter.setWeekNo(new BigDecimal("53"));
            } else if (week54TotalList.contains(parameter.getFiscalYear())) {
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
                week54TotalList, parameter.getSumPeriodCalcType(), parameter.getRangeUnit(), parameter.getSumPeriod());

        //ヘッダ情報の設定（集計範囲の情報）
        header.setFiscalYearFrom(summaryRange.getFiscalYearFrom());
        header.setWeekNoFrom(summaryRange.getWeekNoFrom());
        header.setFiscalYearTo(summaryRange.getFiscalYearTo());
        header.setWeekNoTo(summaryRange.getWeekNoTo());

        currentWeek = "";

        do {

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
                    if (week54TotalList.contains(currentFiscalYear)) {
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

            detailHeaderList.add(new DemandAllWeekReportListDetailHeaderResultData(
                    ApiSimpleConstants.WEEK_NO.concat(currentWeekNo.toString()).concat(ApiSimpleConstants.WEEK), null));

        } while (!currentWeek.equals(summaryRange.getFiscalYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getWeekNoTo().intValue()))));

        header.setDetailHeaderList(detailHeaderList);

        //建物件数分処理を繰り返す（実績取得用）
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
            BigDecimal max = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //カレンダMapの初期化
            List<DemandWeekBuildingCalListDetailResultData> calList;
            Map<String, Date> calMap;

            //明細部の初期化
            detail = new DemandAllWeekReportListDetailResultData();

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
                calMap = calList.stream().collect(Collectors.toMap(
                        x -> x.getFiscalYear().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
            }

            if (buildingDemandList == null || buildingDemandList.size() != 1) {
                detail.setSummaryDate(
                        buildingDemandList.get(0).getWeekStartDay().substring(0, 2).concat(ApiSimpleConstants.SLASH)
                                .concat(buildingDemandList.get(0).getWeekStartDay().substring(2, 4)));
            }

            //現在集計中の年度・週番号を取得する
            currentWeekReport = DemandEmsUtility.getCurrentBuildingWeekReport(buildingCalList, getServerDateTime());
            if (currentWeekReport == null) {
                //取得できない場合は、次のレコードへ
                continue;
            }
            //時間帯別インデックスの初期化
            String currentYear = "";
            BigDecimal currentNo = BigDecimal.ZERO;

            //時間帯別実績の初期化
            timeResult = new ArrayList<>();

            //週開始日の初期化
            weekStart = new ArrayList<>();

            currentWeek = "";

            do {

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
                        if (week54TotalList.contains(currentFiscalYear)) {
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
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    currentWeekNo.intValue()));

                }

                if (calMap.get(currentWeek) == null) {
                    weekStart.add(null);
                } else {
                    weekStart.add(DateUtility
                            .changeDateFormat(calMap.get(currentWeek), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
                }

            } while (!currentWeek.equals(summaryRange.getFiscalYearTo()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            summaryRange.getWeekNoTo().intValue()))));

            detail.setWeekStartDate(weekStart);

            //デマンド週報系統テーブルからデータを取得する
            CommonDemandWeekReportLineListResult weekLineParam = DemandEmsUtility.getWeekReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getSummaryKind(),
                    parameter.getLineGroupId(), parameter.getLineNo(), summaryRange.getFiscalYearFrom(),
                    summaryRange.getFiscalYearTo(), summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo());
            tempReportList = getResultList(commonDemandWeekReportLineListServiceDaoImpl, weekLineParam);

            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
                resultList.add(detail);
                continue;
            }

            //年度、週Noでソートする
            tempReportList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(tempReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

            //取得した件数分処理を繰り返す
            for (CommonDemandWeekReportLineListResult temp : tempReportList) {

                //サマリー値の計算
                if (temp.getLineValueKwh() != null) {
                    //null以外の場合に実施
                    if (summary == null) {
                        summary = temp.getLineValueKwh();
                    } else {
                        summary = summary.add(temp.getLineValueKwh());
                    }

                    if (max == null) {
                        max = temp.getLineValueKwh();
                    } else if (temp.getLineValueKwh().compareTo(max) == 1) {
                        //最大より大きい場合
                        max = temp.getLineValueKwh();
                    }

                    if (Integer.parseInt(currentWeekReport.getFiscalYear().concat(
                            String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                    currentWeekReport.getWeekNo().intValue()))) > Integer
                                            .parseInt(temp.getFiscalYear().concat(
                                                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            temp.getWeekNo().intValue())))) {
                        //現在集計中の週または未来の週の場合（※データはないはずであるが対応しておく）、合計と平均の計算から除外する

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

                //実績値を詰める（一致するまで処理を行う）
                while (true) {

                    //インデックスを変更する
                    if (CheckUtility.isNullOrEmpty(currentYear)) {
                        currentYear = summaryRange.getFiscalYearFrom();
                        currentNo = summaryRange.getWeekNoFrom();
                    } else if (currentNo.compareTo(new BigDecimal(54)) == 0) {
                        //年度と週番号を変更
                        currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                        currentNo = BigDecimal.ONE;
                    } else if (currentNo.compareTo(new BigDecimal(53)) == 0) {
                        if (week54TotalList.isEmpty()) {
                            //年度と週番号を変更
                            currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                            currentNo = BigDecimal.ONE;
                        } else if (week54TotalList.contains(currentYear)) {
                            //54週がある場合、週番号のみ変更
                            currentNo = currentNo.add(BigDecimal.ONE);
                        } else {
                            //年度と週番号を変更
                            currentYear = new BigDecimal(currentYear).add(BigDecimal.ONE).toString();
                            currentNo = BigDecimal.ONE;
                        }
                    } else {
                        //週番号のみ変更
                        currentNo = currentNo.add(BigDecimal.ONE);
                    }

                    if (currentYear.equals(temp.getFiscalYear()) && currentNo.compareTo(temp.getWeekNo()) == 0) {
                        //インデックスと一致する場合は実績を詰め、ループを抜ける
                        timeResult.add(
                                temp.getLineValueKwh().setScale(parameter.getPrecision(),
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

        return new DemandAllWeekReportListResult(header, resultList);
    }

    /**
     * 空の明細を作成する
     *
     * @param detail
     * @param header
     */
    private void setEmptyDetail(DemandAllWeekReportListDetailResultData detail,
            DemandAllWeekReportListHeaderResultData header) throws Exception {
        List<BigDecimal> emptyResult = new ArrayList<>();
        List<String> emptyWeekStart = new ArrayList<>();

        if (header == null || header.getDetailHeaderList() == null || header.getDetailHeaderList().isEmpty()) {
            return;
        }

        for (int i = 0; i <= header.getDetailHeaderList().size() - 1; i++) {
            emptyResult.add(null);
            emptyWeekStart.add(null);
        }

        detail.setTimeResult(emptyResult);
        if (detail.getWeekStartDate() == null || detail.getWeekStartDate().isEmpty()) {
            detail.setWeekStartDate(emptyWeekStart);
        }
        detail.setSummary(null);
        detail.setMax(null);
        detail.setMin(null);
        detail.setAverage(null);
    }

}
