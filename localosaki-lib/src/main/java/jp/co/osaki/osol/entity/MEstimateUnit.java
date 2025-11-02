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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_estimate_unit database table.
 * 
 */
@Entity
@Table(name = "m_estimate_unit")
@NamedQueries({
    @NamedQuery(name = "MEstimateUnit.findAll", query = "SELECT m FROM MEstimateUnit m"),
    @NamedQuery(name = "MEstimateUnit.findEnergyInputCommon",
            query = "SELECT meu FROM MEstimateUnit meu "
            + "JOIN FETCH meu.MEstimateKind mek "
            + "JOIN FETCH mek.MCorp mc "
            + "WHERE mc.corpType = '0' "
            + "AND mc.corpId = meu.id.corpId "
            + "AND meu.id.year =:nendo "
            + "AND meu.id.regionTypeCode =:regionTypeCode "
            + "AND meu.id.estimateId =:estimateId"),})
public class MEstimateUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MEstimateUnitPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="month_1", nullable=false, precision=23, scale=10)
	private BigDecimal month1;

	@Column(name="month_10", nullable=false, precision=23, scale=10)
	private BigDecimal month10;

	@Column(name="month_11", nullable=false, precision=23, scale=10)
	private BigDecimal month11;

	@Column(name="month_12", nullable=false, precision=23, scale=10)
	private BigDecimal month12;

	@Column(name="month_2", nullable=false, precision=23, scale=10)
	private BigDecimal month2;

	@Column(name="month_3", nullable=false, precision=23, scale=10)
	private BigDecimal month3;

	@Column(name="month_4", nullable=false, precision=23, scale=10)
	private BigDecimal month4;

	@Column(name="month_5", nullable=false, precision=23, scale=10)
	private BigDecimal month5;

	@Column(name="month_6", nullable=false, precision=23, scale=10)
	private BigDecimal month6;

	@Column(name="month_7", nullable=false, precision=23, scale=10)
	private BigDecimal month7;

	@Column(name="month_8", nullable=false, precision=23, scale=10)
	private BigDecimal month8;

	@Column(name="month_9", nullable=false, precision=23, scale=10)
	private BigDecimal month9;

	@Column(length=100)
	private String unit;

	@Column(name="unit_divide_id")
	private Long unitDivideId;

	@Column(name="unit_factor_cd", length=50)
	private String unitFactorCd;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MEstimateKind
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="estimate_id", referencedColumnName="estimate_id", nullable=false, insertable=false, updatable=false)
		})
	private MEstimateKind MEstimateKind;

	//bi-directional many-to-one association to MRegionType
	@ManyToOne
	@JoinColumn(name="region_type_code", nullable=false, insertable=false, updatable=false)
	private MRegionType MRegionType;

	public MEstimateUnit() {
	}

	public MEstimateUnitPK getId() {
		return this.id;
	}

	public void setId(MEstimateUnitPK id) {
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

	public BigDecimal getMonth1() {
		return this.month1;
	}

	public void setMonth1(BigDecimal month1) {
		this.month1 = month1;
	}

	public BigDecimal getMonth10() {
		return this.month10;
	}

	public void setMonth10(BigDecimal month10) {
		this.month10 = month10;
	}

	public BigDecimal getMonth11() {
		return this.month11;
	}

	public void setMonth11(BigDecimal month11) {
		this.month11 = month11;
	}

	public BigDecimal getMonth12() {
		return this.month12;
	}

	public void setMonth12(BigDecimal month12) {
		this.month12 = month12;
	}

	public BigDecimal getMonth2() {
		return this.month2;
	}

	public void setMonth2(BigDecimal month2) {
		this.month2 = month2;
	}

	public BigDecimal getMonth3() {
		return this.month3;
	}

	public void setMonth3(BigDecimal month3) {
		this.month3 = month3;
	}

	public BigDecimal getMonth4() {
		return this.month4;
	}

	public void setMonth4(BigDecimal month4) {
		this.month4 = month4;
	}

	public BigDecimal getMonth5() {
		return this.month5;
	}

	public void setMonth5(BigDecimal month5) {
		this.month5 = month5;
	}

	public BigDecimal getMonth6() {
		return this.month6;
	}

	public void setMonth6(BigDecimal month6) {
		this.month6 = month6;
	}

	public BigDecimal getMonth7() {
		return this.month7;
	}

	public void setMonth7(BigDecimal month7) {
		this.month7 = month7;
	}

	public BigDecimal getMonth8() {
		return this.month8;
	}

	public void setMonth8(BigDecimal month8) {
		this.month8 = month8;
	}

	public BigDecimal getMonth9() {
		return this.month9;
	}

	public void setMonth9(BigDecimal month9) {
		this.month9 = month9;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getUnitDivideId() {
		return this.unitDivideId;
	}

	public void setUnitDivideId(Long unitDivideId) {
		this.unitDivideId = unitDivideId;
	}

	public String getUnitFactorCd() {
		return this.unitFactorCd;
	}

	public void setUnitFactorCd(String unitFactorCd) {
		this.unitFactorCd = unitFactorCd;
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

	public MEstimateKind getMEstimateKind() {
		return this.MEstimateKind;
	}

	public void setMEstimateKind(MEstimateKind MEstimateKind) {
		this.MEstimateKind = MEstimateKind;
	}

	public MRegionType getMRegionType() {
		return this.MRegionType;
	}

	public void setMRegionType(MRegionType MRegionType) {
		this.MRegionType = MRegionType;
	}

}