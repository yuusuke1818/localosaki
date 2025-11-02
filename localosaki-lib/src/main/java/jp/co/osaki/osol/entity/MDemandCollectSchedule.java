package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_demand_collect_schedule database table.
 * 
 */
@Entity
@Table(name="m_demand_collect_schedule")
@NamedQuery(name="MDemandCollectSchedule.findAll", query="SELECT m FROM MDemandCollectSchedule m")
public class MDemandCollectSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MDemandCollectSchedulePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="schedule_crontab", nullable=false, length=1000)
	private String scheduleCrontab;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MDemandCollect
	@ManyToOne
	@JoinColumn(name="demand_collect_cd", nullable=false, insertable=false, updatable=false)
	private MDemandCollect MDemandCollect;

	//bi-directional many-to-one association to TBatchStartupSetting
	@ManyToOne
	@JoinColumn(name="batch_process_cd", nullable=false, insertable=false, updatable=false)
	private TBatchStartupSetting TBatchStartupSetting;

	public MDemandCollectSchedule() {
	}

	public MDemandCollectSchedulePK getId() {
		return this.id;
	}

	public void setId(MDemandCollectSchedulePK id) {
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

	public String getScheduleCrontab() {
		return this.scheduleCrontab;
	}

	public void setScheduleCrontab(String scheduleCrontab) {
		this.scheduleCrontab = scheduleCrontab;
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

	public MDemandCollect getMDemandCollect() {
		return this.MDemandCollect;
	}

	public void setMDemandCollect(MDemandCollect MDemandCollect) {
		this.MDemandCollect = MDemandCollect;
	}

	public TBatchStartupSetting getTBatchStartupSetting() {
		return this.TBatchStartupSetting;
	}

	public void setTBatchStartupSetting(TBatchStartupSetting TBatchStartupSetting) {
		this.TBatchStartupSetting = TBatchStartupSetting;
	}

}