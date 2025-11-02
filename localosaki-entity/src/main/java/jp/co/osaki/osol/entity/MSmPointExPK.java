package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_sm_point_ex database table.
 * 
 */
@Embeddable
public class MSmPointExPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="point_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String pointNo;

	public MSmPointExPK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getPointNo() {
		return this.pointNo;
	}
	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MSmPointExPK)) {
			return false;
		}
		MSmPointExPK castOther = (MSmPointExPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.pointNo.equals(castOther.pointNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.pointNo.hashCode();
		
		return hash;
	}
}