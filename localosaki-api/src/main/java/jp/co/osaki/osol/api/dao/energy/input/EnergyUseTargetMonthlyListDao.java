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
import jp.co.osaki.osol.api.parameter.energy.input.EnergyUseTargetMonthlyListParameter;
import jp.co.osaki.osol.api.result.energy.input.EnergyUseTargetMonthlyListResult;
import jp.co.osaki.osol.api.resultdata.energy.input.EnergyUseTargetMonthlyListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.input.EnergyUseTargetMonthlyListServiceDaoImpl;

/**
 * 使用エネルギー各月目標取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class EnergyUseTargetMonthlyListDao extends OsolApiDao<EnergyUseTargetMonthlyListParameter> {

    private final EnergyUseTargetMonthlyListServiceDaoImpl energyUseTargetMonthlyListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public EnergyUseTargetMonthlyListDao() {
        energyUseTargetMonthlyListServiceDaoImpl = new EnergyUseTargetMonthlyListServiceDaoImpl();
    }

    @Override
    public EnergyUseTargetMonthlyListResult query(EnergyUseTargetMonthlyListParameter parameter) throws Exception {

        EnergyUseTargetMonthlyListDetailResultData param = new EnergyUseTargetMonthlyListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setCalYmFrom(parameter.getCalYmFrom());
        param.setCalYmTo(parameter.getCalYmTo());
        List<EnergyUseTargetMonthlyListDetailResultData> resultList = getResultList(
                energyUseTargetMonthlyListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return new EnergyUseTargetMonthlyListResult();
        }
        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        return new EnergyUseTargetMonthlyListResult(resultList);
    }

}
