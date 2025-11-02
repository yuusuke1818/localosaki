package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_meter_loadlimit database table.
 * 
 */
@Embeddable
public class MMeterLoadlimitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_mng_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long meterMngId;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MMeterLoadlimitPK() {
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
		if (!(other instanceof MMeterLoadlimitPK)) {
			return false;
		}
		MMeterLoadlimitPK castOther = (MMeterLoadlimitPK)other;
		return 
			this.meterMngId.equals(castOther.meterMngId)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}