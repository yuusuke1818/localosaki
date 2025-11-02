package jp.co.osaki.sms.resultset;

/**
 * 装置情報ResultSetクラス.
 *
 * @author ozaki.y
 */
public class MDevPrmListResultSet {

    /** 企業ID. */
    private String corpId;

    /** 建物ID. */
    private Long buildingId;

    /** テナントフラグ. */
    private boolean isTenant;

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

    public boolean isTenant() {
        return isTenant;
    }

    public void setTenant(boolean isTenant) {
        this.isTenant = isTenant;
    }
}
