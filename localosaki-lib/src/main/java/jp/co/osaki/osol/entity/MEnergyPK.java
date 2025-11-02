package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_energy database table.
 * 
 */
@Embeddable
public class MEnergyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="eng_type_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String engTypeCd;

	@Column(name="eng_id", unique=true, nullable=false)
	private Long engId;

	public MEnergyPK() {
	}
	public String getEngTypeCd() {
		return this.engTypeCd;
	}
	public void setEngTypeCd(String engTypeCd) {
		this.engTypeCd = engTypeCd;
	}
	public Long getEngId() {
		return this.engId;
	}
	public void setEngId(Long engId) {
		this.engId = engId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MEnergyPK)) {
			return false;
		}
		MEnergyPK castOther = (MEnergyPK)other;
		return 
			this.engTypeCd.equals(castOther.engTypeCd)
			&& this.engId.equals(castOther.engId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.engTypeCd.hashCode();
		hash = hash * prime + this.engId.hashCode();
		
		return hash;
	}
}