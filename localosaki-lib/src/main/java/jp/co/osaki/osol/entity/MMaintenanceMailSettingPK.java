package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_maintenance_mail_setting database table.
 * 
 */
@Embeddable
public class MMaintenanceMailSettingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="maintenance_mail_setting_id", unique=true, nullable=false)
	private Long maintenanceMailSettingId;

	public MMaintenanceMailSettingPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getMaintenanceMailSettingId() {
		return this.maintenanceMailSettingId;
	}
	public void setMaintenanceMailSettingId(Long maintenanceMailSettingId) {
		this.maintenanceMailSettingId = maintenanceMailSettingId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MMaintenanceMailSettingPK)) {
			return false;
		}
		MMaintenanceMailSettingPK castOther = (MMaintenanceMailSettingPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.maintenanceMailSettingId.equals(castOther.maintenanceMailSettingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.maintenanceMailSettingId.hashCode();
		
		return hash;
	}
}