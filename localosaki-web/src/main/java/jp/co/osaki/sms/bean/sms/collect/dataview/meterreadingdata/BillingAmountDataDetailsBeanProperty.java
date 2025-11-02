package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDataByTenant;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDetailData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataFixedCostResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataGroupTotalChargeResultData;

@Named(value = "billingAmountDataDetailsBeanProperty")
@Dependent
public class BillingAmountDataDetailsBeanProperty implements Serializable {

    private static final long serialVersionUID = -5254823700778644268L;

    public void setData(BillingAmountDataByTenant billingAmountDataByTenant) {
    	this.tenantId = String.valueOf(billingAmountDataByTenant.getTenantId());
    	this.buildingNo = billingAmountDataByTenant.getBuildingNo();
    	this.buildingName = billingAmountDataByTenant.getBuildingName();
    	this.usageFees = billingAmountDataByTenant.getMeterSubtotal();
    	this.dispUsageFees = billingAmountDataByTenant.getDispMeterSubtotal();
    	this.meterGroupTotalCharges = billingAmountDataByTenant.getGroupProrateSubtotal();
    	this.dispMeterGroupTotalCharges = billingAmountDataByTenant.getGroupProrateSubtotal();
    	this.fixedCosts = billingAmountDataByTenant.getFixedCostSubtotal();
    	this.dispFixedCosts = billingAmountDataByTenant.getDispFixedCostSubtotal();
    	this.sumUsageFee = billingAmountDataByTenant.getSumUsageFee();
    	this.sumTax = billingAmountDataByTenant.getSumTax();
    	this.salesTaxRate = billingAmountDataByTenant.getSalesTaxRate();
    	this.dispSumUsageFee = billingAmountDataByTenant.getDispSumUsageFee();
    	this.dispSumTax = billingAmountDataByTenant.getDispSumTax();
    	this.fixedCostList = billingAmountDataByTenant.getFixedCosts();
    	this.billingAmountDetails = billingAmountDataByTenant.getBillingAmountDetails();
    }

    public void clearData(){
    	this.tenantId = null;
    	this.buildingNo = null;
    	this.buildingName = null;
    	this.usageFees = null;
    	this.meterGroupTotalCharges = null;
    	this.fixedCosts = null;
    	this.sumUsageFee = null;
    	this.sumTax = null;
    	this.salesTaxRate = null;
    	this.fixedCostList = null;
    	this.billingAmountDetails = null;
    }

    /** ユーザーコード(テナントID) */
    private String tenantId;

    /** テナント番号 */
    private String buildingNo;

    /** テナント名 */
    private String buildingName;

    /** 請求金額データ一覧. */
    private List<BillingAmountDetailData> billingAmountDetails = new ArrayList<BillingAmountDetailData>();

    // FIXME
    /** メーターグループ合計料金一覧. */
    private List<ListSmsBillingAmountDataGroupTotalChargeResultData> meterGroupTotalChargeList = new ArrayList<ListSmsBillingAmountDataGroupTotalChargeResultData>();

    /** グループ按分小計. */
    private String meterGroupTotalCharges;

    /** 表示用：グループ按分小計. */
    private String dispMeterGroupTotalCharges;

    /** 固定費 */
    private List<ListSmsBillingAmountDataFixedCostResultData> fixedCostList = new ArrayList<ListSmsBillingAmountDataFixedCostResultData>();

    /** 固定費小計. */
    private String fixedCosts;

    /** 表示用：固定費小計. */
    private String dispFixedCosts;

    /** 消費税率. */
    private String salesTaxRate;

    /** 消費税扱い. */
    private String salesTaxTreatment;

    /** 小数部端数処理. */
    private String fractionalProcessing;

    /** メーター小計. */
    private String usageFees;

    /** 表示用：メーター小計. */
    private String dispUsageFees;

    /** 今回請求金額(今回使用料金) */
    private String sumUsageFee;

    /** 消費税 */
    private String sumTax;

    /** 表示用：今回請求金額(今回使用料金) */
    private String dispSumUsageFee;

    /** 表示用：消費税 */
    private String dispSumTax;

    /** ステータス. */
    private String status;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public List<ListSmsBillingAmountDataGroupTotalChargeResultData> getMeterGroupTotalChargeList() {
		return meterGroupTotalChargeList;
	}

	public void setMeterGroupTotalChargeList(
			List<ListSmsBillingAmountDataGroupTotalChargeResultData> meterGroupTotalChargeList) {
		this.meterGroupTotalChargeList = meterGroupTotalChargeList;
	}

	public String getMeterGroupTotalCharges() {
		return meterGroupTotalCharges;
	}

	public void setMeterGroupTotalCharges(String meterGroupTotalCharges) {
		this.meterGroupTotalCharges = meterGroupTotalCharges;
	}

	public List<ListSmsBillingAmountDataFixedCostResultData> getFixedCostList() {
		return fixedCostList;
	}

	public void setFixedCostList(List<ListSmsBillingAmountDataFixedCostResultData> fixedCostList) {
		this.fixedCostList = fixedCostList;
	}

	public String getFixedCosts() {
		return fixedCosts;
	}

	public void setFixedCosts(String fixedCosts) {
		this.fixedCosts = fixedCosts;
	}

	public String getSalesTaxRate() {
		return salesTaxRate;
	}

	public void setSalesTaxRate(String salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}

	public String getSalesTaxTreatment() {
		return salesTaxTreatment;
	}

	public void setSalesTaxTreatment(String salesTaxTreatment) {
		this.salesTaxTreatment = salesTaxTreatment;
	}

	public String getFractionalProcessing() {
		return fractionalProcessing;
	}

	public void setFractionalProcessing(String fractionalProcessing) {
		this.fractionalProcessing = fractionalProcessing;
	}

	public String getUsageFees() {
		return usageFees;
	}

	public void setUsageFees(String usageFees) {
		this.usageFees = usageFees;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSumUsageFee() {
		return sumUsageFee;
	}

	public void setSumUsageFee(String sumUsageFee) {
		this.sumUsageFee = sumUsageFee;
	}

	public String getSumTax() {
		return sumTax;
	}

	public void setSumTax(String sumTax) {
		this.sumTax = sumTax;
	}

	public List<BillingAmountDetailData> getBillingAmountDetails() {
		return billingAmountDetails;
	}

	public void setBillingAmountDetails(List<BillingAmountDetailData> billingAmountDetails) {
		this.billingAmountDetails = billingAmountDetails;
	}

	public String getDispSumUsageFee() {
		return dispSumUsageFee;
	}

	public void setDispSumUsageFee(String dispSumUsageFee) {
		this.dispSumUsageFee = dispSumUsageFee;
	}

	public String getDispSumTax() {
		return dispSumTax;
	}

	public void setDispSumTax(String dispSumTax) {
		this.dispSumTax = dispSumTax;
	}

	public String getDispMeterGroupTotalCharges() {
		return dispMeterGroupTotalCharges;
	}

	public void setDispMeterGroupTotalCharges(String dispMeterGroupTotalCharges) {
		this.dispMeterGroupTotalCharges = dispMeterGroupTotalCharges;
	}

	public String getDispFixedCosts() {
		return dispFixedCosts;
	}

	public void setDispFixedCosts(String dispFixedCosts) {
		this.dispFixedCosts = dispFixedCosts;
	}

	public String getDispUsageFees() {
		return dispUsageFees;
	}

	public void setDispUsageFees(String dispUsageFees) {
		this.dispUsageFees = dispUsageFees;
	}
}
