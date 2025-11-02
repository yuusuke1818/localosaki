package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_corp_person database table.
 * 
 */
@Entity
@Table(name = "m_corp_person")
@NamedQueries({
    @NamedQuery(name = "MCorpPerson.findAll", query = "SELECT m FROM MCorpPerson m"),
    @NamedQuery(name = "MCorpPerson.findJoin",
            query = "SELECT mcp FROM MCorpPerson mcp INNER JOIN  mcp.MPerson  "
            //            + "WHERE (mcp.id.personId =:personId or mcp.id.personId =:selectPersonId)"
            + "WHERE (mcp.id.personId LIKE :personId) "
            + "AND (mcp.id.personCorpId LIKE :corpId) "
            + "AND (:personName IS NULL or mcp.MPerson.personName LIKE :personName) "
            + "AND (:personCorpFLG IS NULL or mcp.authorityType IN :personCorpType) "
            + "ORDER BY mcp.id.corpId ASC "
    ),
    @NamedQuery(name = "MCorpPerson.findCorpId",
            query = "SELECT mcp FROM MCorpPerson mcp "
            + "WHERE mcp.id.corpId =:corpId "
            + "AND mcp.delFlg <> 1 "
    ),
    @NamedQuery(name = "MCorpPerson.search",
            query = "SELECT mcp FROM MCorpPerson mcp "
            + "WHERE (mcp.id.personId LIKE :personId) "
            + "AND (mcp.id.personCorpId LIKE :personCorpId) "
            + "AND (mcp.delFlg = 0 )"
    )
})
public class MCorpPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MCorpPersonPK id;

	@Column(name="authority_type", nullable=false, length=6)
	private String authorityType;

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

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to MPerson
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="person_corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="person_id", referencedColumnName="person_id", nullable=false, insertable=false, updatable=false)
		})
	private MPerson MPerson;

	//bi-directional many-to-one association to MCorpPersonAuth
	@OneToMany(mappedBy="MCorpPerson")
	private List<MCorpPersonAuth> MCorpPersonAuths;

	public MCorpPerson() {
	}

	public MCorpPersonPK getId() {
		return this.id;
	}

	public void setId(MCorpPersonPK id) {
		this.id = id;
	}

	public String getAuthorityType() {
		return this.authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
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

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

	public MPerson getMPerson() {
		return this.MPerson;
	}

	public void setMPerson(MPerson MPerson) {
		this.MPerson = MPerson;
	}

	public List<MCorpPersonAuth> getMCorpPersonAuths() {
		return this.MCorpPersonAuths;
	}

	public void setMCorpPersonAuths(List<MCorpPersonAuth> MCorpPersonAuths) {
		this.MCorpPersonAuths = MCorpPersonAuths;
	}

	public MCorpPersonAuth addMCorpPersonAuth(MCorpPersonAuth MCorpPersonAuth) {
		getMCorpPersonAuths().add(MCorpPersonAuth);
		MCorpPersonAuth.setMCorpPerson(this);

		return MCorpPersonAuth;
	}

	public MCorpPersonAuth removeMCorpPersonAuth(MCorpPersonAuth MCorpPersonAuth) {
		getMCorpPersonAuths().remove(MCorpPersonAuth);
		MCorpPersonAuth.setMCorpPerson(null);

		return MCorpPersonAuth;
	}

}