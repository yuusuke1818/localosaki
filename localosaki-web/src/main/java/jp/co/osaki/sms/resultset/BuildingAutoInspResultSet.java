package jp.co.osaki.sms.resultset;

import java.math.BigDecimal;
import java.util.List;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.parameter.BuildingAutoInspSearchCondition;

/**
 * 建物に紐付く自動検針データ取得ResultSetクラス.
 *
 * @author ozaki.y
 */
public class BuildingAutoInspResultSet {

    public BuildingAutoInspResultSet(List<TBuilding> buildingList,
            List<BuildingAutoInspSearchCondition> autoInspDateList) {

        setBuildingList(buildingList);
        setAutoInspDateList(autoInspDateList);
    }

    public BuildingAutoInspResultSet(String corpId, Long buildingId, Long meterType, String meterTypeName,
            String autoInspDay, BigDecimal autoInspHour) {

        setCorpId(corpId);
        setBuildingId(buildingId);
        setMeterType(meterType);
        setMeterTypeName(meterTypeName);
        setAutoInspDay(autoInspDay);
        setAutoInspHour(autoInspHour);
    }

    /** 建物List. */
    private List<TBuilding> buildingList;

    /** 自動検針日List. */
    private List<BuildingAutoInspSearchCondition> autoInspDateList;

    /** 企業ID. */
    private String corpId;

    /** 建物ID. */
    private Long buildingId;

    /** メーター種別. */
    private Long meterType;

    /** メーター種別名称. */
    private String meterTypeName;

    /** 検針実行月. */
    private String autoInspMonth;

    /** 自動検針日. */
    private String autoInspDay;

    /** 自動検針時. */
    private BigDecimal autoInspHour;

    public List<TBuilding> getBuildingList() {
        return buildingList;
    }

    public void setBuildingList(List<TBuilding> buildingList) {
        this.buildingList = buildingList;
    }

    public List<BuildingAutoInspSearchCondition> getAutoInspDateList() {
        return autoInspDateList;
    }

    public void setAutoInspDateList(List<BuildingAutoInspSearchCondition> autoInspDateList) {
        this.autoInspDateList = autoInspDateList;
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

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getAutoInspMonth() {
        return autoInspMonth;
    }

    public void setAutoInspMonth(String autoInspMonth) {
        this.autoInspMonth = autoInspMonth;
    }

    public String getAutoInspDay() {
        return autoInspDay;
    }

    public void setAutoInspDay(String autoInspDay) {
        this.autoInspDay = autoInspDay;
    }

    public BigDecimal getAutoInspHour() {
        return autoInspHour;
    }

    public void setAutoInspHour(BigDecimal autoInspHour) {
        this.autoInspHour = autoInspHour;
    }
}