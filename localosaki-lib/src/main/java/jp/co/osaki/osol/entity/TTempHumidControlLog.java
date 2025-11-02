package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the t_temp_humid_control_log database table.
 * 
 */
@Entity
@Table(name="t_temp_humid_control_log")
@NamedQuery(name="TTempHumidControlLog.findAll", query="SELECT t FROM TTempHumidControlLog t")
public class TTempHumidControlLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TTempHumidControlLogPK id;

	@Column(name="control_alarm_status", length=2)
	private String controlAlarmStatus;

	@Column(name="control_humid", precision=4, scale=1)
	private BigDecimal controlHumid;

	@Column(name="control_temp", precision=4, scale=1)
	private BigDecimal controlTemp;

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

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public TTempHumidControlLog() {
	}

	public TTempHumidControlLogPK getId() {
		return this.id;
	}

	public void setId(TTempHumidControlLogPK id) {
		this.id = id;
	}

	public String getControlAlarmStatus() {
		return this.controlAlarmStatus;
	}

	public void setControlAlarmStatus(String controlAlarmStatus) {
		this.controlAlarmStatus = controlAlarmStatus;
	}

	public BigDecimal getControlHumid() {
		return this.controlHumid;
	}

	public void setControlHumid(BigDecimal controlHumid) {
		this.controlHumid = controlHumid;
	}

	public BigDecimal getControlTemp() {
		return this.controlTemp;
	}

	public void setControlTemp(BigDecimal controlTemp) {
		this.controlTemp = controlTemp;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

}