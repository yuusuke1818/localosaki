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
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDemandUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDemandUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDemandUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物デマンド更新 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingDemandUpdateDao extends OsolApiDao<BuildingDemandUpdateParameter> {

    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl mBuildingDmServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    public BuildingDemandUpdateDao() {
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mBuildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public BuildingDemandUpdateResult query(BuildingDemandUpdateParameter parameter) throws Exception {
        MCorp exCorp;
        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        if (parameter.getResultSet() == null) {
            return new BuildingDemandUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingDemandUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //企業情報の排他チェック
        if (CheckUtility.isNullOrEmpty(resultSet.getCorpId()) || resultSet.getDetail() == null) {
            return new BuildingDemandUpdateResult();
        } else {
            exCorp = corpExclusiveCheck(resultSet);
            if (exCorp == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingDemandUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物デマンド情報の更新
        updateBuildingDemand(resultSet, serverDateTime, loginUserId);
        //グラフ要素情報の更新
        deleteGraphElementInfo(resultSet, serverDateTime, loginUserId);
        //企業情報の更新
        exCorp.setUpdateDate(serverDateTime);
        exCorp.setUpdateUserId(loginUserId);
        merge(mCorpApiServiceDaoImpl, exCorp);
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        //更新後のデータを再取得
        return getNewBuildingDemandData(resultSet);
    }

    /**
     * 企業情報の排他チェックを行う
     * @param result
     * @return
     */
    private MCorp corpExclusiveCheck(BuildingDemandUpdateRequest result) throws Exception {
        MCorp corpParam = new MCorp();
        corpParam.setCorpId(result.getCorpId());
        MCorp exCorp = find(mCorpApiServiceDaoImpl, corpParam);
        if (exCorp == null || !exCorp.getVersion().equals(result.getCorpVersion())) {
            //排他制御のデータがない場合または前に保持をしていたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exCorp;
        }
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingDemandUpdateRequest result) throws Exception {
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
    private void updateBuildingDemand(BuildingDemandUpdateRequest result, Timestamp serverDateTime, Long loginUserId)
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
        if (result.getDetail().getWorkStartTime() != null) {
            updateDemand.setWorkStartTime(new Time(result.getDetail().getWorkStartTime().getTime()));
        } else {
            updateDemand.setWorkStartTime(null);
        }
        if (result.getDetail().getShopOpenTime() != null) {
            updateDemand.setShopOpenTime(new Time(result.getDetail().getShopOpenTime().getTime()));
        } else {
            updateDemand.setShopOpenTime(null);
        }
        if (result.getDetail().getShopCloseTime() != null) {
            updateDemand.setShopCloseTime(new Time(result.getDetail().getShopCloseTime().getTime()));
        } else {
            updateDemand.setShopCloseTime(null);
        }
        if (result.getDetail().getWorkEndTime() != null) {
            updateDemand.setWorkEndTime(new Time(result.getDetail().getWorkEndTime().getTime()));
        } else {
            updateDemand.setWorkEndTime(null);
        }

        // 目標
        updateDemand.setTargetKwhMonth1(result.getDetail().getTargetKwhMonth1());
        updateDemand.setTargetKwhMonth2(result.getDetail().getTargetKwhMonth2());
        updateDemand.setTargetKwhMonth3(result.getDetail().getTargetKwhMonth3());
        updateDemand.setTargetKwhMonth4(result.getDetail().getTargetKwhMonth4());
        updateDemand.setTargetKwhMonth5(result.getDetail().getTargetKwhMonth5());
        updateDemand.setTargetKwhMonth6(result.getDetail().getTargetKwhMonth6());
        updateDemand.setTargetKwhMonth7(result.getDetail().getTargetKwhMonth7());
        updateDemand.setTargetKwhMonth8(result.getDetail().getTargetKwhMonth8());
        updateDemand.setTargetKwhMonth9(result.getDetail().getTargetKwhMonth9());
        updateDemand.setTargetKwhMonth10(result.getDetail().getTargetKwhMonth10());
        updateDemand.setTargetKwhMonth11(result.getDetail().getTargetKwhMonth11());
        updateDemand.setTargetKwhMonth12(result.getDetail().getTargetKwhMonth12());
        // 目標
        updateDemand.setStandardTargetContrastRate(result.getDetail().getStandardTargetContrastRate());
        updateDemand.setExcellentTargetContrastRate(result.getDetail().getExcellentTargetContrastRate());
        updateDemand.setContractKw(result.getDetail().getContractKw());
        // CO2排出量計算用係数
        updateDemand.setCo2EngTypeCd(result.getDetail().getCo2EngTypeCd());
        updateDemand.setCo2EngId(result.getDetail().getCo2EngId());
        // デマンド警報通知メール
        updateDemand.setFacilitiesMailAddress1(result.getDetail().getFacilitiesMailAddress1());
        updateDemand.setFacilitiesMailAddress2(result.getDetail().getFacilitiesMailAddress2());
        updateDemand.setFacilitiesMailAddress3(result.getDetail().getFacilitiesMailAddress3());
        updateDemand.setFacilitiesMailAddress4(result.getDetail().getFacilitiesMailAddress4());
        updateDemand.setFacilitiesMailAddress5(result.getDetail().getFacilitiesMailAddress5());
        // 外気温
        updateDemand.setOutAirTempDispFlg(result.getDetail().getOutAirTempDispFlg());

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
     * グラフ要素情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void deleteGraphElementInfo(BuildingDemandUpdateRequest result, Timestamp serverDateTime, Long loginUserId)
            throws Exception {

        if (result.getDetail() != null && ApiCodeValueConstants.OUT_AIR_TEMP_DISP_FLG.FLG_OFF.getVal()
                .equals(result.getDetail().getOutAirTempDispFlg())) {
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
     * 更新後の建物デマンド情報を取得する
     * @param result
     * @return
     */
    private BuildingDemandUpdateResult getNewBuildingDemandData(BuildingDemandUpdateRequest result) throws Exception {
        BuildingDemandUpdateResult newResult = new BuildingDemandUpdateResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(result.getCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        if (exCorpList == null || exCorpList.size() != 1) {
            return new BuildingDemandUpdateResult();
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingDemandUpdateResult();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = new BuildingDemandListDetailResultData();
        demandParam.setCorpId(result.getCorpId());
        demandParam.setBuildingId(result.getBuildingId());
        List<BuildingDemandListDetailResultData> resultList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);
        if (resultList == null || resultList.size() != 1) {
            return new BuildingDemandUpdateResult();
        } else {
            newResult.setCorpId(exCorpList.get(0).getCorpId());
            newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
            newResult.setCorpVersion(exCorpList.get(0).getVersion());
            newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
            newResult.setDetail(resultList.get(0));
        }

        return newResult;
    }
}
