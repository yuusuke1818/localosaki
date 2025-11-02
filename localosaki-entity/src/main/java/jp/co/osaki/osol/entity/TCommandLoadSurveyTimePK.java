package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_command_load_survey_time database table.
 * 
 */
@Embeddable
public class TCommandLoadSurveyTimePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(unique=true, nullable=false, length=100)
	private String command;

	@Column(name="meter_mng_id", unique=true, nullable=false)
	private Long meterMngId;

	@Column(name="request_date", unique=true, nullable=false, length=8)
	private String requestDate;

	@Column(name="start_time", unique=true, nullable=false, length=4)
	private String startTime;

	@Column(name="end_time", unique=true, nullable=false, length=4)
	private String endTime;

	public TCommandLoadSurveyTimePK() {
	}
	public String getDevId() {
		return this.devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getCommand() {
		return this.command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Long getMeterMngId() {
		return this.meterMngId;
	}
	public void setMeterMngId(Long meterMngId) {
		this.meterMngId = meterMngId;
	}
	public String getRequestDate() {
		return this.requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getStartTime() {
		return this.startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return this.endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TCommandLoadSurveyTimePK)) {
			return false;
		}
		TCommandLoadSurveyTimePK castOther = (TCommandLoadSurveyTimePK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.command.equals(castOther.command)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.requestDate.equals(castOther.requestDate)
			&& this.startTime.equals(castOther.startTime)
			&& this.endTime.equals(castOther.endTime);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.command.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.requestDate.hashCode();
		hash = hash * prime + this.startTime.hashCode();
		hash = hash * prime + this.endTime.hashCode();
		
		return hash;
	}
}