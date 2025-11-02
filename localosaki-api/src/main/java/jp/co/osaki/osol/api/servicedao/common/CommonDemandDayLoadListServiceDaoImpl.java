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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayLoadListResult;
import jp.co.osaki.osol.entity.TDmDayLoad;
import jp.co.osaki.osol.entity.TDmDayLoadPK_;
import jp.co.osaki.osol.entity.TDmDayLoad_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日負荷テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandDayLoadListServiceDaoImpl implements BaseServiceDao<CommonDemandDayLoadListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayLoadListResult> getResultList(CommonDemandDayLoadListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        BigDecimal crntMinFrom = t.getCrntMinFrom();
        BigDecimal crntMinTo = t.getCrntMinTo();
        BigDecimal controlLoadFrom = t.getControlLoadFrom();
        BigDecimal controlLoadTo = t.getControlLoadTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandDayLoadListResult> query = builder.createQuery(CommonDemandDayLoadListResult.class);

        Root<TDmDayLoad> root = query.from(TDmDayLoad.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.smId), smId));
        }
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.measurementDate), measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.measurementDate), measurementDateTo));
        //指定プロット行
        if (crntMinFrom != null && crntMinTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.crntMin), crntMinFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.crntMin), crntMinTo));
        } else if (crntMinFrom != null && crntMinTo == null) {
            whereList.add(builder.equal(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.crntMin), crntMinFrom));
        }
        //制御負荷
        if (controlLoadFrom != null && controlLoadTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.controlLoad), controlLoadFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.controlLoad), controlLoadTo));
        } else if (controlLoadFrom != null && controlLoadTo == null) {
            whereList.add(builder.equal(root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.controlLoad), controlLoadTo));
        }

        query = query.select(builder.construct(CommonDemandDayLoadListResult.class,
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.corpId),
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.buildingId),
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.smId),
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.measurementDate),
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.crntMin),
                root.get(TDmDayLoad_.id).get(TDmDayLoadPK_.controlLoad),
                root.get(TDmDayLoad_.contactOutStatus),
                root.get(TDmDayLoad_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandDayLoadListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayLoadListResult> getResultList(List<CommonDemandDayLoadListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayLoadListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayLoadListResult find(CommonDemandDayLoadListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandDayLoadListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayLoadListResult merge(CommonDemandDayLoadListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandDayLoadListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
