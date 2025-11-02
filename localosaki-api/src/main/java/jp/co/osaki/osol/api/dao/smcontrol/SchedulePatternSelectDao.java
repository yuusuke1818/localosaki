package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.smcontrol.SmControlScheduleLogRegistResult;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleSetLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleTimeLogServiceDaoImpl;
import jp.co.osaki.osol.entity.TSmControlScheduleLog;
import jp.co.osaki.osol.entity.TSmControlScheduleLogPK;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLog;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLogPK;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLogPK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200061Param;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * スケジュールパターン(取得) Dao クラス
 *
 * @author t_hayama
 *
 */
@Stateless
public class SchedulePatternSelectDao extends BaseSmControlDao {
    private final TSmControlScheduleLogServiceDaoImpl tSmControlScheduleLogServiceDaoImpl;
    private final TSmControlScheduleTimeLogServiceDaoImpl tSmControlScheduleTimeLogServiceDaoImpl;
    private final TSmControlScheduleSetLogServiceDaoImpl tSmControlScheduleSetLogServiceDaoImpl;

    private static final String TIME_ZONE_LIST = "timeZoneList";
    private static final String START_TIME_HOUR = "startHour";
    private static final String START_TIME_MINUTE = "startMinute";
    private static final String END_TIME_HOUR = "endHour";
    private static final String END_TIME_MINUTE = "endMinute";


    public SchedulePatternSelectDao() {
        tSmControlScheduleLogServiceDaoImpl = new TSmControlScheduleLogServiceDaoImpl();
        tSmControlScheduleTimeLogServiceDaoImpl = new TSmControlScheduleTimeLogServiceDaoImpl();
        tSmControlScheduleSetLogServiceDaoImpl = new TSmControlScheduleSetLogServiceDaoImpl();
    }

    /**
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    public SmControlScheduleLogRegistResult updateSchedule(FvpCtrlMngResponse<A200061Param> res, Long loginUserId) throws Exception {

        // DBサーバ時刻取得
        Timestamp nowTimestamp = super.getServerDateTime();
        Long id = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_SCHEDULE_LOG_ID.getVal());

        A200061Param resParam = res.getParam();

        //DB上の最新スケジュールを取得
        TSmControlScheduleLog latestLog =  getLatestLog(res.getSmId());
        List<TSmControlScheduleTimeLog> latestScheduleTimeLogList = getLatestSmControlScheduleTimeList(latestLog);
        List<TSmControlScheduleSetLog> latestScheduleSetLogList = getLatestSmControlScheduleSetList(latestLog);

        //機器制御スケジュール履歴登録
        insertSmControlScheduleLog(resParam, id, loginUserId, nowTimestamp, res.getSmId(), latestLog);

        //機器制御スケジュール時間帯履歴登録
        insertSmControlScheduleTimeLog(resParam, id, loginUserId, nowTimestamp, res.getSmId(), latestScheduleTimeLogList);

        //機器制御スケジュール設定履歴登録
        insertSmControlScheduleSetLog(resParam, id, loginUserId, nowTimestamp, res.getSmId(), latestScheduleSetLogList);

        return new SmControlScheduleLogRegistResult();
    }

    /**
     * 機器制御スケジュール履歴 登録処理
     *
     * @param resParam
     * @param id
     * @param loginUserId
     * @param nowTimestamp
     * @param smId
     * @return
     */
    private boolean insertSmControlScheduleLog(A200061Param resParam, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId, TSmControlScheduleLog latestScheduleLog) {

        //対象の日付文字列の長さが違う場合は登録しない
        if(resParam.getSettingDate() == null || resParam.getSettingDate().length() != 10) {
            return false;
        }

        String ymd = "20" + resParam.getSettingDate().substring(0, 6);
        String hm = resParam.getSettingDate().substring(6, 10);
        Date settingDate = DateUtility.conversionDate(ymd + hm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Timestamp settingUpdateDatetime = new Timestamp(settingDate.getTime());
        Integer scheduleManageDesinationFlg = OsolConstants.FLG_OFF;

        if (latestScheduleLog != null) {
            scheduleManageDesinationFlg = latestScheduleLog.getScheduleManageDesignationFlg();
        }

        TSmControlScheduleLog param = new TSmControlScheduleLog();
        TSmControlScheduleLogPK paramPk = new TSmControlScheduleLogPK();
        paramPk.setSmControlScheduleLogId(id);
        paramPk.setSmId(smId);
        param.setId(paramPk);
        param.setSettingUpdateDatetime(settingUpdateDatetime);
        param.setScheduleManageDesignationFlg(scheduleManageDesinationFlg);
        param.setCreateDate(nowTimestamp);
        param.setCreateUserId(loginUserId);
        param.setUpdateDate(nowTimestamp);
        param.setUpdateUserId(loginUserId);
        param.setVersion(0);

        persist(tSmControlScheduleLogServiceDaoImpl, param);

        return true;
    }

    /**
     * 機器制御スケジュール時間帯履歴 登録処理
     *
     * @param res
     * @param id
     * @param loginUserId
     * @param nowTimestamp
     * @param smId
     * @param latestScheduleTimeLogList
     */
    private void insertSmControlScheduleTimeLog(A200061Param res, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId, List<TSmControlScheduleTimeLog> latestScheduleTimeLogList) {
        List<Map<String, Object>> patternMapList = res.getSettingSchedulePatternList();

        for (int patternCnt = 0; patternCnt < patternMapList.size(); patternCnt++) {
            Map<String, Object> patternMap = patternMapList.get(patternCnt);
            @SuppressWarnings("unchecked")
            List<Map<String, String>> timeZoneList = (List<Map<String, String>>)patternMap.get(TIME_ZONE_LIST);

            for (int timeSlotCnt = 0; timeSlotCnt < timeZoneList.size(); timeSlotCnt++) {
                TSmControlScheduleTimeLog param = new TSmControlScheduleTimeLog();
                TSmControlScheduleTimeLogPK paramPk = new TSmControlScheduleTimeLogPK();

                paramPk.setSmControlScheduleLogId(id);
                paramPk.setSmId(smId);
                paramPk.setPatternNo(String.format("%02d", patternCnt + 1));
                paramPk.setTimeSlotNo(timeSlotCnt + 1);

                param.setId(paramPk);
                param.setStartTimeHour(Integer.parseInt(timeZoneList.get(timeSlotCnt).get(START_TIME_HOUR)));
                param.setStartTimeMinute(Integer.parseInt(timeZoneList.get(timeSlotCnt).get(START_TIME_MINUTE)));
                param.setEndTimeHour(Integer.parseInt(timeZoneList.get(timeSlotCnt).get(END_TIME_HOUR)));
                param.setEndTimeMinute(Integer.parseInt(timeZoneList.get(timeSlotCnt).get(END_TIME_MINUTE)));
                param.setCreateDate(nowTimestamp);
                param.setCreateUserId(loginUserId);
                param.setUpdateDate(nowTimestamp);
                param.setUpdateUserId(loginUserId);
                param.setVersion(0);

                persist(tSmControlScheduleTimeLogServiceDaoImpl, param);
            }
        }
    }

    /**
     * 機器制御スケジュール設定履歴 登録処理
     *
     * @param res
     * @param id
     * @param loginUserId
     * @param nowTimestamp
     * @param smId
     * @param latestScheduleSetLogList
     */
    private void insertSmControlScheduleSetLog(A200061Param res, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId, List<TSmControlScheduleSetLog> latestScheduleSetLogList) {

        for (TSmControlScheduleSetLog latestData : latestScheduleSetLogList) {

            TSmControlScheduleSetLog param = new TSmControlScheduleSetLog();
            TSmControlScheduleSetLogPK paramPk = new TSmControlScheduleSetLogPK();

            paramPk.setSmControlScheduleLogId(id);
            paramPk.setSmId(smId);
            paramPk.setTargetMonth(latestData.getId().getTargetMonth());
            paramPk.setControlLoad(latestData.getId().getControlLoad());

            param.setId(paramPk);
            param.setSundayPatternNo(latestData.getSundayPatternNo());
            param.setMondayPatternNo(latestData.getMondayPatternNo());
            param.setTuesdayPatternNo(latestData.getTuesdayPatternNo());
            param.setWednesdayPatternNo(latestData.getWednesdayPatternNo());
            param.setThursdayPatternNo(latestData.getThursdayPatternNo());
            param.setFridayPatternNo(latestData.getFridayPatternNo());
            param.setSaturdayPatternNo(latestData.getSaturdayPatternNo());
            param.setHolidayPatternNo(latestData.getHolidayPatternNo());
            param.setSpecificDate1(latestData.getSpecificDate1());
            param.setSpecificDate2(latestData.getSpecificDate2());
            param.setSpecificDate3(latestData.getSpecificDate3());
            param.setSpecificDate4(latestData.getSpecificDate4());
            param.setSpecificDate5(latestData.getSpecificDate5());
            param.setSpecificDate6(latestData.getSpecificDate6());
            param.setSpecificDate7(latestData.getSpecificDate7());
            param.setSpecificDate8(latestData.getSpecificDate8());
            param.setSpecificDate9(latestData.getSpecificDate9());
            param.setSpecificDate10(latestData.getSpecificDate10());
            param.setSpecificDatePatternNo1(latestData.getSpecificDatePatternNo1());
            param.setSpecificDatePatternNo2(latestData.getSpecificDatePatternNo2());
            param.setSpecificDatePatternNo3(latestData.getSpecificDatePatternNo3());
            param.setSpecificDatePatternNo4(latestData.getSpecificDatePatternNo4());
            param.setSpecificDatePatternNo5(latestData.getSpecificDatePatternNo5());
            param.setSpecificDatePatternNo6(latestData.getSpecificDatePatternNo6());
            param.setSpecificDatePatternNo7(latestData.getSpecificDatePatternNo7());
            param.setSpecificDatePatternNo8(latestData.getSpecificDatePatternNo8());
            param.setSpecificDatePatternNo9(latestData.getSpecificDatePatternNo9());
            param.setSpecificDatePatternNo10(latestData.getSpecificDatePatternNo10());
            param.setCreateDate(nowTimestamp);
            param.setCreateUserId(loginUserId);
            param.setUpdateDate(nowTimestamp);
            param.setUpdateUserId(loginUserId);
            param.setVersion(0);

            persist(tSmControlScheduleSetLogServiceDaoImpl, param);
        }
    }

    /**
     * 機器IDから最新のスケジュール履歴を取得
     *
     * @param smId
     * @return
     */
    private TSmControlScheduleLog getLatestLog(Long smId) {
        TSmControlScheduleLog paramLog = new TSmControlScheduleLog();
        TSmControlScheduleLogPK paramLogPk = new TSmControlScheduleLogPK();
        paramLogPk.setSmId(smId);
        paramLog.setId(paramLogPk);

        List<TSmControlScheduleLog> logList = getResultList(tSmControlScheduleLogServiceDaoImpl,paramLog);
        TSmControlScheduleLog latestLog = logList.stream()
                                                 .sorted(Comparator.comparing(o -> o.getId().getSmControlScheduleLogId(),Comparator.reverseOrder())).findFirst()
                                                 .orElse(null);

        return latestLog;
    }

    /**
     * 最新の機器制御設定履歴情報を取得
     *
     * @param latestLog
     * @return
     */
    private List<TSmControlScheduleSetLog>  getLatestSmControlScheduleSetList(TSmControlScheduleLog latestLog) {

        List<TSmControlScheduleSetLog> registList = new ArrayList<>();

        if (latestLog == null) {
            return registList;
        }

        TSmControlScheduleSetLog paramSetLog = new TSmControlScheduleSetLog();
        TSmControlScheduleSetLogPK paramSetLogPk = new TSmControlScheduleSetLogPK();
        paramSetLogPk.setSmControlScheduleLogId(latestLog.getId().getSmControlScheduleLogId());
        paramSetLog.setId(paramSetLogPk);

        List<TSmControlScheduleSetLog> resultList = getResultList(tSmControlScheduleSetLogServiceDaoImpl, paramSetLog);

        for(TSmControlScheduleSetLog result:resultList) {
            TSmControlScheduleSetLog param = new TSmControlScheduleSetLog();
            TSmControlScheduleSetLogPK paramPk = new TSmControlScheduleSetLogPK();
            paramPk.setSmId(result.getId().getSmId());
            paramPk.setControlLoad(result.getId().getControlLoad());
            paramPk.setTargetMonth(result.getId().getTargetMonth());
            param.setId(paramPk);
            param.setSundayPatternNo(result.getSundayPatternNo());
            param.setMondayPatternNo(result.getMondayPatternNo());
            param.setTuesdayPatternNo(result.getTuesdayPatternNo());
            param.setWednesdayPatternNo(result.getWednesdayPatternNo());
            param.setThursdayPatternNo(result.getThursdayPatternNo());
            param.setFridayPatternNo(result.getFridayPatternNo());
            param.setSaturdayPatternNo(result.getSaturdayPatternNo());
            param.setHolidayPatternNo(result.getHolidayPatternNo());
            param.setSpecificDate1(result.getSpecificDate1());
            param.setSpecificDate2(result.getSpecificDate2());
            param.setSpecificDate3(result.getSpecificDate3());
            param.setSpecificDate4(result.getSpecificDate4());
            param.setSpecificDate5(result.getSpecificDate5());
            param.setSpecificDate6(result.getSpecificDate6());
            param.setSpecificDate7(result.getSpecificDate7());
            param.setSpecificDate8(result.getSpecificDate8());
            param.setSpecificDate9(result.getSpecificDate9());
            param.setSpecificDate10(result.getSpecificDate10());
            param.setSpecificDatePatternNo1(result.getSpecificDatePatternNo1());
            param.setSpecificDatePatternNo2(result.getSpecificDatePatternNo2());
            param.setSpecificDatePatternNo3(result.getSpecificDatePatternNo3());
            param.setSpecificDatePatternNo4(result.getSpecificDatePatternNo4());
            param.setSpecificDatePatternNo5(result.getSpecificDatePatternNo5());
            param.setSpecificDatePatternNo6(result.getSpecificDatePatternNo6());
            param.setSpecificDatePatternNo7(result.getSpecificDatePatternNo7());
            param.setSpecificDatePatternNo8(result.getSpecificDatePatternNo8());
            param.setSpecificDatePatternNo9(result.getSpecificDatePatternNo9());
            param.setSpecificDatePatternNo10(result.getSpecificDatePatternNo10());

            registList.add(param);
        }

        return registList;

    }

    /**
     * 最新の機器制御スケジュール時間帯履歴情報を取得
     *
     * @param latestLog
     * @return
     */
    private List<TSmControlScheduleTimeLog> getLatestSmControlScheduleTimeList(TSmControlScheduleLog latestLog) {

        List<TSmControlScheduleTimeLog> registList = new ArrayList<>();

        if (latestLog == null) {
            return registList;
        }

        TSmControlScheduleTimeLog paramSetLog = new TSmControlScheduleTimeLog();
        TSmControlScheduleTimeLogPK paramSetLogPk = new TSmControlScheduleTimeLogPK();
        paramSetLogPk.setSmControlScheduleLogId(latestLog.getId().getSmControlScheduleLogId());
        paramSetLog.setId(paramSetLogPk);

        List<TSmControlScheduleTimeLog> resultList = getResultList(tSmControlScheduleTimeLogServiceDaoImpl, paramSetLog);

        for(TSmControlScheduleTimeLog result:resultList) {
            TSmControlScheduleTimeLog param = new TSmControlScheduleTimeLog();
            TSmControlScheduleTimeLogPK paramPk = new TSmControlScheduleTimeLogPK();
            paramPk.setSmId(result.getId().getSmId());
            paramPk.setPatternNo(result.getId().getPatternNo());
            paramPk.setTimeSlotNo(result.getId().getTimeSlotNo());
            param.setId(paramPk);
            param.setStartTimeHour(result.getStartTimeHour());
            param.setStartTimeMinute(result.getStartTimeMinute());
            param.setEndTimeHour(result.getEndTimeHour());
            param.setEndTimeMinute(result.getEndTimeMinute());

            registList.add(param);
        }

        return registList;

    }

}
