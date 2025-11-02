package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_maintenance_trader database table.
 * 
 */
@Embeddable
public class MMaintenanceTraderPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="maintenance_trader_id", unique=true, nullable=false)
	private Long maintenanceTraderId;

	public MMaintenanceTraderPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getMaintenanceTraderId() {
		return this.maintenanceTraderId;
	}
	public void setMaintenanceTraderId(Long maintenanceTraderId) {
		this.maintenanceTraderId = maintenanceTraderId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MMaintenanceTraderPK)) {
			return false;
		}
		MMaintenanceTraderPK castOther = (MMaintenanceTraderPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.maintenanceTraderId.equals(castOther.maintenanceTraderId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.maintenanceTraderId.hashCode();
		
		return hash;
	}
}