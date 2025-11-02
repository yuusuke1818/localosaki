package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_unit_price database table.
 * 
 */
@Embeddable
public class MUnitPricePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="price_plan_id", unique=true, nullable=false)
	private Long pricePlanId;

	@Column(name="limit_usage_val", unique=true, nullable=false)
	private Long limitUsageVal;

	public MUnitPricePK() {
	}
	public Long getPricePlanId() {
		return this.pricePlanId;
	}
	public void setPricePlanId(Long pricePlanId) {
		this.pricePlanId = pricePlanId;
	}
	public Long getLimitUsageVal() {
		return this.limitUsageVal;
	}
	public void setLimitUsageVal(Long limitUsageVal) {
		this.limitUsageVal = limitUsageVal;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MUnitPricePK)) {
			return false;
		}
		MUnitPricePK castOther = (MUnitPricePK)other;
		return 
			this.pricePlanId.equals(castOther.pricePlanId)
			&& this.limitUsageVal.equals(castOther.limitUsageVal);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.pricePlanId.hashCode();
		hash = hash * prime + this.limitUsageVal.hashCode();
		
		return hash;
	}
}