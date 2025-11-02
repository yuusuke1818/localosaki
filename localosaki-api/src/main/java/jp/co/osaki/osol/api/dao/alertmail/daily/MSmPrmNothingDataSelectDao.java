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
import jp.co.osaki.osol.api.parameter.alertmail.daily.MSmPrmNothingDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.MSmPrmNothingDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.MSmPrmNothingDataCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.MSmPrmNothingDataSelectServiceDaoImpl;

/**
 * データなしチェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class MSmPrmNothingDataSelectDao extends OsolApiDao<MSmPrmNothingDataSelectParameter> {

    private final MSmPrmNothingDataSelectServiceDaoImpl mSmPrmNothingDataSelectServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public MSmPrmNothingDataSelectDao() {
        mSmPrmNothingDataSelectServiceDaoImpl = new MSmPrmNothingDataSelectServiceDaoImpl();
    }

    @Override
    public MSmPrmNothingDataSelectResult query(MSmPrmNothingDataSelectParameter parameter) throws Exception {
        MSmPrmNothingDataSelectResult result = new MSmPrmNothingDataSelectResult();

        MSmPrmNothingDataCheckListResultData param = new MSmPrmNothingDataCheckListResultData();
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

        List<MSmPrmNothingDataCheckListResultData> resultList = getResultList(mSmPrmNothingDataSelectServiceDaoImpl,
                param);
        result.setMSmPrmNothingDataCheckListResultDataList(resultList);

        return result;
    }

}
