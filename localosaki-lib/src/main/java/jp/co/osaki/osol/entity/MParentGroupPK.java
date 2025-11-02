package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_parent_group database table.
 * 
 */
@Embeddable
public class MParentGroupPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="parent_group_id", unique=true, nullable=false)
	private Long parentGroupId;

	public MParentGroupPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getParentGroupId() {
		return this.parentGroupId;
	}
	public void setParentGroupId(Long parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MParentGroupPK)) {
			return false;
		}
		MParentGroupPK castOther = (MParentGroupPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.parentGroupId.equals(castOther.parentGroupId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.parentGroupId.hashCode();
		
		return hash;
	}
}