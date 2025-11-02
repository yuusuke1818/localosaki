package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_concentrator database table.
 * 
 */
@Embeddable
public class MConcentratorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="concent_id", unique=true, nullable=false)
	private Long concentId;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MConcentratorPK() {
	}
	public Long getConcentId() {
		return this.concentId;
	}
	public void setConcentId(Long concentId) {
		this.concentId = concentId;
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
		if (!(other instanceof MConcentratorPK)) {
			return false;
		}
		MConcentratorPK castOther = (MConcentratorPK)other;
		return 
			this.concentId.equals(castOther.concentId)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.concentId.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}