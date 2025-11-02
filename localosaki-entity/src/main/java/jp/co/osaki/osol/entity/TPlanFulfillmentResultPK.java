package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_plan_fulfillment_result database table.
 * 
 */
@Embeddable
public class TPlanFulfillmentResultPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="plan_fulfillment_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long planFulfillmentId;

	@Column(name="plan_fulfillment_result_id", unique=true, nullable=false)
	private Long planFulfillmentResultId;

	public TPlanFulfillmentResultPK() {
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
	public Long getPlanFulfillmentId() {
		return this.planFulfillmentId;
	}
	public void setPlanFulfillmentId(Long planFulfillmentId) {
		this.planFulfillmentId = planFulfillmentId;
	}
	public Long getPlanFulfillmentResultId() {
		return this.planFulfillmentResultId;
	}
	public void setPlanFulfillmentResultId(Long planFulfillmentResultId) {
		this.planFulfillmentResultId = planFulfillmentResultId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TPlanFulfillmentResultPK)) {
			return false;
		}
		TPlanFulfillmentResultPK castOther = (TPlanFulfillmentResultPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.planFulfillmentId.equals(castOther.planFulfillmentId)
			&& this.planFulfillmentResultId.equals(castOther.planFulfillmentResultId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.planFulfillmentId.hashCode();
		hash = hash * prime + this.planFulfillmentResultId.hashCode();
		
		return hash;
	}
}