package jp.co.osaki.osol.api.dao.smcontrol;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlScheduleLogRegistResult;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleDutyLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleSetLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlScheduleTimeLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;
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
import jp.co.osaki.osol.mng.param.A200060Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;


/**
 *
 * 複数建物・テナント一括 制御 Dao クラス
 *
 * @author da_yamano
 *
 */
@Stateless
public class BulkCtrlDao extends BaseSmControlDao{

    // ServiceDAO呼出

    // 目標電力設定
    private final BuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
    private final BuildingServiceDaoImpl buildingServiceDaoImpl;

    // スケジュール設定
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
    private static final int CONTROL_LOAD_MAX = 16;
    private static final int CONTROL_LOAD_OF_1TIME = CONTROL_LOAD_MAX / 2;

    public BulkCtrlDao() {
        // 目標電力設定
        buildingDmServiceDaoImpl = new BuildingDmServiceDaoImpl();
        buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
        buildingServiceDaoImpl = new BuildingServiceDaoImpl();
        // スケジュール設定
        tSmControlScheduleLogServiceDaoImpl = new TSmControlScheduleLogServiceDaoImpl();
        tSmControlScheduleTimeLogServiceDaoImpl = new TSmControlScheduleTimeLogServiceDaoImpl();
        tSmControlScheduleDutyLogServiceDaoImpl = new TSmControlScheduleDutyLogServiceDaoImpl();
        tSmControlScheduleSetLogServiceDaoImpl = new TSmControlScheduleSetLogServiceDaoImpl();
    }

    // 1トランザクションで複数テーブルに更新をかけるため、
    // DAO内に処理を記載

    // 目標電力設定DB更新処理
    public void updateBuildingDM(BuildingDmResult param) throws Exception{

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

        // 建物機器Result
        BuildingSmResult paramPk = new BuildingSmResult();
        paramPk.setSmId(param.getSmId());
        // 建物機器検索
        List<BuildingSmResult> paramPkList;
        paramPkList = getResultList(buildingSmServiceDaoImpl, paramPk);

        for (BuildingSmResult paramPkelm : paramPkList) {
            param.setCorpId(paramPkelm.getCorpId());
            param.setBuildingId(paramPkelm.getBuildingId());
            param.setUpdateDate(updateDate);

            // 建物デマンドTBL_更新処理
            if (merge(buildingDmServiceDaoImpl, param) == null) {
                // 更新がない場合、後続のDB更新は実施しない
                continue;
            }

            // 建物デマンドに更新があった場合、設定対象の建物の情報を更新する
            BuildingResult buildingParam = new BuildingResult();

            // 値をセット
            buildingParam.setCorpId(paramPkelm.getCorpId());
            buildingParam.setBuildingId(paramPkelm.getBuildingId());
            buildingParam.setUpdateUserId(param.getUpdateUserId());
            buildingParam.setUpdateDate(updateDate);

            // 建物TBL_更新処理
            merge(buildingServiceDaoImpl, buildingParam);

        }

        return ;
    }


    // スケジュール設定DB更新処理
    /**
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    public SmControlScheduleLogRegistResult updateSchedule(FvpCtrlMngResponse<BaseParam> res,Long loginUserId) throws Exception {

        // DBサーバ時刻取得
        Timestamp nowTimestamp = super.getServerDateTime();
        Long id = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_SCHEDULE_LOG_ID.getVal());

        A200005Param resParam = (A200005Param) res.getParam();

        // DB上の最新スケジュールを保持する
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

        // 対象の日付文字列の長さが違う場合は登録しない
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
            // FV2の場合、デューティ選択が存在しないため常に「すべて無効」
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
             // 開始負荷番号を算出
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
                paramPk.setControlLoad(BigDecimal.valueOf(loadCnt + 1).add(startControlLoad));
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

                // G2の場合、同じ負荷番号、月番号のレコードを削除する
                if(g2Flg) {
                    //削除条件を指定
                    Predicate<TSmControlScheduleSetLog> predicateControlLoad = o -> { return o.getId().getControlLoad().equals(param.getId().getControlLoad()); };
                    Predicate<TSmControlScheduleSetLog> predicateMonth = o -> { return o.getId().getTargetMonth().equals(param.getId().getTargetMonth()); };
                    registList.removeIf(predicateControlLoad.and(predicateMonth));
                }

                registList.add(param);

            }
        }

        // 登録処理
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

        List<TSmControlScheduleSetLog> registList = new ArrayList<>();

        if (latestLog == null) {
            return registList;
        }

        TSmControlScheduleSetLog paramSetLog = new TSmControlScheduleSetLog();
        TSmControlScheduleSetLogPK paramSetLogPk = new TSmControlScheduleSetLogPK();
        paramSetLogPk.setSmControlScheduleLogId(latestLog.getId().getSmControlScheduleLogId());
        paramSetLog.setId(paramSetLogPk);

        List<TSmControlScheduleSetLog> resultList = getResultList(tSmControlScheduleSetLogServiceDaoImpl,paramSetLog);

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
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    public SmControlScheduleLogRegistResult updateScheduleEa(FvpCtrlMngResponse<BaseParam> res, Long loginUserId) throws Exception {

        // DBサーバ時刻取得
        Timestamp nowTimestamp = super.getServerDateTime();
        Long id = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_SCHEDULE_LOG_ID.getVal());

        A200060Param resParam = (A200060Param) res.getParam();

        //DB上の最新スケジュールを取得
        TSmControlScheduleLog latestLog =  getLatestLog(res.getSmId());
        List<TSmControlScheduleTimeLog> latestScheduleTimeLogList = getLatestSmControlScheduleTimeList(latestLog);
        List<TSmControlScheduleSetLog> latestScheduleSetLogList = getLatestSmControlScheduleSetList(latestLog);

        //機器制御スケジュール履歴登録
        insertSmControlScheduleLogEa(resParam, id, loginUserId, nowTimestamp, res.getSmId());

        //機器制御スケジュール時間帯履歴登録
        insertSmControlScheduleTimeLogEa(resParam, id, loginUserId, nowTimestamp, res.getSmId(), latestScheduleTimeLogList);

        //機器制御スケジュール設定履歴登録
        insertSmControlScheduleSetLogEa(resParam, id, loginUserId, nowTimestamp, res.getSmId(), latestScheduleSetLogList);

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
    private boolean insertSmControlScheduleLogEa(A200060Param resParam, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId) {

        String settingDateString = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(nowTimestamp);
        String ymd = "20" + settingDateString.substring(0, 6);
        String hm = settingDateString.substring(6, 10);
        Date settingDate = DateUtility.conversionDate(ymd + hm, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Timestamp settingUpdateDatetime = new Timestamp(settingDate.getTime());

        TSmControlScheduleLog param = new TSmControlScheduleLog();
        TSmControlScheduleLogPK paramPk = new TSmControlScheduleLogPK();
        paramPk.setSmControlScheduleLogId(id);
        paramPk.setSmId(smId);
        param.setId(paramPk);
        param.setSettingUpdateDatetime(settingUpdateDatetime);
        param.setScheduleManageDesignationFlg(Integer.parseInt(resParam.getScheduleControl()));
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
     *
     * @param res
     * @param id
     * @param loginUserId
     * @param nowTimestamp
     * @param smId
     * @param latestScheduleTimeLogList
     */
    private void insertSmControlScheduleTimeLogEa(A200060Param res, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId, List<TSmControlScheduleTimeLog> latestScheduleTimeLogList) {

        for (TSmControlScheduleTimeLog latestScheduleTimeLog : latestScheduleTimeLogList) {
            TSmControlScheduleTimeLog param = new TSmControlScheduleTimeLog();
            TSmControlScheduleTimeLogPK paramPk = new TSmControlScheduleTimeLogPK();
            paramPk.setSmControlScheduleLogId(id);
            paramPk.setSmId(smId);
            paramPk.setPatternNo(latestScheduleTimeLog.getId().getPatternNo());
            paramPk.setTimeSlotNo(latestScheduleTimeLog.getId().getTimeSlotNo());
            param.setId(paramPk);
            param.setStartTimeHour(latestScheduleTimeLog.getStartTimeHour());        // 開始時間情報は機器から取得出来ないため、最新情報を保持
            param.setStartTimeMinute(latestScheduleTimeLog.getStartTimeMinute());
            param.setEndTimeHour(latestScheduleTimeLog.getEndTimeHour());            // 終了時間情報は機器から取得出来ないため、最新情報を保持
            param.setEndTimeMinute(latestScheduleTimeLog.getEndTimeMinute());
            param.setCreateDate(nowTimestamp);
            param.setCreateUserId(loginUserId);
            param.setUpdateDate(nowTimestamp);
            param.setUpdateUserId(loginUserId);
            param.setVersion(0);

            persist(tSmControlScheduleTimeLogServiceDaoImpl,param);
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
     * @return
     */
    private boolean insertSmControlScheduleSetLogEa(A200060Param res, Long id, Long loginUserId, Timestamp nowTimestamp, Long smId, List<TSmControlScheduleSetLog> latestScheduleSetLogList) {

        List<TSmControlScheduleSetLog> registList = new ArrayList<>();
        int controlLoadOffset = 1;

        String scheduleControl = getScheduleControlInfoFromCommand(res);
        if (!SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControl) && !SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControl)) {
            // スケジュール制御 が 不正の場合は登録しない
            return false;
        }

        //登録するデータ数を判定
        int controlLoadNum = getInsertControlLoadSize(res, latestScheduleSetLogList);

        //登録データ作成
        List<Map<String, Object>> loadList = res.getLoadList();
        if (controlLoadNum >= CONTROL_LOAD_MAX) {
            List<TSmControlScheduleSetLog> latestDataList = new ArrayList<>(latestScheduleSetLogList);
            if (getLatestControlLoadTypeNum(latestDataList) >= CONTROL_LOAD_MAX) {
                // DBに登録されている最新データが最大数登録されている場合、最新データを流用する分を抜粋
                if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControl)) {
                    latestDataList = latestDataList.stream()
                                                   .filter(data -> data.getId().getControlLoad().intValue() >= (CONTROL_LOAD_OF_1TIME + 1))
                                                   .collect(Collectors.toList());
                } else {
                    latestDataList = latestDataList.stream()
                                                   .filter(data -> data.getId().getControlLoad().intValue() < (CONTROL_LOAD_OF_1TIME + 1))
                                                   .collect(Collectors.toList());
                }
            }

            // 1情報を登録
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControl)) {
                // 機器から取得したデータから登録データを生成
                addScheduleSetLogDataFromRes(registList, loadList, controlLoadOffset, smId);
            } else {
                // DBに登録されている最新データから登録データを生成
                addScheduleSetLogDataFromLatest(registList, latestDataList, controlLoadOffset, smId);
            }

            // 2情報を登録
            controlLoadOffset = CONTROL_LOAD_OF_1TIME + 1;
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControl)) {
                // 機器から取得したデータから登録データを生成
                addScheduleSetLogDataFromRes(registList, loadList, controlLoadOffset, smId);
            } else {
                // DBに登録されている最新データから登録データを生成
                addScheduleSetLogDataFromLatest(registList, latestDataList, controlLoadOffset, smId);
            }
        } else {
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_2.equals(scheduleControl)) {
                controlLoadOffset = CONTROL_LOAD_OF_1TIME + 1;
            }
            addScheduleSetLogDataFromRes(registList, loadList, controlLoadOffset, smId);
        }

        //登録処理
        for (TSmControlScheduleSetLog regist : registList) {
            regist.getId().setSmControlScheduleLogId(id);
            regist.setCreateDate(nowTimestamp);
            regist.setCreateUserId(loginUserId);
            regist.setUpdateDate(nowTimestamp);
            regist.setUpdateUserId(loginUserId);
            regist.setVersion(0);

            persist(tSmControlScheduleSetLogServiceDaoImpl, regist);
        }

        return true;
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

    /**
     * 今回登録するデータサイズを取得
     *
     * @param actualData
     * @param latestDataList
     * @return
     */
    private int getInsertControlLoadSize(A200060Param actualData, List<TSmControlScheduleSetLog> latestDataList) {
        int ret = 0;
        int minDataSize = 0;
        int maxDataSize = 0;
        int actualDataSize = actualData.getLoadList().size();
        int latestDataSize = getLatestControlLoadTypeNum(latestDataList);
        String scheduleControlInfo = null;

        // データ数を判定
        String productCd = actualData.getProductCd();
        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd)) {
            minDataSize = SmControlConstants.SCHEDULE_LOAD_LIST_E_ALPHA;
            maxDataSize = minDataSize * 2;
        } else if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            minDataSize = SmControlConstants.SCHEDULE_LOAD_LIST_E_ALPHA_2;
            maxDataSize = minDataSize * 2;
        } else {
            return ret;
        }

        // スケジュール制御情報を判定
        scheduleControlInfo = getScheduleControlInfoFromCommand(actualData);
        if (scheduleControlInfo == null) {
            return ret;
        }

        // 登録するデータサイズを判定
        //      最新データの登録なし                      → 取得したデータ数分のデータを登録
        //      最新データ数が最大数                      → 最大数分のデータを登録 (取得したデータ数が不足していれば、
        //                                                   登録されているデータで不足分を補い、登録)
        //      最新データ数がスケジュール制御情報数1つ分 → 登録されているデータの情報を判別し、
        //                                                   取得したデータと同じ種別であればスケジュール制御情報数1つ分、
        //                                                   そうでなければ 最大数分を登録
        if (latestDataSize == 0) {
            ret = actualDataSize;
        } else if (latestDataSize == maxDataSize) {
            ret = latestDataSize;
        } else {
            if (SmControlConstants.SCHEDULE_CONTROL_INFO_1.equals(scheduleControlInfo)) {
                if (latestDataList.stream().allMatch(data -> data.getId().getControlLoad().intValue() >= (CONTROL_LOAD_OF_1TIME + 1))) {
                    ret = maxDataSize;
                } else {
                    ret = minDataSize;
                }
            } else {
                if (latestDataList.stream().allMatch(data -> data.getId().getControlLoad().intValue() < (CONTROL_LOAD_OF_1TIME + 1))) {
                    ret = maxDataSize;
                } else {
                    ret = minDataSize;
                }
            }
        }

        return ret;
    }

    /**
     * コマンドからスケジュール制御情報を判定
     * (コマンドの下3桁より判定)
     *
     * @param data
     * @return
     */
    private String getScheduleControlInfoFromCommand(A200060Param data) {
       String ret = null;

       String command = data.getCommand().substring(2);
       if (SmControlConstants.SCHEDULE_CONTROL_INFO_1_CMD.equals(command)) {
           ret = SmControlConstants.SCHEDULE_CONTROL_INFO_1;
       } else if (SmControlConstants.SCHEDULE_CONTROL_INFO_2_CMD.equals(command)) {
           ret = SmControlConstants.SCHEDULE_CONTROL_INFO_2;
       } else {
           // NOT PROCESS
       }

       return ret;
    }

    /**
     * 機器からの取得データから、今回登録用のデータリストを作成
     *
     * @param registList
     * @param dataList
     * @param controlLoadOffset
     * @param smId
     */
    private void addScheduleSetLogDataFromRes(List<TSmControlScheduleSetLog> registList, List<Map<String, Object>> dataList, int controlLoadOffset, Long smId) {
        for (int loadCnt = 0; loadCnt < dataList.size(); loadCnt++) {
            Map<String, Object> scheduleMap = dataList.get(loadCnt);
            @SuppressWarnings("unchecked")
            List<Map<String, String>> settingMonthScheduleList = (List<Map<String, String>>)scheduleMap.get(LOADLIST_MONTH_LIST_NAME) ;

            for (int monthCnt = 0; monthCnt < settingMonthScheduleList.size(); monthCnt++) {
                TSmControlScheduleSetLog param = new TSmControlScheduleSetLog();
                TSmControlScheduleSetLogPK paramPk = new TSmControlScheduleSetLogPK();

                paramPk.setSmId(smId);
                paramPk.setTargetMonth(BigDecimal.valueOf(monthCnt + 1));
                paramPk.setControlLoad(BigDecimal.valueOf(loadCnt + controlLoadOffset));

                Map<String, String> monthlyScheduleMap = settingMonthScheduleList.get(monthCnt);

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

                registList.add(param);
            }
        }
    }

    /**
     * 最新データから、今回登録用のデータリストを作成
     *
     * @param registList
     * @param latestScheduleSetLogDataList
     * @param controlLoadOffset
     * @param smId
     */
    private void addScheduleSetLogDataFromLatest(List<TSmControlScheduleSetLog> registList, List<TSmControlScheduleSetLog> latestScheduleSetLogDataList, int controlLoadOffset, Long smId) {
        for (int loadCnt = 0; loadCnt < CONTROL_LOAD_OF_1TIME; loadCnt++) {
            int controlLoad = loadCnt + controlLoadOffset;

            //対象負荷の最新情報を取得
            List<TSmControlScheduleSetLog> latestDataList = latestScheduleSetLogDataList.stream()
                                                                                        .filter(data -> data.getId().getControlLoad().intValue() == controlLoad)
                                                                                        .collect(Collectors.toList());

            for (int monthCnt = 0; monthCnt < SmControlConstants.SCHEDULE_MONTH_SCHEDULE_LIST; monthCnt++) {
                TSmControlScheduleSetLog param = new TSmControlScheduleSetLog();
                TSmControlScheduleSetLogPK paramPk = new TSmControlScheduleSetLogPK();

                paramPk.setSmId(smId);
                paramPk.setTargetMonth(BigDecimal.valueOf(monthCnt + 1));
                paramPk.setControlLoad(BigDecimal.valueOf(controlLoad));

                TSmControlScheduleSetLog latestData = latestDataList.get(monthCnt);

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

                registList.add(param);
            }
        }
    }

    /**
     * 最新で登録されている制御負荷数を取得
     * →DBには対象月毎でレコード登録されているため、特定の対象月でフィルタしたリスト数で判断
     *
     * @param latestScheduleSetLogList
     * @return
     */
    private int getLatestControlLoadTypeNum(List<TSmControlScheduleSetLog> latestScheduleSetLogList) {
        List<TSmControlScheduleSetLog> latestControlLoadType = latestScheduleSetLogList.stream()
                                                                                       .filter(data -> data.getId().getTargetMonth().intValue() == 1)
                                                                                       .collect(Collectors.toList());
        return latestControlLoadType.size();
    }

}
