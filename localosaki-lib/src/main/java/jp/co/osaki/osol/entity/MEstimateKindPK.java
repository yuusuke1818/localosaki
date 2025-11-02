package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_estimate_kind database table.
 * 
 */
@Embeddable
public class MEstimateKindPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="estimate_id", unique=true, nullable=false)
	private Long estimateId;

	public MEstimateKindPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getEstimateId() {
		return this.estimateId;
	}
	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MEstimateKindPK)) {
			return false;
		}
		MEstimateKindPK castOther = (MEstimateKindPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.estimateId.equals(castOther.estimateId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.estimateId.hashCode();
		
		return hash;
	}
}