package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_estimate_unit database table.
 * 
 */
@Embeddable
public class MEstimateUnitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="estimate_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long estimateId;

	@Column(name="region_type_code", insertable=false, updatable=false, unique=true, nullable=false)
	private Long regionTypeCode;

	@Column(unique=true, nullable=false, length=4)
	private String year;

	public MEstimateUnitPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getEstimateId() {
		return this.estimateId;
	}
	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}
	public Long getRegionTypeCode() {
		return this.regionTypeCode;
	}
	public void setRegionTypeCode(Long regionTypeCode) {
		this.regionTypeCode = regionTypeCode;
	}
	public String getYear() {
		return this.year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MEstimateUnitPK)) {
			return false;
		}
		MEstimateUnitPK castOther = (MEstimateUnitPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.estimateId.equals(castOther.estimateId)
			&& this.regionTypeCode.equals(castOther.regionTypeCode)
			&& this.year.equals(castOther.year);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.estimateId.hashCode();
		hash = hash * prime + this.regionTypeCode.hashCode();
		hash = hash * prime + this.year.hashCode();
		
		return hash;
	}
}