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
 * The persistent class for the t_dm_day_rep_point_input database table.
 *
 */
@Entity
@Table(name = "t_dm_day_rep_point_input")
@NamedQuery(name = "TDmDayRepPointInput.findAll", query = "SELECT t FROM TDmDayRepPointInput t")
public class TDmDayRepPointInput implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TDmDayRepPointInputPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "point_val", precision = 8, scale = 1)
    private BigDecimal pointVal;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MBuildingSmPoint
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "point_no", referencedColumnName = "point_no", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "sm_id", referencedColumnName = "sm_id", nullable = false, insertable = false, updatable = false)
    })
    private MBuildingSmPoint MBuildingSmPoint;

    //bi-directional many-to-one association to TDmDayRep
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "jigen_no", referencedColumnName = "jigen_no", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "measurement_date", referencedColumnName = "measurement_date", nullable = false, insertable = false, updatable = false)
    })
    private TDmDayRep TDmDayRep;

    public TDmDayRepPointInput() {
    }

    public TDmDayRepPointInputPK getId() {
        return this.id;
    }

    public void setId(TDmDayRepPointInputPK id) {
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

    public BigDecimal getPointVal() {
        return this.pointVal;
    }

    public void setPointVal(BigDecimal pointVal) {
        this.pointVal = pointVal;
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

    public MBuildingSmPoint getMBuildingSmPoint() {
        return this.MBuildingSmPoint;
    }

    public void setMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
        this.MBuildingSmPoint = MBuildingSmPoint;
    }

    public TDmDayRep getTDmDayRep() {
        return this.TDmDayRep;
    }

    public void setTDmDayRep(TDmDayRep TDmDayRep) {
        this.TDmDayRep = TDmDayRep;
    }

}