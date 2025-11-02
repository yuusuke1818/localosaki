package jp.co.osaki.osol.api.servicedao.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TAielMasterAreaSettingName;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * エリア情報 EntityServiceDaoクラス
 * @author su-sunada
 *
 */
public class TAielMasterAreaSettingNameServiceDaoImpl implements BaseServiceDao<TAielMasterAreaSettingName> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAielMasterAreaSettingName> getResultList(TAielMasterAreaSettingName target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAielMasterAreaSettingName> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAielMasterAreaSettingName> getResultList(List<TAielMasterAreaSettingName> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TAielMasterAreaSettingName> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TAielMasterAreaSettingName find(TAielMasterAreaSettingName target, EntityManager em) {
        return em.find(TAielMasterAreaSettingName.class, target.getId());
    }

    @Override
    public void persist(TAielMasterAreaSettingName target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TAielMasterAreaSettingName merge(TAielMasterAreaSettingName target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TAielMasterAreaSettingName target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
