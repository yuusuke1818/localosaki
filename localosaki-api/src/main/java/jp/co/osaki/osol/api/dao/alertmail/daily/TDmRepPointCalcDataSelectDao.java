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
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepPointCalcDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepPointCalcDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepPointCalcCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmRepPointCalcSelectServiceDaoImpl;

/**
 * 受電ゼロチェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmRepPointCalcDataSelectDao extends OsolApiDao<TDmRepPointCalcDataSelectParameter> {

    private final TDmRepPointCalcSelectServiceDaoImpl tDmRepPointCalcSelectServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public TDmRepPointCalcDataSelectDao() {
        tDmRepPointCalcSelectServiceDaoImpl = new TDmRepPointCalcSelectServiceDaoImpl();
    }

    @Override
    public TDmRepPointCalcDataSelectResult query(TDmRepPointCalcDataSelectParameter parameter) throws Exception {

        TDmRepPointCalcDataSelectResult result = new TDmRepPointCalcDataSelectResult();

        TDmRepPointCalcCheckListResultData param = new TDmRepPointCalcCheckListResultData();
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

        List<TDmRepPointCalcCheckListResultData> resultList = getResultList(tDmRepPointCalcSelectServiceDaoImpl, param);
        result.setTDmRepPointCalcCheckListResultDataList(resultList);

        return result;
    }

}
