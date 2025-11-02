/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.generic;

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
import jp.co.osaki.osol.api.parameter.generic.CommonSearchBuildingParameter;
import jp.co.osaki.osol.api.result.generic.CommonSearchBuildingResult;
import jp.co.osaki.osol.api.resultdata.generic.CommonSearchBuildingDetailResultData;
import jp.co.osaki.osol.api.servicedao.generic.CommonSearchBuildingServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 汎用建物検索 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class CommonSearchBuildingDao extends OsolApiDao<CommonSearchBuildingParameter> {

    private final CommonSearchBuildingServiceDaoImpl commonSearchBuildingServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public CommonSearchBuildingDao() {
        commonSearchBuildingServiceDaoImpl = new CommonSearchBuildingServiceDaoImpl();
    }

    @Override
    public CommonSearchBuildingResult query(CommonSearchBuildingParameter parameter) throws Exception {
        CommonSearchBuildingResult result = new CommonSearchBuildingResult();

        Map<String, List<Object>> paramMap = new HashMap<>();
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getOperationCorpId());
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);

        List<Object> personIdList = new ArrayList<>();
        personIdList.add(parameter.getLoginPersonId());
        paramMap.put(ApiParamKeyConstants.LOGIN_PERSON_ID, personIdList);

        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(parameter.getLoginCorpId());
        paramMap.put(ApiParamKeyConstants.LOGIN_USER_CORP_ID, loginCorpIdList);

        List<Object> buildingNoList = new ArrayList<>();
        buildingNoList.add(parameter.getBuildingNo());
        paramMap.put(ApiParamKeyConstants.BUILDING_NO, buildingNoList);

        List<Object> prefectureCdList = new ArrayList<>();
        if(parameter.getCommonSearchParameter() != null && !parameter.getCommonSearchParameter().getPrefectureCdList().isEmpty()) {
            for(String target:parameter.getCommonSearchParameter().getPrefectureCdList()) {
                prefectureCdList.add(target);
            }
        }
        paramMap.put(ApiParamKeyConstants.PREFECTURE_CD, prefectureCdList);

        List<Object> buildingTenantCdList = new ArrayList<>();
        buildingTenantCdList.add(parameter.getBuildingTenantCd());
        paramMap.put(ApiParamKeyConstants.BUILDING_TENANT_CD, buildingTenantCdList);

        List<Object> nyukyoTypeCdList = new ArrayList<>();
        nyukyoTypeCdList.add(parameter.getNyukyoTypeCd());
        paramMap.put(ApiParamKeyConstants.NYUKYO_TYPE_CD, nyukyoTypeCdList);

        List<Object> buildingStateCdList = new ArrayList<>();
        buildingStateCdList.add(parameter.getBuildingStatus());
        paramMap.put(ApiParamKeyConstants.BUILDING_STATUS, buildingStateCdList);

        List<Object> osakiFlgList = new ArrayList<>();
        osakiFlgList.add(parameter.getOsakiFlg());
        paramMap.put(ApiParamKeyConstants.OSAKI_FLG, osakiFlgList);

        List<Object> nowDateList = new ArrayList<>();
        nowDateList.add(getServerDateTime());
        paramMap.put(ApiParamKeyConstants.DAY, nowDateList);

        if (!CheckUtility.isNullOrEmpty(parameter.getBuildingCorpId())) {
            List<Object> buildingCorpIdList = new ArrayList<>();
            buildingCorpIdList.add(parameter.getBuildingCorpId());
            paramMap.put(ApiParamKeyConstants.BUILDING_CORP_ID, buildingCorpIdList);
        }
        if (parameter.getBuildingId() != null) {
            List<Object> buildingIdList = new ArrayList<>();
            buildingIdList.add(parameter.getBuildingId());
            paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
        }
        if (!CheckUtility.isNullOrEmpty(parameter.getDivisionCorpId())) {
            List<Object> divisionCorpIdList = new ArrayList<>();
            divisionCorpIdList.add(parameter.getDivisionCorpId());
            paramMap.put(ApiParamKeyConstants.DIVISION_CORP_ID, divisionCorpIdList);
        }
        if (!CheckUtility.isNullOrEmpty(parameter.getDivisionBuildingId())) {
            List<Object> divisionBuildingIdList = new ArrayList<>();
            divisionBuildingIdList.add(parameter.getDivisionBuildingId());
            paramMap.put(ApiParamKeyConstants.DIVISION_BUILDING_ID, divisionBuildingIdList);
        }
        if (!CheckUtility.isNullOrEmpty(parameter.getInputBuildingBorrowByTenant())) {
            List<Object> inputBuildingBorrowByTenantList = new ArrayList<>();
            inputBuildingBorrowByTenantList.add(parameter.getInputBuildingBorrowByTenant());
            paramMap.put(ApiParamKeyConstants.INPUT_BUILDING_BORROW_BY_TENANT_FLG, inputBuildingBorrowByTenantList);
        }

        // 親グループ
        if (parameter.getParentGroupId() != null) {
            List<Object> parentGroupIdList = new ArrayList<>();
            parentGroupIdList.add(parameter.getParentGroupId());
            paramMap.put(ApiParamKeyConstants.PARENT_GROUP_ID, parentGroupIdList);
        }
        // 子グループ
        if (parameter.getChildGroupId() != null) {
            List<Object> childGroupIdList = new ArrayList<>();
            childGroupIdList.add(parameter.getChildGroupId());
            paramMap.put(ApiParamKeyConstants.CHILD_GROUP_ID, childGroupIdList);
        }

        List<CommonSearchBuildingDetailResultData> resultList = getResultList(commonSearchBuildingServiceDaoImpl,
                paramMap);

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setDetailList(resultList);
        return result;
    }

}
