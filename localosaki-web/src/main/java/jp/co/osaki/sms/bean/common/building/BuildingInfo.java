package jp.co.osaki.sms.bean.common.building;

public class BuildingInfo {

    private String corpId;
    private String corpName;
    private String buildingId;
    // 2017/07/31 検索改善
    private String buildingNo;
    private String buildingName;
    private boolean tenantFlg;
    private String buildingTenant;
    private String buildingNoOrName;
    private String buildingAddress;
    private String buildingStatus;
    private String buildingNyukyoType;
    //所属企業ID
    private String divisionCorpId;
    //所属建物ID
    private Long divisionBuildingId;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

    public String getBuildingNyukyoType() {
        return buildingNyukyoType;
    }

    public void setBuildingNyukyoType(String buildingNyukyoType) {
        this.buildingNyukyoType = buildingNyukyoType;
    }

    public String getBuildingNoOrName() {
        return buildingNoOrName;
    }

    public void setBuildingNoOrName(String buildingNoOrName) {
        this.buildingNoOrName = buildingNoOrName;
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

    public boolean getTenantFlg() {
        return tenantFlg;
    }

    public void setTenantFlg(boolean tenantFlg) {
        this.tenantFlg = tenantFlg;
    }
            
    public String getBuildingTenant() {
        return buildingTenant;
    }

    public void setBuildingTenant(String buildingTenant) {
        this.buildingTenant = buildingTenant;
    }

    public String getDivisionCorpId() {
        return divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public Long getDivisionBuildingId() {
        return divisionBuildingId;
    }

    public void setDivisionBuildingId(Long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

}
