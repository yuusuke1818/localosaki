package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The primary key class for the t_load_control_result database table.
 *
 */
@Embeddable
public class TLoadControlResultPK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "sm_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long smId;

    @Temporal(TemporalType.DATE)
    @Column(name = "load_control_date", unique = true, nullable = false)
    private java.util.Date loadControlDate;

    @Column(name = "control_target", unique = true, nullable = false, length = 2)
    private String controlTarget;

    @Column(name = "control_load", unique = true, nullable = false, precision = 3)
    private BigDecimal controlLoad;

    public TLoadControlResultPK() {
    }

    public Long getSmId() {
        return this.smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
    }

    public java.util.Date getLoadControlDate() {
        return this.loadControlDate;
    }

    public void setLoadControlDate(java.util.Date loadControlDate) {
        this.loadControlDate = loadControlDate;
    }

    public String getControlTarget() {
        return this.controlTarget;
    }

    public void setControlTarget(String controlTarget) {
        this.controlTarget = controlTarget;
    }

    public BigDecimal getControlLoad() {
        return this.controlLoad;
    }

    public void setControlLoad(BigDecimal controlLoad) {
        this.controlLoad = controlLoad;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TLoadControlResultPK)) {
            return false;
        }
        TLoadControlResultPK castOther = (TLoadControlResultPK) other;
        return this.smId.equals(castOther.smId)
                && this.loadControlDate.equals(castOther.loadControlDate)
                && this.controlTarget.equals(castOther.controlTarget)
                && (this.controlLoad.equals(castOther.controlLoad));
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.smId.hashCode();
        hash = hash * prime + this.loadControlDate.hashCode();
        hash = hash * prime + this.controlTarget.hashCode();
        hash = hash * prime + this.controlLoad.hashCode();

        return hash;
    }
}