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

import jp.co.osaki.osol.entity.MParentGroup;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class MParentGroupServiceDaoImpl implements BaseServiceDao<MParentGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MParentGroup> getResultList(MParentGroup target, EntityManager em) {
        TypedQuery<MParentGroup> query = em.createNamedQuery("MParentGroup.findCorpIdDelFlgFetch", MParentGroup.class);
        if( target.getDelFlg() == -1)  {
            query = em.createNamedQuery("MParentGroup.findCorpId", MParentGroup.class);
        }
        query.setParameter("corpId", target.getId().getCorpId());
        return query.getResultList();
    }

    @Override
    public List<MParentGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MParentGroup> getResultList(List<MParentGroup> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MParentGroup find(MParentGroup target, EntityManager em) {
        return em.find(MParentGroup.class, target.getId());
    }

    @Override
    public void persist(MParentGroup target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MParentGroup merge(MParentGroup target, EntityManager em) {
        MParentGroup reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(MParentGroup target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MParentGroup> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
