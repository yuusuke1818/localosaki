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

import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TBuildingPlanFulfillmentServiceDaoImpl implements BaseServiceDao<TBuildingPlanFulfillment> {

    @Override
    public TBuildingPlanFulfillment find(TBuildingPlanFulfillment t, EntityManager em) {
        return em.find(TBuildingPlanFulfillment.class, t.getId());
    }

    @Override
    public void persist(TBuildingPlanFulfillment t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TBuildingPlanFulfillment merge(TBuildingPlanFulfillment t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TBuildingPlanFulfillment t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingPlanFulfillment> getResultList(TBuildingPlanFulfillment t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildingPlanFulfillment> query = builder.createQuery(TBuildingPlanFulfillment.class);
        Root<TBuildingPlanFulfillment> tBuildingPlanFulfillmentRoot = query.from(TBuildingPlanFulfillment.class);

        String corpId = t.getId().getCorpId();
        long planFulfillmentId = t.getId().getPlanFulfillmentId();
        Integer delFlg = t.getDelFlg();
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(tBuildingPlanFulfillmentRoot.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId), corpId));
        if (planFulfillmentId != 0) {
            whereList.add(builder.equal(tBuildingPlanFulfillmentRoot.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId), planFulfillmentId));
        }
        if (delFlg != null) {
            whereList.add(builder.equal(tBuildingPlanFulfillmentRoot.get(TBuildingPlanFulfillment_.delFlg), delFlg));
        }

        // 検索条件を組み込む
        query = query.select(tBuildingPlanFulfillmentRoot).where(builder.and(whereList.toArray(new Predicate[]{})));

        // 検索実行
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TBuildingPlanFulfillment> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingPlanFulfillment> getResultList(List<TBuildingPlanFulfillment> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingPlanFulfillment> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
