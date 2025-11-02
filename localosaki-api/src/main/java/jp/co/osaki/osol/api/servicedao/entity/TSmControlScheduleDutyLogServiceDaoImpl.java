package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TSmControlScheduleDutyLog;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author y-maruta
 */
public class TSmControlScheduleDutyLogServiceDaoImpl implements BaseServiceDao<TSmControlScheduleDutyLog> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleDutyLog> getResultList(TSmControlScheduleDutyLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleDutyLog> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleDutyLog> getResultList(List<TSmControlScheduleDutyLog> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TSmControlScheduleDutyLog> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TSmControlScheduleDutyLog find(TSmControlScheduleDutyLog t, EntityManager em) {
        return em.find(TSmControlScheduleDutyLog.class, t.getId());
    }

    @Override
    public void persist(TSmControlScheduleDutyLog t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TSmControlScheduleDutyLog merge(TSmControlScheduleDutyLog t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TSmControlScheduleDutyLog t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
