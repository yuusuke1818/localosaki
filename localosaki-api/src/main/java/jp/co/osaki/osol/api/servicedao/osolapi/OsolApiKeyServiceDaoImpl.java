package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TApiKey;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * APIキーテーブル ServiceDaoクラス
 *
 * @author take_suzuki
 */
public class OsolApiKeyServiceDaoImpl implements BaseServiceDao<TApiKey> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(TApiKey t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(List<TApiKey> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(EntityManager em) {
        //全件検索
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TApiKey> query = builder.createQuery(TApiKey.class);
        Root<TApiKey> root = query.from(TApiKey.class);

        return em.createQuery(query.select(root)).getResultList();
    }

    @Override
    public TApiKey find(TApiKey t, EntityManager em) {
        return em.find(TApiKey.class, t.getId());
    }

    @Override
    public void persist(TApiKey t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TApiKey merge(TApiKey t, EntityManager em) {
        return em.merge(t);
    }

    @Override
    public void remove(TApiKey t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
