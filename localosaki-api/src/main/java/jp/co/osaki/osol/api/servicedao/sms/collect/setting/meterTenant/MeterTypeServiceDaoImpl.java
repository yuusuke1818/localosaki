package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTypeResultData;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;


/**
 * メーター種別設定
 * @author nishida.t
 *
 */
public class MeterTypeServiceDaoImpl implements BaseServiceDao<MeterTypeResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MeterTypeResultData> getResultList(MeterTypeResultData target, EntityManager em) {
//        Long meterType = target.getId().getMeterType();
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        // メーター種別取得範囲
        Long minRange = target.getMinRange();
        Long maxRange = target.getMaxRange();
        // メニュー番号
        Long menuNo = target.getMenuNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MeterTypeResultData> query = builder.createQuery(MeterTypeResultData.class);

        Root<MMeterType> root = query.from(MMeterType.class);

        List<Predicate> whereList = new ArrayList<>();

        // メーター種別
//        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), meterType));
        // 企業ID
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), corpId));
        // 建物ID
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), buildingId));

        // 範囲指定をしている場合
        if (minRange != null && maxRange != null && menuNo != null) {
            whereList.add(builder.and(
                    // メーター種別
                    builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), minRange, maxRange),
                    // メニュー番号
                    builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), menuNo)));
        }
        else {
            whereList.add(
                    builder.or(
                            builder.and(
                                    // メーター種別
                                    builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), 1L),
                                    // メニュー番号
                                    builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), 1L, 2L)),
                            builder.and(
                                    // メーター種別
                                    builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), 2L, 20L),
                                    // メニュー番号
                                    builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), 0L))));
        }

        /*
        whereList.add(builder.and(
                                // メーター種別
                                builder.between(root.get(MMeterType_.id).get(MMeterTypePK_.meterType), 2L, 20L),
                                // メニュー番号
                                builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo), 0L)));
        */

        query = query.select(builder.construct(MeterTypeResultData.class,
                root.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                root.get(MMeterType_.id).get(MMeterTypePK_.corpId),
                root.get(MMeterType_.id).get(MMeterTypePK_.buildingId),
                root.get(MMeterType_.id).get(MMeterTypePK_.menuNo),
                root.get(MMeterType_.recDate),
                root.get(MMeterType_.recMan),
                root.get(MMeterType_.meterTypeName),
                root.get(MMeterType_.calcType),
                root.get(MMeterType_.unitPrice),
                root.get(MMeterType_.allComDiv),
                root.get(MMeterType_.unitUsageBased),
                root.get(MMeterType_.autoInspDay),
                root.get(MMeterType_.co2Coefficient),
                root.get(MMeterType_.unitCo2Coefficient),
                root.get(MMeterType_.autoInspHour),
                root.get(MMeterType_.version),
                root.get(MMeterType_.createUserId),
                root.get(MMeterType_.createDate),
                root.get(MMeterType_.updateUserId),
                root.get(MMeterType_.updateDate))).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.corpId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo)));

        /*
        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.corpId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId)),
                        builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.menuNo))
                );
        */

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MeterTypeResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterTypeResultData> getResultList(List<MeterTypeResultData> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterTypeResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MeterTypeResultData find(MeterTypeResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(MeterTypeResultData target, EntityManager em) {

    }

    @Override
    public MeterTypeResultData merge(MeterTypeResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MeterTypeResultData target, EntityManager em) {
    }

}
