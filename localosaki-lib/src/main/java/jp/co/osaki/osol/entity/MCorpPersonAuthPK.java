package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_corp_person_auth database table.
 * 
 */
@Embeddable
public class MCorpPersonAuthPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="person_corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personCorpId;

	@Column(name="person_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personId;

	@Column(name="authority_cd", insertable=false, updatable=false, unique=true, nullable=false, length=5)
	private String authorityCd;

	public MCorpPersonAuthPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getPersonCorpId() {
		return this.personCorpId;
	}
	public void setPersonCorpId(String personCorpId) {
		this.personCorpId = personCorpId;
	}
	public String getPersonId() {
		return this.personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getAuthorityCd() {
		return this.authorityCd;
	}
	public void setAuthorityCd(String authorityCd) {
		this.authorityCd = authorityCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCorpPersonAuthPK)) {
			return false;
		}
		MCorpPersonAuthPK castOther = (MCorpPersonAuthPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.personCorpId.equals(castOther.personCorpId)
			&& this.personId.equals(castOther.personId)
			&& this.authorityCd.equals(castOther.authorityCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.personCorpId.hashCode();
		hash = hash * prime + this.personId.hashCode();
		hash = hash * prime + this.authorityCd.hashCode();
		
		return hash;
	}
}