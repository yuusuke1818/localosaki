package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MTenantPriceInfo;
import jp.co.osaki.osol.entity.MTenantPriceInfoPK_;
import jp.co.osaki.osol.entity.MTenantPriceInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MTenantPriceInfoServiceDaoImpl implements BaseServiceDao<MTenantPriceInfo>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MTenantPriceInfo> getResultList(MTenantPriceInfo target, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantPriceInfo> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantPriceInfo> getResultList(List<MTenantPriceInfo> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MTenantPriceInfo> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MTenantPriceInfo find(MTenantPriceInfo target, EntityManager em) {
        String corpId = target.getId().getCorpId();
        Long buildingId = target.getId().getBuildingId();
        Long meterType = target.getId().getMeterType();
        Long powerPlanId = target.getId().getPowerPlanId();
        Long pricePlanId = target.getId().getPricePlanId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MTenantPriceInfo> query = builder.createQuery(MTenantPriceInfo.class);
        Root<MTenantPriceInfo> root = query.from(MTenantPriceInfo.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.meterType), meterType));
        whereList.add(builder.equal(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.powerPlanId), powerPlanId));
        whereList.add(builder.equal(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.corpId), corpId));
        whereList.add(builder.equal(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.buildingId), buildingId));

        if (pricePlanId != null) {
            whereList.add(builder.equal(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.pricePlanId), pricePlanId));
        }

        query = query.select(root).where(builder.and(
                whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.desc(root.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.pricePlanId)));

        List<MTenantPriceInfo> resultList = em.createQuery(query).getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public void persist(MTenantPriceInfo target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MTenantPriceInfo merge(MTenantPriceInfo target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MTenantPriceInfo target, EntityManager em) {
        em.remove(target);
    }

}
