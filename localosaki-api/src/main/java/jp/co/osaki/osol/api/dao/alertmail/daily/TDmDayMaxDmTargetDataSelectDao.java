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
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmDayMaxDmTargetDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmDayMaxDmTargetDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmDayMaxDmTargetCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmDayMaxDmTargetSelectServiceDaoImpl;

/**
 * デマンド目標超過チェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmDayMaxDmTargetDataSelectDao extends OsolApiDao<TDmDayMaxDmTargetDataSelectParameter> {

    private final TDmDayMaxDmTargetSelectServiceDaoImpl tDmDayMaxDmTargetSelectServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public TDmDayMaxDmTargetDataSelectDao() {
        tDmDayMaxDmTargetSelectServiceDaoImpl = new TDmDayMaxDmTargetSelectServiceDaoImpl();
    }

    @Override
    public TDmDayMaxDmTargetDataSelectResult query(TDmDayMaxDmTargetDataSelectParameter parameter) throws Exception {

        TDmDayMaxDmTargetDataSelectResult result = new TDmDayMaxDmTargetDataSelectResult();

        TDmDayMaxDmTargetCheckListResultData param = new TDmDayMaxDmTargetCheckListResultData();
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

        List<TDmDayMaxDmTargetCheckListResultData> resultList = getResultList(tDmDayMaxDmTargetSelectServiceDaoImpl,
                param);
        result.setTDmDayMaxDmTargetCheckListResultDataList(resultList);

        return result;
    }

}
