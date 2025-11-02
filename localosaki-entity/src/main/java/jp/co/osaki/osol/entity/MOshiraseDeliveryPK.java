package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_oshirase_delivery database table.
 *
 */
@Embeddable
public class MOshiraseDeliveryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="oshirase_id", insertable=false, updatable=false)
	private Long oshiraseId;

	@Column(name="delivery_cd")
	private String deliveryCd;

	public MOshiraseDeliveryPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getOshiraseId() {
		return this.oshiraseId;
	}
	public void setOshiraseId(Long oshiraseId) {
		this.oshiraseId = oshiraseId;
	}
	public String getDeliveryCd() {
		return this.deliveryCd;
	}
	public void setDeliveryCd(String deliveryCd) {
		this.deliveryCd = deliveryCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MOshiraseDeliveryPK)) {
			return false;
		}
		MOshiraseDeliveryPK castOther = (MOshiraseDeliveryPK)other;
		return
			this.corpId.equals(castOther.corpId)
			&& this.oshiraseId.equals(castOther.oshiraseId)
			&& this.deliveryCd.equals(castOther.deliveryCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.oshiraseId.hashCode();
		hash = hash * prime + this.deliveryCd.hashCode();

		return hash;
	}
}