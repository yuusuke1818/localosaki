package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_auth database table.
 * 
 */
@Entity
@Table(name="m_auth")
@NamedQuery(name="MAuth.findAll", query="SELECT m FROM MAuth m")
public class MAuth implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="authority_cd", unique=true, nullable=false, length=5)
	private String authorityCd;

	@Column(name="authority_name", nullable=false, length=100)
	private String authorityName;

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

	//bi-directional many-to-one association to MCorpPersonAuth
	@OneToMany(mappedBy="MAuth")
	private List<MCorpPersonAuth> MCorpPersonAuths;

	public MAuth() {
	}

	public String getAuthorityCd() {
		return this.authorityCd;
	}

	public void setAuthorityCd(String authorityCd) {
		this.authorityCd = authorityCd;
	}

	public String getAuthorityName() {
		return this.authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
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

	public List<MCorpPersonAuth> getMCorpPersonAuths() {
		return this.MCorpPersonAuths;
	}

	public void setMCorpPersonAuths(List<MCorpPersonAuth> MCorpPersonAuths) {
		this.MCorpPersonAuths = MCorpPersonAuths;
	}

	public MCorpPersonAuth addMCorpPersonAuth(MCorpPersonAuth MCorpPersonAuth) {
		getMCorpPersonAuths().add(MCorpPersonAuth);
		MCorpPersonAuth.setMAuth(this);

		return MCorpPersonAuth;
	}

	public MCorpPersonAuth removeMCorpPersonAuth(MCorpPersonAuth MCorpPersonAuth) {
		getMCorpPersonAuths().remove(MCorpPersonAuth);
		MCorpPersonAuth.setMAuth(null);

		return MCorpPersonAuth;
	}

}