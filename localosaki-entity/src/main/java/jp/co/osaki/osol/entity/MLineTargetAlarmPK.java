package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_line_target_alarm database table.
 * 
 */
@Embeddable
public class MLineTargetAlarmPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="line_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long lineGroupId;

	@Column(name="line_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String lineNo;

	public MLineTargetAlarmPK() {
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
	public Long getLineGroupId() {
		return this.lineGroupId;
	}
	public void setLineGroupId(Long lineGroupId) {
		this.lineGroupId = lineGroupId;
	}
	public String getLineNo() {
		return this.lineNo;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MLineTargetAlarmPK)) {
			return false;
		}
		MLineTargetAlarmPK castOther = (MLineTargetAlarmPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.lineGroupId.equals(castOther.lineGroupId)
			&& this.lineNo.equals(castOther.lineNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.lineGroupId.hashCode();
		hash = hash * prime + this.lineNo.hashCode();
		
		return hash;
	}
}