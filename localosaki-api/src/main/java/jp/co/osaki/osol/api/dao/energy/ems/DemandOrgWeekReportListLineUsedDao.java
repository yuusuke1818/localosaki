/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgWeekReportListLineUsedParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgWeekReportListLineUsedResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportPointListResult;
import jp.co.osaki.osol.api.result.utility.CommonDisplayElementListResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerWeekDataDetailResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerWeekDataResult;
import jp.co.osaki.osol.api.result.utility.CommonLinePowerWeekDataSummaryResult;
import jp.co.osaki.osol.api.result.utility.CurrentWeekReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForWeekResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListLineUsedDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListLineUsedHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListLineUsedHeaderTitleResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgWeekReportListLineUsedSummaryAreaResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandWeekReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * エネルギー使用状況実績取得（個別・週報・系統別使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgWeekReportListLineUsedDao extends OsolApiDao<DemandOrgWeekReportListLineUsedParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final CommonDemandWeekReportLineListServiceDaoImpl commonDemandWeekReportLineListServiceDaoImpl;
    private final CommonDemandWeekReportPointListServiceDaoImpl commonDemandWeekReportPointListServiceDaoImpl;
    private final CommonDemandWeekReportListServiceDaoImpl commonDemandWeekReportListServiceDaoImpl;

    public DemandOrgWeekReportListLineUsedDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        commonDemandWeekReportLineListServiceDaoImpl = new CommonDemandWeekReportLineListServiceDaoImpl();
        commonDemandWeekReportPointListServiceDaoImpl = new CommonDemandWeekReportPointListServiceDaoImpl();
        commonDemandWeekReportListServiceDaoImpl = new CommonDemandWeekReportListServiceDaoImpl();
    }

    @Override
    public DemandOrgWeekReportListLineUsedResult query(DemandOrgWeekReportListLineUsedParameter parameter)
            throws Exception {
        DemandOrgWeekReportListLineUsedResult result = new DemandOrgWeekReportListLineUsedResult();
        DemandOrgWeekReportListLineUsedHeaderResultData header = new DemandOrgWeekReportListLineUsedHeaderResultData();
        List<DemandOrgWeekReportListLineUsedHeaderTitleResultData> headerTitleList = new ArrayList<>();
        List<DemandOrgWeekReportListLineUsedDetailResultData> detailList = new ArrayList<>();
        DemandOrgWeekReportListLineUsedSummaryAreaResultData summary = new DemandOrgWeekReportListLineUsedSummaryAreaResultData();
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
        List<CommonDisplayElementListResult> displayElementList;
        CommonLinePowerWeekDataResult linePowerData;
        List<BigDecimal> elementNumSummaryList = new ArrayList<>();
        List<BigDecimal> elementRateSummaryList = new ArrayList<>();
        List<BigDecimal> elementNumMaxList = new ArrayList<>();
        List<BigDecimal> elementNumMinList = new ArrayList<>();
        List<BigDecimal> elementNumAverageList = new ArrayList<>();

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
                return new DemandOrgWeekReportListLineUsedResult();
            }
            corpCalList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0), getServerDateTime());
            if (corpCalList == null || corpCalList.isEmpty()) {
                return new DemandOrgWeekReportListLineUsedResult();
            }

        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            //建物集計の場合
            //建物デマンド情報を取得する
            BuildingDemandListDetailResultData buildingDmParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            List<BuildingDemandListDetailResultData> buildingDmList = getResultList(
                    buildingDemandListServiceDaoImpl, buildingDmParam);
            if (buildingDmList == null || buildingDmList.size() != 1) {
                return new DemandOrgWeekReportListLineUsedResult();
            }
            buildingCalList = DemandEmsUtility.getWeekBuildingCalList(buildingDmList.get(0), getServerDateTime());
            if (buildingCalList == null || buildingCalList.isEmpty()) {
                return new DemandOrgWeekReportListLineUsedResult();
            }
        }

        //取得したカレンダを年度でフィルタリングする
        if (ApiCodeValueConstants.RANGE_UNIT.YEAR.getVal().equals(parameter.getRangeUnit())) {
            //年単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear()).subtract(parameter.getSumPeriod())
                    .toString();
            fiscalYearTo = new BigDecimal(parameter.getFiscalYear()).add(parameter.getSumPeriod()).toString();
        } else {
            //週単位の場合
            fiscalYearFrom = new BigDecimal(parameter.getFiscalYear())
                    .subtract(parameter.getSumPeriod().divide(new BigDecimal(53), 0, BigDecimal.ROUND_UP))
                    .toString();
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
            currentWeekReport = DemandEmsUtility.getCurrentBuildingWeekReport(buildingCalList,
                    getServerDateTime());
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
        SummaryRangeForWeekResult summaryRange = SummaryRangeUtility.getSummaryRangeForWeek(
                parameter.getFiscalYear(), parameter.getWeekNo(), week54ExistList, parameter.getSumPeriodCalcType(),
                parameter.getRangeUnit(), parameter.getSumPeriod());

        //ヘッダ情報を設定する
        header.setFiscalYearFrom(summaryRange.getFiscalYearFrom());
        header.setFiscalYearTo(summaryRange.getFiscalYearTo());
        header.setWeekNoFrom(summaryRange.getWeekNoFrom());
        header.setWeekNoTo(summaryRange.getWeekNoTo());

        //年度+週No単位でMapを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            if (filterCorpCalList != null && !filterCorpCalList.isEmpty()) {
                weekNoMap = filterCorpCalList.stream().collect(Collectors.toMap(
                        x -> x.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
            if (filterBuildingCalList != null && !filterBuildingCalList.isEmpty()) {
                weekNoMap = filterBuildingCalList.stream().collect(Collectors.toMap(
                        x -> x.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        x.getWeekNo().intValue())),
                        x -> x.getWeekStartDate()));
            }
        }

        do {

            DemandOrgWeekReportListLineUsedDetailResultData detail = new DemandOrgWeekReportListLineUsedDetailResultData();

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

                currentWeek = currentFiscalYear.concat(
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentWeekNo.intValue()));

            }

            detail.setWeekNo(
                    ApiSimpleConstants.WEEK_NO.concat(currentWeekNo.toString()).concat(ApiSimpleConstants.WEEK));
            detail.setFiscalYearWeekNo(currentWeek);
            detail.setWeekStartDate(
                    DateUtility.changeDateFormat(weekNoMap.get(currentWeek), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                            .concat(ApiSimpleConstants.FROM));
            detail.setElementNumResultList(new ArrayList<>());
            detail.setElementRateResultList(new ArrayList<>());

            detailList.add(detail);

        } while (currentWeek != null && !currentWeek.equals(summaryRange.getFiscalYearTo()
                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                        summaryRange.getWeekNoTo().intValue()))));

        //表示対象要素情報を取得する
        if (parameter.getGraphId() == null) {
            displayElementList = getDisplayElementList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                    parameter.getLineGroupId());
        } else {
            displayElementList = getDisplayElementList(parameter.getOperationCorpId(), parameter.getBuildingId(),
                    parameter.getLineGroupId(), parameter.getGraphId());
        }
        if (displayElementList == null || displayElementList.isEmpty()) {
            result.setHeader(header);
            result.setDetailList(detailList);
            return result;
        }

        for (CommonDisplayElementListResult displayElement : displayElementList) {
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                            .equals(displayElement.getElementType())) {
                headerTitleList
                        .add(new DemandOrgWeekReportListLineUsedHeaderTitleResultData(
                                displayElement.getElementName(),
                                ApiSimpleConstants.UNIT_USE_POWER,
                                ApiSimpleConstants.UNIT_USE_POWER,
                                displayElement.getElementType(),
                                displayElement.getGraphColor(),
                                displayElement.getLineTarget(),
                                null));
            } else {
                headerTitleList
                        .add(new DemandOrgWeekReportListLineUsedHeaderTitleResultData(
                                displayElement.getElementName(),
                                displayElement.getUnit(),
                                displayElement.getSummaryUnit(),
                                displayElement.getElementType(),
                                displayElement.getGraphColor(),
                                displayElement.getLineTarget(),
                                null));
            }

            //系統別データを取得する
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal().equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                            .equals(displayElement.getElementType())
                    || ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal()
                            .equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromLine(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        parameter.getLineGroupId(), displayElement.getLineNo(),
                        summaryRange.getFiscalYearFrom(), summaryRange.getFiscalYearTo(),
                        summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo(), parameter.getSummaryKind(),
                        parameter.getPrecision(), parameter.getBelowAccuracyControl(),
                        displayElement.getElementType(), currentWeekReport, null);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromPoint(parameter.getOperationCorpId(), parameter.getBuildingId(),
                        displayElement.getSmId(), displayElement.getPointNo(),
                        summaryRange.getFiscalYearFrom(), summaryRange.getFiscalYearTo(),
                        summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo(), parameter.getSummaryKind(),
                        parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), currentWeekReport, null);
            } else if (ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal().equals(displayElement.getElementType())) {
                linePowerData = getLinePowerDataFromAmedas(parameter.getOperationCorpId(),
                        parameter.getBuildingId(), summaryRange.getFiscalYearFrom(),
                        summaryRange.getFiscalYearTo(), summaryRange.getWeekNoFrom(), summaryRange.getWeekNoTo(),
                        parameter.getSummaryKind(), parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal(), currentWeekReport, null);
            } else {
                linePowerData = null;
            }

            if (linePowerData == null) {
                //系統別データが取得できない場合
                elementNumSummaryList.add(null);
                elementNumMaxList.add(null);
                elementNumMinList.add(null);
                elementNumAverageList.add(null);
                for (DemandOrgWeekReportListLineUsedDetailResultData detail : detailList) {
                    detail.getElementNumResultList().add(null);
                }
            } else {
                //系統別データが取得できた場合
                elementNumSummaryList.add(linePowerData.getSummary().getSummaryNumUsed());
                elementNumMaxList.add(linePowerData.getSummary().getMaxNumUsed());
                elementNumMinList.add(linePowerData.getSummary().getMinNumUsed());
                elementNumAverageList.add(linePowerData.getSummary().getAverageNumUsed());
                for (DemandOrgWeekReportListLineUsedDetailResultData detail : detailList) {
                    boolean flg = false;
                    for (CommonLinePowerWeekDataDetailResult detailResult : linePowerData.getDetail()) {
                        if (detailResult.getFiscalYear()
                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                        detailResult.getWeekNo().intValue()))
                                .equals(detail.getFiscalYearWeekNo())) {
                            detail.getElementNumResultList().add(detailResult.getResultNumUsed());
                            flg = true;
                            break;
                        }
                    }
                    if (!flg) {
                        //実績値が設定できていない
                        detail.getElementNumResultList().add(null);
                    }
                }
            }
        }

        //比率数値を算出する前に、期間の全データを確認し、比率数値を算出すべき対象を絞り込む
        for(int i = 0;i <= headerTitleList.size() - 1; i++) {
            if(ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal().equals(headerTitleList.get(i).getElementType())) {
                //デマンドの場合のみ対象
                for(DemandOrgWeekReportListLineUsedDetailResultData detail : detailList) {
                    if(detail.getElementNumResultList().get(i) != null
                            && detail.getElementNumResultList().get(i).compareTo(BigDecimal.ZERO) < 0) {
                        //マイナス値がある場合、フラグをたてる
                        headerTitleList.get(i).setMinusValueExistFlg(Boolean.TRUE);
                        break;
                    }
                }

                if(headerTitleList.get(i).getMinusValueExistFlg() == null) {
                    //設定されていない場合はfalse
                    headerTitleList.get(i).setMinusValueExistFlg(Boolean.FALSE);
                }
            }
        }


        //比率数値を算出する（明細部）
        for (DemandOrgWeekReportListLineUsedDetailResultData detail : detailList) {
            BigDecimal detailSummary = BigDecimal.ZERO;
            for (int i = 0; i <= headerTitleList.size() - 1; i++) {
                if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                        .equals(headerTitleList.get(i).getElementType())) {
                    //デマンドの場合かつマイナス値がない場合だけ加算
                    if(!headerTitleList.get(i).getMinusValueExistFlg()) {
                        if (detail.getElementNumResultList().get(i) != null) {
                            detailSummary = detailSummary.add(detail.getElementNumResultList().get(i));
                        }
                    }
                }
            }

            for (int i = 0; i <= headerTitleList.size() - 1; i++) {
                if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                        .equals(headerTitleList.get(i).getElementType())) {
                    if(headerTitleList.get(i).getMinusValueExistFlg()) {
                        detail.getElementRateResultList().add(i, null);
                    }else {
                        if (detailSummary.compareTo(BigDecimal.ZERO) == 0) {
                            detail.getElementRateResultList().add(i, null);
                        } else if (detail.getElementNumResultList().get(i) != null) {
                            detail.getElementRateResultList().add(i,
                                    detail.getElementNumResultList().get(i).multiply(new BigDecimal(100))
                                            .divide(detailSummary, parameter.getPrecision(),
                                                    ApiCodeValueConstants.PRECISION_CONTROL
                                                            .getControlType(parameter.getBelowAccuracyControl())));
                        } else {
                            detail.getElementRateResultList().add(i, null);
                        }
                    }
                } else {
                    detail.getElementRateResultList().add(i, null);
                }
            }
        }

        //比率数値を算出する（合計部）
        BigDecimal sumSummary = BigDecimal.ZERO;
        for (int i = 0; i <= headerTitleList.size() - 1; i++) {
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                    .equals(headerTitleList.get(i).getElementType())) {
                //デマンドの場合かつマイナス値がない場合だけ加算
                if(!headerTitleList.get(i).getMinusValueExistFlg()) {
                    if (elementNumSummaryList.get(i) != null) {
                        sumSummary = sumSummary.add(elementNumSummaryList.get(i));
                    }
                }

            }
        }
        for (int i = 0; i <= headerTitleList.size() - 1; i++) {
            if (ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal()
                    .equals(headerTitleList.get(i).getElementType())) {
                if(headerTitleList.get(i).getMinusValueExistFlg()) {
                    elementRateSummaryList.add(i, null);
                }else {
                    if (sumSummary.compareTo(BigDecimal.ZERO) == 0) {
                        elementRateSummaryList.add(i, null);
                    } else if (elementNumSummaryList.get(i) != null) {
                        elementRateSummaryList.add(i,
                                elementNumSummaryList.get(i).multiply(new BigDecimal(100)).divide(sumSummary,
                                        parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    } else {
                        elementRateSummaryList.add(i, null);
                    }
                }

            } else {
                elementRateSummaryList.add(i, null);
            }
        }

        header.setHeaderTitleElementResultList(headerTitleList);
        result.setHeader(header);
        result.setDetailList(detailList);
        summary.setElementNumSummaryList(elementNumSummaryList);
        summary.setElementRateSummaryList(elementRateSummaryList);
        summary.setElementNumMaxList(elementNumMaxList);
        summary.setElementNumMinList(elementNumMinList);
        summary.setElementNumAverageList(elementNumAverageList);
        result.setSummary(summary);
        return result;
    }

    /**
     * 表示対象要素情報取得（グラフID指定）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param graphId
     * @return
     */
    private List<CommonDisplayElementListResult> getDisplayElementList(String corpId, Long buildingId,
            Long lineGroupId, Long graphId) {
        List<CommonDisplayElementListResult> list = new ArrayList<>();

        //グラフ要素設定情報を取得する
        DemandGraphElementListDetailResultData elementParam = DemandEmsUtility.getGraphElementListParam(corpId,
                buildingId, lineGroupId, graphId, null);
        List<DemandGraphElementListDetailResultData> elementList = getResultList(demandGraphElementListServiceDaoImpl,
                elementParam);
        if (elementList == null || elementList.isEmpty()) {
            return null;
        }

        //グラフ要素種別、表示順でソートする
        elementList = SortUtility.sortElementListByGraphElementType(elementList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        for (DemandGraphElementListDetailResultData element : elementList) {
            if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.DEMAND.getVal().equals(element.getGraphElementType())) {
                //デマンドの場合、系統情報からデータを取得する
                LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(corpId, lineGroupId,
                        element.getGraphLineNo(), ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
                List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);
                if (lineList == null || lineList.size() != 1) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }

                if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(lineList.get(0).getLineNo())) {
                    //全体の場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            null,
                            null,
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                } else if (ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
                    //ロギングの場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            lineList.get(0).getLineUnit(),
                            lineList.get(0).getLineUnit(),
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                } else {
                    //ロギング以外の場合
                    list.add(new CommonDisplayElementListResult(
                            lineList.get(0).getLineName(),
                            null,
                            null,
                            ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal(),
                            element.getGraphColorCode(),
                            lineList.get(0).getLineGroupId(),
                            lineList.get(0).getLineNo(),
                            lineList.get(0).getLineTarget(),
                            null,
                            null));
                }
            } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.ANALOG.getVal().equals(element.getGraphElementType())) {
                //アナログの場合、建物機器ポイント情報からデータを取得する
                DemandBuildingSmPointListDetailResultData pointParam = DemandEmsUtility.getBuildingSmPointListParam(
                        corpId, buildingId, element.getGraphSmId(), element.getGraphPointNo());
                List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                        demandBuildingSmPointListServiceDaoImpl, pointParam);
                if (buildingSmPointList == null || buildingSmPointList.size() != 1
                        || ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal()
                                .equals(buildingSmPointList.get(0).getPointSumFlg())) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        buildingSmPointList.get(0).getPointName(),
                        buildingSmPointList.get(0).getPointUnit(),
                        buildingSmPointList.get(0).getPointUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal(),
                        element.getGraphColorCode(),
                        null,
                        null,
                        null,
                        element.getGraphSmId(),
                        element.getGraphPointNo()));

            } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.AMEDAS.getVal().equals(element.getGraphElementType())) {
                //アメダスの場合、建物デマンド情報からデータを取得する
                BuildingDemandListDetailResultData demandParam = DemandEmsUtility.getBuildingDemandListParam(corpId,
                        buildingId);
                List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                        demandParam);
                if (demandList == null || demandList.size() != 1 || ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF
                        .getVal().equals(demandList.get(0).getOutAirTempDispFlg())) {
                    //上記いずれかに該当する場合、次のレコードへ
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        ApiSimpleConstants.OUT_TEMPERATURE,
                        ApiSimpleConstants.UNIT_TEMPERATURE,
                        ApiSimpleConstants.UNIT_TEMPERATURE,
                        ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal(),
                        element.getGraphColorCode(),
                        null,
                        null,
                        null,
                        null,
                        null));
            } else {
                continue;
            }
        }

        return list;
    }

    /**
     * 表示対象要素情報取得（グラフID指定なし）
     *
     * @param corpId
     * @param buidingId
     * @param lineGroupId
     * @return
     */
    private List<CommonDisplayElementListResult> getDisplayElementList(String corpId, Long buildingId,
            Long lineGroupId) {

        List<CommonDisplayElementListResult> list = new ArrayList<>();

        //系統情報を取得する
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(corpId, lineGroupId, null,
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);

        //系統番号順にソートする
        lineList = SortUtility.sortLineListByLineNo(lineList, ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        for (LineListDetailResultData line : lineList) {
            if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(line.getLineNo())) {
                //全体の場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_ALL.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            } else if (ApiCodeValueConstants.LINE_TARGET.LOGGING.getVal().equals(line.getLineTarget())) {
                //ロギングの場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        line.getLineUnit(),
                        line.getLineUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_LOGGING.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            } else {
                //ロギング以外の場合
                list.add(new CommonDisplayElementListResult(
                        line.getLineName(),
                        null,
                        null,
                        ApiCodeValueConstants.ELEMENT_TYPE.LINE_DEMAND.getVal(),
                        null,
                        line.getLineGroupId(),
                        line.getLineNo(),
                        line.getLineTarget(),
                        null,
                        null));
            }
        }

        //建物機器ポイント情報を取得する
        DemandBuildingSmPointListDetailResultData pointParam = DemandEmsUtility.getBuildingSmPointListParam(corpId,
                buildingId, null, null);
        List<DemandBuildingSmPointListDetailResultData> buildingSmPointList = getResultList(
                demandBuildingSmPointListServiceDaoImpl, pointParam);

        if (buildingSmPointList != null && !buildingSmPointList.isEmpty()) {
            //機器ID、ポイント番号でソートする
            buildingSmPointList = buildingSmPointList.stream().sorted(Comparator
                    .comparing(DemandBuildingSmPointListDetailResultData::getSmId,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(DemandBuildingSmPointListDetailResultData::getPointNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        for (DemandBuildingSmPointListDetailResultData demandSmPoint : buildingSmPointList) {
            if (ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal().equals(demandSmPoint.getPointSumFlg())) {
                continue;
            } else {
                //機器ポイント情報を取得
                SmPointListDetailResultData smParam = DemandEmsUtility.getSmPointListParam(demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo());
                List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smParam);
                if (smPointList == null || smPointList.size() != 1
                        || !ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal()
                                .equals(smPointList.get(0).getPointType())) {
                    continue;
                }
                list.add(new CommonDisplayElementListResult(
                        demandSmPoint.getPointName(),
                        demandSmPoint.getPointUnit(),
                        demandSmPoint.getPointUnit(),
                        ApiCodeValueConstants.ELEMENT_TYPE.ANALOG.getVal(),
                        null,
                        null,
                        null,
                        null,
                        demandSmPoint.getSmId(),
                        demandSmPoint.getPointNo()));
            }
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = DemandEmsUtility.getBuildingDemandListParam(corpId,
                buildingId);
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);
        if (buildingDemandList != null && buildingDemandList.size() == 1
                && !ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF.getVal()
                        .equals(buildingDemandList.get(0).getOutAirTempDispFlg())) {
            list.add(new CommonDisplayElementListResult(
                    ApiSimpleConstants.OUT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiSimpleConstants.UNIT_TEMPERATURE,
                    ApiCodeValueConstants.ELEMENT_TYPE.AMEDAS.getVal(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null));
        }

        //グラフ自動配色を取得する
        if (!list.isEmpty()) {
            List<String> colorList = DemandGraphAutoColorUtility.getGraphAutoColorList(list.size());
            if (colorList.size() == list.size()) {
                //全色取得できた場合のみ設定
                for (int i = 0; i < colorList.size(); i++) {
                    list.get(i).setGraphColor(colorList.get(i));
                }
            }
        }

        return list;
    }

    /**
     * 系統別データ作成（系統・週報）
     *
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param elementType
     * @param currentWeek
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerWeekDataResult getLinePowerDataFromLine(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom,
            BigDecimal weekNoTo, String summaryUnit, Integer decimal, String control, String elementType,
            CurrentWeekReportResult currentWeek, List<String> exclusionDataList) {

        List<CommonLinePowerWeekDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerWeekDataSummaryResult summaryData;
        BigDecimal summary = null;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;

        //デマンド週報系統からデータを取得する
        CommonDemandWeekReportLineListResult weekLineParam = DemandEmsUtility.getWeekReportLineListParam(corpId,
                buildingId, summaryUnit, lineGroupId, lineNo, fiscalYearFrom, fiscalYearTo, weekNoFrom, weekNoTo);
        List<CommonDemandWeekReportLineListResult> weekLineList = getResultList(
                commonDemandWeekReportLineListServiceDaoImpl, weekLineParam);
        if (weekLineList != null && !weekLineList.isEmpty()) {
            weekLineList = SortUtility.sortCommonDemandWeekReportLineListByFiscalYearWeekNo(weekLineList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandWeekReportLineListResult line : weekLineList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(line.getFiscalYear().concat(
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, line.getWeekNo().intValue())))) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }
            //サマリ値の設定
            if (summary == null) {
                summary = line.getLineValueKwh();
            } else {
                summary = summary.add(line.getLineValueKwh());
            }

            if (maxUse == null) {
                maxUse = line.getLineValueKwh();
            } else if (line.getLineValueKwh().compareTo(maxUse) >= 0) {
                maxUse = line.getLineValueKwh();
            }

            if (maxMax == null) {
                maxMax = line.getLineMaxKw();
            } else if (line.getLineMaxKw().compareTo(maxMax) >= 0) {
                maxMax = line.getLineMaxKw();
            }

            if (Integer.parseInt(line.getFiscalYear().concat(
                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, line.getWeekNo().intValue()))) >= Integer
                            .parseInt(currentWeek.getFiscalYear().concat(String.format(
                                    StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentWeek.getWeekNo().intValue())))) {
                //データの年度、週Noが現在積算中の年度、週番号以降は最小値、平均値をとらない
            } else {
                if (min == null) {
                    min = line.getLineValueKwh();
                } else if (line.getLineValueKwh().compareTo(min) <= 0) {
                    min = line.getLineValueKwh();
                }

                if (averageSummary == null) {
                    averageSummary = line.getLineValueKwh();
                } else {
                    averageSummary = averageSummary.add(line.getLineValueKwh());
                }

                if (line.getLineValueKwh() != null) {
                    if (averageCount == null) {
                        averageCount = BigDecimal.ONE;
                    } else {
                        averageCount = averageCount.add(BigDecimal.ONE);
                    }
                }
            }

            //明細部の設定
            CommonLinePowerWeekDataDetailResult detailData = new CommonLinePowerWeekDataDetailResult();
            detailData.setFiscalYear(line.getFiscalYear());
            detailData.setWeekNo(line.getWeekNo());
            if (line.getLineValueKwh() != null) {
                detailData.setResultNumUsed(line.getLineValueKwh().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(line.getLineValueKwh());
            }
            if (line.getLineMaxKw() != null) {
                detailData.setResultNumMax(line.getLineMaxKw().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(line.getLineMaxKw());
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerWeekDataSummaryResult();
        if (summary != null) {
            summaryData.setSummaryNumUsed(
                    summary.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerWeekDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（ポイント・週報）
     *
     * @param corpId
     * @param buildingId
     * @param smId
     * @param pointNo
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param currentWeek
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerWeekDataResult getLinePowerDataFromPoint(String corpId, Long buildingId, Long smId,
            String pointNo, String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom, BigDecimal weekNoTo,
            String summaryUnit, Integer decimal, String control, CurrentWeekReportResult currentWeek,
            List<String> exclusionDataList) {
        List<CommonLinePowerWeekDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerWeekDataSummaryResult summaryData;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;
        BigDecimal pointAverageValAfterFormat;
        BigDecimal pointMaxValAfterFormat;

        //機器ポイントデータを取得する
        SmPointListDetailResultData smPointParam = DemandEmsUtility.getSmPointListParam(smId, pointNo);
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);

        //デマンド週報ポイントからデータを取得する
        CommonDemandWeekReportPointListResult weekPointParam = DemandEmsUtility.getWeekReportPointListParam(corpId,
                buildingId, smId, summaryUnit, fiscalYearFrom, fiscalYearTo, weekNoFrom, weekNoTo, pointNo, pointNo);
        List<CommonDemandWeekReportPointListResult> weekPointList = getResultList(
                commonDemandWeekReportPointListServiceDaoImpl, weekPointParam);
        if (weekPointList != null && !weekPointList.isEmpty()) {
            weekPointList = SortUtility.sortCommonDemandWeekReportPointListByFiscalYearWeekNo(weekPointList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandWeekReportPointListResult point : weekPointList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(point.getFiscalYear().concat(
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, point.getWeekNo().intValue())))) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }

            if (point.getPointAvg() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointAverageValAfterFormat = point.getPointAvg().add(smPointList.get(0).getAnalogOffSetValue());
                    pointAverageValAfterFormat = pointAverageValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointAverageValAfterFormat = point.getPointAvg();
                }
            } else {
                pointAverageValAfterFormat = point.getPointAvg();
            }

            if (point.getPointMax() != null) {
                if (smPointList != null && smPointList.size() == 1) {
                    pointMaxValAfterFormat = point.getPointMax().add(smPointList.get(0).getAnalogOffSetValue());
                    pointMaxValAfterFormat = pointMaxValAfterFormat
                            .multiply(smPointList.get(0).getAnalogConversionFactor());
                } else {
                    pointMaxValAfterFormat = point.getPointMax();
                }
            } else {
                pointMaxValAfterFormat = point.getPointMax();
            }

            //サマリ値の設定
            if (maxUse == null) {
                maxUse = pointAverageValAfterFormat;
            } else if (pointAverageValAfterFormat.compareTo(maxUse) >= 0) {
                maxUse = pointAverageValAfterFormat;
            }

            if (maxMax == null) {
                maxMax = pointMaxValAfterFormat;
            } else if (pointMaxValAfterFormat.compareTo(maxMax) >= 0) {
                maxMax = pointMaxValAfterFormat;
            }

            if (Integer.parseInt(point.getFiscalYear().concat(
                    String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, point.getWeekNo().intValue()))) >= Integer
                            .parseInt(currentWeek.getFiscalYear().concat(String.format(
                                    StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentWeek.getWeekNo().intValue())))) {
                //データの年度、週Noが現在積算中の年度、週番号以降は最小値、平均値をとらない
            } else {
                if (min == null) {
                    min = pointAverageValAfterFormat;
                } else if (pointAverageValAfterFormat.compareTo(min) <= 0) {
                    min = pointAverageValAfterFormat;
                }

                if (averageSummary == null) {
                    averageSummary = pointAverageValAfterFormat;
                } else {
                    averageSummary = averageSummary.add(pointAverageValAfterFormat);
                }

                if (pointAverageValAfterFormat != null) {
                    if (averageCount == null) {
                        averageCount = BigDecimal.ONE;
                    } else {
                        averageCount = averageCount.add(BigDecimal.ONE);
                    }
                }
            }

            //明細部の設定
            CommonLinePowerWeekDataDetailResult detailData = new CommonLinePowerWeekDataDetailResult();
            detailData.setFiscalYear(point.getFiscalYear());
            detailData.setWeekNo(point.getWeekNo());
            if (pointAverageValAfterFormat != null) {
                detailData.setResultNumUsed(pointAverageValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(pointAverageValAfterFormat);
            }
            if (pointMaxValAfterFormat != null) {
                detailData.setResultNumMax(pointMaxValAfterFormat.setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(pointMaxValAfterFormat);
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerWeekDataSummaryResult();
        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerWeekDataResult(detailList, summaryData);
    }

    /**
     * 系統別データ作成（アメダス・週報）
     *
     * @param corpId
     * @param buildingId
     * @param fiscalYearFrom
     * @param fiscalYearTo
     * @param weekNoFrom
     * @param weekNoTo
     * @param summaryUnit
     * @param decimal
     * @param control
     * @param currentWeek
     * @param exclusionDataList
     * @return
     */
    private CommonLinePowerWeekDataResult getLinePowerDataFromAmedas(String corpId, Long buildingId,
            String fiscalYearFrom, String fiscalYearTo, BigDecimal weekNoFrom, BigDecimal weekNoTo, String summaryUnit,
            Integer decimal, String control, CurrentWeekReportResult currentWeek, List<String> exclusionDataList) {
        List<CommonLinePowerWeekDataDetailResult> detailList = new ArrayList<>();
        CommonLinePowerWeekDataSummaryResult summaryData;
        BigDecimal maxUse = null;
        BigDecimal maxMax = null;
        BigDecimal min = null;
        BigDecimal averageCount = null;
        BigDecimal averageSummary = null;

        //デマンド週報からデータを取得する
        CommonDemandWeekReportListResult weekParam = DemandEmsUtility.getWeekReportListParam(corpId, buildingId,
                summaryUnit, fiscalYearFrom, fiscalYearTo, weekNoFrom, weekNoTo);
        List<CommonDemandWeekReportListResult> weekList = getResultList(commonDemandWeekReportListServiceDaoImpl,
                weekParam);
        if (weekList != null && !weekList.isEmpty()) {
            weekList = SortUtility.sortCommonDemandWeekReportListByFiscalYearWeekNo(weekList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandWeekReportListResult report : weekList) {
            if (exclusionDataList != null && !exclusionDataList.isEmpty()) {
                if (exclusionDataList.contains(report.getFiscalYear().concat(
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, report.getWeekNo().intValue())))) {
                    //データ除外対象リストにデータが含まれている場合は読み飛ばす
                    continue;
                }
            }
            //サマリ値の設定
            if (report.getOutAirTempAvg() != null) {
                if (maxUse == null) {
                    maxUse = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(maxUse) >= 0) {
                    maxUse = report.getOutAirTempAvg();
                }
            }

            if (report.getOutAirTempMax() != null) {
                if (maxMax == null) {
                    maxMax = report.getOutAirTempMax();
                } else if (report.getOutAirTempMax().compareTo(maxMax) >= 0) {
                    maxMax = report.getOutAirTempMax();
                }
            }

            if (Integer.parseInt(report.getFiscalYear()
                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                            report.getWeekNo().intValue()))) >= Integer
                                    .parseInt(currentWeek.getFiscalYear()
                                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                    currentWeek.getWeekNo().intValue())))) {
                //データの年度、週Noが現在積算中の年度、週番号以降は最小値、平均値をとらない
            } else if (report.getOutAirTempAvg() != null) {
                if (min == null) {
                    min = report.getOutAirTempAvg();
                } else if (report.getOutAirTempAvg().compareTo(min) <= 0) {
                    min = report.getOutAirTempAvg();
                }

                if (averageSummary == null) {
                    averageSummary = report.getOutAirTempAvg();
                } else {
                    averageSummary = averageSummary.add(report.getOutAirTempAvg());
                }

                if (averageCount == null) {
                    averageCount = BigDecimal.ONE;
                } else {
                    averageCount = averageCount.add(BigDecimal.ONE);
                }

            }

            //明細部の設定
            CommonLinePowerWeekDataDetailResult detailData = new CommonLinePowerWeekDataDetailResult();
            detailData.setFiscalYear(report.getFiscalYear());
            detailData.setWeekNo(report.getWeekNo());
            if (report.getOutAirTempAvg() != null) {
                detailData.setResultNumUsed(report.getOutAirTempAvg().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumUsed(report.getOutAirTempAvg());
            }
            if (report.getOutAirTempMax() != null) {
                detailData.setResultNumMax(report.getOutAirTempMax().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            } else {
                detailData.setResultNumMax(report.getOutAirTempMax());
            }
            detailList.add(detailData);

        }

        summaryData = new CommonLinePowerWeekDataSummaryResult();
        if (maxUse != null) {
            summaryData.setMaxNumUsed(
                    maxUse.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (maxMax != null) {
            summaryData.setMaxNumMax(
                    maxMax.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }

        if (min != null) {
            summaryData.setMinNumUsed(
                    min.setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
        }
        if (averageSummary != null) {
            if (averageCount != null) {
                summaryData.setAverageNumUsed(averageSummary.divide(averageCount, decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return new CommonLinePowerWeekDataResult(detailList, summaryData);
    }

}
