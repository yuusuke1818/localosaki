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
 * The persistent class for the m_claimant_info database table.
 * 
 */
@Entity
@Table(name="m_claimant_info")
@NamedQuery(name="MClaimantInfo.findAll", query="SELECT m FROM MClaimantInfo m")
public class MClaimantInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MClaimantInfoPK id;

	@Column(name="claimant_name1", length=30)
	private String claimantName1;

	@Column(name="claimant_name2", length=30)
	private String claimantName2;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="person_corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="person_id", referencedColumnName="person_id", nullable=false, insertable=false, updatable=false)
		})
	private MPerson MPerson;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MClaimantInfo() {
	}

	public MClaimantInfoPK getId() {
		return this.id;
	}

	public void setId(MClaimantInfoPK id) {
		this.id = id;
	}

	public String getClaimantName1() {
		return this.claimantName1;
	}

	public void setClaimantName1(String claimantName1) {
		this.claimantName1 = claimantName1;
	}

	public String getClaimantName2() {
		return this.claimantName2;
	}

	public void setClaimantName2(String claimantName2) {
		this.claimantName2 = claimantName2;
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

	public MPerson getMPerson() {
		return this.MPerson;
	}

	public void setMPerson(MPerson MPerson) {
		this.MPerson = MPerson;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}