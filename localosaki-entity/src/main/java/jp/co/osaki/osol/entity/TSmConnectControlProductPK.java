package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_sm_connect_control_product database table.
 * 
 */
@Embeddable
public class TSmConnectControlProductPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="product_cd", insertable=false, updatable=false, unique=true, nullable=false, length=2)
	private String productCd;

	@Column(name="sm_connect_control_setting_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smConnectControlSettingId;

	public TSmConnectControlProductPK() {
	}
	public String getProductCd() {
		return this.productCd;
	}
	public void setProductCd(String productCd) {
		this.productCd = productCd;
	}
	public Long getSmConnectControlSettingId() {
		return this.smConnectControlSettingId;
	}
	public void setSmConnectControlSettingId(Long smConnectControlSettingId) {
		this.smConnectControlSettingId = smConnectControlSettingId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TSmConnectControlProductPK)) {
			return false;
		}
		TSmConnectControlProductPK castOther = (TSmConnectControlProductPK)other;
		return 
			this.productCd.equals(castOther.productCd)
			&& this.smConnectControlSettingId.equals(castOther.smConnectControlSettingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productCd.hashCode();
		hash = hash * prime + this.smConnectControlSettingId.hashCode();
		
		return hash;
	}
}