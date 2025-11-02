package jp.co.osaki.sms.batch.resultset;

/**
 * 検満通知用 建物情報 ResultSetクラス
 *
 * @author sagi_h
 *
 */
public class ExpiryNoticeBuildingResultSet {
    /** 建物ID */
    private Long buildingId;

    /** 建物名 */
    private String buildingName;

    /**
     * コンストラクタ
     *
     * @param buildingId
     * @param buildingName
     */
    public ExpiryNoticeBuildingResultSet(Long buildingId, String buildingName) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
    }

    /**
     * @return buildingId
     */
    public Long getBuildingId() {
        return buildingId;
    }

    /**
     * @param buildingId セットする buildingId
     */
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    /**
     * @return buildingName
     */
    public String getBuildingName() {
        return buildingName;
    }

    /**
     * @param buildingName セットする buildingName
     */
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
