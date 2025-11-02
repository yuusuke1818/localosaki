package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * 自動検針バッチ処理用.
 * 任意検針の予定検針対象データ
 * @author kobayashi.sho
 */
public class ReserveInspExecResultSet {
    /* 装置ID */
    private String devId;
    /* メーター管理番号 */
    private Long meterMngId;
    /* 処理フラグ（Null：正常終了　1：処理待ち　9：エラーフラグ） */
    private String srvEnt;
    /* メーター種別 */
    private Long meterType;
    /* 乗率(1から9999) */
    private BigDecimal multi;
    /* 建物ID */
    private Long buildingId;
    /* 小数、整数確認フラグ（0、null：小数あり、1：整数のみ） */
    private BigDecimal chkInt;
    /* メーターID */
    private String meterId;

    /**
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param srvEnt 処理フラグ（Null：正常終了　1：処理待ち　9：エラーフラグ）
     * @param meterType メーター種別
     * @param multi 乗率(1から9999)
     * @param meterId メーターID
     * @param buildingId 建物ID
     * @param chkInt 小数、整数確認フラグ（0、null：小数あり、1：整数のみ）
     */
    public ReserveInspExecResultSet(String devId, Long meterMngId, String srvEnt, Long meterType, BigDecimal multi, String meterId, Long buildingId, BigDecimal chkInt) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.srvEnt = srvEnt;
        this.meterType = meterType;
        this.multi = multi;
        this.meterId = meterId;
        this.buildingId = buildingId;
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
    public BigDecimal getChkInt() {
        return chkInt;
    }
    public void setChkInt(BigDecimal chkInt) {
        this.chkInt = chkInt;
    }
    public Long getMeterMngId() {
        return meterMngId;
    }
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }
    public String getSrvEnt() {
        return srvEnt;
    }
    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }
    public BigDecimal getMulti() {
        return multi;
    }
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

}
