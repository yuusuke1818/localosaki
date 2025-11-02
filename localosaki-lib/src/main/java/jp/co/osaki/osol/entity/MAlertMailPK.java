package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_alert_mail database table.
 * 
 */
@Embeddable
public class MAlertMailPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="alert_id", unique=true, nullable=false)
	private Long alertId;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MAlertMailPK() {
	}
	public Long getAlertId() {
		return this.alertId;
	}
	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MAlertMailPK)) {
			return false;
		}
		MAlertMailPK castOther = (MAlertMailPK)other;
		return 
			this.alertId.equals(castOther.alertId)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.alertId.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}