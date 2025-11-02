package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

/**
 * 需要電力予測データ（取得） Paramクラス
 * @author ya-ishida
 *
 */
public class A210009Param extends BaseParam {

    /**
     *  取得日時
     */
    private String selectDate;

    /**
     * 予測データ（30分値）リスト
     */
    private List<Map<String, String>> forecastTimeList;

    /**
     * 予測データ（1日値）リスト
     */
    private List<Map<String, String>> forecastDayList;

    /**
     * @return selectDate
     */
    public String getSelectDate() {
        return selectDate;
    }

    /**
     * @param selectDate セットする selectDate
     */
    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

    /**
     * @return forecastTimeList
     */
    public List<Map<String, String>> getForecastTimeList() {
        return forecastTimeList;
    }

    /**
     * @param forecastTimeList セットする forecastTimeList
     */
    public void setForecastTimeList(List<Map<String, String>> forecastTimeList) {
        this.forecastTimeList = forecastTimeList;
    }

    /**
     * @return forecastDayList
     */
    public List<Map<String, String>> getForecastDayList() {
        return forecastDayList;
    }

    /**
     * @param forecastDayList セットする forecastDayList
     */
    public void setForecastDayList(List<Map<String, String>> forecastDayList) {
        this.forecastDayList = forecastDayList;
    }

}
