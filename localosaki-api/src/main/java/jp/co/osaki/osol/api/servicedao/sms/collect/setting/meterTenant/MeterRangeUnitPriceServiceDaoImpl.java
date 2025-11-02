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
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK_;
import jp.co.osaki.osol.entity.MMeterRange_;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK_;
import jp.co.osaki.osol.entity.MUnitPrice_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 料金単価情報（メーター種別従量値情報をサブクエリで取得して一致させる）
 * @author nishida.t
 *
 */
public class MeterRangeUnitPriceServiceDaoImpl implements BaseServiceDao<MUnitPrice> {

    // 削除に使用（メーター種別従量値情報存在して、料金単価情報に存在しないレコードを削除）
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 更新件数
        int count = 0;
        String corpId = (String)parameterMap.get("corpId").get(0).toString();
        Long buildingId = (Long)parameterMap.get("buildingId").get(0);
        Long meterType = (Long)parameterMap.get("meterType").get(0);
        Long menuNo = (Long)parameterMap.get("menuNo").get(0);
        Long pricePlanId = (Long)parameterMap.get("pricePlanId").get(0);


        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<MUnitPrice> delete = builder.createCriteriaDelete(MUnitPrice.class);
        Root<MUnitPrice> root = delete.from(MUnitPrice.class);

        // サブクエリ
        Subquery<Long> query = delete.subquery(Long.class);
        Root<MUnitPrice> subRoot1 = query.from(MUnitPrice.class);

        // サブクエリ
        Subquery<Long> mMeterRangeSubquery = query.subquery(Long.class);
        Root<MMeterRange> subRoot2 = mMeterRangeSubquery.from(MMeterRange.class);

        List<Predicate> whereSubList = new ArrayList<>();
        // 料金プランID
        whereSubList.add(builder.equal(subRoot2.get(MMeterRange_.id).get(MMeterRangePK_.corpId), corpId));
        whereSubList.add(builder.equal(subRoot2.get(MMeterRange_.id).get(MMeterRangePK_.buildingId), buildingId));
        whereSubList.add(builder.equal(subRoot2.get(MMeterRange_.id).get(MMeterRangePK_.meterType), meterType));
        whereSubList.add(builder.equal(subRoot2.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), menuNo));
        mMeterRangeSubquery.select(subRoot2.get(MMeterRange_.id).get(MMeterRangePK_.rangeValue))
            .where(builder.and(whereSubList.toArray(new Predicate[] {})));

        List<Predicate> whereList = new ArrayList<>();
        // 料金プランID
        whereList.add(builder.equal(subRoot1.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId));
        // 削除用に取得（メーター種別従量値情報存在して、料金単価情報に存在しない）
        whereList.add(subRoot1.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal).in(mMeterRangeSubquery).not());
        query.select(subRoot1.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal))
            .where(builder.and(whereList.toArray(new Predicate[] {})));

        // 条件に一致するレコードを一括更新
        delete.where(builder.and(
                builder.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId),
                root.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal).in(query)));

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
        String corpId = (String)parameterMap.get("corpId").get(0).toString();
        Long buildingId = (Long)parameterMap.get("buildingId").get(0);
        Long meterType = (Long)parameterMap.get("meterType").get(0);
        Long menuNo = (Long)parameterMap.get("menuNo").get(0);
        Long pricePlanId = (Long)parameterMap.get("pricePlanId").get(0);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MUnitPrice> query = builder.createQuery(MUnitPrice.class);
        Root<MUnitPrice> root = query.from(MUnitPrice.class);

        // サブクエリ
        Subquery<Long> mMeterRangeSubquery = query.subquery(Long.class);
        Root<MMeterRange> subRoot = mMeterRangeSubquery.from(MMeterRange.class);

        List<Predicate> whereSubList = new ArrayList<>();
        // 料金プランID
        whereSubList.add(builder.equal(subRoot.get(MMeterRange_.id).get(MMeterRangePK_.corpId), corpId));
        whereSubList.add(builder.equal(subRoot.get(MMeterRange_.id).get(MMeterRangePK_.buildingId), buildingId));
        whereSubList.add(builder.equal(subRoot.get(MMeterRange_.id).get(MMeterRangePK_.meterType), meterType));
        whereSubList.add(builder.equal(subRoot.get(MMeterRange_.id).get(MMeterRangePK_.menuNo), menuNo));

        mMeterRangeSubquery.select(subRoot.get(MMeterRange_.id).get(MMeterRangePK_.rangeValue))
            .where(builder.and(whereSubList.toArray(new Predicate[] {})));

        List<Predicate> whereList = new ArrayList<>();
        // 料金プランID
        whereList.add(builder.equal(root.get(MUnitPrice_.id).get(MUnitPricePK_.pricePlanId), pricePlanId));
        // 表示用に取得（メーター種別従量値情報存在して、料金単価情報に存在する）
        whereList.add(root.get(MUnitPrice_.id).get(MUnitPricePK_.limitUsageVal).in(mMeterRangeSubquery));

        query.select(root)
            .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MUnitPrice> getResultList(List<MUnitPrice> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MUnitPrice> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
