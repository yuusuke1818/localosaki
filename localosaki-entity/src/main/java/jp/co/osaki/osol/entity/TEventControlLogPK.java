package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_event_control_log database table.
 * 
 */
@Embeddable
public class TEventControlLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="record_ymdhms", unique=true, nullable=false, length=14)
	private String recordYmdhms;

	@Column(name="control_load", unique=true, nullable=false, precision=3)
	private BigDecimal controlLoad;

	public TEventControlLogPK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getRecordYmdhms() {
		return this.recordYmdhms;
	}
	public void setRecordYmdhms(String recordYmdhms) {
		this.recordYmdhms = recordYmdhms;
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
		if (!(other instanceof TEventControlLogPK)) {
			return false;
		}
		TEventControlLogPK castOther = (TEventControlLogPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.recordYmdhms.equals(castOther.recordYmdhms)
			&& this.controlLoad.equals(castOther.controlLoad);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.recordYmdhms.hashCode();
		hash = hash * prime + this.controlLoad.hashCode();
		
		return hash;
	}
}