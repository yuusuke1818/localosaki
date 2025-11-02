package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MPriceMenuLightaServiceDaoImpl implements BaseServiceDao<MPriceMenuLighta>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLighta> getResultList(MPriceMenuLighta target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLighta> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLighta> getResultList(List<MPriceMenuLighta> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPriceMenuLighta> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPriceMenuLighta find(MPriceMenuLighta target, EntityManager em) {
        return em.find(MPriceMenuLighta.class, target.getId());
    }

    @Override
    public void persist(MPriceMenuLighta target, EntityManager em) {
        em.persist(target);

    }

    @Override
    public MPriceMenuLighta merge(MPriceMenuLighta target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MPriceMenuLighta target, EntityManager em) {
        em.remove(target);

    }

}
