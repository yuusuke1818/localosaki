package jp.co.osaki.sms.bean.sms.collect.setting.errorinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.api.parameter.sms.collect.setting.errorinfo.ListSmsErrorInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.errorinfo.ListSmsErrorInfoResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.errorinfo.ListSmsErrorInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.errorinfo.ListSmsErrorInfoResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;

/**
 * データ収集装置 機器管理 異常情報画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectSettingErrorInfoBean")
@ConversationScoped
public class InfoBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = 482729942663651013L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "errorInfo";

    /** 1行当たりの表示列数. */
    private static final int DISP_COLUMN_COUNT = 5;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** データ収集装置 装置異常List. */
    private List<ErrorInfoData> devErrorInfoList;

    /** コンセントレーター異常List. */
    private List<List<List<ErrorInfoData>>> concentErrorInfoList;

    /** 通信端末異常・メータ異常List. */
    private List<List<List<ErrorInfoData>>> meterErrorInfoList;

    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private GenericTypeList genericTypeList;

    /**
     * 初期処理.
     *
     * @return 遷移先
     */
    @Override
    public String init() {
        conversationStart();

        return FORWARD_STR;
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        setBuildingInfo(buildingInfo);

        executeReload();

        return init();
    }

    /**
     * 最新の状態に更新.
     *
     * @return 遷移先
     */
    @Logged
    public String reload() {
        executeReload();

        return FORWARD_STR;
    }

    /**
     * 異常情報を更新.
     */
    private void executeReload() {
        ListInfo buildingInfo = getBuildingInfo();

        ListSmsErrorInfoResult errorInfoData = getErrorInfoData(buildingInfo);

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());
        boolean isTenant = buildingInfo.getTenantFlg();

        Map<String, String> devIdMap = toolsPullDownList.getDevPrm(corpId, buildingId, isTenant);
        if (MapUtils.isEmpty(devIdMap)) {
            return;
        }

        List<ErrorInfoData> devErrorInfoList = createDevErrorInfoList(errorInfoData.getDevPrmMap(),
                devIdMap, genericTypeList.getSmsDevSta());
        setDevErrorInfoList(devErrorInfoList);

        devIdMap = new LinkedHashMap<>();
        for (ErrorInfoData devErrorInfo : devErrorInfoList) {
            devIdMap.put(devErrorInfo.getDevName(), devErrorInfo.getDevId());
        }

        setConcentErrorInfoList(createErrorInfoMultiColumnTableList(SmsConstants.MAX_CONCENT_COUNT,
                errorInfoData.getConcentMap(), genericTypeList.getSmsConcentSta(), devIdMap));

        setMeterErrorInfoList(createErrorInfoMultiColumnTableList(SmsConstants.MAX_METER_COUNT,
                errorInfoData.getMeterMap(), genericTypeList.getSmsMeterSta(), devIdMap));
    }

    /**
     * 異常情報データを取得.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 異常情報データ
     */
    private ListSmsErrorInfoResult getErrorInfoData(ListInfo buildingInfo) {
        ListSmsErrorInfoParameter parameter = new ListSmsErrorInfoParameter();
        parameter.setCorpId(buildingInfo.getCorpId());
        parameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
        parameter.setTenant(buildingInfo.getTenantFlg());

        return callApiPost(parameter, ListSmsErrorInfoResponse.class).getResult();
    }

    /**
     * データ収集装置 装置異常Listを生成.
     *
     * @param devPrmMap 装置情報Map
     * @param devIdMap 装置ID・名称Map
     * @param statusDispMap ステータス表示Map
     * @return データ収集装置 装置異常List
     */
    private List<ErrorInfoData> createDevErrorInfoList(Map<String, Map<Long, ListSmsErrorInfoResultData>> devPrmMap,
            Map<String, String> devIdMap, Map<String, String> statusDispMap) {

        List<ErrorInfoData> devErrorInfoList = new ArrayList<>();

        Set<Entry<String, String>> devIdMapEntrySet = devIdMap.entrySet();

        int devNo = 1;
        int noNameDevCount = 0;
        for (Entry<String, String> devIdMapEntry : devIdMapEntrySet) {
            String devId = devIdMapEntry.getValue();
            String devName = devIdMapEntry.getKey();

            ListSmsErrorInfoResultData devErrorInfo = new ArrayList<>(devPrmMap.get(devId).values()).get(0);

            devErrorInfoList.add(
                    createErrorInfo(createEmptyErrorInfoList(1, statusDispMap).get(0), devErrorInfo, devId, devName));

            if (CheckUtility.isNullOrEmpty(devErrorInfo.getDevName())) {
                noNameDevCount++;
            }

            devNo++;
        }

        int dataCount = SmsConstants.MAX_DEV_COUNT;
        if (dataCount > devNo) {
            for (int i = devNo; i <= dataCount; i++) {
                devErrorInfoList.add(createErrorInfo(createEmptyErrorInfoList(1, statusDispMap).get(0), null, null,
                        String.format(SmsConstants.DEFAULT_DEV_NAME_FORMAT, ++noNameDevCount)));
            }
        }

        return devErrorInfoList;
    }

    /**
     * 異常情報複数列テーブル用Listを生成.
     *
     * @param dataCount 生成データ数
     * @param dbDataMap DBデータMap
     * @param statusDispMap ステータス表示Map
     * @param devIdMap 装置ID・名称Map
     * @return 異常情報複数列テーブル用List
     */
    private List<List<List<ErrorInfoData>>> createErrorInfoMultiColumnTableList(int dataCount,
            Map<String, Map<Long, ListSmsErrorInfoResultData>> dbDataMap, Map<String, String> statusDispMap,
            Map<String, String> devIdMap) {

        List<List<ErrorInfoData>> concentErrorInfoAllList = new ArrayList<>();

        Set<Entry<String, String>> devIdMapEntrySet = devIdMap.entrySet();

        for (Entry<String, String> devIdMapEntry : devIdMapEntrySet) {
            String devId = devIdMapEntry.getValue();
            String devName = devIdMapEntry.getKey();

            Map<Long, ListSmsErrorInfoResultData> dbDataInnerMap = null;
            if (!CheckUtility.isNullOrEmpty(devId)) {
                dbDataInnerMap = dbDataMap.get(devId);
            }

            concentErrorInfoAllList.add(createErrorInfoList(createEmptyErrorInfoList(dataCount, statusDispMap),
                    dbDataInnerMap, devId, devName));
        }

        return createMultiColumnTableList(concentErrorInfoAllList, DISP_COLUMN_COUNT, dataCount);
    }

    /**
     * 異常情報Listを生成.
     *
     * @param emptyErrorInfoList 異常情報空データList
     * @param dbDataMap DBデータMap
     * @param devId 装置ID
     * @param devName 装置名称
     * @return 異常情報List
     */
    private List<ErrorInfoData> createErrorInfoList(List<ErrorInfoData> emptyErrorInfoList,
            Map<Long, ListSmsErrorInfoResultData> dbDataMap, String devId, String devName) {

        List<ErrorInfoData> errorInfoList = emptyErrorInfoList;
        for (int i = 0; i < errorInfoList.size(); i++) {
            ErrorInfoData errorInfo = errorInfoList.get(i);

            long dataId = (long) i + 1;
            ListSmsErrorInfoResultData dbData = null;
            if (dbDataMap != null) {
                dbData = dbDataMap.get(dataId);
            }

            createErrorInfo(errorInfo, dbData, devId, devName);
        }

        return errorInfoList;
    }

    /**
     * 異常情報データを生成.
     *
     * @param errorInfo 異常情報データ
     * @param dbData DBデータ
     * @param devId 装置ID
     * @param devName 装置名称
     * @return 異常情報データ
     */
    private ErrorInfoData createErrorInfo(ErrorInfoData errorInfo, ListSmsErrorInfoResultData dbData, String devId,
            String devName) {

        errorInfo.setDevId(devId);
        errorInfo.setDevName(devName);

        if (dbData != null) {
            errorInfo.setDevSta(dbData.getDevSta());
            errorInfo.setConcentId(dbData.getConcentId());
            errorInfo.setConcentSta(dbData.getConcentSta());
            errorInfo.setMeterMngId(dbData.getMeterMngId());
            errorInfo.setMeterSta(dbData.getMeterSta());
            errorInfo.setTermSta(dbData.getTermSta());
        }

        return errorInfo;
    }

    /**
     * 異常情報空データListを生成.
     *
     * @param createCount 生成データ数
     * @param statusDispMap ステータス表示Map
     * @return 異常情報空データList
     */
    private List<ErrorInfoData> createEmptyErrorInfoList(int createCount, Map<String, String> statusDispMap) {
        List<ErrorInfoData> emptyErrorInfoList = new ArrayList<>();

        for (int i = 1; i <= createCount; i++) {
            long id = (long) i;

            ErrorInfoData errorInfoData = new ErrorInfoData(statusDispMap);
            errorInfoData.setConcentId(id);
            errorInfoData.setMeterMngId(id);

            emptyErrorInfoList.add(errorInfoData);
        }

        return emptyErrorInfoList;
    }

    /**
     * 複数列テーブル用Listを生成.
     *
     * @param errorInfoAllList 異常情報全件List
     * @param maxColumnCount 1行当たりの表示列数
     * @param maxRowCount 1列当たりの表示行数
     * @return 複数列テーブル用List
     */
    private List<List<List<ErrorInfoData>>> createMultiColumnTableList(List<List<ErrorInfoData>> errorInfoAllList,
            int maxColumnCount, int maxRowCount) {

        // テーブルList
        List<List<List<ErrorInfoData>>> multiColumnTableList = new ArrayList<>();

        // 行List
        List<List<ErrorInfoData>> rowList = new ArrayList<>();

        // 行Map
        Map<Integer, List<ErrorInfoData>> rowMap = new LinkedHashMap<>();

        for (int columnIndex = 0; columnIndex < errorInfoAllList.size(); columnIndex++) {
            // 装置単位
            List<ErrorInfoData> errorInfoRowList = errorInfoAllList.get(columnIndex);
            for (int rowIndex = 0; rowIndex < errorInfoRowList.size(); rowIndex++) {
                ErrorInfoData errorInfo = errorInfoRowList.get(rowIndex);

                // 列List
                List<ErrorInfoData> columnList = rowMap.getOrDefault(rowIndex, new ArrayList<>());

                columnList.add(errorInfo);
                if (columnList.size() >= maxColumnCount) {
                    // 1行当たりの上限表示列数に達した場合

                    rowList.add(columnList);
                    columnList = new ArrayList<>();

                    if (rowList.size() >= maxRowCount) {
                        // 1列当たりの上限表示行数(= 1テーブル分のデータ行列数)に達した場合
                        multiColumnTableList.add(rowList);
                        rowList = new ArrayList<>();
                    }
                }

                rowMap.put(rowIndex, columnList);
            }
        }

        if (!rowMap.isEmpty()) {
            Set<Entry<Integer, List<ErrorInfoData>>> rowMapEntrySet = rowMap.entrySet();
            for (Entry<Integer, List<ErrorInfoData>> rowMapEntry : rowMapEntrySet) {
                List<ErrorInfoData> columnList = rowMapEntry.getValue();

                if (!columnList.isEmpty()) {
                    // 1行当たりの上限表示列数に達していない行が存在する場合
                    int columnListSize = columnList.size();
                    for (int i = (columnListSize - 1); i < maxColumnCount; i++) {
                        columnList.add(new ErrorInfoData());
                    }

                    rowList.add(columnList);
                }
            }

            if (!rowList.isEmpty()) {
                multiColumnTableList.add(rowList);
            }
        }

        return multiColumnTableList;
    }

    public ListInfo getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo(ListInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public List<ErrorInfoData> getDevErrorInfoList() {
        return devErrorInfoList;
    }

    public void setDevErrorInfoList(List<ErrorInfoData> devErrorInfoList) {
        this.devErrorInfoList = devErrorInfoList;
    }

    public List<List<List<ErrorInfoData>>> getConcentErrorInfoList() {
        return concentErrorInfoList;
    }

    public void setConcentErrorInfoList(List<List<List<ErrorInfoData>>> concentErrorInfoList) {
        this.concentErrorInfoList = concentErrorInfoList;
    }

    public List<List<List<ErrorInfoData>>> getMeterErrorInfoList() {
        return meterErrorInfoList;
    }

    public void setMeterErrorInfoList(List<List<List<ErrorInfoData>>> meterErrorInfoList) {
        this.meterErrorInfoList = meterErrorInfoList;
    }
}
