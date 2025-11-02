package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_amedas_weather database table.
 * 
 */
@Embeddable
public class TAmedasWeatherPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="amedas_observatory_no", insertable=false, updatable=false, unique=true, nullable=false, length=5)
	private String amedasObservatoryNo;

        @Temporal(TemporalType.TIMESTAMP)
	@Column(name="observation_date", unique=true, nullable=false)
	private Date observationDate;

	public TAmedasWeatherPK() {
	}
	public String getAmedasObservatoryNo() {
		return this.amedasObservatoryNo;
	}
	public void setAmedasObservatoryNo(String amedasObservatoryNo) {
		this.amedasObservatoryNo = amedasObservatoryNo;
	}
	public java.util.Date getObservationDate() {
		return this.observationDate;
	}
	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TAmedasWeatherPK)) {
			return false;
		}
		TAmedasWeatherPK castOther = (TAmedasWeatherPK)other;
		return 
			this.amedasObservatoryNo.equals(castOther.amedasObservatoryNo)
			&& this.observationDate.equals(castOther.observationDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.amedasObservatoryNo.hashCode();
		hash = hash * prime + this.observationDate.hashCode();
		
		return hash;
	}
}