/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.alertmail.daily;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepLineWaterOilLeakDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepLineWaterOilLeakDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TDmRepLineWaterOilLeakCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TDmRepLineWaterOilLeakSelectServiceDaoImpl;

/**
 * 水・油漏れチェック Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TDmRepLineWaterOilLeakDataSelectDao extends OsolApiDao<TDmRepLineWaterOilLeakDataSelectParameter> {

    private final TDmRepLineWaterOilLeakSelectServiceDaoImpl tDmRepLineWaterOilLeakSelectServiceDaoImpl;

    public TDmRepLineWaterOilLeakDataSelectDao() {
        tDmRepLineWaterOilLeakSelectServiceDaoImpl = new TDmRepLineWaterOilLeakSelectServiceDaoImpl();
    }

    @Override
    public TDmRepLineWaterOilLeakDataSelectResult query(TDmRepLineWaterOilLeakDataSelectParameter parameter)
            throws Exception {

        TDmRepLineWaterOilLeakDataSelectResult result = new TDmRepLineWaterOilLeakDataSelectResult();

        TDmRepLineWaterOilLeakCheckListResultData param = new TDmRepLineWaterOilLeakCheckListResultData();
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
        param.setChkLineValueKw(parameter.getChkLineValueKw());
        param.setChkJigenNoFrom(parameter.getChkJigenNoFrom());
        param.setChkJigenNoTo(parameter.getChkJigenNoTo());
        param.setChkLineType(parameter.getChkLineType());

        List<TDmRepLineWaterOilLeakCheckListResultData> resultList = getResultList(
                tDmRepLineWaterOilLeakSelectServiceDaoImpl, param);
        result.setTDmRepLineWaterOilLeakCheckListResultDataList(resultList);

        return result;
    }

}
