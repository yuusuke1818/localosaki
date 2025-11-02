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

import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointGroupIdResult;
import jp.co.osaki.osol.entity.TDmDayRepLine;
import jp.co.osaki.osol.entity.TDmDayRepLinePK_;
import jp.co.osaki.osol.entity.TDmDayRepLine_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報系統テーブルデータ取得用 ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class AnalysisEmsBuildingPointGroupIdListServiceDaoImpl implements BaseServiceDao<AnalysisEmsBuildingPointGroupIdResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointGroupIdResult> getResultList(AnalysisEmsBuildingPointGroupIdResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsBuildingPointGroupIdResult> query = builder.createQuery(AnalysisEmsBuildingPointGroupIdResult.class);

        Root<TDmDayRepLine> root = query.from(TDmDayRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId), buildingId));

        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                measurementDateTo));

        query = query.select(builder.construct(AnalysisEmsBuildingPointGroupIdResult.class,
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId))).
                where(builder.and(whereList.toArray(new Predicate[]{})))
                .groupBy(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                        root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                        root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId))
                .orderBy(builder.asc(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId)),
                        builder.asc(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId)),
                        builder.asc(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsBuildingPointGroupIdResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointGroupIdResult> getResultList(List<AnalysisEmsBuildingPointGroupIdResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointGroupIdResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsBuildingPointGroupIdResult find(AnalysisEmsBuildingPointGroupIdResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisEmsBuildingPointGroupIdResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsBuildingPointGroupIdResult merge(AnalysisEmsBuildingPointGroupIdResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisEmsBuildingPointGroupIdResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
