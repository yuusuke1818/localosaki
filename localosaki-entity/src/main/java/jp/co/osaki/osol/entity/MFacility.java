package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the m_facility database table.
 *
 */
@Entity
@Table(name = "m_facility")
@NamedQueries({
    @NamedQuery(name = "MFacility.findAll", query = "SELECT m FROM MFacility m"),
//    @NamedQuery(name = "MFacility.findExcel",
//            query = "SELECT f FROM MFacility f LEFT OUTER JOIN f.MFacilityKinds fk "
//            + " WHERE (:corpId IS NULL OR f.id.corpId =:corpId)"
//            + " AND (:corpName IS NULL OR f.TBuilding.MCorp.corpName LIKE :corpName)"
//            + " AND (:buildingNo IS NULL OR f.TBuilding.buildingNo LIKE :buildingNo)"
//            + " AND (:buildingName IS NULL OR f.TBuilding.buildingName LIKE :buildingName)"
//            + " AND (:facilityLedgerNo IS NULL OR f.facilityLedgerNo LIKE :facilityLedgerNo)"
//            + " AND (:facilityName IS NULL OR f.facilityName LIKE :facilityName)"
//            + " AND (:facilityBigKindId IS NULL OR fk.id.facilityBigKindId =:facilityBigKindId)"
//            + " AND (:facilitySmallKindId IS NULL OR fk.id.facilitySmallKindId =:facilitySmallKindId)"
//            + " AND (:stateNow IS NULL OR (f.disposalYmd IS NULL OR :nowDate <= f.disposalYmd))"
//            + " AND (:stateEnd IS NULL OR (f.disposalYmd < :nowDate))"
//            + " AND f.delFlg = 0"
//            + " ORDER BY f.id.corpId ASC, f.TBuilding.buildingNo ASC, f.facilityLedgerNo ASC"),
    @NamedQuery(name = "MFacility.findByMacilityLedgerNo",
            query = "SELECT m FROM MFacility m"
            + " WHERE m.id.corpId =:corpId"
            + " AND m.facilityLedgerNo =:facilityLedgerNo"
            + " AND m.delFlg <> 1"),
    @NamedQuery(name = "MFacility.findReport",
            query = "SELECT DISTINCT m FROM MFacility m INNER JOIN FETCH M.TFreonFillingReports LEFT JOIN FETCH M.MRefrigerant mr LEFT JOIN mr.MRefrigerantYearGwps WHERE M.id.corpId = :corpId"),
    @NamedQuery(name = "MFacility.findBuildingId",
            query = "SELECT f FROM MFacility f"
            + " WHERE (:facilityLedgerNo IS NULL OR f.facilityLedgerNo LIKE :facilityLedgerNo)"
            + " AND (:facilityName IS NULL OR f.facilityName LIKE :facilityName)"
            + " AND f.delFlg <> 1"),
//    @NamedQuery(name = "MFacility.searchFacility",
//            query
//            = "SELECT mf FROM MFacility mf "
//                    + "INNER JOIN mf.TBuilding tb "
//                    + "INNER JOIN tb.MPrefecture mp "
//                    + "INNER JOIN tb.MCorp mc "
//                    + "INNER JOIN mf.MFacilityKinds mfk "
//                    + "INNER JOIN mfk.MCorpFacilityKind mcf "
//                    + "INNER JOIN mcf.MFacilitySmallKind mfs "
//                    + "INNER JOIN mfs.MFacilityBigKind mfb "
//                    + "LEFT OUTER JOIN mf.MRefrigerant mr "
//                    + "WHERE "
//                    + "(:corpId IS NULL OR mf.id.corpId =:corpId)"
//                    + "AND (:corpName IS NULL OR mc.corpName LIKE :corpName) "
//                    + "AND (:buildingId IS NULL OR tb.id.buildingId =:buildingId) "
//                    + "AND (:buildingNo IS NULL OR tb.buildingNo LIKE :buildingNo) "
//                    + "AND (:buildingName IS NULL OR tb.buildingName LIKE :buildingName) "
//                    + "AND (:facilityBigKindId IS NULL OR mcf.id.facilityBigKindId =:facilityBigKindId)"
//                    + "AND (:facilitySmallKindId IS NULL OR mcf.id.facilitySmallKindId =:facilitySmallKindId )"
//                    + "AND (:facilityName IS NULL OR mf.facilityName LIKE :facilityName) "
//                    + "AND (:facilityLedgerNo IS NULL OR mf.facilityLedgerNo LIKE :facilityLedgerNo) "
//                    + "AND (:facilityStateWorkFlg IS NULL OR (mf.disposalYmd IS NULL OR mf.disposalYmd >=:nowDate)) "
//                    + "AND (:facilityStateRemoveFlg IS NULL OR (mf.disposalYmd IS NOT NULL OR mf.disposalYmd <:nowDate)) "
//                    + "AND (:osakiFlg IS NULL OR tb.buildingDelDate IS NULL) "
//                    + "AND (mf.delFlg =:delFlg) "
//                    + "AND (tb.delFlg =:delFlg) ")
})
public class MFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MFacilityPK id;

    @Temporal(TemporalType.DATE)
    @Column(name = "capitalization_date")
    private Date capitalizationDate;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(nullable = false)
    private Integer disposal;

    @Temporal(TemporalType.DATE)
    @Column(name = "disposal_ymd")
    private Date disposalYmd;

    @Column(name = "facility_capacity", precision = 23, scale = 10)
    private BigDecimal facilityCapacity;

    @Column(name = "facility_ledger_no", nullable = false, length = 50)
    private String facilityLedgerNo;

    @Column(name = "facility_name", length = 50)
    private String facilityName;

    @Column(name = "facility_quantity")
    private Integer facilityQuantity;

    @Temporal(TemporalType.DATE)
    @Column(name = "facility_update_plan_ymd")
    private Date facilityUpdatePlanYmd;

    @Column(name = "freon_possession", nullable = false, precision = 23, scale = 10)
    private BigDecimal freonPossession;

    @Column(name = "ins_ation_location", length = 50)
    private String insAtionLocation;

    @Temporal(TemporalType.DATE)
    @Column(name = "introduction_date")
    private Date introductionDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "made_date")
    private Date madeDate;

    @Column(name = "maker_model_no", length = 50)
    private String makerModelNo;

    @Column(name = "maker_name", length = 50)
    private String makerName;

    @Column(name = "owner_ship_code", nullable = false, length = 6)
    private String ownerShipCode;

    @Column(name = "rated_output", precision = 23, scale = 10)
    private BigDecimal ratedOutput;

    @Column(name = "refrigerant_memo", length = 50)
    private String refrigerantMemo;

    @Column(name = "service_life")
    private Integer serviceLife;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Column(length = 50)
    private String use;

    @Version
    @Column(nullable = false)
    private Integer version;

    //bi-directional many-to-one association to MEnergyType
    @ManyToOne
    @JoinColumn(name = "eng_type_cd", nullable = false)
    private MEnergyType MEnergyType;

    //bi-directional many-to-one association to MFacilityUnit
    @ManyToOne
    @JoinColumn(name = "rated_output_unit")
    private MFacilityUnit MFacilityUnit1;

    //bi-directional many-to-one association to MFacilityUnit
    @ManyToOne
    @JoinColumn(name = "facility_capacity_unit_id")
    private MFacilityUnit MFacilityUnit2;

    //bi-directional many-to-one association to MRefrigerant
    @ManyToOne
    @JoinColumn(name = "refrigerant_id")
    private MRefrigerant MRefrigerant;

    //bi-directional many-to-one association to TBuilding
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "building_id", referencedColumnName = "building_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, insertable = false, updatable = false)
    })
    private TBuilding TBuilding;

    //bi-directional many-to-one association to MFacilityKind
    @OneToMany(mappedBy = "MFacility")
    private List<MFacilityKind> MFacilityKinds;

    //bi-directional many-to-one association to TFacilityDocument
    @OneToMany(mappedBy = "MFacility")
    private List<TFacilityDocument> TFacilityDocuments;

    //bi-directional many-to-one association to TFreonFillingReport
    @OneToMany(mappedBy = "MFacility")
    private List<TFreonFillingReport> TFreonFillingReports;

    //bi-directional many-to-one association to TMaintenanceFacility
    @OneToMany(mappedBy = "MFacility")
    private List<TMaintenanceFacility> TMaintenanceFacilities;

    public MFacility() {
    }

    public MFacilityPK getId() {
        return this.id;
    }

    public void setId(MFacilityPK id) {
        this.id = id;
    }

    public Date getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public void setCapitalizationDate(Date capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
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

    public Integer getDisposal() {
        return this.disposal;
    }

    public void setDisposal(Integer disposal) {
        this.disposal = disposal;
    }

    public Date getDisposalYmd() {
        return this.disposalYmd;
    }

    public void setDisposalYmd(Date disposalYmd) {
        this.disposalYmd = disposalYmd;
    }

    public BigDecimal getFacilityCapacity() {
        return this.facilityCapacity;
    }

    public void setFacilityCapacity(BigDecimal facilityCapacity) {
        this.facilityCapacity = facilityCapacity;
    }

    public String getFacilityLedgerNo() {
        return this.facilityLedgerNo;
    }

    public void setFacilityLedgerNo(String facilityLedgerNo) {
        this.facilityLedgerNo = facilityLedgerNo;
    }

    public String getFacilityName() {
        return this.facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Integer getFacilityQuantity() {
        return this.facilityQuantity;
    }

    public void setFacilityQuantity(Integer facilityQuantity) {
        this.facilityQuantity = facilityQuantity;
    }

    public Date getFacilityUpdatePlanYmd() {
        return this.facilityUpdatePlanYmd;
    }

    public void setFacilityUpdatePlanYmd(Date facilityUpdatePlanYmd) {
        this.facilityUpdatePlanYmd = facilityUpdatePlanYmd;
    }

    public BigDecimal getFreonPossession() {
        return this.freonPossession;
    }

    public void setFreonPossession(BigDecimal freonPossession) {
        this.freonPossession = freonPossession;
    }

    public String getInsAtionLocation() {
        return this.insAtionLocation;
    }

    public void setInsAtionLocation(String insAtionLocation) {
        this.insAtionLocation = insAtionLocation;
    }

    public Date getIntroductionDate() {
        return this.introductionDate;
    }

    public void setIntroductionDate(Date introductionDate) {
        this.introductionDate = introductionDate;
    }

    public Date getMadeDate() {
        return this.madeDate;
    }

    public void setMadeDate(Date madeDate) {
        this.madeDate = madeDate;
    }

    public String getMakerModelNo() {
        return this.makerModelNo;
    }

    public void setMakerModelNo(String makerModelNo) {
        this.makerModelNo = makerModelNo;
    }

    public String getMakerName() {
        return this.makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public String getOwnerShipCode() {
        return this.ownerShipCode;
    }

    public void setOwnerShipCode(String ownerShipCode) {
        this.ownerShipCode = ownerShipCode;
    }

    public BigDecimal getRatedOutput() {
        return this.ratedOutput;
    }

    public void setRatedOutput(BigDecimal ratedOutput) {
        this.ratedOutput = ratedOutput;
    }

    public String getRefrigerantMemo() {
        return this.refrigerantMemo;
    }

    public void setRefrigerantMemo(String refrigerantMemo) {
        this.refrigerantMemo = refrigerantMemo;
    }

    public Integer getServiceLife() {
        return this.serviceLife;
    }

    public void setServiceLife(Integer serviceLife) {
        this.serviceLife = serviceLife;
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

    public String getUse() {
        return this.use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public MEnergyType getMEnergyType() {
        return this.MEnergyType;
    }

    public void setMEnergyType(MEnergyType MEnergyType) {
        this.MEnergyType = MEnergyType;
    }

    public MFacilityUnit getMFacilityUnit1() {
        return this.MFacilityUnit1;
    }

    public void setMFacilityUnit1(MFacilityUnit MFacilityUnit1) {
        this.MFacilityUnit1 = MFacilityUnit1;
    }

    public MFacilityUnit getMFacilityUnit2() {
        return this.MFacilityUnit2;
    }

    public void setMFacilityUnit2(MFacilityUnit MFacilityUnit2) {
        this.MFacilityUnit2 = MFacilityUnit2;
    }

    public MRefrigerant getMRefrigerant() {
        return this.MRefrigerant;
    }

    public void setMRefrigerant(MRefrigerant MRefrigerant) {
        this.MRefrigerant = MRefrigerant;
    }

    public TBuilding getTBuilding() {
        return this.TBuilding;
    }

    public void setTBuilding(TBuilding TBuilding) {
        this.TBuilding = TBuilding;
    }

    public List<MFacilityKind> getMFacilityKinds() {
        return this.MFacilityKinds;
    }

    public void setMFacilityKinds(List<MFacilityKind> MFacilityKinds) {
        this.MFacilityKinds = MFacilityKinds;
    }

    public MFacilityKind addMFacilityKind(MFacilityKind MFacilityKind) {
        getMFacilityKinds().add(MFacilityKind);
        MFacilityKind.setMFacility(this);

        return MFacilityKind;
    }

    public MFacilityKind removeMFacilityKind(MFacilityKind MFacilityKind) {
        getMFacilityKinds().remove(MFacilityKind);
        MFacilityKind.setMFacility(null);

        return MFacilityKind;
    }

    public List<TFacilityDocument> getTFacilityDocuments() {
        return this.TFacilityDocuments;
    }

    public void setTFacilityDocuments(List<TFacilityDocument> TFacilityDocuments) {
        this.TFacilityDocuments = TFacilityDocuments;
    }

    public TFacilityDocument addTFacilityDocument(TFacilityDocument TFacilityDocument) {
        getTFacilityDocuments().add(TFacilityDocument);
        TFacilityDocument.setMFacility(this);

        return TFacilityDocument;
    }

    public TFacilityDocument removeTFacilityDocument(TFacilityDocument TFacilityDocument) {
        getTFacilityDocuments().remove(TFacilityDocument);
        TFacilityDocument.setMFacility(null);

        return TFacilityDocument;
    }

    public List<TFreonFillingReport> getTFreonFillingReports() {
        return this.TFreonFillingReports;
    }

    public void setTFreonFillingReports(List<TFreonFillingReport> TFreonFillingReports) {
        this.TFreonFillingReports = TFreonFillingReports;
    }

    public TFreonFillingReport addTFreonFillingReport(TFreonFillingReport TFreonFillingReport) {
        getTFreonFillingReports().add(TFreonFillingReport);
        TFreonFillingReport.setMFacility(this);

        return TFreonFillingReport;
    }

    public TFreonFillingReport removeTFreonFillingReport(TFreonFillingReport TFreonFillingReport) {
        getTFreonFillingReports().remove(TFreonFillingReport);
        TFreonFillingReport.setMFacility(null);

        return TFreonFillingReport;
    }

    public List<TMaintenanceFacility> getTMaintenanceFacilities() {
        return this.TMaintenanceFacilities;
    }

    public void setTMaintenanceFacilities(List<TMaintenanceFacility> TMaintenanceFacilities) {
        this.TMaintenanceFacilities = TMaintenanceFacilities;
    }

    public TMaintenanceFacility addTMaintenanceFacility(TMaintenanceFacility TMaintenanceFacility) {
        getTMaintenanceFacilities().add(TMaintenanceFacility);
        TMaintenanceFacility.setMFacility(this);

        return TMaintenanceFacility;
    }

    public TMaintenanceFacility removeTMaintenanceFacility(TMaintenanceFacility TMaintenanceFacility) {
        getTMaintenanceFacilities().remove(TMaintenanceFacility);
        TMaintenanceFacility.setMFacility(null);

        return TMaintenanceFacility;
    }

}
