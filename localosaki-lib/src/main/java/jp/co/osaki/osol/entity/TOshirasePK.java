package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_oshirase database table.
 * 
 */
@Embeddable
public class TOshirasePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="oshirase_id", unique=true, nullable=false)
	private Long oshiraseId;

	public TOshirasePK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getOshiraseId() {
		return this.oshiraseId;
	}
	public void setOshiraseId(Long oshiraseId) {
		this.oshiraseId = oshiraseId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TOshirasePK)) {
			return false;
		}
		TOshirasePK castOther = (TOshirasePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.oshiraseId.equals(castOther.oshiraseId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.oshiraseId.hashCode();
		
		return hash;
	}
}