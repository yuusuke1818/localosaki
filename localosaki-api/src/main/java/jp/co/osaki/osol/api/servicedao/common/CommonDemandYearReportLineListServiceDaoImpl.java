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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandYearReportLineListResult;
import jp.co.osaki.osol.entity.TDmYearRepLine;
import jp.co.osaki.osol.entity.TDmYearRepLinePK_;
import jp.co.osaki.osol.entity.TDmYearRepLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * デマンド年報系統テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandYearReportLineListServiceDaoImpl implements BaseServiceDao<CommonDemandYearReportLineListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportLineListResult> getResultList(CommonDemandYearReportLineListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String summaryUnit = t.getSummaryUnit();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        String yearNoFrom = t.getYearNoFrom();
        String yearNoTo = t.getYearNoTo();
        BigDecimal monthNoFrom = t.getMonthNoFrom();
        BigDecimal monthNoTo = t.getMonthNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandYearReportLineListResult> query = builder.createQuery(CommonDemandYearReportLineListResult.class);

        Root<TDmYearRepLine> root = query.from(TDmYearRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.buildingId), buildingId));
        //集計単位
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.summaryUnit), summaryUnit));
        //系統グループID
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineGroupId), lineGroupId));
        //系統番号
        whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineNo), lineNo));
        //年No、月No
        if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom == null && monthNoTo == null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoTo));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo == null) {
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom));
            whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoFrom));
        } else if (!CheckUtility.isNullOrEmpty(yearNoFrom) && !CheckUtility.isNullOrEmpty(yearNoTo)
                && monthNoFrom != null && monthNoTo != null) {
            if (yearNoFrom.equals(yearNoTo)) {
                whereList.add(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom));
                whereList.add(builder.greaterThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoFrom));
                whereList.add(builder.lessThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoTo));
            } else if (Integer.parseInt(yearNoTo) - Integer.parseInt(yearNoFrom) >= 2) {
                //2年以上間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), String.valueOf(Integer.parseInt(yearNoFrom) + 1)),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), String.valueOf(Integer.parseInt(yearNoTo) - 1))),
                        builder.and(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoFrom)),
                        builder.and(builder.equal(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo), yearNoTo),
                                builder.lessThanOrEqualTo(root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo), monthNoTo))));
            }
        }

        query = query.select(builder.construct(CommonDemandYearReportLineListResult.class,
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.corpId),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.buildingId),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.yearNo),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.monthNo),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.summaryUnit),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineGroupId),
                root.get(TDmYearRepLine_.id).get(TDmYearRepLinePK_.lineNo),
                root.get(TDmYearRepLine_.lineMaxKw),
                root.get(TDmYearRepLine_.lineValueKwh),
                root.get(TDmYearRepLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandYearReportLineListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportLineListResult> getResultList(List<CommonDemandYearReportLineListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandYearReportLineListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportLineListResult find(CommonDemandYearReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandYearReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandYearReportLineListResult merge(CommonDemandYearReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandYearReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
