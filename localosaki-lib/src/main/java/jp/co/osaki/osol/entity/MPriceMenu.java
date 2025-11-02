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
 * The persistent class for the m_price_menu database table.
 * 
 */
@Entity
@Table(name="m_price_menu")
@NamedQuery(name="MPriceMenu.findAll", query="SELECT m FROM MPriceMenu m")
public class MPriceMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MPriceMenuPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="menu_name", length=40)
	private String menuName;

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

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	//bi-directional many-to-one association to MPriceMenuPattern
	@OneToMany(mappedBy="MPriceMenu")
	private List<MPriceMenuPattern> MPriceMenuPatterns;

	public MPriceMenu() {
	}

	public MPriceMenuPK getId() {
		return this.id;
	}

	public void setId(MPriceMenuPK id) {
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

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
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

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

	public List<MPriceMenuPattern> getMPriceMenuPatterns() {
		return this.MPriceMenuPatterns;
	}

	public void setMPriceMenuPatterns(List<MPriceMenuPattern> MPriceMenuPatterns) {
		this.MPriceMenuPatterns = MPriceMenuPatterns;
	}

	public MPriceMenuPattern addMPriceMenuPattern(MPriceMenuPattern MPriceMenuPattern) {
		getMPriceMenuPatterns().add(MPriceMenuPattern);
		MPriceMenuPattern.setMPriceMenu(this);

		return MPriceMenuPattern;
	}

	public MPriceMenuPattern removeMPriceMenuPattern(MPriceMenuPattern MPriceMenuPattern) {
		getMPriceMenuPatterns().remove(MPriceMenuPattern);
		MPriceMenuPattern.setMPriceMenu(null);

		return MPriceMenuPattern;
	}

}