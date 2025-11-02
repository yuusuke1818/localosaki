package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MConcentratorResultSet {

    private Long concentId;
    private String devKind;
    private String devId;
    private Long dm2Id;
    private String parentDevId;
    private String corpId;
    private Long buildingId;
    private String commandFlg;
    private String srvEnt;
    private BigDecimal ifType;
    private Timestamp recDate;
    private String recMan;
    private String ipAddr;
    private BigDecimal concentSta;
    private String name;
    private String memo;
    private Long  createUserId;
    private Timestamp createDate;
    private Long updateUserId;
    private Timestamp updateDate;

    public MConcentratorResultSet(Long concent_id, String dev_kind, String dev_id, Long dm2_id, String parent_dev_id, String corp_id, Long building_id,
            String command_flg, String srv_ent, BigDecimal if_type, String ip_addr, BigDecimal concent_sta, String name, String memo) {
        this.concentId = concent_id;
        this.devKind = dev_kind;
        this.devId = dev_id;
        this.dm2Id = dm2_id;
        this.parentDevId = parent_dev_id;
        this.corpId = corp_id;
        this.buildingId = building_id;
        this.commandFlg = command_flg;
        this.srvEnt = srv_ent;
        this.ifType = if_type;
        this.ipAddr = ip_addr;
        this.concentSta = concent_sta;
        this.name = name;
        this.memo = memo;
    }

    public MConcentratorResultSet() {

    }

    public Long getConcentId() {
        return concentId;
    }
    public void setConcentId(Long concent_id) {
        this.concentId = concent_id;
    }
    public String getDevKind() {
        return devKind;
    }
    public void setDevKind(String dev_kind) {
        this.devKind = dev_kind;
    }
    public String getDevId() {
        return devId;
    }
    public void setDevId(String dev_id) {
        this.devId = dev_id;
    }
    public Long getDm2Id() {
        return dm2Id;
    }
    public void setDm2Id(Long dm2_id) {
        this.dm2Id = dm2_id;
    }
    public String getParentDevId() {
        return parentDevId;
    }
    public void setParentDevId(String parent_dev_id) {
        this.parentDevId = parent_dev_id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corp_id) {
        this.corpId = corp_id;
    }
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long building_id) {
        this.buildingId = building_id;
    }
    public String getCommandFlg() {
        return commandFlg;
    }
    public void setCommandFlg(String command_flg) {
        this.commandFlg = command_flg;
    }
    public String getSrvEnt() {
        return srvEnt;
    }
    public void setSrvEnt(String srv_ent) {
        this.srvEnt = srv_ent;
    }
    public BigDecimal getIfType() {
        return ifType;
    }
    public void setIfType(BigDecimal if_type) {
        this.ifType = if_type;
    }
    public String getIpAddr() {
        return ipAddr;
    }
    public void setIpAddr(String ip_addr) {
        this.ipAddr = ip_addr;
    }
    public BigDecimal getConcentSta() {
        return concentSta;
    }
    public void setConcentSta(BigDecimal concent_sta) {
        this.concentSta = concent_sta;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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

    public void setRecDate(Timestamp rec_date) {
        this.recDate = rec_date;
    }

    public String getRecMan() {
        return recMan;
    }

    public void setRecMan(String rec_man) {
        this.recMan = rec_man;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long create_userId) {
        this.createUserId = create_userId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long update_userId) {
        this.updateUserId = update_userId;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp update_date) {
        this.updateDate = update_date;
    }



}
