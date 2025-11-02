package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_facility_kind database table.
 * 
 */
@Embeddable
public class MFacilityKindPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="facility_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilityId;

	@Column(name="facility_big_kind_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilityBigKindId;

	@Column(name="facility_small_kind_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilitySmallKindId;

	public MFacilityKindPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getBuildingId() {
		return this.buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Long getFacilityId() {
		return this.facilityId;
	}
	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
	}
	public Long getFacilityBigKindId() {
		return this.facilityBigKindId;
	}
	public void setFacilityBigKindId(Long facilityBigKindId) {
		this.facilityBigKindId = facilityBigKindId;
	}
	public Long getFacilitySmallKindId() {
		return this.facilitySmallKindId;
	}
	public void setFacilitySmallKindId(Long facilitySmallKindId) {
		this.facilitySmallKindId = facilitySmallKindId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MFacilityKindPK)) {
			return false;
		}
		MFacilityKindPK castOther = (MFacilityKindPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.facilityId.equals(castOther.facilityId)
			&& this.facilityBigKindId.equals(castOther.facilityBigKindId)
			&& this.facilitySmallKindId.equals(castOther.facilitySmallKindId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.facilityId.hashCode();
		hash = hash * prime + this.facilityBigKindId.hashCode();
		hash = hash * prime + this.facilitySmallKindId.hashCode();
		
		return hash;
	}
}