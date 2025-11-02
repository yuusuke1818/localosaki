package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MMeter;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターテーブル ServiceDaoクラス
 * @author tominaga.d
 *
 */
public class MMeterServiceDaoImpl implements BaseServiceDao<MMeter> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(MMeter target, EntityManager em) {
     // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(List<MMeter> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeter find(MMeter target, EntityManager em) {
        return em.find(MMeter.class, target.getId());
    }

    @Override
    public void persist(MMeter target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MMeter merge(MMeter target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeter target, EntityManager em) {
        // 論理削除のため、実装していない
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
