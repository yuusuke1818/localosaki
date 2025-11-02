package jp.co.osaki.osol.api.dao.energy.setting;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLinePointListUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingLinePointListUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLinePointListUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLinePointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLinePointListLineResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MGraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLinePointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.MSmPointPK;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物系統ポイント一覧更新 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingLinePointListUpdateDao extends OsolApiDao<BuildingLinePointListUpdateParameter> {

    //TODO 最後の更新後データ取得は、EntityServiceDaoを使わない
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;
    private final MSmPointServiceDaoImpl mSmPointServiceDaoImpl;
    private final MBuildingSmPointServiceDaoImpl mBuildingSmPointServiceDaoImpl;
    private final MSmLinePointServiceDaoImpl mSmLinePointServiceDaoImpl;
    private final MGraphElementServiceDaoImpl mGraphElementServiceDaoImpl;
    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    public BuildingLinePointListUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
        mSmPointServiceDaoImpl = new MSmPointServiceDaoImpl();
        mBuildingSmPointServiceDaoImpl = new MBuildingSmPointServiceDaoImpl();
        mSmLinePointServiceDaoImpl = new MSmLinePointServiceDaoImpl();
        mGraphElementServiceDaoImpl = new MGraphElementServiceDaoImpl();
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    @Override
    public BuildingLinePointListUpdateResult query(BuildingLinePointListUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;
        MSmPrm exSm;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingLinePointListUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingLinePointListUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingLinePointListUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器情報の排他チェック
        if (resultSet.getSmId() == null) {
            return new BuildingLinePointListUpdateResult();
        } else {
            exSm = smExclusiveCheck(resultSet);
            if (exSm == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器ポイントの更新
        updateSmPointData(resultSet, serverDateTime, loginUserId);
        //建物機器ポイントの更新
        updateBuildingSmPointData(resultSet, serverDateTime, loginUserId);
        //機器系統ポイントの更新
        updateLineSmPointData(resultSet, serverDateTime, loginUserId);
        //グラフ要素情報の更新
        deleteGraphElementData(resultSet, serverDateTime, loginUserId);

        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);
        //機器情報の更新
        exSm.setUpdateDate(serverDateTime);
        exSm.setUpdateUserId(loginUserId);
        merge(mSmPrmServiceDaoImpl, exSm);

        //更新後の情報を取得する
        return getNewPointData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingLinePointListUpdateRequest result) throws Exception {
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
     * 機器の排他チェックを行う
     * @param result
     * @return
     */
    private MSmPrm smExclusiveCheck(BuildingLinePointListUpdateRequest result) throws Exception {
        MSmPrm smParam = new MSmPrm();
        smParam.setSmId(result.getSmId());
        MSmPrm exSm = find(mSmPrmServiceDaoImpl, smParam);
        if (exSm == null || !exSm.getVersion().equals(result.getSmVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exSm;
        }
    }

    /**
     * 機器ポイント情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateSmPointData(BuildingLinePointListUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MSmPoint paramPoint;
        MSmPoint updatePoint = null;
        MSmPointPK pkParamPoint;
        MSmPointPK pkUpdatePoint;
        Boolean newPointFlg;

        for (BuildingLinePointListDetailResultData detail : result.getPointList()) {
            //機器ポイント情報の更新
            paramPoint = new MSmPoint();
            pkParamPoint = new MSmPointPK();
            pkParamPoint.setSmId(result.getSmId());
            pkParamPoint.setPointNo(detail.getPointNo());
            paramPoint.setId(pkParamPoint);
            updatePoint = find(mSmPointServiceDaoImpl, paramPoint);
            if (updatePoint == null) {
                newPointFlg = Boolean.TRUE;
                updatePoint = new MSmPoint();
                pkUpdatePoint = new MSmPointPK();
                pkUpdatePoint.setSmId(result.getSmId());
                pkUpdatePoint.setPointNo(detail.getPointNo());
                updatePoint.setId(pkUpdatePoint);
                updatePoint.setCreateDate(serverDateTime);
                updatePoint.setCreateUserId(loginUserId);
            } else {
                newPointFlg = Boolean.FALSE;
            }

            updatePoint.setPointType(detail.getPointType());
            if (detail.getDmCorrectionFactor() == null) {
                updatePoint.setDmCorrectionFactor(BigDecimal.ONE);
            } else {
                updatePoint.setDmCorrectionFactor(detail.getDmCorrectionFactor());
            }

            if (detail.getAnalogOffSetValue() == null) {
                updatePoint.setAnalogOffSetValue(BigDecimal.ZERO);
            } else {
                updatePoint.setAnalogOffSetValue(detail.getAnalogOffSetValue());
            }

            if (detail.getAnalogConversionFactor() == null) {
                updatePoint.setAnalogConversionFactor(BigDecimal.ONE);
            } else {
                updatePoint.setAnalogConversionFactor(detail.getAnalogConversionFactor());
            }

            updatePoint.setUpdateDate(serverDateTime);
            updatePoint.setUpdateUserId(loginUserId);

            if (newPointFlg) {
                persist(mSmPointServiceDaoImpl, updatePoint);
            } else {
                merge(mSmPointServiceDaoImpl, updatePoint);
            }
        }
    }

    /**
     * 建物機器ポイント情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateBuildingSmPointData(BuildingLinePointListUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MBuildingSmPoint paramPoint;
        MBuildingSmPoint updatePoint = null;
        MBuildingSmPointPK pkParamPoint;
        MBuildingSmPointPK pkUpdatePoint;
        Boolean newPointFlg;

        for (BuildingLinePointListDetailResultData detail : result.getPointList()) {
            //建物機器ポイントの更新
            paramPoint = new MBuildingSmPoint();
            pkParamPoint = new MBuildingSmPointPK();
            pkParamPoint.setCorpId(result.getCorpId());
            pkParamPoint.setBuildingId(result.getBuildingId());
            pkParamPoint.setSmId(result.getSmId());
            pkParamPoint.setPointNo(detail.getPointNo());
            paramPoint.setId(pkParamPoint);
            updatePoint = find(mBuildingSmPointServiceDaoImpl, paramPoint);
            if (updatePoint == null) {
                if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                    //新規かつ削除の場合、次のレコードへ
                    continue;
                }
                newPointFlg = Boolean.TRUE;
                updatePoint = new MBuildingSmPoint();
                pkUpdatePoint = new MBuildingSmPointPK();
                pkUpdatePoint.setCorpId(result.getCorpId());
                pkUpdatePoint.setBuildingId(result.getBuildingId());
                pkUpdatePoint.setSmId(result.getSmId());
                pkUpdatePoint.setPointNo(detail.getPointNo());
                updatePoint.setCreateDate(serverDateTime);
                updatePoint.setCreateUserId(loginUserId);
                updatePoint.setId(pkUpdatePoint);
            } else {
                newPointFlg = Boolean.FALSE;
            }

            updatePoint.setPointName(detail.getPointName());
            updatePoint.setPointUnit(detail.getPointUnit());
            updatePoint.setPointSumFlg(detail.getPointSumFlg());
            updatePoint.setDelFlg(detail.getDelFlg());
            updatePoint.setUpdateDate(serverDateTime);
            updatePoint.setUpdateUserId(loginUserId);

            if (newPointFlg) {
                persist(mBuildingSmPointServiceDaoImpl, updatePoint);
            } else {
                merge(mBuildingSmPointServiceDaoImpl, updatePoint);
            }

        }

    }

    /**
     * 機器系統ポイントを更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateLineSmPointData(BuildingLinePointListUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MSmLinePoint paramLine;
        MSmLinePoint updateLine = null;
        MSmLinePointPK pkParamLine;
        MSmLinePointPK pkUpdateLine;
        Boolean newLineFlg;

        for (BuildingLinePointListDetailResultData detail : result.getPointList()) {
            if (detail.getSmLinePointList() != null && !detail.getSmLinePointList().isEmpty()) {
                for (BuildingLinePointListLineResultData line : detail.getSmLinePointList()) {
                    paramLine = new MSmLinePoint();
                    pkParamLine = new MSmLinePointPK();
                    pkParamLine.setCorpId(result.getCorpId());
                    pkParamLine.setBuildingId(result.getBuildingId());
                    pkParamLine.setSmId(result.getSmId());
                    pkParamLine.setPointNo(detail.getPointNo());
                    pkParamLine.setLineGroupId(result.getLineGroupId());
                    pkParamLine.setLineNo(line.getLineNo());
                    paramLine.setId(pkParamLine);
                    updateLine = find(mSmLinePointServiceDaoImpl, paramLine);
                    if (updateLine == null) {
                        newLineFlg = Boolean.TRUE;
                        if (OsolConstants.FLG_ON.equals(detail.getDelFlg())
                                || OsolConstants.FLG_ON.equals(line.getDelFlg())) {
                            //新規で削除の場合は処理をしない
                            continue;
                        }
                        updateLine = new MSmLinePoint();
                        pkUpdateLine = new MSmLinePointPK();
                        pkUpdateLine.setCorpId(result.getCorpId());
                        pkUpdateLine.setBuildingId(result.getBuildingId());
                        pkUpdateLine.setSmId(result.getSmId());
                        pkUpdateLine.setPointNo(detail.getPointNo());
                        pkUpdateLine.setLineGroupId(result.getLineGroupId());
                        pkUpdateLine.setLineNo(line.getLineNo());
                        updateLine.setId(pkUpdateLine);
                        updateLine.setCreateDate(serverDateTime);
                        updateLine.setCreateUserId(loginUserId);
                    } else {
                        newLineFlg = Boolean.FALSE;
                    }

                    updateLine.setPointCalcType(line.getPointCalcType());
                    updateLine.setComment(line.getComment());
                    if (OsolConstants.FLG_ON.equals(detail.getDelFlg())) {
                        //建物機器ポイントが削除の場合、削除
                        updateLine.setDelFlg(OsolConstants.FLG_ON);
                    } else {
                        updateLine.setDelFlg(line.getDelFlg());
                    }
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);

                    if (newLineFlg) {
                        persist(mSmLinePointServiceDaoImpl, updateLine);
                    } else {
                        merge(mSmLinePointServiceDaoImpl, updateLine);
                    }
                }
            }
        }
    }

    /**
     * グラフ要素情報を更新する
     * @param result
     * @param serverDateTime
     * @param loginUserId
     */
    private void deleteGraphElementData(BuildingLinePointListUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MGraphElement paramElement;
        MGraphElementPK pkParamElement;

        for (BuildingLinePointListDetailResultData detail : result.getPointList()) {
            if (ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal().equals(detail.getPointType())
                    && ApiCodeValueConstants.POINT_SUM_FLG.FLG_OFF.getVal().equals(detail.getPointSumFlg())) {
                //アナログかつポイント集計フラグがOFFの場合、グラフ要素情報を削除する
                paramElement = new MGraphElement();
                pkParamElement = new MGraphElementPK();
                pkParamElement.setCorpId(result.getCorpId());
                pkParamElement.setBuildingId(result.getBuildingId());
                pkParamElement.setLineGroupId(result.getLineGroupId());
                paramElement.setId(pkParamElement);
                paramElement.setGraphElementType(ApiGenericTypeConstants.GRAPH_ELEMENT_TYPE.ANALOG.getVal());
                paramElement.setGraphSmId(result.getSmId());
                paramElement.setGraphPointNo(detail.getPointNo());
                paramElement.setDelFlg(OsolConstants.FLG_OFF);
                List<MGraphElement> elementList = getResultList(mGraphElementServiceDaoImpl, paramElement);
                if (elementList != null && !elementList.isEmpty()) {
                    for (MGraphElement element : elementList) {
                        element.setDelFlg(OsolConstants.FLG_ON);
                        element.setUpdateDate(serverDateTime);
                        element.setUpdateUserId(loginUserId);
                        merge(mGraphElementServiceDaoImpl, element);
                    }
                }
            }
        }

    }

    /**
     * 更新後のポイント情報を取得する
     * @param result
     * @return
     */
    private BuildingLinePointListUpdateResult getNewPointData(BuildingLinePointListUpdateRequest result)
            throws Exception {
        BuildingLinePointListUpdateResult newResult = new BuildingLinePointListUpdateResult();
        List<BuildingLinePointListDetailResultData> detailList = new ArrayList<>();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingLinePointListUpdateResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(result.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new BuildingLinePointListUpdateResult();
        }

        //機器ポイントを取得する
        SmPointListDetailResultData paramSmPoint = new SmPointListDetailResultData();
        paramSmPoint.setSmId(result.getSmId());
        List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, paramSmPoint);
        if (smPointList != null && !smPointList.isEmpty()) {
            for (SmPointListDetailResultData smPoint : smPointList) {
                BuildingLinePointListDetailResultData detail = new BuildingLinePointListDetailResultData();
                detail.setPointNo(smPoint.getPointNo());
                detail.setPointType(smPoint.getPointType());
                detail.setDmCorrectionFactor(smPoint.getDmCorrectionFactor());
                detail.setAnalogOffSetValue(smPoint.getAnalogOffSetValue());
                detail.setAnalogConversionFactor(smPoint.getAnalogConversionFactor());
                detail.setVersionSmPoint(smPoint.getVersion());
                //建物機器ポイントを取得する
                DemandBuildingSmPointListDetailResultData paramBuildingPoint = new DemandBuildingSmPointListDetailResultData();
                paramBuildingPoint.setCorpId(result.getCorpId());
                paramBuildingPoint.setBuildingId(result.getBuildingId());
                paramBuildingPoint.setSmId(result.getSmId());
                paramBuildingPoint.setPointNo(smPoint.getPointNo());
                List<DemandBuildingSmPointListDetailResultData> buildingPointList = getResultList(
                        demandBuildingSmPointListServiceDaoImpl, paramBuildingPoint);
                if (buildingPointList != null && buildingPointList.size() == 1) {
                    detail.setPointName(buildingPointList.get(0).getPointName());
                    detail.setPointUnit(buildingPointList.get(0).getPointUnit());
                    detail.setPointSumFlg(buildingPointList.get(0).getPointSumFlg());
                    detail.setDelFlg(buildingPointList.get(0).getDelFlg());
                    detail.setVersionBuildingSmPoint(buildingPointList.get(0).getVersion());
                }

                //系統情報を取得する
                List<BuildingLinePointListLineResultData> lineDetailList = new ArrayList<>();
                LineListDetailResultData paramLine = new LineListDetailResultData();
                paramLine.setCorpId(result.getCorpId());
                paramLine.setLineGroupId(result.getLineGroupId());
                List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, paramLine);
                if (lineList != null && !lineList.isEmpty()) {
                    for (LineListDetailResultData line : lineList) {
                        BuildingLinePointListLineResultData lineDetail = new BuildingLinePointListLineResultData();
                        lineDetail.setLineNo(line.getLineNo());
                        lineDetail.setLineName(line.getLineName());
                        lineDetail.setInputEnableFlg(line.getInputEnableFlg());
                        //機器系統ポイント情報を取得する
                        MSmLinePoint paramLinePoint = new MSmLinePoint();
                        MSmLinePointPK pkParamLinePoint = new MSmLinePointPK();
                        pkParamLinePoint.setCorpId(result.getCorpId());
                        pkParamLinePoint.setBuildingId(result.getBuildingId());
                        pkParamLinePoint.setLineGroupId(result.getLineGroupId());
                        pkParamLinePoint.setLineNo(line.getLineNo());
                        pkParamLinePoint.setSmId(result.getSmId());
                        pkParamLinePoint.setPointNo(smPoint.getPointNo());
                        paramLinePoint.setId(pkParamLinePoint);
                        MSmLinePoint linePointData = find(mSmLinePointServiceDaoImpl, paramLinePoint);
                        if (linePointData != null) {
                            lineDetail.setPointCalcType(linePointData.getPointCalcType());
                            lineDetail.setComment(linePointData.getComment());
                            lineDetail.setVersion(linePointData.getVersion());
                            lineDetail.setDelFlg(linePointData.getDelFlg());
                        }
                        lineDetailList.add(lineDetail);
                    }
                }

                detail.setSmLinePointList(lineDetailList);
                detailList.add(detail);
            }
        }

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setSmId(exSmList.get(0).getSmId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setSmVersion(exSmList.get(0).getVersion());
        newResult.setLineGroupId(newResult.getLineGroupId());
        newResult.setPointList(detailList);
        return newResult;
    }

}
