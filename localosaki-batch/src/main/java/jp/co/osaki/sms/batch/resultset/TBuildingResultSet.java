package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建物ResultSet
 *
 * @author d-komatsubara
 *
 */
public class TBuildingResultSet {
    /* 企業ID */
    private String corpId;
    /* 建物ID */
    private Long buildingId;
    /* 住所 */
    private String address;
    /* 住所(建物名) */
    private String addressBuilding;
    /* 地下階数 */
    private String basementCount;
    /* 備考 */
    private String biko;
    /* 建物削除日時 */
    private Date buildingDelDate;
    /* 建物削除担当者企業ID */
    private String buildingDelPersonCorpId;
    /* 建物削除担当者ID */
    private String buildingDelPersonId;
    /* 建物名 */
    private String buildingName;
    /* 建物名かな */
    private String buildingNameKana;
    /* 建物番号 */
    private String buildingNo;
    /* 建物短縮名称 */
    private String buildingTansyukuName;
    /* 建物種別 */
    private String buildingType;
    /* 共有持分割合 */
    private BigDecimal commonUsedRate;
    /* 施工年月 */
    private Date conpletedYm;
    /* 作成日時 */
    private Date createDate;
    /* 作成ユーザー識別ID */
    private Long createUserId;
    /* 削除フラグ */
    private Integer delFlg;
    /* 所属建物ID */
    private Long divisionBuildingId;
    /* 所属企業ID */
    private String divisionCorpId;
    /* エネルギー管理指定工場等番号 */
    private String engManageFactoryNo;
    /* エネルギー管理指定工場種別 */
    private String engManageFactoryType;
    /* 推計使用 */
    private String estimateUse;
    /* FAX番号 */
    private String faxNo;
    /* 地上階数 */
    private String floorCount;
    /* フロン法排出特定事業所 */
    private Integer freonDischargeOffice;
    /* 区分所有割合 */
    private BigDecimal kubunShoyuRate;
    /* 入居形態コード */
    private String nyukyoTypeCd;
    /* 公開フラグ */
    private Integer publicFlg;
    /* 電話番号 */
    private String telNo;
    /* 集計終了年月 */
    private Date totalEndYm;
    /* 集計開始年月 */
    private Date totalStartYm;
    /* 更新日時 */
    private Date updateDate;
    /* 更新ユーザー識別ID */
    private Long updateUserId;
    /* バージョン */
    private Integer version;
    /* 郵便番号 */
    private String zipCd;

    /**
     * コンストラクタ
     *
     * @param corpId
     * @param buildingId
     * @param address
     * @param addressBuilding
     * @param basementCount
     * @param biko
     * @param buildingDelDate
     * @param buildingDelPersonCorpId
     * @param buildingDelPersonId
     * @param buildingName
     * @param buildingNameKana
     * @param buildingNo
     * @param buildingTansyukuName
     * @param buildingType
     * @param commonUsedRate
     * @param conpletedYm
     * @param createDate
     * @param createUserId
     * @param delFlg
     * @param divisionBuildingId
     * @param divisionCorpId
     * @param engManageFactoryNo
     * @param engManageFactoryType
     * @param estimateUse
     * @param faxNo
     * @param floorCount
     * @param freonDischargeOffice
     * @param kubunShoyuRate
     * @param nyukyoTypeCd
     * @param publicFlg
     * @param telNo
     * @param totalEndYm
     * @param totalStartYm
     * @param updateDate
     * @param updateUserId
     * @param version
     * @param zipCd
     */
    public TBuildingResultSet(String corpId, Long buildingId, String address, String addressBuilding,
            String basementCount, String biko, Date buildingDelDate, String buildingDelPersonCorpId,
            String buildingDelPersonId, String buildingName, String buildingNameKana, String buildingNo,
            String buildingTansyukuName, String buildingType, BigDecimal commonUsedRate, Date conpletedYm,
            Date createDate, Long createUserId, Integer delFlg, Long divisionBuildingId, String divisionCorpId,
            String engManageFactoryNo, String engManageFactoryType, String estimateUse, String faxNo, String floorCount,
            Integer freonDischargeOffice, BigDecimal kubunShoyuRate, String nyukyoTypeCd, Integer publicFlg,
            String telNo, Date totalEndYm, Date totalStartYm, Date updateDate, Long updateUserId, Integer version,
            String zipCd) {
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.address = address;
        this.addressBuilding = addressBuilding;
        this.basementCount = basementCount;
        this.biko = biko;
        this.buildingDelDate = buildingDelDate;
        this.buildingDelPersonCorpId = buildingDelPersonCorpId;
        this.buildingDelPersonId = buildingDelPersonId;
        this.buildingName = buildingName;
        this.buildingNameKana = buildingNameKana;
        this.buildingNo = buildingNo;
        this.buildingTansyukuName = buildingTansyukuName;
        this.buildingType = buildingType;
        this.commonUsedRate = commonUsedRate;
        this.conpletedYm = conpletedYm;
        this.createDate = createDate;
        this.createUserId = createUserId;
        this.delFlg = delFlg;
        this.divisionBuildingId = divisionBuildingId;
        this.divisionCorpId = divisionCorpId;
        this.engManageFactoryNo = engManageFactoryNo;
        this.engManageFactoryType = engManageFactoryType;
        this.estimateUse = estimateUse;
        this.faxNo = faxNo;
        this.floorCount = floorCount;
        this.freonDischargeOffice = freonDischargeOffice;
        this.kubunShoyuRate = kubunShoyuRate;
        this.nyukyoTypeCd = nyukyoTypeCd;
        this.publicFlg = publicFlg;
        this.telNo = telNo;
        this.totalEndYm = totalEndYm;
        this.totalStartYm = totalStartYm;
        this.updateDate = updateDate;
        this.updateUserId = updateUserId;
        this.version = version;
        this.zipCd = zipCd;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getBasementCount() {
        return basementCount;
    }

    public void setBasementCount(String basementCount) {
        this.basementCount = basementCount;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public Date getBuildingDelDate() {
        return buildingDelDate;
    }

    public void setBuildingDelDate(Date buildingDelDate) {
        this.buildingDelDate = buildingDelDate;
    }

    public String getBuildingDelPersonCorpId() {
        return buildingDelPersonCorpId;
    }

    public void setBuildingDelPersonCorpId(String buildingDelPersonCorpId) {
        this.buildingDelPersonCorpId = buildingDelPersonCorpId;
    }

    public String getBuildingDelPersonId() {
        return buildingDelPersonId;
    }

    public void setBuildingDelPersonId(String buildingDelPersonId) {
        this.buildingDelPersonId = buildingDelPersonId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingNameKana() {
        return buildingNameKana;
    }

    public void setBuildingNameKana(String buildingNameKana) {
        this.buildingNameKana = buildingNameKana;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingTansyukuName() {
        return buildingTansyukuName;
    }

    public void setBuildingTansyukuName(String buildingTansyukuName) {
        this.buildingTansyukuName = buildingTansyukuName;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public BigDecimal getCommonUsedRate() {
        return commonUsedRate;
    }

    public void setCommonUsedRate(BigDecimal commonUsedRate) {
        this.commonUsedRate = commonUsedRate;
    }

    public Date getConpletedYm() {
        return conpletedYm;
    }

    public void setConpletedYm(Date conpletedYm) {
        this.conpletedYm = conpletedYm;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public Long getDivisionBuildingId() {
        return divisionBuildingId;
    }

    public void setDivisionBuildingId(Long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

    public String getDivisionCorpId() {
        return divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public String getEngManageFactoryNo() {
        return engManageFactoryNo;
    }

    public void setEngManageFactoryNo(String engManageFactoryNo) {
        this.engManageFactoryNo = engManageFactoryNo;
    }

    public String getEngManageFactoryType() {
        return engManageFactoryType;
    }

    public void setEngManageFactoryType(String engManageFactoryType) {
        this.engManageFactoryType = engManageFactoryType;
    }

    public String getEstimateUse() {
        return estimateUse;
    }

    public void setEstimateUse(String estimateUse) {
        this.estimateUse = estimateUse;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(String floorCount) {
        this.floorCount = floorCount;
    }

    public Integer getFreonDischargeOffice() {
        return freonDischargeOffice;
    }

    public void setFreonDischargeOffice(Integer freonDischargeOffice) {
        this.freonDischargeOffice = freonDischargeOffice;
    }

    public BigDecimal getKubunShoyuRate() {
        return kubunShoyuRate;
    }

    public void setKubunShoyuRate(BigDecimal kubunShoyuRate) {
        this.kubunShoyuRate = kubunShoyuRate;
    }

    public String getNyukyoTypeCd() {
        return nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public Integer getPublicFlg() {
        return publicFlg;
    }

    public void setPublicFlg(Integer publicFlg) {
        this.publicFlg = publicFlg;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public Date getTotalEndYm() {
        return totalEndYm;
    }

    public void setTotalEndYm(Date totalEndYm) {
        this.totalEndYm = totalEndYm;
    }

    public Date getTotalStartYm() {
        return totalStartYm;
    }

    public void setTotalStartYm(Date totalStartYm) {
        this.totalStartYm = totalStartYm;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getZipCd() {
        return zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

}
