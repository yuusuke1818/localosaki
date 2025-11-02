package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_dm_week_rep_point database table.
 * 
 */
@Embeddable
public class TDmWeekRepPointPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Column(name="fiscal_year", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String fiscalYear;

	@Column(name="week_no", insertable=false, updatable=false, unique=true, nullable=false, precision=2)
	private BigDecimal weekNo;

	@Column(name="summary_unit", insertable=false, updatable=false, unique=true, nullable=false, length=6)
	private String summaryUnit;

	@Column(name="point_no", insertable=false, updatable=false, unique=true, nullable=false, length=4)
	private String pointNo;

	public TDmWeekRepPointPK() {
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
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public String getFiscalYear() {
		return this.fiscalYear;
	}
	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	public BigDecimal getWeekNo() {
		return this.weekNo;
	}
	public void setWeekNo(BigDecimal weekNo) {
		this.weekNo = weekNo;
	}
	public String getSummaryUnit() {
		return this.summaryUnit;
	}
	public void setSummaryUnit(String summaryUnit) {
		this.summaryUnit = summaryUnit;
	}
	public String getPointNo() {
		return this.pointNo;
	}
	public void setPointNo(String pointNo) {
		this.pointNo = pointNo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmWeekRepPointPK)) {
			return false;
		}
		TDmWeekRepPointPK castOther = (TDmWeekRepPointPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.smId.equals(castOther.smId)
			&& this.fiscalYear.equals(castOther.fiscalYear)
			&& this.weekNo.equals(castOther.weekNo)
			&& this.summaryUnit.equals(castOther.summaryUnit)
			&& this.pointNo.equals(castOther.pointNo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.fiscalYear.hashCode();
		hash = hash * prime + this.weekNo.hashCode();
		hash = hash * prime + this.summaryUnit.hashCode();
		hash = hash * prime + this.pointNo.hashCode();
		
		return hash;
	}
}