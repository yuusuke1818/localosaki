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
 * The persistent class for the m_various database table.
 * 
 */
@Entity
@Table(name="m_various")
@NamedQuery(name="MVarious.findAll", query="SELECT m FROM MVarious m")
public class MVarious implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MVariousPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="decimal_fraction", length=1)
	private String decimalFraction;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="sale_tax_deal", length=1)
	private String saleTaxDeal;

	@Column(name="sale_tax_rate", precision=3)
	private BigDecimal saleTaxRate;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="year_close_month", precision=2)
	private BigDecimal yearCloseMonth;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MVarious() {
	}

	public MVariousPK getId() {
		return this.id;
	}

	public void setId(MVariousPK id) {
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

	public String getDecimalFraction() {
		return this.decimalFraction;
	}

	public void setDecimalFraction(String decimalFraction) {
		this.decimalFraction = decimalFraction;
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

	public String getSaleTaxDeal() {
		return this.saleTaxDeal;
	}

	public void setSaleTaxDeal(String saleTaxDeal) {
		this.saleTaxDeal = saleTaxDeal;
	}

	public BigDecimal getSaleTaxRate() {
		return this.saleTaxRate;
	}

	public void setSaleTaxRate(BigDecimal saleTaxRate) {
		this.saleTaxRate = saleTaxRate;
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

	public BigDecimal getYearCloseMonth() {
		return this.yearCloseMonth;
	}

	public void setYearCloseMonth(BigDecimal yearCloseMonth) {
		this.yearCloseMonth = yearCloseMonth;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}