/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandYearBuildingCalListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandYearBuildingCalListResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandYearBuildingCalListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * デマンド年報建物カレンダ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandYearBuildingCalListDao extends OsolApiDao<DemandYearBuildingCalListParameter> {

    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;

    public DemandYearBuildingCalListDao() {
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandYearBuildingCalListResult query(DemandYearBuildingCalListParameter parameter) throws Exception {

        List<DemandYearBuildingCalListDetailResultData> resultList = new ArrayList<>();
        String sumDate = null;

        //集計時を取得する
        if (parameter.getLineGroupId() != null && !CheckUtility.isNullOrEmpty(parameter.getLineNo())) {
            //系統グループ、系統番号が設定されている場合
            //集計デマンド系統情報の取得
            AggregateDmLineListDetailResultData dmLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                    parameter.getOperationCorpId(), parameter.getBuildingId(), parameter.getLineGroupId(),
                    parameter.getLineNo(), null, null, null);
            List<AggregateDmLineListDetailResultData> dmLineList = getResultList(aggregateDmLineListServiceDaoImpl,
                    dmLineParam);

            if (dmLineList != null && dmLineList.size() == 1) {
                //データが取得できた場合、集計デマンド情報を取得
                AggregateDmListDetailResultData dmParam = EnergySettingUtility.getAggregateDmListParam(
                        parameter.getOperationCorpId(), parameter.getBuildingId(),
                        dmLineList.get(0).getAggregateDmId());
                List<AggregateDmListDetailResultData> dmList = getResultList(aggregateDmListServiceDaoImpl, dmParam);
                if (dmList != null && dmList.size() == 1) {
                    sumDate = dmList.get(0).getSumDate();
                }
            }
        }

        if (CheckUtility.isNullOrEmpty(sumDate)) {
            //集計時が取得できない場合または系統情報が設定されていない場合、建物デマンド情報の取得
            BuildingDemandListDetailResultData buildingDmParam = DemandEmsUtility
                    .getBuildingDemandListParam(parameter.getOperationCorpId(), parameter.getBuildingId());
            List<BuildingDemandListDetailResultData> buildingDmList = getResultList(buildingDemandListServiceDaoImpl,
                    buildingDmParam);
            if (buildingDmList == null || buildingDmList.size() != 1) {
                return new DemandYearBuildingCalListResult();
            } else {
                sumDate = buildingDmList.get(0).getSumDate();
            }
        }

        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //建物集計カレンダ情報を取得する
        List<DemandCalendarYearData> calList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                sumDate);

        if (calList == null || calList.isEmpty()) {
            return null;
        }

        //条件に応じてフィルタリングする
        if (!CheckUtility.isNullOrEmpty(parameter.getBaseDate())) {
            calList = calList.stream().filter(
                    i -> i.getMonthStartDate()
                            .compareTo(DateUtility.conversionDate(parameter.getBaseDate(),
                                    DateUtility.DATE_FORMAT_YYYYMMDD)) <= 0
                            && i.getMonthEndDate().compareTo(
                                    DateUtility.conversionDate(parameter.getBaseDate(),
                                            DateUtility.DATE_FORMAT_YYYYMMDD)) >= 0)
                    .collect(Collectors.toList());
        }

        if (calList != null && !calList.isEmpty()) {
            if (!CheckUtility.isNullOrEmpty(parameter.getYearNoFrom())
                    && CheckUtility.isNullOrEmpty(parameter.getYearNoTo())) {
                calList = calList.stream().filter(i -> i.getYearNo().equals(parameter.getYearNoFrom()))
                        .collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getYearNoFrom())
                    && !CheckUtility.isNullOrEmpty(parameter.getYearNoTo())) {
                calList = calList.stream()
                        .filter(i -> Integer.parseInt(i.getYearNo()) >= Integer.parseInt(parameter.getYearNoFrom())
                                && Integer.parseInt(i.getYearNo()) <= Integer.parseInt(parameter.getYearNoTo()))
                        .collect(Collectors.toList());
            }
        }

        if (calList != null && !calList.isEmpty()) {
            if (!CheckUtility.isNullOrEmpty(parameter.getCalYmFrom())
                    && CheckUtility.isNullOrEmpty(parameter.getCalYmTo())) {
                calList = calList.stream()
                        .filter(i -> i.getYearNo().equals(parameter.getCalYmFrom().substring(0, 4))
                                && i.getMonthNo().compareTo(new BigDecimal(parameter.getCalYmFrom().substring(4))) == 0)
                        .collect(Collectors.toList());
            } else if (!CheckUtility.isNullOrEmpty(parameter.getCalYmFrom())
                    && !CheckUtility.isNullOrEmpty(parameter.getCalYmTo())) {

                if (parameter.getCalYmFrom().substring(0, 4).equals(parameter.getCalYmTo().substring(0, 4))) {
                    //年が同じ場合
                    calList = calList.stream()
                            .filter(i -> i.getMonthNo()
                                    .compareTo(new BigDecimal(parameter.getCalYmFrom().substring(4))) >= 0
                                    && i.getMonthNo()
                                            .compareTo(new BigDecimal(parameter.getCalYmTo().substring(4))) <= 0)
                            .collect(Collectors.toList());
                } else if (BigDecimal.ONE.compareTo(new BigDecimal(parameter.getCalYmTo().substring(0, 4))
                        .subtract(new BigDecimal(parameter.getCalYmFrom().substring(0, 4)))) == 0) {
                    //From、Toが1年しか離れていない場合
                    List<DemandCalendarYearData> fromCalList = calList.stream()
                            .filter(i -> i.getYearNo().equals(parameter.getCalYmFrom().substring(0, 4)) && i
                                    .getMonthNo().compareTo(new BigDecimal(parameter.getCalYmFrom().substring(4))) >= 0)
                            .collect(Collectors.toList());
                    List<DemandCalendarYearData> toCalList = calList.stream()
                            .filter(i -> i.getYearNo().equals(parameter.getCalYmTo().substring(0, 4)) && i.getMonthNo()
                                    .compareTo(new BigDecimal(parameter.getCalYmTo().substring(4))) <= 0)
                            .collect(Collectors.toList());
                    calList.clear();
                    if (fromCalList != null && !fromCalList.isEmpty()) {
                        calList.addAll(fromCalList);
                    }
                    if (toCalList != null && !toCalList.isEmpty()) {
                        calList.addAll(toCalList);
                    }
                } else {
                    //上記以外の場合
                    List<DemandCalendarYearData> fromCalList = calList.stream()
                            .filter(i -> i.getYearNo().equals(parameter.getCalYmFrom().substring(0, 4)) && i
                                    .getMonthNo().compareTo(new BigDecimal(parameter.getCalYmFrom().substring(4))) >= 0)
                            .collect(Collectors.toList());
                    List<DemandCalendarYearData> betweenCalList = calList.stream()
                            .filter(i -> new BigDecimal(i.getYearNo()).compareTo(
                                    new BigDecimal(parameter.getYearNoFrom().substring(0, 4)).add(BigDecimal.ONE)) >= 0
                                    && new BigDecimal(i.getYearNo())
                                            .compareTo(new BigDecimal(parameter.getYearNoTo().substring(0, 4))
                                                    .subtract(BigDecimal.ONE)) <= 0)
                            .collect(Collectors.toList());
                    List<DemandCalendarYearData> toCalList = calList.stream()
                            .filter(i -> i.getYearNo().equals(parameter.getCalYmTo().substring(0, 4)) && i.getMonthNo()
                                    .compareTo(new BigDecimal(parameter.getCalYmTo().substring(4))) <= 0)
                            .collect(Collectors.toList());
                    calList.clear();
                    if (fromCalList != null && !fromCalList.isEmpty()) {
                        calList.addAll(fromCalList);
                    }
                    if (betweenCalList != null && !betweenCalList.isEmpty()) {
                        calList.addAll(betweenCalList);
                    }
                    if (toCalList != null && !toCalList.isEmpty()) {
                        calList.addAll(toCalList);
                    }
                }

            }
        }

        if (calList != null && !calList.isEmpty()) {
            //カレンダ年月でソートする
            calList = calList.stream()
                    .sorted(Comparator.comparing(DemandCalendarYearData::getYearNo, Comparator.naturalOrder())
                            .thenComparing(DemandCalendarYearData::getMonthNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        } else {
            return new DemandYearBuildingCalListResult();
        }

        //結果に詰める
        for (DemandCalendarYearData calData : calList) {
            DemandYearBuildingCalListDetailResultData result = new DemandYearBuildingCalListDetailResultData();
            result.setCorpId(parameter.getOperationCorpId());
            result.setBuildingId(parameter.getBuildingId());
            result.setYearNo(calData.getYearNo());
            result.setMonthNo(calData.getMonthNo());
            result.setMonthStartDate(calData.getMonthStartDate());
            result.setMonthEndDate(calData.getMonthEndDate());
            resultList.add(result);
        }

        return new DemandYearBuildingCalListResult(resultList);
    }

}
