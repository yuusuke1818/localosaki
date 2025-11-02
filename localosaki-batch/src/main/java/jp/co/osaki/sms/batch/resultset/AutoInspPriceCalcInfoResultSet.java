package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspPriceCalcInfoResultSet {
    /* メータグループID */
    private Long meterGrpId;
    /* 企業ID */
    private String corpId;
    /* 建物ID */
    private Long buildingId;
    /* 装置ID */
    private String devId;
    /* メーター種別 */
    private Long meterType;
    /* メーター管理ID */
    private Long meterMngID;
    /* 自動検針日（0：なし　/日の指定：1から31） */
    private String day;
    /* 計算方法（1：加算　2：減算） */
    private BigDecimal calcType;
    /* 小数部端数処理（1：四捨五入　2：切り捨て　3：切り上げ） */
    private String decimalFraction;
    public AutoInspPriceCalcInfoResultSet(Long meterGrpId, String corpId, Long buildingId, String devId, Long meterType, Long meterMngID, String day, BigDecimal calcType, String decimalFraction) {
        super();
        this.meterGrpId = meterGrpId;
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.devId = devId;
        this.meterType = meterType;
        this.meterMngID = meterMngID;
        this.day = day;
        this.calcType = calcType;
        this.decimalFraction = decimalFraction;
    }
    public Long getMeterGrpId() {
        return meterGrpId;
    }
    public void setMeterGrpId(Long meterGrpId) {
        this.meterGrpId = meterGrpId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
    }
    public Long getMeterType() {
        return meterType;
    }
    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }
    public Long getMeterMngID() {
        return meterMngID;
    }
    public void setMeterMngID(Long meterMngID) {
        this.meterMngID = meterMngID;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public BigDecimal getCalcType() {
        return calcType;
    }
    public void setCalcType(BigDecimal calcType) {
        this.calcType = calcType;
    }
    public String getDecimalFraction() {
        return decimalFraction;
    }
    public void setDecimalFraction(String decimalFraction) {
        this.decimalFraction = decimalFraction;
    }
}
