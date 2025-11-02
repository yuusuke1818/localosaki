package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_demand_power_forecast_time database table.
 *
 */
@Embeddable
public class TDemandPowerForecastTimePK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name="sm_id", insertable=false, updatable=false, unique=true, nullable=false)
    private Long smId;

    @Temporal(TemporalType.DATE)
    @Column(name="forecast_date", unique=true, nullable=false)
    private java.util.Date forecastDate;

    @Column(name="jigen_no", unique=true, nullable=false, precision=2)
    private BigDecimal jigenNo;

    public TDemandPowerForecastTimePK() {
    }
    public Long getSmId() {
        return this.smId;
    }
    public void setSmId(Long smId) {
        this.smId = smId;
    }
    public java.util.Date getForecastDate() {
        return this.forecastDate;
    }
    public void setForecastDate(java.util.Date forecastDate) {
        this.forecastDate = forecastDate;
    }
    public BigDecimal getJigenNo() {
        return this.jigenNo;
    }
    public void setJigenNo(BigDecimal jigenNo) {
        this.jigenNo = jigenNo;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TDemandPowerForecastTimePK)) {
            return false;
        }
        TDemandPowerForecastTimePK castOther = (TDemandPowerForecastTimePK)other;
        return
            this.smId.equals(castOther.smId)
            && this.forecastDate.equals(castOther.forecastDate)
            && (this.jigenNo.equals(castOther.jigenNo));
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.smId.hashCode();
        hash = hash * prime + this.forecastDate.hashCode();
        hash = hash * prime + this.jigenNo.hashCode();

        return hash;
    }
}