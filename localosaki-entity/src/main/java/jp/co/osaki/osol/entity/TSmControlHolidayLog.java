package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_sm_control_holiday_log database table.
 * 
 */
@Entity
@Table(name="t_sm_control_holiday_log")
@NamedQuery(name="TSmControlHolidayLog.findAll", query="SELECT t FROM TSmControlHolidayLog t")
public class TSmControlHolidayLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmControlHolidayLogPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="setting_update_datetime", nullable=false)
	private Timestamp settingUpdateDatetime;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TSmControlHolidayCalLog
	@OneToMany(mappedBy="TSmControlHolidayLog")
	private List<TSmControlHolidayCalLog> TSmControlHolidayCalLogs;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public TSmControlHolidayLog() {
	}

	public TSmControlHolidayLogPK getId() {
		return this.id;
	}

	public void setId(TSmControlHolidayLogPK id) {
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

	public Timestamp getSettingUpdateDatetime() {
		return this.settingUpdateDatetime;
	}

	public void setSettingUpdateDatetime(Timestamp settingUpdateDatetime) {
		this.settingUpdateDatetime = settingUpdateDatetime;
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

	public List<TSmControlHolidayCalLog> getTSmControlHolidayCalLogs() {
		return this.TSmControlHolidayCalLogs;
	}

	public void setTSmControlHolidayCalLogs(List<TSmControlHolidayCalLog> TSmControlHolidayCalLogs) {
		this.TSmControlHolidayCalLogs = TSmControlHolidayCalLogs;
	}

	public TSmControlHolidayCalLog addTSmControlHolidayCalLog(TSmControlHolidayCalLog TSmControlHolidayCalLog) {
		getTSmControlHolidayCalLogs().add(TSmControlHolidayCalLog);
		TSmControlHolidayCalLog.setTSmControlHolidayLog(this);

		return TSmControlHolidayCalLog;
	}

	public TSmControlHolidayCalLog removeTSmControlHolidayCalLog(TSmControlHolidayCalLog TSmControlHolidayCalLog) {
		getTSmControlHolidayCalLogs().remove(TSmControlHolidayCalLog);
		TSmControlHolidayCalLog.setTSmControlHolidayLog(null);

		return TSmControlHolidayCalLog;
	}

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

}