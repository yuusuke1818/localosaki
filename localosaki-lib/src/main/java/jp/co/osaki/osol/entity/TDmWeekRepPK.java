package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_dm_week_rep database table.
 * 
 */
@Embeddable
public class TDmWeekRepPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="fiscal_year", unique=true, nullable=false, length=4)
	private String fiscalYear;

	@Column(name="week_no", unique=true, nullable=false, precision=2)
	private BigDecimal weekNo;

	@Column(name="summary_unit", unique=true, nullable=false, length=6)
	private String summaryUnit;

	public TDmWeekRepPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmWeekRepPK)) {
			return false;
		}
		TDmWeekRepPK castOther = (TDmWeekRepPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.fiscalYear.equals(castOther.fiscalYear)
			&& this.weekNo.equals(castOther.weekNo)
			&& this.summaryUnit.equals(castOther.summaryUnit);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.fiscalYear.hashCode();
		hash = hash * prime + this.weekNo.hashCode();
		hash = hash * prime + this.summaryUnit.hashCode();
		
		return hash;
	}
}