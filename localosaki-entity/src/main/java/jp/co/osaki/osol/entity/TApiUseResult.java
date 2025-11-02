package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the t_api_use_result database table.
 * 
 */
@Entity
@Table(name="t_api_use_result")
@NamedQuery(name="TApiUseResult.findAll", query="SELECT t FROM TApiUseResult t")
public class TApiUseResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TApiUseResultPK id;

	@Column(name="api_count", nullable=false)
	private Integer apiCount;

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

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	public TApiUseResult() {
	}

	public TApiUseResultPK getId() {
		return this.id;
	}

	public void setId(TApiUseResultPK id) {
		this.id = id;
	}

	public Integer getApiCount() {
		return this.apiCount;
	}

	public void setApiCount(Integer apiCount) {
		this.apiCount = apiCount;
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

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

}