package jp.co.osaki.osol.api.dao.smcontrol;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.smcontrol.SmControlScheduleLogRegistResult;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleDutyLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleSetLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleTimeLogServiceDaoImpl;
import jp.co.osaki.osol.entity.TSmControlScheduleDutyLog;
import jp.co.osaki.osol.entity.TSmControlScheduleDutyLogPK;
import jp.co.osaki.osol.entity.TSmControlScheduleLog;
import jp.co.osaki.osol.entity.TSmControlScheduleLogPK;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLog;
import jp.co.osaki.osol.entity.TSmControlScheduleSetLogPK;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLog;
import jp.co.osaki.osol.entity.TSmControlScheduleTimeLogPK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200005Param;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * スケジュール(設定) Dao クラス.
 *
 * @author sakamoto
 *
 */
@Stateless
public class ScheduleUpdateDao extends BaseSmControlDao {
    private final TSmControlScheduleLogServiceDaoImpl tSmControlScheduleLogServiceDaoImpl;
    private final TSmControlScheduleTimeLogServiceDaoImpl tSmControlScheduleTimeLogServiceDaoImpl;
    private final TSmControlScheduleDutyLogServiceDaoImpl tSmControlScheduleDutyLogServiceDaoImpl;
    private final TSmControlScheduleSetLogServiceDaoImpl tSmControlScheduleSetLogServiceDaoImpl;

    private static final String START_TIME_HOUR = "startHour";
    private static final String START_TIME_MINUTE = "startMinute";
    private static final String END_TIME_HOUR = "endHour";
    private static final String END_TIME_MINUTE = "endMinute";
    private static final String DUTY_ON_TIME_MINUTE = "dutyOnTime";
    private static final String DUTY_OFF_TIME_MINUTE = "dutyOffTime";
    private static final String DUTY_CONTROL_TIME_SLOT = "dutySelect";
    private static final String SUNDAY_PATTERN = "sundayPattern";
    private static final String MONDAY_PATTERN = "mondayPattern";
    private static final String TUESDAY_PATTERN = "tuesdayPattern";
    private static final String WEDNESDAY_PATTERN = "wednesdayPattern";
    private static final String THURSDAY_PATTERN = "thursdayPattern";
    private static final String FRIDAY_PATTERN = "fridayPattern";
    private static final String SATURDAY_PATTERN = "saturdayPattern";
    private static final String NATIONAL_HOLIDAY_PATTERN = "nationalHolidayPattern";
    private static final String SPECIFIC_DAY_ASSIGNMENT = "specificDayAssignment";
    private static final String SPECIFIC_DAY_PATTERN = "specificDayPattern";
    private static final String LOADLIST_MONTH_LIST_NAME = "settingMonthScheduleList";


    public ScheduleUpdateDao() {
        tSmControlScheduleLogServiceDaoImpl = new TSmControlScheduleLogServiceDaoImpl();
        tSmControlScheduleTimeLogServiceDaoImpl = new TSmControlScheduleTimeLogServiceDaoImpl();
        tSmControlScheduleDutyLogServiceDaoImpl = new TSmControlScheduleDutyLogServiceDaoImpl();
        tSmControlScheduleSetLogServiceDaoImpl = new TSmControlScheduleSetLogServiceDaoImpl();
    }

    /**
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    public SmControlScheduleLogRegistResult updateSchedule(FvpCtrlMngResponse<A200005Param> res,Long loginUserId) throws Exception {

     // DBサーバ時刻取得
        Timestamp nowTimestamp = super.getServerDateTime();
        Long id = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_SCHEDULE_LOG_ID.getVal());

        A200005Param resParam = res.getParam();

        //DB上の最新スケジュールを保持する
        TSmControlScheduleLog latestLog =  getLatestLog(res.getSmId());

        insertSmControlScheduleLog(resParam,id,loginUserId,nowTimestamp,res.getSmId());

        insertSmControlScheduleTimeLog(resParam,id,loginUserId,nowTimestamp,res.getSmId());

        insertSmControlScheduleDutyLog(resParam,id,loginUserId,nowTimestamp,res.getSmId());

        insertSmControlScheduleSetLog(resParam,id,loginUserId,nowTimestamp,res.getSmId(),latestLog);

        return new SmControlScheduleLogRegistResult();
    }

    /**
     * 機器制御スケジュール履歴 登録処理
     */
    private boolean insertSmControlScheduleLog( A200005Param resParam,Long id,Long loginUserId,Timestamp nowTimestamp,Long smId) {

      //対象の日付文字列の長さが違う場合は登録しない
        if(resParam.getSettingDate() == null || resParam.getSettingDate().length() != 11) {
            return false;
        }
        String ymd = "20" + resParam.getSettingDate().substring(0, 6);
        String hm = resParam.getSettingDate().substring(7, 11);
        Date settingDate = DateUtility.conversionDate(ymd+hm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Timestamp settingUpdateDatetime = new Timestamp(settingDate.getTime());

        TSmControlScheduleLog param = new TSmControlScheduleLog();
        TSmControlScheduleLogPK paramPk = new TSmControlScheduleLogPK();
        paramPk.setSmControlScheduleLogId(id);
        paramPk.setSmId(smId);
        param.setId(paramPk);
        param.setSettingUpdateDatetime(settingUpdateDatetime);
        param.setScheduleManageDesignationFlg(Integer.parseInt(resParam.getScheduleManageAssignment()));
        param.setCreateDate(nowTimestamp);
        param.setCreateUserId(loginUserId);
        param.setUpdateDate(nowTimestamp);
        param.setUpdateUserId(loginUserId);
        param.setVersion(0);

        persist(tSmControlScheduleLogServiceDaoImpl,param);

        return true;
    }

    /**
     * 機器制御スケジュール時間帯履歴 登録処理
     */
    private void insertSmControlScheduleTimeLog(A200005Param res,Long id,Long loginUserId,Timestamp nowTimestamp,Long smId) {
        List<Map<String, String>> patternMapList = res.getSettingSchedulePatternList();

        for(int patternCnt = 0; patternCnt < patternMapList.size() ; patternCnt++){
            Map<String,String> patternMap = patternMapList.get(patternCnt);
            for(int timeSlotCnt = 1;timeSlotCnt <= 4; timeSlotCnt++) {
                TSmControlScheduleTimeLog param = new TSmControlScheduleTimeLog();
                TSmControlScheduleTimeLogPK paramPk = new TSmControlScheduleTimeLogPK();
                paramPk.setSmControlScheduleLogId(id);
                paramPk.setSmId(smId);
                paramPk.setPatternNo(String.format("%02d", patternCnt + 1));
                paramPk.setTimeSlotNo(timeSlotCnt);
                param.setId(paramPk);
                param.setStartTimeHour(Integer.parseInt(patternMap.get(START_TIME_HOUR + String.valueOf(timeSlotCnt))));
                param.setStartTimeMinute(Integer.parseInt(patternMap.get(START_TIME_MINUTE + String.valueOf(timeSlotCnt))));
                param.setEndTimeHour(Integer.parseInt(patternMap.get(END_TIME_HOUR + String.valueOf(timeSlotCnt))));
                param.setEndTimeMinute(Integer.parseInt(patternMap.get(END_TIME_MINUTE + String.valueOf(timeSlotCnt))));
                param.setCreateDate(nowTimestamp);
                param.setCreateUserId(loginUserId);
                param.setUpdateDate(nowTimestamp);
                param.setUpdateUserId(loginUserId);
                param.setVersion(0);

                persist(tSmControlScheduleTimeLogServiceDaoImpl,param);
            }

        }

    }

    /**
     * 機器制御スケジュールデューティ履歴 登録処理
     */
    private void insertSmControlScheduleDutyLog(A200005Param res,Long id,Long loginUserId,Timestamp nowTimestamp,Long smId) {
        List<Map<String, String>> patternMapList = res.getSettingSchedulePatternList();

        boolean fv2Flg = false;
        if(res.getProductCd().equals(SmControlConstants.PRODUCT_CD_FV2)) {
            fv2Flg = true;
        }

        for(int patternCnt = 0; patternCnt < patternMapList.size() ; patternCnt++){
            Map<String,String> patternMap = patternMapList.get(patternCnt);
            TSmControlScheduleDutyLog param = new TSmControlScheduleDutyLog();
            TSmControlScheduleDutyLogPK paramPk = new TSmControlScheduleDutyLogPK();
            paramPk.setSmControlScheduleLogId(id);
            paramPk.setSmId(smId);
            paramPk.setPatternNo(String.format("%02d", patternCnt + 1));
            param.setId(paramPk);
            param.setDutyOnTimeMinute(Integer.parseInt(patternMap.get(DUTY_ON_TIME_MINUTE)));
            param.setDutyOffTimeMinute(Integer.parseInt(patternMap.get(DUTY_OFF_TIME_MINUTE)));
            //FV2の場合、デューティ選択が存在しないため常に「すべて無効」
            if(fv2Flg) {
                param.setDutyControlTimeSlot("0");
            }else {
                param.setDutyControlTimeSlot(patternMap.get(DUTY_CONTROL_TIME_SLOT));
            }

            param.setCreateDate(nowTimestamp);
            param.setCreateUserId(loginUserId);
            param.setUpdateDate(nowTimestamp);
            param.setUpdateUserId(loginUserId);
            param.setVersion(0);

            persist(tSmControlScheduleDutyLogServiceDaoImpl,param);

        }
    }

    /**
     * 機器制御スケジュール設定履歴 登録処理
     */
    @SuppressWarnings("unchecked")
    private void insertSmControlScheduleSetLog(A200005Param res,Long id,Long loginUserId,Timestamp nowTimestamp,Long smId,TSmControlScheduleLog latestLog) {

        List<TSmControlScheduleSetLog> registList = new ArrayList<>();
        BigDecimal startControlLoad;
        boolean g2Flg = res.getProductCd().equals(SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2);
        if(g2Flg) {
            if(latestLog != null) {
                registList = getLatestSmControlScheduleSetList(latestLog);
            }
             //開始負荷番号を算出
             String targetPage = res.getCommand().substring(1, 3);
             startControlLoad = BigDecimal.valueOf(12 * Integer.parseInt(targetPage));
        }else {
            startControlLoad = BigDecimal.ZERO;
        }

        List<Map<String, Object>> scheduleMapList = res.getLoadList();
        for(int loadCnt = 0 ; loadCnt < scheduleMapList.size(); loadCnt++){
            Map<String,Object> scheduleMap = scheduleMapList.get(loadCnt);
            List<Map<String,String>> monthlyScheduleList = (List<Map<String,String>>)scheduleMap.get(LOADLIST_MONTH_LIST_NAME) ;
            for(int monthCnt = 0; monthCnt < monthlyScheduleList.size(); monthCnt++) {
                Map<String,String> monthlyScheduleMap = monthlyScheduleList.get(monthCnt);
                TSmControlScheduleSetLog param = new TSmControlScheduleSetLog();
                TSmControlScheduleSetLogPK paramPk = new TSmControlScheduleSetLogPK();
                paramPk.setSmId(smId);
                paramPk.setControlLoad(BigDecimal.valueOf(loadCnt  +1).add(startControlLoad));
                paramPk.setTargetMonth(BigDecimal.valueOf(monthCnt + 1));
                param.setId(paramPk);
                param.setSundayPatternNo(monthlyScheduleMap.get(SUNDAY_PATTERN));
                param.setMondayPatternNo(monthlyScheduleMap.get(MONDAY_PATTERN));
                param.setTuesdayPatternNo(monthlyScheduleMap.get(TUESDAY_PATTERN));
                param.setWednesdayPatternNo(monthlyScheduleMap.get(WEDNESDAY_PATTERN));
                param.setThursdayPatternNo(monthlyScheduleMap.get(THURSDAY_PATTERN));
                param.setFridayPatternNo(monthlyScheduleMap.get(FRIDAY_PATTERN));
                param.setSaturdayPatternNo(monthlyScheduleMap.get(SATURDAY_PATTERN));
                param.setHolidayPatternNo(monthlyScheduleMap.get(NATIONAL_HOLIDAY_PATTERN));
                param.setSpecificDate1(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "1")));
                param.setSpecificDate2(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "2")));
                param.setSpecificDate3(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "3")));
                param.setSpecificDate4(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "4")));
                param.setSpecificDate5(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "5")));
                param.setSpecificDate6(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "6")));
                param.setSpecificDate7(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "7")));
                param.setSpecificDate8(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "8")));
                param.setSpecificDate9(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "9")));
                param.setSpecificDate10(Integer.parseInt(monthlyScheduleMap.get(SPECIFIC_DAY_ASSIGNMENT + "10")));
                param.setSpecificDatePatternNo1(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "1"));
                param.setSpecificDatePatternNo2(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "2"));
                param.setSpecificDatePatternNo3(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "3"));
                param.setSpecificDatePatternNo4(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "4"));
                param.setSpecificDatePatternNo5(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "5"));
                param.setSpecificDatePatternNo6(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "6"));
                param.setSpecificDatePatternNo7(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "7"));
                param.setSpecificDatePatternNo8(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "8"));
                param.setSpecificDatePatternNo9(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "9"));
                param.setSpecificDatePatternNo10(monthlyScheduleMap.get(SPECIFIC_DAY_PATTERN + "10"));

                //G2の場合、同じ負荷番号、月番号のレコードを削除する
                if(g2Flg) {
                    //削除条件を指定
                    Predicate<TSmControlScheduleSetLog> predicateControlLoad = o -> { return o.getId().getControlLoad().equals(param.getId().getControlLoad()); };
                    Predicate<TSmControlScheduleSetLog> predicateMonth = o -> { return o.getId().getTargetMonth().equals(param.getId().getTargetMonth()); };
                    registList.removeIf(predicateControlLoad.and(predicateMonth));
                }

                registList.add(param);

            }
        }

        //登録処理
        for(TSmControlScheduleSetLog regist:registList) {
            regist.getId().setSmControlScheduleLogId(id);
            regist.setCreateDate(nowTimestamp);
            regist.setCreateUserId(loginUserId);
            regist.setUpdateDate(nowTimestamp);
            regist.setUpdateUserId(loginUserId);
            regist.setVersion(0);

            persist(tSmControlScheduleSetLogServiceDaoImpl,regist);
        }

    }

    private TSmControlScheduleLog getLatestLog(Long smId) {
        TSmControlScheduleLog paramLog = new TSmControlScheduleLog();
        TSmControlScheduleLogPK paramLogPk = new TSmControlScheduleLogPK();
        paramLogPk.setSmId(smId);
        paramLog.setId(paramLogPk);

        List<TSmControlScheduleLog> logList = getResultList(tSmControlScheduleLogServiceDaoImpl,paramLog);
        TSmControlScheduleLog latestLog = logList.stream().sorted(Comparator.comparing(o -> o.getId().getSmControlScheduleLogId(),Comparator.reverseOrder())).findFirst().orElse(null);

        return latestLog;
    }

    /**
     * 機器番号から最新のスケジュール設定情報を取得し、新しいリストに詰める
     * @param smId
     * @return
     */
    private List<TSmControlScheduleSetLog>  getLatestSmControlScheduleSetList(TSmControlScheduleLog latestLog){


        TSmControlScheduleSetLog paramSetLog = new TSmControlScheduleSetLog();
        TSmControlScheduleSetLogPK paramSetLogPk = new TSmControlScheduleSetLogPK();
        paramSetLogPk.setSmControlScheduleLogId(latestLog.getId().getSmControlScheduleLogId());
        paramSetLog.setId(paramSetLogPk);

        List<TSmControlScheduleSetLog> resultList = getResultList(tSmControlScheduleSetLogServiceDaoImpl,paramSetLog);

        List<TSmControlScheduleSetLog> registList = new ArrayList<>();
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

}
