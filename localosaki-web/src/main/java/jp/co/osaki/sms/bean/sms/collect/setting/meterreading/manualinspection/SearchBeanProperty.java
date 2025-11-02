package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 任意検針 検索画面用プロパティ
 *
 * @author yonezawa.a
 */
@Named(value = "smsCollectSettingMeterreadingManualInspectionSearchBeanProperty")
@Dependent
public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 5042885114997145587L;

    /**
     * 動的検索条件リスト
     */
    private List<Condition> conditionList;

    /**
     * 検索実行フラグ
     */
    private Boolean searchedFlg;

    /**
     * 検索件数
     */
    private int resultCount;

    /**
     * 種別 セレクトボックス
     */
    private Map<String, String> meterTypeNameMap;

    /**
     * 予約検針日 セレクトボックス.
     */
    private Map<String, String> reserveInspHMap;

    /**
     * 予約検針時 セレクトボックス.
     */
    private Map<String, String> reserveInspMMap;

    /**
     * ユーザーコード セレクトボックス
     */
    private Map<String, String> userCdMap;

    public SearchBeanProperty() {
    }

    public Boolean getSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(Boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public Map<String, String> getMeterTypeNameMap() {
        return meterTypeNameMap;
    }

    public void setMeterTypeNameMap(Map<String, String> meterTypeNameMap) {
        this.meterTypeNameMap = meterTypeNameMap;
    }

    public Map<String, String> getUserCdMap() {
        return userCdMap;
    }

    public void setUserCdMap(Map<String, String> userCdMap) {
        this.userCdMap = userCdMap;
    }

    public Map<String, String> getReserveInspHMap() {
        return reserveInspHMap;
    }

    public void setReserveInspHMap(Map<String, String> reserveInspHMap) {
        this.reserveInspHMap = reserveInspHMap;
    }

    public Map<String, String> getReserveInspMMap() {
        return reserveInspMMap;
    }

    public void setReserveInspMMap(Map<String, String> reserveInspMMap) {
        this.reserveInspMMap = reserveInspMMap;
    }

}
