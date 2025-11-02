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

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandCalendarWeekReportParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandCalendarWeekReportResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandCalendarWeekBuildingYearResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandCalendarWeekBuildingYearWeekResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandCalendarWeekCorpYearWeekResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンド情報カレンダー（週報用）　Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandCalendarWeekReportDao extends OsolApiDao<DemandCalendarWeekReportParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    public DemandCalendarWeekReportDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandCalendarWeekReportResult query(DemandCalendarWeekReportParameter parameter) throws Exception {

        DemandCalendarWeekReportResult result = new DemandCalendarWeekReportResult();
        List<DemandCalendarWeekCorpYearWeekResultData> calWeekCorpYearWeekList = new ArrayList<>();
        List<DemandCalendarWeekBuildingYearResultData> calWeekBuildingYearList = new ArrayList<>();
        List<DemandCalendarWeekBuildingYearWeekResultData> calWeekBuildingYearWeekList = new ArrayList<>();

        //当日日付
        Date svDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //前日日付
        Date svDayBefore = DateUtility.conversionDate(
                DateUtility.changeDateFormat(DateUtility.plusDay(getServerDateTime(), -1),
                        DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        //企業集計カレンダ（週報）を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl,
                corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandCalendarWeekReportResult();
        }
        List<DemandWeekCorpCalListDetailResultData> calCorpList = DemandEmsUtility.getWeekCorpCalList(
                corpDemandList.get(0),
                getServerDateTime());
        if (calCorpList == null || calCorpList.isEmpty()) {
            return null;
        }

        //取得したカレンダをシステム日付でフィルタリングする
        List<DemandWeekCorpCalListDetailResultData> filterCalCorpList = calCorpList.stream()
                .filter(i -> i.getWeekStartDate().compareTo(svDate) <= 0 && i.getWeekEndDate().compareTo(svDate) >= 0)
                .collect(Collectors.toList());

        result.setNowDate(svDate);
        result.setBeforeDate(svDayBefore);
        if (filterCalCorpList == null || filterCalCorpList.size() != 1) {
            result.setDefaultYear(null);
            result.setYearList(null);
        } else {
            result.setDefaultYear(filterCalCorpList.get(0).getFiscalYear());
            result.setYearList(
                    DemandEmsUtility.getYearList(DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY),
                            parameter.getSumPeriod()));
        }

        //取得したカレンダ（フィルタリング前）を年度でフィルタリングする
        filterCalCorpList = calCorpList.stream()
                .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(result.getYearList().get(0))
                        && Integer.parseInt(i.getFiscalYear()) <= Integer
                                .parseInt(result.getYearList().get(result.getYearList().size() - 1)))
                .collect(Collectors.toList());

        //年度・週（企業集計）を設定する
        for (DemandWeekCorpCalListDetailResultData cal : filterCalCorpList) {
            calWeekCorpYearWeekList
                    .add(new DemandCalendarWeekCorpYearWeekResultData(cal.getFiscalYear(), cal.getWeekNo(),
                            cal.getWeekStartDate(), cal.getWeekEndDate()));
        }

        result.setCorpYearWeekList(calWeekCorpYearWeekList);

        //建物IDが未設定の場合、ここで終了
        if (parameter.getBuildingId() == null) {
            return result;
        }

        //電力集計カレンダ（週報）を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(
                buildingDemandListServiceDaoImpl, buildingDemandParam);
        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandCalendarWeekReportResult();
        }
        List<DemandWeekBuildingCalListDetailResultData> calBuildingList = DemandEmsUtility.getWeekBuildingCalList(
                buildingDemandList.get(0),
                getServerDateTime());
        if (calBuildingList == null || calBuildingList.isEmpty()) {
            return null;
        }

        //取得したカレンダをシステム日付でフィルタリングする
        List<DemandWeekBuildingCalListDetailResultData> filterCalBuildingList = calBuildingList.stream()
                .filter(i -> i.getWeekStartDate().compareTo(svDate) <= 0 && i.getWeekEndDate().compareTo(svDate) >= 0)
                .collect(Collectors.toList());
        if (filterCalBuildingList != null && filterCalBuildingList.size() == 1) {
            //フィルタリング前のカレンダを年度と集計期間でフィルタリングする
            Integer rangeFrom = new BigDecimal(DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY))
                    .subtract(parameter.getSumPeriod())
                    .intValue();
            Integer rangeTo = Integer.parseInt(DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYY));
            filterCalBuildingList = calBuildingList.stream()
                    .filter(i -> Integer.parseInt(i.getFiscalYear()) >= rangeFrom
                            && Integer.parseInt(i.getFiscalYear()) <= rangeTo)
                    .collect(Collectors.toList());

            if (filterCalBuildingList != null && !filterCalBuildingList.isEmpty()) {
                //年度（電力集計）を作成する
                //年度単位でグルーピングする
                Map<String, List<BigDecimal>> calMap = filterCalBuildingList.stream()
                        .collect(Collectors.groupingBy(DemandWeekBuildingCalListDetailResultData::getFiscalYear,
                                Collectors.mapping(DemandWeekBuildingCalListDetailResultData::getWeekNo,
                                        Collectors.toList())));
                for (Map.Entry<String, List<BigDecimal>> entry : calMap.entrySet()) {
                    if (entry.getValue().contains(new BigDecimal(54))) {
                        //54週が含まれている場合
                        calWeekBuildingYearList.add(new DemandCalendarWeekBuildingYearResultData(entry.getKey(),
                                ApiCodeValueConstants.WEEK_54_EXISTS.ONE.getVal()));
                    } else {
                        calWeekBuildingYearList.add(new DemandCalendarWeekBuildingYearResultData(entry.getKey(),
                                ApiCodeValueConstants.WEEK_54_EXISTS.ZERO.getVal()));
                    }
                }
                result.setBuildingYearList(calWeekBuildingYearList);
                //年度・週（電力集計）を作成する
                for (DemandWeekBuildingCalListDetailResultData cal : filterCalBuildingList) {
                    calWeekBuildingYearWeekList
                            .add(new DemandCalendarWeekBuildingYearWeekResultData(parameter.getBuildingId(),
                                    cal.getFiscalYear(),
                                    cal.getWeekNo(), cal.getWeekStartDate(), cal.getWeekEndDate()));
                }
                result.setBuildingYearWeekList(calWeekBuildingYearWeekList);

            }
        }

        return result;
    }

}
