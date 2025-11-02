package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_line_group_ex database table.
 * 
 */
@Embeddable
public class MLineGroupExPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="line_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long lineGroupId;

	public MLineGroupExPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getLineGroupId() {
		return this.lineGroupId;
	}
	public void setLineGroupId(Long lineGroupId) {
		this.lineGroupId = lineGroupId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MLineGroupExPK)) {
			return false;
		}
		MLineGroupExPK castOther = (MLineGroupExPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.lineGroupId.equals(castOther.lineGroupId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.lineGroupId.hashCode();
		
		return hash;
	}
}