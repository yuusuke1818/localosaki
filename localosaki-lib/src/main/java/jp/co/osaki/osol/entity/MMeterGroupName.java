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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_meter_group_name database table.
 * 
 */
@Entity
@Table(name="m_meter_group_name")
@NamedQuery(name="MMeterGroupName.findAll", query="SELECT m FROM MMeterGroupName m")
public class MMeterGroupName implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMeterGroupNamePK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="meter_group_name", nullable=false, length=10)
	private String meterGroupName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MGroupPrice
	@OneToMany(mappedBy="MMeterGroupName")
	private List<MGroupPrice> MGroupPrices;

	//bi-directional many-to-one association to MMeterGroup
	@OneToMany(mappedBy="MMeterGroupName")
	private List<MMeterGroup> MMeterGroups;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MMeterGroupName() {
	}

	public MMeterGroupNamePK getId() {
		return this.id;
	}

	public void setId(MMeterGroupNamePK id) {
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

	public String getMeterGroupName() {
		return this.meterGroupName;
	}

	public void setMeterGroupName(String meterGroupName) {
		this.meterGroupName = meterGroupName;
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

	public List<MGroupPrice> getMGroupPrices() {
		return this.MGroupPrices;
	}

	public void setMGroupPrices(List<MGroupPrice> MGroupPrices) {
		this.MGroupPrices = MGroupPrices;
	}

	public MGroupPrice addMGroupPrice(MGroupPrice MGroupPrice) {
		getMGroupPrices().add(MGroupPrice);
		MGroupPrice.setMMeterGroupName(this);

		return MGroupPrice;
	}

	public MGroupPrice removeMGroupPrice(MGroupPrice MGroupPrice) {
		getMGroupPrices().remove(MGroupPrice);
		MGroupPrice.setMMeterGroupName(null);

		return MGroupPrice;
	}

	public List<MMeterGroup> getMMeterGroups() {
		return this.MMeterGroups;
	}

	public void setMMeterGroups(List<MMeterGroup> MMeterGroups) {
		this.MMeterGroups = MMeterGroups;
	}

	public MMeterGroup addMMeterGroup(MMeterGroup MMeterGroup) {
		getMMeterGroups().add(MMeterGroup);
		MMeterGroup.setMMeterGroupName(this);

		return MMeterGroup;
	}

	public MMeterGroup removeMMeterGroup(MMeterGroup MMeterGroup) {
		getMMeterGroups().remove(MMeterGroup);
		MMeterGroup.setMMeterGroupName(null);

		return MMeterGroup;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}