package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_meter_group_name database table.
 * 
 */
@Embeddable
public class MMeterGroupNamePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_group_id", unique=true, nullable=false)
	private Long meterGroupId;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MMeterGroupNamePK() {
	}
	public Long getMeterGroupId() {
		return this.meterGroupId;
	}
	public void setMeterGroupId(Long meterGroupId) {
		this.meterGroupId = meterGroupId;
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MMeterGroupNamePK)) {
			return false;
		}
		MMeterGroupNamePK castOther = (MMeterGroupNamePK)other;
		return 
			this.meterGroupId.equals(castOther.meterGroupId)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterGroupId.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}