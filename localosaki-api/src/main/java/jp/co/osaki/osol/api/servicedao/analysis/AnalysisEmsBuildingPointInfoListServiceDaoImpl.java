/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.entity.TDmDayRepPoint;
import jp.co.osaki.osol.entity.TDmDayRepPointPK_;
import jp.co.osaki.osol.entity.TDmDayRepPoint_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class AnalysisEmsBuildingPointInfoListServiceDaoImpl implements BaseServiceDao<CommonDemandDayReportPointListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportPointListResult> getResultList(CommonDemandDayReportPointListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandDayReportPointListResult> query = builder.createQuery(CommonDemandDayReportPointListResult.class);

        Root<TDmDayRepPoint> root = query.from(TDmDayRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId), smId));
        }
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                measurementDateTo));

        query = query.select(builder.construct(CommonDemandDayReportPointListResult.class,
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo),
                root.get(TDmDayRepPoint_.pointVal),
                root.get(TDmDayRepPoint_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate)),
                        builder.asc(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo)),
                        builder.asc(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandDayReportPointListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportPointListResult> getResultList(List<CommonDemandDayReportPointListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportPointListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportPointListResult find(CommonDemandDayReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandDayReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportPointListResult merge(CommonDemandDayReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandDayReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
