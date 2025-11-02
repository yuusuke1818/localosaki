package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_subtype database table.
 * 
 */
@Embeddable
public class MSubtypePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="subtype_id", unique=true, nullable=false)
	private Long subtypeId;

	public MSubtypePK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getSubtypeId() {
		return this.subtypeId;
	}
	public void setSubtypeId(Long subtypeId) {
		this.subtypeId = subtypeId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MSubtypePK)) {
			return false;
		}
		MSubtypePK castOther = (MSubtypePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.subtypeId.equals(castOther.subtypeId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.subtypeId.hashCode();
		
		return hash;
	}
}