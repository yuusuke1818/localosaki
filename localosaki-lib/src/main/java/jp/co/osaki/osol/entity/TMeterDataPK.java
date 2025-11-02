package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_meter_data database table.
 * 
 */
@Embeddable
public class TMeterDataPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="meter_mng_id", unique=true, nullable=false)
	private Long meterMngId;

	public TMeterDataPK() {
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public Long getMeterMngId() {
		return this.meterMngId;
	}
	public void setMeterMngId(Long meterMngId) {
		this.meterMngId = meterMngId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TMeterDataPK)) {
			return false;
		}
		TMeterDataPK castOther = (TMeterDataPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.meterMngId.equals(castOther.meterMngId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		
		return hash;
	}
}