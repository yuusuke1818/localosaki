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
 * The persistent class for the t_demand_power_forecast_day database table.
 * 
 */
@Entity
@Table(name="t_demand_power_forecast_day")
@NamedQuery(name="TDemandPowerForecastDay.findAll", query="SELECT t FROM TDemandPowerForecastDay t")
public class TDemandPowerForecastDay implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TDemandPowerForecastDayPK id;

	@Column(name="air_demand_forecast_value", precision=4)
	private BigDecimal airDemandForecastValue;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="total_demand_value", precision=5)
	private BigDecimal totalDemandValue;

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

	public TDemandPowerForecastDay() {
	}

	public TDemandPowerForecastDayPK getId() {
		return this.id;
	}

	public void setId(TDemandPowerForecastDayPK id) {
		this.id = id;
	}

	public BigDecimal getAirDemandForecastValue() {
		return this.airDemandForecastValue;
	}

	public void setAirDemandForecastValue(BigDecimal airDemandForecastValue) {
		this.airDemandForecastValue = airDemandForecastValue;
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

	public BigDecimal getTotalDemandValue() {
		return this.totalDemandValue;
	}

	public void setTotalDemandValue(BigDecimal totalDemandValue) {
		this.totalDemandValue = totalDemandValue;
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