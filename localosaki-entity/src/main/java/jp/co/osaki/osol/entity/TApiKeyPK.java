package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_api_key database table.
 * 
 */
@Embeddable
public class TApiKeyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="person_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personId;

	public TApiKeyPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getPersonId() {
		return this.personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TApiKeyPK)) {
			return false;
		}
		TApiKeyPK castOther = (TApiKeyPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.personId.equals(castOther.personId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.personId.hashCode();
		
		return hash;
	}
}