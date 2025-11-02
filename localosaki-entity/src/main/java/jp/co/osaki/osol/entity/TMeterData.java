package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_meter_data database table.
 * 
 */
@Entity
@Table(name="t_meter_data")
@NamedQuery(name="TMeterData.findAll", query="SELECT t FROM TMeterData t")
public class TMeterData implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TMeterDataPK id;

	@Column(name="ampere_1", precision=6, scale=2)
	private BigDecimal ampere1;

	@Column(name="ampere_2", precision=6, scale=2)
	private BigDecimal ampere2;

	@Column(name="ampere_3", precision=6, scale=2)
	private BigDecimal ampere3;

	@Column(name="circuit_breaker", length=1)
	private String circuitBreaker;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="current_kwh_1", length=7)
	private String currentKwh1;

	@Column(name="current_kwh_2", length=7)
	private String currentKwh2;

	@Column(name="measure_date")
	private Timestamp measureDate;

	@Column(name="meter_id", length=10)
	private String meterId;

	@Column(name="momentary_pwr", precision=7, scale=3)
	private BigDecimal momentaryPwr;

	@Column(name="power_factor", precision=6, scale=2)
	private BigDecimal powerFactor;

	@Column(name="rec_date", nullable=false)
	private Timestamp recDate;

	@Column(name="rec_man", nullable=false, length=50)
	private String recMan;

	@Column(name="srv_ent", length=1)
	private String srvEnt;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	@Column(name="voltage_12", precision=6, scale=2)
	private BigDecimal voltage12;

	@Column(name="voltage_13", precision=6, scale=2)
	private BigDecimal voltage13;

	@Column(name="voltage_23", precision=6, scale=2)
	private BigDecimal voltage23;

	public TMeterData() {
	}

	public TMeterDataPK getId() {
		return this.id;
	}

	public void setId(TMeterDataPK id) {
		this.id = id;
	}

	public BigDecimal getAmpere1() {
		return this.ampere1;
	}

	public void setAmpere1(BigDecimal ampere1) {
		this.ampere1 = ampere1;
	}

	public BigDecimal getAmpere2() {
		return this.ampere2;
	}

	public void setAmpere2(BigDecimal ampere2) {
		this.ampere2 = ampere2;
	}

	public BigDecimal getAmpere3() {
		return this.ampere3;
	}

	public void setAmpere3(BigDecimal ampere3) {
		this.ampere3 = ampere3;
	}

	public String getCircuitBreaker() {
		return this.circuitBreaker;
	}

	public void setCircuitBreaker(String circuitBreaker) {
		this.circuitBreaker = circuitBreaker;
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

	public String getCurrentKwh1() {
		return this.currentKwh1;
	}

	public void setCurrentKwh1(String currentKwh1) {
		this.currentKwh1 = currentKwh1;
	}

	public String getCurrentKwh2() {
		return this.currentKwh2;
	}

	public void setCurrentKwh2(String currentKwh2) {
		this.currentKwh2 = currentKwh2;
	}

	public Timestamp getMeasureDate() {
		return this.measureDate;
	}

	public void setMeasureDate(Timestamp measureDate) {
		this.measureDate = measureDate;
	}

	public String getMeterId() {
		return this.meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public BigDecimal getMomentaryPwr() {
		return this.momentaryPwr;
	}

	public void setMomentaryPwr(BigDecimal momentaryPwr) {
		this.momentaryPwr = momentaryPwr;
	}

	public BigDecimal getPowerFactor() {
		return this.powerFactor;
	}

	public void setPowerFactor(BigDecimal powerFactor) {
		this.powerFactor = powerFactor;
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

	public String getSrvEnt() {
		return this.srvEnt;
	}

	public void setSrvEnt(String srvEnt) {
		this.srvEnt = srvEnt;
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

	public BigDecimal getVoltage12() {
		return this.voltage12;
	}

	public void setVoltage12(BigDecimal voltage12) {
		this.voltage12 = voltage12;
	}

	public BigDecimal getVoltage13() {
		return this.voltage13;
	}

	public void setVoltage13(BigDecimal voltage13) {
		this.voltage13 = voltage13;
	}

	public BigDecimal getVoltage23() {
		return this.voltage23;
	}

	public void setVoltage23(BigDecimal voltage23) {
		this.voltage23 = voltage23;
	}

}