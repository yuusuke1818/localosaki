package jp.co.osaki.osol.api.dao.sms.collect.dataview;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.ListSmsDataViewParameter;
import jp.co.osaki.osol.api.request.sms.collect.dataview.ListSmsDataViewRequest;
import jp.co.osaki.osol.api.result.sms.collect.dataview.ListSmsDataViewResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDataViewMeterInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDataViewMeterValueServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDataViewTenantInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDemandDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsDemandSameMonthLastYearDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.dataview.ListSmsMaxDemandPast;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DATA_VIEW_HEADER;

/**
 * データ収集装置 データ表示機能 Daoクラス
 *
 * @author ozaki.y
 */
@Stateless
public class ListSmsDataViewDao extends OsolApiDao<ListSmsDataViewParameter> {

    private final ListSmsDataViewMeterInfoServiceDaoImpl meterInfoDaoImpl;
    private final ListSmsDataViewMeterValueServiceDaoImpl meterValueDaoImpl;
    private final ListSmsDataViewTenantInfoServiceDaoImpl tenantInfoDaoImpl;
    private final ListSmsDemandDaoImpl listSmsDemandDaoImpl;
    private final ListSmsDemandSameMonthLastYearDaoImpl listSmsDemandSameMonthLastYearDaoImpl;
    private final ListSmsMaxDemandPast listSmsMaxDemandPast;


    public ListSmsDataViewDao() {
        meterInfoDaoImpl = new ListSmsDataViewMeterInfoServiceDaoImpl();
        meterValueDaoImpl = new ListSmsDataViewMeterValueServiceDaoImpl();
        tenantInfoDaoImpl = new ListSmsDataViewTenantInfoServiceDaoImpl();
        listSmsDemandDaoImpl = new ListSmsDemandDaoImpl();
        listSmsDemandSameMonthLastYearDaoImpl = new ListSmsDemandSameMonthLastYearDaoImpl();
        listSmsMaxDemandPast = new ListSmsMaxDemandPast();
    }

    @Override
    public ListSmsDataViewResult query(ListSmsDataViewParameter parameter) throws Exception {
        String devId = parameter.getDevId();
        boolean isForwardDiract = parameter.isForwardDiract();
        boolean isUse = parameter.isUse();
        String dispTimeUnit = parameter.getDispTimeUnit();
        if (CheckUtility.isNullOrEmpty(dispTimeUnit)) {
        	dispTimeUnit = "30";
        }
        String targetDateTimeFormat = parameter.getTargetDateTimeFormat();
        if (CheckUtility.isNullOrEmpty(targetDateTimeFormat)) {
        	targetDateTimeFormat = DateUtility.DATE_FORMAT_YYYYMMDDHHMM;
        }
        Long buildingId = parameter.getBuildingId();

        ListSmsDataViewRequest request = parameter.getRequest();

        List<String> targetDateTimeList = request.getTargetDateTimeList();
        List<Long> meterMngIdList = request.getMeterMngIdList();

        ListSmsDataViewResultData param = new ListSmsDataViewResultData();
        param.setCorpId(parameter.getCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setTenant(parameter.isTenant());
        param.setDevId(devId);
        param.setForwardDiract(isForwardDiract);
        param.setUse(isUse);
        param.setDispTimeUnit(dispTimeUnit);
        param.setTargetDateTimeList(targetDateTimeList);
        param.setTargetDateTimeFormat(targetDateTimeFormat);

        // 装置に紐付くメーター情報
        List<ListSmsDataViewResultData> meterInfoDataList = getResultList(meterInfoDaoImpl, param);
//        Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap = createMeterDataListMap(meterInfoDataList);	// 2024-10-01
        Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap = createMeterDataListMapIsParamAdd(meterInfoDataList, isUse, isForwardDiract);	// 2024-10-01

        ListSmsDataViewResult result = new ListSmsDataViewResult();

        if (!meterInfoDataMap.isEmpty()) {
	        param.setMeterMngIdList(meterMngIdList);

	        // テナント情報
	        List<ListSmsDataViewResultData> tenantInfoDataList = getResultList(tenantInfoDaoImpl, param);
	        Map<Long, List<ListSmsDataViewResultData>> tenantInfoDataMap = createMeterDataListMap(tenantInfoDataList);

	        // 指定期間内の各メーターの使用量・指針値
	        List<ListSmsDataViewResultData> meterValueDataList = getResultList(meterValueDaoImpl, param);

	//        Map<Long, List<ListSmsDataViewResultData>> meterValueDataMap = createMeterValueDataMap(meterValueDataList,
	//                meterInfoDataMap, isUse, dispTimeUnit, targetDateTimeFormat, targetDateTimeList);
	        Map<Long, List<ListSmsDataViewResultData>> meterValueDataMap = createMeterValueDataMap(meterValueDataList,
	                meterInfoDataMap, isUse, dispTimeUnit, targetDateTimeFormat, targetDateTimeList, buildingId, isForwardDiract);

	        // 最大デマンドの検索(当月、当年)
	        List<ListSmsDataViewResultData> meterMaxDemandDataList = new ArrayList<>();
	        //各日または各月の最大デマンドを格納
	        Map<Long, Map<String, BigDecimal>> maxDemandDateMap = new HashMap<>();
	        //当月最大デマンドをメーターごとに
	        Map<Long, BigDecimal> maxDemandMap = new HashMap<>();

	        // 最大デマンドの検索(昨年同月)
	        List<ListSmsDataViewResultData> meterMaxDemandLastYearDataList = new ArrayList<>();
	        // 昨年同月最大デマンドをメーターごとに
	        Map<Long, BigDecimal> maxDemandLastYearMap = new HashMap<>();

	        // 最大デマンドの検索(過去最大)
	        List<ListSmsDataViewResultData> meterMaxDemandPastDataList = new ArrayList<>();
	        Map<Long, ListSmsDataViewResultData> maxDemandPastMap = new HashMap<>();


	        // 日報画面の場合,既存のデータでデマンドデータの検索を行う（新たに取得しにいかない）
	        if (DateUtility.DATE_FORMAT_YYYYMMDDHHMM.equals(targetDateTimeFormat)
	                && (SmsConstants.DISP_TIME_UNIT.M30.getVal().equals(dispTimeUnit)
	                        || SmsConstants.DISP_TIME_UNIT.M60.getVal().equals(dispTimeUnit))) {
	            for(Entry<Long, List<ListSmsDataViewResultData>> entry : meterValueDataMap.entrySet()) {
	                List<ListSmsDataViewResultData> meterValueList = entry.getValue();
	                // 使用量計算後に、最大デマンドを検索してセットしたListを改めて返却
	                meterValueList = setMaxDemand(meterValueList);
	                // 最大デマンドをsetしたもので上書き
	                meterValueDataMap.put(entry.getKey(), meterValueList);
	            }
	        }else {
	            // 最大デマンドの検索(当月、当年)
	            meterMaxDemandDataList = getResultList(listSmsDemandDaoImpl, param);
	            //各日または各月の最大デマンドを格納
	            maxDemandDateMap = convertMonthOrYearList(meterMaxDemandDataList, targetDateTimeList);
	            //当月の期間最大デマンドをメーターごとに
	            maxDemandMap = createMaxDemandSpesifiedPeriod(meterMaxDemandDataList);
	        }
	        // 最大デマンドの検索(昨年同月)
	        meterMaxDemandLastYearDataList = getResultList(listSmsDemandSameMonthLastYearDaoImpl, param);
	        // 昨年同月最大デマンドをメーターごとに
	        maxDemandLastYearMap = createMaxDemandLastYear(meterMaxDemandLastYearDataList);

	        // 最大デマンドの検索(過去最大)
	        meterMaxDemandPastDataList = getResultList(listSmsMaxDemandPast, param);
	        maxDemandPastMap = createMaxDemandPast(meterMaxDemandPastDataList);


//	        ListSmsDataViewResult result = new ListSmsDataViewResult();
	        result.setCountX(meterMngIdList.size());
	        result.setCountY(targetDateTimeList.size());
	        result.setLoadSurveyDataMap(createLoadSurveyDataMap(meterMngIdList, targetDateTimeList, meterValueDataMap,
	                meterInfoDataMap, tenantInfoDataMap, maxDemandMap, maxDemandDateMap, maxDemandLastYearMap, maxDemandPastMap));
        }
        return result;
    }

    /**
     * 年報のときのためにデータを変形する
     * 月報のときはリストをそのまま返却
     * @param dataList
     * @param targetDateTimeList
     * @return
     */
    private Map<Long, Map<String, BigDecimal>> convertMonthOrYearList(List<ListSmsDataViewResultData> dataList,  List<String> targetDateTimeList){
        Map<Long, Map<String, BigDecimal>> map = new HashMap<>();
        if(targetDateTimeList.size() > 27) {
            for(ListSmsDataViewResultData data : dataList) {
                Map<String, BigDecimal> dateDataMap = new HashMap<>();
                Long meterMngId = data.getMeterMngId();
                if(map.containsKey(meterMngId)) {
                    dateDataMap = map.get(meterMngId);
                }
                dateDataMap.put(data.getYmd(), data.getMaxDemand());
                map.put(meterMngId, dateDataMap);
            }
        }else {
            for(ListSmsDataViewResultData data : dataList) {
                Map<String, BigDecimal> dateDataMap = new HashMap<>();
                Long meterMngId = data.getMeterMngId();
                if(map.containsKey(meterMngId)) {
                    dateDataMap = map.get(meterMngId);
                }
                if(data.getMaxDemand() == null || (dateDataMap.containsKey(data.getYmd().substring(0, 6)) && dateDataMap.get(data.getYmd().substring(0, 6)) != null && dateDataMap.get(data.getYmd().substring(0, 6)).compareTo(data.getMaxDemand()) > 0)) {
                    continue;
                }
                dateDataMap.put(data.getYmd().substring(0, 6), data.getMaxDemand());
                map.put(meterMngId, dateDataMap);
            }
        }
        return map;
    }

    /**
     * 当月、当年最大デマンド
     * @param meterMaxDemandDataList
     * @return
     */
    private Map<Long, BigDecimal> createMaxDemandSpesifiedPeriod(List<ListSmsDataViewResultData> meterMaxDemandDataList){
        // 当月、当年 最大デマンド
        Map<Long, BigDecimal> maxDemandMap = new HashMap<>();
        for(ListSmsDataViewResultData data : meterMaxDemandDataList) {
            if(!maxDemandMap.containsKey(data.getMeterMngId()) && data.getMaxDemand() != null) {
                maxDemandMap.put(data.getMeterMngId(), data.getMaxDemand());
            }else if(data.getMaxDemand() != null){
                BigDecimal maxValue = maxDemandMap.get(data.getMeterMngId());
                if(maxValue.compareTo(data.getMaxDemand()) < 0) {
                    maxDemandMap.put(data.getMeterMngId(), data.getMaxDemand());
                }
            }
        }

        return maxDemandMap;
    }

    /**
     * 昨年同月 最大デマンド
     * @param meterMaxDemandDataList
     * @return
     */
    private Map<Long, BigDecimal> createMaxDemandLastYear(List<ListSmsDataViewResultData> meterMaxDemandLastYearDataList){
        // 当月、当年 最大デマンド
        Map<Long, BigDecimal> maxDemandMap = new HashMap<>();
        for(ListSmsDataViewResultData data : meterMaxDemandLastYearDataList) {
            if(!maxDemandMap.containsKey(data.getMeterMngId()) && data.getMaxDemand() != null) {
                maxDemandMap.put(data.getMeterMngId(), data.getMaxDemand());
            }else if(data.getMaxDemand() != null){
                BigDecimal maxValue = maxDemandMap.get(data.getMeterMngId());
                if(maxValue.compareTo(data.getMaxDemand()) < 0) {
                    maxDemandMap.put(data.getMeterMngId(), data.getMaxDemand());
                }
            }
        }

        return maxDemandMap;
    }

    /**
     * 過去 最大デマンド
     * @param meterMaxDemandDataList
     * @return
     */
    private Map<Long, ListSmsDataViewResultData> createMaxDemandPast(List<ListSmsDataViewResultData> meterMaxDemandPastDataList){
        // 当月、当年 最大デマンド
        Map<Long, ListSmsDataViewResultData> maxDemandMap = new HashMap<>();
        for(ListSmsDataViewResultData data : meterMaxDemandPastDataList) {
            ListSmsDataViewResultData resultData = new ListSmsDataViewResultData();
            if(!maxDemandMap.containsKey(data.getMeterMngId()) && data.getMaxDemand() != null) {
                resultData.setMaxDemandPast(data.getMaxDemand());
                resultData.setMaxDemandPastYmdDisp(data.getYmd().substring(0, 4) + "/" + data.getYmd().substring(4, 6));
                maxDemandMap.put(data.getMeterMngId(), resultData);
            }
            else if(data.getMaxDemand() != null){
                resultData = maxDemandMap.get(data.getMeterMngId());
                if(resultData.getMaxDemandPast().compareTo(data.getMaxDemand()) < 0) {
                    resultData.setMaxDemandPast(data.getMaxDemand());
                    resultData.setMaxDemandPastYmdDisp(data.getYmd().substring(0, 4) + "/" + data.getYmd().substring(4, 6));
                    maxDemandMap.put(data.getMeterMngId(), resultData);
                }
            }
        }

        return maxDemandMap;
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
     * メーターデータListMap(isUse/isForwardDiract付加)を生成. (2024-10-01)
     *
     * @param targetDataList 対象データList
     * @return メーターデータListMap
     */
    private Map<Long, List<ListSmsDataViewResultData>> createMeterDataListMapIsParamAdd(
            List<ListSmsDataViewResultData> targetDataList, boolean isUse, boolean isForwardDiract) {

        Map<Long, List<ListSmsDataViewResultData>> meterDataListMap = new LinkedHashMap<>();
        for (ListSmsDataViewResultData targetData : targetDataList) {
            Long meterMngId = targetData.getMeterMngId();

            List<ListSmsDataViewResultData> meterDataList = meterDataListMap.getOrDefault(meterMngId,
                    new ArrayList<>());
            targetData.setUse(isUse);
            targetData.setForwardDiract(isForwardDiract);
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
     * @param buildingId 建物ID（外部公開API試験用）
     * @param isForwardDiract 正方向フラグ
     * @return 各メーターの使用量・指針値ListMap
     */
//    private Map<Long, List<ListSmsDataViewResultData>> createMeterValueDataMap(
//            List<ListSmsDataViewResultData> targetDataList, Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap,
//            boolean isUse, String dispTimeUnit, String targetDateTimeFormat, List<String> targetDateTimeList) {
    private Map<Long, List<ListSmsDataViewResultData>> createMeterValueDataMap(
            List<ListSmsDataViewResultData> targetDataList, Map<Long, List<ListSmsDataViewResultData>> meterInfoDataMap,
            boolean isUse, String dispTimeUnit, String targetDateTimeFormat, List<String> targetDateTimeList, Long buildingId, boolean isForwardDiract) {

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

            // 企業ID
            String corpId = meterInfoData.getCorpId();	/* 外部公開API試験用 */
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
                resultMeterData.setCorpId(corpId);	/* 外部公開API試験用 */
                resultMeterData.setBuildingId(buildingId);	/* 外部公開API試験用 */
                resultMeterData.setForwardDiract(isForwardDiract);	/* 外部公開API試験用 */
                resultMeterData.setUse(isUse);	/* 外部公開API試験用 */
                resultMeterData.setDevId(devId);
                resultMeterData.setMeterMngId(meterMngId);
                resultMeterData.setTargetDateTime(targetDateTime);
// コメントアウトは、使用量または指針値のみを応答する場合の対応
/**/                if (isUse) {
/**/					resultMeterData.setKwh30(kwh30);
/**/					resultMeterData.setTotalKwh30(totalKwh30);
/**/                } //else {
/**/            		resultMeterData.setDmvKwh(dmvKwh);
//                }
//                resultMeterData.setKwh30(kwh30);
//                resultMeterData.setDmvKwh(dmvKwh);
//                resultMeterData.setTotalKwh30(totalKwh30);
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
     *
     * @param meterValueList
     * @return
     */
    private List<ListSmsDataViewResultData> setMaxDemand(List<ListSmsDataViewResultData> meterValueList) {
        // 最大デマンドを2倍にする
        BigDecimal calcMaxdemand = new BigDecimal("2");
        BigDecimal maxDemand = null;
        BigDecimal maxDemandPeriod = null;
        for(ListSmsDataViewResultData data : meterValueList) {
            if(maxDemandPeriod == null) {
                maxDemandPeriod = data.getKwh30() == null ? null : data.getKwh30().multiply(calcMaxdemand);
            }else if(data.getKwh30() != null && maxDemandPeriod.compareTo(data.getKwh30().multiply(calcMaxdemand)) < 0) {
                maxDemandPeriod =  data.getKwh30() == null ? null : data.getKwh30().multiply(calcMaxdemand);
            }
        }

        for(int i = 0; i < meterValueList.size(); i++) {
            maxDemand = meterValueList.get(i).getKwh30() == null ? null : meterValueList.get(i).getKwh30().multiply(calcMaxdemand);
            if(maxDemand != null) {
                meterValueList.get(i).setMaxDemand(maxDemand);
            }

            if(maxDemandPeriod != null) {
                meterValueList.get(i).setMaxDemandSpecifiedPeriodDisp(maxDemandPeriod.toString());
            }
        }

        return meterValueList;
    }

    /**
     * 指定期間内の合計使用量を算出.
     *
     * @param targetDateMeterDataMap 対象日時をキーとする対象データListMap
     * @param multi 乗率
     * @return 指定期間内の合計使用量
     */
    private BigDecimal calcTotalUseValue(Map<String, ListSmsDataViewResultData> targetDateMeterDataMap, BigDecimal multi) {

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
            Map<Long, List<ListSmsDataViewResultData>> tenantInfoDataListMap,
            Map<Long, BigDecimal> maxDemandMap,
            Map<Long, Map<String, BigDecimal>> maxDemandDateMap,
            Map<Long, BigDecimal> maxDemandLastYearMap,
            Map<Long, ListSmsDataViewResultData> maxDemandPastMap) {

        String keyFormat = SmsConstants.LOAD_SURVEY_DATA_MAP_KEY_FORMAT;
        int startColumnNo = SmsConstants.LOAD_SURVEY_DATA_MAP_START_COLUMN_NO;
        int startRowNo = DATA_VIEW_HEADER.values().length;

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
                loadSurveyData.setMeterMngId(meterInfoData.getMeterMngId());	// 2024-10-30 meterMngId追加
                loadSurveyData.setMeterMngIdDisp(meterInfoData.getMeterMngIdDisp());
                loadSurveyData.setMulti(meterInfoData.getMulti());
                loadSurveyData.setMeterId(meterInfoData.getMeterId());
            }

            if (tenantInfoData != null) {
                loadSurveyData.setDevId(tenantInfoData.getDevId());
                loadSurveyData.setTenantName(tenantInfoData.getTenantName());
            }

            Map<String, ListSmsDataViewResultData> targetDateMeterDataMap = createTargetDateMeterDataMap(
                    meterValueDataList);
            Map<String, BigDecimal> dateDataMap = null;
            if(maxDemandDateMap.containsKey(meterMngId)) {
                dateDataMap = maxDemandDateMap.get(meterMngId);
            }

            for (int y = 0; y < targetDateList.size(); y++) {
                String targetDate = targetDateList.get(y);

                ListSmsDataViewResultData meterValueData = targetDateMeterDataMap.get(targetDate);
                if (meterValueData == null) {
                    meterValueData = new ListSmsDataViewResultData(loadSurveyData.getDevId(), loadSurveyData.getMulti(), loadSurveyData.getMeterId(), loadSurveyData.getMeterTypeName(), loadSurveyData.getCo2Coefficient(), loadSurveyData.getUnitCo2Coefficient(), loadSurveyData.getUnitUsageBased(), loadSurveyData.getTenantName(), loadSurveyData.getMeterMngIdDisp());
                    meterValueData.setTargetDateTime(targetDate);
                } else {
                    meterValueData.setDevId(loadSurveyData.getDevId());
                    meterValueData.setMeterTypeName(loadSurveyData.getMeterTypeName());
                    meterValueData.setCo2Coefficient(loadSurveyData.getCo2Coefficient());
                    meterValueData.setUnitCo2Coefficient(loadSurveyData.getUnitCo2Coefficient());
                    meterValueData.setUnitUsageBased(loadSurveyData.getUnitUsageBased());
                    meterValueData.setMulti(loadSurveyData.getMulti());
                    meterValueData.setMeterId(loadSurveyData.getMeterId());
                    meterValueData.setTenantName(loadSurveyData.getTenantName());
                }
                meterValueData.setUse(meterInfoData.isUse());	// 2024-10-01
                meterValueData.setForwardDiract(meterInfoData.isForwardDiract());	// 2024-10-01
                meterValueData.setMeterMngId(meterInfoData.getMeterMngId());		// 2024-10-30 meterMngId追加

                if(maxDemandMap != null && !maxDemandMap.isEmpty() && maxDemandMap.containsKey(meterMngId)) {
                    meterValueData.setMaxDemandSpecifiedPeriod(maxDemandMap.get(meterMngId));
                    meterValueData.setMaxDemandSpecifiedPeriodDisp(maxDemandMap.get(meterMngId).toString());
                }

                // 月報のときの条件
                if(targetDateList.size() > 27 && dateDataMap != null && !dateDataMap.isEmpty() && dateDataMap.containsKey(targetDate.substring(0, 8))) {
                    meterValueData.setMaxDemand(dateDataMap.get(targetDate.substring(0, 8)));
                }
                // 年報のとき
                else if(targetDateList.size() < 28 && dateDataMap != null && !dateDataMap.isEmpty() && dateDataMap.containsKey(targetDate.substring(0, 6))){
                    meterValueData.setMaxDemand(dateDataMap.get(targetDate.substring(0, 6)));
                }

                if(maxDemandLastYearMap != null && !maxDemandLastYearMap.isEmpty() && maxDemandLastYearMap.containsKey(meterMngId)) {
                    meterValueData.setMaxDemandLastYear(maxDemandLastYearMap.get(meterMngId));
                    meterValueData.setMaxDemandLastYearDisp(maxDemandLastYearMap.get(meterMngId).toString());
                }

                if(maxDemandPastMap != null && !maxDemandPastMap.isEmpty() && maxDemandPastMap.containsKey(meterMngId)) {
                    meterValueData.setMaxDemandPast(maxDemandPastMap.get(meterMngId).getMaxDemandPast());
                    meterValueData.setMaxDemandPastDisp(maxDemandPastMap.get(meterMngId).getMaxDemandPast().toString());
                    meterValueData.setMaxDemandPastYmdDisp(maxDemandPastMap.get(meterMngId).getMaxDemandPastYmdDisp());
                }

//                loadSurveyDataMap.put(String.format(keyFormat, (x + startColumnNo), (y + startRowNo)),
//                        meterValueData);
//                String keyDevIdXY = loadSurveyData.getDevId() + "_" + String.format(keyFormat,  (x + startColumnNo), (y + startRowNo));
                String keyDevIdXY = String.format(keyFormat,  (x + startColumnNo), (y + startRowNo));
                loadSurveyDataMap.put(keyDevIdXY, meterValueData);
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
