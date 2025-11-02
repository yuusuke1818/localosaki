package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_coefficient_history_manage database table.
 * 
 */
@Embeddable
public class MCoefficientHistoryManagePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="eng_type_cd", insertable=false, updatable=false, unique=true, nullable=false, length=3)
	private String engTypeCd;

	@Column(name="eng_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long engId;

	@Column(name="day_and_night_type", unique=true, nullable=false, length=6)
	private String dayAndNightType;

	@Column(name="coefficient_history_id", unique=true, nullable=false)
	private Long coefficientHistoryId;

	public MCoefficientHistoryManagePK() {
	}
	public String getEngTypeCd() {
		return this.engTypeCd;
	}
	public void setEngTypeCd(String engTypeCd) {
		this.engTypeCd = engTypeCd;
	}
	public Long getEngId() {
		return this.engId;
	}
	public void setEngId(Long engId) {
		this.engId = engId;
	}
	public String getDayAndNightType() {
		return this.dayAndNightType;
	}
	public void setDayAndNightType(String dayAndNightType) {
		this.dayAndNightType = dayAndNightType;
	}
	public Long getCoefficientHistoryId() {
		return this.coefficientHistoryId;
	}
	public void setCoefficientHistoryId(Long coefficientHistoryId) {
		this.coefficientHistoryId = coefficientHistoryId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MCoefficientHistoryManagePK)) {
			return false;
		}
		MCoefficientHistoryManagePK castOther = (MCoefficientHistoryManagePK)other;
		return 
			this.engTypeCd.equals(castOther.engTypeCd)
			&& this.engId.equals(castOther.engId)
			&& this.dayAndNightType.equals(castOther.dayAndNightType)
			&& this.coefficientHistoryId.equals(castOther.coefficientHistoryId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.engTypeCd.hashCode();
		hash = hash * prime + this.engId.hashCode();
		hash = hash * prime + this.dayAndNightType.hashCode();
		hash = hash * prime + this.coefficientHistoryId.hashCode();
		
		return hash;
	}
}