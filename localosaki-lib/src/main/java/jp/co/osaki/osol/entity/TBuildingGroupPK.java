package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_building_group database table.
 * 
 */
@Embeddable
public class TBuildingGroupPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="parent_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long parentGroupId;

	@Column(name="child_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long childGroupId;

	public TBuildingGroupPK() {
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
	public Long getParentGroupId() {
		return this.parentGroupId;
	}
	public void setParentGroupId(Long parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	public Long getChildGroupId() {
		return this.childGroupId;
	}
	public void setChildGroupId(Long childGroupId) {
		this.childGroupId = childGroupId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TBuildingGroupPK)) {
			return false;
		}
		TBuildingGroupPK castOther = (TBuildingGroupPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.parentGroupId.equals(castOther.parentGroupId)
			&& this.childGroupId.equals(castOther.childGroupId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.parentGroupId.hashCode();
		hash = hash * prime + this.childGroupId.hashCode();
		
		return hash;
	}
}