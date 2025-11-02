/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.verify;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandVerifySettingUpdateParameter;
import jp.co.osaki.osol.api.request.energy.verify.DemandVerifySettingUpdateRequest;
import jp.co.osaki.osol.api.result.energy.verify.DemandVerifySettingUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmLineControlLoadVerifyListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmLineVerifyListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmControlLoadServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineControlLoadVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineVerifyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerifyPK;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.entity.MSmLineVerifyPK;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * デマンド検証設定変更更新 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class DemandVerifySettingUpdateDao extends OsolApiDao<DemandVerifySettingUpdateParameter> {

    private final MSmLineVerifyServiceDaoImpl smLineVerifyServiceDaoImpl;
    private final MSmControlLoadServiceDaoImpl mSmControlLoadServiceDaoImpl;
    private final MSmLineControlLoadVerifyServiceDaoImpl mSmLineControlLoadVerifyServiceDaoImpl;
    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    public DemandVerifySettingUpdateDao() {
        smLineVerifyServiceDaoImpl = new MSmLineVerifyServiceDaoImpl();
        mSmControlLoadServiceDaoImpl = new MSmControlLoadServiceDaoImpl();
        mSmLineControlLoadVerifyServiceDaoImpl = new MSmLineControlLoadVerifyServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandVerifySettingUpdateResult query(DemandVerifySettingUpdateParameter parameter) throws Exception {

        MCorp exCorp;
        TBuilding exBuilding;
        MSmPrm exSm;
        List<MSmControlLoad> smControlLoadList;
        List<MSmLineVerify> smLineVerifyList;
        List<MSmLineControlLoadVerify> smLineControlLoadVerifyList;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getResultSet() == null) {
            return new DemandVerifySettingUpdateResult();
        }

        //JSON⇒Resultに変換
        DemandVerifySettingUpdateRequest resultSet = parameter.getResultSet();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //企業情報の排他チェック
        if (CheckUtility.isNullOrEmpty(parameter.getCorpId())) {
            return new DemandVerifySettingUpdateResult();
        } else {
            exCorp = corpExclusiveCheck(parameter);
            if (exCorp == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物情報の排他チェック
        if (parameter.getBuildingId() == null) {
            return new DemandVerifySettingUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(parameter);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器情報の排他チェック
        if (parameter.getSmId() == null) {
            return new DemandVerifySettingUpdateResult();
        } else {
            exSm = smExclusiveCheck(parameter);
            if (exSm == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //機器制御負荷の排他チェック
        smControlLoadList = smControlLoadExclusiveCheck(resultSet.getSmControlLoadList());

        //機器系統検証の排他チェック
        smLineVerifyList = smLineVerifyExclusiveCheck(resultSet.getSmLineVerifyList());

        //機器系統制御負荷検証の排他チェック
        smLineControlLoadVerifyList = smLineControlLoadVerifyExclusiveCheck(resultSet.getSmLineControlLoadVerifyList());

        //機器制御負荷の更新
        updateSmControlLoad(resultSet.getSmControlLoadList(), resultSet.getProductControlLoadList(), smControlLoadList,
                resultSet.getSmLineControlLoadVerifyList(), parameter.getSmId(),
                serverDateTime,
                loginUserId);
        //機器系統検証の更新
        updateSmLineVerify(resultSet.getSmLineVerifyList(), smLineVerifyList, serverDateTime, loginUserId);
        //機器系統制御負荷検証の更新
        updateSmLineControlLoadVerify(resultSet.getSmLineControlLoadVerifyList(), smLineControlLoadVerifyList,
                serverDateTime, loginUserId);

        //企業情報の更新
        exCorp.setUpdateDate(serverDateTime);
        exCorp.setUpdateUserId(loginUserId);
        merge(mCorpApiServiceDaoImpl, exCorp);
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);
        //機器情報の更新
        exSm.setUpdateDate(serverDateTime);
        exSm.setUpdateUserId(loginUserId);
        merge(mSmPrmServiceDaoImpl, exSm);

        //更新後の情報を取得する
        return getUpdateData(parameter);
    }

    /**
     * 企業情報の排他チェックを行う
     * @param result
     * @return
     */
    private MCorp corpExclusiveCheck(DemandVerifySettingUpdateParameter parameter) throws Exception {
        MCorp corpParam = new MCorp();
        corpParam.setCorpId(parameter.getCorpId());
        MCorp exCorp = find(mCorpApiServiceDaoImpl, corpParam);
        if (exCorp == null || !exCorp.getVersion().equals(parameter.getCorpVersion())) {
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
    private TBuilding buildingExclusiveCheck(DemandVerifySettingUpdateParameter parameter) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(parameter.getCorpId());
        pkBuildingParam.setBuildingId(parameter.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(parameter.getBuildingVersion())) {
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
    private MSmPrm smExclusiveCheck(DemandVerifySettingUpdateParameter parameter) throws Exception {
        MSmPrm smParam = new MSmPrm();
        smParam.setSmId(parameter.getSmId());
        MSmPrm exSm = find(mSmPrmServiceDaoImpl, smParam);
        if (exSm == null || !exSm.getVersion().equals(parameter.getSmVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exSm;
        }
    }

    /**
     * 機器制御負荷の排他チェックを行う
     * @param updateList
     * @return
     * @throws Exception
     */
    private List<MSmControlLoad> smControlLoadExclusiveCheck(List<SmControlLoadListDetailResultData> updateList)
            throws Exception {
        List<MSmControlLoad> resultList = new ArrayList<>();

        if (updateList == null || updateList.isEmpty()) {
            return resultList;
        }

        for (SmControlLoadListDetailResultData smControlLoad : updateList) {
            //機器制御負荷の排他チェック
            MSmControlLoad param = new MSmControlLoad();
            MSmControlLoadPK pkParam = new MSmControlLoadPK();
            pkParam.setSmId(smControlLoad.getSmId());
            pkParam.setControlLoad(smControlLoad.getControlLoad());
            param.setId(pkParam);
            MSmControlLoad updateData = find(mSmControlLoadServiceDaoImpl, param);
            if (smControlLoad.getVersion() == null) {
                //新規登録データの場合
                if (updateData != null) {
                    //すでに登録済のデータがある場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(null);
                }
            } else {
                //更新データの場合
                if (updateData == null || !updateData.getVersion().equals(smControlLoad.getVersion())) {
                    //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(updateData);
                }
            }
        }

        return resultList;
    }

    /**
     * 機器系統検証の排他チェックを行う
     * @param updateList
     * @return
     * @throws Exception
     */
    private List<MSmLineVerify> smLineVerifyExclusiveCheck(List<SmLineVerifyListDetailResultData> updateList)
            throws Exception {

        List<MSmLineVerify> resultList = new ArrayList<>();

        if (updateList == null || updateList.isEmpty()) {
            return resultList;
        }

        for (SmLineVerifyListDetailResultData smLineVerify : updateList) {
            //機器系統検証の排他チェック
            MSmLineVerify param = new MSmLineVerify();
            MSmLineVerifyPK pkParam = new MSmLineVerifyPK();
            pkParam.setCorpId(smLineVerify.getCorpId());
            pkParam.setLineGroupId(smLineVerify.getLineGroupId());
            pkParam.setLineNo(smLineVerify.getLineNo());
            pkParam.setBuildingId(smLineVerify.getBuildingId());
            pkParam.setSmId(smLineVerify.getSmId());
            param.setId(pkParam);
            MSmLineVerify updateData = find(smLineVerifyServiceDaoImpl, param);
            if (smLineVerify.getVersion() == null) {
                //新規登録データの場合
                if (updateData != null) {
                    //すでに登録済のデータがある場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(null);
                }
            } else {
                //更新データの場合
                if (updateData == null || !updateData.getVersion().equals(smLineVerify.getVersion())) {
                    //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(updateData);
                }
            }
        }

        return resultList;
    }

    /**
     * 機器系統制御負荷検証の排他チェックを行う
     * @param updateList
     * @param lineList
     * @param productList
     * @param corpId
     * @param buildingId
     * @param smId
     * @return
     * @throws Exception
     */
    private List<MSmLineControlLoadVerify> smLineControlLoadVerifyExclusiveCheck(
            List<SmLineControlLoadVerifyListDetailResultData> updateList) throws Exception {

        List<MSmLineControlLoadVerify> resultList = new ArrayList<>();

        if (updateList == null || updateList.isEmpty()) {
            return resultList;
        }

        for (SmLineControlLoadVerifyListDetailResultData smLineControlLoadVerify : updateList) {
            //機器系統制御負荷検証の排他チェック
            MSmLineControlLoadVerify param = new MSmLineControlLoadVerify();
            MSmLineControlLoadVerifyPK pkParam = new MSmLineControlLoadVerifyPK();
            pkParam.setCorpId(smLineControlLoadVerify.getCorpId());
            pkParam.setLineGroupId(smLineControlLoadVerify.getLineGroupId());
            pkParam.setLineNo(smLineControlLoadVerify.getLineNo());
            pkParam.setBuildingId(smLineControlLoadVerify.getBuildingId());
            pkParam.setSmId(smLineControlLoadVerify.getSmId());
            pkParam.setControlLoad(smLineControlLoadVerify.getControlLoad());
            param.setId(pkParam);
            MSmLineControlLoadVerify updateData = find(mSmLineControlLoadVerifyServiceDaoImpl, param);
            if (smLineControlLoadVerify.getVersion() == null) {
                //新規登録データの場合
                if (updateData != null) {
                    //すでに登録済のデータがある場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(null);
                }
            } else {
                //更新データの場合
                if (updateData == null || !updateData.getVersion().equals(smLineControlLoadVerify.getVersion())) {
                    //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
                    throw new OptimisticLockException();
                } else {
                    resultList.add(updateData);
                }
            }
        }

        return resultList;
    }

    /**
     * 機器制御負荷の更新を行う
     * @param updateList
     * @param productList
     * @param entityList
     * @param smLineControlLoadVerifyList
     * @param smId
     * @param serverDateTime
     * @param loginUserId
     * @throws Exception
     */
    private void updateSmControlLoad(List<SmControlLoadListDetailResultData> updateList,
            List<ProductControlLoadListDetailResultData> productList, List<MSmControlLoad> entityList,
            List<SmLineControlLoadVerifyListDetailResultData> smLineControlLoadVerifyList, Long smId,
            Timestamp serverDateTime,
            Long loginUserId) throws Exception {

        List<BigDecimal> registControlLoadList = new ArrayList<>();

        //リレーションの都合上、新規データを作成する
        //機器制御負荷に更新対象がないが、機器系統制御負荷検証に対象データがある場合
        if (smLineControlLoadVerifyList != null && !smLineControlLoadVerifyList.isEmpty()) {
            for (SmLineControlLoadVerifyListDetailResultData smLineControlLoadVerify : smLineControlLoadVerifyList) {
                Boolean existFlg = Boolean.FALSE;
                for (SmControlLoadListDetailResultData updateData : updateList) {
                    if (smLineControlLoadVerify.getSmId().equals(updateData.getSmId())
                            && smLineControlLoadVerify.getControlLoad().compareTo(updateData.getControlLoad()) == 0) {
                        //一致する場合は対応不要
                        existFlg = Boolean.TRUE;
                    }
                }
                if (!existFlg) {
                    //存在しない場合のみ対象に追加
                    if (!registControlLoadList.contains(smLineControlLoadVerify.getControlLoad())) {
                        //一致する制御負荷がリストにない場合のみ追加
                        registControlLoadList.add(smLineControlLoadVerify.getControlLoad());
                    }
                }
            }
        }

        if (!registControlLoadList.isEmpty()) {
            //対象データがすでに登録されていないかチェック
            for (BigDecimal registControlLoad : registControlLoadList) {
                MSmControlLoad param = new MSmControlLoad();
                MSmControlLoadPK pkParam = new MSmControlLoadPK();
                pkParam.setSmId(smId);
                pkParam.setControlLoad(registControlLoad);
                param.setId(pkParam);
                MSmControlLoad updateData = find(mSmControlLoadServiceDaoImpl, param);
                if (updateData != null) {
                    //データがある場合、排他エラー
                    new OptimisticLockException();
                }
            }
            //対象データの登録
            for (BigDecimal registControlLoad : registControlLoadList) {
                MSmControlLoad updateParam = new MSmControlLoad();
                MSmControlLoadPK pkUpdateParam = new MSmControlLoadPK();
                pkUpdateParam.setSmId(smId);
                pkUpdateParam.setControlLoad(registControlLoad);
                updateParam.setId(pkUpdateParam);
                updateParam.setControlLoadName(null);
                //制御負荷名称は製品制御負荷の制御負荷回路名称から取得
                for (ProductControlLoadListDetailResultData product : productList) {
                    if (registControlLoad.compareTo(product.getControlLoad()) == 0) {
                        updateParam.setControlLoadName(product.getControlLoadCircuit());
                        break;
                    }
                }
                updateParam.setControlLoadMemo(null);
                updateParam.setControlLoadShutOffTime(null);
                updateParam.setControlLoadShutOffRank(null);
                updateParam.setControlLoadShutOffCapacity(null);
                updateParam.setDelFlg(OsolConstants.FLG_OFF);
                updateParam.setVersion(0);
                updateParam.setCreateDate(serverDateTime);
                updateParam.setCreateUserId(loginUserId);
                updateParam.setUpdateDate(serverDateTime);
                updateParam.setUpdateUserId(loginUserId);
                persist(mSmControlLoadServiceDaoImpl, updateParam);
            }
        }

        //更新対象データの登録
        int i = 0;
        if (updateList != null && !updateList.isEmpty()) {
            for (SmControlLoadListDetailResultData updateData : updateList) {
                MSmControlLoad updateParam = new MSmControlLoad();
                MSmControlLoadPK pkUpdateParam = new MSmControlLoadPK();
                Boolean newFlg = Boolean.FALSE;

                if (entityList.get(i) == null) {
                    //新規登録
                    newFlg = Boolean.TRUE;
                    pkUpdateParam.setSmId(updateData.getSmId());
                    pkUpdateParam.setControlLoad(updateData.getControlLoad());
                    updateParam.setId(pkUpdateParam);
                    updateParam.setVersion(0);
                    updateParam.setCreateDate(serverDateTime);
                    updateParam.setCreateUserId(loginUserId);
                } else {
                    //更新
                    updateParam = entityList.get(i);
                    updateParam.setVersion(updateData.getVersion());
                }

                updateParam.setControlLoadName(updateData.getControlLoadName());
                updateParam.setControlLoadMemo(updateData.getControlLoadMemo());
                updateParam.setControlLoadShutOffTime(updateData.getControlLoadShutOffTime());
                updateParam.setControlLoadShutOffRank(updateData.getControlLoadShutOffRank());
                updateParam.setControlLoadShutOffCapacity(updateData.getControlLoadShutOffCapacity());
                updateParam.setDelFlg(updateData.getDelFlg());
                updateParam.setUpdateDate(serverDateTime);
                updateParam.setUpdateUserId(loginUserId);

                if (newFlg) {
                    persist(mSmControlLoadServiceDaoImpl, updateParam);
                } else {
                    merge(mSmControlLoadServiceDaoImpl, updateParam);
                }

                i++;
            }
        }

    }

    /**
     * 機器系統検証の更新を行う
     * @param updateList
     * @param entityList
     * @param serverDateTime
     * @param loginUserId
     * @throws Exception
     */
    private void updateSmLineVerify(List<SmLineVerifyListDetailResultData> updateList, List<MSmLineVerify> entityList,
            Timestamp serverDateTime, Long loginUserId) throws Exception {

        if (updateList == null || updateList.isEmpty()) {
            return;
        }

        //更新対象データの登録
        int i = 0;
        for (SmLineVerifyListDetailResultData updateData : updateList) {
            MSmLineVerify updateParam = new MSmLineVerify();
            MSmLineVerifyPK pkUpdateParam = new MSmLineVerifyPK();
            Boolean newFlg = Boolean.FALSE;

            if (entityList.get(i) == null) {
                //新規登録
                newFlg = Boolean.TRUE;
                pkUpdateParam.setCorpId(updateData.getCorpId());
                pkUpdateParam.setLineGroupId(updateData.getLineGroupId());
                pkUpdateParam.setLineNo(updateData.getLineNo());
                pkUpdateParam.setBuildingId(updateData.getBuildingId());
                pkUpdateParam.setSmId(updateData.getSmId());
                updateParam.setId(pkUpdateParam);
                updateParam.setVersion(0);
                updateParam.setCreateDate(serverDateTime);
                updateParam.setCreateUserId(loginUserId);
            } else {
                //更新
                updateParam = entityList.get(i);
                updateParam.setVersion(updateData.getVersion());
            }

            updateParam.setBasicRateUnitPrice(updateData.getBasicRateUnitPrice());
            updateParam.setAirVerifyType(updateData.getAirVerifyType());
            updateParam.setCommodityChargeUnitPrice(updateData.getCommodityChargeUnitPrice());
            updateParam.setReductionRateThreshold(updateData.getReductionRateThreshold());
            updateParam.setReductionCorrectionRate(updateData.getReductionCorrectionRate());
            updateParam.setReductionLowerRateMonth1(updateData.getReductionLowerRateMonth1());
            updateParam.setReductionLowerRateMonth2(updateData.getReductionLowerRateMonth2());
            updateParam.setReductionLowerRateMonth3(updateData.getReductionLowerRateMonth3());
            updateParam.setReductionLowerRateMonth4(updateData.getReductionLowerRateMonth4());
            updateParam.setReductionLowerRateMonth5(updateData.getReductionLowerRateMonth5());
            updateParam.setReductionLowerRateMonth6(updateData.getReductionLowerRateMonth6());
            updateParam.setReductionLowerRateMonth7(updateData.getReductionLowerRateMonth7());
            updateParam.setReductionLowerRateMonth8(updateData.getReductionLowerRateMonth8());
            updateParam.setReductionLowerRateMonth9(updateData.getReductionLowerRateMonth9());
            updateParam.setReductionLowerRateMonth10(updateData.getReductionLowerRateMonth10());
            updateParam.setReductionLowerRateMonth11(updateData.getReductionLowerRateMonth11());
            updateParam.setReductionLowerRateMonth12(updateData.getReductionLowerRateMonth12());
            updateParam.setReductionLowerAmountMonth1(updateData.getReductionLowerAmountMonth1());
            updateParam.setReductionLowerAmountMonth2(updateData.getReductionLowerAmountMonth2());
            updateParam.setReductionLowerAmountMonth3(updateData.getReductionLowerAmountMonth3());
            updateParam.setReductionLowerAmountMonth4(updateData.getReductionLowerAmountMonth4());
            updateParam.setReductionLowerAmountMonth5(updateData.getReductionLowerAmountMonth5());
            updateParam.setReductionLowerAmountMonth6(updateData.getReductionLowerAmountMonth6());
            updateParam.setReductionLowerAmountMonth7(updateData.getReductionLowerAmountMonth7());
            updateParam.setReductionLowerAmountMonth8(updateData.getReductionLowerAmountMonth8());
            updateParam.setReductionLowerAmountMonth9(updateData.getReductionLowerAmountMonth9());
            updateParam.setReductionLowerAmountMonth10(updateData.getReductionLowerAmountMonth10());
            updateParam.setReductionLowerAmountMonth11(updateData.getReductionLowerAmountMonth11());
            updateParam.setReductionLowerAmountMonth12(updateData.getReductionLowerAmountMonth12());
            updateParam.setProposalAmountUsedMonth1(updateData.getProposalAmountUsedMonth1());
            updateParam.setProposalAmountUsedMonth2(updateData.getProposalAmountUsedMonth2());
            updateParam.setProposalAmountUsedMonth3(updateData.getProposalAmountUsedMonth3());
            updateParam.setProposalAmountUsedMonth4(updateData.getProposalAmountUsedMonth4());
            updateParam.setProposalAmountUsedMonth5(updateData.getProposalAmountUsedMonth5());
            updateParam.setProposalAmountUsedMonth6(updateData.getProposalAmountUsedMonth6());
            updateParam.setProposalAmountUsedMonth7(updateData.getProposalAmountUsedMonth7());
            updateParam.setProposalAmountUsedMonth8(updateData.getProposalAmountUsedMonth8());
            updateParam.setProposalAmountUsedMonth9(updateData.getProposalAmountUsedMonth9());
            updateParam.setProposalAmountUsedMonth10(updateData.getProposalAmountUsedMonth10());
            updateParam.setProposalAmountUsedMonth11(updateData.getProposalAmountUsedMonth11());
            updateParam.setProposalAmountUsedMonth12(updateData.getProposalAmountUsedMonth12());
            updateParam.setDelFlg(updateData.getDelFlg());
            updateParam.setUpdateDate(serverDateTime);
            updateParam.setUpdateUserId(loginUserId);

            if (newFlg) {
                persist(smLineVerifyServiceDaoImpl, updateParam);
            } else {
                merge(smLineVerifyServiceDaoImpl, updateParam);
            }

            i++;
        }

    }

    /**
     * 機器系統制御負荷検証の更新を行う
     * @param updateList
     * @param entityList
     * @param serverDateTime
     * @param loginUserId
     * @throws Exception
     */
    private void updateSmLineControlLoadVerify(List<SmLineControlLoadVerifyListDetailResultData> updateList,
            List<MSmLineControlLoadVerify> entityList, Timestamp serverDateTime, Long loginUserId) throws Exception {

        if (updateList == null || updateList.isEmpty()) {
            return;
        }

        //更新対象データの登録
        int i = 0;
        for (SmLineControlLoadVerifyListDetailResultData updateData : updateList) {
            MSmLineControlLoadVerify updateParam = new MSmLineControlLoadVerify();
            MSmLineControlLoadVerifyPK pkUpdateParam = new MSmLineControlLoadVerifyPK();
            Boolean newFlg = Boolean.FALSE;

            if (entityList.get(i) == null) {
                //新規登録
                newFlg = Boolean.TRUE;
                pkUpdateParam.setCorpId(updateData.getCorpId());
                pkUpdateParam.setLineGroupId(updateData.getLineGroupId());
                pkUpdateParam.setLineNo(updateData.getLineNo());
                pkUpdateParam.setBuildingId(updateData.getBuildingId());
                pkUpdateParam.setSmId(updateData.getSmId());
                pkUpdateParam.setControlLoad(updateData.getControlLoad());
                updateParam.setId(pkUpdateParam);
                updateParam.setVersion(0);
                updateParam.setCreateDate(serverDateTime);
                updateParam.setCreateUserId(loginUserId);
            } else {
                //更新
                updateParam = entityList.get(i);
                updateParam.setVersion(updateData.getVersion());
                updateParam.setUpdateDate(serverDateTime);
                updateParam.setUpdateUserId(loginUserId);
            }

            updateParam.setDmLoadShutOffCapacity(updateData.getDmLoadShutOffCapacity());
            updateParam.setEvent1LoadShutOffCapacity(updateData.getEvent1LoadShutOffCapacity());
            updateParam.setEvent2LoadShutOffCapacity(updateData.getEvent2LoadShutOffCapacity());
            updateParam.setDelFlg(updateData.getDelFlg());
            updateParam.setUpdateDate(serverDateTime);
            updateParam.setUpdateUserId(loginUserId);

            if (newFlg) {
                persist(mSmLineControlLoadVerifyServiceDaoImpl, updateParam);
            } else {
                merge(mSmLineControlLoadVerifyServiceDaoImpl, updateParam);
            }

            i++;
        }

    }

    /**
     * 更新後のデータを返却する
     * @param parameter
     * @return
     */
    private DemandVerifySettingUpdateResult getUpdateData(DemandVerifySettingUpdateParameter parameter) {

        DemandVerifySettingUpdateResult result = new DemandVerifySettingUpdateResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        if (exCorpList == null || exCorpList.size() != 1) {
            return new DemandVerifySettingUpdateResult();
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new DemandVerifySettingUpdateResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(parameter.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new DemandVerifySettingUpdateResult();
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setCorpVersion(exCorpList.get(0).getVersion());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setSmId(exSmList.get(0).getSmId());
        result.setSmVersion(exSmList.get(0).getVersion());

        return result;
    }

}
