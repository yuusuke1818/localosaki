package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_repeater database table.
 * 
 */
@Embeddable
public class MRepeaterPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="repeater_mng_id", unique=true, nullable=false)
	private Long repeaterMngId;

	@Column(name="dev_id", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String devId;

	public MRepeaterPK() {
	}
	public Long getRepeaterMngId() {
		return this.repeaterMngId;
	}
	public void setRepeaterMngId(Long repeaterMngId) {
		this.repeaterMngId = repeaterMngId;
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
		if (!(other instanceof MRepeaterPK)) {
			return false;
		}
		MRepeaterPK castOther = (MRepeaterPK)other;
		return 
			this.repeaterMngId.equals(castOther.repeaterMngId)
			&& this.devId.equals(castOther.devId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.repeaterMngId.hashCode();
		hash = hash * prime + this.devId.hashCode();
		
		return hash;
	}
}