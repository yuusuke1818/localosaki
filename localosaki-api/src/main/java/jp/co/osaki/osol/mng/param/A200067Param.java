package jp.co.osaki.osol.mng.param;

import java.util.Map;

import javax.validation.constraints.Pattern;

/**
 * AI設定(取得) param クラス
 * @author nishida.t
 *
 */
public class A200067Param extends BaseParam {

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
     * AielMaster接続
     */
    private String aielMasterConnection;

    /**
     * IPアドレス
     */
    private Map<String, Object> ipAddress;

    /**
     * 異常判定時間
     */
    private String alertTime;

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

    public String getAielMasterConnection() {
        return aielMasterConnection;
    }

    public void setAielMasterConnection(String aielMasterConnection) {
        this.aielMasterConnection = aielMasterConnection;
    }

    public Map<String, Object> getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(Map<String, Object> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }


}
