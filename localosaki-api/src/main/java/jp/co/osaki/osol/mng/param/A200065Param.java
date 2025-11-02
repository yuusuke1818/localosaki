package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 受電30分計測データ（取得）Paramクラス
 * @author ya-ishida
 *
 */
public class A200065Param extends BaseParam {

    /**
     * 取得回数指定
     */
    @NotNull
    @Pattern(regexp="[0-9]{1,2}")
    private String historyCount;

    /**
     * 取得回数（数値）
     */
    private Integer historyCountNum;

    /**
     * 計測日時
     */
    private String measureDayTime;

    /**
     * 時限別リスト
     */
    private List<Map<String, String>> timeValueList;

    public String getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(String historyCount) {
        this.historyCount = historyCount;
    }

    public String getMeasureDayTime() {
        return measureDayTime;
    }

    public void setMeasureDayTime(String measureDayTime) {
        this.measureDayTime = measureDayTime;
    }

    public List<Map<String, String>> getTimeValueList() {
        return timeValueList;
    }

    public void setTimeValueList(List<Map<String, String>> timeValueList) {
        this.timeValueList = timeValueList;
    }

    public Integer getHistoryCountNum() {
        return historyCountNum;
    }

    public void setHistoryCountNum(Integer historyCount) {
        this.historyCountNum = historyCount;
    }
}
