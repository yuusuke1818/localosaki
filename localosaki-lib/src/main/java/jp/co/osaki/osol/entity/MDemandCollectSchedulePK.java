package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_demand_collect_schedule database table.
 * 
 */
@Embeddable
public class MDemandCollectSchedulePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="demand_collect_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String demandCollectCd;

	@Column(name="batch_process_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String batchProcessCd;

	public MDemandCollectSchedulePK() {
	}
	public String getDemandCollectCd() {
		return this.demandCollectCd;
	}
	public void setDemandCollectCd(String demandCollectCd) {
		this.demandCollectCd = demandCollectCd;
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
		if (!(other instanceof MDemandCollectSchedulePK)) {
			return false;
		}
		MDemandCollectSchedulePK castOther = (MDemandCollectSchedulePK)other;
		return 
			this.demandCollectCd.equals(castOther.demandCollectCd)
			&& this.batchProcessCd.equals(castOther.batchProcessCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.demandCollectCd.hashCode();
		hash = hash * prime + this.batchProcessCd.hashCode();
		
		return hash;
	}
}