package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_time_slot_weather database table.
 * 
 */
@Embeddable
public class TTimeSlotWeatherPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="city_cd", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String cityCd;

	@Temporal(TemporalType.DATE)
	@Column(name="target_date", unique=true, nullable=false)
	private java.util.Date targetDate;

	@Column(name="weather_time_slot", unique=true, nullable=false, length=6)
	private String weatherTimeSlot;

	public TTimeSlotWeatherPK() {
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
	public String getWeatherTimeSlot() {
		return this.weatherTimeSlot;
	}
	public void setWeatherTimeSlot(String weatherTimeSlot) {
		this.weatherTimeSlot = weatherTimeSlot;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TTimeSlotWeatherPK)) {
			return false;
		}
		TTimeSlotWeatherPK castOther = (TTimeSlotWeatherPK)other;
		return 
			this.cityCd.equals(castOther.cityCd)
			&& this.targetDate.equals(castOther.targetDate)
			&& this.weatherTimeSlot.equals(castOther.weatherTimeSlot);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cityCd.hashCode();
		hash = hash * prime + this.targetDate.hashCode();
		hash = hash * prime + this.weatherTimeSlot.hashCode();
		
		return hash;
	}
}