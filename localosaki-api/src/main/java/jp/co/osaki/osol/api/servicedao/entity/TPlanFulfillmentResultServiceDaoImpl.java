/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TPlanFulfillmentResult;
import jp.co.osaki.osol.entity.TPlanFulfillmentResultPK_;
import jp.co.osaki.osol.entity.TPlanFulfillmentResult_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TPlanFulfillmentResultServiceDaoImpl implements BaseServiceDao<TPlanFulfillmentResult> {

    @Override
    public TPlanFulfillmentResult find(TPlanFulfillmentResult t, EntityManager em) {
        return em.find(TPlanFulfillmentResult.class, t.getId());
    }

    @Override
    public void persist(TPlanFulfillmentResult t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TPlanFulfillmentResult merge(TPlanFulfillmentResult t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TPlanFulfillmentResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentResult> getResultList(TPlanFulfillmentResult t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TPlanFulfillmentResult> query = builder.createQuery(TPlanFulfillmentResult.class);
        Root<TPlanFulfillmentResult> tPlanFulfillmentResultRoot = query.from(TPlanFulfillmentResult.class);

        String corpId = t.getId().getCorpId();
        long planFulfillmentId = t.getId().getPlanFulfillmentId();
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.corpId), corpId));
        whereList.add(builder.equal(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.id).get(TPlanFulfillmentResultPK_.planFulfillmentId), planFulfillmentId));
        whereList.add(builder.notEqual(tPlanFulfillmentResultRoot.get(TPlanFulfillmentResult_.delFlg), 1));

        // 検索条件を組み込む
        query = query.select(tPlanFulfillmentResultRoot).where(builder.and(whereList.toArray(new Predicate[]{})));

        // 検索実行
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TPlanFulfillmentResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentResult> getResultList(List<TPlanFulfillmentResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
