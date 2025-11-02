/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building.setting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.setting.BuildingDmAvailableEnergyInfoSelectParameter;
import jp.co.osaki.osol.api.result.building.setting.BuildingDmAvailableEnergyInfoSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyDetailDataListResultData;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyDetailDataListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 建物デマンド使用エネルギー情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class BuildingDmAvailableEnergyInfoSelectDao extends OsolApiDao<BuildingDmAvailableEnergyInfoSelectParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final AvailableEnergyDetailDataListServiceDaoImpl availableEnergyDetailDataListServiceDaoImpl;
    private final AvailableEnergyLineListServiceDaoImpl availableEnergyLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public BuildingDmAvailableEnergyInfoSelectDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        availableEnergyDetailDataListServiceDaoImpl = new AvailableEnergyDetailDataListServiceDaoImpl();
        availableEnergyLineListServiceDaoImpl = new AvailableEnergyLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingDmAvailableEnergyInfoSelectResult query(BuildingDmAvailableEnergyInfoSelectParameter parameter) throws Exception {

        BuildingDmAvailableEnergyInfoSelectResult result = new BuildingDmAvailableEnergyInfoSelectResult();
        //企業情報取得
        if(!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }
        CommonCorpExclusionResult corpParam = new CommonCorpExclusionResult();
        corpParam.setCorpId(parameter.getOperationCorpId());

        List<CommonCorpExclusionResult> corpList = getResultList(commonCorpExclusionServiceDaoImpl,corpParam);

        if(corpList == null || corpList.size() != 1 ) {
            return result;
        }

        result.setCorpId(corpList.get(0).getCorpId());
        result.setCorpVersion(corpList.get(0).getVersion());

        //建物情報取得
        CommonBuildingExclusionResult buildingParam = new CommonBuildingExclusionResult();
        buildingParam.setCorpId(parameter.getOperationCorpId());
        buildingParam.setBuildingId(parameter.getBuildingId());

        List<CommonBuildingExclusionResult> buildingList = getResultList(commonBuildingExclusionServiceDaoImpl,buildingParam);

        if(buildingList == null || buildingList.size() != 1 ) {
            return result;
        }

        result.setBuildingId(buildingList.get(0).getBuildingId());
        result.setBuildingVersion(buildingList.get(0).getVersion());

        //使用エネルギー情報取得
        AvailableEnergyDetailDataListResultData availableEnergyParam = new AvailableEnergyDetailDataListResultData();

        availableEnergyParam.setCorpId(parameter.getOperationCorpId());
        availableEnergyParam.setBuildingId(parameter.getBuildingId());
        availableEnergyParam.setEngTypeCd(parameter.getEngTypeCd());
        availableEnergyParam.setEngId(parameter.getEngId());
        availableEnergyParam.setContractId(parameter.getContractId());

        List<AvailableEnergyDetailDataListResultData>availableEnergyResultList = getResultList(availableEnergyDetailDataListServiceDaoImpl, availableEnergyParam);

        // フィルター処理
        availableEnergyResultList = buildingDataFilterDao.applyDataFilter(availableEnergyResultList, new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        availableEnergyResultList.sort(Comparator.comparing(o -> o.getDisplayOrder()));

        result.setAvailableEnergyList(availableEnergyResultList);

        //使用エネルギー情報が存在し、使用エネルギー実績系統値反映フラグがONのもののみ追加する。
        List<AvailableEnergyLineListResultData> existList = new ArrayList<>();
      //使用エネルギー系統情報取得
        AvailableEnergyLineListResultData availableEnergyLineParam = new AvailableEnergyLineListResultData();
        for(AvailableEnergyDetailDataListResultData vailableEnergyResult : availableEnergyResultList.stream().filter(o -> o.getEnergyUseLineValueFlg() == 1).collect(Collectors.toList()) ) {


            availableEnergyLineParam.setCorpId(vailableEnergyResult.getCorpId());
            availableEnergyLineParam.setBuildingId(vailableEnergyResult.getBuildingId());
            availableEnergyLineParam.setEngTypeCd(vailableEnergyResult.getEngTypeCd());
            availableEnergyLineParam.setEngId(vailableEnergyResult.getEngId());
            availableEnergyLineParam.setContractId(vailableEnergyResult.getContractId());

            List<AvailableEnergyLineListResultData>availableEnergyLineResultList = getResultList(availableEnergyLineListServiceDaoImpl, availableEnergyLineParam);
            existList.addAll(availableEnergyLineResultList);
            availableEnergyLineResultList = null;


        }

      //TIME型からDate型への強制変換
        for(AvailableEnergyLineListResultData lineresult : existList) {
            if(lineresult.getStartTime() != null) {
                lineresult.setStartTime(DateUtility.plusYear(DateUtility.plusYear(lineresult.getStartTime(),1),-1));
            }
            if(lineresult.getEndTime() != null) {
                lineresult.setEndTime(DateUtility.plusYear(DateUtility.plusYear(lineresult.getEndTime(),1),-1));
            }

        }

        result.setAvailableEnergyLineList(existList);

        return result;

    }


}
