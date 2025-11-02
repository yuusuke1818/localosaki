package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the t_maintenance_facility database table.
 *
 */
@Entity
@Table(name = "t_maintenance_facility")
@NamedQueries({
    @NamedQuery(name = "TMaintenanceFacility.findAll", query = "SELECT t FROM TMaintenanceFacility t"),
    @NamedQuery(name = "TMaintenanceFacility.findAllByFacility",
            query = "SELECT t FROM TMaintenanceFacility t "
            + " WHERE (:corpId is null or t.id.corpId =:corpId) "
            + " AND (:buildingId is null or t.id.buildingId =:buildingId) "
            + " AND (:facilityId is null or t.id.facilityId =:facilityId) "
            + " AND t.delFlg <>'1' "),
    @NamedQuery(name = "TMaintenanceFacility.findFacilityDetail",
            query = "SELECT t FROM TMaintenanceFacility t"
            + " LEFT OUTER JOIN  t.MFacility mf"
            + " LEFT OUTER JOIN  mf.MFacilityKinds mfk"
            + " LEFT OUTER JOIN  mfk.MCorpFacilityKind mcfk"
            + " LEFT OUTER JOIN  mcfk.MFacilitySmallKind mfsk"
            + " LEFT OUTER JOIN  mfsk.MFacilityBigKind mfbk"
            + " WHERE (:corpId is null or t.id.corpId =:corpId) "
            + " AND (:buildingId is null or t.id.buildingId =:buildingId) "
            + " AND (:maintenanceId is null or t.id.maintenanceId =:maintenanceId) "
            + " AND (:maintenanceRequestId is null or t.id.maintenanceRequestId =:maintenanceRequestId) "
            + " AND t.delFlg <>'1' "),
    @NamedQuery(name = "TMaintenanceFacility.findExcel",
            query = "SELECT mf FROM TMaintenanceFacility mf"
            + " WHERE (:corpId IS NULL OR mf.id.corpId =:corpId) "
            + " AND (:buildingId IS NULL OR mf.id.buildingId =:buildingId) "
            + " AND (:maintenanceId IS NULL OR mf.id.maintenanceId =:maintenanceId) "
            + " AND (:maintenanceRequestId IS NULL OR mf.id.maintenanceRequestId =:maintenanceRequestId) "
            + " AND mf.delFlg = 0 ")
})

public class TMaintenanceFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TMaintenanceFacilityPK id;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name = "exclusion_flg", nullable = false)
    private Integer exclusionFlg;

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

    //bi-directional many-to-one association to TMaintenanceRequestHistory
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_history_id", referencedColumnName = "maintenance_history_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_id", referencedColumnName = "maintenance_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_request_id", referencedColumnName = "maintenance_request_id", nullable = false, insertable = false, updatable = false)
    })
    private TMaintenanceRequestHistory TMaintenanceRequestHistory;

    public TMaintenanceFacility() {
    }

    public TMaintenanceFacilityPK getId() {
        return this.id;
    }

    public void setId(TMaintenanceFacilityPK id) {
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

    public Integer getExclusionFlg() {
        return this.exclusionFlg;
    }

    public void setExclusionFlg(Integer exclusionFlg) {
        this.exclusionFlg = exclusionFlg;
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

    public TMaintenanceRequestHistory getTMaintenanceRequestHistory() {
        return this.TMaintenanceRequestHistory;
    }

    public void setTMaintenanceRequestHistory(TMaintenanceRequestHistory TMaintenanceRequestHistory) {
        this.TMaintenanceRequestHistory = TMaintenanceRequestHistory;
    }

}
