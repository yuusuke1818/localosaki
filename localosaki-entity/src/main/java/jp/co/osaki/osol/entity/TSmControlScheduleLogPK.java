package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_control_schedule_log database table.
 * 
 */
@Embeddable
public class TSmControlScheduleLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_control_schedule_log_id", unique=true, nullable=false)
	private Long smControlScheduleLogId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	public TSmControlScheduleLogPK() {
	}
	public Long getSmControlScheduleLogId() {
		return this.smControlScheduleLogId;
	}
	public void setSmControlScheduleLogId(Long smControlScheduleLogId) {
		this.smControlScheduleLogId = smControlScheduleLogId;
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
		if (!(other instanceof TSmControlScheduleLogPK)) {
			return false;
		}
		TSmControlScheduleLogPK castOther = (TSmControlScheduleLogPK)other;
		return 
			this.smControlScheduleLogId.equals(castOther.smControlScheduleLogId)
			&& this.smId.equals(castOther.smId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smControlScheduleLogId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		
		return hash;
	}
}