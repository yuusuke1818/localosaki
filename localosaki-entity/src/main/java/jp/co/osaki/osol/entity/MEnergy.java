package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_energy database table.
 * 
 */
@Entity
@Table(name = "m_energy")
@NamedQueries({
    @NamedQuery(name = "MEnergy.findAll", query = "SELECT m FROM MEnergy m"),
    @NamedQuery(name = "MEnergy.findNotUse", query = "SELECT m FROM MEnergy m WHERE NOT EXISTS (SELECT c FROM m.TAvailableEnergies c WHERE c.id.corpId =:corpId) AND m.MEnergyType.engTypeCd =:engTypeCd"),
    @NamedQuery(name = "MEnergy.findEnergyId",
            query = "SELECT m FROM MEnergy m "
            + "WHERE m.id.engTypeCd =:engTypeCd "
            + " AND (:supplyArea IS NULL OR m.supplyArea like :supplyArea) "
            + " AND (:supplyCompany IS NULL OR m.supplyCompany like :supplyCompany) "
            + "ORDER BY m.displayOrder ASC "),})
public class MEnergy implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MEnergyPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="eng_name", nullable=false, length=100)
	private String engName;

	@Column(name="supply_area", length=150)
	private String supplyArea;

	@Column(name="supply_company", nullable=false, length=100)
	private String supplyCompany;

	@Column(name="supply_company_biko", length=2000)
	private String supplyCompanyBiko;

	@Column(name="supply_company_tel_no", length=100)
	private String supplyCompanyTelNo;

	@Column(name="supply_company_url", length=300)
	private String supplyCompanyUrl;

	@Column(nullable=false, length=100)
	private String unit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCoefficientHistoryManage
	@OneToMany(mappedBy="MEnergy")
	private List<MCoefficientHistoryManage> MCoefficientHistoryManages;

	//bi-directional many-to-one association to MEnergyType
	@ManyToOne
	@JoinColumn(name="eng_type_cd", nullable=false, insertable=false, updatable=false)
	private MEnergyType MEnergyType;

	//bi-directional many-to-one association to TAvailableEnergy
	@OneToMany(mappedBy="MEnergy")
	private List<TAvailableEnergy> TAvailableEnergies;

	public MEnergy() {
	}

	public MEnergyPK getId() {
		return this.id;
	}

	public void setId(MEnergyPK id) {
		this.id = id;
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

	public String getEngName() {
		return this.engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getSupplyArea() {
		return this.supplyArea;
	}

	public void setSupplyArea(String supplyArea) {
		this.supplyArea = supplyArea;
	}

	public String getSupplyCompany() {
		return this.supplyCompany;
	}

	public void setSupplyCompany(String supplyCompany) {
		this.supplyCompany = supplyCompany;
	}

	public String getSupplyCompanyBiko() {
		return this.supplyCompanyBiko;
	}

	public void setSupplyCompanyBiko(String supplyCompanyBiko) {
		this.supplyCompanyBiko = supplyCompanyBiko;
	}

	public String getSupplyCompanyTelNo() {
		return this.supplyCompanyTelNo;
	}

	public void setSupplyCompanyTelNo(String supplyCompanyTelNo) {
		this.supplyCompanyTelNo = supplyCompanyTelNo;
	}

	public String getSupplyCompanyUrl() {
		return this.supplyCompanyUrl;
	}

	public void setSupplyCompanyUrl(String supplyCompanyUrl) {
		this.supplyCompanyUrl = supplyCompanyUrl;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public List<MCoefficientHistoryManage> getMCoefficientHistoryManages() {
		return this.MCoefficientHistoryManages;
	}

	public void setMCoefficientHistoryManages(List<MCoefficientHistoryManage> MCoefficientHistoryManages) {
		this.MCoefficientHistoryManages = MCoefficientHistoryManages;
	}

	public MCoefficientHistoryManage addMCoefficientHistoryManage(MCoefficientHistoryManage MCoefficientHistoryManage) {
		getMCoefficientHistoryManages().add(MCoefficientHistoryManage);
		MCoefficientHistoryManage.setMEnergy(this);

		return MCoefficientHistoryManage;
	}

	public MCoefficientHistoryManage removeMCoefficientHistoryManage(MCoefficientHistoryManage MCoefficientHistoryManage) {
		getMCoefficientHistoryManages().remove(MCoefficientHistoryManage);
		MCoefficientHistoryManage.setMEnergy(null);

		return MCoefficientHistoryManage;
	}

	public MEnergyType getMEnergyType() {
		return this.MEnergyType;
	}

	public void setMEnergyType(MEnergyType MEnergyType) {
		this.MEnergyType = MEnergyType;
	}

	public List<TAvailableEnergy> getTAvailableEnergies() {
		return this.TAvailableEnergies;
	}

	public void setTAvailableEnergies(List<TAvailableEnergy> TAvailableEnergies) {
		this.TAvailableEnergies = TAvailableEnergies;
	}

	public TAvailableEnergy addTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		getTAvailableEnergies().add(TAvailableEnergy);
		TAvailableEnergy.setMEnergy(this);

		return TAvailableEnergy;
	}

	public TAvailableEnergy removeTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		getTAvailableEnergies().remove(TAvailableEnergy);
		TAvailableEnergy.setMEnergy(null);

		return TAvailableEnergy;
	}

}