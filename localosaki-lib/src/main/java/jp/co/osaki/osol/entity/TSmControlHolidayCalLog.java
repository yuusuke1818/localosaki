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
 * The persistent class for the t_sm_control_holiday_cal_log database table.
 * 
 */
@Entity
@Table(name="t_sm_control_holiday_cal_log")
@NamedQuery(name="TSmControlHolidayCalLog.findAll", query="SELECT t FROM TSmControlHolidayCalLog t")
public class TSmControlHolidayCalLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmControlHolidayCalLogPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TSmControlHolidayLog
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="sm_control_holiday_log_id", referencedColumnName="sm_control_holiday_log_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
		})
	private TSmControlHolidayLog TSmControlHolidayLog;

	public TSmControlHolidayCalLog() {
	}

	public TSmControlHolidayCalLogPK getId() {
		return this.id;
	}

	public void setId(TSmControlHolidayCalLogPK id) {
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

	public TSmControlHolidayLog getTSmControlHolidayLog() {
		return this.TSmControlHolidayLog;
	}

	public void setTSmControlHolidayLog(TSmControlHolidayLog TSmControlHolidayLog) {
		this.TSmControlHolidayLog = TSmControlHolidayLog;
	}

}