package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TDayLoadSurveyResultSet {

    private String devId;
    private Long meterMngId;
    private String getDate;
    private String meterId;
    private Timestamp createDate;
    private Long createUserId;
    private BigDecimal dmvKwh;
    private BigDecimal kwh30;
    private Timestamp recDate;
    private String recMan;
    private Timestamp updateDate;
    private Long updateUserId;
    private String startTime;
    private String endTime;

    public TDayLoadSurveyResultSet() {

    }

    public TDayLoadSurveyResultSet(String devId, Long meterMngId, String getDate, BigDecimal dmvKwh, BigDecimal kwh30) {
        this.devId = devId;
        this.meterMngId = meterMngId;
        this.getDate = getDate;
        this.dmvKwh = dmvKwh;
        this.kwh30 = kwh30;
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
    public String getGetDate() {
        return getDate;
    }
    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }
    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
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
    public BigDecimal getDmvKwh() {
        return dmvKwh;
    }
    public void setDmvKwh(BigDecimal dmvKwh) {
        this.dmvKwh = dmvKwh;
    }
    public BigDecimal getKwh30() {
        return kwh30;
    }
    public void setKwh30(BigDecimal kwh30) {
        this.kwh30 = kwh30;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
