/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandWeekBuildingCalListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandWeekBuildingCalListResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンド週報建物カレンダ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandWeekBuildingCalListDao extends OsolApiDao<DemandWeekBuildingCalListParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    public DemandWeekBuildingCalListDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandWeekBuildingCalListResult query(DemandWeekBuildingCalListParameter parameter) throws Exception {

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDemandParam = DemandEmsUtility
                .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDemandList = getResultList(buildingDemandListServiceDaoImpl,
                buildingDemandParam);

        if (buildingDemandList == null || buildingDemandList.size() != 1) {
            return new DemandWeekBuildingCalListResult();
        }

        List<DemandWeekBuildingCalListDetailResultData> calList = DemandEmsUtility
                .getWeekBuildingCalList(buildingDemandList.get(0), getServerDateTime());

        if (calList == null || calList.isEmpty()) {
            return new DemandWeekBuildingCalListResult();
        }

        //条件に応じてフィルタリングする
        if (!CheckUtility.isNullOrEmpty(parameter.getBaseDate())) {
            calList = calList.stream()
                    .filter(i -> i.getWeekStartDate()
                            .compareTo(DateUtility.conversionDate(parameter.getBaseDate(),
                                    DateUtility.DATE_FORMAT_YYYYMMDD)) <= 0
                            && i.getWeekEndDate().compareTo(
                                    DateUtility.conversionDate(parameter.getBaseDate(),
                                            DateUtility.DATE_FORMAT_YYYYMMDD)) >= 0)
                    .collect(Collectors.toList());
        }

        if (calList != null && !calList.isEmpty()) {
            if (!CheckUtility.isNullOrEmpty(parameter.getFiscalYearFrom())
                    && CheckUtility.isNullOrEmpty(parameter.getFiscalYearTo())
                    && parameter.getWeekNoFrom() == null && parameter.getWeekNoTo() == null) {
                calList = calList.stream().filter(i -> i.getFiscalYear().equals(parameter.getFiscalYearFrom()))
                        .collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getFiscalYearFrom())
                    && !CheckUtility.isNullOrEmpty(parameter.getFiscalYearTo())
                    && parameter.getWeekNoFrom() == null && parameter.getWeekNoTo() == null) {
                calList = calList.stream()
                        .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer
                                .parseInt(parameter.getFiscalYearFrom())
                                && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(parameter.getFiscalYearTo()))
                        .collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getFiscalYearFrom())
                    && CheckUtility.isNullOrEmpty(parameter.getFiscalYearTo())
                    && parameter.getWeekNoFrom() != null && parameter.getWeekNoTo() == null) {
                calList = calList.stream().filter(i -> i.getFiscalYear().equals(parameter.getFiscalYearFrom())
                        && i.getWeekNo().compareTo(parameter.getWeekNoFrom()) == 0).collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getFiscalYearFrom())
                    && !CheckUtility.isNullOrEmpty(parameter.getFiscalYearTo())
                    && parameter.getWeekNoFrom() != null && parameter.getWeekNoTo() == null) {
                calList = calList.stream()
                        .filter(i -> Integer.parseInt(i.getFiscalYear()) >= Integer
                                .parseInt(parameter.getFiscalYearFrom())
                                && Integer.parseInt(i.getFiscalYear()) <= Integer.parseInt(parameter.getFiscalYearTo())
                                && i.getWeekNo().compareTo(parameter.getWeekNoFrom()) == 0)
                        .collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getFiscalYearFrom())
                    && !CheckUtility.isNullOrEmpty(parameter.getFiscalYearTo())
                    && parameter.getWeekNoFrom() != null && parameter.getWeekNoTo() != null) {
                if (parameter.getFiscalYearFrom().equals(parameter.getFiscalYearTo())) {
                    //年度Fromと年度Toが同じ場合
                    calList = calList.stream().filter(i -> i.getFiscalYear().equals(parameter.getFiscalYearFrom())
                            && i.getWeekNo().compareTo(parameter.getWeekNoFrom()) >= 0
                            && i.getWeekNo().compareTo(parameter.getWeekNoTo()) <= 0).collect(Collectors.toList());
                } else if (Integer.parseInt(parameter.getFiscalYearTo())
                        - Integer.parseInt(parameter.getFiscalYearFrom()) >= 2) {
                    //年度Fromと年度Toが2年以上空いている場合
                    calList = calList.stream().filter(i -> (i.getFiscalYear().equals(parameter.getFiscalYearFrom())
                            && i.getWeekNo().compareTo(parameter.getWeekNoFrom()) >= 0)
                            || (i.getFiscalYear().equals(parameter.getFiscalYearTo())
                                    && i.getWeekNo().compareTo(parameter.getWeekNoTo()) <= 0)
                            || (Integer.parseInt(i.getFiscalYear()) >= Integer.parseInt(parameter.getFiscalYearFrom())
                                    + 1
                                    && Integer.parseInt(
                                            i.getFiscalYear()) <= Integer.parseInt(parameter.getFiscalYearTo()) - 1))
                            .collect(Collectors.toList());
                } else {
                    calList = calList.stream().filter(i -> (i.getFiscalYear().equals(parameter.getFiscalYearFrom())
                            && i.getWeekNo().compareTo(parameter.getWeekNoFrom()) >= 0)
                            || (i.getFiscalYear().equals(parameter.getFiscalYearTo())
                                    && i.getWeekNo().compareTo(parameter.getWeekNoTo()) <= 0))
                            .collect(Collectors.toList());
                }
            }
        }

        if (calList != null && !calList.isEmpty()) {
            //年度、週Noでソートする
            calList = calList.stream()
                    .sorted(Comparator
                            .comparing(DemandWeekBuildingCalListDetailResultData::getFiscalYear,
                                    Comparator.naturalOrder())
                            .thenComparing(DemandWeekBuildingCalListDetailResultData::getWeekNo,
                                    Comparator.naturalOrder()))
                    .collect(Collectors.toList());

        }

        return new DemandWeekBuildingCalListResult(calList);
    }

}
