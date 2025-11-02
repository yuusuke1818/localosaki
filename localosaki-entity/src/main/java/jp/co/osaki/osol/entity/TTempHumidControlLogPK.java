package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_temp_humid_control_log database table.
 * 
 */
@Embeddable
public class TTempHumidControlLogPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="record_ymdhms", unique=true, nullable=false, length=14)
	private String recordYmdhms;

	@Column(name="port_out_status", unique=true, nullable=false, length=4)
	private String portOutStatus;

	public TTempHumidControlLogPK() {
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
	public String getPortOutStatus() {
		return this.portOutStatus;
	}
	public void setPortOutStatus(String portOutStatus) {
		this.portOutStatus = portOutStatus;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TTempHumidControlLogPK)) {
			return false;
		}
		TTempHumidControlLogPK castOther = (TTempHumidControlLogPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.recordYmdhms.equals(castOther.recordYmdhms)
			&& this.portOutStatus.equals(castOther.portOutStatus);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.recordYmdhms.hashCode();
		hash = hash * prime + this.portOutStatus.hashCode();
		
		return hash;
	}
}