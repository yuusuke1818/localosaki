/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.NoBuildingSelectParameter;
import jp.co.osaki.osol.api.result.building.NoBuildingSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.NoBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;

/**
 * 建物・テナント一覧取得（建物・テナント番号指定） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class NoBuildingSelectDao extends OsolApiDao<NoBuildingSelectParameter> {

    private final NoBuildingSelectServiceDaoImpl noBuildingSelectServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public NoBuildingSelectDao() {
        noBuildingSelectServiceDaoImpl = new NoBuildingSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    @Override
    public NoBuildingSelectResult query(NoBuildingSelectParameter parameter) throws Exception {
        NoBuildingSelectResult result = new NoBuildingSelectResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new NoBuildingSelectResult();
        }

        AllBuildingListDetailResultData param = new AllBuildingListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setTotalTargetYm(parameter.getTotalTargetYm());
        param.setBuildingNo(parameter.getBuildingNo());
        List<AllBuildingListDetailResultData> resultList = getResultList(noBuildingSelectServiceDaoImpl, param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetailList(resultList);

        return result;
    }

}
