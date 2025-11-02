package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_energy_use_target_monthly database table.
 * 
 */
@Embeddable
public class MEnergyUseTargetMonthlyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="eng_type_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String engTypeCd;

	@Column(unique=true, nullable=false, length=6)
	private String ym;

	public MEnergyUseTargetMonthlyPK() {
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
	public String getYm() {
		return this.ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MEnergyUseTargetMonthlyPK)) {
			return false;
		}
		MEnergyUseTargetMonthlyPK castOther = (MEnergyUseTargetMonthlyPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.engTypeCd.equals(castOther.engTypeCd)
			&& this.ym.equals(castOther.ym);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.engTypeCd.hashCode();
		hash = hash * prime + this.ym.hashCode();
		
		return hash;
	}
}