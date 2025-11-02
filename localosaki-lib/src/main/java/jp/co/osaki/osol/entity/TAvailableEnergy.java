package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the t_available_energy database table.
 *
 */
@Entity
@Table(name = "t_available_energy")
@NamedQueries({
    @NamedQuery(name = "TAvailableEnergy.findAll",
            query = "SELECT t FROM TAvailableEnergy t"),
    @NamedQuery(name = "TAvailableEnergy.findEngTypeCdCorpIdBuildingId",
            query = "SELECT t FROM TAvailableEnergy t "
            + "INNER JOIN FETCH t.MEnergy me "
            + "INNER JOIN FETCH me.MEnergyType met "
            + "INNER JOIN FETCH t.TBuildingEnergyType tet "
            + "WHERE t.id.corpId =:corpId "
            + "AND t.id.buildingId =:buildingId "
            + "AND (:engTypeCd is null or t.id.engTypeCd =:engTypeCd)  "
            + "AND t.delFlg = 0"
            + "ORDER BY tet.displayOrder, met.displayOrder, t.displayOrder"),
    @NamedQuery(name = "TAvailableEnergy.energyInput",
            query = "SELECT t FROM TAvailableEnergy t "
            + "LEFT JOIN t.TBuildingEnergyType tb "
            + "LEFT JOIN FETCH t.TEnergyUses "
            + "INNER JOIN FETCH t.MEnergy me "
            + "INNER JOIN FETCH me.MEnergyType met "
            + "WHERE t.id.corpId =:corpId AND t.id.buildingId =:buildingId "
            + "AND t.delFlg = 0 "
            + "AND t.energyStartYm <=:currentYmDate "
            + "AND (t.energyEndYm IS NULL OR t.energyEndYm >=:currentYmDate) "
            + "ORDER BY tb.displayOrder, met.displayOrder, t.displayOrder, t.customerNo"),
    @NamedQuery(name = "TAvailableEnergy.findDistinctEnergyCd",
            query = "SELECT t.id.corpId, t.id.buildingId, t.id.engTypeCd, MIN(t.displayOrder) FROM TAvailableEnergy t "
            + "WHERE t.id.corpId =:corpId AND t.id.buildingId =:buildingId AND t.energyStartYm <=:currentYm AND (t.energyEndYm IS NULL OR t.energyEndYm <=:currentYm) GROUP BY t.id.corpId, t.id.buildingId, t.id.engTypeCd ORDER BY MIN(t.displayOrder)"),
    @NamedQuery(name = "TAvailableEnergy.energyInputYearSearchBuildingNo",
            query = "SELECT t FROM TAvailableEnergy t "
            + "LEFT JOIN t.TBuildingEnergyType tbet "
            + "LEFT JOIN t.TBuilding tb "
            + "LEFT JOIN FETCH t.TEnergyUses teu "
            + "INNER JOIN FETCH t.MEnergy me "
            + "INNER JOIN FETCH me.MEnergyType met "
            + "WHERE t.id.corpId =:corpId "
            + "AND tb.delFlg =:delFlg "
            + "AND tb.buildingDelDate IS NULL "
            + "AND (:buildingNo IS NULL OR tb.buildingNo =:buildingNo) "
            + "AND (t.energyStartYm <=:nendoEndYmDate "
            + "AND (t.energyEndYm IS NULL OR t.energyEndYm >=:nendoStartYmDate) "
            + ") "
            + "AND t.delFlg =:delFlg "
            + "ORDER BY tb.buildingNo, tbet.displayOrder, met.displayOrder, t.displayOrder, t.customerNo"),
    @NamedQuery(name = "TAvailableEnergy.energyInputYearSearchGroup",
            query = "SELECT t FROM TAvailableEnergy t "
            + "LEFT JOIN t.TBuildingEnergyType tbet "
            + "LEFT JOIN t.TBuilding tb "
            + "LEFT JOIN tb.TBuildingGroups tbg "
            + "ON tbg.delFlg = 0 "
            + "LEFT JOIN tbg.MChildGroup cg "
            + "ON cg.delFlg = 0 "
            + "LEFT JOIN cg.MParentGroup pg "
            + "ON pg.delFlg = 0 "
            + "LEFT JOIN FETCH t.TEnergyUses teu "
            + "INNER JOIN FETCH t.MEnergy me "
            + "INNER JOIN FETCH me.MEnergyType met "
            + "WHERE t.id.corpId =:corpId "
            + "AND tb.delFlg =:delFlg "
            + "AND tb.buildingDelDate IS NULL "
            + "AND (t.energyStartYm <=:nendoEndYmDate "
            + "AND (t.energyEndYm IS NULL OR t.energyEndYm >=:nendoStartYmDate) "
            + ") "
            + "AND tbg.id.parentGroupId =:parentGroupId "
            + "AND (:childGroupId IS NULL OR tbg.id.childGroupId =:childGroupId) "
            + "AND t.delFlg =:delFlg "
            + "ORDER BY tb.buildingNo, tbet.displayOrder, met.displayOrder, t.displayOrder, t.customerNo"),})

public class TAvailableEnergy implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TAvailableEnergyPK id;

    @Column(name = "contract_biko", length = 2000)
    private String contractBiko;

    @Column(name = "contract_power", precision = 23, scale = 10)
    private BigDecimal contractPower;

    @Column(name = "contract_power_unit", length = 100)
    private String contractPowerUnit;

    @Column(name = "contract_type", length = 100)
    private String contractType;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "customer_no", length = 100)
    private String customerNo;

    @Column(name = "day_and_night_type", nullable = false, length = 6)
    private String dayAndNightType;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Temporal(TemporalType.DATE)
    @Column(name = "energy_end_ym")
    private Date energyEndYm;

    @Temporal(TemporalType.DATE)
    @Column(name = "energy_start_ym", nullable = false)
    private Date energyStartYm;

    @Column(name="energy_use_line_value_flg", nullable=false)
    private Integer energyUseLineValueFlg;

    @Column(name = "eng_supply_type", nullable = false, length = 6)
    private String engSupplyType;

    @Column(name = "input_flg", nullable = false)
    private Integer inputFlg;

    @Column(name = "supply_point_specific_no", length = 27)
    private String supplyPointSpecificNo;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Column(name = "use_place", length = 100)
    private String usePlace;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MEnergy
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "eng_id", referencedColumnName = "eng_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "eng_type_cd", referencedColumnName = "eng_type_cd", nullable = false, insertable = false, updatable = false)
    })
    private MEnergy MEnergy;

    //bi-directional many-to-one association to TBuilding
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false)
    })
    private TBuilding TBuilding;

    //bi-directional many-to-one association to TBuildingEnergyType
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "eng_type_cd", referencedColumnName = "eng_type_cd", nullable = false, insertable = false, updatable = false)
    })
    private TBuildingEnergyType TBuildingEnergyType;

    //bi-directional many-to-one association to TEnergyUse
    @OneToMany(mappedBy = "TAvailableEnergy")
    private List<TEnergyUse> TEnergyUses;

    //bi-directional many-to-one association to TAvailableEnergyLine
    @OneToMany(mappedBy="TAvailableEnergy")
    private List<TAvailableEnergyLine> TAvailableEnergyLines;

    public TAvailableEnergy() {
    }

    public TAvailableEnergyPK getId() {
        return this.id;
    }

    public void setId(TAvailableEnergyPK id) {
        this.id = id;
    }

    public String getContractBiko() {
        return this.contractBiko;
    }

    public void setContractBiko(String contractBiko) {
        this.contractBiko = contractBiko;
    }

    public BigDecimal getContractPower() {
        return this.contractPower;
    }

    public void setContractPower(BigDecimal contractPower) {
        this.contractPower = contractPower;
    }

    public String getContractPowerUnit() {
        return this.contractPowerUnit;
    }

    public void setContractPowerUnit(String contractPowerUnit) {
        this.contractPowerUnit = contractPowerUnit;
    }

    public String getContractType() {
        return this.contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
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

    public String getCustomerNo() {
        return this.customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDayAndNightType() {
        return this.dayAndNightType;
    }

    public void setDayAndNightType(String dayAndNightType) {
        this.dayAndNightType = dayAndNightType;
    }

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Date getEnergyEndYm() {
        return this.energyEndYm;
    }

    public void setEnergyEndYm(Date energyEndYm) {
        this.energyEndYm = energyEndYm;
    }

    public Date getEnergyStartYm() {
        return this.energyStartYm;
    }

    public void setEnergyStartYm(Date energyStartYm) {
        this.energyStartYm = energyStartYm;
    }

    public Integer getEnergyUseLineValueFlg() {
        return this.energyUseLineValueFlg;
    }

    public void setEnergyUseLineValueFlg(Integer energyUseLineValueFlg) {
        this.energyUseLineValueFlg = energyUseLineValueFlg;
    }

    public String getEngSupplyType() {
        return this.engSupplyType;
    }

    public void setEngSupplyType(String engSupplyType) {
        this.engSupplyType = engSupplyType;
    }

    public Integer getInputFlg() {
        return this.inputFlg;
    }

    public void setInputFlg(Integer inputFlg) {
        this.inputFlg = inputFlg;
    }

    public String getSupplyPointSpecificNo() {
        return this.supplyPointSpecificNo;
    }

    public void setSupplyPointSpecificNo(String supplyPointSpecificNo) {
        this.supplyPointSpecificNo = supplyPointSpecificNo;
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

    public String getUsePlace() {
        return this.usePlace;
    }

    public void setUsePlace(String usePlace) {
        this.usePlace = usePlace;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public MEnergy getMEnergy() {
        return this.MEnergy;
    }

    public void setMEnergy(MEnergy MEnergy) {
        this.MEnergy = MEnergy;
    }

    public TBuilding getTBuilding() {
        return this.TBuilding;
    }

    public void setTBuilding(TBuilding TBuilding) {
        this.TBuilding = TBuilding;
    }

    public TBuildingEnergyType getTBuildingEnergyType() {
        return this.TBuildingEnergyType;
    }

    public void setTBuildingEnergyType(TBuildingEnergyType TBuildingEnergyType) {
        this.TBuildingEnergyType = TBuildingEnergyType;
    }

    public List<TEnergyUse> getTEnergyUses() {
        return this.TEnergyUses;
    }

    public void setTEnergyUses(List<TEnergyUse> TEnergyUses) {
        this.TEnergyUses = TEnergyUses;
    }

    public TEnergyUse addTEnergyUs(TEnergyUse TEnergyUs) {
        getTEnergyUses().add(TEnergyUs);
        TEnergyUs.setTAvailableEnergy(this);

        return TEnergyUs;
    }

    public TEnergyUse removeTEnergyUs(TEnergyUse TEnergyUs) {
        getTEnergyUses().remove(TEnergyUs);
        TEnergyUs.setTAvailableEnergy(null);

        return TEnergyUs;
    }

    public List<TAvailableEnergyLine> getTAvailableEnergyLines() {
        return this.TAvailableEnergyLines;
    }

    public void setTAvailableEnergyLines(List<TAvailableEnergyLine> TAvailableEnergyLines) {
        this.TAvailableEnergyLines = TAvailableEnergyLines;
    }

    public TAvailableEnergyLine addTAvailableEnergyLine(TAvailableEnergyLine TAvailableEnergyLine) {
        getTAvailableEnergyLines().add(TAvailableEnergyLine);
        TAvailableEnergyLine.setTAvailableEnergy(this);

        return TAvailableEnergyLine;
    }

    public TAvailableEnergyLine removeTAvailableEnergyLine(TAvailableEnergyLine TAvailableEnergyLine) {
        getTAvailableEnergyLines().remove(TAvailableEnergyLine);
        TAvailableEnergyLine.setTAvailableEnergy(null);

        return TAvailableEnergyLine;
    }

}
