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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.resultdata.plan.AnalysisPlanEachPlanResultData;
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
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfoPK_;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class AnalysisPlanEachPlanServiceDaoImpl implements BaseServiceDao<AnalysisPlanEachPlanResultData> {

    @Override
    public List<AnalysisPlanEachPlanResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        String corpId = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CORP_ID)).toString();
        Date planStartDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_START_DATE));
        Date planEndDate = (Date) Utility.getListFirstItem(map.get(ApiParamKeyConstants.PLAN_END_DATE));
        Date totalStartDate = planStartDate;// プラン開始日と集計開始日を同じにする
        Date totalEndDate = planEndDate;// プラン終了日と集計終了日を同じにする
        Object buildingIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_ID));
        Object buildingFilterObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.BUILDING_FILTER));
        Object parentGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.PARENT_GROUP_ID));
        Object childGroupIdObject = Utility.getListFirstItem(map.get(ApiParamKeyConstants.CHILD_GROUP_ID));

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisPlanEachPlanResultData> query = cb.createQuery(AnalysisPlanEachPlanResultData.class);

        Root<TPlanFulfillmentInfo> rootTPlanFulfillmentInfo = query.from(TPlanFulfillmentInfo.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId), corpId));
        whereList.add(cb.equal(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg), 0));

        whereList.add(cb.or(
                cb.and(cb.lessThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planEndDate),
                        cb.greaterThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planStartDate)),
                cb.and(cb.lessThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate),
                        cb.greaterThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planStartDate)),
                cb.and(cb.lessThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planEndDate),
                        cb.greaterThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate)),
                cb.and(cb.lessThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate), planEndDate),
                        cb.greaterThanOrEqualTo(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate), planStartDate))));

        if ((parentGroupIdObject != null && !"".equals(parentGroupIdObject))
                || (buildingIdObject != null && !"".equals(buildingIdObject))) {
            Join<TPlanFulfillmentInfo, TBuildingPlanFulfillment> joinTBuildingPlanFulFillment
                    = rootTPlanFulfillmentInfo.join(TPlanFulfillmentInfo_.TBuildingPlanFulfillments);
            Join<TBuildingPlanFulfillment, TBuilding> joinTBuilding = joinTBuildingPlanFulFillment.join(TBuildingPlanFulfillment_.TBuilding);

            whereList.add(cb.equal(joinTBuilding.get(TBuilding_.delFlg), 0));
            whereList.add(cb.isNull(joinTBuilding.get(TBuilding_.buildingDelDate)));
            whereList.add(cb.or(
                    cb.and(cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), totalEndDate),
                            cb.isNull(joinTBuilding.get(TBuilding_.totalEndYm))),
                    cb.and(cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), totalStartDate),
                            cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), totalEndDate)),
                    cb.and(cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), totalStartDate),
                            cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), totalEndDate)),
                    cb.and(cb.lessThanOrEqualTo(joinTBuilding.get(TBuilding_.totalStartYm), totalEndDate),
                            cb.greaterThanOrEqualTo(joinTBuilding.get(TBuilding_.totalEndYm), totalStartDate))));
            if (buildingIdObject != null && !"".equals(buildingIdObject)) {
                // 建物IDの指定
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
            } else if (parentGroupIdObject != null && !"".equals(parentGroupIdObject)) {
                // 建物グルーピングの指定
                long parentGroupId = Long.parseLong(parentGroupIdObject.toString());
                Join<TBuilding, TBuildingGroup> joinTBuildingGroup = joinTBuilding.join(TBuilding_.TBuildingGroups,JoinType.LEFT);
                Join<TBuildingGroup,MChildGroup> joinChildGroup = joinTBuildingGroup.join(TBuildingGroup_.MChildGroup,JoinType.LEFT);
                Join<MChildGroup,MParentGroup> joinParentGroup = joinChildGroup.join(MChildGroup_.MParentGroup,JoinType.LEFT);

                joinTBuildingGroup.on(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.delFlg), OsolConstants.FLG_OFF));
                joinChildGroup.on(cb.equal(joinChildGroup.get(MChildGroup_.delFlg), OsolConstants.FLG_OFF));
                joinParentGroup.on(cb.equal(joinParentGroup.get(MParentGroup_.delFlg), OsolConstants.FLG_OFF));

                whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.parentGroupId), parentGroupId));
                if (childGroupIdObject != null && !"".equals(childGroupIdObject)) {
                    long childGroupId = Long.parseLong(childGroupIdObject.toString());
                    whereList.add(cb.equal(joinTBuildingGroup.get(TBuildingGroup_.id).get(TBuildingGroupPK_.childGroupId), childGroupId));
                }

                query = query.select(cb.construct(AnalysisPlanEachPlanResultData.class,
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.createDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.updateDate)))
                        .where(cb.and(whereList.toArray(new Predicate[]{})))
                        .groupBy(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.createDate),
                                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.updateDate))
                        .orderBy(cb.asc(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId)),
                                cb.asc(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId)));

                return em.createQuery(query).getResultList();
            }
        }
        // 建物グルーピング検索無しの場合
        query = query.select(cb.construct(AnalysisPlanEachPlanResultData.class,
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.createDate),
                rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.updateDate)))
                .where(cb.and(whereList.toArray(new Predicate[]{})))
                .groupBy(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentName),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentTarget),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentDateType),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentStartDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentEndDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth1),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth2),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth3),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth4),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth5),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth6),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth7),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth8),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth9),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth10),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth11),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonth12),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySunday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyMonday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyTuesday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyWednesday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyThursday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifyFriday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.specifySaturday),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.planFulfillmentContents),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.delFlg),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.createDate),
                        rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.updateDate))
                .orderBy(cb.asc(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.corpId)),
                        cb.asc(rootTPlanFulfillmentInfo.get(TPlanFulfillmentInfo_.id).get(TPlanFulfillmentInfoPK_.planFulfillmentId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachPlanResultData> getResultList(AnalysisPlanEachPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachPlanResultData> getResultList(List<AnalysisPlanEachPlanResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisPlanEachPlanResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanEachPlanResultData find(AnalysisPlanEachPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisPlanEachPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisPlanEachPlanResultData merge(AnalysisPlanEachPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisPlanEachPlanResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
