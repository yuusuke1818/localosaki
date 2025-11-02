package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

/**
 * 建物情報 格納クラス
 *
 * @author yoneda_y
 */
public class BuildingInfo {

    /** 企業ID */
    private String corpId;

    /** 企業名 */
    private String corpName;

    /** 建物ID */
    private Long buildingId;

    /** 建物番号 */
    private String buildingNo;

    /** 建物名 */
    private String buildingName;

    /** 入居形態 */
    private String nyukyoType;

    /** 住所 */
    private String address;

    /** 稼働状況 */
    private String buildingStatus;

    public BuildingInfo() {
    }

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

    public String getNyukyoType() {
        return nyukyoType;
    }

    public void setNyukyoType(String nyukyoType) {
        this.nyukyoType = nyukyoType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

}
