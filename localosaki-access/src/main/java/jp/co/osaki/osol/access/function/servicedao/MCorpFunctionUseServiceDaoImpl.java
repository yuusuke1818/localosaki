package jp.co.osaki.osol.access.function.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MCorpFunctionUsePK_;
import jp.co.osaki.osol.entity.MCorpFunctionUse_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * 企業機能利用マスタサービスDaoクラス
 *
 * @author take_suzuki
 */
public class MCorpFunctionUseServiceDaoImpl implements BaseServiceDao<MCorpFunctionUse> {

    @Override
    @Deprecated
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public List<MCorpFunctionUse> getResultList(MCorpFunctionUse t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorpFunctionUse> getResultList(Map<String, List<Object>> map, EntityManager em) {

		String corpId = map.get("corpId").get(0).toString();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MCorpFunctionUse> query = builder.createQuery(MCorpFunctionUse.class);

        Root<MCorpFunctionUse> root = query.from(MCorpFunctionUse.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MCorpFunctionUse_.id).get(MCorpFunctionUsePK_.corpId), corpId));

        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

		return em.createQuery(query).getResultList();
    }

    @Override
    @Deprecated
    public List<MCorpFunctionUse> getResultList(List<MCorpFunctionUse> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public List<MCorpFunctionUse> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
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
    @Deprecated
    public void persist(MCorpFunctionUse t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public MCorpFunctionUse merge(MCorpFunctionUse t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public void remove(MCorpFunctionUse t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
