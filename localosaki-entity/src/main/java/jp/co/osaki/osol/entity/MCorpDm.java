package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_corp_dm database table.
 * 
 */
@Entity
@Table(name="m_corp_dm")
@NamedQuery(name="MCorpDm.findAll", query="SELECT m FROM MCorpDm m")
public class MCorpDm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="corp_id", unique=true, nullable=false, length=50)
	private String corpId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="sum_date", nullable=false, length=6)
	private String sumDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="week_closing_day_of_week", nullable=false)
	private Integer weekClosingDayOfWeek;

	@Column(name="week_start_day", nullable=false, length=4)
	private String weekStartDay;

	//bi-directional one-to-one association to MCorp
	@OneToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional one-to-one association to MCorpTargetAlarm
	@OneToOne(mappedBy="MCorpDm")
	private MCorpTargetAlarm MCorpTargetAlarm;

	//bi-directional many-to-one association to MLineGroup
	@OneToMany(mappedBy="MCorpDm")
	private List<MLineGroup> MLineGroups;

	public MCorpDm() {
	}

	public String getCorpId() {
		return this.corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
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

	public String getSumDate() {
		return this.sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
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

	public Integer getWeekClosingDayOfWeek() {
		return this.weekClosingDayOfWeek;
	}

	public void setWeekClosingDayOfWeek(Integer weekClosingDayOfWeek) {
		this.weekClosingDayOfWeek = weekClosingDayOfWeek;
	}

	public String getWeekStartDay() {
		return this.weekStartDay;
	}

	public void setWeekStartDay(String weekStartDay) {
		this.weekStartDay = weekStartDay;
	}

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

	public MCorpTargetAlarm getMCorpTargetAlarm() {
		return this.MCorpTargetAlarm;
	}

	public void setMCorpTargetAlarm(MCorpTargetAlarm MCorpTargetAlarm) {
		this.MCorpTargetAlarm = MCorpTargetAlarm;
	}

	public List<MLineGroup> getMLineGroups() {
		return this.MLineGroups;
	}

	public void setMLineGroups(List<MLineGroup> MLineGroups) {
		this.MLineGroups = MLineGroups;
	}

	public MLineGroup addMLineGroup(MLineGroup MLineGroup) {
		getMLineGroups().add(MLineGroup);
		MLineGroup.setMCorpDm(this);

		return MLineGroup;
	}

	public MLineGroup removeMLineGroup(MLineGroup MLineGroup) {
		getMLineGroups().remove(MLineGroup);
		MLineGroup.setMCorpDm(null);

		return MLineGroup;
	}

}