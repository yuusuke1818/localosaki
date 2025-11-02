package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_product_control_load database table.
 * 
 */
@Embeddable
public class MProductControlLoadPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="product_cd", insertable=false, updatable=false, unique=true, nullable=false, length=2)
	private String productCd;

	@Column(name="control_load", unique=true, nullable=false, precision=3)
	private BigDecimal controlLoad;

	public MProductControlLoadPK() {
	}
	public String getProductCd() {
		return this.productCd;
	}
	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}
	public BigDecimal getControlLoad() {
		return this.controlLoad;
	}
	public void setControlLoad(BigDecimal controlLoad) {
		this.controlLoad = controlLoad;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MProductControlLoadPK)) {
			return false;
		}
		MProductControlLoadPK castOther = (MProductControlLoadPK)other;
		return 
			this.productCd.equals(castOther.productCd)
			&& this.controlLoad.equals(castOther.controlLoad);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productCd.hashCode();
		hash = hash * prime + this.controlLoad.hashCode();
		
		return hash;
	}
}