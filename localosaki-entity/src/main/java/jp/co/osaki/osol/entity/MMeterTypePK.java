package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_meter_type database table.
 * 
 */
@Embeddable
public class MMeterTypePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="meter_type", unique=true, nullable=false)
	private Long meterType;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="menu_no", unique=true, nullable=false)
	private Long menuNo;

	public MMeterTypePK() {
	}
	public Long getMeterType() {
		return this.meterType;
	}
	public void setMeterType(Long meterType) {
		this.meterType = meterType;
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
	public Long getMenuNo() {
		return this.menuNo;
	}
	public void setMenuNo(Long menuNo) {
		this.menuNo = menuNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MMeterTypePK)) {
			return false;
		}
		MMeterTypePK castOther = (MMeterTypePK)other;
		return 
			this.meterType.equals(castOther.meterType)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.menuNo.equals(castOther.menuNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.meterType.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.menuNo.hashCode();
		
		return hash;
	}
}