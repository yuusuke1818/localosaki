package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_dm_day_rep_point database table.
 * 
 */
@Embeddable
public class TDmDayRepPointPK implements Serializable {
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

	@Column(name="jigen_no", insertable=false, updatable=false, unique=true, nullable=false, precision=2)
	private BigDecimal jigenNo;

	@Column(name="point_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String pointNo;

	public TDmDayRepPointPK() {
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
	public BigDecimal getJigenNo() {
		return this.jigenNo;
	}
	public void setJigenNo(BigDecimal jigenNo) {
		this.jigenNo = jigenNo;
	}
	public String getPointNo() {
		return this.pointNo;
	}
	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmDayRepPointPK)) {
			return false;
		}
		TDmDayRepPointPK castOther = (TDmDayRepPointPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.smId.equals(castOther.smId)
			&& this.measurementDate.equals(castOther.measurementDate)
			&& this.jigenNo.equals(castOther.jigenNo)
			&& this.pointNo.equals(castOther.pointNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.measurementDate.hashCode();
		hash = hash * prime + this.jigenNo.hashCode();
		hash = hash * prime + this.pointNo.hashCode();
		
		return hash;
	}
}