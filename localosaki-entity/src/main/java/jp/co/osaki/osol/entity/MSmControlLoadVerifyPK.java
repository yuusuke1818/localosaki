package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_sm_control_load_verify database table.
 * 
 */
@Embeddable
public class MSmControlLoadVerifyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="control_load", insertable=false, updatable=false, unique=true, nullable=false, precision=3)
	private BigDecimal controlLoad;

	public MSmControlLoadVerifyPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MSmControlLoadVerifyPK)) {
			return false;
		}
		MSmControlLoadVerifyPK castOther = (MSmControlLoadVerifyPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.controlLoad.equals(castOther.controlLoad);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.controlLoad.hashCode();
		
		return hash;
	}
}