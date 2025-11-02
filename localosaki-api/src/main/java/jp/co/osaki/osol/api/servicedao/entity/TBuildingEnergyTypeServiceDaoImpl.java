package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TBuildingEnergyType;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物エネルギー種別テーブル　EntityServiceDaoクラス
 * @author y-maruta
 */
public class TBuildingEnergyTypeServiceDaoImpl implements BaseServiceDao<TBuildingEnergyType> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEnergyType> getResultList(TBuildingEnergyType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEnergyType> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEnergyType> getResultList(List<TBuildingEnergyType> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingEnergyType> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildingEnergyType find(TBuildingEnergyType t, EntityManager em) {
        return em.find(TBuildingEnergyType.class, t.getId());
    }

    @Override
    public void persist(TBuildingEnergyType t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TBuildingEnergyType merge(TBuildingEnergyType t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TBuildingEnergyType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
