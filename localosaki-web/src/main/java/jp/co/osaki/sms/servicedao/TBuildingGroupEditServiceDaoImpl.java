/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-takaide
 */
public class TBuildingGroupEditServiceDaoImpl implements BaseServiceDao<TBuildingGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingGroup> getResultList(TBuildingGroup target, EntityManager em) {
        TypedQuery<TBuildingGroup> query = em.createNamedQuery("TBuildingGroup.findJoinTBuilding", TBuildingGroup.class);
        query.setParameter("corpId", target.getId().getCorpId());
        return query.getResultList();
    }

    @Override
    public List<TBuildingGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingGroup> getResultList(List<TBuildingGroup> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildingGroup find(TBuildingGroup target, EntityManager em) {
        return em.find(TBuildingGroup.class, target.getId());
    }

    @Override
    public void persist(TBuildingGroup target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TBuildingGroup merge(TBuildingGroup target, EntityManager em) {
        TBuildingGroup reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(TBuildingGroup target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingGroup> getResultList(EntityManager em) {
        TypedQuery<TBuildingGroup> query = em.createNamedQuery("TBuildingGroup.findAll", TBuildingGroup.class);
        return query.getResultList();
    }

}
