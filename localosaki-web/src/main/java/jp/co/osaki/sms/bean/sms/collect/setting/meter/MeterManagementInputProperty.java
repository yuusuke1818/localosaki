package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

/**
 * メーター管理画面 登録/更新時のインプット情報を持つプロパティ
 *
 * @author kimura.m
 */
@Dependent
public class MeterManagementInputProperty implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 5286577893240907971L;

    /** 接続先 装置ID */
    private String devId;
    /** メーター管理番号 */
    private Long meterMngId;
    /** 建物番号 */
    private String buildingNo;
    /** ユーザーコード */
    private String tenantId;
    /** 負荷制限 */
    private String loadlimitMode;
    /** 対象区分(LTE-M用) */
    private String loadlimitTarget;
    /** 負荷電流 */
    private String loadCurrent;
    /** (臨時)負荷電流 */
    private String tempLoadCurrent;
    /** 自動投入 */
    private String autoInjection;
    /** (臨時)自動投入 */
    private String tempAutoInjection;
    /** 開閉器動作カウント */
    private String breakerActCount;
    /** (臨時)開閉器動作カウント */
    private String tempBreakerActCount;
    /** 開閉器カウントクリア */
    private String countClear;
    /** (臨時)開閉器カウントクリア */
    private String tempCountClear;
    /** 開閉区分 */
    private String openMode;
    /** 計器ID */
    private String meterId;
    /**  コメント */
    private String memo;
    /** メーター種別 */
    private Long meterType;
    /** 西暦・和暦表示確認用フラグ */
    private String dispYearFlg;
    /** 検満年月_元号 */
    private String examEndYmGengo;
    /** 検満年月_年 */
    private String examEndYear;
    /** 検満年月_年 和暦用 */
    private String examEndYearWareki;
    /** 検満年月_月 */
    private String examEndMonth;
    /** 検満通知フラグ */
    private String examNotice;
    /** version */
    private int version;
    /** 建物ID */
    private Long buildingId;
    /** 乗率 */
    private String multi;
    /** 送信フラグ */
    private boolean sendFlg;
    /** 開閉制御_送信フラグ */
    private boolean sendFlgSwitch;
    /** 負荷制限_送信フラグ */
    private boolean sendFlgLoadlimit;

    // パルス専用
    /** 現在値 */
    private String currentData;
    /** 現在値（保持用） */
    private String currentDataHidden;
    /** 現在値変更フラグ */
    private boolean currentDataChg;
    /** パルス種別 */
    private String pulseType;
    /** パルス種別(保持用) */
    private String pulseTypeHidden;
    /** パルス種別変更フラグ */
    private boolean pulseTypeChg;
    /** パルス重み */
    private String pulseWeight;
    /** パルス重み(保持用) */
    private String pulseWeightHidden;
    /** パルス重み変更フラグ */
    private boolean pulseWeightChg;

    // ハンディ専用
    /** 無線メーター種別 */
    private String wirelessType;
    /** ハンディ種別(無線メーター種別(wirelessType)の名称) */
    private String handyType;
    /** 無線ID */
    private String wirelessId;
    /** リレー1無線ID */
    private String hop1Id;
    /** リレー2無線ID */
    private String hop2Id;
    /** リレー3無線ID */
    private String hop3Id;
    /** ポーリンググループNo */
    private String pollingId;

    public MeterManagementInputProperty() {
    }

    /**
     * コンストラクタ
     *
     * @param property
     */
    public MeterManagementInputProperty(MeterManagementInputProperty property) {
        this.devId = property.devId;
        this.meterMngId = property.meterMngId;
        this.buildingNo = property.buildingNo;
        this.tenantId = property.tenantId;
        this.loadlimitMode = property.loadlimitMode;
        this.loadlimitTarget = property.loadlimitTarget;
        this.loadCurrent = property.loadCurrent;
        this.tempLoadCurrent = property.tempLoadCurrent;
        this.autoInjection = property.autoInjection;
        this.tempAutoInjection = property.tempAutoInjection;
        this.breakerActCount = property.breakerActCount;
        this.tempBreakerActCount = property.tempBreakerActCount;
        this.countClear = property.countClear;
        this.tempCountClear = property.tempCountClear;
        this.openMode = property.openMode;
        this.meterId = property.meterId;
        this.memo = property.memo;
        this.meterType = property.meterType;
        this.dispYearFlg = property.dispYearFlg;
        this.examEndYmGengo = property.examEndYmGengo;
        this.examEndYear = property.examEndYear;
        this.examEndYearWareki = property.examEndYearWareki;
        this.examEndMonth = property.examEndMonth;
        this.examNotice = property.examNotice;
        this.version = property.version;
        this.buildingId = property.buildingId;
        this.multi = property.multi;
        this.sendFlg = property.sendFlg;
        this.sendFlgSwitch = property.sendFlgSwitch;
        this.sendFlgLoadlimit = property.sendFlgLoadlimit;
        this.currentData = property.currentData;
        this.currentDataHidden = property.currentDataHidden;
        this.currentDataChg = property.currentDataChg;
        this.pulseType = property.pulseType;
        this.pulseTypeHidden = property.pulseTypeHidden;
        this.pulseTypeChg = property.pulseTypeChg;
        this.pulseWeight = property.pulseWeight;
        this.pulseWeightHidden = property.pulseWeightHidden;
        this.pulseWeightChg = property.pulseWeightChg;
        this.wirelessType = property.wirelessType;
        this.handyType = property.handyType;
        this.wirelessId = property.wirelessId;
        this.hop1Id = property.hop1Id;
        this.hop2Id = property.hop2Id;
        this.hop3Id = property.hop3Id;
        this.pollingId = property.pollingId;
    }

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }
    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }
    /**
     * @return meterMngId
     */
    public Long getMeterMngId() {
        return meterMngId;
    }
    /**
     * @param meterMngId セットする meterMngId
     */
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }
    /**
     * @return buildingNo
     */
    public String getBuildingNo() {
        return buildingNo;
    }
    /**
     * @param buildingNo セットする buildingNo
     */
    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }
    /**
     * @return tenantId
     */
    public String getTenantId() {
        return tenantId;
    }
    /**
     * @param tenantId セットする tenantId
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    /**
     * @return loadlimitMode
     */
    public String getLoadlimitMode() {
        return loadlimitMode;
    }
    /**
     * @param loadlimitMode セットする loadlimitMode
     */
    public void setLoadlimitMode(String loadlimitMode) {
        this.loadlimitMode = loadlimitMode;
    }
    /**
     * @return loadlimitTarget
     */
    public String getLoadlimitTarget() {
        return loadlimitTarget;
    }
    /**
     * @param loadlimitTarget セットする loadlimitTarget
     */
    public void setLoadlimitTarget(String loadlimitTarget) {
        this.loadlimitTarget = loadlimitTarget;
    }
    /**
     * @return loadCurrent
     */
    public String getLoadCurrent() {
        return loadCurrent;
    }
    /**
     * @param loadCurrent セットする loadCurrent
     */
    public void setLoadCurrent(String loadCurrent) {
        this.loadCurrent = loadCurrent;
    }
    /**
     * @return tempLoadCurrent
     */
    public String getTempLoadCurrent() {
        return tempLoadCurrent;
    }
    /**
     * @param tempLoadCurrent セットする tempLoadCurrent
     */
    public void setTempLoadCurrent(String tempLoadCurrent) {
        this.tempLoadCurrent = tempLoadCurrent;
    }
    /**
     * @return autoInjection
     */
    public String getAutoInjection() {
        return autoInjection;
    }
    /**
     * @param autoInjection セットする autoInjection
     */
    public void setAutoInjection(String autoInjection) {
        this.autoInjection = autoInjection;
    }
    /**
     * @return tempAutoInjection
     */
    public String getTempAutoInjection() {
        return tempAutoInjection;
    }
    /**
     * @param tempAutoInjection セットする tempAutoInjection
     */
    public void setTempAutoInjection(String tempAutoInjection) {
        this.tempAutoInjection = tempAutoInjection;
    }
    /**
     * @return breakerActCount
     */
    public String getBreakerActCount() {
        return breakerActCount;
    }
    /**
     * @param breakerActCount セットする breakerActCount
     */
    public void setBreakerActCount(String breakerActCount) {
        this.breakerActCount = breakerActCount;
    }
    /**
     * @return tempBreakerActCount
     */
    public String getTempBreakerActCount() {
        return tempBreakerActCount;
    }
    /**
     * @param tempBreakerActCount セットする tempBreakerActCount
     */
    public void setTempBreakerActCount(String tempBreakerActCount) {
        this.tempBreakerActCount = tempBreakerActCount;
    }
    /**
     * @return countClear
     */
    public String getCountClear() {
        return countClear;
    }
    /**
     * @param countClear セットする countClear
     */
    public void setCountClear(String countClear) {
        this.countClear = countClear;
    }
    /**
     * @return tempCountClear
     */
    public String getTempCountClear() {
        return tempCountClear;
    }
    /**
     * @param tempCountClear セットする tempCountClear
     */
    public void setTempCountClear(String tempCountClear) {
        this.tempCountClear = tempCountClear;
    }
    /**
     * @return openMode
     */
    public String getOpenMode() {
        return openMode;
    }
    /**
     * @param openMode セットする openMode
     */
    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }
    /**
     * @return meterId
     */
    public String getMeterId() {
        return meterId;
    }
    /**
     * @param meterId セットする meterId
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }
    /**
     * @param memo セットする memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
    /**
     * @return meterType
     */
    public Long getMeterType() {
        return meterType;
    }
    /**
     * @param meterType セットする meterType
     */
    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }
    /**
     * @return dispYearFlg
     */
    public String getDispYearFlg() {
        return dispYearFlg;
    }
    /**
     * @param dispYearFlg セットする dispYearFlg
     */
    public void setDispYearFlg(String dispYearFlg) {
        this.dispYearFlg = dispYearFlg;
    }
    /**
     * @return examEndYmGengo
     */
    public String getExamEndYmGengo() {
        return examEndYmGengo;
    }
    /**
     * @param examEndYmGengo セットする examEndYmGengo
     */
    public void setExamEndYmGengo(String examEndYmGengo) {
        this.examEndYmGengo = examEndYmGengo;
    }
    /**
     * @return examEndYear
     */
    public String getExamEndYear() {
        return examEndYear;
    }
    /**
     * @param examEndYear セットする examEndYear
     */
    public void setExamEndYear(String examEndYear) {
        this.examEndYear = examEndYear;
    }
    /**
     * @return examEndMonth
     */
    public String getExamEndMonth() {
        return examEndMonth;
    }
    /**
     * @param examEndMonth セットする examEndMonth
     */
    public void setExamEndMonth(String examEndMonth) {
        this.examEndMonth = examEndMonth;
    }
    /**
     * @return examNotice
     */
    public String getExamNotice() {
        return examNotice;
    }
    /**
     * @param examNotice セットする examNotice
     */
    public void setExamNotice(String examNotice) {
        this.examNotice = examNotice;
    }
    /**
     * @return version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @param version セットする version
     */
    public void setVersion(int version) {
        this.version = version;
    }
    /**
     * @return buildingId
     */
    public Long getBuildingId() {
        return buildingId;
    }
    /**
     * @param buildingId セットする buildingId
     */
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    /**
     * @return examEndYearWareki
     */
    public String getExamEndYearWareki() {
        return examEndYearWareki;
    }
    /**
     * @param examEndYearWareki セットする examEndYearWareki
     */
    public void setExamEndYearWareki(String examEndYearWareki) {
        this.examEndYearWareki = examEndYearWareki;
    }
    /**
     * @return currentData
     */
    public String getCurrentData() {
        return currentData;
    }
    /**
     * @param currentData セットする currentData
     */
    public void setCurrentData(String currentData) {
        this.currentData = currentData;
    }
    /**
     * @return currentDataChg
     */
    public boolean getCurrentDataChg() {
        return currentDataChg;
    }
    /**
     * @param currentDataChg セットする currentDataChg
     */
    public void setCurrentDataChg(boolean currentDataChg) {
        this.currentDataChg = currentDataChg;
    }
    /**
     * @return pulseType
     */
    public String getPulseType() {
        return pulseType;
    }
    /**
     * @param pulseType セットする pulseType
     */
    public void setPulseType(String pulseType) {
        this.pulseType = pulseType;
    }
    /**
     * @return pulseTypeChg
     */
    public boolean getPulseTypeChg() {
        return pulseTypeChg;
    }
    /**
     * @param pulseTypeChg セットする pulseTypeChg
     */
    public void setPulseTypeChg(boolean pulseTypeChg) {
        this.pulseTypeChg = pulseTypeChg;
    }
    /**
     * @return pulseWeight
     */
    public String getPulseWeight() {
        return pulseWeight;
    }
    /**
     * @param pulseWeight セットする pulseWeight
     */
    public void setPulseWeight(String pulseWeight) {
        this.pulseWeight = pulseWeight;
    }
    /**
     * @return pulseWeightChg
     */
    public boolean getPulseWeightChg() {
        return pulseWeightChg;
    }
    /**
     * @param pulseWeightChg セットする pulseWeightChg
     */
    public void setPulseWeightChg(boolean pulseWeightChg) {
        this.pulseWeightChg = pulseWeightChg;
    }
    /**
     * @return multi
     */
    public String getMulti() {
        return multi;
    }
    /**
     * @param multi セットする multi
     */
    public void setMulti(String multi) {
        this.multi = multi;
    }
    public String getWirelessType() {
        return wirelessType;
    }
    public void setWirelessType(String wirelessType) {
        this.wirelessType = wirelessType;
    }
    public String getHandyType() {
        return handyType;
    }
    public void setHandyType(String handyType) {
        this.handyType = handyType;
    }
    public String getWirelessId() {
        return wirelessId;
    }
    public void setWirelessId(String wirelessId) {
        this.wirelessId = wirelessId;
    }
    public String getHop1Id() {
        return hop1Id;
    }
    public void setHop1Id(String hop1Id) {
        this.hop1Id = hop1Id;
    }
    public String getHop2Id() {
        return hop2Id;
    }
    public void setHop2Id(String hop2Id) {
        this.hop2Id = hop2Id;
    }
    public String getHop3Id() {
        return hop3Id;
    }
    public void setHop3Id(String hop3Id) {
        this.hop3Id = hop3Id;
    }
    public String getPollingId() {
        return pollingId;
    }
    public void setPollingId(String pollingId) {
        this.pollingId = pollingId;
    }
    public String getCurrentDataHidden() {
        return currentDataHidden;
    }
    public void setCurrentDataHidden(String currentDataHidden) {
        this.currentDataHidden = currentDataHidden;
    }
    public String getPulseTypeHidden() {
        return pulseTypeHidden;
    }
    public void setPulseTypeHidden(String pulseTypeHidden) {
        this.pulseTypeHidden = pulseTypeHidden;
    }
    public String getPulseWeightHidden() {
        return pulseWeightHidden;
    }
    public void setPulseWeightHidden(String pulseWeightHidden) {
        this.pulseWeightHidden = pulseWeightHidden;
    }
    public boolean isSendFlg() {
        return sendFlg;
    }
    public void setSendFlg(boolean sendFlg) {
        this.sendFlg = sendFlg;
    }
    public boolean isSendFlgSwitch() {
        return sendFlgSwitch;
    }
    public void setSendFlgSwitch(boolean sendFlgSwitch) {
        this.sendFlgSwitch = sendFlgSwitch;
    }
    public boolean isSendFlgLoadlimit() {
        return sendFlgLoadlimit;
    }
    public void setSendFlgLoadlimit(boolean sendFlgLoadlimit) {
        this.sendFlgLoadlimit = sendFlgLoadlimit;
    }
}
