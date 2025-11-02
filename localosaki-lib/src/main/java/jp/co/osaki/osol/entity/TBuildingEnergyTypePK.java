package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_building_energy_type database table.
 * 
 */
@Embeddable
public class TBuildingEnergyTypePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", unique=true, nullable=false)
	private Long buildingId;

	@Column(name="eng_type_cd", unique=true, nullable=false, length=3)
	private String engTypeCd;

	public TBuildingEnergyTypePK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TBuildingEnergyTypePK)) {
			return false;
		}
		TBuildingEnergyTypePK castOther = (TBuildingEnergyTypePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.engTypeCd.equals(castOther.engTypeCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.engTypeCd.hashCode();
		
		return hash;
	}
}