package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_municipality database table.
 * 
 */
@Entity
@Table(name="m_municipality")
@NamedQuery(name="MMunicipality.findAll", query="SELECT m FROM MMunicipality m")
public class MMunicipality implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="municipality_cd", unique=true, nullable=false, length=5)
	private String municipalityCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="municipality_name", nullable=false, length=100)
	private String municipalityName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@OneToMany(mappedBy="MMunicipality")
	private List<MCorp> MCorps;

	//bi-directional many-to-one association to MMunicipality
	@ManyToOne
	@JoinColumn(name="parent_prefecture_cd")
	private MMunicipality MMunicipality;

	//bi-directional many-to-one association to MMunicipality
	@OneToMany(mappedBy="MMunicipality")
	private List<MMunicipality> MMunicipalities;

	//bi-directional many-to-one association to MPrefecture
	@ManyToOne
	@JoinColumn(name="prefecture_cd", nullable=false)
	private MPrefecture MPrefecture;

	//bi-directional many-to-one association to MRegionType
	@ManyToOne
	@JoinColumn(name="region_type_code", nullable=false)
	private MRegionType MRegionType;

	//bi-directional many-to-one association to TBuilding
	@OneToMany(mappedBy="MMunicipality")
	private List<TBuilding> TBuildings;

	public MMunicipality() {
	}

	public String getMunicipalityCd() {
		return this.municipalityCd;
	}

	public void setMunicipalityCd(String municipalityCd) {
		this.municipalityCd = municipalityCd;
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

	public String getMunicipalityName() {
		return this.municipalityName;
	}

	public void setMunicipalityName(String municipalityName) {
		this.municipalityName = municipalityName;
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

	public List<MCorp> getMCorps() {
		return this.MCorps;
	}

	public void setMCorps(List<MCorp> MCorps) {
		this.MCorps = MCorps;
	}

	public MCorp addMCorp(MCorp MCorp) {
		getMCorps().add(MCorp);
		MCorp.setMMunicipality(this);

		return MCorp;
	}

	public MCorp removeMCorp(MCorp MCorp) {
		getMCorps().remove(MCorp);
		MCorp.setMMunicipality(null);

		return MCorp;
	}

	public MMunicipality getMMunicipality() {
		return this.MMunicipality;
	}

	public void setMMunicipality(MMunicipality MMunicipality) {
		this.MMunicipality = MMunicipality;
	}

	public List<MMunicipality> getMMunicipalities() {
		return this.MMunicipalities;
	}

	public void setMMunicipalities(List<MMunicipality> MMunicipalities) {
		this.MMunicipalities = MMunicipalities;
	}

	public MMunicipality addMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().add(MMunicipality);
		MMunicipality.setMMunicipality(this);

		return MMunicipality;
	}

	public MMunicipality removeMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().remove(MMunicipality);
		MMunicipality.setMMunicipality(null);

		return MMunicipality;
	}

	public MPrefecture getMPrefecture() {
		return this.MPrefecture;
	}

	public void setMPrefecture(MPrefecture MPrefecture) {
		this.MPrefecture = MPrefecture;
	}

	public MRegionType getMRegionType() {
		return this.MRegionType;
	}

	public void setMRegionType(MRegionType MRegionType) {
		this.MRegionType = MRegionType;
	}

	public List<TBuilding> getTBuildings() {
		return this.TBuildings;
	}

	public void setTBuildings(List<TBuilding> TBuildings) {
		this.TBuildings = TBuildings;
	}

	public TBuilding addTBuilding(TBuilding TBuilding) {
		getTBuildings().add(TBuilding);
		TBuilding.setMMunicipality(this);

		return TBuilding;
	}

	public TBuilding removeTBuilding(TBuilding TBuilding) {
		getTBuildings().remove(TBuilding);
		TBuilding.setMMunicipality(null);

		return TBuilding;
	}

}