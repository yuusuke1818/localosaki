package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MLineGroup;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統グループ　EntityServiceDaoクラス
 * @author t_hirata
 */
public class MLineGroupServiceDaoImpl implements BaseServiceDao<MLineGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineGroup> getResultList(MLineGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineGroup> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineGroup> getResultList(List<MLineGroup> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineGroup> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineGroup find(MLineGroup t, EntityManager em) {
        return em.find(MLineGroup.class, t.getId());
    }

    @Override
    public void persist(MLineGroup t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MLineGroup merge(MLineGroup t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MLineGroup t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
