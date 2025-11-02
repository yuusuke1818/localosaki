package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_batch_startup_setting database table.
 *
 */
@Entity
@Table(name="t_batch_startup_setting")
@NamedQuery(name="TBatchStartupSetting.findAll", query="SELECT t FROM TBatchStartupSetting t")
public class TBatchStartupSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="batch_process_cd", unique=true, nullable=false, length=3)
	private String batchProcessCd;

	@Column(name="batch_process_name", nullable=false, length=100)
	private String batchProcessName;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

    @Column(name="schedule_cron_spring", length=1000)
    private String scheduleCronSpring;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

    //bi-directional many-to-one association to MDemandCollectSchedule
    @OneToMany(mappedBy="TBatchStartupSetting")
    private List<MDemandCollectSchedule> MDemandCollectSchedules;
	//bi-directional many-to-one association to MSmCollectManage
	@OneToMany(mappedBy="TBatchStartupSetting")
	private List<MSmCollectManage> MSmCollectManages;

    public TBatchStartupSetting() {
	}

	public String getBatchProcessCd() {
		return this.batchProcessCd;
	}

	public void setBatchProcessCd(String batchProcessCd) {
		this.batchProcessCd = batchProcessCd;
	}

	public String getBatchProcessName() {
		return this.batchProcessName;
	}

	public void setBatchProcessName(String batchProcessName) {
		this.batchProcessName = batchProcessName;
	}

    public String getScheduleCronSpring() {
        return this.scheduleCronSpring;
    }

    public void setScheduleCronSpring(String scheduleCronSpring) {
        this.scheduleCronSpring = scheduleCronSpring;
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

	public List<MDemandCollectSchedule> getMDemandCollectSchedules() {
		return this.MDemandCollectSchedules;
	}

	public void setMDemandCollectSchedules(List<MDemandCollectSchedule> MDemandCollectSchedules) {
		this.MDemandCollectSchedules = MDemandCollectSchedules;
	}

	public MDemandCollectSchedule addMDemandCollectSchedule(MDemandCollectSchedule MDemandCollectSchedule) {
		getMDemandCollectSchedules().add(MDemandCollectSchedule);
		MDemandCollectSchedule.setTBatchStartupSetting(this);

		return MDemandCollectSchedule;
	}

	public MDemandCollectSchedule removeMDemandCollectSchedule(MDemandCollectSchedule MDemandCollectSchedule) {
		getMDemandCollectSchedules().remove(MDemandCollectSchedule);
		MDemandCollectSchedule.setTBatchStartupSetting(null);

		return MDemandCollectSchedule;
	}

	public List<MSmCollectManage> getMSmCollectManages() {
		return this.MSmCollectManages;
	}

	public void setMSmCollectManages(List<MSmCollectManage> MSmCollectManages) {
		this.MSmCollectManages = MSmCollectManages;
	}

	public MSmCollectManage addMSmCollectManage(MSmCollectManage MSmCollectManage) {
		getMSmCollectManages().add(MSmCollectManage);
		MSmCollectManage.setTBatchStartupSetting(this);

		return MSmCollectManage;
	}

	public MSmCollectManage removeMSmCollectManage(MSmCollectManage MSmCollectManage) {
		getMSmCollectManages().remove(MSmCollectManage);
		MSmCollectManage.setTBatchStartupSetting(null);

		return MSmCollectManage;
	}

}