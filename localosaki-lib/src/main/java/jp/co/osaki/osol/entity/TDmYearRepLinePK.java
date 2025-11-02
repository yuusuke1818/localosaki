package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_dm_year_rep_line database table.
 * 
 */
@Embeddable
public class TDmYearRepLinePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="year_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String yearNo;

	@Column(name="month_no", insertable=false, updatable=false, unique=true, nullable=false, precision=2)
	private BigDecimal monthNo;

	@Column(name="summary_unit", insertable=false, updatable=false, unique=true, nullable=false, length=6)
	private String summaryUnit;

	@Column(name="line_group_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long lineGroupId;

	@Column(name="line_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String lineNo;

	public TDmYearRepLinePK() {
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
	public String getYearNo() {
		return this.yearNo;
	}
	public void setYearNo(String yearNo) {
		this.yearNo = yearNo;
	}
	public BigDecimal getMonthNo() {
		return this.monthNo;
	}
	public void setMonthNo(BigDecimal monthNo) {
		this.monthNo = monthNo;
	}
	public String getSummaryUnit() {
		return this.summaryUnit;
	}
	public void setSummaryUnit(String summaryUnit) {
		this.summaryUnit = summaryUnit;
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
		if (!(other instanceof TDmYearRepLinePK)) {
			return false;
		}
		TDmYearRepLinePK castOther = (TDmYearRepLinePK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.yearNo.equals(castOther.yearNo)
			&& this.monthNo.equals(castOther.monthNo)
			&& this.summaryUnit.equals(castOther.summaryUnit)
			&& this.lineGroupId.equals(castOther.lineGroupId)
			&& this.lineNo.equals(castOther.lineNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.yearNo.hashCode();
		hash = hash * prime + this.monthNo.hashCode();
		hash = hash * prime + this.summaryUnit.hashCode();
		hash = hash * prime + this.lineGroupId.hashCode();
		hash = hash * prime + this.lineNo.hashCode();
		
		return hash;
	}
}