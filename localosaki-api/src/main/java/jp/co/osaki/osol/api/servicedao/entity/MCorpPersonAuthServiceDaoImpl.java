package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MCorpPersonAuthServiceDaoImpl implements BaseServiceDao<MCorpPersonAuth> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPersonAuth> getResultList(MCorpPersonAuth target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPersonAuth> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPersonAuth> getResultList(List<MCorpPersonAuth> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPersonAuth> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorpPersonAuth find(MCorpPersonAuth target, EntityManager em) {
        return em.find(MCorpPersonAuth.class, target.getId());
    }

    @Override
    public void persist(MCorpPersonAuth target, EntityManager em) {
        em.persist(target);

    }

    @Override
    public MCorpPersonAuth merge(MCorpPersonAuth target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MCorpPersonAuth target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
