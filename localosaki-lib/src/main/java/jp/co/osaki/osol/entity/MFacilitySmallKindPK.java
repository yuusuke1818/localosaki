package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_facility_small_kind database table.
 * 
 */
@Embeddable
public class MFacilitySmallKindPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="facility_big_kind_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long facilityBigKindId;

	@Column(name="facility_small_kind_id", unique=true, nullable=false)
	private Long facilitySmallKindId;

	public MFacilitySmallKindPK() {
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
		if (!(other instanceof MFacilitySmallKindPK)) {
			return false;
		}
		MFacilitySmallKindPK castOther = (MFacilitySmallKindPK)other;
		return 
			this.facilityBigKindId.equals(castOther.facilityBigKindId)
			&& this.facilitySmallKindId.equals(castOther.facilitySmallKindId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.facilityBigKindId.hashCode();
		hash = hash * prime + this.facilitySmallKindId.hashCode();
		
		return hash;
	}
}