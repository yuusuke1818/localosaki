package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * メーター取得結果
 * @author kimura.m
 */
public class MeterInfo {

    /** メータ管理番号 */
    private Long meterMngId;

    /** コマンドフラグ */
    private String commandFlg;

    /** 処理フラグ */
    private String srvEnt;

    /** 操作状態 */
    private String operationStatus;

    /** 建物番号 */
    private String buildingNo;

    /** ユーザーコード */
    private Long tenantId;

    /** 建物名 */
    private String buildingName;

    /** メーターID */
    private String meterId;

    /** 開閉区分 */
    private String openMode;

    /** 開閉区分(表示用） */
    private String openModeDisp;

    /** 負荷制限 */
    private String loadlimitMode;

    /** 負荷電流 */
    private String loadCurrent;

    /** 自動投入 */
    private String autoInjection;

    /** 開閉器動作カウント */
    private String breakerActCount;

    /** 開閉カウントクリア */
    private String countClear;

    /** (臨時)負荷電流 */
    private String tempLoadCurrent;

    /** (臨時)自動投入 */
    private String tempAutoInjection;

    /** (臨時)開閉器動作カウント */
    private String tempBreakerActCount;

    /** (臨時)開閉カウントクリア */
    private String tempCountClear;

    /** コンセントレータ名称 */
    private String concentratorName;

    /** I/F種別 */
    private BigDecimal ifType;

    /** コメント */
    private String memo;

    /** 検針値 */
    private BigDecimal dmvKwh;

    /** メーター種別 */
    private Long meterType;

    /** メーター種別名称 */
    private String meterTypeName;

    /** 検満年月(YYYYMM) */
    private String examEndYm;

    /** 検満年月_和暦(YYMM) */
    private String examEndYmWareki;

    /** 西暦・和暦表示確認用フラグ */
    private String dispYearFlg;

    /** 検満通知 */
    private String examNotice;

    /** 乗率 */
    private BigDecimal multi;

    /** メーター状況  */
    private BigDecimal meterPresentSituation;

    /** メーター状況(表示用) */
    private String meterPresentSituationDisp;

    /** アラート停止期間 開始日 */
    private String alertPauseStart;

    /** アラート停止期間 開始日(保持用) */
    private String alertPauseStartHidden;

    /** アラート停止期間 終了日 */
    private String alertPauseEnd;

    /** アラート停止期間 終了日(保持用) */
    private String alertPauseEndHidden;

    /** アラート停止フラグ */
    private BigDecimal alertPauseFlg;

    /** アラート停止(表示用) */
    private String alertPauseDisp;

    /** メーター状態備考 */
    private String meterStatusMemo;

    /** 建物ID */
    private Long buildingId;

    /** 装置ID */
    private String devId;

    /** 装置名 */
    private String devName;

    /** 企業ID */
    private String corpId;

    /** チェックボックスの状態 */
    private Boolean checkBox;

    /** version */
    private int version;

    // パルス専用
    /** 現在値 */
    private BigDecimal currentData;
    /** 現在値（保持用） */
    private BigDecimal currentDataHidden;
    /** 現在値変更フラグ */
    private String currentDataChg;
    /** パルス種別 */
    private String pulseType;
    /** パルス種別（保持用） */
    private String pulseTypeHidden;
    /** パルス種別名称 */
    private String pulseTypeName;
    /** パルス種別変更フラグ */
    private String pulseTypeChg;
    /** パルス重み */
    private BigDecimal pulseWeight;
    /** パルス重み(保持用) */
    private BigDecimal pulseWeightHidden;
    /** パルス重み(一覧表示用) */
    private BigDecimal pulseWeightCal;
    /** パルス重み変更フラグ */
    private String pulseWeightChg;

    // ハンディ検針専用
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
    /***/



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
     * @return commandFlg
     */
    public String getCommandFlg() {
        return commandFlg;
    }

    /**
     * @param commandFlg セットする commandFlg
     */
    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
    }

    /**
     * @return srvEnt
     */
    public String getSrvEnt() {
        return srvEnt;
    }

    /**
     * @param srvEnt セットする srvEnt
     */
    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
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
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId セットする tenantId
     */
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName セットする buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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
     * @return concentratorName
     */
    public String getConcentratorName() {
        return concentratorName;
    }

    /**
     * @param concentratorName セットする concentratorName
     */
    public void setConcentratorName(String concentratorName) {
        this.concentratorName = concentratorName;
    }

    /**
     * @return ifType
     */
    public BigDecimal getIfType() {
        return ifType;
    }

    /**
     * @param ifType セットする ifType
     */
    public void setIfType(BigDecimal ifType) {
        this.ifType = ifType;
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
     * @return meterTypeName
     */
    public String getMeterTypeName() {
        return meterTypeName;
    }

    /**
     * @param meterTypeName セットする meterTypeName
     */
    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    /**
     * @return examEndYm
     */
    public String getExamEndYm() {
        return examEndYm;
    }

    /**
     * @param examEndYm セットする examEndYm
     */
    public void setExamEndYm(String examEndYm) {
        this.examEndYm = examEndYm;
    }

    /**
     * @return examEndYmWareki
     */
    public String getExamEndYmWareki() {
        return examEndYmWareki;
    }

    /**
     * @param examEndYmWareki セットする examEndYmWareki
     */
    public void setExamEndYmWareki(String examEndYmWareki) {
        this.examEndYmWareki = examEndYmWareki;
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
     * @return corpId
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     * @param corpId セットする corpId
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * @return openModeDisp
     */
    public String getOpenModeDisp() {
        return openModeDisp;
    }

    /**
     * @param openModeDisp セットする openModeDisp
     */
    public void setOpenModeDisp(String openModeDisp) {
        this.openModeDisp = openModeDisp;
    }

    /**
     * @return dmvKwh
     */
    public BigDecimal getDmvKwh() {
        return dmvKwh;
    }

    /**
     * @param dmvKwh セットする dmvKwh
     */
    public void setDmvKwh(BigDecimal dmvKwh) {
        this.dmvKwh = dmvKwh;
    }

    /**
     * @return operationStatus
     */
    public String getOperationStatus() {
        return operationStatus;
    }

    /**
     * @param operationStatus セットする operationStatus
     */
    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
     * @return checkBox
     */
    public Boolean getCheckBox() {
        return checkBox;
    }

    /**
     * @param checkBox セットする checkBox
     */
    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
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
     * @return meterPresentSituation
     */
    public BigDecimal getMeterPresentSituation() {
        return meterPresentSituation;
    }

    /**
     * @param meterPresentSituation セットする meterPresentSituation
     */
    public void setMeterPresentSituation(BigDecimal meterPresentSituation) {
        this.meterPresentSituation = meterPresentSituation;
    }

    /**
     * @return meterPresentSituationDisp
     */
    public String getMeterPresentSituationDisp() {
        return meterPresentSituationDisp;
    }

    /**
     * @param meterPresentSituationDisp セットする meterPresentSituationDisp
     */
    public void setMeterPresentSituationDisp(String meterPresentSituationDisp) {
        this.meterPresentSituationDisp = meterPresentSituationDisp;
    }

    /**
     * @return alertPauseStart
     */
    public String getAlertPauseStart() {
        return alertPauseStart;
    }

    /**
     * @param alertPauseStart セットする alertPauseStart
     */
    public void setAlertPauseStart(String alertPauseStart) {
        this.alertPauseStart = alertPauseStart;
    }

    /**
     * @return alertPauseStartHidden
     */
    public String getAlertPauseStartHidden() {
        return alertPauseStartHidden;
    }

    /**
     * @param alertPauseStartHidden セットする alertPauseStartHidden
     */
    public void setAlertPauseStartHidden(String alertPauseStartHidden) {
        this.alertPauseStartHidden = alertPauseStartHidden;
    }

    /**
     * @return alertPauseEnd
     */
    public String getAlertPauseEnd() {
        return alertPauseEnd;
    }

    /**
     * @param alertPauseEnd セットする alertPauseEnd
     */
    public void setAlertPauseEnd(String alertPauseEnd) {
        this.alertPauseEnd = alertPauseEnd;
    }

    /**
     * @return alertPauseEndHidden
     */
    public String getAlertPauseEndHidden() {
        return alertPauseEndHidden;
    }

    /**
     * @param alertPauseEndHidden セットする alertPauseEndHidden
     */
    public void setAlertPauseEndHidden(String alertPauseEndHidden) {
        this.alertPauseEndHidden = alertPauseEndHidden;
    }

    /**
     * @return alertPauseFlg
     */
    public BigDecimal getAlertPauseFlg() {
        return alertPauseFlg;
    }

    /**
     * @param alertPauseFlg セットする alertPauseFlg
     */
    public void setAlertPauseFlg(BigDecimal alertPauseFlg) {
        this.alertPauseFlg = alertPauseFlg;
    }

    /**
     * @param alertPauseDisp セットする alertPauseDisp
     */
    public void setAlertPauseDisp(String alertPauseDisp) {
        this.alertPauseDisp = alertPauseDisp;
    }

    /**
     * @return alertPauseDisp
     */
    public String getAlertPauseDisp() {
        return alertPauseDisp;
    }

    /**
     * @return meterStatusMemo
     */
    public String getMeterStatusMemo() {
        return meterStatusMemo;
    }

    /**
     * @param meterStatusMemo セットする meterStatusMemo
     */
    public void setMeterStatusMemo(String meterStatusMemo) {
        this.meterStatusMemo = meterStatusMemo;
    }

    /**
     * @return pulseType
     */
    public String getPulseType() {
        return pulseType;
    }

    /**
     * @return pulseWeight
     */
    public BigDecimal getPulseWeight() {
        return pulseWeight;
    }

    /**
     * @return multi
     */
    public BigDecimal getMulti() {
        return multi;
    }

    /**
     * @param pulseType セットする pulseType
     */
    public void setPulseType(String pulseType) {
        this.pulseType = pulseType;
    }

    /**
     * @return pulseTypeName
     */
    public String getPulseTypeName() {
        return pulseTypeName;
    }

    /**
     * @param pulseTypeName セットする pulseTypeName
     */
    public void setPulseTypeName(String pulseTypeName) {
        this.pulseTypeName = pulseTypeName;
    }

    /**
     * @param pulseWeight セットする pulseWeight
     */
    public void setPulseWeight(BigDecimal pulseWeight) {
        this.pulseWeight = pulseWeight;
    }

    /**
     * @param multi セットする multi
     */
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

    /**
     * @return currentDataChg
     */
    public String getCurrentDataChg() {
        return currentDataChg;
    }

    /**
     * @param currentDataChg セットする currentDataChg
     */
    public void setCurrentDataChg(String currentDataChg) {
        this.currentDataChg = currentDataChg;
    }

    /**
     * @return pulseTypeChg
     */
    public String getPulseTypeChg() {
        return pulseTypeChg;
    }

    /**
     * @param pulseTypeChg セットする pulseTypeChg
     */
    public void setPulseTypeChg(String pulseTypeChg) {
        this.pulseTypeChg = pulseTypeChg;
    }

    /**
     * @return pulseWeightChg
     */
    public String getPulseWeightChg() {
        return pulseWeightChg;
    }

    /**
     * @param pulseWeightChg セットする pulseWeightChg
     */
    public void setPulseWeightChg(String pulseWeightChg) {
        this.pulseWeightChg = pulseWeightChg;
    }

    /**
     * @return currentData
     */
    public BigDecimal getCurrentData() {
        return currentData;
    }

    /**
     * @param currentData セットする currentData
     */
    public void setCurrentData(BigDecimal currentData) {
        this.currentData = currentData;
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

    public BigDecimal getCurrentDataHidden() {
        return currentDataHidden;
    }

    public void setCurrentDataHidden(BigDecimal currentDataHidden) {
        this.currentDataHidden = currentDataHidden;
    }

    public String getPulseTypeHidden() {
        return pulseTypeHidden;
    }

    public void setPulseTypeHidden(String pulseTypeHidden) {
        this.pulseTypeHidden = pulseTypeHidden;
    }

    public BigDecimal getPulseWeightHidden() {
        return pulseWeightHidden;
    }

    public void setPulseWeightHidden(BigDecimal pulseWeightHidden) {
        this.pulseWeightHidden = pulseWeightHidden;
    }

    public BigDecimal getPulseWeightCal() {
        return pulseWeightCal;
    }

    public void setPulseWeightCal(BigDecimal pulseWeightCal) {
        this.pulseWeightCal = pulseWeightCal;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }
}
