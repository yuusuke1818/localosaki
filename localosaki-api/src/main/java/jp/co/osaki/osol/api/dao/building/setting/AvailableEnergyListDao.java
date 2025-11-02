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
import jp.co.osaki.osol.api.parameter.building.setting.AvailableEnergyListParameter;
import jp.co.osaki.osol.api.result.building.setting.AvailableEnergyListResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyListResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyListServiceDaoImpl;

/**
 * 使用エネルギー情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class AvailableEnergyListDao extends OsolApiDao<AvailableEnergyListParameter> {

    private final AvailableEnergyListServiceDaoImpl availableEnergyListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public AvailableEnergyListDao() {
        availableEnergyListServiceDaoImpl = new AvailableEnergyListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public AvailableEnergyListResult query(AvailableEnergyListParameter parameter) throws Exception {

        AvailableEnergyListResult result = new AvailableEnergyListResult();
        AvailableEnergyListResultData param = new AvailableEnergyListResultData();

        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setEngId(parameter.getEngId());
        param.setContractId(parameter.getContractId());
        param.setEnergyYmFrom(parameter.getEnergyYmFrom());
        param.setEnergyYmTo(parameter.getEnergyYmTo());
        param.setEnergyYmPoint(parameter.getEnergyYmPoint());
        param.setDayAndNightType(parameter.getDayAndNightType());
        param.setEngSupplyType(parameter.getEngSupplyType());

        List<AvailableEnergyListResultData>resultList = getResultList(availableEnergyListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return result;
        }

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList, new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setAvailableEnergyList(resultList);

        return result;

    }


}
