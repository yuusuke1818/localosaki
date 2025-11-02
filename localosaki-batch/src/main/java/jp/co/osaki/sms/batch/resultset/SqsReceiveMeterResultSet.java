package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class SqsReceiveMeterResultSet {

    private String devId;
    private Long meterMngId;
    private Long meterType;
    private BigDecimal multi;

    public SqsReceiveMeterResultSet() {

    }

    public SqsReceiveMeterResultSet(String devId, Long meterMngId, Long meterType, BigDecimal multi) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.meterType = meterType;
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
    public Long getMeterType() {
        return meterType;
    }
    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }
    public BigDecimal getMulti() {
        return multi;
    }
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

}
