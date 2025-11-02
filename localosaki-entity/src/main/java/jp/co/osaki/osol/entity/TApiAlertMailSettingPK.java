package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_api_alert_mail_setting database table.
 * 
 */
@Embeddable
public class TApiAlertMailSettingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="api_alert_mail_setting_id", unique=true, nullable=false)
	private Long apiAlertMailSettingId;

	public TApiAlertMailSettingPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getApiAlertMailSettingId() {
		return this.apiAlertMailSettingId;
	}
	public void setApiAlertMailSettingId(Long apiAlertMailSettingId) {
		this.apiAlertMailSettingId = apiAlertMailSettingId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TApiAlertMailSettingPK)) {
			return false;
		}
		TApiAlertMailSettingPK castOther = (TApiAlertMailSettingPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.apiAlertMailSettingId.equals(castOther.apiAlertMailSettingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.apiAlertMailSettingId.hashCode();
		
		return hash;
	}
}