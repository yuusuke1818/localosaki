package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK_;
import jp.co.osaki.osol.entity.MMeterRange_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MMeterRangeServiceDaoImpl implements BaseServiceDao<MMeterRange> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MMeterRange> getResultList(MMeterRange target, EntityManager em) {
        String corpId = target.getId().getCorpId();
        Long buildingId = target.getId().getBuildingId();
        Long meterType = target.getId().getMeterType();
        Long menuNo = target.getId().getMenuNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeterRange> query = builder.createQuery(MMeterRange.class);

        Root<MMeterRange> root = query.from(MMeterRange.class);

        List<Predicate> whereList = new ArrayList<>();

        if (corpId != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.corpId), corpId));
        }

        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.buildingId), buildingId));
        }

        if (meterType != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.meterType), meterType));
        }

        if (menuNo != null) {
            whereList.add(builder.equal(root.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), menuNo));
        }

        query = query.select(root).where(
                builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(MMeterRange_.id).get(MMeterRangePK_.rangeValue)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeterRange> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MMeterRange> getResultList(List<MMeterRange> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MMeterRange> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MMeterRange find(MMeterRange target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(MMeterRange target, EntityManager em) {

    }

    @Override
    public MMeterRange merge(MMeterRange target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MMeterRange target, EntityManager em) {

    }

}
