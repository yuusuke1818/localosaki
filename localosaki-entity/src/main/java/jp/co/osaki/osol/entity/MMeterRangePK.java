package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_meter_range database table.
 * 
 */
@Embeddable
public class MMeterRangePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="range_value", unique=true, nullable=false)
	private Long rangeValue;

	@Column(name="meter_type", insertable=false, updatable=false, unique=true, nullable=false)
	private Long meterType;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="menu_no", insertable=false, updatable=false, unique=true, nullable=false)
	private Long menuNo;

	public MMeterRangePK() {
	}
	public Long getRangeValue() {
		return this.rangeValue;
	}
	public void setRangeValue(Long rangeValue) {
		this.rangeValue = rangeValue;
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
		if (!(other instanceof MMeterRangePK)) {
			return false;
		}
		MMeterRangePK castOther = (MMeterRangePK)other;
		return 
			this.rangeValue.equals(castOther.rangeValue)
			&& this.meterType.equals(castOther.meterType)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.menuNo.equals(castOther.menuNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.rangeValue.hashCode();
		hash = hash * prime + this.meterType.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.menuNo.hashCode();
		
		return hash;
	}
}