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
import jp.co.osaki.osol.api.parameter.energy.setting.DemandBuildingSmPointListParameter;
import jp.co.osaki.osol.api.result.energy.setting.DemandBuildingSmPointListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物機器ポイント情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandBuildingSmPointListDao extends OsolApiDao<DemandBuildingSmPointListParameter> {

    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public DemandBuildingSmPointListDao() {
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public DemandBuildingSmPointListResult query(DemandBuildingSmPointListParameter parameter) throws Exception {
        DemandBuildingSmPointListResult result = new DemandBuildingSmPointListResult();
        List<DemandBuildingSmPointListDetailResultData> detailList = new ArrayList<>();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
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
            return new DemandBuildingSmPointListResult();
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        //フィルタ処理を行う
        exBuildingList = buildingDataFilterDao.applyDataFilter(exBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new DemandBuildingSmPointListResult();
        }

        //建物機器ポイント情報を取得する
        DemandBuildingSmPointListDetailResultData param = new DemandBuildingSmPointListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setSmId(parameter.getSmId());
        param.setPointNo(parameter.getPointNo());
        param.setPointType(parameter.getPointType());
        List<DemandBuildingSmPointListDetailResultData> pointList = getResultList(
                demandBuildingSmPointListServiceDaoImpl, param);
        if (pointList == null || pointList.isEmpty()) {
            result.setDetailList(new ArrayList<>());
        } else {
            for (DemandBuildingSmPointListDetailResultData point : pointList) {
                DemandBuildingSmPointListDetailResultData detail = new DemandBuildingSmPointListDetailResultData();
                detail.setCorpId(point.getCorpId());
                detail.setBuildingId(point.getBuildingId());
                detail.setSmId(point.getSmId());
                detail.setPointNo(point.getPointNo());
                detail.setPointName(point.getPointName());
                detail.setPointUnit(point.getPointUnit());
                detail.setPointSumFlg(point.getPointSumFlg());
                detail.setDelFlg(point.getDelFlg());
                detail.setVersion(point.getVersion());
                detailList.add(detail);
            }
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setCorpVersion(exCorpList.get(0).getVersion());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setDetailList(detailList);
        return result;
    }

}
