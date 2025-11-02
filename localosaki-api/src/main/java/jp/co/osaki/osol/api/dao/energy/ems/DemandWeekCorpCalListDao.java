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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandWeekCorpCalListParameter;
import jp.co.osaki.osol.api.result.energy.ems.DemandWeekCorpCalListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandWeekCorpCalListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * デマンド週報企業カレンダ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandWeekCorpCalListDao extends OsolApiDao<DemandWeekCorpCalListParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;

    public DemandWeekCorpCalListDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
    }

    @Override
    public DemandWeekCorpCalListResult query(DemandWeekCorpCalListParameter parameter) throws Exception {

        //企業デマンド情報を取得する
        CorpDemandSelectResult corpDemandParam = DemandEmsUtility.getCorpDemandParam(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> corpDemandList = getResultList(corpDemandSelectServiceDaoImpl, corpDemandParam);
        if (corpDemandList == null || corpDemandList.size() != 1) {
            return new DemandWeekCorpCalListResult();
        }

        //企業集計週報カレンダを取得する
        List<DemandWeekCorpCalListDetailResultData> calList = DemandEmsUtility.getWeekCorpCalList(corpDemandList.get(0),
                getServerDateTime());

        if (calList == null || calList.isEmpty()) {
            return new DemandWeekCorpCalListResult();
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
                            .comparing(DemandWeekCorpCalListDetailResultData::getFiscalYear, Comparator.naturalOrder())
                            .thenComparing(DemandWeekCorpCalListDetailResultData::getWeekNo, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

        }

        return new DemandWeekCorpCalListResult(calList);
    }

}
