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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandAllDayReportListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandAllDayReportListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForDayResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllDayReportListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllDayReportListHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.AllBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsAllUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンドデータ実績取得処理（全体・日報用） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandAllDayReportListDao extends OsolApiDao<DemandAllDayReportListParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final AllBuildingListServiceDaoImpl allBuildingListServiceDaoImpl;
    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandAllDayReportListDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        allBuildingListServiceDaoImpl = new AllBuildingListServiceDaoImpl();
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandAllDayReportListResult query(DemandAllDayReportListParameter parameter) throws Exception {

        List<LineListDetailResultData> lineList;
        boolean demandFlg = false;
        List<AllBuildingListDetailResultData> buildingList;
        DemandAllDayReportListHeaderResultData header;
        SummaryRangeForDayResult summaryRange;
        List<String> headerTitleTimeResult;
        DemandAllDayReportListDetailResultData detail;
        List<BigDecimal> timeResult;
        List<CommonDemandDayReportLineListResult> tempReportList;
        List<DemandAllDayReportListDetailResultData> resultList = new ArrayList<>();
        Date currentTime = null;

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandAllDayReportListResult();
        }

        //時刻がNULLの場合、0000を設定
        if (CheckUtility.isNullOrEmpty(parameter.getTimesOfDay())) {
            parameter.setTimesOfDay("0000");
        }

        //集計期間計算方法がNULLの場合、からを設定
        if (CheckUtility.isNullOrEmpty(parameter.getSumPeriodCalcType())) {
            parameter.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
        }

        //集計期間がNULLの場合、24（時間）を設定
        if (parameter.getSumPeriod() == null) {
            parameter.setSumPeriod(new BigDecimal("24"));
        }

        //小数点精度がNULLの場合、第一位を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(1);
        }

        //指定精度未満の制御がNULLの場合、切り捨てを設定
        if (CheckUtility.isNullOrEmpty(parameter.getBelowAccuracyControl())) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_DOWN.getVal());
        }

        //系統情報の取得
        LineListDetailResultData lineParam = DemandEmsUtility.getLineListParam(parameter.getOperationCorpId(),
                parameter.getLineGroupId(), parameter.getLineNo(),
                ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        lineList = getResultList(lineListServiceDaoImpl, lineParam);

        if (lineList == null || lineList.size() != 1) {
            //1件以外はnullを返して処理を終了する
            return new DemandAllDayReportListResult();
        }

        //対象の系統がデマンド値かどうかを判定する
        if (!ApiGenericTypeConstants.LINE_TARGET.LOGGING.getVal().equals(lineList.get(0).getLineTarget())) {
            //ロギング以外はデマンド値
            demandFlg = Boolean.TRUE;
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
            return new DemandAllDayReportListResult();
        }

        //フィルタ処理を行う
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingList == null || buildingList.isEmpty()) {
            return new DemandAllDayReportListResult();
        }

        //データ取得の範囲取得
        summaryRange = SummaryRangeUtility.getSummaryRangeForDay(
                DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                parameter.getTimesOfDay(),
                parameter.getSumPeriodCalcType(), parameter.getSumPeriod());

        //ヘッダー情報の設定
        headerTitleTimeResult = new ArrayList<>();
        header = new DemandAllDayReportListHeaderResultData();
        header.setRangeFrom(summaryRange.getRangeFrom());
        header.setRangeTo(summaryRange.getRangeTo());
        if (demandFlg) {
            header.setUnit(ApiSimpleConstants.UNIT_DEMAND);
            header.setSummaryUnit(ApiSimpleConstants.UNIT_USE_POWER);
        } else {
            header.setUnit(lineList.get(0).getLineUnit());
            header.setSummaryUnit(lineList.get(0).getLineUnit());
        }
        header.setSummaryBuildingCount(0);

        do {

            if (currentTime == null) {
                currentTime = DateUtility.conversionDate(summaryRange.getRangeFrom(),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            } else {
                currentTime = DateUtility.plusMinute(currentTime, 30);
            }

            headerTitleTimeResult.add(DateUtility.changeDateFormat(currentTime, DateUtility.DATE_FORMAT_HHMM_COLON)
                    .concat(ApiSimpleConstants.FROM));

        } while (!currentTime
                .equals(DateUtility.conversionDate(summaryRange.getRangeTo(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        header.setHeaderTitleTimeResult(headerTitleTimeResult);

        if (buildingList == null || buildingList.isEmpty()) {
            //0件の場合、ヘッダのみ設定して処理を終了
            return new DemandAllDayReportListResult(header, null);
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
            BigDecimal max = null;
            BigDecimal min = null;
            BigDecimal averageCount = null;

            //明細部の初期化
            detail = new DemandAllDayReportListDetailResultData();

            //明細部の設定（建物情報のみ）
            detail.setCorpId(buildingResult.getCorpId());
            detail.setBuildingId(buildingResult.getBuildingId());
            detail.setBuildingNo(buildingResult.getBuildingNo());
            detail.setBuildingType(buildingResult.getBuildingType());
            detail.setBuildingName(buildingResult.getBuildingName());

            //時間帯別実績の初期化
            timeResult = new ArrayList<>();

            //時間帯別実績インデックスの初期化
            Date currentDate = null;
            BigDecimal currentDateNo = BigDecimal.ZERO;

            //デマンド日報系統テーブルからデータを取得する
            CommonDemandDayReportLineListResult dayReportParam = DemandEmsUtility.getDayReportLineListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), summaryRange.getMeasurementDateFrom(), summaryRange.getJigenNoFrom(),
                    summaryRange.getMeasurementDateTo(), summaryRange.getJigenNoTo());
            tempReportList = getResultList(commonDemandDayReportLineListServiceDaoImpl, dayReportParam);

            if (tempReportList == null || tempReportList.isEmpty()) {
                //実績が取得できない場合、建物情報のみを返して次のレコードへ
                setEmptyDetail(detail, header);
            } else {

                //計測日時、時限Noでソートする
                tempReportList = SortUtility.sortCommonDemandDayReportLineListByMeasurement(tempReportList,
                        ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

                //取得した件数分処理を繰り返す
                for (CommonDemandDayReportLineListResult temp : tempReportList) {
                    //サマリー値の計算
                    if (temp.getLineValueKw() != null) {
                        //null以外の場合に実施
                        if (summary == null) {
                            summary = temp.getLineValueKw();
                        } else {
                            summary = summary.add(temp.getLineValueKw());
                        }

                        if (averageCount == null) {
                            averageCount = BigDecimal.ONE;
                        } else {
                            averageCount = averageCount.add(BigDecimal.ONE);
                        }

                        if (max == null) {
                            max = temp.getLineValueKw();
                        } else if (temp.getLineValueKw().compareTo(max) == 1) {
                            //最大より大きい場合
                            max = temp.getLineValueKw();
                        }

                        if (min == null) {
                            min = temp.getLineValueKw();
                        } else if (temp.getLineValueKw().compareTo(min) == -1) {
                            //最小より小さい場合
                            min = temp.getLineValueKw();
                        }
                    }

                    //実績値を詰める（インデックスが一致するまで処理を行う）
                    while (true) {

                        //インデックスを変更する
                        if (currentDate == null) {
                            //初期値を設定
                            currentDate = summaryRange.getMeasurementDateFrom();
                            currentDateNo = summaryRange.getJigenNoFrom();
                        } else if (currentDateNo.compareTo(new BigDecimal(48)) == 0) {
                            //日付と行を変更する
                            currentDateNo = BigDecimal.ONE;
                            currentDate = DateUtility.plusDay(currentDate, 1);
                        } else {
                            //行だけ変更する
                            currentDateNo = currentDateNo.add(BigDecimal.ONE);
                        }

                        if (temp.getMeasurementDate().equals(currentDate)
                                && temp.getJigenNo().compareTo(currentDateNo) == 0) {
                            //インデックスと一致する場合は実績値を詰める
                            timeResult.add(temp.getLineValueKw().setScale(parameter.getPrecision(),
                                    ApiCodeValueConstants.PRECISION_CONTROL
                                            .getControlType(parameter.getBelowAccuracyControl())));
                            //ループを抜ける
                            break;
                        } else {
                            //一致しない場合はnullを詰める
                            timeResult.add(null);
                        }
                    }
                }

                //timeResultの要素数とheaderTitleTimeResultの要素数が一致するまでnullを詰めて追加する
                while (timeResult.size() != headerTitleTimeResult.size()) {
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
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl()));
                }
                //デマンド系統の場合は、合計値をkWHに変換
                if (summary != null) {
                    if (demandFlg) {
                        summary = summary.divide(new BigDecimal(2), parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl()));
                    } else {
                        summary = summary.setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl()));
                    }
                }

                //最大値、最小値の小数制御
                if (max != null) {
                    max = max.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl()));
                }

                if (min != null) {
                    min = min.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl()));
                }

                //明細部の設定（実績）
                detail.setTimeResult(timeResult);
                detail.setSummary(summary);
                detail.setMax(max);
                detail.setMin(min);
                detail.setAverage(average);
            }

            //デマンド月報データを取得する（目標電力取得のため）
            CommonDemandMonthReportListResult monthReportParam = DemandEmsUtility.getMonthReportListParam(
                    buildingResult.getCorpId(), buildingResult.getBuildingId(), summaryRange.getMeasurementDateFrom(),
                    summaryRange.getMeasurementDateTo());
            List<CommonDemandMonthReportListResult> monthRepList = getResultList(
                    commonDemandMonthReportListServiceDaoImpl, monthReportParam);
            if (monthRepList != null && monthRepList.size() == 1) {
                detail.setTargetPower(monthRepList.get(0).getTargetKw().setScale(parameter.getPrecision(),
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
            }

            //契約電力を取得する
            //建物デマンド情報を取得する
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

        return new DemandAllDayReportListResult(header, resultList);
    }

    /**
     * 空の明細を作成する
     *
     * @param detail
     * @param header
     */
    private void setEmptyDetail(DemandAllDayReportListDetailResultData detail,
            DemandAllDayReportListHeaderResultData header) throws Exception {
        List<BigDecimal> emptyTimeResult = new ArrayList<>();

        if (header == null || header.getHeaderTitleTimeResult() == null
                || header.getHeaderTitleTimeResult().isEmpty()) {
            return;
        }

        for (int i = 0; i <= header.getHeaderTitleTimeResult().size() - 1; i++) {
            emptyTimeResult.add(null);
        }

        detail.setTimeResult(emptyTimeResult);
        detail.setSummary(null);
        detail.setMax(null);
        detail.setMin(null);
        detail.setAverage(null);
        detail.setContractPower(null);
        detail.setTargetPower(null);
    }

}
