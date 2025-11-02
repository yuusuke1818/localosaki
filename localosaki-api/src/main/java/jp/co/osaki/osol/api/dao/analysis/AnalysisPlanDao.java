/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisPlanParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisPlanResult;
import jp.co.osaki.osol.api.result.servicedao.AnalysisPlanBuildingResultData;
import jp.co.osaki.osol.api.resultdata.plan.AnalysisPlanEachBuildingResultData;
import jp.co.osaki.osol.api.resultdata.plan.AnalysisPlanEachPlanResultData;
import jp.co.osaki.osol.api.resultdata.plan.PlanFulfillmentResultDetailResultData;
import jp.co.osaki.osol.api.servicedao.plan.AnalysisPlanBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.AnalysisPlanEachBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.AnalysisPlanEachPlanServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.PlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 集計分析-計画履行 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class AnalysisPlanDao extends OsolApiDao<AnalysisPlanParameter> {

    private final AnalysisPlanEachPlanServiceDaoImpl analysisPlanEachPlanServiceDaoImpl;
    private final AnalysisPlanBuildingServiceDaoImpl analysisPlanBuildingServiceDaoImpl;
    private final PlanFulfillmentResultServiceDaoImpl planFulfillmentResultServiceDaoImpl;
    private final AnalysisPlanEachBuildingServiceDaoImpl analysisPlanEachBuildingServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AnalysisPlanDao() {
        analysisPlanEachPlanServiceDaoImpl = new AnalysisPlanEachPlanServiceDaoImpl();
        analysisPlanBuildingServiceDaoImpl = new AnalysisPlanBuildingServiceDaoImpl();
        planFulfillmentResultServiceDaoImpl = new PlanFulfillmentResultServiceDaoImpl();
        analysisPlanEachBuildingServiceDaoImpl = new AnalysisPlanEachBuildingServiceDaoImpl();
    }

    @Override
    public AnalysisPlanResult query(AnalysisPlanParameter parameter) throws Exception {

        //プランごとのプラン情報を取得
        List<AnalysisPlanEachPlanResultData> eachPlanList = getPlanEachPlanResultSetList(parameter);

        //建物ごとのプラン情報を取得
        List<AnalysisPlanEachBuildingResultData> eachBuildingList = getPlanEachBuildingResultSetList(parameter);

        return new AnalysisPlanResult(eachPlanList, eachBuildingList);
    }

    /**
     * プランごとのプラン情報を取得
     *
     * @param corpId 企業ID（必須）
     * @param buildingId 建物ID(任意）
     * @param buildingFilter 建物・テナント絞込み(任意）
     * @param parentGroupId 親グループID（任意）
     * @param childGroupId 子グループID（任意）
     * @param startDate 開始日時（必須）
     * @param endDate 終了日時（必須）
     * @return
     */
    private List<AnalysisPlanEachPlanResultData> getPlanEachPlanResultSetList(AnalysisPlanParameter parameter) {
        Map<String, List<Object>> paramMap = new HashMap<>();

        paramMap.put(ApiParamKeyConstants.CORP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.CORP_ID).add(parameter.getOperationCorpId());

        paramMap.put(ApiParamKeyConstants.BUILDING_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.BUILDING_ID).add(parameter.getBuildingId());

        paramMap.put(ApiParamKeyConstants.BUILDING_FILTER, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.BUILDING_FILTER).add(parameter.getBuildingNarrowing());

        paramMap.put(ApiParamKeyConstants.PARENT_GROUP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PARENT_GROUP_ID).add(parameter.getParentGroupId());

        paramMap.put(ApiParamKeyConstants.CHILD_GROUP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.CHILD_GROUP_ID).add(parameter.getChildGroupId());

        paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PLAN_START_DATE).add(parameter.getPlanStartDate());

        paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PLAN_END_DATE).add(parameter.getPlanEndDate());

        List<AnalysisPlanEachPlanResultData> planEachPlanResultList = getResultList(analysisPlanEachPlanServiceDaoImpl,
                paramMap);

        if (planEachPlanResultList == null || planEachPlanResultList.isEmpty()) {
            return planEachPlanResultList;
        }

        for (AnalysisPlanEachPlanResultData planResultSet : planEachPlanResultList) {
            long planId = planResultSet.getPlanFulfillmentId();
            paramMap.put(ApiParamKeyConstants.BUILDING_ID, new ArrayList<>());
            paramMap.get(ApiParamKeyConstants.BUILDING_ID).add(parameter.getBuildingId());

            paramMap.put(ApiParamKeyConstants.BUILDING_FILTER, new ArrayList<>());
            paramMap.get(ApiParamKeyConstants.BUILDING_FILTER).add(parameter.getBuildingNarrowing());

            paramMap.put(ApiParamKeyConstants.TOTAL_START_DATE, new ArrayList<>());
            paramMap.get(ApiParamKeyConstants.TOTAL_START_DATE).add(parameter.getPlanStartDate());

            paramMap.put(ApiParamKeyConstants.TOTAL_END_DATE, new ArrayList<>());
            paramMap.get(ApiParamKeyConstants.TOTAL_END_DATE).add(parameter.getPlanEndDate());

            paramMap.put(ApiParamKeyConstants.PLAN_ID, new ArrayList<>());
            paramMap.get(ApiParamKeyConstants.PLAN_ID).add(planId);
            // プランの建物検索
            List<AnalysisPlanBuildingResultData> planBuildingResultList = getResultList(
                    analysisPlanBuildingServiceDaoImpl,
                    paramMap);
            // フィルター処理
            planBuildingResultList = buildingDataFilterDao.applyDataFilter(planBuildingResultList,
                    new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

            if (planBuildingResultList == null || planBuildingResultList.isEmpty()) {
                continue;
            }
            for (AnalysisPlanBuildingResultData buildingResultSet : planBuildingResultList) {
                long dbBuildingId = buildingResultSet.getBuildingId();
                paramMap.put(ApiParamKeyConstants.BUILDING_ID, new ArrayList<>());
                paramMap.get(ApiParamKeyConstants.BUILDING_ID).add(dbBuildingId);

                paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, new ArrayList<>());
                paramMap.get(ApiParamKeyConstants.PLAN_START_DATE).add(DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD));

                paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, new ArrayList<>());
                paramMap.get(ApiParamKeyConstants.PLAN_END_DATE).add(DateUtility.changeDateFormat(parameter.getPlanEndDate(), DateUtility.DATE_FORMAT_YYYYMMDD));

                // 実績値の検索
                List<PlanFulfillmentResultDetailResultData> planResultResultList = getResultList(
                        planFulfillmentResultServiceDaoImpl, paramMap);
                if (planResultResultList == null || planResultResultList.isEmpty()) {
                    continue;
                }
                buildingResultSet.setPlanFulfillmentResultList(planResultResultList);
            }
            planResultSet.setPlanBuildingList(planBuildingResultList);
        }

        return planEachPlanResultList;

    }

    /**
     * 建物ごとのプランを取得
     *
     * @param corpId
     * @param buildingIdString
     * @param buildingFilter
     * @param parentGroupId
     * @param childGroupId
     * @param startDate
     * @param startDateString
     * @param endDate
     * @param endDateString
     * @return
     */
    private List<AnalysisPlanEachBuildingResultData> getPlanEachBuildingResultSetList(AnalysisPlanParameter parameter) {
        Map<String, List<Object>> paramMap = new HashMap<>();
        paramMap.put(ApiParamKeyConstants.CORP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.CORP_ID).add(parameter.getOperationCorpId());

        paramMap.put(ApiParamKeyConstants.BUILDING_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.BUILDING_ID).add(parameter.getBuildingId());

        paramMap.put(ApiParamKeyConstants.BUILDING_FILTER, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.BUILDING_FILTER).add(parameter.getBuildingNarrowing());

        paramMap.put(ApiParamKeyConstants.PARENT_GROUP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PARENT_GROUP_ID).add(parameter.getParentGroupId());

        paramMap.put(ApiParamKeyConstants.CHILD_GROUP_ID, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.CHILD_GROUP_ID).add(parameter.getChildGroupId());

        paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PLAN_START_DATE).add(parameter.getPlanStartDate());

        paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, new ArrayList<>());
        paramMap.get(ApiParamKeyConstants.PLAN_END_DATE).add(parameter.getPlanEndDate());

        List<AnalysisPlanEachBuildingResultData> buildingResultSetList = getResultList(
                analysisPlanEachBuildingServiceDaoImpl, paramMap);
        // フィルター処理
        buildingResultSetList = buildingDataFilterDao.applyDataFilter(buildingResultSetList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingResultSetList == null || buildingResultSetList.isEmpty()) {
            return buildingResultSetList;
        }

        for (AnalysisPlanEachBuildingResultData buildingResultSet : buildingResultSetList) {
            Long buildingId = buildingResultSet.getBuildingId();

            parameter.setBuildingId(buildingId);
            List<AnalysisPlanEachPlanResultData> planEachPlanList = getPlanEachPlanResultSetList(parameter);

            buildingResultSet.setPlanEachPlanList(planEachPlanList);
        }
        return buildingResultSetList;
    }

}
