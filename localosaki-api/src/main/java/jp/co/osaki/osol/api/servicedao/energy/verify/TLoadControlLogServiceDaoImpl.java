/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.TLoadControlLogResult;
import jp.co.osaki.osol.entity.TLoadControlLog;
import jp.co.osaki.osol.entity.TLoadControlLogPK_;
import jp.co.osaki.osol.entity.TLoadControlLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TLoadControlLogServiceDaoImpl implements BaseServiceDao<TLoadControlLogResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TLoadControlLogResult> getResultList(TLoadControlLogResult t, EntityManager em) {
        String recordStartYmdhm = t.getRecordYmdhm();
        String recordEndYmdhm = t.getRecordEndYmdhm();
        long smId = t.getSmId();

        String recordYmdhmFrom = t.getRecordYmdhmFrom();
        String recordYmdhmTo = t.getRecordYmdhmTo();
        Integer orderFlg = t.getOrderFlg();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TLoadControlLogResult> query = builder.createQuery(TLoadControlLogResult.class);

        Root<TLoadControlLog> root = query.from(TLoadControlLog.class);
        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.smId), smId));
        if (!CheckUtility.isNullOrEmpty(recordStartYmdhm)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm), recordStartYmdhm));
        }
        if (!CheckUtility.isNullOrEmpty(recordEndYmdhm)) {
            whereList.add(builder.lessThan(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm),
                    recordEndYmdhm));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmFrom)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm), recordYmdhmFrom));
        }
        if (!CheckUtility.isNullOrEmpty(recordYmdhmTo)) {
            whereList.add(builder.lessThan(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm),
                    recordYmdhmTo));
        }

        if (orderFlg == null || orderFlg.equals(0)) {
            orderFlg = 1;
        }

        // ソート
        List<Order> orderList = new ArrayList<>();
        switch (orderFlg) {
        case 1:
            orderList.add(builder.asc(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm)));
            orderList.add(builder.desc(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.restMs)));
            break;
        case 2:
            orderList.add(builder.desc(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm)));
            orderList.add(builder.asc(root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.restMs)));
            break;
        }

        query = query.select(builder.construct(TLoadControlLogResult.class,
                root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.smId),
                root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.recordYmdhm),
                root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.restMs),
                root.get(TLoadControlLog_.id).get(TLoadControlLogPK_.controlStatus),
                root.get(TLoadControlLog_.targetKw),
                root.get(TLoadControlLog_.nowKw),
                root.get(TLoadControlLog_.adjustKw),
                root.get(TLoadControlLog_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TLoadControlLogResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TLoadControlLogResult> getResultList(List<TLoadControlLogResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TLoadControlLogResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TLoadControlLogResult find(TLoadControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TLoadControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TLoadControlLogResult merge(TLoadControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TLoadControlLogResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
