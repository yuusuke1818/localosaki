/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.setting.EnergyListParameter;
import jp.co.osaki.osol.api.result.building.setting.EnergyListResult;
import jp.co.osaki.osol.api.resultdata.building.setting.EnergyListResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.EnergyListServiceDaoImpl;

/**
 * エネルギー情報取得 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class EnergyListDao extends OsolApiDao<EnergyListParameter> {

    private final EnergyListServiceDaoImpl energyListServiceDaoImpl;

    public EnergyListDao() {
        energyListServiceDaoImpl = new EnergyListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public EnergyListResult query(EnergyListParameter parameter) throws Exception {

        EnergyListResult result = new EnergyListResult();
        EnergyListResultData param = new EnergyListResultData();

        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setEngId(parameter.getEngId());
        List<EnergyListResultData>resultList = getResultList(energyListServiceDaoImpl, param);
        if (resultList == null || resultList.isEmpty()) {
            return result;
        }

        result.setEnergyList(resultList);

        return result;

    }


}
