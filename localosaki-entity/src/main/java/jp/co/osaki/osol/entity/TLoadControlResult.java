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
 * The persistent class for the t_load_control_result database table.
 * 
 */
@Entity
@Table(name="t_load_control_result")
@NamedQuery(name="TLoadControlResult.findAll", query="SELECT t FROM TLoadControlResult t")
public class TLoadControlResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TLoadControlResultPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="daily_total_minute", precision=4)
	private BigDecimal dailyTotalMinute;

	@Column(name="measurement_ymdhm", length=12)
	private String measurementYmdhm;

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

	public TLoadControlResult() {
	}

	public TLoadControlResultPK getId() {
		return this.id;
	}

	public void setId(TLoadControlResultPK id) {
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

	public BigDecimal getDailyTotalMinute() {
		return this.dailyTotalMinute;
	}

	public void setDailyTotalMinute(BigDecimal dailyTotalMinute) {
		this.dailyTotalMinute = dailyTotalMinute;
	}

	public String getMeasurementYmdhm() {
		return this.measurementYmdhm;
	}

	public void setMeasurementYmdhm(String measurementYmdhm) {
		this.measurementYmdhm = measurementYmdhm;
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