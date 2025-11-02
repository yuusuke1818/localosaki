package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_demand_power_forecast_time database table.
 * 
 */
@Entity
@Table(name="t_demand_power_forecast_time")
@NamedQuery(name="TDemandPowerForecastTime.findAll", query="SELECT t FROM TDemandPowerForecastTime t")
public class TDemandPowerForecastTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDemandPowerForecastTimePK id;

	@Column(name="ai_forecast_value", precision=4)
	private BigDecimal aiForecastValue;

	@Column(name="comfortable_forecast_value", precision=4)
	private BigDecimal comfortableForecastValue;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="eco_forecast_value", precision=4)
	private BigDecimal ecoForecastValue;

	@Column(name="normal_forecast_value", precision=4)
	private BigDecimal normalForecastValue;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MSmPrm
	@ManyToOne
	@JoinColumn(name="sm_id", nullable=false, insertable=false, updatable=false)
	private MSmPrm MSmPrm;

	public TDemandPowerForecastTime() {
	}

	public TDemandPowerForecastTimePK getId() {
		return this.id;
	}

	public void setId(TDemandPowerForecastTimePK id) {
		this.id = id;
	}

	public BigDecimal getAiForecastValue() {
		return this.aiForecastValue;
	}

	public void setAiForecastValue(BigDecimal aiForecastValue) {
		this.aiForecastValue = aiForecastValue;
	}

	public BigDecimal getComfortableForecastValue() {
		return this.comfortableForecastValue;
	}

	public void setComfortableForecastValue(BigDecimal comfortableForecastValue) {
		this.comfortableForecastValue = comfortableForecastValue;
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

	public BigDecimal getEcoForecastValue() {
		return this.ecoForecastValue;
	}

	public void setEcoForecastValue(BigDecimal ecoForecastValue) {
		this.ecoForecastValue = ecoForecastValue;
	}

	public BigDecimal getNormalForecastValue() {
		return this.normalForecastValue;
	}

	public void setNormalForecastValue(BigDecimal normalForecastValue) {
		this.normalForecastValue = normalForecastValue;
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

	public MSmPrm getMSmPrm() {
		return this.MSmPrm;
	}

	public void setMSmPrm(MSmPrm MSmPrm) {
		this.MSmPrm = MSmPrm;
	}

}