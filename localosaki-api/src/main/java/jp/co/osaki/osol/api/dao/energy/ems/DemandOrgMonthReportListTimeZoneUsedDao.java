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

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgMonthReportListTimeZoneUsedParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgMonthReportListTimeZoneUsedResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForMonthResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListTimeZoneUsedDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListTimeZoneUsedHeaderResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgMonthReportListTimeZoneUsedSummaryAreaResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandMonthReportListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandGraphAutoColorUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * エネルギー使用状況実績取得（個別・月報・時間帯別使用量） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgMonthReportListTimeZoneUsedDao extends OsolApiDao<DemandOrgMonthReportListTimeZoneUsedParameter> {

    private final CommonDemandMonthReportListServiceDaoImpl commonDemandMonthReportListServiceDaoImpl;

    public DemandOrgMonthReportListTimeZoneUsedDao() {
        commonDemandMonthReportListServiceDaoImpl = new CommonDemandMonthReportListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgMonthReportListTimeZoneUsedResult query(DemandOrgMonthReportListTimeZoneUsedParameter parameter)
            throws Exception {
        DemandOrgMonthReportListTimeZoneUsedResult result = new DemandOrgMonthReportListTimeZoneUsedResult();
        DemandOrgMonthReportListTimeZoneUsedHeaderResultData header = new DemandOrgMonthReportListTimeZoneUsedHeaderResultData();
        List<DemandOrgMonthReportListTimeZoneUsedDetailResultData> detailList = new ArrayList<>();
        DemandOrgMonthReportListTimeZoneUsedSummaryAreaResultData summary = new DemandOrgMonthReportListTimeZoneUsedSummaryAreaResultData();
        BigDecimal summaryOpenTime = null;
        BigDecimal summaryOpenPreparation = null;
        BigDecimal summaryClosePreparation = null;
        BigDecimal summaryCloseTime = null;
        BigDecimal maxOpenTime = null;
        BigDecimal maxOpenPreparation = null;
        BigDecimal maxClosePreparation = null;
        BigDecimal maxCloseTime = null;
        BigDecimal minOpenTime = null;
        BigDecimal minOpenPreparation = null;
        BigDecimal minClosePreparation = null;
        BigDecimal minCloseTime = null;
        BigDecimal summaryAverageOpenTime = null;
        BigDecimal summaryAverageOpenPreparation = null;
        BigDecimal summaryAverageClosePreparation = null;
        BigDecimal summaryAverageCloseTime = null;
        BigDecimal summaryAverageCountOpenTime = null;
        BigDecimal summaryAverageCountOpenPreparation = null;
        BigDecimal summaryAverageCountClosePreparation = null;
        BigDecimal summaryAverageCountCloseTime = null;
        Date currentDate = null;

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

        //集計範囲を取得する
        SummaryRangeForMonthResult summaryRange = SummaryRangeUtility
                .getSummaryRangeForMonth(
                        DateUtility.changeDateFormat(parameter.getYmd(), DateUtility.DATE_FORMAT_YYYYMMDD),
                        parameter.getSumPeriodCalcType(), parameter.getSumPeriod());
        //ヘッダ部に値をセットする
        header.setDayFrom(summaryRange.getMeasurementDateFrom());
        header.setDayTo(summaryRange.getMeasurementDateTo());

        do {

            DemandOrgMonthReportListTimeZoneUsedDetailResultData detail = new DemandOrgMonthReportListTimeZoneUsedDetailResultData();
            if (currentDate == null) {
                currentDate = summaryRange.getMeasurementDateFrom();
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            //明細部に日付と曜日をセットする
            detail.setDay(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
            detail.setDayOfWeek(DateUtility.getDayOfWeekDay(currentDate));
            detail.setOpenTimeKwh(null);
            detail.setOpenPreparationKwh(null);
            detail.setClosePreparationKwh(null);
            detail.setCloseTimeKwh(null);

            detailList.add(detail);
        } while (!currentDate.equals(summaryRange.getMeasurementDateTo()));

        //デマンド月報データを取得する
        CommonDemandMonthReportListResult demandMonthReportParam = DemandEmsUtility.getMonthReportListParam(
                parameter.getOperationCorpId(), parameter.getBuildingId(), summaryRange.getMeasurementDateFrom(),
                summaryRange.getMeasurementDateTo());
        List<CommonDemandMonthReportListResult> demandMonthReportList = getResultList(
                commonDemandMonthReportListServiceDaoImpl, demandMonthReportParam);
        if (demandMonthReportList != null && !demandMonthReportList.isEmpty()) {
            demandMonthReportList = SortUtility.sortCommonDemandMonthReportListByMeasurement(demandMonthReportList,
                    ApiCodeValueConstants.SORT_ORDER.ASC.getVal());
        }

        for (CommonDemandMonthReportListResult demandMonthReport : demandMonthReportList) {
            for (DemandOrgMonthReportListTimeZoneUsedDetailResultData detail : detailList) {
                if (demandMonthReport.getMeasurementDate()
                        .compareTo(DateUtility.conversionDate(detail.getDay(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)) == 0) {
                    //開店時間
                    if (demandMonthReport.getOpenTimeKwh() == null) {
                        detail.setOpenTimeKwh(null);
                    } else {
                        detail.setOpenTimeKwh(demandMonthReport.getOpenTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandMonthReport.getOpenTimeKwh() != null) {
                        if (summaryOpenTime == null) {
                            summaryOpenTime = demandMonthReport.getOpenTimeKwh();
                        } else {
                            summaryOpenTime = summaryOpenTime.add(demandMonthReport.getOpenTimeKwh());
                        }
                        if (maxOpenTime == null) {
                            maxOpenTime = demandMonthReport.getOpenTimeKwh();
                        } else if (demandMonthReport.getOpenTimeKwh().compareTo(maxOpenTime) >= 0) {
                            maxOpenTime = demandMonthReport.getOpenTimeKwh();
                        }

                        if (minOpenTime == null) {
                            minOpenTime = demandMonthReport.getOpenTimeKwh();
                        } else if (demandMonthReport.getOpenTimeKwh().compareTo(minOpenTime) <= 0) {
                            minOpenTime = demandMonthReport.getOpenTimeKwh();
                        }
                        if (summaryAverageCountOpenTime == null) {
                            summaryAverageCountOpenTime = BigDecimal.ONE;
                        } else {
                            summaryAverageCountOpenTime = summaryAverageCountOpenTime.add(BigDecimal.ONE);
                        }
                        if (summaryAverageOpenTime == null) {
                            summaryAverageOpenTime = demandMonthReport.getOpenTimeKwh();
                        } else {
                            summaryAverageOpenTime = summaryAverageOpenTime.add(demandMonthReport.getOpenTimeKwh());
                        }

                    }
                    //開店準備時間
                    if (demandMonthReport.getOpenPreparationKwh() == null) {
                        detail.setOpenPreparationKwh(null);
                    } else {
                        detail.setOpenPreparationKwh(
                                demandMonthReport.getOpenPreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandMonthReport.getOpenPreparationKwh() != null) {
                        if (summaryOpenPreparation == null) {
                            summaryOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        } else {
                            summaryOpenPreparation = summaryOpenPreparation
                                    .add(demandMonthReport.getOpenPreparationKwh());
                        }
                        if (maxOpenPreparation == null) {
                            maxOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        } else if (demandMonthReport.getOpenPreparationKwh().compareTo(maxOpenPreparation) >= 0) {
                            maxOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        }

                        if (minOpenPreparation == null) {
                            minOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        } else if (demandMonthReport.getOpenPreparationKwh().compareTo(minOpenPreparation) <= 0) {
                            minOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        }
                        if (summaryAverageCountOpenPreparation == null) {
                            summaryAverageCountOpenPreparation = BigDecimal.ONE;
                        } else {
                            summaryAverageCountOpenPreparation = summaryAverageCountOpenPreparation.add(BigDecimal.ONE);
                        }
                        if (summaryAverageOpenPreparation == null) {
                            summaryAverageOpenPreparation = demandMonthReport.getOpenPreparationKwh();
                        } else {
                            summaryAverageOpenPreparation = summaryAverageOpenPreparation
                                    .add(demandMonthReport.getOpenPreparationKwh());
                        }

                    }

                    //閉店準備時間
                    if (demandMonthReport.getClosePreparationKwh() == null) {
                        detail.setClosePreparationKwh(null);
                    } else {
                        detail.setClosePreparationKwh(
                                demandMonthReport.getClosePreparationKwh().setScale(parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandMonthReport.getClosePreparationKwh() != null) {
                        if (summaryClosePreparation == null) {
                            summaryClosePreparation = demandMonthReport.getClosePreparationKwh();
                        } else {
                            summaryClosePreparation = summaryClosePreparation
                                    .add(demandMonthReport.getClosePreparationKwh());
                        }
                        if (maxClosePreparation == null) {
                            maxClosePreparation = demandMonthReport.getClosePreparationKwh();
                        } else if (demandMonthReport.getClosePreparationKwh().compareTo(maxClosePreparation) >= 0) {
                            maxClosePreparation = demandMonthReport.getClosePreparationKwh();
                        }

                        if (minClosePreparation == null) {
                            minClosePreparation = demandMonthReport.getClosePreparationKwh();
                        } else if (demandMonthReport.getClosePreparationKwh().compareTo(minClosePreparation) <= 0) {
                            minClosePreparation = demandMonthReport.getClosePreparationKwh();
                        }
                        if (summaryAverageCountClosePreparation == null) {
                            summaryAverageCountClosePreparation = BigDecimal.ONE;
                        } else {
                            summaryAverageCountClosePreparation = summaryAverageCountClosePreparation
                                    .add(BigDecimal.ONE);
                        }
                        if (summaryAverageClosePreparation == null) {
                            summaryAverageClosePreparation = demandMonthReport.getClosePreparationKwh();
                        } else {
                            summaryAverageClosePreparation = summaryAverageClosePreparation
                                    .add(demandMonthReport.getClosePreparationKwh());
                        }

                    }

                    //閉店時間
                    if (demandMonthReport.getCloseTimeKwh() == null) {
                        detail.setCloseTimeKwh(null);
                    } else {
                        detail.setCloseTimeKwh(demandMonthReport.getCloseTimeKwh().setScale(parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
                    }

                    if (demandMonthReport.getCloseTimeKwh() != null) {
                        if (summaryCloseTime == null) {
                            summaryCloseTime = demandMonthReport.getCloseTimeKwh();
                        } else {
                            summaryCloseTime = summaryCloseTime.add(demandMonthReport.getCloseTimeKwh());
                        }
                        if (maxCloseTime == null) {
                            maxCloseTime = demandMonthReport.getCloseTimeKwh();
                        } else if (demandMonthReport.getCloseTimeKwh().compareTo(maxCloseTime) >= 0) {
                            maxCloseTime = demandMonthReport.getCloseTimeKwh();
                        }

                        if (minCloseTime == null) {
                            minCloseTime = demandMonthReport.getCloseTimeKwh();
                        } else if (demandMonthReport.getCloseTimeKwh().compareTo(minCloseTime) <= 0) {
                            minCloseTime = demandMonthReport.getCloseTimeKwh();
                        }
                        if (summaryAverageCountCloseTime == null) {
                            summaryAverageCountCloseTime = BigDecimal.ONE;
                        } else {
                            summaryAverageCountCloseTime = summaryAverageCountCloseTime.add(BigDecimal.ONE);
                        }
                        if (summaryAverageCloseTime == null) {
                            summaryAverageCloseTime = demandMonthReport.getCloseTimeKwh();
                        } else {
                            summaryAverageCloseTime = summaryAverageCloseTime.add(demandMonthReport.getCloseTimeKwh());
                        }

                    }
                }
            }
        }

        //サマリ値を設定する
        if (summaryOpenTime == null) {
            summary.setSummaryOpenTime(null);
        } else {
            summary.setSummaryOpenTime(
                    summaryOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryOpenPreparation == null) {
            summary.setSummaryOpenPreparation(null);
        } else {
            summary.setSummaryOpenPreparation(
                    summaryOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryClosePreparation == null) {
            summary.setSummaryClosePreparation(null);
        } else {
            summary.setSummaryClosePreparation(
                    summaryClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (summaryCloseTime == null) {
            summary.setSummaryCloseTime(null);
        } else {
            summary.setSummaryCloseTime(
                    summaryCloseTime.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxOpenTime == null) {
            summary.setMaxOpenTime(null);
        } else {
            summary.setMaxOpenTime(
                    maxOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxOpenPreparation == null) {
            summary.setMaxOpenPreparation(null);
        } else {
            summary.setMaxOpenPreparation(
                    maxOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxClosePreparation == null) {
            summary.setMaxClosePreparation(null);
        } else {
            summary.setMaxClosePreparation(
                    maxClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (maxCloseTime == null) {
            summary.setMaxCloseTime(null);
        } else {
            summary.setMaxCloseTime(
                    maxCloseTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minOpenTime == null) {
            summary.setMinOpenTime(null);
        } else {
            summary.setMinOpenTime(
                    minOpenTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minOpenPreparation == null) {
            summary.setMinOpenPreparation(null);
        } else {
            summary.setMinOpenPreparation(
                    minOpenPreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minCloseTime == null) {
            summary.setMinCloseTime(null);
        } else {
            summary.setMinCloseTime(
                    minCloseTime.setScale(parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                            .getControlType(parameter.getBelowAccuracyControl())));
        }
        if (minClosePreparation == null) {
            summary.setMinClosePreparation(null);
        } else {
            summary.setMinClosePreparation(
                    minClosePreparation.setScale(parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountOpenTime == null || summaryAverageOpenTime == null
                || summaryAverageOpenTime.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageOpenTime(null);
        } else {
            summary.setAverageOpenTime(summaryAverageOpenTime.divide(summaryAverageCountOpenTime,
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountOpenPreparation == null || summaryAverageOpenPreparation == null
                || summaryAverageOpenPreparation.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageOpenPreparation(null);
        } else {
            summary.setAverageOpenPreparation(
                    summaryAverageOpenPreparation.divide(summaryAverageCountOpenPreparation, parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountClosePreparation == null || summaryAverageClosePreparation == null
                || summaryAverageClosePreparation.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageClosePreparation(null);
        } else {
            summary.setAverageClosePreparation(
                    summaryAverageClosePreparation.divide(summaryAverageCountClosePreparation, parameter.getPrecision(),
                            ApiCodeValueConstants.PRECISION_CONTROL
                                    .getControlType(parameter.getBelowAccuracyControl())));
        }

        if (summaryAverageCountCloseTime == null || summaryAverageCloseTime == null
                || summaryAverageCloseTime.compareTo(BigDecimal.ZERO) == 0) {
            summary.setAverageCloseTime(null);
        } else {
            summary.setAverageCloseTime(summaryAverageCloseTime.divide(summaryAverageCountCloseTime,
                    parameter.getPrecision(),
                    ApiCodeValueConstants.PRECISION_CONTROL.getControlType(parameter.getBelowAccuracyControl())));
        }

        BigDecimal totalSummary = BigDecimal.ZERO;
        if (summary.getSummaryOpenTime() != null) {
            totalSummary = totalSummary.add(summary.getSummaryOpenTime());
        }
        if (summary.getSummaryOpenPreparation() != null) {
            totalSummary = totalSummary.add(summary.getSummaryOpenPreparation());
        }
        if (summary.getSummaryClosePreparation() != null) {
            totalSummary = totalSummary.add(summary.getSummaryClosePreparation());
        }
        if (summary.getSummaryCloseTime() != null) {
            totalSummary = totalSummary.add(summary.getSummaryCloseTime());
        }

        if (totalSummary.compareTo(BigDecimal.ZERO) == 0) {
            summary.setSummaryRateOpenTime(null);
            summary.setSummaryRateOpenPreparation(null);
            summary.setSummaryRateClosePreparation(null);
            summary.setSummaryRateCloseTime(null);
        } else {
            if (summary.getSummaryOpenTime() == null || summary.getSummaryOpenTime().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateOpenTime(null);
            } else {
                summary.setSummaryRateOpenTime(
                        summary.getSummaryOpenTime().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryOpenPreparation() == null
                    || summary.getSummaryOpenPreparation().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateOpenPreparation(null);
            } else {
                summary.setSummaryRateOpenPreparation(
                        summary.getSummaryOpenPreparation().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(), ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryClosePreparation() == null
                    || summary.getSummaryClosePreparation().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateClosePreparation(null);
            } else {
                summary.setSummaryRateClosePreparation(
                        summary.getSummaryClosePreparation().multiply(new BigDecimal(100))
                                .divide(totalSummary, parameter.getPrecision(),
                                        ApiCodeValueConstants.PRECISION_CONTROL
                                                .getControlType(parameter.getBelowAccuracyControl())));
            }
            if (summary.getSummaryCloseTime() == null
                    || summary.getSummaryCloseTime().compareTo(BigDecimal.ZERO) == 0) {
                summary.setSummaryRateCloseTime(null);
            } else {
                summary.setSummaryRateCloseTime(
                        summary.getSummaryCloseTime().multiply(new BigDecimal(100)).divide(totalSummary,
                                parameter.getPrecision(),
                                ApiCodeValueConstants.PRECISION_CONTROL
                                        .getControlType(parameter.getBelowAccuracyControl())));
            }
        }

        //グラフ色を設定する
        List<String> graphColorList = DemandGraphAutoColorUtility.getGraphAutoColorList(4);
        if (graphColorList.size() == 4) {
            summary.setGraphColorOpenTime(graphColorList.get(0));
            summary.setGraphColorOpenPreparation(graphColorList.get(1));
            summary.setGraphColorClosePreparation(graphColorList.get(2));
            summary.setGraphColorCloseTime(graphColorList.get(3));
        }

        result.setHeader(header);
        result.setDetailList(detailList);
        result.setSummary(summary);
        return result;
    }

}
