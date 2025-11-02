package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_demand_collect_product database table.
 * 
 */
@Embeddable
public class MDemandCollectProductPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="demand_collect_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String demandCollectCd;

	@Column(name="product_cd", insertable=false, updatable=false, unique=true, nullable=false, length=2)
	private String productCd;

	public MDemandCollectProductPK() {
	}
	public String getDemandCollectCd() {
		return this.demandCollectCd;
	}
	public void setDemandCollectCd(String demandCollectCd) {
		this.demandCollectCd = demandCollectCd;
	}
	public String getProductCd() {
		return this.productCd;
	}
	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MDemandCollectProductPK)) {
			return false;
		}
		MDemandCollectProductPK castOther = (MDemandCollectProductPK)other;
		return 
			this.demandCollectCd.equals(castOther.demandCollectCd)
			&& this.productCd.equals(castOther.productCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.demandCollectCd.hashCode();
		hash = hash * prime + this.productCd.hashCode();
		
		return hash;
	}
}