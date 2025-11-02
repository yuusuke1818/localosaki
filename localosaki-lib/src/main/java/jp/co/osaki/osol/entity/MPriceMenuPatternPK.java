package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_price_menu_pattern database table.
 * 
 */
@Embeddable
public class MPriceMenuPatternPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="time_ptn_no", unique=true, nullable=false)
	private Long timePtnNo;

	@Column(name="menu_no", insertable=false, updatable=false, unique=true, nullable=false)
	private Long menuNo;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MPriceMenuPatternPK() {
	}
	public Long getTimePtnNo() {
		return this.timePtnNo;
	}
	public void setTimePtnNo(Long timePtnNo) {
		this.timePtnNo = timePtnNo;
	}
	public Long getMenuNo() {
		return this.menuNo;
	}
	public void setMenuNo(Long menuNo) {
		this.menuNo = menuNo;
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MPriceMenuPatternPK)) {
			return false;
		}
		MPriceMenuPatternPK castOther = (MPriceMenuPatternPK)other;
		return 
			this.timePtnNo.equals(castOther.timePtnNo)
			&& this.menuNo.equals(castOther.menuNo)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.timePtnNo.hashCode();
		hash = hash * prime + this.menuNo.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}