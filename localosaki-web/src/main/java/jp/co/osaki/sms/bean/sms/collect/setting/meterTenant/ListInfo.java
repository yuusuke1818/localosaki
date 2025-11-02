package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import jp.co.osaki.osol.entity.TBuildingPK;

/**
 * 建物、検索結果格納クラス
 *
 */
public class ListInfo {

    private TBuildingPK id;

    private String corpId;

    private String corpName;

    private String buildingId;

    private String buildingStatus;

    private String buildingNo;

    private String buildingName;

    private String buildingAddress;

    private String nyukyoType;

    private String buildingTenant;

    private boolean tenantFlg;

    private String tenantId;

    public ListInfo() {
    }

    public TBuildingPK getId() {
        return id;
    }

    public void setId(TBuildingPK id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
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

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getNyukyoType() {
        return nyukyoType;
    }

    public void setNyukyoType(String nyukyoType) {
        this.nyukyoType = nyukyoType;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBuildingTenant() {
        return buildingTenant;
    }

    public void setBuildingTenant(String buildingTenant) {
        this.buildingTenant = buildingTenant;
    }

    public boolean getTenantFlg() {
        return tenantFlg;
    }

    public void setTenantFlg(boolean tenantFlg) {
        this.tenantFlg = tenantFlg;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "corpId = " + corpId
                + " buildingId = " + buildingId
                + " buildingStatus = " + buildingStatus
                + " buildingNo = " + buildingNo
                + " buildingName = " + buildingName
                + " buildingAddress = " + buildingAddress;
    }
}
