package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_line_time_standards database table.
 * 
 */
@Embeddable
public class MLineTimeStandardPK implements Serializable {
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

	@Column(name="jigen_no", unique=true, nullable=false, precision=2)
	private BigDecimal jigenNo;

	public MLineTimeStandardPK() {
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
	public BigDecimal getJigenNo() {
		return this.jigenNo;
	}
	public void setJigenNo(BigDecimal jigenNo) {
		this.jigenNo = jigenNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MLineTimeStandardPK)) {
			return false;
		}
		MLineTimeStandardPK castOther = (MLineTimeStandardPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.lineGroupId.equals(castOther.lineGroupId)
			&& this.lineNo.equals(castOther.lineNo)
			&& this.jigenNo.equals(castOther.jigenNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.lineGroupId.hashCode();
		hash = hash * prime + this.lineNo.hashCode();
		hash = hash * prime + this.jigenNo.hashCode();
		
		return hash;
	}
}