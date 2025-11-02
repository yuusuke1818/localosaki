package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GetDevNameServiceDaoImpl implements BaseServiceDao<MDevPrm>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(List<MDevPrm> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm find(MDevPrm target, EntityManager em) {
        return em.find(MDevPrm.class, target.getDevId());
    }

    @Override
    public void persist(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm merge(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }
}
