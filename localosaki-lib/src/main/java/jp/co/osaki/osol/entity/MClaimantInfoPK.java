package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_claimant_info database table.
 * 
 */
@Embeddable
public class MClaimantInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="person_corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personCorpId;

	@Column(name="person_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String personId;

	public MClaimantInfoPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getBuildingId() {
		return this.buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MClaimantInfoPK)) {
			return false;
		}
		MClaimantInfoPK castOther = (MClaimantInfoPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.personCorpId.equals(castOther.personCorpId)
			&& this.personId.equals(castOther.personId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.personCorpId.hashCode();
		hash = hash * prime + this.personId.hashCode();
		
		return hash;
	}
}