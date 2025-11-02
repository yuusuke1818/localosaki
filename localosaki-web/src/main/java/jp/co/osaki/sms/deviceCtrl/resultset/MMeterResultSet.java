package jp.co.osaki.sms.deviceCtrl.resultset;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MMeterResultSet {

    private String devId;
    private String devKind;
    private Long meterMngId;
    private Timestamp recDate;
    private String recMan;
    private String commandFlg;
    private String srvEnt;
    private String Name;
    private String meterId;
    private String meterIdOld;
    private BigDecimal ifType;
    private BigDecimal concentId;
    private String termAddr;
    private Long buildingId;
    private String openMode;
    private BigDecimal meterSta;
    private BigDecimal termSta;
    private String memo;
    private BigDecimal meterType;
    private String comMeter;
    private BigDecimal basicPrice;
    private String examEndYm;
    private String examNotice;
    private BigDecimal multi;
    private BigDecimal pulseWeight;
    private String pulseWeightChg;
    private String pulseType;
    private String pulseTypeChg;
    private BigDecimal currentData;
    private String currentDataChg;
    private String wirelessType;
    private String wirelessId;
    private String hop1Id;
    private String hop2Id;
    private String hop3Id;
    private String pollingId;
    private String dispYearFlg;
    private String alarm;
    private Long createUserId;
    private Timestamp createDate;
    private Long updateUserId;
    private Timestamp updateDate;
    private String buildingName;


    public MMeterResultSet() {

    }


    public MMeterResultSet(String devId, String devKind, Long meterMngId, String commandFlg, String srvEnt, String name,
            String meterId, String meterIdOld, BigDecimal ifType, BigDecimal concentId, String termAddr,
            Long buildingId, String openMode, BigDecimal meterSta, BigDecimal termSta, String memo,
            BigDecimal meterType, String comMeter, BigDecimal basicPrice, String examEndYm, String examNotice,
            BigDecimal multi, BigDecimal pulseWeight, String pulseWeightChg, String pulseType, String pulseTypeChg,
            BigDecimal currentData, String currentDataChg, String wirelessType, String wirelessId, String hop1Id,
            String hop2Id, String hop3Id, String pollingId, String dispYearFlg, String alarm) {
        this.devId = devId;
        this.devKind = devKind;
        this.meterMngId = meterMngId;
        this.commandFlg = commandFlg;
        this.srvEnt = srvEnt;
        Name = name;
        this.meterId = meterId;
        this.meterIdOld = meterIdOld;
        this.ifType = ifType;
        this.concentId = concentId;
        this.termAddr = termAddr;
        this.buildingId = buildingId;
        this.openMode = openMode;
        this.meterSta = meterSta;
        this.termSta = termSta;
        this.memo = memo;
        this.meterType = meterType;
        this.comMeter = comMeter;
        this.basicPrice = basicPrice;
        this.examEndYm = examEndYm;
        this.examNotice = examNotice;
        this.multi = multi;
        this.pulseWeight = pulseWeight;
        this.pulseWeightChg = pulseWeightChg;
        this.pulseType = pulseType;
        this.pulseTypeChg = pulseTypeChg;
        this.currentData = currentData;
        this.currentDataChg = currentDataChg;
        this.wirelessType = wirelessType;
        this.wirelessId = wirelessId;
        this.hop1Id = hop1Id;
        this.hop2Id = hop2Id;
        this.hop3Id = hop3Id;
        this.pollingId = pollingId;
        this.dispYearFlg = dispYearFlg;
        this.alarm = alarm;
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
    public String getCommandFlg() {
        return commandFlg;
    }
    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
    }
    public String getSrvEnt() {
        return srvEnt;
    }
    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
    public String getMeterIdOld() {
        return meterIdOld;
    }
    public void setMeterIdOld(String meterIdOld) {
        this.meterIdOld = meterIdOld;
    }
    public BigDecimal getIfType() {
        return ifType;
    }
    public void setIfType(BigDecimal ifType) {
        this.ifType = ifType;
    }
    public BigDecimal getConcentId() {
        return concentId;
    }
    public void setConcentId(BigDecimal concentId) {
        this.concentId = concentId;
    }
    public String getTermAddr() {
        return termAddr;
    }
    public void setTermAddr(String termAddr) {
        this.termAddr = termAddr;
    }
    public Long getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
    public String getOpenMode() {
        return openMode;
    }
    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }
    public BigDecimal getMeterSta() {
        return meterSta;
    }
    public void setMeterSta(BigDecimal meterSta) {
        this.meterSta = meterSta;
    }
    public BigDecimal getTermSta() {
        return termSta;
    }
    public void setTermSta(BigDecimal termSta) {
        this.termSta = termSta;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public BigDecimal getMeterType() {
        return meterType;
    }
    public void setMeterType(BigDecimal meterType) {
        this.meterType = meterType;
    }
    public String getComMeter() {
        return comMeter;
    }
    public void setComMeter(String comMeter) {
        this.comMeter = comMeter;
    }
    public BigDecimal getBasicPrice() {
        return basicPrice;
    }
    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }
    public String getExamEndYm() {
        return examEndYm;
    }
    public void setExamEndYm(String examEndYm) {
        this.examEndYm = examEndYm;
    }
    public String getExamNotice() {
        return examNotice;
    }
    public void setExamNotice(String examNotice) {
        this.examNotice = examNotice;
    }
    public BigDecimal getMulti() {
        return multi;
    }
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }
    public BigDecimal getPulseWeight() {
        return pulseWeight;
    }
    public void setPulseWeight(BigDecimal pulseWeight) {
        this.pulseWeight = pulseWeight;
    }
    public String getPulseWeightChg() {
        return pulseWeightChg;
    }
    public void setPulseWeightChg(String pulseWeightChg) {
        this.pulseWeightChg = pulseWeightChg;
    }
    public String getPulseType() {
        return pulseType;
    }
    public void setPulseType(String pulseType) {
        this.pulseType = pulseType;
    }
    public String getPulseTypeChg() {
        return pulseTypeChg;
    }
    public void setPulseTypeChg(String pulseTypeChg) {
        this.pulseTypeChg = pulseTypeChg;
    }
    public BigDecimal getCurrentData() {
        return currentData;
    }
    public void setCurrentData(BigDecimal currentData) {
        this.currentData = currentData;
    }
    public String getCurrentDataChg() {
        return currentDataChg;
    }
    public void setCurrentDataChg(String currentDataChg) {
        this.currentDataChg = currentDataChg;
    }
    public String getWirelessType() {
        return wirelessType;
    }
    public void setWirelessType(String wirelessType) {
        this.wirelessType = wirelessType;
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
    public String getDispYearFlg() {
        return dispYearFlg;
    }
    public void setDispYearFlg(String dispYearFlg) {
        this.dispYearFlg = dispYearFlg;
    }
    public String getAlarm() {
        return alarm;
    }
    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
    public Long getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }
    public Timestamp getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }




    public String getDevKind() {
        return devKind;
    }




    public void setDevKind(String devKind) {
        this.devKind = devKind;
    }


    public String getBuildingName() {
        return buildingName;
    }


    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }










}
