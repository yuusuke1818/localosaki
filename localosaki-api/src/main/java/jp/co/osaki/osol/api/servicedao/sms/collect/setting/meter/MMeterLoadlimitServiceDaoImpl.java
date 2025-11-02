/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メータ負荷制限設定 ServiceDaoクラス
 * @author sagi_h
 *
 */
public class MMeterLoadlimitServiceDaoImpl implements BaseServiceDao<MMeterLoadlimit> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterLoadlimit> getResultList(MMeterLoadlimit target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterLoadlimit> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterLoadlimit> getResultList(List<MMeterLoadlimit> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeterLoadlimit> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeterLoadlimit find(MMeterLoadlimit target, EntityManager em) {
        return em.find(MMeterLoadlimit.class, target.getId());
    }

    @Override
    public void persist(MMeterLoadlimit target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MMeterLoadlimit merge(MMeterLoadlimit target, EntityManager em) {
        return em.merge(target);
    }
    @Override
    public void remove(MMeterLoadlimit target, EntityManager em) {

        em.remove(target);
    }

}
