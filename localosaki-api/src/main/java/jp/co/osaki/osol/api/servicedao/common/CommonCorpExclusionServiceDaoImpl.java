package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 排他制御用企業情報取得用　ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonCorpExclusionServiceDaoImpl implements BaseServiceDao<CommonCorpExclusionResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonCorpExclusionResult> getResultList(CommonCorpExclusionResult target, EntityManager em) {
        String corpId = target.getCorpId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonCorpExclusionResult> query = builder.createQuery(CommonCorpExclusionResult.class);

        Root<MCorp> root = query.from(MCorp.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MCorp_.corpId), corpId));

        query = query.select(builder.construct(CommonCorpExclusionResult.class,
                root.get(MCorp_.corpId),
                root.get(MCorp_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonCorpExclusionResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonCorpExclusionResult> getResultList(List<CommonCorpExclusionResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonCorpExclusionResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonCorpExclusionResult find(CommonCorpExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(CommonCorpExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonCorpExclusionResult merge(CommonCorpExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(CommonCorpExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
