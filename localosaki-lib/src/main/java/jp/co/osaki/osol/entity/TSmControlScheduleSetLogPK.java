package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_control_schedule_set_log database table.
 *
 */
@Embeddable
public class TSmControlScheduleSetLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_control_schedule_log_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smControlScheduleLogId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="control_load", unique=true, nullable=false, precision=3)
	private BigDecimal controlLoad;

	@Column(name="target_month", unique=true, nullable=false, precision=2)
	private BigDecimal targetMonth;

	public TSmControlScheduleSetLogPK() {
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
	public BigDecimal getControlLoad() {
		return this.controlLoad;
	}
	public void setControlLoad(BigDecimal controlLoad) {
		this.controlLoad = controlLoad;
	}
	public BigDecimal getTargetMonth() {
		return this.targetMonth;
	}
	public void setTargetMonth(BigDecimal targetMonth) {
		this.targetMonth = targetMonth;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TSmControlScheduleSetLogPK)) {
			return false;
		}
		TSmControlScheduleSetLogPK castOther = (TSmControlScheduleSetLogPK)other;
		return
			this.smControlScheduleLogId.equals(castOther.smControlScheduleLogId)
			&& this.smId.equals(castOther.smId)
			&& this.controlLoad.equals(castOther.controlLoad)
			&& this.targetMonth.equals(castOther.targetMonth);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smControlScheduleLogId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.controlLoad.hashCode();
		hash = hash * prime + this.targetMonth.hashCode();

		return hash;
	}
}