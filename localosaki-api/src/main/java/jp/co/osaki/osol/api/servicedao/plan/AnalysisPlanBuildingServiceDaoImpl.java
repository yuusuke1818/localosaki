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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.result.servicedao.AnalysisPlanBuildingResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class AnalysisPlanBuildingServiceDaoImpl implements BaseServiceDao<AnalysisPlanBuildingResultData> {

    @Override
    public List<AnalysisPlanBuildingResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        String corpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID)).toString();
        Object planIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_ID));
        Object buildingIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        Date startDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.TOTAL_START_DATE));
        Date endDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.TOTAL_END_DATE));
        Object parentGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PARENT_GROUP_ID));
        Object childGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CHILD_GROUP_ID));
        Object buildingFilterObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_FILTER));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisPlanBuildingResultData> query = cb.createQuery(AnalysisPlanBuildingResultData.class);

        Root<TBuildingPlanFulfillment> rootTBuildingPlanFulfillment = query.from(TBuildingPlanFulfillment.class);
        Join<TBuildingPlanFulfillment, TBuilding> joinTBuilding = rootTBuildingPlanFulfillment.join(TBuildingPlanFulfillment_.TBuilding);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId), corpId));
        if (planIdObject != null) {
            long planId = Long.parseLong(planIdObject.toString());
            whereList.add(cb.equal(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId), planId));
        }

        if (buildingIdObject != null) {
            long buildingId = Long.parseLong(buildingIdObject.toString());
            if (buildingFilterObject != null && !"".equals(buildingFilterObject)) {
                Boolean biuldingFilter = Boolean.parseBoolean(buildingFilterObject.toString());
                if (biuldingFilter) {
                    whereList.add(
                            cb.or (
                                cb.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId),
                                cb.equal(joinTBuilding.get(TBuilding_.divisionBuildingId), buildingId)));
                } else {
                    whereList.add(cb.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
                }
            } else {
                whereList.add(cb.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
            }
        }

        whereList.add(cb.equal(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg), 0));

        // 建物検索時のお決まり
        whereList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
        whereList.add(cb.or(
                cb.and(cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), endDate),
                        cb.isNull(joinTBuilding.get(TBuilding_.totalEndYm))),
                cb.and(cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), startDate),
                        cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), endDate)),
                cb.and(cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), startDate),
                        cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), endDate)),
                cb.and(cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), endDate),
                        cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), startDate))));
        whereList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));

        if (parentGroupIdObject != null && !"".equals(parentGroupIdObject)) {
            // 建物グループをJOINと条件に組み込む
            Join<TBuilding, TBuildingGroup> joinTBuildingGroup = joinTBuilding.join(TBuilding_.TBuildingGroups,JoinType.LEFT);
            Join<TBuildingGroup, MChildGroup> joinMChildGroup = joinTBuildingGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
            Join<MChildGroup, MParentGroup> joinMParentGroup = joinMChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);
            List<Order> orderByList = new ArrayList<>();

            joinTBuildingGroup.on(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
            joinMChildGroup.on(cb.equal(joinMChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
            joinMParentGroup.on(cb.equal(joinMParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

            whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId),
                    new Long(parentGroupIdObject.toString())));

            orderByList.add(cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)));
            orderByList.add(cb.asc(joinMParentGroup.get(MParentGroup_.displayOrder)));
            orderByList.add(cb.asc(joinMChildGroup.get(MChildGroup_.displayOrder)));
            orderByList.add(cb.asc(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

            if (childGroupIdObject != null && !"".equals(childGroupIdObject)) {
                // 子グループの条件を追加
                whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                        new Long(childGroupIdObject.toString())));
            }
            query = query.select(cb.construct(AnalysisPlanBuildingResultData.class,
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.buildingId),
                    joinTBuilding.get(TBuilding_.buildingType),
                    joinTBuilding.get(TBuilding_.buildingNo),
                    joinTBuilding.get(TBuilding_.buildingName),
                    joinMParentGroup.get(MParentGroup_.parentGroupName),
                    joinMChildGroup.get(MChildGroup_.childGroupName),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthNeedCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthAchieveCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthTargetInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthNeedCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthAchieveCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthTargetInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.createDate),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.updateDate)))
                    .where(cb.and(whereList.toArray(new Predicate[]{})))
                    .orderBy(orderByList.toArray(new Order[]{}));

        } else {
            query = query.select(cb.construct(AnalysisPlanBuildingResultData.class,
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.buildingId),
                    joinTBuilding.get(TBuilding_.buildingType),
                    joinTBuilding.get(TBuilding_.buildingNo),
                    joinTBuilding.get(TBuilding_.buildingName),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthNeedCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthAchieveCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.prevMonthTargetInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthNeedCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthAchieveCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.thisMonthTargetInputCount),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.createDate),
                    rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.updateDate)))
                    .where(cb.and(whereList.toArray(new Predicate[]{})))
                    .orderBy(cb.asc(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.corpId)),
                            cb.asc(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.planFulfillmentId)),
                            cb.asc(rootTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.id).get(TBuildingPlanFulfillmentPK_.buildingId)));
        }
        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanBuildingResultData> getResultList(AnalysisPlanBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanBuildingResultData> getResultList(List<AnalysisPlanBuildingResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanBuildingResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanBuildingResultData find(AnalysisPlanBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisPlanBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanBuildingResultData merge(AnalysisPlanBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisPlanBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
