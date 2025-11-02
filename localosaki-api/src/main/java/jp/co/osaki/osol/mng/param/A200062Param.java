package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * スケジュールパターン(設定) Param クラス
 *
 * @autho t_hayama
 *
 */
public class A200062Param extends BaseParam {

    /**
     * スケジュール設定パターンリスト
     */
    @NotNull
    @Size(max=16, min=16)
    private List<Map<String, Object>> settingSchedulePatternList;


    @AssertTrue
    public boolean isValidateSettingSchedulePatternList() {
        return settingSchedulePatternList != null && validateTimeZoneList(settingSchedulePatternList, new HashMap<String, String>() {
            {
                put("timeZoneList", "");    // 時間帯リスト
            }
        });
    }

    private boolean validateTimeZoneList(List<Map<String, Object>> list, HashMap<String, String> validationPattern) {
        for (Map<String, Object> obj : list) {
            for (Entry<String, String> ent : validationPattern.entrySet()) {

                Object value = obj.get(ent.getKey());
                if (value == null) {
                    return false;
                }

                if (value instanceof String) {
                    if (!((String)value).matches(ent.getValue())) {
                        return false;
                    }
                } else if (value instanceof List) {
                    // 入れ子リストバリデーション
                    @SuppressWarnings("unchecked")
                    List<Map<String, String>> valueList = (List<Map<String, String>>)value;
                    if (!this.validateList(valueList, new HashMap<String, String>() {{
                        put("startHour","[0-9]{1,2}");      // 開始時間(時)
                        put("startMinute","[0-9]{1,2}");    // 開始時間(分)
                        put("endHour","[0-9]{1,2}");        // 終了時間(時)
                        put("endMinute","[0-9]{1,2}");      // 終了時間(分)
                        put("onMinute","[0-9]{1,2}");       // ON時間(分)
                        put("offMinute","[0-9]{1,2}");      // OFF時間(分)
                    }})) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public List<Map<String, Object>> getSettingSchedulePatternList() {
        return settingSchedulePatternList;
    }

    public void setSettingSchedulePatternList(List<Map<String, Object>> settingSchedulePatternList) {
        this.settingSchedulePatternList = settingSchedulePatternList;
    }

    @Override
    public boolean partDataComparison(Object oldData) {
        // 一括機器制御系：比較処理
        // スケジュール管理指定比較
        return true;
    }

}
