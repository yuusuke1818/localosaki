package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTenantSmsInfoResultData;
import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MeterTenantSmsInfoServiceDaoImpl implements BaseServiceDao<MeterTenantSmsInfoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MeterTenantSmsInfoResultData> getResultList(MeterTenantSmsInfoResultData target, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterTenantSmsInfoResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterTenantSmsInfoResultData> getResultList(List<MeterTenantSmsInfoResultData> entityList,
            EntityManager em) {
        return null;
    }

    @Override
    public List<MeterTenantSmsInfoResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MeterTenantSmsInfoResultData find(MeterTenantSmsInfoResultData target, EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MeterTenantSmsInfoResultData> query = builder.createQuery(MeterTenantSmsInfoResultData.class);
        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MTenantSm> joinMTenantSm = root.join(TBuilding_.MTenantSms , JoinType.LEFT);
        Join<TBuilding, MPriceMenuLighta> joinMPriceMenuLighta = root.join(TBuilding_.MPriceMenuLightas , JoinType.LEFT);
        Join<TBuilding, MPriceMenuLightb> joinMPriceMenuLightb = root.join(TBuilding_.MPriceMenuLightbs , JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();

        // 建物.企業ID（テナント）
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        // 建物.建物ID（テナント）
        whereList.add(builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query = query.select(builder.construct(MeterTenantSmsInfoResultData.class,
                root.get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinMTenantSm.get(MTenantSm_.tenantId),
                joinMTenantSm.get(MTenantSm_.fixed1Name),
                joinMTenantSm.get(MTenantSm_.fixed1Price),
                joinMTenantSm.get(MTenantSm_.fixed2Name),
                joinMTenantSm.get(MTenantSm_.fixed2Price),
                joinMTenantSm.get(MTenantSm_.fixed3Name),
                joinMTenantSm.get(MTenantSm_.fixed3Price),
                joinMTenantSm.get(MTenantSm_.fixed4Name),
                joinMTenantSm.get(MTenantSm_.fixed4Price),
                joinMTenantSm.get(MTenantSm_.priceMenuNo),
                joinMTenantSm.get(MTenantSm_.contractCapacity),
                joinMTenantSm.get(MTenantSm_.divRate1),
                joinMTenantSm.get(MTenantSm_.divRate2),
                joinMTenantSm.get(MTenantSm_.divRate3),
                joinMTenantSm.get(MTenantSm_.divRate4),
                joinMTenantSm.get(MTenantSm_.divRate5),
                joinMTenantSm.get(MTenantSm_.divRate6),
                joinMTenantSm.get(MTenantSm_.divRate7),
                joinMTenantSm.get(MTenantSm_.divRate8),
                joinMTenantSm.get(MTenantSm_.divRate9),
                joinMTenantSm.get(MTenantSm_.divRate10),
                joinMPriceMenuLighta,
                joinMPriceMenuLightb))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        List<MeterTenantSmsInfoResultData> resultList = em.createQuery(query).getResultList();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public void persist(MeterTenantSmsInfoResultData target, EntityManager em) {

    }

    @Override
    public MeterTenantSmsInfoResultData merge(MeterTenantSmsInfoResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MeterTenantSmsInfoResultData target, EntityManager em) {

    }

}
