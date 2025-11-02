package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_command database table.
 * 
 */
@Entity
@Table(name="t_command")
@NamedQuery(name="TCommand.findAll", query="SELECT t FROM TCommand t")
public class TCommand implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TCommandPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(length=100)
	private String linking;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="retry_count", precision=3)
	private BigDecimal retryCount;

	@Column(name="srv_ent", length=1)
	private String srvEnt;

	@Column(length=100)
	private String tag;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	public TCommand() {
	}

	public TCommandPK getId() {
		return this.id;
	}

	public void setId(TCommandPK id) {
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

	public String getLinking() {
		return this.linking;
	}

	public void setLinking(String linking) {
		this.linking = linking;
	}

	public String getRecMan() {
		return this.recMan;
	}

	public void setRecMan(String recMan) {
		this.recMan = recMan;
	}

	public BigDecimal getRetryCount() {
		return this.retryCount;
	}

	public void setRetryCount(BigDecimal retryCount) {
		this.retryCount = retryCount;
	}

	public String getSrvEnt() {
		return this.srvEnt;
	}

	public void setSrvEnt(String srvEnt) {
		this.srvEnt = srvEnt;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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

}