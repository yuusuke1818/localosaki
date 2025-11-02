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
 * The persistent class for the t_monthly_unit_denominator database table.
 * 
 */
@Entity
@Table(name="t_monthly_unit_denominator")
@NamedQuery(name="TMonthlyUnitDenominator.findAll", query="SELECT t FROM TMonthlyUnitDenominator t")
public class TMonthlyUnitDenominator implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TMonthlyUnitDenominatorPK id;

	@Column(name="change_auth_flg", nullable=false)
	private Integer changeAuthFlg;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="input_out_flg", nullable=false)
	private Integer inputOutFlg;

	@Column(name="unit_divide_value", precision=23, scale=10)
	private BigDecimal unitDivideValue;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MBuildUnitDenominator
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="unit_divide_id", referencedColumnName="unit_divide_id", nullable=false, insertable=false, updatable=false)
		})
	private MBuildUnitDenominator MBuildUnitDenominator;

	public TMonthlyUnitDenominator() {
	}

	public TMonthlyUnitDenominatorPK getId() {
		return this.id;
	}

	public void setId(TMonthlyUnitDenominatorPK id) {
		this.id = id;
	}

	public Integer getChangeAuthFlg() {
		return this.changeAuthFlg;
	}

	public void setChangeAuthFlg(Integer changeAuthFlg) {
		this.changeAuthFlg = changeAuthFlg;
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

	public Integer getInputOutFlg() {
		return this.inputOutFlg;
	}

	public void setInputOutFlg(Integer inputOutFlg) {
		this.inputOutFlg = inputOutFlg;
	}

	public BigDecimal getUnitDivideValue() {
		return this.unitDivideValue;
	}

	public void setUnitDivideValue(BigDecimal unitDivideValue) {
		this.unitDivideValue = unitDivideValue;
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

	public MBuildUnitDenominator getMBuildUnitDenominator() {
		return this.MBuildUnitDenominator;
	}

	public void setMBuildUnitDenominator(MBuildUnitDenominator MBuildUnitDenominator) {
		this.MBuildUnitDenominator = MBuildUnitDenominator;
	}

}