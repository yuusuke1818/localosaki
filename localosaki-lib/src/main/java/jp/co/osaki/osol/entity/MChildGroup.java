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
 * The persistent class for the m_child_group database table.
 * 
 */
@Entity
@Table(name = "m_child_group")
@NamedQueries({
    @NamedQuery(name = "MChildGroup.findAll", query = "SELECT m FROM MChildGroup m"),
    @NamedQuery(name = "MChildGroup.findGroupIdGroupName", 
            query = "SELECT m FROM MChildGroup m "
                    + " WHERE m.id.corpId =:corpId "
                    + " AND m.id.parentGroupId =:parentGroupId "
                    + " AND (:delFlg IS NULL OR m.delFlg =:delFlg) "
                    + " ORDER BY m.displayOrder, m.id.childGroupId"),
//    @NamedQuery(name = "MChildGroup.findGroupIdGroupName", query = "SELECT m FROM MChildGroup m INNER JOIN m.MParentGroup p WHERE m.id.corpId =:corpId AND p.id.corpId =:corpId ORDER BY p.displayOrder, m.displayOrder"),
    @NamedQuery(name = "MChildGroup.findCorpIdGroupName", 
            query = "SELECT m FROM MChildGroup m INNER JOIN m.MParentGroup p "
                    + " WHERE m.id.corpId =:corpId "
                    + " AND p.id.corpId =:corpId "
                    + " AND m.delFlg = 0"
                    + " AND p.delFlg = 0"
                    + " ORDER BY p.displayOrder, m.displayOrder"),})
public class MChildGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MChildGroupPK id;

	@Column(name="child_group_name", nullable=false, length=100)
	private String childGroupName;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MParentGroup
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="parent_group_id", referencedColumnName="parent_group_id", nullable=false, insertable=false, updatable=false)
		})
	private MParentGroup MParentGroup;

	//bi-directional many-to-one association to TBuildingGroup
	@OneToMany(mappedBy="MChildGroup")
	private List<TBuildingGroup> TBuildingGroups;

	public MChildGroup() {
	}

	public MChildGroupPK getId() {
		return this.id;
	}

	public void setId(MChildGroupPK id) {
		this.id = id;
	}

	public String getChildGroupName() {
		return this.childGroupName;
	}

	public void setChildGroupName(String childGroupName) {
		this.childGroupName = childGroupName;
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

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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

	public MParentGroup getMParentGroup() {
		return this.MParentGroup;
	}

	public void setMParentGroup(MParentGroup MParentGroup) {
		this.MParentGroup = MParentGroup;
	}

	public List<TBuildingGroup> getTBuildingGroups() {
		return this.TBuildingGroups;
	}

	public void setTBuildingGroups(List<TBuildingGroup> TBuildingGroups) {
		this.TBuildingGroups = TBuildingGroups;
	}

	public TBuildingGroup addTBuildingGroup(TBuildingGroup TBuildingGroup) {
		getTBuildingGroups().add(TBuildingGroup);
		TBuildingGroup.setMChildGroup(this);

		return TBuildingGroup;
	}

	public TBuildingGroup removeTBuildingGroup(TBuildingGroup TBuildingGroup) {
		getTBuildingGroups().remove(TBuildingGroup);
		TBuildingGroup.setMChildGroup(null);

		return TBuildingGroup;
	}

}