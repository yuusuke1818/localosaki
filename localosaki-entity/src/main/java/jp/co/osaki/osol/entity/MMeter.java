package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_meter database table.
 *
 */
@Entity
@Table(name="m_meter")
@NamedQuery(name="MMeter.findAll", query="SELECT m FROM MMeter m")
public class MMeter implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MMeterPK id;

    @Column(length=40)
    private String alarm;

    @Column(name="alert_pause_end", length=8)
    private String alertPauseEnd;

    @Column(name="alert_pause_flg", precision=1)
    private BigDecimal alertPauseFlg;

    @Column(name="alert_pause_start", length=8)
    private String alertPauseStart;

    @Column(name="basic_price", precision=6, scale=2)
    private BigDecimal basicPrice;

    @Column(name="com_meter", length=3)
    private String comMeter;

    @Column(name="command_flg", length=1)
    private String commandFlg;

    @Column(name="concent_id", precision=2)
    private BigDecimal concentId;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="current_data", precision=8)
    private BigDecimal currentData;

    @Column(name="current_data_chg", length=1)
    private String currentDataChg;

    @Column(name="del_flg", nullable=false)
    private Integer delFlg;

    @Column(name="disp_year_flg", length=1)
    private String dispYearFlg;

    @Column(name="exam_end_ym", length=6)
    private String examEndYm;

    @Column(name="exam_notice", length=1)
    private String examNotice;

    @Column(name="hop1_id", length=12)
    private String hop1Id;

    @Column(name="hop2_id", length=12)
    private String hop2Id;

    @Column(name="hop3_id", length=12)
    private String hop3Id;

    @Column(name="if_type", precision=1)
    private BigDecimal ifType;

    @Column(length=40)
    private String memo;

    @Column(name="meter_id", length=10)
    private String meterId;

    @Column(name="meter_id_old", length=20)
    private String meterIdOld;

    @Column(name="meter_pres_situ", precision=1)
    private BigDecimal meterPresSitu;

    @Column(name="meter_sta", precision=1)
    private BigDecimal meterSta;

    @Column(name="meter_sta_memo", length=1000)
    private String meterStaMemo;

    @Column(name="meter_type")
    private Long meterType;

    @Column(precision=4)
    private BigDecimal multi;

    @Column(length=20)
    private String name;

    @Column(name="open_mode", length=2)
    private String openMode;

    @Column(name="polling_id", length=40)
    private String pollingId;

    @Column(name="pulse_type", length=1)
    private String pulseType;

    @Column(name="pulse_type_chg", length=1)
    private String pulseTypeChg;

    @Column(name="pulse_weight", precision=5)
    private BigDecimal pulseWeight;

    @Column(name="pulse_weight_chg", length=1)
    private String pulseWeightChg;

    @Column(name="rec_date", nullable=false)
    private Timestamp recDate;

    @Column(name="rec_man", nullable=false, length=50)
    private String recMan;

    @Column(name="reserve_insp_date")
    private Timestamp reserveInspDate;

    @Column(name="srv_ent", length=1)
    private String srvEnt;

    @Column(name="term_addr", length=2)
    private String termAddr;

    @Column(name="term_sta", precision=1)
    private BigDecimal termSta;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    @Column(name="wireless_id", length=12)
    private String wirelessId;

    @Column(name="wireless_type", length=1)
    private String wirelessType;

    //bi-directional many-to-one association to MManualInsp
    @OneToMany(mappedBy="MMeter")
    private List<MManualInsp> MManualInsps;

    //bi-directional many-to-one association to MDevPrm
    @ManyToOne
    @JoinColumn(name="dev_id", nullable=false, insertable=false, updatable=false)
    private MDevPrm MDevPrm;

    //bi-directional many-to-one association to MMeterGroup
    @OneToMany(mappedBy="MMeter")
    private List<MMeterGroup> MMeterGroups;

    //bi-directional many-to-one association to MMeterLoadlimit
    @OneToMany(mappedBy="MMeter")
    private List<MMeterLoadlimit> MMeterLoadlimits;

    //bi-directional many-to-one association to TBuildDevMeterRelation
    @OneToMany(mappedBy="MMeter")
    private List<TBuildDevMeterRelation> TBuildDevMeterRelations;

    public MMeter() {
    }

    public MMeterPK getId() {
        return this.id;
    }

    public void setId(MMeterPK id) {
        this.id = id;
    }

    public String getAlarm() {
        return this.alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getAlertPauseEnd() {
        return this.alertPauseEnd;
    }

    public void setAlertPauseEnd(String alertPauseEnd) {
        this.alertPauseEnd = alertPauseEnd;
    }

    public BigDecimal getAlertPauseFlg() {
        return this.alertPauseFlg;
    }

    public void setAlertPauseFlg(BigDecimal alertPauseFlg) {
        this.alertPauseFlg = alertPauseFlg;
    }

    public String getAlertPauseStart() {
        return this.alertPauseStart;
    }

    public void setAlertPauseStart(String alertPauseStart) {
        this.alertPauseStart = alertPauseStart;
    }

    public BigDecimal getBasicPrice() {
        return this.basicPrice;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getComMeter() {
        return this.comMeter;
    }

    public void setComMeter(String comMeter) {
        this.comMeter = comMeter;
    }

    public String getCommandFlg() {
        return this.commandFlg;
    }

    public void setCommandFlg(String commandFlg) {
        this.commandFlg = commandFlg;
    }

    public BigDecimal getConcentId() {
        return this.concentId;
    }

    public void setConcentId(BigDecimal concentId) {
        this.concentId = concentId;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public BigDecimal getCurrentData() {
        return this.currentData;
    }

    public void setCurrentData(BigDecimal currentData) {
        this.currentData = currentData;
    }

    public String getCurrentDataChg() {
        return this.currentDataChg;
    }

    public void setCurrentDataChg(String currentDataChg) {
        this.currentDataChg = currentDataChg;
    }

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public String getDispYearFlg() {
        return this.dispYearFlg;
    }

    public void setDispYearFlg(String dispYearFlg) {
        this.dispYearFlg = dispYearFlg;
    }

    public String getExamEndYm() {
        return this.examEndYm;
    }

    public void setExamEndYm(String examEndYm) {
        this.examEndYm = examEndYm;
    }

    public String getExamNotice() {
        return this.examNotice;
    }

    public void setExamNotice(String examNotice) {
        this.examNotice = examNotice;
    }

    public String getHop1Id() {
        return this.hop1Id;
    }

    public void setHop1Id(String hop1Id) {
        this.hop1Id = hop1Id;
    }

    public String getHop2Id() {
        return this.hop2Id;
    }

    public void setHop2Id(String hop2Id) {
        this.hop2Id = hop2Id;
    }

    public String getHop3Id() {
        return this.hop3Id;
    }

    public void setHop3Id(String hop3Id) {
        this.hop3Id = hop3Id;
    }

    public BigDecimal getIfType() {
        return this.ifType;
    }

    public void setIfType(BigDecimal ifType) {
        this.ifType = ifType;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMeterId() {
        return this.meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getMeterIdOld() {
        return this.meterIdOld;
    }

    public void setMeterIdOld(String meterIdOld) {
        this.meterIdOld = meterIdOld;
    }

    public BigDecimal getMeterPresSitu() {
        return this.meterPresSitu;
    }

    public void setMeterPresSitu(BigDecimal meterPresSitu) {
        this.meterPresSitu = meterPresSitu;
    }

    public BigDecimal getMeterSta() {
        return this.meterSta;
    }

    public void setMeterSta(BigDecimal meterSta) {
        this.meterSta = meterSta;
    }

    public String getMeterStaMemo() {
        return this.meterStaMemo;
    }

    public void setMeterStaMemo(String meterStaMemo) {
        this.meterStaMemo = meterStaMemo;
    }

    public Long getMeterType() {
        return this.meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public BigDecimal getMulti() {
        return this.multi;
    }

    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenMode() {
        return this.openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getPollingId() {
        return this.pollingId;
    }

    public void setPollingId(String pollingId) {
        this.pollingId = pollingId;
    }

    public String getPulseType() {
        return this.pulseType;
    }

    public void setPulseType(String pulseType) {
        this.pulseType = pulseType;
    }

    public String getPulseTypeChg() {
        return this.pulseTypeChg;
    }

    public void setPulseTypeChg(String pulseTypeChg) {
        this.pulseTypeChg = pulseTypeChg;
    }

    public BigDecimal getPulseWeight() {
        return this.pulseWeight;
    }

    public void setPulseWeight(BigDecimal pulseWeight) {
        this.pulseWeight = pulseWeight;
    }

    public String getPulseWeightChg() {
        return this.pulseWeightChg;
    }

    public void setPulseWeightChg(String pulseWeightChg) {
        this.pulseWeightChg = pulseWeightChg;
    }

    public Timestamp getRecDate() {
        return this.recDate;
    }

    public void setRecDate(Timestamp recDate) {
        this.recDate = recDate;
    }

    public String getRecMan() {
        return this.recMan;
    }

    public void setRecMan(String recMan) {
        this.recMan = recMan;
    }

    public Timestamp getReserveInspDate() {
        return this.reserveInspDate;
    }

    public void setReserveInspDate(Timestamp reserveInspDate) {
        this.reserveInspDate = reserveInspDate;
    }

    public String getSrvEnt() {
        return this.srvEnt;
    }

    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }

    public String getTermAddr() {
        return this.termAddr;
    }

    public void setTermAddr(String termAddr) {
        this.termAddr = termAddr;
    }

    public BigDecimal getTermSta() {
        return this.termSta;
    }

    public void setTermSta(BigDecimal termSta) {
        this.termSta = termSta;
    }

    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getWirelessId() {
        return this.wirelessId;
    }

    public void setWirelessId(String wirelessId) {
        this.wirelessId = wirelessId;
    }

    public String getWirelessType() {
        return this.wirelessType;
    }

    public void setWirelessType(String wirelessType) {
        this.wirelessType = wirelessType;
    }

    public List<MManualInsp> getMManualInsps() {
        return this.MManualInsps;
    }

    public void setMManualInsps(List<MManualInsp> MManualInsps) {
        this.MManualInsps = MManualInsps;
    }

    public MManualInsp addMManualInsp(MManualInsp MManualInsp) {
        getMManualInsps().add(MManualInsp);
        MManualInsp.setMMeter(this);

        return MManualInsp;
    }

    public MManualInsp removeMManualInsp(MManualInsp MManualInsp) {
        getMManualInsps().remove(MManualInsp);
        MManualInsp.setMMeter(null);

        return MManualInsp;
    }

    public MDevPrm getMDevPrm() {
        return this.MDevPrm;
    }

    public void setMDevPrm(MDevPrm MDevPrm) {
        this.MDevPrm = MDevPrm;
    }

    public List<MMeterGroup> getMMeterGroups() {
        return this.MMeterGroups;
    }

    public void setMMeterGroups(List<MMeterGroup> MMeterGroups) {
        this.MMeterGroups = MMeterGroups;
    }

    public MMeterGroup addMMeterGroup(MMeterGroup MMeterGroup) {
        getMMeterGroups().add(MMeterGroup);
        MMeterGroup.setMMeter(this);

        return MMeterGroup;
    }

    public MMeterGroup removeMMeterGroup(MMeterGroup MMeterGroup) {
        getMMeterGroups().remove(MMeterGroup);
        MMeterGroup.setMMeter(null);

        return MMeterGroup;
    }

    public List<MMeterLoadlimit> getMMeterLoadlimits() {
        return this.MMeterLoadlimits;
    }

    public void setMMeterLoadlimits(List<MMeterLoadlimit> MMeterLoadlimits) {
        this.MMeterLoadlimits = MMeterLoadlimits;
    }

    public MMeterLoadlimit addMMeterLoadlimit(MMeterLoadlimit MMeterLoadlimit) {
        getMMeterLoadlimits().add(MMeterLoadlimit);
        MMeterLoadlimit.setMMeter(this);

        return MMeterLoadlimit;
    }

    public MMeterLoadlimit removeMMeterLoadlimit(MMeterLoadlimit MMeterLoadlimit) {
        getMMeterLoadlimits().remove(MMeterLoadlimit);
        MMeterLoadlimit.setMMeter(null);

        return MMeterLoadlimit;
    }

    public List<TBuildDevMeterRelation> getTBuildDevMeterRelations() {
        return this.TBuildDevMeterRelations;
    }

    public void setTBuildDevMeterRelations(List<TBuildDevMeterRelation> TBuildDevMeterRelations) {
        this.TBuildDevMeterRelations = TBuildDevMeterRelations;
    }

    public TBuildDevMeterRelation addTBuildDevMeterRelation(TBuildDevMeterRelation TBuildDevMeterRelation) {
        getTBuildDevMeterRelations().add(TBuildDevMeterRelation);
        TBuildDevMeterRelation.setMMeter(this);

        return TBuildDevMeterRelation;
    }

    public TBuildDevMeterRelation removeTBuildDevMeterRelation(TBuildDevMeterRelation TBuildDevMeterRelation) {
        getTBuildDevMeterRelations().remove(TBuildDevMeterRelation);
        TBuildDevMeterRelation.setMMeter(null);

        return TBuildDevMeterRelation;
    }

}