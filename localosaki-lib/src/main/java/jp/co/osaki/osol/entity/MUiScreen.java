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
 * The persistent class for the m_ui_screen database table.
 * 
 */
@Entity
@Table(name="m_ui_screen")
@NamedQuery(name="MUiScreen.findAll", query="SELECT m FROM MUiScreen m")
public class MUiScreen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ui_screen_id", unique=true, nullable=false, length=10)
	private String uiScreenId;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="display_order", nullable=false)
	private Integer displayOrder;

	@Column(name="ui_screen_bean", nullable=false, length=50)
	private String uiScreenBean;

	@Column(name="ui_screen_name", nullable=false, length=50)
	private String uiScreenName;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MPerson
	@OneToMany(mappedBy="MUiScreen")
	private List<MPerson> MPersons;

	public MUiScreen() {
	}

	public String getUiScreenId() {
		return this.uiScreenId;
	}

	public void setUiScreenId(String uiScreenId) {
		this.uiScreenId = uiScreenId;
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

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getUiScreenBean() {
		return this.uiScreenBean;
	}

	public void setUiScreenBean(String uiScreenBean) {
		this.uiScreenBean = uiScreenBean;
	}

	public String getUiScreenName() {
		return this.uiScreenName;
	}

	public void setUiScreenName(String uiScreenName) {
		this.uiScreenName = uiScreenName;
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

	public List<MPerson> getMPersons() {
		return this.MPersons;
	}

	public void setMPersons(List<MPerson> MPersons) {
		this.MPersons = MPersons;
	}

	public MPerson addMPerson(MPerson MPerson) {
		getMPersons().add(MPerson);
		MPerson.setMUiScreen(this);

		return MPerson;
	}

	public MPerson removeMPerson(MPerson MPerson) {
		getMPersons().remove(MPerson);
		MPerson.setMUiScreen(null);

		return MPerson;
	}

}