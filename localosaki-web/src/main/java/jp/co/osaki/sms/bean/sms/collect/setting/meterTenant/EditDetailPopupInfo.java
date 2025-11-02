package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.util.List;

public class EditDetailPopupInfo implements Cloneable {

    /**
     * メーター種別設定.メーター種別
     */
    private Long meterType;

    /**
     * メーター種別設定.メニュー番号(0:その他  1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
     */
    private Long menuNo;

    /**
     * メーター種別設定.メーター種別名称
     */
    private String meterTypeName;

    /**
     * メーター種別設定.従量単位
     */
    private String unitUsageBased;

    /**
     * 料金単価情報.料金プランID
     */
    private Long pricePlanId;

    /**
     * 基本料金（0:その他、従量電灯B）
     */
    private String basicPrice;

    /**
     * 最低料金（1:従量電灯A）
     */
    private String lowestPrice;

    /**
     * 燃料費調整額(最初の15kWhまで)
     */
    private String fuelAdjustPrice;

    /**
     * 燃料費調整額(15kWh超過)
     */
    private String adjustPriceOver15;

    /**
     * 再生可能エネルギー発電促進賦課金(最初の15kWhまで)
     */
    private String renewEnerPrice;

    /**
     * 再生可能エネルギー発電促進賦課金(15kWh超過)
     */
    private String renewPriceOver15;

    /**
     * テナント料金情報.割引率
     */
    private String discountRate;

    /**
     * 従量料金単価リスト
     */
    private List<RangeUnitPriceData> rangeUnitPriceDataList;

    private boolean bulkFlg;

    @Override
    protected EditDetailPopupInfo clone() throws CloneNotSupportedException {
        return (EditDetailPopupInfo) super.clone();
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public Long getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(Long menuNo) {
        this.menuNo = menuNo;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getUnitUsageBased() {
        return unitUsageBased;
    }

    public void setUnitUsageBased(String unitUsageBased) {
        this.unitUsageBased = unitUsageBased;
    }

    public Long getPricePlanId() {
        return pricePlanId;
    }

    public void setPricePlanId(Long pricePlanId) {
        this.pricePlanId = pricePlanId;
    }

    public List<RangeUnitPriceData> getRangeUnitPriceDataList() {
        return rangeUnitPriceDataList;
    }

    public String getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(String basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getFuelAdjustPrice() {
        return fuelAdjustPrice;
    }

    public void setFuelAdjustPrice(String fuelAdjustPrice) {
        this.fuelAdjustPrice = fuelAdjustPrice;
    }

    public String getAdjustPriceOver15() {
        return adjustPriceOver15;
    }

    public void setAdjustPriceOver15(String adjustPriceOver15) {
        this.adjustPriceOver15 = adjustPriceOver15;
    }

    public String getRenewEnerPrice() {
        return renewEnerPrice;
    }

    public void setRenewEnerPrice(String renewEnerPrice) {
        this.renewEnerPrice = renewEnerPrice;
    }

    public String getRenewPriceOver15() {
        return renewPriceOver15;
    }

    public void setRenewPriceOver15(String renewPriceOver15) {
        this.renewPriceOver15 = renewPriceOver15;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public void setRangeUnitPriceDataList(List<RangeUnitPriceData> rangeUnitPriceDataList) {
        this.rangeUnitPriceDataList = rangeUnitPriceDataList;
    }

    public boolean isBulkFlg() {
        return bulkFlg;
    }

    public void setBulkFlg(boolean bulkFlg) {
        this.bulkFlg = bulkFlg;
    }
}
