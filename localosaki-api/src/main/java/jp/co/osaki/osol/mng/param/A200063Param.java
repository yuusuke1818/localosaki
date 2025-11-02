package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 受電日報（取得） Paramクラス
 * @author ya-ishida
 *
 */
public class A200063Param extends BaseParam {

    /**
     * 日度
     */
    @NotNull
    @Pattern(regexp="[0-9]{1,2}")
    private String daysAgo;

    /**
     * 計測日時
     */
    private String measureDayTime;

    /**
     * 集計時
     */
    private String aggregateHour;

    /**
     * 時限別リスト
     */
    private List<Map<String, String>> timeValueList;

    public String getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(String daysAgo) {
        this.daysAgo = daysAgo;
    }

    public String getMeasureDayTime() {
        return measureDayTime;
    }

    public void setMeasureDayTime(String measureDayTime) {
        this.measureDayTime = measureDayTime;
    }

    public String getAggregateHour() {
        return aggregateHour;
    }

    public void setAggregateHour(String aggregateHour) {
        this.aggregateHour = aggregateHour;
    }

    public List<Map<String, String>> getTimeValueList() {
        return timeValueList;
    }

    public void setTimeValueList(List<Map<String, String>> timeValueList) {
        this.timeValueList = timeValueList;
    }

}
