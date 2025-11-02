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
 * The persistent class for the m_facility_unit database table.
 * 
 */
@Entity
@Table(name = "m_facility_unit")
@NamedQueries({
    @NamedQuery(name = "MFacilityUnit.findAll", query = "SELECT m FROM MFacilityUnit m"),
    @NamedQuery(name = "MFacilityUnit.findAllSortDisplayOrder", query = "SELECT m FROM MFacilityUnit m ORDER BY m.displayOrder ASC")
})
public class MFacilityUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="facility_unit_id", unique=true, nullable=false)
	private Long facilityUnitId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="facility_unit", nullable=false, length=100)
	private String facilityUnit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MFacility
	@OneToMany(mappedBy="MFacilityUnit1")
	private List<MFacility> MFacilities1;

	//bi-directional many-to-one association to MFacility
	@OneToMany(mappedBy="MFacilityUnit2")
	private List<MFacility> MFacilities2;

	public MFacilityUnit() {
	}

	public Long getFacilityUnitId() {
		return this.facilityUnitId;
	}

	public void setFacilityUnitId(Long facilityUnitId) {
		this.facilityUnitId = facilityUnitId;
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

	public String getFacilityUnit() {
		return this.facilityUnit;
	}

	public void setFacilityUnit(String facilityUnit) {
		this.facilityUnit = facilityUnit;
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

	public List<MFacility> getMFacilities1() {
		return this.MFacilities1;
	}

	public void setMFacilities1(List<MFacility> MFacilities1) {
		this.MFacilities1 = MFacilities1;
	}

	public MFacility addMFacilities1(MFacility MFacilities1) {
		getMFacilities1().add(MFacilities1);
		MFacilities1.setMFacilityUnit1(this);

		return MFacilities1;
	}

	public MFacility removeMFacilities1(MFacility MFacilities1) {
		getMFacilities1().remove(MFacilities1);
		MFacilities1.setMFacilityUnit1(null);

		return MFacilities1;
	}

	public List<MFacility> getMFacilities2() {
		return this.MFacilities2;
	}

	public void setMFacilities2(List<MFacility> MFacilities2) {
		this.MFacilities2 = MFacilities2;
	}

	public MFacility addMFacilities2(MFacility MFacilities2) {
		getMFacilities2().add(MFacilities2);
		MFacilities2.setMFacilityUnit2(this);

		return MFacilities2;
	}

	public MFacility removeMFacilities2(MFacility MFacilities2) {
		getMFacilities2().remove(MFacilities2);
		MFacilities2.setMFacilityUnit2(null);

		return MFacilities2;
	}

}