package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDetailData;

public class BillingAmountDataPdfBeanProperty {

    /** メーター種別名. */
    private String meterTypeName;

    /** 単位. */
    private String unitUsageBased;

    /** 最新検針値. */
    private String latestInspVal;

    /** 前回検針値. */
    private String prevInspVal;

    /** 乗率. */
    private String multipleRate;

    /** 使用量. */
    private String usageVal;

    /** 従量料金. */
    private String usageVolumeFee;

    /** 基本料金. */
    private String basicCharge;

    /** 再エネ賦課金. */
    private String renewableEnergyLevy;

    /** 使用料金. */
    private String usageFee;

    /** 検針日. */
    private String inspDate;

    /** 共用費. */
    private String sharedFee;

    /** メータ管理番号. */
    private String meterId;

    public BillingAmountDataPdfBeanProperty() {

    }

    public BillingAmountDataPdfBeanProperty(BillingAmountDetailData data){
        this.meterId = nvl(data.getMeterId());                                 // 型：文字  メーター管理番号
        this.meterTypeName = nvl(data.getMeterTypeName());                     // 型：文字  メーター種別名
        this.unitUsageBased = nvl(data.getUnitUsageBased());                   // 型：文字  単位
        this.latestInspVal = data.getThisInspVal();                            // 型：数値  最新検針値
        this.prevInspVal = data.getPrevInspVal();                              // 型：数値  前回検針値
        this.multipleRate = data.getMultipleRate();                            // 型：数値  乗率
        this.usageVal = data.getUsageVal();                                    // 型：数値  使用量
        this.basicCharge = removeComma(data.getBasicCharge());                 // 型：数値  基本料金
        this.usageVolumeFee = removeComma(data.getUsageVolumeFeeForPdf());     // 型：数値  従量料金※(従量料金＋燃料調整費)
        this.renewableEnergyLevy = removeComma(data.getRenewableEnergyLevy()); // 型：数値  再エネ賦課金
        this.usageFee = removeComma(data.getUsageFee());                       // 型：数値  使用料金※(割引後の額)
        this.inspDate = (data.getDispInspDate() == null ? "          " : data.getDispInspDate()); // 型：文字(先頭10文字)  検針日
    }

    private String nvl(String val) {
        if (val == null) {
            return "";
        }
        return val;
    }
    private String removeComma(String val) {
        if (val == null) {
            return "0"; // null の場合は"0"を出力する。（null にすると null と出力され、""にすると異常終了する）
        }
        return val.replace(",", "");
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

    public String getLatestInspVal() {
        return latestInspVal;
    }

    public void setLatestInspVal(String latestInspVal) {
        this.latestInspVal = latestInspVal;
    }

    public String getPrevInspVal() {
        return prevInspVal;
    }

    public void setPrevInspVal(String prevInspVal) {
        this.prevInspVal = prevInspVal;
    }

    public String getMultipleRate() {
        return multipleRate;
    }

    public void setMultipleRate(String multipleRate) {
        this.multipleRate = multipleRate;
    }

    public String getUsageVal() {
        return usageVal;
    }

    public void setUsageVal(String usageVal) {
        this.usageVal = usageVal;
    }

    public String getUsageVolumeFee() {
        return usageVolumeFee;
    }

    public void setUsageVolumeFee(String usageVolumeFee) {
        this.usageVolumeFee = usageVolumeFee;
    }

    public String getBasicCharge() {
        return basicCharge;
    }

    public void setBasicCharge(String basicCharge) {
        this.basicCharge = basicCharge;
    }

    public String getRenewableEnergyLevy() {
		return renewableEnergyLevy;
	}

	public void setRenewableEnergyLevy(String renewableEnergyLevy) {
		this.renewableEnergyLevy = renewableEnergyLevy;
	}

	public String getUsageFee() {
        return usageFee;
    }

    public void setUsageFee(String usageFee) {
        this.usageFee = usageFee;
    }

    public String getInspDate() {
        return inspDate;
    }

    public void setInspDate(String inspDate) {
        this.inspDate = inspDate;
    }

    public String getSharedFee() {
        return sharedFee;
    }

    public void setSharedFee(String sharedFee) {
        this.sharedFee = sharedFee;
    }
    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
}
