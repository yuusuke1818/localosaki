/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.DemandGraphElementListParameter;
import jp.co.osaki.osol.api.result.energy.setting.DemandGraphElementListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;

/**
 * グラフ要素設定情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class DemandGraphElementListDao extends OsolApiDao<DemandGraphElementListParameter> {

    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public DemandGraphElementListDao() {
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandGraphElementListResult query(DemandGraphElementListParameter parameter) throws Exception {

        DemandGraphElementListDetailResultData param = new DemandGraphElementListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setGraphId(parameter.getGraphId());
        param.setGraphElementId(parameter.getGraphElementId());
        List<DemandGraphElementListDetailResultData> resultList = getResultList(demandGraphElementListServiceDaoImpl,
                param);
        if (resultList == null || resultList.isEmpty()) {
            return new DemandGraphElementListResult(resultList);
        }

        // フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        return new DemandGraphElementListResult(resultList);
    }

}
