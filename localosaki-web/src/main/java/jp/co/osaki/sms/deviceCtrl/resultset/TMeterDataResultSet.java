package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TMeterDataResultSet {

    private String devId;
    private Long meterMngId;
    private BigDecimal ampere1;
    private BigDecimal ampere2;
    private BigDecimal ampere3;
    private String circuitBreaker;
    private Timestamp createDate;
    private Long createUserId;
    private String currentKwh1;
    private String currentKwh2;
    private Timestamp measureDate;
    private String meterId;
    private BigDecimal momentaryPwr;
    private BigDecimal powerFactor;
    private Timestamp recDate;
    private String recMan;
    private String srvEnt;
    private Timestamp updateDate;
    private Long updateUserId;
    private Integer version;
    private BigDecimal voltage12;
    private BigDecimal voltage13;
    private BigDecimal voltage23;

    public TMeterDataResultSet() {

    }



    public TMeterDataResultSet(String devId, Long meterMngId, BigDecimal ampere1, BigDecimal ampere2, BigDecimal ampere3,
            String circuitBreaker, String currentKwh1, String currentKwh2, Timestamp measureDate, String meterId,
            BigDecimal momentaryPwr, BigDecimal powerFactor, String srvEnt, BigDecimal voltage12, BigDecimal voltage13,
            BigDecimal voltage23) {
        super();
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.ampere1 = ampere1;
        this.ampere2 = ampere2;
        this.ampere3 = ampere3;
        this.circuitBreaker = circuitBreaker;
        this.currentKwh1 = currentKwh1;
        this.currentKwh2 = currentKwh2;
        this.measureDate = measureDate;
        this.meterId = meterId;
        this.momentaryPwr = momentaryPwr;
        this.powerFactor = powerFactor;
        this.srvEnt = srvEnt;
        this.voltage12 = voltage12;
        this.voltage13 = voltage13;
        this.voltage23 = voltage23;
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
    public BigDecimal getAmpere1() {
        return ampere1;
    }
    public void setAmpere1(BigDecimal ampere1) {
        this.ampere1 = ampere1;
    }
    public BigDecimal getAmpere2() {
        return ampere2;
    }
    public void setAmpere2(BigDecimal ampere2) {
        this.ampere2 = ampere2;
    }
    public BigDecimal getAmpere3() {
        return ampere3;
    }
    public void setAmpere3(BigDecimal ampere3) {
        this.ampere3 = ampere3;
    }
    public String getCircuitBreaker() {
        return circuitBreaker;
    }
    public void setCircuitBreaker(String circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    public Long getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public String getCurrentKwh1() {
        return currentKwh1;
    }
    public void setCurrentKwh1(String currentKwh1) {
        this.currentKwh1 = currentKwh1;
    }
    public String getCurrentKwh2() {
        return currentKwh2;
    }
    public void setCurrentKwh2(String currentKwh2) {
        this.currentKwh2 = currentKwh2;
    }
    public Timestamp getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(Timestamp measureDate) {
        this.measureDate = measureDate;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    public BigDecimal getMomentaryPwr() {
        return momentaryPwr;
    }
    public void setMomentaryPwr(BigDecimal momentaryPwr) {
        this.momentaryPwr = momentaryPwr;
    }
    public BigDecimal getPowerFactor() {
        return powerFactor;
    }
    public void setPowerFactor(BigDecimal powerFactor) {
        this.powerFactor = powerFactor;
    }
    public Timestamp getRecDate() {
        return recDate;
    }
    public void setRecDate(Timestamp recDate) {
        this.recDate = recDate;
    }
    public String getRecMan() {
        return recMan;
    }
    public void setRecMan(String recMan) {
        this.recMan = recMan;
    }
    public String getSrvEnt() {
        return srvEnt;
    }
    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
    public Long getUpdateUserId() {
        return updateUserId;
    }
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    public BigDecimal getVoltage12() {
        return voltage12;
    }
    public void setVoltage12(BigDecimal voltage12) {
        this.voltage12 = voltage12;
    }
    public BigDecimal getVoltage13() {
        return voltage13;
    }
    public void setVoltage13(BigDecimal voltage13) {
        this.voltage13 = voltage13;
    }
    public BigDecimal getVoltage23() {
        return voltage23;
    }
    public void setVoltage23(BigDecimal voltage23) {
        this.voltage23 = voltage23;
    }


}
