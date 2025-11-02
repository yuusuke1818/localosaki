package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmUpdateParameter;
import jp.co.osaki.osol.api.request.energy.setting.CorpTargetAlarmUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonPrefectureResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpTargetAlarmBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpTargetAlarmSelectDetailResultData;
import jp.co.osaki.osol.api.servicedao.building.IdBuildingSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonPrefectureServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpTargetAlarmSelectServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpTargetAlarm;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 企業目標超過警報建物更新 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class CorpTargetAlarmUpdateDao extends OsolApiDao<CorpTargetAlarmUpdateParameter> {
    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    /** 建物状況：稼働中 */
    private static final int BUILDING_SITUATION_NOW = 1;
    /** 建物状況：稼動終了 */
    private static final int BUILDING_SITUATION_END = 2;
    /** 建物状況：削除済 */
    private static final int BUILDING_SITUATION_DEL = 3;

    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MCorpTargetAlarmServiceDaoImpl corpTargetAlarmServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl mBuildingDmServiceDaoImpl;
    private final BuildingDemandListServiceDaoImpl buildingDemandListServiceDaoImpl;
    private final IdBuildingSelectServiceDaoImpl idBuildingSelectServiceDaoImpl;
    private final CorpTargetAlarmSelectServiceDaoImpl corpTargetAlarmSelectServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonPrefectureServiceDaoImpl commonPrefectureServiceDaoImpl;

    public CorpTargetAlarmUpdateDao() {
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        corpTargetAlarmServiceDaoImpl = new MCorpTargetAlarmServiceDaoImpl();
        mBuildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        buildingDemandListServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        idBuildingSelectServiceDaoImpl = new IdBuildingSelectServiceDaoImpl();
        corpTargetAlarmSelectServiceDaoImpl = new CorpTargetAlarmSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonPrefectureServiceDaoImpl = new CommonPrefectureServiceDaoImpl();
    }

    @Override
    public CorpTargetAlarmUpdateResult query(CorpTargetAlarmUpdateParameter parameter) throws Exception {
        MCorp exCorp;
        List<TBuilding> exBuildingList = new ArrayList<>();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getRequest() == null) {
            return new CorpTargetAlarmUpdateResult();
        }

        //JSON⇒Resultに変換
        CorpTargetAlarmUpdateRequest requestSet = parameter.getRequest();

      //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

       //企業情報の排他チェック
        if (CheckUtility.isNullOrEmpty(requestSet.getCorpId()) || requestSet.getVersion() == null
                || requestSet.getListTargetAlarmBuilding() == null ||  requestSet.getListTargetAlarmBuilding().isEmpty()) {
            return new CorpTargetAlarmUpdateResult();
        } else {
            exCorp = corpExclusiveCheck(requestSet);
            if (exCorp == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

        for(CorpTargetAlarmBuildingListDetailResultData buildingDetail : requestSet.getListTargetAlarmBuilding()) {
          //建物情報の排他チェック
            if (buildingDetail.getBuildingId() == null) {
                return new CorpTargetAlarmUpdateResult();
            } else {
                TBuilding exBuilding = buildingExclusiveCheck(requestSet.getCorpId(),buildingDetail);
                if (exBuilding == null) {
                    //排他エラー
                    throw new OptimisticLockException();
                }
                exBuildingList.add(exBuilding);
            }
        }

        // 企業目標超過警報更新
        writeMCorpTargetAlarm(requestSet.getCorpId(), requestSet.getCorpTargetAlarmResultSet(), loginUserId, serverDateTime);

        // 企業目標超過警報建物一覧更新
        for (CorpTargetAlarmBuildingListDetailResultData rs : requestSet.getListTargetAlarmBuilding()) {
            writeMBuildingDm(requestSet.getCorpId(), rs.getBuildingId(), rs.getTarget(), loginUserId, serverDateTime);
        }

      //企業情報の更新
        exCorp.setUpdateDate(serverDateTime);
        exCorp.setUpdateUserId(loginUserId);
        merge(mCorpApiServiceDaoImpl, exCorp);
        //建物情報の更新
        for(TBuilding exBuilding : exBuildingList) {
            exBuilding.setUpdateDate(serverDateTime);
            exBuilding.setUpdateUserId(loginUserId);
            merge(tBuildingApiServiceDaoImpl, exBuilding);
        }

        return getNewCorpTargetAlartResult(parameter);

    }

    private CorpTargetAlarmUpdateResult getNewCorpTargetAlartResult(CorpTargetAlarmUpdateParameter parameter) throws Exception{
        CorpTargetAlarmUpdateResult result = new CorpTargetAlarmUpdateResult();
        CorpTargetAlarmUpdateRequest request = parameter.getRequest();
      //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(request.getCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        if (exCorpList == null || exCorpList.size() != 1) {
            return result;
        }

        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setVersion(exCorpList.get(0).getVersion());

        result.setCorpTargetAlarmResultSet(getCorpTargetAlarmSelectResult(parameter));
        result.setListTargetAlarmBuilding(getCorpTargetAlarmBuildingListResult(parameter));

        return result;
    }

    private void writeMCorpTargetAlarm(String corpId, CorpTargetAlarmSelectDetailResultData corpTargetAlarm, Long userId,
            Timestamp timestamp) {

        boolean isNew = false;
        MCorpTargetAlarm entity = findMCorpTargetAlarm(corpId);
        if (entity == null) {
            entity = new MCorpTargetAlarm();
            entity.setCorpId(corpId);
            isNew = true;
        }

        // 警報通知先メールアドレス
        entity.setAlertMailAddress1(corpTargetAlarm.getAlertMailAddress1());
        entity.setAlertMailAddress2(corpTargetAlarm.getAlertMailAddress2());
        entity.setAlertMailAddress3(corpTargetAlarm.getAlertMailAddress3());
        entity.setAlertMailAddress4(corpTargetAlarm.getAlertMailAddress4());
        entity.setAlertMailAddress5(corpTargetAlarm.getAlertMailAddress5());
        entity.setAlertMailAddress6(corpTargetAlarm.getAlertMailAddress6());
        entity.setAlertMailAddress7(corpTargetAlarm.getAlertMailAddress7());
        entity.setAlertMailAddress8(corpTargetAlarm.getAlertMailAddress8());
        entity.setAlertMailAddress9(corpTargetAlarm.getAlertMailAddress9());
        entity.setAlertMailAddress10(corpTargetAlarm.getAlertMailAddress10());

        // 検出期間/条件
        entity.setMonthlyAlarmFlg(corpTargetAlarm.getMonthlyAlarmFlg());
        entity.setPeriodAlarmFlg(corpTargetAlarm.getPeriodAlarmFlg());
        entity.setYearAlarmFlg(corpTargetAlarm.getYearAlarmFlg());
        entity.setTargetKwhMonthlyOverRate(corpTargetAlarm.getTargetKwhMonthlyOverRate());
        entity.setTargetKwhPeriodOverRate(corpTargetAlarm.getTargetKwhPeriodOverRate());
        entity.setTargetKwhYearOverRate(corpTargetAlarm.getTargetKwhYearOverRate());
        entity.setMonthlyAlarmLockDate(corpTargetAlarm.getMonthlyAlarmLockDate());
        entity.setPeriodAlarmLockDate(corpTargetAlarm.getPeriodAlarmLockDate());
        entity.setYearAlarmLockDate(corpTargetAlarm.getYearAlarmLockDate());
        entity.setSharePeriod(corpTargetAlarm.getSharePeriod());

        // 検出時刻
        entity.setDetectTime(new Time(corpTargetAlarm.getDetectTime().getTime()));

        // 目標電力量
        entity.setTargetKwhMonth1(corpTargetAlarm.getTargetKwhMonth1());
        entity.setTargetKwhMonth2(corpTargetAlarm.getTargetKwhMonth2());
        entity.setTargetKwhMonth3(corpTargetAlarm.getTargetKwhMonth3());
        entity.setTargetKwhMonth4(corpTargetAlarm.getTargetKwhMonth4());
        entity.setTargetKwhMonth5(corpTargetAlarm.getTargetKwhMonth5());
        entity.setTargetKwhMonth6(corpTargetAlarm.getTargetKwhMonth6());
        entity.setTargetKwhMonth7(corpTargetAlarm.getTargetKwhMonth7());
        entity.setTargetKwhMonth8(corpTargetAlarm.getTargetKwhMonth8());
        entity.setTargetKwhMonth9(corpTargetAlarm.getTargetKwhMonth9());
        entity.setTargetKwhMonth10(corpTargetAlarm.getTargetKwhMonth10());
        entity.setTargetKwhMonth11(corpTargetAlarm.getTargetKwhMonth11());
        entity.setTargetKwhMonth12(corpTargetAlarm.getTargetKwhMonth12());

        // 更新情報
        entity.setUpdateUserId(userId);
        entity.setUpdateDate(timestamp);

        if (isNew) {
            entity.setDelFlg(OsolConstants.FLG_OFF);
            entity.setVersion(0);
            entity.setCreateUserId(userId);
            entity.setCreateDate(timestamp);
            persist(corpTargetAlarmServiceDaoImpl, entity);
        } else {
            merge(corpTargetAlarmServiceDaoImpl, entity);
        }
    }

    private MCorpTargetAlarm findMCorpTargetAlarm(String corpId) {
        MCorpTargetAlarm entity = new MCorpTargetAlarm();
        entity.setCorpId(corpId);
        return find(corpTargetAlarmServiceDaoImpl, entity);
    }

    private void writeMBuildingDm(String corpId, Long buildingId, Integer targetFlg, Long userId, Timestamp timestamp) {

        MBuildingDm entity = findMBuildingDm(corpId, buildingId);

        // 企業目標超過警報対象フラグ
        entity.setCoopTargetAlarmFlg(targetFlg);
        // 更新情報
        entity.setUpdateUserId(userId);
        entity.setUpdateDate(timestamp);

        merge(mBuildingDmServiceDaoImpl, entity);
    }

    private MBuildingDm findMBuildingDm(String corpId, Long buildingId) {
        MBuildingDm entity = new MBuildingDm();
        MBuildingDmPK id = new MBuildingDmPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        entity.setId(id);
        return find(mBuildingDmServiceDaoImpl, entity);
    }

    /**
     * 企業情報の排他チェックを行う
     * @param request
     * @return
     */
    private MCorp corpExclusiveCheck(CorpTargetAlarmUpdateRequest request) throws Exception {
        MCorp corpParam = new MCorp();
        corpParam.setCorpId(request.getCorpId());
        MCorp exCorp = find(mCorpApiServiceDaoImpl, corpParam);
        if (exCorp == null || !exCorp.getVersion().equals(request.getVersion())) {
            //排他制御のデータがない場合または前に保持をしていたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exCorp;
        }
    }

    /**
     * 建物情報の排他チェックを行う
     * @param request
     * @return
     */
    private TBuilding buildingExclusiveCheck(String corpId,CorpTargetAlarmBuildingListDetailResultData request) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(corpId);
        pkBuildingParam.setBuildingId(request.getBuildingId());
        buildingParam.setId(pkBuildingParam);
        TBuilding exBuilding = find(tBuildingApiServiceDaoImpl, buildingParam);
        if (exBuilding == null || !exBuilding.getVersion().equals(request.getVersion())) {
            //排他制御のデータがない場合または前に保持していたVersionと異なる場合、排他エラー
            throw new OptimisticLockException();
        } else {
            return exBuilding;
        }
    }

    public CorpTargetAlarmSelectDetailResultData getCorpTargetAlarmSelectResult(CorpTargetAlarmUpdateParameter parameter) throws Exception {
        CorpTargetAlarmSelectDetailResultData detailResult = new CorpTargetAlarmSelectDetailResultData();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new CorpTargetAlarmSelectDetailResultData();
        }

        //企業目標超過警報情報を取得する
        CorpTargetAlarmSelectDetailResultData detailParam = new CorpTargetAlarmSelectDetailResultData();
        detailParam.setCorpId(parameter.getOperationCorpId());
        List<CorpTargetAlarmSelectDetailResultData> detailList = getResultList(corpTargetAlarmSelectServiceDaoImpl,
                detailParam);

        if (detailList == null || detailList.size() != 1) {
            detailResult = new CorpTargetAlarmSelectDetailResultData();
        } else {
            detailResult = detailList.get(0);
            detailResult.setDetectTime(convTimeToDate(detailResult.getDetectTime()));
        }

        return detailResult;
    }

    public List<CorpTargetAlarmBuildingListDetailResultData> getCorpTargetAlarmBuildingListResult(CorpTargetAlarmUpdateParameter parameter) throws Exception {
        List<CorpTargetAlarmBuildingListDetailResultData> detailListResult = new ArrayList<>();

        //稼動状況を判断する時間
        Timestamp serverDateTime = getServerDateTime();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //企業のフィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new ArrayList<>();
        }

        //建物デマンド情報を取得する
        BuildingDemandListDetailResultData demandParam = new BuildingDemandListDetailResultData();
        demandParam.setCorpId(parameter.getOperationCorpId());
        List<BuildingDemandListDetailResultData> demandList = getResultList(buildingDemandListServiceDaoImpl,
                demandParam);

        //建物のフィルター処理を行う
        demandList = buildingDataFilterDao.applyDataFilter(demandList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (demandList != null && !demandList.isEmpty()) {
            for (BuildingDemandListDetailResultData demand : demandList) {
                CorpTargetAlarmBuildingListDetailResultData detail = new CorpTargetAlarmBuildingListDetailResultData();
                //排他建物情報を取得する
                CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
                exBuildingParam.setCorpId(demand.getCorpId());
                exBuildingParam.setBuildingId(demand.getBuildingId());
                List<CommonBuildingExclusionResult> exBuildingList = getResultList(
                        commonBuildingExclusionServiceDaoImpl, exBuildingParam);

                if (exBuildingList != null && exBuildingList.size() == 1) {
                    detail.setVersion(exBuildingList.get(0).getVersion());
                }

                //建物デマンド情報を設定する
                detail.setCorpId(demand.getCorpId());
                detail.setBuildingId(demand.getBuildingId());
                detail.setTarget(demand.getCoopTargetAlarmFlg());

                //建物情報を取得する
                AllBuildingListDetailResultData buildingParam = new AllBuildingListDetailResultData();
                buildingParam.setCorpId(demand.getCorpId());
                buildingParam.setBuildingId(demand.getBuildingId());
                List<AllBuildingListDetailResultData> buildingList = getResultList(idBuildingSelectServiceDaoImpl,
                        buildingParam);

                if (buildingList != null && buildingList.size() == 1) {
                    detail.setBuildingNo(buildingList.get(0).getBuildingNo());
                    detail.setBuildingName(buildingList.get(0).getBuildingName());
                    detail.setPrefectureCd(buildingList.get(0).getPrefectureCd());
                    detail.setAddress(buildingList.get(0).getAddress());
                    detail.setAddressBuilding(buildingList.get(0).getAddressBuilding());
                    detail.setNyukyoTypeCd(buildingList.get(0).getNyukyoTypeCd());
                    detail.setBuildingType(buildingList.get(0).getBuildingType());
                    detail.setVersion(buildingList.get(0).getVersion());

                    //都道府県名
                    CommonPrefectureResult prefectureParam = new CommonPrefectureResult();
                    prefectureParam.setPrefectureCd(buildingList.get(0).getPrefectureCd());
                    List<CommonPrefectureResult> prefectureList = getResultList(commonPrefectureServiceDaoImpl,
                            prefectureParam);
                    if (prefectureList != null && prefectureList.size() == 1) {
                        detail.setPrefectureName(prefectureList.get(0).getPrefectureName());
                    }

                    // 稼働状況
                    if (buildingList.get(0).getBuildingDelDate() != null) {
                        detail.setStatus(BUILDING_SITUATION_DEL);
                    } else if (buildingList.get(0).getTotalEndYm() == null
                            || buildingList.get(0).getTotalEndYm().compareTo(serverDateTime) >= 0) {
                        detail.setStatus(BUILDING_SITUATION_NOW);
                    } else if (buildingList.get(0).getTotalEndYm() != null
                            && buildingList.get(0).getTotalEndYm().compareTo(serverDateTime) < 0) {
                        detail.setStatus(BUILDING_SITUATION_END);
                    }
                }

                detailListResult.add(detail);
            }
        }

        return detailListResult;
    }

    private Date convTimeToDate (Date date) {
        if(date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

}
