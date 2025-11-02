package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_build_unit_denominator database table.
 * 
 */
@Embeddable
public class MBuildUnitDenominatorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="unit_divide_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long unitDivideId;

	public MBuildUnitDenominatorPK() {
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
	public Long getUnitDivideId() {
		return this.unitDivideId;
	}
	public void setUnitDivideId(Long unitDivideId) {
		this.unitDivideId = unitDivideId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MBuildUnitDenominatorPK)) {
			return false;
		}
		MBuildUnitDenominatorPK castOther = (MBuildUnitDenominatorPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.unitDivideId.equals(castOther.unitDivideId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.unitDivideId.hashCode();
		
		return hash;
	}
}