package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MBuildingSms;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針実行 BuidingSms ServiceDaoクラス
 * @author tominaga.d
 */
public class MBuildingSmsServiceDaoImpl implements BaseServiceDao<MBuildingSms>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(List<MBuildingSms> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MBuildingSms find(MBuildingSms target, EntityManager em) {
        return em.find(MBuildingSms.class, target.getId());
    }

    @Override
    public void persist(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MBuildingSms merge(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
