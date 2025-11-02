/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportListResult;
import jp.co.osaki.osol.entity.TDmMonthRep;
import jp.co.osaki.osol.entity.TDmMonthRepPK_;
import jp.co.osaki.osol.entity.TDmMonthRep_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド月報テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandMonthReportListServiceDaoImpl implements BaseServiceDao<CommonDemandMonthReportListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportListResult> getResultList(CommonDemandMonthReportListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandMonthReportListResult> query = builder.createQuery(CommonDemandMonthReportListResult.class);

        Root<TDmMonthRep> root = query.from(TDmMonthRep.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.buildingId), buildingId));
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate), measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate), measurementDateTo));

        query = query.select(builder.construct(CommonDemandMonthReportListResult.class,
                root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.corpId),
                root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.buildingId),
                root.get(TDmMonthRep_.id).get(TDmMonthRepPK_.measurementDate),
                root.get(TDmMonthRep_.sumDate),
                root.get(TDmMonthRep_.targetKw),
                root.get(TDmMonthRep_.maxKw),
                root.get(TDmMonthRep_.minKw),
                root.get(TDmMonthRep_.workStartTime),
                root.get(TDmMonthRep_.shopOpenTime),
                root.get(TDmMonthRep_.shopCloseTime),
                root.get(TDmMonthRep_.workEndTime),
                root.get(TDmMonthRep_.openTimeKwh),
                root.get(TDmMonthRep_.openPreparationKwh),
                root.get(TDmMonthRep_.closePreparationKwh),
                root.get(TDmMonthRep_.closeTimeKwh),
                root.get(TDmMonthRep_.outAirTempAvg),
                root.get(TDmMonthRep_.outAirTempMax),
                root.get(TDmMonthRep_.outAirTempMin),
                root.get(TDmMonthRep_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<CommonDemandMonthReportListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportListResult> getResultList(List<CommonDemandMonthReportListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportListResult find(CommonDemandMonthReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandMonthReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportListResult merge(CommonDemandMonthReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandMonthReportListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
