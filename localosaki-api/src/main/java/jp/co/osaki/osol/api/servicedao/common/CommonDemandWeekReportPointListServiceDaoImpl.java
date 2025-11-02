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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportPointListResult;
import jp.co.osaki.osol.entity.TDmWeekRepPoint;
import jp.co.osaki.osol.entity.TDmWeekRepPointPK_;
import jp.co.osaki.osol.entity.TDmWeekRepPoint_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド週報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandWeekReportPointListServiceDaoImpl implements BaseServiceDao<CommonDemandWeekReportPointListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportPointListResult> getResultList(CommonDemandWeekReportPointListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        String summaryUnit = t.getSummaryUnit();
        String fiscalYearFrom = t.getFiscalYearFrom();
        String fiscalYearTo = t.getFiscalYearTo();
        BigDecimal weekNoFrom = t.getWeekNoFrom();
        BigDecimal weekNoTo = t.getWeekNoTo();
        String pointNoFrom = t.getPointNoFrom();
        String pointNoTo = t.getPointNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandWeekReportPointListResult> query = builder.createQuery(CommonDemandWeekReportPointListResult.class);

        Root<TDmWeekRepPoint> root = query.from(TDmWeekRepPoint.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.smId), smId));
        }
        //集計単位
        whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.summaryUnit), summaryUnit));

        //年度、週No
        if (fiscalYearFrom.equals(fiscalYearTo)) {
            whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), fiscalYearFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoTo));
        } else if (Integer.parseInt(fiscalYearTo) - Integer.parseInt(fiscalYearFrom) >= 2) {
            //2年以上間隔があいている場合
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoFrom)),
                    builder.and(builder.greaterThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearFrom) + 1)),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearTo) - 1))),
                    builder.and(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoTo))));
        } else {
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoFrom)),
                    builder.and(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo), weekNoTo))));
        }

        //ポイントNo
        if (!CheckUtility.isNullOrEmpty(pointNoFrom) && !CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.pointNo), pointNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.pointNo), pointNoTo));
        } else if (!CheckUtility.isNullOrEmpty(pointNoFrom) && CheckUtility.isNullOrEmpty(pointNoTo)) {
            whereList.add(builder.equal(root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.pointNo), pointNoFrom));
        }

        query = query.select(builder.construct(CommonDemandWeekReportPointListResult.class,
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.corpId),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.buildingId),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.smId),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.fiscalYear),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.weekNo),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.summaryUnit),
                root.get(TDmWeekRepPoint_.id).get(TDmWeekRepPointPK_.pointNo),
                root.get(TDmWeekRepPoint_.pointAvg),
                root.get(TDmWeekRepPoint_.pointMax),
                root.get(TDmWeekRepPoint_.pointMin),
                root.get(TDmWeekRepPoint_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandWeekReportPointListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportPointListResult> getResultList(List<CommonDemandWeekReportPointListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportPointListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportPointListResult find(CommonDemandWeekReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandWeekReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportPointListResult merge(CommonDemandWeekReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandWeekReportPointListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
