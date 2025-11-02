/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.setting.AvailableEnergyLineListParameter;
import jp.co.osaki.osol.api.result.building.setting.AvailableEnergyLineListResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyLineListServiceDaoImpl;

/**
 * 使用エネルギー系統情報取得 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class AvailableEnergyLineListDao extends OsolApiDao<AvailableEnergyLineListParameter> {

    private final AvailableEnergyLineListServiceDaoImpl availableEnergyLineListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AvailableEnergyLineListDao() {
        availableEnergyLineListServiceDaoImpl = new AvailableEnergyLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public AvailableEnergyLineListResult query(AvailableEnergyLineListParameter parameter) throws Exception {

        AvailableEnergyLineListResult result = new AvailableEnergyLineListResult();
        AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();

        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setEngId(parameter.getEngId());
        param.setContractId(parameter.getContractId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setLineNo(parameter.getLineNo());


        List<AvailableEnergyLineListResultData>resultList = getResultList(availableEnergyLineListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return result;
        }

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList, new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setAvailableEnergyLineList(resultList);

        return result;

    }


}
