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
 * The persistent class for the t_load_control_log database table.
 * 
 */
@Entity
@Table(name="t_load_control_log")
@NamedQuery(name="TLoadControlLog.findAll", query="SELECT t FROM TLoadControlLog t")
public class TLoadControlLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TLoadControlLogPK id;

	@Column(name="adjust_kw", precision=10, scale=1)
	private BigDecimal adjustKw;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="now_kw", precision=10, scale=1)
	private BigDecimal nowKw;

	@Column(name="target_kw", precision=10, scale=1)
	private BigDecimal targetKw;

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

	public TLoadControlLog() {
	}

	public TLoadControlLogPK getId() {
		return this.id;
	}

	public void setId(TLoadControlLogPK id) {
		this.id = id;
	}

	public BigDecimal getAdjustKw() {
		return this.adjustKw;
	}

	public void setAdjustKw(BigDecimal adjustKw) {
		this.adjustKw = adjustKw;
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

	public BigDecimal getNowKw() {
		return this.nowKw;
	}

	public void setNowKw(BigDecimal nowKw) {
		this.nowKw = nowKw;
	}

	public BigDecimal getTargetKw() {
		return this.targetKw;
	}

	public void setTargetKw(BigDecimal targetKw) {
		this.targetKw = targetKw;
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