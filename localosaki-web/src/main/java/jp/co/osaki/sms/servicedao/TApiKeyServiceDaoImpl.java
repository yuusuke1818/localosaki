package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TApiKey;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * APIキー情報
 *
 * @author y-maruta
 */
public class TApiKeyServiceDaoImpl implements BaseServiceDao<TApiKey> {

    /**
     * APIキー情報取得
     *
     * @param target
     * @param em
     * @return ApiKeyList
     */
    @Override
    public List<TApiKey> getResultList(TApiKey target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TApiKey find(TApiKey target, EntityManager em) {
        // APIキー情報を1件取得
        TApiKey reseltObject = em.find(TApiKey.class, target.getId());
        return reseltObject;
    }

    @Override
    public void persist(TApiKey target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public TApiKey merge(TApiKey target, EntityManager em) {
        TApiKey reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(TApiKey target, EntityManager em) {
        TApiKey ms = em.find(TApiKey.class, target.getId());
        em.remove(ms);
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(List<TApiKey> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TApiKey> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
