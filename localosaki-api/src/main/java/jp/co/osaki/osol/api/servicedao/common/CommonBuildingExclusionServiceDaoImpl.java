package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 排他制御用建物情報取得用　ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonBuildingExclusionServiceDaoImpl implements BaseServiceDao<CommonBuildingExclusionResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonBuildingExclusionResult> getResultList(CommonBuildingExclusionResult target, EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonBuildingExclusionResult> query = builder.createQuery(CommonBuildingExclusionResult.class);

        Root<TBuilding> root = query.from(TBuilding.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query = query.select(builder.construct(CommonBuildingExclusionResult.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                root.get(TBuilding_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonBuildingExclusionResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonBuildingExclusionResult> getResultList(List<CommonBuildingExclusionResult> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonBuildingExclusionResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonBuildingExclusionResult find(CommonBuildingExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(CommonBuildingExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonBuildingExclusionResult merge(CommonBuildingExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(CommonBuildingExclusionResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
