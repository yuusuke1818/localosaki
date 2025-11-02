/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.PlanBuildingTenantSearchParameter;
import jp.co.osaki.osol.api.request.plan.PlanSearchRequest;
import jp.co.osaki.osol.api.result.plan.PlanBuildingTenantSearchResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.plan.PlanBuildingTenantSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.plan.PlanBuildingTenantSearchPersonSearchResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.BuildingPersonSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.PlanBuildingTenantSearchServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 計画履行建物・テナント検索 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class PlanBuildingTenantSearchDao extends OsolApiDao<PlanBuildingTenantSearchParameter> {

    private final PlanBuildingTenantSearchServiceDaoImpl planBuildingTenantSearchServiceDaoImpl;
    private final BuildingPersonSearchServiceDaoImpl buildingPersonSearchServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public PlanBuildingTenantSearchDao() {
        planBuildingTenantSearchServiceDaoImpl = new PlanBuildingTenantSearchServiceDaoImpl();
        buildingPersonSearchServiceDaoImpl = new BuildingPersonSearchServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public PlanBuildingTenantSearchResult query(PlanBuildingTenantSearchParameter parameter) throws Exception {
        PlanBuildingTenantSearchResult result = new PlanBuildingTenantSearchResult();

        Map<String, List<Object>> paramMap = createParameterMap(parameter.getLoginCorpId(),
                parameter.getOperationCorpId(), parameter.getLoginPersonId(), parameter.getPlanSearchRequest(),
                parameter.getNyukyoTypeCd(), parameter.getBuildingStatus(), parameter.getBuildingTenantCd(),
                parameter.getBuildingNo(), parameter.getOsakiFlg(), parameter.getInAlwaysPlanFlg(),
                parameter.getBuildingCorpId(), parameter.getBuildingId(), parameter.getDivisionCorpId(),
                parameter.getDivisionBuildingId(), parameter.getInputBuildingBorrowByTenant(),
                parameter.getParentGroupId(),parameter.getChildGroupId());

        //企業の排他情報の取得
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new PlanBuildingTenantSearchResult();
        }

        // 建物一覧の取得
        List<PlanBuildingTenantSearchDetailResultData> planBuildingList = getResultList(
                planBuildingTenantSearchServiceDaoImpl,
                paramMap);

        // フィルター処理
        planBuildingList = buildingDataFilterDao.applyDataFilter(planBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        for (PlanBuildingTenantSearchDetailResultData resultSet : planBuildingList) {

            Map<String, List<Object>> buildingPersonMap = new HashMap<>();
            List<Object> corpIdList = new ArrayList<>();
            corpIdList.add(resultSet.getCorpId());
            List<Object> buildingIdList = new ArrayList<>();
            buildingIdList.add(resultSet.getBuildingId());

            // 計画履行入力権限
            List<Object> authorityCdList = new ArrayList<>();
            authorityCdList.add("007");

            buildingPersonMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);
            buildingPersonMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
            buildingPersonMap.put("authorityCdList", authorityCdList);

            // 建物担当者情報を取得してセットする
            List<PlanBuildingTenantSearchPersonSearchResultData> buildingPersonList = getBuildingPersonSearchResultSetList(
                    buildingPersonMap);
            if (buildingPersonList == null) {
                buildingPersonList = new ArrayList<>();
            }
            resultSet.setBuildingPersonList(buildingPersonList);
        }

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetailList(planBuildingList);
        return result;
    }

    public List<PlanBuildingTenantSearchPersonSearchResultData> getBuildingPersonSearchResultSetList(
            Map<String, List<Object>> paramMap) {
        return getResultList(buildingPersonSearchServiceDaoImpl, paramMap);
    }

    /**
     *
     * @param loginCorpId
     * @param corpId
     * @param loginPersonId
     * @param planSearchRequest 件コードのListがある
     * @param nyukyoTypeCd
     * @param buildingStatus
     * @param buildingTenantCd
     * @param buildingNo
     * @param osakiFlg
     * @param inAlwaysPlanFlg
     * @param buildingCorpId
     * @param buildingId
     * @param divisionCorpId
     * @param divisionBuildingId
     * @param inputBuildingBorrowByTenant
     * @param parentGroupId
     * @param childGroupId
     * @return
     * @throws Exception
     */
    private Map<String, List<Object>> createParameterMap(String loginCorpId, String corpId, String loginPersonId,
            PlanSearchRequest planSearchRequest, String nyukyoTypeCd, String buildingStatus, String buildingTenantCd,
            String buildingNo, String osakiFlg, String inAlwaysPlanFlg, String buildingCorpId,
            Long buildingId, String divisionCorpId, String divisionBuildingId, String inputBuildingBorrowByTenant,
            Long parentGroupId, Long childGroupId)
            throws Exception {
        Map<String, List<Object>> paramMap = new HashMap<>();

        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(loginCorpId);
        paramMap.put(ApiParamKeyConstants.LOGIN_USER_CORP_ID, loginCorpIdList);

        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(corpId);
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);

        if (!CheckUtility.isNullOrEmpty(loginPersonId)) {
            List<Object> loginPersonIdList = new ArrayList<>();
            loginPersonIdList.add(loginPersonId);
            paramMap.put(ApiParamKeyConstants.LOGIN_PERSON_ID, loginPersonIdList);
        }

        if(planSearchRequest != null && !planSearchRequest.getPrefectureCdList().isEmpty()) {
            List<Object> prefectureCdList = new ArrayList<>();
            for(String target:planSearchRequest.getPrefectureCdList()) {
                prefectureCdList.add(target);
            }
            paramMap.put(ApiParamKeyConstants.PREFECTURE_CD, prefectureCdList);
        }

        if (!CheckUtility.isNullOrEmpty(nyukyoTypeCd)) {
            List<Object> nyukyoTypeCdList = new ArrayList<>();
            nyukyoTypeCdList.add(nyukyoTypeCd);
            paramMap.put(ApiParamKeyConstants.NYUKYO_TYPE_CD, nyukyoTypeCdList);
        }
        if (!CheckUtility.isNullOrEmpty(buildingStatus)) {
            List<Object> buildingStatusList = new ArrayList<>();
            buildingStatusList.add(buildingStatus);
            paramMap.put(ApiParamKeyConstants.BUILDING_STATUS, buildingStatusList);
        }
        if (!CheckUtility.isNullOrEmpty(buildingTenantCd)) {
            List<Object> buildingTenantCdList = new ArrayList<>();
            buildingTenantCdList.add(buildingTenantCd);
            paramMap.put(ApiParamKeyConstants.BUILDING_TENANT_CD, buildingTenantCdList);
        }
        if (!CheckUtility.isNullOrEmpty(buildingNo)) {
            List<Object> buildingNoList = new ArrayList<>();
            buildingNoList.add(buildingNo);
            paramMap.put(ApiParamKeyConstants.BUILDING_NO, buildingNoList);
        }
        if (!CheckUtility.isNullOrEmpty(osakiFlg)) {
            List<Object> osakiFlgList = new ArrayList<>();
            osakiFlgList.add(osakiFlg);
            paramMap.put(ApiParamKeyConstants.OSAKI_FLG, osakiFlgList);
        }
        if (!CheckUtility.isNullOrEmpty(inAlwaysPlanFlg)) {
            List<Object> inAlwaysPlanFlgList = new ArrayList<>();
            inAlwaysPlanFlgList.add(inAlwaysPlanFlg);
            paramMap.put(ApiParamKeyConstants.IN_ALWAYS_PLAN_FLG, inAlwaysPlanFlgList);
        }
        if (!CheckUtility.isNullOrEmpty(buildingCorpId)) {
            List<Object> buildingCorpIdList = new ArrayList<>();
            buildingCorpIdList.add(buildingCorpId);
            paramMap.put(ApiParamKeyConstants.BUILDING_CORP_ID, buildingCorpIdList);
        }
        if (buildingId == null) {
            List<Object> buildingIdList = new ArrayList<>();
            buildingIdList.add(buildingId);
            paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
        }
        if (!CheckUtility.isNullOrEmpty(divisionCorpId)) {
            List<Object> divisionCorpIdList = new ArrayList<>();
            divisionCorpIdList.add(divisionCorpId);
            paramMap.put(ApiParamKeyConstants.DIVISION_CORP_ID, divisionCorpIdList);
        }
        if (!CheckUtility.isNullOrEmpty(divisionBuildingId)) {
            List<Object> divisionBuildingIdList = new ArrayList<>();
            divisionBuildingIdList.add(divisionBuildingId);
            paramMap.put(ApiParamKeyConstants.DIVISION_BUILDING_ID, divisionBuildingIdList);
        }
        if (!CheckUtility.isNullOrEmpty(inputBuildingBorrowByTenant)) {
            List<Object> inputBuildingBorrowByTenantList = new ArrayList<>();
            inputBuildingBorrowByTenantList.add(inputBuildingBorrowByTenant);
            paramMap.put(ApiParamKeyConstants.INPUT_BUILDING_BORROW_BY_TENANT_FLG, inputBuildingBorrowByTenantList);
        }
        // 親グループ
        if (parentGroupId != null) {
            List<Object> parentGroupIdList = new ArrayList<>();
            parentGroupIdList.add(parentGroupId);
            paramMap.put(ApiParamKeyConstants.PARENT_GROUP_ID, parentGroupIdList);
        }
        // 子グループ
        if (childGroupId != null) {
            List<Object> childGroupIdList = new ArrayList<>();
            childGroupIdList.add(childGroupId);
            paramMap.put(ApiParamKeyConstants.CHILD_GROUP_ID, childGroupIdList);
        }
        List<Object> nowDate = new ArrayList<>();
        nowDate.add(getServerDateTime());
        paramMap.put("nowDate", nowDate);
        return paramMap;
    }

}
