/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.DemandGraphSettingListParameter;
import jp.co.osaki.osol.api.result.energy.setting.DemandGraphSettingListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphSettingListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphSettingListServiceDaoImpl;

/**
 * グラフ設定情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandGraphSettingListDao extends OsolApiDao<DemandGraphSettingListParameter> {

    private final DemandGraphSettingListServiceDaoImpl demandGraphSettingListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public DemandGraphSettingListDao() {
        demandGraphSettingListServiceDaoImpl = new DemandGraphSettingListServiceDaoImpl();
    }

    @Override
    public DemandGraphSettingListResult query(DemandGraphSettingListParameter parameter) throws Exception {
        DemandGraphSettingListResult result = new DemandGraphSettingListResult();

        List<DemandGraphSettingListDetailResultData> resultList;
        DemandGraphSettingListDetailResultData param = new DemandGraphSettingListDetailResultData();

        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setGraphId(parameter.getGraphId());
        resultList = getResultList(demandGraphSettingListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return new DemandGraphSettingListResult();
        }

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (resultList == null || resultList.isEmpty()) {
            return new DemandGraphSettingListResult();
        }

        result.setDetailList(resultList);
        return result;
    }

}
