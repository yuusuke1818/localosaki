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
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingGraphListParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingGraphListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingGraphListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingGraphListElementResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphSettingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphElementListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandGraphSettingListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物グラフ設定一覧取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingGraphListDao extends OsolApiDao<BuildingGraphListParameter> {

    //TODO EntityServiceDaoを使わないようにする
    private final DemandGraphSettingListServiceDaoImpl demandgraphSettingListServiceDaoImpl;
    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final MBuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingGraphListDao() {
        demandgraphSettingListServiceDaoImpl = new DemandGraphSettingListServiceDaoImpl();
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        buildingSmPointServiceDaoImpl = new MBuildingSmPointServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public BuildingGraphListResult query(BuildingGraphListParameter parameter) throws Exception {
        BuildingGraphListResult result = new BuildingGraphListResult();

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
            return new BuildingGraphListResult();
        }

        List<BuildingGraphListDetailResultData> graphList = new ArrayList<>();

        // グラフ設定取得
        DemandGraphSettingListDetailResultData settingParam = new DemandGraphSettingListDetailResultData();
        settingParam.setCorpId(parameter.getOperationCorpId());
        settingParam.setBuildingId(parameter.getBuildingId());
        settingParam.setLineGroupId(parameter.getLineGroupId());
        List<DemandGraphSettingListDetailResultData> settingList = getResultList(demandgraphSettingListServiceDaoImpl,
                settingParam);
        if (settingList != null) {
            for (DemandGraphSettingListDetailResultData setting : settingList) {
                BuildingGraphListDetailResultData detail = new BuildingGraphListDetailResultData();
                detail.setGraphId(setting.getGraphId());
                detail.setGraphName(setting.getGraphName());
                detail.setInitialViewFlg(setting.getInitialViewFlg());
                detail.setDelFlg(setting.getDelFlg());
                detail.setVersion(setting.getVersion());

                // グラフ要素
                List<BuildingGraphListElementResultData> lineElementList = new ArrayList<>();
                List<BuildingGraphListElementResultData> analogElementList = new ArrayList<>();
                DemandGraphElementListDetailResultData elementParam = new DemandGraphElementListDetailResultData();
                elementParam.setCorpId(parameter.getOperationCorpId());
                elementParam.setBuildingId(parameter.getBuildingId());
                elementParam.setLineGroupId(parameter.getLineGroupId());
                elementParam.setGraphId(setting.getGraphId());
                List<DemandGraphElementListDetailResultData> graphElementList = getResultList(
                        demandGraphElementListServiceDaoImpl, elementParam);
                if (graphElementList != null) {
                    for (DemandGraphElementListDetailResultData element : graphElementList) {
                        BuildingGraphListElementResultData elementDetail = new BuildingGraphListElementResultData();
                        elementDetail.setGraphElementId(element.getGraphElementId());
                        elementDetail.setGraphColorCode(element.getGraphColorCode());
                        elementDetail.setDisplayOrder(element.getDisplayOrder());
                        elementDetail.setDelFlg(element.getDelFlg());
                        elementDetail.setVersion(element.getVersion());
                        if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.DEMAND.getVal()
                                .equals(element.getGraphElementType())) {
                            // デマンド
                            elementDetail.setGraphLineGroupId(element.getGraphLineGroupId());
                            elementDetail.setGraphLineNo(element.getGraphLineNo());

                            // 系統名
                            LineListDetailResultData lineParam = new LineListDetailResultData();
                            lineParam.setCorpId(parameter.getOperationCorpId());
                            lineParam.setLineGroupId(parameter.getLineGroupId());
                            lineParam.setLineNo(element.getGraphLineNo());
                            List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, lineParam);
                            if (lineList != null && lineList.size() > 0) {
                                elementDetail.setGraphLineName(lineList.get(0).getLineName());
                            }

                            lineElementList.add(elementDetail);
                        } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.ANALOG.getVal()
                                .equals(element.getGraphElementType())) {
                            // アナログ
                            elementDetail.setGraphSmId(element.getGraphSmId());
                            elementDetail.setGraphPointNo(element.getGraphPointNo());

                            MBuildingSmPoint entity = new MBuildingSmPoint();
                            MBuildingSmPointPK id = new MBuildingSmPointPK();
                            id.setCorpId(parameter.getOperationCorpId());
                            id.setBuildingId(parameter.getBuildingId());
                            id.setSmId(element.getGraphSmId());
                            id.setPointNo(element.getGraphPointNo());
                            entity.setId(id);
                            MBuildingSmPoint buildingSmPoint = find(buildingSmPointServiceDaoImpl, entity);
                            if (buildingSmPoint != null) {
                                elementDetail.setGraphPointName(buildingSmPoint.getPointName());
                            }

                            elementDetail.setTempFlg(false);
                            analogElementList.add(elementDetail);
                        } else if (ApiCodeValueConstants.GRAPH_VALUE_TYPE.AMEDAS.getVal()
                                .equals(element.getGraphElementType())) {
                            // アメダス
                            elementDetail.setTempFlg(true);
                            elementDetail.setGraphPointName(ApiSimpleConstants.OUT_TEMPERATURE);
                            analogElementList.add(elementDetail);
                        }
                    }
                }
                // 並び替え
                lineElementList
                        .sort((BuildingGraphListElementResultData b1, BuildingGraphListElementResultData b2) -> (b1
                                .getDisplayOrder().compareTo(b2.getDisplayOrder())));
                analogElementList
                        .sort((BuildingGraphListElementResultData b1, BuildingGraphListElementResultData b2) -> (b1
                                .getDisplayOrder().compareTo(b2.getDisplayOrder())));
                // グラフ設定に格納
                detail.setLineElementList(lineElementList);
                detail.setAnalogElementList(analogElementList);
                // グラフリスト
                graphList.add(detail);
            }
        }

        graphList.sort((BuildingGraphListDetailResultData g1,
                BuildingGraphListDetailResultData g2) -> (g1.getGraphId().compareTo(g2.getGraphId())));

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setLineGroupId(parameter.getLineGroupId());
        result.setGraphList(graphList);

        return result;
    }

}
