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
 * The persistent class for the m_price_menu_lighta database table.
 * 
 */
@Entity
@Table(name="m_price_menu_lighta")
@NamedQuery(name="MPriceMenuLighta.findAll", query="SELECT m FROM MPriceMenuLighta m")
public class MPriceMenuLighta implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MPriceMenuLightaPK id;

	@Column(name="adjust_price_over_15", precision=6, scale=2)
	private BigDecimal adjustPriceOver15;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="fuel_adjust_price", precision=6, scale=2)
	private BigDecimal fuelAdjustPrice;

	@Column(name="lowest_price", precision=6, scale=2)
	private BigDecimal lowestPrice;

	@Column(name="power_price_over_300", precision=6, scale=2)
	private BigDecimal powerPriceOver300;

	@Column(name="power_price_to_120", precision=6, scale=2)
	private BigDecimal powerPriceTo120;

	@Column(name="power_price_to_300", precision=6, scale=2)
	private BigDecimal powerPriceTo300;

	@Column(name="rec_date")
	private Timestamp recDate;

	@Column(name="rec_man", length=50)
	private String recMan;

	@Column(name="renew_ener_price", precision=6, scale=2)
	private BigDecimal renewEnerPrice;

	@Column(name="renew_price_over_15", precision=6, scale=2)
	private BigDecimal renewPriceOver15;

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

	public MPriceMenuLighta() {
	}

	public MPriceMenuLightaPK getId() {
		return this.id;
	}

	public void setId(MPriceMenuLightaPK id) {
		this.id = id;
	}

	public BigDecimal getAdjustPriceOver15() {
		return this.adjustPriceOver15;
	}

	public void setAdjustPriceOver15(BigDecimal adjustPriceOver15) {
		this.adjustPriceOver15 = adjustPriceOver15;
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

	public BigDecimal getFuelAdjustPrice() {
		return this.fuelAdjustPrice;
	}

	public void setFuelAdjustPrice(BigDecimal fuelAdjustPrice) {
		this.fuelAdjustPrice = fuelAdjustPrice;
	}

	public BigDecimal getLowestPrice() {
		return this.lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public BigDecimal getPowerPriceOver300() {
		return this.powerPriceOver300;
	}

	public void setPowerPriceOver300(BigDecimal powerPriceOver300) {
		this.powerPriceOver300 = powerPriceOver300;
	}

	public BigDecimal getPowerPriceTo120() {
		return this.powerPriceTo120;
	}

	public void setPowerPriceTo120(BigDecimal powerPriceTo120) {
		this.powerPriceTo120 = powerPriceTo120;
	}

	public BigDecimal getPowerPriceTo300() {
		return this.powerPriceTo300;
	}

	public void setPowerPriceTo300(BigDecimal powerPriceTo300) {
		this.powerPriceTo300 = powerPriceTo300;
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

	public BigDecimal getRenewPriceOver15() {
		return this.renewPriceOver15;
	}

	public void setRenewPriceOver15(BigDecimal renewPriceOver15) {
		this.renewPriceOver15 = renewPriceOver15;
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