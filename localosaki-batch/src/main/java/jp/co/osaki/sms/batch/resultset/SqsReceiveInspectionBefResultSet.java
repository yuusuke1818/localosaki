package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;
import java.util.Date;

public class SqsReceiveInspectionBefResultSet {
    private String devId;
    private Long meterMngId;
    private Date latestInspDate;
    private Long buildingId;
    private Long meterType;
    private BigDecimal multi;
    private BigDecimal latestInspVal;

    public SqsReceiveInspectionBefResultSet(String devId, Long meterMngId, Date latestInspDate, Long buildingId,
            Long meterType, BigDecimal multi, BigDecimal latestInspVal) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.latestInspDate = latestInspDate;
        this.buildingId = buildingId;
        this.meterType = meterType;
        this.multi = multi;
        this.latestInspVal = latestInspVal;
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
    public Date getLatestInspDate() {
        return latestInspDate;
    }
    public void setLatestInspDate(Date latestInspDate) {
        this.latestInspDate = latestInspDate;
    }
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
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
    public BigDecimal getLatestInspVal() {
        return latestInspVal;
    }
    public void setLatestInspVal(BigDecimal latestInspVal) {
        this.latestInspVal = latestInspVal;
    }

}
