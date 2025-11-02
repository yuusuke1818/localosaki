package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_sm_collect_manage database table.
 * 
 */
@Embeddable
public class MSmCollectManagePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="batch_process_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String batchProcessCd;

	public MSmCollectManagePK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getBatchProcessCd() {
		return this.batchProcessCd;
	}
	public void setBatchProcessCd(String batchProcessCd) {
		this.batchProcessCd = batchProcessCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MSmCollectManagePK)) {
			return false;
		}
		MSmCollectManagePK castOther = (MSmCollectManagePK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.batchProcessCd.equals(castOther.batchProcessCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.batchProcessCd.hashCode();
		
		return hash;
	}
}