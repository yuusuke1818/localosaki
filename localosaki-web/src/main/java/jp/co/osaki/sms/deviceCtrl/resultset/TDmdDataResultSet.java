package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TDmdDataResultSet {

    private String devId;
    private String measureDate;
    private String time;
    private String closeTime;
    private Timestamp createDate;
    private Long createUserId;
    private Timestamp daymaxDemTime;
    private String daymaxDemand;
    private BigDecimal daytotalKwh;
    private BigDecimal demand1;
    private BigDecimal demand2;
    private BigDecimal kwh1;
    private BigDecimal kwh2;
    private Timestamp recDate;
    private String recMan;
    private BigDecimal target1;
    private BigDecimal target2;
    private Timestamp updateDate;
    private Long updateUserId;

    public TDmdDataResultSet() {

    }

    public TDmdDataResultSet(String devId, String measureDate, String time, String closeTime, Timestamp daymaxDemTime,
            String daymaxDemand, BigDecimal daytotalKwh, BigDecimal demand1, BigDecimal demand2, BigDecimal kwh1,
            BigDecimal kwh2, BigDecimal target1, BigDecimal target2) {
        super();
        this.devId = devId;
        this.measureDate = measureDate;
        this.time = time;
        this.closeTime = closeTime;
        this.daymaxDemTime = daymaxDemTime;
        this.daymaxDemand = daymaxDemand;
        this.daytotalKwh = daytotalKwh;
        this.demand1 = demand1;
        this.demand2 = demand2;
        this.kwh1 = kwh1;
        this.kwh2 = kwh2;
        this.target1 = target1;
        this.target2 = target2;
    }

    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
    }
    public String getMeasureDate() {
        return measureDate;
    }
    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getCloseTime() {
        return closeTime;
    }
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
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
    public Timestamp getDaymaxDemTime() {
        return daymaxDemTime;
    }
    public void setDaymaxDemTime(Timestamp daymaxDemTime) {
        this.daymaxDemTime = daymaxDemTime;
    }
    public String getDaymaxDemand() {
        return daymaxDemand;
    }
    public void setDaymaxDemand(String daymaxDemand) {
        this.daymaxDemand = daymaxDemand;
    }
    public BigDecimal getDaytotalKwh() {
        return daytotalKwh;
    }
    public void setDaytotalKwh(BigDecimal daytotalKwh) {
        this.daytotalKwh = daytotalKwh;
    }
    public BigDecimal getDemand1() {
        return demand1;
    }
    public void setDemand1(BigDecimal demand1) {
        this.demand1 = demand1;
    }
    public BigDecimal getDemand2() {
        return demand2;
    }
    public void setDemand2(BigDecimal demand2) {
        this.demand2 = demand2;
    }
    public BigDecimal getKwh1() {
        return kwh1;
    }
    public void setKwh1(BigDecimal kwh1) {
        this.kwh1 = kwh1;
    }
    public BigDecimal getKwh2() {
        return kwh2;
    }
    public void setKwh2(BigDecimal kwh2) {
        this.kwh2 = kwh2;
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
    public BigDecimal getTarget1() {
        return target1;
    }
    public void setTarget1(BigDecimal target1) {
        this.target1 = target1;
    }
    public BigDecimal getTarget2() {
        return target2;
    }
    public void setTarget2(BigDecimal target2) {
        this.target2 = target2;
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




}
