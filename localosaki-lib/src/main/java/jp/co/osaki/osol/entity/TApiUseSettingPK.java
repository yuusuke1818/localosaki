package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_api_use_setting database table.
 * 
 */
@Embeddable
public class TApiUseSettingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="api_kind", unique=true, nullable=false, length=10)
	private String apiKind;

	public TApiUseSettingPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getApiKind() {
		return this.apiKind;
	}
	public void setApiKind(String apiKind) {
		this.apiKind = apiKind;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TApiUseSettingPK)) {
			return false;
		}
		TApiUseSettingPK castOther = (TApiUseSettingPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.apiKind.equals(castOther.apiKind);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.apiKind.hashCode();
		
		return hash;
	}
}