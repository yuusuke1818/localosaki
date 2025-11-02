package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;

/**
*
* AielMasterエリア設定(取得) Param クラス
*
* @author s_sunada
*
*/
public class A210005Param extends BaseParam {

    /**
     * 日付
     */
    private String dateTime;

    /**
     * エリアリスト
     */
    private List<Map<String, Object>> areaList;

    /**
     * 設定
     */
    private String areaConfig;

    /**
     * 空調容量
     */
    private String airControlCapacity;

    /**
     * 制御対象高
     */
    private String controlTargetHigh;

    /**
     * 制御対象中
     */
    private String controlTargetMiddle;

    /**
     * 制御対象低
     */
    private String controlTargetLow;

    /**
     * センサリスト
     */
    private List<Map<String, String>> sensorList;


    /**
     * エリア名称リスト
     */
    private List<AreaNameExtractResultData> areaNameList;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Map<String, Object>> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Map<String, Object>> areaList) {
        this.areaList = areaList;
    }

    public String getAreaConfig() {
        return areaConfig;
    }

    public void setAreaConfig(String areaConfig) {
        this.areaConfig = areaConfig;
    }

    public String getAirControlCapacity() {
        return airControlCapacity;
    }

    public void setAirControlCapacity(String airControlCapacity) {
        this.airControlCapacity = airControlCapacity;
    }

    public String getControlTargetHigh() {
        return controlTargetHigh;
    }

    public void setControlTargetHigh(String controlTargetHigh) {
        this.controlTargetHigh = controlTargetHigh;
    }

    public String getControlTargetMiddle() {
        return controlTargetMiddle;
    }

    public void setControlTargetMiddle(String controlTargetMiddle) {
        this.controlTargetMiddle = controlTargetMiddle;
    }

    public String getControlTargetLow() {
        return controlTargetLow;
    }

    public void setControlTargetLow(String controlTargetLow) {
        this.controlTargetLow = controlTargetLow;
    }

    public List<Map<String, String>> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<Map<String, String>> sensorList) {
        this.sensorList = sensorList;
    }

    public List<AreaNameExtractResultData> getAreaNameList() {
        return areaNameList;
    }

    public void setAreaNameList(List<AreaNameExtractResultData> areaNameList) {
        this.areaNameList = areaNameList;
    }

}
