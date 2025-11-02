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
 * The persistent class for the m_corp_function_use database table.
 * 
 */
@Entity
@Table(name="m_corp_function_use")
@NamedQuery(name="MCorpFunctionUse.findAll", query="SELECT m FROM MCorpFunctionUse m")
public class MCorpFunctionUse implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCorpFunctionUsePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(name="use_flg", nullable=false)
	private Integer useFlg;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to MFunction
	@ManyToOne
	@JoinColumn(name="function_cd", nullable=false, insertable=false, updatable=false)
	private MFunction MFunction;

	public MCorpFunctionUse() {
	}

	public MCorpFunctionUsePK getId() {
		return this.id;
	}

	public void setId(MCorpFunctionUsePK id) {
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

	public Integer getUseFlg() {
		return this.useFlg;
	}

	public void setUseFlg(Integer useFlg) {
		this.useFlg = useFlg;
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

	public MFunction getMFunction() {
		return this.MFunction;
	}

	public void setMFunction(MFunction MFunction) {
		this.MFunction = MFunction;
	}

}