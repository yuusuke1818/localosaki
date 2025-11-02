package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_demand_power_forecast_day database table.
 * 
 */
@Embeddable
public class TDemandPowerForecastDayPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
	private Long smId;

	@Temporal(TemporalType.DATE)
	@Column(name="forecast_date", unique=true, nullable=false)
	private java.util.Date forecastDate;

	public TDemandPowerForecastDayPK() {
	}
	public Long getSmId() {
		return this.smId;
	}
	public void setSmId(Long smId) {
		this.smId = smId;
	}
	public java.util.Date getForecastDate() {
		return this.forecastDate;
	}
	public void setForecastDate(java.util.Date forecastDate) {
		this.forecastDate = forecastDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDemandPowerForecastDayPK)) {
			return false;
		}
		TDemandPowerForecastDayPK castOther = (TDemandPowerForecastDayPK)other;
		return 
			this.smId.equals(castOther.smId)
			&& this.forecastDate.equals(castOther.forecastDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.smId.hashCode();
		hash = hash * prime + this.forecastDate.hashCode();
		
		return hash;
	}
}