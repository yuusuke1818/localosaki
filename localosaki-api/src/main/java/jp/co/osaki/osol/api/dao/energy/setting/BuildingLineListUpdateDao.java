package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineListUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingLineListUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineListUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineGroupListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MAggregateDmLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmTimeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTimeStandardsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTypeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MAggregateDmLine;
import jp.co.osaki.osol.entity.MAggregateDmLinePK;
import jp.co.osaki.osol.entity.MGraph;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.MGraphPK;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineGroup;
import jp.co.osaki.osol.entity.MLineGroupPK;
import jp.co.osaki.osol.entity.MLinePK;
import jp.co.osaki.osol.entity.MLineTargetAlarm;
import jp.co.osaki.osol.entity.MLineTargetAlarmPK;
import jp.co.osaki.osol.entity.MLineTargetAlarmTime;
import jp.co.osaki.osol.entity.MLineTargetAlarmTimePK;
import jp.co.osaki.osol.entity.MLineTimeStandard;
import jp.co.osaki.osol.entity.MLineTimeStandardPK;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物系統一覧更新 Daoクラス
 * @author t_hirata
 */
@Stateless
public class BuildingLineListUpdateDao extends OsolApiDao<BuildingLineListUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MLineGroupServiceDaoImpl mLineGroupServiceDaoImpl;
    private final MLineServiceDaoImpl mLineServiceDaoImpl;
    private final MLineTypeServiceDaoImpl mLineTypeServiceDaoImpl;
    private final MGraphServiceDaoImpl mGraphServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final MLineTimeStandardsServiceDaoImpl mLineTimeStandardsServiceDaoImpl;
    private final MLineTargetAlarmServiceDaoImpl mLineTargetAlarmServiceDaoImpl;
    private final MLineTargetAlarmTimeServiceDaoImpl mLineTargetAlarmTimeServiceDaoImpl;
    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final MAggregateDmLineServiceDaoImpl mAggregateDmLineServiceDaoImpl;
    private final AvailableEnergyLineListServiceDaoImpl availableEnergyLineListServiceDaoImpl;
    private final TAvailableEnergyServiceDaoImpl tAvailableEnergyServiceDaoImpl;


    public BuildingLineListUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mLineGroupServiceDaoImpl = new MLineGroupServiceDaoImpl();
        mLineServiceDaoImpl = new MLineServiceDaoImpl();
        mLineTypeServiceDaoImpl = new MLineTypeServiceDaoImpl();
        mGraphServiceDaoImpl = new MGraphServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        mLineTimeStandardsServiceDaoImpl = new MLineTimeStandardsServiceDaoImpl();
        mLineTargetAlarmServiceDaoImpl = new MLineTargetAlarmServiceDaoImpl();
        mLineTargetAlarmTimeServiceDaoImpl = new MLineTargetAlarmTimeServiceDaoImpl();
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        mAggregateDmLineServiceDaoImpl = new MAggregateDmLineServiceDaoImpl();
        availableEnergyLineListServiceDaoImpl = new AvailableEnergyLineListServiceDaoImpl();
        tAvailableEnergyServiceDaoImpl = new TAvailableEnergyServiceDaoImpl();
    }

    @Override
    public BuildingLineListUpdateResult query(BuildingLineListUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingLineListUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingLineListUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingLineListUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //系統情報の更新
        updateLineData(resultSet, serverDateTime, loginUserId);
        //系統グループ削除または無効になった系統に関連する情報を更新
        updateInvalidLineRelatedData(resultSet, serverDateTime, loginUserId);
        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        return getNewLineData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingLineListUpdateRequest result) throws Exception {
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
     * 系統情報の更新を行う
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateLineData(BuildingLineListUpdateRequest result, Timestamp serverDateTime, Long loginUserId)
            throws Exception {
        MLineGroup paramGroup;
        MLineGroup updateGroup = null;
        MLineGroupPK pkParamGroup;
        MLineGroupPK pkUpdateGroup;
        MLine paramLine;
        MLine updateLine = null;
        MLinePK pkParamLine;
        MLinePK pkUpdateLine = null;
        MLineType paramLineType;
        MLineType updateLineType;
        Boolean newGroupFlg;
        Boolean newLineFlg;
        Boolean noRegistFlg;
        Long lineGroupId;

        for (CorpLineListDetailResultData line : result.getDetail().getLineList()) {
            if (ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal().equals(line.getLineGroupType())) {
                //企業標準系統の場合は、次のレコードへ
                continue;
            }

            //系統グループの更新
            if (line.getLineGroupId() != null) {
                paramGroup = new MLineGroup();
                pkParamGroup = new MLineGroupPK();
                pkParamGroup.setCorpId(result.getCorpId());
                pkParamGroup.setLineGroupId(line.getLineGroupId());
                paramGroup.setId(pkParamGroup);
                updateGroup = find(mLineGroupServiceDaoImpl, paramGroup);
                if (updateGroup == null) {
                    newGroupFlg = Boolean.TRUE;
                } else {
                    newGroupFlg = Boolean.FALSE;
                }
            } else {
                newGroupFlg = Boolean.TRUE;
            }

            if (newGroupFlg) {
                if (OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                    //新規登録で削除の場合は次のレコードへ
                    continue;
                }
                lineGroupId = super.createId(OsolConstants.ID_SEQUENCE_NAME.LINE_GROUP_ID.getVal());
                updateGroup = new MLineGroup();
                pkUpdateGroup = new MLineGroupPK();
                pkUpdateGroup.setCorpId(result.getCorpId());
                pkUpdateGroup.setLineGroupId(lineGroupId);
                updateGroup.setId(pkUpdateGroup);
                updateGroup.setCreateDate(serverDateTime);
                updateGroup.setCreateUserId(loginUserId);
            } else {
                lineGroupId = line.getLineGroupId();
            }

            updateGroup.setLineGroupName(line.getLineGroupName());
            updateGroup.setLineGroupType(ApiGenericTypeConstants.LINE_GROUP_TYPE.INDIVIDUAL.getVal());
            updateGroup.setInitialViewFlg(line.getInitialViewFlg());
            updateGroup.setBuildingId(result.getBuildingId());
            updateGroup.setDelFlg(line.getDelFlg());
            updateGroup.setUpdateDate(serverDateTime);
            updateGroup.setUpdateUserId(loginUserId);

            if (newGroupFlg) {
                persist(mLineGroupServiceDaoImpl, updateGroup);
            } else {
                merge(mLineGroupServiceDaoImpl, updateGroup);
            }

            //系統（全体）の更新
            if (line.getLineAll() != null) {
                if (line.getLineGroupId() != null) {
                    paramLine = new MLine();
                    pkParamLine = new MLinePK();
                    pkParamLine.setCorpId(result.getCorpId());
                    pkParamLine.setLineGroupId(lineGroupId);
                    pkParamLine.setLineNo(line.getLineAll().getLineNo());
                    paramLine.setId(pkParamLine);
                    updateLine = find(mLineServiceDaoImpl, paramLine);
                    if (updateLine == null) {
                        newLineFlg = Boolean.TRUE;
                    } else {
                        newLineFlg = Boolean.FALSE;
                    }
                } else {
                    newLineFlg = Boolean.TRUE;
                }

                if (newLineFlg) {
                    if (OsolConstants.FLG_ON.equals(line.getLineAll().getDelFlg())
                            || OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                        //新規かつ削除の場合は処理を行わない
                        noRegistFlg = Boolean.TRUE;
                    } else {
                        noRegistFlg = Boolean.FALSE;
                        updateLine = new MLine();
                        pkUpdateLine = new MLinePK();
                        pkUpdateLine.setCorpId(result.getCorpId());
                        pkUpdateLine.setLineGroupId(lineGroupId);
                        pkUpdateLine.setLineNo(line.getLineAll().getLineNo());
                        updateLine.setId(pkUpdateLine);
                        updateLine.setCreateDate(serverDateTime);
                        updateLine.setCreateUserId(loginUserId);
                    }
                } else {
                    noRegistFlg = Boolean.FALSE;
                }

                if (!noRegistFlg) {
                    //系統種別情報を取得
                    paramLineType = new MLineType();
                    paramLineType.setLineType(line.getLineAll().getLineType());
                    updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);
                    updateLine.setLineName(line.getLineAll().getLineName());
                    updateLine.setLineUnit(line.getLineAll().getLineUnit());
                    updateLine.setLineTarget(line.getLineAll().getLineTarget());
                    updateLine.setMLineType(updateLineType);
                    updateLine.setLineEnableFlg(line.getLineAll().getLineEnableFlg());
                    updateLine.setInputEnableFlg(OsolConstants.FLG_OFF);
                    if (OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                        //系統グループが削除の場合は削除
                        updateLine.setDelFlg(OsolConstants.FLG_ON);
                    } else {
                        updateLine.setDelFlg(line.getLineAll().getDelFlg());
                    }
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);
                    if (newLineFlg) {
                        persist(mLineServiceDaoImpl, updateLine);
                    } else {
                        merge(mLineServiceDaoImpl, updateLine);
                    }
                }
            }

            //系統（その他）の更新
            if (line.getLineEtc() != null) {
                if (line.getLineGroupId() != null) {
                    paramLine = new MLine();
                    pkParamLine = new MLinePK();
                    pkParamLine.setCorpId(result.getCorpId());
                    pkParamLine.setLineGroupId(lineGroupId);
                    pkParamLine.setLineNo(line.getLineEtc().getLineNo());
                    paramLine.setId(pkParamLine);
                    updateLine = find(mLineServiceDaoImpl, paramLine);
                    if (updateLine == null) {
                        newLineFlg = Boolean.TRUE;
                    } else {
                        newLineFlg = Boolean.FALSE;
                    }
                } else {
                    newLineFlg = Boolean.TRUE;
                }

                if (newLineFlg) {
                    if (OsolConstants.FLG_ON.equals(line.getLineEtc().getDelFlg())
                            || OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                        //新規かつ削除の場合は処理を行わない
                        noRegistFlg = Boolean.TRUE;
                    } else {
                        noRegistFlg = Boolean.FALSE;
                        updateLine = new MLine();
                        pkUpdateLine = new MLinePK();
                        pkUpdateLine.setCorpId(result.getCorpId());
                        pkUpdateLine.setLineGroupId(lineGroupId);
                        pkUpdateLine.setLineNo(line.getLineEtc().getLineNo());
                        updateLine.setId(pkUpdateLine);
                        updateLine.setCreateDate(serverDateTime);
                        updateLine.setCreateUserId(loginUserId);
                    }
                } else {
                    noRegistFlg = Boolean.FALSE;
                }

                if (!noRegistFlg) {
                    //系統種別情報を取得
                    paramLineType = new MLineType();
                    paramLineType.setLineType(line.getLineEtc().getLineType());
                    updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);
                    updateLine.setLineName(line.getLineEtc().getLineName());
                    updateLine.setLineUnit(line.getLineEtc().getLineUnit());
                    updateLine.setLineTarget(line.getLineEtc().getLineTarget());
                    updateLine.setMLineType(updateLineType);
                    updateLine.setLineEnableFlg(line.getLineEtc().getLineEnableFlg());
                    updateLine.setInputEnableFlg(OsolConstants.FLG_OFF);
                    if (OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                        //系統グループが削除の場合は削除
                        updateLine.setDelFlg(OsolConstants.FLG_ON);
                    } else {
                        updateLine.setDelFlg(line.getLineEtc().getDelFlg());
                    }
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);
                    if (newLineFlg) {
                        persist(mLineServiceDaoImpl, updateLine);
                    } else {
                        merge(mLineServiceDaoImpl, updateLine);
                    }
                }
            }

            //系統（上記以外）の更新
            if (line.getLineList() != null && !line.getLineList().isEmpty()) {
                for (LineListDetailResultData detailLine : line.getLineList()) {
                    if (line.getLineGroupId() != null) {
                        paramLine = new MLine();
                        pkParamLine = new MLinePK();
                        pkParamLine.setCorpId(result.getCorpId());
                        pkParamLine.setLineGroupId(lineGroupId);
                        pkParamLine.setLineNo(detailLine.getLineNo());
                        paramLine.setId(pkParamLine);
                        updateLine = find(mLineServiceDaoImpl, paramLine);
                        if (updateLine == null) {
                            newLineFlg = Boolean.TRUE;
                        } else {
                            newLineFlg = Boolean.FALSE;
                        }
                    } else {
                        newLineFlg = Boolean.TRUE;
                    }

                    if (newLineFlg) {
                        if (OsolConstants.FLG_ON.equals(detailLine.getDelFlg())
                                || OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                            //新規かつ削除の場合は処理を行わない
                            noRegistFlg = Boolean.TRUE;
                        } else {
                            noRegistFlg = Boolean.FALSE;
                            updateLine = new MLine();
                            pkUpdateLine = new MLinePK();
                            pkUpdateLine.setCorpId(result.getCorpId());
                            pkUpdateLine.setLineGroupId(lineGroupId);
                            pkUpdateLine.setLineNo(detailLine.getLineNo());
                            updateLine.setId(pkUpdateLine);
                            updateLine.setCreateDate(serverDateTime);
                            updateLine.setCreateUserId(loginUserId);
                        }
                    } else {
                        noRegistFlg = Boolean.FALSE;
                    }

                    if (!noRegistFlg) {
                        //系統種別情報を取得
                        paramLineType = new MLineType();
                        paramLineType.setLineType(detailLine.getLineType());
                        updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);
                        updateLine.setLineName(detailLine.getLineName());
                        updateLine.setLineUnit(detailLine.getLineUnit());
                        updateLine.setLineTarget(detailLine.getLineTarget());
                        updateLine.setMLineType(updateLineType);
                        updateLine.setLineEnableFlg(detailLine.getLineEnableFlg());
                        updateLine.setInputEnableFlg(detailLine.getInputEnableFlg());
                        if (OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                            //系統グループが削除の場合は削除
                            updateLine.setDelFlg(OsolConstants.FLG_ON);
                        } else {
                            updateLine.setDelFlg(detailLine.getDelFlg());
                        }
                        updateLine.setUpdateDate(serverDateTime);
                        updateLine.setUpdateUserId(loginUserId);
                        if (newLineFlg) {
                            persist(mLineServiceDaoImpl, updateLine);
                        } else {
                            merge(mLineServiceDaoImpl, updateLine);
                        }
                    }
                }
            }

        }
    }

    /**
     * 系統グループ削除または無効となった系統に関連するデータの更新を行う
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateInvalidLineRelatedData(BuildingLineListUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) {

        TAvailableEnergy paramEnergy;
        TAvailableEnergyPK pkParamEnergy;
        MAggregateDmLine paramLine;
        MGraph paramGraph;
        MGraphElement paramGraphElement;
        MLineTargetAlarm paramTarget;
        MLineTargetAlarmTime paramTargetTime;
        MLineTimeStandard paramTime;
        List<MAggregateDmLine> updateLineList;
        List<MGraph> updateGraphList;
        List<MGraphElement> updateGraphElementList;
        List<MLineTargetAlarm> updateTargetList;
        List<MLineTargetAlarmTime> updateTargetTimeList;
        List<MLineTimeStandard> updateTimeList;
        List<AvailableEnergyLineListResultData> energyResultList;
        MAggregateDmLinePK pkParamLine;
        MGraphPK pkParamGraph;
        MGraphElementPK pkParamGraphElement;
        MLineTargetAlarmPK pkParamTarget;
        MLineTargetAlarmTimePK pkParamTargetTime;
        MLineTimeStandardPK pkParamTime;

        for (CorpLineListDetailResultData line : result.getDetail().getLineList()) {
            if (ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal().equals(line.getLineGroupType())) {
                //企業標準系統の場合は、次のレコードへ
                continue;
            }

            if (line.getLineGroupId() != null) {
                if (OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                    //系統グループ削除の場合
                    //使用エネルギーのフラグ更新（エネルギー自動反映対応）
                    AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
                    param.setCorpId(result.getCorpId());
                    param.setBuildingId(result.getBuildingId());
                    param.setLineGroupId(line.getLineGroupId());
                    energyResultList = getResultList(availableEnergyLineListServiceDaoImpl, param);
                    if(energyResultList != null && !energyResultList.isEmpty()) {
                        for(AvailableEnergyLineListResultData energyLine : energyResultList) {
                            paramEnergy = new TAvailableEnergy();
                            pkParamEnergy = new TAvailableEnergyPK();
                            pkParamEnergy.setCorpId(energyLine.getCorpId());
                            pkParamEnergy.setBuildingId(energyLine.getBuildingId());
                            pkParamEnergy.setEngTypeCd(energyLine.getEngTypeCd());
                            pkParamEnergy.setContractId(energyLine.getContractId());
                            pkParamEnergy.setEngId(energyLine.getEngId());
                            paramEnergy.setId(pkParamEnergy);

                            List<TAvailableEnergy> targetEnergyList = getResultList(tAvailableEnergyServiceDaoImpl,paramEnergy);
                            targetEnergyList = targetEnergyList.stream().filter(o -> o.getEnergyUseLineValueFlg()
                                    .equals(OsolConstants.FLG_ON)).collect(Collectors.toList());
                            if(targetEnergyList != null && !targetEnergyList.isEmpty()) {
                                for(TAvailableEnergy targetEnergy : targetEnergyList) {
                                    targetEnergy.setEnergyUseLineValueFlg(OsolConstants.FLG_OFF);
                                    merge(tAvailableEnergyServiceDaoImpl,targetEnergy);
                                }

                            }

                        }

                    }

                    //集計デマンド系統情報を削除する
                    paramLine = new MAggregateDmLine();
                    pkParamLine = new MAggregateDmLinePK();
                    pkParamLine.setCorpId(result.getCorpId());
                    pkParamLine.setLineGroupId(line.getLineGroupId());
                    paramLine.setId(pkParamLine);
                    updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
                    if (updateLineList != null && !updateLineList.isEmpty()) {
                        for (MAggregateDmLine aggregateLine : updateLineList) {
                            remove(mAggregateDmLineServiceDaoImpl, aggregateLine);
                        }
                    }

                    //グラフ設定の削除
                    paramGraph = new MGraph();
                    pkParamGraph = new MGraphPK();
                    pkParamGraph.setCorpId(result.getCorpId());
                    pkParamGraph.setLineGroupId(line.getLineGroupId());
                    paramGraph.setId(pkParamGraph);
                    paramGraph.setDelFlg(OsolConstants.FLG_OFF);
                    updateGraphList = getResultList(mGraphServiceDaoImpl, paramGraph);
                    if (updateGraphList != null && !updateGraphList.isEmpty()) {
                        for (MGraph graph : updateGraphList) {
                            graph.setDelFlg(OsolConstants.FLG_ON);
                            graph.setUpdateDate(serverDateTime);
                            graph.setUpdateUserId(loginUserId);
                            merge(mGraphServiceDaoImpl, graph);
                        }
                    }

                    //グラフ要素設定の削除
                    paramGraphElement = new MGraphElement();
                    pkParamGraphElement = new MGraphElementPK();
                    pkParamGraphElement.setCorpId(result.getCorpId());
                    pkParamGraphElement.setLineGroupId(line.getLineGroupId());
                    paramGraphElement.setId(pkParamGraphElement);
                    paramGraphElement.setDelFlg(OsolConstants.FLG_OFF);
                    updateGraphElementList = getResultList(mGraphElementServiceDaoImpl, paramGraphElement);
                    if (updateGraphElementList != null && !updateGraphElementList.isEmpty()) {
                        for (MGraphElement graphElement : updateGraphElementList) {
                            graphElement.setDelFlg(OsolConstants.FLG_ON);
                            graphElement.setUpdateDate(serverDateTime);
                            graphElement.setUpdateUserId(loginUserId);
                            merge(mGraphElementServiceDaoImpl, graphElement);
                        }
                    }

                    //建物系統目標超過警報の削除
                    paramTarget = new MLineTargetAlarm();
                    pkParamTarget = new MLineTargetAlarmPK();
                    pkParamTarget.setCorpId(result.getCorpId());
                    pkParamTarget.setLineGroupId(line.getLineGroupId());
                    paramTarget.setId(pkParamTarget);
                    paramTarget.setDelFlg(OsolConstants.FLG_OFF);
                    updateTargetList = getResultList(mLineTargetAlarmServiceDaoImpl, paramTarget);
                    if (updateTargetList != null && !updateTargetList.isEmpty()) {
                        for (MLineTargetAlarm target : updateTargetList) {
                            target.setDelFlg(OsolConstants.FLG_ON);
                            target.setUpdateDate(serverDateTime);
                            target.setUpdateUserId(loginUserId);
                            merge(mLineTargetAlarmServiceDaoImpl, target);
                        }
                    }

                    //建物系統目標超過警報時間の削除
                    paramTargetTime = new MLineTargetAlarmTime();
                    pkParamTargetTime = new MLineTargetAlarmTimePK();
                    pkParamTargetTime.setCorpId(result.getCorpId());
                    pkParamTargetTime.setLineGroupId(line.getLineGroupId());
                    paramTargetTime.setId(pkParamTargetTime);
                    paramTargetTime.setDelFlg(OsolConstants.FLG_OFF);
                    updateTargetTimeList = getResultList(mLineTargetAlarmTimeServiceDaoImpl, paramTargetTime);
                    if (updateTargetTimeList != null && !updateTargetTimeList.isEmpty()) {
                        for (MLineTargetAlarmTime targetAlarm : updateTargetTimeList) {
                            targetAlarm.setDelFlg(OsolConstants.FLG_ON);
                            targetAlarm.setUpdateDate(serverDateTime);
                            targetAlarm.setUpdateUserId(loginUserId);
                            merge(mLineTargetAlarmTimeServiceDaoImpl, targetAlarm);
                        }
                    }

                } else {
                    //削除以外の場合、個別の系統を確認し、無効な系統に対するデータの更新を行う
                    //全体
                    if (line.getLineAll() != null && ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal()
                            .equals(line.getLineAll().getLineEnableFlg())) {
                        //使用エネルギーのフラグ更新（エネルギー自動反映対応）
                        AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
                        param.setCorpId(result.getCorpId());
                        param.setBuildingId(result.getBuildingId());
                        param.setLineGroupId(line.getLineGroupId());
                        param.setLineNo(line.getLineAll().getLineNo());
                        energyResultList = getResultList(availableEnergyLineListServiceDaoImpl, param);
                        if(energyResultList != null && !energyResultList.isEmpty()) {
                            for(AvailableEnergyLineListResultData energyLine : energyResultList) {
                                paramEnergy = new TAvailableEnergy();
                                pkParamEnergy = new TAvailableEnergyPK();
                                pkParamEnergy.setCorpId(energyLine.getCorpId());
                                pkParamEnergy.setBuildingId(energyLine.getBuildingId());
                                pkParamEnergy.setEngTypeCd(energyLine.getEngTypeCd());
                                pkParamEnergy.setContractId(energyLine.getContractId());
                                pkParamEnergy.setEngId(energyLine.getEngId());
                                paramEnergy.setId(pkParamEnergy);

                                List<TAvailableEnergy> targetEnergyList = getResultList(tAvailableEnergyServiceDaoImpl,paramEnergy);
                                targetEnergyList = targetEnergyList.stream().filter(o -> o.getEnergyUseLineValueFlg()
                                        .equals(OsolConstants.FLG_ON)).collect(Collectors.toList());
                                if(targetEnergyList != null && !targetEnergyList.isEmpty()) {
                                    for(TAvailableEnergy targetEnergy : targetEnergyList) {
                                        targetEnergy.setEnergyUseLineValueFlg(OsolConstants.FLG_OFF);
                                        merge(tAvailableEnergyServiceDaoImpl,targetEnergy);
                                    }

                                }

                            }

                        }

                        //集計デマンド系統情報を削除する
                        paramLine = new MAggregateDmLine();
                        pkParamLine = new MAggregateDmLinePK();
                        pkParamLine.setCorpId(result.getCorpId());
                        pkParamLine.setLineGroupId(line.getLineGroupId());
                        pkParamLine.setLineNo(line.getLineAll().getLineNo());
                        paramLine.setId(pkParamLine);
                        updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
                        if (updateLineList != null && !updateLineList.isEmpty()) {
                            for (MAggregateDmLine aggregateLine : updateLineList) {
                                remove(mAggregateDmLineServiceDaoImpl, aggregateLine);
                            }
                        }

                        //グラフ要素情報を削除する
                        paramGraphElement = new MGraphElement();
                        pkParamGraphElement = new MGraphElementPK();
                        pkParamGraphElement.setCorpId(result.getCorpId());
                        pkParamGraphElement.setLineGroupId(line.getLineGroupId());
                        paramGraphElement.setId(pkParamGraphElement);
                        paramGraphElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
                        paramGraphElement.setGraphLineGroupId(line.getLineGroupId());
                        paramGraphElement.setGraphLineNo(line.getLineAll().getLineNo());
                        paramGraphElement.setDelFlg(OsolConstants.FLG_OFF);
                        updateGraphElementList = getResultList(mGraphElementServiceDaoImpl, paramGraphElement);
                        if (updateGraphElementList != null && !updateGraphElementList.isEmpty()) {
                            for (MGraphElement element : updateGraphElementList) {
                                element.setDelFlg(OsolConstants.FLG_ON);
                                element.setUpdateDate(serverDateTime);
                                element.setUpdateUserId(loginUserId);
                                merge(mGraphElementServiceDaoImpl, element);
                            }
                        }

                        //建物系統目標超過系統情報を削除する
                        paramTarget = new MLineTargetAlarm();
                        pkParamTarget = new MLineTargetAlarmPK();
                        pkParamTarget.setCorpId(result.getCorpId());
                        pkParamTarget.setLineGroupId(line.getLineGroupId());
                        pkParamTarget.setLineNo(line.getLineAll().getLineNo());
                        paramTarget.setId(pkParamTarget);
                        paramTarget.setDelFlg(OsolConstants.FLG_OFF);
                        updateTargetList = getResultList(mLineTargetAlarmServiceDaoImpl, paramTarget);
                        if (updateTargetList != null && !updateTargetList.isEmpty()) {
                            for (MLineTargetAlarm target : updateTargetList) {
                                target.setDelFlg(OsolConstants.FLG_ON);
                                target.setUpdateDate(serverDateTime);
                                target.setUpdateUserId(loginUserId);
                                merge(mLineTargetAlarmServiceDaoImpl, target);
                            }
                        }

                        //建物系統目標超過系統時間情報を削除する
                        paramTargetTime = new MLineTargetAlarmTime();
                        pkParamTargetTime = new MLineTargetAlarmTimePK();
                        pkParamTargetTime.setCorpId(result.getCorpId());
                        pkParamTargetTime.setLineGroupId(line.getLineGroupId());
                        pkParamTargetTime.setLineNo(line.getLineAll().getLineNo());
                        paramTargetTime.setId(pkParamTargetTime);
                        paramTargetTime.setDelFlg(OsolConstants.FLG_OFF);
                        updateTargetTimeList = getResultList(mLineTargetAlarmTimeServiceDaoImpl, paramTargetTime);
                        if (updateTargetTimeList != null && !updateTargetTimeList.isEmpty()) {
                            for (MLineTargetAlarmTime targetTime : updateTargetTimeList) {
                                targetTime.setDelFlg(OsolConstants.FLG_ON);
                                targetTime.setUpdateDate(serverDateTime);
                                targetTime.setUpdateUserId(loginUserId);
                                merge(mLineTargetAlarmTimeServiceDaoImpl, targetTime);
                            }
                        }

                        //建物系統時限標準値情報を削除する
                        paramTime = new MLineTimeStandard();
                        pkParamTime = new MLineTimeStandardPK();
                        pkParamTime.setCorpId(result.getCorpId());
                        pkParamTime.setLineGroupId(line.getLineGroupId());
                        pkParamTime.setLineNo(line.getLineAll().getLineNo());
                        paramTime.setId(pkParamTime);
                        paramTime.setDelFlg(OsolConstants.FLG_OFF);
                        updateTimeList = getResultList(mLineTimeStandardsServiceDaoImpl, paramTime);
                        if (updateTimeList != null && !updateTimeList.isEmpty()) {
                            for (MLineTimeStandard lineTime : updateTimeList) {
                                lineTime.setDelFlg(OsolConstants.FLG_ON);
                                lineTime.setUpdateDate(serverDateTime);
                                lineTime.setUpdateUserId(loginUserId);
                                merge(mLineTimeStandardsServiceDaoImpl, lineTime);
                            }
                        }
                    }
                    //その他
                    if (line.getLineEtc() != null && ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal()
                            .equals(line.getLineEtc().getLineEnableFlg())) {

                        //使用エネルギーのフラグ更新（エネルギー自動反映対応）
                        AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
                        param.setCorpId(result.getCorpId());
                        param.setBuildingId(result.getBuildingId());
                        param.setLineGroupId(line.getLineGroupId());
                        param.setLineNo(line.getLineEtc().getLineNo());
                        energyResultList = getResultList(availableEnergyLineListServiceDaoImpl, param);
                        if(energyResultList != null && !energyResultList.isEmpty()) {
                            for(AvailableEnergyLineListResultData energyLine : energyResultList) {
                                paramEnergy = new TAvailableEnergy();
                                pkParamEnergy = new TAvailableEnergyPK();
                                pkParamEnergy.setCorpId(energyLine.getCorpId());
                                pkParamEnergy.setBuildingId(energyLine.getBuildingId());
                                pkParamEnergy.setEngTypeCd(energyLine.getEngTypeCd());
                                pkParamEnergy.setContractId(energyLine.getContractId());
                                pkParamEnergy.setEngId(energyLine.getEngId());
                                paramEnergy.setId(pkParamEnergy);

                                List<TAvailableEnergy> targetEnergyList = getResultList(tAvailableEnergyServiceDaoImpl,paramEnergy);
                                targetEnergyList = targetEnergyList.stream().filter(o -> o.getEnergyUseLineValueFlg()
                                        .equals(OsolConstants.FLG_ON)).collect(Collectors.toList());
                                if(targetEnergyList != null && !targetEnergyList.isEmpty()) {
                                    for(TAvailableEnergy targetEnergy : targetEnergyList) {
                                        targetEnergy.setEnergyUseLineValueFlg(OsolConstants.FLG_OFF);
                                        merge(tAvailableEnergyServiceDaoImpl,targetEnergy);
                                    }

                                }

                            }

                        }

                        //集計デマンド系統情報を削除する
                        paramLine = new MAggregateDmLine();
                        pkParamLine = new MAggregateDmLinePK();
                        pkParamLine.setCorpId(result.getCorpId());
                        pkParamLine.setLineGroupId(line.getLineGroupId());
                        pkParamLine.setLineNo(line.getLineEtc().getLineNo());
                        paramLine.setId(pkParamLine);
                        updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
                        if (updateLineList != null && !updateLineList.isEmpty()) {
                            for (MAggregateDmLine aggregateLine : updateLineList) {
                                remove(mAggregateDmLineServiceDaoImpl, aggregateLine);
                            }
                        }

                        //グラフ要素情報を削除する
                        paramGraphElement = new MGraphElement();
                        pkParamGraphElement = new MGraphElementPK();
                        pkParamGraphElement.setCorpId(result.getCorpId());
                        pkParamGraphElement.setLineGroupId(line.getLineGroupId());
                        paramGraphElement.setId(pkParamGraphElement);
                        paramGraphElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
                        paramGraphElement.setGraphLineGroupId(line.getLineGroupId());
                        paramGraphElement.setGraphLineNo(line.getLineEtc().getLineNo());
                        paramGraphElement.setDelFlg(OsolConstants.FLG_OFF);
                        updateGraphElementList = getResultList(mGraphElementServiceDaoImpl, paramGraphElement);
                        if (updateGraphElementList != null && !updateGraphElementList.isEmpty()) {
                            for (MGraphElement element : updateGraphElementList) {
                                element.setDelFlg(OsolConstants.FLG_ON);
                                element.setUpdateDate(serverDateTime);
                                element.setUpdateUserId(loginUserId);
                                merge(mGraphElementServiceDaoImpl, element);
                            }
                        }

                        //建物系統目標超過系統情報を削除する
                        paramTarget = new MLineTargetAlarm();
                        pkParamTarget = new MLineTargetAlarmPK();
                        pkParamTarget.setCorpId(result.getCorpId());
                        pkParamTarget.setLineGroupId(line.getLineGroupId());
                        pkParamTarget.setLineNo(line.getLineEtc().getLineNo());
                        paramTarget.setId(pkParamTarget);
                        paramTarget.setDelFlg(OsolConstants.FLG_OFF);
                        updateTargetList = getResultList(mLineTargetAlarmServiceDaoImpl, paramTarget);
                        if (updateTargetList != null && !updateTargetList.isEmpty()) {
                            for (MLineTargetAlarm target : updateTargetList) {
                                target.setDelFlg(OsolConstants.FLG_ON);
                                target.setUpdateDate(serverDateTime);
                                target.setUpdateUserId(loginUserId);
                                merge(mLineTargetAlarmServiceDaoImpl, target);
                            }
                        }

                        //建物系統目標超過系統時間情報を削除する
                        paramTargetTime = new MLineTargetAlarmTime();
                        pkParamTargetTime = new MLineTargetAlarmTimePK();
                        pkParamTargetTime.setCorpId(result.getCorpId());
                        pkParamTargetTime.setLineGroupId(line.getLineGroupId());
                        pkParamTargetTime.setLineNo(line.getLineEtc().getLineNo());
                        paramTargetTime.setId(pkParamTargetTime);
                        paramTargetTime.setDelFlg(OsolConstants.FLG_OFF);
                        updateTargetTimeList = getResultList(mLineTargetAlarmTimeServiceDaoImpl, paramTargetTime);
                        if (updateTargetTimeList != null && !updateTargetTimeList.isEmpty()) {
                            for (MLineTargetAlarmTime targetTime : updateTargetTimeList) {
                                targetTime.setDelFlg(OsolConstants.FLG_ON);
                                targetTime.setUpdateDate(serverDateTime);
                                targetTime.setUpdateUserId(loginUserId);
                                merge(mLineTargetAlarmTimeServiceDaoImpl, targetTime);
                            }
                        }

                        //建物系統時限標準値情報を削除する
                        paramTime = new MLineTimeStandard();
                        pkParamTime = new MLineTimeStandardPK();
                        pkParamTime.setCorpId(result.getCorpId());
                        pkParamTime.setLineGroupId(line.getLineGroupId());
                        pkParamTime.setLineNo(line.getLineEtc().getLineNo());
                        paramTime.setId(pkParamTime);
                        paramTime.setDelFlg(OsolConstants.FLG_OFF);
                        updateTimeList = getResultList(mLineTimeStandardsServiceDaoImpl, paramTime);
                        if (updateTimeList != null && !updateTimeList.isEmpty()) {
                            for (MLineTimeStandard lineTime : updateTimeList) {
                                lineTime.setDelFlg(OsolConstants.FLG_ON);
                                lineTime.setUpdateDate(serverDateTime);
                                lineTime.setUpdateUserId(loginUserId);
                                merge(mLineTimeStandardsServiceDaoImpl, lineTime);
                            }
                        }
                    }
                    //上記以外
                    if (line.getLineList() != null && !line.getLineList().isEmpty()) {
                        for (LineListDetailResultData lineDetail : line.getLineList()) {
                            if (ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal()
                                    .equals(lineDetail.getLineEnableFlg())) {

                                //使用エネルギーのフラグ更新（エネルギー自動反映対応）
                                AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
                                param.setCorpId(result.getCorpId());
                                param.setBuildingId(result.getBuildingId());
                                param.setLineGroupId(line.getLineGroupId());
                                param.setLineNo(lineDetail.getLineNo());
                                energyResultList = getResultList(availableEnergyLineListServiceDaoImpl, param);
                                if(energyResultList != null && !energyResultList.isEmpty()) {
                                    for(AvailableEnergyLineListResultData energyLine : energyResultList) {
                                        paramEnergy = new TAvailableEnergy();
                                        pkParamEnergy = new TAvailableEnergyPK();
                                        pkParamEnergy.setCorpId(energyLine.getCorpId());
                                        pkParamEnergy.setBuildingId(energyLine.getBuildingId());
                                        pkParamEnergy.setEngTypeCd(energyLine.getEngTypeCd());
                                        pkParamEnergy.setContractId(energyLine.getContractId());
                                        pkParamEnergy.setEngId(energyLine.getEngId());
                                        paramEnergy.setId(pkParamEnergy);

                                        List<TAvailableEnergy> targetEnergyList = getResultList(tAvailableEnergyServiceDaoImpl,paramEnergy);
                                        targetEnergyList = targetEnergyList.stream().filter(o -> o.getEnergyUseLineValueFlg()
                                                .equals(OsolConstants.FLG_ON)).collect(Collectors.toList());
                                        if(targetEnergyList != null && !targetEnergyList.isEmpty()) {
                                            for(TAvailableEnergy targetEnergy : targetEnergyList) {
                                                targetEnergy.setEnergyUseLineValueFlg(OsolConstants.FLG_OFF);
                                                merge(tAvailableEnergyServiceDaoImpl,targetEnergy);
                                            }

                                        }

                                    }

                                }

                                //集計デマンド系統情報を削除する
                                paramLine = new MAggregateDmLine();
                                pkParamLine = new MAggregateDmLinePK();
                                pkParamLine.setCorpId(result.getCorpId());
                                pkParamLine.setLineGroupId(line.getLineGroupId());
                                pkParamLine.setLineNo(lineDetail.getLineNo());
                                paramLine.setId(pkParamLine);
                                updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
                                if (updateLineList != null && !updateLineList.isEmpty()) {
                                    for (MAggregateDmLine aggregateLine : updateLineList) {
                                        remove(mAggregateDmLineServiceDaoImpl, aggregateLine);
                                    }
                                }

                                //グラフ要素情報を削除する
                                paramGraphElement = new MGraphElement();
                                pkParamGraphElement = new MGraphElementPK();
                                pkParamGraphElement.setCorpId(result.getCorpId());
                                pkParamGraphElement.setLineGroupId(line.getLineGroupId());
                                paramGraphElement.setId(pkParamGraphElement);
                                paramGraphElement
                                        .setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
                                paramGraphElement.setGraphLineGroupId(line.getLineGroupId());
                                paramGraphElement.setGraphLineNo(lineDetail.getLineNo());
                                paramGraphElement.setDelFlg(OsolConstants.FLG_OFF);
                                updateGraphElementList = getResultList(mGraphElementServiceDaoImpl, paramGraphElement);
                                if (updateGraphElementList != null && !updateGraphElementList.isEmpty()) {
                                    for (MGraphElement element : updateGraphElementList) {
                                        element.setDelFlg(OsolConstants.FLG_ON);
                                        element.setUpdateDate(serverDateTime);
                                        element.setUpdateUserId(loginUserId);
                                        merge(mGraphElementServiceDaoImpl, element);
                                    }
                                }

                                //建物系統目標超過系統情報を削除する
                                paramTarget = new MLineTargetAlarm();
                                pkParamTarget = new MLineTargetAlarmPK();
                                pkParamTarget.setCorpId(result.getCorpId());
                                pkParamTarget.setLineGroupId(line.getLineGroupId());
                                pkParamTarget.setLineNo(lineDetail.getLineNo());
                                paramTarget.setId(pkParamTarget);
                                paramTarget.setDelFlg(OsolConstants.FLG_OFF);
                                updateTargetList = getResultList(mLineTargetAlarmServiceDaoImpl, paramTarget);
                                if (updateTargetList != null && !updateTargetList.isEmpty()) {
                                    for (MLineTargetAlarm target : updateTargetList) {
                                        target.setDelFlg(OsolConstants.FLG_ON);
                                        target.setUpdateDate(serverDateTime);
                                        target.setUpdateUserId(loginUserId);
                                        merge(mLineTargetAlarmServiceDaoImpl, target);
                                    }
                                }

                                //建物系統目標超過系統時間情報を削除する
                                paramTargetTime = new MLineTargetAlarmTime();
                                pkParamTargetTime = new MLineTargetAlarmTimePK();
                                pkParamTargetTime.setCorpId(result.getCorpId());
                                pkParamTargetTime.setLineGroupId(line.getLineGroupId());
                                pkParamTargetTime.setLineNo(lineDetail.getLineNo());
                                paramTargetTime.setId(pkParamTargetTime);
                                paramTargetTime.setDelFlg(OsolConstants.FLG_OFF);
                                updateTargetTimeList = getResultList(mLineTargetAlarmTimeServiceDaoImpl,
                                        paramTargetTime);
                                if (updateTargetTimeList != null && !updateTargetTimeList.isEmpty()) {
                                    for (MLineTargetAlarmTime targetTime : updateTargetTimeList) {
                                        targetTime.setDelFlg(OsolConstants.FLG_ON);
                                        targetTime.setUpdateDate(serverDateTime);
                                        targetTime.setUpdateUserId(loginUserId);
                                        merge(mLineTargetAlarmTimeServiceDaoImpl, targetTime);
                                    }
                                }

                                //建物系統時限標準値情報を削除する
                                paramTime = new MLineTimeStandard();
                                pkParamTime = new MLineTimeStandardPK();
                                pkParamTime.setCorpId(result.getCorpId());
                                pkParamTime.setLineGroupId(line.getLineGroupId());
                                pkParamTime.setLineNo(lineDetail.getLineNo());
                                paramTime.setId(pkParamTime);
                                paramTime.setDelFlg(OsolConstants.FLG_OFF);
                                updateTimeList = getResultList(mLineTimeStandardsServiceDaoImpl, paramTime);
                                if (updateTimeList != null && !updateTimeList.isEmpty()) {
                                    for (MLineTimeStandard lineTime : updateTimeList) {
                                        lineTime.setDelFlg(OsolConstants.FLG_ON);
                                        lineTime.setUpdateDate(serverDateTime);
                                        lineTime.setUpdateUserId(loginUserId);
                                        merge(mLineTimeStandardsServiceDaoImpl, lineTime);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * 更新後の系統情報を取得する
     * @param result
     * @return
     */
    private BuildingLineListUpdateResult getNewLineData(BuildingLineListUpdateRequest result) throws Exception {
        BuildingLineListUpdateResult newResult = new BuildingLineListUpdateResult();
        BuildingLineGroupListDetailResultData detail = new BuildingLineGroupListDetailResultData();
        List<CorpLineListDetailResultData> lineList = new ArrayList<>();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingLineListUpdateResult();
        }

        //企業標準系統情報を取得する
        List<CorpLineListDetailResultData> corpLineList = getLineGroupList(
                result.getCorpId(),
                ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal(),
                null);

        if (corpLineList != null && corpLineList.size() == 1) {
            lineList.add(corpLineList.get(0));
        }

        //建物系統を取得する
        List<CorpLineListDetailResultData> buildingLineList = getLineGroupList(result.getCorpId(),
                ApiGenericTypeConstants.LINE_GROUP_TYPE.INDIVIDUAL.getVal(), result.getBuildingId());

        if (buildingLineList != null && !buildingLineList.isEmpty()) {
            lineList.addAll(buildingLineList);
        }

        detail.setLineList(lineList);
        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setDetail(detail);

        return newResult;
    }

    /**
     * 指定の系統グループ情報を取得する
     * @param loginUserId
     * @param loginPersonId
     * @param loginUserCorpId
     * @param corpId
     * @param lineGroupType
     * @param buildingId
     * @return
     */
    private List<CorpLineListDetailResultData> getLineGroupList(String corpId,
            String lineGroupType,
            Long buildingId) throws Exception {
        List<CorpLineListDetailResultData> ret = new ArrayList<>();

        // 系統グループ情報を取得
        LineGroupSearchDetailResultData param = new LineGroupSearchDetailResultData();
        param.setCorpId(corpId);
        param.setLineGroupType(lineGroupType);
        param.setBuildingId(buildingId);
        List<LineGroupSearchDetailResultData> _ret = getResultList(lineGroupSearchServiceDaoImpl, param);
        if (_ret != null) {
            for (LineGroupSearchDetailResultData _rs : _ret) {
                CorpLineListDetailResultData lineListBaseResultSet = new CorpLineListDetailResultData();
                lineListBaseResultSet.setLineGroupId(_rs.getLineGroupId());
                lineListBaseResultSet.setLineGroupName(_rs.getLineGroupName());
                lineListBaseResultSet.setLineGroupType(_rs.getLineGroupType());
                lineListBaseResultSet.setInitialViewFlg(_rs.getInitialViewFlg());
                lineListBaseResultSet.setDelFlg(_rs.getDelFlg());
                lineListBaseResultSet.setLineGroupVersion(_rs.getGroupVersion());

                // 系統情報を取得
                LineListDetailResultData _param = new LineListDetailResultData();
                _param.setCorpId(corpId);
                _param.setLineGroupId(_rs.getLineGroupId());
                List<LineListDetailResultData> __ret = getResultList(lineListServiceDaoImpl, _param);
                if (__ret != null) {
                    List<LineListDetailResultData> lineList = new ArrayList<>();
                    for (LineListDetailResultData __rs : __ret) {
                        if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(__rs.getLineNo())) {
                            // 全体
                            LineListDetailResultData lineAll = new LineListDetailResultData();
                            lineAll.setLineNo(__rs.getLineNo());
                            lineAll.setLineType(__rs.getLineType());
                            lineAll.setLineName(__rs.getLineName());
                            lineAll.setLineTarget(__rs.getLineTarget());
                            lineAll.setLineEnableFlg(__rs.getLineEnableFlg());
                            lineAll.setDelFlg(__rs.getDelFlg());
                            lineAll.setVersion(__rs.getVersion());
                            lineListBaseResultSet.setLineAll(lineAll);
                        } else if (ApiCodeValueConstants.LINE_TARGET.ETC.getVal().equals(__rs.getLineNo())) {
                            // その他
                            LineListDetailResultData lineEtc = new LineListDetailResultData();
                            lineEtc.setLineNo(__rs.getLineNo());
                            lineEtc.setLineType(__rs.getLineType());
                            lineEtc.setLineName(__rs.getLineName());
                            lineEtc.setLineTarget(__rs.getLineTarget());
                            lineEtc.setLineEnableFlg(__rs.getLineEnableFlg());
                            lineEtc.setDelFlg(__rs.getDelFlg());
                            lineEtc.setVersion(__rs.getVersion());
                            lineListBaseResultSet.setLineEtc(lineEtc);
                        } else {
                            lineList.add(__rs);
                        }
                    }
                    // ソート
                    lineList.sort(
                            (LineListDetailResultData rs1, LineListDetailResultData rs2) -> rs1.getLineNo()
                                    .compareTo(rs2.getLineNo()));

                    lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                        Integer l1 = Integer.parseInt(rs1.getLineNo());
                        Integer l2 = Integer.parseInt(rs2.getLineNo());
                        return l1.compareTo(l2);
                    });

                    lineListBaseResultSet.setLineList(lineList);
                }
                ret.add(lineListBaseResultSet);
            }
        }

        // ソート
        ret.sort(
                (CorpLineListDetailResultData rs1, CorpLineListDetailResultData rs2) -> rs1.getLineGroupId()
                        .compareTo(rs2.getLineGroupId()));

        return ret;
    }

}
