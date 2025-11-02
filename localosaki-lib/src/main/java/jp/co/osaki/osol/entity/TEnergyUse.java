package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


/**
 * The persistent class for the t_energy_use database table.
 * 
 */
@Entity
@Table(name = "t_energy_use")
@NamedQueries({
    @NamedQuery(name = "TEnergyUse.findAll", query = "SELECT t FROM TEnergyUse t"),
//    @NamedQuery(name = "TEnergyUse.findPkAll", query = "SELECT teu FROM TEnergyUse teu WHERE teu.id.corpId =:corpId AND teu.id.buildingId =:buildingId AND teu.id.engTypeCd =:engTypeCd AND teu.id.contractId =:contractId AND teu.id.useYm =:useYm"),
    @NamedQuery(name = "TEnergyUse.findPKData", query =
            "SELECT teu FROM TEnergyUse teu "
                    + "WHERE teu.id.corpId =:corpId"
                    + " AND teu.id.buildingId =:buildingId"
                    + " AND teu.id.engTypeCd =:engTypeCd"
                    + " AND teu.id.engId =:engId"
                    + " AND teu.id.contractId =:contractId"),})
public class TEnergyUse implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TEnergyUsePK id;

	@Column(name="billed_amount", precision=10)
	private BigDecimal billedAmount;

	@Column(name="change_auth_flg", nullable=false)
	private Integer changeAuthFlg;

	@Column(name="co2_coefficient", precision=15, scale=10)
	private BigDecimal co2Coefficient;

	@Column(name="co2_unit", length=100)
	private String co2Unit;

	@Column(name="co2_value", precision=23, scale=10)
	private BigDecimal co2Value;

	@Column(name="contract_biko", length=2000)
	private String contractBiko;

	@Column(name="contract_power", precision=23, scale=10)
	private BigDecimal contractPower;

	@Column(name="contract_power_unit", length=100)
	private String contractPowerUnit;

	@Column(name="contract_type", length=100)
	private String contractType;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="customer_no", length=100)
	private String customerNo;

	@Column(name="day_and_night_type", nullable=false, length=6)
	private String dayAndNightType;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="eng_supply_type", nullable=false, length=6)
	private String engSupplyType;

	@Column(name="first_eng_coefficient", precision=15, scale=10)
	private BigDecimal firstEngCoefficient;

	@Column(name="first_eng_unit", length=100)
	private String firstEngUnit;

	@Column(name="first_eng_value", precision=23, scale=10)
	private BigDecimal firstEngValue;

	@Column(name="fuel_adjustment_cost", precision=12, scale=2)
	private BigDecimal fuelAdjustmentCost;

	@Column(name="hanei_type", nullable=false, length=6)
	private String haneiType;

	@Column(name="max_power", precision=23, scale=10)
	private BigDecimal maxPower;

	@Temporal(TemporalType.DATE)
	@Column(name="meter_check_date")
	private Date meterCheckDate;

	@Column(name="meter_image_file_name", length=510)
	private String meterImageFileName;

	@Column(name="meter_image_file_size", nullable=false)
	private Long meterImageFileSize;

	@Column(name="meter_image_path", length=510)
	private String meterImagePath;

	@Temporal(TemporalType.DATE)
	@Column(name="payment_date")
	private Date paymentDate;

	@Column(name="power_coefficient", precision=23, scale=10)
	private BigDecimal powerCoefficient;

	@Column(name="r_energy_gp_levy", precision=10)
	private BigDecimal rEnergyGpLevy;

	@Column(name="supply_point_specific_no", length=27)
	private String supplyPointSpecificNo;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Column(name="use_days", precision=10)
	private BigDecimal useDays;

	@Column(name="use_place", length=100)
	private String usePlace;

	@Column(name="use_unit1", nullable=false, length=100)
	private String useUnit1;

	@Column(name="use_unit2", nullable=false, length=100)
	private String useUnit2;

	@Column(name="use_value1", precision=23, scale=10)
	private BigDecimal useValue1;

	@Column(name="use_value2", precision=23, scale=10)
	private BigDecimal useValue2;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to TAvailableEnergy
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="contract_id", referencedColumnName="contract_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="eng_id", referencedColumnName="eng_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="eng_type_cd", referencedColumnName="eng_type_cd", nullable=false, insertable=false, updatable=false)
		})
	private TAvailableEnergy TAvailableEnergy;

	public TEnergyUse() {
	}

	public TEnergyUsePK getId() {
		return this.id;
	}

	public void setId(TEnergyUsePK id) {
		this.id = id;
	}

	public BigDecimal getBilledAmount() {
		return this.billedAmount;
	}

	public void setBilledAmount(BigDecimal billedAmount) {
		this.billedAmount = billedAmount;
	}

	public Integer getChangeAuthFlg() {
		return this.changeAuthFlg;
	}

	public void setChangeAuthFlg(Integer changeAuthFlg) {
		this.changeAuthFlg = changeAuthFlg;
	}

	public BigDecimal getCo2Coefficient() {
		return this.co2Coefficient;
	}

	public void setCo2Coefficient(BigDecimal co2Coefficient) {
		this.co2Coefficient = co2Coefficient;
	}

	public String getCo2Unit() {
		return this.co2Unit;
	}

	public void setCo2Unit(String co2Unit) {
		this.co2Unit = co2Unit;
	}

	public BigDecimal getCo2Value() {
		return this.co2Value;
	}

	public void setCo2Value(BigDecimal co2Value) {
		this.co2Value = co2Value;
	}

	public String getContractBiko() {
		return this.contractBiko;
	}

	public void setContractBiko(String contractBiko) {
		this.contractBiko = contractBiko;
	}

	public BigDecimal getContractPower() {
		return this.contractPower;
	}

	public void setContractPower(BigDecimal contractPower) {
		this.contractPower = contractPower;
	}

	public String getContractPowerUnit() {
		return this.contractPowerUnit;
	}

	public void setContractPowerUnit(String contractPowerUnit) {
		this.contractPowerUnit = contractPowerUnit;
	}

	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
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

	public String getCustomerNo() {
		return this.customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getDayAndNightType() {
		return this.dayAndNightType;
	}

	public void setDayAndNightType(String dayAndNightType) {
		this.dayAndNightType = dayAndNightType;
	}

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public String getEngSupplyType() {
		return this.engSupplyType;
	}

	public void setEngSupplyType(String engSupplyType) {
		this.engSupplyType = engSupplyType;
	}

	public BigDecimal getFirstEngCoefficient() {
		return this.firstEngCoefficient;
	}

	public void setFirstEngCoefficient(BigDecimal firstEngCoefficient) {
		this.firstEngCoefficient = firstEngCoefficient;
	}

	public String getFirstEngUnit() {
		return this.firstEngUnit;
	}

	public void setFirstEngUnit(String firstEngUnit) {
		this.firstEngUnit = firstEngUnit;
	}

	public BigDecimal getFirstEngValue() {
		return this.firstEngValue;
	}

	public void setFirstEngValue(BigDecimal firstEngValue) {
		this.firstEngValue = firstEngValue;
	}

	public BigDecimal getFuelAdjustmentCost() {
		return this.fuelAdjustmentCost;
	}

	public void setFuelAdjustmentCost(BigDecimal fuelAdjustmentCost) {
		this.fuelAdjustmentCost = fuelAdjustmentCost;
	}

	public String getHaneiType() {
		return this.haneiType;
	}

	public void setHaneiType(String haneiType) {
		this.haneiType = haneiType;
	}

	public BigDecimal getMaxPower() {
		return this.maxPower;
	}

	public void setMaxPower(BigDecimal maxPower) {
		this.maxPower = maxPower;
	}

	public Date getMeterCheckDate() {
		return this.meterCheckDate;
	}

	public void setMeterCheckDate(Date meterCheckDate) {
		this.meterCheckDate = meterCheckDate;
	}

	public String getMeterImageFileName() {
		return this.meterImageFileName;
	}

	public void setMeterImageFileName(String meterImageFileName) {
		this.meterImageFileName = meterImageFileName;
	}

	public Long getMeterImageFileSize() {
		return this.meterImageFileSize;
	}

	public void setMeterImageFileSize(Long meterImageFileSize) {
		this.meterImageFileSize = meterImageFileSize;
	}

	public String getMeterImagePath() {
		return this.meterImagePath;
	}

	public void setMeterImagePath(String meterImagePath) {
		this.meterImagePath = meterImagePath;
	}

	public Date getPaymentDate() {
		return this.paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getPowerCoefficient() {
		return this.powerCoefficient;
	}

	public void setPowerCoefficient(BigDecimal powerCoefficient) {
		this.powerCoefficient = powerCoefficient;
	}

	public BigDecimal getREnergyGpLevy() {
		return this.rEnergyGpLevy;
	}

	public void setREnergyGpLevy(BigDecimal rEnergyGpLevy) {
		this.rEnergyGpLevy = rEnergyGpLevy;
	}

	public String getSupplyPointSpecificNo() {
		return this.supplyPointSpecificNo;
	}

	public void setSupplyPointSpecificNo(String supplyPointSpecificNo) {
		this.supplyPointSpecificNo = supplyPointSpecificNo;
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

	public BigDecimal getUseDays() {
		return this.useDays;
	}

	public void setUseDays(BigDecimal useDays) {
		this.useDays = useDays;
	}

	public String getUsePlace() {
		return this.usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	public String getUseUnit1() {
		return this.useUnit1;
	}

	public void setUseUnit1(String useUnit1) {
		this.useUnit1 = useUnit1;
	}

	public String getUseUnit2() {
		return this.useUnit2;
	}

	public void setUseUnit2(String useUnit2) {
		this.useUnit2 = useUnit2;
	}

	public BigDecimal getUseValue1() {
		return this.useValue1;
	}

	public void setUseValue1(BigDecimal useValue1) {
		this.useValue1 = useValue1;
	}

	public BigDecimal getUseValue2() {
		return this.useValue2;
	}

	public void setUseValue2(BigDecimal useValue2) {
		this.useValue2 = useValue2;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TAvailableEnergy getTAvailableEnergy() {
		return this.TAvailableEnergy;
	}

	public void setTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
		this.TAvailableEnergy = TAvailableEnergy;
	}

}