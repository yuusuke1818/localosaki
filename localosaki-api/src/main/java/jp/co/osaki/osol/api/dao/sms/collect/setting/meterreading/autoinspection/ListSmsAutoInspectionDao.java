package jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionMeterTypeInfoServiceDaoImpl;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ取得API Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class ListSmsAutoInspectionDao extends OsolApiDao<ListSmsAutoInspectionParameter> {

    private final ListSmsAutoInspectionMeterTypeInfoServiceDaoImpl meterTypeInfoImpl;
    private final ListSmsAutoInspectionInfoServiceDaoImpl autoInspectionInfoImpl;

    public ListSmsAutoInspectionDao() {
        meterTypeInfoImpl = new ListSmsAutoInspectionMeterTypeInfoServiceDaoImpl();
        autoInspectionInfoImpl = new ListSmsAutoInspectionInfoServiceDaoImpl();
    }

    @Override
    public ListSmsAutoInspectionResult query(ListSmsAutoInspectionParameter parameter) throws Exception {
        ListSmsAutoInspectionResultData param = new ListSmsAutoInspectionResultData();
        param.setCorpId(parameter.getCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setTenant(parameter.isTenant());

        // メーター種別情報Map
        Map<Long, ListSmsAutoInspectionResultData> meterTypeInfoMap = createMeterTypeKeyDataMap(
                getResultList(meterTypeInfoImpl, param));

        // 自動検針データList
        List<ListSmsAutoInspectionResultData> autoInspDataList = getResultList(autoInspectionInfoImpl, param);

        // 装置-メーター種別をキーとする自動検針データMap
        Map<String, Map<Long, ListSmsAutoInspectionResultData>> devMeterTypeAutoInspInfoMap = createDevMeterTypeAutoInspectionInfoMap(
                autoInspDataList);

        // 自動検針データMap
        Map<Long, ListSmsAutoInspectionResultData> autoInspectionInfoMap = createMeterTypeKeyDataMap(
                autoInspDataList, true);

        ListSmsAutoInspectionResult result = new ListSmsAutoInspectionResult();
        result.setAutoInspectionInfoList(createAutoInspectionInfoList(meterTypeInfoMap, autoInspectionInfoMap));
        result.setDevMeterTypeAutoInspInfoMap(devMeterTypeAutoInspInfoMap);

        return result;
    }

    /**
     * 装置-メーター種別をキーとする自動検針データMapを生成.
     *
     * @param autoInspectionDataList 自動検針データList
     * @return 装置-メーター種別をキーとする自動検針データMap
     */
    private Map<String, Map<Long, ListSmsAutoInspectionResultData>> createDevMeterTypeAutoInspectionInfoMap(
            List<ListSmsAutoInspectionResultData> autoInspectionDataList) {

        Map<String, Map<Long, ListSmsAutoInspectionResultData>> resultMap = new LinkedHashMap<>();

        for (ListSmsAutoInspectionResultData autoInspectionData : autoInspectionDataList) {
            String devId = autoInspectionData.getDevId();
            Long meterType = autoInspectionData.getMeterType();

            Map<Long, ListSmsAutoInspectionResultData> meterTypeKeyMap = resultMap.getOrDefault(devId,
                    new LinkedHashMap<>());

            meterTypeKeyMap.put(meterType, autoInspectionData);
            resultMap.put(devId, meterTypeKeyMap);
        }

        return resultMap;
    }

    /**
     * メーター種別をキーとするデータMapを生成.
     *
     * @param targetDataList 対象データList
     * @return メーター種別をキーとするデータMap
     */
    private Map<Long, ListSmsAutoInspectionResultData> createMeterTypeKeyDataMap(
            List<ListSmsAutoInspectionResultData> targetDataList) {

        return createMeterTypeKeyDataMap(targetDataList, false);
    }

    /**
     * メーター種別をキーとするデータMapを生成.
     *
     * @param targetDataList 対象データList
     * @param sameMeterTypeCheckFlg 同一メーター種別チェックフラグ
     * @return メーター種別をキーとするデータMap
     */
    private Map<Long, ListSmsAutoInspectionResultData> createMeterTypeKeyDataMap(
            List<ListSmsAutoInspectionResultData> targetDataList, boolean sameMeterTypeCheckFlg) {

        Set<Long> meterTypeSet = new HashSet<>();

        Map<Long, ListSmsAutoInspectionResultData> meterTypeKeyDataMap = new LinkedHashMap<>();
        for (ListSmsAutoInspectionResultData targetData : targetDataList) {
            Long meterType = targetData.getMeterType();
            if (sameMeterTypeCheckFlg && !meterTypeSet.add(meterType)) {
                break;
            }

            meterTypeKeyDataMap.put(meterType, targetData);
        }

        return meterTypeKeyDataMap;
    }

    /**
     * 自動検針データListを生成.
     *
     * @param meterTypeInfoMap メーター種別情報Map
     * @param autoInspectionInfoMap 自動検針データMap
     * @return 自動検針データList
     */
    private List<ListSmsAutoInspectionResultData> createAutoInspectionInfoList(
            Map<Long, ListSmsAutoInspectionResultData> meterTypeInfoMap,
            Map<Long, ListSmsAutoInspectionResultData> autoInspectionInfoMap) {

        List<ListSmsAutoInspectionResultData> autoInspectionInfoList = new ArrayList<>();

        Set<Entry<Long, ListSmsAutoInspectionResultData>> meterTypeInfoMapEntrySet = meterTypeInfoMap.entrySet();
        for (Entry<Long, ListSmsAutoInspectionResultData> meterTypeInfoMapEntry : meterTypeInfoMapEntrySet) {
            Long meterType = meterTypeInfoMapEntry.getKey();
            ListSmsAutoInspectionResultData resultData = meterTypeInfoMapEntry.getValue();

            ListSmsAutoInspectionResultData autoInspectionInfo = autoInspectionInfoMap.get(meterType);
            if (autoInspectionInfo != null) {
                resultData.setInspectionMonth(autoInspectionInfo.getInspectionMonth());
                resultData.setInspectionDay(autoInspectionInfo.getInspectionDay());
                resultData.setInspectionHour(autoInspectionInfo.getInspectionHour());
                resultData.setVersion(autoInspectionInfo.getVersion());
            }

            autoInspectionInfoList.add(resultData);
        }

        return autoInspectionInfoList;
    }
}
