package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_all_night_day database table.
 * 
 */
@Embeddable
public class MAllNightDayPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false, length=4)
	private String year;

	@Column(unique=true, nullable=false, length=2)
	private String month;

	@Column(unique=true, nullable=false, length=2)
	private String day;

	@Column(name="corp_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="building_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long buildingId;

	public MAllNightDayPK() {
	}
	public String getYear() {
		return this.year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return this.month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return this.day;
	}
	public void setDay(String day) {
		this.day = day;
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
		if (!(other instanceof MAllNightDayPK)) {
			return false;
		}
		MAllNightDayPK castOther = (MAllNightDayPK)other;
		return 
			this.year.equals(castOther.year)
			&& this.month.equals(castOther.month)
			&& this.day.equals(castOther.day)
			&& this.corpId.equals(castOther.corpId)
			&& this.buildingId.equals(castOther.buildingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.year.hashCode();
		hash = hash * prime + this.month.hashCode();
		hash = hash * prime + this.day.hashCode();
		hash = hash * prime + this.corpId.hashCode();
		hash = hash * prime + this.buildingId.hashCode();
		
		return hash;
	}
}