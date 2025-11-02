package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_building_info database table.
 * 
 */
@Embeddable
public class MBuildingInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="buildng_info_id", unique=true, nullable=false, length=40)
	private String buildngInfoId;

	@Column(unique=true, nullable=false, length=40)
	private String username;

	@Column(unique=true, nullable=false, length=20)
	private String password;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MBuildingInfoPK() {
	}
	public String getBuildngInfoId() {
		return this.buildngInfoId;
	}
	public void setBuildngInfoId(String buildngInfoId) {
		this.buildngInfoId = buildngInfoId;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCorpId() {
		return this.corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Long getBuildingId() {
		return this.buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MBuildingInfoPK)) {
			return false;
		}
		MBuildingInfoPK castOther = (MBuildingInfoPK)other;
		return 
			this.buildngInfoId.equals(castOther.buildngInfoId)
			&& this.username.equals(castOther.username)
			&& this.password.equals(castOther.password)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.buildngInfoId.hashCode();
		hash = hash * prime + this.username.hashCode();
		hash = hash * prime + this.password.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}