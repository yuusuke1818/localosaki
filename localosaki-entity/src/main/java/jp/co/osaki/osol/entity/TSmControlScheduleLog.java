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
 * The persistent class for the t_sm_control_schedule_log database table.
 * 
 */
@Entity
@Table(name="t_sm_control_schedule_log")
@NamedQuery(name="TSmControlScheduleLog.findAll", query="SELECT t FROM TSmControlScheduleLog t")
public class TSmControlScheduleLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TSmControlScheduleLogPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="schedule_manage_designation_flg", nullable=false)
	private Integer scheduleManageDesignationFlg;

	@Column(name="setting_update_datetime", nullable=false)
	private Timestamp settingUpdateDatetime;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	//bi-directional many-to-one association to TSmControlScheduleDutyLog
	@OneToMany(mappedBy="TSmControlScheduleLog")
	private List<TSmControlScheduleDutyLog> TSmControlScheduleDutyLogs;

	//bi-directional many-to-one association to TSmControlScheduleSetLog
	@OneToMany(mappedBy="TSmControlScheduleLog")
	private List<TSmControlScheduleSetLog> TSmControlScheduleSetLogs;

	//bi-directional many-to-one association to TSmControlScheduleTimeLog
	@OneToMany(mappedBy="TSmControlScheduleLog")
	private List<TSmControlScheduleTimeLog> TSmControlScheduleTimeLogs;

	public TSmControlScheduleLog() {
	}

	public TSmControlScheduleLogPK getId() {
		return this.id;
	}

	public void setId(TSmControlScheduleLogPK id) {
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

	public Integer getScheduleManageDesignationFlg() {
		return this.scheduleManageDesignationFlg;
	}

	public void setScheduleManageDesignationFlg(Integer scheduleManageDesignationFlg) {
		this.scheduleManageDesignationFlg = scheduleManageDesignationFlg;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

    public List<TSmControlScheduleDutyLog> getTSmControlScheduleDutyLogs() {
        return this.TSmControlScheduleDutyLogs;
    }

    public void setTSmControlScheduleDutyLogs(List<TSmControlScheduleDutyLog> TSmControlScheduleDutyLogs) {
        this.TSmControlScheduleDutyLogs = TSmControlScheduleDutyLogs;
    }

    public TSmControlScheduleDutyLog addTSmControlScheduleDutyLog(TSmControlScheduleDutyLog TSmControlScheduleDutyLog) {
        getTSmControlScheduleDutyLogs().add(TSmControlScheduleDutyLog);
        TSmControlScheduleDutyLog.setTSmControlScheduleLog(this);

        return TSmControlScheduleDutyLog;
    }

    public List<TSmControlScheduleSetLog> getTSmControlScheduleSetLogs() {
		return this.TSmControlScheduleSetLogs;
	}

	public void setTSmControlScheduleSetLogs(List<TSmControlScheduleSetLog> TSmControlScheduleSetLogs) {
		this.TSmControlScheduleSetLogs = TSmControlScheduleSetLogs;
	}

	public TSmControlScheduleSetLog addTSmControlScheduleSetLog(TSmControlScheduleSetLog TSmControlScheduleSetLog) {
		getTSmControlScheduleSetLogs().add(TSmControlScheduleSetLog);
		TSmControlScheduleSetLog.setTSmControlScheduleLog(this);

		return TSmControlScheduleSetLog;
	}

	public TSmControlScheduleSetLog removeTSmControlScheduleSetLog(TSmControlScheduleSetLog TSmControlScheduleSetLog) {
		getTSmControlScheduleSetLogs().remove(TSmControlScheduleSetLog);
		TSmControlScheduleSetLog.setTSmControlScheduleLog(null);

		return TSmControlScheduleSetLog;
	}

	public List<TSmControlScheduleTimeLog> getTSmControlScheduleTimeLogs() {
		return this.TSmControlScheduleTimeLogs;
	}

	public void setTSmControlScheduleTimeLogs(List<TSmControlScheduleTimeLog> TSmControlScheduleTimeLogs) {
		this.TSmControlScheduleTimeLogs = TSmControlScheduleTimeLogs;
	}

	public TSmControlScheduleTimeLog addTSmControlScheduleTimeLog(TSmControlScheduleTimeLog TSmControlScheduleTimeLog) {
		getTSmControlScheduleTimeLogs().add(TSmControlScheduleTimeLog);
		TSmControlScheduleTimeLog.setTSmControlScheduleLog(this);

		return TSmControlScheduleTimeLog;
	}

	public TSmControlScheduleTimeLog removeTSmControlScheduleTimeLog(TSmControlScheduleTimeLog TSmControlScheduleTimeLog) {
		getTSmControlScheduleTimeLogs().remove(TSmControlScheduleTimeLog);
		TSmControlScheduleTimeLog.setTSmControlScheduleLog(null);

		return TSmControlScheduleTimeLog;
	}

}