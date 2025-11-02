/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.alertmail.daily;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepPointTempHumidSensorDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepPointTempHumidSensorDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepPointTempHumidSensorCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmRepPointTempHumidSensorSelectServiceDaoImpl;

/**
 * 温湿度センサーチェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmRepPointTempHumidSensorDataSelectDao extends OsolApiDao<TDmRepPointTempHumidSensorDataSelectParameter> {

    private final TDmRepPointTempHumidSensorSelectServiceDaoImpl tDmRepPointTempHumidSensorSelectServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public TDmRepPointTempHumidSensorDataSelectDao() {
        tDmRepPointTempHumidSensorSelectServiceDaoImpl = new TDmRepPointTempHumidSensorSelectServiceDaoImpl();
    }

    @Override
    public TDmRepPointTempHumidSensorDataSelectResult query(TDmRepPointTempHumidSensorDataSelectParameter parameter)
            throws Exception {

        TDmRepPointTempHumidSensorDataSelectResult result = new TDmRepPointTempHumidSensorDataSelectResult();

        TDmRepPointTempHumidSensorCheckListResultData param = new TDmRepPointTempHumidSensorCheckListResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setCorpName(parameter.getCorpName());
        param.setBuildingId(parameter.getBuildingId());
        param.setBuildingNo(parameter.getBuildingNo());
        param.setBuildingName(parameter.getBuildingName());
        param.setSmId(parameter.getSmId());
        param.setSmAddress(parameter.getSmAddress());
        param.setIpAddress(parameter.getIpAddress());
        param.setNowDate(parameter.getNowDate());
        param.setTargetDate(parameter.getTargetDate());
        param.setThreshold(parameter.getThreshold());

        List<TDmRepPointTempHumidSensorCheckListResultData> resultList = getResultList(
                tDmRepPointTempHumidSensorSelectServiceDaoImpl, param);
        result.setTDmRepPointTempHumidSensorCheckListResultDataList(resultList);

        return result;
    }

}
