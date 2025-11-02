/**
*
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TCommand;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * コマンド処理テーブル ServiceDaoクラス
 * @author sagi_h
 *
 */
public class TCommandServiceDaoImpl implements BaseServiceDao<TCommand> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TCommand> getResultList(TCommand target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TCommand> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TCommand> getResultList(List<TCommand> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TCommand> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TCommand find(TCommand target, EntityManager em) {
        return em.find(TCommand.class, target.getId());
    }

    @Override
    public void persist(TCommand target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public TCommand merge(TCommand target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(TCommand target, EntityManager em) {
        em.remove(target);
    }

}
