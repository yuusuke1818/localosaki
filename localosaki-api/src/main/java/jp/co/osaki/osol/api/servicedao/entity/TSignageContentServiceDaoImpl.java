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

import jp.co.osaki.osol.entity.TSignageContent;
import jp.co.osaki.osol.entity.TSignageContentPK_;
import jp.co.osaki.osol.entity.TSignageContent_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * サイネージコンテンツ EntityServiceDaoクラス
 * @author n-takada
 */
public class TSignageContentServiceDaoImpl implements BaseServiceDao<TSignageContent> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSignageContent> getResultList(TSignageContent t, EntityManager em) {

        String corpId = t.getId().getCorpId();
        Long buildingId = t.getId().getBuildingId();
        String signageContentsType = t.getSignageContentsType();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TSignageContent> query = cb.createQuery(TSignageContent.class);
        Root<TSignageContent> root = query.from(TSignageContent.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(TSignageContent_.id).get(TSignageContentPK_.corpId), corpId));
        whereList.add(cb.equal(root.get(TSignageContent_.id).get(TSignageContentPK_.buildingId), buildingId));
        whereList.add(cb.equal(root.get(TSignageContent_.signageContentsType), signageContentsType));
        query = query.select(root)
                .where(cb.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TSignageContent> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSignageContent> getResultList(List<TSignageContent> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSignageContent> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSignageContent find(TSignageContent t, EntityManager em) {
        return em.find(TSignageContent.class, t.getId());
    }

    @Override
    public void persist(TSignageContent t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSignageContent merge(TSignageContent t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSignageContent t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
