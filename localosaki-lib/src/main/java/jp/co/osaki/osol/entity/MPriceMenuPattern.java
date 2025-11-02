package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_price_menu_pattern database table.
 * 
 */
@Entity
@Table(name="m_price_menu_pattern")
@NamedQuery(name="MPriceMenuPattern.findAll", query="SELECT m FROM MPriceMenuPattern m")
public class MPriceMenuPattern implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MPriceMenuPatternPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="end_time", length=2)
	private String endTime;

	@Column(name="other_flg", precision=1)
	private BigDecimal otherFlg;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="start_time", length=2)
	private String startTime;

	@Column(name="time_name", length=40)
	private String timeName;

	@Column(name="time_price", precision=6, scale=2)
	private BigDecimal timePrice;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MPriceMenu
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="menu_no", referencedColumnName="menu_no", nullable=false, insertable=false, updatable=false)
		})
	private MPriceMenu MPriceMenu;

	public MPriceMenuPattern() {
	}

	public MPriceMenuPatternPK getId() {
		return this.id;
	}

	public void setId(MPriceMenuPatternPK id) {
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

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getOtherFlg() {
		return this.otherFlg;
	}

	public void setOtherFlg(BigDecimal otherFlg) {
		this.otherFlg = otherFlg;
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

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTimeName() {
		return this.timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public BigDecimal getTimePrice() {
		return this.timePrice;
	}

	public void setTimePrice(BigDecimal timePrice) {
		this.timePrice = timePrice;
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

	public MPriceMenu getMPriceMenu() {
		return this.MPriceMenu;
	}

	public void setMPriceMenu(MPriceMenu MPriceMenu) {
		this.MPriceMenu = MPriceMenu;
	}

}