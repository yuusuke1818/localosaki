package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MSmAirSetting;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器空調設定 EntityServiceDaoクラス
 * @author ya-ishida
 *
 */
public class MSmAirSettingServiceDaoImpl implements BaseServiceDao<MSmAirSetting> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmAirSetting> getResultList(MSmAirSetting target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmAirSetting> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmAirSetting> getResultList(List<MSmAirSetting> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MSmAirSetting> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MSmAirSetting find(MSmAirSetting target, EntityManager em) {
        return em.find(MSmAirSetting.class, target.getId());
    }

    @Override
    public void persist(MSmAirSetting target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MSmAirSetting merge(MSmAirSetting target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MSmAirSetting target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
