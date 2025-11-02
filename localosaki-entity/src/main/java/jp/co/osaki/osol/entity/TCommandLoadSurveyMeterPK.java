package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the t_command_load_survey_meter database table.
 * 
 */
@Embeddable
public class TCommandLoadSurveyMeterPK implements Serializable {
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

	public TCommandLoadSurveyMeterPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TCommandLoadSurveyMeterPK)) {
			return false;
		}
		TCommandLoadSurveyMeterPK castOther = (TCommandLoadSurveyMeterPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.command.equals(castOther.command)
			&& this.meterMngId.equals(castOther.meterMngId)
			&& this.requestDate.equals(castOther.requestDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.command.hashCode();
		hash = hash * prime + this.meterMngId.hashCode();
		hash = hash * prime + this.requestDate.hashCode();
		
		return hash;
	}
}