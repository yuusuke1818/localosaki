package jp.co.osaki.sms.bean.sms.server.setting.handy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * ハンディ端末 検索画面用プロパティ
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingHandySearchBeanProperty")
@Dependent
public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = -5994080073835124174L;

    /**
     * 動的検索条件リスト
     */
    private List<Condition> conditionList;

    /**
     * 検索実行フラグ
     */
    private Boolean searchedFlg;

    /**
     * ハンディ端末ID セレクトボックス
     */
    private Map<String, String> handyDeviceIdMap;

    /**
     * ハンディ端末状態 セレクトボックス
     */
    private Map<String, String> handyDeviceStatusMap;

    public SearchBeanProperty() {
    }

    public Boolean getSearchedFlg() {
        return searchedFlg;
    }

    public void setSearchedFlg(Boolean searchedFlg) {
        this.searchedFlg = searchedFlg;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public Map<String, String> getHandyDeviceIdMap() {
        return handyDeviceIdMap;
    }

    public void setHandyDeviceIdMap(Map<String, String> handyDeviceIdMap) {
        this.handyDeviceIdMap = handyDeviceIdMap;
    }

    public Map<String, String> getHandyDeviceStatusMap() {
        return handyDeviceStatusMap;
    }

    public void setHandyDeviceStatusMap(Map<String, String> handyDeviceStatusMap) {
        this.handyDeviceStatusMap = handyDeviceStatusMap;
    }
}
