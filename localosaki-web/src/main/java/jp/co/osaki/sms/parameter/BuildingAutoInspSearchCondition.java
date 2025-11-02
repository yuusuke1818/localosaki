package jp.co.osaki.sms.parameter;

import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物に紐付く自動検針日 検索条件.
 *
 * @author ozaki.y
 */
public class BuildingAutoInspSearchCondition {

    /**
     * コンストラクタ.
     *
     * @param autoInspMonth 検針実行月
     * @param autoInspDay 自動検針日
     */
    public BuildingAutoInspSearchCondition(String autoInspMonth, String autoInspDay) {
        if (!CheckUtility.isNullOrEmpty(autoInspMonth)) {
            setAutoInspMonth(Integer.parseInt(autoInspMonth));
        }

        setAutoInspDay(autoInspDay);
    }

    /** 検針実行月. */
    private Integer autoInspMonth;

    /** 自動検針日. */
    private String autoInspDay;

    public Integer getAutoInspMonth() {
        return autoInspMonth;
    }

    public void setAutoInspMonth(Integer autoInspMonth) {
        this.autoInspMonth = autoInspMonth;
    }

    public String getAutoInspDay() {
        return autoInspDay;
    }

    public void setAutoInspDay(String autoInspDay) {
        this.autoInspDay = autoInspDay;
    }
}
