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
import jp.co.osaki.osol.api.parameter.building.GroupBuildingListParameter;
import jp.co.osaki.osol.api.result.building.GroupBuildingListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.GroupBuildingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;

/**
 * 建物・テナント一覧取得（グループ指定） Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class GroupBuildingListDao extends OsolApiDao<GroupBuildingListParameter> {

    private final GroupBuildingListServiceDaoImpl groupBuildingListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    public GroupBuildingListDao() {
        groupBuildingListServiceDaoImpl = new GroupBuildingListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public GroupBuildingListResult query(GroupBuildingListParameter parameter) throws Exception {
        GroupBuildingListResult result = new GroupBuildingListResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new GroupBuildingListResult();
        }

        AllBuildingListDetailResultData param = new AllBuildingListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setTotalTargetYm(parameter.getTotalTargetYm());
        param.setParentGroupId(parameter.getParentGroupId());
        param.setChildGroupId(parameter.getChildGroupId());
        List<AllBuildingListDetailResultData> resultList = getResultList(groupBuildingListServiceDaoImpl, param);

        //フィルター処理
        resultList = buildingDataFilterDao.applyDataFilter(resultList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetailList(resultList);

        return result;
    }

}
