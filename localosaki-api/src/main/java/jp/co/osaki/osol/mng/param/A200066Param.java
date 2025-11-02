package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 30分フィーダ計測データ（取得）Paramクラス
 * @author ya-ishida
 *
 */
public class A200066Param extends BaseParam {

    /**
     * Eα、Eα2のポイント数
     */
    public static final int POINT_COUNT_EA = 16;

    /**
     * ポイントグループ
     */
    @NotNull
    @Pattern(regexp="[0-9A-F]{1}")
    private String pointGroup;

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
     * ポイントグループリスト
     */
    private List<Map<String, Object>> pointGroupList;

    /**
     * ポイントリスト
     */
    private List<Map<String, Object>> pointList;

    /**
     * ポイントリスト（データ取得時、すべてのポイントの時限別リストを投入した状態）
     */
    private List<Map<String, String>> pointDataTempList;

    public List<Map<String, Object>> getPointGroupList() {
        return pointGroupList;
    }

    public void setPointGroupList(List<Map<String, Object>> pointGroupList) {
        this.pointGroupList = pointGroupList;
    }

    public String getPointGroup() {
        return pointGroup;
    }

    public void setPointGroup(String pointGroup) {
        this.pointGroup = pointGroup;
    }

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

    public List<Map<String, Object>> getPointList() {
        return pointList;
    }

    public void setPointList(List<Map<String, Object>> pointList) {
        this.pointList = pointList;
    }

    public List<Map<String, String>> getPointDataTempList() {
        return pointDataTempList;
    }

    public void setPointDataTempList(List<Map<String, String>> pointDataTempList) {
        this.pointDataTempList = pointDataTempList;
    }

    public Integer getHistoryCountNum() {
        return historyCountNum;
    }

    public void setHistoryCountNum(Integer historyCount) {
        this.historyCountNum = historyCount;
    }

}
