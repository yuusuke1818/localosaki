package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspExecResultSet {
    /* 建物ID */
    private Long buildingId;
    /* 装置ID */
    private String devId;
    /* メーター種別 */
    private Long meterType;
    /* メーター種別名 */
    private String meterTypeName;
    /* 月毎の自動検針実施有無（0：なし　1：あり） */
    private String month;
    /* 自動検針日（0：なし　/日の指定：1から31） */
    private String day;
    /* 自動検針時（0から23） */
    private BigDecimal hour;
    /* 待ち時間 */
    private BigDecimal waitTime;
    /* 小数、整数確認フラグ（0、null：小数あり、1：整数のみ） */
    private BigDecimal chkInt;
    public AutoInspExecResultSet(Long buildingId, String devId, Long meterType, String meterTypeName, String month, String day, BigDecimal hour, BigDecimal waitTime, BigDecimal chkInt) {
        super();
        this.buildingId = buildingId;
        this.devId = devId;
        this.meterType = meterType;
        this.meterTypeName = meterTypeName;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.waitTime = waitTime;
        this.chkInt = chkInt;
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
    public String getMeterTypeName() {
        return meterTypeName;
    }
    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public BigDecimal getHour() {
        return hour;
    }
    public void setHour(BigDecimal hour) {
        this.hour = hour;
    }
    public BigDecimal getWaitTime() {
        return waitTime;
    }
    public void setWaitTime(BigDecimal waitTime) {
        this.waitTime = waitTime;
    }
    public BigDecimal getChkInt() {
        return chkInt;
    }
    public void setChkInt(BigDecimal chkInt) {
        this.chkInt = chkInt;
    }

}
