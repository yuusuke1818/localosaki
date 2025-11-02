package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MCorpTargetAlarm;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業目標超過警報 EntityServiceDaoクラス
 *
 * @author t_hirata
 */
public class MCorpTargetAlarmServiceDaoImpl implements BaseServiceDao<MCorpTargetAlarm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpTargetAlarm> getResultList(MCorpTargetAlarm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpTargetAlarm> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpTargetAlarm> getResultList(List<MCorpTargetAlarm> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpTargetAlarm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorpTargetAlarm find(MCorpTargetAlarm t, EntityManager em) {
        return em.find(MCorpTargetAlarm.class, t.getCorpId());
    }

    @Override
    public void persist(MCorpTargetAlarm t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public MCorpTargetAlarm merge(MCorpTargetAlarm t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(MCorpTargetAlarm t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
