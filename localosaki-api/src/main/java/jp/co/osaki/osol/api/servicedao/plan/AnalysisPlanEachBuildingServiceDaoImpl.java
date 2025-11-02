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
import jp.co.osaki.osol.api.resultdata.plan.AnalysisPlanEachBuildingResultData;
import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroupPK_;
import jp.co.osaki.osol.entity.MChildGroup_;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroupPK_;
import jp.co.osaki.osol.entity.MParentGroup_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK_;
import jp.co.osaki.osol.entity.TBuildingGroup_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class AnalysisPlanEachBuildingServiceDaoImpl implements BaseServiceDao<AnalysisPlanEachBuildingResultData> {

    @Override
    public List<AnalysisPlanEachBuildingResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        String corpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID)).toString();
        Date planStartDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_START_DATE));
        Date planEndDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_END_DATE));
        Date totalStartDate = planStartDate;
        Date totalEndDate = planEndDate;
        Object buildingIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        Object buildingFilterObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_FILTER));
        Object parentGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PARENT_GROUP_ID));
        Object childGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CHILD_GROUP_ID));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisPlanEachBuildingResultData> query = cb.createQuery(AnalysisPlanEachBuildingResultData.class);

        Root<TBuilding> rootTBuilding = query.from(TBuilding.class);
        Join<TBuilding, TBuildingPlanFulfillment> joinTBuildingPlanFulfillment = rootTBuilding.join(TBuilding_.TBuildingPlanFulfillments, JoinType.INNER);
        Join<TBuildingPlanFulfillment, TPlanFulfillmentInfo> joinTPlanFulfillmentInfo = joinTBuildingPlanFulfillment.join(TBuildingPlanFulfillment_.TPlanFulfillmentInfo);
        Join<TBuilding, TBuildingGroup> joinTBuildingGroup = rootTBuilding.join(TBuilding_.TBuildingGroups,JoinType.LEFT);
        Join<TBuildingGroup, MChildGroup> joinMChildGroup = joinTBuildingGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
        Join<MChildGroup, MParentGroup> joinMParentGroup = joinMChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);

        joinTBuildingGroup.on(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
        joinMChildGroup.on(cb.equal(joinMChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
        joinMParentGroup.on(cb.equal(joinMParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

        List<Predicate> whereList = new ArrayList<>();
        List<Order> orderByList = new ArrayList<>();

        whereList.add(cb.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
        whereList.add(cb.equal(rootTBuilding.get(TBuilding_.delFlg), 0));
        whereList.add(cb.isNull(rootTBuilding.get(TBuilding_.buildingDelDate)));
        whereList.add(cb.or(
                cb.and(cb.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), totalEndDate),
                        cb.isNull(rootTBuilding.get(TBuilding_.totalEndYm))),
                cb.and(cb.greaterThanOrEqualTo(rootTBuilding.get(TBuilding_.totalEndYm), totalStartDate),
                        cb.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalEndYm), totalEndDate)),
                cb.and(cb.greaterThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), totalStartDate),
                        cb.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), totalEndDate)),
                cb.and(cb.lessThanOrEqualTo(rootTBuilding.get(TBuilding_.totalStartYm), totalEndDate),
                        cb.greaterThanOrEqualTo(rootTBuilding.get(TBuilding_.totalEndYm), totalStartDate))));

        whereList.add(cb.equal(joinTBuildingPlanFulfillment.get(TBuildingPlanFulfillment_.delFlg), 0));
        whereList.add(cb.equal(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg), 0));
        whereList.add(cb.or(
                cb.and(cb.lessThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planEndDate),
                        cb.greaterThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planStartDate)),
                cb.and(cb.lessThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate),
                        cb.greaterThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planStartDate)),
                cb.and(cb.lessThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planStartDate),
                        cb.greaterThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate)),
                cb.and(cb.lessThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate),
                        cb.greaterThanOrEqualTo(joinTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planStartDate))));

        if (buildingIdObject != null) {
            Long buildingId = Long.parseLong(buildingIdObject.toString());
            if (buildingFilterObject != null && !"".equals(buildingFilterObject)) {
                Boolean biuldingFilter = Boolean.parseBoolean(buildingFilterObject.toString());
                if (biuldingFilter) {
                    whereList.add(
                            cb.or (
                                cb.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId),
                                cb.equal(rootTBuilding.get(TBuilding_.divisionBuildingId), buildingId)));
                } else {
                    whereList.add(cb.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
                }
            } else {
                whereList.add(cb.equal(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));
            }
        }

        if (parentGroupIdObject != null && !"".equals(parentGroupIdObject)) {
            // 建物グループを条件に組み込む
            whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId),
                    new Long(parentGroupIdObject.toString())));

            orderByList.add(cb.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)));
            orderByList.add(cb.asc(joinMParentGroup.get(MParentGroup_.displayOrder)));
            orderByList.add(cb.asc(joinMChildGroup.get(MChildGroup_.displayOrder)));
            orderByList.add(cb.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId)));

            if (childGroupIdObject != null && !"".equals(childGroupIdObject)) {
                // 子グループの条件を追加
                whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId),
                        new Long(childGroupIdObject.toString())));
            }
            query = query.select(cb.construct(AnalysisPlanEachBuildingResultData.class,
                    rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                    joinMParentGroup.get(MParentGroup_.id).get(MParentGroupPK_.parentGroupId),
                    joinMParentGroup.get(MParentGroup_.displayOrder),
                    joinMChildGroup.get(MChildGroup_.id).get(MChildGroupPK_.childGroupId),
                    joinMChildGroup.get(MChildGroup_.displayOrder),
                    rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                    rootTBuilding.get(TBuilding_.buildingType),
                    rootTBuilding.get(TBuilding_.buildingNo),
                    rootTBuilding.get(TBuilding_.buildingName),
                    rootTBuilding.get(TBuilding_.divisionBuildingId)))
                    .where(cb.and(whereList.toArray(new Predicate[]{})))
                    .groupBy(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                            joinMParentGroup.get(MParentGroup_.id).get(MParentGroupPK_.parentGroupId),
                            joinMParentGroup.get(MParentGroup_.displayOrder),
                            joinMChildGroup.get(MChildGroup_.id).get(MChildGroupPK_.childGroupId),
                            joinMChildGroup.get(MChildGroup_.displayOrder),
                            rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                            rootTBuilding.get(TBuilding_.buildingNo),
                            rootTBuilding.get(TBuilding_.buildingName),
                            rootTBuilding.get(TBuilding_.divisionBuildingId))
                    .orderBy(orderByList.toArray(new Order[]{}));
        } else {
            orderByList.add(cb.asc(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)));
            orderByList.add(cb.asc(rootTBuilding.get(TBuilding_.buildingNo)));

            query = query.select(cb.construct(AnalysisPlanEachBuildingResultData.class,
                    rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                    rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                    rootTBuilding.get(TBuilding_.buildingType),
                    rootTBuilding.get(TBuilding_.buildingNo),
                    rootTBuilding.get(TBuilding_.buildingName),
                    rootTBuilding.get(TBuilding_.divisionBuildingId)))
                    .where(cb.and(whereList.toArray(new Predicate[]{})))
                    .groupBy(rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                            rootTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                            rootTBuilding.get(TBuilding_.buildingNo),
                            rootTBuilding.get(TBuilding_.buildingName),
                            rootTBuilding.get(TBuilding_.divisionBuildingId))
                    .orderBy(orderByList.toArray(new Order[]{}));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachBuildingResultData> getResultList(AnalysisPlanEachBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachBuildingResultData> getResultList(List<AnalysisPlanEachBuildingResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachBuildingResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanEachBuildingResultData find(AnalysisPlanEachBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisPlanEachBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanEachBuildingResultData merge(AnalysisPlanEachBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisPlanEachBuildingResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
