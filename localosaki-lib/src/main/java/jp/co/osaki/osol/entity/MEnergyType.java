package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_energy_type database table.
 *
 */
@Entity
@Table(name = "m_energy_type")
@NamedQueries({
    @NamedQuery(name = "MEnergyType.findAll", query = "SELECT m FROM MEnergyType m ORDER BY m.displayOrder ASC"),
    @NamedQuery(name = "MEnergyType.findAllOrderByAsc", query = "SELECT m FROM MEnergyType m ORDER BY m.engTypeCd ASC"),
    @NamedQuery(name = "MEnergyType.findEngTypeName", query = "SELECT m FROM MEnergyType m WHERE m.engTypeName =:engTypeName ORDER BY m.engTypeCd ASC"),
    @NamedQuery(name = "MEnergyType.findAllOmitEstimateCd", query = "SELECT m FROM MEnergyType m WHERE m.engTypeCd <>'999' ORDER BY m.displayOrder ASC"),
    @NamedQuery(name = "MEnergyType.findNotUseCorpId", query = "SELECT m FROM MEnergyType m WHERE NOT EXISTS (SELECT mem FROM TAvailableEnergy mem WHERE m.engTypeCd = mem.id.engTypeCd AND mem.id.corpId =:corpId AND mem.id.buildingId =:buildingId)"),})
public class MEnergyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "eng_type_cd", unique = true, nullable = false, length = 3)
    private String engTypeCd;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "distribution_suikei_out_flg", nullable = false)
    private Integer distributionSuikeiOutFlg;

    @Column(name = "eng_type_name", nullable = false, length = 100)
    private String engTypeName;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MEnergy
    @OneToMany(mappedBy = "MEnergyType")
    private List<MEnergy> MEnergies;

    //bi-directional many-to-one association to MEnergyUseTargetMonthly
    @OneToMany(mappedBy = "MEnergyType")
    private List<MEnergyUseTargetMonthly> MEnergyUseTargetMonthlies;

    //bi-directional many-to-one association to MFacility
    @OneToMany(mappedBy = "MEnergyType")
    private List<MFacility> MFacilities;

    public MEnergyType() {
    }

    public String getEngTypeCd() {
        return this.engTypeCd;
    }

    public void setEngTypeCd(String engTypeCd) {
        this.engTypeCd = engTypeCd;
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

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getDistributionSuikeiOutFlg() {
        return this.distributionSuikeiOutFlg;
    }

    public void setDistributionSuikeiOutFlg(Integer distributionSuikeiOutFlg) {
        this.distributionSuikeiOutFlg = distributionSuikeiOutFlg;
    }

    public String getEngTypeName() {
        return this.engTypeName;
    }

    public void setEngTypeName(String engTypeName) {
        this.engTypeName = engTypeName;
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

    public List<MEnergy> getMEnergies() {
        return this.MEnergies;
    }

    public void setMEnergies(List<MEnergy> MEnergies) {
        this.MEnergies = MEnergies;
    }

    public MEnergy addMEnergy(MEnergy MEnergy) {
        getMEnergies().add(MEnergy);
        MEnergy.setMEnergyType(this);

        return MEnergy;
    }

    public MEnergy removeMEnergy(MEnergy MEnergy) {
        getMEnergies().remove(MEnergy);
        MEnergy.setMEnergyType(null);

        return MEnergy;
    }

    public List<MEnergyUseTargetMonthly> getMEnergyUseTargetMonthlies() {
        return this.MEnergyUseTargetMonthlies;
    }

    public void setMEnergyUseTargetMonthlies(List<MEnergyUseTargetMonthly> MEnergyUseTargetMonthlies) {
        this.MEnergyUseTargetMonthlies = MEnergyUseTargetMonthlies;
    }

    public MEnergyUseTargetMonthly addMEnergyUseTargetMonthly(MEnergyUseTargetMonthly MEnergyUseTargetMonthly) {
        getMEnergyUseTargetMonthlies().add(MEnergyUseTargetMonthly);
        MEnergyUseTargetMonthly.setMEnergyType(this);

        return MEnergyUseTargetMonthly;
    }

    public MEnergyUseTargetMonthly removeMEnergyUseTargetMonthly(MEnergyUseTargetMonthly MEnergyUseTargetMonthly) {
        getMEnergyUseTargetMonthlies().remove(MEnergyUseTargetMonthly);
        MEnergyUseTargetMonthly.setMEnergyType(null);

        return MEnergyUseTargetMonthly;
    }

    public List<MFacility> getMFacilities() {
        return this.MFacilities;
    }

    public void setMFacilities(List<MFacility> MFacilities) {
        this.MFacilities = MFacilities;
    }

    public MFacility addMFacility(MFacility MFacility) {
        getMFacilities().add(MFacility);
        MFacility.setMEnergyType(this);

        return MFacility;
    }

    public MFacility removeMFacility(MFacility MFacility) {
        getMFacilities().remove(MFacility);
        MFacility.setMEnergyType(null);

        return MFacility;
    }

}
