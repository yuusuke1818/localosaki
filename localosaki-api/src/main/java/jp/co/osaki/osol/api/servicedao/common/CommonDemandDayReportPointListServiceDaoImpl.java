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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.entity.TDmDayRepPoint;
import jp.co.osaki.osol.entity.TDmDayRepPointPK_;
import jp.co.osaki.osol.entity.TDmDayRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandDayReportPointListServiceDaoImpl implements BaseServiceDao<CommonDemandDayReportPointListResult> {

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
        BigDecimal jigenNoFrom = t.getJigenNoFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        BigDecimal jigenNoTo = t.getJigenNoTo();
        String pointNoFrom = t.getPointNoFrom();
        String pointNoTo = t.getPointNoTo();

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
        //計測年月日・時限No
        if (measurementDateFrom.equals(measurementDateTo)) {
            whereList.add(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), measurementDateFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoTo));
        } else {
            //Fromの2日後
            Date measurementDateFrom2DayAfter = DateUtility.plusDay(measurementDateFrom, 2);
            if (measurementDateFrom2DayAfter.compareTo(measurementDateTo) <= 0) {
                //2日以上の間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), DateUtility.plusDay(measurementDateFrom, 1)),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), DateUtility.plusDay(measurementDateTo, -1))),
                        builder.and(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo), jigenNoTo))));
            }
        }
        //ポイントNo
        if (!CheckUtility.isNullOrEmpty(pointNoFrom) && !CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo), pointNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo), pointNoTo));
        } else if (!CheckUtility.isNullOrEmpty(pointNoFrom) && CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.equal(root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo), pointNoFrom));
        }

        query = query.select(builder.construct(CommonDemandDayReportPointListResult.class,
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.corpId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.buildingId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.smId),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.measurementDate),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.jigenNo),
                root.get(TDmDayRepPoint_.id).get(TDmDayRepPointPK_.pointNo),
                root.get(TDmDayRepPoint_.pointVal),
                root.get(TDmDayRepPoint_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

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
