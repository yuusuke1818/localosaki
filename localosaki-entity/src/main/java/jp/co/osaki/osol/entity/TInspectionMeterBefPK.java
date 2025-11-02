package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_inspection_meter_bef database table.
 * 
 */
@Embeddable
public class TInspectionMeterBefPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="meter_mng_id", unique=true, nullable=false)
	private Long meterMngId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="latest_insp_date", unique=true, nullable=false)
	private java.util.Date latestInspDate;

	public TInspectionMeterBefPK() {
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
	public java.util.Date getLatestInspDate() {
		return this.latestInspDate;
	}
	public void setLatestInspDate(java.util.Date latestInspDate) {
		this.latestInspDate = latestInspDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TInspectionMeterBefPK)) {
			return false;
		}
		TInspectionMeterBefPK castOther = (TInspectionMeterBefPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.latestInspDate.equals(castOther.latestInspDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.latestInspDate.hashCode();
		
		return hash;
	}
}