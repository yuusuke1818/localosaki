package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_parent_group database table.
 * 
 */
@Entity
@Table(name = "m_parent_group")
@NamedQueries({
    @NamedQuery(name = "MParentGroup.findAll", query = "SELECT m FROM MParentGroup m"),
    @NamedQuery(name = "MParentGroup.findCorpId", query = "SELECT c FROM MParentGroup c WHERE c.id.corpId =:corpId ORDER BY c.id.parentGroupId"),
    @NamedQuery(name = "MParentGroup.findExcel",
            query = "SELECT DISTINCT c FROM MParentGroup c "
            + "LEFT JOIN FETCH c.MChildGroups mg "
            + "WHERE c.id.corpId =:corpId "
            + "AND c.delFlg = 0 "
            + "AND (mg.delFlg IS NULL OR mg.delFlg = 0) "
            + "ORDER BY c.displayOrder, mg.displayOrder"),
    @NamedQuery(name = "MParentGroup.findCorpIdDelFlgFetch",
            query = "SELECT DISTINCT c FROM MParentGroup c LEFT JOIN FETCH c.MChildGroups"
            + " WHERE c.id.corpId =:corpId "
            + " AND c.delFlg = 0"
            + " ORDER BY c.displayOrder, c.id.parentGroupId"),
    @NamedQuery(name = "MParentGroup.findExcelCorpId",
            query = "SELECT c FROM MParentGroup c LEFT JOIN c.MChildGroups cg"
            + " WHERE c.id.corpId =:corpId"
            + " AND c.delFlg = 0"
            + " AND cg.delFlg = 0"
            + " ORDER BY c.displayOrder, c.id.parentGroupId, cg.displayOrder, cg.id.childGroupId"),})
public class MParentGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MParentGroupPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="overlap_flg", nullable=false)
	private Integer overlapFlg;

	@Column(name="parent_group_name", nullable=false, length=100)
	private String parentGroupName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MChildGroup
	@OneToMany(mappedBy="MParentGroup")
	private List<MChildGroup> MChildGroups;

	public MParentGroup() {
	}

	public MParentGroupPK getId() {
		return this.id;
	}

	public void setId(MParentGroupPK id) {
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

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getOverlapFlg() {
		return this.overlapFlg;
	}

	public void setOverlapFlg(Integer overlapFlg) {
		this.overlapFlg = overlapFlg;
	}

	public String getParentGroupName() {
		return this.parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
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

	public List<MChildGroup> getMChildGroups() {
		return this.MChildGroups;
	}

	public void setMChildGroups(List<MChildGroup> MChildGroups) {
		this.MChildGroups = MChildGroups;
	}

	public MChildGroup addMChildGroup(MChildGroup MChildGroup) {
		getMChildGroups().add(MChildGroup);
		MChildGroup.setMParentGroup(this);

		return MChildGroup;
	}

	public MChildGroup removeMChildGroup(MChildGroup MChildGroup) {
		getMChildGroups().remove(MChildGroup);
		MChildGroup.setMParentGroup(null);

		return MChildGroup;
	}

}