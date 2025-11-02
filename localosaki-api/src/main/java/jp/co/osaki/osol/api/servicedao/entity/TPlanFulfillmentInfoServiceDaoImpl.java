/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class TPlanFulfillmentInfoServiceDaoImpl implements BaseServiceDao<TPlanFulfillmentInfo> {

    @Override
    public void persist(TPlanFulfillmentInfo t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TPlanFulfillmentInfo find(TPlanFulfillmentInfo t, EntityManager em) {
        return em.find(TPlanFulfillmentInfo.class, t.getId());
    }

    @Override
    public TPlanFulfillmentInfo merge(TPlanFulfillmentInfo t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentInfo> getResultList(TPlanFulfillmentInfo t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentInfo> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentInfo> getResultList(List<TPlanFulfillmentInfo> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TPlanFulfillmentInfo> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TPlanFulfillmentInfo t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
