package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspMeterResultSet {
    /* 装置ID */
    private String devId;
    /* メーター管理番号 */
    private Long meterMngId;
    /* 処理フラグ（Null：正常終了　1：処理待ち　9：エラーフラグ） */
    private String srvEnt;
    /* 乗率(1から9999) */
    private BigDecimal multi;
    /* メーターID */
    private String meterId;
    public AutoInspMeterResultSet(String devId, Long meterMngId, String srvEnt, BigDecimal multi, String meterId) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.srvEnt = srvEnt;
        this.multi = multi;
        this.meterId = meterId;
    }

    public AutoInspMeterResultSet(String devId, Long meterMngId, String srvEnt, BigDecimal multi) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.srvEnt = srvEnt;
        this.multi = multi;
    }

    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
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
