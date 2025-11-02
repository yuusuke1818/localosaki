package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MTenantSm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MTenantSmsServiceDaoImpl implements BaseServiceDao<MTenantSm>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MTenantSm> getResultList(MTenantSm target, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantSm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantSm> getResultList(List<MTenantSm> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantSm> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MTenantSm find(MTenantSm target, EntityManager em) {
        return em.find(MTenantSm.class, target.getId());
    }

    @Override
    public void persist(MTenantSm target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MTenantSm merge(MTenantSm target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MTenantSm target, EntityManager em) {

    }

}
