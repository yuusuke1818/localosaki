package jp.co.osaki.osol.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_work_hst database table.
 * 
 */
@Embeddable
public class TWorkHstPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="dev_id", unique=true, nullable=false, length=8)
	private String devId;

	@Column(unique=true, nullable=false, length=20)
	private String command;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="rec_date", unique=true, nullable=false)
	private java.util.Date recDate;

	public TWorkHstPK() {
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
	public java.util.Date getRecDate() {
		return this.recDate;
	}
	public void setRecDate(java.util.Date recDate) {
		this.recDate = recDate;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TWorkHstPK)) {
			return false;
		}
		TWorkHstPK castOther = (TWorkHstPK)other;
		return 
			this.devId.equals(castOther.devId)
			&& this.command.equals(castOther.command)
			&& this.recDate.equals(castOther.recDate);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.devId.hashCode();
		hash = hash * prime + this.command.hashCode();
		hash = hash * prime + this.recDate.hashCode();
		
		return hash;
	}
}