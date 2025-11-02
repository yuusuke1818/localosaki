package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the t_maintenance_request database table.
 *
 */
@Entity
@Table(name = "t_maintenance_request")
@NamedQueries({
    @NamedQuery(name = "TMaintenanceRequest.findAll", query = "SELECT t FROM TMaintenanceRequest t"),
    @NamedQuery(name = "TMaintenanceRequest.findJoin",
            query = "SELECT t FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.MCorp mc"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND t.delFlg <>'1' "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND tb.delFlg  <>'1' "
            + " ORDER BY t.maintenanceManageNo DESC"),
    @NamedQuery(name = "TMaintenanceRequest.findJoinCount",
            query = "SELECT COUNT(t) FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.MCorp mc"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND t.delFlg <>'1' "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND tb.delFlg  <>'1' "),
    @NamedQuery(name = "TMaintenanceRequestEdit.findJoin",
            query = "SELECT tmr FROM TMaintenanceRequest tmr INNER JOIN tmr.TMaintenance tm "
            + "INNER JOIN tmr.TMaintenanceRequestHistories trh "
            + "LEFT OUTER JOIN trh.TMaintenanceDocuments tmd "
            + "LEFT OUTER JOIN tmd.TRelatedDocument "
            + "LEFT OUTER JOIN trh.TMaintenanceFacilities tmf "
            + "WHERE "
            + "tmr.id.corpId = :corpId "
            + "AND tmr.id.maintenanceId =:maintenanceId "
            + "AND tmr.id.maintenanceRequestId =:maintenanceRequestId  "
            + " ORDER BY tmr.maintenanceManageNo ASC "),
    @NamedQuery(name = "TMaintenanceRequest.findExcelForFacility",
            query = "SELECT mr FROM TMaintenanceRequest mr "
            + " WHERE (mr.id.corpId = :corpId)"
            + " AND (mr.id.buildingId = :buildingId)"
            + " AND (mr.id.maintenanceId IN :maintenanceId)"
            + " AND (mr.requestStatus = :requestStatus)"
            + " AND mr.delFlg = 0"
            + " ORDER BY mr.executePlanStartDate ASC, mr.executePlanEndDate ASC"),
    @NamedQuery(name = "TMaintenanceRequest.findJoinForBuilding", // 建物担当用
            query = "SELECT t FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.TBuildingPersons tbp "
            + " INNER JOIN  tb.MCorp mc"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND (:personCorpId IS NULL OR tbp.id.personCorpId =:personCorpId) "
            + " AND (:personId IS NULL OR tbp.id.personId =:personId) "
            + " AND (tbp.delFlg = 0) "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND t.delFlg <>'1' "
            + " AND tb.delFlg  <>'1' "
            + " ORDER BY t.maintenanceManageNo DESC"),
    @NamedQuery(name = "TMaintenanceRequest.findJoinForBuildingCount", // 建物担当用
            query = "SELECT COUNT(DISTINCT t) FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.TBuildingPersons tbp "
            + " INNER JOIN  tb.MCorp mc"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND (:personCorpId IS NULL OR tbp.id.personCorpId =:personCorpId) "
            + " AND (:personId IS NULL OR tbp.id.personId =:personId) "
            + " AND (tbp.delFlg = 0) "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND t.delFlg <>'1' "
            + " AND tb.delFlg  <>'1' "),
    @NamedQuery(name = "TMaintenanceRequest.findJoinForCorp", // 企業担当用
            query = "SELECT t FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.MCorp mc"
            + " INNER JOIN  mc.MCorpPersons mcp"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND (:personCorpId IS NULL OR mcp.id.personCorpId =:personCorpId) "
            + " AND (:personId IS NULL OR mcp.id.personId =:personId) "
            + " AND (:personId IS NULL OR mcp.authorityType ='0') "
            + " AND (mcp.delFlg = 0) "
            + " AND t.delFlg <>'1' "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND tb.delFlg  <>'1' "
            + " ORDER BY t.maintenanceManageNo DESC"),
    @NamedQuery(name = "TMaintenanceRequest.findJoinForCorpCount", // 企業担当用
            query = "SELECT COUNT(DISTINCT t) FROM TMaintenanceRequest t"
            + " INNER JOIN  t.TMaintenanceRequestHistories tmrh"
            + " INNER JOIN  tmrh.TMaintenanceFacilities tmf"
            + " INNER JOIN  tmf.MFacility mfc"
            + " INNER JOIN  mfc.TBuilding tb"
            + " INNER JOIN  tb.MCorp mc"
            + " INNER JOIN  mc.MCorpPersons mcp"
            + " WHERE (:maintenanceManageNo is null or t.maintenanceManageNo = :maintenanceManageNo)"
            + " AND (:requestStatusFlg is null or t.requestStatus IN :requestStatus)"
            + " AND (:corpId is null or mc.corpId LIKE :corpId)"
            + " AND (:corpName is null or mc.corpName LIKE :corpName)"
            + " AND (:corpIdOrName is null or mc.corpId LIKE :corpIdOrName or mc.corpName LIKE :corpIdOrName)"
            + " AND (:buildingNo is null or tb.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName is null or tb.buildingName LIKE :buildingName)"
            + " AND (:buildingNoOrName is null or tb.buildingNo LIKE :buildingNoOrName or tb.buildingName LIKE :buildingNoOrName)"
            + " AND (:requestTypeFlg is null or t.requestType IN :requestType)"
            + " AND (:requestOverview is null or t.requestOverview LIKE :requestOverview)"
            + " AND (:facilityLedgerNo IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNo) "
            + " AND (:facilityName IS NULL OR mfc.facilityName LIKE :facilityName) "
            + " AND (:facilityLedgerNoOrName IS NULL OR mfc.facilityLedgerNo LIKE :facilityLedgerNoOrName OR mfc.facilityName LIKE :facilityLedgerNoOrName) "
            + " AND (:personCorpId IS NULL OR mcp.id.personCorpId =:personCorpId) "
            + " AND (:personId IS NULL OR mcp.id.personId =:personId) "
            + " AND (:personId IS NULL OR mcp.authorityType ='0') "
            + " AND (mcp.delFlg = 0) "
            + " AND t.delFlg <>'1' "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND ("
            + " (:buildingTenant = '0' AND tb.divisionCorpId IS NULL AND tb.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND tb.divisionCorpId IS NOT NULL AND tb.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND tb.delFlg  <>'1' "),})
public class TMaintenanceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TMaintenanceRequestPK id;

    @Temporal(TemporalType.DATE)
    @Column(name = "accrual_date")
    private Date accrualDate;

    @Column(length = 300)
    private String cause;

    @Temporal(TemporalType.DATE)
    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "contact_us", nullable = false, length = 6)
    private String contactUs;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Temporal(TemporalType.DATE)
    @Column(name = "execute_plan_end_date")
    private Date executePlanEndDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "execute_plan_start_date")
    private Date executePlanStartDate;

    @Column(name = "maintenance_fax_no", length = 100)
    private String maintenanceFaxNo;

    @Column(name = "maintenance_mail_address", length = 100)
    private String maintenanceMailAddress;

    @Column(name = "maintenance_manage_no", nullable = false)
    private Long maintenanceManageNo;

    @Column(name = "maintenance_tel_no", length = 100)
    private String maintenanceTelNo;

    @Column(name = "maintenance_trader_name", length = 100)
    private String maintenanceTraderName;

    @Column(name = "maintenance_trader_person_name", length = 50)
    private String maintenanceTraderPersonName;

    @Column(name = "other_contact_info", length = 300)
    private String otherContactInfo;

    @Temporal(TemporalType.DATE)
    @Column(name = "planning_date")
    private Date planningDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "request_date")
    private Date requestDate;

    @Column(name = "request_overview", length = 30)
    private String requestOverview;

    @Temporal(TemporalType.DATE)
    @Column(name = "request_regist_date", nullable = false)
    private Date requestRegistDate;

    @Column(name = "request_status", nullable = false, length = 6)
    private String requestStatus;

    @Column(name = "request_type", nullable = false, length = 6)
    private String requestType;

    @Column(length = 500)
    private String situation;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to TFreonFillingReport
    @OneToMany(mappedBy = "TMaintenanceRequest")
    private List<TFreonFillingReport> TFreonFillingReports;

    //bi-directional many-to-one association to MPerson
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "request_corp_id", referencedColumnName = "corp_id", nullable = false),
        @JoinColumn(name = "request_person_id", referencedColumnName = "person_id", nullable = false)
    })
    private MPerson MPerson1;

    //bi-directional many-to-one association to MPerson
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "reception_corp_id", referencedColumnName = "corp_id"),
        @JoinColumn(name = "reception_person_id", referencedColumnName = "person_id")
    })
    private MPerson MPerson2;

    //bi-directional many-to-one association to TMaintenance
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "maintenance_id", referencedColumnName = "maintenance_id", nullable = false, insertable = false, updatable = false)
    })
    private TMaintenance TMaintenance;

    //bi-directional many-to-one association to TMaintenanceRequestHistory
    @OneToMany(mappedBy = "TMaintenanceRequest")
    private List<TMaintenanceRequestHistory> TMaintenanceRequestHistories;

    public TMaintenanceRequest() {
    }

    public TMaintenanceRequestPK getId() {
        return this.id;
    }

    public void setId(TMaintenanceRequestPK id) {
        this.id = id;
    }

    public Date getAccrualDate() {
        return this.accrualDate;
    }

    public void setAccrualDate(Date accrualDate) {
        this.accrualDate = accrualDate;
    }

    public String getCause() {
        return this.cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Date getCompletionDate() {
        return this.completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getContactUs() {
        return this.contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
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

    public Date getExecutePlanEndDate() {
        return this.executePlanEndDate;
    }

    public void setExecutePlanEndDate(Date executePlanEndDate) {
        this.executePlanEndDate = executePlanEndDate;
    }

    public Date getExecutePlanStartDate() {
        return this.executePlanStartDate;
    }

    public void setExecutePlanStartDate(Date executePlanStartDate) {
        this.executePlanStartDate = executePlanStartDate;
    }

    public String getMaintenanceFaxNo() {
        return this.maintenanceFaxNo;
    }

    public void setMaintenanceFaxNo(String maintenanceFaxNo) {
        this.maintenanceFaxNo = maintenanceFaxNo;
    }

    public String getMaintenanceMailAddress() {
        return this.maintenanceMailAddress;
    }

    public void setMaintenanceMailAddress(String maintenanceMailAddress) {
        this.maintenanceMailAddress = maintenanceMailAddress;
    }

    public Long getMaintenanceManageNo() {
        return this.maintenanceManageNo;
    }

    public void setMaintenanceManageNo(Long maintenanceManageNo) {
        this.maintenanceManageNo = maintenanceManageNo;
    }

    public String getMaintenanceTelNo() {
        return this.maintenanceTelNo;
    }

    public void setMaintenanceTelNo(String maintenanceTelNo) {
        this.maintenanceTelNo = maintenanceTelNo;
    }

    public String getMaintenanceTraderName() {
        return this.maintenanceTraderName;
    }

    public void setMaintenanceTraderName(String maintenanceTraderName) {
        this.maintenanceTraderName = maintenanceTraderName;
    }

    public String getMaintenanceTraderPersonName() {
        return this.maintenanceTraderPersonName;
    }

    public void setMaintenanceTraderPersonName(String maintenanceTraderPersonName) {
        this.maintenanceTraderPersonName = maintenanceTraderPersonName;
    }

    public String getOtherContactInfo() {
        return this.otherContactInfo;
    }

    public void setOtherContactInfo(String otherContactInfo) {
        this.otherContactInfo = otherContactInfo;
    }

    public Date getPlanningDate() {
        return this.planningDate;
    }

    public void setPlanningDate(Date planningDate) {
        this.planningDate = planningDate;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestOverview() {
        return this.requestOverview;
    }

    public void setRequestOverview(String requestOverview) {
        this.requestOverview = requestOverview;
    }

    public Date getRequestRegistDate() {
        return this.requestRegistDate;
    }

    public void setRequestRegistDate(Date requestRegistDate) {
        this.requestRegistDate = requestRegistDate;
    }

    public String getRequestStatus() {
        return this.requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSituation() {
        return this.situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
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

    public List<TFreonFillingReport> getTFreonFillingReports() {
        return this.TFreonFillingReports;
    }

    public void setTFreonFillingReports(List<TFreonFillingReport> TFreonFillingReports) {
        this.TFreonFillingReports = TFreonFillingReports;
    }

    public TFreonFillingReport addTFreonFillingReport(TFreonFillingReport TFreonFillingReport) {
        getTFreonFillingReports().add(TFreonFillingReport);
        TFreonFillingReport.setTMaintenanceRequest(this);

        return TFreonFillingReport;
    }

    public TFreonFillingReport removeTFreonFillingReport(TFreonFillingReport TFreonFillingReport) {
        getTFreonFillingReports().remove(TFreonFillingReport);
        TFreonFillingReport.setTMaintenanceRequest(null);

        return TFreonFillingReport;
    }

    public MPerson getMPerson1() {
        return this.MPerson1;
    }

    public void setMPerson1(MPerson MPerson1) {
        this.MPerson1 = MPerson1;
    }

    public MPerson getMPerson2() {
        return this.MPerson2;
    }

    public void setMPerson2(MPerson MPerson2) {
        this.MPerson2 = MPerson2;
    }

    public TMaintenance getTMaintenance() {
        return this.TMaintenance;
    }

    public void setTMaintenance(TMaintenance TMaintenance) {
        this.TMaintenance = TMaintenance;
    }

    public List<TMaintenanceRequestHistory> getTMaintenanceRequestHistories() {
        return this.TMaintenanceRequestHistories;
    }

    public void setTMaintenanceRequestHistories(List<TMaintenanceRequestHistory> TMaintenanceRequestHistories) {
        this.TMaintenanceRequestHistories = TMaintenanceRequestHistories;
    }

    public TMaintenanceRequestHistory addTMaintenanceRequestHistory(TMaintenanceRequestHistory TMaintenanceRequestHistory) {
        getTMaintenanceRequestHistories().add(TMaintenanceRequestHistory);
        TMaintenanceRequestHistory.setTMaintenanceRequest(this);

        return TMaintenanceRequestHistory;
    }

    public TMaintenanceRequestHistory removeTMaintenanceRequestHistory(TMaintenanceRequestHistory TMaintenanceRequestHistory) {
        getTMaintenanceRequestHistories().remove(TMaintenanceRequestHistory);
        TMaintenanceRequestHistory.setTMaintenanceRequest(null);

        return TMaintenanceRequestHistory;
    }

}
