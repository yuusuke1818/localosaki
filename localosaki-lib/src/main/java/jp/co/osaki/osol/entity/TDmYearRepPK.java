package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_dm_year_rep database table.
 * 
 */
@Embeddable
public class TDmYearRepPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	@Column(name="year_no", unique=true, nullable=false, length=4)
	private String yearNo;

	@Column(name="month_no", unique=true, nullable=false, precision=2)
	private BigDecimal monthNo;

	@Column(name="summary_unit", unique=true, nullable=false, length=6)
	private String summaryUnit;

	public TDmYearRepPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDmYearRepPK)) {
			return false;
		}
		TDmYearRepPK castOther = (TDmYearRepPK)other;
		return 
			this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId)
			&& this.yearNo.equals(castOther.yearNo)
			&& this.monthNo.equals(castOther.monthNo)
			&& this.summaryUnit.equals(castOther.summaryUnit);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		hash = hash * prime + this.yearNo.hashCode();
		hash = hash * prime + this.monthNo.hashCode();
		hash = hash * prime + this.summaryUnit.hashCode();
		
		return hash;
	}
}