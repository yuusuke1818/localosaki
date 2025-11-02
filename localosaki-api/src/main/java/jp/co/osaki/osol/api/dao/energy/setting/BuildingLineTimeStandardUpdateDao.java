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
import jp.co.osaki.osol.api.request.energy.setting.BuildingLineTimeStandardUpdateRequest;
import jp.co.osaki.osol.api.response.energy.setting.BuildingLineTimeStandardUpdateParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineTimeStandardUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingLineTimeStandardListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTimeStandardsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MLineTimeStandard;
import jp.co.osaki.osol.entity.MLineTimeStandardPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 時限標準値データ更新 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingLineTimeStandardUpdateDao extends OsolApiDao<BuildingLineTimeStandardUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MLineTimeStandardsServiceDaoImpl mLineTimeStandardsServiceDaoImpl;
    private final BuildingLineTimeStandardListServiceDaoImpl buildingLineTimeStandardListServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    public BuildingLineTimeStandardUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mLineTimeStandardsServiceDaoImpl = new MLineTimeStandardsServiceDaoImpl();
        buildingLineTimeStandardListServiceDaoImpl = new BuildingLineTimeStandardListServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingLineTimeStandardUpdateResult query(BuildingLineTimeStandardUpdateParameter parameter)
            throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingLineTimeStandardUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingLineTimeStandardUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingLineTimeStandardUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物系統時限標準値の更新
        updateLineTimeStandardData(resultSet, serverDateTime, loginUserId);
        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        return getNewLineTimeStandardData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingLineTimeStandardUpdateRequest result) throws Exception {
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
     * 建物系統時限標準値の更新をする
     * @param result
     */
    private void updateLineTimeStandardData(BuildingLineTimeStandardUpdateRequest result, Timestamp serverDateTime,
            Long loginUserId) throws Exception {
        MLineTimeStandard paramStandard;
        MLineTimeStandard updateStandard = null;
        MLineTimeStandardPK pkParamStandard;
        MLineTimeStandardPK pkUpdateStandard;
        Boolean newStandardFlg;

        for (BuildingLineTimeStandardListDetailResultData detail : result.getDetailList()) {
            if (detail.getTimeList() != null && !detail.getTimeList().isEmpty()) {
                for (BuildingLineTimeStandardListTimeResultData timeDetail : detail.getTimeList()) {
                    paramStandard = new MLineTimeStandard();
                    pkParamStandard = new MLineTimeStandardPK();
                    pkParamStandard.setCorpId(timeDetail.getCorpId());
                    pkParamStandard.setBuildingId(timeDetail.getBuildingId());
                    pkParamStandard.setLineGroupId(timeDetail.getLineGroupId());
                    pkParamStandard.setLineNo(timeDetail.getLineNo());
                    pkParamStandard.setJigenNo(timeDetail.getJigenNo());
                    paramStandard.setId(pkParamStandard);
                    updateStandard = find(mLineTimeStandardsServiceDaoImpl, paramStandard);
                    if (updateStandard == null) {
                        newStandardFlg = Boolean.TRUE;
                        if (OsolConstants.FLG_ON.equals(timeDetail.getDelFlg())) {
                            //新規かつ削除の場合は次のレコードへ
                            continue;
                        }
                        updateStandard = new MLineTimeStandard();
                        pkUpdateStandard = new MLineTimeStandardPK();
                        pkUpdateStandard.setCorpId(timeDetail.getCorpId());
                        pkUpdateStandard.setBuildingId(timeDetail.getBuildingId());
                        pkUpdateStandard.setLineGroupId(timeDetail.getLineGroupId());
                        pkUpdateStandard.setLineNo(timeDetail.getLineNo());
                        pkUpdateStandard.setJigenNo(timeDetail.getJigenNo());
                        updateStandard.setId(pkUpdateStandard);
                        updateStandard.setCreateDate(serverDateTime);
                        updateStandard.setCreateUserId(loginUserId);
                    } else {
                        newStandardFlg = Boolean.FALSE;
                    }

                    updateStandard.setLineLimitStandardKw(timeDetail.getLineLimitStandardKw());
                    updateStandard.setLineLowerStandardKw(timeDetail.getLineLowerStandardKw());
                    updateStandard.setDelFlg(timeDetail.getDelFlg());
                    updateStandard.setVersion(timeDetail.getVersion());
                    updateStandard.setUpdateDate(serverDateTime);
                    updateStandard.setUpdateUserId(loginUserId);

                    if (newStandardFlg) {
                        persist(mLineTimeStandardsServiceDaoImpl, updateStandard);
                    } else {
                        merge(mLineTimeStandardsServiceDaoImpl, updateStandard);
                    }

                }
            }
        }
    }

    /**
     * 更新後の建物系統時限標準値情報を取得する
     * @param result
     * @return
     */
    private BuildingLineTimeStandardUpdateResult getNewLineTimeStandardData(
            BuildingLineTimeStandardUpdateRequest result) throws Exception {
        BuildingLineTimeStandardUpdateResult newResult = new BuildingLineTimeStandardUpdateResult();
        List<BuildingLineTimeStandardListDetailResultData> detailList = new ArrayList<>();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingLineTimeStandardUpdateResult();
        }

        //系統情報を取得する
        LineListDetailResultData paramLine = new LineListDetailResultData();
        paramLine.setCorpId(result.getCorpId());
        paramLine.setLineGroupId(result.getLineGroupId());
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
                paramStandard.setCorpId(result.getCorpId());
                paramStandard.setBuildingId(result.getBuildingId());
                paramStandard.setLineGroupId(result.getLineGroupId());
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

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setLineGroupId(result.getLineGroupId());
        newResult.setDetailList(detailList);
        return newResult;
    }

}
