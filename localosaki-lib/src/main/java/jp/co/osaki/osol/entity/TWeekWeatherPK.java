package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_week_weather database table.
 * 
 */
@Embeddable
public class TWeekWeatherPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="city_cd", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String cityCd;

	@Temporal(TemporalType.DATE)
	@Column(name="target_date", unique=true, nullable=false)
	private java.util.Date targetDate;

	public TWeekWeatherPK() {
	}
	public String getCityCd() {
		return this.cityCd;
	}
	public void setCityCd(String cityCd) {
		this.cityCd = cityCd;
	}
	public java.util.Date getTargetDate() {
		return this.targetDate;
	}
	public void setTargetDate(java.util.Date targetDate) {
		this.targetDate = targetDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TWeekWeatherPK)) {
			return false;
		}
		TWeekWeatherPK castOther = (TWeekWeatherPK)other;
		return 
			this.cityCd.equals(castOther.cityCd)
			&& this.targetDate.equals(castOther.targetDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cityCd.hashCode();
		hash = hash * prime + this.targetDate.hashCode();
		
		return hash;
	}
}