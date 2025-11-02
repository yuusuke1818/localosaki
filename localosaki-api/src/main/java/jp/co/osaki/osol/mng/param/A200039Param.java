package jp.co.osaki.osol.mng.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import jp.co.osaki.osol.mng.constants.SmControlConstants;

/**
 *
 * 日報(取得) Param クラス
 *
 * @author da_yamano
 *
 */
public class A200039Param extends BaseParam {

    /**
     * コマンド生成パラメータ
     */
    @NotNull
    @Pattern(regexp = "[0-9]{1,2}")
    private String commandParameter;

    /**
     * 計測ポイントリスト
     */
    @Pattern(regexp = "[0-9]{1,2}")
    private String measurePointList;

    public String getCommandParameter() {
        return commandParameter;
    }

    public void setCommandParameter(String commandParameter) {
        this.commandParameter = commandParameter;
    }

    /**
     * DBコマンド用xmlで利用
     * ポイントリストのサイズを指定
     */
    private Integer occursPointNum;

    public Integer getOccursPointNum() {
        return occursPointNum;
    }

    //計測ポイント数をxmlで参照するための処理
    public void setOccursPointNum(Integer occursPointNum) {
        this.occursPointNum = occursPointNum;
        // 計測ポイント数に値をセット
        this.measurePointNum = String.format("%02d", occursPointNum);
    }

    /**
     * HDコマンド用xmlで利用
     * 回数リストのサイズを指定
     */
    private Integer occursCd;

    public Integer getOccursCd() {
        return occursCd;
    }

    //コマンド生成パラメータをxmlで参照するための処理
    public void setOccursCd(Integer occursCd) {
        // コマンドにコマンド生成パラメータを追加
        super.setCommand(String.format("%s%02d", super.getCommand(), occursCd));
        this.occursCd = occursCd;
    }

    /**
     * HDコマンド用 ポイントデータ
     */
    private String pointData;

    // コマンドパラメータで0が指定(過去12回分履歴取得)された際、xmlにてコマンドによるリストサイズ参照ができなくなる為、以下処理にてそれぞれのリストに値を格納する
    public void setPointData(String pointData) {
        if (occursCd == 0) {

            // 一時保存用リストに30分間デマンド値を追加する処理
            String data = pointData.substring(0, SmControlConstants.ALL_HALF_HOUR_DEMAND_DATA_BYTE_INDEX);
            this.timeZeroList = new ArrayList<>();
            for (int i = 0; i < SmControlConstants.TIME_LIST_SIZE; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("demandData", data.substring(SmControlConstants.HALF_HOUR_DEMAND_DATA_BYTE * i,
                        SmControlConstants.HALF_HOUR_DEMAND_DATA_BYTE * (i + 1)));
                timeZeroList.add(map);
            }

            // 一時保存用リストに節電可能電力を追加する処理
            data = pointData.substring(SmControlConstants.ALL_HALF_HOUR_DEMAND_DATA_BYTE_INDEX,
                    SmControlConstants.ALL_SAVING_DEMAND_DATA_BYTE_INDEX);
            this.timeNumList = new ArrayList<>();
            for (int i = 0; i < SmControlConstants.TIME_LIST_SIZE; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("savingDemand", data.substring(SmControlConstants.SAVING_DEMAND_BYTE * i,
                        SmControlConstants.SAVING_DEMAND_BYTE * (i + 1)));
                timeNumList.add(map);
            }
            pointData = pointData.substring(SmControlConstants.DEMAND_DATA_BYTE_INDEX);
        }

        // 一時保存用ポイントリスト生成
        this.pointListHD = new ArrayList<>();

        // 回数リストのサイズ
        int occurs = occursCd;

        if (occurs == 0) {
            // コマンドパラメータで0が指定されたら回数リストのサイズに最大のリストサイズを格納
            occurs = SmControlConstants.TIME_LIST_SIZE;
        }

        // 指定された回数分の全デマンドデータを取得
        Matcher m = java.util.regex.Pattern
                .compile(String.format(".{%s}",
                        SmControlConstants.POINT_NUM_BYTE + SmControlConstants.DEMAND_BYTE * occurs))
                .matcher(pointData);

        // 一時保存用ポイントリストにデマンドデータを格納
        while (m.find()) {

            List<Map<String, String>> list = new ArrayList<>();

            String data = m.group();
            for (int i = 0; i < occurs; i++) {
                Map<String, String> map = new HashMap<>();
                // ポイントNoをセット
                map.put("pointNo", data.substring(0, SmControlConstants.POINT_NUM_BYTE));
                // 取得した全デマンドデータからポイントNoの2バイト分を飛ばしてデマンドデータを4バイト毎に格納
                map.put("demandData",
                        data.substring(SmControlConstants.POINT_NUM_BYTE + SmControlConstants.DEMAND_BYTE * i,
                                SmControlConstants.POINT_NUM_BYTE + SmControlConstants.DEMAND_BYTE * (i + 1)));
                list.add(map);
            }

            Map<String, Object> hdMap = new HashMap<>();
            hdMap.put("timeUpListHD", list);
            this.pointListHD.add(hdMap);
        }

    }

    public String getPointData() {
        return pointData;
    }

    public String getMeasurePointList() {
        return measurePointList;
    }

    public void setMeasurePointList(String measurePointList) {
        this.measurePointList = measurePointList;
    }

    /**
     * カレンダー日報
     */
    private Map<String, Object> calendarDayReport;

    /**
     * 計測日時
     */
    private String measureDayTime;

    /**
     * 集計時
     */
    private String aggregateHour;

    /**
     * デマンド日報
     */
    private Map<String, Object> demandDayReport;

    /**
     * 計測ポイント数
     */
    private String measurePointNum;

    /**
     * 日最大デマンド
     */
    private String dayMaxDemand;

    /**
     * 日最大デマンド発生日時
     */
    private String dayMaxDemandTime;

    /**
     * 月最大デマンド
     */
    private String monthMaxDemand;

    /**
     * 月最大デマンド発生日時
     */
    private String monthMaxDemandTime;

    /**
     * 年最大デマンド
     */
    private String yearMaxDemand;

    /**
     * 年最大デマンド発生日時
     */
    private String yearMaxDemandTime;

    /**
     * 30分計測データ
     */
    private Map<String, Object> demandMeasureData;

    /**
     *************************************
     *コマンドDB000で利用するリスト      *
     *************************************
     */

    /**
     * ポイントリストDB000用
     */
    private List<Map<String, Object>> pointList;

    /**
     * 時限リスト(一時保存用)
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<Map<String, String>> timeUpList;

    /**
     * 時限リストDB用
     */
    private List<Map<String, String>> timeList;

    // ポイントリストに時限リストを追加する処理
    public List<Map<String, Object>> getPointList() {

        //ポイントリストが空ならそのまま値を返却
        if (pointList == null) {
            return pointList;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        //時限リスト(一時保存用)を追加
        map.put("timeList", timeUpList);

        //ポイントリスト0番目に時限リスト(一時保存用)を追加
        pointList.add(0, map);

        return pointList;
    }

    /**
     *************************************
     *コマンドHD000で利用するリスト      *
     *************************************
     */

    /**
     * 回数リスト
     */
    private List<Map<String, Object>> timeNumList;

    /**
     * ポイントリストHD000用
     */
    private List<Map<String, Object>> pointListHD;

    /**
     * 節電可能電力リスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<Map<String, Object>> savingTimeList;

    /**
     * 回数リスト(一時保存用)
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<Map<String, Object>> timeUpListHD;

    /**
     * 回数リスト0
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<Map<String, Object>> timeZeroList;

    //回数リストにポイントリストと回数リスト0を追加する処理
    public List<Map<String, Object>> getTimeNumList() {

        //回数リストが空ならそのまま値を返却
        if (timeNumList == null) {
            return timeNumList;
        }

        int i = 0;
        for (Map<String, Object> map : timeNumList) {

            ArrayList<Map<String, Object>> pointListNew = new ArrayList<>();

            //回数リスト0のi番目を追加
            //ポイント番号("")をセットする
            timeZeroList.get(i).put("pointNo", "");
            pointListNew.add(timeZeroList.get(i));

            for (int j = 0; j < pointListHD.size(); j++) {

                //ポイントリスト内にある回数リスト(一時保存用)のi番目を追加
				@SuppressWarnings("unchecked")
                List<Map<String, Object>> list = (List) pointListHD.get(j).get("timeUpListHD");

                pointListNew.add(list.get(i));
            } //ポイントリスト分繰り返す

            i++;
            map.put("pointList", pointListNew);
        } //回数リスト分繰り返す

        return timeNumList;

    }

    /**
     *************************************
     *コマンドD0000で利用するリスト      *
     *************************************
     */

    /**
     * ポイントリストD0000用
     */
    private List<Map<String, List<Map<String, String>>>> pointListD0;

    public List<Map<String, List<Map<String, String>>>> getPointListD0() {
        return pointListD0;
    }

    public void setPointListD0(List<Map<String, List<Map<String, String>>>> pointList) {
        this.pointListD0 = pointList;
    }

    public List<Map<String, Object>> getSavingTimeList() {
        return savingTimeList;
    }

    public List<Map<String, Object>> getPointListHD() {
        return pointListHD;
    }

    public void setSavingTimeList(List<Map<String, Object>> savingTimeList) {
        this.savingTimeList = savingTimeList;
    }

    public List<Map<String, Object>> getTimeUpListHD() {
        return timeUpListHD;
    }

    public void setTimeUpListHD(List<Map<String, Object>> timeUpListHD) {
        this.timeUpListHD = timeUpListHD;
    }

    public List<Map<String, Object>> getTimeZeroList() {
        return timeZeroList;
    }

    public void setTimeZeroList(List<Map<String, Object>> timeZeroList) {
        this.timeZeroList = timeZeroList;
    }

    public void setPointListHD(List<Map<String, Object>> pointListHD) {
        this.pointListHD = pointListHD;
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

    public void setPointList(List<Map<String, Object>> pointList) {
        this.pointList = pointList;
    }

    public void setTimeList(List<Map<String, String>> timeList) {
        this.timeList = timeList;
    }

    public String getMeasurePointNum() {
        return measurePointNum;
    }

    public void setMeasurePointNum(String measurePointNum) {
        this.measurePointNum = measurePointNum;
    }

    public String getDayMaxDemand() {
        return dayMaxDemand;
    }

    public void setDayMaxDemand(String dayMaxDemand) {
        this.dayMaxDemand = dayMaxDemand;
    }

    public String getDayMaxDemandTime() {
        return dayMaxDemandTime;
    }

    public void setDayMaxDemandTime(String dayMaxDemandTime) {
        this.dayMaxDemandTime = dayMaxDemandTime;
    }

    public String getMonthMaxDemand() {
        return monthMaxDemand;
    }

    public void setMonthMaxDemand(String monthMaxDemand) {
        this.monthMaxDemand = monthMaxDemand;
    }

    public String getMonthMaxDemandTime() {
        return monthMaxDemandTime;
    }

    public void setMonthMaxDemandTime(String monthMaxDemandTime) {
        this.monthMaxDemandTime = monthMaxDemandTime;
    }

    public String getYearMaxDemand() {
        return yearMaxDemand;
    }

    public void setYearMaxDemand(String yearMaxDemand) {
        this.yearMaxDemand = yearMaxDemand;
    }

    public String getYearMaxDemandTime() {
        return yearMaxDemandTime;
    }

    public void setYearMaxDemandTime(String yearMaxDemandTime) {
        this.yearMaxDemandTime = yearMaxDemandTime;
    }

    public Map<String, Object> getCalendarDayReport() {
        return calendarDayReport;
    }

    public void setCalendarDayReport(Map<String, Object> calendarDayReport) {
        this.calendarDayReport = calendarDayReport;
    }

    public Map<String, Object> getDemandDayReport() {
        return demandDayReport;
    }

    public void setDemandDayReport(Map<String, Object> demandDayReport) {
        this.demandDayReport = demandDayReport;
    }

    public Map<String, Object> getDemandMeasureData() {
        return demandMeasureData;
    }

    public void setDemandMeasureData(Map<String, Object> demandMeasureData) {
        this.demandMeasureData = demandMeasureData;
    }

    public void setTimeNumList(List<Map<String, Object>> timeNumList) {
        this.timeNumList = timeNumList;
    }

    public List<Map<String, String>> getTimeUpList() {
        return timeUpList;
    }

    public void setTimeUpList(List<Map<String, String>> timeUpList) {
        this.timeUpList = timeUpList;
    }

    public List<Map<String, String>> getTimeList() {
        return timeList;
    }

}