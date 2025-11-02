package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;

/**
*
* イベント制御設定(取得) Param クラス
*
* @author t_sakamoto
*
*/
public class A200020Param extends BaseParam {

    /**
     * 設定変更履歴
     */
    @Pattern(regexp = "[0-9]")
    private String settingChangeHist;

    /**
     * 建物ID
     */
    private Long buildingId;

    /**
     * 取得日時(設定変更日時)(週含む)
     */
    private String settingDate;

    /**
     * 負荷リスト
     */
    private List<Map<String, Object>> loadList;

    /**
     * イベント制御設定リスト
     */
    private List<Map<String, String>> settingEventCtrlList;

    /**
     * 現在温度情報
     */
    private List<CurrentTempretureExtractResultData> currentTemperatureList;

    /**
     * 連動設定情報
     */
    private List<LinkSettingExtractResultData> linkSettingList;

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public List<Map<String, Object>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, Object>> loadList) {
        this.loadList = loadList;
    }

    public List<Map<String, String>> getSettingEventCtrlList() {
        return settingEventCtrlList;
    }

    public void setSettingEventCtrlList(List<Map<String, String>> settingEventCtrlList) {
        this.settingEventCtrlList = settingEventCtrlList;
    }

    public String getSettingChangeHist() {
        return settingChangeHist;
    }

    public void setSettingChangeHist(String settingChangeHist) {
        this.settingChangeHist = settingChangeHist;
    }

    public List<CurrentTempretureExtractResultData> getCurrentTemperatureList() {
        return currentTemperatureList;
    }

    public void setCurrentTemperatureList(List<CurrentTempretureExtractResultData> currentTemperatureList) {
        this.currentTemperatureList = currentTemperatureList;
    }

    public List<LinkSettingExtractResultData> getLinkSettingList() {
        return linkSettingList;
    }

    public void setLinkSettingList(List<LinkSettingExtractResultData> linkSettingList) {
        this.linkSettingList = linkSettingList;
    }

}
