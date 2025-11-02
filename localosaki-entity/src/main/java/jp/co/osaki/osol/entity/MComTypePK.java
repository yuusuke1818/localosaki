package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_com_type database table.
 * 
 */
@Embeddable
public class MComTypePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="com_type", unique=true, nullable=false, length=2)
	private String comType;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MComTypePK() {
	}
	public String getComType() {
		return this.comType;
	}
	public void setComType(String comType) {
		this.comType = comType;
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
		if (!(other instanceof MComTypePK)) {
			return false;
		}
		MComTypePK castOther = (MComTypePK)other;
		return 
			this.comType.equals(castOther.comType)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.comType.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}