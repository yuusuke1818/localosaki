package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * The persistent class for the m_corp database table.
 *
 */
@Entity
@Table(name = "m_corp")
@NamedQueries({
    @NamedQuery(name = "MCorp.findAll", query = "SELECT m FROM MCorp m"),
    @NamedQuery(name = "MCorp.findJoin",        //ユーザー情報担当企業設定　設定可能企業 (大崎管理者)
            query = "SELECT mc FROM MCorp mc LEFT OUTER JOIN mc.MPrefecture m "
            + "WHERE "
            + "(:corpId is null or mc.corpId like :corpId) "
            + "AND (:corpName is null or mc.corpName like :corpName) "
            + "AND (:corpIdOrName is null or mc.corpId like :corpIdOrName or mc.corpName like :corpIdOrName) "      //検索条件
            + "AND (mc.corpType IN :corpType) "
            + "AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "
            + "AND (mc.corpId <> :selectCorpId) "
            + "ORDER BY mc.corpId ASC "
    ),
    @NamedQuery(name = "MCorp.findForCorpPerson",   //ユーザー情報担当企業設定　設定可能企業 (大崎管理者以外)
            query = "SELECT mc FROM MCorp mc LEFT OUTER JOIN mc.MPrefecture m INNER JOIN mc.MCorpPersons mcp "
            + "WHERE "
            + "(:corpId is null or mc.corpId like :corpId) "                //検索条件
            + "AND (:corpName is null or mc.corpName like :corpName) "      //検索条件
            + "AND (:corpIdOrName is null or mc.corpId like :corpIdOrName or mc.corpName like :corpIdOrName) "      //検索条件
            + "AND (mc.corpType IN :corpType) "         //権限グループによる条件
            + "AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "   //検索条件
            + "AND (mc.corpId <> :selectCorpId) "       //除外(編集ユーザーの企業)
            + "AND (mcp.id.personId = :authorityPersonId) "   //操作ユーザーの担当範囲絞り込み
            + "AND (mcp.id.personCorpId = :authorityCorpId) "   //操作ユーザーの担当範囲絞り込み
            + "AND (mcp.delFlg = 0) "   //担当なし(は削除フラグ立つ)を除外
            + "ORDER BY mc.corpId ASC "
    ),
    @NamedQuery(name = "MCorp.findJoinCorpType",
            query = "SELECT mc FROM MCorp mc LEFT OUTER JOIN mc.MPrefecture m "
            + "WHERE "
            + "((:corpId is null or mc.corpId like :corpId) OR  (:corpName is null or mc.corpName like :corpName)) "
            + "AND (:corpTypeFlg is null or mc.corpType IN :corpType) "
            + "AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "
            + "AND (mc.corpId <> :selectCorpId) "
            + "AND (mc.corpType NOT IN ('0')) "
            + "ORDER BY mc.corpId ASC "
    ),
    @NamedQuery(name = "MCorp.findForCorpSelect",
            query = "SELECT mc FROM MCorp mc LEFT OUTER JOIN mc.MPrefecture m  INNER JOIN mc.MCorpPersons mcp "
            + "WHERE "
            + "((:corpId is null or mc.corpId like :corpId) OR (:corpName is null or mc.corpName like :corpName)) "
            + "AND (:corpTypeFlg is null or mc.corpType IN :corpType) "
            + "AND (:prefectureCdFlg is null or m.prefectureCd IN :prefectureCd) "
            + "AND (mc.corpId <> :selectCorpId) "           //操作中企業を除く
            + "AND (mc.corpId <> :authorityCorpId) "        //自企業を除く
            + "AND (mc.corpType NOT IN ('0')) "
            + "AND (mcp.id.personId = :authorityPersonId) "
            + "AND (mcp.id.personCorpId = :authorityCorpId) "
            + "AND (mcp.delFlg = 0) "
            + "ORDER BY mc.corpId ASC "
    ),})
public class MCorp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "corp_id", unique = true, nullable = false, length = 50)
    private String corpId;

    @Column(nullable = false, length = 100)
    private String address1;

    @Column(length = 100)
    private String address2;

    @Column(name = "corp_kana", length = 200)
    private String corpKana;

    @Column(name="corp_logo_image_file_name", length=100)
    private String corpLogoImageFileName;

    @Column(name="corp_logo_image_file_path", length=300)
    private String corpLogoImageFilePath;

    @Column(name = "corp_name", nullable = false, length = 50)
    private String corpName;

    @Column(name = "corp_type", nullable = false, length = 6)
    private String corpType;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @Column(name = "create_user_id", nullable = false)
    private Long createUserId;

    @Column(name = "executive_name", length = 50)
    private String executiveName;

    @Column(name = "executive_post_name", length = 50)
    private String executivePostName;

    @Column(name = "fax_no", length = 100)
    private String faxNo;

    @Column(name="maintenance_use_setting", nullable=false, length=6)
    private String maintenanceUseSetting;

    @Column(name = "specific_business_no", length = 7)
    private String specificBusinessNo;

    @Column(name = "specific_emission_cd", length = 9)
    private String specificEmissionCd;

    @Column(name = "tel_no", length = 100)
    private String telNo;

    @Column(name = "update_date", nullable = false)
    private Timestamp updateDate;

    @Column(name = "update_user_id", nullable = false)
    private Long updateUserId;

    @Temporal(TemporalType.DATE)
    @Column(name = "use_stop_end_date")
    private Date useStopEndDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "use_stop_start_date")
    private Date useStopStartDate;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(name = "zip_cd", length = 10)
    private String zipCd;

    //bi-directional many-to-one association to MMunicipality
    @ManyToOne
    @JoinColumn(name = "municipality_cd")
    private MMunicipality MMunicipality;

    //bi-directional many-to-one association to MPerson
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "contact_person_corp_id", referencedColumnName = "corp_id"),
        @JoinColumn(name = "contact_person_id", referencedColumnName = "person_id")
    })
    private MPerson MPerson;

    //bi-directional many-to-one association to MPrefecture
    @ManyToOne
    @JoinColumn(name = "prefecture_cd", nullable = false)
    private MPrefecture MPrefecture;

    //bi-directional many-to-one association to MCorpFacilityKind
    @OneToMany(mappedBy = "MCorp")
    private List<MCorpFacilityKind> MCorpFacilityKinds;

    //bi-directional many-to-one association to MCorpPerson
    @OneToMany(mappedBy = "MCorp")
    private List<MCorpPerson> MCorpPersons;

    //bi-directional many-to-one association to MEstimateKind
    @OneToMany(mappedBy = "MCorp")
    private List<MEstimateKind> MEstimateKinds;

    //bi-directional many-to-one association to MLoginIpAddr
    @OneToMany(mappedBy = "MCorp")
    private List<MLoginIpAddr> MLoginIpAddrs;

    //bi-directional many-to-one association to MMaintenanceMailSetting
    @OneToMany(mappedBy="MCorp")
    private List<MMaintenanceMailSetting> MMaintenanceMailSettings;

    //bi-directional many-to-one association to MMaintenanceTrader
    @OneToMany(mappedBy = "MCorp")
    private List<MMaintenanceTrader> MMaintenanceTraders;

    //bi-directional many-to-one association to MPerson
    @OneToMany(mappedBy = "MCorp")
    private List<MPerson> MPersons;

    //bi-directional many-to-one association to MSubtype
    @OneToMany(mappedBy = "MCorp")
    private List<MSubtype> MSubtypes;

    //bi-directional many-to-one association to MUnitDivide
    @OneToMany(mappedBy = "MCorp")
    private List<MUnitDivide> MUnitDivides;

    //bi-directional many-to-one association to TAvailableEnergyBulkInput
    @OneToMany(mappedBy = "MCorp")
    private List<TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs;

    //bi-directional many-to-one association to TBuilding
    @OneToMany(mappedBy = "MCorp")
    private List<TBuilding> TBuildings;

    //bi-directional many-to-one association to TOshirase
    @OneToMany(mappedBy = "MCorp")
    private List<TOshirase> TOshirases;

    //bi-directional many-to-one association to TFacilityBulkInput
    @OneToMany(mappedBy="MCorp")
    private List<TFacilityBulkInput> TFacilityBulkInputs;

    //bi-directional many-to-one association to TPlanFulfillmentInfo
    @OneToMany(mappedBy="MCorp")
    private List<TPlanFulfillmentInfo> TPlanFulfillmentInfos;

    //bi-directional many-to-one association to MCorpFunctionUse
    @OneToMany(mappedBy="MCorp")
    private List<MCorpFunctionUse> MCorpFunctionUses;

    //bi-directional one-to-one association to MCorpDm
    @OneToOne(mappedBy="MCorp")
    private MCorpDm MCorpDm;

    //bi-directional many-to-one association to TAggregateReservationInfo
    @OneToMany(mappedBy="MCorp")
    private List<TAggregateReservationInfo> TAggregateReservationInfos;

    public MCorp() {
    }

    public String getCorpId() {
        return this.corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCorpKana() {
        return this.corpKana;
    }

    public void setCorpKana(String corpKana) {
        this.corpKana = corpKana;
    }

    public String getCorpLogoImageFileName() {
        return this.corpLogoImageFileName;
    }

    public void setCorpLogoImageFileName(String corpLogoImageFileName) {
        this.corpLogoImageFileName = corpLogoImageFileName;
    }

    public String getCorpLogoImageFilePath() {
        return this.corpLogoImageFilePath;
    }

    public void setCorpLogoImageFilePath(String corpLogoImageFilePath) {
        this.corpLogoImageFilePath = corpLogoImageFilePath;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpType() {
        return this.corpType;
    }

    public void setCorpType(String corpType) {
        this.corpType = corpType;
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

    public String getExecutiveName() {
        return this.executiveName;
    }

    public void setExecutiveName(String executiveName) {
        this.executiveName = executiveName;
    }

    public String getExecutivePostName() {
        return this.executivePostName;
    }

    public void setExecutivePostName(String executivePostName) {
        this.executivePostName = executivePostName;
    }

    public String getFaxNo() {
        return this.faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getMaintenanceUseSetting() {
        return this.maintenanceUseSetting;
    }

    public void setMaintenanceUseSetting(String maintenanceUseSetting) {
        this.maintenanceUseSetting = maintenanceUseSetting;
    }

    public String getSpecificBusinessNo() {
        return this.specificBusinessNo;
    }

    public void setSpecificBusinessNo(String specificBusinessNo) {
        this.specificBusinessNo = specificBusinessNo;
    }

    public String getSpecificEmissionCd() {
        return this.specificEmissionCd;
    }

    public void setSpecificEmissionCd(String specificEmissionCd) {
        this.specificEmissionCd = specificEmissionCd;
    }

    public String getTelNo() {
        return this.telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
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

    public Date getUseStopEndDate() {
        return this.useStopEndDate;
    }

    public void setUseStopEndDate(Date useStopEndDate) {
        this.useStopEndDate = useStopEndDate;
    }

    public Date getUseStopStartDate() {
        return this.useStopStartDate;
    }

    public void setUseStopStartDate(Date useStopStartDate) {
        this.useStopStartDate = useStopStartDate;
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

    public MMunicipality getMMunicipality() {
        return this.MMunicipality;
    }

    public void setMMunicipality(MMunicipality MMunicipality) {
        this.MMunicipality = MMunicipality;
    }

    public MPerson getMPerson() {
        return this.MPerson;
    }

    public void setMPerson(MPerson MPerson) {
        this.MPerson = MPerson;
    }

    public MPrefecture getMPrefecture() {
        return this.MPrefecture;
    }

    public void setMPrefecture(MPrefecture MPrefecture) {
        this.MPrefecture = MPrefecture;
    }

    public List<MCorpFacilityKind> getMCorpFacilityKinds() {
        return this.MCorpFacilityKinds;
    }

    public void setMCorpFacilityKinds(List<MCorpFacilityKind> MCorpFacilityKinds) {
        this.MCorpFacilityKinds = MCorpFacilityKinds;
    }

    public MCorpFacilityKind addMCorpFacilityKind(MCorpFacilityKind MCorpFacilityKind) {
        getMCorpFacilityKinds().add(MCorpFacilityKind);
        MCorpFacilityKind.setMCorp(this);

        return MCorpFacilityKind;
    }

    public MCorpFacilityKind removeMCorpFacilityKind(MCorpFacilityKind MCorpFacilityKind) {
        getMCorpFacilityKinds().remove(MCorpFacilityKind);
        MCorpFacilityKind.setMCorp(null);

        return MCorpFacilityKind;
    }

    public List<MCorpPerson> getMCorpPersons() {
        return this.MCorpPersons;
    }

    public void setMCorpPersons(List<MCorpPerson> MCorpPersons) {
        this.MCorpPersons = MCorpPersons;
    }

    public MCorpPerson addMCorpPerson(MCorpPerson MCorpPerson) {
        getMCorpPersons().add(MCorpPerson);
        MCorpPerson.setMCorp(this);

        return MCorpPerson;
    }

    public MCorpPerson removeMCorpPerson(MCorpPerson MCorpPerson) {
        getMCorpPersons().remove(MCorpPerson);
        MCorpPerson.setMCorp(null);

        return MCorpPerson;
    }

    public List<MEstimateKind> getMEstimateKinds() {
        return this.MEstimateKinds;
    }

    public void setMEstimateKinds(List<MEstimateKind> MEstimateKinds) {
        this.MEstimateKinds = MEstimateKinds;
    }

    public MEstimateKind addMEstimateKind(MEstimateKind MEstimateKind) {
        getMEstimateKinds().add(MEstimateKind);
        MEstimateKind.setMCorp(this);

        return MEstimateKind;
    }

    public MEstimateKind removeMEstimateKind(MEstimateKind MEstimateKind) {
        getMEstimateKinds().remove(MEstimateKind);
        MEstimateKind.setMCorp(null);

        return MEstimateKind;
    }

    public List<MLoginIpAddr> getMLoginIpAddrs() {
        return this.MLoginIpAddrs;
    }

    public void setMLoginIpAddrs(List<MLoginIpAddr> MLoginIpAddrs) {
        this.MLoginIpAddrs = MLoginIpAddrs;
    }

    public MLoginIpAddr addMLoginIpAddr(MLoginIpAddr MLoginIpAddr) {
        getMLoginIpAddrs().add(MLoginIpAddr);
        MLoginIpAddr.setMCorp(this);

        return MLoginIpAddr;
    }

    public MLoginIpAddr removeMLoginIpAddr(MLoginIpAddr MLoginIpAddr) {
        getMLoginIpAddrs().remove(MLoginIpAddr);
        MLoginIpAddr.setMCorp(null);

        return MLoginIpAddr;
    }

    public List<MMaintenanceMailSetting> getMMaintenanceMailSettings() {
        return this.MMaintenanceMailSettings;
    }

    public void setMMaintenanceMailSettings(List<MMaintenanceMailSetting> MMaintenanceMailSettings) {
        this.MMaintenanceMailSettings = MMaintenanceMailSettings;
    }

    public MMaintenanceMailSetting addMMaintenanceMailSetting(MMaintenanceMailSetting MMaintenanceMailSetting) {
        getMMaintenanceMailSettings().add(MMaintenanceMailSetting);
        MMaintenanceMailSetting.setMCorp(this);

        return MMaintenanceMailSetting;
    }

    public MMaintenanceMailSetting removeMMaintenanceMailSetting(MMaintenanceMailSetting MMaintenanceMailSetting) {
        getMMaintenanceMailSettings().remove(MMaintenanceMailSetting);
        MMaintenanceMailSetting.setMCorp(null);

        return MMaintenanceMailSetting;
    }

    public List<MMaintenanceTrader> getMMaintenanceTraders() {
        return this.MMaintenanceTraders;
    }

    public void setMMaintenanceTraders(List<MMaintenanceTrader> MMaintenanceTraders) {
        this.MMaintenanceTraders = MMaintenanceTraders;
    }

    public MMaintenanceTrader addMMaintenanceTrader(MMaintenanceTrader MMaintenanceTrader) {
        getMMaintenanceTraders().add(MMaintenanceTrader);
        MMaintenanceTrader.setMCorp(this);

        return MMaintenanceTrader;
    }

    public MMaintenanceTrader removeMMaintenanceTrader(MMaintenanceTrader MMaintenanceTrader) {
        getMMaintenanceTraders().remove(MMaintenanceTrader);
        MMaintenanceTrader.setMCorp(null);

        return MMaintenanceTrader;
    }

    public List<MPerson> getMPersons() {
        return this.MPersons;
    }

    public void setMPersons(List<MPerson> MPersons) {
        this.MPersons = MPersons;
    }

    public MPerson addMPerson(MPerson MPerson) {
        getMPersons().add(MPerson);
        MPerson.setMCorp(this);

        return MPerson;
    }

    public MPerson removeMPerson(MPerson MPerson) {
        getMPersons().remove(MPerson);
        MPerson.setMCorp(null);

        return MPerson;
    }

    public List<MSubtype> getMSubtypes() {
        return this.MSubtypes;
    }

    public void setMSubtypes(List<MSubtype> MSubtypes) {
        this.MSubtypes = MSubtypes;
    }

    public MSubtype addMSubtype(MSubtype MSubtype) {
        getMSubtypes().add(MSubtype);
        MSubtype.setMCorp(this);

        return MSubtype;
    }

    public MSubtype removeMSubtype(MSubtype MSubtype) {
        getMSubtypes().remove(MSubtype);
        MSubtype.setMCorp(null);

        return MSubtype;
    }

    public List<MUnitDivide> getMUnitDivides() {
        return this.MUnitDivides;
    }

    public void setMUnitDivides(List<MUnitDivide> MUnitDivides) {
        this.MUnitDivides = MUnitDivides;
    }

    public MUnitDivide addMUnitDivide(MUnitDivide MUnitDivide) {
        getMUnitDivides().add(MUnitDivide);
        MUnitDivide.setMCorp(this);

        return MUnitDivide;
    }

    public MUnitDivide removeMUnitDivide(MUnitDivide MUnitDivide) {
        getMUnitDivides().remove(MUnitDivide);
        MUnitDivide.setMCorp(null);

        return MUnitDivide;
    }

    public List<TAvailableEnergyBulkInput> getTAvailableEnergyBulkInputs() {
        return this.TAvailableEnergyBulkInputs;
    }

    public void setTAvailableEnergyBulkInputs(List<TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs) {
        this.TAvailableEnergyBulkInputs = TAvailableEnergyBulkInputs;
    }

    public TAvailableEnergyBulkInput addTAvailableEnergyBulkInput(TAvailableEnergyBulkInput TAvailableEnergyBulkInput) {
        getTAvailableEnergyBulkInputs().add(TAvailableEnergyBulkInput);
        TAvailableEnergyBulkInput.setMCorp(this);

        return TAvailableEnergyBulkInput;
    }

    public TAvailableEnergyBulkInput removeTAvailableEnergyBulkInput(TAvailableEnergyBulkInput TAvailableEnergyBulkInput) {
        getTAvailableEnergyBulkInputs().remove(TAvailableEnergyBulkInput);
        TAvailableEnergyBulkInput.setMCorp(null);

        return TAvailableEnergyBulkInput;
    }

    public List<TBuilding> getTBuildings() {
        return this.TBuildings;
    }

    public void setTBuildings(List<TBuilding> TBuildings) {
        this.TBuildings = TBuildings;
    }

    public TBuilding addTBuilding(TBuilding TBuilding) {
        getTBuildings().add(TBuilding);
        TBuilding.setMCorp(this);

        return TBuilding;
    }

    public TBuilding removeTBuilding(TBuilding TBuilding) {
        getTBuildings().remove(TBuilding);
        TBuilding.setMCorp(null);

        return TBuilding;
    }

    public List<TOshirase> getTOshirases() {
        return this.TOshirases;
    }

    public void setTOshirases(List<TOshirase> TOshirases) {
        this.TOshirases = TOshirases;
    }

    public TOshirase addTOshiras(TOshirase TOshiras) {
        getTOshirases().add(TOshiras);
        TOshiras.setMCorp(this);

        return TOshiras;
    }

    public TOshirase removeTOshiras(TOshirase TOshiras) {
        getTOshirases().remove(TOshiras);
        TOshiras.setMCorp(null);

        return TOshiras;
    }

    public List<TFacilityBulkInput> getTFacilityBulkInputs() {
            return this.TFacilityBulkInputs;
    }

    public void setTFacilityBulkInputs(List<TFacilityBulkInput> TFacilityBulkInputs) {
            this.TFacilityBulkInputs = TFacilityBulkInputs;
    }

    public TFacilityBulkInput addTFacilityBulkInput(TFacilityBulkInput TFacilityBulkInput) {
            getTFacilityBulkInputs().add(TFacilityBulkInput);
            TFacilityBulkInput.setMCorp(this);

            return TFacilityBulkInput;
    }

    public TFacilityBulkInput removeTFacilityBulkInput(TFacilityBulkInput TFacilityBulkInput) {
            getTFacilityBulkInputs().remove(TFacilityBulkInput);
            TFacilityBulkInput.setMCorp(null);

            return TFacilityBulkInput;
    }

    public List<TPlanFulfillmentInfo> getTPlanFulfillmentInfos() {
            return this.TPlanFulfillmentInfos;
    }

    public void setTPlanFulfillmentInfos(List<TPlanFulfillmentInfo> TPlanFulfillmentInfos) {
            this.TPlanFulfillmentInfos = TPlanFulfillmentInfos;
    }

    public TPlanFulfillmentInfo addTPlanFulfillmentInfo(TPlanFulfillmentInfo TPlanFulfillmentInfo) {
            getTPlanFulfillmentInfos().add(TPlanFulfillmentInfo);
            TPlanFulfillmentInfo.setMCorp(this);

            return TPlanFulfillmentInfo;
    }

    public TPlanFulfillmentInfo removeTPlanFulfillmentInfo(TPlanFulfillmentInfo TPlanFulfillmentInfo) {
            getTPlanFulfillmentInfos().remove(TPlanFulfillmentInfo);
            TPlanFulfillmentInfo.setMCorp(null);

            return TPlanFulfillmentInfo;
    }

    public MCorpDm getMCorpDm() {
            return this.MCorpDm;
    }

    public void setMCorpDm(MCorpDm MCorpDm) {
            this.MCorpDm = MCorpDm;
    }

    public List<MCorpFunctionUse> getMCorpFunctionUses() {
            return this.MCorpFunctionUses;
    }

    public void setMCorpFunctionUses(List<MCorpFunctionUse> MCorpFunctionUses) {
            this.MCorpFunctionUses = MCorpFunctionUses;
    }

    public MCorpFunctionUse addMCorpFunctionUs(MCorpFunctionUse MCorpFunctionUs) {
            getMCorpFunctionUses().add(MCorpFunctionUs);
            MCorpFunctionUs.setMCorp(this);

            return MCorpFunctionUs;
    }

    public MCorpFunctionUse removeMCorpFunctionUs(MCorpFunctionUse MCorpFunctionUs) {
            getMCorpFunctionUses().remove(MCorpFunctionUs);
            MCorpFunctionUs.setMCorp(null);

            return MCorpFunctionUs;
    }

    public List<TAggregateReservationInfo> getTAggregateReservationInfos() {
        return this.TAggregateReservationInfos;
    }

    public void setTAggregateReservationInfos(List<TAggregateReservationInfo> TAggregateReservationInfos) {
        this.TAggregateReservationInfos = TAggregateReservationInfos;
    }

    public TAggregateReservationInfo addTAggregateReservationInfo(TAggregateReservationInfo TAggregateReservationInfo) {
        getTAggregateReservationInfos().add(TAggregateReservationInfo);
        TAggregateReservationInfo.setMCorp(this);

        return TAggregateReservationInfo;
    }

    public TAggregateReservationInfo removeTAggregateReservationInfo(TAggregateReservationInfo TAggregateReservationInfo) {
        getTAggregateReservationInfos().remove(TAggregateReservationInfo);
        TAggregateReservationInfo.setMCorp(null);

        return TAggregateReservationInfo;
    }

}
