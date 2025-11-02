/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.input;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.input.EnergyUseListParameter;
import jp.co.osaki.osol.api.result.energy.input.EnergyUseListResult;
import jp.co.osaki.osol.api.resultdata.energy.input.EnergyUseListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.input.EnergyUseListServiceDaoImpl;

/**
 * 使用エネルギー実績取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class EnergyUseListDao extends OsolApiDao<EnergyUseListParameter> {

    private final EnergyUseListServiceDaoImpl energyUseListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public EnergyUseListDao() {
        energyUseListServiceDaoImpl = new EnergyUseListServiceDaoImpl();
    }

    @Override
    public EnergyUseListResult query(EnergyUseListParameter parameter) throws Exception {
        EnergyUseListDetailResultData param = new EnergyUseListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setEngId(parameter.getEngId());
        param.setContractId(parameter.getContractId());
        param.setCalYmFrom(parameter.getCalYmFrom());
        param.setCalYmTo(parameter.getCalYmTo());
        param.setDayAndNightType(parameter.getDayAndNightType());
        param.setEngSupplyType(parameter.getEngSupplyType());

        List<EnergyUseListDetailResultData> resultList = getResultList(energyUseListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return new EnergyUseListResult();
        }

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        return new EnergyUseListResult(resultList);
    }

}
