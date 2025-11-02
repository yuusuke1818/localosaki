package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "meterReadingDataProperty")
@Dependent
public class MeterReadingDataBeanProperty implements Serializable {

    private static final long serialVersionUID = -1160815226196692430L;

    /** タイトル. */
    private String title;

    /** 内容. */
    private String content;

    /** 接続先. */
    private String devId;

    /** 接続先プルダウンMap. */
    private Map<String, String> devIdMap;

    /** 月検針連番. */
    private String monthMeterReadingNo;

    /** 月検針連番プルダウンリスト. */
    private List<String> monthMeterReadingNoList;

    /** 表示年. */
    private String year;

    /** 表示年プルダウンリスト. */
    private List<String> yearList;

    /** 表示月. */
    private String month;

    /** 表示月プルダウンリスト. */
    private List<String> monthList;

    /** 検針データ表示フラグ  true:検針データ  false:請求金額データ. */
    private Boolean meterReadingDataFlg;

    /** 検針データボタン活性  true:検針データ  false:請求金額データ. */
    private Boolean meterReadingDataDisabled;

    /** 請求金額データ表示フラグ  true:請求金額データ  false:検針データ. */
    private Boolean billingAmountDataFlg;

    /** 請求金額データボタン活性  true:請求金額データ  false:検針データ. */
    private Boolean billingAmountDataDisabled;

    /** 表示更新ボタン活性. */
    private Boolean btnReloadDisabled;

    /** 検針データボタンスタイル */
    private String meterReadingDataStyle;

    /** 請求金額ボタンスタイル */
    private String billingAmountDataStyle;

    /** 月検針連番（条件保持用） */
    private String searchMonthMeterReadingNo;

    /** 表示年（条件保持用） */
    private String searchYear;

    /** 表示付き（条件保持用） */
    private String searchMonth;

    /** 検索実行フラグ */
    private Boolean searchFlg;

    /** 表示更新タイトル **/
    private String updateDisplayTitle;

    /** 表示更新メッセージ **/
    private String updateDisplayMessage;

    /** 表示年(範囲検索用). */
    private String fromYear;

    /** 表示月(範囲検索用). */
    private String fromMonth;

    /** 表示年(範囲検索用). */
    private String toYear;

    /** 表示月(範囲検索用). */
    private String toMonth;

    /** 表示年(範囲検索用)（条件保持用）. */
    private String searchFromYear;

    /** 表示月(範囲検索用)（条件保持用）. */
    private String searchFromMonth;

    /** 表示年(範囲検索用)（条件保持用）. */
    private String searchToYear;

    /** 表示月(範囲検索用)（条件保持用）. */
    private String searchToMonth;

    /** 按分料金チェックボックス. */
    private boolean proratedChargeFlg;

    /** 固定費チェックボックス. */
    private boolean fixedCostFlg;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public void setDevIdMap(Map<String, String> devIdMap) {
        this.devIdMap = devIdMap;
    }

    public String getMonthMeterReadingNo() {
        return monthMeterReadingNo;
    }

    public void setMonthMeterReadingNo(String monthMeterReadingNo) {
        this.monthMeterReadingNo = monthMeterReadingNo;
    }

    public List<String> getMonthMeterReadingNoList() {
        return monthMeterReadingNoList;
    }

    public void setMonthMeterReadingNoList(List<String> monthMeterReadingNoList) {
        this.monthMeterReadingNoList = monthMeterReadingNoList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<String> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    public Boolean getMeterReadingDataFlg() {
        return meterReadingDataFlg;
    }

    public void setMeterReadingDataFlg(Boolean meterReadingDataFlg) {
        this.meterReadingDataFlg = meterReadingDataFlg;
    }

    public Boolean getMeterReadingDataDisabled() {
        return meterReadingDataDisabled;
    }

    public void setMeterReadingDataDisabled(Boolean meterReadingDataDisabled) {
        this.meterReadingDataDisabled = meterReadingDataDisabled;
    }

    public Boolean getBillingAmountDataFlg() {
        return billingAmountDataFlg;
    }

    public void setBillingAmountDataFlg(Boolean billingAmountDataFlg) {
        this.billingAmountDataFlg = billingAmountDataFlg;
    }

    public Boolean getBillingAmountDataDisabled() {
        return billingAmountDataDisabled;
    }

    public void setBillingAmountDataDisabled(Boolean billingAmountDataDisabled) {
        this.billingAmountDataDisabled = billingAmountDataDisabled;
    }

    public Boolean getBtnReloadDisabled() {
        return btnReloadDisabled;
    }

    public void setBtnReloadDisabled(Boolean btnReloadDisabled) {
        this.btnReloadDisabled = btnReloadDisabled;
    }

    public String getMeterReadingDataStyle() {
        return meterReadingDataStyle;
    }

    public void setMeterReadingDataStyle(String meterReadingDataStyle) {
        this.meterReadingDataStyle = meterReadingDataStyle;
    }

    public String getBillingAmountDataStyle() {
        return billingAmountDataStyle;
    }

    public void setBillingAmountDataStyle(String billingAmountDataStyle) {
        this.billingAmountDataStyle = billingAmountDataStyle;
    }

    public String getSearchMonthMeterReadingNo() {
        return searchMonthMeterReadingNo;
    }

    public void setSearchMonthMeterReadingNo(String searchMonthMeterReadingNo) {
        this.searchMonthMeterReadingNo = searchMonthMeterReadingNo;
    }

    public String getSearchYear() {
        return searchYear;
    }

    public void setSearchYear(String searchYear) {
        this.searchYear = searchYear;
    }

    public String getSearchMonth() {
        return searchMonth;
    }

    public void setSearchMonth(String searchMonth) {
        this.searchMonth = searchMonth;
    }

    public Boolean getSearchFlg() {
        return searchFlg;
    }

    public void setSearchFlg(Boolean searchFlg) {
        this.searchFlg = searchFlg;
    }

    public String getUpdateDisplayTitle() {
        return updateDisplayTitle;
    }

    public void setUpdateDisplayTitle(String updateDisplayTitle) {
        this.updateDisplayTitle = updateDisplayTitle;
    }

    public String getUpdateDisplayMessage() {
        return updateDisplayMessage;
    }

    public void setUpdateDisplayMessage(String updateDisplayMessage) {
        this.updateDisplayMessage = updateDisplayMessage;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    public String getSearchFromYear() {
        return searchFromYear;
    }

    public void setSearchFromYear(String searchFromYear) {
        this.searchFromYear = searchFromYear;
    }

    public String getSearchFromMonth() {
        return searchFromMonth;
    }

    public void setSearchFromMonth(String searchFromMonth) {
        this.searchFromMonth = searchFromMonth;
    }

    public String getSearchToYear() {
        return searchToYear;
    }

    public void setSearchToYear(String searchToYear) {
        this.searchToYear = searchToYear;
    }

    public String getSearchToMonth() {
        return searchToMonth;
    }

    public void setSearchToMonth(String searchToMonth) {
        this.searchToMonth = searchToMonth;
    }

    public boolean isProratedChargeFlg() {
        return proratedChargeFlg;
    }

    public void setProratedChargeFlg(boolean proratedChargeFlg) {
        this.proratedChargeFlg = proratedChargeFlg;
    }

    public boolean isFixedCostFlg() {
        return fixedCostFlg;
    }

    public void setFixedCostFlg(boolean fixedCostFlg) {
        this.fixedCostFlg = fixedCostFlg;
    }

}
