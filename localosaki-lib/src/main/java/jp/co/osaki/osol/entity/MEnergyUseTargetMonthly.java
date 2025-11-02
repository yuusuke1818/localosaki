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
 * The persistent class for the m_energy_use_target_monthly database table.
 * 
 */
@Entity
@Table(name="m_energy_use_target_monthly")
@NamedQuery(name="MEnergyUseTargetMonthly.findAll", query="SELECT m FROM MEnergyUseTargetMonthly m")
public class MEnergyUseTargetMonthly implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MEnergyUseTargetMonthlyPK id;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="target_billed_amount", precision=10)
	private BigDecimal targetBilledAmount;

	@Column(name="target_use_unit1", nullable=false, length=100)
	private String targetUseUnit1;

	@Column(name="target_use_unit2", nullable=false, length=100)
	private String targetUseUnit2;

	@Column(name="target_use_value1", precision=23, scale=10)
	private BigDecimal targetUseValue1;

	@Column(name="target_use_value2", precision=23, scale=10)
	private BigDecimal targetUseValue2;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MEnergyType
	@ManyToOne
	@JoinColumn(name="eng_type_cd", nullable=false, insertable=false, updatable=false)
	private MEnergyType MEnergyType;

	//bi-directional many-to-one association to TBuilding
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false)
		})
	private TBuilding TBuilding;

	public MEnergyUseTargetMonthly() {
	}

	public MEnergyUseTargetMonthlyPK getId() {
		return this.id;
	}

	public void setId(MEnergyUseTargetMonthlyPK id) {
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

	public BigDecimal getTargetBilledAmount() {
		return this.targetBilledAmount;
	}

	public void setTargetBilledAmount(BigDecimal targetBilledAmount) {
		this.targetBilledAmount = targetBilledAmount;
	}

	public String getTargetUseUnit1() {
		return this.targetUseUnit1;
	}

	public void setTargetUseUnit1(String targetUseUnit1) {
		this.targetUseUnit1 = targetUseUnit1;
	}

	public String getTargetUseUnit2() {
		return this.targetUseUnit2;
	}

	public void setTargetUseUnit2(String targetUseUnit2) {
		this.targetUseUnit2 = targetUseUnit2;
	}

	public BigDecimal getTargetUseValue1() {
		return this.targetUseValue1;
	}

	public void setTargetUseValue1(BigDecimal targetUseValue1) {
		this.targetUseValue1 = targetUseValue1;
	}

	public BigDecimal getTargetUseValue2() {
		return this.targetUseValue2;
	}

	public void setTargetUseValue2(BigDecimal targetUseValue2) {
		this.targetUseValue2 = targetUseValue2;
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

	public MEnergyType getMEnergyType() {
		return this.MEnergyType;
	}

	public void setMEnergyType(MEnergyType MEnergyType) {
		this.MEnergyType = MEnergyType;
	}

	public TBuilding getTBuilding() {
		return this.TBuilding;
	}

	public void setTBuilding(TBuilding TBuilding) {
		this.TBuilding = TBuilding;
	}

}