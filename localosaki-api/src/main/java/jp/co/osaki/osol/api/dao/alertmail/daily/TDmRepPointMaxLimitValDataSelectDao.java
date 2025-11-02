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
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepPointMaxLimitValDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepPointMaxLimitValDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepPointMaxLimitValCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmRepPointMaxLimitValSelectServiceDaoImpl;

/**
 * 上限値チェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmRepPointMaxLimitValDataSelectDao extends OsolApiDao<TDmRepPointMaxLimitValDataSelectParameter> {

    private final TDmRepPointMaxLimitValSelectServiceDaoImpl tDmRepPointMaxLimitValSelectServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public TDmRepPointMaxLimitValDataSelectDao() {
        tDmRepPointMaxLimitValSelectServiceDaoImpl = new TDmRepPointMaxLimitValSelectServiceDaoImpl();
    }

    @Override
    public TDmRepPointMaxLimitValDataSelectResult query(TDmRepPointMaxLimitValDataSelectParameter parameter)
            throws Exception {

        TDmRepPointMaxLimitValDataSelectResult result = new TDmRepPointMaxLimitValDataSelectResult();

        TDmRepPointMaxLimitValCheckListResultData param = new TDmRepPointMaxLimitValCheckListResultData();
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

        List<TDmRepPointMaxLimitValCheckListResultData> resultList = getResultList(
                tDmRepPointMaxLimitValSelectServiceDaoImpl, param);
        result.setTDmRepPointMaxLimitValCheckListResultDataList(resultList);

        return result;
    }

}
