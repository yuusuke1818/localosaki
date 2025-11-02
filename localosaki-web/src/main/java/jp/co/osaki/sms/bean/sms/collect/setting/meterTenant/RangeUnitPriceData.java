package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

public class RangeUnitPriceData implements Cloneable {

    /**
     * メーター種別従量値情報.従量値
     */
    private Long rangeValue;

    /**
     * 料金単価情報.料金単価
     */
    private String unitPrice;

    @Override
    protected RangeUnitPriceData clone() throws CloneNotSupportedException {
        return (RangeUnitPriceData) super.clone();
    }

    public Long getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(Long rangeValue) {
        this.rangeValue = rangeValue;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

}
