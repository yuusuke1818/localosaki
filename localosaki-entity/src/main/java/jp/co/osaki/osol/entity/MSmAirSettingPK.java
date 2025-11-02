package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the m_sm_air_setting database table.
 *
 */
@Embeddable
public class MSmAirSettingPK implements Serializable {
    //default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "sm_id", insertable = false, updatable = false, unique = true, nullable = false)
    private Long smId;

    @Column(name = "output_port_no", unique = true, nullable = false, precision = 3)
    private BigDecimal outputPortNo;

    public MSmAirSettingPK() {
    }

    public Long getSmId() {
        return this.smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
    }

    public BigDecimal getOutputPortNo() {
        return this.outputPortNo;
    }

    public void setOutputPortNo(BigDecimal outputPortNo) {
        this.outputPortNo = outputPortNo;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MSmAirSettingPK)) {
            return false;
        }
        MSmAirSettingPK castOther = (MSmAirSettingPK) other;
        return this.smId.equals(castOther.smId)
                && (this.outputPortNo.equals(castOther.outputPortNo));
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.smId.hashCode();
        hash = hash * prime + this.outputPortNo.hashCode();

        return hash;
    }
}