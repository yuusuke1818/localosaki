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
import jp.co.osaki.osol.api.parameter.energy.setting.CorpLineListUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.CorpLineListUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.CorpLineListUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MAggregateDmLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmTimeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTimeStandardsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTypeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyServiceDaoImpl;
import jp.co.osaki.osol.entity.MAggregateDmLine;
import jp.co.osaki.osol.entity.MAggregateDmLinePK;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
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
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 企業系統一覧更新 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class CorpLineListUpdateDao extends OsolApiDao<CorpLineListUpdateParameter> {

    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;
    private final MLineServiceDaoImpl mLineServiceDaoImpl;
    private final MLineGroupServiceDaoImpl mLineGroupServiceDaoImpl;
    private final MLineTypeServiceDaoImpl mLineTypeServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final MAggregateDmLineServiceDaoImpl mAggregateDmLineServiceDaoImpl;
    private final MLineTimeStandardsServiceDaoImpl mLineTimeStandardsServiceDaoImpl;
    private final MLineTargetAlarmServiceDaoImpl mLineTargetAlarmServiceDaoImpl;
    private final MLineTargetAlarmTimeServiceDaoImpl mLineTargetAlarmTimeServiceDaoImpl;
    private final AvailableEnergyLineListServiceDaoImpl availableEnergyLineListServiceDaoImpl;
    private final TAvailableEnergyServiceDaoImpl tAvailableEnergyServiceDaoImpl;


    public CorpLineListUpdateDao() {
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        mLineServiceDaoImpl = new MLineServiceDaoImpl();
        mLineGroupServiceDaoImpl = new MLineGroupServiceDaoImpl();
        mLineTypeServiceDaoImpl = new MLineTypeServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        mAggregateDmLineServiceDaoImpl = new MAggregateDmLineServiceDaoImpl();
        mLineTimeStandardsServiceDaoImpl = new MLineTimeStandardsServiceDaoImpl();
        mLineTargetAlarmServiceDaoImpl = new MLineTargetAlarmServiceDaoImpl();
        mLineTargetAlarmTimeServiceDaoImpl = new MLineTargetAlarmTimeServiceDaoImpl();
        availableEnergyLineListServiceDaoImpl = new AvailableEnergyLineListServiceDaoImpl();
        tAvailableEnergyServiceDaoImpl = new TAvailableEnergyServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public CorpLineListUpdateResult query(CorpLineListUpdateParameter parameter) throws Exception {
        MCorp exCorp;
        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();
        if (parameter.getResultSet() == null) {
            return new CorpLineListUpdateResult();
        }

        //更新情報
        CorpLineListUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //企業情報の排他チェック
        if (CheckUtility.isNullOrEmpty(resultSet.getCorpId()) || resultSet.getDetail() == null) {
            return new CorpLineListUpdateResult();
        } else {
            exCorp = corpExclusiveCheck(resultSet);
            if (exCorp == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //系統グループ情報の更新
        Long lineGroupId = writeLineGroupInfo(resultSet, serverDateTime, loginUserId);
        //系統の更新
        writeLineInfo(resultSet, lineGroupId, serverDateTime, loginUserId);
        //無効となったデータの更新
        updateInvalidLineRelatedData(resultSet, lineGroupId, serverDateTime, loginUserId);
        //企業情報の更新
        exCorp.setUpdateDate(serverDateTime);
        exCorp.setUpdateUserId(loginUserId);
        merge(mCorpApiServiceDaoImpl, exCorp);
        //更新後のデータを再取得
        return getNewLineListData(resultSet);
    }

    /**
     * 企業情報の排他チェックを行う
     * @param result
     * @return
     */
    private MCorp corpExclusiveCheck(CorpLineListUpdateRequest result) throws Exception {
        MCorp corpParam = new MCorp();
        corpParam.setCorpId(result.getCorpId());
        MCorp exCorp = find(mCorpApiServiceDaoImpl, corpParam);
        if (exCorp == null || !exCorp.getVersion().equals(result.getVersion())) {
            //排他制御のデータがない場合または前に保持をしていたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exCorp;
        }
    }

    /**
     * 系統グループ情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     * @return
     */
    private Long writeLineGroupInfo(CorpLineListUpdateRequest result, Timestamp serverDateTime, Long loginUserId)
            throws Exception {
        if (result.getDetail().getLineGroupId() == null) {
            //系統グループIDが設定されていない場合のみ、系統グループを登録する
            MLineGroup lineGroup = new MLineGroup();
            MLineGroupPK pkLineGroup = new MLineGroupPK();
            Long lineGroupId = createId(OsolConstants.ID_SEQUENCE_NAME.LINE_GROUP_ID.getVal());
            pkLineGroup.setCorpId(result.getCorpId());
            pkLineGroup.setLineGroupId(lineGroupId);
            lineGroup.setId(pkLineGroup);
            lineGroup.setLineGroupName(ApiCodeValueConstants.LINE_GROUP_TYPE.CORP_STANDARD.getName());
            lineGroup.setLineGroupType(ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal());
            lineGroup.setBuildingId(null);
            lineGroup.setDelFlg(OsolConstants.FLG_OFF);
            lineGroup.setCreateDate(serverDateTime);
            lineGroup.setCreateUserId(loginUserId);
            lineGroup.setUpdateDate(serverDateTime);
            lineGroup.setUpdateUserId(loginUserId);
            persist(mLineGroupServiceDaoImpl, lineGroup);
            return lineGroupId;
        } else {
            return result.getDetail().getLineGroupId();
        }
    }

    /**
     * 系統情報を更新する
     * @param result
     * @param lineGroupId
     * @param serverDateTime
     * @param loginUserId
     */
    private void writeLineInfo(CorpLineListUpdateRequest result, Long lineGroupId, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MLine paramLine;
        MLine updateLine = null;
        MLinePK pkParamLine;
        MLinePK pkUpdateLine;
        MLineType paramLineType;
        MLineType updateLineType;
        Boolean newLineFlg;

        //全体の系統情報の更新
        if (result.getDetail().getLineAll() != null) {
            if (result.getDetail().getLineGroupId() != null) {
                paramLine = new MLine();
                pkParamLine = new MLinePK();
                pkParamLine.setCorpId(result.getCorpId());
                pkParamLine.setLineGroupId(lineGroupId);
                pkParamLine.setLineNo(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal());
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

            //系統種別の取得
            paramLineType = new MLineType();
            paramLineType.setLineType(result.getDetail().getLineAll().getLineType());
            updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);

            if (newLineFlg) {
                //新規登録
                updateLine = new MLine();
                pkUpdateLine = new MLinePK();
                pkUpdateLine.setCorpId(result.getCorpId());
                pkUpdateLine.setLineGroupId(lineGroupId);
                pkUpdateLine.setLineNo(result.getDetail().getLineAll().getLineNo());
                updateLine.setId(pkUpdateLine);
                updateLine.setCreateDate(serverDateTime);
                updateLine.setCreateUserId(loginUserId);
            }

            updateLine.setLineName(result.getDetail().getLineAll().getLineName());
            updateLine.setLineUnit(result.getDetail().getLineAll().getLineUnit());
            updateLine.setLineTarget(result.getDetail().getLineAll().getLineTarget());
            updateLine.setMLineType(updateLineType);
            updateLine.setInputEnableFlg(result.getDetail().getLineAll().getInputEnableFlg());
            updateLine.setLineEnableFlg(result.getDetail().getLineAll().getLineEnableFlg());
            updateLine.setDelFlg(result.getDetail().getLineAll().getDelFlg());
            updateLine.setUpdateDate(serverDateTime);
            updateLine.setUpdateUserId(loginUserId);

            if (newLineFlg) {
                persist(mLineServiceDaoImpl, updateLine);
            } else {
                merge(mLineServiceDaoImpl, updateLine);
            }

        }

        //その他の系統情報の更新
        if (result.getDetail().getLineEtc() != null) {

            if (result.getDetail().getLineGroupId() != null) {
                paramLine = new MLine();
                pkParamLine = new MLinePK();
                pkParamLine.setCorpId(result.getCorpId());
                pkParamLine.setLineGroupId(lineGroupId);
                pkParamLine.setLineNo(result.getDetail().getLineEtc().getLineNo());
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

            //系統種別の取得
            paramLineType = new MLineType();
            paramLineType.setLineType(result.getDetail().getLineEtc().getLineType());
            updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);

            if (newLineFlg) {
                //新規登録
                updateLine = new MLine();
                pkUpdateLine = new MLinePK();
                pkUpdateLine.setCorpId(result.getCorpId());
                pkUpdateLine.setLineGroupId(lineGroupId);
                pkUpdateLine.setLineNo(result.getDetail().getLineEtc().getLineNo());
                updateLine.setId(pkUpdateLine);
                updateLine.setLineName(result.getDetail().getLineEtc().getLineName());
                updateLine.setLineUnit(result.getDetail().getLineEtc().getLineUnit());
                updateLine.setLineTarget(result.getDetail().getLineEtc().getLineTarget());
                updateLine.setMLineType(updateLineType);
                updateLine.setInputEnableFlg(result.getDetail().getLineEtc().getInputEnableFlg());
                updateLine.setLineEnableFlg(result.getDetail().getLineEtc().getLineEnableFlg());
                updateLine.setDelFlg(result.getDetail().getLineEtc().getDelFlg());
                updateLine.setCreateDate(serverDateTime);
                updateLine.setCreateUserId(loginUserId);
                updateLine.setUpdateDate(serverDateTime);
                updateLine.setUpdateUserId(loginUserId);
                persist(mLineServiceDaoImpl, updateLine);
            } else {
                //更新
                updateLine.setLineName(result.getDetail().getLineEtc().getLineName());
                updateLine.setLineUnit(result.getDetail().getLineEtc().getLineUnit());
                updateLine.setLineTarget(result.getDetail().getLineEtc().getLineTarget());
                updateLine.setMLineType(updateLineType);
                updateLine.setInputEnableFlg(result.getDetail().getLineEtc().getInputEnableFlg());
                updateLine.setLineEnableFlg(result.getDetail().getLineEtc().getLineEnableFlg());
                updateLine.setDelFlg(result.getDetail().getLineEtc().getDelFlg());
                updateLine.setUpdateDate(serverDateTime);
                updateLine.setUpdateUserId(loginUserId);
                merge(mLineServiceDaoImpl, updateLine);
            }

        }

        //上記以外の系統情報の更新
        if (result.getDetail().getLineList() != null && !result.getDetail().getLineList().isEmpty()) {
            for (LineListDetailResultData line : result.getDetail().getLineList()) {
                if (result.getDetail().getLineGroupId() != null) {
                    paramLine = new MLine();
                    pkParamLine = new MLinePK();
                    pkParamLine.setCorpId(result.getCorpId());
                    pkParamLine.setLineGroupId(lineGroupId);
                    pkParamLine.setLineNo(line.getLineNo());
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

                //系統種別の取得
                paramLineType = new MLineType();
                paramLineType.setLineType(line.getLineType());
                updateLineType = find(mLineTypeServiceDaoImpl, paramLineType);

                if (newLineFlg) {
                    //新規登録
                    updateLine = new MLine();
                    pkUpdateLine = new MLinePK();
                    pkUpdateLine.setCorpId(result.getCorpId());
                    pkUpdateLine.setLineGroupId(lineGroupId);
                    pkUpdateLine.setLineNo(line.getLineNo());
                    updateLine.setId(pkUpdateLine);
                    updateLine.setLineName(line.getLineName());
                    updateLine.setLineUnit(line.getLineUnit());
                    updateLine.setLineTarget(line.getLineTarget());
                    updateLine.setMLineType(updateLineType);
                    updateLine.setInputEnableFlg(line.getInputEnableFlg());
                    updateLine.setLineEnableFlg(line.getLineEnableFlg());
                    updateLine.setDelFlg(line.getDelFlg());
                    updateLine.setCreateDate(serverDateTime);
                    updateLine.setCreateUserId(loginUserId);
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);
                    persist(mLineServiceDaoImpl, updateLine);
                } else {
                    //更新
                    updateLine.setLineName(line.getLineName());
                    updateLine.setLineUnit(line.getLineUnit());
                    updateLine.setLineTarget(line.getLineTarget());
                    updateLine.setMLineType(updateLineType);
                    updateLine.setInputEnableFlg(line.getInputEnableFlg());
                    updateLine.setLineEnableFlg(line.getLineEnableFlg());
                    updateLine.setDelFlg(line.getDelFlg());
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);
                    merge(mLineServiceDaoImpl, updateLine);
                }
            }
        }

    }

    /**
     * 無効となったデータに関連する各種情報の更新を行う
     * @param result
     * @param lineGroupId
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateInvalidLineRelatedData(CorpLineListUpdateRequest result, Long lineGroupId,
            Timestamp serverDateTime,
            Long loginUserId) throws Exception {

        TAvailableEnergy paramEnergy;
        TAvailableEnergyPK pkParamEnergy;
        MAggregateDmLine paramLine;
        MGraphElement paramElement;
        MLineTargetAlarm paramTarget;
        MLineTargetAlarmTime paramTargetTime;
        MLineTimeStandard paramTime;
        List<MAggregateDmLine> updateLineList;
        List<MGraphElement> updateElementList;
        List<MLineTargetAlarm> updateTargetList;
        List<MLineTargetAlarmTime> updateTargetTimeList;
        List<MLineTimeStandard> updateTimeList;
        List<AvailableEnergyLineListResultData> energyResultList;
        MAggregateDmLinePK pkParamLine;
        MGraphElementPK pkParamElement;
        MLineTargetAlarmPK pkParamTarget;
        MLineTargetAlarmTimePK pkParamTargetTime;
        MLineTimeStandardPK pkParamTime;

        if (result.getDetail().getLineAll() != null && ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal()
                .equals(result.getDetail().getLineAll().getLineEnableFlg())) {
            //全体が無効の場合
            //使用エネルギーのフラグ更新（エネルギー自動反映対応）
            AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
            param.setCorpId(result.getCorpId());
            param.setLineGroupId(result.getDetail().getLineGroupId());
            param.setLineNo(result.getDetail().getLineAll().getLineNo());
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
            pkParamLine.setLineGroupId(lineGroupId);
            pkParamLine.setLineNo(result.getDetail().getLineAll().getLineNo());
            paramLine.setId(pkParamLine);
            updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
            if (updateLineList != null && !updateLineList.isEmpty()) {
                for (MAggregateDmLine aggregate : updateLineList) {
                    remove(mAggregateDmLineServiceDaoImpl, aggregate);
                }
            }

            //グラフ要素情報を削除する
            paramElement = new MGraphElement();
            pkParamElement = new MGraphElementPK();
            pkParamElement.setCorpId(result.getCorpId());
            paramElement.setId(pkParamElement);
            paramElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
            paramElement.setGraphLineGroupId(lineGroupId);
            paramElement.setGraphLineNo(result.getDetail().getLineAll().getLineNo());
            paramElement.setDelFlg(OsolConstants.FLG_OFF);
            updateElementList = getResultList(mGraphElementServiceDaoImpl, paramElement);
            if (updateElementList != null && !updateElementList.isEmpty()) {
                for (MGraphElement element : updateElementList) {
                    element.setDelFlg(OsolConstants.FLG_ON);
                    element.setUpdateDate(serverDateTime);
                    element.setUpdateUserId(loginUserId);
                    merge(mGraphElementServiceDaoImpl, element);
                }
            }

            //建物系統目標超過警報を更新する
            paramTarget = new MLineTargetAlarm();
            pkParamTarget = new MLineTargetAlarmPK();
            pkParamTarget.setCorpId(result.getCorpId());
            pkParamTarget.setLineGroupId(lineGroupId);
            pkParamTarget.setLineNo(result.getDetail().getLineAll().getLineNo());
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

            //建物系統目標超過警報時間を更新する
            paramTargetTime = new MLineTargetAlarmTime();
            pkParamTargetTime = new MLineTargetAlarmTimePK();
            pkParamTargetTime.setCorpId(result.getCorpId());
            pkParamTargetTime.setLineGroupId(lineGroupId);
            pkParamTargetTime.setLineNo(result.getDetail().getLineAll().getLineNo());
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

            //建物系統時限標準値を更新する
            paramTime = new MLineTimeStandard();
            pkParamTime = new MLineTimeStandardPK();
            pkParamTime.setCorpId(result.getCorpId());
            pkParamTime.setLineGroupId(lineGroupId);
            pkParamTime.setLineNo(result.getDetail().getLineAll().getLineNo());
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

        if (result.getDetail().getLineEtc() != null && ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal()
                .equals(result.getDetail().getLineEtc().getLineEnableFlg())) {
            //その他が無効の場合
            //使用エネルギーのフラグ更新（エネルギー自動反映対応）
            AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
            param.setCorpId(result.getCorpId());
            param.setLineGroupId(result.getDetail().getLineGroupId());
            param.setLineNo(result.getDetail().getLineEtc().getLineNo());
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
            pkParamLine.setLineGroupId(lineGroupId);
            pkParamLine.setLineNo(result.getDetail().getLineEtc().getLineNo());
            paramLine.setId(pkParamLine);
            updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
            if (updateLineList != null && !updateLineList.isEmpty()) {
                for (MAggregateDmLine aggregate : updateLineList) {
                    remove(mAggregateDmLineServiceDaoImpl, aggregate);
                }
            }

            //グラフ要素情報を削除する
            paramElement = new MGraphElement();
            pkParamElement = new MGraphElementPK();
            pkParamElement.setCorpId(result.getCorpId());
            paramElement.setId(pkParamElement);
            paramElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
            paramElement.setGraphLineGroupId(lineGroupId);
            paramElement.setGraphLineNo(result.getDetail().getLineEtc().getLineNo());
            paramElement.setDelFlg(OsolConstants.FLG_OFF);
            updateElementList = getResultList(mGraphElementServiceDaoImpl, paramElement);
            if (updateElementList != null && !updateElementList.isEmpty()) {
                for (MGraphElement element : updateElementList) {
                    element.setDelFlg(OsolConstants.FLG_ON);
                    element.setUpdateDate(serverDateTime);
                    element.setUpdateUserId(loginUserId);
                    merge(mGraphElementServiceDaoImpl, element);
                }
            }

            //建物系統目標超過警報を更新する
            paramTarget = new MLineTargetAlarm();
            pkParamTarget = new MLineTargetAlarmPK();
            pkParamTarget.setCorpId(result.getCorpId());
            pkParamTarget.setLineGroupId(lineGroupId);
            pkParamTarget.setLineNo(result.getDetail().getLineEtc().getLineNo());
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

            //建物系統目標超過警報時間を更新する
            paramTargetTime = new MLineTargetAlarmTime();
            pkParamTargetTime = new MLineTargetAlarmTimePK();
            pkParamTargetTime.setCorpId(result.getCorpId());
            pkParamTargetTime.setLineGroupId(lineGroupId);
            pkParamTargetTime.setLineNo(result.getDetail().getLineEtc().getLineNo());
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

            //建物系統時限標準値を更新する
            paramTime = new MLineTimeStandard();
            pkParamTime = new MLineTimeStandardPK();
            pkParamTime.setCorpId(result.getCorpId());
            pkParamTime.setLineGroupId(lineGroupId);
            pkParamTime.setLineNo(result.getDetail().getLineEtc().getLineNo());
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

        if (result.getDetail().getLineList() != null && !result.getDetail().getLineList().isEmpty()) {
            for (LineListDetailResultData line : result.getDetail().getLineList()) {
                if (ApiCodeValueConstants.LINE_ENABLE_FLG.INVALID.getVal().equals(line.getLineEnableFlg())) {
                    //無効の場合
                    //使用エネルギーのフラグ更新（エネルギー自動反映対応）
                    AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();
                    param.setCorpId(result.getCorpId());
                    param.setLineGroupId(result.getDetail().getLineGroupId());
                    param.setLineNo(line.getLineNo());
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
                    pkParamLine.setLineGroupId(lineGroupId);
                    pkParamLine.setLineNo(line.getLineNo());
                    paramLine.setId(pkParamLine);
                    updateLineList = getResultList(mAggregateDmLineServiceDaoImpl, paramLine);
                    if (updateLineList != null && !updateLineList.isEmpty()) {
                        for (MAggregateDmLine aggregate : updateLineList) {
                            remove(mAggregateDmLineServiceDaoImpl, aggregate);
                        }
                    }

                    //グラフ要素情報を削除する
                    paramElement = new MGraphElement();
                    pkParamElement = new MGraphElementPK();
                    pkParamElement.setCorpId(result.getCorpId());
                    paramElement.setId(pkParamElement);
                    paramElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.LINE.getVal());
                    paramElement.setGraphLineGroupId(lineGroupId);
                    paramElement.setGraphLineNo(line.getLineNo());
                    paramElement.setDelFlg(OsolConstants.FLG_OFF);
                    updateElementList = getResultList(mGraphElementServiceDaoImpl, paramElement);
                    if (updateElementList != null && !updateElementList.isEmpty()) {
                        for (MGraphElement element : updateElementList) {
                            element.setDelFlg(OsolConstants.FLG_ON);
                            element.setUpdateDate(serverDateTime);
                            element.setUpdateUserId(loginUserId);
                            merge(mGraphElementServiceDaoImpl, element);
                        }
                    }

                    //建物系統目標超過警報を更新する
                    paramTarget = new MLineTargetAlarm();
                    pkParamTarget = new MLineTargetAlarmPK();
                    pkParamTarget.setCorpId(result.getCorpId());
                    pkParamTarget.setLineGroupId(lineGroupId);
                    pkParamTarget.setLineNo(line.getLineNo());
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

                    //建物系統目標超過警報時間を更新する
                    paramTargetTime = new MLineTargetAlarmTime();
                    pkParamTargetTime = new MLineTargetAlarmTimePK();
                    pkParamTargetTime.setCorpId(result.getCorpId());
                    pkParamTargetTime.setLineGroupId(lineGroupId);
                    pkParamTargetTime.setLineNo(line.getLineNo());
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

                    //建物系統時限標準値を更新する
                    paramTime = new MLineTimeStandard();
                    pkParamTime = new MLineTimeStandardPK();
                    pkParamTime.setCorpId(result.getCorpId());
                    pkParamTime.setLineGroupId(lineGroupId);
                    pkParamTime.setLineNo(line.getLineNo());
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

    /**
     * 更新後の最新情報を取得する
     * @param result
     * @return
     */
    private CorpLineListUpdateResult getNewLineListData(CorpLineListUpdateRequest result) throws Exception {

        CorpLineListUpdateResult newResult = new CorpLineListUpdateResult();
        CorpLineListDetailResultData detailResult = new CorpLineListDetailResultData();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(result.getCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        if (exList == null || exList.size() != 1) {
            return new CorpLineListUpdateResult();
        }

        //企業系統グループを取得する
        LineGroupSearchDetailResultData groupParam = new LineGroupSearchDetailResultData();
        groupParam.setCorpId(result.getCorpId());
        groupParam.setLineGroupType(ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal());
        List<LineGroupSearchDetailResultData> groupList = getResultList(lineGroupSearchServiceDaoImpl, groupParam);

        if (groupList != null && groupList.size() == 1) {
            detailResult.setLineGroupId(groupList.get(0).getLineGroupId());
            detailResult.setLineGroupName(groupList.get(0).getLineGroupName());
            detailResult.setLineGroupType(groupList.get(0).getLineGroupType());
            detailResult.setDelFlg(groupList.get(0).getDelFlg());
            detailResult.setLineGroupVersion(groupList.get(0).getGroupVersion());

            //系統情報を取得する
            LineListDetailResultData lineParam = new LineListDetailResultData();
            lineParam.setCorpId(result.getCorpId());
            lineParam.setLineGroupId(groupList.get(0).getLineGroupId());
            List<LineListDetailResultData> tempLineList = getResultList(lineListServiceDaoImpl, lineParam);

            if (tempLineList == null || tempLineList.isEmpty()) {
                detailResult.setLineList(new ArrayList<>());
                detailResult.setLineAll(new LineListDetailResultData());
                detailResult.setLineEtc(new LineListDetailResultData());
            } else {
                List<LineListDetailResultData> lineList = new ArrayList<>();
                LineListDetailResultData allLine = new LineListDetailResultData();
                LineListDetailResultData etcLine = new LineListDetailResultData();
                for (LineListDetailResultData line : tempLineList) {
                    if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(line.getLineTarget())) {
                        //全体の場合
                        allLine.setLineNo(line.getLineNo());
                        allLine.setLineType(line.getLineType());
                        allLine.setLineName(line.getLineName());
                        allLine.setLineTarget(line.getLineTarget());
                        allLine.setInputEnableFlg(line.getInputEnableFlg());
                        allLine.setLineEnableFlg(line.getLineEnableFlg());
                        allLine.setDelFlg(line.getDelFlg());
                        allLine.setVersion(line.getVersion());
                    } else if (ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(line.getLineTarget())) {
                        //その他の場合
                        etcLine.setLineNo(line.getLineNo());
                        etcLine.setLineType(line.getLineType());
                        etcLine.setLineName(line.getLineName());
                        etcLine.setLineTarget(line.getLineTarget());
                        etcLine.setInputEnableFlg(line.getInputEnableFlg());
                        etcLine.setLineEnableFlg(line.getLineEnableFlg());
                        etcLine.setDelFlg(line.getDelFlg());
                        etcLine.setVersion(line.getVersion());
                    } else {
                        //上記以外の場合
                        lineList.add(line);
                    }
                }

                if (!lineList.isEmpty()) {
                    //ソートする
                    lineList.sort(
                            (LineListDetailResultData rs1, LineListDetailResultData rs2) -> rs1.getLineNo()
                                    .compareTo(rs2.getLineNo()));

                    lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                        Integer l1 = Integer.parseInt(rs1.getLineNo());
                        Integer l2 = Integer.parseInt(rs2.getLineNo());
                        return l1.compareTo(l2);
                    });
                }

                detailResult.setLineList(lineList);
                detailResult.setLineAll(allLine);
                detailResult.setLineEtc(etcLine);
            }
        }

        newResult.setCorpId(exList.get(0).getCorpId());
        newResult.setVersion(exList.get(0).getVersion());
        newResult.setDetail(detailResult);

        return newResult;

    }

}
