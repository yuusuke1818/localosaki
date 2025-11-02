package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.CurrentTempretureExtractResultData;

/**
*
* 複数建物・テナント一括 温度制御(取得) Param クラス
*
* @author f_takemura
*/

public class A200041Param extends BaseParam {

    /**
     * 設定変更履歴
     */
    @Pattern(regexp = "[0-9]")
    private String settingChangeHist;

    /*
     ************************************
     * 温度制御で利用するパラメータ     *
     ************************************
     */
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

    /*
     ************************************
     * イベント制御で利用するパラメータ *
     ************************************
     */

    /**
     * 負荷??
     */
    private List<Map<String, Object>> loadList;

    /*
     ************************************
     * 共通で利用するパラメータ *
     ************************************
     */

    /**
     * 機器ID
     */
    private Long smId;

    /**
     * 建物ID
     */
    private Long buildingId;

    /**
     * 現在温度取得フラグ
     */
    private Integer getCurrentTemperatureFlg;

    /**
     * 現在温度情報
     */
    private List<CurrentTempretureExtractResultData> currentTemperatureList;

    /**
     * 連動設定情報
     */
    private List<Map<String, String>> linkSettingList;

    public Long getSmId() {
        return smId;
    }

    public void setSmId(Long smId) {
        this.smId = smId;
    }

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

    public List<Map<String, Object>> getLoadList() {
        return loadList;
    }

    public void setLoadList(List<Map<String, Object>> loadList) {
        this.loadList = loadList;
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

    public List<Map<String, String>> getLinkSettingList() {
        return linkSettingList;
    }

    public void setLinkSettingList(List<Map<String, String>> linkSettingList) {
        this.linkSettingList = linkSettingList;
    }

    public Integer getGetCurrentTemperatureFlg() {
        return getCurrentTemperatureFlg;
    }

    public void setGetCurrentTemperatureFlg(Integer getCurrentTemperatureFlg) {
        this.getCurrentTemperatureFlg = getCurrentTemperatureFlg;
    }

}
