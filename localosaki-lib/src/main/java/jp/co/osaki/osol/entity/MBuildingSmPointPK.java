package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_building_sm_point database table.
 * 
 */
@Embeddable
public class MBuildingSmPointPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="point_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String pointNo;

	public MBuildingSmPointPK() {
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
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getPointNo() {
		return this.pointNo;
	}
	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MBuildingSmPointPK)) {
			return false;
		}
		MBuildingSmPointPK castOther = (MBuildingSmPointPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.smId.equals(castOther.smId)
			&& this.pointNo.equals(castOther.pointNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.pointNo.hashCode();
		
		return hash;
	}
}