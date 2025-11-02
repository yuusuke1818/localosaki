package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_control_schedule_duty_log database table.
 * 
 */
@Embeddable
public class TSmControlScheduleDutyLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_control_schedule_log_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smControlScheduleLogId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="pattern_no", unique=true, nullable=false, length=2)
	private String patternNo;

	public TSmControlScheduleDutyLogPK() {
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
	public String getPatternNo() {
		return this.patternNo;
	}
	public void setPatternNo(String patternNo) {
		this.patternNo = patternNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TSmControlScheduleDutyLogPK)) {
			return false;
		}
		TSmControlScheduleDutyLogPK castOther = (TSmControlScheduleDutyLogPK)other;
		return 
			this.smControlScheduleLogId.equals(castOther.smControlScheduleLogId)
			&& this.smId.equals(castOther.smId)
			&& this.patternNo.equals(castOther.patternNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smControlScheduleLogId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.patternNo.hashCode();
		
		return hash;
	}
}