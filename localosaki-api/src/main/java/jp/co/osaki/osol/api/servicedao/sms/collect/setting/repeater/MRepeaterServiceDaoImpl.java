/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.repeater;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MRepeater;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 中継装置設定 ServiceDaoクラス
 * @author maruta.y
 *
 */
public class MRepeaterServiceDaoImpl implements BaseServiceDao<MRepeater> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MRepeater> getResultList(MRepeater target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MRepeater> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MRepeater> getResultList(List<MRepeater> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MRepeater> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MRepeater find(MRepeater target, EntityManager em) {
        return em.find(MRepeater.class, target.getId());
    }

    @Override
    public void persist(MRepeater target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MRepeater merge(MRepeater target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MRepeater target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
