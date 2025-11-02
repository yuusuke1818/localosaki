package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.math.BigDecimal;

/**
 * メーター種別設定 + 従量値 検索結果
 * @author kobayashi.sho
 */
public class MeterType {

    /** メーター種別(Key). */
    private Long meterType;

    /** 企業ID(Key). */
    private String corpId;

    /** 建物ID(Key). */
    private Long buildingId;

    /** メニュー番号(Key)(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2). */
    private Long menuNo;

    /** メニュー名称(DB外項目). */
    private String menuName;

    /** メーター種別名称. */
    private String meterTypeName;

    /** 計算方法（NULL：--  1：単一制  2：段階制  3：業務用季節別時間帯別電力  4：業務用電力）. */
    private BigDecimal calcType;

    /** 単一制単価. */
    private BigDecimal unitPrice;

    /** 全体共用按分（NULL：--  1：固定  2：均等  3：使用量率  4：最大デマンド  5：料金メニュー）. */
    private String allComDiv;

    /** 従量単位. */
    private String unitUsageBased;

    /** 自動検針日時（日）（0：日の指定なし  1～31：日の指定）. */
    private String autoInspDay;

    /** CO2排出係数. */
    private String co2Coefficient;

    /** CO2排出係数単位. */
    private String unitCo2Coefficient;

    /** CO2排出係数単位名称(DB外項目). */
    private String unitCo2CoefficientName;

    /** 自動検針日時（時）（0～23：時の指定）. */
    private String autoInspHour;

    /** 排他制御用カラム. */
    private Integer version;

    /** 従量値(","区切り). */
    private String rangeValue;

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
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

    public Long getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(Long menuNo) {
        this.menuNo = menuNo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public BigDecimal getCalcType() {
        return calcType;
    }

    public void setCalcType(BigDecimal calcType) {
        this.calcType = calcType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAllComDiv() {
        return allComDiv;
    }

    public void setAllComDiv(String allComDiv) {
        this.allComDiv = allComDiv;
    }

    public String getUnitUsageBased() {
        return unitUsageBased;
    }

    public void setUnitUsageBased(String unitUsageBased) {
        this.unitUsageBased = unitUsageBased;
    }

    public String getAutoInspDay() {
        return autoInspDay;
    }

    public void setAutoInspDay(String autoInspDay) {
        this.autoInspDay = autoInspDay;
    }

    public String getCo2Coefficient() {
        return co2Coefficient;
    }

    public void setCo2Coefficient(String co2Coefficient) {
        this.co2Coefficient = co2Coefficient;
    }

    public String getUnitCo2Coefficient() {
        return unitCo2Coefficient;
    }

    public void setUnitCo2Coefficient(String unitCo2Coefficient) {
        this.unitCo2Coefficient = unitCo2Coefficient;
    }

    public String getAutoInspHour() {
        return autoInspHour;
    }

    public void setAutoInspHour(String autoInspHour) {
        this.autoInspHour = autoInspHour;
    }

    public String getUnitCo2CoefficientName() {
        return unitCo2CoefficientName;
    }

    public void setUnitCo2CoefficientName(String unitCo2CoefficientName) {
        this.unitCo2CoefficientName = unitCo2CoefficientName;
    }

    public String getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
