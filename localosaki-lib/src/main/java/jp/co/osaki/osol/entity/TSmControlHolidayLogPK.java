package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_control_holiday_log database table.
 * 
 */
@Embeddable
public class TSmControlHolidayLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_control_holiday_log_id", unique=true, nullable=false)
	private Long smControlHolidayLogId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	public TSmControlHolidayLogPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TSmControlHolidayLogPK)) {
			return false;
		}
		TSmControlHolidayLogPK castOther = (TSmControlHolidayLogPK)other;
		return 
			this.smControlHolidayLogId.equals(castOther.smControlHolidayLogId)
			&& this.smId.equals(castOther.smId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smControlHolidayLogId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		
		return hash;
	}
}