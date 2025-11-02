package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_dm_day_load database table.
 * 
 */
@Embeddable
public class TDmDayLoadPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Temporal(TemporalType.DATE)
	@Column(name="measurement_date", insertable=false, updatable=false, unique=true, nullable=false)
	private java.util.Date measurementDate;

	@Column(name="crnt_min", insertable=false, updatable=false, unique=true, nullable=false, precision=2)
	private BigDecimal crntMin;

	@Column(name="control_load", unique=true, nullable=false, precision=3)
	private BigDecimal controlLoad;

	public TDmDayLoadPK() {
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
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public java.util.Date getMeasurementDate() {
		return this.measurementDate;
	}
	public void setMeasurementDate(java.util.Date measurementDate) {
		this.measurementDate = measurementDate;
	}
	public BigDecimal getCrntMin() {
		return this.crntMin;
	}
	public void setCrntMin(BigDecimal crntMin) {
		this.crntMin = crntMin;
	}
	public BigDecimal getControlLoad() {
		return this.controlLoad;
	}
	public void setControlLoad(BigDecimal controlLoad) {
		this.controlLoad = controlLoad;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmDayLoadPK)) {
			return false;
		}
		TDmDayLoadPK castOther = (TDmDayLoadPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.smId.equals(castOther.smId)
			&& this.measurementDate.equals(castOther.measurementDate)
			&& this.crntMin.equals(castOther.crntMin)
			&& this.controlLoad.equals(castOther.controlLoad);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.measurementDate.hashCode();
		hash = hash * prime + this.crntMin.hashCode();
		hash = hash * prime + this.controlLoad.hashCode();
		
		return hash;
	}
}