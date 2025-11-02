/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDemandListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDemandListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;

/**
 * 建物デマンド情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class BuildingDemandListDao extends OsolApiDao<BuildingDemandListParameter> {

    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public BuildingDemandListDao() {
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingDemandListResult query(BuildingDemandListParameter parameter) throws Exception {
        BuildingDemandListResult result = new BuildingDemandListResult();
        List<BuildingDemandListDetailResultData> detailList = new ArrayList<>();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new BuildingDemandListResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = new BuildingDemandListDetailResultData();
        demandParam.setCorpId(parameter.getOperationCorpId());
        demandParam.setBuildingId(parameter.getBuildingId());
        List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);

        //フィルタ処理を行う
        demandList = buildingDataFilterDao.applyDataFilter(demandList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (demandList == null || demandList.isEmpty()) {
            return new BuildingDemandListResult();
        }

        for (BuildingDemandListDetailResultData demand : demandList) {
            //排他建物情報を取得する
            CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
            exBuildingParam.setCorpId(demand.getCorpId());
            exBuildingParam.setBuildingId(demand.getBuildingId());
            List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                    exBuildingParam);

            if (exBuildingList == null || exBuildingList.size() != 1) {
                continue;
            }

            demand.setVersionBuilding(exBuildingList.get(0).getVersion());
            detailList.add(demand);
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setVersion(exCorpList.get(0).getVersion());
        result.setDetailList(detailList);
        return result;
    }

}
