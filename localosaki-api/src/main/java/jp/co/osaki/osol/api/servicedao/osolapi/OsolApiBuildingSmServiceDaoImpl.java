package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物機器
 *
 */
public class OsolApiBuildingSmServiceDaoImpl implements BaseServiceDao<MBuildingSm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MBuildingSm> getResultList(MBuildingSm target, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MBuildingSm> cq = cb.createQuery(MBuildingSm.class);
        Root<MBuildingSm> root = cq.from(MBuildingSm.class);
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId), target.getId().getCorpId()));
        whereList.add(cb.equal(root.get(MBuildingSm_.id).get(MBuildingSmPK_.smId), target.getId().getSmId()));
        cq = cq.select(root).where(cb.and(whereList.toArray(new Predicate[]{})));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<MBuildingSm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MBuildingSm> getResultList(List<MBuildingSm> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MBuildingSm> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MBuildingSm find(MBuildingSm target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(MBuildingSm target, EntityManager em) {

    }

    @Override
    public MBuildingSm merge(MBuildingSm target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MBuildingSm target, EntityManager em) {

    }

}
