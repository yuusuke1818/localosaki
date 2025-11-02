package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK_;
import jp.co.osaki.osol.entity.MUnitPrice_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class UnitPriceServiceDaoImpl implements BaseServiceDao<MUnitPrice> {

    // 一括削除に使用
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 更新件数
        int count = 0;
        Long pricePlanId = (Long)parameterMap.get("pricePlanId").get(0);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<MUnitPrice> delete = builder.createCriteriaDelete(MUnitPrice.class);
        Root<MUnitPrice> root = delete.from(MUnitPrice.class);

        List<Predicate> whereList = new ArrayList<>();
        // 料金プランID
        whereList.add(builder.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId));

        // 条件に一致するレコードを一括更新
        delete.where(builder.and(
                builder.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId)));

        count += em.createQuery(delete).executeUpdate();
        return count;
    }

    @Override
    public List<MUnitPrice> getResultList(MUnitPrice target, EntityManager em) {

        Long pricePlanId = target.getId().getPricePlanId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MUnitPrice> query = builder.createQuery(MUnitPrice.class);

        Root<MUnitPrice> root = query.from(MUnitPrice.class);

        List<Predicate> whereList = new ArrayList<>();
        //メーター種別
        whereList.add(builder.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[]{})));

        query.orderBy(builder.asc(root.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MUnitPrice> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<MUnitPrice> getResultList(List<MUnitPrice> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<MUnitPrice> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public MUnitPrice find(MUnitPrice target, EntityManager em) {
        return em.find(MUnitPrice.class, target.getId());
    }

    @Override
    public void persist(MUnitPrice target, EntityManager em) {
        em.persist(target);
    }

    @Override
    public MUnitPrice merge(MUnitPrice target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MUnitPrice target, EntityManager em) {


    }
}
