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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandYearCorpCalListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandYearCorpCalListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandYearCorpCalListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.DemandCalendarYearUtility;

/**
 * デマンド年報企業カレンダ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandYearCorpCalListDao extends OsolApiDao<DemandYearCorpCalListParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;

    public DemandYearCorpCalListDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandYearCorpCalListResult query(DemandYearCorpCalListParameter parameter) throws Exception {

        List<DemandYearCorpCalListDetailResultData> resultList = new ArrayList<>();

        //企業デマンド情報を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandYearCorpCalListResult();
        }

        String yearNoFrom = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .subtract(new BigDecimal("8")));
        String yearNoTo = String
                .valueOf(new BigDecimal(DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYY))
                        .add(BigDecimal.ONE));

        //企業集計カレンダを取得する
        List<DemandCalendarYearData> calList = DemandCalendarYearUtility.getCalendarYearList(yearNoFrom, yearNoTo,
                corpDemandList.get(0).getSumDate());

        if (calList == null || calList.isEmpty()) {
            return new DemandYearCorpCalListResult();
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
            return new DemandYearCorpCalListResult();
        }

        //結果に詰める
        for (DemandCalendarYearData calData : calList) {
            DemandYearCorpCalListDetailResultData result = new DemandYearCorpCalListDetailResultData();
            result.setCorpId(parameter.getOperationCorpId());
            result.setYearNo(calData.getYearNo());
            result.setMonthNo(calData.getMonthNo());
            result.setMonthStartDate(calData.getMonthStartDate());
            result.setMonthEndDate(calData.getMonthEndDate());
            resultList.add(result);
        }

        return new DemandYearCorpCalListResult(resultList);
    }

}
