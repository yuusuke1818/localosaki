package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * フィーダ日報（取得）Paramクラス
 * @author ya-ishida
 *
 */
public class A200064Param extends BaseParam {

    /**
     * ポイントグループ
     */
    @NotNull
    @Pattern(regexp="[0-9A-F]{1}")
    private String pointGroup;

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
     * ポイントグループリスト
     */
    private List<Map<String, Object>> pointGroupList;

    /**
     * ポイントリスト
     */
    private List<Map<String, Object>> pointList;

    /**
     * ポイントリスト（データ取得時）
     */
    private List<Map<String,List<Map<String,String>>>> pointDataList;

    /**
     * 時限別リスト
     */
    private List<Map<String, String>> timeValueList;

    public String getPointGroup() {
        return pointGroup;
    }

    public void setPointGroup(String pointGroup) {
        this.pointGroup = pointGroup;
    }

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

    public List<Map<String, Object>> getPointGroupList() {
        return pointGroupList;
    }

    public void setPointGroupList(List<Map<String, Object>> pointGroupList) {
        this.pointGroupList = pointGroupList;
    }

    public List<Map<String, Object>> getPointList() {
        return pointList;
    }

    public void setPointList(List<Map<String, Object>> pointList) {
        this.pointList = pointList;
    }

    public List<Map<String, List<Map<String, String>>>> getPointDataList() {
        return pointDataList;
    }

    public void setPointDataList(List<Map<String, List<Map<String, String>>>> pointDataList) {
        this.pointDataList = pointDataList;
    }

    public List<Map<String, String>> getTimeValueList() {
        return timeValueList;
    }

    public void setTimeValueList(List<Map<String, String>> timeValueList) {
        this.timeValueList = timeValueList;
    }

}
