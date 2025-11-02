package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物
 *
 */
public class OsolApiBuildingServiceDaoImpl implements BaseServiceDao<TBuilding> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<TBuilding> getResultList(TBuilding target, EntityManager em) {
        return null;
    }

    @Override
    public List<TBuilding> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<TBuilding> getResultList(List<TBuilding> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<TBuilding> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public TBuilding find(TBuilding target, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TBuilding> cq = cb.createQuery(TBuilding.class);
        Root<TBuilding> root = cq.from(TBuilding.class);
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), target.getId().getCorpId()));
        whereList.add(cb.equal(root.get(TBuilding_.buildingNo), target.getBuildingNo()));
        whereList.add(cb.equal(root.get(TBuilding_.delFlg), 0));
        cq = cq.select(root).where(cb.and(whereList.toArray(new Predicate[]{})));
        List<TBuilding> list = em.createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void persist(TBuilding target, EntityManager em) {

    }

    @Override
    public TBuilding merge(TBuilding target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(TBuilding target, EntityManager em) {

    }

}
