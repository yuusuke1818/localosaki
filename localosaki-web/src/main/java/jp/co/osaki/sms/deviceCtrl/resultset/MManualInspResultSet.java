package jp.co.osaki.sms.deviceCtrl.resultset;

import java.sql.Timestamp;

public class MManualInspResultSet {

    private String devId;
    private Long meterMngId;
    private String commandFlg;
    private Timestamp createDate;
    private Long createUserId;
    private Timestamp recDate;
    private String recMan;
    private String srvEnt;
    private Timestamp updateDate;
    private Long updateUserId;
    private String inspDate;

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
    public String getInspDate() {
        return inspDate;
    }
    public void setInspDate(String inspDate) {
        this.inspDate = inspDate;
    }


}
