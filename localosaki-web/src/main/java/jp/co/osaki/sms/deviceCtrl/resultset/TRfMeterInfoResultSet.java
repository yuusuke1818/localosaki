package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TRfMeterInfoResultSet {

    private Long meterMngId;
    private Long meterType;
    private String meterId;
    private String tenantId;
    private String buildingName;
    private String address1;
    private String address2;
    private String wirelessId;
    private String hop1Id;
    private String hop2Id;
    private String hop3Id;
    private String pollingId;
    private BigDecimal multi;
    private Timestamp latestInspDate;
    private String latestInspVal;
    private String alarm;
    private Timestamp prevInspDate;
    private String prevInspVal;
    private String latestInspDateStr;
    private String prevInspDateStr;


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
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
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
    public BigDecimal getMulti() {
        return multi;
    }
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }
    public Timestamp getLatestInspDate() {
        return latestInspDate;
    }
    public void setLatestInspDate(Timestamp latestInspDate) {
        this.latestInspDate = latestInspDate;
    }
    public String getLatestInspVal() {
        return latestInspVal;
    }
    public void setLatestInspVal(String latestInspVal) {
        this.latestInspVal = latestInspVal;
    }
    public String getAlarm() {
        return alarm;
    }
    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
    public Timestamp getPrevInspDate() {
        return prevInspDate;
    }
    public void setPrevInspDate(Timestamp prevInspDate) {
        this.prevInspDate = prevInspDate;
    }
    public String getPrevInspVal() {
        return prevInspVal;
    }
    public void setPrevInspVal(String prevInspVal) {
        this.prevInspVal = prevInspVal;
    }
    public String getLatestInspDateStr() {
        return latestInspDateStr;
    }
    public void setLatestInspDateStr(String latestInspDateStr) {
        this.latestInspDateStr = latestInspDateStr;
    }
    public String getPrevInspDateStr() {
        return prevInspDateStr;
    }
    public void setPrevInspDateStr(String prevInspDateStr) {
        this.prevInspDateStr = prevInspDateStr;
    }



}
