package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_region_type database table.
 * 
 */
@Entity
@Table(name="m_region_type")
@NamedQuery(name="MRegionType.findAll", query="SELECT m FROM MRegionType m")
public class MRegionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="region_type_code", unique=true, nullable=false)
	private Long regionTypeCode;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="region_type_name", nullable=false, length=500)
	private String regionTypeName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MEstimateUnit
	@OneToMany(mappedBy="MRegionType")
	private List<MEstimateUnit> MEstimateUnits;

	//bi-directional many-to-one association to MMunicipality
	@OneToMany(mappedBy="MRegionType")
	private List<MMunicipality> MMunicipalities;

	public MRegionType() {
	}

	public Long getRegionTypeCode() {
		return this.regionTypeCode;
	}

	public void setRegionTypeCode(Long regionTypeCode) {
		this.regionTypeCode = regionTypeCode;
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

	public String getRegionTypeName() {
		return this.regionTypeName;
	}

	public void setRegionTypeName(String regionTypeName) {
		this.regionTypeName = regionTypeName;
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

	public List<MEstimateUnit> getMEstimateUnits() {
		return this.MEstimateUnits;
	}

	public void setMEstimateUnits(List<MEstimateUnit> MEstimateUnits) {
		this.MEstimateUnits = MEstimateUnits;
	}

	public MEstimateUnit addMEstimateUnit(MEstimateUnit MEstimateUnit) {
		getMEstimateUnits().add(MEstimateUnit);
		MEstimateUnit.setMRegionType(this);

		return MEstimateUnit;
	}

	public MEstimateUnit removeMEstimateUnit(MEstimateUnit MEstimateUnit) {
		getMEstimateUnits().remove(MEstimateUnit);
		MEstimateUnit.setMRegionType(null);

		return MEstimateUnit;
	}

	public List<MMunicipality> getMMunicipalities() {
		return this.MMunicipalities;
	}

	public void setMMunicipalities(List<MMunicipality> MMunicipalities) {
		this.MMunicipalities = MMunicipalities;
	}

	public MMunicipality addMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().add(MMunicipality);
		MMunicipality.setMRegionType(this);

		return MMunicipality;
	}

	public MMunicipality removeMMunicipality(MMunicipality MMunicipality) {
		getMMunicipalities().remove(MMunicipality);
		MMunicipality.setMRegionType(null);

		return MMunicipality;
	}

}