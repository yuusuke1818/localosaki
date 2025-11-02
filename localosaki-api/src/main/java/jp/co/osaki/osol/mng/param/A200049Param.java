package jp.co.osaki.osol.mng.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

/**
*
* デマンド(取得) Eα param クラス
*
* @autho t_hayama
*
*/
public class A200049Param extends BaseParam {

    /**
     * 設定変更履歴
     */
    @Pattern(regexp="[0-9]")
    private String settingChangeHist;


    /**
     * 設定変更日時
     */
    private String settingDate;

    /**
     * パルス重み
     */
    private String pulseWeight;

    /**
     * 目標電力
     */
    private String targetPower;

    /**
     * 予約 (時計同期方式)
     */
    private String clockMirrorMethod;

    /**
     * 初期電力
     */
    private String initialPower;

    /**
     * 予約 (高負荷容量)
     */
    private String highLoadCapacity;

    /**
     * 限界電力
     */
    private String limitPower;

    /**
     * 遮断電力
     */
    private String blockPower;

    /**
     * 復帰電力
     */
    private String returnPower;

    /**
     * 警報ロック時間
     */
    private String alertLockTime;

    /**
     * 複数遮断時間
     */
    private String multiBlockTime;

    /**
     * 複数遮断時間リスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<String> _minLoadBlockTimeList;

    /**
     * 遮断方式
     */
    private String blockMethod;

    /**
     * 遮断方法リスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<String> _loadBlockMethodList;

    /**
     * 遮断順位リスト (温度参照ポイントリスト)
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<String> _loadBlockRankList;

    /**
     * 温度参照ポイントリスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<String> _temperatureRefPointList;

    /**
     * 負荷遮断容量リスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    private List<String> _loadBlockCapacityList;

    /**
     * 目標値切替
     */
    private String switchTarget;

    /**
     * 目標電力乗率
     */
    private String targetPowerMultiplier;

    /**
     * 月最大デマンドリスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    public List<String> _maxDemandMonthList;

    /**
     * 月目標電力リスト
     *
     * 機器からのレスポンス一時格納用リスト
     */
    public List<String> _maxTargetPowerMonthList;

    /**
     * 時限リスト
     */
    public List<Map<String, String>> timeList;

    /**
     * 無計量検出時間
     */
    private String noWeighingDetTime;

    /**
     * ファジィ
     */
    private String fuzzy;

    /**
     * 冷房切替
     */
    private String switchCoolingAndHeating;

    /**
     * 冷暖切替参照ポイント
     */
    private String switchCoolingAndHeatingRefPoint;

    /**
     * Result用の負荷情報リスト
     *
     * 一時格納した情報を負荷情報リストにまとめて返却
     */
    public List<Map<String, String>> getLoadInfoList() {
        List<Map<String, String>> loadInfoList = new ArrayList<>();

        // _minLoadBlockTimeList, _loadBlockMethodList, _loadBlockRankList, _loadBlockCapacityList
        // は同じサイズのため、_minLoadBlockTimeList のサイズ分ループさせる
        if (_minLoadBlockTimeList != null) {
            for (int i = 0; i < _minLoadBlockTimeList.size(); i++) {
                HashMap<String, String> loadInfo = new HashMap<>();

                loadInfo.put("minLoadBlockTime", _minLoadBlockTimeList.get(i));
                loadInfo.put("loadBlockMethod", _loadBlockMethodList.get(i));
                loadInfo.put("loadBlockRank", _loadBlockRankList.get(i));
                loadInfo.put("loadBlockCapacity", _loadBlockCapacityList.get(i));
                if (_temperatureRefPointList != null) {
                    loadInfo.put("temperatureRefPoint", _temperatureRefPointList.get(i));
                }

                loadInfoList.add(loadInfo);
            }
        }

        return loadInfoList;
    }

    /**
     * Result用のX月リスト
     *
     * 一時格納した情報をX月リストにまとめて返却
     */
    public List<Map<String, String>> getMonthList(){
        List<Map<String, String>> monthInfoList = new ArrayList<>();

        // _maxDemandMonthList, _maxTargetPowerMonthList
        // は同じサイズのため、_maxDemandMonthList のサイズ分ループさせる
        if (_maxDemandMonthList != null) {
            for (int i = 0; i < _maxDemandMonthList.size(); i++) {
                HashMap<String, String> monthInfo = new HashMap<>();

                monthInfo.put("maxDemandMonth", _maxDemandMonthList.get(i));
                monthInfo.put("targetPowerMonth", _maxTargetPowerMonthList.get(i));

                monthInfoList.add(monthInfo);
            }
        }

        return monthInfoList;
    }


    public String getSettingChangeHist() {
        return settingChangeHist;
    }

    public void setSettingChangeHist(String settingChangeHist) {
        this.settingChangeHist = settingChangeHist;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getPulseWeight() {
        return pulseWeight;
    }

    public void setPulseWeight(String pulseWeight) {
        this.pulseWeight = pulseWeight;
    }

    public String getTargetPower() {
        return targetPower;
    }

    public void setTargetPower(String targetPower) {
        this.targetPower = targetPower;
    }

    public String getClockMirrorMethod() {
        return clockMirrorMethod;
    }

    public void setClockMirrorMethod(String clockMirrorMethod) {
        this.clockMirrorMethod = clockMirrorMethod;
    }

    public String getInitialPower() {
        return initialPower;
    }

    public void setInitialPower(String initialPower) {
        this.initialPower = initialPower;
    }

    public String getHighLoadCapacity() {
        return highLoadCapacity;
    }

    public void setHighLoadCapacity(String highLoadCapacity) {
        this.highLoadCapacity = highLoadCapacity;
    }

    public String getLimitPower() {
        return limitPower;
    }

    public void setLimitPower(String limitPower) {
        this.limitPower = limitPower;
    }

    public String getBlockPower() {
        return blockPower;
    }

    public void setBlockPower(String blockPower) {
        this.blockPower = blockPower;
    }

    public String getReturnPower() {
        return returnPower;
    }

    public void setReturnPower(String returnPower) {
        this.returnPower = returnPower;
    }

    public String getAlertLockTime() {
        return alertLockTime;
    }

    public void setAlertLockTime(String alertLockTime) {
        this.alertLockTime = alertLockTime;
    }

    public String getMultiBlockTime() {
        return multiBlockTime;
    }

    public void setMultiBlockTime(String multiBlockTime) {
        this.multiBlockTime = multiBlockTime;
    }

    public List<String> get_minLoadBlockTimeList() {
        return _minLoadBlockTimeList;
    }

    public void set_minLoadBlockTimeList(List<String> _minLoadBlockTimeList) {
        this._minLoadBlockTimeList = _minLoadBlockTimeList;
    }

    public String getBlockMethod() {
        return blockMethod;
    }

    public void setBlockMethod(String blockMethod) {
        this.blockMethod = blockMethod;
    }

    public List<String> get_loadBlockMethodList() {
        return _loadBlockMethodList;
    }

    public void set_loadBlockMethodList(List<String> _loadBlockMethodList) {
        this._loadBlockMethodList = _loadBlockMethodList;
    }

    public List<String> get_loadBlockRankList() {
        return _loadBlockRankList;
    }

    public void set_loadBlockRankList(List<String> _loadBlockRankList) {
        this._loadBlockRankList = _loadBlockRankList;
    }

    public List<String> get_temperatureRefPointList() {
        return _temperatureRefPointList;
    }

    public void set_temperatureRefPointList(List<String> _temperatureRefPointList) {
        this._temperatureRefPointList = _temperatureRefPointList;
    }

    public List<String> get_loadBlockCapacityList() {
        return _loadBlockCapacityList;
    }

    public void set_loadBlockCapacityList(List<String> _loadBlockCapacityList) {
        this._loadBlockCapacityList = _loadBlockCapacityList;
    }

    public String getSwitchTarget() {
        return switchTarget;
    }

    public void setSwitchTarget(String switchTarget) {
        this.switchTarget = switchTarget;
    }

    public String getTargetPowerMultiplier() {
        return targetPowerMultiplier;
    }

    public void setTargetPowerMultiplier(String targetPowerMultiplier) {
        this.targetPowerMultiplier = targetPowerMultiplier;
    }

    public List<String> get_maxDemandMonthList() {
        return _maxDemandMonthList;
    }

    public void set_maxDemandMonthList(List<String> _maxDemandMonthList) {
        this._maxDemandMonthList = _maxDemandMonthList;
    }

    public List<String> get_maxTargetPowerMonthList() {
        return _maxTargetPowerMonthList;
    }

    public void set_maxTargetPowerMonthList(List<String> _maxTargetPowerMonthList) {
        this._maxTargetPowerMonthList = _maxTargetPowerMonthList;
    }

    public List<Map<String, String>> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Map<String, String>> timeList) {
        this.timeList = timeList;
    }

    public String getNoWeighingDetTime() {
        return noWeighingDetTime;
    }

    public void setNoWeighingDetTime(String noWeighingDetTime) {
        this.noWeighingDetTime = noWeighingDetTime;
    }

    public String getFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(String fuzzy) {
        this.fuzzy = fuzzy;
    }

    public String getSwitchCoolingAndHeating() {
        return switchCoolingAndHeating;
    }

    public void setSwitchCoolingAndHeating(String switchCoolingAndHeating) {
        this.switchCoolingAndHeating = switchCoolingAndHeating;
    }

    public String getSwitchCoolingAndHeatingRefPoint() {
        return switchCoolingAndHeatingRefPoint;
    }

    public void setSwitchCoolingAndHeatingRefPoint(String switchCoolingAndHeatingRefPoint) {
        this.switchCoolingAndHeatingRefPoint = switchCoolingAndHeatingRefPoint;
    }

}
