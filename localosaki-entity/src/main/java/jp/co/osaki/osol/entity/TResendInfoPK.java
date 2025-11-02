package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_resend_info database table.
 * 
 */
@Embeddable
public class TResendInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="building_id", unique=true, nullable=false)
	private Long buildingId;

	@Column(name="resend_datetime", unique=true, nullable=false, length=12)
	private String resendDatetime;

	public TResendInfoPK() {
	}
	public Long getBuildingId() {
		return this.buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getResendDatetime() {
		return this.resendDatetime;
	}
	public void setResendDatetime(String resendDatetime) {
		this.resendDatetime = resendDatetime;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TResendInfoPK)) {
			return false;
		}
		TResendInfoPK castOther = (TResendInfoPK)other;
		return 
			this.buildingId.equals(castOther.buildingId)
			&& this.resendDatetime.equals(castOther.resendDatetime);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.resendDatetime.hashCode();
		
		return hash;
	}
}