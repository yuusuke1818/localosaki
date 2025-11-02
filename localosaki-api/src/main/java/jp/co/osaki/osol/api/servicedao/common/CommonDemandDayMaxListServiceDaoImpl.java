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

import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayMaxListResult;
import jp.co.osaki.osol.entity.TDmDayMax;
import jp.co.osaki.osol.entity.TDmDayMaxPK_;
import jp.co.osaki.osol.entity.TDmDayMax_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日最大テーブルデータ取得用 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CommonDemandDayMaxListServiceDaoImpl implements BaseServiceDao<CommonDemandDayMaxListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayMaxListResult> getResultList(CommonDemandDayMaxListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long smId = t.getSmId();
        Date measurementDateFrom = t.getMeasurementDateFrom();
        Date measurementDateTo = t.getMeasurementDateTo();
        BigDecimal crntMinFrom = t.getCrntMinFrom();
        BigDecimal crntMinTo = t.getCrntMinTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonDemandDayMaxListResult> query = builder.createQuery(CommonDemandDayMaxListResult.class);

        Root<TDmDayMax> root = query.from(TDmDayMax.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.buildingId), buildingId));
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.smId), smId));
        }
        //計測年月日
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.measurementDate), measurementDateFrom));
        whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.measurementDate), measurementDateTo));
        //指定プロット行
        if (crntMinFrom != null && crntMinTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.crntMin), crntMinFrom));
            whereList.add(builder.lessThanOrEqualTo(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.crntMin), crntMinTo));
        } else if (crntMinFrom != null && crntMinTo == null) {
            whereList.add(builder.equal(root.get(TDmDayMax_.id).get(TDmDayMaxPK_.crntMin), crntMinFrom));
        }

        query = query.select(builder.construct(CommonDemandDayMaxListResult.class,
                root.get(TDmDayMax_.id).get(TDmDayMaxPK_.corpId),
                root.get(TDmDayMax_.id).get(TDmDayMaxPK_.buildingId),
                root.get(TDmDayMax_.id).get(TDmDayMaxPK_.smId),
                root.get(TDmDayMax_.id).get(TDmDayMaxPK_.measurementDate),
                root.get(TDmDayMax_.id).get(TDmDayMaxPK_.crntMin),
                root.get(TDmDayMax_.kwVal),
                root.get(TDmDayMax_.kwValUpdateTime),
                root.get(TDmDayMax_.plotAnalogPointNo1Val),
                root.get(TDmDayMax_.plotAnalogPointNo2Val),
                root.get(TDmDayMax_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonDemandDayMaxListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayMaxListResult> getResultList(List<CommonDemandDayMaxListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CommonDemandDayMaxListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayMaxListResult find(CommonDemandDayMaxListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CommonDemandDayMaxListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommonDemandDayMaxListResult merge(CommonDemandDayMaxListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CommonDemandDayMaxListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
