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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportPointListResult;
import jp.co.osaki.osol.entity.TDmMonthRepPoint;
import jp.co.osaki.osol.entity.TDmMonthRepPointPK_;
import jp.co.osaki.osol.entity.TDmMonthRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド月報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandMonthReportPointListServiceDaoImpl implements BaseServiceDao<CommonDemandMonthReportPointListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportPointListResult> getResultList(CommonDemandMonthReportPointListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        String pointNoFrom = t.getPointNoFrom();
        String pointNoTo = t.getPointNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandMonthReportPointListResult> query = builder.createQuery(CommonDemandMonthReportPointListResult.class);

        Root<TDmMonthRepPoint> root = query.from(TDmMonthRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.smId), smId));
        }
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.measurementDate), measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.measurementDate), measurementDateTo));

        //ポイントNo
        if (!CheckUtility.isNullOrEmpty(pointNoFrom) && !CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.pointNo), pointNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.pointNo), pointNoTo));
        } else if (!CheckUtility.isNullOrEmpty(pointNoFrom) && CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.equal(root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.pointNo), pointNoFrom));
        }

        query = query.select(builder.construct(CommonDemandMonthReportPointListResult.class,
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.corpId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.buildingId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.smId),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.measurementDate),
                root.get(TDmMonthRepPoint_.id).get(TDmMonthRepPointPK_.pointNo),
                root.get(TDmMonthRepPoint_.pointAvg),
                root.get(TDmMonthRepPoint_.pointMax),
                root.get(TDmMonthRepPoint_.pointMin),
                root.get(TDmMonthRepPoint_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandMonthReportPointListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportPointListResult> getResultList(List<CommonDemandMonthReportPointListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportPointListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportPointListResult find(CommonDemandMonthReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandMonthReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportPointListResult merge(CommonDemandMonthReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandMonthReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
