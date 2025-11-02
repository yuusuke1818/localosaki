package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_inspection_meter database table.
 * 
 */
@Embeddable
public class TInspectionMeterPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="meter_mng_id", unique=true, nullable=false)
	private Long meterMngId;

	@Column(name="insp_year", unique=true, nullable=false, length=4)
	private String inspYear;

	@Column(name="insp_month", unique=true, nullable=false, length=2)
	private String inspMonth;

	@Column(name="insp_month_no", unique=true, nullable=false)
	private Long inspMonthNo;

	public TInspectionMeterPK() {
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
	public String getInspYear() {
		return this.inspYear;
	}
	public void setInspYear(String inspYear) {
		this.inspYear = inspYear;
	}
	public String getInspMonth() {
		return this.inspMonth;
	}
	public void setInspMonth(String inspMonth) {
		this.inspMonth = inspMonth;
	}
	public Long getInspMonthNo() {
		return this.inspMonthNo;
	}
	public void setInspMonthNo(Long inspMonthNo) {
		this.inspMonthNo = inspMonthNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TInspectionMeterPK)) {
			return false;
		}
		TInspectionMeterPK castOther = (TInspectionMeterPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.inspYear.equals(castOther.inspYear)
			&& this.inspMonth.equals(castOther.inspMonth)
			&& this.inspMonthNo.equals(castOther.inspMonthNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.inspYear.hashCode();
		hash = hash * prime + this.inspMonth.hashCode();
		hash = hash * prime + this.inspMonthNo.hashCode();
		
		return hash;
	}
}