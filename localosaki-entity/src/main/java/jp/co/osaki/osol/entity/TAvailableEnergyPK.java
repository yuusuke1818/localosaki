package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_available_energy database table.
 * 
 */
@Embeddable
public class TAvailableEnergyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="eng_type_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String engTypeCd;

	@Column(name="eng_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long engId;

	@Column(name="contract_id", unique=true, nullable=false)
	private Long contractId;

	public TAvailableEnergyPK() {
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
	public String getEngTypeCd() {
		return this.engTypeCd;
	}
	public void setEngTypeCd(String engTypeCd) {
		this.engTypeCd = engTypeCd;
	}
	public Long getEngId() {
		return this.engId;
	}
	public void setEngId(Long engId) {
		this.engId = engId;
	}
	public Long getContractId() {
		return this.contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TAvailableEnergyPK)) {
			return false;
		}
		TAvailableEnergyPK castOther = (TAvailableEnergyPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.engTypeCd.equals(castOther.engTypeCd)
			&& this.engId.equals(castOther.engId)
			&& this.contractId.equals(castOther.contractId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.engTypeCd.hashCode();
		hash = hash * prime + this.engId.hashCode();
		hash = hash * prime + this.contractId.hashCode();
		
		return hash;
	}
}