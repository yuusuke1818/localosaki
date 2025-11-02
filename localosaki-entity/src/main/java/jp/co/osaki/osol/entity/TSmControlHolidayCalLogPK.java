package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_control_holiday_cal_log database table.
 * 
 */
@Embeddable
public class TSmControlHolidayCalLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_control_holiday_log_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smControlHolidayLogId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="holiday_mmdd", unique=true, nullable=false, length=4)
	private String holidayMmdd;

	public TSmControlHolidayCalLogPK() {
	}
	public Long getSmControlHolidayLogId() {
		return this.smControlHolidayLogId;
	}
	public void setSmControlHolidayLogId(Long smControlHolidayLogId) {
		this.smControlHolidayLogId = smControlHolidayLogId;
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getHolidayMmdd() {
		return this.holidayMmdd;
	}
	public void setHolidayMmdd(String holidayMmdd) {
		this.holidayMmdd = holidayMmdd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TSmControlHolidayCalLogPK)) {
			return false;
		}
		TSmControlHolidayCalLogPK castOther = (TSmControlHolidayCalLogPK)other;
		return 
			this.smControlHolidayLogId.equals(castOther.smControlHolidayLogId)
			&& this.smId.equals(castOther.smId)
			&& this.holidayMmdd.equals(castOther.holidayMmdd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smControlHolidayLogId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.holidayMmdd.hashCode();
		
		return hash;
	}
}