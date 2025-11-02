/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportListResult;
import jp.co.osaki.osol.entity.TDmWeekRep;
import jp.co.osaki.osol.entity.TDmWeekRepPK_;
import jp.co.osaki.osol.entity.TDmWeekRep_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド週報テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandWeekReportListServiceDaoImpl implements BaseServiceDao<CommonDemandWeekReportListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportListResult> getResultList(CommonDemandWeekReportListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String summaryUnit = t.getSummaryUnit();
        String fiscalYearFrom = t.getFiscalYearFrom();
        String fiscalYearTo = t.getFiscalYearTo();
        BigDecimal weekNoFrom = t.getWeekNoFrom();
        BigDecimal weekNoTo = t.getWeekNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandWeekReportListResult> query = builder.createQuery(CommonDemandWeekReportListResult.class);

        Root<TDmWeekRep> root = query.from(TDmWeekRep.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.buildingId), buildingId));
        //集計単位
        whereList.add(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.summaryUnit), summaryUnit));

        //年度、週No
        if (fiscalYearFrom.equals(fiscalYearTo)) {
            whereList.add(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), fiscalYearFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoTo));
        } else if (Integer.parseInt(fiscalYearTo) - Integer.parseInt(fiscalYearFrom) >= 2) {
            //2年以上間隔があいている場合
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoFrom)),
                    builder.and(builder.greaterThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearFrom) + 1)),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearTo) - 1))),
                    builder.and(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoTo))));
        } else {
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoFrom)),
                    builder.and(builder.equal(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo), weekNoTo))));
        }

        query = query.select(builder.construct(CommonDemandWeekReportListResult.class,
                root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.corpId),
                root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.buildingId),
                root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.fiscalYear),
                root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.weekNo),
                root.get(TDmWeekRep_.id).get(TDmWeekRepPK_.summaryUnit),
                root.get(TDmWeekRep_.sumDate),
                root.get(TDmWeekRep_.maxKw),
                root.get(TDmWeekRep_.minKw),
                root.get(TDmWeekRep_.workStartTime),
                root.get(TDmWeekRep_.shopOpenTime),
                root.get(TDmWeekRep_.shopCloseTime),
                root.get(TDmWeekRep_.workEndTime),
                root.get(TDmWeekRep_.openTimeKwh),
                root.get(TDmWeekRep_.openPreparationKwh),
                root.get(TDmWeekRep_.closePreparationKwh),
                root.get(TDmWeekRep_.closeTimeKwh),
                root.get(TDmWeekRep_.outAirTempAvg),
                root.get(TDmWeekRep_.outAirTempMax),
                root.get(TDmWeekRep_.outAirTempMin),
                root.get(TDmWeekRep_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandWeekReportListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportListResult> getResultList(List<CommonDemandWeekReportListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportListResult find(CommonDemandWeekReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandWeekReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportListResult merge(CommonDemandWeekReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandWeekReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
