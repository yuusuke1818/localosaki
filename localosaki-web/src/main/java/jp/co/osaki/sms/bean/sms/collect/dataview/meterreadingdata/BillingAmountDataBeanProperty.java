package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "billingAmountDataBeanProperty")
@Dependent
public class BillingAmountDataBeanProperty implements Serializable {

    private static final long serialVersionUID = -652148224342720851L;

    /** 印刷対象テナントコード開始. */
    private String printBuildingNoFrom;

    /** 印刷対象テナントコード終了. */
    private String printBuildingNoTo;

    /** 請求年月日 **/
    private String billingDate;

    /** 発行日. */
    private String dateIssue;

    /** 発行日(カレンダーコントロール用). */
    private Date calDateIssue;

    /** 納期限. */
    private String deadLine;

    /** 納期限(カレンダーコントロール用). */
    private Date calDeadLine;

    /** 和暦表記. */
    private Boolean japaneseCalendar;

    /** 請求者名. */
    private String claimantName;

    /** 登録番号. */
    private String regNo;

    /** 請求書. */
    private Boolean invoice;

    /** 領収書. */
    private Boolean receipt;

    /** 請求書および領収書控え. */
    private Boolean invoiceAndReceiptCopy;

    /** 消費税率(画面表示用). */
    private String salesTaxRate;

    public String getPrintBuildingNoFrom() {
        return printBuildingNoFrom;
    }

    public void setPrintBuildingNoFrom(String printBuildingNoFrom) {
        this.printBuildingNoFrom = printBuildingNoFrom;
    }

    public String getPrintBuildingNoTo() {
        return printBuildingNoTo;
    }

    public void setPrintBuildingNoTo(String printBuildingNoTo) {
        this.printBuildingNoTo = printBuildingNoTo;
    }

    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public Date getCalDateIssue() {
        return calDateIssue;
    }

    public void setCalDateIssue(Date calDateIssue) {
        this.calDateIssue = calDateIssue;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public Date getCalDeadLine() {
        return calDeadLine;
    }

    public void setCalDeadLine(Date calDeadLine) {
        this.calDeadLine = calDeadLine;
    }

    public Boolean getJapaneseCalendar() {
        return japaneseCalendar;
    }

    public void setJapaneseCalendar(Boolean japaneseCalendar) {
        this.japaneseCalendar = japaneseCalendar;
    }

    public String getClaimantName() {
		return claimantName;
	}

	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Boolean getInvoice() {
        return invoice;
    }

    public void setInvoice(Boolean invoice) {
        this.invoice = invoice;
    }

    public Boolean getReceipt() {
        return receipt;
    }

    public void setReceipt(Boolean receipt) {
        this.receipt = receipt;
    }

    public Boolean getInvoiceAndReceiptCopy() {
        return invoiceAndReceiptCopy;
    }

    public void setInvoiceAndReceiptCopy(Boolean invoiceAndReceiptCopy) {
        this.invoiceAndReceiptCopy = invoiceAndReceiptCopy;
    }

    public String getSalesTaxRate() {
        return salesTaxRate;
    }

    public void setSalesTaxRate(String salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

}
