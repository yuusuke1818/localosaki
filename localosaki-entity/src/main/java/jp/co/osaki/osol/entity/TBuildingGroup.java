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
 * The persistent class for the t_building_group database table.
 * 
 */
@Entity
@Table(name = "t_building_group")
@NamedQueries({
    @NamedQuery(name = "TBuildingGroup.findAll", query = "SELECT t FROM TBuildingGroup t"),
    @NamedQuery(name = "TBuildingGroup.findExcel", query = "SELECT t FROM TBuildingGroup t WHERE t.id.corpId =:corpId ORDER BY t.id.corpId, t.id.buildingId, t.id.parentGroupId, t.id.childGroupId"),
    @NamedQuery(name = "TBuildingGroup.findJoinTBuilding", query
            = "SELECT t FROM TBuildingGroup t LEFT JOIN FETCH t.TBuilding "
            + " WHERE t.id.corpId =:corpId "),
    @NamedQuery(name = "TBuildingGroup.findJoinTBuildingParent", query
            = "SELECT t FROM TBuildingGroup t LEFT JOIN FETCH t.TBuilding "
            + " WHERE t.id.corpId =:corpId "
            + " AND t.id.parentGroupId =:parentGroupId "
            + " AND t.delFlg = 0 "
            + " ORDER BY t.id.corpId, t.id.buildingId, t.id.parentGroupId, t.id.childGroupId"),})
public class TBuildingGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TBuildingGroupPK id;

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

	//bi-directional many-to-one association to MChildGroup
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="child_group_id", referencedColumnName="child_group_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="parent_group_id", referencedColumnName="parent_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MChildGroup MChildGroup;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public TBuildingGroup() {
	}

	public TBuildingGroupPK getId() {
		return this.id;
	}

	public void setId(TBuildingGroupPK id) {
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

	public MChildGroup getMChildGroup() {
		return this.MChildGroup;
	}

	public void setMChildGroup(MChildGroup MChildGroup) {
		this.MChildGroup = MChildGroup;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}