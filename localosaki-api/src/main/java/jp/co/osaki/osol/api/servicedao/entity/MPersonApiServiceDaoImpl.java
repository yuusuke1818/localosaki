/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPerson;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * m_personテーブルをm_personエンティティにてfind, persist, margeするEntityServiceDaoクラス（一時的にAPIをつける）
 *
 * @author n-takada
 */
public class MPersonApiServiceDaoImpl implements BaseServiceDao<MPerson> {

    @Override
    public MPerson find(MPerson t, EntityManager em) {
        return em.find(MPerson.class, t.getId());
    }

    @Override
    public void persist(MPerson t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MPerson merge(MPerson t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MPerson t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(MPerson t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(List<MPerson> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
