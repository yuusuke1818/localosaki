package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the t_freon_filling_report database table.
 *
 */
@Entity
@Table(name = "t_freon_filling_report")
@NamedQueries({
    @NamedQuery(name = "TFreonFillingReport.findAll", query = "SELECT t FROM TFreonFillingReport t"),
    @NamedQuery(name = "TFreonFillingReport.findTotalLeakageAmount",
            query = "SELECT DISTINCT t FROM TFreonFillingReport t "
            + "LEFT JOIN t.MFacility mf "
            + "LEFT JOIN mf.TBuilding tb "
            + "LEFT JOIN mf.MRefrigerant mr "
            + "LEFT JOIN mr.MRefrigerantYearGwps mryg ON mryg.id.year = :year "
            + "WHERE t.id.corpId = :corpId "
            + "AND t.fillingDate BETWEEN :fromDate AND :toDate "
            + "AND t.delFlg = :delFlg "
            + "AND mf.delFlg = :delFlg "
            + "AND tb.delFlg = :delFlg "
            + "AND tb.buildingDelDate IS NULL"),
    @NamedQuery(name = "TFreonFillingReport.findFreonInputStateList",
            query = "SELECT DISTINCT t FROM TFreonFillingReport t "
            + "LEFT JOIN t.MFacility mf "
            + "LEFT JOIN mf.TBuilding tb "
            + "LEFT JOIN mf.MFacilityKinds mfk "
            + "LEFT JOIN mfk.MCorpFacilityKind mc "
            + "LEFT JOIN mc.MFacilitySmallKind mfs "
            + "LEFT JOIN mfs.MFacilityBigKind mfg "
            + "LEFT JOIN mf.MRefrigerant mr "
            + "WHERE t.id.corpId = :corpId "
            + "AND t.delFlg = :delFlg "
            + "AND mf.delFlg = :delFlg "
            + "AND tb.delFlg = :delFlg "
            + "AND tb.buildingDelDate IS NULL "
            + "AND mfk.delFlg = :delFlg "
            + "ORDER BY t.updateDate DESC"),
    @NamedQuery(name = "TFreonFillingReport.findExcel",
            query = "SELECT t FROM TFreonFillingReport t "
            + "LEFT JOIN t.MFacility mf "
            + "LEFT JOIN mf.TBuilding tb "
            + "LEFT JOIN tb.MPrefecture mp "
            + "LEFT JOIN mf.MRefrigerant mr "
            + "LEFT JOIN mr.MRefrigerantYearGwps mryg ON mryg.id.year = :year "
            + "WHERE t.id.corpId = :corpId "
            + "AND t.fillingDate BETWEEN :fromDate AND :toDate "
            + "AND t.delFlg = 0 "
            + "AND mf.delFlg = 0 "
            + "AND tb.delFlg = 0 "
            + "AND tb.buildingDelDate IS NULL "
            + "ORDER BY "
            + "t.id.corpId, t.fillingDate, t.MFacility.TBuilding.MPrefecture.prefectureCd, t.MFacility.TBuilding.buildingNo,"
            + "t.MFacility.MRefrigerant.displayOrder, t.id.maintenanceId, t.id.maintenanceRequestId, t.id.facilityId, t.id.freonFillingReportId")
})
public class TFreonFillingReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TFreonFillingReportPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "filling_amount", nullable = false, precision = 23, scale = 10)
    private BigDecimal fillingAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "filling_date", nullable = false)
    private Date fillingDate;

    @Column(name = "leakage_amount", nullable = false, precision = 23, scale = 10)
    private BigDecimal leakageAmount;

    @Column(name = "recovered_amount", nullable = false, precision = 23, scale = 10)
    private BigDecimal recoveredAmount;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MFacility
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "facility_id", referencedColumnName = "facility_id", nullable = false, insertable = false, updatable = false)
    })
    private MFacility MFacility;

    //bi-directional many-to-one association to TMaintenanceRequest
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_id", referencedColumnName = "maintenance_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_request_id", referencedColumnName = "maintenance_request_id", nullable = false, insertable = false, updatable = false)
    })
    private TMaintenanceRequest TMaintenanceRequest;

    public TFreonFillingReport() {
    }

    public TFreonFillingReportPK getId() {
        return this.id;
    }

    public void setId(TFreonFillingReportPK id) {
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

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public BigDecimal getFillingAmount() {
        return this.fillingAmount;
    }

    public void setFillingAmount(BigDecimal fillingAmount) {
        this.fillingAmount = fillingAmount;
    }

    public Date getFillingDate() {
        return this.fillingDate;
    }

    public void setFillingDate(Date fillingDate) {
        this.fillingDate = fillingDate;
    }

    public BigDecimal getLeakageAmount() {
        return this.leakageAmount;
    }

    public void setLeakageAmount(BigDecimal leakageAmount) {
        this.leakageAmount = leakageAmount;
    }

    public BigDecimal getRecoveredAmount() {
        return this.recoveredAmount;
    }

    public void setRecoveredAmount(BigDecimal recoveredAmount) {
        this.recoveredAmount = recoveredAmount;
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

    public MFacility getMFacility() {
        return this.MFacility;
    }

    public void setMFacility(MFacility MFacility) {
        this.MFacility = MFacility;
    }

    public TMaintenanceRequest getTMaintenanceRequest() {
        return this.TMaintenanceRequest;
    }

    public void setTMaintenanceRequest(TMaintenanceRequest TMaintenanceRequest) {
        this.TMaintenanceRequest = TMaintenanceRequest;
    }

}
