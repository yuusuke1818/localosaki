package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 建物装置設定 建物検索画面用プロパティ
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingBuildingDeviceSearchBeanProperty")
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
     * 都道府県用セレクトボックス
     */
    private Map<String, String> prefectureMap;

    /**
     * 入居形態用セレクトボックス
     */
    private Map<String, String> nyukyoTypeMap;

    /**
     * 建物状況用セレクトボックス
     */
    private Map<String, String> buildingStatusMap;

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

    public Map<String, String> getPrefectureMap() {
        return prefectureMap;
    }

    public void setPrefectureMap(Map<String, String> prefectureMap) {
        this.prefectureMap = prefectureMap;
    }

    public Map<String, String> getNyukyoTypeMap() {
        return nyukyoTypeMap;
    }

    public void setNyukyoTypeMap(Map<String, String> nyukyoTypeMap) {
        this.nyukyoTypeMap = nyukyoTypeMap;
    }

    public Map<String, String> getBuildingStatusMap() {
        return buildingStatusMap;
    }

    public void setBuildingStatusMap(Map<String, String> buildingStatusMap) {
        this.buildingStatusMap = buildingStatusMap;
    }

}
