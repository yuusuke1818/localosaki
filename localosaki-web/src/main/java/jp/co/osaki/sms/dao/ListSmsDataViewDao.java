/**
 *
 */
package jp.co.osaki.sms.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.ListSmsDataViewMeterInfoServiceDaoImpl;
import jp.co.osaki.sms.servicedao.ListSmsDataViewMeterValueServiceDaoImpl;
import jp.co.osaki.sms.servicedao.ListSmsDataViewTenantInfoServiceDaoImpl;

/**
 *
 */
@Stateless
public class ListSmsDataViewDao extends SmsDao {

    private final ListSmsDataViewMeterInfoServiceDaoImpl meterInfoDaoImpl;
    private final ListSmsDataViewMeterValueServiceDaoImpl meterValueDaoImpl;
    private final ListSmsDataViewTenantInfoServiceDaoImpl tenantInfoDaoImpl;

    public ListSmsDataViewDao() {
        meterInfoDaoImpl = new ListSmsDataViewMeterInfoServiceDaoImpl();
        meterValueDaoImpl = new ListSmsDataViewMeterValueServiceDaoImpl();
        tenantInfoDaoImpl = new ListSmsDataViewTenantInfoServiceDaoImpl();
    }

    public ListSmsDataViewResult query(
            String corpId,
            Long buildingId,
            boolean isTenant,
            String devId,
            boolean isForwardDiract,
            List<String> targetDateTimeList,
            List<Long> meterMngIdList,
            boolean isUse,
            String dispTimeUnit,
            String targetDateTimeFormat) {

        ListSmsDataViewResultData param = new ListSmsDataViewResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        param.setTenant(isTenant);
        param.setDevId(devId);
        param.setForwardDiract(isForwardDiract);
        param.setUse(isUse);
        param.setDispTimeUnit(dispTimeUnit);
        param.setTargetDateTimeList(targetDateTimeList);
        param.setTargetDateTimeFormat(targetDateTimeFormat);

        // 装置に紐付くメーター情報
        List<ListSmsDataViewResultData> meterInfoDataList = getResultList(meterInfoDaoImpl, param);
        Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap = createMeterDataListMap(meterInfoDataList);

        param.setMeterMngIdList(meterMngIdList);

        // テナント情報
        List<ListSmsDataViewResultData> tenantInfoDataList = getResultList(tenantInfoDaoImpl, param);
        Map<Long, List<ListSmsDataViewResultData>> tenantInfoDataMap = createMeterDataListMap(tenantInfoDataList);

        // 指定期間内の各メーターの使用量・指針値
        List<ListSmsDataViewResultData> meterValueDataList = getResultList(meterValueDaoImpl, param);

        Map<Long, List<ListSmsDataViewResultData>> meterValueDataMap = createMeterValueDataMap(
                meterValueDataList,
                meterInfoDataMap,
                isUse,
                dispTimeUnit,
                targetDateTimeFormat,
                targetDateTimeList);

        ListSmsDataViewResult result = new ListSmsDataViewResult();
        result.setCountX(meterMngIdList.size());
        result.setCountY(targetDateTimeList.size());
        result.setLoadSurveyDataMap(createLoadSurveyDataMap(meterMngIdList, targetDateTimeList, meterValueDataMap,
                meterInfoDataMap, tenantInfoDataMap));
        return result;
    }

    /**
     * メーターデータListMapを生成.
     *
     * @param targetDataList 対象データList
     * @return メーターデータListMap
     */
    private Map<Long, List<ListSmsDataViewResultData>> createMeterDataListMap(
            List<ListSmsDataViewResultData> targetDataList) {

        Map<Long, List<ListSmsDataViewResultData>> meterDataListMap = new LinkedHashMap<>();
        for (ListSmsDataViewResultData targetData : targetDataList) {
            Long meterMngId = targetData.getMeterMngId();

            List<ListSmsDataViewResultData> meterDataList = meterDataListMap.getOrDefault(meterMngId,
                    new ArrayList<>());
            meterDataList.add(targetData);

            meterDataListMap.put(meterMngId, meterDataList);
        }

        return meterDataListMap;
    }

    /**
     * 各メーターのデータ用使用量・指針値ListMapを生成.
     *
     * @param targetDataList 対象データList
     * @param meterInfoDataMap 装置に紐付くメーター情報Map
     * @param isUse 使用量フラグ
     * @param dispTimeUnit 表示時間単位
     * @param targetDateTimeFormat 対象日時フォーマット
     * @param targetDateTimeList 対象日時文字列List
     * @return 各メーターの使用量・指針値ListMap
     */
    private Map<Long, List<ListSmsDataViewResultData>> createMeterValueDataMap(
            List<ListSmsDataViewResultData> targetDataList, Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap,
            boolean isUse, String dispTimeUnit, String targetDateTimeFormat, List<String> targetDateTimeList) {

        // メーター管理番号をキーとする対象データListMap
        Map<Long, List<ListSmsDataViewResultData>> meterValueListMapBefore = createMeterDataListMap(targetDataList);
        Map<Long, List<ListSmsDataViewResultData>> meterValueListMap = new LinkedHashMap<>();

        Set<Entry<Long, List<ListSmsDataViewResultData>>> meterListEntrySet = meterValueListMapBefore
                .entrySet();

        for (Entry<Long, List<ListSmsDataViewResultData>> meterListEntry : meterListEntrySet) {
            Long meterMngId = meterListEntry.getKey();

            // 対象日時をキーとする対象データListMap
            Map<String, ListSmsDataViewResultData> targetDateMeterDataMap = createTargetDateMeterDataMap(
                    meterListEntry.getValue());

            ListSmsDataViewResultData meterInfoData = meterInfoDataMap.get(meterMngId).get(0);

            // 装置ID
            String devId = meterInfoData.getDevId();
            // 乗率
            BigDecimal multi = meterInfoData.getMulti();

            // 使用量合計
            BigDecimal totalKwh30 = calcTotalUseValue(targetDateMeterDataMap, multi);

            // CO2換算値
            BigDecimal co2ConvertValue = null;
            if (totalKwh30 != null) {
                // CO2排出係数
                BigDecimal co2Coefficient = meterInfoData.getCo2Coefficient();
                if (co2Coefficient == null) {
                    co2Coefficient = BigDecimal.ZERO;
                }

                co2ConvertValue = totalKwh30.multiply(co2Coefficient).setScale(2, RoundingMode.HALF_UP);
            }

            for (String targetDateTime : targetDateTimeList) {
                ListSmsDataViewResultData currentMeterData = targetDateMeterDataMap.get(targetDateTime);

                // 指針値A
                BigDecimal dmvA = null;
                int integerLengthA = 0;

                // 指針値B
                BigDecimal dmvB = null;
                int integerLengthB = 0;

                if (currentMeterData != null) {
                    dmvB = currentMeterData.getDmvKwh();
                    integerLengthB = currentMeterData.getIntegerLength();
                }

                BigDecimal kwh30 = null;
                BigDecimal dmvKwh = null;

                if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)
                        && (SmsConstants.DISP_TIME_UNIT.M30.getVal().equals(dispTimeUnit)
                                || (!isUse && SmsConstants.DISP_TIME_UNIT.M60.getVal().equals(dispTimeUnit)))) {
                    // 日報 30分使用量・指針値または1時間指針値取得時

                    if (currentMeterData != null) {
                        // 使用量
                        kwh30 = currentMeterData.getKwh30();
                        // 指針値
                        dmvKwh = currentMeterData.getDmvKwh();
                    }

                } else {
                    // 日報 30分使用量・指針値及び1時間指針値取得時以外

                    ListSmsDataViewResultData nextMeterData = targetDateMeterDataMap.get(
                            getDmvTargetDateTimeStr(targetDateTime, targetDateTimeFormat, 1, dispTimeUnit, false));

                    if (nextMeterData != null) {
                        // 指針値
                        dmvKwh = nextMeterData.getDmvKwh();

                        dmvA = dmvKwh;
                        integerLengthA = nextMeterData.getIntegerLength();
                    }

                    // 使用量(指針値から算出)
                    kwh30 = calcUseValueFromDmv(dmvA, integerLengthA, dmvB, integerLengthB, multi);
                }

                ListSmsDataViewResultData resultMeterData = new ListSmsDataViewResultData();
                resultMeterData.setDevId(devId);
                resultMeterData.setMeterMngId(meterMngId);
                resultMeterData.setTargetDateTime(targetDateTime);
                resultMeterData.setKwh30(kwh30);
                resultMeterData.setDmvKwh(dmvKwh);
                resultMeterData.setTotalKwh30(totalKwh30);
                resultMeterData.setCo2ConvertValue(co2ConvertValue);

                List<ListSmsDataViewResultData> meterValueList = meterValueListMap.getOrDefault(meterMngId,
                        new ArrayList<>());
                meterValueList.add(resultMeterData);
                meterValueListMap.put(meterMngId, meterValueList);
            }
        }

        return meterValueListMap;
    }

    /**
     * 指定期間内の合計使用量を算出.
     *
     * @param targetDateMeterDataMap 対象日時をキーとする対象データListMap
     * @param multi 乗率
     * @return 指定期間内の合計使用量
     */
    private BigDecimal calcTotalUseValue(Map<String, ListSmsDataViewResultData> targetDateMeterDataMap,
            BigDecimal multi) {

        if (MapUtils.isEmpty(targetDateMeterDataMap)) {
            return null;
        }

        // 指針値A
        BigDecimal dmvA = null;
        int integerLengthA = 0;

        // 指針値B
        BigDecimal dmvB = null;
        int integerLengthB = 0;

        Collection<ListSmsDataViewResultData> targetDateMeterDataClct = targetDateMeterDataMap.values();
        for (ListSmsDataViewResultData currentMeterData : targetDateMeterDataClct) {
            if (currentMeterData != null) {
                BigDecimal currentDmvKwh = currentMeterData.getDmvKwh();
                int currentIntegerLength = currentMeterData.getIntegerLength();

                if (currentDmvKwh != null) {
                    if (dmvB == null) {
                        // 指定期間内最古の指針値
                        dmvB = currentDmvKwh;
                        integerLengthB = currentIntegerLength;

                    } else {
                        // 指定期間内最新の指針値
                        dmvA = currentDmvKwh;
                        integerLengthA = currentIntegerLength;
                    }
                }
            }
        }

        return calcUseValueFromDmv(dmvA, integerLengthA, dmvB, integerLengthB, multi);
    }

    /**
     * 指針値の対象日時文字列を取得.
     *
     * @param baseDateTimeStr 取得する指針値の対象日時文字列
     * @param targetDateTimeFormat 対象日時フォーマット
     * @param amount 日時差分量
     * @param dispTimeUnit 表示時間単位
     * @param totalFlg 使用量合計算出時フラグ
     * @return 指針値の対象日時文字列
     */
    private String getDmvTargetDateTimeStr(String baseDateTimeStr, String targetDateTimeFormat, int amount,
            String dispTimeUnit, boolean totalFlg) {

        Date baseDateTime = DateUtility.conversionDate(baseDateTimeStr, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Date targetDateTime = null;

        ChronoField plusUnit = null;

        if (totalFlg) {
            if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)) {
                // 日報
                plusUnit = ChronoField.DAY_OF_MONTH;

            } else if (DateUtility.DATE_FORMAT_YYYYMMDD.equals(targetDateTimeFormat)) {
                // 月報
                plusUnit = ChronoField.MONTH_OF_YEAR;

            } else if (DateUtility.DATE_FORMAT_YYYYMM.equals(targetDateTimeFormat)) {
                // 年報
                plusUnit = ChronoField.YEAR;
            }

        } else {
            if (SmsConstants.DISP_TIME_UNIT.M60.getVal().equals(dispTimeUnit)) {
                // 日報
                // 表示時間単位が1時間の場合
                plusUnit = ChronoField.HOUR_OF_DAY;

            } else {
                if (DateUtility.DATE_FORMAT_YYYYMMDD.equals(targetDateTimeFormat)) {
                    // 月報
                    plusUnit = ChronoField.DAY_OF_MONTH;

                } else if (DateUtility.DATE_FORMAT_YYYYMM.equals(targetDateTimeFormat)) {
                    // 年報
                    plusUnit = ChronoField.MONTH_OF_YEAR;
                }
            }
        }

        if (plusUnit == null) {
            return null;
        }

        if (plusUnit == ChronoField.HOUR_OF_DAY) {
            targetDateTime = DateUtility.plusHour(baseDateTime, amount);

        } else if (plusUnit == ChronoField.DAY_OF_MONTH) {
            targetDateTime = DateUtility.plusDay(baseDateTime, amount);

        } else if (plusUnit == ChronoField.MONTH_OF_YEAR) {
            targetDateTime = DateUtility.plusMonth(baseDateTime, amount);

        } else if (plusUnit == ChronoField.YEAR) {
            targetDateTime = DateUtility.plusYear(baseDateTime, amount);
        }

        return DateUtility.changeDateFormat(targetDateTime, DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
    }

    /**
     * 指針値から使用量を算出.
     *
     * @param dmvA 指針値A
     * @param integerLengthA 指針値Aの整数部桁数
     * @param dmvB 指針値B
     * @param integerLengthB 指針値Bの整数部桁数
     * @param multi 乗率
     */
    private BigDecimal calcUseValueFromDmv(BigDecimal dmvA, int integerLengthA, BigDecimal dmvB, int integerLengthB,
            BigDecimal multi) {

        if (dmvA == null || dmvB == null) {
            return null;
        }

        if (multi == null) {
            multi = BigDecimal.ONE;
        }

        int integerLength = integerLengthA;
        if (integerLength < integerLengthB) {
            integerLength = integerLengthB;
        }

        if (dmvA.compareTo(dmvB) < 0) {
            // A < Bとなった場合
            // A = A + 10の[整数桁数]乗
            dmvA = dmvA.add(new BigDecimal(10).pow(integerLength));
        }

        // 60分指針値 = (A - B) × C[乗率]
        return dmvA.subtract(dmvB).multiply(multi);
    }

    /**
     * ロードサーベイデータMapを生成.
     *
     * @param meterMngIdList 対象メーター管理番号List
     * @param targetDateList 対象日時文字列List
     * @param targetMeterValueDataList 指定期間内の各メーターの使用量・指針値List
     * @param targetTotalAmountDataList 各メーターごとの合計量List
     * @param targetMeterInfoDataList 装置に紐付くメーター情報List
     * @param targetTenantInfoDataList テナント情報List
     * @return ロードサーベイデータMap
     */
    private Map<String, ListSmsDataViewResultData> createLoadSurveyDataMap(
            List<Long> meterMngIdList,
            List<String> targetDateList,
            Map<Long, List<ListSmsDataViewResultData>> meterValueDataListMap,
            Map<Long, List<ListSmsDataViewResultData>> meterInfoDataListMap,
            Map<Long, List<ListSmsDataViewResultData>> tenantInfoDataListMap) {

        String keyFormat = ListSmsDataViewResult.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = ListSmsDataViewResult.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = ListSmsDataViewResult.LOAD_SURVEY_DATA_MAP_START_ROW_NO;

        Map<String, ListSmsDataViewResultData> loadSurveyDataMap = new LinkedHashMap<>();

        for (int x = 0; x < meterMngIdList.size(); x++) {
            Long meterMngId = meterMngIdList.get(x);

            List<ListSmsDataViewResultData> meterValueDataList = meterValueDataListMap.get(meterMngId);
            List<ListSmsDataViewResultData> meterInfoDataList = meterInfoDataListMap.get(meterMngId);
            List<ListSmsDataViewResultData> tenantInfoDataList = tenantInfoDataListMap.get(meterMngId);

            ListSmsDataViewResultData meterInfoData = null;
            if (CollectionUtils.isNotEmpty(meterInfoDataList)) {
                meterInfoData = meterInfoDataList.get(0);
            }

            ListSmsDataViewResultData tenantInfoData = null;
            if (CollectionUtils.isNotEmpty(tenantInfoDataList)) {
                tenantInfoData = tenantInfoDataList.get(0);
            }

            ListSmsDataViewResultData loadSurveyData = new ListSmsDataViewResultData();

            loadSurveyData.setMeterMngId(meterMngId);

            if (meterInfoData != null) {
                loadSurveyData.setDevId(meterInfoData.getDevId());
                loadSurveyData.setMeterTypeName(meterInfoData.getMeterTypeName());
                loadSurveyData.setCo2Coefficient(meterInfoData.getCo2Coefficient());
                loadSurveyData.setUnitCo2Coefficient(meterInfoData.getUnitCo2Coefficient());
                loadSurveyData.setUnitUsageBased(meterInfoData.getUnitUsageBased());
                loadSurveyData.setMeterMngIdDisp(meterInfoData.getMeterMngIdDisp());
                loadSurveyData.setMulti(meterInfoData.getMulti());
            }

            if (tenantInfoData != null) {
                loadSurveyData.setDevId(tenantInfoData.getDevId());
                loadSurveyData.setTenantName(tenantInfoData.getTenantName());
            }

            Map<String, ListSmsDataViewResultData> targetDateMeterDataMap = createTargetDateMeterDataMap(
                    meterValueDataList);

            for (int y = 0; y < targetDateList.size(); y++) {
                String targetDate = targetDateList.get(y);

                ListSmsDataViewResultData meterValueData = targetDateMeterDataMap.get(targetDate);
                if (meterValueData == null) {
                    meterValueData = loadSurveyData;
                    meterValueData.setTargetDateTime(targetDate);

                } else {
                    meterValueData.setDevId(loadSurveyData.getDevId());
                    meterValueData.setMeterTypeName(loadSurveyData.getMeterTypeName());
                    meterValueData.setCo2Coefficient(loadSurveyData.getCo2Coefficient());
                    meterValueData.setUnitCo2Coefficient(loadSurveyData.getUnitCo2Coefficient());
                    meterValueData.setUnitUsageBased(loadSurveyData.getUnitUsageBased());
                    meterValueData.setMulti(loadSurveyData.getMulti());
                    meterValueData.setTenantName(loadSurveyData.getTenantName());
                }

                loadSurveyDataMap.put(String.format(keyFormat, (x + startColumnNo), (y + startRowNo)),
                        meterValueData);
            }
        }

        return loadSurveyDataMap;
    }

    /**
     * 対象日時メーターデータMapを生成.
     *
     * @param targetDataList 対象データList
     * @return 対象日時メーターデータMap
     */
    private Map<String, ListSmsDataViewResultData> createTargetDateMeterDataMap(
            List<ListSmsDataViewResultData> targetDataList) {

        Map<String, ListSmsDataViewResultData> targetDateMeterDataMap = new LinkedHashMap<>();

        if (CollectionUtils.isNotEmpty(targetDataList)) {
            for (ListSmsDataViewResultData targetData : targetDataList) {
                targetDateMeterDataMap.put(targetData.getTargetDateTime(), targetData);
            }
        }

        return targetDateMeterDataMap;
    }

}
