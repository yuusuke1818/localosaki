package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MLine;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統テーブル　EntityServiceDaoクラス
 * @author t_hirata
 */
public class MLineServiceDaoImpl implements BaseServiceDao<MLine> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLine> getResultList(MLine t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLine> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLine> getResultList(List<MLine> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLine> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLine find(MLine t, EntityManager em) {
        return em.find(MLine.class, t.getId());
    }

    @Override
    public void persist(MLine t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MLine merge(MLine t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MLine t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
