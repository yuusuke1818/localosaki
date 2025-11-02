/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.LineGroupSearchParameter;
import jp.co.osaki.osol.api.result.energy.setting.LineGroupSearchResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 系統グループ取得Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class LineGroupSearchDao extends OsolApiDao<LineGroupSearchParameter> {

    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public LineGroupSearchDao() {
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public LineGroupSearchResult query(LineGroupSearchParameter parameter) throws Exception {
        LineGroupSearchResult result = new LineGroupSearchResult();
        List<LineGroupSearchDetailResultData> detailList = new ArrayList<>();

        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            //選択中企業が設定されている場合は、そちらを優先
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new LineGroupSearchResult();
        }

        //系統グループ情報を取得する
        LineGroupSearchDetailResultData groupParam = new LineGroupSearchDetailResultData();
        groupParam.setCorpId(parameter.getOperationCorpId());
        groupParam.setLineGroupType(parameter.getLineGroupType());
        groupParam.setBuildingId(parameter.getBuildingId());
        List<LineGroupSearchDetailResultData> groupList = getResultList(lineGroupSearchServiceDaoImpl, groupParam);

        //建物のフィルタ処理を行うが、企業標準系統はフィルタをかけられないので、企業系統と個別系統に分割する
        List<LineGroupSearchDetailResultData> corpList = new ArrayList<>();
        List<LineGroupSearchDetailResultData> buildingList = new ArrayList<>();
        for (LineGroupSearchDetailResultData group : groupList) {
            if (ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal().equals(group.getLineGroupType())) {
                corpList.add(group);
            } else {
                buildingList.add(group);
            }
        }

        //個別系統のみフィルタをかける
        if (!buildingList.isEmpty()) {
            buildingList = buildingDataFilterDao.applyDataFilter(buildingList,
                    new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));
        }

        if (corpList.isEmpty() && (buildingList == null || buildingList.isEmpty())) {
            return new LineGroupSearchResult();
        }

        groupList.clear();
        if (!corpList.isEmpty()) {
            groupList.addAll(corpList);
        }
        if (buildingList != null && !buildingList.isEmpty()) {
            groupList.addAll(buildingList);
        }

        for (LineGroupSearchDetailResultData group : groupList) {
            if (group.getBuildingId() != null) {
                //排他建物情報を取得する
                CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
                exBuildingParam.setCorpId(group.getCorpId());
                exBuildingParam.setBuildingId(group.getBuildingId());
                List<CommonBuildingExclusionResult> exBuildingList = getResultList(
                        commonBuildingExclusionServiceDaoImpl,
                        exBuildingParam);
                if (exBuildingList == null || exBuildingList.size() != 1) {
                    continue;
                }
                detailList.add(new LineGroupSearchDetailResultData(group.getCorpId(), group.getLineGroupId(),
                        group.getLineGroupName(), group.getLineGroupType(),group.getInitialViewFlg(), group.getBuildingId(), group.getDelFlg(),
                        group.getGroupVersion(), exBuildingList.get(0).getVersion()));
            } else {
                detailList.add(new LineGroupSearchDetailResultData(group.getCorpId(), group.getLineGroupId(),
                        group.getLineGroupName(), group.getLineGroupType(),group.getInitialViewFlg(), group.getBuildingId(), group.getDelFlg(),
                        group.getGroupVersion(), null));
            }

        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setVersion(exCorpList.get(0).getVersion());
        result.setDetailList(detailList);
        return result;
    }

}
