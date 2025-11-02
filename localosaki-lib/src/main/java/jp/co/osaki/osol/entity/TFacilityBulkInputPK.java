package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_facility_bulk_input database table.
 * 
 */
@Embeddable
public class TFacilityBulkInputPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="facility_bulk_input_id", unique=true, nullable=false)
	private Long facilityBulkInputId;

	public TFacilityBulkInputPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getFacilityBulkInputId() {
		return this.facilityBulkInputId;
	}
	public void setFacilityBulkInputId(Long facilityBulkInputId) {
		this.facilityBulkInputId = facilityBulkInputId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TFacilityBulkInputPK)) {
			return false;
		}
		TFacilityBulkInputPK castOther = (TFacilityBulkInputPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.facilityBulkInputId.equals(castOther.facilityBulkInputId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.facilityBulkInputId.hashCode();
		
		return hash;
	}
}