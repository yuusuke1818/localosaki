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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the t_building database table.
 *
 */
@Entity
@Table(name = "t_building")
@NamedQueries({
    @NamedQuery(name = "TBuilding.findJoinForPersoninBuilding",     //企業の全建物
            query = "SELECT DISTINCT j FROM TBuilding j LEFT OUTER JOIN j.MPrefecture m"
            + " LEFT OUTER JOIN j.TBuildingGroups tbg "
            + " ON tbg.delFlg = 0 "
            + " LEFT OUTER JOIN tbg.MChildGroup cg "
            + " ON cg.delFlg = 0 "
            + " LEFT OUTER JOIN cg.MParentGroup pg "
            + " ON pg.delFlg = 0 "
            + " WHERE j.id.corpId =:corpId"
            + " AND (:buildingNo is null or j.buildingNo like :buildingNo) "
            + " AND (:buildingName is null or j.buildingName like :buildingName) "
            + " AND (:buildingNoOrName is null or j.buildingNo like :buildingNoOrName or j.buildingName like :buildingNoOrName) "
            + " AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "
            + " AND (:nowflg is null or (:nowDate <= j.totalEndYm or j.totalEndYm IS NULL ) and  j.buildingDelDate IS NULL ) "
            + " AND (:endflg is null or (:nowDate > j.totalEndYm )) "
            + " AND (:delflg IS NULL AND (j.buildingDelDate IS NULL OR :notdelflg IS NOT NULL )"    //削除済み条件指定がなくて、削除されていない(削除者記録なし)
                    + " OR (:delflg IS NOT NULL AND j.buildingDelDate IS NOT NULL ) )"    //か 削除済み条件指定があり、削除者が記録されてる
            + " AND ("
            + " (:buildingTenant = '0' AND j.divisionCorpId IS NULL AND j.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND j.divisionCorpId IS NOT NULL AND j.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND j.delFlg = 0 "
            + " AND (:parentGroupId IS NULL OR tbg.id.parentGroupId = :parentGroupId) "
            + " AND (:childGroupId IS NULL OR tbg.id.childGroupId = :childGroupId) "
            + "ORDER BY j.buildingNo ASC"),
    @NamedQuery(name = "TBuilding.findJoinForPersoninBuildingPerson",   //担当している建物
            query = "SELECT DISTINCT j FROM TBuilding j LEFT OUTER JOIN j.MPrefecture m INNER JOIN j.TBuildingPersons tbp "
            + " LEFT OUTER JOIN j.TBuildingGroups tbg "
            + " ON tbg.delFlg = 0 "
            + " LEFT OUTER JOIN tbg.MChildGroup cg "
            + " ON cg.delFlg = 0 "
            + " LEFT OUTER JOIN cg.MParentGroup pg "
            + " ON pg.delFlg = 0 "
            + " WHERE j.id.corpId =:corpId"
            + " AND (:buildingNo is null or j.buildingNo like :buildingNo) "
            + " AND (:buildingName is null or j.buildingName like :buildingName) "
            + " AND (:buildingNoOrName is null or j.buildingNo like :buildingNoOrName or j.buildingName like :buildingNoOrName) "
            + " AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "
            + " AND (:nowflg is null or (:nowDate <= j.totalEndYm or j.totalEndYm IS NULL ) and  j.buildingDelDate IS NULL ) "
            + " AND (:endflg is null or (:nowDate > j.totalEndYm )) "
            + " AND (:delflg IS NULL AND (j.buildingDelDate IS NULL OR :notdelflg IS NOT NULL )"    //削除済み条件指定がなくて、削除されていない(削除者記録なし)
                    + " OR (:delflg IS NOT NULL AND j.buildingDelDate IS NOT NULL ) )"    //か 削除済み条件指定があり、削除者が記録されてる
            + " AND j.delFlg = 0 "
            + " AND (tbp.id.personId = :authorityPersonId) "
            + " AND (tbp.id.personCorpId = :authorityCorpId) "
            + " AND ("
            + " (:buildingTenant = '0' AND j.divisionCorpId IS NULL AND j.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND j.divisionCorpId IS NOT NULL AND j.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND (tbp.delFlg = 0) "
            + " AND (:parentGroupId IS NULL OR tbg.id.parentGroupId = :parentGroupId) "
            + " AND (:childGroupId IS NULL OR tbg.id.childGroupId = :childGroupId) "
            + "ORDER BY j.buildingNo ASC"),
    @NamedQuery(name = "TBuilding.findJoinForBuildingGroup",
            query = "SELECT j FROM TBuilding j LEFT OUTER JOIN j.MPrefecture m"
            + " WHERE j.id.corpId =:corpId"
            + " AND (:buildingNo is null or j.buildingNo like :buildingNo) "
            + " AND (:buildingName is null or j.buildingName like :buildingName) "
            + " AND (:buildingNoOrName is null or j.buildingNo like :buildingNoOrName or j.buildingName like :buildingNoOrName) "
            + " AND (:prefectureCd is null or m.prefectureCd =:prefectureCd) "
            + " AND (:nyukyoTypeCd is null or j.nyukyoTypeCd =:nyukyoTypeCd) "
            + " AND (:nowflg is null or (:nowDate <= j.totalEndYm or j.totalEndYm IS NULL ) and  j.buildingDelDate IS NULL ) "
            + " AND (:endflg is null or (:nowDate > j.totalEndYm )) "
            + " AND (:delflg IS NULL AND (j.buildingDelDate IS NULL OR :notdelflg IS NOT NULL )"    //削除済み条件指定がなくて、削除されていない(削除者記録なし)
            + " OR (:delflg IS NOT NULL AND j.buildingDelDate IS NOT NULL ) )"    //か 削除済み条件指定があり、削除者が記録されてる
            + " AND ("
            + " (:buildingTenant = '0' AND j.divisionCorpId IS NULL AND j.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND j.divisionCorpId IS NOT NULL AND j.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " AND j.delFlg = 0 "
            + "ORDER BY j.buildingNo ASC"),
    @NamedQuery(name = "TBuilding.energyInputSearch",
            query = "SELECT DISTINCT t FROM TBuilding t "
            + "LEFT OUTER JOIN FETCH t.TBuildingPersons p "
            + "LEFT OUTER JOIN FETCH p.MPerson mp "
            + "LEFT JOIN FETCH t.MMunicipality mm "
            + "LEFT JOIN FETCH mm.MRegionType "
            + " WHERE t.id.corpId =:corpId"
            + " AND (:prefectureCd IS NULL OR t.MPrefecture.prefectureCd =:prefectureCd)"
            + " AND ((:buildingNo IS NULL OR t.buildingNo LIKE :buildingNo) OR (:buildingName IS NULL OR t.buildingName LIKE :buildingName)) "
            + " AND (:personId IS NULL OR (p.id.personId LIKE :personId AND p.delFlg = 0 AND p.id.personCorpId = :corpId))"
            + " AND (:personName IS NULL OR (mp.personName LIKE :personName AND p.delFlg = 0 AND p.id.personCorpId = :corpId))"
            + " AND (:delFlg IS NULL OR t.delFlg =:delFlg)"
            + " AND (:totalStartYmString IS NULL OR (t.totalEndYm IS NULL OR t.totalEndYm >=:totalStartYm))"
            + " AND (:totalEndYmString IS NULL OR (t.totalEndYm IS NOT NULL AND t.totalEndYm <:totalEndYm))"
            + " AND (:buildingDelPersonId IS NULL OR t.buildingDelPersonId IS NOT NULL)"
            + " AND (:buildingDelPersonId IS NOT NULL OR t.buildingDelPersonId IS NULL)"
            + " AND ("
            + " (:buildingTenant = '0' AND t.divisionCorpId IS NULL AND t.divisionBuildingId IS NULL) OR "
            + " (:buildingTenant = '1' AND t.divisionCorpId IS NOT NULL AND t.divisionBuildingId IS NOT NULL) OR "
            + " (:buildingTenant is null) "
            + " ) "
            + " ORDER BY t.buildingNo, p.id.personId"),
    @NamedQuery(name = "TBuilding.findExcel",
            query = "SELECT b FROM TBuilding b "
            + " WHERE (:corpId IS NULL OR b.id.corpId =:corpId)"
            + " AND (:corpName IS NULL OR b.MCorp.corpName LIKE :corpName)"
            + " AND (:prefectureCd IS NULL OR b.MPrefecture.prefectureCd =:prefectureCd)"
            + " AND (:buildingNo IS NULL OR b.buildingNo LIKE :buildingNo)"
            + " AND (:buildingName IS NULL OR b.buildingName LIKE :buildingName)"
            + " AND (:nyukyoTypeCd IS NULL OR b.nyukyoTypeCd =:nyukyoTypeCd)"
            + " AND (:stateNow IS NULL OR (b.totalEndYm IS NULL OR :nowDate <= b.totalEndYm))"
            + " AND (:stateEnd IS NULL OR (b.totalEndYm < :nowDate))"
            + " AND (:stateDeleted IS NULL OR (b.buildingDelPersonId IS NOT NULL))"
            + " AND b.delFlg = 0"
            + " ORDER BY b.id.corpId ASC, b.buildingNo ASC"),
    @NamedQuery(name = "TBuilding.findExcelForFreon",
            query = "SELECT b FROM TBuilding b "
            + " WHERE (:corpId IS NULL OR b.id.corpId =:corpId)"
            + " AND b.freonDischargeOffice = 1"
            + " AND b.delFlg = 0"
            + " AND b.buildingDelDate IS NULL "
            + " ORDER BY b.id.corpId ASC, b.MPrefecture.prefectureCd ASC, b.buildingNo ASC"),
    @NamedQuery(name = "TBuilding.energyInputYearBuildingNo",
            query = "SELECT tb FROM TBuilding tb"),
    @NamedQuery(name = "TBuilding.findReport",
            query = "SELECT tb FROM TBuilding tb WHERE TB.id.corpId = :corpId"),
    @NamedQuery(name = "TBuilding.findReport2",
            query = "SELECT tb FROM TBuilding tb INNER JOIN FETCH TB.TMaintenances WHERE TB.id.corpId = :corpId AND TB.buildingNo = :buildingNo"),
    @NamedQuery(name = "TBuilding.findByBuildingNo",
            query = "SELECT tb FROM TBuilding tb "
            + " WHERE tb.id.corpId =:corpId "
            + " AND tb.buildingNo =:buildingNo "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND tb.delFlg = 0 "),
    @NamedQuery(name = "TBuilding.findByBuildingNoForCorpPerson",
            query = "SELECT tb FROM TBuilding tb INNER JOIN tb.MCorp mc INNER JOIN mc.MCorpPersons mcp "
            + " WHERE tb.id.corpId =:corpId "
            + " AND tb.buildingNo =:buildingNo "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND tb.delFlg <> 1 "
            + " AND mcp.id.corpId =:corpId "
            + " AND mcp.id.personCorpId =:personCorpId "
            + " AND mcp.id.personId =:loginPersonId "
            + " AND mcp.authorityType ='0' "
            + " AND mcp.delFlg <> 1 "
    ),
    @NamedQuery(name = "TBuilding.findByBuildingNoForBuildingPerson",
            query = "SELECT tb FROM TBuilding tb INNER JOIN tb.TBuildingPersons tbps "
            + " WHERE tb.id.corpId =:corpId "
            + " AND tb.buildingNo =:buildingNo "
            + " AND tb.buildingDelPersonId IS NULL "
            + " AND tb.delFlg <> 1 "
            + " AND tbps.id.corpId =:corpId "
            + " AND tbps.id.personCorpId =:personCorpId "
            + " AND tbps.id.personId =:loginPersonId "
            + " AND tbps.delFlg <> 1 ")
})

public class TBuilding implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TBuildingPK id;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(name = "address_building", length = 100)
    private String addressBuilding;

    @Column(name = "basement_count", length = 2)
    private String basementCount;

    @Column(length = 2000)
    private String biko;

    @Column(name = "building_del_date")
    private Timestamp buildingDelDate;

    @Column(name = "building_del_person_corp_id", length = 50)
    private String buildingDelPersonCorpId;

    @Column(name = "building_del_person_id", length = 50)
    private String buildingDelPersonId;

    @Column(name = "building_name", nullable = false, length = 100)
    private String buildingName;

    @Column(name = "building_name_kana", length = 200)
    private String buildingNameKana;

    @Column(name = "building_no", nullable = false, length = 150)
    private String buildingNo;

    @Column(name = "building_tansyuku_name", length = 40)
    private String buildingTansyukuName;

    @Column(name="building_type", nullable=false, length=6)
    private String buildingType;

    @Column(name = "common_used_rate", precision = 13, scale = 10)
    private BigDecimal commonUsedRate;

    @Temporal(TemporalType.DATE)
    @Column(name = "conpleted_ym")
    private Date conpletedYm;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "del_flg", nullable = false)
    private Integer delFlg;

    @Column(name="division_building_id")
    private Long divisionBuildingId;

    @Column(name="division_corp_id", length=50)
    private String divisionCorpId;

    @Column(name = "eng_manage_factory_no", length = 7)
    private String engManageFactoryNo;

    @Column(name = "eng_manage_factory_type", length = 6)
    private String engManageFactoryType;

    @Column(name = "estimate_use", nullable = false, length = 6)
    private String estimateUse;

    @Column(name = "fax_no", length = 100)
    private String faxNo;

    @Column(name = "floor_count", length = 2)
    private String floorCount;

    @Column(name = "freon_discharge_office", nullable = false)
    private Integer freonDischargeOffice;

    @Column(name = "kubun_shoyu_rate", precision = 13, scale = 10)
    private BigDecimal kubunShoyuRate;

    @Column(name = "nyukyo_type_cd", nullable = false, length = 6)
    private String nyukyoTypeCd;

    @Column(name="public_flg", nullable=false)
    private Integer publicFlg;

    @Column(name = "tel_no", length = 100)
    private String telNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "total_end_ym")
    private Date totalEndYm;

    @Temporal(TemporalType.DATE)
    @Column(name = "total_start_ym")
    private Date totalStartYm;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(name = "zip_cd", length = 10)
    private String zipCd;

    //bi-directional many-to-one association to MWeatherCity
    @ManyToOne
    @JoinColumn(name="city_cd")
    private MWeatherCity MWeatherCity;

    //bi-directional many-to-one association to TSignageContent
    @OneToMany(mappedBy="TBuilding")
    private List<TSignageContent> TSignageContents;

    //bi-directional many-to-one association to MBuildUnitDenominator
    @OneToMany(mappedBy = "TBuilding")
    private List<MBuildUnitDenominator> MBuildUnitDenominators;

    //bi-directional many-to-one association to MEnergyUseTargetMonthly
    @OneToMany(mappedBy = "TBuilding")
    private List<MEnergyUseTargetMonthly> MEnergyUseTargetMonthlies;

    //bi-directional many-to-one association to MFacility
    @OneToMany(mappedBy = "TBuilding")
    private List<MFacility> MFacilities;

    //bi-directional many-to-one association to TAvailableEnergy
    @OneToMany(mappedBy = "TBuilding")
    private List<TAvailableEnergy> TAvailableEnergies;

    //bi-directional many-to-one association to MCorp
    @ManyToOne
    @JoinColumn(name = "corp_id", nullable = false, insertable = false, updatable = false)
    private MCorp MCorp;

    //bi-directional many-to-one association to MMunicipality
    @ManyToOne
    @JoinColumn(name = "municipality_cd")
    private MMunicipality MMunicipality;

    //bi-directional many-to-one association to MPrefecture
    @ManyToOne
    @JoinColumn(name = "prefecture_cd", nullable = false)
    private MPrefecture MPrefecture;

    //bi-directional many-to-one association to TBuildingDocument
    @OneToMany(mappedBy = "TBuilding")
    private List<TBuildingDocument> TBuildingDocuments;

    //bi-directional many-to-one association to TBuildingEstimateKind
    @OneToMany(mappedBy = "TBuilding")
    private List<TBuildingEstimateKind> TBuildingEstimateKinds;

    //bi-directional many-to-one association to TBuildingGroup
    @OneToMany(mappedBy = "TBuilding")
    private List<TBuildingGroup> TBuildingGroups;

    //bi-directional many-to-one association to TBuildingPerson
    @OneToMany(mappedBy = "TBuilding")
    private List<TBuildingPerson> TBuildingPersons;

    //bi-directional many-to-one association to TBuildingSubtype
    @OneToMany(mappedBy = "TBuilding")
    private List<TBuildingSubtype> TBuildingSubtypes;

    //bi-directional many-to-one association to TMaintenance
    @OneToMany(mappedBy = "TBuilding")
    private List<TMaintenance> TMaintenances;

    //bi-directional many-to-one association to TBuildingPlanFulfillment
    @OneToMany(mappedBy="TBuilding")
    private List<TBuildingPlanFulfillment> TBuildingPlanFulfillments;

    //bi-directional many-to-one association to MBuildingDm
    @OneToMany(mappedBy="TBuilding")
    private List<MBuildingDm> MBuildingDms;

    //bi-directional many-to-one association to MBuildingSm
    @OneToMany(mappedBy="TBuilding")
    private List<MBuildingSm> MBuildingSm;

    //bi-directional many-to-one association to MAllNightDay
    @OneToMany(mappedBy="TBuilding")
    private List<MAllNightDay> MAllNightDays;

    //bi-directional many-to-one association to MBuildingInfo
    @OneToMany(mappedBy="TBuilding")
    private List<MBuildingInfo> MBuildingInfos;

    //bi-directional many-to-one association to MBuildingSms
    @OneToMany(mappedBy="TBuilding")
    private List<MBuildingSms> MBuildingSms;

    //bi-directional many-to-one association to MClaimantInfo
    @OneToMany(mappedBy="TBuilding")
    private List<MClaimantInfo> MClaimantInfos;

    //bi-directional many-to-one association to MComDivrate
    @OneToMany(mappedBy="TBuilding")
    private List<MComDivrate> MComDivrates;

    //bi-directional many-to-one association to MComType
    @OneToMany(mappedBy="TBuilding")
    private List<MComType> MComTypes;

    //bi-directional many-to-one association to MDevRelation
    @OneToMany(mappedBy="TBuilding")
    private List<MDevRelation> MDevRelations;

    //bi-directional many-to-one association to MMeterGroupName
    @OneToMany(mappedBy="TBuilding")
    private List<MMeterGroupName> MMeterGroupNames;

    //bi-directional many-to-one association to MMeterType
    @OneToMany(mappedBy="TBuilding")
    private List<MMeterType> MMeterTypes;

    //bi-directional many-to-one association to MPriceMenu
    @OneToMany(mappedBy="TBuilding")
    private List<MPriceMenu> MPriceMenus;

    //bi-directional many-to-one association to MPriceMenuFamily
    @OneToMany(mappedBy="TBuilding")
    private List<MPriceMenuFamily> MPriceMenuFamilies;

    //bi-directional many-to-one association to MPriceMenuLighta
    @OneToMany(mappedBy="TBuilding")
    private List<MPriceMenuLighta> MPriceMenuLightas;

    //bi-directional many-to-one association to MPriceMenuLightb
    @OneToMany(mappedBy="TBuilding")
    private List<MPriceMenuLightb> MPriceMenuLightbs;

    //bi-directional many-to-one association to MTenantPriceInfo
    @OneToMany(mappedBy="TBuilding")
    private List<MTenantPriceInfo> MTenantPriceInfos;

    //bi-directional many-to-one association to MTenantSm
    @OneToMany(mappedBy="TBuilding")
    private List<MTenantSm> MTenantSms;

    //bi-directional many-to-one association to MTypeStage
    @OneToMany(mappedBy="TBuilding")
    private List<MTypeStage> MTypeStages;

    //bi-directional many-to-one association to MVarious
    @OneToMany(mappedBy="TBuilding")
    private List<MVarious> MVariouses;

    //bi-directional many-to-one association to TBuildDevMeterRelation
    @OneToMany(mappedBy="TBuilding")
    private List<TBuildDevMeterRelation> TBuildDevMeterRelations;

    public TBuilding() {
    }

    public TBuildingPK getId() {
        return this.id;
    }

    public void setId(TBuildingPK id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressBuilding() {
        return this.addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getBasementCount() {
        return this.basementCount;
    }

    public void setBasementCount(String basementCount) {
        this.basementCount = basementCount;
    }

    public String getBiko() {
        return this.biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public Timestamp getBuildingDelDate() {
        return this.buildingDelDate;
    }

    public void setBuildingDelDate(Timestamp buildingDelDate) {
        this.buildingDelDate = buildingDelDate;
    }

    public String getBuildingDelPersonCorpId() {
        return this.buildingDelPersonCorpId;
    }

    public void setBuildingDelPersonCorpId(String buildingDelPersonCorpId) {
        this.buildingDelPersonCorpId = buildingDelPersonCorpId;
    }

    public String getBuildingDelPersonId() {
        return this.buildingDelPersonId;
    }

    public void setBuildingDelPersonId(String buildingDelPersonId) {
        this.buildingDelPersonId = buildingDelPersonId;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingNameKana() {
        return this.buildingNameKana;
    }

    public void setBuildingNameKana(String buildingNameKana) {
        this.buildingNameKana = buildingNameKana;
    }

    public String getBuildingNo() {
        return this.buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingTansyukuName() {
        return this.buildingTansyukuName;
    }

    public void setBuildingTansyukuName(String buildingTansyukuName) {
        this.buildingTansyukuName = buildingTansyukuName;
    }

    public String getBuildingType() {
        return this.buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public BigDecimal getCommonUsedRate() {
        return this.commonUsedRate;
    }

    public void setCommonUsedRate(BigDecimal commonUsedRate) {
        this.commonUsedRate = commonUsedRate;
    }

    public Date getConpletedYm() {
        return this.conpletedYm;
    }

    public void setConpletedYm(Date conpletedYm) {
        this.conpletedYm = conpletedYm;
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

    public Long getDivisionBuildingId() {
        return this.divisionBuildingId;
    }

    public void setDivisionBuildingId(Long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

    public String getDivisionCorpId() {
        return this.divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public String getEngManageFactoryNo() {
        return this.engManageFactoryNo;
    }

    public void setEngManageFactoryNo(String engManageFactoryNo) {
        this.engManageFactoryNo = engManageFactoryNo;
    }

    public String getEngManageFactoryType() {
        return this.engManageFactoryType;
    }

    public void setEngManageFactoryType(String engManageFactoryType) {
        this.engManageFactoryType = engManageFactoryType;
    }

    public String getEstimateUse() {
        return this.estimateUse;
    }

    public void setEstimateUse(String estimateUse) {
        this.estimateUse = estimateUse;
    }

    public String getFaxNo() {
        return this.faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getFloorCount() {
        return this.floorCount;
    }

    public void setFloorCount(String floorCount) {
        this.floorCount = floorCount;
    }

    public Integer getFreonDischargeOffice() {
        return this.freonDischargeOffice;
    }

    public void setFreonDischargeOffice(Integer freonDischargeOffice) {
        this.freonDischargeOffice = freonDischargeOffice;
    }

    public BigDecimal getKubunShoyuRate() {
        return this.kubunShoyuRate;
    }

    public void setKubunShoyuRate(BigDecimal kubunShoyuRate) {
        this.kubunShoyuRate = kubunShoyuRate;
    }

    public String getNyukyoTypeCd() {
        return this.nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public Integer getPublicFlg() {
            return this.publicFlg;
    }

    public void setPublicFlg(Integer publicFlg) {
            this.publicFlg = publicFlg;
    }

    public String getTelNo() {
        return this.telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Date getTotalEndYm() {
        return this.totalEndYm;
    }

    public void setTotalEndYm(Date totalEndYm) {
        this.totalEndYm = totalEndYm;
    }

    public Date getTotalStartYm() {
        return this.totalStartYm;
    }

    public void setTotalStartYm(Date totalStartYm) {
        this.totalStartYm = totalStartYm;
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

    public String getZipCd() {
        return this.zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

    public MWeatherCity getMWeatherCity() {
            return this.MWeatherCity;
    }

    public void setMWeatherCity(MWeatherCity MWeatherCity) {
            this.MWeatherCity = MWeatherCity;
    }

    public List<TSignageContent> getTSignageContents() {
            return this.TSignageContents;
    }

    public void setTSignageContents(List<TSignageContent> TSignageContents) {
            this.TSignageContents = TSignageContents;
    }

    public TSignageContent addTSignageContent(TSignageContent TSignageContent) {
            getTSignageContents().add(TSignageContent);
            TSignageContent.setTBuilding(this);

            return TSignageContent;
    }

    public TSignageContent removeTSignageContent(TSignageContent TSignageContent) {
            getTSignageContents().remove(TSignageContent);
            TSignageContent.setTBuilding(null);

            return TSignageContent;
    }

    public List<MBuildUnitDenominator> getMBuildUnitDenominators() {
        return this.MBuildUnitDenominators;
    }

    public void setMBuildUnitDenominators(List<MBuildUnitDenominator> MBuildUnitDenominators) {
        this.MBuildUnitDenominators = MBuildUnitDenominators;
    }

    public MBuildUnitDenominator addMBuildUnitDenominator(MBuildUnitDenominator MBuildUnitDenominator) {
        getMBuildUnitDenominators().add(MBuildUnitDenominator);
        MBuildUnitDenominator.setTBuilding(this);

        return MBuildUnitDenominator;
    }

    public MBuildUnitDenominator removeMBuildUnitDenominator(MBuildUnitDenominator MBuildUnitDenominator) {
        getMBuildUnitDenominators().remove(MBuildUnitDenominator);
        MBuildUnitDenominator.setTBuilding(null);

        return MBuildUnitDenominator;
    }

    public List<MEnergyUseTargetMonthly> getMEnergyUseTargetMonthlies() {
        return this.MEnergyUseTargetMonthlies;
    }

    public void setMEnergyUseTargetMonthlies(List<MEnergyUseTargetMonthly> MEnergyUseTargetMonthlies) {
        this.MEnergyUseTargetMonthlies = MEnergyUseTargetMonthlies;
    }

    public MEnergyUseTargetMonthly addMEnergyUseTargetMonthly(MEnergyUseTargetMonthly MEnergyUseTargetMonthly) {
        getMEnergyUseTargetMonthlies().add(MEnergyUseTargetMonthly);
        MEnergyUseTargetMonthly.setTBuilding(this);

        return MEnergyUseTargetMonthly;
    }

    public MEnergyUseTargetMonthly removeMEnergyUseTargetMonthly(MEnergyUseTargetMonthly MEnergyUseTargetMonthly) {
        getMEnergyUseTargetMonthlies().remove(MEnergyUseTargetMonthly);
        MEnergyUseTargetMonthly.setTBuilding(null);

        return MEnergyUseTargetMonthly;
    }

    public List<MFacility> getMFacilities() {
        return this.MFacilities;
    }

    public void setMFacilities(List<MFacility> MFacilities) {
        this.MFacilities = MFacilities;
    }

    public MFacility addMFacility(MFacility MFacility) {
        getMFacilities().add(MFacility);
        MFacility.setTBuilding(this);

        return MFacility;
    }

    public MFacility removeMFacility(MFacility MFacility) {
        getMFacilities().remove(MFacility);
        MFacility.setTBuilding(null);

        return MFacility;
    }

    public List<TAvailableEnergy> getTAvailableEnergies() {
        return this.TAvailableEnergies;
    }

    public void setTAvailableEnergies(List<TAvailableEnergy> TAvailableEnergies) {
        this.TAvailableEnergies = TAvailableEnergies;
    }

    public TAvailableEnergy addTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
        getTAvailableEnergies().add(TAvailableEnergy);
        TAvailableEnergy.setTBuilding(this);

        return TAvailableEnergy;
    }

    public TAvailableEnergy removeTAvailableEnergy(TAvailableEnergy TAvailableEnergy) {
        getTAvailableEnergies().remove(TAvailableEnergy);
        TAvailableEnergy.setTBuilding(null);

        return TAvailableEnergy;
    }

    public MCorp getMCorp() {
        return this.MCorp;
    }

    public void setMCorp(MCorp MCorp) {
        this.MCorp = MCorp;
    }

    public MMunicipality getMMunicipality() {
        return this.MMunicipality;
    }

    public void setMMunicipality(MMunicipality MMunicipality) {
        this.MMunicipality = MMunicipality;
    }

    public MPrefecture getMPrefecture() {
        return this.MPrefecture;
    }

    public void setMPrefecture(MPrefecture MPrefecture) {
        this.MPrefecture = MPrefecture;
    }

    public List<TBuildingDocument> getTBuildingDocuments() {
        return this.TBuildingDocuments;
    }

    public void setTBuildingDocuments(List<TBuildingDocument> TBuildingDocuments) {
        this.TBuildingDocuments = TBuildingDocuments;
    }

    public TBuildingDocument addTBuildingDocument(TBuildingDocument TBuildingDocument) {
        getTBuildingDocuments().add(TBuildingDocument);
        TBuildingDocument.setTBuilding(this);

        return TBuildingDocument;
    }

    public TBuildingDocument removeTBuildingDocument(TBuildingDocument TBuildingDocument) {
        getTBuildingDocuments().remove(TBuildingDocument);
        TBuildingDocument.setTBuilding(null);

        return TBuildingDocument;
    }

    public List<TBuildingEstimateKind> getTBuildingEstimateKinds() {
        return this.TBuildingEstimateKinds;
    }

    public void setTBuildingEstimateKinds(List<TBuildingEstimateKind> TBuildingEstimateKinds) {
        this.TBuildingEstimateKinds = TBuildingEstimateKinds;
    }

    public TBuildingEstimateKind addTBuildingEstimateKind(TBuildingEstimateKind TBuildingEstimateKind) {
        getTBuildingEstimateKinds().add(TBuildingEstimateKind);
        TBuildingEstimateKind.setTBuilding(this);

        return TBuildingEstimateKind;
    }

    public TBuildingEstimateKind removeTBuildingEstimateKind(TBuildingEstimateKind TBuildingEstimateKind) {
        getTBuildingEstimateKinds().remove(TBuildingEstimateKind);
        TBuildingEstimateKind.setTBuilding(null);

        return TBuildingEstimateKind;
    }

    public List<TBuildingGroup> getTBuildingGroups() {
        return this.TBuildingGroups;
    }

    public void setTBuildingGroups(List<TBuildingGroup> TBuildingGroups) {
        this.TBuildingGroups = TBuildingGroups;
    }

    public TBuildingGroup addTBuildingGroup(TBuildingGroup TBuildingGroup) {
        getTBuildingGroups().add(TBuildingGroup);
        TBuildingGroup.setTBuilding(this);

        return TBuildingGroup;
    }

    public TBuildingGroup removeTBuildingGroup(TBuildingGroup TBuildingGroup) {
        getTBuildingGroups().remove(TBuildingGroup);
        TBuildingGroup.setTBuilding(null);

        return TBuildingGroup;
    }

    public List<TBuildingPerson> getTBuildingPersons() {
        return this.TBuildingPersons;
    }

    public void setTBuildingPersons(List<TBuildingPerson> TBuildingPersons) {
        this.TBuildingPersons = TBuildingPersons;
    }

    public TBuildingPerson addTBuildingPerson(TBuildingPerson TBuildingPerson) {
        getTBuildingPersons().add(TBuildingPerson);
        TBuildingPerson.setTBuilding(this);

        return TBuildingPerson;
    }

    public TBuildingPerson removeTBuildingPerson(TBuildingPerson TBuildingPerson) {
        getTBuildingPersons().remove(TBuildingPerson);
        TBuildingPerson.setTBuilding(null);

        return TBuildingPerson;
    }

    public List<TBuildingSubtype> getTBuildingSubtypes() {
        return this.TBuildingSubtypes;
    }

    public void setTBuildingSubtypes(List<TBuildingSubtype> TBuildingSubtypes) {
        this.TBuildingSubtypes = TBuildingSubtypes;
    }

    public TBuildingSubtype addTBuildingSubtype(TBuildingSubtype TBuildingSubtype) {
        getTBuildingSubtypes().add(TBuildingSubtype);
        TBuildingSubtype.setTBuilding(this);

        return TBuildingSubtype;
    }

    public TBuildingSubtype removeTBuildingSubtype(TBuildingSubtype TBuildingSubtype) {
        getTBuildingSubtypes().remove(TBuildingSubtype);
        TBuildingSubtype.setTBuilding(null);

        return TBuildingSubtype;
    }

    public List<TMaintenance> getTMaintenances() {
        return this.TMaintenances;
    }

    public void setTMaintenances(List<TMaintenance> TMaintenances) {
        this.TMaintenances = TMaintenances;
    }

    public TMaintenance addTMaintenance(TMaintenance TMaintenance) {
        getTMaintenances().add(TMaintenance);
        TMaintenance.setTBuilding(this);

        return TMaintenance;
    }

    public TMaintenance removeTMaintenance(TMaintenance TMaintenance) {
        getTMaintenances().remove(TMaintenance);
        TMaintenance.setTBuilding(null);

        return TMaintenance;
    }

    public List<TBuildingPlanFulfillment> getTBuildingPlanFulfillments() {
            return this.TBuildingPlanFulfillments;
    }

    public void setTBuildingPlanFulfillments(List<TBuildingPlanFulfillment> TBuildingPlanFulfillments) {
            this.TBuildingPlanFulfillments = TBuildingPlanFulfillments;
    }

    public TBuildingPlanFulfillment addTBuildingPlanFulfillment(TBuildingPlanFulfillment TBuildingPlanFulfillment) {
            getTBuildingPlanFulfillments().add(TBuildingPlanFulfillment);
            TBuildingPlanFulfillment.setTBuilding(this);

            return TBuildingPlanFulfillment;
    }

    public TBuildingPlanFulfillment removeTBuildingPlanFulfillment(TBuildingPlanFulfillment TBuildingPlanFulfillment) {
            getTBuildingPlanFulfillments().remove(TBuildingPlanFulfillment);
            TBuildingPlanFulfillment.setTBuilding(null);

            return TBuildingPlanFulfillment;
    }

    public List<MBuildingDm> getMBuildingDms() {
        return this.MBuildingDms;
    }

    public void setMBuildingDms(List<MBuildingDm> MBuildingDms) {
        this.MBuildingDms = MBuildingDms;
    }

    public MBuildingDm addMBuildingDm(MBuildingDm MBuildingDm) {
        getMBuildingDms().add(MBuildingDm);
        MBuildingDm.setTBuilding(this);

        return MBuildingDm;
    }

    public MBuildingDm removeMBuildingDm(MBuildingDm MBuildingDm) {
        getMBuildingDms().remove(MBuildingDm);
        MBuildingDm.setTBuilding(null);

        return MBuildingDm;
    }

    public List<MBuildingSm> getMBuildingSm() {
        return this.MBuildingSm;
    }

    public void setMBuildingSm(List<MBuildingSm> MBuildingSm) {
        this.MBuildingSm = MBuildingSm;
    }

    public MBuildingSm addMBuildingSm(MBuildingSm MBuildingSm) {
        getMBuildingSm().add(MBuildingSm);
        MBuildingSm.setTBuilding(this);

        return MBuildingSm;
    }

    public MBuildingSm removeMBuildingSm(MBuildingSm MBuildingSm) {
        getMBuildingSm().remove(MBuildingSm);
        MBuildingSm.setTBuilding(null);

        return MBuildingSm;
    }

    public List<MAllNightDay> getMAllNightDays() {
        return this.MAllNightDays;
    }

    public void setMAllNightDays(List<MAllNightDay> MAllNightDays) {
        this.MAllNightDays = MAllNightDays;
    }

    public MAllNightDay addMAllNightDay(MAllNightDay MAllNightDay) {
        getMAllNightDays().add(MAllNightDay);
        MAllNightDay.setTBuilding(this);

        return MAllNightDay;
    }

    public MAllNightDay removeMAllNightDay(MAllNightDay MAllNightDay) {
        getMAllNightDays().remove(MAllNightDay);
        MAllNightDay.setTBuilding(null);

        return MAllNightDay;
    }

    public List<MBuildingInfo> getMBuildingInfos() {
        return this.MBuildingInfos;
    }

    public void setMBuildingInfos(List<MBuildingInfo> MBuildingInfos) {
        this.MBuildingInfos = MBuildingInfos;
    }

    public MBuildingInfo addMBuildingInfo(MBuildingInfo MBuildingInfo) {
        getMBuildingInfos().add(MBuildingInfo);
        MBuildingInfo.setTBuilding(this);

        return MBuildingInfo;
    }

    public MBuildingInfo removeMBuildingInfo(MBuildingInfo MBuildingInfo) {
        getMBuildingInfos().remove(MBuildingInfo);
        MBuildingInfo.setTBuilding(null);

        return MBuildingInfo;
    }

    public List<MBuildingSms> getMBuildingSms() {
        return this.MBuildingSms;
    }

    public void setMBuildingSms(List<MBuildingSms> MBuildingSms) {
        this.MBuildingSms = MBuildingSms;
    }

    public MBuildingSms addMBuildingSm(MBuildingSms MBuildingSm) {
        getMBuildingSms().add(MBuildingSm);
        MBuildingSm.setTBuilding(this);

        return MBuildingSm;
    }

    public MBuildingSms removeMBuildingSm(MBuildingSms MBuildingSm) {
        getMBuildingSms().remove(MBuildingSm);
        MBuildingSm.setTBuilding(null);

        return MBuildingSm;
    }

    public List<MClaimantInfo> getMClaimantInfos() {
        return this.MClaimantInfos;
    }

    public void setMClaimantInfos(List<MClaimantInfo> MClaimantInfos) {
        this.MClaimantInfos = MClaimantInfos;
    }

    public MClaimantInfo addMClaimantInfo(MClaimantInfo MClaimantInfo) {
        getMClaimantInfos().add(MClaimantInfo);
        MClaimantInfo.setTBuilding(this);

        return MClaimantInfo;
    }

    public MClaimantInfo removeMClaimantInfo(MClaimantInfo MClaimantInfo) {
        getMClaimantInfos().remove(MClaimantInfo);
        MClaimantInfo.setTBuilding(null);

        return MClaimantInfo;
    }

    public List<MComDivrate> getMComDivrates() {
        return this.MComDivrates;
    }

    public void setMComDivrates(List<MComDivrate> MComDivrates) {
        this.MComDivrates = MComDivrates;
    }

    public MComDivrate addMComDivrate(MComDivrate MComDivrate) {
        getMComDivrates().add(MComDivrate);
        MComDivrate.setTBuilding(this);

        return MComDivrate;
    }

    public MComDivrate removeMComDivrate(MComDivrate MComDivrate) {
        getMComDivrates().remove(MComDivrate);
        MComDivrate.setTBuilding(null);

        return MComDivrate;
    }

    public List<MComType> getMComTypes() {
        return this.MComTypes;
    }

    public void setMComTypes(List<MComType> MComTypes) {
        this.MComTypes = MComTypes;
    }

    public MComType addMComType(MComType MComType) {
        getMComTypes().add(MComType);
        MComType.setTBuilding(this);

        return MComType;
    }

    public MComType removeMComType(MComType MComType) {
        getMComTypes().remove(MComType);
        MComType.setTBuilding(null);

        return MComType;
    }

    public List<MDevRelation> getMDevRelations() {
        return this.MDevRelations;
    }

    public void setMDevRelations(List<MDevRelation> MDevRelations) {
        this.MDevRelations = MDevRelations;
    }

    public MDevRelation addMDevRelation(MDevRelation MDevRelation) {
        getMDevRelations().add(MDevRelation);
        MDevRelation.setTBuilding(this);

        return MDevRelation;
    }

    public MDevRelation removeMDevRelation(MDevRelation MDevRelation) {
        getMDevRelations().remove(MDevRelation);
        MDevRelation.setTBuilding(null);

        return MDevRelation;
    }

    public List<MMeterGroupName> getMMeterGroupNames() {
        return this.MMeterGroupNames;
    }

    public void setMMeterGroupNames(List<MMeterGroupName> MMeterGroupNames) {
        this.MMeterGroupNames = MMeterGroupNames;
    }

    public MMeterGroupName addMMeterGroupName(MMeterGroupName MMeterGroupName) {
        getMMeterGroupNames().add(MMeterGroupName);
        MMeterGroupName.setTBuilding(this);

        return MMeterGroupName;
    }

    public MMeterGroupName removeMMeterGroupName(MMeterGroupName MMeterGroupName) {
        getMMeterGroupNames().remove(MMeterGroupName);
        MMeterGroupName.setTBuilding(null);

        return MMeterGroupName;
    }

    public List<MMeterType> getMMeterTypes() {
        return this.MMeterTypes;
    }

    public void setMMeterTypes(List<MMeterType> MMeterTypes) {
        this.MMeterTypes = MMeterTypes;
    }

    public MMeterType addMMeterType(MMeterType MMeterType) {
        getMMeterTypes().add(MMeterType);
        MMeterType.setTBuilding(this);

        return MMeterType;
    }

    public MMeterType removeMMeterType(MMeterType MMeterType) {
        getMMeterTypes().remove(MMeterType);
        MMeterType.setTBuilding(null);

        return MMeterType;
    }

    public List<MPriceMenu> getMPriceMenus() {
        return this.MPriceMenus;
    }

    public void setMPriceMenus(List<MPriceMenu> MPriceMenus) {
        this.MPriceMenus = MPriceMenus;
    }

    public MPriceMenu addMPriceMenus(MPriceMenu MPriceMenus) {
        getMPriceMenus().add(MPriceMenus);
        MPriceMenus.setTBuilding(this);

        return MPriceMenus;
    }

    public MPriceMenu removeMPriceMenus(MPriceMenu MPriceMenus) {
        getMPriceMenus().remove(MPriceMenus);
        MPriceMenus.setTBuilding(null);

        return MPriceMenus;
    }

    public List<MPriceMenuFamily> getMPriceMenuFamilies() {
        return this.MPriceMenuFamilies;
    }

    public void setMPriceMenuFamilies(List<MPriceMenuFamily> MPriceMenuFamilies) {
        this.MPriceMenuFamilies = MPriceMenuFamilies;
    }

    public MPriceMenuFamily addMPriceMenuFamily(MPriceMenuFamily MPriceMenuFamily) {
        getMPriceMenuFamilies().add(MPriceMenuFamily);
        MPriceMenuFamily.setTBuilding(this);

        return MPriceMenuFamily;
    }

    public MPriceMenuFamily removeMPriceMenuFamily(MPriceMenuFamily MPriceMenuFamily) {
        getMPriceMenuFamilies().remove(MPriceMenuFamily);
        MPriceMenuFamily.setTBuilding(null);

        return MPriceMenuFamily;
    }

    public List<MPriceMenuLighta> getMPriceMenuLightas() {
        return this.MPriceMenuLightas;
    }

    public void setMPriceMenuLightas(List<MPriceMenuLighta> MPriceMenuLightas) {
        this.MPriceMenuLightas = MPriceMenuLightas;
    }

    public MPriceMenuLighta addMPriceMenuLighta(MPriceMenuLighta MPriceMenuLighta) {
        getMPriceMenuLightas().add(MPriceMenuLighta);
        MPriceMenuLighta.setTBuilding(this);

        return MPriceMenuLighta;
    }

    public MPriceMenuLighta removeMPriceMenuLighta(MPriceMenuLighta MPriceMenuLighta) {
        getMPriceMenuLightas().remove(MPriceMenuLighta);
        MPriceMenuLighta.setTBuilding(null);

        return MPriceMenuLighta;
    }

    public List<MPriceMenuLightb> getMPriceMenuLightbs() {
        return this.MPriceMenuLightbs;
    }

    public void setMPriceMenuLightbs(List<MPriceMenuLightb> MPriceMenuLightbs) {
        this.MPriceMenuLightbs = MPriceMenuLightbs;
    }

    public MPriceMenuLightb addMPriceMenuLightb(MPriceMenuLightb MPriceMenuLightb) {
        getMPriceMenuLightbs().add(MPriceMenuLightb);
        MPriceMenuLightb.setTBuilding(this);

        return MPriceMenuLightb;
    }

    public MPriceMenuLightb removeMPriceMenuLightb(MPriceMenuLightb MPriceMenuLightb) {
        getMPriceMenuLightbs().remove(MPriceMenuLightb);
        MPriceMenuLightb.setTBuilding(null);

        return MPriceMenuLightb;
    }

    public List<MTenantPriceInfo> getMTenantPriceInfos() {
        return this.MTenantPriceInfos;
    }

    public void setMTenantPriceInfos(List<MTenantPriceInfo> MTenantPriceInfos) {
        this.MTenantPriceInfos = MTenantPriceInfos;
    }

    public MTenantPriceInfo addMTenantPriceInfo(MTenantPriceInfo MTenantPriceInfo) {
        getMTenantPriceInfos().add(MTenantPriceInfo);
        MTenantPriceInfo.setTBuilding(this);

        return MTenantPriceInfo;
    }

    public MTenantPriceInfo removeMTenantPriceInfo(MTenantPriceInfo MTenantPriceInfo) {
        getMTenantPriceInfos().remove(MTenantPriceInfo);
        MTenantPriceInfo.setTBuilding(null);

        return MTenantPriceInfo;
    }

    public List<MTenantSm> getMTenantSms() {
        return this.MTenantSms;
    }

    public void setMTenantSms(List<MTenantSm> MTenantSms) {
        this.MTenantSms = MTenantSms;
    }

    public MTenantSm addMTenantSm(MTenantSm MTenantSm) {
        getMTenantSms().add(MTenantSm);
        MTenantSm.setTBuilding(this);

        return MTenantSm;
    }

    public MTenantSm removeMTenantSm(MTenantSm MTenantSm) {
        getMTenantSms().remove(MTenantSm);
        MTenantSm.setTBuilding(null);

        return MTenantSm;
    }

    public List<MTypeStage> getMTypeStages() {
        return this.MTypeStages;
    }

    public void setMTypeStages(List<MTypeStage> MTypeStages) {
        this.MTypeStages = MTypeStages;
    }

    public MTypeStage addMTypeStage(MTypeStage MTypeStage) {
        getMTypeStages().add(MTypeStage);
        MTypeStage.setTBuilding(this);

        return MTypeStage;
    }

    public MTypeStage removeMTypeStage(MTypeStage MTypeStage) {
        getMTypeStages().remove(MTypeStage);
        MTypeStage.setTBuilding(null);

        return MTypeStage;
    }

    public List<MVarious> getMVariouses() {
        return this.MVariouses;
    }

    public void setMVariouses(List<MVarious> MVariouses) {
        this.MVariouses = MVariouses;
    }

    public MVarious addMVarious(MVarious MVarious) {
        getMVariouses().add(MVarious);
        MVarious.setTBuilding(this);

        return MVarious;
    }

    public MVarious removeMVarious(MVarious MVarious) {
        getMVariouses().remove(MVarious);
        MVarious.setTBuilding(null);

        return MVarious;
    }

    public List<TBuildDevMeterRelation> getTBuildDevMeterRelations() {
        return this.TBuildDevMeterRelations;
    }

    public void setTBuildDevMeterRelations(List<TBuildDevMeterRelation> TBuildDevMeterRelations) {
        this.TBuildDevMeterRelations = TBuildDevMeterRelations;
    }

    public TBuildDevMeterRelation addTBuildDevMeterRelation(TBuildDevMeterRelation TBuildDevMeterRelation) {
        getTBuildDevMeterRelations().add(TBuildDevMeterRelation);
        TBuildDevMeterRelation.setTBuilding(this);

        return TBuildDevMeterRelation;
    }

    public TBuildDevMeterRelation removeTBuildDevMeterRelation(TBuildDevMeterRelation TBuildDevMeterRelation) {
        getTBuildDevMeterRelations().remove(TBuildDevMeterRelation);
        TBuildDevMeterRelation.setTBuilding(null);

        return TBuildDevMeterRelation;
    }

}