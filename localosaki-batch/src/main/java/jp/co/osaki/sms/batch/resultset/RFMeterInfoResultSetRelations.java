package jp.co.osaki.sms.batch.resultset;

/**
 * SMS CSV取込 建物、装置関連リストResultSetクラス
 *
 * @author yonezawa.a
 *
 */
public class RFMeterInfoResultSetRelations {

    private String corpId;

    private Long buildingId;

    public RFMeterInfoResultSetRelations(String corpId, Long buildingId) {
        this.corpId = corpId;
        this.buildingId = buildingId;
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

}
