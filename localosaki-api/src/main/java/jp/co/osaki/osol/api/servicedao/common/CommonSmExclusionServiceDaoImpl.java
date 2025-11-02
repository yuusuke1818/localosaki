package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 排他制御用機器情報取得用　ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonSmExclusionServiceDaoImpl implements BaseServiceDao<CommonSmExclusionResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonSmExclusionResult> getResultList(CommonSmExclusionResult target, EntityManager em) {
        Long smId = target.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonSmExclusionResult> query = builder.createQuery(CommonSmExclusionResult.class);

        Root<MSmPrm> root = query.from(MSmPrm.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(MSmPrm_.smId), smId));

        query = query.select(builder.construct(CommonSmExclusionResult.class,
                root.get(MSmPrm_.smId),
                root.get(MSmPrm_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonSmExclusionResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonSmExclusionResult> getResultList(List<CommonSmExclusionResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonSmExclusionResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonSmExclusionResult find(CommonSmExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(CommonSmExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonSmExclusionResult merge(CommonSmExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(CommonSmExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
