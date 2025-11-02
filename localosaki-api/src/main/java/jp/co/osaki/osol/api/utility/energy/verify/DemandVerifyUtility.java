package jp.co.osaki.osol.api.utility.energy.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandMonthReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.EventControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TLoadControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.TempHumidControlLogVerifyResult;
import jp.co.osaki.osol.api.result.utility.CommonControlLoadResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlySmSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlMonthlyTimeTableResult;
import jp.co.osaki.osol.api.result.utility.CommonEventControlYearlyMonthSummaryResult;
import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeCurrentNextResult;
import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternNoResult;
import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.api.result.utility.CurrentYearReportResult;
import jp.co.osaki.osol.api.result.utility.SummaryRangeForYearResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadFlgSeparateListFlgResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleDutyLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.ems.DemandDataUtility;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.entity.MSmControlLoadVerify;
import jp.co.osaki.osol.entity.MSmControlLoadVerifyPK;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerifyPK;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.entity.MSmLineVerifyPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.DemandCalendarYearData;
import jp.co.osaki.osol.utility.StringUtility;

/**
 * 検証関連 Utilityクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtility {

    /**
     * 製品制御負荷情報取得のパラメータを取得する
     * @param productCd
     * @return
     */
    public static final ProductControlLoadListDetailResultData getProductControlLoadParam(String productCd) {
        ProductControlLoadListDetailResultData param = new ProductControlLoadListDetailResultData();
        param.setProductCd(productCd);
        return param;
    }

    /**
     * 機器系統検証情報を取得用のパラメータを取得する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param smId
     * @return
     */
    public static final MSmLineVerify getSmLineVerifyParam(String corpId, Long buildingId, Long lineGroupId,
            String lineNo,
            Long smId) {
        MSmLineVerify param = new MSmLineVerify();
        MSmLineVerifyPK pkParam = new MSmLineVerifyPK();
        pkParam.setCorpId(corpId);
        pkParam.setBuildingId(buildingId);
        pkParam.setLineGroupId(lineGroupId);
        pkParam.setLineNo(lineNo);
        pkParam.setSmId(smId);
        param.setId(pkParam);
        return param;
    }

    /**
     * 機器系統制御負荷検証情報を取得用のパラメータを取得する
     * @param corpId
     * @param buildingId
     * @param lineGroupId
     * @param lineNo
     * @param smId
     * @return
     */
    public static final MSmLineControlLoadVerify getSmLineControlLoadVerifyParam(String corpId, Long buildingId,
            Long lineGroupId, String lineNo, Long smId) {
        MSmLineControlLoadVerify param = new MSmLineControlLoadVerify();
        MSmLineControlLoadVerifyPK pkParam = new MSmLineControlLoadVerifyPK();
        pkParam.setCorpId(corpId);
        pkParam.setBuildingId(buildingId);
        pkParam.setLineGroupId(lineGroupId);
        pkParam.setLineNo(lineNo);
        pkParam.setSmId(smId);
        param.setId(pkParam);
        return param;
    }

    /**
     * 負荷制御履歴取得用のパラメータを設定する
     * @param smId
     * @param recordYmdHmFrom
     * @param recordYmdHmTo
     * @param recordEndYmdhm
     * @param orderFlg
     * @return
     */
    public static final TLoadControlLogResult getLoadControlLogResultParam(Long smId, String recordYmdHmFrom,
            String recordYmdHmTo, String recordEndYmdhm, Integer orderFlg) {
        TLoadControlLogResult param = new TLoadControlLogResult();
        param.setSmId(smId);
        param.setRecordYmdhmFrom(recordYmdHmFrom);
        param.setRecordYmdhmTo(recordYmdHmTo);
        param.setRecordEndYmdhm(recordEndYmdhm);
        param.setOrderFlg(orderFlg);
        return param;
    }

    /**
     * 機器制御負荷検証取得用のパラメータを設定する
     * @param smId
     * @return
     */
    public static final MSmControlLoadVerify getSmControlLoadVerifyParam(Long smId) {
        MSmControlLoadVerify param = new MSmControlLoadVerify();
        MSmControlLoadVerifyPK pkParam = new MSmControlLoadVerifyPK();
        pkParam.setSmId(smId);
        param.setId(pkParam);
        return param;
    }

    /**
     * 機器制御スケジュール履歴取得用のパラメータを設定する
     * @param smControlScheduleLogId
     * @param smId
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @param settingUpdateDateTimeOutRange
     * @return
     */
    public static final SmControlScheduleLogListDetailResultData getSmControlScheduleLogListParam(
            Long smControlScheduleLogId,
            Long smId, Date settingUpdateDateTimeFrom, Date settingUpdateDateTimeTo,
            Date settingUpdateDateTimeOutRange) {
        SmControlScheduleLogListDetailResultData param = new SmControlScheduleLogListDetailResultData();
        param.setSmControlScheduleLogId(smControlScheduleLogId);
        param.setSmId(smId);
        param.setSettingUpdateDateTimeFrom(settingUpdateDateTimeFrom);
        param.setSettingUpdateDateTimeTo(settingUpdateDateTimeTo);
        param.setSettingUpdateDateTimeOutRange(settingUpdateDateTimeOutRange);
        return param;
    }

    /**
     * 機器制御スケジュール設定履歴取得用のパラメータを設定する
     * @param smControlScheduleLogId
     * @param smId
     * @param controlLoadFrom
     * @param controlLoadTo
     * @param targetMonthFrom
     * @param targetMonthTo
     * @return
     */
    public static final SmControlScheduleSetLogListDetailResultData getSmControlScheduleSetLogListParam(
            Long smControlScheduleLogId, Long smId, BigDecimal controlLoadFrom, BigDecimal controlLoadTo,
            BigDecimal targetMonthFrom, BigDecimal targetMonthTo) {
        SmControlScheduleSetLogListDetailResultData param = new SmControlScheduleSetLogListDetailResultData();
        param.setSmControlScheduleLogId(smControlScheduleLogId);
        param.setSmId(smId);
        param.setControlLoadFrom(controlLoadFrom);
        param.setControlLoadTo(controlLoadTo);
        param.setTargetMonthFrom(targetMonthFrom);
        param.setTargetMonthTo(targetMonthTo);
        return param;
    }

    /**
     * 機器制御スケジュール時間帯履歴取得用のパラメータを設定する
     * @param smControlScheduleLogId
     * @param smId
     * @param patternNoFrom
     * @param patternNoTo
     * @param timeSlotNoFrom
     * @param timeSlotNoTo
     * @return
     */
    public static final SmControlScheduleTimeLogListDetailResultData getSmControlScheduleTimeLogListParam(
            Long smControlScheduleLogId, Long smId, String patternNoFrom, String patternNoTo, Integer timeSlotNoFrom,
            Integer timeSlotNoTo) {
        SmControlScheduleTimeLogListDetailResultData param = new SmControlScheduleTimeLogListDetailResultData();
        param.setSmControlScheduleLogId(smControlScheduleLogId);
        param.setSmId(smId);
        param.setPatternNoFrom(patternNoFrom);
        param.setPatternNoTo(patternNoTo);
        param.setTimeSlotNoFrom(timeSlotNoFrom);
        param.setTimeSlotNoTo(timeSlotNoTo);
        return param;
    }

    /**
     * 機器制御スケジュールデューティ履歴取得用のパラメータを設定する
     * @param smControlScheduleLogId
     * @param smId
     * @param patternNoFrom
     * @param patternNoTo
     * @return
     */
    public static final SmControlScheduleDutyLogListDetailResultData getSmControlScheduleDutyLogListParam(
            Long smControlScheduleLogId, Long smId, String patternNoFrom, String patternNoTo) {
        SmControlScheduleDutyLogListDetailResultData param = new SmControlScheduleDutyLogListDetailResultData();
        param.setSmControlScheduleLogId(smControlScheduleLogId);
        param.setSmId(smId);
        param.setPatternNoFrom(patternNoFrom);
        param.setPatternNoTo(patternNoTo);
        return param;
    }

    /**
     * 機器制御祝日設定履歴取得用のパラメータを設定する
     * @param smControlHolidayLogId
     * @param smId
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @param settingUpdateDateTimeOutRange
     * @return
     */
    public static final SmControlHolidayLogListDetailResultData getSmControlHolidayLogListParam(
            Long smControlHolidayLogId,
            Long smId, Date settingUpdateDateTimeFrom, Date settingUpdateDateTimeTo,
            Date settingUpdateDateTimeOutRange) {
        SmControlHolidayLogListDetailResultData param = new SmControlHolidayLogListDetailResultData();
        param.setSmControlHolidayLogId(smControlHolidayLogId);
        param.setSmId(smId);
        param.setSettingUpdateDateTimeFrom(settingUpdateDateTimeFrom);
        param.setSettingUpdateDateTimeTo(settingUpdateDateTimeTo);
        param.setSettingUpdateDateTimeOutRange(settingUpdateDateTimeOutRange);
        return param;
    }

    /**
     * 機器制御祝日設定カレンダ履歴取得用のパラメータを設定する
     * @param smControlHolidayLogId
     * @param smId
     * @param holidayMmDdFrom
     * @param holidayMmDdTo
     * @return
     */
    public static final SmControlHolidayCalLogListDetailResultData getSmControlHolidayCalLogListParam(
            Long smControlHolidayLogId, Long smId, String holidayMmDdFrom, String holidayMmDdTo) {
        SmControlHolidayCalLogListDetailResultData param = new SmControlHolidayCalLogListDetailResultData();
        param.setSmControlHolidayLogId(smControlHolidayLogId);
        param.setSmId(smId);
        param.setHolidayMmDdFrom(holidayMmDdFrom);
        param.setHolidayMmDdTo(holidayMmDdTo);
        return param;
    }

    /**
     * スケジュール情報の開始を取得する
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final Timestamp getSettingUpdateDateTimeFrom(Date measurementDate, BigDecimal jigenNo) {
        //計測年月日と時限Noから取得した時間の1日前（日またぎを想定して）を返す
        return new Timestamp(
                DateUtility.plusDay(DateUtility.conversionDate(
                        DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                                .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false)),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM), -1).getTime());
    }

    /**
     * スケジュール情報の終了を取得する
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final Timestamp getSettingUpdateDateTimeTo(Date measurementDate, BigDecimal jigenNo) {
        Date tempSettingUpdateDateTo = DateUtility.conversionDate(
                DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                        .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false)),
                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        //30分加算する
        tempSettingUpdateDateTo = DateUtility.plusMinute(tempSettingUpdateDateTo, 30);
        return new Timestamp(tempSettingUpdateDateTo.getTime());
    }

    /**
     * 負荷制御ログ情報の開始を取得する
     *
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final String getRecordYmdHmFrom(Date measurementDate, BigDecimal jigenNo) {
        //計測年月日と時限Noから取得した時間を返す
        return DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false));
    }

    /**
     * 負荷制御ログ情報の終了を取得する
     *
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final String getRecordYmdHmTo(Date measurementDate, BigDecimal jigenNo) {
        Date ymdHmTo = DateUtility
                .conversionDate(
                        DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                                .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false)),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        //30分加算する
        ymdHmTo = DateUtility.plusMinute(ymdHmTo, 30);
        return DateUtility.changeDateFormat(ymdHmTo, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
    }

    /**
     * イベント制御ログ、温湿度制御ログ情報の開始を取得する（日報用）
     *
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final String getRecordYmdHmsFromForDaily(Date measurementDate, BigDecimal jigenNo) {
        //計測年月日と時限Noから取得した時間+00を返す
        return DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false)).concat("00");
    }

    /**
     * イベント制御ログ、温湿度制御ログ情報の開始を取得する（月報・年報用）
     *
     * @param measurementDate
     * @return
     */
    public static final String getRecordYmdHmsFromForMonthlyYearly(Date measurementDate) {
        //計測年月日+000000を返す
        return DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD).concat("000000");
    }

    /**
     * イベント制御ログ、温湿度制御ログ情報の終了を取得する（日報用）
     *
     * @param measurementDate
     * @param jigenNo
     * @return
     */
    public static final String getRecordYmdHmsToForDaily(Date measurementDate, BigDecimal jigenNo) {
        Date ymdHmTo = DateUtility
                .conversionDate(
                        DateUtility.changeDateFormat(measurementDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                                .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNo, false)),
                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        //30分加算する
        ymdHmTo = DateUtility.plusMinute(ymdHmTo, 30);
        return DateUtility.changeDateFormat(ymdHmTo, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS);
    }

    /**
     * イベント制御ログ、温湿度制御ログ情報の終了を取得する（月報・年報用）
     *
     * @param measurementDate
     * @return
     */
    public static final String getRecordYmdHmsToForMonthlyYearly(Date measurementDate) {
        //計測年月日の翌日+000000を返す
        return DateUtility.changeDateFormat(DateUtility.plusDay(measurementDate, 1), DateUtility.DATE_FORMAT_YYYYMMDD)
                .concat("000000");
    }

    /**
     * 制御負荷数に基づく制御状態の桁数チェック
     *
     * @param loadControlList
     * @param controlLoadCount
     * @return
     */
    public static final Boolean checkLoadControlList(List<TLoadControlLogResult> loadControlList,
            Integer controlLoadCount) {

        if (loadControlList == null || loadControlList.isEmpty() || controlLoadCount == null || controlLoadCount == 0) {
            //負荷制御ログ情報がない、または制御負荷が存在しない場合はチェック不要
            return true;
        }

        for (TLoadControlLogResult loadControl : loadControlList) {
            switch (controlLoadCount) {
            case 4:
            case 6:
                //空白を除いて2桁以外はエラー（この負荷数の場合、3桁目はスペースで来るので、念のため）
                return loadControl.getControlStatus().trim().length() == 2;
            case 12:
                //3桁以外はエラー
                return loadControl.getControlStatus().length() == 3;
            case 16:
                //4桁以外はエラー
                return loadControl.getControlStatus().length() == 4;
            case 36:
                //9桁以外はエラー
                return loadControl.getControlStatus().length() == 9;
            case 96:
                //24桁以外はエラー
                return loadControl.getControlStatus().length() == 24;
            default:
                return false;
            }
        }

        return false;
    }

    /**
     * 負荷制御ログの制御状態を制御負荷単位に分解し、LinkedHashSetに格納する
     *
     * @param loadControlList
     * @param controlLoadCount
     * @param recordYmdHmFrom
     * @return
     */
    public static final LinkedHashSet<CommonControlLoadResult> createControlLoadSet(
            List<TLoadControlLogResult> loadControlList, Integer controlLoadCount, String recordYmdHmFrom) {

        LinkedHashSet<CommonControlLoadResult> resultSet = new LinkedHashSet<>();
        //String beforeRecordYmdHm = null;
        StringBuilder allFree = new StringBuilder();

        if (loadControlList == null || loadControlList.isEmpty() || controlLoadCount == null || controlLoadCount == 0) {
            //負荷制御ログ情報がない、または制御負荷が存在しない場合はnullを返す
            return null;
        }

        //制御負荷数だけ、すべて開放のデータを作成する
        for (int i = 1; i <= controlLoadCount; i++) {
            allFree.append(ApiSimpleConstants.LOAD_CONTROL_FREE);
        }

        //1件目のデータを精査する
        if (DateUtility.conversionDate(loadControlList.get(0).getRecordYmdhm(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)
                .after(DateUtility.conversionDate(recordYmdHmFrom, DateUtility.DATE_FORMAT_YYYYMMDDHHMM))) {
            //1件目のデータが開始より後の場合、開始日のデータを作成する（すべて開放）
            resultSet.add(new CommonControlLoadResult(recordYmdHmFrom, allFree.toString()));
        }

        //負荷制御ログ情報を繰り返す
        for (TLoadControlLogResult loadControl : loadControlList) {
            //制御状態を負荷ごとに分割
            String loadStatus = createLoadStatus(loadControl, controlLoadCount);
            resultSet.add(new CommonControlLoadResult(loadControl.getRecordYmdhm(), loadStatus));
        }

        return resultSet;

    }

    /**
     * デマンド制御を制御負荷ごとに分解し、時限Noを付与する（G2以前用）
     *
     * @param controlLoadSet
     * @return
     */
    public static final LinkedHashSet<CommonDemandControlTimeTableResult> createDemandControlTimeTable(
            LinkedHashSet<CommonControlLoadResult> controlLoadSet) {
        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadSet == null || controlLoadSet.isEmpty()) {
            return null;
        }

        for (CommonControlLoadResult controlLoad : controlLoadSet) {
            //対象日時の時限番号を取得する
            BigDecimal jigenNo = getJigenNoForDemand(controlLoad.getRecordDate());
            //制御状態を制御負荷ごとに分割する
            List<String> controlStatusList = Arrays.asList(controlLoad.getControlStatus().split(""));
            for (int i = 1; i <= controlStatusList.size(); i++) {
                resultSet.add(new CommonDemandControlTimeTableResult(i,
                        DateUtility.conversionDate(controlLoad.getRecordDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                        jigenNo, controlStatusList.get(i - 1)));
            }

        }

        return resultSet;
    }

    /**
     * デマンド制御を制御負荷ごとに分解し、時限Noを付与する（Eα以降用）
     * @param controlLoadSet
     * @param demandControlFlgData
     * @return
     */
    public static final LinkedHashSet<CommonDemandControlTimeTableResult> createDemandControlTimeTable(
            LinkedHashSet<CommonControlLoadResult> controlLoadSet,ProductControlLoadFlgSeparateListFlgResultData demandControlFlgData){
        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadSet == null || controlLoadSet.isEmpty()) {
            return null;
        }

        for (CommonControlLoadResult controlLoad : controlLoadSet) {
            //対象日時の時限番号を取得する
            BigDecimal jigenNo = getJigenNoForDemand(controlLoad.getRecordDate());
            //制御状態を制御負荷ごとに分割する
            List<String> controlStatusList = Arrays.asList(controlLoad.getControlStatus().split(""));
            if(controlStatusList.size() != demandControlFlgData.getControlLoadCount()) {
                //制御負荷数に矛盾がある場合は次のレコードへ
                continue;
            }
            for (int i = 0; i < controlStatusList.size(); i++) {
                resultSet.add(new CommonDemandControlTimeTableResult(demandControlFlgData.getProductControlLoadList().get(i).getControlLoad().intValue(),
                        DateUtility.conversionDate(controlLoad.getRecordDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                        jigenNo, controlStatusList.get(i)));
            }

        }

        return resultSet;
    }


    /**
     * デマンド制御用　TimeTableサマリ情報を作成する（G2以前用）
     *
     * @param timeTableSet
     * @param smLineControlVerifyList
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param jigenNoFrom
     * @param jigenNoTo
     * @param controlLoadCount
     * @return
     */
    public static final List<CommonDemandControlSummaryResult> createDemandControlTimeTableSummary(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal jigenNoFrom, BigDecimal jigenNoTo,
            Integer controlLoadCount) {

        List<CommonDemandControlSummaryResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        BigDecimal currentJigenNo = jigenNoFrom;
        List<String> beforeControlStatusList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutDateList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutDateTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> beforeCutJigenNoList = new ArrayList<>(controlLoadCount);
        List<Date> beforeDateList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> beforeJigenNoList = new ArrayList<>(controlLoadCount);

        BigDecimal shutOffCapacity;

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (int i = 1; i <= controlLoadCount; i++) {
                resultList.add(new CommonDemandControlSummaryResult(i, currentDate, currentJigenNo, null, null, null));
            }
            if (currentDate.equals(measurementDateTo) && currentJigenNo.compareTo(jigenNoTo) == 0) {
                break;
            } else if (currentJigenNo.compareTo(new BigDecimal("48")) == 0) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutDateTimeList.add(null);
            beforeCutJigenNoList.add(null);
            beforeDateList.add(null);
            beforeJigenNoList.add(null);
        }

        //タイムテーブルセットをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            //日を算出する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
            if (timeTableDate.before(measurementDateFrom) || (timeTableDate.equals(measurementDateFrom)
                    && timeTable.getJigenNo().compareTo(jigenNoFrom) < 0)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合、遮断開始時間は集計開始にあわせて遮断情報を設定
                    beforeCutDateList.set(timeTable.getControlLoad() - 1, measurementDateFrom);
                    beforeCutDateTimeList
                            .set(timeTable.getControlLoad() - 1,
                                    DateUtility.conversionDate(
                                            DateUtility
                                                    .changeDateFormat(measurementDateFrom,
                                                            DateUtility.DATE_FORMAT_YYYYMMDD)
                                                    .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNoFrom, false)),
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
                    beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, jigenNoFrom);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を削除する
                    beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                    beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, null);
                    beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, null);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                }
            } else if (timeTableDate.after(measurementDateTo)
                    || (timeTableDate.equals(measurementDateTo) && timeTable.getJigenNo().compareTo(jigenNoTo) > 0)) {
                //集計期間より後の場合は何もしない
                continue;
            } else {
                //集計期間内の場合
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getJigenNo().compareTo(result.getJigenNo()) == 0
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //計測年月日、時限No、制御負荷が同じ場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //遮断で、その前が開放の場合、制御回数をカウントアップする
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(timeTable.getControlLoad() - 1, timeTableDate);
                            beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, timeTable.getMeasurementDate());
                            beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, timeTable.getJigenNo());
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //開放で、その前が遮断の場合、制御時間を算出する。
                            //前回の遮断情報
                            Date beforeCutDate = beforeCutDateList.get(timeTable.getControlLoad() - 1);
                            Date beforeCutDateTime = beforeCutDateTimeList.get(timeTable.getControlLoad() - 1);
                            BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(timeTable.getControlLoad() - 1);
                            //開放時間（分）
                            Integer freeMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                    .substring(2));
                            //遮断時間（分）
                            Integer cutMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                            if (timeTableDate.equals(beforeCutDate)
                                    && timeTable.getJigenNo().compareTo(beforeCutJigenNo) == 0) {
                                //同一時限で遮断→開放となった場合、対象時限内で制御時間を算出する
                                if (result.getControlTimeSummary() == null) {
                                    result.setControlTimeSummary(new BigDecimal(freeMinute - cutMinute));
                                } else {
                                    result.setControlTimeSummary(
                                            result.getControlTimeSummary().add(new BigDecimal(freeMinute - cutMinute)));
                                }
                            } else {
                                //異なる時限で遮断→開放となった場合、対象時限内とその前の時限の制御時間を算出する
                                //対象時限内
                                if (result.getJigenNo().remainder(new BigDecimal("2"))
                                        .compareTo(BigDecimal.ZERO) == 0) {
                                    //偶数時限の場合は30分からの差分
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(new BigDecimal(freeMinute - 30));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(new BigDecimal(freeMinute - 30)));
                                    }
                                } else if (result.getJigenNo().remainder(new BigDecimal("2"))
                                        .compareTo(BigDecimal.ONE) == 0) {
                                    //奇数時限の場合は取得した開放時間そのもの
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(new BigDecimal(freeMinute));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(new BigDecimal(freeMinute - 30)));
                                    }
                                }

                                //前の時限の制御時間を算出する
                                for (CommonDemandControlSummaryResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getJigenNo().compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //計測年月日、時限No、制御負荷が一致したら処理を終了する
                                        break;
                                    }

                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断時点の計測年月日、時限No、制御負荷が一致した場合、時限終了時間との差分
                                        if (beforeResult.getJigenNo().remainder(new BigDecimal("2"))
                                                .compareTo(BigDecimal.ZERO) == 0) {
                                            //偶数時限の場合、60分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(new BigDecimal(60 - cutMinute));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(new BigDecimal(60 - cutMinute)));
                                            }
                                        } else if (beforeResult.getJigenNo().remainder(new BigDecimal("2"))
                                                .compareTo(BigDecimal.ONE) == 0) {
                                            //奇数時限の場合、30分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(new BigDecimal(30 - cutMinute));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(new BigDecimal(30 - cutMinute)));
                                            }
                                        }
                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            || (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                                    && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) < 0)) {
                                        //遮断時限の計測年月日、時限Noを超えた場合
                                        if (timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                            //制御負荷が同じ場合、30分を設定する
                                            beforeResult.setControlTimeSummary(new BigDecimal("30"));
                                        }
                                    }
                                }
                            }
                            //遮断情報を解除
                            beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                            beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, null);
                            beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, null);
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());

                        }
                        break;
                    }
                }
                beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                beforeDateList.set(timeTable.getControlLoad() - 1, timeTableDate);
                beforeJigenNoList.set(timeTable.getControlLoad() - 1, timeTable.getJigenNo());
                beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
            }

        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < controlLoadCount; i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終時限までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutDateTime = beforeCutDateTimeList.get(i);
                BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(i);
                //遮断時間（分）
                Integer cutMinute = Integer.parseInt(
                        DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (beforeCutDate.equals(result.getMeasurementDate())
                            && beforeCutJigenNo.compareTo(result.getJigenNo()) == 0
                            && i + 1 == result.getControlLoad()) {
                        //時限終了時間との差分
                        if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0) {
                            //偶数時限の場合、60分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(new BigDecimal(60 - cutMinute));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(new BigDecimal(60 - cutMinute)));
                            }
                        } else if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ONE) == 0) {
                            //奇数時限の場合、30分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(new BigDecimal(30 - cutMinute));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(new BigDecimal(30 - cutMinute)));
                            }
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i + 1 == result.getControlLoad()) {
                        //30分を加算する
                        result.setControlTimeSummary(new BigDecimal("30"));
                    }
                }
            }
        }

        //削減値を算出する
        for (CommonDemandControlSummaryResult result : resultList) {
            if (result.getControlTimeSummary() != null) {
                //制御時間が設定されている場合のみ、削減値を計算する
                shutOffCapacity = BigDecimal.ZERO;
                if (smLineControlVerifyList != null && !smLineControlVerifyList.isEmpty()) {
                    for (MSmLineControlLoadVerify smLineControlLoadVerify : smLineControlVerifyList) {
                        if (result.getControlLoad()
                                .equals(smLineControlLoadVerify.getId().getControlLoad().intValue())) {
                            shutOffCapacity = smLineControlLoadVerify.getDmLoadShutOffCapacity();
                        }
                    }
                }
                if (shutOffCapacity == null) {
                    result.setReductionValue(BigDecimal.ZERO);
                } else {
                    result.setReductionValue(result.getControlTimeSummary().multiply(shutOffCapacity)
                            .divide(new BigDecimal("30"), 10, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        return resultList;
    }

    /**
     * デマンド制御用　TimeTableサマリ情報を作成する（Eα以降用）
     *
     * @param timeTableSet
     * @param smLineControlVerifyList
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param jigenNoFrom
     * @param jigenNoTo
     * @param controlLoadCount
     * @return
     */
    public static final List<CommonDemandControlSummaryResult> createDemandControlTimeTableSummary(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal jigenNoFrom, BigDecimal jigenNoTo,
            ProductControlLoadFlgSeparateListFlgResultData demandControlFlgData) {

        List<CommonDemandControlSummaryResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        BigDecimal currentJigenNo = jigenNoFrom;
        List<String> beforeControlStatusList = new ArrayList<>(demandControlFlgData.getControlLoadCount());
        List<Date> beforeCutDateList = new ArrayList<>(demandControlFlgData.getControlLoadCount());
        List<Date> beforeCutDateTimeList = new ArrayList<>(demandControlFlgData.getControlLoadCount());
        List<BigDecimal> beforeCutJigenNoList = new ArrayList<>(demandControlFlgData.getControlLoadCount());
        List<Date> beforeDateList = new ArrayList<>(demandControlFlgData.getControlLoadCount());
        List<BigDecimal> beforeJigenNoList = new ArrayList<>(demandControlFlgData.getControlLoadCount());

        BigDecimal shutOffCapacity;

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (ProductControlLoadListDetailResultData productControlLoadData : demandControlFlgData.getProductControlLoadList()) {
                resultList.add(new CommonDemandControlSummaryResult(productControlLoadData.getControlLoad().intValue(), currentDate, currentJigenNo, null, null, null));
            }
            if (currentDate.equals(measurementDateTo) && currentJigenNo.compareTo(jigenNoTo) == 0) {
                break;
            } else if (currentJigenNo.compareTo(new BigDecimal("48")) == 0) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < demandControlFlgData.getControlLoadCount(); i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutDateTimeList.add(null);
            beforeCutJigenNoList.add(null);
            beforeDateList.add(null);
            beforeJigenNoList.add(null);
        }

        //タイムテーブルセットをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            //timeTableのcontrolLoadからListのIndexを算出する
            Integer index = getListIndex(timeTable.getControlLoad(), demandControlFlgData.getProductControlLoadList());
            if(index == null) {
                continue;
            }
            //日を算出する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
            if (timeTableDate.before(measurementDateFrom) || (timeTableDate.equals(measurementDateFrom)
                    && timeTable.getJigenNo().compareTo(jigenNoFrom) < 0)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合、遮断開始時間は集計開始にあわせて遮断情報を設定
                    beforeCutDateList.set(index, measurementDateFrom);
                    beforeCutDateTimeList.set(index, DateUtility.conversionDate(DateUtility.changeDateFormat(measurementDateFrom,DateUtility.DATE_FORMAT_YYYYMMDD)
                                                    .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNoFrom, false)),DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
                    beforeCutJigenNoList.set(index, jigenNoFrom);
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を削除する
                    beforeCutDateList.set(index, null);
                    beforeCutDateTimeList.set(index, null);
                    beforeCutJigenNoList.set(index, null);
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                }
            } else if (timeTableDate.after(measurementDateTo)
                    || (timeTableDate.equals(measurementDateTo) && timeTable.getJigenNo().compareTo(jigenNoTo) > 0)) {
                //集計期間より後の場合は何もしない
                continue;
            } else {
                //集計期間内の場合
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getJigenNo().compareTo(result.getJigenNo()) == 0
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //計測年月日、時限No、制御負荷が同じ場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlStatusList.get(index))) {
                            //遮断で、その前が開放の場合、制御回数をカウントアップする
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(index, timeTableDate);
                            beforeCutDateTimeList.set(index, timeTable.getMeasurementDate());
                            beforeCutJigenNoList.set(index, timeTable.getJigenNo());
                            beforeControlStatusList.set(index, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(index))) {
                            //開放で、その前が遮断の場合、制御時間を算出する。
                            //前回の遮断情報
                            Date beforeCutDate = beforeCutDateList.get(index);
                            Date beforeCutDateTime = beforeCutDateTimeList.get(index);
                            BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(index);
                            //開放時間（分）
                            Integer freeMinute = Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM).substring(2));
                            //遮断時間（分）
                            Integer cutMinute = Integer.parseInt(DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                            if (timeTableDate.equals(beforeCutDate) && timeTable.getJigenNo().compareTo(beforeCutJigenNo) == 0) {
                                //同一時限で遮断→開放となった場合、対象時限内で制御時間を算出する
                                if (result.getControlTimeSummary() == null) {
                                    result.setControlTimeSummary(BigDecimal.valueOf(freeMinute - cutMinute));
                                } else {
                                    result.setControlTimeSummary(result.getControlTimeSummary().add(new BigDecimal(freeMinute - cutMinute)));
                                }
                            } else {
                                //異なる時限で遮断→開放となった場合、対象時限内とその前の時限の制御時間を算出する
                                //対象時限内
                                if (result.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ZERO) == 0) {
                                    //偶数時限の場合は30分からの差分
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeMinute - 30));
                                    } else {
                                        result.setControlTimeSummary(result.getControlTimeSummary().add(BigDecimal.valueOf(freeMinute - 30)));
                                    }
                                } else if (result.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ONE) == 0) {
                                    //奇数時限の場合は取得した開放時間そのもの
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeMinute));
                                    } else {
                                        result.setControlTimeSummary(result.getControlTimeSummary().add(BigDecimal.valueOf(freeMinute - 30)));
                                    }
                                }

                                //前の時限の制御時間を算出する
                                for (CommonDemandControlSummaryResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getJigenNo().compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //計測年月日、時限No、制御負荷が一致したら処理を終了する
                                        break;
                                    }

                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断時点の計測年月日、時限No、制御負荷が一致した場合、時限終了時間との差分
                                        if (beforeResult.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ZERO) == 0) {
                                            //偶数時限の場合、60分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(60 - cutMinute));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary().add(BigDecimal.valueOf(60 - cutMinute)));
                                            }
                                        } else if (beforeResult.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ONE) == 0) {
                                            //奇数時限の場合、30分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(30 - cutMinute));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary().add(BigDecimal.valueOf(30 - cutMinute)));
                                            }
                                        }
                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            || (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                                    && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) < 0)) {
                                        //遮断時限の計測年月日、時限Noを超えた場合
                                        if (timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                            //制御負荷が同じ場合、30分を設定する
                                            beforeResult.setControlTimeSummary(BigDecimal.valueOf(30));
                                        }
                                    }
                                }
                            }
                            //遮断情報を解除
                            beforeCutDateList.set(index, null);
                            beforeCutDateTimeList.set(index, null);
                            beforeCutJigenNoList.set(index, null);
                            beforeControlStatusList.set(index, timeTable.getControlStatus());

                        }
                        break;
                    }
                }
                beforeControlStatusList.set(index, timeTable.getControlStatus());
                beforeDateList.set(index, timeTableDate);
                beforeJigenNoList.set(index, timeTable.getJigenNo());
                beforeControlStatusList.set(index, timeTable.getControlStatus());
            }

        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < demandControlFlgData.getControlLoadCount(); i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終時限までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutDateTime = beforeCutDateTimeList.get(i);
                BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(i);
                //遮断時間（分）
                Integer cutMinute = Integer.parseInt(DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                for (CommonDemandControlSummaryResult result : resultList) {
                    //resultからindexを取得する
                    Integer index = getListIndex(result.getControlLoad(), demandControlFlgData.getProductControlLoadList());
                    if(index == null) {
                        continue;
                    }
                    if (beforeCutDate.equals(result.getMeasurementDate()) && beforeCutJigenNo.compareTo(result.getJigenNo()) == 0 && i  == index) {
                        //時限終了時間との差分
                        if (result.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ZERO) == 0) {
                            //偶数時限の場合、60分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(60 - cutMinute));
                            } else {
                                result.setControlTimeSummary(result.getControlTimeSummary().add(BigDecimal.valueOf(60 - cutMinute)));
                            }
                        } else if (result.getJigenNo().remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ONE) == 0) {
                            //奇数時限の場合、30分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(30 - cutMinute));
                            } else {
                                result.setControlTimeSummary(result.getControlTimeSummary().add(BigDecimal.valueOf(30 - cutMinute)));
                            }
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i  == index) {
                        //30分を加算する
                        result.setControlTimeSummary(BigDecimal.valueOf(30));
                    }
                }
            }
        }

        //削減値を算出する
        for (CommonDemandControlSummaryResult result : resultList) {
            if (result.getControlTimeSummary() != null) {
                //制御時間が設定されている場合のみ、削減値を計算する
                shutOffCapacity = BigDecimal.ZERO;
                if (smLineControlVerifyList != null && !smLineControlVerifyList.isEmpty()) {
                    for (MSmLineControlLoadVerify smLineControlLoadVerify : smLineControlVerifyList) {
                        if (result.getControlLoad().equals(smLineControlLoadVerify.getId().getControlLoad().intValue())) {
                            shutOffCapacity = smLineControlLoadVerify.getDmLoadShutOffCapacity();
                        }
                    }
                }
                if (shutOffCapacity == null) {
                    result.setReductionValue(BigDecimal.ZERO);
                } else {
                    result.setReductionValue(result.getControlTimeSummary().multiply(shutOffCapacity)
                            .divide(BigDecimal.valueOf(30), 10, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        return resultList;
    }

    /**
     * 温湿度・イベント制御用　TimeTableサマリ情報を作成する（日報・G2以前用）
     *
     * @param timeTableSet
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param jigenNoFrom
     * @param jigenNoTo
     * @param controlLoadCount
     * @return
     */
    public static final List<CommonDemandControlSummaryResult> createEventControlTimeTableSummaryDaily(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal jigenNoFrom, BigDecimal jigenNoTo,
            Integer controlLoadCount) {

        List<CommonDemandControlSummaryResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        BigDecimal currentJigenNo = jigenNoFrom;
        List<String> beforeControlStatusList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutDateList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutDateTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> beforeCutJigenNoList = new ArrayList<>(controlLoadCount);
        List<Date> beforeDateList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> beforeJigenNoList = new ArrayList<>(controlLoadCount);
        List<Date> beforeDateMinuteList = new ArrayList<>(controlLoadCount);

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (int i = 1; i <= controlLoadCount; i++) {
                resultList.add(new CommonDemandControlSummaryResult(i, currentDate, currentJigenNo, null, null, null));
            }
            if (currentDate.equals(measurementDateTo) && currentJigenNo.compareTo(jigenNoTo) == 0) {
                break;
            } else if (currentJigenNo.compareTo(new BigDecimal("48")) == 0) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutDateTimeList.add(null);
            beforeCutJigenNoList.add(null);
            beforeDateList.add(null);
            beforeJigenNoList.add(null);
            beforeDateMinuteList.add(null);
        }

        //タイムテーブルセットをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            //日を算出する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);

            if (timeTableDate.before(measurementDateFrom) || (timeTableDate.equals(measurementDateFrom)
                    && timeTable.getJigenNo().compareTo(jigenNoFrom) < 0)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合、遮断開始時間は集計開始にあわせて遮断情報を設定
                    beforeCutDateList.set(timeTable.getControlLoad() - 1, measurementDateFrom);
                    beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, DateUtility.conversionDate(
                            DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD)
                                    .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNoFrom, false)).concat("00"),
                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                    beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, jigenNoFrom);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を削除する
                    beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                    beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, null);
                    beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, null);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                }
            } else if (timeTableDate.after(measurementDateTo)
                    || (timeTableDate.equals(measurementDateTo) && timeTable.getJigenNo().compareTo(jigenNoTo) > 0)) {
                //集計期間より後の場合は何もしない
                continue;
            } else {
                //集計期間内の場合
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getJigenNo().compareTo(result.getJigenNo()) == 0
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //計測年月日、時限No、制御負荷が同じ場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //遮断で前が開放の場合、制御回数のカウントアップ
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(timeTable.getControlLoad() - 1, timeTableDate);
                            beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, timeTable.getMeasurementDate());
                            beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, timeTable.getJigenNo());
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //開放で、その前が遮断の場合、制御時間を算出する。
                            //前回の遮断情報
                            Date beforeCutDate = beforeCutDateList.get(timeTable.getControlLoad() - 1);
                            Date beforeCutDateTime = beforeCutDateTimeList.get(timeTable.getControlLoad() - 1);
                            BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(timeTable.getControlLoad() - 1);
                            //開放時間（分）
                            Integer freeMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                    .substring(2));
                            //開放時間（秒）
                            Integer freeSecond = Integer.parseInt(DateUtility
                                    .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMMSS)
                                    .substring(4));
                            //開放時間（分・秒：秒単位で保持）
                            Integer freeTime = freeMinute * 60 + freeSecond;
                            //遮断時間（分）
                            Integer cutMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                            //遮断時間（秒）
                            Integer cutSecond = Integer.parseInt(DateUtility
                                    .changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4));
                            //遮断時間（分・秒：秒単位で保持）
                            Integer cutTime = cutMinute * 60 + cutSecond;
                            if (beforeCutDate.equals(timeTableDate)
                                    && beforeCutJigenNo.compareTo(timeTable.getJigenNo()) == 0) {
                                //遮断された時限と同じ場合
                                if (freeMinute.equals(cutMinute)) {
                                    //1分以内で制御が発生した場合、秒単位で制御時間を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeSecond)
                                                .subtract(BigDecimal.valueOf(cutSecond)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeSecond)
                                                        .subtract(BigDecimal.valueOf(cutSecond))));
                                    }
                                    //処理をした時刻を保持
                                    beforeDateMinuteList.set(timeTable.getControlLoad() - 1,
                                            DateUtility.conversionDate(
                                                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                                } else {
                                    //1分以上後で制御が発生した場合、秒単位で制御時間を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeTime)
                                                .subtract(BigDecimal.valueOf(cutTime)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeTime)
                                                        .subtract(BigDecimal.valueOf(cutTime))));
                                    }
                                    //処理をした時刻を保持
                                    beforeDateMinuteList.set(timeTable.getControlLoad() - 1,
                                            DateUtility.conversionDate(
                                                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                                }
                            } else {
                                //異なる時限で遮断→開放となった場合、対象時限内とその前の時限の制御時間を算出する
                                //対象時限内
                                if (result.getJigenNo().remainder(BigDecimal.valueOf(2))
                                        .compareTo(BigDecimal.ZERO) == 0) {
                                    //偶数時限の場合は30分からの差分を秒単位でなので、分を30分引いて、秒を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf((freeMinute - 30) * 60)
                                                .add(BigDecimal.valueOf(freeSecond)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf((freeMinute - 30) * 60)
                                                                .add(BigDecimal.valueOf(freeSecond))));
                                    }
                                } else if (result.getJigenNo().remainder(BigDecimal.valueOf(2))
                                        .compareTo(BigDecimal.ONE) == 0) {
                                    //奇数時限の場合は取得した開放時間そのもの
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeTime));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeTime)));
                                    }
                                }

                                //前の時限の制御時間を算出する
                                for (CommonDemandControlSummaryResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getJigenNo().compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //計測年月日、時限No、制御負荷が一致したら処理を終了する
                                        break;
                                    }

                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断時点の計測年月日、時限No、制御負荷が一致した場合、時限終了時間との差分
                                        if (beforeResult.getJigenNo().remainder(BigDecimal.valueOf(2))
                                                .compareTo(BigDecimal.ZERO) == 0) {
                                            //偶数時限の場合、60分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(3600 - cutTime));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf(3600 - cutTime)));
                                            }
                                        } else if (beforeResult.getJigenNo().remainder(new BigDecimal("2"))
                                                .compareTo(BigDecimal.ONE) == 0) {
                                            //奇数時限の場合、30分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(1800 - cutTime));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf(1800 - cutTime)));
                                            }
                                        }
                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            || (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                                    && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) < 0)) {
                                        //遮断時限の計測年月日、時限Noを超えた場合
                                        if (timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                            //制御負荷が同じ場合、30分（1800秒）を設定する
                                            beforeResult.setControlTimeSummary(BigDecimal.valueOf(1800));
                                        }
                                    }
                                }

                                //処理をした時刻を保持
                                beforeDateMinuteList.set(timeTable.getControlLoad() - 1,
                                        DateUtility.conversionDate(
                                                DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

                            }

                            //遮断情報を解除
                            beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                            beforeCutDateTimeList.set(timeTable.getControlLoad() - 1, null);
                            beforeCutJigenNoList.set(timeTable.getControlLoad() - 1, null);
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                        }
                        break;
                    }
                }
                beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                beforeDateList.set(timeTable.getControlLoad() - 1, timeTableDate);
                beforeJigenNoList.set(timeTable.getControlLoad() - 1, timeTable.getJigenNo());
                beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
            }

        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < controlLoadCount; i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終時限までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutDateTime = beforeCutDateTimeList.get(i);
                BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(i);
                //遮断時間（分）
                Integer cutMinute = Integer.parseInt(
                        DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                //遮断時間（秒）
                Integer cutSecond = Integer.parseInt(
                        DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4));
                //遮断時間（分・秒：秒単位で保持）
                Integer cutTime = cutMinute * 60 + cutSecond;
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (beforeCutDate.equals(result.getMeasurementDate())
                            && beforeCutJigenNo.compareTo(result.getJigenNo()) == 0
                            && i + 1 == result.getControlLoad()) {
                        //時限終了時間との差分
                        if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0) {
                            //偶数時限の場合、60分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(3600 - cutTime));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(BigDecimal.valueOf(3600 - cutTime)));
                            }
                        } else if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ONE) == 0) {
                            //奇数時限の場合、30分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(1800 - cutTime));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(BigDecimal.valueOf(1800 - cutTime)));
                            }
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i + 1 == result.getControlLoad()) {
                        //30分（1800秒）を加算する
                        result.setControlTimeSummary(BigDecimal.valueOf(1800));
                    }
                }
            }
        }

        //秒単位で保持している制御時間を分単位に加算する（秒の端数は切り上げ）
        for (CommonDemandControlSummaryResult result : resultList) {
            if (result.getControlTimeSummary() != null
                    && BigDecimal.ZERO.compareTo(result.getControlTimeSummary()) != 0) {
                if (BigDecimal.ZERO.compareTo(result.getControlTimeSummary().remainder(BigDecimal.valueOf(60))) == 0) {
                    //60で割り切れる場合、60で割った結果を加算
                    result.setControlTimeSummary(result.getControlTimeSummary().divide(BigDecimal.valueOf(60)));
                } else {
                    //60で割り切れない場合、60で割った結果を小数第一位で切り上げ
                    result.setControlTimeSummary(
                            result.getControlTimeSummary().divide(BigDecimal.valueOf(60), 0, RoundingMode.UP));
                }
            }
        }

        return resultList;

    }

    /**
     * 温湿度・イベント制御用　TimeTableサマリ情報を作成する（日報・Eα以降用）
     *
     * @param timeTableSet
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param jigenNoFrom
     * @param jigenNoTo
     * @param eventFlgData
     * @return
     */
    public static final List<CommonDemandControlSummaryResult> createEventControlTimeTableSummaryDaily(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            Date measurementDateFrom, Date measurementDateTo, BigDecimal jigenNoFrom, BigDecimal jigenNoTo,
            List<ProductControlLoadListDetailResultData> eventFlgData) {

        List<CommonDemandControlSummaryResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        BigDecimal currentJigenNo = jigenNoFrom;
        List<String> beforeControlStatusList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeCutDateList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeCutDateTimeList = new ArrayList<>(eventFlgData.size());
        List<BigDecimal> beforeCutJigenNoList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeDateList = new ArrayList<>(eventFlgData.size());
        List<BigDecimal> beforeJigenNoList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeDateMinuteList = new ArrayList<>(eventFlgData.size());

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (int i = 0; i < eventFlgData.size(); i++) {
                resultList.add(new CommonDemandControlSummaryResult(eventFlgData.get(i).getControlLoad().intValue(), currentDate, currentJigenNo, null, null, null));
            }
            if (currentDate.equals(measurementDateTo) && currentJigenNo.compareTo(jigenNoTo) == 0) {
                break;
            } else if (currentJigenNo.compareTo(new BigDecimal("48")) == 0) {
                currentDate = DateUtility.plusDay(currentDate, 1);
                currentJigenNo = BigDecimal.ONE;
            } else {
                currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < eventFlgData.size(); i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutDateTimeList.add(null);
            beforeCutJigenNoList.add(null);
            beforeDateList.add(null);
            beforeJigenNoList.add(null);
            beforeDateMinuteList.add(null);
        }

        //タイムテーブルセットをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            Integer index = getListIndex(timeTable.getControlLoad(), eventFlgData);
            if(index == null) {
                continue;
            }
            //日を算出する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);

            if (timeTableDate.before(measurementDateFrom) || (timeTableDate.equals(measurementDateFrom)
                    && timeTable.getJigenNo().compareTo(jigenNoFrom) < 0)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合、遮断開始時間は集計開始にあわせて遮断情報を設定
                    beforeCutDateList.set(index, measurementDateFrom);
                    beforeCutDateTimeList.set(index, DateUtility.conversionDate(
                            DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD)
                                    .concat(DemandDataUtility.changeJigenNoToHHMM(jigenNoFrom, false)).concat("00"),
                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                    beforeCutJigenNoList.set(index, jigenNoFrom);
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を削除する
                    beforeCutDateList.set(index, null);
                    beforeCutDateTimeList.set(index, null);
                    beforeCutJigenNoList.set(index, null);
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                }
            } else if (timeTableDate.after(measurementDateTo)
                    || (timeTableDate.equals(measurementDateTo) && timeTable.getJigenNo().compareTo(jigenNoTo) > 0)) {
                //集計期間より後の場合は何もしない
                continue;
            } else {
                //集計期間内の場合
                for (CommonDemandControlSummaryResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getJigenNo().compareTo(result.getJigenNo()) == 0
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //計測年月日、時限No、制御負荷が同じ場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE
                                        .equals(beforeControlStatusList.get(index))) {
                            //遮断で前が開放の場合、制御回数のカウントアップ
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(index, timeTableDate);
                            beforeCutDateTimeList.set(index, timeTable.getMeasurementDate());
                            beforeCutJigenNoList.set(index, timeTable.getJigenNo());
                            beforeControlStatusList.set(index, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT
                                        .equals(beforeControlStatusList.get(index))) {
                            //開放で、その前が遮断の場合、制御時間を算出する。
                            //前回の遮断情報
                            Date beforeCutDate = beforeCutDateList.get(index);
                            Date beforeCutDateTime = beforeCutDateTimeList.get(index);
                            BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(index);
                            //開放時間（分）
                            Integer freeMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                    .substring(2));
                            //開放時間（秒）
                            Integer freeSecond = Integer.parseInt(DateUtility
                                    .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMMSS)
                                    .substring(4));
                            //開放時間（分・秒：秒単位で保持）
                            Integer freeTime = freeMinute * 60 + freeSecond;
                            //遮断時間（分）
                            Integer cutMinute = Integer.parseInt(DateUtility
                                    .changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                            //遮断時間（秒）
                            Integer cutSecond = Integer.parseInt(DateUtility
                                    .changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4));
                            //遮断時間（分・秒：秒単位で保持）
                            Integer cutTime = cutMinute * 60 + cutSecond;
                            if (beforeCutDate.equals(timeTableDate)
                                    && beforeCutJigenNo.compareTo(timeTable.getJigenNo()) == 0) {
                                //遮断された時限と同じ場合
                                if (freeMinute.equals(cutMinute)) {
                                    //1分以内で制御が発生した場合、秒単位で制御時間を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeSecond)
                                                .subtract(BigDecimal.valueOf(cutSecond)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeSecond)
                                                        .subtract(BigDecimal.valueOf(cutSecond))));
                                    }
                                    //処理をした時刻を保持
                                    beforeDateMinuteList.set(index, DateUtility.conversionDate(
                                                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                                } else {
                                    //1分以上後で制御が発生した場合、秒単位で制御時間を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeTime)
                                                .subtract(BigDecimal.valueOf(cutTime)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeTime)
                                                        .subtract(BigDecimal.valueOf(cutTime))));
                                    }
                                    //処理をした時刻を保持
                                    beforeDateMinuteList.set(index, DateUtility.conversionDate(
                                                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                                }
                            } else {
                                //異なる時限で遮断→開放となった場合、対象時限内とその前の時限の制御時間を算出する
                                //対象時限内
                                if (result.getJigenNo().remainder(BigDecimal.valueOf(2))
                                        .compareTo(BigDecimal.ZERO) == 0) {
                                    //偶数時限の場合は30分からの差分を秒単位でなので、分を30分引いて、秒を加算
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf((freeMinute - 30) * 60)
                                                .add(BigDecimal.valueOf(freeSecond)));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf((freeMinute - 30) * 60)
                                                                .add(BigDecimal.valueOf(freeSecond))));
                                    }
                                } else if (result.getJigenNo().remainder(BigDecimal.valueOf(2))
                                        .compareTo(BigDecimal.ONE) == 0) {
                                    //奇数時限の場合は取得した開放時間そのもの
                                    if (result.getControlTimeSummary() == null) {
                                        result.setControlTimeSummary(BigDecimal.valueOf(freeTime));
                                    } else {
                                        result.setControlTimeSummary(
                                                result.getControlTimeSummary().add(BigDecimal.valueOf(freeTime)));
                                    }
                                }

                                //前の時限の制御時間を算出する
                                for (CommonDemandControlSummaryResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getJigenNo().compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //計測年月日、時限No、制御負荷が一致したら処理を終了する
                                        break;
                                    }

                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) == 0
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断時点の計測年月日、時限No、制御負荷が一致した場合、時限終了時間との差分
                                        if (beforeResult.getJigenNo().remainder(BigDecimal.valueOf(2))
                                                .compareTo(BigDecimal.ZERO) == 0) {
                                            //偶数時限の場合、60分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(3600 - cutTime));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf(3600 - cutTime)));
                                            }
                                        } else if (beforeResult.getJigenNo().remainder(new BigDecimal("2"))
                                                .compareTo(BigDecimal.ONE) == 0) {
                                            //奇数時限の場合、30分からの差分
                                            if (beforeResult.getControlTimeSummary() == null) {
                                                beforeResult.setControlTimeSummary(BigDecimal.valueOf(1800 - cutTime));
                                            } else {
                                                beforeResult.setControlTimeSummary(beforeResult.getControlTimeSummary()
                                                        .add(BigDecimal.valueOf(1800 - cutTime)));
                                            }
                                        }
                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            || (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                                    && beforeCutJigenNo.compareTo(beforeResult.getJigenNo()) < 0)) {
                                        //遮断時限の計測年月日、時限Noを超えた場合
                                        if (timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                            //制御負荷が同じ場合、30分（1800秒）を設定する
                                            beforeResult.setControlTimeSummary(BigDecimal.valueOf(1800));
                                        }
                                    }
                                }

                                //処理をした時刻を保持
                                beforeDateMinuteList.set(index, DateUtility.conversionDate(
                                                DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                                                DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

                            }

                            //遮断情報を解除
                            beforeCutDateList.set(index, null);
                            beforeCutDateTimeList.set(index, null);
                            beforeCutJigenNoList.set(index, null);
                            beforeControlStatusList.set(index, timeTable.getControlStatus());
                        }
                        break;
                    }
                }
                beforeControlStatusList.set(index, timeTable.getControlStatus());
                beforeDateList.set(index, timeTableDate);
                beforeJigenNoList.set(index, timeTable.getJigenNo());
                beforeControlStatusList.set(index, timeTable.getControlStatus());
            }

        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < eventFlgData.size(); i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終時限までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutDateTime = beforeCutDateTimeList.get(i);
                BigDecimal beforeCutJigenNo = beforeCutJigenNoList.get(i);
                //遮断時間（分）
                Integer cutMinute = Integer.parseInt(
                        DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMM).substring(2));
                //遮断時間（秒）
                Integer cutSecond = Integer.parseInt(
                        DateUtility.changeDateFormat(beforeCutDateTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4));
                //遮断時間（分・秒：秒単位で保持）
                Integer cutTime = cutMinute * 60 + cutSecond;
                for (CommonDemandControlSummaryResult result : resultList) {
                    Integer index = getListIndex(result.getControlLoad(), eventFlgData);
                    if(index == null) {
                        continue;
                    }
                    if (beforeCutDate.equals(result.getMeasurementDate())
                            && beforeCutJigenNo.compareTo(result.getJigenNo()) == 0
                            && i == index) {
                        //時限終了時間との差分
                        if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ZERO) == 0) {
                            //偶数時限の場合、60分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(3600 - cutTime));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(BigDecimal.valueOf(3600 - cutTime)));
                            }
                        } else if (result.getJigenNo().remainder(new BigDecimal("2")).compareTo(BigDecimal.ONE) == 0) {
                            //奇数時限の場合、30分からの差分
                            if (result.getControlTimeSummary() == null) {
                                result.setControlTimeSummary(BigDecimal.valueOf(1800 - cutTime));
                            } else {
                                result.setControlTimeSummary(
                                        result.getControlTimeSummary().add(BigDecimal.valueOf(1800 - cutTime)));
                            }
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i == index) {
                        //30分（1800秒）を加算する
                        result.setControlTimeSummary(BigDecimal.valueOf(1800));
                    }
                }
            }
        }

        //秒単位で保持している制御時間を分単位に加算する（秒の端数は切り上げ）
        for (CommonDemandControlSummaryResult result : resultList) {
            if (result.getControlTimeSummary() != null
                    && BigDecimal.ZERO.compareTo(result.getControlTimeSummary()) != 0) {
                if (BigDecimal.ZERO.compareTo(result.getControlTimeSummary().remainder(BigDecimal.valueOf(60))) == 0) {
                    //60で割り切れる場合、60で割った結果を加算
                    result.setControlTimeSummary(result.getControlTimeSummary().divide(BigDecimal.valueOf(60)));
                } else {
                    //60で割り切れない場合、60で割った結果を小数第一位で切り上げ
                    result.setControlTimeSummary(
                            result.getControlTimeSummary().divide(BigDecimal.valueOf(60), 0, RoundingMode.UP));
                }
            }
        }

        return resultList;

    }

    /**
     * 機器のサマリ情報を作成する
     *
     * @param timeTableSummaryList
     * @return
     */
    public static final List<CommonDemandControlSmSummaryResult> createDemandControlSmSummary(
            List<CommonDemandControlSummaryResult> timeTableSummaryList) {
        List<CommonDemandControlSmSummaryResult> resultList = new ArrayList<>();
        Date beforeMeasurementDate = null;
        BigDecimal beforeJigenNo = null;
        BigDecimal controlCount = null;
        BigDecimal reductionValue = null;

        if (timeTableSummaryList == null || timeTableSummaryList.isEmpty()) {
            return null;
        }

        //TimeTableサマリを計測年月日、時限No順にソートする
        timeTableSummaryList = timeTableSummaryList.stream()
                .sorted(Comparator
                        .comparing(CommonDemandControlSummaryResult::getMeasurementDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(CommonDemandControlSummaryResult::getJigenNo, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        for (CommonDemandControlSummaryResult timeTableSummary : timeTableSummaryList) {
            if (beforeJigenNo != null && beforeJigenNo.compareTo(timeTableSummary.getJigenNo()) != 0) {
                //時限番号が変更された場合、サマリを算出する
                CommonDemandControlSmSummaryResult result = new CommonDemandControlSmSummaryResult();
                result.setMeasurementDate(beforeMeasurementDate);
                result.setJigenNo(beforeJigenNo);
                result.setControlCount(controlCount);
                result.setReductionValue(reductionValue);
                resultList.add(result);
                controlCount = null;
                reductionValue = null;
            }

            if (timeTableSummary.getControlCount() != null) {
                if (controlCount == null) {
                    controlCount = timeTableSummary.getControlCount();
                } else {
                    controlCount = controlCount.add(timeTableSummary.getControlCount());
                }
            }

            if (timeTableSummary.getReductionValue() != null) {
                if (reductionValue == null) {
                    reductionValue = timeTableSummary.getReductionValue();
                } else {
                    reductionValue = reductionValue.add(timeTableSummary.getReductionValue());
                }
            }

            beforeMeasurementDate = timeTableSummary.getMeasurementDate();
            beforeJigenNo = timeTableSummary.getJigenNo();
        }

        //最終データの処理を行う。
        CommonDemandControlSmSummaryResult result = new CommonDemandControlSmSummaryResult();
        result.setMeasurementDate(beforeMeasurementDate);
        result.setJigenNo(beforeJigenNo);
        result.setControlCount(controlCount);
        result.setReductionValue(reductionValue);
        resultList.add(result);

        return resultList;
    }

    /**
     * 制御状態を負荷ごとに分割した値を返す（デマンド制御負荷）
     *
     * @param loadControl
     * @param controlLoadCount
     * @return
     */
    private static String createLoadStatus(TLoadControlLogResult loadControl, Integer controlLoadCount) {

        String binaryString = DemandDataUtility.createBinaryStringFromControlStatus(loadControl.getControlStatus());
        if (CheckUtility.isNullOrEmpty(binaryString)) {
            return null;
        }

        if (controlLoadCount == 4 || controlLoadCount == 6) {
            //4負荷、6負荷の場合、3文字目から読み、制御負荷数を超える文字は切り捨てる
            return binaryString.substring(2).substring(0, controlLoadCount);
        } else {
            //制御負荷数を超える文字は切り捨てる
            return binaryString.substring(0, controlLoadCount);
        }

    }

    /**
     * 時限Noを取得する（デマンド制御ログ）
     *
     * @param recordDate
     * @return
     */
    private static BigDecimal getJigenNoForDemand(String recordDate) {
        String hh = recordDate.substring(8, 10);
        String mm = recordDate.substring(10);

        BigDecimal jigenNo = DemandDataUtility.changeHHToJigenNo(hh);
        if (Integer.parseInt(mm) >= 00 && Integer.parseInt(mm) <= 29) {
            return jigenNo;
        } else {
            return jigenNo.add(BigDecimal.ONE);
        }
    }

    /**
     * 温湿度制御ログを、制御負荷単位に分解したLinkedHashSetを作成する
     *
     * @param tempHumidControlList
     * @param controlLoadCount
     * @param recordYmdHmsFrom
     * @return
     */
    public static final LinkedHashSet<CommonControlLoadResult> createTempHumidControlSet(
            List<TempHumidControlLogVerifyResult> tempHumidControlList, Integer controlLoadCount,
            String recordYmdHmsFrom) {
        LinkedHashSet<CommonControlLoadResult> resultSet = new LinkedHashSet<>();
        StringBuilder allFree = new StringBuilder();

        if (tempHumidControlList == null || tempHumidControlList.isEmpty() || controlLoadCount == null
                || controlLoadCount == 0) {
            //温湿度制御ログ情報がない、または制御負荷が存在しない場合はnullを返す
            return null;
        }

        //制御負荷数だけ、すべて開放のデータを作成する
        for (int i = 1; i <= controlLoadCount; i++) {
            allFree.append(ApiSimpleConstants.LOAD_CONTROL_FREE);
        }

        //1件目のデータを精査する
        if (DateUtility
                .conversionDate(tempHumidControlList.get(0).getRecordYmdhms(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
                .after(DateUtility.conversionDate(recordYmdHmsFrom, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS))) {
            //1件目のデータが開始より後の場合、開始日（YYYYMMDDHHMM）のデータを作成する（すべて開放）]
            resultSet.add(new CommonControlLoadResult(recordYmdHmsFrom, allFree.toString()));
        }

        //温湿度制御ログ情報を繰り返す
        for (TempHumidControlLogVerifyResult tempHumidControl : tempHumidControlList) {

            //制御状態を負荷ごとに分割
            String loadStatus = createTempHumidStatus(tempHumidControl, controlLoadCount);
            resultSet.add(new CommonControlLoadResult(tempHumidControl.getRecordYmdhms(), loadStatus));
        }

        return resultSet;
    }

    /**
     * イベント制御、温湿度制御を制御負荷ごとに分解し、時限Noを付与する（G2以前用）
     *
     * @param controlLoadSet
     * @return
     */
    public static final LinkedHashSet<CommonDemandControlTimeTableResult> createEventControlTimeTable(
            LinkedHashSet<CommonControlLoadResult> controlLoadSet) {

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadSet == null || controlLoadSet.isEmpty()) {
            return null;
        }

        for (CommonControlLoadResult controlLoad : controlLoadSet) {
            //対象日時の時限番号を取得する
            BigDecimal jigenNo = getJigenNoForEvent(controlLoad.getRecordDate());
            //制御状態を制御負荷ごとに分割する
            List<String> controlStatusList = Arrays.asList(controlLoad.getControlStatus().split(""));
            for (int i = 1; i <= controlStatusList.size(); i++) {
                resultSet.add(new CommonDemandControlTimeTableResult(i,
                        DateUtility.conversionDate(controlLoad.getRecordDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                        jigenNo, controlStatusList.get(i - 1)));
            }

        }

        return resultSet;

    }

    /**
     * イベント制御、温湿度制御を制御負荷ごとに分解し、時限Noを付与する（Eα以降用）
     *
     * @param controlLoadSet
     * @return
     */
    public static final LinkedHashSet<CommonDemandControlTimeTableResult> createEventControlTimeTable(
            LinkedHashSet<CommonControlLoadResult> controlLoadSet,
            List<ProductControlLoadListDetailResultData> eventFlgData) {

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadSet == null || controlLoadSet.isEmpty()) {
            return null;
        }

        for (CommonControlLoadResult controlLoad : controlLoadSet) {
            //対象日時の時限番号を取得する
            BigDecimal jigenNo = getJigenNoForEvent(controlLoad.getRecordDate());
            //制御状態を制御負荷ごとに分割する
            List<String> controlStatusList = Arrays.asList(controlLoad.getControlStatus().split(""));
            if(eventFlgData.size() != controlStatusList.size()) {
                continue;
            }
            for (int i = 0; i < controlStatusList.size(); i++) {
                resultSet.add(new CommonDemandControlTimeTableResult(eventFlgData.get(i).getControlLoad().intValue(),
                        DateUtility.conversionDate(controlLoad.getRecordDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                        jigenNo, controlStatusList.get(i)));
            }

        }

        return resultSet;

    }

    /**
     * 時限Noを取得する（温湿度制御ログ・イベント制御ログ）
     *
     * @param recordDate
     * @return
     */
    private static BigDecimal getJigenNoForEvent(String recordDate) {
        String hh = recordDate.substring(8, 10);
        String mm = recordDate.substring(10, 12);

        BigDecimal jigenNo = DemandDataUtility.changeHHToJigenNo(hh);
        if (Integer.parseInt(mm) >= 00 && Integer.parseInt(mm) <= 29) {
            return jigenNo;
        } else {
            return jigenNo.add(BigDecimal.ONE);
        }
    }

    /**
     * 制御状態を負荷ごとに分割した値を返す（温湿度制御負荷）
     *
     * @param tempHumidControl
     * @param controlLoadCount
     * @return
     */
    private static String createTempHumidStatus(TempHumidControlLogVerifyResult tempHumidControl,
            Integer controlLoadCount) {
        String binaryString = DemandDataUtility
                .createBinaryStringFromControlStatus(tempHumidControl.getPortOutStatus());
        if (CheckUtility.isNullOrEmpty(binaryString)) {
            return null;
        }

        //制御負荷数を超える文字は切り捨てる
        return binaryString.substring(0, controlLoadCount);
    }

    /**
     * 温湿度・イベント制御用　TimeTableサマリ情報を作成する（月報・年報用）（G2以前用）
     *
     * @param timeTableSet
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param controlLoadCount
     * @param monthlyReportList
     * @param smLineControlVerifyList
     * @param smLineVerifyList
     * @param smControlLoadList
     * @param scheduleSet
     * @return
     */
    public static final List<CommonEventControlMonthlyTimeTableResult> createEventControlTimeTableSummaryMonthlyYearly(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            Date measurementDateFrom, Date measurementDateTo, Integer controlLoadCount,
            List<CommonDemandMonthReportLineListResult> monthlyReportList,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmLineVerify> smLineVerifyList, List<MSmControlLoadVerify> smControlLoadList,
            LinkedHashSet<CommonScheduleResult> scheduleSet) {

        List<CommonEventControlMonthlyTimeTableResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        List<String> beforeControlStatusList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutDateList = new ArrayList<>(controlLoadCount);
        List<Date> beforeCutTimeList = new ArrayList<>(controlLoadCount);
        Map<BigDecimal, Map<Date, BigDecimal>> runningHoursMap = new HashMap<>();
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(controlLoadCount);
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> shutOffCapacityRate = new ArrayList<>(controlLoadCount);
        Map<BigDecimal, Map<Date, BigDecimal>> allControlUsedMap = new HashMap<>();
        String airVerifyType;

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (int i = 1; i <= controlLoadCount; i++) {
                resultList.add(new CommonEventControlMonthlyTimeTableResult(i, currentDate, null, null, null, null,
                        null, null, null, null, null, null, null));
            }
            if (currentDate.equals(measurementDateTo)) {
                break;
            } else {
                currentDate = DateUtility.plusDay(currentDate, 1);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutTimeList.add(null);
            if (i == 0) {
                runningHoursMap.put(BigDecimal.ONE, new HashMap<>());
                allControlUsedMap.put(BigDecimal.ONE, new HashMap<>());
            } else if (i == 9) {
                runningHoursMap.put(BigDecimal.TEN, new HashMap<>());
                allControlUsedMap.put(BigDecimal.TEN, new HashMap<>());
            } else {
                runningHoursMap.put(new BigDecimal(i + 1), new HashMap<>());
                allControlUsedMap.put(new BigDecimal(i + 1), new HashMap<>());
            }
            shutOffCapacityList.add(null);
        }

        //稼動時間を取得する（制御負荷別/日付別）
        for (CommonScheduleResult schedule : scheduleSet) {
            if (schedule.getStartTime() == null || schedule.getEndTime() == null
                    || schedule.getStartTime().equals(schedule.getEndTime())
                    || schedule.getStartTime().after(schedule.getEndTime())) {
                continue;
            }

            //開始日から日付を算出
            Date scheduleDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(schedule.getStartTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
            //開始、終了日をLongに変換
            Long startTime = schedule.getStartTime().getTime();
            Long endTime = schedule.getEndTime().getTime();
            BigDecimal diffTime = new BigDecimal(endTime).subtract(new BigDecimal(startTime));
            //Mapに詰める
            if (runningHoursMap.get(schedule.getControlLoad()).get(scheduleDate) == null) {
                runningHoursMap.get(schedule.getControlLoad()).put(scheduleDate, diffTime);
            } else {
                diffTime = diffTime.add(runningHoursMap.get(schedule.getControlLoad()).get(scheduleDate));
                runningHoursMap.get(schedule.getControlLoad()).put(scheduleDate, diffTime);
            }
        }

        //すべての稼働時間を時間単位（小数第11位を四捨五入）に変換し、24時間から減算する
        BigDecimal divideNum = new BigDecimal("1000").multiply(new BigDecimal("60")).multiply(new BigDecimal("60"));
        for (Map.Entry<BigDecimal, Map<Date, BigDecimal>> entry : runningHoursMap.entrySet()) {
            for (Map.Entry<Date, BigDecimal> childEntry : entry.getValue().entrySet()) {
                BigDecimal newRunnningHours = childEntry.getValue();
                newRunnningHours = newRunnningHours.divide(divideNum, 10, BigDecimal.ROUND_HALF_UP);
                newRunnningHours = new BigDecimal("24").subtract(newRunnningHours);
                childEntry.setValue(newRunnningHours);
            }
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList
                            .get(smLineControl.getId().getControlLoad().intValue() - 1);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary
                            .add(shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1));
                }
            }
        }

        //遮断容量比率を算出する
        for (BigDecimal shutOffCapacity : shutOffCapacityList) {
            if (shutOffCapacity == null || shutOffCapacitySummary == null
                    || BigDecimal.ZERO.compareTo(shutOffCapacitySummary) == 0) {
                shutOffCapacityRate.add(null);
            } else {
                shutOffCapacityRate.add(shutOffCapacity.multiply(new BigDecimal("100")).divide(shutOffCapacitySummary,
                        10, BigDecimal.ROUND_HALF_UP));
            }
        }

        //全負荷運転使用量を算出する（制御負荷別・日付別）
        for (Map.Entry<BigDecimal, Map<Date, BigDecimal>> entry : runningHoursMap.entrySet()) {
            BigDecimal shutOffCapacity = shutOffCapacityList.get(entry.getKey().intValue() - 1);
            BigDecimal tempRunnningHours = null;
            for (Map.Entry<Date, BigDecimal> childEntry : entry.getValue().entrySet()) {
                tempRunnningHours = childEntry.getValue();
                //Mapに詰める
                if (shutOffCapacity == null || tempRunnningHours == null) {
                    allControlUsedMap.get(entry.getKey()).put(childEntry.getKey(), null);
                } else {
                    allControlUsedMap.get(entry.getKey()).put(childEntry.getKey(),
                            shutOffCapacity.multiply(tempRunnningHours));
                }
            }
        }

        //タイムテーブルをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            //対象の日付を取得する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);

            if (timeTableDate.before(measurementDateFrom)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合
                    //遮断時刻は、集計開始時間にしておく
                    beforeCutDateList.set(timeTable.getControlLoad() - 1,
                            DateUtility.conversionDate(
                                    DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD),
                                    DateUtility.DATE_FORMAT_YYYYMMDD));
                    beforeCutTimeList
                            .set(timeTable.getControlLoad() - 1,
                                    DateUtility.conversionDate(
                                            DateUtility.changeDateFormat(measurementDateFrom,
                                                    DateUtility.DATE_FORMAT_YYYYMMDD).concat("000000"),
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を解除する
                    beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, null);
                    beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                }
            } else {
                //集計期間内の場合、結果をループする
                for (CommonEventControlMonthlyTimeTableResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //制御負荷と計測年月日が一致する場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //今が遮断で前が開放の場合、制御回数をカウントアップ
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(timeTable.getControlLoad() - 1,
                                    DateUtility.conversionDate(
                                            DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                    DateUtility.DATE_FORMAT_YYYYMMDD),
                                            DateUtility.DATE_FORMAT_YYYYMMDD));
                            beforeCutTimeList.set(timeTable.getControlLoad() - 1, timeTable.getMeasurementDate());
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT
                                        .equals(beforeControlStatusList.get(timeTable.getControlLoad() - 1))) {
                            //今が開放で前が遮断の場合、遮断時間を算出する
                            //遮断情報を取得
                            Date beforeCutDate = beforeCutDateList.get(timeTable.getControlLoad() - 1);
                            Date beforeCutTime = beforeCutTimeList.get(timeTable.getControlLoad() - 1);
                            if (timeTableDate.equals(beforeCutDate)) {
                                //遮断と同じ日付の場合、前遮断時からの差分を算出する
                                //遮断時刻（秒単位）
                                Integer cutSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(0, 2))
                                        * 60 * 60)
                                        + (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                .substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS)
                                                .substring(4)));
                                //開放時刻（秒単位）
                                Integer freeSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                        .substring(0, 2)) * 60 * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMM).substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                                if (result.getControlTimeSecond() == null) {
                                    result.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                                } else {
                                    result.setControlTimeSecond(
                                            result.getControlTimeSecond().add(new BigDecimal(freeSecond - cutSecond)));
                                }
                            } else {
                                //異なる日付の場合、まずは当日分の差分を算出する
                                //開放時刻（秒単位）
                                Integer freeSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                        .substring(0, 2)) * 60 * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMM).substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                                //取得した開放時刻の秒が遮断時間
                                if (result.getControlTimeSecond() == null) {
                                    result.setControlTimeSecond(new BigDecimal(freeSecond));
                                } else {
                                    result.setControlTimeSecond(
                                            result.getControlTimeSecond().add(new BigDecimal(freeSecond)));
                                }
                                //前日までの遮断時間を算出する
                                for (CommonEventControlMonthlyTimeTableResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //制御負荷と計測年月日が一致したらループを抜ける
                                        break;
                                    }
                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断日と同じ場合、24時との差分
                                        //遮断時刻（秒単位）
                                        Integer cutSecond = (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                .substring(0, 2)) * 60 * 60)
                                                + (Integer.parseInt(DateUtility
                                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                        .substring(2)) * 60)
                                                + (Integer.parseInt(DateUtility
                                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS)
                                                        .substring(4)));
                                        freeSecond = 86400;
                                        if (beforeResult.getControlTimeSecond() == null) {
                                            beforeResult.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                                        } else {
                                            beforeResult.setControlTimeSecond(beforeResult.getControlTimeSecond()
                                                    .add(new BigDecimal(freeSecond - cutSecond)));
                                        }

                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //開放日までの間は86400秒
                                        beforeResult.setControlTimeSecond(new BigDecimal("86400"));
                                    }
                                }
                            }
                            //遮断情報を解除
                            beforeCutDateList.set(timeTable.getControlLoad() - 1, null);
                            beforeCutTimeList.set(timeTable.getControlLoad() - 1, null);
                            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
                        }
                        break;
                    }
                }
            }

            beforeControlStatusList.set(timeTable.getControlLoad() - 1, timeTable.getControlStatus());
        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < controlLoadCount; i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終日までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutTime = beforeCutTimeList.get(i);
                for (CommonEventControlMonthlyTimeTableResult result : resultList) {
                    if (beforeCutDate.equals(result.getMeasurementDate()) && i + 1 == result.getControlLoad()) {
                        //24時との差分
                        //遮断時刻（秒単位）
                        Integer cutSecond = (Integer.parseInt(DateUtility
                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(0, 2)) * 60
                                * 60)
                                + (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(2))
                                        * 60)
                                + (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                        Integer freeSecond = 86400;
                        if (result.getControlTimeSecond() == null) {
                            result.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                        } else {
                            result.setControlTimeSecond(
                                    result.getControlTimeSecond().add(new BigDecimal(freeSecond - cutSecond)));
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i  == result.getControlLoad()) {
                        //86400秒を加算する
                        result.setControlTimeSecond(new BigDecimal("86400"));
                    }
                }
            }
        }

        //制御時間、制御回数を元にその他の項目を設定する
        for (CommonEventControlMonthlyTimeTableResult result : resultList) {
            //制御時間（時間）の算出
            if (result.getControlTimeSecond() == null
                    || result.getControlTimeSecond().compareTo(BigDecimal.ZERO) == 0) {
                result.setControlTimeHour(null);
            } else {
                result.setControlTimeHour(
                        result.getControlTimeSecond().divide(new BigDecimal("3600"), 10, BigDecimal.ROUND_HALF_UP));
            }

            //稼働時間の抽出
            BigDecimal workTime = runningHoursMap.get(new BigDecimal(result.getControlLoad()))
                    .get(result.getMeasurementDate());

            if (workTime == null) {
                //稼働時間が抽出できない場合は24時間
                workTime = new BigDecimal("24");
            }
            result.setWorkTime(workTime);

            //全負荷運転使用量の抽出
            BigDecimal allControlTime = allControlUsedMap.get(new BigDecimal(result.getControlLoad()))
                    .get(result.getMeasurementDate());

            if (allControlTime == null) {
                if (shutOffCapacityList.get(result.getControlLoad() - 1) != null) {
                    allControlTime = shutOffCapacityList.get(result.getControlLoad() - 1)
                            .multiply(new BigDecimal("24"));
                }
            }
            result.setAllControlTime(allControlTime);

            //制御率の算出（制御時間/稼動時間）
            if (result.getControlTimeHour() == null || workTime == null || BigDecimal.ZERO.compareTo(workTime) == 0) {
                result.setControlRate(null);
            } else {
                result.setControlRate(result.getControlTimeHour().multiply(new BigDecimal("100")).divide(workTime, 10,
                        BigDecimal.ROUND_HALF_UP));
                if (result.getControlRate().compareTo(new BigDecimal("100")) > 0) {
                    //100%を超える場合は100固定
                    result.setControlRate(new BigDecimal("100"));
                }
            }

            //負荷ごとの制御後使用量（系統別使用量*制御容量比率）、制御前使用量（制御後使用量/(1-制御率）)、削減量（制御前使用量 - 制御後使用量）
            for (CommonDemandMonthReportLineListResult monthReport : monthlyReportList) {
                if (result.getMeasurementDate().equals(monthReport.getMeasurementDate())) {
                    if (shutOffCapacityRate.get(result.getControlLoad() - 1) == null
                            || monthReport.getLineValueKwh() == null) {
                        result.setAfterUsedValue(null);
                        result.setBeforeUsedValue(null);
                        result.setReductionValue(null);
                    } else {
                        result.setAfterUsedValue(monthReport.getLineValueKwh()
                                .multiply(shutOffCapacityRate.get(result.getControlLoad() - 1))
                                .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP));
                        if (result.getControlRate() == null
                                || result.getControlRate().compareTo(new BigDecimal("100")) == 0) {
                            result.setBeforeUsedValue(null);
                            result.setReductionValue(null);
                        } else {
                            result.setBeforeUsedValue(result.getAfterUsedValue().divide(
                                    (BigDecimal.ONE.subtract(result.getControlRate().divide(new BigDecimal("100")))),
                                    10, BigDecimal.ROUND_HALF_UP));
                            result.setReductionValue(result.getBeforeUsedValue().subtract(result.getAfterUsedValue()));
                        }
                    }
                    break;
                }
            }

            //負荷率
            if (allControlTime == null || BigDecimal.ZERO.compareTo(allControlTime) == 0) {
                result.setAfterLoadFactorDetail(null);
                result.setBeforeLoadFactorDetail(null);
            } else {
                //制御後使用量負荷率（制御後使用量/全負荷運転使用量）
                if (result.getAfterUsedValue() == null) {
                    result.setAfterLoadFactorDetail(null);
                } else {
                    result.setAfterLoadFactorDetail(result.getAfterUsedValue().multiply(new BigDecimal("100"))
                            .divide(allControlTime, 10, BigDecimal.ROUND_HALF_UP));
                }
                //制御前使用量負荷率（制御前使用量/全負荷運転使用量）
                if (result.getBeforeUsedValue() == null) {
                    result.setBeforeLoadFactorDetail(null);
                } else {
                    result.setBeforeLoadFactorDetail(result.getBeforeUsedValue().multiply(new BigDecimal("100"))
                            .divide(allControlTime, 10, BigDecimal.ROUND_HALF_UP));
                }

            }
        }

        return resultList;

    }

    /**
     * イベント制御用　TimeTableサマリ情報を作成する（月報・年報用）（Eα以降用）
     *
     * @param timeTableSet
     * @param measurementDateFrom
     * @param measurementDateTo
     * @param monthlyReportList
     * @param smLineControlVerifyList
     * @param smLineVerifyList
     * @param smControlLoadList
     * @param scheduleSet
     * @param eventFlgData
     * @return
     */
    public static final List<CommonEventControlMonthlyTimeTableResult> createEventControlTimeTableSummaryMonthlyYearly(
            LinkedHashSet<CommonDemandControlTimeTableResult> timeTableSet,
            Date measurementDateFrom, Date measurementDateTo,
            List<CommonDemandMonthReportLineListResult> monthlyReportList,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmLineVerify> smLineVerifyList, List<MSmControlLoadVerify> smControlLoadList,
            LinkedHashSet<CommonScheduleResult> scheduleSet,
            List<ProductControlLoadListDetailResultData> eventFlgData) {

        List<CommonEventControlMonthlyTimeTableResult> resultList = new ArrayList<>();
        Date currentDate = measurementDateFrom;
        List<String> beforeControlStatusList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeCutDateList = new ArrayList<>(eventFlgData.size());
        List<Date> beforeCutTimeList = new ArrayList<>(eventFlgData.size());
        Map<BigDecimal, Map<Date, BigDecimal>> runningHoursMap = new HashMap<>();
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(eventFlgData.size());
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> shutOffCapacityRate = new ArrayList<>(eventFlgData.size());
        Map<BigDecimal, Map<Date, BigDecimal>> allControlUsedMap = new HashMap<>();
        String airVerifyType;

        if (timeTableSet == null || timeTableSet.isEmpty()) {
            //TimeTableが取得できていない場合は、処理を終了する
            return null;
        }

        //表示範囲内のListを作成する
        while (true) {
            for (int i = 0; i < eventFlgData.size(); i++) {
                resultList.add(new CommonEventControlMonthlyTimeTableResult(eventFlgData.get(i).getControlLoad().intValue(), currentDate,
                        null, null, null, null, null, null, null, null, null, null, null));
            }
            if (currentDate.equals(measurementDateTo)) {
                break;
            } else {
                currentDate = DateUtility.plusDay(currentDate, 1);
            }
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < eventFlgData.size(); i++) {
            beforeControlStatusList.add(ApiSimpleConstants.LOAD_CONTROL_FREE);
            beforeCutDateList.add(null);
            beforeCutTimeList.add(null);
            runningHoursMap.put(eventFlgData.get(i).getControlLoad(), new HashMap<>());
            allControlUsedMap.put(eventFlgData.get(i).getControlLoad(), new HashMap<>());
            shutOffCapacityList.add(null);
        }

        //稼動時間を取得する（制御負荷別/日付別）
        for (CommonScheduleResult schedule : scheduleSet) {
            if (schedule.getStartTime() == null || schedule.getEndTime() == null
                    || schedule.getStartTime().equals(schedule.getEndTime())
                    || schedule.getStartTime().after(schedule.getEndTime())) {
                continue;
            }

            //開始日から日付を算出
            Date scheduleDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(schedule.getStartTime(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);
            //開始、終了日をLongに変換
            Long startTime = schedule.getStartTime().getTime();
            Long endTime = schedule.getEndTime().getTime();
            BigDecimal diffTime = new BigDecimal(endTime).subtract(new BigDecimal(startTime));
            //Mapに詰める
            if(runningHoursMap.get(schedule.getControlLoad()) != null) {
                if (runningHoursMap.get(schedule.getControlLoad()).get(scheduleDate) == null) {
                    runningHoursMap.get(schedule.getControlLoad()).put(scheduleDate, diffTime);
                } else {
                    diffTime = diffTime.add(runningHoursMap.get(schedule.getControlLoad()).get(scheduleDate));
                    runningHoursMap.get(schedule.getControlLoad()).put(scheduleDate, diffTime);
                }
            }
        }

        //すべての稼働時間を時間単位（小数第11位を四捨五入）に変換し、24時間から減算する
        BigDecimal divideNum = new BigDecimal("1000").multiply(new BigDecimal("60")).multiply(new BigDecimal("60"));
        for (Map.Entry<BigDecimal, Map<Date, BigDecimal>> entry : runningHoursMap.entrySet()) {
            for (Map.Entry<Date, BigDecimal> childEntry : entry.getValue().entrySet()) {
                BigDecimal newRunnningHours = childEntry.getValue();
                newRunnningHours = newRunnningHours.divide(divideNum, 10, BigDecimal.ROUND_HALF_UP);
                newRunnningHours = new BigDecimal("24").subtract(newRunnningHours);
                childEntry.setValue(newRunnningHours);
            }
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            Integer index = getListIndex(smLineControl.getId().getControlLoad().intValue(), eventFlgData);
            if(index == null) {
                continue;
            }
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(index, smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(index, smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(index) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList.get(index);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary.add(shutOffCapacityList.get(index));
                }
            }
        }

        //遮断容量比率を算出する
        for (BigDecimal shutOffCapacity : shutOffCapacityList) {
            if (shutOffCapacity == null || shutOffCapacitySummary == null
                    || BigDecimal.ZERO.compareTo(shutOffCapacitySummary) == 0) {
                shutOffCapacityRate.add(null);
            } else {
                shutOffCapacityRate.add(shutOffCapacity.multiply(new BigDecimal("100")).divide(shutOffCapacitySummary,
                        10, BigDecimal.ROUND_HALF_UP));
            }
        }

        //全負荷運転使用量を算出する（制御負荷別・日付別）
        for (Map.Entry<BigDecimal, Map<Date, BigDecimal>> entry : runningHoursMap.entrySet()) {
            Integer index = getListIndex(entry.getKey().intValue(), eventFlgData);
            if(index == null) {
                continue;
            }
            BigDecimal shutOffCapacity = shutOffCapacityList.get(index);
            BigDecimal tempRunnningHours = null;
            for (Map.Entry<Date, BigDecimal> childEntry : entry.getValue().entrySet()) {
                tempRunnningHours = childEntry.getValue();
                //Mapに詰める
                if (shutOffCapacity == null || tempRunnningHours == null) {
                    allControlUsedMap.get(entry.getKey()).put(childEntry.getKey(), null);
                } else {
                    allControlUsedMap.get(entry.getKey()).put(childEntry.getKey(),
                            shutOffCapacity.multiply(tempRunnningHours));
                }
            }
        }

        //タイムテーブルをループする
        for (CommonDemandControlTimeTableResult timeTable : timeTableSet) {
            Integer index = getListIndex(timeTable.getControlLoad(), eventFlgData);
            if(index == null) {
                continue;
            }
            //対象の日付を取得する
            Date timeTableDate = DateUtility.conversionDate(
                    DateUtility.changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                    DateUtility.DATE_FORMAT_YYYYMMDD);

            if (timeTableDate.before(measurementDateFrom)) {
                //集計期間より前の場合
                if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())) {
                    //遮断の場合
                    //遮断時刻は、集計開始時間にしておく
                    beforeCutDateList.set(index, DateUtility.conversionDate(
                                    DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD),
                                    DateUtility.DATE_FORMAT_YYYYMMDD));
                    beforeCutTimeList.set(index, DateUtility.conversionDate(
                                            DateUtility.changeDateFormat(measurementDateFrom,
                                                    DateUtility.DATE_FORMAT_YYYYMMDD).concat("000000"),
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())) {
                    //開放の場合、遮断情報を解除する
                    beforeCutDateList.set(index, null);
                    beforeControlStatusList.set(index, null);
                    beforeControlStatusList.set(index, timeTable.getControlStatus());
                }
            } else {
                //集計期間内の場合、結果をループする
                for (CommonEventControlMonthlyTimeTableResult result : resultList) {
                    if (timeTableDate.equals(result.getMeasurementDate())
                            && timeTable.getControlLoad().equals(result.getControlLoad())) {
                        //制御負荷と計測年月日が一致する場合のみ処理を行う
                        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_FREE
                                        .equals(beforeControlStatusList.get(index))) {
                            //今が遮断で前が開放の場合、制御回数をカウントアップ
                            if (result.getControlCount() == null) {
                                result.setControlCount(BigDecimal.ONE);
                            } else {
                                result.setControlCount(result.getControlCount().add(BigDecimal.ONE));
                            }
                            //遮断情報を設定
                            beforeCutDateList.set(index, DateUtility.conversionDate(
                                            DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                    DateUtility.DATE_FORMAT_YYYYMMDD),
                                            DateUtility.DATE_FORMAT_YYYYMMDD));
                            beforeCutTimeList.set(index, timeTable.getMeasurementDate());
                            beforeControlStatusList.set(index, timeTable.getControlStatus());
                        } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(timeTable.getControlStatus())
                                && ApiSimpleConstants.LOAD_CONTROL_CUT
                                        .equals(beforeControlStatusList.get(index))) {
                            //今が開放で前が遮断の場合、遮断時間を算出する
                            //遮断情報を取得
                            Date beforeCutDate = beforeCutDateList.get(index);
                            Date beforeCutTime = beforeCutTimeList.get(index);
                            if (timeTableDate.equals(beforeCutDate)) {
                                //遮断と同じ日付の場合、前遮断時からの差分を算出する
                                //遮断時刻（秒単位）
                                Integer cutSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(0, 2))
                                        * 60 * 60)
                                        + (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                .substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS)
                                                .substring(4)));
                                //開放時刻（秒単位）
                                Integer freeSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                        .substring(0, 2)) * 60 * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMM).substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                                if (result.getControlTimeSecond() == null) {
                                    result.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                                } else {
                                    result.setControlTimeSecond(
                                            result.getControlTimeSecond().add(new BigDecimal(freeSecond - cutSecond)));
                                }
                            } else {
                                //異なる日付の場合、まずは当日分の差分を算出する
                                //開放時刻（秒単位）
                                Integer freeSecond = (Integer.parseInt(DateUtility
                                        .changeDateFormat(timeTable.getMeasurementDate(), DateUtility.DATE_FORMAT_HHMM)
                                        .substring(0, 2)) * 60 * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMM).substring(2)) * 60)
                                        + (Integer.parseInt(DateUtility.changeDateFormat(timeTable.getMeasurementDate(),
                                                DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                                //取得した開放時刻の秒が遮断時間
                                if (result.getControlTimeSecond() == null) {
                                    result.setControlTimeSecond(new BigDecimal(freeSecond));
                                } else {
                                    result.setControlTimeSecond(
                                            result.getControlTimeSecond().add(new BigDecimal(freeSecond)));
                                }
                                //前日までの遮断時間を算出する
                                for (CommonEventControlMonthlyTimeTableResult beforeResult : resultList) {
                                    if (timeTableDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //制御負荷と計測年月日が一致したらループを抜ける
                                        break;
                                    }
                                    if (beforeCutDate.equals(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //遮断日と同じ場合、24時との差分
                                        //遮断時刻（秒単位）
                                        Integer cutSecond = (Integer.parseInt(DateUtility
                                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                .substring(0, 2)) * 60 * 60)
                                                + (Integer.parseInt(DateUtility
                                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM)
                                                        .substring(2)) * 60)
                                                + (Integer.parseInt(DateUtility
                                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS)
                                                        .substring(4)));
                                        freeSecond = 86400;
                                        if (beforeResult.getControlTimeSecond() == null) {
                                            beforeResult.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                                        } else {
                                            beforeResult.setControlTimeSecond(beforeResult.getControlTimeSecond()
                                                    .add(new BigDecimal(freeSecond - cutSecond)));
                                        }

                                    } else if (beforeCutDate.before(beforeResult.getMeasurementDate())
                                            && timeTable.getControlLoad().equals(beforeResult.getControlLoad())) {
                                        //開放日までの間は86400秒
                                        beforeResult.setControlTimeSecond(new BigDecimal("86400"));
                                    }
                                }
                            }
                            //遮断情報を解除
                            beforeCutDateList.set(index, null);
                            beforeCutTimeList.set(index, null);
                            beforeControlStatusList.set(index, timeTable.getControlStatus());
                        }
                        break;
                    }
                }
            }

            beforeControlStatusList.set(index, timeTable.getControlStatus());
        }

        //タイムテーブル終了後、最終時限までの処理を行う
        for (int i = 0; i < eventFlgData.size(); i++) {
            Boolean endFlg = Boolean.FALSE;
            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlStatusList.get(i))) {
                //遮断の場合のみ、最終日までの遮断時間を設定する
                //前回の遮断情報
                Date beforeCutDate = beforeCutDateList.get(i);
                Date beforeCutTime = beforeCutTimeList.get(i);
                for (CommonEventControlMonthlyTimeTableResult result : resultList) {
                    Integer index = getListIndex(result.getControlLoad(), eventFlgData);
                    if(index == null) {
                        continue;
                    }
                    if (beforeCutDate.equals(result.getMeasurementDate()) && i == index) {
                        //24時との差分
                        //遮断時刻（秒単位）
                        Integer cutSecond = (Integer.parseInt(DateUtility
                                .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(0, 2)) * 60
                                * 60)
                                + (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMM).substring(2))
                                        * 60)
                                + (Integer.parseInt(DateUtility
                                        .changeDateFormat(beforeCutTime, DateUtility.DATE_FORMAT_HHMMSS).substring(4)));
                        Integer freeSecond = 86400;
                        if (result.getControlTimeSecond() == null) {
                            result.setControlTimeSecond(new BigDecimal(freeSecond - cutSecond));
                        } else {
                            result.setControlTimeSecond(
                                    result.getControlTimeSecond().add(new BigDecimal(freeSecond - cutSecond)));
                        }
                        endFlg = Boolean.TRUE;
                    } else if (endFlg && i == index) {
                        //86400秒を加算する
                        result.setControlTimeSecond(new BigDecimal("86400"));
                    }
                }
            }
        }

        //制御時間、制御回数を元にその他の項目を設定する
        for (CommonEventControlMonthlyTimeTableResult result : resultList) {
            Integer index = getListIndex(result.getControlLoad(), eventFlgData);
            //制御時間（時間）の算出
            if (result.getControlTimeSecond() == null
                    || result.getControlTimeSecond().compareTo(BigDecimal.ZERO) == 0) {
                result.setControlTimeHour(null);
            } else {
                result.setControlTimeHour(
                        result.getControlTimeSecond().divide(new BigDecimal("3600"), 10, BigDecimal.ROUND_HALF_UP));
            }

            //稼働時間の抽出
            BigDecimal workTime = runningHoursMap.get(new BigDecimal(result.getControlLoad()))
                    .get(result.getMeasurementDate());

            if (workTime == null) {
                //稼働時間が抽出できない場合は24時間
                workTime = new BigDecimal("24");
            }
            result.setWorkTime(workTime);

            //全負荷運転使用量の抽出
            BigDecimal allControlTime = allControlUsedMap.get(new BigDecimal(result.getControlLoad()))
                    .get(result.getMeasurementDate());

            if (allControlTime == null) {
                if (shutOffCapacityList.get(index) != null) {
                    allControlTime = shutOffCapacityList.get(index).multiply(new BigDecimal("24"));
                }
            }
            result.setAllControlTime(allControlTime);

            //制御率の算出（制御時間/稼動時間）
            if (result.getControlTimeHour() == null || workTime == null || BigDecimal.ZERO.compareTo(workTime) == 0) {
                result.setControlRate(null);
            } else {
                result.setControlRate(result.getControlTimeHour().multiply(new BigDecimal("100")).divide(workTime, 10, BigDecimal.ROUND_HALF_UP));
                if (result.getControlRate().compareTo(new BigDecimal("100")) > 0) {
                    //100%を超える場合は100固定
                    result.setControlRate(new BigDecimal("100"));
                }
            }

            //負荷ごとの制御後使用量（系統別使用量*制御容量比率）、制御前使用量（制御後使用量/(1-制御率）)、削減量（制御前使用量 - 制御後使用量）
            for (CommonDemandMonthReportLineListResult monthReport : monthlyReportList) {
                if (result.getMeasurementDate().equals(monthReport.getMeasurementDate())) {
                    if (shutOffCapacityRate.get(index) == null || monthReport.getLineValueKwh() == null) {
                        result.setAfterUsedValue(null);
                        result.setBeforeUsedValue(null);
                        result.setReductionValue(null);
                    } else {
                        result.setAfterUsedValue(monthReport.getLineValueKwh()
                                .multiply(shutOffCapacityRate.get(index)).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP));
                        if (result.getControlRate() == null || result.getControlRate().compareTo(new BigDecimal("100")) == 0) {
                            result.setBeforeUsedValue(null);
                            result.setReductionValue(null);
                        } else {
                            result.setBeforeUsedValue(result.getAfterUsedValue().divide(
                                    (BigDecimal.ONE.subtract(result.getControlRate().divide(new BigDecimal("100")))),
                                    10, BigDecimal.ROUND_HALF_UP));
                            result.setReductionValue(result.getBeforeUsedValue().subtract(result.getAfterUsedValue()));
                        }
                    }
                    break;
                }
            }

            //負荷率
            if (allControlTime == null || BigDecimal.ZERO.compareTo(allControlTime) == 0) {
                result.setAfterLoadFactorDetail(null);
                result.setBeforeLoadFactorDetail(null);
            } else {
                //制御後使用量負荷率（制御後使用量/全負荷運転使用量）
                if (result.getAfterUsedValue() == null) {
                    result.setAfterLoadFactorDetail(null);
                } else {
                    result.setAfterLoadFactorDetail(result.getAfterUsedValue().multiply(new BigDecimal("100"))
                            .divide(allControlTime, 10, BigDecimal.ROUND_HALF_UP));
                }
                //制御前使用量負荷率（制御前使用量/全負荷運転使用量）
                if (result.getBeforeUsedValue() == null) {
                    result.setBeforeLoadFactorDetail(null);
                } else {
                    result.setBeforeLoadFactorDetail(result.getBeforeUsedValue().multiply(new BigDecimal("100"))
                            .divide(allControlTime, 10, BigDecimal.ROUND_HALF_UP));
                }

            }
        }

        return resultList;

    }

    /**
     * イベント制御（月報）の機器毎のサマリを作成する（G2以前用）
     *
     * @param timeTableList
     * @param controlLoadCount
     * @param monthlyReportList
     * @param smLineControlVerifyList
     * @param smLineVerifyList
     * @param smControlLoadList
     * @param corpId
     * @param corpCalList
     * @param sumDate
     * @return
     */
    public static final List<CommonEventControlMonthlySmSummaryResult> createEventControlSmSummaryMonthly(
            List<CommonEventControlMonthlyTimeTableResult> timeTableList, Integer controlLoadCount,
            List<CommonDemandMonthReportLineListResult> monthlyReportList,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmLineVerify> smLineVerifyList, List<MSmControlLoadVerify> smControlLoadList, String corpId,
            List<DemandCalendarYearData> corpCalList, String sumDate) {

        List<CommonEventControlMonthlySmSummaryResult> resultList = new ArrayList<>();
        CommonEventControlMonthlySmSummaryResult result = new CommonEventControlMonthlySmSummaryResult();
        Date beforeDate = null;
        List<BigDecimal> workTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(controlLoadCount);
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> shutOffCapacityRate = new ArrayList<>(controlLoadCount);
        List<BigDecimal> allControlUsedList = new ArrayList<>(controlLoadCount);
        BigDecimal allControlUsedSummary = null;
        String airVerifyType;
        BigDecimal reductionRateThreshold;
        BigDecimal reductionCorrectionRate;
        BigDecimal reductionLowerRateMonth;
        BigDecimal reductionLowerAmountMonth;
        BigDecimal monthNo;

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            workTimeList.add(null);
            shutOffCapacityList.add(null);
            allControlUsedList.add(null);
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList
                            .get(smLineControl.getId().getControlLoad().intValue() - 1);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary
                            .add(shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1));
                }
            }
        }

        //遮断容量比率を算出する
        for (BigDecimal shutOffCapacity : shutOffCapacityList) {
            if (shutOffCapacity == null || shutOffCapacitySummary == null
                    || BigDecimal.ZERO.compareTo(shutOffCapacitySummary) == 0) {
                shutOffCapacityRate.add(null);
            } else {
                shutOffCapacityRate.add(shutOffCapacity.multiply(new BigDecimal("100")).divide(shutOffCapacitySummary,
                        10, BigDecimal.ROUND_HALF_UP));
            }
        }

        //タイムテーブルを計測年月日、制御負荷順にソートする
        if(timeTableList != null && !timeTableList.isEmpty()) {
            timeTableList = timeTableList.stream().sorted(Comparator
                    .comparing(CommonEventControlMonthlyTimeTableResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(CommonEventControlMonthlyTimeTableResult::getControlLoad, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

            for (CommonEventControlMonthlyTimeTableResult timeTable : timeTableList) {

                if (beforeDate == null) {
                    result = new CommonEventControlMonthlySmSummaryResult();
                    result.setMeasurementDate(timeTable.getMeasurementDate());
                } else if (!beforeDate.equals(timeTable.getMeasurementDate())) {
                    resultList.add(result);
                    result = new CommonEventControlMonthlySmSummaryResult();
                    result.setMeasurementDate(timeTable.getMeasurementDate());
                }

                //制御時間
                if (timeTable.getControlTimeHour() != null) {
                    if (result.getControlTime() == null) {
                        result.setControlTime(timeTable.getControlTimeHour());
                    } else {
                        result.setControlTime(result.getControlTime().add(timeTable.getControlTimeHour()));
                    }
                }

                //制御回数
                if (timeTable.getControlCount() != null) {
                    if (result.getControlCount() == null) {
                        result.setControlCount(timeTable.getControlCount());
                    } else {
                        result.setControlCount(result.getControlCount().add(timeTable.getControlCount()));
                    }
                }

                //制御前使用量
                if (timeTable.getBeforeUsedValue() != null) {
                    if (result.getBeforeControlUsedValue() == null) {
                        result.setBeforeControlUsedValue(timeTable.getBeforeUsedValue());
                    } else {
                        result.setBeforeControlUsedValue(
                                result.getBeforeControlUsedValue().add(timeTable.getBeforeUsedValue()));
                    }
                }
                // 制御前使用量がnullで、制御率100%の場合、制御後の使用量を設定
                // 制御率100%の場合は、制御前使用量がnullになる
                // 制御がない場合も、制御前使用量がnullになる
                else {
                    if (timeTable.getAfterUsedValue() != null) {
                        if (result.getBeforeControlUsedValue() == null) {
                            result.setBeforeControlUsedValue(timeTable.getAfterUsedValue());
                        } else {
                            result.setBeforeControlUsedValue(
                                    result.getBeforeControlUsedValue().add(timeTable.getAfterUsedValue()));
                        }
                    }
                }

                //削減量
                if (timeTable.getReductionValue() != null) {
                    if (result.getReductionValue() == null) {
                        result.setReductionValue(timeTable.getReductionValue());
                    } else {
                        result.setReductionValue(result.getReductionValue().add(timeTable.getReductionValue()));
                    }
                }

                beforeDate = timeTable.getMeasurementDate();
            }

            //最終データの処理
            resultList.add(result);
        }

        //制御後使用量の設定
        for (CommonDemandMonthReportLineListResult monthReport : monthlyReportList) {
            Boolean existEventData = Boolean.FALSE;
            if(resultList != null && !resultList.isEmpty()) {
                //制御ログがある場合、制御ログと一致する箇所があるか探す
                for (CommonEventControlMonthlySmSummaryResult summaryResult : resultList) {
                    if (summaryResult.getMeasurementDate().equals(monthReport.getMeasurementDate())) {
                        summaryResult.setAfterControlUsedValue(monthReport.getLineValueKwh());
                        existEventData = Boolean.TRUE;
                        break;
                    }
                }
            }

            if(!existEventData) {
                //制御ログがない場合、新たなサマリデータを作成する必要がある
                result = new CommonEventControlMonthlySmSummaryResult();
                result.setMeasurementDate(monthReport.getMeasurementDate());
                result.setAfterControlUsedValue(monthReport.getLineValueKwh());
                resultList.add(result);
            }
        }


        for (CommonEventControlMonthlySmSummaryResult summaryResult : resultList) {

            if (summaryResult.getAfterControlUsedValue() != null) {
                //制御後使用量が設定されている場合かつ各項目がNULLの場合、設定をやり直す
                //制御時間
                if (summaryResult.getControlTime() == null) {
                    summaryResult.setControlTime(BigDecimal.ZERO);
                }
                //制御回数
                if (summaryResult.getControlCount() == null) {
                    summaryResult.setControlCount(BigDecimal.ZERO);
                }
                //制御前使用量（制御後使用量）
                if (summaryResult.getBeforeControlUsedValue() == null) {
                    summaryResult.setBeforeControlUsedValue(summaryResult.getAfterControlUsedValue());
                }
                //削減量
                if (summaryResult.getReductionValue() == null) {
                    summaryResult.setReductionValue(BigDecimal.ZERO);
                }

            }

            allControlUsedSummary = null;
            //全負荷運転使用量（稼働時間が日付単位で違うのでここで算出するしかない）
            for (int i = 0; i < controlLoadCount; i++) {
                Boolean existEventData = Boolean.FALSE;
                //稼働時間の取得
                if(timeTableList != null && !timeTableList.isEmpty()) {
                    for (CommonEventControlMonthlyTimeTableResult timeTable : timeTableList) {
                        if (summaryResult.getMeasurementDate().equals(timeTable.getMeasurementDate())
                                && new BigDecimal(timeTable.getControlLoad())
                                        .compareTo(new BigDecimal(i).add(BigDecimal.ONE)) == 0) {
                            if (timeTable.getAllControlTime() != null) {
                                if (allControlUsedSummary == null) {
                                    allControlUsedSummary = timeTable.getAllControlTime();
                                } else {
                                    allControlUsedSummary = allControlUsedSummary.add(timeTable.getAllControlTime());
                                }
                            }
                            existEventData = Boolean.TRUE;
                            break;
                        }
                    }
                }

                if(!existEventData) {
                    //稼働時間が取得できない場合は24時間*遮断容量
                    if(shutOffCapacityList != null && shutOffCapacityList.get(i) != null) {
                        if(allControlUsedSummary == null) {
                            allControlUsedSummary = BigDecimal.valueOf(24).multiply(shutOffCapacityList.get(i));
                        }else {
                            allControlUsedSummary = allControlUsedSummary.add(BigDecimal.valueOf(24).multiply(shutOffCapacityList.get(i)));
                        }
                    }

                }

            }

            summaryResult.setAllControlTime(allControlUsedSummary);
            //制御後使用量負荷率（制御後使用量/全負荷運転使用量）
            if (summaryResult.getAfterControlUsedValue() == null || allControlUsedSummary == null
                    || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
                summaryResult.setAfterControlLoadFactor(null);
            } else {
                summaryResult.setAfterControlLoadFactor(summaryResult.getAfterControlUsedValue()
                        .multiply(new BigDecimal("100")).divide(allControlUsedSummary, 1, BigDecimal.ROUND_HALF_UP));
            }

            //制御前使用量負荷率（制御前使用量/全負荷運転使用量）
            if (summaryResult.getBeforeControlUsedValue() == null || allControlUsedSummary == null
                    || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
                summaryResult.setBeforeControlLoadFactor(null);
            } else {
                summaryResult.setBeforeControlLoadFactor(summaryResult.getBeforeControlUsedValue()
                        .multiply(new BigDecimal("100")).divide(allControlUsedSummary, 1, BigDecimal.ROUND_HALF_UP));
            }

            //削減率（1-(制御後使用量/制御前使用量））
            if (summaryResult.getAfterControlUsedValue() == null || summaryResult.getBeforeControlUsedValue() == null
                    || BigDecimal.ZERO.compareTo(summaryResult.getBeforeControlUsedValue()) == 0) {
                summaryResult.setReductionRate(null);
            } else {
                summaryResult.setReductionRate(new BigDecimal("100")
                        .subtract((summaryResult.getAfterControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(summaryResult.getBeforeControlUsedValue(), 1, BigDecimal.ROUND_HALF_UP))));
            }

            //補正削減率の各情報を取得する
            //対象日の月度（企業集計ベース）
            if (DemandEmsUtility.getCurrentYearReport(corpCalList, summaryResult.getMeasurementDate(),
                    sumDate) == null) {
                monthNo = null;
            } else {
                monthNo = DemandEmsUtility
                        .getCurrentYearReport(corpCalList, summaryResult.getMeasurementDate(), sumDate)
                        .getMonthNo();
            }

            //削減率閾値
            reductionRateThreshold = getReductionRateThreshold(smLineVerifyList);
            //削減補正率
            reductionCorrectionRate = getReductionCorrectionRate(smLineVerifyList);
            //削減下限率
            reductionLowerRateMonth = getReductionLowerRateMonth(smLineVerifyList, monthNo);
            //削減下限使用量
            reductionLowerAmountMonth = getReductionLowerAmountMonth(smLineVerifyList, monthNo);

            if (reductionRateThreshold == null && reductionCorrectionRate == null && reductionLowerRateMonth == null
                    && reductionLowerAmountMonth == null) {
                //補正削減率の各情報の設定がない場合
                summaryResult.setCorrectionReductionRate(null);
                //補正後制御前使用量（制御前使用量と同じ）
                summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getBeforeControlUsedValue());
            } else if (reductionRateThreshold != null && reductionCorrectionRate != null) {
                if (summaryResult.getReductionRate() == null) {
                    //補正削減率の各情報の設定がない場合
                    summaryResult.setCorrectionReductionRate(null);
                    //補正後制御前使用量（制御前使用量と同じ）
                    summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getBeforeControlUsedValue());
                } else if (summaryResult.getReductionRate().compareTo(reductionRateThreshold) >= 0) {
                    //補正削減率1（削減率×削減補正率）
                    summaryResult.setCorrectionReductionRate(summaryResult.getReductionRate()
                            .multiply(reductionCorrectionRate.divide(new BigDecimal("100")))
                            .setScale(1, BigDecimal.ROUND_HALF_UP));
                    if (reductionLowerRateMonth != null && reductionLowerAmountMonth != null) {
                        if (summaryResult.getCorrectionReductionRate().compareTo(reductionLowerRateMonth) <= 0
                                || (summaryResult.getAfterControlUsedValue() != null && summaryResult
                                        .getAfterControlUsedValue().compareTo(reductionLowerAmountMonth) <= 0)) {
                            //補正削減率2（0）
                            summaryResult.setCorrectionReductionRate(BigDecimal.ZERO);
                        }
                    }
                    //補正後制御前使用量（実績/(1-補正削減率))
                    if (summaryResult.getBeforeControlUsedValue() == null
                            || summaryResult.getCorrectionReductionRate() == null
                            || new BigDecimal("100").compareTo(summaryResult.getCorrectionReductionRate()) == 0) {
                        summaryResult.setCorrectionBeforeControlUsedValue(null);
                    } else {
                        summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getAfterControlUsedValue()
                                .divide((BigDecimal.ONE.subtract(
                                        summaryResult.getCorrectionReductionRate().divide(new BigDecimal("100")))), 1,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                } else {
                    //補正削減率1（削減率）
                    summaryResult.setCorrectionReductionRate(summaryResult.getReductionRate());
                    if (reductionLowerRateMonth != null && reductionLowerAmountMonth != null) {
                        if (summaryResult.getCorrectionReductionRate().compareTo(reductionLowerRateMonth) <= 0
                                || (summaryResult.getAfterControlUsedValue() != null && summaryResult
                                        .getAfterControlUsedValue().compareTo(reductionLowerAmountMonth) <= 0)) {
                            //補正削減率2（0）
                            summaryResult.setCorrectionReductionRate(BigDecimal.ZERO);
                        }
                    }
                    //補正後制御前使用量（実績/(1-補正削減率))
                    if (summaryResult.getBeforeControlUsedValue() == null
                            || summaryResult.getCorrectionReductionRate() == null
                            || new BigDecimal("100").compareTo(summaryResult.getCorrectionReductionRate()) == 0) {
                        summaryResult.setCorrectionBeforeControlUsedValue(null);
                    } else {
                        summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getAfterControlUsedValue()
                                .divide((BigDecimal.ONE.subtract(
                                        summaryResult.getCorrectionReductionRate().divide(new BigDecimal("100")))), 1,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                }
            }

            //補正後削減量（補正後制御前使用量 - 実績値）
            if (summaryResult.getCorrectionBeforeControlUsedValue() == null
                    || summaryResult.getAfterControlUsedValue() == null) {
                summaryResult.setCorrectionReductionValue(null);
            } else {
                summaryResult.setCorrectionReductionValue(summaryResult.getCorrectionBeforeControlUsedValue()
                        .subtract(summaryResult.getAfterControlUsedValue()));
            }

        }

        return resultList;
    }

    /**
     * イベント制御（月報）の機器毎のサマリを作成する（Eα以降用）
     *
     * @param timeTableList
     * @param monthlyReportList
     * @param smLineControlVerifyList
     * @param smLineVerifyList
     * @param smControlLoadList
     * @param corpId
     * @param corpCalList
     * @param sumDate
     * @param eventFlgData
     * @return
     */
    public static final List<CommonEventControlMonthlySmSummaryResult> createEventControlSmSummaryMonthly(
            List<CommonEventControlMonthlyTimeTableResult> timeTableList,
            List<CommonDemandMonthReportLineListResult> monthlyReportList,
            List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmLineVerify> smLineVerifyList, List<MSmControlLoadVerify> smControlLoadList, String corpId,
            List<DemandCalendarYearData> corpCalList, String sumDate,
            List<ProductControlLoadListDetailResultData> eventFlgData) {

        List<CommonEventControlMonthlySmSummaryResult> resultList = new ArrayList<>();
        CommonEventControlMonthlySmSummaryResult result = new CommonEventControlMonthlySmSummaryResult();
        Date beforeDate = null;
        List<BigDecimal> workTimeList = new ArrayList<>(eventFlgData.size());
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(eventFlgData.size());
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> shutOffCapacityRate = new ArrayList<>(eventFlgData.size());
        List<BigDecimal> allControlUsedList = new ArrayList<>(eventFlgData.size());
        BigDecimal allControlUsedSummary = null;
        String airVerifyType;
        BigDecimal reductionRateThreshold;
        BigDecimal reductionCorrectionRate;
        BigDecimal reductionLowerRateMonth;
        BigDecimal reductionLowerAmountMonth;
        BigDecimal monthNo;

        //直前の値の初期値を設定する
        for (int i = 0; i < eventFlgData.size(); i++) {
            workTimeList.add(null);
            shutOffCapacityList.add(null);
            allControlUsedList.add(null);
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            Integer index = getListIndex(smLineControl.getId().getControlLoad().intValue(), eventFlgData);
            if(index == null) {
                continue;
            }
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(index, smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(index, smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(index) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList.get(index);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary.add(shutOffCapacityList.get(index));
                }
            }
        }

        //遮断容量比率を算出する
        for (BigDecimal shutOffCapacity : shutOffCapacityList) {
            if (shutOffCapacity == null || shutOffCapacitySummary == null || BigDecimal.ZERO.compareTo(shutOffCapacitySummary) == 0) {
                shutOffCapacityRate.add(null);
            } else {
                shutOffCapacityRate.add(shutOffCapacity.multiply(new BigDecimal("100")).divide(shutOffCapacitySummary,
                        10, BigDecimal.ROUND_HALF_UP));
            }
        }

        //タイムテーブルを計測年月日、制御負荷順にソートする
        if(timeTableList != null && !timeTableList.isEmpty()) {
            timeTableList = timeTableList.stream().sorted(Comparator
                    .comparing(CommonEventControlMonthlyTimeTableResult::getMeasurementDate,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(CommonEventControlMonthlyTimeTableResult::getControlLoad, Comparator.naturalOrder()))
                    .collect(Collectors.toList());

            for (CommonEventControlMonthlyTimeTableResult timeTable : timeTableList) {

                if (beforeDate == null) {
                    result = new CommonEventControlMonthlySmSummaryResult();
                    result.setMeasurementDate(timeTable.getMeasurementDate());
                } else if (!beforeDate.equals(timeTable.getMeasurementDate())) {
                    resultList.add(result);
                    result = new CommonEventControlMonthlySmSummaryResult();
                    result.setMeasurementDate(timeTable.getMeasurementDate());
                }

                //制御時間
                if (timeTable.getControlTimeHour() != null) {
                    if (result.getControlTime() == null) {
                        result.setControlTime(timeTable.getControlTimeHour());
                    } else {
                        result.setControlTime(result.getControlTime().add(timeTable.getControlTimeHour()));
                    }
                }

                //制御回数
                if (timeTable.getControlCount() != null) {
                    if (result.getControlCount() == null) {
                        result.setControlCount(timeTable.getControlCount());
                    } else {
                        result.setControlCount(result.getControlCount().add(timeTable.getControlCount()));
                    }
                }

                //制御前使用量
                if (timeTable.getBeforeUsedValue() != null) {
                    if (result.getBeforeControlUsedValue() == null) {
                        result.setBeforeControlUsedValue(timeTable.getBeforeUsedValue());
                    } else {
                        result.setBeforeControlUsedValue(
                                result.getBeforeControlUsedValue().add(timeTable.getBeforeUsedValue()));
                    }
                }
                // 制御前使用量がnullで、制御率100%の場合、制御後の使用量を設定
                // 制御率100%の場合は、制御前使用量がnullになる
                // 制御がない場合も、制御前使用量がnullになる
                else {
                    if (timeTable.getAfterUsedValue() != null) {
                        if (result.getBeforeControlUsedValue() == null) {
                            result.setBeforeControlUsedValue(timeTable.getAfterUsedValue());
                        } else {
                            result.setBeforeControlUsedValue(
                                    result.getBeforeControlUsedValue().add(timeTable.getAfterUsedValue()));
                        }
                    }
                }

                //削減量
                if (timeTable.getReductionValue() != null) {
                    if (result.getReductionValue() == null) {
                        result.setReductionValue(timeTable.getReductionValue());
                    } else {
                        result.setReductionValue(result.getReductionValue().add(timeTable.getReductionValue()));
                    }
                }

                beforeDate = timeTable.getMeasurementDate();
            }

            //最終データの処理
            resultList.add(result);
        }

        //制御後使用量の設定
        for (CommonDemandMonthReportLineListResult monthReport : monthlyReportList) {
            Boolean existEventData = Boolean.FALSE;
            if(resultList != null && !resultList.isEmpty()) {
                //制御ログがある場合、制御ログと一致する箇所があるか探す
                for (CommonEventControlMonthlySmSummaryResult summaryResult : resultList) {
                    if (summaryResult.getMeasurementDate().equals(monthReport.getMeasurementDate())) {
                        summaryResult.setAfterControlUsedValue(monthReport.getLineValueKwh());
                        existEventData = Boolean.TRUE;
                        break;
                    }
                }
            }

            if(!existEventData) {
                //制御ログがない場合、新たなサマリデータを作成する必要がある
                result = new CommonEventControlMonthlySmSummaryResult();
                result.setMeasurementDate(monthReport.getMeasurementDate());
                result.setAfterControlUsedValue(monthReport.getLineValueKwh());
                resultList.add(result);
            }
        }

        for (CommonEventControlMonthlySmSummaryResult summaryResult : resultList) {

            if (summaryResult.getAfterControlUsedValue() != null) {
                //制御後使用量が設定されている場合かつ各項目がNULLの場合、設定をやり直す
                //制御時間
                if (summaryResult.getControlTime() == null) {
                    summaryResult.setControlTime(BigDecimal.ZERO);
                }
                //制御回数
                if (summaryResult.getControlCount() == null) {
                    summaryResult.setControlCount(BigDecimal.ZERO);
                }
                //制御前使用量（制御後使用量）
                if (summaryResult.getBeforeControlUsedValue() == null) {
                    summaryResult.setBeforeControlUsedValue(summaryResult.getAfterControlUsedValue());
                }
                //削減量
                if (summaryResult.getReductionValue() == null) {
                    summaryResult.setReductionValue(BigDecimal.ZERO);
                }

            }

            allControlUsedSummary = null;
            //全負荷運転使用量（稼働時間が日付単位で違うのでここで算出するしかない）
            for (int i = 0; i < eventFlgData.size(); i++) {
                Boolean existEventData = Boolean.FALSE;
                //稼働時間の取得
                if(timeTableList != null && !timeTableList.isEmpty()){
                    for (CommonEventControlMonthlyTimeTableResult timeTable : timeTableList) {
                        if (summaryResult.getMeasurementDate().equals(timeTable.getMeasurementDate())
                                && new BigDecimal(timeTable.getControlLoad())
                                        .compareTo(eventFlgData.get(i).getControlLoad()) == 0) {
                            if (timeTable.getAllControlTime() != null) {
                                if (allControlUsedSummary == null) {
                                    allControlUsedSummary = timeTable.getAllControlTime();
                                } else {
                                    allControlUsedSummary = allControlUsedSummary.add(timeTable.getAllControlTime());
                                }
                            }
                            existEventData = Boolean.TRUE;
                            break;
                        }
                    }
                }

                if(!existEventData) {
                    //稼働時間が取得できない場合は24時間*遮断容量
                    Integer index = getListIndex(eventFlgData.get(i).getControlLoad().intValue(), eventFlgData);
                    if(index != null) {
                        if(shutOffCapacityList != null && shutOffCapacityList.get(index) != null) {
                            if(allControlUsedSummary == null) {
                                allControlUsedSummary = BigDecimal.valueOf(24).multiply(shutOffCapacityList.get(index));
                            }else {
                                allControlUsedSummary = allControlUsedSummary.add(BigDecimal.valueOf(24).multiply(shutOffCapacityList.get(index)));
                            }
                        }
                    }
                }

            }

            summaryResult.setAllControlTime(allControlUsedSummary);
            //制御後使用量負荷率（制御後使用量/全負荷運転使用量）
            if (summaryResult.getAfterControlUsedValue() == null || allControlUsedSummary == null
                    || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
                summaryResult.setAfterControlLoadFactor(null);
            } else {
                summaryResult.setAfterControlLoadFactor(summaryResult.getAfterControlUsedValue()
                        .multiply(new BigDecimal("100")).divide(allControlUsedSummary, 1, BigDecimal.ROUND_HALF_UP));
            }

            //制御前使用量負荷率（制御前使用量/全負荷運転使用量）
            if (summaryResult.getBeforeControlUsedValue() == null || allControlUsedSummary == null
                    || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
                summaryResult.setBeforeControlLoadFactor(null);
            } else {
                summaryResult.setBeforeControlLoadFactor(summaryResult.getBeforeControlUsedValue()
                        .multiply(new BigDecimal("100")).divide(allControlUsedSummary, 1, BigDecimal.ROUND_HALF_UP));
            }

            //削減率（1-(制御後使用量/制御前使用量））
            if (summaryResult.getAfterControlUsedValue() == null || summaryResult.getBeforeControlUsedValue() == null
                    || BigDecimal.ZERO.compareTo(summaryResult.getBeforeControlUsedValue()) == 0) {
                summaryResult.setReductionRate(null);
            } else {
                summaryResult.setReductionRate(new BigDecimal("100")
                        .subtract((summaryResult.getAfterControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(summaryResult.getBeforeControlUsedValue(), 1, BigDecimal.ROUND_HALF_UP))));
            }

            //補正削減率の各情報を取得する
            //対象日の月度（企業集計ベース）
            if (DemandEmsUtility.getCurrentYearReport(corpCalList, summaryResult.getMeasurementDate(),
                    sumDate) == null) {
                monthNo = null;
            } else {
                monthNo = DemandEmsUtility
                        .getCurrentYearReport(corpCalList, summaryResult.getMeasurementDate(), sumDate)
                        .getMonthNo();
            }

            //削減率閾値
            reductionRateThreshold = getReductionRateThreshold(smLineVerifyList);
            //削減補正率
            reductionCorrectionRate = getReductionCorrectionRate(smLineVerifyList);
            //削減下限率
            reductionLowerRateMonth = getReductionLowerRateMonth(smLineVerifyList, monthNo);
            //削減下限使用量
            reductionLowerAmountMonth = getReductionLowerAmountMonth(smLineVerifyList, monthNo);

            if (reductionRateThreshold == null && reductionCorrectionRate == null && reductionLowerRateMonth == null
                    && reductionLowerAmountMonth == null) {
                //補正削減率の各情報の設定がない場合
                summaryResult.setCorrectionReductionRate(null);
                //補正後制御前使用量（制御前使用量と同じ）
                summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getBeforeControlUsedValue());
            } else if (reductionRateThreshold != null && reductionCorrectionRate != null) {
                if (summaryResult.getReductionRate() == null) {
                    //補正削減率の各情報の設定がない場合
                    summaryResult.setCorrectionReductionRate(null);
                    //補正後制御前使用量（制御前使用量と同じ）
                    summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getBeforeControlUsedValue());
                } else if (summaryResult.getReductionRate().compareTo(reductionRateThreshold) >= 0) {
                    //補正削減率1（削減率×削減補正率）
                    summaryResult.setCorrectionReductionRate(summaryResult.getReductionRate()
                            .multiply(reductionCorrectionRate.divide(new BigDecimal("100")))
                            .setScale(1, BigDecimal.ROUND_HALF_UP));
                    if (reductionLowerRateMonth != null && reductionLowerAmountMonth != null) {
                        if (summaryResult.getCorrectionReductionRate().compareTo(reductionLowerRateMonth) <= 0
                                || (summaryResult.getAfterControlUsedValue() != null && summaryResult
                                        .getAfterControlUsedValue().compareTo(reductionLowerAmountMonth) <= 0)) {
                            //補正削減率2（0）
                            summaryResult.setCorrectionReductionRate(BigDecimal.ZERO);
                        }
                    }
                    //補正後制御前使用量（実績/(1-補正削減率))
                    if (summaryResult.getBeforeControlUsedValue() == null
                            || summaryResult.getCorrectionReductionRate() == null
                            || new BigDecimal("100").compareTo(summaryResult.getCorrectionReductionRate()) == 0) {
                        summaryResult.setCorrectionBeforeControlUsedValue(null);
                    } else {
                        summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getAfterControlUsedValue()
                                .divide((BigDecimal.ONE.subtract(
                                        summaryResult.getCorrectionReductionRate().divide(new BigDecimal("100")))), 1,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                } else {
                    //補正削減率1（削減率）
                    summaryResult.setCorrectionReductionRate(summaryResult.getReductionRate());
                    if (reductionLowerRateMonth != null && reductionLowerAmountMonth != null) {
                        if (summaryResult.getCorrectionReductionRate().compareTo(reductionLowerRateMonth) <= 0
                                || (summaryResult.getAfterControlUsedValue() != null && summaryResult
                                        .getAfterControlUsedValue().compareTo(reductionLowerAmountMonth) <= 0)) {
                            //補正削減率2（0）
                            summaryResult.setCorrectionReductionRate(BigDecimal.ZERO);
                        }
                    }
                    //補正後制御前使用量（実績/(1-補正削減率))
                    if (summaryResult.getBeforeControlUsedValue() == null
                            || summaryResult.getCorrectionReductionRate() == null
                            || new BigDecimal("100").compareTo(summaryResult.getCorrectionReductionRate()) == 0) {
                        summaryResult.setCorrectionBeforeControlUsedValue(null);
                    } else {
                        summaryResult.setCorrectionBeforeControlUsedValue(summaryResult.getAfterControlUsedValue()
                                .divide((BigDecimal.ONE.subtract(
                                        summaryResult.getCorrectionReductionRate().divide(new BigDecimal("100")))), 1,
                                        BigDecimal.ROUND_HALF_UP));
                    }
                }
            }

            //補正後削減量（補正後制御前使用量 - 実績値）
            if (summaryResult.getCorrectionBeforeControlUsedValue() == null
                    || summaryResult.getAfterControlUsedValue() == null) {
                summaryResult.setCorrectionReductionValue(null);
            } else {
                summaryResult.setCorrectionReductionValue(summaryResult.getCorrectionBeforeControlUsedValue()
                        .subtract(summaryResult.getAfterControlUsedValue()));
            }

        }

        return resultList;
    }

    /**
     * 削減率閾値を取得する
     *
     * @param smLineVerifyList
     * @return
     */
    private static BigDecimal getReductionRateThreshold(List<MSmLineVerify> smLineVerifyList) {
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            return null;
        }

        return smLineVerifyList.get(0).getReductionRateThreshold();
    }

    /**
     * 削減補正率を取得する
     *
     * @param smLineVerifyList
     * @return
     */
    private static BigDecimal getReductionCorrectionRate(List<MSmLineVerify> smLineVerifyList) {
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            return null;
        }

        return smLineVerifyList.get(0).getReductionCorrectionRate();
    }

    /**
     * 削減下限率を取得する
     *
     * @param smLineVerifyList
     * @param monthNo
     * @return
     */
    private static BigDecimal getReductionLowerRateMonth(List<MSmLineVerify> smLineVerifyList, BigDecimal monthNo) {

        if (smLineVerifyList == null || smLineVerifyList.size() != 1 || monthNo == null) {
            return null;
        }

        if (BigDecimal.ONE.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth1();
        } else if (new BigDecimal("2").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth2();
        } else if (new BigDecimal("3").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth3();
        } else if (new BigDecimal("4").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth4();
        } else if (new BigDecimal("5").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth5();
        } else if (new BigDecimal("6").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth6();
        } else if (new BigDecimal("7").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth7();
        } else if (new BigDecimal("8").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth8();
        } else if (new BigDecimal("9").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth9();
        } else if (BigDecimal.TEN.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth10();
        } else if (new BigDecimal("11").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth11();
        } else if (new BigDecimal("12").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerRateMonth12();
        } else {
            return null;
        }
    }

    /**
     * 削減下限使用量を取得する
     *
     * @param smLineVerifyList
     * @param monthNo
     * @return
     */
    private static BigDecimal getReductionLowerAmountMonth(List<MSmLineVerify> smLineVerifyList, BigDecimal monthNo) {

        if (smLineVerifyList == null || smLineVerifyList.size() != 1 || monthNo == null) {
            return null;
        }

        if (BigDecimal.ONE.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth1();
        } else if (new BigDecimal("2").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth2();
        } else if (new BigDecimal("3").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth3();
        } else if (new BigDecimal("4").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth4();
        } else if (new BigDecimal("5").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth5();
        } else if (new BigDecimal("6").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth6();
        } else if (new BigDecimal("7").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth7();
        } else if (new BigDecimal("8").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth8();
        } else if (new BigDecimal("9").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth9();
        } else if (BigDecimal.TEN.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth10();
        } else if (new BigDecimal("11").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth11();
        } else if (new BigDecimal("12").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getReductionLowerAmountMonth12();
        } else {
            return null;
        }

    }

    /**
     * 提案使用量を取得する
     *
     * @param smLineVerifyList
     * @param monthNo
     * @return
     */
    private static BigDecimal getProposalAmountUsedMonth(List<MSmLineVerify> smLineVerifyList, BigDecimal monthNo) {

        if (smLineVerifyList == null || smLineVerifyList.size() != 1 || monthNo == null) {
            return null;
        }

        if (BigDecimal.ONE.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth1();
        } else if (new BigDecimal("2").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth2();
        } else if (new BigDecimal("3").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth3();
        } else if (new BigDecimal("4").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth4();
        } else if (new BigDecimal("5").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth5();
        } else if (new BigDecimal("6").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth6();
        } else if (new BigDecimal("7").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth7();
        } else if (new BigDecimal("8").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth8();
        } else if (new BigDecimal("9").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth9();
        } else if (BigDecimal.TEN.compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth10();
        } else if (new BigDecimal("11").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth11();
        } else if (new BigDecimal("12").compareTo(monthNo) == 0) {
            return smLineVerifyList.get(0).getProposalAmountUsedMonth12();
        } else {
            return null;
        }

    }

    /**
     * イベント制御ログを、制御負荷単位に分解したLinkedHashSetを作成する（G2以前用）
     *
     * @param eventControlList
     * @param controlLoadCount
     * @param recordYmdHmsFrom
     * @param productCd
     * @return
     */
    public static final LinkedHashSet<CommonControlLoadResult> createEventControlSet(
            List<EventControlLogResult> eventControlList, Integer controlLoadCount, String recordYmdHmsFrom,
            String productCd) {

        LinkedHashSet<CommonControlLoadResult> resultSet = new LinkedHashSet<>();
        StringBuilder allFree = new StringBuilder();
        StringBuilder currentLoadStatus = new StringBuilder();
        String beforeRecordDate = null;

        if (eventControlList == null || eventControlList.isEmpty() || controlLoadCount == null
                || controlLoadCount == 0) {
            //イベント制御ログ情報がない、または制御負荷が存在しない場合はnullを返す
            return null;
        }

        //制御負荷数だけ、すべて開放のデータを作成する
        for (int i = 1; i <= controlLoadCount; i++) {
            allFree.append(ApiSimpleConstants.LOAD_CONTROL_FREE);
        }

        //記録日時、制御負荷の順にソートする
        eventControlList = eventControlList.stream()
                .sorted(Comparator.comparing(EventControlLogResult::getRecordYmdhms, Comparator.naturalOrder())
                        .thenComparing(EventControlLogResult::getControlLoad, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        //1件目のデータを精査する
        if (DateUtility
                .conversionDate(eventControlList.get(0).getRecordYmdhms(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
                .after(DateUtility.conversionDate(recordYmdHmsFrom, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS))) {
            //1件目のデータが開始より後の場合、開始日（YYYYMMDDHHMM）のデータを作成する（すべて開放）]
            resultSet.add(new CommonControlLoadResult(recordYmdHmsFrom, allFree.toString()));
        }

        //イベント制御ログ情報を繰り返す
        for (EventControlLogResult eventControl : eventControlList) {

            if (beforeRecordDate == null) {
                //すべて開放のデータを作成する
                currentLoadStatus = allFree;
            } else if (!beforeRecordDate.equals(eventControl.getRecordYmdhms())) {
                //前の記録日時と異なる場合
                resultSet.add(new CommonControlLoadResult(beforeRecordDate, currentLoadStatus.toString()));
                //すべて開放のデータを作成する
                currentLoadStatus = allFree;
            }

            //制御状態を取得
            String loadStatus = createEventStatus(eventControl, productCd);
            //指定の制御負荷の位置に制御状態を設定する
            currentLoadStatus.setCharAt(eventControl.getControlLoad().subtract(BigDecimal.ONE).intValue(),
                    loadStatus.charAt(0));

            beforeRecordDate = eventControl.getRecordYmdhms();
        }

        //最終データのみ処理を行う
        resultSet.add(new CommonControlLoadResult(beforeRecordDate, currentLoadStatus.toString()));

        return resultSet;
    }

    /**
     * イベント制御ログを、制御負荷単位に分解したLinkedHashSetを作成する（Eα以降用）
     *
     * @param eventControlList
     * @param controlLoadCount
     * @param recordYmdHmsFrom
     * @param productCd
     * @param eventFlgData
     * @return
     */
    public static final LinkedHashSet<CommonControlLoadResult> createEventControlSet(
            List<EventControlLogResult> eventControlList, Integer controlLoadCount, String recordYmdHmsFrom,
            String productCd, List<ProductControlLoadListDetailResultData> eventFlgData) {

        LinkedHashSet<CommonControlLoadResult> resultSet = new LinkedHashSet<>();
        StringBuilder allFree = new StringBuilder();
        StringBuilder currentLoadStatus = new StringBuilder();
        String beforeRecordDate = null;

        if (eventControlList == null || eventControlList.isEmpty() || controlLoadCount == null
                || controlLoadCount == 0) {
            //イベント制御ログ情報がない、または制御負荷が存在しない場合はnullを返す
            return null;
        }

        //制御負荷数だけ、すべて開放のデータを作成する
        for (int i = 1; i <= controlLoadCount; i++) {
            allFree.append(ApiSimpleConstants.LOAD_CONTROL_FREE);
        }

        //記録日時、制御負荷の順にソートする
        eventControlList = eventControlList.stream()
                .sorted(Comparator.comparing(EventControlLogResult::getRecordYmdhms, Comparator.naturalOrder())
                        .thenComparing(EventControlLogResult::getControlLoad, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        //1件目のデータを精査する
        if (DateUtility
                .conversionDate(eventControlList.get(0).getRecordYmdhms(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)
                .after(DateUtility.conversionDate(recordYmdHmsFrom, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS))) {
            //1件目のデータが開始より後の場合、開始日（YYYYMMDDHHMM）のデータを作成する（すべて開放）]
            resultSet.add(new CommonControlLoadResult(recordYmdHmsFrom, allFree.toString()));
        }

        //イベント制御ログ情報を繰り返す
        for (EventControlLogResult eventControl : eventControlList) {

            if (beforeRecordDate == null) {
                //すべて開放のデータを作成する
                currentLoadStatus = allFree;
            } else if (!beforeRecordDate.equals(eventControl.getRecordYmdhms())) {
                //前の記録日時と異なる場合
                resultSet.add(new CommonControlLoadResult(beforeRecordDate, currentLoadStatus.toString()));
                //すべて開放のデータを作成する
                currentLoadStatus = allFree;
            }

            //制御状態を取得
            String loadStatus = createEventStatus(eventControl, productCd);
            //指定の制御負荷の位置に制御状態を設定する
            Integer index = getListIndex(eventControl.getControlLoad().intValue(), eventFlgData);
            if(index == null) {
                continue;
            }
            currentLoadStatus.setCharAt(index, loadStatus.charAt(0));

            beforeRecordDate = eventControl.getRecordYmdhms();
        }

        //最終データのみ処理を行う
        resultSet.add(new CommonControlLoadResult(beforeRecordDate, currentLoadStatus.toString()));

        return resultSet;
    }

    /**
     * 制御状態を負荷ごとに分割して値を作成する（イベント制御負荷）
     *
     * @param eventControl
     * @param productCd
     * @return
     */
    private static String createEventStatus(EventControlLogResult eventControl, String productCd) {

        if(OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)) {
            String binaryString = DemandDataUtility
                    .createBinaryStringFromControlStatus(eventControl.getControlEvent1Kind());
            if (CheckUtility.isNullOrEmpty(binaryString)) {
                return null;
            } else {
                return binaryString.substring(0, 1);
            }
        } else {
            return eventControl.getControlStatus();
        }

    }

    /**
     * 指定月のサマリ情報を作成する（G2以前）
     * @param smSummaryList
     * @param smLineVerifyList
     * @param controlLoadCount
     * @param smLineControlVerifyList
     * @param smControlLoadList
     * @param currentYear
     * @param currentMonth
     * @return
     */
    public static final CommonEventControlYearlyMonthSummaryResult createEventControlMonthSummaryYearly(
            List<CommonEventControlMonthlySmSummaryResult> smSummaryList, List<MSmLineVerify> smLineVerifyList,
            Integer controlLoadCount, List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmControlLoadVerify> smControlLoadList, String currentYear, BigDecimal currentMonth) {

        CommonEventControlYearlyMonthSummaryResult result = new CommonEventControlYearlyMonthSummaryResult();
        List<BigDecimal> workTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(controlLoadCount);
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> allControlUsedList = new ArrayList<>(controlLoadCount);
        BigDecimal allControlUsedSummary = null;
        String airVerifyType;

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            workTimeList.add(null);
            shutOffCapacityList.add(null);
            allControlUsedList.add(null);
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList
                            .get(smLineControl.getId().getControlLoad().intValue() - 1);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary
                            .add(shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1));
                }
            }
        }

        //年月を設定する
        result.setCalYm(
                currentYear.concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentMonth.intValue())));

        //提案使用量
        result.setProposalUsedValue(getProposalAmountUsedMonth(smLineVerifyList, currentMonth));

        if(smSummaryList != null && !smSummaryList.isEmpty()) {
            for (CommonEventControlMonthlySmSummaryResult smSummary : smSummaryList) {
                //全負荷運転使用量
                if (smSummary.getAfterControlUsedValue() != null && smSummary.getAllControlTime() != null) {
                    if (result.getAllControlTime() == null) {
                        result.setAllControlTime(smSummary.getAllControlTime());
                    } else {
                        result.setAllControlTime(result.getAllControlTime().add(smSummary.getAllControlTime()));
                    }
                }

                //制御時間
                if (smSummary.getControlTime() != null) {
                    if (result.getControlTime() == null) {
                        result.setControlTime(smSummary.getControlTime());
                    } else {
                        result.setControlTime(result.getControlTime().add(smSummary.getControlTime()));
                    }
                }

                //制御回数
                if (smSummary.getControlCount() != null) {
                    if (result.getControlCount() == null) {
                        result.setControlCount(smSummary.getControlCount());
                    } else {
                        result.setControlCount(result.getControlCount().add(smSummary.getControlCount()));
                    }
                }

                //制御後使用量
                if (smSummary.getAfterControlUsedValue() != null) {
                    if (result.getAfterControlUsedValue() == null) {
                        result.setAfterControlUsedValue(smSummary.getAfterControlUsedValue());
                    } else {
                        result.setAfterControlUsedValue(
                                result.getAfterControlUsedValue().add(smSummary.getAfterControlUsedValue()));
                    }
                }

                //制御前使用量
                if (smSummary.getBeforeControlUsedValue() != null) {
                    if (result.getBeforeControlUsedValue() == null) {
                        result.setBeforeControlUsedValue(smSummary.getBeforeControlUsedValue());
                    } else {
                        result.setBeforeControlUsedValue(
                                result.getBeforeControlUsedValue().add(smSummary.getBeforeControlUsedValue()));
                    }
                }

                //削減量
                if (smSummary.getReductionValue() != null) {
                    if (result.getReductionValue() == null) {
                        result.setReductionValue(smSummary.getReductionValue());
                    } else {
                        result.setReductionValue(result.getReductionValue().add(smSummary.getReductionValue()));
                    }
                }

                //補正後制御前使用量
                if (smSummary.getCorrectionBeforeControlUsedValue() != null) {
                    if (result.getCorrectionBeforeControlUsedValue() == null) {
                        result.setCorrectionBeforeControlUsedValue(smSummary.getCorrectionBeforeControlUsedValue());
                    } else {
                        result.setCorrectionBeforeControlUsedValue(result.getCorrectionBeforeControlUsedValue()
                                .add(smSummary.getCorrectionBeforeControlUsedValue()));
                    }
                }

                //補正後削減量
                if (smSummary.getCorrectionReductionValue() != null) {
                    if (result.getCorrectionReductionValue() == null) {
                        result.setCorrectionReductionValue(smSummary.getCorrectionReductionValue());
                    } else {
                        result.setCorrectionReductionValue(
                                result.getCorrectionReductionValue().add(smSummary.getCorrectionReductionValue()));
                    }
                }
            }
        }

        //負荷率の算出と判定
        allControlUsedSummary = result.getAllControlTime();

        //負荷率の算出
        if (allControlUsedSummary == null || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
            result.setAfterControlLoadFactor(null);
            result.setBeforeControlLoadFactor(null);
        } else {
            //制御後使用量負荷率（制御後使用量/全負荷運転使用量（1か月分））
            if (result.getAfterControlUsedValue() == null) {
                result.setAfterControlLoadFactor(null);
            } else {
                result.setAfterControlLoadFactor(
                        result.getAfterControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(result.getAllControlTime(), 10, RoundingMode.HALF_UP));
            }
            //制御前使用量負荷率（制御前使用量/全負荷運転使用量（1か月分））
            if (result.getBeforeControlUsedValue() == null) {
                result.setBeforeControlLoadFactor(null);
            } else {
                result.setBeforeControlLoadFactor(
                        result.getBeforeControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(result.getAllControlTime(), 10, RoundingMode.HALF_UP));
            }
        }

        //判定の実施
        if (result.getAfterControlUsedValue() == null || result.getProposalUsedValue() == null) {
            result.setJudge(null);
        } else {
            result.setJudge(result.getAfterControlUsedValue().compareTo(result.getProposalUsedValue()) <= 0);
        }

        return result;
    }

    /**
     * 指定月のサマリ情報を作成する（Eα以降）
     * @param smSummaryList
     * @param smLineVerifyList
     * @param controlLoadCount
     * @param smLineControlVerifyList
     * @param smControlLoadList
     * @param currentYear
     * @param currentMonth
     * @param eventFlgData
     * @return
     */
    public static final CommonEventControlYearlyMonthSummaryResult createEventControlMonthSummaryYearly(
            List<CommonEventControlMonthlySmSummaryResult> smSummaryList, List<MSmLineVerify> smLineVerifyList,
            Integer controlLoadCount, List<MSmLineControlLoadVerify> smLineControlVerifyList,
            List<MSmControlLoadVerify> smControlLoadList, String currentYear, BigDecimal currentMonth,
            List<ProductControlLoadListDetailResultData> eventFlgData) {

        CommonEventControlYearlyMonthSummaryResult result = new CommonEventControlYearlyMonthSummaryResult();
        List<BigDecimal> workTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(controlLoadCount);
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> allControlUsedList = new ArrayList<>(controlLoadCount);
        BigDecimal allControlUsedSummary = null;
        String airVerifyType;

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            workTimeList.add(null);
            shutOffCapacityList.add(null);
            allControlUsedList.add(null);
        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            Integer index = getListIndex(smLineControl.getId().getControlLoad().intValue(), eventFlgData);
            if(index == null) {
                continue;
            }
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(index, smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(index, smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(index) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList.get(index);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary.add(shutOffCapacityList.get(index));
                }
            }
        }

        //年月を設定する
        result.setCalYm(
                currentYear.concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentMonth.intValue())));

        //提案使用量
        result.setProposalUsedValue(getProposalAmountUsedMonth(smLineVerifyList, currentMonth));

        if(smSummaryList != null && !smSummaryList.isEmpty()) {
            for (CommonEventControlMonthlySmSummaryResult smSummary : smSummaryList) {
                //全負荷運転使用量
                if (smSummary.getAfterControlUsedValue() != null && smSummary.getAllControlTime() != null) {
                    if (result.getAllControlTime() == null) {
                        result.setAllControlTime(smSummary.getAllControlTime());
                    } else {
                        result.setAllControlTime(result.getAllControlTime().add(smSummary.getAllControlTime()));
                    }
                }

                //制御時間
                if (smSummary.getControlTime() != null) {
                    if (result.getControlTime() == null) {
                        result.setControlTime(smSummary.getControlTime());
                    } else {
                        result.setControlTime(result.getControlTime().add(smSummary.getControlTime()));
                    }
                }

                //制御回数
                if (smSummary.getControlCount() != null) {
                    if (result.getControlCount() == null) {
                        result.setControlCount(smSummary.getControlCount());
                    } else {
                        result.setControlCount(result.getControlCount().add(smSummary.getControlCount()));
                    }
                }

                //制御後使用量
                if (smSummary.getAfterControlUsedValue() != null) {
                    if (result.getAfterControlUsedValue() == null) {
                        result.setAfterControlUsedValue(smSummary.getAfterControlUsedValue());
                    } else {
                        result.setAfterControlUsedValue(
                                result.getAfterControlUsedValue().add(smSummary.getAfterControlUsedValue()));
                    }
                }

                //制御前使用量
                if (smSummary.getBeforeControlUsedValue() != null) {
                    if (result.getBeforeControlUsedValue() == null) {
                        result.setBeforeControlUsedValue(smSummary.getBeforeControlUsedValue());
                    } else {
                        result.setBeforeControlUsedValue(
                                result.getBeforeControlUsedValue().add(smSummary.getBeforeControlUsedValue()));
                    }
                }

                //削減量
                if (smSummary.getReductionValue() != null) {
                    if (result.getReductionValue() == null) {
                        result.setReductionValue(smSummary.getReductionValue());
                    } else {
                        result.setReductionValue(result.getReductionValue().add(smSummary.getReductionValue()));
                    }
                }

                //補正後制御前使用量
                if (smSummary.getCorrectionBeforeControlUsedValue() != null) {
                    if (result.getCorrectionBeforeControlUsedValue() == null) {
                        result.setCorrectionBeforeControlUsedValue(smSummary.getCorrectionBeforeControlUsedValue());
                    } else {
                        result.setCorrectionBeforeControlUsedValue(result.getCorrectionBeforeControlUsedValue()
                                .add(smSummary.getCorrectionBeforeControlUsedValue()));
                    }
                }

                //補正後削減量
                if (smSummary.getCorrectionReductionValue() != null) {
                    if (result.getCorrectionReductionValue() == null) {
                        result.setCorrectionReductionValue(smSummary.getCorrectionReductionValue());
                    } else {
                        result.setCorrectionReductionValue(
                                result.getCorrectionReductionValue().add(smSummary.getCorrectionReductionValue()));
                    }
                }
            }
        }

        //負荷率の算出と判定
        allControlUsedSummary = result.getAllControlTime();

        //負荷率の算出
        if (allControlUsedSummary == null || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
            result.setAfterControlLoadFactor(null);
            result.setBeforeControlLoadFactor(null);
        } else {
            //制御後使用量負荷率（制御後使用量/全負荷運転使用量（1か月分））
            if (result.getAfterControlUsedValue() == null) {
                result.setAfterControlLoadFactor(null);
            } else {
                result.setAfterControlLoadFactor(
                        result.getAfterControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(result.getAllControlTime(), 10, RoundingMode.HALF_UP));
            }
            //制御前使用量負荷率（制御前使用量/全負荷運転使用量（1か月分））
            if (result.getBeforeControlUsedValue() == null) {
                result.setBeforeControlLoadFactor(null);
            } else {
                result.setBeforeControlLoadFactor(
                        result.getBeforeControlUsedValue().multiply(new BigDecimal("100"))
                                .divide(result.getAllControlTime(), 10, RoundingMode.HALF_UP));
            }
        }

        //判定の実施
        if (result.getAfterControlUsedValue() == null || result.getProposalUsedValue() == null) {
            result.setJudge(null);
        } else {
            result.setJudge(result.getAfterControlUsedValue().compareTo(result.getProposalUsedValue()) <= 0);
        }

        return result;
    }

    /**
     * 月単位のサマリ情報を作成する
     *
     * @param smSummaryList
     * @param calList
     * @param smLineVerifyList
     * @param summaryRange
     * @param controlLoadCount
     * @param smLineControlVerifyList
     * @param smControlLoadList
     * @param sumDate
     * @return
     */
    public static final List<CommonEventControlYearlyMonthSummaryResult> createEventControlMonthSummaryYearly(
            List<CommonEventControlMonthlySmSummaryResult> smSummaryList, List<DemandCalendarYearData> calList,
            List<MSmLineVerify> smLineVerifyList, SummaryRangeForYearResult summaryRange, Integer controlLoadCount,
            List<MSmLineControlLoadVerify> smLineControlVerifyList, List<MSmControlLoadVerify> smControlLoadList,
            String sumDate) {

        List<CommonEventControlYearlyMonthSummaryResult> resultList = new ArrayList<>();
        List<BigDecimal> workTimeList = new ArrayList<>(controlLoadCount);
        List<BigDecimal> shutOffCapacityList = new ArrayList<>(controlLoadCount);
        BigDecimal shutOffCapacitySummary = null;
        List<BigDecimal> allControlUsedList = new ArrayList<>(controlLoadCount);
        BigDecimal allControlUsedSummary = null;
        String airVerifyType;

        if (smSummaryList == null || smSummaryList.isEmpty()) {
            return null;
        }

        //直前の値の初期値を設定する
        for (int i = 0; i < controlLoadCount; i++) {
            workTimeList.add(null);
            shutOffCapacityList.add(null);
            allControlUsedList.add(null);
        }

        //稼動時間を取得する
        //        for (MSmControlLoadVerify controlLoadVerify : smControlLoadList) {
        //            workTimeList.set(controlLoadVerify.getId().getControlLoad().intValue() - 1,
        //                    controlLoadVerify.getControlLoadRunningHours());
        //        }

        //遮断容量を取得する
        if (smLineVerifyList == null || smLineVerifyList.size() != 1) {
            airVerifyType = ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal();
        } else {
            airVerifyType = smLineVerifyList.get(0).getAirVerifyType();
        }
        for (MSmLineControlLoadVerify smLineControl : smLineControlVerifyList) {
            if (ApiGenericTypeConstants.AIR_VERIFY_TYPE.COOL.getVal().equals(airVerifyType)) {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent1LoadShutOffCapacity());
            } else {
                shutOffCapacityList.set(smLineControl.getId().getControlLoad().intValue() - 1,
                        smLineControl.getEvent2LoadShutOffCapacity());
            }
            if (shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1) != null) {
                if (shutOffCapacitySummary == null) {
                    shutOffCapacitySummary = shutOffCapacityList
                            .get(smLineControl.getId().getControlLoad().intValue() - 1);
                } else {
                    shutOffCapacitySummary = shutOffCapacitySummary
                            .add(shutOffCapacityList.get(smLineControl.getId().getControlLoad().intValue() - 1));
                }
            }
        }

        //全負荷運転使用量を算出する
        //        for (int i = 0; i < controlLoadCount; i++) {
        //            if (shutOffCapacityList.get(i) == null || workTimeList.get(i) == null) {
        //                allControlUsedList.set(i, null);
        //            } else {
        //                allControlUsedList.set(i, shutOffCapacityList.get(i).multiply(workTimeList.get(i)));
        //                if (allControlUsedSummary == null) {
        //                    allControlUsedSummary = allControlUsedList.get(i);
        //                } else {
        //                    allControlUsedSummary = allControlUsedSummary.add(allControlUsedList.get(i));
        //                }
        //            }
        //        }

        //集計範囲分のサマリリストを作成する
        String currentYear = summaryRange.getYearFrom();
        BigDecimal currentMonth = summaryRange.getMonthFrom();
        while (new BigDecimal(currentYear).compareTo(new BigDecimal(summaryRange.getYearTo())) < 0
                || (currentYear.equals(summaryRange.getYearTo())
                        && currentMonth.compareTo(summaryRange.getMonthTo()) <= 0)) {
            resultList.add(new CommonEventControlYearlyMonthSummaryResult(
                    currentYear
                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentMonth.intValue())),
                    null, null, null, null, null, null, null, null, null, null, null, null));
            if (new BigDecimal("12").compareTo(currentMonth) == 0) {
                currentYear = String.valueOf(Integer.parseInt(currentYear) + 1);
                currentMonth = BigDecimal.ONE;
            } else {
                currentMonth = currentMonth.add(BigDecimal.ONE);
            }
        }

        for (CommonEventControlMonthlySmSummaryResult smSummary : smSummaryList) {
            //計測年月日の月度を算出する
            String currentCalYm;
            CurrentYearReportResult currentYearResult = DemandEmsUtility.getCurrentYearReport(calList,
                    smSummary.getMeasurementDate(), sumDate);
            if (currentYearResult == null) {
                continue;
            } else {
                currentCalYm = currentYearResult.getCalYear().concat(String
                        .format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, currentYearResult.getMonthNo().intValue()));
            }

            for (CommonEventControlYearlyMonthSummaryResult result : resultList) {
                if (result.getCalYm().equals(currentCalYm)) {

                    //全負荷運転使用量
                    if (smSummary.getAfterControlUsedValue() != null && smSummary.getAllControlTime() != null) {
                        if (result.getAllControlTime() == null) {
                            result.setAllControlTime(smSummary.getAllControlTime());
                        } else {
                            result.setAllControlTime(result.getAllControlTime().add(smSummary.getAllControlTime()));
                        }
                    }

                    //制御時間
                    if (smSummary.getControlTime() != null) {
                        if (result.getControlTime() == null) {
                            result.setControlTime(smSummary.getControlTime());
                        } else {
                            result.setControlTime(result.getControlTime().add(smSummary.getControlTime()));
                        }
                    }
                    //制御回数
                    if (smSummary.getControlCount() != null) {
                        if (result.getControlCount() == null) {
                            result.setControlCount(smSummary.getControlCount());
                        } else {
                            result.setControlCount(result.getControlCount().add(smSummary.getControlCount()));
                        }
                    }
                    //制御後使用量
                    if (smSummary.getAfterControlUsedValue() != null) {
                        if (result.getAfterControlUsedValue() == null) {
                            result.setAfterControlUsedValue(smSummary.getAfterControlUsedValue());
                        } else {
                            result.setAfterControlUsedValue(
                                    result.getAfterControlUsedValue().add(smSummary.getAfterControlUsedValue()));
                        }
                    }
                    //制御前使用量
                    if (smSummary.getBeforeControlUsedValue() != null) {
                        if (result.getBeforeControlUsedValue() == null) {
                            result.setBeforeControlUsedValue(smSummary.getBeforeControlUsedValue());
                        } else {
                            result.setBeforeControlUsedValue(
                                    result.getBeforeControlUsedValue().add(smSummary.getBeforeControlUsedValue()));
                        }
                    }
                    //削減量
                    if (smSummary.getReductionValue() != null) {
                        if (result.getReductionValue() == null) {
                            result.setReductionValue(smSummary.getReductionValue());
                        } else {
                            result.setReductionValue(result.getReductionValue().add(smSummary.getReductionValue()));
                        }
                    }
                    //補正後制御前使用量
                    if (smSummary.getCorrectionBeforeControlUsedValue() != null) {
                        if (result.getCorrectionBeforeControlUsedValue() == null) {
                            result.setCorrectionBeforeControlUsedValue(smSummary.getCorrectionBeforeControlUsedValue());
                        } else {
                            result.setCorrectionBeforeControlUsedValue(result.getCorrectionBeforeControlUsedValue()
                                    .add(smSummary.getCorrectionBeforeControlUsedValue()));
                        }
                    }
                    //補正後削減量
                    if (smSummary.getCorrectionReductionValue() != null) {
                        if (result.getCorrectionReductionValue() == null) {
                            result.setCorrectionReductionValue(smSummary.getCorrectionReductionValue());
                        } else {
                            result.setCorrectionReductionValue(
                                    result.getCorrectionReductionValue().add(smSummary.getCorrectionReductionValue()));
                        }
                    }
                    //提案使用量
                    if (result.getProposalUsedValue() == null) {
                        result.setProposalUsedValue(getProposalAmountUsedMonth(smLineVerifyList,
                                new BigDecimal(currentYearResult.getMonthNo().intValue())));
                    }

                    break;
                }
            }
        }

        //負荷率の算出と判定
        for (CommonEventControlYearlyMonthSummaryResult result : resultList) {

            allControlUsedSummary = result.getAllControlTime();

            //負荷率の算出
            if (allControlUsedSummary == null || BigDecimal.ZERO.compareTo(allControlUsedSummary) == 0) {
                result.setAfterControlLoadFactor(null);
                result.setBeforeControlLoadFactor(null);
            } else {
                List<DemandCalendarYearData> tempCalList = calList.stream()
                        .filter(i -> result.getCalYm().equals(i.getYearNo().concat(
                                String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, i.getMonthNo().intValue()))))
                        .collect(Collectors.toList());
                if (tempCalList == null || tempCalList.size() != 1) {
                    result.setAfterControlLoadFactor(null);
                    result.setBeforeControlLoadFactor(null);
                } else {
                    //制御後使用量負荷率（制御後使用量/全負荷運転使用量（1か月分））
                    if (result.getAfterControlUsedValue() == null) {
                        result.setAfterControlLoadFactor(null);
                    } else {
                        result.setAfterControlLoadFactor(
                                result.getAfterControlUsedValue().multiply(new BigDecimal("100"))
                                        .divide(result.getAllControlTime(), 10, BigDecimal.ROUND_HALF_UP));
                    }
                    //制御前使用量負荷率（制御前使用量/全負荷運転使用量（1か月分））
                    if (result.getBeforeControlUsedValue() == null) {
                        result.setBeforeControlLoadFactor(null);
                    } else {
                        result.setBeforeControlLoadFactor(
                                result.getBeforeControlUsedValue().multiply(new BigDecimal("100"))
                                        .divide(result.getAllControlTime(), 10, BigDecimal.ROUND_HALF_UP));
                    }

                }
            }

            //判定の実施
            if (result.getAfterControlUsedValue() == null || result.getProposalUsedValue() == null) {
                result.setJudge(null);
            } else {
                result.setJudge(result.getAfterControlUsedValue().compareTo(result.getProposalUsedValue()) <= 0);
            }

        }

        return resultList;

    }

    /**
     * 機器制御スケジュール履歴を機器制御スケジュール履歴IDでソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlScheduleLogListDetailResultData> sortScheduleLogList(
            List<SmControlScheduleLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator.comparing(
                                SmControlScheduleLogListDetailResultData::getSmControlScheduleLogId,
                                Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator.comparing(
                                SmControlScheduleLogListDetailResultData::getSmControlScheduleLogId,
                                Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 機器制御スケジュール設定履歴を機器制御スケジュール履歴ID、制御負荷、対象月でソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlScheduleSetLogListDetailResultData> sortScheduleSetLogList(
            List<SmControlScheduleSetLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleSetLogListDetailResultData::getSmControlScheduleLogId,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleSetLogListDetailResultData::getControlLoad,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleSetLogListDetailResultData::getTargetMonth,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleSetLogListDetailResultData::getSmControlScheduleLogId,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleSetLogListDetailResultData::getControlLoad,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleSetLogListDetailResultData::getTargetMonth,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 機器スケジュール時間帯履歴を機器制御スケジュール履歴ID、パターン番号、時間帯番号でソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlScheduleTimeLogListDetailResultData> sortScheduleTimeLogList(
            List<SmControlScheduleTimeLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleTimeLogListDetailResultData::getSmControlScheduleLogId,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getPatternNo,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getTimeSlotNo,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleTimeLogListDetailResultData::getSmControlScheduleLogId,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getPatternNo,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getTimeSlotNo,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 機器スケジュール時間帯履歴を機器制御開始時間（時）、開始時間（分）、終了時間（時）、終了時間（分）でソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlScheduleTimeLogListDetailResultData> sortScheduleTimeLogListByTime(
            List<SmControlScheduleTimeLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeHour,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeMinute,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeHour,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeMinute,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeHour,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeMinute,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeHour,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeMinute,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 機器制御祝日設定履歴を機器制御祝日設定履歴IDでソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlHolidayLogListDetailResultData> sortHolidayLogList(
            List<SmControlHolidayLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator.comparing(SmControlHolidayLogListDetailResultData::getSmControlHolidayLogId,
                                Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator.comparing(SmControlHolidayLogListDetailResultData::getSmControlHolidayLogId,
                                Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * 機器制御祝日設定カレンダ履歴を機器制御祝日設定履歴ID、祝日月日でソートする
     * @param dataList
     * @param sortOrder 0：昇順 1：降順
     * @return
     */
    public static final List<SmControlHolidayCalLogListDetailResultData> sortHolidayCalLogList(
            List<SmControlHolidayCalLogListDetailResultData> dataList, String sortOrder) {
        if (dataList == null || dataList.isEmpty()) {
            return dataList;
        } else {
            if (ApiCodeValueConstants.SORT_ORDER.ASC.getVal().equals(sortOrder)) {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlHolidayCalLogListDetailResultData::getSmControlHolidayLogId,
                                        Comparator.naturalOrder())
                                .thenComparing(SmControlHolidayCalLogListDetailResultData::getHolidayMmDd,
                                        Comparator.naturalOrder()))
                        .collect(Collectors.toList());
            } else {
                return dataList.stream()
                        .sorted(Comparator
                                .comparing(SmControlHolidayCalLogListDetailResultData::getSmControlHolidayLogId,
                                        Comparator.reverseOrder())
                                .thenComparing(SmControlHolidayCalLogListDetailResultData::getHolidayMmDd,
                                        Comparator.reverseOrder()))
                        .collect(Collectors.toList());
            }
        }
    }

    /**
     * スケジュール情報のTimeTableを作成する
     * @param scheduleResult
     * @return
     */
    public static final LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleTimeTable(
            LinkedHashSet<CommonScheduleResult> scheduleResult) {

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (scheduleResult == null || scheduleResult.isEmpty()) {
            return resultSet;
        }

        //制御負荷、開始時間順にソートする
        scheduleResult = scheduleResult.stream()
                .sorted(Comparator.comparing(CommonScheduleResult::getControlLoad, Comparator.naturalOrder())
                        .thenComparing(CommonScheduleResult::getStartTime, Comparator.naturalOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (CommonScheduleResult schedule : scheduleResult) {
            CommonDemandControlTimeTableResult startResult = new CommonDemandControlTimeTableResult();
            startResult.setControlLoad(schedule.getControlLoad().intValue());
            startResult.setMeasurementDate(schedule.getStartTime());
            startResult.setJigenNo(getJigenNoForDemand(
                    DateUtility.changeDateFormat(schedule.getStartTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
            startResult.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
            CommonDemandControlTimeTableResult endResult = new CommonDemandControlTimeTableResult();
            endResult.setControlLoad(schedule.getControlLoad().intValue());
            endResult.setMeasurementDate(schedule.getEndTime());
            endResult.setJigenNo(getJigenNoForDemand(
                    DateUtility.changeDateFormat(schedule.getEndTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
            endResult.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
            resultSet.add(startResult);
            resultSet.add(endResult);
        }

        return resultSet;
    }

//    public static LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleControlTimeTableSet(
//            HashMap<BigDecimal, LinkedHashSet<CommonScheduleResult>> scheduleSet,
//            LinkedHashSet<CommonDemandControlTimeTableResult> controlLoadTimeTableSet,
//            Timestamp settingUpdateDateTimeTo, Boolean eventFlg) {
//
//        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();
//
//        if (controlLoadTimeTableSet == null || controlLoadTimeTableSet.isEmpty()) {
//            //制御情報がない場合は、終了
//            return resultSet;
//        }
//
//        if (scheduleSet == null || scheduleSet.isEmpty()) {
//            //スケジュール情報がない場合は、制御情報をそのまま返却する
//            return controlLoadTimeTableSet;
//        }
//
//        //制御情報を制御負荷、制御時間順にソートする
//        controlLoadTimeTableSet = controlLoadTimeTableSet.stream()
//                .sorted(Comparator
//                        .comparing(CommonDemandControlTimeTableResult::getControlLoad, Comparator.naturalOrder())
//                        .thenComparing(CommonDemandControlTimeTableResult::getMeasurementDate,
//                                Comparator.naturalOrder()))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//
//        Integer beforeControlLoad = null;
//        Integer currentControlLoad = null;
//        String beforeControlLoadStatus = null;
//        String currentControlLoadStatus = null;
//        List<Date> controlLoadCutTimeList = null;
//
//        //制御情報を順に処理する
//        for (CommonDemandControlTimeTableResult controlLoadTimeTable : controlLoadTimeTableSet) {
//            currentControlLoad = controlLoadTimeTable.getControlLoad();
//            currentControlLoadStatus = controlLoadTimeTable.getControlStatus();
//            if (beforeControlLoad != null && !beforeControlLoad.equals(currentControlLoad)
//                    && ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlLoadStatus)) {
//                //制御負荷が変更となり、前の情報が遮断の場合
//                LinkedHashSet<CommonDemandControlTimeTableResult> newResult = createScheduleControlLoadMergeTable2(
//                        controlLoadCutTimeList, new Date(settingUpdateDateTimeTo.getTime()), scheduleSet,
//                        beforeControlLoad, eventFlg);
//                if (newResult != null && !newResult.isEmpty()) {
//                    resultSet.addAll(newResult);
//                }
//
//                beforeControlLoad = null;
//                beforeControlLoadStatus = null;
//                if (controlLoadCutTimeList != null) {
//                    controlLoadCutTimeList.clear();
//                }
//            }
//
//            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(currentControlLoadStatus)) {
//                //遮断の場合
//                if (controlLoadCutTimeList == null) {
//                    controlLoadCutTimeList = new ArrayList<>();
//                }
//                controlLoadCutTimeList.add(controlLoadTimeTable.getMeasurementDate());
//            } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(currentControlLoadStatus)) {
//                //開放の場合
//                if (CheckUtility.isNullOrEmpty(beforeControlLoadStatus)
//                        || ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlLoadStatus)) {
//                    //1件目または開放が続いている場合は、開放の情報をセット
//                    resultSet.add(controlLoadTimeTable);
//                } else {
//                    //遮断から開放された場合、スケジュールとの突合せ
//                    LinkedHashSet<CommonDemandControlTimeTableResult> mergeResult = createScheduleControlLoadMergeTable2(
//                            controlLoadCutTimeList, controlLoadTimeTable.getMeasurementDate(), scheduleSet,
//                            controlLoadTimeTable.getControlLoad(), eventFlg);
//                    if (mergeResult != null && !mergeResult.isEmpty()) {
//                        resultSet.addAll(mergeResult);
//                    }
//                    if (controlLoadCutTimeList != null) {
//                        controlLoadCutTimeList.clear();
//                    }
//                }
//            }
//
//            beforeControlLoad = currentControlLoad;
//            beforeControlLoadStatus = currentControlLoadStatus;
//
//        }
//
//        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlLoadStatus)) {
//            //最終レコードが遮断で終了している場合
//            LinkedHashSet<CommonDemandControlTimeTableResult> lastResult = createScheduleControlLoadMergeTable2(
//                    controlLoadCutTimeList, new Date(settingUpdateDateTimeTo.getTime()), scheduleSet, beforeControlLoad,
//                    eventFlg);
//            if (lastResult != null && !lastResult.isEmpty()) {
//                resultSet.addAll(lastResult);
//            }
//        }
//
//        return resultSet;
//    }

    /**
     * 制御情報からスケジュール情報を除いたTimeTableSetを作成する
     * @param scheduleSet
     * @param controlLoadSet
     * @param settingUpdateDateTimeTo
     * @param eventFlg;
     * @return
     */
    public static LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleControlTimeTableSet(
            LinkedHashSet<CommonScheduleResult> scheduleSet,
            LinkedHashSet<CommonDemandControlTimeTableResult> controlLoadTimeTableSet,
            Timestamp settingUpdateDateTimeTo, Boolean eventFlg) {

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadTimeTableSet == null || controlLoadTimeTableSet.isEmpty()) {
            //制御情報がない場合は、終了
            return resultSet;
        }

        if (scheduleSet == null || scheduleSet.isEmpty()) {
            //スケジュール情報がない場合は、制御情報をそのまま返却する
            return controlLoadTimeTableSet;
        }

        //制御情報を制御負荷、制御時間順にソートする
        controlLoadTimeTableSet = controlLoadTimeTableSet.stream()
                .sorted(Comparator
                        .comparing(CommonDemandControlTimeTableResult::getControlLoad, Comparator.naturalOrder())
                        .thenComparing(CommonDemandControlTimeTableResult::getMeasurementDate,
                                Comparator.naturalOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Integer beforeControlLoad = null;
        Integer currentControlLoad = null;
        String beforeControlLoadStatus = null;
        String currentControlLoadStatus = null;
        List<Date> controlLoadCutTimeList = null;

        //制御情報を順に処理する
        for (CommonDemandControlTimeTableResult controlLoadTimeTable : controlLoadTimeTableSet) {
            currentControlLoad = controlLoadTimeTable.getControlLoad();
            currentControlLoadStatus = controlLoadTimeTable.getControlStatus();
            if (beforeControlLoad != null && !beforeControlLoad.equals(currentControlLoad)
                    && ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlLoadStatus)) {
                //制御負荷が変更となり、前の情報が遮断の場合
                LinkedHashSet<CommonDemandControlTimeTableResult> newResult = createScheduleControlLoadMergeTable(
                        controlLoadCutTimeList, new Date(settingUpdateDateTimeTo.getTime()), scheduleSet,
                        beforeControlLoad, eventFlg);
                if (newResult != null && !newResult.isEmpty()) {
                    resultSet.addAll(newResult);
                }

                beforeControlLoad = null;
                beforeControlLoadStatus = null;
                if (controlLoadCutTimeList != null) {
                    controlLoadCutTimeList.clear();
                }
            }

            if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(currentControlLoadStatus)) {
                //遮断の場合
                if (controlLoadCutTimeList == null) {
                    controlLoadCutTimeList = new ArrayList<>();
                }
                controlLoadCutTimeList.add(controlLoadTimeTable.getMeasurementDate());
            } else if (ApiSimpleConstants.LOAD_CONTROL_FREE.equals(currentControlLoadStatus)) {
                //開放の場合
                if (CheckUtility.isNullOrEmpty(beforeControlLoadStatus)
                        || ApiSimpleConstants.LOAD_CONTROL_FREE.equals(beforeControlLoadStatus)) {
                    //1件目または開放が続いている場合は、開放の情報をセット
                    resultSet.add(controlLoadTimeTable);
                } else {
                    //遮断から開放された場合、スケジュールとの突合せ
                    LinkedHashSet<CommonDemandControlTimeTableResult> mergeResult = createScheduleControlLoadMergeTable(
                            controlLoadCutTimeList, controlLoadTimeTable.getMeasurementDate(), scheduleSet,
                            controlLoadTimeTable.getControlLoad(), eventFlg);
                    if (mergeResult != null && !mergeResult.isEmpty()) {
                        resultSet.addAll(mergeResult);
                    }
                    if (controlLoadCutTimeList != null) {
                        controlLoadCutTimeList.clear();
                    }
                }
            }

            beforeControlLoad = currentControlLoad;
            beforeControlLoadStatus = currentControlLoadStatus;

        }

        if (ApiSimpleConstants.LOAD_CONTROL_CUT.equals(beforeControlLoadStatus)) {
            //最終レコードが遮断で終了している場合
            LinkedHashSet<CommonDemandControlTimeTableResult> lastResult = createScheduleControlLoadMergeTable(
                    controlLoadCutTimeList, new Date(settingUpdateDateTimeTo.getTime()), scheduleSet, beforeControlLoad,
                    eventFlg);
            if (lastResult != null && !lastResult.isEmpty()) {
                resultSet.addAll(lastResult);
            }
        }

        return resultSet;
    }

    /**
     * スケジュールとの突合せをしたTimeTableSetを作成する
     * @param controlLoadCutTimeList
     * @param endCutTime
     * @param scheduleSet
     * @param controlLoad
     * @param eventFlg
     * @return
     */
//    private static LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleControlLoadMergeTable2(
//            List<Date> controlLoadCutTimeList, Date endCutTime,
//            HashMap<BigDecimal, LinkedHashSet<CommonScheduleResult>> scheduleSet,
//            Integer controlLoad, Boolean eventFlg) {
//
//        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();
//
//        if (controlLoadCutTimeList == null || controlLoadCutTimeList.isEmpty()) {
//            return resultSet;
//        }
//
//        Date startCutTime = controlLoadCutTimeList.get(0);
//        //該当期間に該当するスケジュール情報を取得する
//        LinkedHashSet<CommonScheduleResult> filterSchedule = new LinkedHashSet<>();
//        LinkedHashSet<CommonScheduleResult> filterScheduleControlLoad = new LinkedHashSet<>();
//        LinkedHashSet<CommonScheduleResult> filterScheduleStep1 = new LinkedHashSet<>();
//        LinkedHashSet<CommonScheduleResult> filterScheduleStep2 = new LinkedHashSet<>();
//        LinkedHashSet<CommonScheduleResult> filterScheduleStep3 = new LinkedHashSet<>();
//
//        //まず、制御負荷でフィルタ
//        if (scheduleSet != null && !scheduleSet.isEmpty()) {
//            filterScheduleControlLoad = scheduleSet.get(BigDecimal.valueOf(controlLoad));
//            if (filterScheduleControlLoad != null && !filterScheduleControlLoad.isEmpty()) {
//                //次の条件①～③でそれぞれフィルタ
//                //条件① 遮断開始 < スケジュール終了 <= 遮断終了
//                filterScheduleStep1 = filterScheduleControlLoad.stream()
//                        .filter(i -> startCutTime.before(i.getEndTime()) && i.getEndTime().compareTo(endCutTime) <= 0)
//                        .collect(Collectors.toCollection(LinkedHashSet::new));
//                if (filterScheduleStep1 != null && !filterScheduleStep1.isEmpty()) {
//                    filterSchedule.addAll(filterScheduleStep1);
//                }
//                //条件② 遮断開始 <= スケジュール開始　< 遮断終了
//                filterScheduleStep2 = filterScheduleControlLoad.stream()
//                        .filter(i -> startCutTime.compareTo(i.getStartTime()) <= 0
//                                && i.getStartTime().before(endCutTime))
//                        .collect(Collectors.toCollection(LinkedHashSet::new));
//                if (filterScheduleStep2 != null && !filterScheduleStep2.isEmpty()) {
//                    filterSchedule.addAll(filterScheduleStep2);
//                }
//                //条件③ 遮断開始 > スケジュール開始 and 遮断終了 < スケジュール終了
//                filterScheduleStep3 = filterScheduleControlLoad.stream()
//                        .filter(i -> startCutTime.after(i.getStartTime()) && endCutTime.before(i.getEndTime()))
//                        .collect(Collectors.toCollection(LinkedHashSet::new));
//                if (filterScheduleStep3 != null && !filterScheduleStep3.isEmpty()) {
//                    filterSchedule.addAll(filterScheduleStep3);
//                }
//            }
//        }
//
//        if (filterSchedule == null || filterSchedule.isEmpty()) {
//            //スケジュールが該当しない場合は、遮断開始時間と遮断終了時間をそれぞれセットする
//            //遮断情報
//            for (Date cutTime : controlLoadCutTimeList) {
//                CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
//                cutInfo.setControlLoad(controlLoad);
//                cutInfo.setMeasurementDate(cutTime);
//                if (eventFlg) {
//                    cutInfo.setJigenNo(getJigenNoForEvent(
//                            DateUtility.changeDateFormat(cutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                } else {
//                    cutInfo.setJigenNo(getJigenNoForDemand(
//                            DateUtility.changeDateFormat(cutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                }
//                cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                resultSet.add(cutInfo);
//            }
//            //開放情報
//            CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
//            freeInfo.setControlLoad(controlLoad);
//            freeInfo.setMeasurementDate(endCutTime);
//            if (eventFlg) {
//                freeInfo.setJigenNo(getJigenNoForEvent(
//                        DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//            } else {
//                freeInfo.setJigenNo(getJigenNoForDemand(
//                        DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//            }
//            freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
//            resultSet.add(freeInfo);
//        } else {
//            //いったん、開始時間順にソートする
//            filterSchedule = filterSchedule.stream()
//                    .sorted(Comparator.comparing(CommonScheduleResult::getStartTime, Comparator.naturalOrder()))
//                    .collect(Collectors.toCollection(LinkedHashSet::new));
//            Iterator<CommonScheduleResult> iterator = filterSchedule.iterator();
//            Date beforeEndTime = null;
//            while (iterator.hasNext()) {
//                CommonScheduleResult currentSchedule = iterator.next();
//                if (resultSet == null || resultSet.isEmpty()) {
//                    //1件目の場合
//                    beforeEndTime = null;
//                    if (currentSchedule.getStartTime().after(startCutTime)) {
//                        //スケジュール開始 > 遮断開始の場合、遮断開始～スケジュール開始までのデータを作成する
//                        //遮断開始
//                        CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
//                        cutInfo.setControlLoad(controlLoad);
//                        cutInfo.setMeasurementDate(startCutTime);
//                        if (eventFlg) {
//                            cutInfo.setJigenNo(getJigenNoForEvent(
//                                    DateUtility.changeDateFormat(startCutTime,
//                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                        } else {
//                            cutInfo.setJigenNo(getJigenNoForDemand(
//                                    DateUtility.changeDateFormat(startCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                        }
//                        cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                        resultSet.add(cutInfo);
//                        //スケジュール開始
//                        CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
//                        freeInfo.setControlLoad(controlLoad);
//                        freeInfo.setMeasurementDate(currentSchedule.getStartTime());
//                        freeInfo.setJigenNo(getJigenNoForDemand(
//                                DateUtility.changeDateFormat(currentSchedule.getStartTime(),
//                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                        freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
//                        resultSet.add(freeInfo);
//                        beforeEndTime = currentSchedule.getEndTime();
//                        if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
//                            //期間内に遮断情報が存在する場合
//                            for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
//                                //2件目から順に精査
//                                if (startCutTime.before(controlLoadCutTimeList.get(i))
//                                        && currentSchedule.getStartTime().after(controlLoadCutTimeList.get(i))) {
//                                    //開始と終了の間にある場合、遮断情報に追加
//                                    CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
//                                    addCutInfo.setControlLoad(controlLoad);
//                                    addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
//                                    if (eventFlg) {
//                                        addCutInfo.setJigenNo(getJigenNoForEvent(
//                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                                    } else {
//                                        addCutInfo.setJigenNo(getJigenNoForDemand(
//                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                                    }
//                                    addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                                    resultSet.add(addCutInfo);
//                                }
//                            }
//                        }
//                    }
//
//                } else {
//                    //2件目以降の場合、1つ前のスケジュール終了と現在のスケジュール開始までのデータを作成する
//                    //1つ前のスケジュール終了
//                    CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
//                    cutInfo.setControlLoad(controlLoad);
//                    cutInfo.setMeasurementDate(beforeEndTime);
//                    cutInfo.setJigenNo(getJigenNoForDemand(
//                            DateUtility.changeDateFormat(beforeEndTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                    cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                    resultSet.add(cutInfo);
//                    //現在のスケジュール開始
//                    CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
//                    freeInfo.setControlLoad(controlLoad);
//                    freeInfo.setMeasurementDate(currentSchedule.getStartTime());
//                    freeInfo.setJigenNo(getJigenNoForDemand(
//                            DateUtility.changeDateFormat(currentSchedule.getStartTime(),
//                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                    freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
//                    resultSet.add(freeInfo);
//                    beforeEndTime = currentSchedule.getEndTime();
//                    if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
//                        //期間内に遮断情報が存在する場合
//                        for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
//                            //2件目から順に精査
//                            if (beforeEndTime.before(controlLoadCutTimeList.get(i))
//                                    && currentSchedule.getStartTime().after(controlLoadCutTimeList.get(i))) {
//                                //開始と終了の間にある場合、遮断情報に追加
//                                CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
//                                addCutInfo.setControlLoad(controlLoad);
//                                addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
//                                if (eventFlg) {
//                                    addCutInfo.setJigenNo(getJigenNoForEvent(
//                                            DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                                } else {
//                                    addCutInfo.setJigenNo(getJigenNoForDemand(
//                                            DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                                }
//                                addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                                resultSet.add(addCutInfo);
//                            }
//                        }
//                    }
//                }
//                if (!iterator.hasNext()) {
//                    //最終レコードの場合
//                    if (currentSchedule.getEndTime().before(endCutTime)) {
//                        //スケジュール終了 < 遮断終了の場合、スケジュール終了～遮断終了のデータを作成する
//                        //スケジュール終了
//                        CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
//                        cutInfo.setControlLoad(controlLoad);
//                        cutInfo.setMeasurementDate(currentSchedule.getEndTime());
//                        cutInfo.setJigenNo(getJigenNoForDemand(
//                                DateUtility.changeDateFormat(currentSchedule.getEndTime(),
//                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                        cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                        resultSet.add(cutInfo);
//                        //遮断終了
//                        CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
//                        freeInfo.setControlLoad(controlLoad);
//                        freeInfo.setMeasurementDate(endCutTime);
//                        if (eventFlg) {
//                            freeInfo.setJigenNo(getJigenNoForEvent(
//                                    DateUtility.changeDateFormat(endCutTime,
//                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                        } else {
//                            freeInfo.setJigenNo(getJigenNoForDemand(
//                                    DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                        }
//                        freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
//                        resultSet.add(freeInfo);
//                        if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
//                            //期間内に遮断情報が存在する場合
//                            for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
//                                //2件目から順に精査
//                                if (currentSchedule.getEndTime().before(controlLoadCutTimeList.get(i))
//                                        && endCutTime.after(controlLoadCutTimeList.get(i))) {
//                                    //開始と終了の間にある場合、遮断情報に追加
//                                    CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
//                                    addCutInfo.setControlLoad(controlLoad);
//                                    addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
//                                    if (eventFlg) {
//                                        addCutInfo.setJigenNo(getJigenNoForEvent(
//                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
//                                    } else {
//                                        addCutInfo.setJigenNo(getJigenNoForDemand(
//                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
//                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
//                                    }
//                                    addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
//                                    resultSet.add(addCutInfo);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (resultSet != null || !resultSet.isEmpty()) {
//            //制御時間でソート
//            resultSet = resultSet.stream().sorted(Comparator
//                    .comparing(CommonDemandControlTimeTableResult::getMeasurementDate, Comparator.naturalOrder()))
//                    .collect(Collectors.toCollection(LinkedHashSet::new));
//        }
//
//        return resultSet;
//    }

    /**
     * スケジュールとの突合せをしたTimeTableSetを作成する
     * @param controlLoadCutTimeList
     * @param endCutTime
     * @param scheduleSet
     * @param controlLoad
     * @param eventFlg
     * @return
     */
    private static LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleControlLoadMergeTable(
            List<Date> controlLoadCutTimeList, Date endCutTime, LinkedHashSet<CommonScheduleResult> scheduleSet,
            Integer controlLoad, Boolean eventFlg) {

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        if (controlLoadCutTimeList == null || controlLoadCutTimeList.isEmpty()) {
            return resultSet;
        }

        Date startCutTime = controlLoadCutTimeList.get(0);
        Date currentStartCutTime = controlLoadCutTimeList.get(0);
        //該当期間に該当するスケジュール情報を取得する
        LinkedHashSet<CommonScheduleResult> filterSchedule = new LinkedHashSet<>();
        LinkedHashSet<CommonScheduleResult> filterScheduleControlLoad = new LinkedHashSet<>();
        LinkedHashSet<CommonScheduleResult> filterScheduleStep1 = new LinkedHashSet<>();
        LinkedHashSet<CommonScheduleResult> filterScheduleStep2 = new LinkedHashSet<>();
        LinkedHashSet<CommonScheduleResult> filterScheduleStep3 = new LinkedHashSet<>();

        //まず、制御負荷でフィルタ
        if (scheduleSet != null && !scheduleSet.isEmpty()) {
            filterScheduleControlLoad = scheduleSet.stream()
                    .filter(i -> controlLoad.equals(i.getControlLoad().intValue()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (filterScheduleControlLoad != null && !filterScheduleControlLoad.isEmpty()) {
                //次の条件①～③でそれぞれフィルタ
                //条件① 遮断開始 < スケジュール終了 <= 遮断終了
                filterScheduleStep1 = filterScheduleControlLoad.stream()
                        .filter(i -> startCutTime.before(i.getEndTime()) && i.getEndTime().compareTo(endCutTime) <= 0)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                if (filterScheduleStep1 != null && !filterScheduleStep1.isEmpty()) {
                    filterSchedule.addAll(filterScheduleStep1);
                }
                //条件② 遮断開始 <= スケジュール開始　< 遮断終了
                filterScheduleStep2 = filterScheduleControlLoad.stream()
                        .filter(i -> startCutTime.compareTo(i.getStartTime()) <= 0
                                && i.getStartTime().before(endCutTime))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                if (filterScheduleStep2 != null && !filterScheduleStep2.isEmpty()) {
                    filterSchedule.addAll(filterScheduleStep2);
                }
                //条件③ 遮断開始 > スケジュール開始 and 遮断終了 < スケジュール終了
                filterScheduleStep3 = filterScheduleControlLoad.stream()
                        .filter(i -> startCutTime.after(i.getStartTime()) && endCutTime.before(i.getEndTime()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                if (filterScheduleStep3 != null && !filterScheduleStep3.isEmpty()) {
                    filterSchedule.addAll(filterScheduleStep3);
                }
            }
        }

        if (filterSchedule == null || filterSchedule.isEmpty()) {
            //スケジュールが該当しない場合は、遮断開始時間と遮断終了時間をそれぞれセットする
            //遮断情報
            for (Date cutTime : controlLoadCutTimeList) {
                CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
                cutInfo.setControlLoad(controlLoad);
                cutInfo.setMeasurementDate(cutTime);
                if (eventFlg) {
                    cutInfo.setJigenNo(getJigenNoForEvent(
                            DateUtility.changeDateFormat(cutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                } else {
                    cutInfo.setJigenNo(getJigenNoForDemand(
                            DateUtility.changeDateFormat(cutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                }
                cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                resultSet.add(cutInfo);
            }
            //開放情報
            CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
            freeInfo.setControlLoad(controlLoad);
            freeInfo.setMeasurementDate(endCutTime);
            if (eventFlg) {
                freeInfo.setJigenNo(getJigenNoForEvent(
                        DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
            } else {
                freeInfo.setJigenNo(getJigenNoForDemand(
                        DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
            }
            freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
            resultSet.add(freeInfo);
        } else {
            //いったん、開始時間順にソートする
            filterSchedule = filterSchedule.stream()
                    .sorted(Comparator.comparing(CommonScheduleResult::getStartTime, Comparator.naturalOrder()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            Iterator<CommonScheduleResult> iterator = filterSchedule.iterator();
            Date beforeEndTime = null;
            while (iterator.hasNext()) {
                CommonScheduleResult currentSchedule = iterator.next();
                if (resultSet == null || resultSet.isEmpty()) {
                    //1件目の場合
                    beforeEndTime = null;
                    if (currentSchedule.getStartTime().after(currentStartCutTime)) {
                        //スケジュール開始 > 遮断開始の場合、遮断開始～スケジュール開始までのデータを作成する
                        //遮断開始
                        CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
                        cutInfo.setControlLoad(controlLoad);
                        cutInfo.setMeasurementDate(currentStartCutTime);
                        if (eventFlg) {
                            cutInfo.setJigenNo(getJigenNoForEvent(
                                    DateUtility.changeDateFormat(currentStartCutTime,
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                        } else {
                            cutInfo.setJigenNo(getJigenNoForDemand(
                                    DateUtility.changeDateFormat(currentStartCutTime,
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                        }
                        cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                        resultSet.add(cutInfo);
                        //スケジュール開始
                        CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
                        freeInfo.setControlLoad(controlLoad);
                        freeInfo.setMeasurementDate(currentSchedule.getStartTime());
                        freeInfo.setJigenNo(getJigenNoForDemand(
                                DateUtility.changeDateFormat(currentSchedule.getStartTime(),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                        freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
                        resultSet.add(freeInfo);
                        beforeEndTime = currentSchedule.getEndTime();
                        if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
                            //期間内に遮断情報が存在する場合
                            for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
                                //2件目から順に精査
                                if (startCutTime.before(controlLoadCutTimeList.get(i))
                                        && currentSchedule.getStartTime().after(controlLoadCutTimeList.get(i))) {
                                    //開始と終了の間にある場合、遮断情報に追加
                                    CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
                                    addCutInfo.setControlLoad(controlLoad);
                                    addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
                                    if (eventFlg) {
                                        addCutInfo.setJigenNo(getJigenNoForEvent(
                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                                    } else {
                                        addCutInfo.setJigenNo(getJigenNoForDemand(
                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                                    }
                                    addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                                    resultSet.add(addCutInfo);
                                }
                            }
                        }
                    } else {
                        //まれにスケジュール開始 <= 遮断開始がある。 スケジュール終了 < 遮断終了の場合、遮断開始をスケジュール終了にする
                        if ((currentSchedule.getStartTime().before(currentStartCutTime)
                                || currentSchedule.getStartTime().equals(currentStartCutTime))
                                && currentSchedule.getEndTime().before(endCutTime)) {
                            currentStartCutTime = currentSchedule.getEndTime();
                        }
                    }

                } else {
                    //2件目以降の場合、1つ前のスケジュール終了と現在のスケジュール開始までのデータを作成する
                    //1つ前のスケジュール終了
                    CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
                    cutInfo.setControlLoad(controlLoad);
                    cutInfo.setMeasurementDate(beforeEndTime);
                    cutInfo.setJigenNo(getJigenNoForDemand(
                            DateUtility.changeDateFormat(beforeEndTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                    cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                    resultSet.add(cutInfo);
                    //現在のスケジュール開始
                    CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
                    freeInfo.setControlLoad(controlLoad);
                    freeInfo.setMeasurementDate(currentSchedule.getStartTime());
                    freeInfo.setJigenNo(getJigenNoForDemand(
                            DateUtility.changeDateFormat(currentSchedule.getStartTime(),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                    freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
                    resultSet.add(freeInfo);
                    beforeEndTime = currentSchedule.getEndTime();
                    if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
                        //期間内に遮断情報が存在する場合
                        for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
                            //2件目から順に精査
                            if (beforeEndTime.before(controlLoadCutTimeList.get(i))
                                    && currentSchedule.getStartTime().after(controlLoadCutTimeList.get(i))) {
                                //開始と終了の間にある場合、遮断情報に追加
                                CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
                                addCutInfo.setControlLoad(controlLoad);
                                addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
                                if (eventFlg) {
                                    addCutInfo.setJigenNo(getJigenNoForEvent(
                                            DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                                } else {
                                    addCutInfo.setJigenNo(getJigenNoForDemand(
                                            DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                                }
                                addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                                resultSet.add(addCutInfo);
                            }
                        }
                    }
                }
                if (!iterator.hasNext()) {
                    //最終レコードの場合
                    if (currentSchedule.getEndTime().before(endCutTime)) {
                        //スケジュール終了 < 遮断終了の場合、スケジュール終了～遮断終了のデータを作成する
                        //スケジュール終了
                        CommonDemandControlTimeTableResult cutInfo = new CommonDemandControlTimeTableResult();
                        cutInfo.setControlLoad(controlLoad);
                        cutInfo.setMeasurementDate(currentSchedule.getEndTime());
                        cutInfo.setJigenNo(getJigenNoForDemand(
                                DateUtility.changeDateFormat(currentSchedule.getEndTime(),
                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                        cutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                        resultSet.add(cutInfo);
                        //遮断終了
                        CommonDemandControlTimeTableResult freeInfo = new CommonDemandControlTimeTableResult();
                        freeInfo.setControlLoad(controlLoad);
                        freeInfo.setMeasurementDate(endCutTime);
                        if (eventFlg) {
                            freeInfo.setJigenNo(getJigenNoForEvent(
                                    DateUtility.changeDateFormat(endCutTime,
                                            DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                        } else {
                            freeInfo.setJigenNo(getJigenNoForDemand(
                                    DateUtility.changeDateFormat(endCutTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                        }
                        freeInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);
                        resultSet.add(freeInfo);
                        if (controlLoadCutTimeList != null && controlLoadCutTimeList.size() >= 2) {
                            //期間内に遮断情報が存在する場合
                            for (int i = 1; i <= controlLoadCutTimeList.size() - 1; i++) {
                                //2件目から順に精査
                                if (currentSchedule.getEndTime().before(controlLoadCutTimeList.get(i))
                                        && endCutTime.after(controlLoadCutTimeList.get(i))) {
                                    //開始と終了の間にある場合、遮断情報に追加
                                    CommonDemandControlTimeTableResult addCutInfo = new CommonDemandControlTimeTableResult();
                                    addCutInfo.setControlLoad(controlLoad);
                                    addCutInfo.setMeasurementDate(controlLoadCutTimeList.get(i));
                                    if (eventFlg) {
                                        addCutInfo.setJigenNo(getJigenNoForEvent(
                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)));
                                    } else {
                                        addCutInfo.setJigenNo(getJigenNoForDemand(
                                                DateUtility.changeDateFormat(controlLoadCutTimeList.get(i),
                                                        DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
                                    }
                                    addCutInfo.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);
                                    resultSet.add(addCutInfo);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (resultSet != null || !resultSet.isEmpty()) {
            //制御時間でソート
            resultSet = resultSet.stream().sorted(Comparator
                    .comparing(CommonDemandControlTimeTableResult::getMeasurementDate, Comparator.naturalOrder()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return resultSet;
    }

    public static final HashMap<BigDecimal, LinkedHashSet<CommonScheduleResult>> createScheduleMap(
            LinkedHashSet<CommonScheduleResult> scheduleSet, Integer controlLoadCount) {

        HashMap<BigDecimal, LinkedHashSet<CommonScheduleResult>> resultMap = new HashMap<>();

        //空のセットを作成する
        for (int i = 1; i <= controlLoadCount; i++) {
            resultMap.put(BigDecimal.valueOf(i), new LinkedHashSet<>());
        }

        for (CommonScheduleResult schedule : scheduleSet) {
            LinkedHashSet<CommonScheduleResult> temp = resultMap.get(schedule.getControlLoad());
            temp.add(schedule);
            resultMap.put(schedule.getControlLoad(), temp);
        }

        return resultMap;
    }

    /**
     * スケジュール時間帯履歴の情報を電気が使える時間帯から電気が使えない時間帯に変換する
     * @param scheduleTimeLogList
     * @return
     */
    public static final List<SmControlScheduleTimeLogListDetailResultData> createTimeSchedule(
            List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList) {

        List<SmControlScheduleTimeLogListDetailResultData> resultList = new ArrayList<>();

        if (scheduleTimeLogList == null || scheduleTimeLogList.isEmpty()) {
            return scheduleTimeLogList;
        }

        Long beforeSmControlScheduleLogId = null;
        String beforePatternNo = null;
        List<SmControlScheduleTimeLogListDetailResultData> tempList = null;
        for (SmControlScheduleTimeLogListDetailResultData scheduleTime : scheduleTimeLogList) {
            if (!checkValidSchedule(scheduleTime)) {
                //無効な時間帯時間帯データの場合、次のレコードへ
                beforeSmControlScheduleLogId = scheduleTime.getSmControlScheduleLogId();
                beforePatternNo = scheduleTime.getPatternNo();
                continue;
            }

            if (tempList == null
                    || (beforeSmControlScheduleLogId != null
                            && !beforeSmControlScheduleLogId.equals(scheduleTime.getSmControlScheduleLogId()))
                    || (beforePatternNo != null && !beforePatternNo.equals(scheduleTime.getPatternNo()))) {
                //tempがnullの場合、スケジュール履歴IDが変更になった場合、パターン番号が変更になった場合
                if (tempList != null && !tempList.isEmpty()) {
                    resultList.addAll(createPatterNoSchedule(tempList));
                }
                tempList = new ArrayList<>();
            }

            tempList.add(scheduleTime);
            beforeSmControlScheduleLogId = scheduleTime.getSmControlScheduleLogId();
            beforePatternNo = scheduleTime.getPatternNo();

        }

        //最後のレコードの処理
        if (tempList != null && !tempList.isEmpty()) {
            resultList.addAll(createPatterNoSchedule(tempList));
        }

        return resultList;
    }

    /**
     * パターン番号単位の電気が使えない時間帯のスケジュールを作成する
     * @param scheduleTimeLogList
     * @return
     */
    private static final List<SmControlScheduleTimeLogListDetailResultData> createPatterNoSchedule(
            List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList) {

        List<SmControlScheduleTimeLogListDetailResultData> resultList = new ArrayList<>();

        if (scheduleTimeLogList == null || scheduleTimeLogList.isEmpty()) {
            return scheduleTimeLogList;
        }

        //開始時間（時）、開始時間（分）、終了時間（時）、終了時間（分）単位でソートする
        scheduleTimeLogList = sortScheduleTimeLogListByTime(scheduleTimeLogList,
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal());

        Integer beforeStartTimeHour = null;
        //Integer beforeStartTimeMinute = null;
        Integer beforeEndTimeHour = null;
        Integer beforeEndTimeMinute = null;
        Integer currentTimeSlotNo = 0;
        for (SmControlScheduleTimeLogListDetailResultData scheduleTimeLog : scheduleTimeLogList) {
            if (beforeStartTimeHour == null) {
                //1件目の場合
                if (!(scheduleTimeLog.getStartTimeHour().equals(0) && scheduleTimeLog.getStartTimeMinute().equals(0))) {
                    //開始が00:00ではない場合、00:00～開始時間までのレコードを作成する
                    currentTimeSlotNo++;
                    resultList.add(new SmControlScheduleTimeLogListDetailResultData(
                            scheduleTimeLog.getSmControlScheduleLogId(), scheduleTimeLog.getSmId(),
                            scheduleTimeLog.getPatternNo(), currentTimeSlotNo, 0, 0, scheduleTimeLog.getStartTimeHour(),
                            scheduleTimeLog.getStartTimeMinute(), 0));
                }
                //レコードの保持
                beforeStartTimeHour = scheduleTimeLog.getStartTimeHour();
                //beforeStartTimeMinute = scheduleTimeLog.getStartTimeMinute();
                beforeEndTimeHour = scheduleTimeLog.getEndTimeHour();
                beforeEndTimeMinute = scheduleTimeLog.getEndTimeMinute();
            } else {
                //2件目以降の場合
                if (scheduleTimeLog.getStartTimeHour().compareTo(beforeEndTimeHour) > 0
                        || (scheduleTimeLog.getStartTimeHour().equals(beforeEndTimeHour)
                                && scheduleTimeLog.getStartTimeMinute().compareTo(beforeEndTimeMinute) > 0)) {
                    //前回の終了時間から開始時間まで空きがある場合、前回の終了時間～今回の開始時間までのレコードを作成する
                    currentTimeSlotNo++;
                    resultList.add(new SmControlScheduleTimeLogListDetailResultData(
                            scheduleTimeLog.getSmControlScheduleLogId(), scheduleTimeLog.getSmId(),
                            scheduleTimeLog.getPatternNo(), currentTimeSlotNo, beforeEndTimeHour, beforeEndTimeMinute,
                            scheduleTimeLog.getStartTimeHour(),
                            scheduleTimeLog.getStartTimeMinute(), 0));
                    //レコードの保持
                    beforeStartTimeHour = scheduleTimeLog.getStartTimeHour();
                    //beforeStartTimeMinute = scheduleTimeLog.getStartTimeMinute();
                    beforeEndTimeHour = scheduleTimeLog.getEndTimeHour();
                    beforeEndTimeMinute = scheduleTimeLog.getEndTimeMinute();
                } else {
                    //空きがない場合、
                    if (scheduleTimeLog.getEndTimeHour().compareTo(beforeEndTimeHour) >= 0
                            || (scheduleTimeLog.getEndTimeHour().equals(beforeEndTimeHour)
                                    && scheduleTimeLog.getEndTimeMinute().compareTo(beforeEndTimeMinute) >= 0)) {
                        //終了時間が前回の終了時間以降の場合、終了時間のみ保持
                        beforeEndTimeHour = scheduleTimeLog.getEndTimeHour();
                        beforeEndTimeMinute = scheduleTimeLog.getEndTimeMinute();
                    }
                }
            }
        }

        if (!(beforeEndTimeHour.equals(24) && beforeEndTimeMinute.equals(0))) {
            //最新の終了時間が24:00ではない場合、前回の終了時間～24:00までのレコードを作成する
            currentTimeSlotNo++;
            resultList.add(new SmControlScheduleTimeLogListDetailResultData(
                    scheduleTimeLogList.get(0).getSmControlScheduleLogId(), scheduleTimeLogList.get(0).getSmId(),
                    scheduleTimeLogList.get(0).getPatternNo(), currentTimeSlotNo, beforeEndTimeHour,
                    beforeEndTimeMinute,
                    24, 0, 0));
        }

        return resultList;
    }

    /**
     * スケジュールの開始/終了のSetを作成する
     * @param scheduleLogList
     * @param scheduleSetLogList
     * @param scheduleTimeLogList
     * @param holidayLogList
     * @param holidayCalLogList
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @return
     */
    public static final LinkedHashSet<CommonScheduleResult> createSchedule(
            List<SmControlScheduleLogListDetailResultData> scheduleLogList,
            List<SmControlScheduleSetLogListDetailResultData> scheduleSetLogList,
            List<SmControlScheduleTimeLogListDetailResultData> scheduleTimeLogList,
            List<SmControlHolidayLogListDetailResultData> holidayLogList,
            List<SmControlHolidayCalLogListDetailResultData> holidayCalLogList,
            Timestamp settingUpdateDateTimeFrom, Timestamp settingUpdateDateTimeTo) {

        LinkedHashSet<CommonScheduleResult> resultSet = new LinkedHashSet<>();
        Map<BigDecimal, LinkedHashSet<CommonScheduleResult>> controlLoadScheduleMap = new LinkedHashMap<>();
        Map<BigDecimal, CommonScheduleResult> beforeScheduleInfoMap = new LinkedHashMap<>();

        //スケジュールが変更されるタイミングの情報を作成する
        LinkedHashSet<CommonSchedulePatternChangeResult> schedulePatternChangeResultSet = createSchedulePatternChangeResult(
                scheduleLogList, holidayLogList, settingUpdateDateTimeFrom, settingUpdateDateTimeTo);

        if (schedulePatternChangeResultSet == null || schedulePatternChangeResultSet.isEmpty()) {
            //スケジュールがないので、終了
            return resultSet;
        }

        //取得したトリガを使って、current/nextの分布を作成する
        LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> currentNextResultSet = createPatternChangeCurrentNextResult(
                schedulePatternChangeResultSet);

        if (currentNextResultSet == null || currentNextResultSet.isEmpty()) {
            return resultSet;
        }

        Date currentDate = null;
        Boolean currentHolidayFlg = null;
        String currentDayOfWeek = null;
        Long currentSmControlScheduleLogId = null;
        Integer currentScheduleManageDesignationFlg = null;
        Long currentSmControlHolidayLogId = null;
        for (CommonSchedulePatternChangeCurrentNextResult current : currentNextResultSet) {

            if (current.getNextSchedulePatternChangeResult() == null) {
                //次のレコードがない場合は最終に置き換える
                current.setNextSchedulePatternChangeResult(
                        new CommonSchedulePatternChangeResult(settingUpdateDateTimeTo, Boolean.TRUE, Boolean.FALSE,
                                Boolean.FALSE, settingUpdateDateTimeTo, null, null, null));
            }

            if (current.getCurrentSchedulePatternChangeResult().getDateChangeFlg()) {
                //日付変更の場合
                currentDate = current.getCurrentSchedulePatternChangeResult().getChangeAfterDate();
                currentDayOfWeek = DateUtility.getDayOfWeekDay(currentDate);
            }
            if (current.getCurrentSchedulePatternChangeResult().getScheduleLogChangeFlg()) {
                //スケジュール変更の場合
                currentSmControlScheduleLogId = current.getCurrentSchedulePatternChangeResult()
                        .getChangeAfterSmControlScheduleLogId();
                currentScheduleManageDesignationFlg = current.getCurrentSchedulePatternChangeResult()
                        .getChangeAfterScheduleManageDesignationFlg();
            }
            if (current.getCurrentSchedulePatternChangeResult().getHolidayLogChangeFlg()) {
                //祝日変更の場合
                currentSmControlHolidayLogId = current.getCurrentSchedulePatternChangeResult()
                        .getChangeAfterSmControlHolidayLogId();
            }

            if (currentSmControlScheduleLogId == null || currentScheduleManageDesignationFlg == null
                    || OsolConstants.FLG_OFF.equals(currentScheduleManageDesignationFlg)) {
                //適用スケジュールがない場合、またはスケジュール管理指定フラグがOFFの場合、次のレコードへ
                continue;
            }

            if (currentSmControlHolidayLogId == null) {
                //適用祝日がない場合、祝日以外
                currentHolidayFlg = Boolean.FALSE;
            } else {
                //祝日判定を行う
                currentHolidayFlg = getHolidayFlg(currentDate, currentSmControlHolidayLogId, holidayCalLogList);
            }

            //制御負荷ごとのパターン番号を取得する
            LinkedHashSet<CommonSchedulePatternNoResult> patternNoSet = getPatternNo(currentDate,
                    currentSmControlScheduleLogId, currentDayOfWeek, currentHolidayFlg, scheduleSetLogList);
            if (patternNoSet == null || patternNoSet.isEmpty()) {
                //パターン番号を取得できない場合は次のレコードへ
                continue;
            } else {
                //制御負荷単位でソートする
                patternNoSet = patternNoSet.stream().sorted(
                        Comparator.comparing(CommonSchedulePatternNoResult::getControlLoad, Comparator.naturalOrder()))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }

            for (CommonSchedulePatternNoResult patternNo : patternNoSet) {
                //Mapの有無を確認する
                LinkedHashSet<CommonScheduleResult> currentControlLoadSchedule = controlLoadScheduleMap
                        .get(patternNo.getControlLoad());
                if (currentControlLoadSchedule == null) {
                    currentControlLoadSchedule = new LinkedHashSet<>();
                }
                //前回、対象の制御負荷で処理をした情報を確認する
                CommonScheduleResult beforeControlLoadSchedule = beforeScheduleInfoMap.get(patternNo.getControlLoad());

                //対象パターン番号の時間帯情報を取得する
                Long baseScheduleId = currentSmControlScheduleLogId;
                List<SmControlScheduleTimeLogListDetailResultData> filterScheduleTimeLogList = scheduleTimeLogList
                        .stream().filter(i -> baseScheduleId.equals(i.getSmControlScheduleLogId())
                                && patternNo.getPatternNo().equals(i.getPatternNo()))
                        .collect(Collectors.toList());
                if (filterScheduleTimeLogList == null || filterScheduleTimeLogList.isEmpty()) {
                    //パターン情報が取得できない場合
                    continue;
                } else {
                    //開始、終了の順でソート
                    filterScheduleTimeLogList = filterScheduleTimeLogList.stream()
                            .sorted(Comparator
                                    .comparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeHour,
                                            Comparator.naturalOrder())
                                    .thenComparing(SmControlScheduleTimeLogListDetailResultData::getStartTimeMinute,
                                            Comparator.naturalOrder())
                                    .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeHour,
                                            Comparator.naturalOrder())
                                    .thenComparing(SmControlScheduleTimeLogListDetailResultData::getEndTimeMinute,
                                            Comparator.naturalOrder()))
                            .collect(Collectors.toList());
                    for (SmControlScheduleTimeLogListDetailResultData filterTime : filterScheduleTimeLogList) {

                        if (!checkValidSchedule(filterTime)) {
                            //無効なデータはスルー
                            continue;
                        }

                        //開始時間、終了時間を日付をつけてセットする
                        Date startDate = DateUtility.conversionDate(
                                DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                                        .concat(String
                                                .format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                        filterTime.getStartTimeHour())
                                                .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                        filterTime.getStartTimeMinute()))),
                                DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                        Date endDate = null;
                        if (filterTime.getEndTimeHour() == 24) {
                            //24時の場合は、翌日の00時として処理をする
                            endDate = DateUtility.conversionDate(
                                    DateUtility
                                            .changeDateFormat(DateUtility.plusDay(currentDate, 1),
                                                    DateUtility.DATE_FORMAT_YYYYMMDD)
                                            .concat("00")
                                            .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                    filterTime.getEndTimeMinute())),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                        } else {
                            endDate = DateUtility.conversionDate(
                                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD)
                                            .concat(String
                                                    .format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            filterTime.getEndTimeHour())
                                                    .concat(String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2,
                                                            filterTime.getEndTimeMinute()))),
                                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
                        }

                        if (endDate.compareTo(
                                new Date(current.getCurrentSchedulePatternChangeResult().getPatternChangeTimestamp()
                                        .getTime())) <= 0
                                || startDate.compareTo(new Date(current.getNextSchedulePatternChangeResult()
                                        .getPatternChangeTimestamp().getTime())) >= 0) {
                            //スケジュール適用期間内にないデータはスルー
                            continue;
                        }
                        if (startDate.before(new Date(current.getCurrentSchedulePatternChangeResult()
                                .getPatternChangeTimestamp().getTime()))) {
                            //開始がスケジュール適用開始よりも前の場合、開始を変更
                            startDate = new Date(current.getCurrentSchedulePatternChangeResult()
                                    .getPatternChangeTimestamp().getTime());
                        }
                        if (endDate.after(new Date(
                                current.getNextSchedulePatternChangeResult().getPatternChangeTimestamp().getTime()))) {
                            //終了がスケジュール適用終了よりも後の場合、終了を変更
                            endDate = new Date(
                                    current.getNextSchedulePatternChangeResult().getPatternChangeTimestamp().getTime());
                        }

                        if (beforeControlLoadSchedule == null) {
                            beforeControlLoadSchedule = new CommonScheduleResult();
                            beforeControlLoadSchedule.setControlLoad(patternNo.getControlLoad());
                            beforeControlLoadSchedule.setStartTime(startDate);
                            beforeControlLoadSchedule.setEndTime(endDate);
                        } else {
                            //前の処理レコードと比較
                            if (beforeControlLoadSchedule.getEndTime().before(startDate)) {
                                //前のレコードの終了 < 現在の開始の場合、beforeをsetにつめ、beforeを作り直し
                                currentControlLoadSchedule.add(beforeControlLoadSchedule);
                                beforeControlLoadSchedule = new CommonScheduleResult();
                                beforeControlLoadSchedule.setControlLoad(patternNo.getControlLoad());
                                beforeControlLoadSchedule.setStartTime(startDate);
                                beforeControlLoadSchedule.setEndTime(endDate);
                            } else if (beforeControlLoadSchedule.getStartTime().compareTo(startDate) <= 0
                                    && startDate.compareTo(beforeControlLoadSchedule.getEndTime()) <= 0
                                    && beforeControlLoadSchedule.getEndTime().before(endDate)) {
                                //前のレコードの開始 <= 現在の開始 <= 前のレコードの終了 and 前のレコードの終了 < 現在の終了の場合、beforeの終了を変更
                                beforeControlLoadSchedule.setEndTime(endDate);
                            }
                        }

                        //beforeをMapにセット
                        beforeScheduleInfoMap.put(patternNo.getControlLoad(), beforeControlLoadSchedule);
                        //setをMapにセット
                        controlLoadScheduleMap.put(patternNo.getControlLoad(), currentControlLoadSchedule);
                    }
                }

            }

        }

        //最終レコードの処理をする
        for (Map.Entry<BigDecimal, CommonScheduleResult> entry : beforeScheduleInfoMap.entrySet()) {
            if (entry.getValue() != null) {
                //null以外の場合、setに詰める
                LinkedHashSet<CommonScheduleResult> controlLoadSchedule = controlLoadScheduleMap.get(entry.getKey());
                if (controlLoadSchedule == null) {
                    controlLoadSchedule = new LinkedHashSet<>();
                }
                controlLoadSchedule.add(entry.getValue());
            }
        }

        //制御負荷ごとにまとまっている情報をresultSetに詰めなおす
        for (Map.Entry<BigDecimal, LinkedHashSet<CommonScheduleResult>> entry : controlLoadScheduleMap.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                resultSet.addAll(entry.getValue());
            }
        }

        return resultSet;
    }

    /**
     * 対象のスケジュールをチェックする（無効の場合はFALSEを返す）
     * @param currentSchedule
     * @param currentDateNotSpecificDate
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @return
     */
    private static Boolean checkValidSchedule(SmControlScheduleTimeLogListDetailResultData currentSchedule) {

        if (currentSchedule.getStartTimeHour() >= 24 || currentSchedule.getEndTimeHour() > 24
                || (currentSchedule.getEndTimeHour() == 24 && currentSchedule.getEndTimeMinute() > 0)) {
            //開始は24時以降、終了は24時を超えたら無効
            return Boolean.FALSE;
        } else if (currentSchedule.getStartTimeMinute() >= 60 || currentSchedule.getEndTimeMinute() >= 60) {
            //60分以降は無効
            return Boolean.FALSE;
        } else if (currentSchedule.getStartTimeHour() > currentSchedule.getEndTimeHour()
                || (currentSchedule.getStartTimeHour().equals(currentSchedule.getEndTimeHour())
                        && currentSchedule.getStartTimeMinute() >= currentSchedule.getEndTimeMinute())) {
            //開始時刻 >= 終了時刻は無効
            return Boolean.FALSE;
        }

        return Boolean.TRUE;

    }

    /**
     * スケジュールが変わるタイミングの情報を作成する
     * @param scheduleLogList
     * @param holidayLogList
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternChangeResult> createSchedulePatternChangeResult(
            List<SmControlScheduleLogListDetailResultData> scheduleLogList,
            List<SmControlHolidayLogListDetailResultData> holidayLogList,
            Timestamp settingUpdateDateTimeFrom, Timestamp settingUpdateDateTimeTo) {

        LinkedHashSet<CommonSchedulePatternChangeResult> resultSet = new LinkedHashSet<>();
        //変更されるタイミング①：日付が変更される
        LinkedHashSet<CommonSchedulePatternChangeResult> dateChangeResultSet = createDateChangeResult(
                resultSet, settingUpdateDateTimeFrom, settingUpdateDateTimeTo);
        if (dateChangeResultSet != null && !dateChangeResultSet.isEmpty()) {
            resultSet.addAll(dateChangeResultSet);
        }
        //変更されるタイミング②：適用するスケジュールが変更される
        LinkedHashSet<CommonSchedulePatternChangeResult> scheduleChangeResultSet = createScheduleChangeResult(
                resultSet, scheduleLogList, settingUpdateDateTimeFrom);
        if (scheduleChangeResultSet != null && !scheduleChangeResultSet.isEmpty()) {
            resultSet.addAll(scheduleChangeResultSet);
        }
        //変更されるタイミング③：適用する祝日情報が変更される
        LinkedHashSet<CommonSchedulePatternChangeResult> holidayChangeResultSet = createHolidayChangeResult(
                resultSet, holidayLogList, settingUpdateDateTimeFrom);
        if (holidayChangeResultSet != null && !holidayChangeResultSet.isEmpty()) {
            resultSet.addAll(holidayChangeResultSet);
        }

        //変更されるトリガでソート
        if (resultSet != null && !resultSet.isEmpty()) {
            resultSet = resultSet.stream().sorted(Comparator
                    .comparing(CommonSchedulePatternChangeResult::getPatternChangeTimestamp, Comparator.naturalOrder()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return resultSet;
    }

    /**
     * スケジュールパターン番号を取得する
     * @param currentDate
     * @param currentSmControlScheduleLogId
     * @param currentDayOfWeek
     * @param currentHolidayFlg
     * @param scheduleSetLogList
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternNoResult> getPatternNo(Date currentDate,
            Long currentSmControlScheduleLogId, String currentDayOfWeek,
            Boolean currentHolidayFlg, List<SmControlScheduleSetLogListDetailResultData> scheduleSetLogList) {

        LinkedHashSet<CommonSchedulePatternNoResult> resultSet = new LinkedHashSet<>();

        if (scheduleSetLogList == null || scheduleSetLogList.isEmpty() || currentDate == null
                || currentSmControlScheduleLogId == null || CheckUtility.isNullOrEmpty(currentDayOfWeek)
                || currentHolidayFlg == null) {
            return resultSet;
        }

        BigDecimal targetDate = new BigDecimal(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_MM));
        Integer dd = Integer.parseInt(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_DD));

        List<SmControlScheduleSetLogListDetailResultData> filterScheduleSetLogList = scheduleSetLogList.stream()
                .filter(i -> currentSmControlScheduleLogId.equals(i.getSmControlScheduleLogId())
                        && targetDate.compareTo(i.getTargetMonth()) == 0)
                .collect(Collectors.toList());

        if (filterScheduleSetLogList == null || filterScheduleSetLogList.isEmpty()) {
            return resultSet;
        }

        for (SmControlScheduleSetLogListDetailResultData scheduleSet : filterScheduleSetLogList) {
            //特定日の判断をする
            if (dd.equals(scheduleSet.getSpecificDate1())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo1())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo1())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo1()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate2())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo2())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo2())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo2()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate3())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo3())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo3())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo3()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate4())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo4())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo4())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo4()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate5())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo5())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo5())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo5()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate6())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo6())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo6())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo6()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate7())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo7())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo7())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo7()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate8())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo8())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo8())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo8()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate9())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo9())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo9())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo9()));
                continue;
            }
            if (dd.equals(scheduleSet.getSpecificDate10())
                    && !CheckUtility.isNullOrEmpty(scheduleSet.getSpecificDatePatternNo10())
                    && !"00".equals(scheduleSet.getSpecificDatePatternNo10())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getSpecificDatePatternNo10()));
                continue;
            }
            //祝日の判断をする
            if (currentHolidayFlg && !CheckUtility.isNullOrEmpty(scheduleSet.getHolidayPatternNo())
                    && !"00".equals(scheduleSet.getHolidayPatternNo())) {
                resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                        scheduleSet.getHolidayPatternNo()));
                continue;
            }
            //曜日の判断をする
            if (DateUtility.DAY_OF_WEEK.SUNDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getSundayPatternNo())
                        && !"00".equals(scheduleSet.getSundayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getSundayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.MONDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getMondayPatternNo())
                        && !"00".equals(scheduleSet.getMondayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getMondayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.TUESDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getTuesdayPatternNo())
                        && !"00".equals(scheduleSet.getTuesdayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getTuesdayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.WEDNESDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getWednesdayPatternNo())
                        && !"00".equals(scheduleSet.getWednesdayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getWednesdayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.THURSDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getThursdayPatternNo())
                        && !"00".equals(scheduleSet.getThursdayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getThursdayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.FRIDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getFridayPatternNo())
                        && !"00".equals(scheduleSet.getFridayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getFridayPatternNo()));
                }
                continue;
            }
            if (DateUtility.DAY_OF_WEEK.SATURDAY.getName().equals(currentDayOfWeek)) {
                if (!CheckUtility.isNullOrEmpty(scheduleSet.getSaturdayPatternNo())
                        && !"00".equals(scheduleSet.getSaturdayPatternNo())) {
                    resultSet.add(new CommonSchedulePatternNoResult(scheduleSet.getControlLoad(),
                            scheduleSet.getSaturdayPatternNo()));
                }
                continue;
            }
        }

        return resultSet;
    }

    /**
     * 祝日判定を行う
     * @param currentDate
     * @param currentSmControlHolidayLogId
     * @param holidayCalLogList
     * @return
     */
    private static Boolean getHolidayFlg(Date currentDate, Long currentSmControlHolidayLogId,
            List<SmControlHolidayCalLogListDetailResultData> holidayCalLogList) {

        if (holidayCalLogList == null || holidayCalLogList.isEmpty() || currentDate == null
                || currentSmControlHolidayLogId == null) {
            return Boolean.FALSE;
        }

        String currentMmdd = DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_MMDD);

        List<SmControlHolidayCalLogListDetailResultData> filterHolidayCalLogList = holidayCalLogList.stream()
                .filter(i -> currentSmControlHolidayLogId.equals(i.getSmControlHolidayLogId())
                        && currentMmdd.equals(i.getHolidayMmDd()))
                .collect(Collectors.toList());

        if (filterHolidayCalLogList == null || filterHolidayCalLogList.isEmpty()) {
            //祝日情報なし
            return Boolean.FALSE;
        } else {
            //祝日情報あり
            return Boolean.TRUE;
        }
    }

    /**
     * スケジュールパターン変更のcurrentNextのsetを作成する
     * @param patternChangeResult
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> createPatternChangeCurrentNextResult(
            LinkedHashSet<CommonSchedulePatternChangeResult> patternChangeResult) {

        LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> resultSet = new LinkedHashSet<>();
        CommonSchedulePatternChangeResult current = null;
        CommonSchedulePatternChangeResult next = null;

        if (patternChangeResult == null || patternChangeResult.isEmpty()) {
            return resultSet;
        }

        Iterator<CommonSchedulePatternChangeResult> iterator = patternChangeResult.iterator();
        current = iterator.next();

        while (iterator.hasNext()) {
            if (next != null) {
                current = next;
            }
            next = iterator.next();
            resultSet.add(new CommonSchedulePatternChangeCurrentNextResult(current, next));
        }

        //最終データの処理
        if (next != null) {
            current = next;
        }
        next = null;
        resultSet.add(new CommonSchedulePatternChangeCurrentNextResult(current, next));

        return resultSet;
    }

    /**
     * 日付変更トリガのTimeTableを作成する
     * @param currentResultSet
     * @param settingUpdateDateTimeFrom
     * @param settingUpdateDateTimeTo
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternChangeResult> createDateChangeResult(
            LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            Timestamp settingUpdateDateTimeFrom, Timestamp settingUpdateDateTimeTo) {

        LinkedHashSet<CommonSchedulePatternChangeResult> resultSet = new LinkedHashSet<>();
        Date currentDate = null;
        Date endDate = DateUtility.conversionDate(DateUtility
                .changeDateFormat(new Date(settingUpdateDateTimeTo.getTime()), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        if (currentResultSet != null && !currentResultSet.isEmpty()) {
            resultSet = new LinkedHashSet<>(currentResultSet);
        }

        do {

            CommonSchedulePatternChangeResult detail = new CommonSchedulePatternChangeResult();

            if (currentDate == null) {
                currentDate = DateUtility
                        .conversionDate(DateUtility.changeDateFormat(new Date(settingUpdateDateTimeFrom.getTime()),
                                DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);
                if (currentResultSet == null || currentResultSet.isEmpty()) {
                    //単純に追加
                    detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                    detail.setDateChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterDate(currentDate);
                    detail.setScheduleLogChangeFlg(Boolean.FALSE);
                    detail.setHolidayLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlScheduleLogId(null);
                    detail.setChangeAfterScheduleManageDesignationFlg(null);
                    detail.setChangeAfterSmControlHolidayLogId(null);
                    resultSet.add(detail);
                } else {
                    //同一キーのデータを探し出し、上書き
                    Boolean writeData = Boolean.FALSE;
                    for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                        if (changeResult.getPatternChangeTimestamp().equals(settingUpdateDateTimeFrom)) {
                            changeResult.setDateChangeFlg(Boolean.TRUE);
                            changeResult.setChangeAfterDate(currentDate);
                            writeData = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!writeData) {
                        //上書きしていない場合は追加
                        detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                        detail.setDateChangeFlg(Boolean.TRUE);
                        detail.setChangeAfterDate(currentDate);
                        detail.setScheduleLogChangeFlg(Boolean.FALSE);
                        detail.setHolidayLogChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterSmControlScheduleLogId(null);
                        detail.setChangeAfterScheduleManageDesignationFlg(null);
                        detail.setChangeAfterSmControlHolidayLogId(null);
                        resultSet.add(detail);
                    }
                }
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
                if (currentResultSet == null || currentResultSet.isEmpty()) {
                    //単純に追加
                    detail.setPatternChangeTimestamp(new Timestamp(currentDate.getTime()));
                    detail.setDateChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterDate(currentDate);
                    detail.setScheduleLogChangeFlg(Boolean.FALSE);
                    detail.setHolidayLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlScheduleLogId(null);
                    detail.setChangeAfterScheduleManageDesignationFlg(null);
                    detail.setChangeAfterSmControlHolidayLogId(null);
                    resultSet.add(detail);
                } else {
                    //同一キーのデータを探し出し、上書き
                    Boolean writeData = Boolean.FALSE;
                    for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                        if (changeResult.getPatternChangeTimestamp().equals(new Timestamp(currentDate.getTime()))) {
                            changeResult.setDateChangeFlg(Boolean.TRUE);
                            changeResult.setChangeAfterDate(currentDate);
                            writeData = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!writeData) {
                        //上書きしていない場合は追加
                        detail.setPatternChangeTimestamp(new Timestamp(currentDate.getTime()));
                        detail.setDateChangeFlg(Boolean.TRUE);
                        detail.setChangeAfterDate(currentDate);
                        detail.setScheduleLogChangeFlg(Boolean.FALSE);
                        detail.setHolidayLogChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterSmControlScheduleLogId(null);
                        detail.setChangeAfterScheduleManageDesignationFlg(null);
                        detail.setChangeAfterSmControlHolidayLogId(null);
                        resultSet.add(detail);
                    }
                }
            }
        } while (!currentDate.equals(endDate));

        return resultSet;
    }

    /**
     * スケジュール変更トリガのTimeTableを作成する
     * @param currentResultSet
     * @param scheduleLogList
     * @param settingUpdateDateTimeFrom
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternChangeResult> createScheduleChangeResult(
            LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            List<SmControlScheduleLogListDetailResultData> scheduleLogList, Timestamp settingUpdateDateTimeFrom) {

        if (scheduleLogList == null || scheduleLogList.isEmpty()) {
            return currentResultSet;
        }

        LinkedHashSet<CommonSchedulePatternChangeResult> resultSet = new LinkedHashSet<>();

        if (currentResultSet != null && !currentResultSet.isEmpty()) {
            resultSet = new LinkedHashSet<>(currentResultSet);
        }

        if (scheduleLogList.size() == 1) {
            //1件の場合

            CommonSchedulePatternChangeResult detail = new CommonSchedulePatternChangeResult();
            if (currentResultSet == null || currentResultSet.isEmpty()) {
                //単純に追加
                if (scheduleLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                    //開始より前の場合は、開始に変更
                    detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                } else {
                    detail.setPatternChangeTimestamp(scheduleLogList.get(0).getSettingUpdateDateTime());
                }
                detail.setScheduleLogChangeFlg(Boolean.TRUE);
                detail.setChangeAfterSmControlScheduleLogId(scheduleLogList.get(0).getSmControlScheduleLogId());
                detail.setChangeAfterScheduleManageDesignationFlg(
                        scheduleLogList.get(0).getScheduleManageDesignationFlg());
                detail.setDateChangeFlg(Boolean.FALSE);
                detail.setChangeAfterDate(null);
                detail.setHolidayLogChangeFlg(Boolean.FALSE);
                detail.setChangeAfterSmControlHolidayLogId(null);
                resultSet.add(detail);
            } else {
                //同一キーのデータを探し出し、上書き
                Boolean writeData = Boolean.FALSE;
                for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                    if (scheduleLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)
                            && changeResult.getPatternChangeTimestamp().equals(settingUpdateDateTimeFrom)) {
                        changeResult.setScheduleLogChangeFlg(Boolean.TRUE);
                        changeResult.setChangeAfterSmControlScheduleLogId(
                                scheduleLogList.get(0).getSmControlScheduleLogId());
                        changeResult.setChangeAfterScheduleManageDesignationFlg(
                                scheduleLogList.get(0).getScheduleManageDesignationFlg());
                        writeData = Boolean.TRUE;
                        break;
                    } else if (!scheduleLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)
                            && changeResult.getPatternChangeTimestamp()
                                    .equals(scheduleLogList.get(0).getSettingUpdateDateTime())) {
                        changeResult.setScheduleLogChangeFlg(Boolean.TRUE);
                        changeResult.setChangeAfterSmControlScheduleLogId(
                                scheduleLogList.get(0).getSmControlScheduleLogId());
                        changeResult.setChangeAfterScheduleManageDesignationFlg(
                                scheduleLogList.get(0).getScheduleManageDesignationFlg());
                        writeData = Boolean.TRUE;
                        break;
                    }
                }
                if (!writeData) {
                    //上書きしていない場合は追加
                    if (scheduleLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                        //開始より前の場合は、開始に変更
                        detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                    } else {
                        detail.setPatternChangeTimestamp(scheduleLogList.get(0).getSettingUpdateDateTime());
                    }
                    detail.setScheduleLogChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterSmControlScheduleLogId(scheduleLogList.get(0).getSmControlScheduleLogId());
                    detail.setChangeAfterScheduleManageDesignationFlg(
                            scheduleLogList.get(0).getScheduleManageDesignationFlg());
                    detail.setDateChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterDate(null);
                    detail.setHolidayLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlHolidayLogId(null);
                    resultSet.add(detail);
                }
            }

        } else {
            //2件以上の場合、1つ前のデータが不要なケースがある
            int index = 0;
            if (settingUpdateDateTimeFrom.equals(scheduleLogList.get(1).getSettingUpdateDateTime())) {
                //開始と同一の場合、最初のデータは不要
                index = 1;
            }

            for (int i = index; i < scheduleLogList.size(); i++) {
                CommonSchedulePatternChangeResult detail = new CommonSchedulePatternChangeResult();
                if (currentResultSet == null || currentResultSet.isEmpty()) {
                    //単純に追加
                    if (i == 0 && scheduleLogList.get(i).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                        //開始より前のデータの場合は、開始として対応する
                        detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                    } else {
                        detail.setPatternChangeTimestamp(scheduleLogList.get(i).getSettingUpdateDateTime());
                    }
                    detail.setScheduleLogChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterSmControlScheduleLogId(scheduleLogList.get(i).getSmControlScheduleLogId());
                    detail.setChangeAfterScheduleManageDesignationFlg(
                            scheduleLogList.get(i).getScheduleManageDesignationFlg());
                    detail.setDateChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterDate(null);
                    detail.setHolidayLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlHolidayLogId(null);
                    resultSet.add(detail);
                } else {
                    //同一キーのデータを探し出し、上書き
                    Boolean writeData = Boolean.FALSE;
                    for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                        if (changeResult.getPatternChangeTimestamp()
                                .equals(scheduleLogList.get(i).getSettingUpdateDateTime())
                                || (i == 0 && scheduleLogList.get(i).getSettingUpdateDateTime()
                                        .before(settingUpdateDateTimeFrom)
                                        && changeResult.getPatternChangeTimestamp()
                                                .equals(settingUpdateDateTimeFrom))) {
                            changeResult.setScheduleLogChangeFlg(Boolean.TRUE);
                            changeResult.setChangeAfterSmControlScheduleLogId(
                                    scheduleLogList.get(i).getSmControlScheduleLogId());
                            changeResult.setChangeAfterScheduleManageDesignationFlg(
                                    scheduleLogList.get(i).getScheduleManageDesignationFlg());
                            writeData = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!writeData) {
                        //上書きしていない場合は追加
                        if (i == 0 && scheduleLogList.get(i).getSettingUpdateDateTime()
                                .before(settingUpdateDateTimeFrom)) {
                            //開始より前のデータの場合は、開始として対応する
                            detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                        } else {
                            detail.setPatternChangeTimestamp(scheduleLogList.get(i).getSettingUpdateDateTime());
                        }
                        detail.setScheduleLogChangeFlg(Boolean.TRUE);
                        detail.setChangeAfterSmControlScheduleLogId(scheduleLogList.get(i).getSmControlScheduleLogId());
                        detail.setChangeAfterScheduleManageDesignationFlg(
                                scheduleLogList.get(i).getScheduleManageDesignationFlg());
                        detail.setDateChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterDate(null);
                        detail.setHolidayLogChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterSmControlHolidayLogId(null);
                        resultSet.add(detail);
                    }
                }
            }
        }

        return resultSet;
    }

    /**
     * 祝日変更トリガのタイムテーブルを作成する
     * @param currentResultSet
     * @param holidayLogList
     * @param settingUpdateDateTimeFrom
     * @return
     */
    private static LinkedHashSet<CommonSchedulePatternChangeResult> createHolidayChangeResult(
            LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            List<SmControlHolidayLogListDetailResultData> holidayLogList, Timestamp settingUpdateDateTimeFrom) {

        if (holidayLogList == null || holidayLogList.isEmpty()) {
            return currentResultSet;
        }

        LinkedHashSet<CommonSchedulePatternChangeResult> resultSet = new LinkedHashSet<>();

        if (currentResultSet != null && !currentResultSet.isEmpty()) {
            resultSet = new LinkedHashSet<>(currentResultSet);
        }

        if (holidayLogList.size() == 1) {
            //1件の場合
            CommonSchedulePatternChangeResult detail = new CommonSchedulePatternChangeResult();
            if (currentResultSet == null || currentResultSet.isEmpty()) {
                //単純に追加
                if (holidayLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                    //開始より前の場合は開始として処理
                    detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                } else {
                    detail.setPatternChangeTimestamp(holidayLogList.get(0).getSettingUpdateDateTime());
                }

                detail.setHolidayLogChangeFlg(Boolean.TRUE);
                detail.setChangeAfterSmControlHolidayLogId(holidayLogList.get(0).getSmControlHolidayLogId());
                detail.setDateChangeFlg(Boolean.FALSE);
                detail.setChangeAfterDate(null);
                detail.setScheduleLogChangeFlg(Boolean.FALSE);
                detail.setChangeAfterSmControlScheduleLogId(null);
                resultSet.add(detail);
            } else {
                //同一キーのデータを探し出し、上書き
                Boolean writeData = Boolean.FALSE;
                for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                    if (holidayLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)
                            && changeResult.getPatternChangeTimestamp().equals(settingUpdateDateTimeFrom)) {
                        changeResult.setHolidayLogChangeFlg(Boolean.TRUE);
                        changeResult.setChangeAfterSmControlHolidayLogId(
                                holidayLogList.get(0).getSmControlHolidayLogId());
                        writeData = Boolean.TRUE;
                        break;
                    } else if (!holidayLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)
                            && changeResult.getPatternChangeTimestamp()
                                    .equals(holidayLogList.get(0).getSettingUpdateDateTime())) {
                        changeResult.setHolidayLogChangeFlg(Boolean.TRUE);
                        changeResult.setChangeAfterSmControlHolidayLogId(
                                holidayLogList.get(0).getSmControlHolidayLogId());
                        writeData = Boolean.TRUE;
                        break;
                    }
                }
                if (!writeData) {
                    //上書きしていない場合は追加
                    if (holidayLogList.get(0).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                        //開始より前の場合は開始として処理
                        detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                    } else {
                        detail.setPatternChangeTimestamp(holidayLogList.get(0).getSettingUpdateDateTime());
                    }
                    detail.setHolidayLogChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterSmControlHolidayLogId(holidayLogList.get(0).getSmControlHolidayLogId());
                    detail.setDateChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterDate(null);
                    detail.setScheduleLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlScheduleLogId(null);
                    resultSet.add(detail);
                }
            }
        } else {
            //2件以上の場合、1つ前のデータが不要なケースがある
            int index = 0;
            if (settingUpdateDateTimeFrom.equals(holidayLogList.get(1).getSettingUpdateDateTime())) {
                //開始と同一の場合、最初のデータは不要
                index = 1;
            }

            for (int i = index; i < holidayLogList.size(); i++) {
                CommonSchedulePatternChangeResult detail = new CommonSchedulePatternChangeResult();
                if (currentResultSet == null || currentResultSet.isEmpty()) {
                    //単純に追加
                    if (i == 0 && holidayLogList.get(i).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                        //開始より前のデータの場合は、開始として対応する
                        detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                    } else {
                        detail.setPatternChangeTimestamp(holidayLogList.get(i).getSettingUpdateDateTime());
                    }
                    detail.setHolidayLogChangeFlg(Boolean.TRUE);
                    detail.setChangeAfterSmControlHolidayLogId(holidayLogList.get(i).getSmControlHolidayLogId());
                    detail.setDateChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterDate(null);
                    detail.setScheduleLogChangeFlg(Boolean.FALSE);
                    detail.setChangeAfterSmControlScheduleLogId(null);
                    resultSet.add(detail);
                } else {
                    //同一キーのデータを探し出し、上書き
                    Boolean writeData = Boolean.FALSE;
                    for (CommonSchedulePatternChangeResult changeResult : resultSet) {
                        if (changeResult.getPatternChangeTimestamp()
                                .equals(holidayLogList.get(i).getSettingUpdateDateTime())
                                || (i == 0
                                        && holidayLogList.get(0).getSettingUpdateDateTime()
                                                .before(settingUpdateDateTimeFrom)
                                        && changeResult.getPatternChangeTimestamp()
                                                .equals(settingUpdateDateTimeFrom))) {
                            changeResult.setHolidayLogChangeFlg(Boolean.TRUE);
                            changeResult.setChangeAfterSmControlHolidayLogId(
                                    holidayLogList.get(i).getSmControlHolidayLogId());
                            writeData = Boolean.TRUE;
                            break;
                        }
                    }
                    if (!writeData) {
                        if (i == 0
                                && holidayLogList.get(i).getSettingUpdateDateTime().before(settingUpdateDateTimeFrom)) {
                            //開始より前のデータの場合は、開始として対応する
                            detail.setPatternChangeTimestamp(settingUpdateDateTimeFrom);
                        } else {
                            detail.setPatternChangeTimestamp(holidayLogList.get(i).getSettingUpdateDateTime());
                        }
                        detail.setHolidayLogChangeFlg(Boolean.TRUE);
                        detail.setChangeAfterSmControlHolidayLogId(holidayLogList.get(i).getSmControlHolidayLogId());
                        detail.setDateChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterDate(null);
                        detail.setScheduleLogChangeFlg(Boolean.FALSE);
                        detail.setChangeAfterSmControlScheduleLogId(null);
                        resultSet.add(detail);
                    }
                }
            }
        }

        return resultSet;
    }

    /**
     * スケジュール情報に時限Noを付与する
     * @param scheduleSet
     * @return
     */
    public static LinkedHashSet<CommonDemandControlTimeTableResult> createScheduleControlTimeTableResult(
            LinkedHashSet<CommonScheduleResult> scheduleSet) {

        if (scheduleSet == null || scheduleSet.isEmpty()) {
            return new LinkedHashSet<>();
        }

        LinkedHashSet<CommonDemandControlTimeTableResult> resultSet = new LinkedHashSet<>();

        for (CommonScheduleResult schedule : scheduleSet) {
            CommonDemandControlTimeTableResult startData = new CommonDemandControlTimeTableResult();
            CommonDemandControlTimeTableResult endData = new CommonDemandControlTimeTableResult();

            //開始データの作成
            startData.setControlLoad(schedule.getControlLoad().intValue());
            startData.setMeasurementDate(schedule.getStartTime());
            startData.setJigenNo(getJigenNoForDemand(
                    DateUtility.changeDateFormat(schedule.getStartTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
            startData.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_CUT);

            //終了データの作成
            endData.setControlLoad(schedule.getControlLoad().intValue());
            endData.setMeasurementDate(schedule.getEndTime());
            endData.setJigenNo(getJigenNoForDemand(
                    DateUtility.changeDateFormat(schedule.getEndTime(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
            endData.setControlStatus(ApiSimpleConstants.LOAD_CONTROL_FREE);

            resultSet.add(startData);
            resultSet.add(endData);
        }

        return resultSet;
    }

    /**
     * 負荷制御ログの最終レコードを精査し、遮断で終わっていたら、最終レコードを追加する
     * @param loadControlLogList
     * @param limitDate
     * @return
     */
    public static final List<TLoadControlLogResult> addLoadControlLogRecord(
            List<TLoadControlLogResult> loadControlLogList, Date limitDate) {

        if (loadControlLogList == null || loadControlLogList.isEmpty()) {
            //データがない場合は何もしない
            return loadControlLogList;
        }

        //最終レコードの制御状態を取得
        String controlStatus = loadControlLogList.get(loadControlLogList.size() - 1).getControlStatus();
        String[] statusArray = controlStatus.split("");
        Boolean controlFlg = Boolean.FALSE;

        for (String status : statusArray) {
            if (!"0".equals(status)) {
                controlFlg = Boolean.TRUE;
                break;
            }
        }

        //遮断レコードがある場合はlimitDateに開放されるレコードを追加
        if (controlFlg) {
            StringBuilder allFreeStatus = new StringBuilder();
            do {
                allFreeStatus.append("0");
            } while (allFreeStatus.length() != statusArray.length);

            TLoadControlLogResult addRecord = new TLoadControlLogResult();
            addRecord.setSmId(loadControlLogList.get(loadControlLogList.size() - 1).getSmId());
            addRecord.setRecordYmdhm(DateUtility.changeDateFormat(limitDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMM));
            addRecord.setRestMs("3000");
            addRecord.setControlStatus(allFreeStatus.toString());
            loadControlLogList.add(addRecord);
        }

        return loadControlLogList;
    }

    /**
     * 温湿度制御ログの最終レコードを精査し、遮断で終わっていたら、最終レコードを追加する
     * @param tempHumidControlLogList
     * @param limitDate
     * @return
     */
    public static final List<TempHumidControlLogVerifyResult> addTempHumidLogRecord(
            List<TempHumidControlLogVerifyResult> tempHumidControlLogList, Date limitDate) {

        if (tempHumidControlLogList == null || tempHumidControlLogList.isEmpty()) {
            //データがない場合は何もしない
            return tempHumidControlLogList;
        }

        //最終レコードのポート出力状態を取得
        String portOutStatus = tempHumidControlLogList.get(tempHumidControlLogList.size() - 1).getPortOutStatus();
        String[] statusArray = portOutStatus.split("");
        Boolean controlFlg = Boolean.FALSE;

        for (String status : statusArray) {
            if (!"0".equals(status)) {
                controlFlg = Boolean.TRUE;
                break;
            }
        }

        //遮断レコードがある場合はlimitDateに開放されるレコードを追加
        if (controlFlg) {
            StringBuilder allFreeStatus = new StringBuilder();
            do {
                allFreeStatus.append("0");
            } while (allFreeStatus.length() != statusArray.length);

            TempHumidControlLogVerifyResult addRecord = new TempHumidControlLogVerifyResult();
            addRecord.setSmId(tempHumidControlLogList.get(tempHumidControlLogList.size() - 1).getSmId());
            addRecord.setRecordYmdhms(DateUtility.changeDateFormat(limitDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
            addRecord.setPortOutStatus(allFreeStatus.toString());
            tempHumidControlLogList.add(addRecord);
        }

        return tempHumidControlLogList;
    }

    /**
     * イベント制御ログの最終レコードを精査し、遮断で終わっていたら、最終レコードを追加する
     * @param eventControlLogList
     * @param limitDate
     * @param productCd
     * @param controlLoad
     * @return
     */
    public static final List<EventControlLogResult> addEventLogRecord(List<EventControlLogResult> eventControlLogList,
            Date limitDate, String productCd, Integer controlLoad) {

        if (eventControlLogList == null || eventControlLogList.isEmpty()) {
            return eventControlLogList;
        }

        //最終レコードの制御状態を確認
        String status = null;
        if (OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd)) {
            //G2の場合、制御イベント1要因を取得後、2進数の変換して1文字目
            status = createEventStatus(eventControlLogList.get(eventControlLogList.size() - 1),
                    productCd);
        } else {
            status = eventControlLogList.get(eventControlLogList.size() - 1).getControlStatus();
        }

        //遮断の場合はlimitDateに開放されるレコードを追加
        if (!"0".equals(status)) {
            EventControlLogResult addRecord = new EventControlLogResult();
            addRecord.setSmId(eventControlLogList.get(eventControlLogList.size() - 1).getSmId());
            addRecord.setRecordYmdhms(DateUtility.changeDateFormat(limitDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
            addRecord.setControlLoad(BigDecimal.valueOf(controlLoad));
            addRecord.setControlStatus("0");
            addRecord.setControlEvent1Kind("0");
            eventControlLogList.add(addRecord);
        }

        return eventControlLogList;
    }

    /**
     * 制御負荷からListのインデックス番号を取得する
     * @param controlLoad
     * @param flgDataList
     * @return
     */
    public static Integer getListIndex(Integer controlLoad, List<ProductControlLoadListDetailResultData> flgDataList) {

        Integer index = null;

        for(int i = 0;i < flgDataList.size();i++) {
            if(controlLoad.equals(flgDataList.get(i).getControlLoad().intValue())) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * 対象の製品コードがG2以前か否かを返却する
     * @param productCd
     * @return
     */
    public static boolean isBeforeG2(String productCd) {
        return OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_ALPHA_C.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_ALPHA_G.getVal().equals(productCd)
                || OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productCd);
    }

    /**
     * 対象製品のコードがG2以降か否かを返却する
     * @param productCd
     * @return
     */
    public static boolean isAfterG2(String productCd) {
        return !(OsolConstants.PRODUCT_CD.FV2.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_D.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_ALPHA_C.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productCd)
                    || OsolConstants.PRODUCT_CD.FVP_ALPHA_G.getVal().equals(productCd));
    }

}
