package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MBuildingTargetAlarm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物目標超過警報 EntityServiceDaoクラス
 * @author t_hirata
 */
public class MBuildingTargetAlarmServiceDaoImpl implements BaseServiceDao<MBuildingTargetAlarm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingTargetAlarm> getResultList(MBuildingTargetAlarm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingTargetAlarm> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingTargetAlarm> getResultList(List<MBuildingTargetAlarm> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingTargetAlarm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MBuildingTargetAlarm find(MBuildingTargetAlarm t, EntityManager em) {
        return em.find(MBuildingTargetAlarm.class, t.getId());
    }

    @Override
    public void persist(MBuildingTargetAlarm t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MBuildingTargetAlarm merge(MBuildingTargetAlarm t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MBuildingTargetAlarm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
