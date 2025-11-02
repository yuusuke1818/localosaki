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
 * The persistent class for the m_auto_insp database table.
 * 
 */
@Entity
@Table(name="m_auto_insp")
@NamedQuery(name="MAutoInsp.findAll", query="SELECT m FROM MAutoInsp m")
public class MAutoInsp implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MAutoInspPK id;

	@Column(name="command_flg", length=1)
	private String commandFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(length=2)
	private String day;

	@Column(precision=2)
	private BigDecimal hour;

	@Column(length=12)
	private String month;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="srv_ent", length=1)
	private String srvEnt;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="wait_time", precision=2)
	private BigDecimal waitTime;

	//bi-directional many-to-one association to MDevPrm
	@ManyToOne
	@JoinColumn(name="dev_id", nullable=false, insertable=false, updatable=false)
	private MDevPrm MDevPrm;

	public MAutoInsp() {
	}

	public MAutoInspPK getId() {
		return this.id;
	}

	public void setId(MAutoInspPK id) {
		this.id = id;
	}

	public String getCommandFlg() {
		return this.commandFlg;
	}

	public void setCommandFlg(String commandFlg) {
		this.commandFlg = commandFlg;
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

	public String getDay() {
		return this.day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public BigDecimal getHour() {
		return this.hour;
	}

	public void setHour(BigDecimal hour) {
		this.hour = hour;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Timestamp getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Timestamp recDate) {
		this.recDate = recDate;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
	}

	public String getSrvEnt() {
		return this.srvEnt;
	}

	public void setSrvEnt(String srvEnt) {
		this.srvEnt = srvEnt;
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

	public BigDecimal getWaitTime() {
		return this.waitTime;
	}

	public void setWaitTime(BigDecimal waitTime) {
		this.waitTime = waitTime;
	}

	public MDevPrm getMDevPrm() {
		return this.MDevPrm;
	}

	public void setMDevPrm(MDevPrm MDevPrm) {
		this.MDevPrm = MDevPrm;
	}

}