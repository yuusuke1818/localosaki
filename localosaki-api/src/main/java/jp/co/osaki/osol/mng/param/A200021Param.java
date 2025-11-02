package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;

/**
*
* イベント制御設定(設定) Param クラス
*
* @author t_sakamoto
*
*/
public class A200021Param extends BaseParam {

    /**
     * 取得日時(設定変更日時)(週含む)
     */
    private String settingDate;

    /**
     * 負荷??
     */
    private List<Map<String, Object>> loadList;

    /**
     * 連動設定情報
     */
    private List<LinkSettingExtractResultData> linkSettingList;

    @AssertTrue
    public boolean isValidateLoadList() {
        return loadList != null && validateList2(loadList, new HashMap<String, String>() {
            {
                put("eventCtrlFlg", "[0-9]"); // イベント制御フラグ
                put("settingEventCtrlList", ""); // イベント??制御設定
                put("blockHoldTime", "[0-9]{1,2}"); // 遮断保持時間
                put("returnHoldTime", "[0-9]{1,2}"); // 復帰保持時間
            }
        });
    }

    private boolean validateList2(List<Map<String, Object>> list, HashMap<String, String> validationPattern) {
        for (Map<String, Object> obj : list) {
            for (Entry<String, String> ent : validationPattern.entrySet()) {

                Object value = obj.get(ent.getKey());
                if (value == null) {
                    return false;
                }

                if (value instanceof String) {
                    if (!((String) value).matches(ent.getValue())) {
                        return false;
                    }
                } else if (value instanceof List) {
                    // リスト内のリストをバリデーションチェック
					@SuppressWarnings("unchecked")
					List<Map<String,String>> valueList = (List<Map<String,String>>)value;
					if(!this.validateList(valueList, new HashMap<String, String>(){{
                            put("measuredValue", "[0-9]{1,3}"); // 計測値
                            put("ctrlThreshold", "[\\s\\S0-9]{1,6}"); // 制御閾値
                            put("ctrlTerms", "[0-9]"); // 制御条件
                            put("eventTerms", "[0-9]"); // イベント条件
                            put("returnWidth", "[0-9]{1,5}"); // 復帰幅
                        }
                    })) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public List<Map<String, Object>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, Object>> loadList) {
        this.loadList = loadList;
    }

    public List<LinkSettingExtractResultData> getLinkSettingList() {
        return linkSettingList;
    }

    public void setLinkSettingList(List<LinkSettingExtractResultData> linkSettingList) {
        this.linkSettingList = linkSettingList;
    }

}
