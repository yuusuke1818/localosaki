package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MCorpPerson;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MCorpPersonServiceDaoImpl implements BaseServiceDao<MCorpPerson> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPerson> getResultList(MCorpPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPerson> getResultList(List<MCorpPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorpPerson find(MCorpPerson target, EntityManager em) {
        return em.find(MCorpPerson.class, target.getId());
    }

    @Override
    public void persist(MCorpPerson target, EntityManager em) {
        em.persist(target);

    }

    @Override
    public MCorpPerson merge(MCorpPerson target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MCorpPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

}
