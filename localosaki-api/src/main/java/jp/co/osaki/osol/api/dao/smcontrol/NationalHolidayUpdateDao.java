package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlHolidayCalLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlHolidayLogServiceDaoImpl;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLog;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLogPK;
import jp.co.osaki.osol.entity.TSmControlHolidayLog;
import jp.co.osaki.osol.entity.TSmControlHolidayLogPK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200052Param;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * 祝日(設定) Dao クラス
 *
 * @author t_hayama
 *
 */
@Stateless
public class NationalHolidayUpdateDao extends BaseSmControlDao {

    // ServiceDAO呼出
    private final TSmControlHolidayLogServiceDaoImpl tSmControlHolidayLogServiceDaoImpl;
    private final TSmControlHolidayCalLogServiceDaoImpl tSmControlHolidayCalLogServiceDaoImpl;

    public NationalHolidayUpdateDao() {
        tSmControlHolidayLogServiceDaoImpl = new TSmControlHolidayLogServiceDaoImpl();
        tSmControlHolidayCalLogServiceDaoImpl = new TSmControlHolidayCalLogServiceDaoImpl();
    }

    public void updateBuildingDM(BuildingDmResult param, FvpCtrlMngResponse<A200052Param> res) throws Exception {

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

        //祝日情報を登録する
        updateHoliday(res, param.getUpdateUserId(), updateDate);

        return;
    }

    /**
     * 祝日情報を登録する
     * @param res
     * @param loginUserId
     * @param updateDate
     */
    private void updateHoliday(FvpCtrlMngResponse<A200052Param> res, Long loginUserId, Timestamp updateDate) {
        List<Map<String, String>> monthList = res.getParam().getMonthList();
        //小の月マップを作成(月:最大日数)
        Map<Integer,Integer> shortMonthMap = new HashMap<>();
        shortMonthMap.put(2, 29);
        shortMonthMap.put(4, 30);
        shortMonthMap.put(6, 30);
        shortMonthMap.put(9, 30);
        shortMonthMap.put(11, 30);

        Long smId = res.getSmId();
        String settingDateString = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(updateDate);
        Date settingDate = DateUtility.conversionDate("20" + settingDateString.substring(0, 6) + settingDateString.substring(6, 10), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

        //最新の登録済み祝日を取得する
        TSmControlHolidayLog latestLogParam = new TSmControlHolidayLog();
        TSmControlHolidayLogPK latestLogParamPk = new TSmControlHolidayLogPK();
        latestLogParamPk.setSmId(smId);
        latestLogParam.setId(latestLogParamPk);
        List<TSmControlHolidayLog> latestLogList = getResultList(tSmControlHolidayLogServiceDaoImpl,latestLogParam);
        TSmControlHolidayLog latestHoliday = latestLogList.stream().sorted(Comparator.comparing(o -> o.getId().getSmControlHolidayLogId(),Comparator.reverseOrder())).findFirst().orElse(null);

        List<String> latestHolidayList = new ArrayList<>();
        if(latestHoliday != null) {
            TSmControlHolidayCalLog latestCalParam = new TSmControlHolidayCalLog();
            TSmControlHolidayCalLogPK latestCalParamPk = new TSmControlHolidayCalLogPK();
            latestCalParamPk.setSmControlHolidayLogId(latestHoliday.getId().getSmControlHolidayLogId());
            latestCalParam.setId(latestCalParamPk);
            List<TSmControlHolidayCalLog> latestCalList = getResultList(tSmControlHolidayCalLogServiceDaoImpl,latestCalParam);

            //登録されている最新の祝日リストを作成する

            for(TSmControlHolidayCalLog holidayCal:latestCalList) {
                latestHolidayList.add(holidayCal.getId().getHolidayMmdd());
            }
        }

        List<String> newHolidayList = new ArrayList<>();
        List<TSmControlHolidayCalLog> addHolidayList = new ArrayList<>();
        //先頭から1月として祝日カレンダを順次処理する
        Integer month = 1;
        for(Map<String,String> holidayMap : monthList) {
            //ソート
            Object[] keyArray = holidayMap.keySet().stream().toArray();
            Arrays.sort(keyArray);
            //有効最大日付の算出
            Integer validDate = 31;
            if(shortMonthMap.get(month) != null) {
                validDate = shortMonthMap.get(month);
            }

            //登録済み日付保持用リスト
            List<String> addedList = new ArrayList<>();
            for(String key:holidayMap.keySet()) {
                //日付が00(無効)か、最大日数以上が設定されているか、同一月で登録済みの日付の場合は登録しない
                if(holidayMap.get(key).equals("00")
                        || validDate < Integer.parseInt(holidayMap.get(key))
                        || addedList.contains(holidayMap.get(key))
                        ) {
                    continue;
                }
                addedList.add(holidayMap.get(key));

                TSmControlHolidayCalLog smConHolCalLog = new TSmControlHolidayCalLog();
                TSmControlHolidayCalLogPK smConHolCalLogPk = new TSmControlHolidayCalLogPK();
                smConHolCalLogPk.setSmId(smId);
                smConHolCalLogPk.setHolidayMmdd(String.format("%02d", month) + holidayMap.get(key));
                newHolidayList.add(smConHolCalLogPk.getHolidayMmdd());
                smConHolCalLog.setId(smConHolCalLogPk);
                smConHolCalLog.setCreateDate(updateDate);
                smConHolCalLog.setCreateUserId(loginUserId);
                smConHolCalLog.setUpdateDate(updateDate);
                smConHolCalLog.setUpdateUserId(loginUserId);
                smConHolCalLog.setVersion(0);

                addHolidayList.add(smConHolCalLog);
            }

            month++;
        }

        //対象日付の内容に変更があるかどうか判定する
        boolean alterFlg = false;
        //対象の項目数比較
        if(latestHolidayList.size() != newHolidayList.size()) {
            alterFlg = true;
        }else {
          //内部に含まれる情報が一致するか判定する
            for(String mmdd:newHolidayList) {
                if(!latestHolidayList.contains(mmdd)) {
                    alterFlg = true;
                    break;
                }
            }
        }

        //変更があった場合のみデータを追加する
        if(alterFlg) {
            //祝日設定
            Long smControlHolidayLogId = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_HOLIDAY_LOG_ID.getVal());
            TSmControlHolidayLog smConHolLog = new TSmControlHolidayLog();
            TSmControlHolidayLogPK smConHolLogPk = new TSmControlHolidayLogPK();
            smConHolLogPk.setSmControlHolidayLogId(smControlHolidayLogId);
            smConHolLogPk.setSmId(smId);
            smConHolLog.setId(smConHolLogPk);
            smConHolLog.setSettingUpdateDatetime(new Timestamp(settingDate.getTime()));
            smConHolLog.setCreateDate(updateDate);
            smConHolLog.setCreateUserId(loginUserId);
            smConHolLog.setUpdateDate(updateDate);
            smConHolLog.setUpdateUserId(loginUserId);
            smConHolLog.setVersion(0);
            persist(tSmControlHolidayLogServiceDaoImpl,smConHolLog);

            for(TSmControlHolidayCalLog addHoliday : addHolidayList) {
                addHoliday.getId().setSmControlHolidayLogId(smControlHolidayLogId);
                persist(tSmControlHolidayCalLogServiceDaoImpl,addHoliday);
            }
        }
    }
}
