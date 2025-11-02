package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_corp_function_use database table.
 * 
 */
@Embeddable
public class MCorpFunctionUsePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="function_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String functionCd;

	public MCorpFunctionUsePK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getFunctionCd() {
		return this.functionCd;
	}
	public void setFunctionCd(String functionCd) {
		this.functionCd = functionCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCorpFunctionUsePK)) {
			return false;
		}
		MCorpFunctionUsePK castOther = (MCorpFunctionUsePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.functionCd.equals(castOther.functionCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.functionCd.hashCode();
		
		return hash;
	}
}