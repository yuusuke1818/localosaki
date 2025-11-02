package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPrefecture;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MPrefectureServiceDaoImpl implements BaseServiceDao<MPrefecture> {

    @Override
    public List<MPrefecture> getResultList(MPrefecture target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPrefecture find(MPrefecture target, EntityManager em) {
        return em.find(MPrefecture.class, target.getPrefectureCd());
    }

    @Override
    public void persist(MPrefecture target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPrefecture merge(MPrefecture target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MPrefecture target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPrefecture> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPrefecture> getResultList(List<MPrefecture> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPrefecture> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
