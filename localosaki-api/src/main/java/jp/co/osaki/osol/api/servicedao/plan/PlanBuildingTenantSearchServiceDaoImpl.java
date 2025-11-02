/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.resultdata.plan.PlanBuildingTenantSearchDetailResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class PlanBuildingTenantSearchServiceDaoImpl
        implements BaseServiceDao<PlanBuildingTenantSearchDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchDetailResultData> getResultList(PlanBuildingTenantSearchDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchDetailResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        // 企業IDと担当者IDは必須
        Object orgCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.LOGIN_USER_CORP_ID));
        Object selectCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID));
        Object personId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.LOGIN_PERSON_ID));
        Object osakiFlg = Utility.getListFirstItem(map.get(ApiParamKeyConstants.OSAKI_FLG));
        Object inAlwaysPlanFlg = Utility.getListFirstItem(map.get(ApiParamKeyConstants.IN_ALWAYS_PLAN_FLG));
        if (orgCorpId == null || "".equals(orgCorpId)
                || selectCorpId == null || "".equals(selectCorpId)
                || personId == null || "".equals(personId)
                || osakiFlg == null || "".equals(osakiFlg)) {
            return new ArrayList<>();
        }

        Object buildingNo = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_NO));
        // 都道府県
        List<Object> prefectureList = map.get(ApiParamKeyConstants.PREFECTURE_CD);

        Object buildingStatus = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_STATUS));
        Object nyukyoTypeCd = Utility.getListFirstItem(map.get(ApiParamKeyConstants.NYUKYO_TYPE_CD));
        Date nowDate = (Date) Utility.getListFirstItem(map.get("nowDate"));
        Object buildingTenant = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_TENANT_CD));
        Object buildingCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_CORP_ID));
        Object buildingId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        Object divisionCorpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.DIVISION_CORP_ID));
        Object divisionBuildingId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.DIVISION_BUILDING_ID));
        Object inputBuildingBorrowByTenant = Utility
                .getListFirstItem(map.get(ApiParamKeyConstants.INPUT_BUILDING_BORROW_BY_TENANT_FLG));

        // 建物グルーピング
        Object parentGroupId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PARENT_GROUP_ID));
        Object childGroupId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CHILD_GROUP_ID));


        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PlanBuildingTenantSearchDetailResultData> query = builder
                .createQuery(PlanBuildingTenantSearchDetailResultData.class);
        Root<TBuilding> rootTBuilding = query.from(TBuilding.class);
        Join<TBuilding, MPrefecture> joinMPrefecture = rootTBuilding.join(TBuilding_.MPrefecture);
        Join<TBuilding, TBuildingPlanFulfillment> joinTBuildingPlanFulfillment = rootTBuilding
                .join(TBuilding_.TBuildingPlanFulfillments);

        Join<TBuildingPlanFulfillment, TPlanFulfillmentInfo> joinTPlanFulfillmentInfo = joinTBuildingPlanFulfillment
                .join(TBuildingPlanFulfillment_.TPlanFulfillmentInfo, JoinType.LEFT);

        Subquery<TBuilding> subQuery = query.subquery(TBuilding.class);
        Root<TBuilding> subQueryRoot = subQuery.from(TBuilding.class);
        Join<TBuilding, MPrefecture> subQueryPrefecture = subQueryRoot.join(TBuilding_.MPrefecture);
        Join<TBuilding, TBuildingGroup> subQueryGroup = subQueryRoot.join(TBuilding_.TBuildingGroups, JoinType.LEFT);
        Join<TBuildingGroup,MChildGroup> subQueryChildGroup = subQueryGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
        Join<MChildGroup,MParentGroup> subQueryParentGroup = subQueryChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);

        //削除フラグ
        subQueryGroup.on(builder.equal(subQueryGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        subQueryChildGroup.on(builder.equal(subQueryChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        subQueryParentGroup.on(builder.equal(subQueryParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> subQueryWhereList = new ArrayList<>();

        subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.id).get(TBuildingPK_.corpId), selectCorpId));
        subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.id).get(TBuildingPK_.corpId),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)));
        subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));
        if (!"1".equals(osakiFlg)
                || "1".equals(osakiFlg) && buildingStatus != null
                        && ("1".equals(buildingStatus) || "2".equals(buildingStatus))) {
            // 大崎権限ではない場合、大崎権限かつ建物状況が稼働中、稼動終了ではない場合
            subQueryWhereList.add(builder.isNull(subQueryRoot.get(TBuilding_.buildingDelDate)));
        }
        if (buildingTenant != null) {
            subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.buildingType), buildingTenant));
        }
        if (buildingNo != null && !"".equals(buildingNo)) {
            buildingNo = "%" + buildingNo + "%";
            subQueryWhereList.add(builder.or(
                    builder.like(subQueryRoot.get(TBuilding_.buildingNo), buildingNo.toString()),
                    builder.like(subQueryRoot.get(TBuilding_.buildingName), buildingNo.toString())));
        }
        // 県コード(任意)
        if (prefectureList != null && !prefectureList.isEmpty()) {
            subQueryWhereList.add(subQueryPrefecture.get(MPrefecture_.prefectureCd).in(prefectureList));
        }
        if (nyukyoTypeCd != null && !"".equals(nyukyoTypeCd)) {
            subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.nyukyoTypeCd), nyukyoTypeCd.toString()));
        }
        if (buildingCorpId != null && !"".equals(buildingCorpId) && buildingId != null && !"".equals(buildingId)) {
            subQueryWhereList
                    .add(builder.equal(subQueryRoot.get(TBuilding_.divisionCorpId), buildingCorpId.toString()));
            subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.divisionBuildingId),
                    Long.parseLong(buildingId.toString())));
        }
        if (divisionCorpId != null && !"".equals(divisionCorpId) && divisionBuildingId != null
                && !"".equals(divisionBuildingId)) {
            subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.id).get(TBuildingPK_.corpId),
                    divisionCorpId.toString()));
            subQueryWhereList.add(builder.equal(subQueryRoot.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                    Long.parseLong(divisionBuildingId.toString())));
        }
        // 建物グルーピング
        if (parentGroupId != null && !"".equals(parentGroupId)) {
            subQueryWhereList.add(builder.equal(
                    subQueryGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId), parentGroupId));
        }
        if (childGroupId != null && !"".equals(childGroupId)) {
            subQueryWhereList
                    .add(builder.equal(subQueryGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                            childGroupId));
        }

        subQuery.select(subQueryRoot);
        subQuery.distinct(true);
        subQuery.where(builder.and(subQueryWhereList.toArray(new Predicate[] {})));

        // 条件設定
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId),
                selectCorpId));
        whereList.add(builder.exists(subQuery));

        // プラン
        Expression<Long> prevMonthInputCountEx = null;
        Expression<Long> prevMonthNeedCountEx = null;
        Expression<Long> prevMonthAchieveCountEx = null;
        Expression<Long> prevMonthTargetInputCountEx = null;
        Expression<Long> thisMonthInputCountEx = null;
        Expression<Long> thisMonthNeedCountEx = null;
        Expression<Long> thisMonthAchieveCountEx = null;
        Expression<Long> thisMonthTargetInputCountEx = null;
        if (inAlwaysPlanFlg != null && "false".equals(inAlwaysPlanFlg)) {
            prevMonthInputCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthInputCount));
            prevMonthNeedCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthNeedCount));
            prevMonthAchieveCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthAchieveCount));
            prevMonthTargetInputCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthTargetInputCount));
            thisMonthInputCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthInputCount));
            thisMonthNeedCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthNeedCount));
            thisMonthAchieveCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthAchieveCount));
            thisMonthTargetInputCountEx = builder.<Long> selectCase()
                    .when(builder.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                            "4"), 0L)
                    .otherwise(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthTargetInputCount));
        } else {
            prevMonthInputCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthInputCount);
            prevMonthNeedCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthNeedCount);
            prevMonthAchieveCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthAchieveCount);
            prevMonthTargetInputCountEx = joinTBuildingPlanFulfillment
                    .get(TBuildingPlanFulfillment_.prevMonthTargetInputCount);
            thisMonthInputCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthInputCount);
            thisMonthNeedCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthNeedCount);
            thisMonthAchieveCountEx = joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthAchieveCount);
            thisMonthTargetInputCountEx = joinTBuildingPlanFulfillment
                    .get(TBuildingPlanFulfillment_.thisMonthTargetInputCount);
        }

        if (buildingStatus != null && !"".equals(buildingStatus)) {
            switch (buildingStatus.toString()) {
            case "1":
                whereList.add(builder.or(
                        builder.and(
                                builder.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), nowDate),
                                builder.isNull(rootTBuilding.get(TBuilding_.totalEndYm))),
                        builder.and(
                                builder.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), nowDate),
                                builder.greaterThanOrEqualTo(rootTBuilding.get(TBuilding_.totalEndYm), nowDate))));

                break;
            case "2":
                whereList.add(builder.lessThan(rootTBuilding.get(TBuilding_.totalEndYm), nowDate));
                break;
            case "3":
                whereList.add(builder.isNotNull(rootTBuilding.get(TBuilding_.buildingDelDate)));
                break;
            default:
                break;
            }
        }

        whereList.add(builder.equal(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg), 0));
        whereList.add(builder.equal(rootTBuilding.get(TBuilding_.delFlg), 0));

        query = query.select(builder.construct(PlanBuildingTenantSearchDetailResultData.class,
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                rootTBuilding.get(TBuilding_.buildingNo),
                rootTBuilding.get(TBuilding_.buildingName),
                rootTBuilding.get(TBuilding_.buildingType),
                joinMPrefecture.get(MPrefecture_.prefectureCd),
                rootTBuilding.get(TBuilding_.address),
                rootTBuilding.get(TBuilding_.addressBuilding),
                rootTBuilding.get(TBuilding_.nyukyoTypeCd),
                rootTBuilding.get(TBuilding_.divisionBuildingId),
                rootTBuilding.get(TBuilding_.divisionCorpId),
                rootTBuilding.get(TBuilding_.totalStartYm),
                rootTBuilding.get(TBuilding_.totalEndYm),
                rootTBuilding.get(TBuilding_.buildingDelDate),
                builder.sum(prevMonthInputCountEx).as(Long.class),
                builder.sum(prevMonthNeedCountEx).as(Long.class),
                builder.sum(prevMonthAchieveCountEx).as(Long.class),
                builder.sum(prevMonthTargetInputCountEx).as(Long.class),
                builder.sum(thisMonthInputCountEx).as(Long.class),
                builder.sum(thisMonthNeedCountEx).as(Long.class),
                builder.sum(thisMonthAchieveCountEx).as(Long.class),
                builder.sum(thisMonthTargetInputCountEx).as(Long.class),
                rootTBuilding.get(TBuilding_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .groupBy(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                        rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                        rootTBuilding.get(TBuilding_.buildingNo),
                        rootTBuilding.get(TBuilding_.buildingName),
                        rootTBuilding.get(TBuilding_.buildingType),
                        joinMPrefecture.get(MPrefecture_.prefectureCd),
                        rootTBuilding.get(TBuilding_.address),
                        rootTBuilding.get(TBuilding_.addressBuilding),
                        rootTBuilding.get(TBuilding_.nyukyoTypeCd),
                        rootTBuilding.get(TBuilding_.divisionBuildingId),
                        rootTBuilding.get(TBuilding_.divisionCorpId),
                        rootTBuilding.get(TBuilding_.totalStartYm),
                        rootTBuilding.get(TBuilding_.totalEndYm),
                        rootTBuilding.get(TBuilding_.buildingDelDate))
                .orderBy(
                        builder.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                        builder.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));
        //テナント所属建物検索で所属建物が存在しない場合は0件にする
        if (Boolean.valueOf(inputBuildingBorrowByTenant.toString())) {
            return new ArrayList<>();
        } else {
            return em.createQuery(query).getResultList();
        }

    }

    @Override
    public List<PlanBuildingTenantSearchDetailResultData> getResultList(
            List<PlanBuildingTenantSearchDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PlanBuildingTenantSearchDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanBuildingTenantSearchDetailResultData find(PlanBuildingTenantSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(PlanBuildingTenantSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanBuildingTenantSearchDetailResultData merge(PlanBuildingTenantSearchDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(PlanBuildingTenantSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
