package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MPriceMenuLightbServiceDaoImpl implements BaseServiceDao<MPriceMenuLightb>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLightb> getResultList(MPriceMenuLightb target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLightb> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLightb> getResultList(List<MPriceMenuLightb> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLightb> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPriceMenuLightb find(MPriceMenuLightb target, EntityManager em) {
        return em.find(MPriceMenuLightb.class, target.getId());
    }

    @Override
    public void persist(MPriceMenuLightb target, EntityManager em) {
        em.persist(target);

    }

    @Override
    public MPriceMenuLightb merge(MPriceMenuLightb target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MPriceMenuLightb target, EntityManager em) {
        em.remove(target);

    }

}
