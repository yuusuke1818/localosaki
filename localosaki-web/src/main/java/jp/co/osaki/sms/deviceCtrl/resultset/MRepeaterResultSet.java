package jp.co.osaki.sms.deviceCtrl.resultset;

import java.sql.Timestamp;

public class MRepeaterResultSet {

    private Long repeaterMngId;
    private String devKind;
    private String devId;
    private Long dm2Id;
    private String parentDevId;
    private String corpId;
    private Long buildingId;
    private String commandFlg;
    private Timestamp createDate;
    private Long createUserId;
    private String memo;
    private Timestamp recDate;
    private String recMan;
    private String repeaterId;
    private String srvEnt;
    private Timestamp updateDate;
    private Long updateUserId;
    private Integer version;

    public MRepeaterResultSet() {

    }

    public MRepeaterResultSet(Long repeaterMngId, String devKind, String devId, Long dm2Id, String parentDevId,
            String corpId, Long buildingId, String commandFlg, String memo, String repeaterId, String srvEnt) {
        super();
        this.repeaterMngId = repeaterMngId;
        this.devKind = devKind;
        this.devId = devId;
        this.dm2Id = dm2Id;
        this.parentDevId = parentDevId;
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.commandFlg = commandFlg;
        this.memo = memo;
        this.repeaterId = repeaterId;
        this.srvEnt = srvEnt;
    }




    public Long getRepeaterMngId() {
        return repeaterMngId;
    }
    public void setRepeaterMngId(Long repeaterMngId) {
        this.repeaterMngId = repeaterMngId;
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
    public Long getDm2Id() {
        return dm2Id;
    }
    public void setDm2Id(Long dm2Id) {
        this.dm2Id = dm2Id;
    }
    public String getParentDevId() {
        return parentDevId;
    }
    public void setParentDevId(String parentDevId) {
        this.parentDevId = parentDevId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
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
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
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
    public String getRepeaterId() {
        return repeaterId;
    }
    public void setRepeaterId(String repeaterId) {
        this.repeaterId = repeaterId;
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



}
