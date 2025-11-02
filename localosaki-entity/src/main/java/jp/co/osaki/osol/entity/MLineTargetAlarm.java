package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_line_target_alarm database table.
 * 
 */
@Entity
@Table(name="m_line_target_alarm")
@NamedQuery(name="MLineTargetAlarm.findAll", query="SELECT m FROM MLineTargetAlarm m")
public class MLineTargetAlarm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MLineTargetAlarmPK id;

	@Column(name="continue_days", nullable=false, precision=2)
	private BigDecimal continueDays;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="line_over_rate", nullable=false, precision=4)
	private BigDecimal lineOverRate;

	@Column(name="over_rate_detect_flg", nullable=false)
	private Integer overRateDetectFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildingDm
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildingDm MBuildingDm;

	//bi-directional many-to-one association to MLine
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false, insertable=false, updatable=false)
		})
	private MLine MLine;

	//bi-directional many-to-one association to MLineTargetAlarmTime
	@OneToMany(mappedBy="MLineTargetAlarm")
	private List<MLineTargetAlarmTime> MLineTargetAlarmTimes;

	public MLineTargetAlarm() {
	}

	public MLineTargetAlarmPK getId() {
		return this.id;
	}

	public void setId(MLineTargetAlarmPK id) {
		this.id = id;
	}

	public BigDecimal getContinueDays() {
		return this.continueDays;
	}

	public void setContinueDays(BigDecimal continueDays) {
		this.continueDays = continueDays;
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

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public BigDecimal getLineOverRate() {
		return this.lineOverRate;
	}

	public void setLineOverRate(BigDecimal lineOverRate) {
		this.lineOverRate = lineOverRate;
	}

	public Integer getOverRateDetectFlg() {
		return this.overRateDetectFlg;
	}

	public void setOverRateDetectFlg(Integer overRateDetectFlg) {
		this.overRateDetectFlg = overRateDetectFlg;
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

	public MBuildingDm getMBuildingDm() {
		return this.MBuildingDm;
	}

	public void setMBuildingDm(MBuildingDm MBuildingDm) {
		this.MBuildingDm = MBuildingDm;
	}

	public MLine getMLine() {
		return this.MLine;
}

	public void setMLine(MLine MLine) {
		this.MLine = MLine;
	}

	public List<MLineTargetAlarmTime> getMLineTargetAlarmTimes() {
		return this.MLineTargetAlarmTimes;
	}

	public void setMLineTargetAlarmTimes(List<MLineTargetAlarmTime> MLineTargetAlarmTimes) {
		this.MLineTargetAlarmTimes = MLineTargetAlarmTimes;
	}

	public MLineTargetAlarmTime addMLineTargetAlarmTime(MLineTargetAlarmTime MLineTargetAlarmTime) {
		getMLineTargetAlarmTimes().add(MLineTargetAlarmTime);
		MLineTargetAlarmTime.setMLineTargetAlarm(this);

		return MLineTargetAlarmTime;
	}

	public MLineTargetAlarmTime removeMLineTargetAlarmTime(MLineTargetAlarmTime MLineTargetAlarmTime) {
		getMLineTargetAlarmTimes().remove(MLineTargetAlarmTime);
		MLineTargetAlarmTime.setMLineTargetAlarm(null);

		return MLineTargetAlarmTime;
	}

}