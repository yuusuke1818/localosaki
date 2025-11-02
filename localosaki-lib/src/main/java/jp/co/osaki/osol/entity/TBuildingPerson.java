package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the t_building_person database table.
 *
 */
@Entity
@Table(name = "t_building_person")
@NamedQueries({
    @NamedQuery(name = "TBuildingPerson.findAll", query = "SELECT t FROM TBuildingPerson t"),
    @NamedQuery(name = "TBuildingPerson.findCorpId", query = "SELECT t FROM TBuildingPerson t WHERE t.id.corpId =:corpId "),
    @NamedQuery(name = "TBuildingPerson.findCorpIdBuildingId", query = "SELECT t FROM TBuildingPerson t WHERE t.id.corpId =:corpId AND t.id.buildingId =:buildingId AND t.delFlg <>1"),
    @NamedQuery(name = "TBuildingPerson.findJoinPerson",
            query = "SELECT tbp FROM TBuildingPerson tbp INNER JOIN tbp.MPerson mp "
            + "WHERE mp.id.personId LIKE :personId "
            + "AND (:corpId IS NULL OR tbp.id.personCorpId LIKE :corpId) "
            + "AND (:personName IS NULL OR mp.personName LIKE :personName) "),
    @NamedQuery(name = "TBuildingPerson.findPersonBuilding", query = "SELECT t FROM TBuildingPerson t "
            + "WHERE (:corpId IS NULL OR t.id.corpId =:corpId) "
            + "AND t.id.personCorpId =:personCorpId AND t.id.personId =:personId ")
})
public class TBuildingPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildingPersonPK id;

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

	public TBuildingPerson() {
	}

	public TBuildingPersonPK getId() {
		return this.id;
	}

	public void setId(TBuildingPersonPK id) {
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