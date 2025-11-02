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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.entity.TDmMonthRepLine;
import jp.co.osaki.osol.entity.TDmMonthRepLinePK_;
import jp.co.osaki.osol.entity.TDmMonthRepLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド月報系統テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandMonthReportLineListServiceDaoImpl implements BaseServiceDao<CommonDemandMonthReportLineListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportLineListResult> getResultList(CommonDemandMonthReportLineListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandMonthReportLineListResult> query = builder.createQuery(CommonDemandMonthReportLineListResult.class);

        Root<TDmMonthRepLine> root = query.from(TDmMonthRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.buildingId), buildingId));
        //系統グループID
        whereList.add(builder.equal(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.lineGroupId), lineGroupId));
        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.lineNo), lineNo));
        }
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.measurementDate), measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.measurementDate), measurementDateTo));

        query = query.select(builder.construct(CommonDemandMonthReportLineListResult.class,
                root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.corpId),
                root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.buildingId),
                root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.measurementDate),
                root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.lineGroupId),
                root.get(TDmMonthRepLine_.id).get(TDmMonthRepLinePK_.lineNo),
                root.get(TDmMonthRepLine_.lineMaxKw),
                root.get(TDmMonthRepLine_.lineValueKwh),
                root.get(TDmMonthRepLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandMonthReportLineListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportLineListResult> getResultList(List<CommonDemandMonthReportLineListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandMonthReportLineListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportLineListResult find(CommonDemandMonthReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandMonthReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandMonthReportLineListResult merge(CommonDemandMonthReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandMonthReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
