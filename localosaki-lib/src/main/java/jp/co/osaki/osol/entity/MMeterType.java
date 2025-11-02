package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_meter_type database table.
 * 
 */
@Entity
@Table(name="m_meter_type")
@NamedQuery(name="MMeterType.findAll", query="SELECT m FROM MMeterType m")
public class MMeterType implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MMeterTypePK id;

	@Column(name="all_com_div", length=1)
	private String allComDiv;

	@Column(name="auto_insp_day", length=2)
	private String autoInspDay;

	@Column(name="auto_insp_hour", length=2)
	private String autoInspHour;

	@Column(name="calc_type", precision=2)
	private BigDecimal calcType;

	@Column(name="co2_coefficient", precision=4, scale=3)
	private BigDecimal co2Coefficient;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="meter_type_name", length=20)
	private String meterTypeName;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="unit_co2_coefficient", length=20)
	private String unitCo2Coefficient;

	@Column(name="unit_price", precision=7, scale=3)
	private BigDecimal unitPrice;

	@Column(name="unit_usage_based", length=20)
	private String unitUsageBased;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MMeterRange
	@OneToMany(mappedBy="MMeterType")
	private List<MMeterRange> MMeterRanges;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MMeterType() {
	}

	public MMeterTypePK getId() {
		return this.id;
	}

	public void setId(MMeterTypePK id) {
		this.id = id;
	}

	public String getAllComDiv() {
		return this.allComDiv;
	}

	public void setAllComDiv(String allComDiv) {
		this.allComDiv = allComDiv;
	}

	public String getAutoInspDay() {
		return this.autoInspDay;
	}

	public void setAutoInspDay(String autoInspDay) {
		this.autoInspDay = autoInspDay;
	}

	public String getAutoInspHour() {
		return this.autoInspHour;
	}

	public void setAutoInspHour(String autoInspHour) {
		this.autoInspHour = autoInspHour;
	}

	public BigDecimal getCalcType() {
		return this.calcType;
	}

	public void setCalcType(BigDecimal calcType) {
		this.calcType = calcType;
	}

	public BigDecimal getCo2Coefficient() {
		return this.co2Coefficient;
	}

	public void setCo2Coefficient(BigDecimal co2Coefficient) {
		this.co2Coefficient = co2Coefficient;
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

	public String getMeterTypeName() {
		return this.meterTypeName;
	}

	public void setMeterTypeName(String meterTypeName) {
		this.meterTypeName = meterTypeName;
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

	public String getUnitCo2Coefficient() {
		return this.unitCo2Coefficient;
	}

	public void setUnitCo2Coefficient(String unitCo2Coefficient) {
		this.unitCo2Coefficient = unitCo2Coefficient;
	}

	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnitUsageBased() {
		return this.unitUsageBased;
	}

	public void setUnitUsageBased(String unitUsageBased) {
		this.unitUsageBased = unitUsageBased;
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

	public List<MMeterRange> getMMeterRanges() {
		return this.MMeterRanges;
	}

	public void setMMeterRanges(List<MMeterRange> MMeterRanges) {
		this.MMeterRanges = MMeterRanges;
	}

	public MMeterRange addMMeterRange(MMeterRange MMeterRange) {
		getMMeterRanges().add(MMeterRange);
		MMeterRange.setMMeterType(this);

		return MMeterRange;
	}

	public MMeterRange removeMMeterRange(MMeterRange MMeterRange) {
		getMMeterRanges().remove(MMeterRange);
		MMeterRange.setMMeterType(null);

		return MMeterRange;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}