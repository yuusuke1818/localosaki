package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_dm_day_rep database table.
 * 
 */
@Embeddable
public class TDmDayRepPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Temporal(TemporalType.DATE)
	@Column(name="measurement_date", unique=true, nullable=false)
	private java.util.Date measurementDate;

	@Column(name="jigen_no", unique=true, nullable=false, precision=2)
	private BigDecimal jigenNo;

	public TDmDayRepPK() {
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
	public BigDecimal getJigenNo() {
		return this.jigenNo;
	}
	public void setJigenNo(BigDecimal jigenNo) {
		this.jigenNo = jigenNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmDayRepPK)) {
			return false;
		}
		TDmDayRepPK castOther = (TDmDayRepPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.measurementDate.equals(castOther.measurementDate)
			&& this.jigenNo.equals(castOther.jigenNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.measurementDate.hashCode();
		hash = hash * prime + this.jigenNo.hashCode();
		
		return hash;
	}
}