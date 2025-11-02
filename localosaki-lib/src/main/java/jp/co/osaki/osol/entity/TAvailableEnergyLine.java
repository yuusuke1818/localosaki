package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Time;
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
 * The persistent class for the t_available_energy_line database table.
 * 
 */
@Entity
@Table(name="t_available_energy_line")
@NamedQuery(name="TAvailableEnergyLine.findAll", query="SELECT t FROM TAvailableEnergyLine t")
public class TAvailableEnergyLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TAvailableEnergyLinePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="end_time")
	private Time endTime;

	@Column(name="start_time")
	private Time startTime;

	@Column(name="summary_unit", nullable=false, length=6)
	private String summaryUnit;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MLine
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="line_corp_id", referencedColumnName="corp_id", nullable=false),
		@JoinColumn(name="line_group_id", referencedColumnName="line_group_id", nullable=false),
		@JoinColumn(name="line_no", referencedColumnName="line_no", nullable=false)
		})
	private MLine MLine;

	//bi-directional many-to-one association to TAvailableEnergy
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="contract_id", referencedColumnName="contract_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="eng_id", referencedColumnName="eng_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="eng_type_cd", referencedColumnName="eng_type_cd", nullable=false, insertable=false, updatable=false)
		})
	private TAvailableEnergy TAvailableEnergy;

	public TAvailableEnergyLine() {
	}

	public TAvailableEnergyLinePK getId() {
		return this.id;
	}

	public void setId(TAvailableEnergyLinePK id) {
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

	public Time getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public String getSummaryUnit() {
		return this.summaryUnit;
	}

	public void setSummaryUnit(String summaryUnit) {
		this.summaryUnit = summaryUnit;
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

	public MLine getMLine() {
		return this.MLine;
	}

	public void setMLine(MLine MLine) {
		this.MLine = MLine;
	}

	public TAvailableEnergy getTAvailableEnergy() {
		return this.TAvailableEnergy;
	}

	public void setTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		this.TAvailableEnergy = TAvailableEnergy;
	}

}