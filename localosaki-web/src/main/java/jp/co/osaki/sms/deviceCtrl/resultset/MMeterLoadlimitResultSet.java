package jp.co.osaki.sms.deviceCtrl.resultset;

import java.sql.Timestamp;

public class MMeterLoadlimitResultSet {

    private Long meterMngId;
    private String devKind;
    private String devId;
    private String autoInjection;
    private String breakerActCount;
    private String commandFlg;
    private String countClear;
    private Timestamp createDate;
    private Long createUserId;
    private String loadCurrent;
    private String loadlimitMode;
    private Timestamp recDate;
    private String recMan;
    private String srvEnt;
    private String tempAutoInjection;
    private String tempBreakerActCount;
    private String tempCountClear;
    private String tempLoadCurrent;
    private Timestamp updateDate;
    private Long updateUserId;

    public MMeterLoadlimitResultSet() {

    }

    public MMeterLoadlimitResultSet(Long meterMngId, String devKind, String devId, String autoInjection,
            String breakerActCount, String commandFlg, String countClear, String loadCurrent, String loadlimitMode,
            String srvEnt, String tempAutoInjection, String tempBreakerActCount, String tempCountClear,
            String tempLoadCurrent) {
        super();
        this.meterMngId = meterMngId;
        this.devKind = devKind;
        this.devId = devId;
        this.autoInjection = autoInjection;
        this.breakerActCount = breakerActCount;
        this.commandFlg = commandFlg;
        this.countClear = countClear;
        this.loadCurrent = loadCurrent;
        this.loadlimitMode = loadlimitMode;
        this.srvEnt = srvEnt;
        this.tempAutoInjection = tempAutoInjection;
        this.tempBreakerActCount = tempBreakerActCount;
        this.tempCountClear = tempCountClear;
        this.tempLoadCurrent = tempLoadCurrent;
    }

    public Long getMeterMngId() {
        return meterMngId;
    }
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }
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
    public String getAutoInjection() {
        return autoInjection;
    }
    public void setAutoInjection(String autoInjection) {
        this.autoInjection = autoInjection;
    }
    public String getBreakerActCount() {
        return breakerActCount;
    }
    public void setBreakerActCount(String breakerActCount) {
        this.breakerActCount = breakerActCount;
    }
    public String getCommandFlg() {
        return commandFlg;
    }
    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
    }
    public String getCountClear() {
        return countClear;
    }
    public void setCountClear(String countClear) {
        this.countClear = countClear;
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
    public String getLoadCurrent() {
        return loadCurrent;
    }
    public void setLoadCurrent(String loadCurrent) {
        this.loadCurrent = loadCurrent;
    }
    public String getLoadlimitMode() {
        return loadlimitMode;
    }
    public void setLoadlimitMode(String loadlimitMode) {
        this.loadlimitMode = loadlimitMode;
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
    public String getTempAutoInjection() {
        return tempAutoInjection;
    }
    public void setTempAutoInjection(String tempAutoInjection) {
        this.tempAutoInjection = tempAutoInjection;
    }
    public String getTempBreakerActCount() {
        return tempBreakerActCount;
    }
    public void setTempBreakerActCount(String tempBreakerActCount) {
        this.tempBreakerActCount = tempBreakerActCount;
    }
    public String getTempCountClear() {
        return tempCountClear;
    }
    public void setTempCountClear(String tempCountClear) {
        this.tempCountClear = tempCountClear;
    }
    public String getTempLoadCurrent() {
        return tempLoadCurrent;
    }
    public void setTempLoadCurrent(String tempLoadCurrent) {
        this.tempLoadCurrent = tempLoadCurrent;
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
