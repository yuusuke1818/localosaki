/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportListResult;
import jp.co.osaki.osol.entity.TDmDayRep;
import jp.co.osaki.osol.entity.TDmDayRepPK_;
import jp.co.osaki.osol.entity.TDmDayRep_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandDayReportListServiceDaoImpl implements BaseServiceDao<CommonDemandDayReportListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportListResult> getResultList(CommonDemandDayReportListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        BigDecimal jigenNoFrom = t.getJigenNoFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        BigDecimal jigenNoTo = t.getJigenNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandDayReportListResult> query = builder.createQuery(CommonDemandDayReportListResult.class);

        Root<TDmDayRep> root = query.from(TDmDayRep.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId), buildingId));

        //計測年月日・時限No
        if (measurementDateFrom.equals(measurementDateTo)) {
            whereList.add(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoTo));
        } else {
            //Fromの2日後
            Date measurementDateFrom2DayAfter = DateUtility.plusDay(measurementDateFrom, 2);
            if (measurementDateFrom2DayAfter.compareTo(measurementDateTo) <= 0) {
                //2日以上の間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), DateUtility.plusDay(measurementDateFrom, 1)),
                                builder.lessThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), DateUtility.plusDay(measurementDateTo, -1))),
                        builder.and(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.equal(root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo), jigenNoTo))));
            }
        }

        query = query.select(builder.construct(CommonDemandDayReportListResult.class,
                root.get(TDmDayRep_.id).get(TDmDayRepPK_.corpId),
                root.get(TDmDayRep_.id).get(TDmDayRepPK_.buildingId),
                root.get(TDmDayRep_.id).get(TDmDayRepPK_.measurementDate),
                root.get(TDmDayRep_.id).get(TDmDayRepPK_.jigenNo),
                root.get(TDmDayRep_.sumDate),
                root.get(TDmDayRep_.targetKw),
                root.get(TDmDayRep_.outAirTemp),
                root.get(TDmDayRep_.recordDate),
                root.get(TDmDayRep_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandDayReportListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportListResult> getResultList(List<CommonDemandDayReportListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportListResult find(CommonDemandDayReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandDayReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportListResult merge(CommonDemandDayReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandDayReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
