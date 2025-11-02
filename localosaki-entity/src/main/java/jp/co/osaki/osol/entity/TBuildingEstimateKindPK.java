package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_building_estimate_kind database table.
 * 
 */
@Embeddable
public class TBuildingEstimateKindPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="estimate_corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String estimateCorpId;

	@Column(name="estimate_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long estimateId;

	public TBuildingEstimateKindPK() {
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
	public String getEstimateCorpId() {
		return this.estimateCorpId;
	}
	public void setEstimateCorpId(String estimateCorpId) {
		this.estimateCorpId = estimateCorpId;
	}
	public Long getEstimateId() {
		return this.estimateId;
	}
	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TBuildingEstimateKindPK)) {
			return false;
		}
		TBuildingEstimateKindPK castOther = (TBuildingEstimateKindPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.estimateCorpId.equals(castOther.estimateCorpId)
			&& this.estimateId.equals(castOther.estimateId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.estimateCorpId.hashCode();
		hash = hash * prime + this.estimateId.hashCode();
		
		return hash;
	}
}