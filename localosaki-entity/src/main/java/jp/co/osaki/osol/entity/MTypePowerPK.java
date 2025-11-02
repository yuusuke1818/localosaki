package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the m_type_power database table.
 * 
 */
@Embeddable
public class MTypePowerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_type", insertable=false, updatable=false, unique=true, nullable=false)
	private Long meterType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="basic_date", insertable=false, updatable=false, unique=true, nullable=false)
	private java.util.Date basicDate;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MTypePowerPK() {
	}
	public Long getMeterType() {
		return this.meterType;
	}
	public void setMeterType(Long meterType) {
		this.meterType = meterType;
	}
	public java.util.Date getBasicDate() {
		return this.basicDate;
	}
	public void setBasicDate(java.util.Date basicDate) {
		this.basicDate = basicDate;
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
		if (!(other instanceof MTypePowerPK)) {
			return false;
		}
		MTypePowerPK castOther = (MTypePowerPK)other;
		return 
			this.meterType.equals(castOther.meterType)
			&& this.basicDate.equals(castOther.basicDate)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterType.hashCode();
		hash = hash * prime + this.basicDate.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}