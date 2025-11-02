package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MBuildingDm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class MBuildingDmServiceDaoImpl implements BaseServiceDao<MBuildingDm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingDm> getResultList(MBuildingDm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingDm> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingDm> getResultList(List<MBuildingDm> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingDm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MBuildingDm find(MBuildingDm t, EntityManager em) {
        return em.find(MBuildingDm.class, t.getId());
    }

    @Override
    public void persist(MBuildingDm t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MBuildingDm merge(MBuildingDm t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MBuildingDm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
