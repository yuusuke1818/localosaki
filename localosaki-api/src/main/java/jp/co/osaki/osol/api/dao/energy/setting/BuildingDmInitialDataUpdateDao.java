package jp.co.osaki.osol.api.dao.energy.setting;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmInitialDataUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.BuildingDmInitialDataUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmInitialDataUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDmInitialDataSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDmInitialDataSelectMonthDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.BuildingDmInitialDataUtility;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TDmYearRep;
import jp.co.osaki.osol.entity.TDmYearRepLine;
import jp.co.osaki.osol.entity.TDmYearRepLinePK;
import jp.co.osaki.osol.entity.TDmYearRepPK;
import jp.co.osaki.osol.entity.TDmYearRepPoint;
import jp.co.osaki.osol.entity.TDmYearRepPointPK;

/**
 * 建物導入前データ更新 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingDmInitialDataUpdateDao extends OsolApiDao<BuildingDmInitialDataUpdateParameter> {

    //TODO 参照のみのServiceDaoImplをEntityServiceDaoから取得しないようにする
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final TDmYearRepLineServiceDaoImpl tDmYearRepLineServiceDaoImpl;
    private final TDmYearRepPointServiceDaoImpl tDmYearRepPointServiceDaoImpl;
    private final TDmYearRepServiceDaoImpl tDmYearRepServiceDaoImpl;
    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final MBuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    public BuildingDmInitialDataUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        tDmYearRepLineServiceDaoImpl = new TDmYearRepLineServiceDaoImpl();
        tDmYearRepPointServiceDaoImpl = new TDmYearRepPointServiceDaoImpl();
        tDmYearRepServiceDaoImpl = new TDmYearRepServiceDaoImpl();
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        buildingSmPointServiceDaoImpl = new MBuildingSmPointServiceDaoImpl();
        buildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    @Override
    public BuildingDmInitialDataUpdateResult query(BuildingDmInitialDataUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new BuildingDmInitialDataUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingDmInitialDataUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (resultSet.getBuildingId() == null) {
            return new BuildingDmInitialDataUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(resultSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //更新処理を行う
        if (resultSet.getDetailList() != null && !resultSet.getDetailList().isEmpty()) {
            //外部キーの関係上、先に外気温を更新する必要がある
            for (BuildingDmInitialDataSelectDetailResultData detail : resultSet.getDetailList()) {
                if (BuildingDmInitialDataUtility.KIND_ANALOG.equals(detail.getKind())) {
                    if (ApiSimpleConstants.OUT_TEMPERATURE.equals(detail.getName())) {
                        //外気温
                        updateOutTemperatureInitialData(detail, resultSet.getCorpId(), resultSet.getBuildingId(),
                                serverDateTime, loginUserId);
                        break;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }

            //系統とアナログの更新
            for (BuildingDmInitialDataSelectDetailResultData detail : resultSet.getDetailList()) {
                if (BuildingDmInitialDataUtility.KIND_LINE.equals(detail.getKind())) {
                    //系統
                    updateLineInitialData(detail, resultSet.getCorpId(), resultSet.getBuildingId(), serverDateTime,
                            loginUserId);
                } else if (BuildingDmInitialDataUtility.KIND_ANALOG.equals(detail.getKind())) {
                    if (ApiSimpleConstants.OUT_TEMPERATURE.equals(detail.getName())) {
                        continue;
                    } else {
                        //アナログ
                        updateAnalogInitialData(detail, resultSet.getCorpId(), resultSet.getBuildingId(),
                                serverDateTime, loginUserId);
                    }
                }
            }
        }

        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        //更新後のデータを取得する
        return getNewBuildingDmInitialnData(resultSet);
    }

    /**
     * 建物情報の排他チェックを行う
     * @param result
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingDmInitialDataUpdateRequest result) throws Exception {
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
     * 系統の導入前データを更新する
     * @param result
     * @param corpId
     * @param buildingId
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateLineInitialData(BuildingDmInitialDataSelectDetailResultData result, String corpId,
            Long buildingId, Timestamp serverDateTime, Long loginUserId) throws Exception {
        //デマンド年報系統を更新する
        TDmYearRepLine paramLine;
        TDmYearRepLine updateLine;
        TDmYearRepLinePK pkParamLine;
        TDmYearRepLinePK pkUpdateLine;
        Boolean newLineFlg;

        if (result.getMonthDetailList() != null && !result.getMonthDetailList().isEmpty()) {
            for (BuildingDmInitialDataSelectMonthDetailResultData monthDetail : result.getMonthDetailList()) {
                for (int i = 1; i <= 12; i++) {
                    paramLine = new TDmYearRepLine();
                    pkParamLine = new TDmYearRepLinePK();
                    pkParamLine.setCorpId(corpId);
                    pkParamLine.setBuildingId(buildingId);
                    pkParamLine.setLineGroupId(result.getLineGroupId());
                    pkParamLine.setLineNo(result.getLineNo());
                    pkParamLine.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                    pkParamLine.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                    pkParamLine.setMonthNo(new BigDecimal(i));
                    paramLine.setId(pkParamLine);
                    updateLine = find(tDmYearRepLineServiceDaoImpl, paramLine);
                    if (updateLine == null) {
                        newLineFlg = Boolean.TRUE;
                        updateLine = new TDmYearRepLine();
                        pkUpdateLine = new TDmYearRepLinePK();
                        pkUpdateLine.setCorpId(corpId);
                        pkUpdateLine.setBuildingId(buildingId);
                        pkUpdateLine.setLineGroupId(result.getLineGroupId());
                        pkUpdateLine.setLineNo(result.getLineNo());
                        pkUpdateLine.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                        pkUpdateLine.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                        pkUpdateLine.setMonthNo(new BigDecimal(i));
                        updateLine.setId(pkUpdateLine);
                        updateLine.setCreateDate(serverDateTime);
                        updateLine.setCreateUserId(loginUserId);
                    } else {
                        newLineFlg = Boolean.FALSE;
                    }

                    updateLine.setLineMaxKw(null);
                    updateLine.setUpdateDate(serverDateTime);
                    updateLine.setUpdateUserId(loginUserId);
                    //月の値を設定
                    switch (i) {
                    case 1:
                        updateLine.setLineValueKwh(monthDetail.getJan());
                        break;
                    case 2:
                        updateLine.setLineValueKwh(monthDetail.getFeb());
                        break;
                    case 3:
                        updateLine.setLineValueKwh(monthDetail.getMar());
                        break;
                    case 4:
                        updateLine.setLineValueKwh(monthDetail.getApr());
                        break;
                    case 5:
                        updateLine.setLineValueKwh(monthDetail.getMay());
                        break;
                    case 6:
                        updateLine.setLineValueKwh(monthDetail.getJun());
                        break;
                    case 7:
                        updateLine.setLineValueKwh(monthDetail.getJul());
                        break;
                    case 8:
                        updateLine.setLineValueKwh(monthDetail.getAug());
                        break;
                    case 9:
                        updateLine.setLineValueKwh(monthDetail.getSep());
                        break;
                    case 10:
                        updateLine.setLineValueKwh(monthDetail.getOct());
                        break;
                    case 11:
                        updateLine.setLineValueKwh(monthDetail.getNov());
                        break;
                    case 12:
                        updateLine.setLineValueKwh(monthDetail.getDec());
                        break;
                    default:
                        updateLine.setLineValueKwh(null);
                        break;
                    }

                    if (newLineFlg) {
                        persist(tDmYearRepLineServiceDaoImpl, updateLine);
                    } else {
                        merge(tDmYearRepLineServiceDaoImpl, updateLine);
                    }

                }

            }
        }

    }

    /**
     * アナログの導入前データを更新する
     * @param result
     * @param corpId
     * @param buildingId
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateAnalogInitialData(BuildingDmInitialDataSelectDetailResultData result, String corpId,
            Long buildingId, Timestamp serverDateTime, Long loginUserId) throws Exception {
        //デマンド年報ポイントを更新する
        TDmYearRepPoint paramAnalog;
        TDmYearRepPoint updateAnalog;
        TDmYearRepPointPK pkParamAnalog;
        TDmYearRepPointPK pkUpdateAnalog;
        Boolean newAnalogFlg;
        List<BigDecimal> monthAverageList = new ArrayList<>();

        if (result.getMonthDetailList() != null && !result.getMonthDetailList().isEmpty()) {
            for (BuildingDmInitialDataSelectMonthDetailResultData monthDetail : result.getMonthDetailList()) {
                if (BuildingDmInitialDataUtility.COL_AVERAGE.equals(monthDetail.getColumn())) {
                    //平均値の場合、値のみ保持する
                    if (monthAverageList != null && !monthAverageList.isEmpty()) {
                        monthAverageList.clear();
                    }
                    monthAverageList.add(monthDetail.getJan());
                    monthAverageList.add(monthDetail.getFeb());
                    monthAverageList.add(monthDetail.getMar());
                    monthAverageList.add(monthDetail.getApr());
                    monthAverageList.add(monthDetail.getMay());
                    monthAverageList.add(monthDetail.getJun());
                    monthAverageList.add(monthDetail.getJul());
                    monthAverageList.add(monthDetail.getAug());
                    monthAverageList.add(monthDetail.getSep());
                    monthAverageList.add(monthDetail.getOct());
                    monthAverageList.add(monthDetail.getNov());
                    monthAverageList.add(monthDetail.getDec());
                } else {
                    //最大値の場合、データを更新する
                    for (int i = 1; i <= 12; i++) {
                        paramAnalog = new TDmYearRepPoint();
                        pkParamAnalog = new TDmYearRepPointPK();
                        pkParamAnalog.setCorpId(corpId);
                        pkParamAnalog.setBuildingId(buildingId);
                        pkParamAnalog.setSmId(result.getSmId());
                        pkParamAnalog.setPointNo(result.getPointNo());
                        pkParamAnalog.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                        pkParamAnalog.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                        pkParamAnalog.setMonthNo(new BigDecimal(i));
                        paramAnalog.setId(pkParamAnalog);
                        updateAnalog = find(tDmYearRepPointServiceDaoImpl, paramAnalog);
                        if (updateAnalog == null) {
                            newAnalogFlg = Boolean.TRUE;
                            updateAnalog = new TDmYearRepPoint();
                            pkUpdateAnalog = new TDmYearRepPointPK();
                            pkUpdateAnalog.setCorpId(corpId);
                            pkUpdateAnalog.setBuildingId(buildingId);
                            pkUpdateAnalog.setSmId(result.getSmId());
                            pkUpdateAnalog.setPointNo(result.getPointNo());
                            pkUpdateAnalog.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                            pkUpdateAnalog.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                            pkUpdateAnalog.setMonthNo(new BigDecimal(i));
                            updateAnalog.setId(pkUpdateAnalog);
                            updateAnalog.setCreateDate(serverDateTime);
                            updateAnalog.setCreateUserId(loginUserId);
                        } else {
                            newAnalogFlg = Boolean.FALSE;
                        }

                        updateAnalog.setUpdateDate(serverDateTime);
                        updateAnalog.setUpdateUserId(loginUserId);
                        updateAnalog.setPointAvg(monthAverageList.get(i - 1));
                        //月の値を設定
                        switch (i) {
                        case 1:
                            updateAnalog.setPointMax(monthDetail.getJan());
                            break;
                        case 2:
                            updateAnalog.setPointMax(monthDetail.getFeb());
                            break;
                        case 3:
                            updateAnalog.setPointMax(monthDetail.getMar());
                            break;
                        case 4:
                            updateAnalog.setPointMax(monthDetail.getApr());
                            break;
                        case 5:
                            updateAnalog.setPointMax(monthDetail.getMay());
                            break;
                        case 6:
                            updateAnalog.setPointMax(monthDetail.getJun());
                            break;
                        case 7:
                            updateAnalog.setPointMax(monthDetail.getJul());
                            break;
                        case 8:
                            updateAnalog.setPointMax(monthDetail.getAug());
                            break;
                        case 9:
                            updateAnalog.setPointMax(monthDetail.getSep());
                            break;
                        case 10:
                            updateAnalog.setPointMax(monthDetail.getOct());
                            break;
                        case 11:
                            updateAnalog.setPointMax(monthDetail.getNov());
                            break;
                        case 12:
                            updateAnalog.setPointMax(monthDetail.getDec());
                            break;
                        default:
                            updateAnalog.setPointMax(null);
                            break;
                        }

                        if (newAnalogFlg) {
                            persist(tDmYearRepPointServiceDaoImpl, updateAnalog);
                        } else {
                            merge(tDmYearRepPointServiceDaoImpl, updateAnalog);
                        }
                    }

                }
            }
        }

    }

    /**
     * 外気温の導入前データを更新する
     * @param result
     * @param corpId
     * @param buildingId
     * @param serverDateTime
     * @param loginUserId
     */
    private void updateOutTemperatureInitialData(BuildingDmInitialDataSelectDetailResultData result, String corpId,
            Long buildingId, Timestamp serverDateTime, Long loginUserId) throws Exception {
        //デマンド年報を更新する
        TDmYearRep paramAnalog;
        TDmYearRep updateAnalog;
        TDmYearRepPK pkParamAnalog;
        TDmYearRepPK pkUpdateAnalog;
        Boolean newAnalogFlg;
        List<BigDecimal> monthAverageList = new ArrayList<>();

        if (result.getMonthDetailList() != null && !result.getMonthDetailList().isEmpty()) {
            for (BuildingDmInitialDataSelectMonthDetailResultData monthDetail : result.getMonthDetailList()) {
                if (BuildingDmInitialDataUtility.COL_AVERAGE.equals(monthDetail.getColumn())) {
                    //平均値の場合、値のみ保持する
                    if (monthAverageList != null && !monthAverageList.isEmpty()) {
                        monthAverageList.clear();
                    }
                    monthAverageList.add(monthDetail.getJan());
                    monthAverageList.add(monthDetail.getFeb());
                    monthAverageList.add(monthDetail.getMar());
                    monthAverageList.add(monthDetail.getApr());
                    monthAverageList.add(monthDetail.getMay());
                    monthAverageList.add(monthDetail.getJun());
                    monthAverageList.add(monthDetail.getJul());
                    monthAverageList.add(monthDetail.getAug());
                    monthAverageList.add(monthDetail.getSep());
                    monthAverageList.add(monthDetail.getOct());
                    monthAverageList.add(monthDetail.getNov());
                    monthAverageList.add(monthDetail.getDec());
                } else {
                    //最大値の場合、データを更新する
                    for (int i = 1; i <= 12; i++) {
                        paramAnalog = new TDmYearRep();
                        pkParamAnalog = new TDmYearRepPK();
                        pkParamAnalog.setCorpId(corpId);
                        pkParamAnalog.setBuildingId(buildingId);
                        pkParamAnalog.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                        pkParamAnalog.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                        pkParamAnalog.setMonthNo(new BigDecimal(i));
                        paramAnalog.setId(pkParamAnalog);
                        updateAnalog = find(tDmYearRepServiceDaoImpl, paramAnalog);
                        if (updateAnalog == null) {
                            newAnalogFlg = Boolean.TRUE;
                            updateAnalog = new TDmYearRep();
                            pkUpdateAnalog = new TDmYearRepPK();
                            pkUpdateAnalog.setCorpId(corpId);
                            pkUpdateAnalog.setBuildingId(buildingId);
                            pkUpdateAnalog.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                            pkUpdateAnalog.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                            pkUpdateAnalog.setMonthNo(new BigDecimal(i));
                            updateAnalog.setId(pkUpdateAnalog);
                            updateAnalog.setSumDate(BuildingDmInitialDataUtility.SUM_DATE);
                            updateAnalog.setMaxKw(BigDecimal.ZERO);
                            updateAnalog.setMinKw(BigDecimal.ZERO);
                            updateAnalog.setCreateDate(serverDateTime);
                            updateAnalog.setCreateUserId(loginUserId);
                        } else {
                            newAnalogFlg = Boolean.FALSE;
                        }

                        updateAnalog.setUpdateDate(serverDateTime);
                        updateAnalog.setUpdateUserId(loginUserId);
                        updateAnalog.setOutAirTempAvg(monthAverageList.get(i - 1));
                        //月の値を設定
                        switch (i) {
                        case 1:
                            updateAnalog.setOutAirTempMax(monthDetail.getJan());
                            break;
                        case 2:
                            updateAnalog.setOutAirTempMax(monthDetail.getFeb());
                            break;
                        case 3:
                            updateAnalog.setOutAirTempMax(monthDetail.getMar());
                            break;
                        case 4:
                            updateAnalog.setOutAirTempMax(monthDetail.getApr());
                            break;
                        case 5:
                            updateAnalog.setOutAirTempMax(monthDetail.getMay());
                            break;
                        case 6:
                            updateAnalog.setOutAirTempMax(monthDetail.getJun());
                            break;
                        case 7:
                            updateAnalog.setOutAirTempMax(monthDetail.getJul());
                            break;
                        case 8:
                            updateAnalog.setOutAirTempMax(monthDetail.getAug());
                            break;
                        case 9:
                            updateAnalog.setOutAirTempMax(monthDetail.getSep());
                            break;
                        case 10:
                            updateAnalog.setOutAirTempMax(monthDetail.getOct());
                            break;
                        case 11:
                            updateAnalog.setOutAirTempMax(monthDetail.getNov());
                            break;
                        case 12:
                            updateAnalog.setOutAirTempMax(monthDetail.getDec());
                            break;
                        default:
                            updateAnalog.setOutAirTempMax(null);
                            break;
                        }

                        if (newAnalogFlg) {
                            persist(tDmYearRepServiceDaoImpl, updateAnalog);
                        } else {
                            merge(tDmYearRepServiceDaoImpl, updateAnalog);
                        }
                    }

                }
            }
        }

    }

    /**
     * 更新後の導入前データ情報を取得する
     * @param result
     * @return
     */
    private BuildingDmInitialDataUpdateResult getNewBuildingDmInitialnData(BuildingDmInitialDataUpdateRequest result)
            throws Exception {
        BuildingDmInitialDataUpdateResult newResult = new BuildingDmInitialDataUpdateResult();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(result.getCorpId());
        exBuildingParam.setBuildingId(result.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingDmInitialDataUpdateResult();
        }

        List<BuildingDmInitialDataSelectDetailResultData> detailList = new ArrayList<>();

        // 系統
        BuildingDmInitialDataSelectDetailResultData line = getLineAll(result.getCorpId(), result.getBuildingId());
        if (line != null)
            detailList.add(line);

        // アナログ
        List<BuildingDmInitialDataSelectDetailResultData> analog = getAnalogList(result.getCorpId(),
                result.getBuildingId());
        if (analog != null)
            detailList.addAll(analog);

        // 外気温
        BuildingDmInitialDataSelectDetailResultData out = getOutTemperature(result.getCorpId(),
                result.getBuildingId());
        if (out != null)
            detailList.add(out);

        newResult.setCorpId(result.getCorpId());
        newResult.setBuildingId(exBuildingList.get(0).getBuildingId());
        newResult.setBuildingVersion(exBuildingList.get(0).getVersion());
        newResult.setDetailList(detailList);
        return newResult;
    }

    /**
     * 系統
     * @param corpId
     * @param buildingId
     * @return
     */
    private BuildingDmInitialDataSelectDetailResultData getLineAll(String corpId, Long buildingId) throws Exception {

        BuildingDmInitialDataSelectDetailResultData _ret = null;

        // 系統グループ
        LineGroupSearchDetailResultData pLineGrp = new LineGroupSearchDetailResultData();
        pLineGrp.setCorpId(corpId);
        pLineGrp.setLineGroupType(ApiCodeValueConstants.LINE_GROUP_TYPE.CORP_STANDARD.getVal()); // 企業
        List<LineGroupSearchDetailResultData> retLineGrp = getResultList(lineGroupSearchServiceDaoImpl, pLineGrp);
        if (retLineGrp != null && retLineGrp.size() > 0) {
            // 系統
            LineGroupSearchDetailResultData rsLineGrp = retLineGrp.get(0);
            LineListDetailResultData pLine = new LineListDetailResultData();
            pLine.setCorpId(corpId);
            pLine.setLineGroupId(rsLineGrp.getLineGroupId());
            pLine.setLineNo(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal()); // 全体
            List<LineListDetailResultData> retLine = getResultList(lineListServiceDaoImpl, pLine);
            if (retLine != null && retLine.size() > 0) {
                LineListDetailResultData rsLine = retLine.get(0);
                // 全体行
                _ret = new BuildingDmInitialDataSelectDetailResultData();
                _ret.setLineGroupId(rsLine.getLineGroupId());
                _ret.setLineNo(rsLine.getLineNo());
                _ret.setKind(BuildingDmInitialDataUtility.KIND_LINE);
                _ret.setName(rsLine.getLineName());
                BuildingDmInitialDataSelectMonthDetailResultData _setting = new BuildingDmInitialDataSelectMonthDetailResultData();
                _setting.setColumn(BuildingDmInitialDataUtility.COL_USED);
                _setting.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                // デマンド年報系統
                TDmYearRepLinePK _id = new TDmYearRepLinePK();
                _id.setCorpId(corpId);
                _id.setBuildingId(buildingId);
                _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                _id.setLineGroupId(rsLine.getLineGroupId());
                _id.setLineNo(rsLine.getLineNo());
                TDmYearRepLine pDmYearRepLine = new TDmYearRepLine();
                pDmYearRepLine.setId(_id);
                List<TDmYearRepLine> retDmYearRepLine = getResultList(tDmYearRepLineServiceDaoImpl, pDmYearRepLine);
                if (retDmYearRepLine != null) {
                    for (TDmYearRepLine dmYearRepLine : retDmYearRepLine) {
                        switch (dmYearRepLine.getId().getMonthNo().intValue()) {
                        case 1:
                            _setting.setJan(dmYearRepLine.getLineValueKwh());
                            break;
                        case 2:
                            _setting.setFeb(dmYearRepLine.getLineValueKwh());
                            break;
                        case 3:
                            _setting.setMar(dmYearRepLine.getLineValueKwh());
                            break;
                        case 4:
                            _setting.setApr(dmYearRepLine.getLineValueKwh());
                            break;
                        case 5:
                            _setting.setMay(dmYearRepLine.getLineValueKwh());
                            break;
                        case 6:
                            _setting.setJun(dmYearRepLine.getLineValueKwh());
                            break;
                        case 7:
                            _setting.setJul(dmYearRepLine.getLineValueKwh());
                            break;
                        case 8:
                            _setting.setAug(dmYearRepLine.getLineValueKwh());
                            break;
                        case 9:
                            _setting.setSep(dmYearRepLine.getLineValueKwh());
                            break;
                        case 10:
                            _setting.setOct(dmYearRepLine.getLineValueKwh());
                            break;
                        case 11:
                            _setting.setNov(dmYearRepLine.getLineValueKwh());
                            break;
                        case 12:
                            _setting.setDec(dmYearRepLine.getLineValueKwh());
                            break;
                        }
                    }
                }
                _ret.addSetting(_setting);
            }
        }
        return _ret;
    }

    /**
     * アナログ
     * @param corpId
     * @param buildingId
     * @return
     */
    private List<BuildingDmInitialDataSelectDetailResultData> getAnalogList(String corpId, Long buildingId)
            throws Exception {

        List<BuildingDmInitialDataSelectDetailResultData> _ret = null;

        // 建物機器ポイント
        MBuildingSmPointPK id = new MBuildingSmPointPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        MBuildingSmPoint pBuildingSmPoint = new MBuildingSmPoint();
        pBuildingSmPoint.setId(id);
        MSmPoint smPoint = new MSmPoint();
        smPoint.setPointType(ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal()); // アナログ
        pBuildingSmPoint.setMSmPoint(smPoint);
        pBuildingSmPoint.setPointSumFlg(ApiCodeValueConstants.POINT_SUM_FLG.FLG_ON.getVal());
        List<MBuildingSmPoint> retBuildingSmPoint = getResultList(buildingSmPointServiceDaoImpl, pBuildingSmPoint);
        if (retBuildingSmPoint != null) {
            _ret = new ArrayList<>();
            for (MBuildingSmPoint buildingSmPoint : retBuildingSmPoint) {
                BuildingDmInitialDataSelectDetailResultData _rs = new BuildingDmInitialDataSelectDetailResultData();
                _rs.setKind(BuildingDmInitialDataUtility.KIND_ANALOG);
                _rs.setSmId(buildingSmPoint.getId().getSmId());
                _rs.setPointNo(buildingSmPoint.getId().getPointNo());
                _rs.setName(buildingSmPoint.getPointName());
                // 平均値
                BuildingDmInitialDataSelectMonthDetailResultData _ave = new BuildingDmInitialDataSelectMonthDetailResultData();
                _ave.setColumn(BuildingDmInitialDataUtility.COL_AVERAGE);
                _ave.setUnit(buildingSmPoint.getPointUnit());
                // 最大値
                BuildingDmInitialDataSelectMonthDetailResultData _max = new BuildingDmInitialDataSelectMonthDetailResultData();
                _max.setColumn(BuildingDmInitialDataUtility.COL_MAX);
                _max.setUnit(buildingSmPoint.getPointUnit());
                // デマンド年報ポイント
                TDmYearRepPointPK _id = new TDmYearRepPointPK();
                _id.setCorpId(corpId);
                _id.setBuildingId(buildingId);
                _id.setSmId(buildingSmPoint.getId().getSmId());
                _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                _id.setPointNo(buildingSmPoint.getId().getPointNo());
                TDmYearRepPoint pDmYearRepPoint = new TDmYearRepPoint();
                pDmYearRepPoint.setId(_id);
                List<TDmYearRepPoint> retDmYearRepPoint = getResultList(tDmYearRepPointServiceDaoImpl, pDmYearRepPoint);
                if (retDmYearRepPoint != null) {
                    for (TDmYearRepPoint dmYearRepPoint : retDmYearRepPoint) {
                        switch (dmYearRepPoint.getId().getMonthNo().intValue()) {
                        case 1:
                            _ave.setJan(dmYearRepPoint.getPointAvg());
                            _max.setJan(dmYearRepPoint.getPointMax());
                            break;
                        case 2:
                            _ave.setFeb(dmYearRepPoint.getPointAvg());
                            _max.setFeb(dmYearRepPoint.getPointMax());
                            break;
                        case 3:
                            _ave.setMar(dmYearRepPoint.getPointAvg());
                            _max.setMar(dmYearRepPoint.getPointMax());
                            break;
                        case 4:
                            _ave.setApr(dmYearRepPoint.getPointAvg());
                            _max.setApr(dmYearRepPoint.getPointMax());
                            break;
                        case 5:
                            _ave.setMay(dmYearRepPoint.getPointAvg());
                            _max.setMay(dmYearRepPoint.getPointMax());
                            break;
                        case 6:
                            _ave.setJun(dmYearRepPoint.getPointAvg());
                            _max.setJun(dmYearRepPoint.getPointMax());
                            break;
                        case 7:
                            _ave.setJul(dmYearRepPoint.getPointAvg());
                            _max.setJul(dmYearRepPoint.getPointMax());
                            break;
                        case 8:
                            _ave.setAug(dmYearRepPoint.getPointAvg());
                            _max.setAug(dmYearRepPoint.getPointMax());
                            break;
                        case 9:
                            _ave.setSep(dmYearRepPoint.getPointAvg());
                            _max.setSep(dmYearRepPoint.getPointMax());
                            break;
                        case 10:
                            _ave.setOct(dmYearRepPoint.getPointAvg());
                            _max.setOct(dmYearRepPoint.getPointMax());
                            break;
                        case 11:
                            _ave.setNov(dmYearRepPoint.getPointAvg());
                            _max.setNov(dmYearRepPoint.getPointMax());
                            break;
                        case 12:
                            _ave.setDec(dmYearRepPoint.getPointAvg());
                            _max.setDec(dmYearRepPoint.getPointMax());
                            break;
                        }
                    }
                }
                _rs.addSetting(_ave);
                _rs.addSetting(_max);
                _ret.add(_rs);
            }
        }
        return _ret;
    }

    /**
     * 外気温
     * @param corpId
     * @param buildingId
     * @return
     */
    private BuildingDmInitialDataSelectDetailResultData getOutTemperature(String corpId, Long buildingId)
            throws Exception {

        BuildingDmInitialDataSelectDetailResultData _ret = null;

        // 外気温表示
        if (!isOutAirTempDisp(corpId, buildingId))
            return null;

        // デマンド年報
        TDmYearRepPK _id = new TDmYearRepPK();
        _id.setCorpId(corpId);
        _id.setBuildingId(buildingId);
        _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
        _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
        TDmYearRep param = new TDmYearRep();
        param.setId(_id);
        List<TDmYearRep> retDmYearRep = getResultList(tDmYearRepServiceDaoImpl, param);
        if (retDmYearRep != null) {
            _ret = new BuildingDmInitialDataSelectDetailResultData();
            _ret.setKind(BuildingDmInitialDataUtility.KIND_ANALOG);
            _ret.setName(ApiSimpleConstants.OUT_TEMPERATURE);
            _ret.setOutTemp(true);
            // 平均値
            BuildingDmInitialDataSelectMonthDetailResultData _ave = new BuildingDmInitialDataSelectMonthDetailResultData();
            _ave.setColumn(BuildingDmInitialDataUtility.COL_AVERAGE);
            _ave.setUnit(BuildingDmInitialDataUtility.UNIT_TEMP);
            // 最大値
            BuildingDmInitialDataSelectMonthDetailResultData _max = new BuildingDmInitialDataSelectMonthDetailResultData();
            _max.setColumn(BuildingDmInitialDataUtility.COL_MAX);
            _max.setUnit(BuildingDmInitialDataUtility.UNIT_TEMP);

            for (TDmYearRep dmYearRep : retDmYearRep) {
                switch (dmYearRep.getId().getMonthNo().intValue()) {
                case 1:
                    _ave.setJan(dmYearRep.getOutAirTempAvg());
                    _max.setJan(dmYearRep.getOutAirTempMax());
                    break;
                case 2:
                    _ave.setFeb(dmYearRep.getOutAirTempAvg());
                    _max.setFeb(dmYearRep.getOutAirTempMax());
                    break;
                case 3:
                    _ave.setMar(dmYearRep.getOutAirTempAvg());
                    _max.setMar(dmYearRep.getOutAirTempMax());
                    break;
                case 4:
                    _ave.setApr(dmYearRep.getOutAirTempAvg());
                    _max.setApr(dmYearRep.getOutAirTempMax());
                    break;
                case 5:
                    _ave.setMay(dmYearRep.getOutAirTempAvg());
                    _max.setMay(dmYearRep.getOutAirTempMax());
                    break;
                case 6:
                    _ave.setJun(dmYearRep.getOutAirTempAvg());
                    _max.setJun(dmYearRep.getOutAirTempMax());
                    break;
                case 7:
                    _ave.setJul(dmYearRep.getOutAirTempAvg());
                    _max.setJul(dmYearRep.getOutAirTempMax());
                    break;
                case 8:
                    _ave.setAug(dmYearRep.getOutAirTempAvg());
                    _max.setAug(dmYearRep.getOutAirTempMax());
                    break;
                case 9:
                    _ave.setSep(dmYearRep.getOutAirTempAvg());
                    _max.setSep(dmYearRep.getOutAirTempMax());
                    break;
                case 10:
                    _ave.setOct(dmYearRep.getOutAirTempAvg());
                    _max.setOct(dmYearRep.getOutAirTempMax());
                    break;
                case 11:
                    _ave.setNov(dmYearRep.getOutAirTempAvg());
                    _max.setNov(dmYearRep.getOutAirTempMax());
                    break;
                case 12:
                    _ave.setDec(dmYearRep.getOutAirTempAvg());
                    _max.setDec(dmYearRep.getOutAirTempMax());
                    break;
                }
            }
            _ret.addSetting(_ave);
            _ret.addSetting(_max);
        }

        return _ret;
    }

    /**
     * 外気温表示
     * @param corpId
     * @param buildingId
     * @return
     */
    private boolean isOutAirTempDisp(String corpId, Long buildingId) throws Exception {
        // 建物デマンド
        MBuildingDmPK id = new MBuildingDmPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        MBuildingDm pBuildingDm = new MBuildingDm();
        pBuildingDm.setId(id);
        MBuildingDm retBuildingDm = find(buildingDmServiceDaoImpl, pBuildingDm);
        if (retBuildingDm == null)
            return false;
        // 外気温表示フラグ
        return retBuildingDm.getOutAirTempDispFlg() == 1;
    }

}
