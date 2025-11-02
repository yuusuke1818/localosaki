/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.alertmail.daily;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepLineMinusValDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepLineMinusValDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepLineMinusValCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmRepLineMinusValSelectServiceDaoImpl;

/**
 * マイナス値チェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmRepLineMinusValDataSelectDao extends OsolApiDao<TDmRepLineMinusValDataSelectParameter> {

    private final TDmRepLineMinusValSelectServiceDaoImpl tDmRepLineMinusValSelectServiceDaoImpl;

    public TDmRepLineMinusValDataSelectDao() {
        tDmRepLineMinusValSelectServiceDaoImpl = new TDmRepLineMinusValSelectServiceDaoImpl();
    }

    @Override
    public TDmRepLineMinusValDataSelectResult query(TDmRepLineMinusValDataSelectParameter parameter)
            throws Exception {

        TDmRepLineMinusValDataSelectResult result = new TDmRepLineMinusValDataSelectResult();

        TDmRepLineMinusValCheckListResultData param = new TDmRepLineMinusValCheckListResultData();
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

        List<TDmRepLineMinusValCheckListResultData> resultList = getResultList(
                tDmRepLineMinusValSelectServiceDaoImpl, param);
        result.setTDmRepLineMinusValCheckListResultDataList(resultList);

        return result;
    }

}
