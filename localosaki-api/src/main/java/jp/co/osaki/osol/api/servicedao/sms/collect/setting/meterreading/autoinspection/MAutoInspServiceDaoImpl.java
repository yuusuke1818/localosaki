package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MAutoInsp;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 自動検針月 日時設定 ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class MAutoInspServiceDaoImpl implements BaseServiceDao<MAutoInsp> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(MAutoInsp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(List<MAutoInsp> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MAutoInsp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MAutoInsp find(MAutoInsp target, EntityManager em) {
        return em.find(MAutoInsp.class, target.getId());
    }

    @Override
    public void persist(MAutoInsp target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MAutoInsp merge(MAutoInsp target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MAutoInsp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
