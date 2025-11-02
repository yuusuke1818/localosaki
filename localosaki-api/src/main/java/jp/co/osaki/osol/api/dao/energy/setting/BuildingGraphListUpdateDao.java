package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingGraphListUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingGraphListUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingGraphListUpdateResult;
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
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.entity.MGraph;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.MGraphPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物グラフ設定一覧更新 Daoクラス
 * @author t_hirata
 */
@Stateless
public class BuildingGraphListUpdateDao extends OsolApiDao<BuildingGraphListUpdateParameter> {

    //TODO EntityServiceDaoを使わないようにする
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MGraphServiceDaoImpl mGraphServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final DemandGraphSettingListServiceDaoImpl demandgraphSettingListServiceDaoImpl;
    private final DemandGraphElementListServiceDaoImpl demandGraphElementListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final MBuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    public BuildingGraphListUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mGraphServiceDaoImpl = new MGraphServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        demandgraphSettingListServiceDaoImpl = new DemandGraphSettingListServiceDaoImpl();
        demandGraphElementListServiceDaoImpl = new DemandGraphElementListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        buildingSmPointServiceDaoImpl = new MBuildingSmPointServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingGraphListUpdateResult query(BuildingGraphListUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingGraphListUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingGraphListUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingGraphListUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //グラフ情報の更新を行う
        updateGraphList(resultSet, serverDateTime, loginUserId);

        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        //更新後のデータを取得する
        return getNewGraphData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingGraphListUpdateRequest result) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(result.getCorpId());
        pkBuildingParam.setBuildingId(result.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(result.getBuildingVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exBuilding;
        }
    }

    /**
     * グラフ情報の更新を行う
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateGraphList(BuildingGraphListUpdateRequest result, Timestamp serverDateTime, Long loginUserId)
            throws Exception {
        MGraph paramGraph;
        MGraph updateGraph = null;
        MGraphPK pkParamGraph;
        MGraphPK pkUpdateGraph;
        MGraphElement paramElement;
        MGraphElement updateElement = null;
        MGraphElementPK pkParamElement;
        MGraphElementPK pkUpdateElement;
        Boolean newGraphFlg;
        Boolean newElementFlg;
        Long graphId;
        Long graphElementId;

        for (BuildingGraphListDetailResultData detail : result.getGraphList()) {
            //グラフ設定の更新

            if (detail.getGraphId() != null) {
                paramGraph = new MGraph();
                pkParamGraph = new MGraphPK();
                pkParamGraph.setCorpId(result.getCorpId());
                pkParamGraph.setBuildingId(result.getBuildingId());
                pkParamGraph.setLineGroupId(result.getLineGroupId());
                pkParamGraph.setGraphId(detail.getGraphId());
                paramGraph.setId(pkParamGraph);
                updateGraph = find(mGraphServiceDaoImpl, paramGraph);
                if (updateGraph == null) {
                    newGraphFlg = Boolean.TRUE;
                } else {
                    newGraphFlg = Boolean.FALSE;
                }
            } else {
                newGraphFlg = Boolean.TRUE;
            }

            if (newGraphFlg) {
                if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                    //削除フラグがあり、新規の場合は次のレコードへ
                    continue;
                }
                graphId = super.createId(OsolConstants.ID_SEQUENCE_NAME.GRAPH_ID.getVal());
                updateGraph = new MGraph();
                pkUpdateGraph = new MGraphPK();
                pkUpdateGraph.setCorpId(result.getCorpId());
                pkUpdateGraph.setBuildingId(result.getBuildingId());
                pkUpdateGraph.setLineGroupId(result.getLineGroupId());
                pkUpdateGraph.setGraphId(graphId);
                updateGraph.setId(pkUpdateGraph);
                updateGraph.setCreateDate(serverDateTime);
                updateGraph.setCreateUserId(loginUserId);
            } else {
                graphId = detail.getGraphId();
            }

            updateGraph.setGraphName(detail.getGraphName());
            updateGraph.setInitialViewFlg(detail.getInitialViewFlg());
            updateGraph.setDelFlg(detail.getDelFlg());
            updateGraph.setUpdateDate(serverDateTime);
            updateGraph.setUpdateUserId(loginUserId);

            if (newGraphFlg) {
                persist(mGraphServiceDaoImpl, updateGraph);
            } else {
                merge(mGraphServiceDaoImpl, updateGraph);
            }

            //グラフ要素設定の更新（系統）
            if (detail.getLineElementList() != null && !detail.getLineElementList().isEmpty()) {
                for (BuildingGraphListElementResultData element : detail.getLineElementList()) {
                    if (detail.getGraphId() != null && element.getGraphElementId() != null) {
                        paramElement = new MGraphElement();
                        pkParamElement = new MGraphElementPK();
                        pkParamElement.setCorpId(result.getCorpId());
                        pkParamElement.setBuildingId(result.getBuildingId());
                        pkParamElement.setLineGroupId(result.getLineGroupId());
                        pkParamElement.setGraphId(graphId);
                        pkParamElement.setGraphElementId(element.getGraphElementId());
                        paramElement.setId(pkParamElement);
                        updateElement = find(mGraphElementServiceDaoImpl, paramElement);
                        if (updateElement == null) {
                            newElementFlg = Boolean.TRUE;
                        } else {
                            newElementFlg = Boolean.FALSE;
                        }
                    } else {
                        newElementFlg = Boolean.TRUE;
                    }

                    if (newElementFlg) {
                        if (OsolConstants.FLG_ON.equals(detail.getDelFlg())
                                || OsolConstants.FLG_ON.equals(element.getDelFlg())) {
                            //削除フラグがあり、新規の場合は次のレコードへ
                            continue;
                        }
                        graphElementId = super.createId(OsolConstants.ID_SEQUENCE_NAME.GRAPH_ELEMENT_ID.getVal());
                        updateElement = new MGraphElement();
                        pkUpdateElement = new MGraphElementPK();
                        pkUpdateElement.setCorpId(result.getCorpId());
                        pkUpdateElement.setBuildingId(result.getBuildingId());
                        pkUpdateElement.setLineGroupId(result.getLineGroupId());
                        pkUpdateElement.setGraphId(graphId);
                        pkUpdateElement.setGraphElementId(graphElementId);
                        updateElement.setId(pkUpdateElement);
                        updateElement.setCreateDate(serverDateTime);
                        updateElement.setCreateUserId(loginUserId);

                    } else {
                        graphElementId = element.getGraphElementId();
                    }

                    updateElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
                    updateElement.setGraphLineGroupId(element.getGraphLineGroupId());
                    updateElement.setGraphLineNo(element.getGraphLineNo());
                    updateElement.setGraphSmId(null);
                    updateElement.setGraphPointNo(null);
                    updateElement.setGraphColorCode(element.getGraphColorCode());
                    updateElement.setDisplayOrder(element.getDisplayOrder());
                    if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                        //グラフ設定が削除の場合は、削除する
                        updateElement.setDelFlg(OsolConstants.FLG_ON);
                    } else {
                        //上記以外は要素の設定に従う
                        updateElement.setDelFlg(element.getDelFlg());
                    }
                    updateElement.setUpdateDate(serverDateTime);
                    updateElement.setUpdateUserId(loginUserId);

                    if (newElementFlg) {
                        persist(mGraphElementServiceDaoImpl, updateElement);
                    } else {
                        merge(mGraphElementServiceDaoImpl, updateElement);
                    }
                }
            }

            //グラフ要素設定の更新（アナログ・外気温）
            if (detail.getAnalogElementList() != null && !detail.getAnalogElementList().isEmpty()) {
                for (BuildingGraphListElementResultData element : detail.getAnalogElementList()) {
                    if (element.getGraphElementId() != null) {
                        paramElement = new MGraphElement();
                        pkParamElement = new MGraphElementPK();
                        pkParamElement.setCorpId(result.getCorpId());
                        pkParamElement.setBuildingId(result.getBuildingId());
                        pkParamElement.setLineGroupId(result.getLineGroupId());
                        pkParamElement.setGraphId(graphId);
                        pkParamElement.setGraphElementId(element.getGraphElementId());
                        paramElement.setId(pkParamElement);
                        updateElement = find(mGraphElementServiceDaoImpl, paramElement);
                        if (updateElement == null) {
                            newElementFlg = Boolean.TRUE;
                        } else {
                            newElementFlg = Boolean.FALSE;
                        }
                    } else {
                        newElementFlg = Boolean.TRUE;
                    }

                    if (newElementFlg) {
                        if (OsolConstants.FLG_ON.equals(detail.getDelFlg())
                                || OsolConstants.FLG_ON.equals(element.getDelFlg())) {
                            //削除フラグがあり、新規の場合は次のレコードへ
                            continue;
                        }
                        graphElementId = super.createId(OsolConstants.ID_SEQUENCE_NAME.GRAPH_ELEMENT_ID.getVal());
                        updateElement = new MGraphElement();
                        pkUpdateElement = new MGraphElementPK();
                        pkUpdateElement.setCorpId(result.getCorpId());
                        pkUpdateElement.setBuildingId(result.getBuildingId());
                        pkUpdateElement.setLineGroupId(result.getLineGroupId());
                        pkUpdateElement.setGraphId(graphId);
                        pkUpdateElement.setGraphElementId(graphElementId);
                        updateElement.setId(pkUpdateElement);
                        updateElement.setCreateDate(serverDateTime);
                        updateElement.setCreateUserId(loginUserId);

                    } else {
                        graphElementId = element.getGraphElementId();
                    }

                    if (element.getGraphSmId() == null) {
                        //機器IDが設定されていない場合は外気温とする
                        updateElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.AMEDAS.getVal());
                    } else {
                        //設定されている場合はアナログ
                        updateElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.ANALOG.getVal());
                    }
                    updateElement.setGraphLineGroupId(null);
                    updateElement.setGraphLineNo(null);
                    if (element.getGraphSmId() == null) {
                        //外気温
                        updateElement.setGraphSmId(null);
                        updateElement.setGraphPointNo(null);
                    } else {
                        //アナログ
                        updateElement.setGraphSmId(element.getGraphSmId());
                        updateElement.setGraphPointNo(element.getGraphPointNo());
                    }
                    updateElement.setGraphColorCode(element.getGraphColorCode());
                    updateElement.setDisplayOrder(element.getDisplayOrder());
                    if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                        //グラフ設定が削除の場合は、削除する
                        updateElement.setDelFlg(OsolConstants.FLG_ON);
                    } else {
                        //上記以外は要素の設定に従う
                        updateElement.setDelFlg(element.getDelFlg());
                    }
                    updateElement.setUpdateDate(serverDateTime);
                    updateElement.setUpdateUserId(loginUserId);

                    if (newElementFlg) {
                        persist(mGraphElementServiceDaoImpl, updateElement);
                    } else {
                        merge(mGraphElementServiceDaoImpl, updateElement);
                    }
                }
            }

        }
    }

    /**
     * 更新後のグラフ情報を取得する
     * @param result
     * @return
     */
    private BuildingGraphListUpdateResult getNewGraphData(BuildingGraphListUpdateRequest result) throws Exception {
        BuildingGraphListUpdateResult newResult = new BuildingGraphListUpdateResult();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingGraphListUpdateResult();
        }

        List<BuildingGraphListDetailResultData> graphList = new ArrayList<>();

        // グラフ設定取得
        DemandGraphSettingListDetailResultData settingParam = new DemandGraphSettingListDetailResultData();
        settingParam.setCorpId(result.getCorpId());
        settingParam.setBuildingId(result.getBuildingId());
        settingParam.setLineGroupId(result.getLineGroupId());
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
                elementParam.setCorpId(result.getCorpId());
                elementParam.setBuildingId(result.getBuildingId());
                elementParam.setLineGroupId(result.getLineGroupId());
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
                            lineParam.setCorpId(result.getCorpId());
                            lineParam.setLineGroupId(result.getLineGroupId());
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
                            id.setCorpId(result.getCorpId());
                            id.setBuildingId(result.getBuildingId());
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

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setLineGroupId(result.getLineGroupId());
        newResult.setGraphList(graphList);

        return newResult;
    }

}
