package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspUnitPriceResultSet {
    private Long limitUsageVal;
    private BigDecimal unitPrice;
    public AutoInspUnitPriceResultSet(Long limitUsageVal, BigDecimal unitPrice) {
        super();
        this.limitUsageVal = limitUsageVal;
        this.unitPrice = unitPrice;
    }
    public Long getLimitUsageVal() {
        return limitUsageVal;
    }
    public void setLimitUsageVal(Long limitUsageVal) {
        this.limitUsageVal = limitUsageVal;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

}
