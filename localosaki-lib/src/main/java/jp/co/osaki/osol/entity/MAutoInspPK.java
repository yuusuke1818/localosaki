package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_auto_insp database table.
 * 
 */
@Embeddable
public class MAutoInspPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_type", unique=true, nullable=false)
	private Long meterType;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MAutoInspPK() {
	}
	public Long getMeterType() {
		return this.meterType;
	}
	public void setMeterType(Long meterType) {
		this.meterType = meterType;
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
		if (!(other instanceof MAutoInspPK)) {
			return false;
		}
		MAutoInspPK castOther = (MAutoInspPK)other;
		return 
			this.meterType.equals(castOther.meterType)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterType.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}