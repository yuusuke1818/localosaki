/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MChildGroup;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class MChildGroupServiceDaoImpl implements BaseServiceDao<MChildGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MChildGroup> getResultList(MChildGroup target, EntityManager em) {
//        TypedQuery<MChildGroup> query = em.createNamedQuery("MChildGroup.findCorpIdGroupName", MChildGroup.class);
//        query.setParameter("corpId", target.getId().getCorpId());
//        return query.getResultList();

        if (target.getId().getParentGroupId() != null) {
            TypedQuery<MChildGroup> query = em.createNamedQuery("MChildGroup.findGroupIdGroupName", MChildGroup.class);
            query.setParameter("corpId", target.getId().getCorpId());
            query.setParameter("parentGroupId", target.getId().getParentGroupId());
            if (target.getDelFlg() == null) {
                query.setParameter("delFlg", null);
            } else {
                query.setParameter("delFlg", target.getDelFlg());
            }
            return query.getResultList();
        } else {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<MChildGroup> criteriaQuery = criteriaBuilder.createQuery(MChildGroup.class);
            Root<MChildGroup> childGroup = criteriaQuery.from(MChildGroup.class);
            List<Predicate> MChildGroupList = new ArrayList<>();

            // 企業IDが一致する情報を取得
            MChildGroupList.add(criteriaBuilder.equal(childGroup.get("id").<String>get("corpId"), target.getId().getCorpId()));
            MChildGroupList.add(criteriaBuilder.equal(childGroup.get("delFlg"), target.getDelFlg()));

            // 検索条件を組み込む
            criteriaQuery = criteriaQuery.select(childGroup).where(criteriaBuilder.and(MChildGroupList.toArray(new Predicate[]{})));

            // 検索実行
            List<MChildGroup> resultList = em.createQuery(criteriaQuery).getResultList();
            return resultList;
        }
    }

    @Override
    public List<MChildGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MChildGroup> getResultList(List<MChildGroup> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MChildGroup find(MChildGroup target, EntityManager em) {
        return em.find(MChildGroup.class, target.getId());
    }

    @Override
    public void persist(MChildGroup target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MChildGroup merge(MChildGroup target, EntityManager em) {
        MChildGroup reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(MChildGroup target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MChildGroup> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
