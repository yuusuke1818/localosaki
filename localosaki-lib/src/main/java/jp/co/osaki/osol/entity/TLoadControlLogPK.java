package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_load_control_log database table.
 * 
 */
@Embeddable
public class TLoadControlLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="record_ymdhm", unique=true, nullable=false, length=12)
	private String recordYmdhm;

	@Column(name="rest_ms", unique=true, nullable=false, length=4)
	private String restMs;

	@Column(name="control_status", unique=true, nullable=false, length=24)
	private String controlStatus;

	public TLoadControlLogPK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getRecordYmdhm() {
		return this.recordYmdhm;
	}
	public void setRecordYmdhm(String recordYmdhm) {
		this.recordYmdhm = recordYmdhm;
	}
	public String getRestMs() {
		return this.restMs;
	}
	public void setRestMs(String restMs) {
		this.restMs = restMs;
	}
	public String getControlStatus() {
		return this.controlStatus;
	}
	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TLoadControlLogPK)) {
			return false;
		}
		TLoadControlLogPK castOther = (TLoadControlLogPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.recordYmdhm.equals(castOther.recordYmdhm)
			&& this.restMs.equals(castOther.restMs)
			&& this.controlStatus.equals(castOther.controlStatus);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.recordYmdhm.hashCode();
		hash = hash * prime + this.restMs.hashCode();
		hash = hash * prime + this.controlStatus.hashCode();
		
		return hash;
	}
}