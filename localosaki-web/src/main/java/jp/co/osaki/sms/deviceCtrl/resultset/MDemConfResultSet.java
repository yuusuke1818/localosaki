package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MDemConfResultSet {

    private String devKind;
    private String devId;
    private BigDecimal alarmLock;
    private String commandFlg;
    private Timestamp createDate;
    private Long createUserId;
    private BigDecimal monLastDay;
    private BigDecimal pulseWeight;
    private Timestamp recDate;
    private String recMan;
    private String srvEnt;
    private BigDecimal targetPwr;
    private BigDecimal thresholdPwr;
    private Timestamp updateDate;

    public MDemConfResultSet(String devKind, String devId, BigDecimal alarmLock, String commandFlg,
            BigDecimal monLastDay, BigDecimal pulseWeight, String srvEnt, BigDecimal targetPwr,
            BigDecimal thresholdPwr) {
        super();
        this.devKind = devKind;
        this.devId = devId;
        this.alarmLock = alarmLock;
        this.commandFlg = commandFlg;
        this.monLastDay = monLastDay;
        this.pulseWeight = pulseWeight;
        this.srvEnt = srvEnt;
        this.targetPwr = targetPwr;
        this.thresholdPwr = thresholdPwr;
    }

    public MDemConfResultSet() {
    }

    private Long updateUserId;



    public String getDevKind() {
        return devKind;
    }
    public void setDevKind(String devKind) {
        this.devKind = devKind;
    }
    public String getDevId() {
        return devId;
    }
    public void setDevId(String devId) {
        this.devId = devId;
    }
    public BigDecimal getAlarmLock() {
        return alarmLock;
    }
    public void setAlarmLock(BigDecimal alarmLock) {
        this.alarmLock = alarmLock;
    }
    public String getCommandFlg() {
        return commandFlg;
    }
    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
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
    public BigDecimal getMonLastDay() {
        return monLastDay;
    }
    public void setMonLastDay(BigDecimal monLastDay) {
        this.monLastDay = monLastDay;
    }
    public BigDecimal getPulseWeight() {
        return pulseWeight;
    }
    public void setPulseWeight(BigDecimal pulseWeight) {
        this.pulseWeight = pulseWeight;
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
    public BigDecimal getTargetPwr() {
        return targetPwr;
    }
    public void setTargetPwr(BigDecimal targetPwr) {
        this.targetPwr = targetPwr;
    }
    public BigDecimal getThresholdPwr() {
        return thresholdPwr;
    }
    public void setThresholdPwr(BigDecimal thresholdPwr) {
        this.thresholdPwr = thresholdPwr;
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
