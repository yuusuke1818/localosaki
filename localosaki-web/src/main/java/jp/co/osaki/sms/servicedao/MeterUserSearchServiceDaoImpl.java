package jp.co.osaki.sms.servicedao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPerson;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 担当者情報(メーターユーザー一覧用)
 *
 * @author nishida.t
 */
public class MeterUserSearchServiceDaoImpl implements BaseServiceDao<MPerson> {

    @Override
    public List<MPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 担当者情報リスト取得
     *
     * @param target
     * @param em
     * @return 担当者情報List
     */
    @Override
    public List<MPerson> getResultList(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 担当者情報取得処理
     *
     * @param target 担当者キー情報Bean
     * @param em エンティティマネージャ
     * @return 担当者情報(Mperson)
     */
    @Override
    public MPerson find(MPerson target, EntityManager em) {
        // 担当者情報を1件取得
        MPerson reseltObject = em.find(MPerson.class, target.getId());
        return reseltObject;
    }

    @Override
    public void persist(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPerson merge(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MPerson target, EntityManager em) {

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(List<MPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
