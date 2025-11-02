package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;

/**
*
* 温度制御(取得) Param クラス.
*
* @author da_yamano
*
*/
public class A200002Param extends BaseParam {

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
     * 取得日時
     */
    private String settingDate;

    /**
     * 温湿度制御
     */
    private String ctrlTH;

    /**
     * 温湿度制御条件
     */
    private String ctrlTermsTH;

    /**
     * 冷暖房切替用4点端末器アドレス
     */
    private String terminalEquipaddressCW;

    /**
     *デマンド連動制御条件(超過)
     */
    private String demandGangCtrlTermsEx;

    /**
     * デマンド連動制御条件(注意)
     */
    private String demandGangCtrlTermsCa;

    /**
     * デマンド連動制御条件(遮断)
     */
    private String demandGangCtrlTermsBl;

    /**
     * デマンド連動制御条件(限界)
     */
    private String demandGangCtrlTermsLi;

    /**
     *デマンド連動制御条件(高負荷)
     */
    private String demandGangCtrlTermsHi;

    /**
     * 温湿度制御時間帯No.X
     */
    private List<Map<String, String>> ctrlTimeZoneTHList;

    /**
     * 制御ポートNo.Y設定
     */
    private List<Map<String, String>> settingCtrlPortList;

    /**
     * 現在温度情報
     */
    private List<CurrentTempretureExtractResultData> currentTemperatureList;

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getSettingChangeHist() {
        return settingChangeHist;
    }

    public void setSettingChangeHist(String settingChangeHist) {
        this.settingChangeHist = settingChangeHist;
    }

    public List<Map<String, String>> getCtrlTimeZoneTHList() {
        return ctrlTimeZoneTHList;
    }

    public void setCtrlTimeZoneTHList(List<Map<String, String>> ctrlTimeZoneTHList) {
        this.ctrlTimeZoneTHList = ctrlTimeZoneTHList;
    }

    public List<Map<String, String>> getSettingCtrlPortList() {
        return settingCtrlPortList;
    }

    public void setSettingCtrlPortList(List<Map<String, String>> settingCtrlPortList) {
        this.settingCtrlPortList = settingCtrlPortList;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getCtrlTH() {
        return ctrlTH;
    }

    public void setCtrlTH(String ctrlTH) {
        this.ctrlTH = ctrlTH;
    }

    public String getCtrlTermsTH() {
        return ctrlTermsTH;
    }

    public void setCtrlTermsTH(String ctrlTermsTH) {
        this.ctrlTermsTH = ctrlTermsTH;
    }

    public String getTerminalEquipaddressCW() {
        return terminalEquipaddressCW;
    }

    public void setTerminalEquipaddressCW(String terminalEquipaddressCW) {
        this.terminalEquipaddressCW = terminalEquipaddressCW;
    }

    public String getDemandGangCtrlTermsEx() {
        return demandGangCtrlTermsEx;
    }

    public void setDemandGangCtrlTermsEx(String demandGangCtrlTermsEx) {
        this.demandGangCtrlTermsEx = demandGangCtrlTermsEx;
    }

    public String getDemandGangCtrlTermsCa() {
        return demandGangCtrlTermsCa;
    }

    public void setDemandGangCtrlTermsCa(String demandGangCtrlTermsCa) {
        this.demandGangCtrlTermsCa = demandGangCtrlTermsCa;
    }

    public String getDemandGangCtrlTermsBl() {
        return demandGangCtrlTermsBl;
    }

    public void setDemandGangCtrlTermsBl(String demandGangCtrlTermsBl) {
        this.demandGangCtrlTermsBl = demandGangCtrlTermsBl;
    }

    public String getDemandGangCtrlTermsLi() {
        return demandGangCtrlTermsLi;
    }

    public void setDemandGangCtrlTermsLi(String demandGangCtrlTermsLi) {
        this.demandGangCtrlTermsLi = demandGangCtrlTermsLi;
    }

    public String getDemandGangCtrlTermsHi() {
        return demandGangCtrlTermsHi;
    }

    public void setDemandGangCtrlTermsHi(String demandGangCtrlTermsHi) {
        this.demandGangCtrlTermsHi = demandGangCtrlTermsHi;
    }

    public List<CurrentTempretureExtractResultData> getCurrentTemperatureList() {
        return currentTemperatureList;
    }

    public void setCurrentTemperatureList(List<CurrentTempretureExtractResultData> currentTemperatureList) {
        this.currentTemperatureList = currentTemperatureList;
    }

}
