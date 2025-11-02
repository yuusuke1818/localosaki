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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants.BUILDING_TYPE;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataInfoSearchResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsMeterReadingServiceDaoImpl implements BaseServiceDao<ListSmsMeterReadingDataInfoSearchResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {

        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        String devId = target.getDevId();
        List<String> devIdList = target.getDevIdList();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterReadingDataInfoSearchResultData> query = builder.createQuery(ListSmsMeterReadingDataInfoSearchResultData.class);

        Root<MMeter> root = query.from(MMeter.class);
        Join<MMeter, MDevPrm> joinDevPrm = root.join(MMeter_.MDevPrm, JoinType.LEFT);
        Join<MDevPrm, MDevRelation> joinDevRelation = joinDevPrm.join(MDevPrm_.MDevRelations, JoinType.LEFT);
        Join<MDevRelation, TBuilding> joinBuilding1 = joinDevRelation.join(MDevRelation_.TBuilding, JoinType.LEFT);
        Join<TBuilding, MMeterType> joinMeterType = joinBuilding1.join(TBuilding_.MMeterTypes, JoinType.LEFT);
        Join<MMeter, TBuildDevMeterRelation> joinBuildDevMeterRelation = root.join(MMeter_.TBuildDevMeterRelations, JoinType.LEFT);
        Join<TBuildDevMeterRelation, TBuilding> joinBuilding2 = joinBuildDevMeterRelation.join(TBuildDevMeterRelation_.TBuilding, JoinType.LEFT);
        Join<TBuilding, MTenantSm> joinTenantSm = joinBuilding2.join(TBuilding_.MTenantSms, JoinType.LEFT);

        joinMeterType.on(
                builder.equal(
                        root.get(MMeter_.meterType),
                        joinMeterType.get(MMeterType_.id).get(MMeterTypePK_.meterType)));
        joinBuilding2.on(
                builder.equal(
                        joinBuilding2.get(TBuilding_.buildingType), BUILDING_TYPE.TENANT.getVal()));


        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(joinBuilding1.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(joinBuilding1.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
        //装置ID
        if(null != devId && !devId.equals("0")){
            whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        }else if(null != devIdList){
            whereList.add(builder.and(root.get(MMeter_.id).get(MMeterPK_.devId).in(devIdList)));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(ListSmsMeterReadingDataInfoSearchResultData.class,
                joinBuilding2.get(TBuilding_.buildingName),
                joinBuilding2.get(TBuilding_.buildingNo),
                joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.devId),
                joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId),
                joinMeterType.get(MMeterType_.meterTypeName),
                joinTenantSm.get(MTenantSm_.tenantId),
                root.get(MMeter_.meterType))).distinct(true).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        query.orderBy(
                builder.asc(joinBuildDevMeterRelation.get(TBuildDevMeterRelation_.id).get(TBuildDevMeterRelationPK_.meterMngId)),
                builder.asc(joinTenantSm.get(MTenantSm_.tenantId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(List<ListSmsMeterReadingDataInfoSearchResultData> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterReadingDataInfoSearchResultData> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterReadingDataInfoSearchResultData find(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {

    }

    @Override
    public ListSmsMeterReadingDataInfoSearchResultData merge(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterReadingDataInfoSearchResultData target, EntityManager em) {


    }
}
