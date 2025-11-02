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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportPointListResult;
import jp.co.osaki.osol.entity.TDmYearRepPoint;
import jp.co.osaki.osol.entity.TDmYearRepPointPK_;
import jp.co.osaki.osol.entity.TDmYearRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandYearReportPointListServiceDaoImpl implements BaseServiceDao<CommonDemandYearReportPointListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportPointListResult> getResultList(CommonDemandYearReportPointListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        String summaryUnit = t.getSummaryUnit();
        String yearNoFrom = t.getYearNoFrom();
        String yearNoTo = t.getYearNoTo();
        String pointNoFrom = t.getPointNoFrom();
        String pointNoTo = t.getPointNoTo();
        BigDecimal monthNoFrom = t.getMonthNoFrom();
        BigDecimal monthNoTo = t.getMonthNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandYearReportPointListResult> query = builder.createQuery(CommonDemandYearReportPointListResult.class);

        Root<TDmYearRepPoint> root = query.from(TDmYearRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId), smId));
        }
        //集計単位
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.summaryUnit), summaryUnit));
        //年No、月No
        if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoTo));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom));
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo != null) {
            if (yearNoFrom.equals(yearNoTo)) {
                whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom));
                whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoFrom));
                whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoTo));
            } else if (Integer.parseInt(yearNoTo) - Integer.parseInt(yearNoFrom) >= 2) {
                //2年以上間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), String.valueOf(Integer.parseInt(yearNoFrom) + 1)),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), String.valueOf(Integer.parseInt(yearNoTo) - 1))),
                        builder.and(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoFrom)),
                        builder.and(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo), monthNoTo))));
            }
        }

        //ポイントNo
        if (!CheckUtility.isNullOrEmpty(pointNoFrom) && !CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), pointNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), pointNoTo));
        } else if (!CheckUtility.isNullOrEmpty(pointNoFrom) && CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), pointNoFrom));
        }

        query = query.select(builder.construct(CommonDemandYearReportPointListResult.class,
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.summaryUnit),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo),
                root.get(TDmYearRepPoint_.pointAvg),
                root.get(TDmYearRepPoint_.pointMax),
                root.get(TDmYearRepPoint_.pointMin),
                root.get(TDmYearRepPoint_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandYearReportPointListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportPointListResult> getResultList(List<CommonDemandYearReportPointListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportPointListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportPointListResult find(CommonDemandYearReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandYearReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportPointListResult merge(CommonDemandYearReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandYearReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
