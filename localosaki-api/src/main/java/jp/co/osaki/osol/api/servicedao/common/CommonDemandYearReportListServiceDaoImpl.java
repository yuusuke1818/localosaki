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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportListResult;
import jp.co.osaki.osol.entity.TDmYearRep;
import jp.co.osaki.osol.entity.TDmYearRepPK_;
import jp.co.osaki.osol.entity.TDmYearRep_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandYearReportListServiceDaoImpl implements BaseServiceDao<CommonDemandYearReportListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportListResult> getResultList(CommonDemandYearReportListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String summaryUnit = t.getSummaryUnit();
        String yearNoFrom = t.getYearNoFrom();
        String yearNoTo = t.getYearNoTo();
        BigDecimal monthNoFrom = t.getMonthNoFrom();
        BigDecimal monthNoTo = t.getMonthNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandYearReportListResult> query = builder.createQuery(CommonDemandYearReportListResult.class);

        Root<TDmYearRep> root = query.from(TDmYearRep.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.buildingId), buildingId));
        //集計単位
        whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.summaryUnit), summaryUnit));
        //年No、月No
        if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoTo));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom));
            whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo != null) {
            if (yearNoFrom.equals(yearNoTo)) {
                whereList.add(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom));
                whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoFrom));
                whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoTo));
            } else if (Integer.parseInt(yearNoTo) - Integer.parseInt(yearNoFrom) >= 2) {
                //2年以上間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), String.valueOf(Integer.parseInt(yearNoFrom) + 1)),
                                builder.lessThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), String.valueOf(Integer.parseInt(yearNoTo) - 1))),
                        builder.and(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoFrom)),
                        builder.and(builder.equal(root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo), monthNoTo))));
            }
        }

        query = query.select(builder.construct(CommonDemandYearReportListResult.class,
                root.get(TDmYearRep_.id).get(TDmYearRepPK_.corpId),
                root.get(TDmYearRep_.id).get(TDmYearRepPK_.buildingId),
                root.get(TDmYearRep_.id).get(TDmYearRepPK_.yearNo),
                root.get(TDmYearRep_.id).get(TDmYearRepPK_.monthNo),
                root.get(TDmYearRep_.id).get(TDmYearRepPK_.summaryUnit),
                root.get(TDmYearRep_.sumDate),
                root.get(TDmYearRep_.maxKw),
                root.get(TDmYearRep_.minKw),
                root.get(TDmYearRep_.workStartTime),
                root.get(TDmYearRep_.shopOpenTime),
                root.get(TDmYearRep_.shopCloseTime),
                root.get(TDmYearRep_.workEndTime),
                root.get(TDmYearRep_.openTimeKwh),
                root.get(TDmYearRep_.openPreparationKwh),
                root.get(TDmYearRep_.closePreparationKwh),
                root.get(TDmYearRep_.closeTimeKwh),
                root.get(TDmYearRep_.outAirTempAvg),
                root.get(TDmYearRep_.outAirTempMax),
                root.get(TDmYearRep_.outAirTempMin),
                root.get(TDmYearRep_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandYearReportListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportListResult> getResultList(List<CommonDemandYearReportListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportListResult find(CommonDemandYearReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandYearReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportListResult merge(CommonDemandYearReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandYearReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
