package jp.co.osaki.osol.api.dao.smcontrol;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.servicedao.entity.MSmControlLoadServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlHolidayCalLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TSmControlHolidayLogServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLog;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLogPK;
import jp.co.osaki.osol.entity.TSmControlHolidayLog;
import jp.co.osaki.osol.entity.TSmControlHolidayLogPK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200006Param;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;

/**
 *
 * デマンド(取得) Dao クラス.
 *
 * @author da_yamano
 *
 */
@Stateless
public class DemandSelectDao extends BaseSmControlDao {

    // ServiceDAO呼出
    private final BuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
    private final BuildingServiceDaoImpl buildingServiceDaoImpl;
    private final TSmControlHolidayLogServiceDaoImpl tSmControlHolidayLogServiceDaoImpl;
    private final TSmControlHolidayCalLogServiceDaoImpl tSmControlHolidayCalLogServiceDaoImpl;
    private final MSmControlLoadServiceDaoImpl mSmControlLoadServiceDaoImpl;

    public DemandSelectDao() {
        buildingDmServiceDaoImpl = new BuildingDmServiceDaoImpl();
        buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
        buildingServiceDaoImpl = new BuildingServiceDaoImpl();
        tSmControlHolidayLogServiceDaoImpl = new TSmControlHolidayLogServiceDaoImpl();
        tSmControlHolidayCalLogServiceDaoImpl = new TSmControlHolidayCalLogServiceDaoImpl();
        mSmControlLoadServiceDaoImpl = new MSmControlLoadServiceDaoImpl();
    }

    // 1トランザクションで複数テーブルに更新をかけるため、
    // DAO内に処理を記載
    public void updateBuildingDM(BuildingDmResult param, FvpCtrlMngResponse<A200006Param> res) throws Exception {

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

        //機器制御負荷を登録する
        updateSmControlLoad(res, param.getUpdateUserId(), updateDate);

        //祝日情報を登録する
        updateHoliday(res, param.getUpdateUserId(), updateDate);

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

        return;
    }

    /**
     * 機器制御負荷を登録する
     * @param res
     * @param loginUserId
     * @param updateDate
     */
    private void updateSmControlLoad(FvpCtrlMngResponse<A200006Param> res, Long loginUserId, Timestamp updateDate) {
        //最小負荷遮断時間リスト
        List<String> minLoadBlockTimeList = res.getParam().getMinLoadBlockTimeList();
        //遮断設定リスト
        List<Map<String, String>> _loadInfoList = res.getParam().get_loadInfoList();
        //負荷遮断容量リスト
        List<String> loadBlockCapacityList = res.getParam().getLoadBlockCapacityList();
        //機器ID
        Long smId = res.getSmId();
        //インデックス
        int controlLoadIndex = 0;
        for (String minLoadBlockTime : minLoadBlockTimeList) {
            //最小負荷遮断順位（遮断方法+遮断順位）
            String shutOffRank = null;
            //遮断方法
            String loadBlockMethod = _loadInfoList.get(controlLoadIndex).get("loadBlockMethod");
            //遮断順位
            String loadBlockRank = _loadInfoList.get(controlLoadIndex).get("loadBlockRank");
            //負荷遮断容量
            String shutOffCapacity = loadBlockCapacityList.get(controlLoadIndex);

            //各項目、数値変換できるかを確認する。数値以外の場合は、Nullを設定
            if (!StringUtility.isNumeric(minLoadBlockTime)) {
                minLoadBlockTime = null;
            }
            if (!StringUtility.isNumeric(loadBlockMethod)) {
                loadBlockMethod = null;
            }
            if (!StringUtility.isNumeric(loadBlockRank)) {
                loadBlockRank = null;
            }
            if (!StringUtility.isNumeric(shutOffCapacity)) {
                shutOffCapacity = null;
            }

            //最小負荷遮断順位を設定
            if (loadBlockMethod != null && loadBlockRank != null) {
                shutOffRank = loadBlockMethod.concat(loadBlockRank);
            }

            //インデックスをカウントアップ
            controlLoadIndex++;
            MSmControlLoad param = new MSmControlLoad();
            MSmControlLoadPK pkParam = new MSmControlLoadPK();
            pkParam.setSmId(smId);
            pkParam.setControlLoad(BigDecimal.valueOf(controlLoadIndex));
            param.setId(pkParam);
            MSmControlLoad updateData = find(mSmControlLoadServiceDaoImpl, param);
            if (updateData == null) {
                //対象データがない場合
                updateData = new MSmControlLoad();
                updateData.setId(pkParam);
                updateData.setControlLoadShutOffTime(minLoadBlockTime);
                updateData.setControlLoadShutOffRank(shutOffRank);
                updateData.setControlLoadShutOffCapacity(shutOffCapacity);
                updateData.setDelFlg(OsolConstants.FLG_OFF);
                updateData.setCreateUserId(loginUserId);
                updateData.setCreateDate(updateDate);
                updateData.setUpdateUserId(loginUserId);
                updateData.setUpdateDate(updateDate);
                persist(mSmControlLoadServiceDaoImpl, updateData);
            } else {
                //対象データがある場合
                updateData.setControlLoadShutOffTime(minLoadBlockTime);
                updateData.setControlLoadShutOffRank(shutOffRank);
                updateData.setControlLoadShutOffCapacity(shutOffCapacity);
                updateData.setDelFlg(OsolConstants.FLG_OFF);
                updateData.setUpdateUserId(loginUserId);
                updateData.setUpdateDate(updateDate);
                merge(mSmControlLoadServiceDaoImpl, updateData);
            }
        }
    }

    /**
     * 祝日情報を登録する
     * @param res
     * @param loginUserId
     * @param updateDate
     */
    private void updateHoliday(FvpCtrlMngResponse<A200006Param> res, Long loginUserId, Timestamp updateDate) {
        List<Map<String, String>> monthList = res.getParam().get_monthList();
        //小の月マップを作成(月:最大日数)
        Map<Integer,Integer> shortMonthMap = new HashMap<>();
        shortMonthMap.put(2, 29);
        shortMonthMap.put(4, 30);
        shortMonthMap.put(6, 30);
        shortMonthMap.put(9, 30);
        shortMonthMap.put(11, 30);

        Long smId = res.getSmId();
        String settingDateString = res.getParam().getSettingDate();
        Date settingDate = DateUtility.conversionDate("20" + settingDateString.substring(0, 6) + settingDateString.substring(7, 11), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

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
