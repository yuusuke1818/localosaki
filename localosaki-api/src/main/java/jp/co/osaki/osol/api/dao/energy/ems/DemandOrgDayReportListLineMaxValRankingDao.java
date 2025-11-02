/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandOrgDayReportListLineMaxValRankingParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandOrgDayReportListLineMaxValRankingResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandOrgDayReportListLineMaxValRankingResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.ems.SummaryRangeUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * エネルギー使用状況実績取得（個別・日報・系統最大値ランキング） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandOrgDayReportListLineMaxValRankingDao extends OsolApiDao<DemandOrgDayReportListLineMaxValRankingParameter> {

    private final CommonDemandDayReportLineListServiceDaoImpl commonDemandDayReportLineListServiceDaoImpl;
    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    public DemandOrgDayReportListLineMaxValRankingDao() {
        commonDemandDayReportLineListServiceDaoImpl = new CommonDemandDayReportLineListServiceDaoImpl();
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    @Override
    public DemandOrgDayReportListLineMaxValRankingResult query(DemandOrgDayReportListLineMaxValRankingParameter parameter) {
        String corpId = parameter.getOperationCorpId();
        Long buildingId = parameter.getBuildingId();
        Long lineGroupId = parameter.getLineGroupId();
        String lineNo = parameter.getLineNo();
        String summaryKind = parameter.getSummaryKind();
        String year = parameter.getYear();
        BigDecimal month = parameter.getMonth();
        String sumPeriodCalcType = parameter.getSumPeriodCalcType();
        BigDecimal sumPeriod = parameter.getSumPeriod();
        Integer rankCount = parameter.getRankCount();

        //集計日がnullの場合、「0:企業集計」を設定する
        if(summaryKind == null) {
            summaryKind = ApiCodeValueConstants.SUMMARY_KIND.COMPANY.getVal();
        }

        //集計期間計算方法がnullの場合は「0:から」を設定する
        if(sumPeriodCalcType == null) {
            sumPeriodCalcType = ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal();
        }

        //集計期間がnullの場合は「1」を設定する
        if(sumPeriod == null) {
            sumPeriod = new BigDecimal("1");
        }

        //集計日取得
        String sumDate = null;
      //年報カレンダを作成する
        if (ApiGenericTypeConstants.SUMMARY_UNIT.CORP.getVal().equals(parameter.getSummaryKind())) {
            //企業集計の場合
            //企業デマンド情報を取得する
            CorpDemandSelectResult corpDemandParam = DemandEmsUtility
                    .getCorpDemandParam(parameter.getOperationCorpId());
            List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
            if (corpDemandList != null && corpDemandList.size() == 1) {
                sumDate = corpDemandList.get(0).getSumDate();
            }
        } else if (ApiGenericTypeConstants.SUMMARY_UNIT.BUILDING.getVal().equals(parameter.getSummaryKind())) {
          //建物集計の場合
            //集計デマンド系統情報を取得する
            AggregateDmLineListDetailResultData aggregateLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    ApiGenericTypeConstants.LINE_TARGET.ALL.getVal(), null, null, null);
            List<AggregateDmLineListDetailResultData> aggregateLineList = getResultList(
                    aggregateDmLineListServiceDaoImpl, aggregateLineParam);
            if (aggregateLineList != null && aggregateLineList.size() == 1) {
                //集計デマンド情報を取得する
                AggregateDmListDetailResultData aggregateParam = EnergySettingUtility.getAggregateDmListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(),
                        aggregateLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> aggregateList = getResultList(aggregateDmListServiceDaoImpl,
                        aggregateParam);
                if (aggregateList != null && aggregateList.size() == 1) {
                    sumDate = aggregateList.get(0).getSumDate();
                }
            }

            if (CheckUtility.isNullOrEmpty(sumDate)) {
                //建物デマンド情報を取得する
                BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                        .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
                List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                        buildingDemandListServiceDaoImpl, buildingDemandParam);
                if (buildingDemandList != null && buildingDemandList.size() == 1) {
                    sumDate = buildingDemandList.get(0).getSumDate();
                }
            }
        }

        if(sumDate == null) {
            return new DemandOrgDayReportListLineMaxValRankingResult();
        }

        //年報カレンダー作成
        DecimalFormat format = new DecimalFormat("#");
        format.setMinimumIntegerDigits(2);
        format.setMaximumIntegerDigits(2);
        String monthString = format.format(month);
        Date toDate = DateUtility.plusYear(DateUtility.conversionDate(year.concat(monthString), DateUtility.DATE_FORMAT_YYYYMM),1);
        String toYear = DateUtility.changeDateFormat(toDate, DateUtility.DATE_FORMAT_YYYY);
        List<DemandCalendarYearData> yearCalendar = DemandCalendarYearUtility.getCalendarYearList(year, toYear, sumDate);

        //集計範囲取得
        SummaryRangeForYearResult summaryRange = SummaryRangeUtility.getSummaryRangeForYear(year, month, sumPeriodCalcType, sumPeriod);
        DemandCalendarYearData fromCalendar = yearCalendar.stream().filter(o -> o.getYearNo().equals(summaryRange.getYearFrom()))
                .filter(o -> o.getMonthNo().equals(summaryRange.getMonthFrom()))
                .findFirst().orElse(null);
        DemandCalendarYearData toCalendar = yearCalendar.stream().filter(o -> o.getYearNo().equals(summaryRange.getYearTo()))
                .filter(o -> o.getMonthNo().equals(summaryRange.getMonthTo()))
                .findFirst().orElse(null);

        CommonDemandDayReportLineListResult param = new CommonDemandDayReportLineListResult();

        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setLineGroupId(lineGroupId);
        param.setLineNo(lineNo);
        param.setMeasurementDateFrom(fromCalendar.getMonthStartDate());
        param.setMeasurementDateTo(toCalendar.getMonthEndDate());
        List<CommonDemandDayReportLineListResult> lineDataResultList = getResultList(commonDemandDayReportLineListServiceDaoImpl,param);

        //ソート 系統電力の降順、計測年月日の降順、時限Noの降順
        Comparator<CommonDemandDayReportLineListResult> comparator =
                Comparator.comparing(CommonDemandDayReportLineListResult::getLineValueKw,Comparator.reverseOrder())
                .thenComparing(CommonDemandDayReportLineListResult::getMeasurementDate,Comparator.reverseOrder())
                .thenComparing(CommonDemandDayReportLineListResult::getJigenNo,Comparator.reverseOrder());
        lineDataResultList = lineDataResultList.stream().sorted(comparator).collect(Collectors.toList());

        List<DemandOrgDayReportListLineMaxValRankingResultData> resultList = new ArrayList<>();
        Integer rankNo = 1;
        Integer countNo = 1;
        for (CommonDemandDayReportLineListResult result : lineDataResultList) {

            DemandOrgDayReportListLineMaxValRankingResultData data = new DemandOrgDayReportListLineMaxValRankingResultData();
            if (resultList.isEmpty()
                    || resultList.get(resultList.size() - 1).getLineValueKw().equals(result.getLineValueKw())) {
                data.setRankNumber(rankNo.toString());
            } else {
                data.setRankNumber(countNo.toString());
                rankNo = countNo;
            }
            data.setMeasurementDateTime(result.getMeasurementDate());
            data.setMeasurementDate(
                    DateUtility.changeDateFormat(result.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
            data.setMeasurementTime(DemandDataUtility.changeJigenNoToHHMM(result.getJigenNo(), false));
            data.setLineValueKw(result.getLineValueKw());
            resultList.add(data);

            countNo++;

            if (resultList.size() == rankCount) {
                break;
            }
        }
        DemandOrgDayReportListLineMaxValRankingResult result = new DemandOrgDayReportListLineMaxValRankingResult();
        result.setLineMaxValRankingResultList(resultList);

        return result;
    }

}
