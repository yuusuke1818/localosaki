package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_day_load_survey_max_demand database table.
 * 
 */
@Embeddable
public class TDayLoadSurveyMaxDemandPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id")
	private String devId;

	@Column(name="meter_mng_id")
	private Long meterMngId;

	@Column(name="get_survey_date")
	private String getSurveyDate;

	public TDayLoadSurveyMaxDemandPK() {
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public Long getMeterMngId() {
		return this.meterMngId;
	}
	public void setMeterMngId(Long meterMngId) {
		this.meterMngId = meterMngId;
	}
	public String getGetSurveyDate() {
		return this.getSurveyDate;
	}
	public void setGetSurveyDate(String getSurveyDate) {
		this.getSurveyDate = getSurveyDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TDayLoadSurveyMaxDemandPK)) {
			return false;
		}
		TDayLoadSurveyMaxDemandPK castOther = (TDayLoadSurveyMaxDemandPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.getSurveyDate.equals(castOther.getSurveyDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.getSurveyDate.hashCode();
		
		return hash;
	}
}