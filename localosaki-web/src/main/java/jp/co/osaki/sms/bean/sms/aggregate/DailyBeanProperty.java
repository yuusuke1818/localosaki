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
 *
 */
@Named(value = "smsAggregateDailyBeanProperty")
@Dependent
public class DailyBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5098939796642213703L;

    // 検索実行済みフラグ
    private boolean searchedFlg;
    // 一覧表示css制御用
    private String columnClassesStr;
    // 企業ID
    private String corpId;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

}
