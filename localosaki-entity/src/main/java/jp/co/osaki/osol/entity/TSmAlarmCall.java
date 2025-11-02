package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_sm_alarm_call database table.
 * 
 */
@Entity
@Table(name="t_sm_alarm_call")
@NamedQuery(name="TSmAlarmCall.findAll", query="SELECT t FROM TSmAlarmCall t")
public class TSmAlarmCall implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sm_id", unique=true, nullable=false)
	private Long smId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="sm_alarm_call_date", nullable=false)
	private Timestamp smAlarmCallDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional one-to-one association to MSmPrm
	@OneToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public TSmAlarmCall() {
	}

	public Long getSmId() {
		return this.smId;
	}

	public void setSmId(Long smId) {
		this.smId = smId;
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

	public Timestamp getSmAlarmCallDate() {
		return this.smAlarmCallDate;
	}

	public void setSmAlarmCallDate(Timestamp smAlarmCallDate) {
		this.smAlarmCallDate = smAlarmCallDate;
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