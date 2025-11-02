package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業機能利用マスタサービスDaoクラス
 * @author nishida.t
 *
 */
public class OsolApiCorpFunctionUseServiceDaoImpl implements BaseServiceDao<MCorpFunctionUse> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpFunctionUse> getResultList(MCorpFunctionUse target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpFunctionUse> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpFunctionUse> getResultList(List<MCorpFunctionUse> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpFunctionUse> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
    * 企業機能利用マスタ検索
    *
    * @param t 企業機能利用マスタエンティティ
    * @param em エンティティマネージャ
    * @return 企業機能利用マスタエンティティ
    */
    @Override
    public MCorpFunctionUse find(MCorpFunctionUse t, EntityManager em) {
        return em.find(MCorpFunctionUse.class, t.getId());
    }

    @Override
    public void persist(MCorpFunctionUse target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorpFunctionUse merge(MCorpFunctionUse target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MCorpFunctionUse target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
