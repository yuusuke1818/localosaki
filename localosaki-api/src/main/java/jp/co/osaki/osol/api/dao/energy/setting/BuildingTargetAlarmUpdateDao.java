package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingTargetAlarmUpdateParameter;
import jp.co.osaki.osol.api.request.building.setting.BuildingTargetAlarmUpdateRequest;
import jp.co.osaki.osol.api.result.energy.setting.BuildingTargetAlarmUpdateResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmLineListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingTargetAlarmSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MLineTargetAlarmTimeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingTargetAlarm;
import jp.co.osaki.osol.entity.MBuildingTargetAlarmPK;
import jp.co.osaki.osol.entity.MLineTargetAlarm;
import jp.co.osaki.osol.entity.MLineTargetAlarmPK;
import jp.co.osaki.osol.entity.MLineTargetAlarmTime;
import jp.co.osaki.osol.entity.MLineTargetAlarmTimePK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物目標超過警報更新 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class BuildingTargetAlarmUpdateDao extends OsolApiDao<BuildingTargetAlarmUpdateParameter> {

    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;
    private final MBuildingTargetAlarmServiceDaoImpl buildingTargetAlarmServiceDaoImpl;
    private final MLineTargetAlarmServiceDaoImpl lineTargetAlarmServiceDaoImpl;
    private final MLineTargetAlarmTimeServiceDaoImpl lineTargetAlarmTimeServiceDaoImpl;
    private final MBuildingTargetAlarmServiceDaoImpl mBuildingTargetAlarmServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;

    public BuildingTargetAlarmUpdateDao() {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        buildingTargetAlarmServiceDaoImpl = new MBuildingTargetAlarmServiceDaoImpl();
        lineTargetAlarmServiceDaoImpl = new MLineTargetAlarmServiceDaoImpl();
        lineTargetAlarmTimeServiceDaoImpl = new MLineTargetAlarmTimeServiceDaoImpl();
        mBuildingTargetAlarmServiceDaoImpl = new MBuildingTargetAlarmServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
    }

    @Override
    public BuildingTargetAlarmUpdateResult query(BuildingTargetAlarmUpdateParameter parameter) throws Exception {

        TBuilding exBuilding;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        if (parameter.getRequest() == null) {
            return new BuildingTargetAlarmUpdateResult();
        }

        //JSON⇒Resultに変換
        BuildingTargetAlarmUpdateRequest requestSet = parameter.getRequest();

        if(CheckUtility.isNullOrEmpty(requestSet.getSelectedCorpId())) {
            requestSet.setSelectedCorpId(parameter.getOperationCorpId());
        }else {
            parameter.setOperationCorpId(requestSet.getSelectedCorpId());
        }

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //建物情報の排他チェック
        if (requestSet.getBuildingId() == null) {
            return new BuildingTargetAlarmUpdateResult();
        } else {
            exBuilding = buildingExclusiveCheck(requestSet);
            if (exBuilding == null) {
                //排他エラー
                throw new OptimisticLockException();
            }
        }

     // 建物目標超過警報
        writeBuildingTargetAlarm(requestSet.getSelectedCorpId(),
                requestSet.getBuildingId(),
                requestSet.getBuildingTargetAlarmResultSet(),
                loginUserId,
                serverDateTime);

        if (requestSet.getBuildingTargetAlarmLineList() != null) {
            for (BuildingTargetAlarmLineListDetailResultData buildingTargetAlarmLine : requestSet
                    .getBuildingTargetAlarmLineList()) {
                // 建物系統目標超過警報
                writeBuildingTargetAlarmLine(requestSet.getSelectedCorpId(),
                        requestSet.getBuildingId(),
                        requestSet.getLineGroupId(),
                        buildingTargetAlarmLine,
                        loginUserId,
                        serverDateTime);
                if (buildingTargetAlarmLine.getTimeList() != null) {
                    for (BuildingTargetAlarmLineListTimeResultData buildingTargetAlarmLineTime : buildingTargetAlarmLine
                            .getTimeList()) {
                        // 建物系統目標超過警報時間
                        writeBuildingTargetAlarmLineTime(requestSet.getSelectedCorpId(),
                                requestSet.getBuildingId(),
                                requestSet.getLineGroupId(),
                                buildingTargetAlarmLine.getLineNo(),
                                buildingTargetAlarmLineTime,
                                loginUserId,
                                serverDateTime);
                    }
                }
            }
        }

        //※企業情報の更新は行わない
        //建物情報の更新
        exBuilding.setUpdateDate(serverDateTime);
        exBuilding.setUpdateUserId(loginUserId);
        merge(tBuildingApiServiceDaoImpl, exBuilding);

        return getNewBuildingTargetAlartResult(requestSet);

    }

    /**
     * 建物目標超過警報
     * @param corpId
     * @param buildingId
     * @param buildingTargetAlarm
     * @param userId
     * @param timestamp
     */
    private void writeBuildingTargetAlarm(String corpId, Long buildingId,
            BuildingTargetAlarmSelectDetailResultData buildingTargetAlarm, Long userId, Timestamp timestamp) {

        boolean isNew = false;
        //
        MBuildingTargetAlarmPK id = new MBuildingTargetAlarmPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        //
        MBuildingTargetAlarm param = new MBuildingTargetAlarm();
        param.setId(id);
        //
        MBuildingTargetAlarm entity = find(buildingTargetAlarmServiceDaoImpl, param);
        if (entity == null) {
            isNew = true;
            entity = new MBuildingTargetAlarm();
            entity.setId(id);
            entity.setDelFlg(OsolConstants.FLG_OFF);
            entity.setVersion(0);
            entity.setCreateUserId(userId);
            entity.setCreateDate(timestamp);
        }
        entity.setDetectDayOfWeek(buildingTargetAlarm.getDetectDayOfWeek());
        if (buildingTargetAlarm.getDetectTime() != null) {
            entity.setDetectTime(new Time(buildingTargetAlarm.getDetectTime().getTime()));
        }
        entity.setAlertMailAddress1(buildingTargetAlarm.getAlertMailAddress1());
        entity.setAlertMailAddress2(buildingTargetAlarm.getAlertMailAddress2());
        entity.setAlertMailAddress3(buildingTargetAlarm.getAlertMailAddress3());
        entity.setAlertMailAddress4(buildingTargetAlarm.getAlertMailAddress4());
        entity.setAlertMailAddress5(buildingTargetAlarm.getAlertMailAddress5());
        entity.setAlertMailAddress6(buildingTargetAlarm.getAlertMailAddress6());
        entity.setAlertMailAddress7(buildingTargetAlarm.getAlertMailAddress7());
        entity.setAlertMailAddress8(buildingTargetAlarm.getAlertMailAddress8());
        entity.setAlertMailAddress9(buildingTargetAlarm.getAlertMailAddress9());
        entity.setAlertMailAddress10(buildingTargetAlarm.getAlertMailAddress10());
        entity.setMonthlyAlarmFlg(buildingTargetAlarm.getMonthlyAlarmFlg());
        entity.setPeriodAlarmFlg(buildingTargetAlarm.getPeriodAlarmFlg());
        entity.setYearAlarmFlg(buildingTargetAlarm.getYearAlarmFlg());
        entity.setMonthlyAlarmLockDate(buildingTargetAlarm.getMonthlyAlarmLockDate());
        entity.setPeriodAlarmLockDate(buildingTargetAlarm.getPeriodAlarmLockDate());
        entity.setYearAlarmLockDate(buildingTargetAlarm.getYearAlarmLockDate());
        if (buildingTargetAlarm.getSharePeriod() != null) {
            entity.setSharePeriod(buildingTargetAlarm.getSharePeriod());
        }
        entity.setTargetKwhMonthlyOverDate(buildingTargetAlarm.getTargetKwhMonthlyOverDate());
        entity.setTargetKwhPeriodOverDate(buildingTargetAlarm.getTargetKwhPeriodOverDate());
        entity.setTargetKwhYearOverDate(buildingTargetAlarm.getTargetKwhYearOverDate());
        entity.setUpdateUserId(userId);
        entity.setUpdateDate(timestamp);

        if (isNew) {
            persist(buildingTargetAlarmServiceDaoImpl, entity);
        } else {
            merge(buildingTargetAlarmServiceDaoImpl, entity);
        }
    }

    /**
     * 建物系統目標超過警報
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param buildingTargetAlarmLine
     * @param userId
     * @param timestamp
     */
    private void writeBuildingTargetAlarmLine(String corpId, Long buildingId, Long lineGroupId,
            BuildingTargetAlarmLineListDetailResultData buildingTargetAlarmLine, Long userId, Timestamp timestamp) {

        boolean isNew = false;
        MLineTargetAlarmPK id = new MLineTargetAlarmPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        id.setLineGroupId(lineGroupId);
        id.setLineNo(buildingTargetAlarmLine.getLineNo());
        MLineTargetAlarm param = new MLineTargetAlarm();
        param.setId(id);
        MLineTargetAlarm entity = find(lineTargetAlarmServiceDaoImpl, param);
        if (entity == null) {
            isNew = true;
            entity = new MLineTargetAlarm();
            entity.setId(id);
            entity.setDelFlg(OsolConstants.FLG_OFF);
            entity.setVersion(0);
            entity.setCreateUserId(userId);
            entity.setCreateDate(timestamp);
        }
        entity.setLineOverRate(buildingTargetAlarmLine.getLineOverRate());
        entity.setOverRateDetectFlg(buildingTargetAlarmLine.getOverRateDetectFlg());
        entity.setContinueDays(buildingTargetAlarmLine.getContinueDays());
        entity.setUpdateUserId(userId);
        entity.setUpdateDate(timestamp);
        if (isNew) {
            persist(lineTargetAlarmServiceDaoImpl, entity);
        } else {
            merge(lineTargetAlarmServiceDaoImpl, entity);
        }
    }

    /**
     * 建物系統目標超過警報時間
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param buildingTargetAlarmLineTime
     * @param userId
     * @param timestamp
     */
    private void writeBuildingTargetAlarmLineTime(String corpId, Long buildingId, Long lineGroupId, String lineNo,
            BuildingTargetAlarmLineListTimeResultData buildingTargetAlarmLineTime, Long userId, Timestamp timestamp) {
        boolean isNew = false;
        MLineTargetAlarmTimePK id = new MLineTargetAlarmTimePK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        id.setLineGroupId(lineGroupId);
        id.setLineNo(lineNo);
        id.setJigenNo(buildingTargetAlarmLineTime.getJigenNo());
        MLineTargetAlarmTime param = new MLineTargetAlarmTime();
        param.setId(id);
        MLineTargetAlarmTime entity = find(lineTargetAlarmTimeServiceDaoImpl, param);
        if (entity == null) {
            isNew = true;
            entity = new MLineTargetAlarmTime();
            entity.setId(id);
            entity.setDelFlg(OsolConstants.FLG_OFF);
            entity.setVersion(0);
            entity.setCreateUserId(userId);
            entity.setCreateDate(timestamp);
        }
        entity.setDetectFlg(buildingTargetAlarmLineTime.getDetectFlg());
        entity.setUpdateUserId(userId);
        entity.setUpdateDate(timestamp);
        if (isNew) {
            persist(lineTargetAlarmTimeServiceDaoImpl, entity);
        } else {
            merge(lineTargetAlarmTimeServiceDaoImpl, entity);
        }
    }

    /**
     * 建物情報の排他チェックを行う
     * @param request
     * @return
     */
    private TBuilding buildingExclusiveCheck(BuildingTargetAlarmUpdateRequest request) throws Exception {
        TBuilding buildingParam = new TBuilding();
        TBuildingPK pkBuildingParam = new TBuildingPK();
        pkBuildingParam.setCorpId(request.getSelectedCorpId());
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
     *
     */
    private BuildingTargetAlarmUpdateResult getNewBuildingTargetAlartResult(BuildingTargetAlarmUpdateRequest request) {
        BuildingTargetAlarmUpdateResult result = new BuildingTargetAlarmUpdateResult();

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(request.getSelectedCorpId());
        exBuildingParam.setBuildingId(request.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return result;
        }

        result.setCorpId(request.getSelectedCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setLineGroupId(request.getLineGroupId());

        result.setBuildingTargetAlarmResultSet(getBuildingTargetAlarmSelectResult(request));
        result.setBuildingTargetAlarmLineList(getBuildingTargetAlarmLineListDetailResultDataList(request));
        return result;
    }

    private BuildingTargetAlarmSelectDetailResultData getBuildingTargetAlarmSelectResult(BuildingTargetAlarmUpdateRequest request) {

        //建物目標超過警報を取得する
        MBuildingTargetAlarm paramAlarm = new MBuildingTargetAlarm();
        MBuildingTargetAlarmPK pkParamAlarm = new MBuildingTargetAlarmPK();
        pkParamAlarm.setCorpId(request.getSelectedCorpId());
        pkParamAlarm.setBuildingId(request.getBuildingId());
        paramAlarm.setId(pkParamAlarm);
        MBuildingTargetAlarm alarm = find(mBuildingTargetAlarmServiceDaoImpl, paramAlarm);
        BuildingTargetAlarmSelectDetailResultData detail = new BuildingTargetAlarmSelectDetailResultData();
        if (alarm == null) {
            return new BuildingTargetAlarmSelectDetailResultData();
        } else {
            detail.setDetectDayOfWeek(alarm.getDetectDayOfWeek());
            detail.setDetectTime(convTimeToDate(alarm.getDetectTime()));
            detail.setAlertMailAddress1(alarm.getAlertMailAddress1());
            detail.setAlertMailAddress2(alarm.getAlertMailAddress2());
            detail.setAlertMailAddress3(alarm.getAlertMailAddress3());
            detail.setAlertMailAddress4(alarm.getAlertMailAddress4());
            detail.setAlertMailAddress5(alarm.getAlertMailAddress5());
            detail.setAlertMailAddress6(alarm.getAlertMailAddress6());
            detail.setAlertMailAddress7(alarm.getAlertMailAddress7());
            detail.setAlertMailAddress8(alarm.getAlertMailAddress8());
            detail.setAlertMailAddress9(alarm.getAlertMailAddress9());
            detail.setAlertMailAddress10(alarm.getAlertMailAddress10());
            detail.setAlertIntermalMailAddress1(alarm.getAlertIntermalMailAddress1());
            detail.setAlertIntermalMailAddress2(alarm.getAlertIntermalMailAddress2());
            detail.setMailWillSendTime(convTimeToDate(alarm.getMailWillSendTime()));
            detail.setMailLastSendTime(alarm.getMailLastSendTime());
            detail.setMonthlyAlarmFlg(alarm.getMonthlyAlarmFlg());
            detail.setPeriodAlarmFlg(alarm.getPeriodAlarmFlg());
            detail.setYearAlarmFlg(alarm.getYearAlarmFlg());
            detail.setMonthlyAlarmLockDate(alarm.getMonthlyAlarmLockDate());
            detail.setPeriodAlarmLockDate(alarm.getPeriodAlarmLockDate());
            detail.setYearAlarmLockDate(alarm.getYearAlarmLockDate());
            detail.setSharePeriod(alarm.getSharePeriod());
            detail.setTargetKwhMonthlyOverDate(alarm.getTargetKwhMonthlyOverDate());
            detail.setTargetKwhPeriodOverDate(alarm.getTargetKwhPeriodOverDate());
            detail.setTargetKwhYearOverDate(alarm.getTargetKwhYearOverDate());
            detail.setDelFlg(alarm.getDelFlg());
            detail.setVersion(alarm.getVersion());
        }

        return detail;
    }

    public List<BuildingTargetAlarmLineListDetailResultData> getBuildingTargetAlarmLineListDetailResultDataList(BuildingTargetAlarmUpdateRequest request) {
        List<BuildingTargetAlarmLineListDetailResultData> detailList = new ArrayList<>();

        //系統情報を取得する
        LineListDetailResultData paramLine = new LineListDetailResultData();
        paramLine.setCorpId(request.getSelectedCorpId());
        paramLine.setLineGroupId(request.getLineGroupId());
        List<LineListDetailResultData> lineList = getResultList(lineListServiceDaoImpl, paramLine);
        if (lineList != null && !lineList.isEmpty()) {
            // ソート
            lineList.sort((LineListDetailResultData o1, LineListDetailResultData o2) -> {
                Long no1 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(o1.getLineTarget())
                        ? Long.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(o1.getLineTarget()) ? Long.MAX_VALUE
                                : Long.parseLong(o1.getLineNo());
                Long no2 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(o2.getLineTarget())
                        ? Long.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(o2.getLineTarget()) ? Long.MAX_VALUE
                                : Long.parseLong(o2.getLineNo());
                return no1.compareTo(no2);
            });

            for (LineListDetailResultData line : lineList) {
                BuildingTargetAlarmLineListDetailResultData detail = new BuildingTargetAlarmLineListDetailResultData();
                List<BuildingTargetAlarmLineListTimeResultData> timeDetailList = new ArrayList<>();
                detail.setLineNo(line.getLineNo());
                detail.setLineName(line.getLineName());
                //建物系統目標超過警報情報を取得する
                MLineTargetAlarm paramAlarm = new MLineTargetAlarm();
                MLineTargetAlarmPK pkParamAlarm = new MLineTargetAlarmPK();
                pkParamAlarm.setCorpId(request.getSelectedCorpId());
                pkParamAlarm.setBuildingId(request.getBuildingId());
                pkParamAlarm.setLineGroupId(request.getLineGroupId());
                pkParamAlarm.setLineNo(line.getLineNo());
                paramAlarm.setId(pkParamAlarm);
                MLineTargetAlarm alarm = find(lineTargetAlarmServiceDaoImpl, paramAlarm);
                if (alarm == null) {
                    //取得できない場合は次のレコードへ
                    detail.setLineOverRate(null);
                    detail.setOverRateDetectFlg(null);
                    detail.setContinueDays(null);
                    detail.setDelFlg(null);
                    detail.setVersion(null);
                    detail.setTimeList(new ArrayList<>());
                    detailList.add(detail);
                    continue;
                }
                detail.setLineOverRate(alarm.getLineOverRate());
                detail.setOverRateDetectFlg(alarm.getOverRateDetectFlg());
                detail.setContinueDays(alarm.getContinueDays());
                detail.setDelFlg(alarm.getDelFlg());
                detail.setVersion(alarm.getVersion());
                //建物系統目標超過警報時間を取得する
                MLineTargetAlarmTime paramTime = new MLineTargetAlarmTime();
                MLineTargetAlarmTimePK pkParamTime = new MLineTargetAlarmTimePK();
                pkParamTime.setCorpId(request.getSelectedCorpId());
                pkParamTime.setBuildingId(request.getBuildingId());
                pkParamTime.setLineGroupId(request.getLineGroupId());
                pkParamTime.setLineNo(line.getLineNo());
                paramTime.setId(pkParamTime);
                List<MLineTargetAlarmTime> timeList = getResultList(lineTargetAlarmTimeServiceDaoImpl, paramTime);
                if (timeList == null || timeList.isEmpty()) {
                    detail.setTimeList(new ArrayList<>());
                } else {
                    for (MLineTargetAlarmTime time : timeList) {
                        BuildingTargetAlarmLineListTimeResultData timeDetail = new BuildingTargetAlarmLineListTimeResultData();
                        timeDetail.setJigenNo(time.getId().getJigenNo());
                        timeDetail.setDetectFlg(time.getDetectFlg());
                        timeDetail.setVersion(time.getVersion());
                        timeDetail.setDelFlg(time.getDelFlg());
                        timeDetailList.add(timeDetail);
                    }
                    detail.setTimeList(timeDetailList);
                }
                detailList.add(detail);
            }
        }
        return detailList;
    }

    private Date convTimeToDate (Time time) {
        if(time == null) {
            return null;
        }
        return new Date(time.getTime());
    }

}
