package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_refrigerant_year_gwp database table.
 *
 */
@Entity
@Table(name = "m_refrigerant_year_gwp")
@NamedQueries({
    @NamedQuery(name = "MRefrigerantYearGwp.findAll", query = "SELECT m FROM MRefrigerantYearGwp m")
})

public class MRefrigerantYearGwp implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MRefrigerantYearGwpPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(nullable = false, precision = 23, scale = 10)
    private BigDecimal gwp;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MRefrigerant
    @ManyToOne
    @JoinColumn(name = "refrigerant_id", nullable = false, insertable = false, updatable = false)
    private MRefrigerant MRefrigerant;

    public MRefrigerantYearGwp() {
    }

    public MRefrigerantYearGwpPK getId() {
        return this.id;
    }

    public void setId(MRefrigerantYearGwpPK id) {
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

    public BigDecimal getGwp() {
        return this.gwp;
    }

    public void setGwp(BigDecimal gwp) {
        this.gwp = gwp;
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

    public MRefrigerant getMRefrigerant() {
        return this.MRefrigerant;
    }

    public void setMRefrigerant(MRefrigerant MRefrigerant) {
        this.MRefrigerant = MRefrigerant;
    }

}
