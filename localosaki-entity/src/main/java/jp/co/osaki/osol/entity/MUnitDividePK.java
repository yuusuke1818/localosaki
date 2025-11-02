package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_unit_divide database table.
 * 
 */
@Embeddable
public class MUnitDividePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="unit_divide_id", unique=true, nullable=false)
	private Long unitDivideId;

	public MUnitDividePK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getUnitDivideId() {
		return this.unitDivideId;
	}
	public void setUnitDivideId(Long unitDivideId) {
		this.unitDivideId = unitDivideId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MUnitDividePK)) {
			return false;
		}
		MUnitDividePK castOther = (MUnitDividePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.unitDivideId.equals(castOther.unitDivideId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.unitDivideId.hashCode();
		
		return hash;
	}
}