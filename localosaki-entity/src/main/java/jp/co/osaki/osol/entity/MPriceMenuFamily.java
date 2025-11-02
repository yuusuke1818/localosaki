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
 * The persistent class for the m_price_menu_family database table.
 * 
 */
@Entity
@Table(name="m_price_menu_family")
@NamedQuery(name="MPriceMenuFamily.findAll", query="SELECT m FROM MPriceMenuFamily m")
public class MPriceMenuFamily implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MPriceMenuFamilyPK id;

	@Column(name="basic_price", precision=6, scale=2)
	private BigDecimal basicPrice;

	@Column(name="basic_price_over", precision=6, scale=2)
	private BigDecimal basicPriceOver;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="discount_rate", precision=3)
	private BigDecimal discountRate;

	@Column(name="ener_equip_disc_price", precision=6, scale=2)
	private BigDecimal enerEquipDiscPrice;

	@Column(name="fuel_adjust_price", precision=6, scale=2)
	private BigDecimal fuelAdjustPrice;

	@Column(name="lowest_month_price", precision=6, scale=2)
	private BigDecimal lowestMonthPrice;

	@Column(name="micom_disc_price", precision=6, scale=2)
	private BigDecimal micomDiscPrice;

	@Column(name="power_price_day_other", precision=6, scale=2)
	private BigDecimal powerPriceDayOther;

	@Column(name="power_price_day_summer", precision=6, scale=2)
	private BigDecimal powerPriceDaySummer;

	@Column(name="power_price_family", precision=6, scale=2)
	private BigDecimal powerPriceFamily;

	@Column(name="power_price_night", precision=6, scale=2)
	private BigDecimal powerPriceNight;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="renew_ener_price", precision=6, scale=2)
	private BigDecimal renewEnerPrice;

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

	public MPriceMenuFamily() {
	}

	public MPriceMenuFamilyPK getId() {
		return this.id;
	}

	public void setId(MPriceMenuFamilyPK id) {
		this.id = id;
	}

	public BigDecimal getBasicPrice() {
		return this.basicPrice;
	}

	public void setBasicPrice(BigDecimal basicPrice) {
		this.basicPrice = basicPrice;
	}

	public BigDecimal getBasicPriceOver() {
		return this.basicPriceOver;
	}

	public void setBasicPriceOver(BigDecimal basicPriceOver) {
		this.basicPriceOver = basicPriceOver;
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

	public BigDecimal getDiscountRate() {
		return this.discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public BigDecimal getEnerEquipDiscPrice() {
		return this.enerEquipDiscPrice;
	}

	public void setEnerEquipDiscPrice(BigDecimal enerEquipDiscPrice) {
		this.enerEquipDiscPrice = enerEquipDiscPrice;
	}

	public BigDecimal getFuelAdjustPrice() {
		return this.fuelAdjustPrice;
	}

	public void setFuelAdjustPrice(BigDecimal fuelAdjustPrice) {
		this.fuelAdjustPrice = fuelAdjustPrice;
	}

	public BigDecimal getLowestMonthPrice() {
		return this.lowestMonthPrice;
	}

	public void setLowestMonthPrice(BigDecimal lowestMonthPrice) {
		this.lowestMonthPrice = lowestMonthPrice;
	}

	public BigDecimal getMicomDiscPrice() {
		return this.micomDiscPrice;
	}

	public void setMicomDiscPrice(BigDecimal micomDiscPrice) {
		this.micomDiscPrice = micomDiscPrice;
	}

	public BigDecimal getPowerPriceDayOther() {
		return this.powerPriceDayOther;
	}

	public void setPowerPriceDayOther(BigDecimal powerPriceDayOther) {
		this.powerPriceDayOther = powerPriceDayOther;
	}

	public BigDecimal getPowerPriceDaySummer() {
		return this.powerPriceDaySummer;
	}

	public void setPowerPriceDaySummer(BigDecimal powerPriceDaySummer) {
		this.powerPriceDaySummer = powerPriceDaySummer;
	}

	public BigDecimal getPowerPriceFamily() {
		return this.powerPriceFamily;
	}

	public void setPowerPriceFamily(BigDecimal powerPriceFamily) {
		this.powerPriceFamily = powerPriceFamily;
	}

	public BigDecimal getPowerPriceNight() {
		return this.powerPriceNight;
	}

	public void setPowerPriceNight(BigDecimal powerPriceNight) {
		this.powerPriceNight = powerPriceNight;
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

	public BigDecimal getRenewEnerPrice() {
		return this.renewEnerPrice;
	}

	public void setRenewEnerPrice(BigDecimal renewEnerPrice) {
		this.renewEnerPrice = renewEnerPrice;
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

}