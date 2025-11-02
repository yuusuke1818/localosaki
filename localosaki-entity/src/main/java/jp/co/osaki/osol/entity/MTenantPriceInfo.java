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
 * The persistent class for the m_tenant_price_info database table.
 * 
 */
@Entity
@Table(name="m_tenant_price_info")
@NamedQuery(name="MTenantPriceInfo.findAll", query="SELECT m FROM MTenantPriceInfo m")
public class MTenantPriceInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MTenantPriceInfoPK id;

	@Column(name="basic_price", precision=6, scale=2)
	private BigDecimal basicPrice;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="discount_rate", precision=3)
	private BigDecimal discountRate;

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

	public MTenantPriceInfo() {
	}

	public MTenantPriceInfoPK getId() {
		return this.id;
	}

	public void setId(MTenantPriceInfoPK id) {
		this.id = id;
	}

	public BigDecimal getBasicPrice() {
		return this.basicPrice;
	}

	public void setBasicPrice(BigDecimal basicPrice) {
		this.basicPrice = basicPrice;
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

}