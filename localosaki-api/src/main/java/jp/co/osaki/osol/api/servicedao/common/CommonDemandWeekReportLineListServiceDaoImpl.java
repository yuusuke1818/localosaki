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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandWeekReportLineListResult;
import jp.co.osaki.osol.entity.TDmWeekRepLine;
import jp.co.osaki.osol.entity.TDmWeekRepLinePK_;
import jp.co.osaki.osol.entity.TDmWeekRepLine_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド週報系統テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandWeekReportLineListServiceDaoImpl implements BaseServiceDao<CommonDemandWeekReportLineListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportLineListResult> getResultList(CommonDemandWeekReportLineListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String summaryUnit = t.getSummaryUnit();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        String fiscalYearFrom = t.getFiscalYearFrom();
        String fiscalYearTo = t.getFiscalYearTo();
        BigDecimal weekNoFrom = t.getWeekNoFrom();
        BigDecimal weekNoTo = t.getWeekNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandWeekReportLineListResult> query = builder.createQuery(CommonDemandWeekReportLineListResult.class);

        Root<TDmWeekRepLine> root = query.from(TDmWeekRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.buildingId), buildingId));
        //集計単位
        whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.summaryUnit), summaryUnit));
        //系統グループID
        whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.lineGroupId), lineGroupId));
        //系統番号
        whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.lineNo), lineNo));
        //年度、週No
        if (fiscalYearFrom.equals(fiscalYearTo)) {
            whereList.add(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), fiscalYearFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoTo));
        } else if (Integer.parseInt(fiscalYearTo) - Integer.parseInt(fiscalYearFrom) >= 2) {
            //2年以上間隔があいている場合
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoFrom)),
                    builder.and(builder.greaterThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearFrom) + 1)),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), String.valueOf(Integer.parseInt(fiscalYearTo) - 1))),
                    builder.and(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoTo))));
        } else {
            whereList.add(builder.or(
                    builder.and(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), fiscalYearFrom),
                            builder.greaterThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoFrom)),
                    builder.and(builder.equal(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear), fiscalYearTo),
                            builder.lessThanOrEqualTo(root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo), weekNoTo))));
        }

        query = query.select(builder.construct(CommonDemandWeekReportLineListResult.class,
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.corpId),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.buildingId),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.fiscalYear),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.weekNo),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.summaryUnit),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.lineGroupId),
                root.get(TDmWeekRepLine_.id).get(TDmWeekRepLinePK_.lineNo),
                root.get(TDmWeekRepLine_.lineMaxKw),
                root.get(TDmWeekRepLine_.lineValueKwh),
                root.get(TDmWeekRepLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandWeekReportLineListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportLineListResult> getResultList(List<CommonDemandWeekReportLineListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandWeekReportLineListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportLineListResult find(CommonDemandWeekReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandWeekReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandWeekReportLineListResult merge(CommonDemandWeekReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandWeekReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
