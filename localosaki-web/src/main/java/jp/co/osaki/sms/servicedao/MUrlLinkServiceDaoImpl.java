package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MUrlLink;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * URLリンク情報
 *
 * @author y-maruta
 */
public class MUrlLinkServiceDaoImpl implements BaseServiceDao<MUrlLink> {

    /**
     * URLリンク情報取得
     *
     * @param target
     * @param em
     * @return URLリンク情報List
     */
    @Override
    public List<MUrlLink> getResultList(MUrlLink target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MUrlLink find(MUrlLink target, EntityManager em) {
        // URLリンク情報を1件取得
        MUrlLink reseltObject = em.find(MUrlLink.class, target.getUrlLink());

        return reseltObject;
    }

    @Override
    public void persist(MUrlLink target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MUrlLink merge(MUrlLink target, EntityManager em) {
        MUrlLink reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(MUrlLink target, EntityManager em) {
        MUrlLink ms = em.find(MUrlLink.class, target.getUrlLink());
        em.remove(ms);
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MUrlLink> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MUrlLink> getResultList(List<MUrlLink> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MUrlLink> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
