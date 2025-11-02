package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_sm_control_schedule_time_log database table.
 * 
 */
@Entity
@Table(name="t_sm_control_schedule_time_log")
@NamedQuery(name="TSmControlScheduleTimeLog.findAll", query="SELECT t FROM TSmControlScheduleTimeLog t")
public class TSmControlScheduleTimeLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmControlScheduleTimeLogPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="end_time_hour", nullable=false)
	private Integer endTimeHour;

	@Column(name="end_time_minute", nullable=false)
	private Integer endTimeMinute;

	@Column(name="start_time_hour", nullable=false)
	private Integer startTimeHour;

	@Column(name="start_time_minute", nullable=false)
	private Integer startTimeMinute;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TSmControlScheduleLog
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sm_control_schedule_log_id", referencedColumnName="sm_control_schedule_log_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private TSmControlScheduleLog TSmControlScheduleLog;

	public TSmControlScheduleTimeLog() {
	}

	public TSmControlScheduleTimeLogPK getId() {
		return this.id;
	}

	public void setId(TSmControlScheduleTimeLogPK id) {
		this.id = id;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getEndTimeHour() {
		return this.endTimeHour;
	}

	public void setEndTimeHour(Integer endTimeHour) {
		this.endTimeHour = endTimeHour;
	}

	public Integer getEndTimeMinute() {
		return this.endTimeMinute;
	}

	public void setEndTimeMinute(Integer endTimeMinute) {
		this.endTimeMinute = endTimeMinute;
	}

	public Integer getStartTimeHour() {
		return this.startTimeHour;
	}

	public void setStartTimeHour(Integer startTimeHour) {
		this.startTimeHour = startTimeHour;
	}

	public Integer getStartTimeMinute() {
		return this.startTimeMinute;
	}

	public void setStartTimeMinute(Integer startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TSmControlScheduleLog getTSmControlScheduleLog() {
		return this.TSmControlScheduleLog;
	}

	public void setTSmControlScheduleLog(TSmControlScheduleLog TSmControlScheduleLog) {
		this.TSmControlScheduleLog = TSmControlScheduleLog;
	}

}