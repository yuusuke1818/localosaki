package jp.co.osaki.sms.bean.sms.server.setting.collect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 装置 検索画面用プロパティ
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingCollectSearchBeanProperty")
@Dependent
public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = -2477291762324567491L;

    /**
     * 動的検索条件リスト
     */
    private List<Condition> conditionList;

    /**
     * 検索実行フラグ
     */
    private Boolean searchedFlg;

    /**
     * 装置ID セレクトボックス
     */
    private Map<String, String> collectDeviceIdMap;

    /**
     * 装置状態 セレクトボックス
     */
    private Map<String, String> collectDeviceStatusMap;

    public SearchBeanProperty() {
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public Boolean getSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(Boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

    public Map<String, String> getCollectDeviceIdMap() {
        return collectDeviceIdMap;
    }

    public void setCollectDeviceIdMap(Map<String, String> collectDeviceIdMap) {
        this.collectDeviceIdMap = collectDeviceIdMap;
    }

    public Map<String, String> getCollectDeviceStatusMap() {
        return collectDeviceStatusMap;
    }

    public void setCollectDeviceStatusMap(Map<String, String> collectDeviceStatusMap) {
        this.collectDeviceStatusMap = collectDeviceStatusMap;
    }

}
