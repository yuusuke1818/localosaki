/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building.setting;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.setting.BuildingDmAvailableEnergyInfoUpdateParameter;
import jp.co.osaki.osol.api.request.building.setting.AvailableEnergyLineListRequestSet;
import jp.co.osaki.osol.api.request.building.setting.AvailableEnergyListRequestSet;
import jp.co.osaki.osol.api.request.building.setting.BuildingDmAvailableEnergyInfoUpdateRequest;
import jp.co.osaki.osol.api.request.building.setting.BuildingEnergyTypeListRequestSet;
import jp.co.osaki.osol.api.result.building.setting.BuildingDmAvailableEnergyInfoUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyListResultData;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyLineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.building.setting.AvailableEnergyListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingEnergyTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLinePK;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyLine;
import jp.co.osaki.osol.entity.TAvailableEnergyLinePK;
import jp.co.osaki.osol.entity.TAvailableEnergyPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingEnergyType;
import jp.co.osaki.osol.entity.TBuildingEnergyTypePK;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 建物デマンド使用エネルギー情報更新 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class BuildingDmAvailableEnergyInfoUpdateDao extends OsolApiDao<BuildingDmAvailableEnergyInfoUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final TAvailableEnergyServiceDaoImpl tAvailableEnergyServiceDaoImpl;
    private final TAvailableEnergyLineServiceDaoImpl tAvailableEnergyLineServiceDaoImpl;
    private final TBuildingEnergyTypeServiceDaoImpl tBuildingEnergyTypeServiceDaoImpl;
    private final AvailableEnergyListServiceDaoImpl availableEnergyListServiceDaoImpl;
    private final AvailableEnergyLineListServiceDaoImpl availableEnergyLineListServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final MLineServiceDaoImpl mLineServiceDaoImpl;

    public BuildingDmAvailableEnergyInfoUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        tAvailableEnergyServiceDaoImpl = new TAvailableEnergyServiceDaoImpl();
        tAvailableEnergyLineServiceDaoImpl = new TAvailableEnergyLineServiceDaoImpl();
        tBuildingEnergyTypeServiceDaoImpl = new TBuildingEnergyTypeServiceDaoImpl();
        availableEnergyListServiceDaoImpl = new AvailableEnergyListServiceDaoImpl();
        availableEnergyLineListServiceDaoImpl = new AvailableEnergyLineListServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        mLineServiceDaoImpl = new MLineServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public BuildingDmAvailableEnergyInfoUpdateResult query(BuildingDmAvailableEnergyInfoUpdateParameter parameter) throws Exception {

        BuildingDmAvailableEnergyInfoUpdateResult result = new BuildingDmAvailableEnergyInfoUpdateResult();

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getRequest() == null) {
            return new BuildingDmAvailableEnergyInfoUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingDmAvailableEnergyInfoUpdateRequest request = parameter.getRequest();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (request.getBuildingId() == null) {
            return new BuildingDmAvailableEnergyInfoUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(request);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        //建物エネルギー種別登録
        updateBuildingEnergyType(request.getBuildingEnergyTypeList(),serverDateTime,loginUserId);

        //使用エネルギー情報登録
        updateAvailableEnergy(request,serverDateTime,loginUserId);

        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        //建物情報取得
        CommonBuildingExclusionResult buildingParam = new CommonBuildingExclusionResult();
        buildingParam.setCorpId(parameter.getOperationCorpId());
        buildingParam.setBuildingId(request.getBuildingId());

        List<CommonBuildingExclusionResult> buildingList = getResultList(commonBuildingExclusionServiceDaoImpl,buildingParam);

        if(buildingList == null || buildingList.size() != 1 ) {
            return result;
        }

        result.setCorpId(buildingList.get(0).getCorpId());
        result.setBuildingId(buildingList.get(0).getBuildingId());
        result.setBuildingVersion(buildingList.get(0).getVersion());

        //使用エネルギー 登録後情報取得
        result.setAvailableEnergyList(getAvailableEnergyList(request));

      //使用エネルギー系統 登録後情報取得
        result.setAvailableEnergyLineList(getAvailableEnergyLineList(request));

        return result;

    }

    /**
     * 建物情報の排他チェックを行う
     * @param request
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingDmAvailableEnergyInfoUpdateRequest request) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(request.getCorpId());
        pkBuildingParam.setBuildingId(request.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(request.getBuildingVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exBuilding;
        }
    }

    /**
     * 使用エネルギー情報と、それに対応する使用エネルギー系統情報を更新する
     *
     * @param request
     * @param serverDateTime
     * @param loginUser
     * @throws Exception
     */
    private void updateAvailableEnergy(BuildingDmAvailableEnergyInfoUpdateRequest request,Timestamp serverDateTime,Long loginUser)  throws Exception {
        TAvailableEnergy findAvailableEnergy;
        TAvailableEnergy updateAvailableEnergy = null;
        TAvailableEnergyPK findAvailableEnergyPK;
        TAvailableEnergyPK updateAvailableEnergyPK;
        Boolean persistFlg;

        if(request.getAvailableEnergyList() == null || request.getAvailableEnergyList().isEmpty()) {
            return;
        }

        for(AvailableEnergyListRequestSet availableEnergyReq: request.getAvailableEnergyList()) {
            //契約IDがnullの場合は新規登録と判断する
            if(availableEnergyReq.getContractId() == null) {
                persistFlg = true;
            }else {
                findAvailableEnergyPK = new TAvailableEnergyPK();
                findAvailableEnergy = new TAvailableEnergy();
                findAvailableEnergyPK.setCorpId(availableEnergyReq.getCorpId());
                findAvailableEnergyPK.setBuildingId(availableEnergyReq.getBuildingId());
                findAvailableEnergyPK.setEngTypeCd(availableEnergyReq.getEngTypeCd());
                findAvailableEnergyPK.setEngId(availableEnergyReq.getEngId());
                findAvailableEnergyPK.setContractId(availableEnergyReq.getContractId());
                findAvailableEnergy.setId(findAvailableEnergyPK);
                updateAvailableEnergy = find(tAvailableEnergyServiceDaoImpl,findAvailableEnergy);
                if(updateAvailableEnergy == null) {
                    persistFlg = Boolean.TRUE;
                }else {
                    persistFlg = Boolean.FALSE;
                }
            }

            if(persistFlg) {
                if(availableEnergyReq.getDelFlg().equals(OsolConstants.FLG_ON)) {
                  //新規で削除データの場合、処理を行わない
                    continue;
                }
                availableEnergyReq.setContractId(createId(OsolConstants.ID_SEQUENCE_NAME.CONTRACT_ID.getVal()));
                //取得した契約IDをセット
                updateAvailableEnergy = new TAvailableEnergy();
                updateAvailableEnergyPK = new TAvailableEnergyPK();
                updateAvailableEnergyPK.setCorpId(availableEnergyReq.getCorpId());
                updateAvailableEnergyPK.setBuildingId(availableEnergyReq.getBuildingId());
                updateAvailableEnergyPK.setEngTypeCd(availableEnergyReq.getEngTypeCd());
                updateAvailableEnergyPK.setEngId(availableEnergyReq.getEngId());
                updateAvailableEnergyPK.setContractId(availableEnergyReq.getContractId());

                updateAvailableEnergy.setId(updateAvailableEnergyPK);
                updateAvailableEnergy.setCreateDate(serverDateTime);
                updateAvailableEnergy.setCreateUserId(loginUser);

            }
            updateAvailableEnergy.setEnergyStartYm(availableEnergyReq.getEnergyStartYm());
            updateAvailableEnergy.setEnergyEndYm(availableEnergyReq.getEnergyEndYm());
            updateAvailableEnergy.setCustomerNo(availableEnergyReq.getCustomerNo());
            updateAvailableEnergy.setSupplyPointSpecificNo(availableEnergyReq.getSupplyPointSpecificNo());
            updateAvailableEnergy.setDayAndNightType(availableEnergyReq.getDayAndNightType());
            updateAvailableEnergy.setEngSupplyType(availableEnergyReq.getEngSupplyType());
            updateAvailableEnergy.setUsePlace(availableEnergyReq.getUsePlace());
            updateAvailableEnergy.setContractType(availableEnergyReq.getContractType());
            updateAvailableEnergy.setContractPower(availableEnergyReq.getContractPower());
            updateAvailableEnergy.setContractPowerUnit(availableEnergyReq.getContractPowerUnit());
            updateAvailableEnergy.setContractBiko(availableEnergyReq.getContractBiko());
            updateAvailableEnergy.setInputFlg(availableEnergyReq.getInputFlg());
            updateAvailableEnergy.setDisplayOrder(availableEnergyReq.getDisplayOrder());
            updateAvailableEnergy.setDelFlg(availableEnergyReq.getDelFlg());
            updateAvailableEnergy.setUpdateDate(serverDateTime);
            updateAvailableEnergy.setUpdateUserId(loginUser);
            updateAvailableEnergy.setEnergyUseLineValueFlg(availableEnergyReq.getEnergyUseLineValueFlg());

            if(persistFlg) {
                persist(tAvailableEnergyServiceDaoImpl, updateAvailableEnergy);
            }else {
                merge(tAvailableEnergyServiceDaoImpl, updateAvailableEnergy);
            }

            //使用エネルギー系統登録
            //行番号が一致する使用エネルギー系統に、契約IDを設定する
            List<AvailableEnergyLineListRequestSet> availableEnergyLineRequestList=
                    request.getAvailableEnergyLineList().stream()
                    .filter(o -> o.getRowNo().equals(availableEnergyReq.getRowNo()))
                    .collect(Collectors.toList());
            for(AvailableEnergyLineListRequestSet req:availableEnergyLineRequestList) {
                req.setContractId(availableEnergyReq.getContractId());
            }

            //使用エネルギー実績系統値反映フラグが1：有効の場合のみ、使用エネルギー系統の更新処理を行う
            if (OsolConstants.FLG_ON.equals(updateAvailableEnergy.getEnergyUseLineValueFlg())) {
                updateAvailableEnergyLine(availableEnergyLineRequestList, serverDateTime, loginUser);
            }

        }
    }

    /**
     * 使用エネルギー系統情報を更新する
     *
     * @param requestList
     * @param serverDateTime
     * @param loginUser
     * @throws Exception
     */
    private void updateAvailableEnergyLine(List<AvailableEnergyLineListRequestSet> requestList,Timestamp serverDateTime,Long loginUser)  throws Exception {
        TAvailableEnergyLine findAvailableEnergyLine;
        TAvailableEnergyLine updateAvailableEnergyLine = null;
        TAvailableEnergyLinePK availableEnergyLinePK;
        Boolean persistFlg;
        if(requestList == null || requestList.isEmpty()) {
            return;
        }

        for(AvailableEnergyLineListRequestSet request : requestList) {
            availableEnergyLinePK = new TAvailableEnergyLinePK();
            findAvailableEnergyLine = new TAvailableEnergyLine();
            updateAvailableEnergyLine = new TAvailableEnergyLine();

            availableEnergyLinePK.setCorpId(request.getCorpId());
            availableEnergyLinePK.setBuildingId(request.getBuildingId());
            availableEnergyLinePK.setEngTypeCd(request.getEngTypeCd());
            availableEnergyLinePK.setEngId(request.getEngId());
            availableEnergyLinePK.setContractId(request.getContractId());

            findAvailableEnergyLine.setId(availableEnergyLinePK);

            TAvailableEnergyLine line = find(tAvailableEnergyLineServiceDaoImpl,findAvailableEnergyLine);

            //データが存在する場合、削除を実施する
            if(line == null) {
                persistFlg = Boolean.TRUE;
                updateAvailableEnergyLine.setId(availableEnergyLinePK);
                updateAvailableEnergyLine.setCreateDate(serverDateTime);
                updateAvailableEnergyLine.setCreateUserId(loginUser);
                updateAvailableEnergyLine.setVersion(0);
            }else {
                persistFlg = Boolean.FALSE;
                updateAvailableEnergyLine = line;
            }

            MLine targetLine = new MLine();
            MLinePK targetLinePK = new MLinePK();
            targetLinePK.setCorpId(request.getCorpId());
            targetLinePK.setLineGroupId(request.getLineGroupId());
            targetLinePK.setLineNo(request.getLineNo());
            targetLine.setId(targetLinePK);
            updateAvailableEnergyLine.setMLine(find(mLineServiceDaoImpl,targetLine));
            updateAvailableEnergyLine.setSummaryUnit(request.getSummaryUnit());
            if(request.getStartTime() != null) {
                updateAvailableEnergyLine.setStartTime(Time.valueOf(DateUtility.changeDateFormat(request.getStartTime(), DateUtility.DATE_FORMAT_HHMMSS_COLON)));
            }
            if(request.getEndTime() != null) {
                updateAvailableEnergyLine.setEndTime(Time.valueOf(DateUtility.changeDateFormat(request.getEndTime(), DateUtility.DATE_FORMAT_HHMMSS_COLON)));
            }

            updateAvailableEnergyLine.setUpdateDate(serverDateTime);
            updateAvailableEnergyLine.setUpdateUserId(loginUser);

            if(persistFlg) {
                persist(tAvailableEnergyLineServiceDaoImpl,updateAvailableEnergyLine);
            }else {
                merge(tAvailableEnergyLineServiceDaoImpl,updateAvailableEnergyLine);
            }


        }

    }

    /**
     * 使用エネルギー情報を取得する
     * @param request
     * @return
     * @throws Exception
     */
    public List<AvailableEnergyListResultData> getAvailableEnergyList(BuildingDmAvailableEnergyInfoUpdateRequest request) throws Exception {

        AvailableEnergyListResultData param = new AvailableEnergyListResultData();

        param.setCorpId(request.getCorpId());
        param.setBuildingId(request.getBuildingId());

        return getResultList(availableEnergyListServiceDaoImpl, param);

    }

    /**
     * 使用エネルギー系統情報を取得する
     * @param request
     * @return
     * @throws Exception
     */
    public List<AvailableEnergyLineListResultData> getAvailableEnergyLineList(BuildingDmAvailableEnergyInfoUpdateRequest request) throws Exception {

        AvailableEnergyLineListResultData param = new AvailableEnergyLineListResultData();

        param.setCorpId(request.getCorpId());
        param.setBuildingId(request.getBuildingId());

        List<AvailableEnergyLineListResultData> resultList = getResultList(availableEnergyLineListServiceDaoImpl, param);

        //TIME型からDate型への強制変換
        for(AvailableEnergyLineListResultData result : resultList) {
            if(result.getStartTime() != null) {
                result.setStartTime(DateUtility.plusYear(DateUtility.plusYear(result.getStartTime(),1),-1));
            }
            if(result.getEndTime() != null) {
                result.setEndTime(DateUtility.plusYear(DateUtility.plusYear(result.getEndTime(),1),-1));
            }


        }

        return resultList;

    }

    /**
     * 建物エネルギー種別を更新する
     *
     * @param requestList
     * @param serverDateTime
     * @param loginUser
     * @throws Exception
     */
    private void updateBuildingEnergyType(List<BuildingEnergyTypeListRequestSet> requestList,Timestamp serverDateTime,Long loginUser)  throws Exception {
        TBuildingEnergyType findBuildingEnergyType;
        TBuildingEnergyType updateBuildingEnergyType = null;
        TBuildingEnergyTypePK tBuildingEnergyTypePK;

        if(requestList == null || requestList.isEmpty()) {
            return;
        }

        for(BuildingEnergyTypeListRequestSet request : requestList) {
            tBuildingEnergyTypePK = new TBuildingEnergyTypePK();
            findBuildingEnergyType = new TBuildingEnergyType();
            updateBuildingEnergyType = new TBuildingEnergyType();

            tBuildingEnergyTypePK.setCorpId(request.getCorpId());
            tBuildingEnergyTypePK.setBuildingId(request.getBuildingId());
            tBuildingEnergyTypePK.setEngTypeCd(request.getEngTypeCd());

            findBuildingEnergyType.setId(tBuildingEnergyTypePK);
            updateBuildingEnergyType.setId(tBuildingEnergyTypePK);

            TBuildingEnergyType result = find(tBuildingEnergyTypeServiceDaoImpl,findBuildingEnergyType);

            //データが存在確認
            Boolean isPersist = false;
            if(result == null ) {
                //データが存在せず、登録データの削除フラグがONの場合はなにもしない
                if(request.getDelFlg() == OsolConstants.FLG_ON) {
                    continue;
                }

                updateBuildingEnergyType.setCreateDate(serverDateTime);
                updateBuildingEnergyType.setCreateUserId(loginUser);
                isPersist = true;
            }else {
                updateBuildingEnergyType = result;
            }

            updateBuildingEnergyType.setDisplayOrder(request.getDisplayOrder());
            updateBuildingEnergyType.setDelFlg(request.getDelFlg());
            updateBuildingEnergyType.setUpdateDate(serverDateTime);
            updateBuildingEnergyType.setUpdateUserId(loginUser);

            if(isPersist) {
                persist(tBuildingEnergyTypeServiceDaoImpl,updateBuildingEnergyType);
            }else {
                merge(tBuildingEnergyTypeServiceDaoImpl,updateBuildingEnergyType);
            }


        }

    }

}
