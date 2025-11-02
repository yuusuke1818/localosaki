package jp.co.osaki.osol.api.dao.smoperation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.BooleanUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationBuildingSearchParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.result.smoperation.SmOperationBuildingSearchResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandDmMonthRepPointLastDatelResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductSpecListDetailResultData;
import jp.co.osaki.osol.api.resultdata.generic.CommonSearchBuildingDetailResultData;
import jp.co.osaki.osol.api.resultdata.smoperation.SmOperationBuildingSearchDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandDmMonthRepPointLastDateDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductSpecListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.generic.CommonSearchBuildingServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 建物機器制御情報検索 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmOperationBuildingSearchDao extends OsolApiDao<SmOperationBuildingSearchParameter> {

    private final CommonSearchBuildingServiceDaoImpl commonSearchBuildingServiceDaoImpl;
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;
    private final ProductSpecListServiceDaoImpl productSpecListServiceDaoImpl;
    private final DemandDmMonthRepPointLastDateDaoImpl demandDmMonthRepPointLastDateDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public SmOperationBuildingSearchDao() {
        commonSearchBuildingServiceDaoImpl = new CommonSearchBuildingServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
        productSpecListServiceDaoImpl = new ProductSpecListServiceDaoImpl();
        demandDmMonthRepPointLastDateDaoImpl = new DemandDmMonthRepPointLastDateDaoImpl();
    }

    @Override
    public SmOperationBuildingSearchResult query(SmOperationBuildingSearchParameter parameter) throws Exception {
        List<SmOperationBuildingSearchDetailResultData> resultList = new ArrayList<>();


        //建物検索を行う
        Map<String, List<Object>> buildingSearchParamMap = getBuildingSearchCondition(parameter);
        List<CommonSearchBuildingDetailResultData> buildingList = getResultList(commonSearchBuildingServiceDaoImpl,
                buildingSearchParamMap);

        if (buildingList == null || buildingList.isEmpty()) {
            return new SmOperationBuildingSearchResult(resultList);
        }

        // フィルター処理
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        //建物番号順にソートする
        buildingList = buildingList.stream()
                .sorted(Comparator.comparing(CommonSearchBuildingDetailResultData::getBuildingNo,
                        Comparator.naturalOrder()))
                .collect(Collectors.toList());

        for (CommonSearchBuildingDetailResultData building : buildingList) {
            //建物機器検索を行う
            DemandBuildingSmListDetailResultData buildingSmParam = new DemandBuildingSmListDetailResultData();
            buildingSmParam.setCorpId(building.getCorpId());
            buildingSmParam.setBuildingId(building.getBuildingId());
            List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                    demandBuildingSmListServiceDaoImpl, buildingSmParam);
            if (buildingSmList == null || buildingSmList.isEmpty()) {
                //建物に機器が存在しない場合は除外
                continue;
            }

            //機器IDでソートする
            buildingSmList = buildingSmList.stream().sorted(
                    Comparator.comparing(DemandBuildingSmListDetailResultData::getSmId, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

            for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
                //機器検索を行う
                SmSelectResult smParam = new SmSelectResult();
                smParam.setSmId(buildingSm.getSmId());
                List<SmSelectResult> smList = getResultList(smSelectResultServiceDaoImpl, smParam);
                if (smList == null || smList.size() != 1) {
                    //機器が取得できない場合は除外
                    continue;
                }
                if (!CheckUtility.isNullOrEmpty(parameter.getProductCd())) {
                    if (!parameter.getProductCd().equals(smList.get(0).getProductCd())) {
                        //製品コードが一致しない場合は除外
                        continue;
                    }
                }
                if (!CheckUtility.isNullOrEmpty(parameter.getIpAddress())) {
                    if (CheckUtility.isNullOrEmpty(smList.get(0).getIpAddress())) {
                        //IPアドレスが設定されていない場合は除外
                        continue;
                    }
                    if (!smList.get(0).getIpAddress().matches(".*".concat(parameter.getIpAddress()).concat(".*"))) {
                        //IPアドレスと部分一致しない場合は除外
                        continue;
                    }
                }

                //リプレース対応 表示条件に掛からなければ除外する
                if(!isRunning(parameter, smList.get(0))) {
                    continue;
                }

                //製品情報を取得する
                ProductSpecListDetailResultData productParam = new ProductSpecListDetailResultData();
                productParam.setProductCd(smList.get(0).getProductCd());
                List<ProductSpecListDetailResultData> productList = getResultList(productSpecListServiceDaoImpl, productParam);
                if (productList == null || productList.size() != 1) {
                    //製品情報を取得できない場合は除外
                    continue;
                }

                if (!isSupportedProductCd(smList.get(0).getProductCd())) {
                    //サポートしない製品コードの場合は除外
                    continue;
                }

                // 最終計測年月日 取得
                DemandDmMonthRepPointLastDatelResultData demandPointResultData =
                        new DemandDmMonthRepPointLastDatelResultData();
                Date measurementDate = null;

                // 取得用パラメータ設定
                demandPointResultData.setCorpId(buildingSm.getCorpId());
                demandPointResultData.setBuildingId(buildingSm.getBuildingId());
                demandPointResultData.setSmId(buildingSm.getSmId());

                List<DemandDmMonthRepPointLastDatelResultData> demandPointResultList = getResultList(
                        demandDmMonthRepPointLastDateDaoImpl, demandPointResultData);

                if (demandPointResultList != null && demandPointResultList.size() > 0) {
                    measurementDate = demandPointResultList.get(0).getMeasurementDate();
                }

                //データを設定する
                resultList.add(setResultData(building, buildingSm, smList.get(0), productList.get(0), measurementDate));
            }

        }

        return new SmOperationBuildingSearchResult(resultList);
    }

    /**
     * 建物検索の条件を設定する
     * @param parameter
     * @return
     * @throws Exception
     */
    private Map<String, List<Object>> getBuildingSearchCondition(SmOperationBuildingSearchParameter parameter)
            throws Exception {

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
        if (parameter.getSmOperationSearchParameter() != null
                && parameter.getSmOperationSearchParameter().getPrefectureCdList() != null
                && !parameter.getSmOperationSearchParameter().getPrefectureCdList().isEmpty()) {

            for(String target:parameter.getSmOperationSearchParameter().getPrefectureCdList()) {
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

        return paramMap;

    }

    /**
     * 検索結果をセットする
     * @param buildingData
     * @param buildingSmData
     * @param smData
     * @param productData
     * @return
     */
    private SmOperationBuildingSearchDetailResultData setResultData(CommonSearchBuildingDetailResultData buildingData,
            DemandBuildingSmListDetailResultData buildingSmData, SmSelectResult smData,
            ProductSpecListDetailResultData productData, Date measurementDate) {
        SmOperationBuildingSearchDetailResultData result = new SmOperationBuildingSearchDetailResultData();

        result.setCorpId(buildingData.getCorpId());
        result.setCorpName(buildingData.getCorpName());
        result.setBuildingId(buildingData.getBuildingId());
        result.setBuildingNo(buildingData.getBuildingNo());
        result.setBuildingName(buildingData.getBuildingName());
        result.setBuildingNameKana(buildingData.getBuildingNameKana());
        result.setBuildingTansyukuName(buildingData.getBuildingTansyukuName());
        result.setMunicipalityCd(buildingData.getMunicipalityCd());
        result.setPrefectureCd(buildingData.getPrefectureCd());
        result.setZipCd(buildingData.getZipCd());
        result.setAddress(buildingData.getAddress());
        result.setAddressBuilding(buildingData.getAddressBuilding());
        result.setTelNo(buildingData.getTelNo());
        result.setFaxNo(buildingData.getFaxNo());
        result.setBiko(buildingData.getBiko());
        result.setFloorCount(buildingData.getFloorCount());
        result.setBasementCount(buildingData.getBasementCount());
        result.setNyukyoTypeCd(buildingData.getNyukyoTypeCd());
        result.setCommonUsedRate(buildingData.getCommonUsedRate());
        result.setKubunShoyuRate(buildingData.getKubunShoyuRate());
        result.setConpletedYm(buildingData.getConpletedYm());
        result.setEngManageFactoryType(buildingData.getEngManageFactoryType());
        result.setEngManageFactoryNo(buildingData.getEngManageFactoryNo());
        result.setTotalStartYm(buildingData.getTotalStartYm());
        result.setTotalEndYm(buildingData.getTotalEndYm());
        result.setEstimateUse(buildingData.getEstimateUse());
        result.setBuildingDelDate(buildingData.getBuildingDelDate());
        result.setBuildingDelPersonCorpId(buildingData.getBuildingDelPersonCorpId());
        result.setBuildingDelPersonId(buildingData.getBuildingDelPersonId());
        result.setFreonDischargeOffice(buildingData.getFreonDischargeOffice());
        result.setDivisionCorpId(buildingData.getDivisionCorpId());
        result.setDivisionBuildingId(buildingData.getDivisionBuildingId());
        result.setCityCd(buildingData.getCityCd());
        result.setBuildingType(buildingData.getBuildingType());
        result.setPrefectureName(buildingData.getPrefectureName());
        result.setProductCd(smData.getProductCd());
        result.setProductName(productData.getProductName());
        result.setSmId(buildingSmData.getSmId());
        result.setSmAddress(smData.getSmAddress());
        result.setIpAddress(smData.getIpAddress());
        result.setLastMeasurementDate(measurementDate);
        result.setAielMasterConnect(smData.getAielMasterConnectFlg());

        return result;
    }

    /**
     * サポートする製品コードかチェック
     * @param productCd 製品コード
     * @return true:サポート false:非サポート
     */
    private boolean isSupportedProductCd(String productCd) {
        boolean result = false;

        if (OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd) ||
            OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd) ||
            OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd) ||
            OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)||
            OsolConstants.PRODUCT_CD.FVP_E_ALPHA.getVal().equals(productCd)||
            OsolConstants.PRODUCT_CD.FVP_E_ALPHA2.getVal().equals(productCd)) {
            result = true;
        }

        return result;
    }

    /**
     * 稼働中機器の取得
     */
    private boolean isRunning(SmOperationBuildingSearchParameter building_prm, SmSelectResult sm_prm) {

        //falseであれば取得する(全機種取得)
        if(BooleanUtils.isFalse(building_prm.getIsRunningOnly())) {
            return true;
        }

        if(BooleanUtils.isNotFalse(building_prm.getIsRunningOnly())){
                // null null
                if(sm_prm.getStartDate() == null && sm_prm.getEndDate() == null) {
                    return true;
                }
                //endDate >= today
                if(sm_prm.getStartDate() == null && getServerDateTime().before(DateUtility.plusDay(sm_prm.getEndDate(), 1))) {
                    return true;
                }
                //endDate <= today
                if(sm_prm.getStartDate() == null && getServerDateTime().after(DateUtility.plusDay(sm_prm.getEndDate(), 1))) {
                    return false;
                }
                //startDate <= today null
                if(getServerDateTime().after(sm_prm.getStartDate()) && sm_prm.getEndDate() == null) {
                    return true;
                }
                //startDate <= today <= endDate
                if(getServerDateTime().after(sm_prm.getStartDate()) && getServerDateTime().before(DateUtility.plusDay(sm_prm.getEndDate(), 1))){
                    return true;
                }
            }
        return false;
    }

}
