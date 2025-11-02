package jp.co.osaki.sms.bean.sms.server.setting.ocr;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * AieLink 検索画面用プロパティ
 * 「OCR検針」→「AieLink」へ変更
 *
 * @author iwasaki_y
 */
@Named(value = "smsServerSettingOcrSearchBeanProperty")
@Dependent
public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 8781114603700084551L;

    /**
     * 動的検索条件リスト
     */
    private List<Condition> conditionList;

    /**
     * 検索実行フラグ
     */
    private Boolean searchedFlg;

    /**
     * AieLinkID セレクトボックス
     * 「OCR検針」→「AieLink」へ変更
     */
    private Map<String, String> ocrDeviceIdMap;

    /**
     * AieLink状態 セレクトボックス
     * 「OCR検針」→「AieLink」へ変更
     */
    private Map<String, String> ocrDeviceStatusMap;

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

    public Map<String, String> getOcrDeviceIdMap() {
        return ocrDeviceIdMap;
    }

    public void setOcrDeviceIdMap(Map<String, String> ocrDeviceIdMap) {
        this.ocrDeviceIdMap = ocrDeviceIdMap;
    }

    public Map<String, String> getOcrDeviceStatusMap() {
        return ocrDeviceStatusMap;
    }

    public void setOcrDeviceStatusMap(Map<String, String> ocrDeviceStatusMap) {
        this.ocrDeviceStatusMap = ocrDeviceStatusMap;
    }
}
