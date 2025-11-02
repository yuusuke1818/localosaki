/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MMeterGroup;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ設定 ServiceDaoクラス
 * @author maruta.y
 *
 */
public class MMeterGroupServiceDaoImpl implements BaseServiceDao<MMeterGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(MMeterGroup target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(List<MMeterGroup> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterGroup> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeterGroup find(MMeterGroup target, EntityManager em) {
        return em.find(MMeterGroup.class, target.getId());
    }

    @Override
    public void persist(MMeterGroup target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeterGroup merge(MMeterGroup target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeterGroup target, EntityManager em) {
        em.remove(target);
    }
}
