/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MCorp;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業テーブル　EntityServiceDaoクラス（osol-webと名前が重複するので、一時的に"Api"を付与。統一時に削除）
 * @author n-takada
 */
public class MCorpApiServiceDaoImpl implements BaseServiceDao<MCorp> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(MCorp t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(List<MCorp> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorp find(MCorp t, EntityManager em) {
        return em.find(MCorp.class, t.getCorpId());
    }

    @Override
    public void persist(MCorp t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorp merge(MCorp t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MCorp t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
