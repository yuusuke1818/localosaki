package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_timely_weather database table.
 * 
 */
@Embeddable
public class TTimelyWeatherPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="city_cd", insertable=false, updatable=false, unique=true, nullable=false, length=8)
	private String cityCd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="target_date_time", unique=true, nullable=false)
	private java.util.Date targetDateTime;

	public TTimelyWeatherPK() {
	}
	public String getCityCd() {
		return this.cityCd;
	}
	public void setCityCd(String cityCd) {
		this.cityCd = cityCd;
	}
	public java.util.Date getTargetDateTime() {
		return this.targetDateTime;
	}
	public void setTargetDateTime(java.util.Date targetDateTime) {
		this.targetDateTime = targetDateTime;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TTimelyWeatherPK)) {
			return false;
		}
		TTimelyWeatherPK castOther = (TTimelyWeatherPK)other;
		return 
			this.cityCd.equals(castOther.cityCd)
			&& this.targetDateTime.equals(castOther.targetDateTime);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cityCd.hashCode();
		hash = hash * prime + this.targetDateTime.hashCode();
		
		return hash;
	}
}