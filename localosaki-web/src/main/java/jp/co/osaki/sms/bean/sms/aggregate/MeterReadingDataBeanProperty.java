/**
 *
 */
package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.sms.bean.building.info.Condition;
import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 検針データダウンロードの検索条件保持クラス
 */
@Named(value = "smsAggregateMeterReadingDataBeanProperty")
@Dependent
public class MeterReadingDataBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    private static final long serialVersionUID = 5098939796642213799L;

    // 検索実行済みフラグ
    private boolean searchedFlg;
    // 一覧表示css制御用
    private String columnClassesStr;
    // 企業表示フラグ
    private boolean viewCorpIdAndName;


    /** 検索結果ボタン活性 */
    private Boolean searchResultDisabled;

    /** 検索結果ボタンスタイル */
    private String searchResultStyle;

    /** 予約状況表示フラグ */
    private Boolean reservationStatusFlg;

    /** 予約状況ボタン活性 */
    private Boolean reservationStatusDisabled;

    /** 予約状況ボタンスタイル */
    private String reservationStatusStyle;


    public boolean isSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

    public String getColumnClassesStr() {
        return columnClassesStr;
    }

    public void setColumnClassesStr(String columnClassesStr) {
        this.columnClassesStr = columnClassesStr;
    }

	public boolean isViewCorpIdAndName() {
		return viewCorpIdAndName;
	}

	public void setViewCorpIdAndName(boolean viewCorpIdAndName) {
		this.viewCorpIdAndName = viewCorpIdAndName;
	}

	public Boolean getSearchResultDisabled() {
		return searchResultDisabled;
	}

	public void setSearchResultDisabled(Boolean searchResultDisabled) {
		this.searchResultDisabled = searchResultDisabled;
	}

	public String getSearchResultStyle() {
		return searchResultStyle;
	}

	public void setSearchResultStyle(String searchResultStyle) {
		this.searchResultStyle = searchResultStyle;
	}

	public Boolean getReservationStatusFlg() {
		return reservationStatusFlg;
	}

	public void setReservationStatusFlg(Boolean reservationStatusFlg) {
		this.reservationStatusFlg = reservationStatusFlg;
	}

	public Boolean getReservationStatusDisabled() {
		return reservationStatusDisabled;
	}

	public void setReservationStatusDisabled(Boolean reservationStatusDisabled) {
		this.reservationStatusDisabled = reservationStatusDisabled;
	}

	public String getReservationStatusStyle() {
		return reservationStatusStyle;
	}

	public void setReservationStatusStyle(String reservationStatusStyle) {
		this.reservationStatusStyle = reservationStatusStyle;
	}
}
