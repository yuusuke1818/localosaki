package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmBaseInfoUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDmBaseInfoUpdateDmLineRequestSet;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDmBaseInfoUpdateDmRequestSet;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDmBaseInfoUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmBaseInfoUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.AggregateDmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MAggregateDmLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MAggregateDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.EnergySettingUtility;
import jp.co.osaki.osol.entity.MAggregateDm;
import jp.co.osaki.osol.entity.MAggregateDmLine;
import jp.co.osaki.osol.entity.MAggregateDmLinePK;
import jp.co.osaki.osol.entity.MAggregateDmPK;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物デマンド更新 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class BuildingDmBaseInfoUpdateDao extends OsolApiDao<BuildingDmBaseInfoUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl mBuildingDmServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final MAggregateDmServiceDaoImpl mAggregateDmServiceDaoImpl;
    private final MAggregateDmLineServiceDaoImpl mAggregateDmLineServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final AggregateDmListServiceDaoImpl aggregateDmListServiceDaoImpl;
    private final AggregateDmLineListServiceDaoImpl aggregateDmLineListServiceDaoImpl;

    public BuildingDmBaseInfoUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mBuildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        mAggregateDmServiceDaoImpl = new MAggregateDmServiceDaoImpl();
        mAggregateDmLineServiceDaoImpl = new MAggregateDmLineServiceDaoImpl();
        aggregateDmListServiceDaoImpl = new AggregateDmListServiceDaoImpl();
        aggregateDmLineListServiceDaoImpl = new AggregateDmLineListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingDmBaseInfoUpdateResult query(BuildingDmBaseInfoUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingDmBaseInfoUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingDmBaseInfoUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingDmBaseInfoUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物デマンド情報の更新
        updateBuildingDemand(resultSet, serverDateTime, loginUserId);
        //集計デマンド情報の更新
        updateAggregateDm(resultSet, serverDateTime, loginUserId);
        //集計デマンド系統情報の更新
        updateAggregateDmLine(resultSet, serverDateTime, loginUserId);
        //グラフ要素情報の更新
        deleteGraphElementInfo(resultSet, serverDateTime, loginUserId);
        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        //最新の情報を取得して終了
        return getNewBuildingDmBaseInfo(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingDmBaseInfoUpdateRequest result) throws Exception {
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
     * 建物デマンドを更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateBuildingDemand(BuildingDmBaseInfoUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId)
            throws Exception {
        MBuildingDm paramDemand;
        MBuildingDm updateDemand = null;
        MBuildingDmPK pkParamDemand;
        MBuildingDmPK pkUpdateDemand;
        Boolean newDemandFlg;

        paramDemand = new MBuildingDm();
        pkParamDemand = new MBuildingDmPK();
        pkParamDemand.setCorpId(result.getCorpId());
        pkParamDemand.setBuildingId(result.getBuildingId());
        paramDemand.setId(pkParamDemand);
        updateDemand = find(mBuildingDmServiceDaoImpl, paramDemand);
        if (updateDemand == null) {
            newDemandFlg = Boolean.TRUE;
            updateDemand = new MBuildingDm();
            pkUpdateDemand = new MBuildingDmPK();
            pkUpdateDemand.setCorpId(result.getCorpId());
            pkUpdateDemand.setBuildingId(result.getBuildingId());
            updateDemand.setId(pkUpdateDemand);
            updateDemand.setCreateDate(serverDateTime);
            updateDemand.setCreateUserId(loginUserId);
        } else {
            newDemandFlg = Boolean.FALSE;
        }

        // 時間帯
        if (result.getBuildingDmDetail().getWorkStartTime() != null) {
            updateDemand.setWorkStartTime(new Time(result.getBuildingDmDetail().getWorkStartTime().getTime()));
        } else {
            updateDemand.setWorkStartTime(null);
        }
        if (result.getBuildingDmDetail().getShopOpenTime() != null) {
            updateDemand.setShopOpenTime(new Time(result.getBuildingDmDetail().getShopOpenTime().getTime()));
        } else {
            updateDemand.setShopOpenTime(null);
        }
        if (result.getBuildingDmDetail().getShopCloseTime() != null) {
            updateDemand.setShopCloseTime(new Time(result.getBuildingDmDetail().getShopCloseTime().getTime()));
        } else {
            updateDemand.setShopCloseTime(null);
        }
        if (result.getBuildingDmDetail().getWorkEndTime() != null) {
            updateDemand.setWorkEndTime(new Time(result.getBuildingDmDetail().getWorkEndTime().getTime()));
        } else {
            updateDemand.setWorkEndTime(null);
        }

        // 目標
        updateDemand.setTargetKwhMonth1(result.getBuildingDmDetail().getTargetKwhMonth1());
        updateDemand.setTargetKwhMonth2(result.getBuildingDmDetail().getTargetKwhMonth2());
        updateDemand.setTargetKwhMonth3(result.getBuildingDmDetail().getTargetKwhMonth3());
        updateDemand.setTargetKwhMonth4(result.getBuildingDmDetail().getTargetKwhMonth4());
        updateDemand.setTargetKwhMonth5(result.getBuildingDmDetail().getTargetKwhMonth5());
        updateDemand.setTargetKwhMonth6(result.getBuildingDmDetail().getTargetKwhMonth6());
        updateDemand.setTargetKwhMonth7(result.getBuildingDmDetail().getTargetKwhMonth7());
        updateDemand.setTargetKwhMonth8(result.getBuildingDmDetail().getTargetKwhMonth8());
        updateDemand.setTargetKwhMonth9(result.getBuildingDmDetail().getTargetKwhMonth9());
        updateDemand.setTargetKwhMonth10(result.getBuildingDmDetail().getTargetKwhMonth10());
        updateDemand.setTargetKwhMonth11(result.getBuildingDmDetail().getTargetKwhMonth11());
        updateDemand.setTargetKwhMonth12(result.getBuildingDmDetail().getTargetKwhMonth12());
        // 目標
        updateDemand.setStandardTargetContrastRate(result.getBuildingDmDetail().getStandardTargetContrastRate());
        updateDemand.setExcellentTargetContrastRate(result.getBuildingDmDetail().getExcellentTargetContrastRate());
        updateDemand.setContractKw(result.getBuildingDmDetail().getContractKw());
        // CO2排出量計算用係数
        updateDemand.setCo2EngTypeCd(result.getBuildingDmDetail().getCo2EngTypeCd());
        updateDemand.setCo2EngId(result.getBuildingDmDetail().getCo2EngId());
        // デマンド警報通知メール
        updateDemand.setFacilitiesMailAddress1(result.getBuildingDmDetail().getFacilitiesMailAddress1());
        updateDemand.setFacilitiesMailAddress2(result.getBuildingDmDetail().getFacilitiesMailAddress2());
        updateDemand.setFacilitiesMailAddress3(result.getBuildingDmDetail().getFacilitiesMailAddress3());
        updateDemand.setFacilitiesMailAddress4(result.getBuildingDmDetail().getFacilitiesMailAddress4());
        updateDemand.setFacilitiesMailAddress5(result.getBuildingDmDetail().getFacilitiesMailAddress5());
        // 外気温
        updateDemand.setOutAirTempDispFlg(result.getBuildingDmDetail().getOutAirTempDispFlg());

        // AI設定情報
        updateDemand.setDayCommodityChargeUnitPrice(result.getBuildingDmDetail().getDayCommodityChargeUnitPrice());
        updateDemand.setNightCommodityChargeUnitPrice(result.getBuildingDmDetail().getNightCommodityChargeUnitPrice());

        // 更新情報
        updateDemand.setUpdateDate(serverDateTime);
        updateDemand.setUpdateUserId(loginUserId);

        if (newDemandFlg) {
            persist(mBuildingDmServiceDaoImpl, updateDemand);
        } else {
            merge(mBuildingDmServiceDaoImpl, updateDemand);
        }

    }

    /**
     * 集計デマンド情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     * @throws Exception
     */
    private void updateAggregateDm(BuildingDmBaseInfoUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId)
            throws Exception {

        MAggregateDm paramDm;
        MAggregateDm updateDm = null;
        MAggregateDmPK pkParamDm;
        MAggregateDmPK pkUpdateDm;
        Boolean newUpdateDmFlg;

        if (result.getAggregateDmList() == null || result.getAggregateDmList().isEmpty()) {
            return;
        }

        for (BuildingDmBaseInfoUpdateDmRequestSet aggregateDm : result.getAggregateDmList()) {

            if (aggregateDm.getAggregateDmId() == null) {
                newUpdateDmFlg = Boolean.TRUE;
            } else {
                paramDm = new MAggregateDm();
                pkParamDm = new MAggregateDmPK();
                pkParamDm.setCorpId(aggregateDm.getCorpId());
                pkParamDm.setBuildingId(aggregateDm.getBuildingId());
                pkParamDm.setAggregateDmId(aggregateDm.getAggregateDmId());
                paramDm.setId(pkParamDm);
                updateDm = find(mAggregateDmServiceDaoImpl, paramDm);
                if (updateDm == null) {
                    newUpdateDmFlg = Boolean.TRUE;
                } else {
                    newUpdateDmFlg = Boolean.FALSE;
                }
            }

            if (newUpdateDmFlg) {
                if (aggregateDm.getDelFlg().equals(OsolConstants.FLG_ON)) {
                    //新規で削除データの場合、処理を行わない
                    continue;
                }
                Long aggregateDmId = createId(OsolConstants.ID_SEQUENCE_NAME.AGGREGATE_DM_ID.getVal());
                //取得した集計デマンドIDをリクエストにセット
                aggregateDm.setAggregateDmId(aggregateDmId);
                updateDm = new MAggregateDm();
                pkUpdateDm = new MAggregateDmPK();
                pkUpdateDm.setCorpId(aggregateDm.getCorpId());
                pkUpdateDm.setBuildingId(aggregateDm.getBuildingId());
                pkUpdateDm.setAggregateDmId(aggregateDmId);
                updateDm.setId(pkUpdateDm);
                updateDm.setCreateDate(serverDateTime);
                updateDm.setCreateUserId(loginUserId);
            }

            // 削除フラグをみて、集計日名称と集計日を更新する
            if(!aggregateDm.getDelFlg().equals(OsolConstants.FLG_ON)) {
                // 削除でない場合
                updateDm.setSumDate(aggregateDm.getSumDate());
                updateDm.setAggregateDmName(aggregateDm.getAggregateDmName());
            }
            updateDm.setDelFlg(aggregateDm.getDelFlg());
            updateDm.setUpdateDate(serverDateTime);
            updateDm.setUpdateUserId(loginUserId);

            if (newUpdateDmFlg) {
                persist(mAggregateDmServiceDaoImpl, updateDm);
            } else {
                merge(mAggregateDmServiceDaoImpl, updateDm);
            }

        }

    }

    /**
     * 集計デマンド系統情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     * @throws Exception
     */
    private void updateAggregateDmLine(BuildingDmBaseInfoUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId)
            throws Exception {

        MAggregateDmLine paramDmLine;
        MAggregateDmLine updateDmLine = null;
        MAggregateDmLinePK pkParamDmLine;
        MAggregateDmLinePK pkUpdateDmLine;
        Boolean newUpdateDmLineFlg;

        if (result.getAggregateDmList() == null || result.getAggregateDmList().isEmpty()
                || result.getAggregateDmLineList() == null || result.getAggregateDmLineList().isEmpty()) {
            return;
        }

        for (BuildingDmBaseInfoUpdateDmLineRequestSet aggregateDmLine : result.getAggregateDmLineList()) {
            if (aggregateDmLine.getAggregateDmId() == null) {
                //集計デマンドも新規登録の場合
                newUpdateDmLineFlg = Boolean.TRUE;
                //集計デマンド情報から集計デマンドIDを取得する
                for (BuildingDmBaseInfoUpdateDmRequestSet aggregateDm : result.getAggregateDmList()) {
                    if (aggregateDm.getAggregateRowNo().equals(aggregateDmLine.getAggregateRowNo())) {
                        aggregateDmLine.setAggregateDmId(aggregateDm.getAggregateDmId());
                        break;
                    }
                }
            } else {
                paramDmLine = new MAggregateDmLine();
                pkParamDmLine = new MAggregateDmLinePK();
                pkParamDmLine.setCorpId(aggregateDmLine.getCorpId());
                pkParamDmLine.setBuildingId(aggregateDmLine.getBuildingId());
                pkParamDmLine.setLineGroupId(aggregateDmLine.getLineGroupId());
                pkParamDmLine.setLineNo(aggregateDmLine.getLineNo());
                paramDmLine.setId(pkParamDmLine);
                updateDmLine = find(mAggregateDmLineServiceDaoImpl, paramDmLine);
                if (updateDmLine == null) {
                    newUpdateDmLineFlg = Boolean.TRUE;
                } else {
                    newUpdateDmLineFlg = Boolean.FALSE;
                }
            }

            if (newUpdateDmLineFlg) {
                if (aggregateDmLine.getDelFlg().equals(OsolConstants.FLG_ON)
                        || aggregateDmLine.getAggregateDmId() == null) {
                    //新規で削除データの場合、またはデマンド集計が取得できない場合次のレコードへ
                    continue;
                }

                updateDmLine = new MAggregateDmLine();
                pkUpdateDmLine = new MAggregateDmLinePK();
                pkUpdateDmLine.setCorpId(aggregateDmLine.getCorpId());
                pkUpdateDmLine.setBuildingId(aggregateDmLine.getBuildingId());
                pkUpdateDmLine.setLineGroupId(aggregateDmLine.getLineGroupId());
                pkUpdateDmLine.setLineNo(aggregateDmLine.getLineNo());
                updateDmLine.setId(pkUpdateDmLine);
                updateDmLine.setCreateDate(serverDateTime);
                updateDmLine.setCreateUserId(loginUserId);
            } else {
                if (aggregateDmLine.getDelFlg().equals(OsolConstants.FLG_ON)) {
                    //更新で削除の場合、物理削除を行う
                    remove(mAggregateDmLineServiceDaoImpl, updateDmLine);
                    continue;
                }
            }

            MAggregateDm aggregateDmParam = new MAggregateDm();
            MAggregateDmPK pkAggregateDmParam = new MAggregateDmPK();
            pkAggregateDmParam.setCorpId(aggregateDmLine.getAggregateDmCorpId());
            pkAggregateDmParam.setBuildingId(aggregateDmLine.getAggregateDmBuildingId());
            pkAggregateDmParam.setAggregateDmId(aggregateDmLine.getAggregateDmId());
            aggregateDmParam.setId(pkAggregateDmParam);
            updateDmLine.setMAggregateDm(find(mAggregateDmServiceDaoImpl, aggregateDmParam));
            updateDmLine.setUpdateDate(serverDateTime);
            updateDmLine.setUpdateUserId(loginUserId);

            if (newUpdateDmLineFlg) {
                persist(mAggregateDmLineServiceDaoImpl, updateDmLine);
            } else {
                merge(mAggregateDmLineServiceDaoImpl, updateDmLine);
            }
        }

    }

    /**
     * グラフ要素情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void deleteGraphElementInfo(BuildingDmBaseInfoUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId)
            throws Exception {

        if (result.getBuildingDmDetail() != null && ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF.getVal()
                .equals(result.getBuildingDmDetail().getOutAirTempDispFlg())) {
            //外気温が非表示の場合、グラフ要素設定から削除する
            MGraphElement paramGraph = new MGraphElement();
            MGraphElementPK pkParamGraph = new MGraphElementPK();
            pkParamGraph.setCorpId(result.getCorpId());
            pkParamGraph.setBuildingId(result.getBuildingId());
            paramGraph.setId(pkParamGraph);
            paramGraph.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.AMEDAS.getVal());
            paramGraph.setDelFlg(OsolConstants.FLG_OFF);
            List<MGraphElement> graphList = getResultList(mGraphElementServiceDaoImpl, paramGraph);
            if (graphList != null && !graphList.isEmpty()) {
                for (MGraphElement graph : graphList) {
                    graph.setDelFlg(OsolConstants.FLG_ON);
                    graph.setUpdateDate(serverDateTime);
                    graph.setUpdateUserId(loginUserId);
                    merge(mGraphElementServiceDaoImpl, graph);
                }
            }
        }
    }

    /**
     * 更新後の建物デマンド基本情報を取得する
     * @param result
     * @return
     * @throws Exception
     */
    private BuildingDmBaseInfoUpdateResult getNewBuildingDmBaseInfo(BuildingDmBaseInfoUpdateRequest result)
            throws Exception {

        BuildingDmBaseInfoUpdateResult newResult = new BuildingDmBaseInfoUpdateResult();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingDmBaseInfoUpdateResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData buildingDmParam = EnergySettingUtility
                .getBuildingDemandListParam(result.getCorpId(), result.getBuildingId());
        List<BuildingDemandListDetailResultData> buildingDmList = getResultList(buildingDemandListServiceDaoImpl,
                buildingDmParam);
        if (buildingDmList == null || buildingDmList.size() != 1) {
            return new BuildingDmBaseInfoUpdateResult();
        } else {
            newResult.setBuildingDmDetail(buildingDmList.get(0));
        }

        //集計デマンド情報を取得する
        AggregateDmListDetailResultData aggregateDmParam = EnergySettingUtility
                .getAggregateDmListParam(result.getCorpId(), result.getBuildingId(), null);
        newResult.setAggregateDmList(getResultList(aggregateDmListServiceDaoImpl, aggregateDmParam));

        //集計デマンド系統情報を取得する
        AggregateDmLineListDetailResultData aggregateDmLineParam = EnergySettingUtility.getAggregateDmLineListParam(
                result.getCorpId(), result.getBuildingId(), null, null, null, null, null);
        newResult.setAggregateDmLineList(getResultList(aggregateDmLineListServiceDaoImpl, aggregateDmLineParam));

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());

        return newResult;

    }

}
