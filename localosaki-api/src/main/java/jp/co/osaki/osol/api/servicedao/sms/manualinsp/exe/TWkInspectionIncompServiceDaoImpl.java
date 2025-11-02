package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TWkInspectionIncomp;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針実行 TWkInspectionIncomp ServiceDaoクラス
 * @author tominaga.d
 */
public class TWkInspectionIncompServiceDaoImpl implements BaseServiceDao<TWkInspectionIncomp>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWkInspectionIncomp> getResultList(TWkInspectionIncomp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWkInspectionIncomp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWkInspectionIncomp> getResultList(List<TWkInspectionIncomp> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TWkInspectionIncomp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TWkInspectionIncomp find(TWkInspectionIncomp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TWkInspectionIncomp target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TWkInspectionIncomp merge(TWkInspectionIncomp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TWkInspectionIncomp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
