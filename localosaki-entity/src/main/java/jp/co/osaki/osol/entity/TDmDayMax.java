package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the t_dm_day_max database table.
 *
 */
@Entity
@Table(name="t_dm_day_max")
@NamedQuery(name="TDmDayMax.findAll", query="SELECT t FROM TDmDayMax t")
public class TDmDayMax implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TDmDayMaxPK id;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="kw_val", nullable=false, precision=10, scale=1)
    private BigDecimal kwVal;

    @Column(name="kw_val_update_time", nullable=false)
    private Timestamp kwValUpdateTime;

    @Column(name="plot_analog_point_no_1_val", precision=8, scale=1)
    private BigDecimal plotAnalogPointNo1Val;

    @Column(name="plot_analog_point_no_2_val", precision=8, scale=1)
    private BigDecimal plotAnalogPointNo2Val;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    //bi-directional many-to-one association to TDmDayLoad
    @OneToMany(mappedBy="TDmDayMax")
    private List<TDmDayLoad> TDmDayLoads;

    //bi-directional many-to-one association to MBuildingSm
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="sm_id", referencedColumnName="sm_id", nullable=false, insertable=false, updatable=false)
        })
    private MBuildingSm MBuildingSm;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="building_id", referencedColumnName="building_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="corp_id", referencedColumnName="corp_id",nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="measurement_date", referencedColumnName="measurement_date",nullable=false, insertable=false, updatable=false)
        })
    private TDmMonthRep TDmMonthRep;

    public TDmDayMax() {
    }

    public TDmDayMaxPK getId() {
        return this.id;
    }

    public void setId(TDmDayMaxPK id) {
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

    public BigDecimal getKwVal() {
        return this.kwVal;
    }

    public void setKwVal(BigDecimal kwVal) {
        this.kwVal = kwVal;
    }

    public Timestamp getKwValUpdateTime() {
        return this.kwValUpdateTime;
    }

    public void setKwValUpdateTime(Timestamp kwValUpdateTime) {
        this.kwValUpdateTime = kwValUpdateTime;
    }

    public BigDecimal getPlotAnalogPointNo1Val() {
        return this.plotAnalogPointNo1Val;
    }

    public void setPlotAnalogPointNo1Val(BigDecimal plotAnalogPointNo1Val) {
        this.plotAnalogPointNo1Val = plotAnalogPointNo1Val;
    }

    public BigDecimal getPlotAnalogPointNo2Val() {
        return this.plotAnalogPointNo2Val;
    }

    public void setPlotAnalogPointNo2Val(BigDecimal plotAnalogPointNo2Val) {
        this.plotAnalogPointNo2Val = plotAnalogPointNo2Val;
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

    public List<TDmDayLoad> getTDmDayLoads() {
        return this.TDmDayLoads;
    }

    public void setTDmDayLoads(List<TDmDayLoad> TDmDayLoads) {
        this.TDmDayLoads = TDmDayLoads;
    }

    public TDmDayLoad addTDmDayLoad(TDmDayLoad TDmDayLoad) {
        getTDmDayLoads().add(TDmDayLoad);
        TDmDayLoad.setTDmDayMax(this);

        return TDmDayLoad;
    }

    public TDmDayLoad removeTDmDayLoad(TDmDayLoad TDmDayLoad) {
        getTDmDayLoads().remove(TDmDayLoad);
        TDmDayLoad.setTDmDayMax(null);

        return TDmDayLoad;
    }

    public MBuildingSm getMBuildingSm() {
        return this.MBuildingSm;
    }

    public void setMBuildingSm(MBuildingSm MBuildingSm) {
        this.MBuildingSm = MBuildingSm;
    }

    public TDmMonthRep getTDmMonthRep() {
        return this.TDmMonthRep;
    }

    public void setTDmMonthRep(TDmMonthRep TDmMonthRep) {
        this.TDmMonthRep = TDmMonthRep;
    }

}