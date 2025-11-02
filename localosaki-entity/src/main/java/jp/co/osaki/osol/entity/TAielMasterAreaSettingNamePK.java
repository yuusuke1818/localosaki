package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_aiel_master_area_setting_name database table.
 * 
 */
@Embeddable
public class TAielMasterAreaSettingNamePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="area_no", unique=true, nullable=false)
	private Integer areaNo;

	public TAielMasterAreaSettingNamePK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public Integer getAreaNo() {
		return this.areaNo;
	}
	public void setAreaNo(Integer areaNo) {
		this.areaNo = areaNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TAielMasterAreaSettingNamePK)) {
			return false;
		}
		TAielMasterAreaSettingNamePK castOther = (TAielMasterAreaSettingNamePK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.areaNo.equals(castOther.areaNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.areaNo.hashCode();
		
		return hash;
	}
}