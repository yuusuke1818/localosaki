package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the m_sm_point database table.
 *
 */
@Entity
@Table(name = "m_sm_point")
@NamedQuery(name = "MSmPoint.findAll", query = "SELECT m FROM MSmPoint m")
public class MSmPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MSmPointPK id;

    @Column(name = "analog_conversion_factor", nullable = false, precision = 7, scale = 4)
    private BigDecimal analogConversionFactor;

    @Column(name = "analog_off_set_value", nullable = false, precision = 6, scale = 3)
    private BigDecimal analogOffSetValue;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "dm_correction_factor", nullable = false, precision = 6, scale = 3)
    private BigDecimal dmCorrectionFactor;

    @Column(name = "point_type", nullable = false, length = 6)
    private String pointType;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MBuildingSmPoint
    @OneToMany(mappedBy = "MSmPoint")
    private List<MBuildingSmPoint> MBuildingSmPoints;

    //bi-directional many-to-one association to MSmPrm
    @ManyToOne
    @JoinColumn(name = "sm_id", nullable = false, insertable = false, updatable = false)
    private MSmPrm MSmPrm;

    //bi-directional many-to-one association to MSmPointEx
    @OneToMany(mappedBy = "MSmPoint")
    private List<MSmPointEx> MSmPointExs;

    //bi-directional many-to-one association to TDmDayRepPoint
    @OneToMany(mappedBy = "MSmPoint")
    private List<TDmDayRepPoint> TDmDayRepPoints;

    public MSmPoint() {
    }

    public MSmPointPK getId() {
        return this.id;
    }

    public void setId(MSmPointPK id) {
        this.id = id;
    }

    public BigDecimal getAnalogConversionFactor() {
        return this.analogConversionFactor;
    }

    public void setAnalogConversionFactor(BigDecimal analogConversionFactor) {
        this.analogConversionFactor = analogConversionFactor;
    }

    public BigDecimal getAnalogOffSetValue() {
        return this.analogOffSetValue;
    }

    public void setAnalogOffSetValue(BigDecimal analogOffSetValue) {
        this.analogOffSetValue = analogOffSetValue;
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

    public BigDecimal getDmCorrectionFactor() {
        return this.dmCorrectionFactor;
    }

    public void setDmCorrectionFactor(BigDecimal dmCorrectionFactor) {
        this.dmCorrectionFactor = dmCorrectionFactor;
    }

    public String getPointType() {
        return this.pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
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

    public List<MBuildingSmPoint> getMBuildingSmPoints() {
        return this.MBuildingSmPoints;
    }

    public void setMBuildingSmPoints(List<MBuildingSmPoint> MBuildingSmPoints) {
        this.MBuildingSmPoints = MBuildingSmPoints;
    }

    public MBuildingSmPoint addMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
        getMBuildingSmPoints().add(MBuildingSmPoint);
        MBuildingSmPoint.setMSmPoint(this);

        return MBuildingSmPoint;
    }

    public MBuildingSmPoint removeMBuildingSmPoint(MBuildingSmPoint MBuildingSmPoint) {
        getMBuildingSmPoints().remove(MBuildingSmPoint);
        MBuildingSmPoint.setMSmPoint(null);

        return MBuildingSmPoint;
    }

    public MSmPrm getMSmPrm() {
        return this.MSmPrm;
    }

    public void setMSmPrm(MSmPrm MSmPrm) {
        this.MSmPrm = MSmPrm;
    }

    public List<MSmPointEx> getMSmPointExs() {
        return this.MSmPointExs;
    }

    public void setMSmPointExs(List<MSmPointEx> MSmPointExs) {
        this.MSmPointExs = MSmPointExs;
    }

    public MSmPointEx addMSmPointEx(MSmPointEx MSmPointEx) {
        getMSmPointExs().add(MSmPointEx);
        MSmPointEx.setMSmPoint(this);

        return MSmPointEx;
    }

    public MSmPointEx removeMSmPointEx(MSmPointEx MSmPointEx) {
        getMSmPointExs().remove(MSmPointEx);
        MSmPointEx.setMSmPoint(null);

        return MSmPointEx;
    }

    public List<TDmDayRepPoint> getTDmDayRepPoints() {
        return this.TDmDayRepPoints;
    }

    public void setTDmDayRepPoints(List<TDmDayRepPoint> TDmDayRepPoints) {
        this.TDmDayRepPoints = TDmDayRepPoints;
    }

    public TDmDayRepPoint addTDmDayRepPoints(TDmDayRepPoint TDmDayRepPoints) {
        getTDmDayRepPoints().add(TDmDayRepPoints);
        TDmDayRepPoints.setMSmPoint(this);

        return TDmDayRepPoints;
    }

    public TDmDayRepPoint removeTDmDayRepPoints(TDmDayRepPoint TDmDayRepPoints) {
        getTDmDayRepPoints().remove(TDmDayRepPoints);
        TDmDayRepPoints.setMSmPoint(null);

        return TDmDayRepPoints;
    }

}