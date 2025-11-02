package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_hopid_history database table.
 * 
 */
@Embeddable
public class THopidHistoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(name="wireless_id", unique=true, nullable=false, length=12)
	private String wirelessId;

	public THopidHistoryPK() {
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getWirelessId() {
		return this.wirelessId;
	}
	public void setWirelessId(String wirelessId) {
		this.wirelessId = wirelessId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof THopidHistoryPK)) {
			return false;
		}
		THopidHistoryPK castOther = (THopidHistoryPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.wirelessId.equals(castOther.wirelessId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.wirelessId.hashCode();
		
		return hash;
	}
}