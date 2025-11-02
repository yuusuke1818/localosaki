/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.concentrator;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MConcentrator;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * コンセントレータ設定 ServiceDaoクラス
 * @author maruta.y
 *
 */
public class MConcentratorServiceDaoImpl implements BaseServiceDao<MConcentrator> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MConcentrator> getResultList(MConcentrator target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MConcentrator> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MConcentrator> getResultList(List<MConcentrator> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MConcentrator> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MConcentrator find(MConcentrator target, EntityManager em) {
        return em.find(MConcentrator.class, target.getId());
    }

    @Override
    public void persist(MConcentrator target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MConcentrator merge(MConcentrator target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MConcentrator target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
