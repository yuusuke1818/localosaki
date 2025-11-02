package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

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

import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants.BUILDING_TYPE;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataInfoResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK_;
import jp.co.osaki.osol.entity.MMeterGroup_;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.co.osaki.osol.entity.MPriceMenuLighta_;
import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.co.osaki.osol.entity.MPriceMenuLightb_;
import jp.co.osaki.osol.entity.MTenantPriceInfo;
import jp.co.osaki.osol.entity.MTenantPriceInfoPK_;
import jp.co.osaki.osol.entity.MTenantPriceInfo_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK_;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.MVarious_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsBillingAmountDataServiceDaoImpl implements BaseServiceDao<ListSmsBillingAmountDataInfoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsBillingAmountDataInfoResultData> getResultList(ListSmsBillingAmountDataInfoResultData target, EntityManager em) {

        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsBillingAmountDataInfoResultData> query = builder.createQuery(ListSmsBillingAmountDataInfoResultData.class);

        Root<MMeter> root = query.from(MMeter.class);
        Join<MMeter, MDevPrm> joinDevPrm = root.join(MMeter_.MDevPrm, JoinType.LEFT);
        Join<MDevPrm, MDevRelation> joinDevRelation = joinDevPrm.join(MDevPrm_.MDevRelations, JoinType.LEFT);
        Join<MDevRelation, TBuilding> joinBuilding1 = joinDevRelation.join(MDevRelation_.TBuilding, JoinType.LEFT);
        Join<TBuilding, MMeterType> joinMeterType = joinBuilding1.join(TBuilding_.MMeterTypes, JoinType.LEFT);
        Join<MMeter, TBuildDevMeterRelation> joinBuildDevMeterRelation = root.join(MMeter_.TBuildDevMeterRelations, JoinType.LEFT);
        Join<TBuildDevMeterRelation, TBuilding> joinBuilding2 = joinBuildDevMeterRelation.join(TBuildDevMeterRelation_.TBuilding, JoinType.LEFT);
        Join<TBuilding, MTenantSm> joinTenantSm = joinBuilding2.join(TBuilding_.MTenantSms);
        Join<TBuilding, MVarious> joinVarious = joinBuilding1.join(TBuilding_.MVariouses, JoinType.LEFT);
        Join<TBuilding, MTenantPriceInfo> joinTenantPriceInfo = joinBuilding2.join(TBuilding_.MTenantPriceInfos, JoinType.LEFT);
        Join<MMeter, MMeterGroup> joinMeterGroup = root.join(MMeter_.MMeterGroups, JoinType.LEFT);
        Join<TBuilding, MPriceMenuLighta> joinPriceMenuLighta = joinBuilding2.join(TBuilding_.MPriceMenuLightas, JoinType.LEFT);
        Join<TBuilding, MPriceMenuLightb> joinPriceMenuLightb = joinBuilding2.join(TBuilding_.MPriceMenuLightbs, JoinType.LEFT);

        joinTenantPriceInfo.on(
        		builder.and(
//        				builder.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.powerPlanId), joinTenantSm.get(MTenantSm_.priceMenuNo)),
        				builder.or(
        						builder.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.powerPlanId), joinTenantSm.get(MTenantSm_.priceMenuNo)),
        						builder.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.powerPlanId), 0)),
        				builder.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.buildingId), joinTenantSm.get(MTenantSm_.id).get(MTenantSmPK_.buildingId)),
                        builder.equal(joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.meterType), root.get(MMeter_.meterType))));

        joinMeterType.on(builder.equal(joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType), root.get(MMeter_.meterType)));

        joinBuildDevMeterRelation.on(
                builder.notEqual(
                        joinBuilding1.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                        joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.buildingId)));
        joinBuilding2.on(
                builder.equal(
                        joinBuilding2.get(TBuilding_.buildingType), BUILDING_TYPE.TENANT.getVal()));


        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(joinBuilding1.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(joinBuilding1.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        query = query.select(builder.construct(ListSmsBillingAmountDataInfoResultData.class,
                joinBuilding2.get(TBuilding_.id).get(TBuildingPK_.corpId),
                joinBuilding2.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinBuilding2.get(TBuilding_.buildingName),
                joinBuilding2.get(TBuilding_.buildingNo),
                joinVarious.get(MVarious_.id),
                joinVarious.get(MVarious_.saleTaxRate),
                joinVarious.get(MVarious_.saleTaxDeal),
                joinVarious.get(MVarious_.decimalFraction),
                joinTenantSm.get(MTenantSm_.fixed1Name),
                joinTenantSm.get(MTenantSm_.fixed1Price),
                joinTenantSm.get(MTenantSm_.fixed2Name),
                joinTenantSm.get(MTenantSm_.fixed2Price),
                joinTenantSm.get(MTenantSm_.fixed3Name),
                joinTenantSm.get(MTenantSm_.fixed3Price),
                joinTenantSm.get(MTenantSm_.fixed4Name),
                joinTenantSm.get(MTenantSm_.fixed4Price),
                joinTenantSm.get(MTenantSm_.priceMenuNo),
                joinTenantSm.get(MTenantSm_.contractCapacity),
                joinTenantSm.get(MTenantSm_.micomDiscCapacity),
                joinTenantSm.get(MTenantSm_.elecHomeDisc),
                joinTenantPriceInfo.get(MTenantPriceInfo_.id),
                joinTenantPriceInfo.get(MTenantPriceInfo_.id).get(MTenantPriceInfoPK_.pricePlanId),
                joinTenantPriceInfo.get(MTenantPriceInfo_.basicPrice),
                joinTenantPriceInfo.get(MTenantPriceInfo_.discountRate),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId),
                root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.memo),
                root.get(MMeter_.meterId),
                root.get(MMeter_.meterType),
                joinMeterType.get(MMeterType_.meterTypeName),
                joinMeterType.get(MMeterType_.unitUsageBased),
                joinMeterGroup.get(MMeterGroup_.id),
                joinMeterGroup.get(MMeterGroup_.id).get(MMeterGroupPK_.meterGroupId),
                joinPriceMenuLighta.get(MPriceMenuLighta_.id),
                joinPriceMenuLighta.get(MPriceMenuLighta_.lowestPrice),
                joinPriceMenuLighta.get(MPriceMenuLighta_.fuelAdjustPrice),
                joinPriceMenuLighta.get(MPriceMenuLighta_.adjustPriceOver15),
                joinPriceMenuLighta.get(MPriceMenuLighta_.renewEnerPrice),
                joinPriceMenuLighta.get(MPriceMenuLighta_.renewPriceOver15),
                joinPriceMenuLightb.get(MPriceMenuLightb_.id),
                joinPriceMenuLightb.get(MPriceMenuLightb_.basicPrice),
                joinPriceMenuLightb.get(MPriceMenuLightb_.fuelAdjustPrice),
                joinPriceMenuLightb.get(MPriceMenuLightb_.renewEnerPrice),
                joinTenantSm.get(MTenantSm_.tenantId))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        query.orderBy(builder.asc(joinBuilding2.get(TBuilding_.id).get(TBuildingPK_.buildingId)));
        query.distinct(true);

//        String hqlQueryString = em.createQuery(query).unwrap(org.hibernate.Query.class).getQueryString();
//        ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
//        SessionImplementor hibernateSession = em.unwrap(SessionImplementor.class);
//        QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator
//                                                               ("", hqlQueryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
//        queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
//        String sqlQueryString = queryTranslator.getSQLString();
//
//        System.out.println("--- sqlQueryString:" + sqlQueryString);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsBillingAmountDataInfoResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsBillingAmountDataInfoResultData> getResultList(List<ListSmsBillingAmountDataInfoResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsBillingAmountDataInfoResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsBillingAmountDataInfoResultData find(ListSmsBillingAmountDataInfoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsBillingAmountDataInfoResultData target, EntityManager em) {

    }

    @Override
    public ListSmsBillingAmountDataInfoResultData merge(ListSmsBillingAmountDataInfoResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsBillingAmountDataInfoResultData target, EntityManager em) {


    }
}
