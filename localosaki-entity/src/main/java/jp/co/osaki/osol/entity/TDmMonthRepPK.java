package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_dm_month_rep database table.
 * 
 */
@Embeddable
public class TDmMonthRepPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Temporal(TemporalType.DATE)
	@Column(name="measurement_date", unique=true, nullable=false)
	private java.util.Date measurementDate;

	public TDmMonthRepPK() {
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
	public java.util.Date getMeasurementDate() {
		return this.measurementDate;
	}
	public void setMeasurementDate(java.util.Date measurementDate) {
		this.measurementDate = measurementDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmMonthRepPK)) {
			return false;
		}
		TDmMonthRepPK castOther = (TDmMonthRepPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.measurementDate.equals(castOther.measurementDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.measurementDate.hashCode();
		
		return hash;
	}
}