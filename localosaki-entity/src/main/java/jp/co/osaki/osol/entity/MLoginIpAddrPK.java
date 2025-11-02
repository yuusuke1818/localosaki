package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_login_ip_addr database table.
 * 
 */
@Embeddable
public class MLoginIpAddrPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="login_ip_addr_id", unique=true, nullable=false)
	private Long loginIpAddrId;

	public MLoginIpAddrPK() {
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getLoginIpAddrId() {
		return this.loginIpAddrId;
	}
	public void setLoginIpAddrId(Long loginIpAddrId) {
		this.loginIpAddrId = loginIpAddrId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MLoginIpAddrPK)) {
			return false;
		}
		MLoginIpAddrPK castOther = (MLoginIpAddrPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.loginIpAddrId.equals(castOther.loginIpAddrId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.loginIpAddrId.hashCode();
		
		return hash;
	}
}