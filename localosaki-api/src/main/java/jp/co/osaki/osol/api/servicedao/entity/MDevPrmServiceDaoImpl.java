/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 装置情報テーブル EntityServiceDaoクラス
 *
 * @author yoneda_y
 */
public class MDevPrmServiceDaoImpl implements BaseServiceDao<MDevPrm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(MDevPrm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(List<MDevPrm> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm find(MDevPrm t, EntityManager em) {
        return em.find(MDevPrm.class, t.getDevId());
    }

    @Override
    public void persist(MDevPrm t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MDevPrm merge(MDevPrm t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MDevPrm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
