package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.BuildDevMeterResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterGroupNameResultData;
import jp.co.osaki.osol.entity.TBuilding;

@Named(value = "smsCollectSettingTenantEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    private static final long serialVersionUID = -798523268574466855L;

    // 更新処理フラグ(更新:true/新規作成:false)
    private boolean updateProcessFlg;

    // 企業ID
    private String corpId;

    // 建物ID
    private Long buildingId;

    // ----------------------------------------------------------
    // 基本情報 関連
    // ----------------------------------------------------------
    // 建物番号
    private String buildingNo;

    // 建物テナント
    private String buildingTenant;

    // テナントフラグ
    private Boolean tenantFlg;

    // 建物名
    private String buildingName;

    // 建物名かな
    private String buildingNameKana;

    // 建物短縮名称
    private String buildingTansyukuName;

    // 郵便番号
    private String zipCd;

    // 都道府県コード
    private String prefectureCd;

    // 自治体コード
    private String municipalityCd;

    // 住所
    private String municipalityName;

    // 住所(建物名)
    private String addressBuilding;

    // TEL
    private String telNo;

    // FAX
    private String faxNo;

    // 備考
    private String biko;

    // 地上階数
    private String floorCount;

    // 地下階数
    private String basementCount;

    // 入居形態コード
    private String nyukyoTypeCd;

    // 共有部比率
    private String commonUsedRate;

    // 区分所有割合
    private String kubunShoyuRate;

    // 竣工年月
    private String conpletedYm;

    // エネルギー管理指定工場種別
    private String engManageFactoryType;

    // エネルギー管理指定工場番号
    private String engManageFactoryNo;

    // 細分類ID
    private Long subtypeId;

    // フロン法排出特定事業所
    private Integer freonDischargeOffice;
    private boolean freonDischargeOfficeFlg; //画面表示用

    // 集計開始年月
    private String totalStartYm;

    // 集計終了年月
    private String totalEndYm;

    // 推計フラグ
    private String estimateUse;

    // 推計種別ID
    private Long estimateId;

    // 入力担当者ID
    private String personId;

    /* 他 */
    // 作成日時
    private Date createDate;
    // 作成ユーザーID
    private long createUserId;
    // 更新日時
    private Date updateDate;
    // 更新ユーザーID
    private long updateUserId;
    // 削除フラグ
    private Integer delFlg;
    private boolean bDelFlg; //画面表示用

    // 建物削除日時
    private Date buildingDelDate;
    // 建物削除者
    private String buildingDelUserId;

    private String divisionCorpId;

    private String divisionCorpName;

    private Long divisionBuildingId;

    private String divisionBuildingNo;

    private String divisionBuildingName;

    private boolean bPublicFlg; //画面表示用

    // entity情報保持
    private TBuilding tBuilding;

    // ----------------------------------------------------------
    // メーターテナント情報
    // ----------------------------------------------------------

    // テナントID（画面上はユーザーコード）
    private String tenantId;

    // 固定費１名称
    private String fixedName1;

    // 固定費１金額
    private String fixedPrice1;

    // 固定費２名称
    private String fixedName2;

    // 固定費２金額
    private String fixedPrice2;

    // 固定費３名称
    private String fixedName3;

    // 固定費３金額
    private String fixedPrice3;

    // 固定費４名称
    private String fixedName4;

    // 固定費４金額
    private String fixedPrice4;

    // 料金メニュー番号
    private BigDecimal priceMenuNo;

    // 契約容量
    private BigDecimal contractCapacity;

    // 按分率1
    private String divRate1;

    // 按分率2
    private String divRate2;

    // 按分率3
    private String divRate3;

    // 按分率4
    private String divRate4;

    // 按分率5
    private String divRate5;

    // 按分率6
    private String divRate6;

    // 按分率7
    private String divRate7;

    // 按分率8
    private String divRate8;

    // 按分率9
    private String divRate9;

    // 按分率10
    private String divRate10;

    //メーターグループ名称設定リスト
    private List<MeterGroupNameResultData> meterGroupNameList;

    // メーター種別詳細情報リスト（詳細設定画面）
    private List<EditDetailPopupInfo> EditDetailPopupInfoList;

    // 登録計器（メーター）ID
    private String meterListStr = "";

    // 登録計器（メーター）ID
    private List<BuildDevMeterResultData> buildDevMeterList;





    // ----------------------------------------------------------
    // 入力担当者 関連
    // ----------------------------------------------------------
    // 一覧表示css制御用
    private String columnClassesStr;

    // 一覧表示css制御用
    private String columnClassesStrPerson;

    // 一覧表示css制御用
    private String tableHeaderClassesStr;

    // 暫定
    private String addPersonId;

    private boolean searchedFlg;

    // ---------検索条件----------
    /**
     * 担当者ふりがな
     */
    private String personKana;
    /**
     * 担当者名
     */
    private String personName;
    /**
     * 部署名
     */
    private String deptName;
    /**
     * 役職名
     */
    private String positionName;
    /**
     * ユーザID
     */
    private String userId;

    /**
     * アカウント状態
     */
    @SuppressWarnings("unused")
    private Map<String, String> CheckAccountStatusTypeMap;

    public EditBeanProperty() {
    }


    public boolean getUpdateProcessFlg() {
        return updateProcessFlg;
    }

    public void setUpdateProcessFlg(boolean updateProcessFlg) {
        this.updateProcessFlg = updateProcessFlg;
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

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Boolean getTenantFlg() {
        return tenantFlg;
    }

    public void setTenantFlg(Boolean tenantFlg) {
        this.tenantFlg = tenantFlg;
    }

    public String getBuildingTenant() {
        return buildingTenant;
    }

    public void setBuildingTenant(String buildingTenant) {
        this.buildingTenant = buildingTenant;
    }

    public String getBuildingNameKana() {
        return buildingNameKana;
    }

    public void setBuildingNameKana(String buildingNameKana) {
        this.buildingNameKana = buildingNameKana;
    }

    public String getBuildingTansyukuName() {
        return buildingTansyukuName;
    }

    public void setBuildingTansyukuName(String buildingTansyukuName) {
        this.buildingTansyukuName = buildingTansyukuName;
    }

    public String getZipCd() {
        return zipCd;
    }

    public void setZipCd(String zipCd) {
        this.zipCd = zipCd;
    }

    public String getPrefectureCd() {
        return prefectureCd;
    }

    public void setPrefectureCd(String prefectureCd) {
        this.prefectureCd = prefectureCd;
    }

    public String getMunicipalityCd() {
        return municipalityCd;
    }

    public void setMunicipalityCd(String municipalityCd) {
        this.municipalityCd = municipalityCd;
    }

    public String getMunicipalityName() {
        return municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        this.municipalityName = municipalityName;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getBiko() {
        return biko;
    }

    public void setBiko(String biko) {
        this.biko = biko;
    }

    public String getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(String floorCount) {
        this.floorCount = floorCount;
    }

    public String getBasementCount() {
        return basementCount;
    }

    public void setBasementCount(String basementCount) {
        this.basementCount = basementCount;
    }

    public String getNyukyoTypeCd() {
        return nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public String getCommonUsedRate() {
        return commonUsedRate;
    }

    public void setCommonUsedRate(String commonUsedRate) {
        this.commonUsedRate = commonUsedRate;
    }

    public String getKubunShoyuRate() {
        return kubunShoyuRate;
    }

    public void setKubunShoyuRate(String kubunShoyuRate) {
        this.kubunShoyuRate = kubunShoyuRate;
    }

    public String getConpletedYm() {
        return conpletedYm;
    }

    public void setConpletedYm(String conpletedYm) {
        this.conpletedYm = conpletedYm;
    }

    public String getEngManageFactoryType() {
        return engManageFactoryType;
    }

    public void setEngManageFactoryType(String engManageFactoryType) {
        this.engManageFactoryType = engManageFactoryType;
    }

    public String getEngManageFactoryNo() {
        return engManageFactoryNo;
    }

    public void setEngManageFactoryNo(String engManageFactoryNo) {
        this.engManageFactoryNo = engManageFactoryNo;
    }

    public Long getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(Long subtypeId) {
        this.subtypeId = subtypeId;
    }

    public Integer getFreonDischargeOffice() {
        return freonDischargeOffice;
    }

    public void setFreonDischargeOffice(Integer freonDischargeOffice) {
        this.freonDischargeOffice = freonDischargeOffice;
    }

    public boolean getFreonDischargeOfficeFlg() {
        return freonDischargeOfficeFlg;
    }

    public void setFreonDischargeOfficeFlg(boolean freonDischargeOfficeFlg) {
        this.freonDischargeOfficeFlg = freonDischargeOfficeFlg;
    }

    public String getTotalStartYm() {
        return totalStartYm;
    }

    public void setTotalStartYm(String totalStartYm) {
        this.totalStartYm = totalStartYm;
    }

    public String getTotalEndYm() {
        return totalEndYm;
    }

    public void setTotalEndYm(String totalEndYm) {
        this.totalEndYm = totalEndYm;
    }

    public String getEstimateUse() {
        return estimateUse;
    }

    public void setEstimateUse(String estimateUse) {
        this.estimateUse = estimateUse;
    }

    public Long getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(Long estimateId) {
        this.estimateId = estimateId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public boolean getbDelFlg() {
        return bDelFlg;
    }

    public void setbDelFlg(boolean bDelFlg) {
        this.bDelFlg = bDelFlg;
    }

    public Date getBuildingDelDate() {
        return buildingDelDate;
    }

    public void setBuildingDelDate(Date buildingDelDate) {
        this.buildingDelDate = buildingDelDate;
    }

    public String getBuildingDelUserId() {
        return buildingDelUserId;
    }

    public void setBuildingDelUserId(String buildingDelUserId) {
        this.buildingDelUserId = buildingDelUserId;
    }

    public String getDivisionCorpId() {
        return divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public String getDivisionCorpName() {
        return divisionCorpName;
    }

    public void setDivisionCorpName(String divisionCorpName) {
        this.divisionCorpName = divisionCorpName;
    }

    public Long getDivisionBuildingId() {
        return divisionBuildingId;
    }

    public void setDivisionBuildingId(Long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

    public String getDivisionBuildingNo() {
        return divisionBuildingNo;
    }

    public void setDivisionBuildingNo(String divisionBuildingNo) {
        this.divisionBuildingNo = divisionBuildingNo;
    }

    public String getDivisionBuildingName() {
        return divisionBuildingName;
    }

    public void setDivisionBuildingName(String divisionBuildingName) {
        this.divisionBuildingName = divisionBuildingName;
    }

    public boolean getbPublicFlg() {
        return bPublicFlg;
    }

    public void setbPublicFlg(boolean bPublicFlg) {
        this.bPublicFlg = bPublicFlg;
    }


    public String getColumnClassesStr() {
        return columnClassesStr;
    }

    public void setColumnClassesStr(String columnClassesStr) {
        this.columnClassesStr = columnClassesStr;
    }

    public String getColumnClassesStrPerson() {
        return columnClassesStrPerson;
    }

    public void setColumnClassesStrPerson(String columnClassesStrPerson) {
        this.columnClassesStrPerson = columnClassesStrPerson;
    }


    // 暫定
    public String getAddPersonId() {
        return addPersonId;
    }

    // 暫定
    public void setAddPersonId(String addPersonId) {
        this.addPersonId = addPersonId;
    }


    public TBuilding gettBuilding() {
        return tBuilding;
    }

    public void settBuilding(TBuilding tBuilding) {
        this.tBuilding = tBuilding;
    }


    public boolean getSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

    public String getPersonKana() {
        return personKana;
    }

    public void setPersonKana(String personKana) {
        this.personKana = personKana;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getCheckAccountStatusTypeMap() {
        LinkedHashMap<String, String> retMap = new LinkedHashMap<>();
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getName(),
                OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getName(),
                OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName(),
                OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName(),
                OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getVal());
        retMap.put(OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getName(),
                OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getVal());
        return retMap;
    }

    public void setCheckAccountStatusTypeMap(Map<String, String> CheckAccountStatusTypeMap) {
        this.CheckAccountStatusTypeMap = CheckAccountStatusTypeMap;
    }

    public String getTableHeaderClassesStr() {
        return tableHeaderClassesStr;
    }

    public void setTableHeaderClassesStr(String tableHeaderClassesStr) {
        this.tableHeaderClassesStr = tableHeaderClassesStr;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFixedName1() {
        return fixedName1;
    }

    public void setFixedName1(String fixedName1) {
        this.fixedName1 = fixedName1;
    }

    public String getFixedPrice1() {
        return fixedPrice1;
    }

    public void setFixedPrice1(String fixedPrice1) {
        this.fixedPrice1 = fixedPrice1;
    }

    public String getFixedName2() {
        return fixedName2;
    }

    public void setFixedName2(String fixedName2) {
        this.fixedName2 = fixedName2;
    }

    public String getFixedPrice2() {
        return fixedPrice2;
    }

    public void setFixedPrice2(String fixedPrice2) {
        this.fixedPrice2 = fixedPrice2;
    }

    public String getFixedName3() {
        return fixedName3;
    }

    public void setFixedName3(String fixedName3) {
        this.fixedName3 = fixedName3;
    }

    public String getFixedPrice3() {
        return fixedPrice3;
    }

    public void setFixedPrice3(String fixedPrice3) {
        this.fixedPrice3 = fixedPrice3;
    }

    public String getFixedName4() {
        return fixedName4;
    }

    public void setFixedName4(String fixedName4) {
        this.fixedName4 = fixedName4;
    }

    public String getFixedPrice4() {
        return fixedPrice4;
    }

    public void setFixedPrice4(String fixedPrice4) {
        this.fixedPrice4 = fixedPrice4;
    }

    public BigDecimal getPriceMenuNo() {
        return priceMenuNo;
    }

    public void setPriceMenuNo(BigDecimal priceMenuNo) {
        this.priceMenuNo = priceMenuNo;
    }


    public BigDecimal getContractCapacity() {
        return contractCapacity;
    }


    public void setContractCapacity(BigDecimal contractCapacity) {
        this.contractCapacity = contractCapacity;
    }

    public String getDivRate1() {
        return divRate1;
    }


    public void setDivRate1(String divRate1) {
        this.divRate1 = divRate1;
    }


    public String getDivRate2() {
        return divRate2;
    }


    public void setDivRate2(String divRate2) {
        this.divRate2 = divRate2;
    }


    public String getDivRate3() {
        return divRate3;
    }


    public void setDivRate3(String divRate3) {
        this.divRate3 = divRate3;
    }


    public String getDivRate4() {
        return divRate4;
    }


    public void setDivRate4(String divRate4) {
        this.divRate4 = divRate4;
    }


    public String getDivRate5() {
        return divRate5;
    }


    public void setDivRate5(String divRate5) {
        this.divRate5 = divRate5;
    }


    public String getDivRate6() {
        return divRate6;
    }


    public void setDivRate6(String divRate6) {
        this.divRate6 = divRate6;
    }


    public String getDivRate7() {
        return divRate7;
    }


    public void setDivRate7(String divRate7) {
        this.divRate7 = divRate7;
    }


    public String getDivRate8() {
        return divRate8;
    }


    public void setDivRate8(String divRate8) {
        this.divRate8 = divRate8;
    }


    public String getDivRate9() {
        return divRate9;
    }


    public void setDivRate9(String divRate9) {
        this.divRate9 = divRate9;
    }


    public String getDivRate10() {
        return divRate10;
    }


    public void setDivRate10(String divRate10) {
        this.divRate10 = divRate10;
    }

    public List<MeterGroupNameResultData> getMeterGroupNameList() {
        return meterGroupNameList;
    }


    public void setMeterGroupNameList(List<MeterGroupNameResultData> meterGroupNameList) {
        this.meterGroupNameList = meterGroupNameList;
    }

    public List<EditDetailPopupInfo> getEditDetailPopupInfoList() {
        return EditDetailPopupInfoList;
    }

    public void setEditDetailPopupInfoList(List<EditDetailPopupInfo> editDetailPopupInfoList) {
        EditDetailPopupInfoList = editDetailPopupInfoList;
    }

    public String getMeterListStr() {
        return meterListStr;
    }

    public void setMeterListStr(String meterListStr) {
        this.meterListStr = meterListStr;
    }

    public List<BuildDevMeterResultData> getBuildingDevMeterList() {
        return buildDevMeterList;
    }

    public void setBuildingDevMeterList(List<BuildDevMeterResultData> buildingDevMeterList) {
        this.buildDevMeterList = buildingDevMeterList;
    }

}
