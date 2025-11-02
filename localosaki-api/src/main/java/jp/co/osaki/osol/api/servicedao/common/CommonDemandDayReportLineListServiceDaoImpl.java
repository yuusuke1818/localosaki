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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.entity.TDmDayRepLine;
import jp.co.osaki.osol.entity.TDmDayRepLinePK_;
import jp.co.osaki.osol.entity.TDmDayRepLine_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報系統テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandDayReportLineListServiceDaoImpl implements BaseServiceDao<CommonDemandDayReportLineListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportLineListResult> getResultList(CommonDemandDayReportLineListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        BigDecimal jigenNoFrom = t.getJigenNoFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        BigDecimal jigenNoTo = t.getJigenNoTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandDayReportLineListResult> query = builder.createQuery(CommonDemandDayReportLineListResult.class);

        Root<TDmDayRepLine> root = query.from(TDmDayRepLine.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId), buildingId));
        //系統グループID
        whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId), lineGroupId));
        //系統番号
        if(lineNo != null) {
            whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo), lineNo));
        }

        //計測年月日・時限No
        if (measurementDateFrom != null && measurementDateTo != null
                && jigenNoFrom == null && jigenNoTo == null) {
            // 計測年月日の範囲指定
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateTo));
        } else if (measurementDateFrom.equals(measurementDateTo)) {
            whereList.add(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateFrom));
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoTo));
        } else {
            //Fromの2日後
            Date measurementDateFrom2DayAfter = DateUtility.plusDay(measurementDateFrom, 2);
            if (measurementDateFrom2DayAfter.compareTo(measurementDateTo) <= 0) {
                //2日以上の間隔があいている場合
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), DateUtility.plusDay(measurementDateFrom, 1)),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), DateUtility.plusDay(measurementDateTo, -1))),
                        builder.and(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoTo))));
            } else {
                whereList.add(builder.or(
                        builder.and(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateFrom),
                                builder.greaterThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoFrom)),
                        builder.and(builder.equal(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate), measurementDateTo),
                                builder.lessThanOrEqualTo(root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo), jigenNoTo))));
            }
        }

        query = query.select(builder.construct(CommonDemandDayReportLineListResult.class,
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.corpId),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.buildingId),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.measurementDate),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.jigenNo),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineGroupId),
                root.get(TDmDayRepLine_.id).get(TDmDayRepLinePK_.lineNo),
                root.get(TDmDayRepLine_.lineLimitStandardKw),
                root.get(TDmDayRepLine_.lineLowerStandardKw),
                root.get(TDmDayRepLine_.lineValueKw),
                root.get(TDmDayRepLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandDayReportLineListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportLineListResult> getResultList(List<CommonDemandDayReportLineListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayReportLineListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportLineListResult find(CommonDemandDayReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandDayReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayReportLineListResult merge(CommonDemandDayReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandDayReportLineListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
