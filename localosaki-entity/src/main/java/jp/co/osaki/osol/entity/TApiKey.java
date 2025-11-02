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
 * The persistent class for the t_api_key database table.
 * 
 */
@Entity
@Table(name="t_api_key")
@NamedQuery(name="TApiKey.findAll", query="SELECT t FROM TApiKey t")
public class TApiKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TApiKeyPK id;

	@Column(name="api_key", nullable=false, length=64)
	private String apiKey;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="expiration_date", nullable=false)
	private Timestamp expirationDate;

	@Column(name="issued_date", nullable=false)
	private Timestamp issuedDate;

	@Column(name="refresh_key", length=64)
	private String refreshKey;

	@Column(name="refresh_key_expiration_date")
	private Timestamp refreshKeyExpirationDate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(name="validity_period_min", nullable=false)
	private Integer validityPeriodMin;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="person_id", referencedColumnName="person_id", nullable=false, insertable=false, updatable=false)
		})
	private MPerson MPerson;

	public TApiKey() {
	}

	public TApiKeyPK getId() {
		return this.id;
	}

	public void setId(TApiKeyPK id) {
		this.id = id;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

	public Timestamp getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Timestamp getIssuedDate() {
		return this.issuedDate;
	}

	public void setIssuedDate(Timestamp issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getRefreshKey() {
		return this.refreshKey;
	}

	public void setRefreshKey(String refreshKey) {
		this.refreshKey = refreshKey;
	}

	public Timestamp getRefreshKeyExpirationDate() {
		return this.refreshKeyExpirationDate;
	}

	public void setRefreshKeyExpirationDate(Timestamp refreshKeyExpirationDate) {
		this.refreshKeyExpirationDate = refreshKeyExpirationDate;
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

	public Integer getValidityPeriodMin() {
		return this.validityPeriodMin;
	}

	public void setValidityPeriodMin(Integer validityPeriodMin) {
		this.validityPeriodMin = validityPeriodMin;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public MPerson getMPerson() {
		return this.MPerson;
	}

	public void setMPerson(MPerson MPerson) {
		this.MPerson = MPerson;
	}

}