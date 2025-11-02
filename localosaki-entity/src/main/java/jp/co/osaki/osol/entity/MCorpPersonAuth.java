package jp.co.osaki.osol.entity;

import java.io.Serializable;
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
 * The persistent class for the m_corp_person_auth database table.
 * 
 */
@Entity
@Table(name="m_corp_person_auth")
@NamedQuery(name="MCorpPersonAuth.findAll", query="SELECT m FROM MCorpPersonAuth m")
public class MCorpPersonAuth implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCorpPersonAuthPK id;

	@Column(name="authority_flg", nullable=false)
	private Integer authorityFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MAuth
	@ManyToOne
	@JoinColumn(name="authority_cd", nullable=false, insertable=false, updatable=false)
	private MAuth MAuth;

	//bi-directional many-to-one association to MCorpPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="person_corp_id", referencedColumnName="person_corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="person_id", referencedColumnName="person_id", nullable=false, insertable=false, updatable=false)
		})
	private MCorpPerson MCorpPerson;

	public MCorpPersonAuth() {
	}

	public MCorpPersonAuthPK getId() {
		return this.id;
	}

	public void setId(MCorpPersonAuthPK id) {
		this.id = id;
	}

	public Integer getAuthorityFlg() {
		return this.authorityFlg;
	}

	public void setAuthorityFlg(Integer authorityFlg) {
		this.authorityFlg = authorityFlg;
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

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
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

	public MAuth getMAuth() {
		return this.MAuth;
	}

	public void setMAuth(MAuth MAuth) {
		this.MAuth = MAuth;
	}

	public MCorpPerson getMCorpPerson() {
		return this.MCorpPerson;
	}

	public void setMCorpPerson(MCorpPerson MCorpPerson) {
		this.MCorpPerson = MCorpPerson;
	}

}