package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_meter_group database table.
 * 
 */
@Embeddable
public class MMeterGroupPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long meterGroupId;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="meter_mng_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long meterMngId;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MMeterGroupPK() {
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
	public Long getMeterMngId() {
		return this.meterMngId;
	}
	public void setMeterMngId(Long meterMngId) {
		this.meterMngId = meterMngId;
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MMeterGroupPK)) {
			return false;
		}
		MMeterGroupPK castOther = (MMeterGroupPK)other;
		return 
			this.meterGroupId.equals(castOther.meterGroupId)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterGroupId.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}