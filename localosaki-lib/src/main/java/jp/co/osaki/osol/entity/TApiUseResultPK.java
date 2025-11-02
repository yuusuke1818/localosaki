package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_api_use_result database table.
 * 
 */
@Embeddable
public class TApiUseResultPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Temporal(TemporalType.DATE)
	@Column(name="use_date", unique=true, nullable=false)
	private java.util.Date useDate;

	@Column(name="api_kind", unique=true, nullable=false, length=10)
	private String apiKind;

	public TApiUseResultPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public java.util.Date getUseDate() {
		return this.useDate;
	}
	public void setUseDate(java.util.Date useDate) {
		this.useDate = useDate;
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
		if (!(other instanceof TApiUseResultPK)) {
			return false;
		}
		TApiUseResultPK castOther = (TApiUseResultPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.useDate.equals(castOther.useDate)
			&& this.apiKind.equals(castOther.apiKind);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.useDate.hashCode();
		hash = hash * prime + this.apiKind.hashCode();
		
		return hash;
	}
}