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
 * The persistent class for the t_event_control_log database table.
 * 
 */
@Entity
@Table(name="t_event_control_log")
@NamedQuery(name="TEventControlLog.findAll", query="SELECT t FROM TEventControlLog t")
public class TEventControlLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TEventControlLogPK id;

	@Column(name="control_event1_kind", length=1)
	private String controlEvent1Kind;

	@Column(name="control_event1_val", precision=5)
	private BigDecimal controlEvent1Val;

	@Column(name="control_event2_kind", length=1)
	private String controlEvent2Kind;

	@Column(name="control_event2_val", precision=5)
	private BigDecimal controlEvent2Val;

	@Column(name="control_event3_kind", length=1)
	private String controlEvent3Kind;

	@Column(name="control_event3_val", precision=5)
	private BigDecimal controlEvent3Val;

	@Column(name="control_status", length=1)
	private String controlStatus;

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

	public TEventControlLog() {
	}

	public TEventControlLogPK getId() {
		return this.id;
	}

	public void setId(TEventControlLogPK id) {
		this.id = id;
	}

	public String getControlEvent1Kind() {
		return this.controlEvent1Kind;
	}

	public void setControlEvent1Kind(String controlEvent1Kind) {
		this.controlEvent1Kind = controlEvent1Kind;
	}

	public BigDecimal getControlEvent1Val() {
		return this.controlEvent1Val;
	}

	public void setControlEvent1Val(BigDecimal controlEvent1Val) {
		this.controlEvent1Val = controlEvent1Val;
	}

	public String getControlEvent2Kind() {
		return this.controlEvent2Kind;
	}

	public void setControlEvent2Kind(String controlEvent2Kind) {
		this.controlEvent2Kind = controlEvent2Kind;
	}

	public BigDecimal getControlEvent2Val() {
		return this.controlEvent2Val;
	}

	public void setControlEvent2Val(BigDecimal controlEvent2Val) {
		this.controlEvent2Val = controlEvent2Val;
	}

	public String getControlEvent3Kind() {
		return this.controlEvent3Kind;
	}

	public void setControlEvent3Kind(String controlEvent3Kind) {
		this.controlEvent3Kind = controlEvent3Kind;
	}

	public BigDecimal getControlEvent3Val() {
		return this.controlEvent3Val;
	}

	public void setControlEvent3Val(BigDecimal controlEvent3Val) {
		this.controlEvent3Val = controlEvent3Val;
	}

	public String getControlStatus() {
		return this.controlStatus;
	}

	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
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