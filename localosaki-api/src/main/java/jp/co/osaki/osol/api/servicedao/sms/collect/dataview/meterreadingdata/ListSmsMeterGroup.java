package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.math.BigDecimal;

/**
 * メーターグループ合計料金データ ResultDataクラス.
 * @author kobayashi.sho
 */
public class ListSmsMeterGroup {

    /** 企業ID. */
    private String corpId;

    /** 建物ID. */
    private Long buildingId;

    /** 表示年. */
    private String year;

    /** 表示月. */
    private String month;

    /** メーターグループID. */
    private Long meterGroupId;

    /** グループ名. */
    private String meterGroupName;

    /** グループ料金合計. */
    private BigDecimal groupPrice;

    public ListSmsMeterGroup() {

    }

    public ListSmsMeterGroup(Long meterGroupId, String meterGroupName, BigDecimal groupPrice){
        this.meterGroupId = meterGroupId;
        this.meterGroupName = meterGroupName;
        this.groupPrice = groupPrice;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setMeterGroupId(Long meterGroupId) {
        this.meterGroupId = meterGroupId;
    }

    public Long getMeterGroupId() {
        return meterGroupId;
    }

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public String getMeterGroupName() {
        return meterGroupName;
    }

    public void setMeterGroupName(String meterGroupName) {
        this.meterGroupName = meterGroupName;
    }

}
