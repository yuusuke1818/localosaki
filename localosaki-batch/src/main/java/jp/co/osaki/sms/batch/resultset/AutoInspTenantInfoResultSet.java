package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class AutoInspTenantInfoResultSet {
    /* 契約容量(kVA) */
    private BigDecimal contractCapacity;
    /* 電気契約プランID(0：その他, 1：従量電灯A, 2:従量電灯B, 3:ファミリータイププラン2) */
    private Long powerPlanId;
    /* 料金プランID */
    private Long pricePlanId;
    /* 基本料金 */
    private BigDecimal basicPrice;
    /* 割引率 */
    private BigDecimal discountRate;
    public AutoInspTenantInfoResultSet(BigDecimal contractCapacity, Long powerPlanId, Long pricePlanId, BigDecimal basicPrice, BigDecimal discountRate) {
        super();
        this.contractCapacity = contractCapacity;
        this.powerPlanId = powerPlanId;
        this.pricePlanId = pricePlanId;
        this.basicPrice = basicPrice;
        this.discountRate = discountRate;
    }
    public BigDecimal getContractCapacity() {
        return contractCapacity;
    }
    public void setContractCapacity(BigDecimal contractCapacity) {
        this.contractCapacity = contractCapacity;
    }
    public Long getPowerPlanId() {
        return powerPlanId;
    }
    public void setPowerPlanId(Long powerPlanId) {
        this.powerPlanId = powerPlanId;
    }
    public Long getPricePlanId() {
        return pricePlanId;
    }
    public void setPricePlanId(Long pricePlanId) {
        this.pricePlanId = pricePlanId;
    }
    public BigDecimal getBasicPrice() {
        return basicPrice;
    }
    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }
    public BigDecimal getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}
