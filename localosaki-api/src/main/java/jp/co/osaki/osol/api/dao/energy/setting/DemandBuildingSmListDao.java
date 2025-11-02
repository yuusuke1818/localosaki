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
import jp.co.osaki.osol.api.parameter.energy.ems.DemandBuildingSmListParameter;
import jp.co.osaki.osol.api.result.energy.setting.DemandBuildingSmListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物機器データ取得処理 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandBuildingSmListDao extends OsolApiDao<DemandBuildingSmListParameter> {

    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public DemandBuildingSmListDao() {
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandBuildingSmListResult query(DemandBuildingSmListParameter parameter) throws Exception {
        DemandBuildingSmListResult result = new DemandBuildingSmListResult();
        List<DemandBuildingSmListDetailResultData> detailList = new ArrayList<>();

        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            //選択中企業が設定されている場合は、そちらを優先
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandBuildingSmListResult();
        }

        //建物機器からデータを取得する
        DemandBuildingSmListDetailResultData buildingSmParam = new DemandBuildingSmListDetailResultData();
        buildingSmParam.setCorpId(parameter.getOperationCorpId());
        buildingSmParam.setBuildingId(parameter.getBuildingId());
        buildingSmParam.setSmId(parameter.getSmId());
        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                demandBuildingSmListServiceDaoImpl, buildingSmParam);
        //フィルタ処理を行う
        buildingSmList = buildingDataFilterDao.applyDataFilter(buildingSmList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingSmList == null || buildingSmList.isEmpty()) {
            return new DemandBuildingSmListResult();
        }

        for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
            //建物排他情報を取得する
            CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
            exBuildingParam.setCorpId(buildingSm.getCorpId());
            exBuildingParam.setBuildingId(buildingSm.getBuildingId());
            List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                    exBuildingParam);
            if (exBuildingList == null || exBuildingList.isEmpty()) {
                continue;
            }

            //機器排他情報を取得する
            CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
            exSmParam.setSmId(buildingSm.getSmId());
            List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
            if (exSmList == null || exSmList.size() != 1) {
                continue;
            }

            detailList.add(new DemandBuildingSmListDetailResultData(buildingSm.getCorpId(), buildingSm.getBuildingId(),
                    buildingSm.getSmId(), buildingSm.getSmAddress(), buildingSm.getProductCd(),
                    buildingSm.getProductName(), buildingSm.getLoadControlOutput(), buildingSm.getBuildingSmVersion(),
                    exBuildingList.get(0).getVersion(), exSmList.get(0).getVersion()));
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setVersion(exCorpList.get(0).getVersion());
        result.setDetailList(detailList);

        return result;
    }

}
