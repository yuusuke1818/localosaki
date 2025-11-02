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
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineTimeStandardListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineTimeStandardListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingLineTimeStandardListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物系統時限標準値取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class BuildingLineTimeStandardListDao extends OsolApiDao<BuildingLineTimeStandardListParameter> {

    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingLineTimeStandardListDao() {
        buildingLineTimeStandardListServiceDaoImpl = new BuildingLineTimeStandardListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public BuildingLineTimeStandardListResult query(BuildingLineTimeStandardListParameter parameter) throws Exception {
        BuildingLineTimeStandardListResult result = new BuildingLineTimeStandardListResult();
        List<BuildingLineTimeStandardListDetailResultData> detailList = new ArrayList<>();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        //フィルタ処理を行う
        exBuildingList = buildingDataFilterDao.applyDataFilter(exBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingLineTimeStandardListResult();
        }

        //系統情報を取得する
        LineListDetailResultData paramLine = new LineListDetailResultData();
        paramLine.setCorpId(parameter.getOperationCorpId());
        paramLine.setLineGroupId(parameter.getLineGroupId());
        paramLine.setLineNo(parameter.getLineNo());
        paramLine.setLineEnableFlg(ApiCodeValueConstants.LINE_ENABLE_FLG.VALID.getVal());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, paramLine);
        if (lineList != null && !lineList.isEmpty()) {
            // ソート
            lineList.sort((LineListDetailResultData r1, LineListDetailResultData r2) -> {
                Integer l1 = r1.getLineNo().equals(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal()) ? 0
                        : r1.getLineNo().equals(ApiGenericTypeConstants.LINE_TARGET.ETC.getVal()) ? Integer.MAX_VALUE
                                : Integer.parseInt(r1.getLineNo());
                Integer l2 = r2.getLineNo().equals(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal()) ? 0
                        : r2.getLineNo().equals(ApiGenericTypeConstants.LINE_TARGET.ETC.getVal()) ? Integer.MAX_VALUE
                                : Integer.parseInt(r2.getLineNo());
                return l1.compareTo(l2);
            });
            for (LineListDetailResultData line : lineList) {
                BuildingLineTimeStandardListDetailResultData detail = new BuildingLineTimeStandardListDetailResultData();
                detail.setCorpId(line.getCorpId());
                detail.setLineGroupId(line.getLineGroupId());
                detail.setLineNo(line.getLineNo());
                detail.setLineName(line.getLineName());
                //建物系統時限標準値情報を取得する
                BuildingLineTimeStandardListTimeResultData paramStandard = new BuildingLineTimeStandardListTimeResultData();
                paramStandard.setCorpId(parameter.getOperationCorpId());
                paramStandard.setBuildingId(parameter.getBuildingId());
                paramStandard.setLineGroupId(parameter.getLineGroupId());
                paramStandard.setLineNo(line.getLineNo());
                List<BuildingLineTimeStandardListTimeResultData> standardList = getResultList(
                        buildingLineTimeStandardListServiceDaoImpl, paramStandard);
                if (standardList == null || standardList.isEmpty()) {
                    detail.setTimeList(new ArrayList<>());
                } else {
                    List<BuildingLineTimeStandardListTimeResultData> detailTimeList = new ArrayList<>();
                    for (BuildingLineTimeStandardListTimeResultData standard : standardList) {
                        BuildingLineTimeStandardListTimeResultData detailTime = new BuildingLineTimeStandardListTimeResultData();
                        detailTime.setCorpId(standard.getCorpId());
                        detailTime.setBuildingId(standard.getBuildingId());
                        detailTime.setLineGroupId(standard.getLineGroupId());
                        detailTime.setLineNo(standard.getLineNo());
                        detailTime.setJigenNo(standard.getJigenNo());
                        detailTime.setLineLimitStandardKw(standard.getLineLimitStandardKw());
                        detailTime.setLineLowerStandardKw(standard.getLineLowerStandardKw());
                        detailTime.setDelFlg(standard.getDelFlg());
                        detailTime.setVersion(standard.getVersion());
                        detailTimeList.add(detailTime);
                    }
                    detail.setTimeList(detailTimeList);
                }
                detailList.add(detail);
            }
        }

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setLineGroupId(parameter.getLineGroupId());
        result.setDetailList(detailList);
        return result;
    }

}
