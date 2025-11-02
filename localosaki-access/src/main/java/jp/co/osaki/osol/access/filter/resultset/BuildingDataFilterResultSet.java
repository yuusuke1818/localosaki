package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * 建物データフィルター用 取得結果
 *
 * @author take_suzuki
 */
public class BuildingDataFilterResultSet extends BaseResultSet {

    /**
     * 企業ID
     */
    private String corpId;

    /**
     * 建物ID
     */
    private Long buildingId;

    /**
     * コンストラクタ
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     */
    public BuildingDataFilterResultSet(String corpId, Long buildingId) {
        this.corpId = corpId;
        this.buildingId = buildingId;
    }

    /**
     *
     * 企業ID 取得
     * 
     * @return 企業ID
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     *
     * 企業ID 設定
     * 
     * @param corpId 企業ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     *
     * 建物ID 取得
     * 
     * @return 建物ID
     */
    public Long getBuildingId() {
        return buildingId;
    }

    /**
     *
     * 建物ID 設定
     * 
     * @param buildingId 建物ID
     */
    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

}
