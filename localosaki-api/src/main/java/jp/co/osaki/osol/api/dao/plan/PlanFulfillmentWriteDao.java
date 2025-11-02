/*
P * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.plan;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.google.gson.Gson;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentWriteParameter;
import jp.co.osaki.osol.api.request.plan.PlanFulfillmentWriteRequest;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentWriteResult;
import jp.co.osaki.osol.api.result.plan.TPlanFulfillmentInfoContentsJson;
import jp.co.osaki.osol.api.resultdata.plan.BuildingTenantPlanResult;
import jp.co.osaki.osol.api.resultdata.plan.PlanBuildingTenantSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.plan.PlanSearchApiDetailResultData;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingPlanFulfillmentServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TPlanFulfillmentInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.BuildingIdServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfoPK;

/**
 * 計画履行情報入力 Daoクラス
 * @author n-takada
 */
@Stateless
public class PlanFulfillmentWriteDao extends OsolApiDao<PlanFulfillmentWriteParameter> {

//    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;
//    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final TPlanFulfillmentInfoServiceDaoImpl planFulfillmentWriteImpl;
    private final TBuildingPlanFulfillmentServiceDaoImpl tBuildingPlanFulfillmentServiceDaoImpl;
//    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
//    private final PlanSearchServiceDaoImpl planSearchServiceDaoImpl;
//    private final BuildingTenantPlanSearchDaoImpl buildingTenantPlanSearchImpl;
    private final BuildingIdServiceDaoImpl buildingIdServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public PlanFulfillmentWriteDao() {
//        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
//        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        planFulfillmentWriteImpl = new TPlanFulfillmentInfoServiceDaoImpl();
        tBuildingPlanFulfillmentServiceDaoImpl = new TBuildingPlanFulfillmentServiceDaoImpl();
//        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
//        planSearchServiceDaoImpl = new PlanSearchServiceDaoImpl();
//        buildingTenantPlanSearchImpl = new BuildingTenantPlanSearchDaoImpl();
        buildingIdServiceDaoImpl = new BuildingIdServiceDaoImpl();
    }

    @Override
    public PlanFulfillmentWriteResult query(PlanFulfillmentWriteParameter parameter) throws Exception {

        PlanFulfillmentWriteResult resultSet = new PlanFulfillmentWriteResult();
        Timestamp timestamp = getServerDateTime();
        MPerson mPerson = this.getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        if (parameter.getResultSet().getPlanSearchApiDetailResult().getPlanFulfillmentId() == null) {
            Long planId = persist(parameter.getResultSet().getPlanSearchApiDetailResult(), timestamp, mPerson,
                    parameter);
            PlanSearchApiDetailResultData resultData = new PlanSearchApiDetailResultData();
            resultData.setPlanFulfillmentId(planId);
            resultSet.setPlanSearchApiDetailResult(resultData);
            return resultSet;
        } else {
            merge(parameter.getResultSet().getPlanSearchApiDetailResult(), timestamp, mPerson, parameter);
            PlanSearchApiDetailResultData resultData = new PlanSearchApiDetailResultData();
            resultData.setPlanFulfillmentId(
                    parameter.getResultSet().getPlanSearchApiDetailResult().getPlanFulfillmentId());
            resultSet.setPlanSearchApiDetailResult(resultData);
            return resultSet;
        }
    }

    /**
     * 企業情報の排他チェックを行う
     * @param result
     * @return
     */
//    private MCorp corpExclusiveCheck(PlanFulfillmentWriteRequest result) throws Exception {
//        MCorp corpParam = new MCorp();
//        corpParam.setCorpId(result.getCorpId());
//        MCorp exCorp = find(mCorpApiServiceDaoImpl, corpParam);
//        return exCorp;
//        //        if (exCorp == null || !exCorp.getVersion().equals(result.getCorpVersion())) {
//        //            //排他制御のデータがない場合または前に保持をしていたVersionと異なる場合、排他エラー
//        //            throw new OptimisticLockException();
//        //        } else {
//        //            return exCorp;
//        //        }
//    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
//    private TBuilding buildingExclusiveCheck(BuildingTenantPlanResult result) throws Exception {
//        TBuilding buildingParam = new TBuilding();
//        TBuildingPK pkBuildingParam = new TBuildingPK();
//        pkBuildingParam.setCorpId(result.getCorpId());
//        pkBuildingParam.setBuildingId(result.getBuildingId());
//        buildingParam.setId(pkBuildingParam);
//        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
//        if (exBuilding == null || !exBuilding.getVersion().equals(result.getBuildingVersion())) {
//            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
//            throw new OptimisticLockException();
//        } else {
//            return exBuilding;
//        }
//    }

    /**
     * 更新後のデータを取得する
     * @param parameter
     * @param planFulFillmentId
     * @return
     */
//    private PlanFulfillmentWriteResult getNewPlanInfo(PlanFulfillmentWriteParameter parameter, Long planFulFillmentId)
//            throws Exception {
//        PlanFulfillmentWriteResult newResult = new PlanFulfillmentWriteResult();
//
//        //建物設定で、計画を更新しているので、排他情報を見る必要がある
//        //排他企業情報を取得する
//        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
//        exCorpParam.setCorpId(parameter.getOperationCorpId());
//        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);
//
//        if (exCorpList == null || exCorpList.size() != 1) {
//            return new PlanFulfillmentWriteResult();
//        }
//
//        Map<String, List<Object>> paramMap = createServiceDaoParameter(
//                parameter.getOperationCorpId(), null, null,
//                null, null);
//
//        List<PlanSearchApiDetailResultData> resultSetList = getResultList(planSearchServiceDaoImpl, paramMap);
//
//        for (PlanSearchApiDetailResultData newResultSet : resultSetList) {
//            if (!newResultSet.getPlanFulfillmentId().equals(planFulFillmentId)) {
//                continue;
//            }
//            List<BuildingTenantPlanResult> list = new ArrayList<>();
//            if (ApiCodeValueConstants.PLAN_FULFILLMENT_TARGET.SELECT.getVal().equals(
//                    newResultSet.getPlanFulfillmentTarget())) {
//                BuildingTenantPlanResult param = new BuildingTenantPlanResult();
//                param.setCorpId(newResultSet.getCorpId());
//                param.setPlanFulfillmentId(newResultSet.getPlanFulfillmentId());
//                list = getResultList(buildingTenantPlanSearchImpl, param);
//                // フィルター処理
//                list = buildingDataFilterDao.applyDataFilter(list,
//                        new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));
//            }
//            newResultSet.setBuildingTenantList(list);
//            newResultSet.setCorpId(exCorpList.get(0).getCorpId());
//            newResultSet.setCorpVersion(exCorpList.get(0).getVersion());
//        }
//
//        newResult.setCorpId(exCorpList.get(0).getCorpId());
//        newResult.setCorpVersion(exCorpList.get(0).getVersion());
//        newResult.setPlanSearchApiDetailResult(resultSetList.get(0));
//        return newResult;
//
//    }

    /**
    *
    * @param corpId
    * @param planName
    * @param planStartYm
    * @param planEndYm
    * @param planTargetCdList
    * @return
    */
//    private Map<String, List<Object>> createServiceDaoParameter(String corpId, String planName,
//            String planStartYm, String planEndYm, String planTargetCdList) throws Exception {
//        Map<String, List<Object>> paramMap = new HashMap<>();
//
//        List<Object> corpIdList = new ArrayList<>();
//        corpIdList.add(corpId);
//        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);
//
//        List<Object> planNameList = new ArrayList<>();
//        planNameList.add(planName);
//        paramMap.put(ApiParamKeyConstants.PLAN_FULFILLMENT_NAME, planNameList);
//
//        List<Object> planStartYmList = new ArrayList<>();
//        planStartYmList.add(planStartYm);
//        paramMap.put(ApiParamKeyConstants.PLAN_START_YM, planStartYmList);
//
//        List<Object> planEndYmList = new ArrayList<>();
//        planEndYmList.add(planEndYm);
//        paramMap.put(ApiParamKeyConstants.PLAN_END_YM, planEndYmList);
//
//        if (!CheckUtility.isNullOrEmpty(planTargetCdList)) {
//            List<Object> planTargetCdListList = new ArrayList<>();
//            StringTokenizer st = new StringTokenizer(planTargetCdList, ",");
//            while (st.hasMoreTokens()) {
//                planTargetCdListList.add(st.nextToken());
//            }
//            paramMap.put(ApiParamKeyConstants.PLAN_FULFILLMENT_TARGET_LIST, planTargetCdListList);
//        }
//        return paramMap;
//    }

    /**
     * パラメタから建物IDリスト取得
     *
     * @param parameter
     * @return
     */
    private List<Long> getTargetBuildingIdList(PlanFulfillmentWriteParameter parameter) {
        List<Long> buildingIdList = new ArrayList<>();
        Map<String, List<Object>> paramMap = new HashMap<>();
        List<Object> paramList = new ArrayList<>();
        PlanFulfillmentWriteRequest resultSet = parameter.getResultSet();
        paramList.add(parameter.getOperationCorpId());
        paramMap.put(ApiParamKeyConstants.CORP_ID, paramList);
        switch (resultSet.getPlanSearchApiDetailResult().getPlanFulfillmentTarget()) {
        case "1": // 全建物、テナント
            paramMap.put("tenantFlg", new ArrayList<>());
            paramMap.put("buildingFlg", new ArrayList<>());
            buildingIdList = getBuildingIdList(paramMap);
            break;
        case "2": // 全建物
            paramMap.put("tenantFlg", null);
            paramMap.put("buildingFlg", new ArrayList<>());
            buildingIdList = getBuildingIdList(paramMap);
            break;
        case "3": // 全テナント
            paramMap.put("tenantFlg", new ArrayList<>());
            paramMap.put("buildingFlg", null);
            buildingIdList = getBuildingIdList(paramMap);
            break;
        case "4": // 指定建物
            List<BuildingTenantPlanResult> buildingTenantList = resultSet.getPlanSearchApiDetailResult()
                    .getBuildingTenantList();
            for (BuildingTenantPlanResult item : buildingTenantList) {
                buildingIdList.add(item.getBuildingId());
            }
            break;
        default:
            break;
        }
        return buildingIdList;
    }

    /**
     * 建物・テナントの建物IDを取得する
     *
     * @return
     */
    private List<Long> getBuildingIdList(Map<String, List<Object>> paramMap) {
        return getResultList(buildingIdServiceDaoImpl, paramMap);
    }

    /**
      * 新規登録時のメソッド。
      *
      * @param resultSet
      * @param timestamp
      * @return
      */
    private long persist(PlanSearchApiDetailResultData resultSet, Timestamp timestamp, MPerson person,
            PlanFulfillmentWriteParameter parameter) {
        long planId = createId(OsolConstants.ID_SEQUENCE_NAME.PLAN_FULFILLMENT_ID.getVal());
        resultSet.setPlanFulfillmentId(planId);
        // 計画履行情報へ書き込み
        persistTPlanFulfillmentInfo(resultSet, timestamp, person);
        // 対象建物ID取得
        List<Long> buildingIdList = getTargetBuildingIdList(parameter);
        // 建物計画履行へ書き込み
        persistTBuildingPlanFulfillment(resultSet.getPlanBuildingResultSet(), buildingIdList, resultSet.getCorpId(),
                resultSet.getPlanFulfillmentId(), person, timestamp);

        return planId;
    }

    /**
     * レコードをマージする。
     *
     * @param resultSet
     * @param timestamp
     */
    private void merge(PlanSearchApiDetailResultData resultSet, Timestamp timestamp, MPerson person,
            PlanFulfillmentWriteParameter parameter) {

        TPlanFulfillmentInfo searchEntity = new TPlanFulfillmentInfo();
        TPlanFulfillmentInfoPK searchId = new TPlanFulfillmentInfoPK();
        searchId.setCorpId(resultSet.getCorpId());
        searchId.setPlanFulfillmentId(resultSet.getPlanFulfillmentId());
        searchEntity.setId(searchId);

        TPlanFulfillmentInfo entity = find(planFulfillmentWriteImpl, searchEntity);
        if (entity == null) {
            return;
        }
        entity.setPlanFulfillmentName(resultSet.getPlanFulfillmentName());
        entity.setPlanFulfillmentTarget(resultSet.getPlanFulfillmentTarget());
        entity.setPlanFulfillmentDateType(resultSet.getPlanFulfillmentDateType());
        entity.setPlanFulfillmentStartDate(resultSet.getPlanFulfillmentStartDate());
        entity.setPlanFulfillmentEndDate(resultSet.getPlanFulfillmentEndDate());
        entity.setSpecifyMonth1(resultSet.getSpecifyMonth1());
        entity.setSpecifyMonth2(resultSet.getSpecifyMonth2());
        entity.setSpecifyMonth3(resultSet.getSpecifyMonth3());
        entity.setSpecifyMonth4(resultSet.getSpecifyMonth4());
        entity.setSpecifyMonth5(resultSet.getSpecifyMonth5());
        entity.setSpecifyMonth6(resultSet.getSpecifyMonth6());
        entity.setSpecifyMonth7(resultSet.getSpecifyMonth7());
        entity.setSpecifyMonth8(resultSet.getSpecifyMonth8());
        entity.setSpecifyMonth9(resultSet.getSpecifyMonth9());
        entity.setSpecifyMonth10(resultSet.getSpecifyMonth10());
        entity.setSpecifyMonth11(resultSet.getSpecifyMonth11());
        entity.setSpecifyMonth12(resultSet.getSpecifyMonth12());
        entity.setSpecifySunday(checkSupecifyWeek(resultSet.getSpecifySunday()));
        entity.setSpecifyMonday(checkSupecifyWeek(resultSet.getSpecifyMonday()));
        entity.setSpecifyTuesday(checkSupecifyWeek(resultSet.getSpecifyTuesday()));
        entity.setSpecifyWednesday(checkSupecifyWeek(resultSet.getSpecifyWednesday()));
        entity.setSpecifyThursday(checkSupecifyWeek(resultSet.getSpecifyThursday()));
        entity.setSpecifyFriday(checkSupecifyWeek(resultSet.getSpecifyFriday()));
        entity.setSpecifySaturday(checkSupecifyWeek(resultSet.getSpecifySaturday()));
        entity.setPlanFulfillmentContents(resultSet.getPlanFulfillmentContents());

        entity.setDelFlg(0);
        entity.setUpdateUserId(person.getUserId());
        entity.setUpdateDate(timestamp);

        merge(planFulfillmentWriteImpl, entity);
        mergePlanBuilding(resultSet, person, timestamp, parameter);
    }

    /**
    *
    * @param resultSet
    * @param mPerson
    * @param timestamp
    */
    private void mergePlanBuilding(PlanSearchApiDetailResultData resultSet, MPerson person, Timestamp timestamp,
            PlanFulfillmentWriteParameter parameter) {
        List<Long> updateBuildingIdList = getTargetBuildingIdList(parameter);
        List<TBuildingPlanFulfillment> orgBuildingPlanList = getTBuildingPlanList(resultSet.getCorpId(),
                resultSet.getPlanFulfillmentId());

        for (TBuildingPlanFulfillment orgBuildingPlan : orgBuildingPlanList) {
            long buildingId = orgBuildingPlan.getId().getBuildingId();
            if (updateBuildingIdList.contains(buildingId)) {
                // 両方に有る場合、削除フラグ解除
                mergeTBuildingPlanFulfillment(parameter.getResultSet().getPlanSearchApiDetailResult(), buildingId,
                        person, timestamp, 0);
                // 実績も削除フラグOFF
            } else {
                // updateListに存在しない場合削除フラグ
                mergeTBuildingPlanFulfillment(resultSet, buildingId, person, timestamp, 1);
                // 実績も削除フラグON
            }
            updateBuildingIdList.remove(orgBuildingPlan.getId().getBuildingId());
        }

        PlanBuildingTenantSearchDetailResultData planBuildingResultSet = resultSet.getPlanBuildingResultSet();
        persistTBuildingPlanFulfillment(planBuildingResultSet, updateBuildingIdList,
                resultSet.getCorpId(), resultSet.getPlanFulfillmentId(), person, timestamp);
    }

    /**
     * 現存する建物プランを全部取得する
     *
     * @param corpId
     * @return
     */
    private List<TBuildingPlanFulfillment> getTBuildingPlanList(String corpId, long planId) {
        TBuildingPlanFulfillment tbpfEntity = new TBuildingPlanFulfillment();
        TBuildingPlanFulfillmentPK tbpfEntityPK = new TBuildingPlanFulfillmentPK();
        tbpfEntityPK.setCorpId(corpId);
        tbpfEntityPK.setPlanFulfillmentId(planId);
        tbpfEntity.setId(tbpfEntityPK);
        tbpfEntity.setDelFlg(null);
        return getResultList(tBuildingPlanFulfillmentServiceDaoImpl, tbpfEntity);
    }

    /**
     * 建物計画履行へ書き込み
     *
     * @param resultSet
     * @param timestamp
     * @param person
     */
    private void mergeTBuildingPlanFulfillment(PlanSearchApiDetailResultData planSearchResultSet, long buildingId,
            MPerson person, Timestamp timestamp, int delFlg) {
        PlanBuildingTenantSearchDetailResultData planBuildingResultSet = planSearchResultSet.getPlanBuildingResultSet();

        TBuildingPlanFulfillment searchEntity = new TBuildingPlanFulfillment();
        TBuildingPlanFulfillmentPK searchEntityPK = new TBuildingPlanFulfillmentPK();
        searchEntityPK.setCorpId(planSearchResultSet.getCorpId());
        searchEntityPK.setBuildingId(buildingId);
        searchEntityPK.setPlanFulfillmentId(planSearchResultSet.getPlanFulfillmentId());
        searchEntity.setId(searchEntityPK);
        TBuildingPlanFulfillment tbpfEntity = find(tBuildingPlanFulfillmentServiceDaoImpl, searchEntity);
        tbpfEntity.setDelFlg(delFlg);
        tbpfEntity.setPrevMonthInputCount(planBuildingResultSet.getSumPrevMonthInputCount());
        tbpfEntity.setPrevMonthNeedCount(planBuildingResultSet.getSumPrevMonthNeedCount());
        tbpfEntity.setPrevMonthAchieveCount(planBuildingResultSet.getSumPrevMonthAchieveCount());
        tbpfEntity.setPrevMonthTargetInputCount(planBuildingResultSet.getSumPrevMonthTargetInputCount());
        tbpfEntity.setThisMonthInputCount(planBuildingResultSet.getSumThisMonthInputCount());
        tbpfEntity.setThisMonthNeedCount(planBuildingResultSet.getSumThisMonthNeedCount());
        tbpfEntity.setThisMonthAchieveCount(planBuildingResultSet.getSumThisMonthAchieveCount());
        tbpfEntity.setThisMonthTargetInputCount(planBuildingResultSet.getSumThisMonthTargetInputCount());
        tbpfEntity.setUpdateDate(timestamp);
        tbpfEntity.setUpdateUserId(person.getUserId());
        merge(tBuildingPlanFulfillmentServiceDaoImpl, tbpfEntity);
    }

    /**
     * 建物計画履行へ書き込み
     *
     * @param resultSet
     * @param timestamp
     * @param person
     */
    private void persistTBuildingPlanFulfillment(PlanBuildingTenantSearchDetailResultData planBuildingResultSet,
            List<Long> buildingIdList, String corpId, Long planId, MPerson person, Timestamp timestamp) {
        for (Long buildingId : buildingIdList) {
            TBuildingPlanFulfillment tbpfEntity = persistTBuildingPlanFulfillment(planBuildingResultSet, 0, buildingId,
                    corpId, planId, person, timestamp);
            persist(tBuildingPlanFulfillmentServiceDaoImpl, tbpfEntity);
            tbpfEntity.getId().getPlanFulfillmentId();
        }
    }

    /**
     * 建物計画履行へ書き込み
     *
     * @param resultSet
     * @param timestamp
     * @param person
     */
    private TBuildingPlanFulfillment persistTBuildingPlanFulfillment(
            PlanBuildingTenantSearchDetailResultData planBuildingResultSet,
            int delFlg, Long buildingId, String corpId, Long planId, MPerson person, Timestamp timestamp) {
        TBuildingPlanFulfillment tbpfEntity = new TBuildingPlanFulfillment();
        TBuildingPlanFulfillmentPK tbpfEntityPK = new TBuildingPlanFulfillmentPK();
        tbpfEntityPK.setCorpId(corpId);
        tbpfEntityPK.setBuildingId(buildingId);
        tbpfEntityPK.setPlanFulfillmentId(planId);
        tbpfEntity.setId(tbpfEntityPK);
        tbpfEntity.setPrevMonthInputCount(planBuildingResultSet.getSumPrevMonthInputCount());
        tbpfEntity.setPrevMonthNeedCount(planBuildingResultSet.getSumPrevMonthNeedCount());
        tbpfEntity.setPrevMonthAchieveCount(planBuildingResultSet.getSumPrevMonthAchieveCount());
        tbpfEntity.setPrevMonthTargetInputCount(planBuildingResultSet.getSumPrevMonthTargetInputCount());
        tbpfEntity.setThisMonthInputCount(planBuildingResultSet.getSumThisMonthInputCount());
        tbpfEntity.setThisMonthNeedCount(planBuildingResultSet.getSumThisMonthNeedCount());
        tbpfEntity.setThisMonthAchieveCount(planBuildingResultSet.getSumThisMonthAchieveCount());
        tbpfEntity.setThisMonthTargetInputCount(planBuildingResultSet.getSumThisMonthTargetInputCount());
        tbpfEntity.setDelFlg(delFlg);
        tbpfEntity.setVersion(0);
        tbpfEntity.setCreateUserId(person.getUserId());
        tbpfEntity.setCreateDate(timestamp);
        tbpfEntity.setUpdateUserId(person.getUserId());
        tbpfEntity.setUpdateDate(timestamp);
        return tbpfEntity;
    }

    /**
     * TPlanFulfillmentInfoを新規登録する。
     *
     * @param resultSet
     * @param timestamp
     */
    private void persistTPlanFulfillmentInfo(PlanSearchApiDetailResultData resultSet, Timestamp timestamp,
            MPerson person) {
        TPlanFulfillmentInfo entity = new TPlanFulfillmentInfo();
        TPlanFulfillmentInfoPK id = new TPlanFulfillmentInfoPK();
        id.setCorpId(resultSet.getCorpId());
        id.setPlanFulfillmentId(resultSet.getPlanFulfillmentId());
        entity.setId(id);

        entity.setPlanFulfillmentName(resultSet.getPlanFulfillmentName());
        entity.setPlanFulfillmentTarget(resultSet.getPlanFulfillmentTarget());
        entity.setPlanFulfillmentDateType(resultSet.getPlanFulfillmentDateType());
        entity.setPlanFulfillmentStartDate(resultSet.getPlanFulfillmentStartDate());
        entity.setPlanFulfillmentEndDate(resultSet.getPlanFulfillmentEndDate());
        entity.setSpecifyMonth1(resultSet.getSpecifyMonth1());
        entity.setSpecifyMonth2(resultSet.getSpecifyMonth2());
        entity.setSpecifyMonth3(resultSet.getSpecifyMonth3());
        entity.setSpecifyMonth4(resultSet.getSpecifyMonth4());
        entity.setSpecifyMonth5(resultSet.getSpecifyMonth5());
        entity.setSpecifyMonth6(resultSet.getSpecifyMonth6());
        entity.setSpecifyMonth7(resultSet.getSpecifyMonth7());
        entity.setSpecifyMonth8(resultSet.getSpecifyMonth8());
        entity.setSpecifyMonth9(resultSet.getSpecifyMonth9());
        entity.setSpecifyMonth10(resultSet.getSpecifyMonth10());
        entity.setSpecifyMonth11(resultSet.getSpecifyMonth11());
        entity.setSpecifyMonth12(resultSet.getSpecifyMonth12());
        entity.setSpecifySunday(checkSupecifyWeek(resultSet.getSpecifySunday()));
        entity.setSpecifyMonday(checkSupecifyWeek(resultSet.getSpecifyMonday()));
        entity.setSpecifyTuesday(checkSupecifyWeek(resultSet.getSpecifyTuesday()));
        entity.setSpecifyWednesday(checkSupecifyWeek(resultSet.getSpecifyWednesday()));
        entity.setSpecifyThursday(checkSupecifyWeek(resultSet.getSpecifyThursday()));
        entity.setSpecifyFriday(checkSupecifyWeek(resultSet.getSpecifyFriday()));
        entity.setSpecifySaturday(checkSupecifyWeek(resultSet.getSpecifySaturday()));

        Gson gson = new Gson();
        TPlanFulfillmentInfoContentsJson contents = gson.fromJson(resultSet.getPlanFulfillmentContents(),
                TPlanFulfillmentInfoContentsJson.class);
        contents.setPlanId(id.getPlanFulfillmentId());
        String json = gson.toJson(contents);
        entity.setPlanFulfillmentContents(json);

        entity.setDelFlg(0);
        entity.setVersion(0);
        entity.setCreateUserId(person.getUserId());
        entity.setCreateDate(timestamp);
        entity.setUpdateUserId(person.getUserId());
        entity.setUpdateDate(timestamp);

        persist(planFulfillmentWriteImpl, entity);
    }

    /**
     * 曜日のnullチェック
     * @param target
     * @return
     */
    private Integer checkSupecifyWeek(Integer target) {

        if (target != null) {
            return target;
        }

        return 0;
    }
}
